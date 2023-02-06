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

import com.google.protobuf.Any;
import com.google.rpc.ErrorInfo;

import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.grpc.error.RequestForError;
import hu.icellmobilsoft.sampler.common.grpc.error.ResponseForError;
import hu.icellmobilsoft.sampler.common.sample.grpc.BaseMessage;
import hu.icellmobilsoft.sampler.common.sample.grpc.DummyRequest;
import hu.icellmobilsoft.sampler.common.sample.grpc.DummyResponse;
import hu.icellmobilsoft.sampler.common.sample.rest.post.SampleRequestType;
import hu.icellmobilsoft.sampler.common.sample.rest.post.SampleResponseType;
import hu.icellmobilsoft.sampler.common.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.common.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.common.sample.xsd.grpc.DummyXsdRequest;
import hu.icellmobilsoft.sampler.common.sample.xsd.grpc.DummyXsdResponse;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * CDI action bean
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
@ApplicationScoped
public class SampleGrpcAction {

    @Inject
    private Logger log;

    /**
     * dummy call
     * 
     * @param request
     *            {@link DummyRequest}
     * @param responseObserver
     *            {@link StreamObserver<DummyResponse>}
     */
    public void call(DummyRequest request, StreamObserver<DummyResponse> responseObserver) {

        DummyResponse response = toResponse(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    /**
     * dummy call
     *
     * @param request
     *            {@link DummyRequest}
     * @param responseObserver
     *            {@link StreamObserver<DummyResponse>}
     */
    public void call(DummyXsdRequest request, StreamObserver<DummyXsdResponse> responseObserver) {

        DummyXsdResponse response = toXsdResponse(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    private DummyXsdResponse toXsdResponse(DummyXsdRequest request) {
        SampleRequestType sampleRequest = request.getRequest();
        DummyXsdResponse.Builder builder = DummyXsdResponse.newBuilder();
        SampleResponseType.Builder builder1 = SampleResponseType.newBuilder();
        SampleType.Builder builder2 = SampleType.newBuilder();
        builder2.setSampleId(RandomUtil.generateId());
        builder2.setSampleStatus(SampleStatusEnumType.SAMPLE_STATUS_ENUM_TYPE_DONE);
        builder2.setColumnA(sampleRequest.getSample().getColumnA());
        builder2.setColumnB(sampleRequest.getSample().getColumnB());
        builder1.setSample(builder2);
        builder.setResponse(builder1);

        return builder.build();
    }

    private DummyResponse toResponse(DummyRequest request) {
        BaseMessage baseMessage = request.getRequest();

        DummyResponse.Builder builder = DummyResponse.newBuilder();

        hu.icellmobilsoft.sampler.common.sample.grpc.BaseMessage.Builder baseMessageBuilder = BaseMessage.newBuilder();
        baseMessageBuilder.setAmount(baseMessage.getAmount());
        baseMessageBuilder.setFirstName(baseMessage.getFirstName());
        baseMessageBuilder.setIsActive(baseMessage.getIsActive());
        baseMessageBuilder.setCount(baseMessage.getCount());
        baseMessageBuilder.setDate(baseMessage.getDate());

        builder.setResponse(baseMessageBuilder.build());

        return builder.build();
    }

    /**
     * dummy error call
     * 
     * @param request
     *            {@link DummyRequest}
     * @param responseObserver
     *            {@link StreamObserver<DummyResponse>}
     */
    public void call(RequestForError request, StreamObserver<ResponseForError> responseObserver) {

        try {
            throw new Exception("test exception");

        } catch (Exception e) {

            com.google.rpc.Status.Builder statusBuilder = com.google.rpc.Status.newBuilder();

            statusBuilder.setCode(com.google.rpc.Code.INTERNAL.getNumber());
            statusBuilder.setMessage("Grpc server error");
            statusBuilder.addDetails(Any.pack(ErrorInfo.newBuilder() //
                    .setReason(e.getMessage()) //
                    .setDomain("sample") //
                    .putMetadata("requestId", request.getRequestId()) //
                    .build()));

            responseObserver.onError(StatusProto.toStatusRuntimeException(statusBuilder.build()));
        }

    }

}
