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

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.google.rpc.Code;
import com.google.rpc.Status;

import hu.icellmobilsoft.coffee.dto.exception.AccessDeniedException;
import hu.icellmobilsoft.coffee.dto.exception.BONotFoundException;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.BusinessException;
import hu.icellmobilsoft.coffee.dto.exception.DtoConversionException;
import hu.icellmobilsoft.coffee.dto.exception.ServiceUnavailableException;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.common.grpc.core.exception.ExceptionMapper;

/**
 * Implementation of {@link ExceptionMapper} that maps {@link BaseException} to gRPC {@link Status}.
 *
 * @author mark.petrenyi
 * @since 2.0.0
 */
@ApplicationScoped
@Priority(1)
public class BaseExceptionMapper implements ExceptionMapper<BaseException> {

    @Inject
    private IExceptionStatusTranslator exceptionStatusTranslator;
    @Inject
    private Logger log;

    /**
     * Maps the given {@link BaseException} to a gRPC {@link Status}.
     *
     * @param e
     *            The exception to be mapped.
     * @return The mapped gRPC {@link Status}.
     */
    @Override
    public Status toStatus(BaseException e) {
        Status status = null;
        if (e instanceof AccessDeniedException) {
            status = exceptionStatusTranslator.createStatus(e, Code.UNAUTHENTICATED);
        } else if (e instanceof BONotFoundException) {
            status = exceptionStatusTranslator.createStatus(e, Code.NOT_FOUND);
        } else if (e instanceof DtoConversionException) {
            status = exceptionStatusTranslator.createStatus(e, Code.INVALID_ARGUMENT);
        } else if (e instanceof ServiceUnavailableException) {
            status = exceptionStatusTranslator.createStatus(e, Code.UNAVAILABLE);
        } else if (e instanceof BusinessException) {
            status = exceptionStatusTranslator.createStatus(e, Code.FAILED_PRECONDITION);
        }

        if (status != null) {
            log.error("Known error: ", e);
        } else {
            log.error("Unknown error: ", e);
            status = exceptionStatusTranslator.createStatus(e, Code.INTERNAL);
        }
        return status;
    }

}
