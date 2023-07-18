/*-
 * #%L
 * Coffee
 * %%
 * Copyright (C) 2020 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.quarkus.coffee.module.mp.restclient.util;

import javax.enterprise.inject.Vetoed;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;

/**
 * Useful utility collection when using MicroProfile REST Client.
 *
 * @author imre.scheffer
 * @since 1.0.0
 */
@Vetoed
public class MPRestClientUtil {

    /**
     * General REST Client exception converter at the framework level
     *
     * @param e
     *            exception
     * @return BaseException derived
     */
    public static BaseException toBaseException(Exception e) {
        if (e instanceof WebApplicationException) {
            return toBaseException((WebApplicationException) e);
        } else if (e instanceof ProcessingException) {
            return toBaseException((ProcessingException) e);
        } else {
            return new TechnicalException(CoffeeFaultType.OPERATION_FAILED, "REST client unhandled exception: " + e.getLocalizedMessage(), e);
        }
    }

    /**
     * The errors provided by the handled ResponseExceptionMapper are natively wrapped in this
     *
     * @param e
     *            WebApplicationException
     * @return BaseException derived
     */
    public static BaseException toBaseException(WebApplicationException e) {
        if (e.getCause() instanceof BaseException) {
            return (BaseException) e.getCause();
        } else {
            return new TechnicalException(CoffeeFaultType.OPERATION_FAILED, "REST client handled exception: " + e.getLocalizedMessage(), e);
        }
    }

    /**
     * Errors that occur during the use of the REST client, such as when the readTimeout of RestClientBuilder expires, are thrown
     * 
     *
     * @param e
     *            ProcessingException
     * @return TechnicalException
     */
    public static TechnicalException toBaseException(ProcessingException e) {
        return new TechnicalException(CoffeeFaultType.OPERATION_FAILED, e.getLocalizedMessage(), e);
    }
}
