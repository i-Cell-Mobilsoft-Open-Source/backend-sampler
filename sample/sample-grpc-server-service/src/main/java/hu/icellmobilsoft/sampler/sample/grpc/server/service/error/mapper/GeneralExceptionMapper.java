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

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;

import com.google.rpc.Code;
import com.google.rpc.Status;

import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.grpc.base.exception.ExceptionMapper;
import hu.icellmobilsoft.coffee.se.logging.Logger;

/**
 * Implementation of {@link ExceptionMapper} that maps general Exceptions to gRPC {@link Status}. If the exception is not recognized, it maps it to
 * {@link Code#INTERNAL}.
 * 
 * It uses {@link IExceptionStatusTranslator} to translate exceptions to GRPC statuses.
 *
 * @author mark.petrenyi
 * @since 2.0.0
 */
@Dependent
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    @Inject
    private Logger log;

    @Inject
    private IExceptionStatusTranslator exceptionStatusTranslator;

    /**
     * Maps exceptions to GRPC statuses.
     * 
     * @param e
     *            the exception to be mapped
     * @return the GRPC status
     * @see Status
     */
    @Override
    public Status toStatus(Exception e) {
        Status status = null;
        if (e instanceof NotAuthorizedException) {
            status = exceptionStatusTranslator.createStatus(e, Code.UNAUTHENTICATED, CoffeeFaultType.NOT_AUTHORIZED);
        } else if (e instanceof ForbiddenException) {
            status = exceptionStatusTranslator.createStatus(e, Code.PERMISSION_DENIED, CoffeeFaultType.FORBIDDEN);
        } else if (e instanceof IllegalArgumentException || e.getCause() instanceof IllegalArgumentException) {
            status = exceptionStatusTranslator.createStatus(e, Code.INVALID_ARGUMENT, CoffeeFaultType.ILLEGAL_ARGUMENT_EXCEPTION);
        }

        if (status != null) {
            log.error("Known error: ", e);
        } else {
            log.error("Unknown error: ", e);
            status = exceptionStatusTranslator.createStatus(e, Code.INTERNAL, CoffeeFaultType.GENERIC_EXCEPTION);
        }
        return status;
    }

}
