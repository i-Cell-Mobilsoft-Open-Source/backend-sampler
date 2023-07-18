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
package hu.icellmobilsoft.sampler.sample.grpc.server.service.error.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;

import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.rest.projectstage.ProjectStage;

/**
 * Default implementation for translating exceptions to status.
 *
 * @author mark.petrenyi
 * @since 2.0.0
 */
@ApplicationScoped
public class DefaultExceptionStatusTranslator implements IExceptionStatusTranslator {

    @Inject
    private ProjectStage projectStage;

    // @Inject
    // private LocalizedMessage localizedMessage;

    @Override
    public Status createStatus(Exception e, Code code, Enum<?> faultType) {
        if (e == null || code == null) {
            return null;
        }
        Enum<?> faultTypeToReturn = faultType != null ? faultType : CoffeeFaultType.OPERATION_FAILED;
        ErrorInfo.Builder errorInfoBuilder = ErrorInfo.newBuilder() //
                // TODO active request scope + ProjectHeader produce
                // .setReason(localizedMessage.message(faultTypeToReturn)) //
                .setReason(faultTypeToReturn.name())//
                .setDomain("sample-service");
        if (!projectStage.isProductionStage()) {
            errorInfoBuilder.putMetadata("exception", e.getLocalizedMessage());
        }
        return Status.newBuilder() //
                .setCode(code.getNumber())//
                .setMessage(faultTypeToReturn.name())//
                .addDetails(Any.pack(errorInfoBuilder.build())) //
                .build();
    }
}
