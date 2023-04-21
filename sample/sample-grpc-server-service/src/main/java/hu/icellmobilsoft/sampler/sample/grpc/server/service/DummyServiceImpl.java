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
package hu.icellmobilsoft.sampler.sample.grpc.server.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyRequest;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyResponse;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyService;
import hu.icellmobilsoft.sampler.sample.grpc.server.service.action.SampleGrpcAction;
import hu.icellmobilsoft.sampler.sample.grpc.server.service.action.SampleGrpcRequestScopeAction;
import io.grpc.stub.StreamObserver;

/**
 * gRPC service call to CDI bean
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
@ApplicationScoped
public class DummyServiceImpl implements DummyService {

    @Inject
    private SampleGrpcAction sampleGrpcAction;

    @Inject
    private SampleGrpcRequestScopeAction sampleGrpcRequestScopeAction;

    @Override
    public void getDummy(DummyRequest request, StreamObserver<DummyResponse> responseObserver) {
        // delegate to cdi bean
        sampleGrpcAction.call(request, responseObserver);

    }

    @Override
    @ActivateRequestContext
    public void getDummyRequestScope(DummyRequest request, StreamObserver<DummyResponse> responseObserver) {
        // delegate to cdi bean
        sampleGrpcRequestScopeAction.call(request, responseObserver);
    }

}
