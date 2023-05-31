/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2023 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.grpc.server;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.grpc.core.extension.metric.api.IMetricInterceptor;
import hu.icellmobilsoft.sampler.grpc.core.extension.metric.api.ServerMetricInterceptorQualifier;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.IOpentracingInterceptor;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.ServerOpentracingInterceptorQualifier;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;

/**
 * Sample gRPC server manager
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
@Alternative
@Priority(1)
@ApplicationScoped
public class GrpcServerManager extends hu.icellmobilsoft.coffee.grpc.server.GrpcServerManager {

    @Inject
    private Logger log;

    @Inject
    private BeanManager beanManager;

    /**
     * request/response minta interceptor, ezzel ovatosan kell banni, mert fura módon fordított sorrendben hivodik a hívási lanc, és többször is
     * befuthat ide egy folyamatban
     * 
     * @param serverBuilder
     *            szerver builder
     */
    @Override
    protected void addInterceptor(ServerBuilder<?> serverBuilder) {
        super.addInterceptor(serverBuilder);
        // modul szintu dependency, mp-metric, micrometer, telemetry...
        Instance<IMetricInterceptor> instanceMetric = CDI.current().select(IMetricInterceptor.class, new ServerMetricInterceptorQualifier.Literal());
        if (instanceMetric.isResolvable()) {
            serverBuilder.intercept((ServerInterceptor) instanceMetric.get()); // 2
        } else {
            log.warn("Could not find Metric interceptor implementation for gRPC server.");
        }
        Instance<IOpentracingInterceptor> instanceOpentrace = CDI.current().select(IOpentracingInterceptor.class,
                new ServerOpentracingInterceptorQualifier.Literal());
        if (instanceOpentrace.isResolvable()) {
            serverBuilder.intercept((ServerInterceptor) instanceOpentrace.get()); // 1
        } else {
            log.warn("Could not find Opentracing interceptor implementation for gRPC server.");
        }
    }
}
