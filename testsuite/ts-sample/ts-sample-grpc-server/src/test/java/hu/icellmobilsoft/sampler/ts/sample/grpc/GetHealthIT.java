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
package hu.icellmobilsoft.sampler.ts.sample.grpc;

import java.util.Map;
import java.util.stream.Collectors;

import jakarta.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import hu.icellmobilsoft.roaster.api.TestSuiteGroup;
import hu.icellmobilsoft.roaster.jaxrs.response.producer.RestProcessor;
import hu.icellmobilsoft.roaster.restassured.BaseConfigurableWeldIT;
import hu.icellmobilsoft.roaster.restassured.response.producer.impl.ConfigurableResponseProcessor;
import hu.icellmobilsoft.sampler.ts.sample.grpc.dto.HealthCheck;
import hu.icellmobilsoft.sampler.ts.sample.grpc.dto.HealthResponse;

/**
 * Health service test
 *
 * @author karoly.tammas
 * @since 2.0.0
 */
@DisplayName("Testing health service get")
@Tag(TestSuiteGroup.RESTASSURED)
class GetHealthIT extends BaseConfigurableWeldIT {

    public static final String GRPC_HEALTH_NAME = "gRPC";

    private static final String REST_CONFIG_KEY = "testsuite.rest.sampleService.management.health";

    @Inject
    @RestProcessor(configKey = REST_CONFIG_KEY)
    private ConfigurableResponseProcessor<HealthResponse> responseProcessor;

    @Test
    @DisplayName("test get - json")
    void testGet_json() {

        HealthResponse response = responseProcessor.getJson(HealthResponse.class);

        Assertions.assertEquals(HealthCheckResponse.Status.UP, response.getStatus());
        Assertions.assertTrue(CollectionUtils.isNotEmpty(response.getChecks()));

        Map<String, HealthCheckResponse.Status> checkNameAndStatusMap = response.getChecks()
                .stream()
                .collect(Collectors.toMap(HealthCheck::getName, HealthCheck::getStatus));

        Assertions.assertTrue(checkNameAndStatusMap.containsKey(GRPC_HEALTH_NAME));
        Assertions.assertEquals(HealthCheckResponse.Status.UP, checkNameAndStatusMap.get(GRPC_HEALTH_NAME));

    }

}
