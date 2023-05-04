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
package hu.icellmobilsoft.sampler.grpc.client.extension;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.Config;

import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.utils.annotation.AnnotationUtil;
import hu.icellmobilsoft.sampler.grpc.client.GrpcClient;
import hu.icellmobilsoft.sampler.grpc.client.interceptor.ClientRequestInterceptor;
import hu.icellmobilsoft.sampler.grpc.client.interceptor.ClientResponseInterceptor;
import hu.icellmobilsoft.sampler.grpc.core.extension.metric.api.ClientMetricInterceptorQualifier;
import hu.icellmobilsoft.sampler.grpc.core.extension.metric.api.IMetricInterceptor;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.ClientOpentracingInterceptorQualifier;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.IOpentracingInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Factory class for grpc producer template
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
@ApplicationScoped
public class ManagedChannelProducer {

    @Inject
    private Logger log;

    @Inject
    private Config config;

    private Map<String, ManagedChannel> managedChannelInstances = new HashMap<>();

    /**
     * produce ManagedChannel
     * 
     * @param injectionPoint
     *            the injection point
     * @return ManagedChannel
     */
    @Produces
    @Dependent
    @GrpcClient(configKey = "")
    public ManagedChannel produceManagedChannel(InjectionPoint injectionPoint) {
        Optional<GrpcClient> annotation = AnnotationUtil.getAnnotation(injectionPoint, GrpcClient.class);
        String configKey = annotation.map(GrpcClient::configKey).orElse(null);

        return getInstance(configKey);
    }

    private synchronized ManagedChannel getInstance(String configKey) {
        return managedChannelInstances.computeIfAbsent(configKey, v -> createManagedChannel(configKey));
    }

    private ManagedChannel createManagedChannel(String configKey) {
        log.info("Creating ManagedChannel for configKey:[{0}]", configKey);
        try {

            String host = config.getOptionalValue("coffee.grpc.client." + configKey + ".host", String.class)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Missing configuration property by `configKey` " + configKey + ", must be set with `host` parameter"));

            int port = config.getOptionalValue("coffee.grpc.client." + configKey + ".port", Integer.class)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Missing configuration property by `configKey` " + configKey + ", must be set with `port` parameter"));

            // TODO usePlaintext config
            ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(host, port).usePlaintext();

            // metric interceptor configuration
            configureInterceptor(channelBuilder);

            return channelBuilder.build();
        } catch (Exception e) {
            log.error(MessageFormat.format("Exception on initializing ManagedChannel for configKey [{0}], [{1}]", configKey, e.getLocalizedMessage()),
                    e);
            throw new IllegalStateException(e);
        }
    }

    private void configureInterceptor(ManagedChannelBuilder<?> channelBuilder) {
        // request/response interceptor
        channelBuilder.intercept(new ClientRequestInterceptor());
        channelBuilder.intercept(new ClientResponseInterceptor());
        
        // observability interceptors if available
        Instance<IMetricInterceptor> instanceMetric = CDI.current().select(IMetricInterceptor.class, new ClientMetricInterceptorQualifier.Literal());
        if (instanceMetric.isResolvable()) {
            channelBuilder.intercept((ClientInterceptor) instanceMetric.get());
        } else {
            log.warn("Could not find Metric interceptor implementation for gRPC client.");
        }
        Instance<IOpentracingInterceptor> instanceOpentracing = CDI.current().select(IOpentracingInterceptor.class,
                new ClientOpentracingInterceptorQualifier.Literal());
        if (instanceOpentracing.isResolvable()) {
            channelBuilder.intercept((ClientInterceptor) instanceOpentracing.get());
        } else {
            log.warn("Could not find Opentracing interceptor implementation for gRPC client.");
        }

    }
}
