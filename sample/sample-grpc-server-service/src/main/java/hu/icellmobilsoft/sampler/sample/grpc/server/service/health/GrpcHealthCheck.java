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
package hu.icellmobilsoft.sampler.sample.grpc.server.service.health;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import hu.icellmobilsoft.coffee.grpc.server.health.GrpcHealth;
import hu.icellmobilsoft.coffee.se.logging.Logger;

/**
 * Health check for gRPC server availability
 *
 * @author karoly.tamas
 * @since 2.0.0
 */
@ApplicationScoped
public class GrpcHealthCheck {

    @Inject
    private Logger logger;

    @Inject
    private GrpcHealth grpcHealth;

    private String builderName;

    /**
     * Init the health builderName
     */
    @PostConstruct
    public void initHealthConfig() {
        builderName = "gRPC";
    }

    /**
     * Checking gRPC server
     *
     * @return The created HealthCheckResponse contains information about whether the gRPC server is reachable.
     */
    public HealthCheckResponse checkGrpc() {
        try {
            return grpcHealth.check(builderName);
        } catch (Throwable e) {
            // we catch every exception and error so that the probe doesn't encounter any unhandled errors or exceptions
            logger.error("Error occured while checking gRPC server.", e);
            return HealthCheckResponse.builder().name(builderName).up().build();
        }
    }

    /**
     * Adding to health/ready endpoint
     *
     * @return health values
     */
    @Produces
    @Readiness
    public HealthCheck produceGrpcStartup() {
        return this::checkGrpc;
    }

}
