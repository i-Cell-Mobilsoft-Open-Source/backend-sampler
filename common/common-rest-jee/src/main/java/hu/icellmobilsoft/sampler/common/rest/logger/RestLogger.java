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
package hu.icellmobilsoft.sampler.common.rest.logger;

import jakarta.annotation.Priority;
import jakarta.ws.rs.ext.Provider;

import hu.icellmobilsoft.coffee.rest.log.optimized.BaseRestLogger;
import hu.icellmobilsoft.sampler.common.rest.filter.CustomPriorities;
import hu.icellmobilsoft.sampler.dto.constant.IHttpHeaderConstants;

/**
 * REST request-response logger
 *
 * @author ischeffer
 * @since 0.1.0
 *
 */
@Provider
@Priority(CustomPriorities.PRE_AUTHENTICATION)
public class RestLogger extends BaseRestLogger {

    @Override
    public String sessionKey() {
        return IHttpHeaderConstants.HEADER_SID;
    }
}
