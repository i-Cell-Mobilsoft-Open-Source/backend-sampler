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
package hu.icellmobilsoft.sampler.sample.quarkus.coffee.filter.request;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.dto.common.LogConstants;
import hu.icellmobilsoft.coffee.se.logging.mdc.MDC;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.sample.quarkus.coffee.cdi.BaseApplicationContainer;
import hu.icellmobilsoft.sampler.sample.quarkus.coffee.filter.RequestResponseLogger;

/**
 * Base class for REST logging
 *
 * @author ischeffer
 * @since 0.1.0
 */
@Provider
public class RestRequestLogger implements ContainerRequestFilter {

    @Inject
    @ThisLogger
    AppLogger log;

    @Inject
    BaseApplicationContainer baseApplicationContainer;

    @Inject
    RequestResponseLogger requestResponseLogger;

    @Context
    UriInfo uriInfo;

    @Context
    HttpServletResponse httpServletResponse;

    /** {@inheritDoc} */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        MDC.clear();
        MDC.put(LogConstants.LOG_SERVICE_NAME, baseApplicationContainer.getCoffeeAppName());
        processRequest(requestContext);
    }

    /**
     * Processes HTTP request.
     *
     * @param requestContext
     *            context
     * @return HTTP request message or null if logging is disabled
     */
    protected String processRequest(ContainerRequestContext requestContext) {
        // if (RestLoggerUtil.logDisabled(requestContext, LogSpecifierTarget.REQUEST)) {
        // return null;
        // }

        StringBuffer message = new StringBuffer();
        printRequestLine(message, requestContext);
        printRequestHeaders(message, requestContext);
        printRequestEntity(message, requestContext);

        String messageString = message.toString();
        log.info(message.toString());
        return messageString;
    }

    /**
     * Name of the session key present in the HTTP header. The logger will search for this key in the HTTP headers and use its value in the
     * <code>MDC.put(LogConstants.LOG_SESSION_ID, ertek)</code> részben.<br>
     * <br>
     * It is meaningful for process identification and logging in Graylog
     * 
     * @return session key
     */
    public String sessionKey() {
        return "extSessionId";
    }

    /**
     * Prints request headers from {@link ContainerRequestContext} and appends given {@link StringBuffer} with the print result.
     *
     * @param b
     *            request message
     * @param requestContext
     *            context
     * @see RequestResponseLogger#printRequestHeaders(java.util.Map)
     */
    protected void printRequestHeaders(StringBuffer b, ContainerRequestContext requestContext) {
        b.append(requestResponseLogger.printRequestHeaders(requestContext.getHeaders()));
        String sessionId = null;
        if (requestContext.getHeaders().containsKey(sessionKey())) {
            sessionId = requestContext.getHeaders().get(sessionKey()).get(0);
        }
        MDC.put(LogConstants.LOG_SESSION_ID, StringUtils.defaultIfBlank(sessionId, RandomUtil.generateId()));
    }

    /**
     * Prints http path info from {@link ContainerRequestContext} and appends given {@link StringBuffer} with the print result.
     *
     * @param b
     *            request message
     * @param requestContext
     *            context
     * @see RequestResponseLogger#printRequestLine(ContainerRequestContext)
     */
    protected void printRequestLine(StringBuffer b, ContainerRequestContext requestContext) {
        b.append(requestResponseLogger.printRequestLine(requestContext));
    }

    /**
     * Prints http entity from {@link ContainerRequestContext} and appends given {@link StringBuffer} with the print result.
     * 
     * @param b
     *            request message
     * @param requestContext
     *            context
     * @see RequestResponseLogger#printRequestEntity(ContainerRequestContext)
     */
    protected void printRequestEntity(StringBuffer b, ContainerRequestContext requestContext) {
        b.append(requestResponseLogger.printRequestEntity(requestContext));
    }

}
