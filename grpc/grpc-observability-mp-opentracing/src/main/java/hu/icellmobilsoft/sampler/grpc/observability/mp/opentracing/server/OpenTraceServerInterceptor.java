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
package hu.icellmobilsoft.sampler.grpc.observability.mp.opentracing.server;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.spi.CDI;

import hu.icellmobilsoft.coffee.module.mp.opentracing.extension.OpenTraceResolver;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.IOpentracingInterceptor;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.ServerOpentracingInterceptorQualifier;
import hu.icellmobilsoft.sampler.grpc.observability.mp.opentracing.common.AbstractOpenTraceInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.Tracer.SpanBuilder;

/**
 * gRPC server interceptor that handle tracing data collection
 * 
 * @author czenczl
 * @since 2.0.0
 */
@ServerOpentracingInterceptorQualifier
@Dependent
public class OpenTraceServerInterceptor extends AbstractOpenTraceInterceptor implements ServerInterceptor, IOpentracingInterceptor {

    private static final String TAG_SERVER = "server";

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata requestMetadata, ServerCallHandler<ReqT, RespT> next) {

        OpenTraceResolver openTraceResolver = CDI.current().select(OpenTraceResolver.class).get();
        Tracer tracer = openTraceResolver.resolveTracer();

        SpanBuilder spanBuilder = createSpanBuilder(call.getMethodDescriptor(), tracer);

        // not use span if one currently active
        spanBuilder.ignoreActiveSpan();

        // start trace
        Span span = spanBuilder.start();
        Scope scope = tracer.activateSpan(span);
        OpenTraceServerCall<ReqT, RespT> tracingServerCall = new OpenTraceServerCall<>(call, span, scope);
        return new OpenTraceServerCallListener<ReqT>(next.startCall(tracingServerCall, requestMetadata));

    }

    @Override
    protected String getSpanKind() {
        return TAG_SERVER;
    }

}
