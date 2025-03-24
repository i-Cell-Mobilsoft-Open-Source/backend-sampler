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

import java.util.Map;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import hu.icellmobilsoft.coffee.dto.common.commonservice.FunctionCodeType;
import hu.icellmobilsoft.roaster.api.TestSuiteGroup;
import hu.icellmobilsoft.roaster.jaxrs.response.producer.RestProcessor;
import hu.icellmobilsoft.roaster.restassured.BaseConfigurableWeldIT;
import hu.icellmobilsoft.roaster.restassured.response.producer.impl.ConfigurableResponseProcessor;
import hu.icellmobilsoft.sampler.api.jee.rest.ISampleRest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;

/**
 * Sample service {@link ISampleRest#getSample()} test
 *
 * @author Imre Scheffer
 * @since 0.1.0
 */
@DisplayName("Testing Sample service get")
@Tag(TestSuiteGroup.RESTASSURED)
class GetSampleIT extends BaseConfigurableWeldIT {

    private static final String REST_CONFIG_KEY = "testsuite.rest.sampleService.sample";

    @Inject
    @RestProcessor(configKey = REST_CONFIG_KEY)
    private ConfigurableResponseProcessor<SampleResponse> responseProcessor;

    @Test
    @DisplayName("test get - json")
    void testGet_json() {

        SampleResponse response = responseProcessor.getJson(SampleResponse.class);

        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
    }

    @Test
    @DisplayName("test get - xml")
    void testGet_xml() {

        SampleResponse response = responseProcessor.getXml(SampleResponse.class);

        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
    }

    @Test
    @DisplayName("test get - json - locale - hu")
    void testGet_json_localeHu() {

        responseProcessor.setQueryParams(Map.of("lang","hu"));
        SampleResponse response = responseProcessor.getJson(SampleResponse.class);

        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
    }

    @Test
    @DisplayName("test get - json - locale - en")
    void testGet_json_localeEn() {

        responseProcessor.setQueryParams(Map.of("lang","en"));
        SampleResponse response = responseProcessor.getJson(SampleResponse.class);

        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
    }

    @Test
    @DisplayName("test get - json - locale - de")
    void testGet_json_localeDe() {

        responseProcessor.setQueryParams(Map.of("lang","de"));
        SampleResponse response = responseProcessor.getJson(SampleResponse.class);

        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
    }
}
