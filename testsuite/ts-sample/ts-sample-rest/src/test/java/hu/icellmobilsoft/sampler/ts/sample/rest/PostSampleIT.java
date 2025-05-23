/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 i-Cell Mobilsoft Zrt.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hu.icellmobilsoft.sampler.ts.sample.rest;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import hu.icellmobilsoft.coffee.dto.common.commonservice.FunctionCodeType;
import hu.icellmobilsoft.coffee.dto.common.commonservice.InvalidRequestFault;
import hu.icellmobilsoft.roaster.api.TestSuiteGroup;
import hu.icellmobilsoft.roaster.jaxrs.response.producer.RestProcessor;
import hu.icellmobilsoft.roaster.restassured.BaseConfigurableWeldIT;
import hu.icellmobilsoft.roaster.restassured.response.producer.impl.ConfigurableResponseProcessor;
import hu.icellmobilsoft.sampler.api.jee.rest.ISampleRest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.ts.sample.rest.builder.SampleRequestBuilder;

/**
 * Sample service {@link ISampleRest#postSample(hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest)} test
 *
 * @author Imre Scheffer
 * @since 0.1.0
 */
@DisplayName("Testing Sample service post")
@Tag(TestSuiteGroup.RESTASSURED)
class PostSampleIT extends BaseConfigurableWeldIT {

    private static final String REST_CONFIG_KEY = "testsuite.rest.sampleService.sample";

    @Inject
    private SampleRequestBuilder requestBuilder;

    @Inject
    @RestProcessor(configKey = REST_CONFIG_KEY)
    private ConfigurableResponseProcessor<SampleResponse> responseProcessor;

    @Inject
    @RestProcessor(configKey = REST_CONFIG_KEY, expectedStatusCode = 400)
    private ConfigurableResponseProcessor<InvalidRequestFault> responseProcessorError;

    @Nested
    @DisplayName("Testing JSON request")
    class Json {

