/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.restservice.rest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import com.google.protobuf.Timestamp;

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.grpc.client.GrpcClient;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.grpc.api.service.error.ErrorServiceGrpc;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.BaseMessage;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyRequest;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyResponse;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyServiceGrpc;

/**
 * Sample action for testing gRPC
 * 
 * @author czenczl
 * @since 2.0.0
 */
@Model
public class RestSampleGetAction extends BaseAction {

    @Inject
    @ThisLogger
    private AppLogger log;

    @Inject
    @GrpcClient(configKey = "DummyServiceGrpc")
    private DummyServiceGrpc.DummyServiceBlockingStub dummyGrpcService;

    @Inject
    @GrpcClient(configKey = "ErrorServiceGrpc")
    private ErrorServiceGrpc.ErrorServiceBlockingStub errorGrpcService;

    /**
     * Dummy sample call to call gRPC service
     * 
     * @return SampleResponse Sample response with random id
     * @throws BaseException
     *             if error
     */
    public SampleResponse sample() throws BaseException {

        // sample message build
        DummyRequest.Builder reqBuilder = DummyRequest.newBuilder();
        BaseMessage.Builder baseMessageBuilder = BaseMessage.newBuilder();
        baseMessageBuilder.setAmount(3.14);
        baseMessageBuilder.setFirstName("first");
        baseMessageBuilder.setIsActive(true);
        baseMessageBuilder.setCount(50);
        LocalDate date = LocalDate.of(2022, 04, 14);
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(date.toEpochSecond(LocalTime.now(), ZoneOffset.UTC)).build();
        baseMessageBuilder.setDate(timestamp);
        reqBuilder.setRequest(baseMessageBuilder.build());
        DummyRequest dummyRequest = reqBuilder.build();

        // grpc client
        DummyResponse helloResponse = dummyGrpcService.getDummy(dummyRequest);

        log.info(helloResponse.getResponse().getFirstName());
        log.info(helloResponse.getResponse().getDate() + "");
        log.info(helloResponse.getResponse().getCount() + "");

        SampleResponse response = new SampleResponse();

        handleSuccessResultType(response);
        return response;
    }
}
