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

import com.google.rpc.Code;
import com.google.rpc.Status;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;

/**
 * Interface for translating exceptions to status.
 *
 * @author mark.petrenyi
 * @since 2.0.0
 */
public interface IExceptionStatusTranslator {

    /**
     * Creates a {@link Status} from a given {@link BaseException} and {@link Code}.
     *
     * @param e
     *            The BaseException to translate.
     * @param code
     *            The code to set in the status.
     * @return The status created from the given BaseException and code.
     */
    default Status createStatus(BaseException e, Code code) {
        if (e == null || code == null) {
            return null;
        }
        return createStatus(e, code, e.getFaultTypeEnum());
    }

    /**
     * Creates a {@link Status} from a given {@link BaseException}, {@link Code} and fault type.
     *
     * @param e
     *            The Exception to translate.
     * @param code
     *            The code to set in the status.
     * @param faultType
     *            The fault type to set in the status.
     * @return The status created from the given Exception, code, and fault type.
     */
    Status createStatus(Exception e, Code code, Enum<?> faultType);
}
