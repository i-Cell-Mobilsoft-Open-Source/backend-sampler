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
package hu.icellmobilsoft.sampler.common.rest.filter;

import java.io.IOException;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;

import hu.icellmobilsoft.sampler.common.rest.cdi.RequestContainer;
import hu.icellmobilsoft.sampler.common.rest.header.ProjectHeader;

/**
 * Common utility request filter. Primary target is processing http request headers
 * <p>
 * Must be run before exception handlers, for correct error codes language transalting (see: {@link PreMatching})
 *
 * @author imre.scheffer
 * @since 0.1.0
 */
@Provider
@PreMatching
@Priority(CustomPriorities.PRE_AUTHENTICATION)
public class RequestFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        RequestContainer requestContainer = CDI.current().select(RequestContainer.class).get();
        ProjectHeader header = ProjectHeader.readHeaders(requestContext.getHeaders());

        // Set remote IP if in Forwarded header not specified.
        if (StringUtils.isBlank(header.getForwarded()) && request != null) {
            header.setForwarded(request.getRemoteAddr());
        }

        requestContainer.setProjectHeader(header);
    }
}
