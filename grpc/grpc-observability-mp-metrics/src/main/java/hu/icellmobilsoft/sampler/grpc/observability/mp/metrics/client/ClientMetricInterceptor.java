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
package hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.client;

import jakarta.enterprise.context.Dependent;

import hu.icellmobilsoft.sampler.grpc.core.extension.metric.api.ClientMetricInterceptorQualifier;
import hu.icellmobilsoft.sampler.grpc.core.extension.metric.api.IMetricInterceptor;
import hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.bundle.MetricBundle;
import hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.common.AbstractMetricInterceptor;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.MethodDescriptor;

/**
 * gRPC server interceptor that will collect metrics using microprofile-metrics api
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
@ClientMetricInterceptorQualifier
@Dependent
public class ClientMetricInterceptor extends AbstractMetricInterceptor implements ClientInterceptor, IMetricInterceptor {

    private static final String METADATA_NAME_REQUEST = "grpc_client_requests_sent_messages";
    private static final String METADATA_NAME_RESPONSE = "grpc_client_responses_received_messages";
    private static final String METADATA_NAME_TIMER = "grpc_client_processing_duration_seconds";

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        MetricBundle metricBundle = createMetricBundle(method);

        return new MetricClientCall<>(next.newCall(method, callOptions), metricBundle);
    }

    @Override
    protected String getRequestMetadataName() {
        return METADATA_NAME_REQUEST;
    }

    @Override
    protected String getResponseMetadataName() {
        return METADATA_NAME_RESPONSE;
    }

    @Override
    protected String getTimerMetadataName() {
        return METADATA_NAME_TIMER;
    }

}