        @Test
        @DisplayName("Testing JSON: empty request")
        void testEmpty_json() {
            // given

            // when
            SampleResponse response = responseProcessor.postJson(requestBuilder.getDefault(), SampleResponse.class);

            // then
            Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing JSON: valid dto success")
        void testJsonValidDto() {
            // given
            SampleRequest request = requestBuilder.getDefault();

            // when
            SampleResponse response = responseProcessor.postJson(request, SampleResponse.class);

            // then
            Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing JSON: valid json string success")
        void testJsonValidString() {
            // given
            String request = "{\n" +
                    "                        \"context\": {\n" +
                    "                            \"requestId\": \"4EEMHK5QH4FVX601\",\n" +
                    "                            \"timestamp\": \"2023-12-05T22:11:46.382Z\"\n" +
                    "                        },\n" +
                    "                            \"sample\": {\n" +
                    "                            \"columnA\": \"colA\",\n" +
                    "                            \"columnB\": \"VALUE_B\",\n" +
                    "                            \"columnC\": \"VALUE_C\"\n" +
                    "                        }\n" +
                    "                    }";

            // when
            SampleResponse response = responseProcessor.postJson(request, SampleResponse.class);

            // then
            Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing JSON: invalid json string with UNKNOWN field should throw validation errors")
        void testJsonUnknownField() {
            // given
            String request = "{\n" +
                    "                        \"context\": {\n" +
                    "                            \"requestId\": \"4EEMHK5QH4FVX601\",\n" +
                    "                            \"timestamp\": \"2023-12-05T22:11:46.382Z\"\n" +
                    "                        },\n" +
                    "                            \"sample\": {\n" +
                    "                            \"columnA\": \"colA\",\n" +
                    "                            \"columnB\": \"VALUE_B\",\n" +
                    "                            \"columnC\": \"VALUE_C\",\n" +
                    "                            \"valamiIsmeretlen\": \"VALUE_C\"\n" +
                    "                        }\n" +
                    "                    }";

            // when
            var response = responseProcessorError.postJson(request, InvalidRequestFault.class);

            // then
            Assertions.assertEquals(FunctionCodeType.ERROR, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing JSON: invalid json string with UNKNOWN ENUM value should throw validation error")
        void testJsonUnknownEnumValue() {
            // given
            String request = "{\n" +
                    "                        \"context\": {\n" +
                    "                            \"requestId\": \"4EEMHK5QH4FVX601\",\n" +
                    "                            \"timestamp\": \"2023-12-05T22:11:46.382Z\"\n" +
                    "                        },\n" +
                    "                            \"sample\": {\n" +
                    "                            \"columnA\": \"colA\",\n" +
                    "                            \"columnB\": \"valamiIsmeretlen\",\n" +
                    "                            \"columnC\": \"VALUE_C\"\n" +
                    "                        }\n" +
                    "                    }";

            // when
            var response = responseProcessorError.postJson(request, InvalidRequestFault.class);

            // then
            Assertions.assertEquals(FunctionCodeType.ERROR, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing JSON: invalid json string with UNKNOWN optional ENUM value should throw validation error")
        void testJsonUnknownOptionalEnumValue() {
            // given
            String request = "{\n" +
                    "                        \"context\": {\n" +
                    "                            \"requestId\": \"4EEMHK5QH4FVX601\",\n" +
                    "                            \"timestamp\": \"2023-12-05T22:11:46.382Z\"\n" +
                    "                        },\n" +
                    "                            \"sample\": {\n" +
                    "                            \"columnA\": \"colA\",\n" +
                    "                            \"columnB\": \"VALUE_B\",\n" +
                    "                            \"columnC\": \"valamiIsmeretlen\"\n" +
                    "                        }\n" +
                    "                    }";

            // when
            var response = responseProcessorError.postJson(request, InvalidRequestFault.class);

            // then
            Assertions.assertEquals(FunctionCodeType.ERROR, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing JSON: invalid json string using ENUM ordinal value should throw validation error")
        void testJsonEnumOrdinalValue() {
            // given
            String request = "{\n" +
                    "                        \"context\": {\n" +
                    "                            \"requestId\": \"4EEMHK5QH4FVX601\",\n" +
                    "                            \"timestamp\": \"2023-12-05T22:11:46.382Z\"\n" +
                    "                        },\n" +
                    "                            \"sample\": {\n" +
                    "                            \"columnA\": \"colA\",\n" +
                    "                            \"columnB\": \"0\",\n" +
                    "                            \"columnC\": \"1\"\n" +
                    "                        }\n" +
                    "                    }";

            // when
            var response = responseProcessorError.postJson(request, InvalidRequestFault.class);

            // then
            Assertions.assertEquals(FunctionCodeType.ERROR, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing JSON: invalid json string using wrong TIME format should throw validation error")
        void testJsonWrongTimeFormat() {
            // given
            String request = "{\n" +
                    "                        \"context\": {\n" +
                    "                            \"requestId\": \"4EEMHK5QH4FVX601\",\n" +
                    "                            \"timestamp\": \"2022-03-10T12:15\"\n" +
                    "                        },\n" +
                    "                            \"sample\": {\n" +
                    "                            \"columnA\": \"colA\",\n" +
                    "                            \"columnB\": \"VALUE_B\",\n" +
                    "                            \"columnC\": \"VALUE_C\"\n" +
                    "                        }\n" +
                    "                    }";

            // when
            var response = responseProcessorError.postJson(request, InvalidRequestFault.class);

            // then
            Assertions.assertEquals(FunctionCodeType.ERROR, response.getFuncCode());
        }
    }

    @Nested
    @DisplayName("Testing XML request")
    class Xml {

        @Test
        @DisplayName("Testing XML: empty request")
        void testEmpty_xml() {
            // given

            // when
            SampleResponse response = responseProcessor.postXml(requestBuilder.getDefault(), SampleResponse.class);

            // then
            Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing XML: valid dto success")
        void testXmlValidDto() {
            // given
            SampleRequest request = requestBuilder.getDefault();

            // when
            var response = responseProcessor.postXml(request, SampleResponse.class);

            // then
            Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing XML: valid xml string success")
        void testXmlValidString() {
            // given
            String request = "<ns2:SampleRequest xmlns=\"http://common.dto.coffee.icellmobilsoft.hu/commonservice\" xmlns:ns2=\"http://dto.sampler.icellmobilsoft.hu/sample/rest/post\">\n" +
                    "                      <context>\n" +
                    "                        <requestId>4EEMW6I0QS7JKQ01</requestId>\n" +
                    "                        <timestamp>2023-12-05T22:23:08.52Z</timestamp>\n" +
                    "                      </context>\n" +
                    "                      <ns2:sample>\n" +
                    "                        <ns2:columnA>colA</ns2:columnA>\n" +
                    "                        <ns2:columnB>VALUE_B</ns2:columnB>\n" +
                    "                        <ns2:columnC>VALUE_C</ns2:columnC>\n" +
                    "                      </ns2:sample>\n" +
                    "                    </ns2:SampleRequest>";

            // when
            var response = responseProcessor.postXml(request, SampleResponse.class);

            // then
            Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing XML: invalid xml string with UNKNOWN field should throw validation error")
        void testXmlUnknownField() {
            // given
            String request = "<ns2:SampleRequest xmlns=\"http://common.dto.coffee.icellmobilsoft.hu/commonservice\" xmlns:ns2=\"http://dto.sampler.icellmobilsoft.hu/sample/rest/post\">\n" +
                    "                      <context>\n" +
                    "                        <requestId>4EEMW6I0QS7JKQ01</requestId>\n" +
                    "                        <timestamp>2023-12-05T22:23:08.52Z</timestamp>\n" +
                    "                      </context>\n" +
                    "                      <ns2:sample>\n" +
                    "                        <ns2:columnA>colA</ns2:columnA>\n" +
                    "                        <ns2:columnB>VALUE_B</ns2:columnB>\n" +
                    "                        <ns2:columnC>VALUE_C</ns2:columnC>\n" +
                    "                        <ns2:valamiIsmeretlen>VALUE_C</ns2:valamiIsmeretlen>\n" +
                    "                      </ns2:sample>\n" +
                    "                    </ns2:SampleRequest>";

            // when
            var response = responseProcessorError.postXml(request, InvalidRequestFault.class);

            // then
            Assertions.assertEquals(FunctionCodeType.ERROR, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing XML: invalid xml string with UNKNOWN ENUM value should throw validation error")
        void testXmlUnknownEnumValue() {
            // given
            String request = "<ns2:SampleRequest xmlns=\"http://common.dto.coffee.icellmobilsoft.hu/commonservice\" xmlns:ns2=\"http://dto.sampler.icellmobilsoft.hu/sample/rest/post\">\n" +
                    "                      <context>\n" +
                    "                        <requestId>4EEMW6I0QS7JKQ01</requestId>\n" +
                    "                        <timestamp>2023-12-05T22:23:08.52Z</timestamp>\n" +
                    "                      </context>\n" +
                    "                      <ns2:sample>\n" +
                    "                        <ns2:columnA>colA</ns2:columnA>\n" +
                    "                        <ns2:columnB>valamiIsmeretlen</ns2:columnB>\n" +
                    "                        <ns2:columnC>VALUE_C</ns2:columnC>\n" +
                    "                      </ns2:sample>\n" +
                    "                    </ns2:SampleRequest>";

            // when
            var response = responseProcessorError.postXml(request, InvalidRequestFault.class);

            // then
            Assertions.assertEquals(FunctionCodeType.ERROR, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing XML: invalid xml string with UNKNOWN optional ENUM value should throw validation error")
        void testXmlUnknownOptionalEnumValue() {
            // given
            String request = "<ns2:SampleRequest xmlns=\"http://common.dto.coffee.icellmobilsoft.hu/commonservice\" xmlns:ns2=\"http://dto.sampler.icellmobilsoft.hu/sample/rest/post\">\n" +
                    "                      <context>\n" +
                    "                        <requestId>4EEMW6I0QS7JKQ01</requestId>\n" +
                    "                        <timestamp>2023-12-05T22:23:08.52Z</timestamp>\n" +
                    "                      </context>\n" +
                    "                      <ns2:sample>\n" +
                    "                        <ns2:columnA>colA</ns2:columnA>\n" +
                    "                        <ns2:columnB>VALUE_B</ns2:columnB>\n" +
                    "                        <ns2:columnC>valamiIsmeretlen</ns2:columnC>\n" +
                    "                      </ns2:sample>\n" +
                    "                    </ns2:SampleRequest>";

            // when
            var response = responseProcessorError.postXml(request, InvalidRequestFault.class);

            // then
            Assertions.assertEquals(FunctionCodeType.ERROR, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing XML: invalid xml string using ENUM ordinal value should throw validation error")
        void testXmlEnumOrdinalValue() {
            // given
            String request = "<ns2:SampleRequest xmlns=\"http://common.dto.coffee.icellmobilsoft.hu/commonservice\" xmlns:ns2=\"http://dto.sampler.icellmobilsoft.hu/sample/rest/post\">\n" +
                    "                      <context>\n" +
                    "                        <requestId>4EEMW6I0QS7JKQ01</requestId>\n" +
                    "                        <timestamp>2023-12-05T22:23:08.52Z</timestamp>\n" +
                    "                      </context>\n" +
                    "                      <ns2:sample>\n" +
                    "                        <ns2:columnA>colA</ns2:columnA>\n" +
                    "                        <ns2:columnB>1</ns2:columnB>\n" +
                    "                        <ns2:columnC>2</ns2:columnC>\n" +
                    "                      </ns2:sample>\n" +
                    "                    </ns2:SampleRequest>";

            // when
            var response = responseProcessorError.postXml(request, InvalidRequestFault.class);

            // then
            Assertions.assertEquals(FunctionCodeType.ERROR, response.getFuncCode());
        }

        @Test
        @DisplayName("Testing XML: invalid xml string using wrong TIME format should throw validation error")
        void testXmlWrongTimeFormat() {
            // given
            String request = "<ns2:SampleRequest xmlns=\"http://common.dto.coffee.icellmobilsoft.hu/commonservice\" xmlns:ns2=\"http://dto.sampler.icellmobilsoft.hu/sample/rest/post\">\n" +
                    "                      <context>\n" +
                    "                        <requestId>4EEMW6I0QS7JKQ01</requestId>\n" +
                    "                        <timestamp>2022-03-10T12:15</timestamp>\n" +
                    "                      </context>\n" +
                    "                      <ns2:sample>\n" +
                    "                        <ns2:columnA>colA</ns2:columnA>\n" +
                    "                        <ns2:columnB>VALUE_B</ns2:columnB>\n" +
                    "                        <ns2:columnC>VALUE_C</ns2:columnC>\n" +
                    "                      </ns2:sample>\n" +
                    "                    </ns2:SampleRequest>";

            // when
            var response = responseProcessorError.postXml(request, InvalidRequestFault.class);

            // then
            Assertions.assertEquals(FunctionCodeType.ERROR, response.getFuncCode());
        }

    }
}
