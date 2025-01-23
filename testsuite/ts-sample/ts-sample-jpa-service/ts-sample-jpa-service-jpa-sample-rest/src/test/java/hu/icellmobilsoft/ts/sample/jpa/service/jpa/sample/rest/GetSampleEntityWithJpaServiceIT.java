/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2024 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.ts.sample.jpa.service.jpa.sample.rest;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import hu.icellmobilsoft.coffee.dto.common.commonservice.FunctionCodeType;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.roaster.api.TestSuiteGroup;
import hu.icellmobilsoft.roaster.jaxrs.response.producer.RestProcessor;
import hu.icellmobilsoft.roaster.restassured.BaseConfigurableWeldIT;
import hu.icellmobilsoft.roaster.restassured.response.producer.impl.ConfigurableResponseProcessor;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;

/**
 * IT tests for {@link hu.icellmobilsoft.sampler.api.jee.rest.ISampleRest#getSample()}
 *
 * @author gyengus
 * @since 2.0.0
 */
@Tag(TestSuiteGroup.RESTASSURED)
public class GetSampleEntityWithJpaServiceIT extends BaseConfigurableWeldIT {

    public static final String REST_CONFIG_KEY = "testsuite.rest.sampleService.sample"; //"sampler.service.sample.base.uri";

    @Inject
    @RestProcessor(configKey = REST_CONFIG_KEY)
    private ConfigurableResponseProcessor<SampleResponse> responseProcessor;

    @Test
    void getSampleEntityTest() throws BaseException {
        // GIVEN

        // WHEN
        SampleResponse response = getResponse();
        SampleType actual = response.getSample();

        // THEN
        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode(), "functionCode");
        Assertions.assertAll(
                () -> Assertions.assertEquals(SampleValueEnumType.VALUE_A, actual.getColumnB()),
                () -> Assertions.assertEquals(SampleStatusEnumType.DONE, actual.getSampleStatus())
        );
    }

    private SampleResponse getResponse() throws BaseException {
        return responseProcessor.getJson(SampleResponse.class);
    }
}
