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

import java.net.URI;

import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import hu.icellmobilsoft.coffee.dto.common.commonservice.FunctionCodeType;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.roaster.api.TestSuiteGroup;
import hu.icellmobilsoft.roaster.restassured.BaseConfigurableWeldIT;
import hu.icellmobilsoft.sampler.api.jee.rest.ISampleRest;
import hu.icellmobilsoft.sampler.api.jee.rest.client.ISampleRestJsonClient;
import hu.icellmobilsoft.sampler.api.jee.rest.client.ISampleRestXmlClient;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.ts.sample.rest.builder.SampleRequestBuilder;

/**
 * Sample service {@link ISampleRest#postSample(hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest)} test
 *
 * @author Imre Scheffer
 * @since 2.0.0
 */
@DisplayName("Testing Sample service GET with JAX-RS")
@Tag(TestSuiteGroup.JAXRS)
class GetSampleJaxrsIT extends BaseConfigurableWeldIT {

    private static final String REST_CONFIG_KEY = "sampler.service.sample.base.uri";

    @Inject
    private SampleRequestBuilder requestBuilder;

    @Inject
    @ConfigProperty(name = REST_CONFIG_KEY)
    private String baseUri;

    @Test
    @DisplayName("GET default request JSON")
    void testDefaultGetJson() throws BaseException {

        ISampleRestJsonClient apiImpl = RestClientBuilder.newBuilder()
                // set URI
                .baseUri(URI.create(baseUri))
                // build API interface
                .build(ISampleRestJsonClient.class);
        SampleResponse response = apiImpl.getSample();
        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
    }

    @Test
    @DisplayName("GET default request XML")
    void testDefaultGetXml() throws BaseException {

        ISampleRestXmlClient apiImpl = RestClientBuilder.newBuilder()
                // set URI
                .baseUri(URI.create(baseUri))
                // build API interface
                .build(ISampleRestXmlClient.class);
        SampleResponse response = apiImpl.getSample();
        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode());
    }
}
