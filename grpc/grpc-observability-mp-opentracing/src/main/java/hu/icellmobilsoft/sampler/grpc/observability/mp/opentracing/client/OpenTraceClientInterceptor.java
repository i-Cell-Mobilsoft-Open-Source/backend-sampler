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
package hu.icellmobilsoft.sampler.grpc.observability.mp.opentracing.client;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.spi.CDI;

import hu.icellmobilsoft.coffee.module.mp.opentracing.extension.OpenTraceResolver;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.ClientOpentracingInterceptorQualifier;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.IOpentracingInterceptor;
import hu.icellmobilsoft.sampler.grpc.observability.mp.opentracing.common.AbstractOpenTraceInterceptor;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.MethodDescriptor;
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
@ClientOpentracingInterceptorQualifier
@Dependent
public class OpenTraceClientInterceptor extends AbstractOpenTraceInterceptor implements ClientInterceptor, IOpentracingInterceptor {

    private static final String TAG_CLIENT = "client";

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        OpenTraceResolver openTraceResolver = CDI.current().select(OpenTraceResolver.class).get();
        Tracer tracer = openTraceResolver.resolveTracer();

        SpanBuilder spanBuilder = createSpanBuilder(method, tracer);

        // start trace
        Span span = spanBuilder.start();
        Scope scope = tracer.activateSpan(span);
        ClientCall<ReqT, RespT> clientCall = next.newCall(method, callOptions);
        return new OpenTraceClientCall<ReqT, RespT>(clientCall, span, scope);
    }

    @Override
    protected String getSpanKind() {
        return TAG_CLIENT;
    }

}
