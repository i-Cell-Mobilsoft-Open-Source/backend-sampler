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

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.ClientOpentracingInterceptorQualifier;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.IOpentracingInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;

/**
 * Factory class for grpc producer template
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
@Alternative
@Priority(1)
@ApplicationScoped
public class ManagedChannelProducer extends hu.icellmobilsoft.coffee.grpc.client.extension.ManagedChannelProducer {

    @Inject
    private Logger log;

    @Override
    protected void configureChannelBuilder(ManagedChannelBuilder<?> channelBuilder) throws BaseException {
        super.configureChannelBuilder(channelBuilder);

        // observability interceptors if available
        Instance<IOpentracingInterceptor> instanceOpentracing = CDI.current().select(IOpentracingInterceptor.class,
                new ClientOpentracingInterceptorQualifier.Literal());
        if (instanceOpentracing.isResolvable()) {
            channelBuilder.intercept((ClientInterceptor) instanceOpentracing.get());
        } else {
            log.warn("Could not find Opentracing interceptor implementation for gRPC client.");
        }

    }
}
