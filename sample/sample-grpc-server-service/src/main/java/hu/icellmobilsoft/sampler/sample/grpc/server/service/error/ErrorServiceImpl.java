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
package hu.icellmobilsoft.sampler.sample.grpc.server.service.error;

import com.google.rpc.Status;

import hu.icellmobilsoft.sampler.common.grpc.core.exception.ExceptionHandler;
import hu.icellmobilsoft.sampler.common.grpc.error.ErrorServiceGrpc.ErrorServiceImplBase;
import hu.icellmobilsoft.sampler.common.grpc.error.RequestForError;
import hu.icellmobilsoft.sampler.common.grpc.error.ResponseForError;
import hu.icellmobilsoft.sampler.sample.grpc.server.service.action.SampleGrpcAction;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;

/**
 * Delegate gRPC service call to CDI bean
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
public class ErrorServiceImpl extends ErrorServiceImplBase {

    private SampleGrpcAction sampleGrpcAction;

    public ErrorServiceImpl(SampleGrpcAction sampleGrpcAction) {
        this.sampleGrpcAction = sampleGrpcAction;
    }

    @Override
    public void error(RequestForError request, StreamObserver<ResponseForError> responseObserver) {
        // delegate to cdi bean
        try {
            sampleGrpcAction.call(request, responseObserver);
        } catch (Throwable e) {
            Status status = ExceptionHandler.getInstance().handle(e);
            responseObserver.onError(StatusProto.toStatusRuntimeException(status));
        }
    }

}
