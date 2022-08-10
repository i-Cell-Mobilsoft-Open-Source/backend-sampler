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
package hu.icellmobilsoft.sampler.sample.quarkus.restservice.rest.filter.response;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.jboss.logging.Logger;

import hu.icellmobilsoft.sampler.sample.quarkus.restservice.rest.log.RequestResponseLogger;
import hu.icellmobilsoft.sampler.sample.quarkus.restservice.rest.utils.stream.OutputStreamCopier;

@Provider
public class RestResponseLogger implements WriterInterceptor {

    @Inject
    private Logger log;

    @Context
    private UriInfo uriInfo;

    @Inject
    private RequestResponseLogger requestResponseLogger;

    @Context
    private HttpServletResponse res;

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        processResponse(context);
    }

    /**
     * Processes HTTP response.
     * 
     * @param context
     *            context
     * @return HTTP response message or null if logging is disabled
     * @throws IOException
     *             if response cannot be processed.
     */
    protected String processResponse(WriterInterceptorContext context) throws IOException {
        StringBuffer message = new StringBuffer();
        try {

            printResponseLine(message, context);
            printResponseHeaders(message, context);

            OutputStream originalStream = context.getOutputStream();
            byte[] entityCopy = new byte[0];
            OutputStreamCopier osc = new OutputStreamCopier(originalStream);
            context.setOutputStream(osc);
            // elegessuk a stream-et, kozben masoljuk a tartalmat
            try {
                context.proceed();
            } finally {
                // IS: kerdeses erdemes-e vissza irni az eredeti stream-et...
                context.setOutputStream(originalStream);
            }
            entityCopy = osc.getCopy();

            printResponseEntity(message, context, entityCopy);
        } finally {
            log.info(message.toString());
        }
        return message.toString();
    }

    /**
     * Prints response from {@link WriterInterceptorContext} and appends given {@link StringBuffer} with the print result.
     * 
     * @param b
     *            response message
     * @param context
     *            context
     * @param entityCopy
     *            entity
     * @see RequestResponseLogger#printResponseEntity(String, WriterInterceptorContext, byte[])
     */
    protected void printResponseEntity(StringBuffer b, WriterInterceptorContext context, byte[] entityCopy) {
        b.append(requestResponseLogger.printResponseEntity(uriInfo.getAbsolutePath().toASCIIString(), context, entityCopy));
    }

    /**
     * Prints response URL line and appends given {@link StringBuffer} with the print result.
     * 
     * @param b
     *            response message
     * @param context
     *            context
     * @see RequestResponseLogger#printResponseLine(String, int, String, String)
     */
    protected void printResponseLine(StringBuffer b, WriterInterceptorContext context) {
        String fullPath = uriInfo.getAbsolutePath().toASCIIString();
        // int status = res.getStatusCode();
        int status = res.getStatus();

        Status statusEnum = Status.fromStatusCode(status);
        String statusInfo = statusEnum != null ? statusEnum.getReasonPhrase() : null;
        MediaType mediaType = context.getMediaType();
        b.append(requestResponseLogger.printResponseLine(fullPath, status, String.valueOf(statusInfo), String.valueOf(mediaType)));
    }

    /**
     * Prints response header values and appends given {@link StringBuffer} with the print result.
     * 
     * @param b
     *            response message
     * @param context
     *            context
     * @see RequestResponseLogger#printResponseHeaders(java.util.Map)
     */
    protected void printResponseHeaders(StringBuffer b, WriterInterceptorContext context) {
        b.append(requestResponseLogger.printResponseHeaders(context.getHeaders()));
    }

}
