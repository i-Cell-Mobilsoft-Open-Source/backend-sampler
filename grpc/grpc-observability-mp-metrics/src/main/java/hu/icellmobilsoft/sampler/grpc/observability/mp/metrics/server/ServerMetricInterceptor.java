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
package hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.server;

import jakarta.enterprise.context.Dependent;

import hu.icellmobilsoft.sampler.grpc.core.extension.metric.api.IMetricInterceptor;
import hu.icellmobilsoft.sampler.grpc.core.extension.metric.api.ServerMetricInterceptorQualifier;
import hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.bundle.MetricBundle;
import hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.common.AbstractMetricInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

/**
 * gRPC server interceptor that will collect metrics using microprofile-metrics api
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
@ServerMetricInterceptorQualifier
@Dependent
public class ServerMetricInterceptor extends AbstractMetricInterceptor implements ServerInterceptor, IMetricInterceptor {

    private static final String METADATA_NAME_REQUEST = "grpc_server_requests_received_messages";
    private static final String METADATA_NAME_RESPONSE = "grpc_server_responses_sent_messages";
    private static final String METADATA_NAME_TIMER = "grpc_server_processing_duration_seconds";

    @Override
    public <R, S> ServerCall.Listener<R> interceptCall(ServerCall<R, S> call, Metadata requestMetadata, ServerCallHandler<R, S> next) {

        MetricBundle metricBundle = createMetricBundle(call.getMethodDescriptor());

        MetricServerCall<R, S> monitoringCall = new MetricServerCall<R, S>(call, metricBundle);

        return new MetricServerCallListener<R>(next.startCall(monitoringCall, requestMetadata), metricBundle, monitoringCall::getResponseCode);
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
