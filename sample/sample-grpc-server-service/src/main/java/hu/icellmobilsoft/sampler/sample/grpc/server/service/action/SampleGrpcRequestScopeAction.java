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
package hu.icellmobilsoft.sampler.sample.grpc.server.service.action;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.se.util.string.RandomUtil;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.BaseMessage;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyRequest;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyResponse;
import io.grpc.stub.StreamObserver;

/**
 * CDI request scope action bean
 * 
 * @author mark.petrenyi
 * @since 2.0.0
 *
 */
@Model
public class SampleGrpcRequestScopeAction {

    @Inject
    private Logger log;

    private String requestCache;

    /**
     * init requestCache 
     */
    @PostConstruct
    public void init() {
        requestCache = RandomUtil.generateId();
    }

    /**
     * dummy call
     * 
     * @param request
     *            {@link DummyRequest}
     * @param responseObserver
     *            {@link StreamObserver<DummyResponse>}
     */
    public void call(DummyRequest request, StreamObserver<DummyResponse> responseObserver) {
        log.info("requestCache is:[{0}]", requestCache);
        DummyResponse response = toResponse(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    private DummyResponse toResponse(DummyRequest request) {
        BaseMessage baseMessage = request.getRequest();

        DummyResponse.Builder builder = DummyResponse.newBuilder();

        BaseMessage.Builder baseMessageBuilder = BaseMessage.newBuilder();
        baseMessageBuilder.setAmount(baseMessage.getAmount());
        baseMessageBuilder.setFirstName(baseMessage.getFirstName());
        baseMessageBuilder.setIsActive(baseMessage.getIsActive());
        baseMessageBuilder.setCount(baseMessage.getCount());
        baseMessageBuilder.setDate(baseMessage.getDate());

        builder.setResponse(baseMessageBuilder.build());

        return builder.build();
    }

}
