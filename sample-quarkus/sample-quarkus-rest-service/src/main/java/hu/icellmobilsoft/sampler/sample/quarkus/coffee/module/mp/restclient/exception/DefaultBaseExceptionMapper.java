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
package hu.icellmobilsoft.sampler.sample.quarkus.coffee.module.mp.restclient.exception;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.dto.common.commonservice.BONotFound;
import hu.icellmobilsoft.coffee.dto.common.commonservice.BaseExceptionResultType;
import hu.icellmobilsoft.coffee.dto.common.commonservice.BusinessFault;
import hu.icellmobilsoft.coffee.dto.common.commonservice.TechnicalFault;
import hu.icellmobilsoft.coffee.dto.exception.AccessDeniedException;
import hu.icellmobilsoft.coffee.dto.exception.BONotFoundException;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.BusinessException;
import hu.icellmobilsoft.coffee.dto.exception.DtoConversionException;
import hu.icellmobilsoft.coffee.dto.exception.ServiceUnavailableException;
import hu.icellmobilsoft.sampler.sample.quarkus.coffee.cdi.BaseApplicationContainer;

/**
 * Handled exception mapper based on coffee
 * 
 * @author czenczl
 * @version 0.1.0
 *
 */
@Provider
public class DefaultBaseExceptionMapper implements ExceptionMapper<BaseException> {

    @Inject
    BaseApplicationContainer baseApplicationContainer;

    @Inject
    IExceptionMessageTranslator exceptionMessageTranslator;

    @Inject
    @ThisLogger
    AppLogger log;

    @Override
    public Response toResponse(BaseException e) {
        log.error("Known error: ", e);

        return handleException(e);
    }

    /**
     * Exception handling
     *
     * @param e
     *            exception
     * @return assembled response
     */
    protected Response handleException(BaseException e) {
        if (e instanceof AccessDeniedException) {
            return createResponse(e, Response.Status.UNAUTHORIZED, new BusinessFault());
        } else if (e instanceof BONotFoundException) {
            return createResponse(e, IExceptionMessageTranslator.HTTP_STATUS_I_AM_A_TEAPOT, new BONotFound());
        } else if (e instanceof DtoConversionException) {
            return createResponse(e, Response.Status.BAD_REQUEST, new BusinessFault());
        } else if (e instanceof ServiceUnavailableException) {
            return createResponse(e, Response.Status.SERVICE_UNAVAILABLE, new BusinessFault());
            // TODO xsd validation missing
            // } else if (e instanceof XsdProcessingException) {
            // XsdProcessingException xsdProcessingException = (XsdProcessingException) e;
            // return createValidationErrorResponse(e, xsdProcessingException.getErrors());
        } else if (e instanceof BusinessException) {
            return createResponse(e, Response.Status.INTERNAL_SERVER_ERROR, new BusinessFault());
        } else {
            // BaseException/TechnicalException
            return createResponse(e, Response.Status.INTERNAL_SERVER_ERROR, new TechnicalFault());
        }
    }

    /**
     * Creating response
     *
     * @param e
     *            the exception
     * @param status
     *            the desired {@link Response.Status} to be passed in the response
     * @param dto
     *            {@link BaseExceptionResultType} derived, included in the response
     * @return assembled response
     */
    protected Response createResponse(BaseException e, Response.Status status, BaseExceptionResultType dto) {
        return createResponse(e, status.getStatusCode(), dto);
    }

    /**
     * Creating response
     *
     * @param e
     *            the exception
     * @param statusCode
     *            the desired status code to be passed in the response (pl.: 418)
     * @param dto
     *            {@link BaseExceptionResultType} derived, included in the response
     * @return assembled response
     */
    protected Response createResponse(BaseException e, int statusCode, BaseExceptionResultType dto) {
        exceptionMessageTranslator.addCommonInfo(dto, e, e.getFaultTypeEnum());
        return Response.status(statusCode).entity(dto).build();
    }
}
