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
package hu.icellmobilsoft.sampler.sample.restservice.rest.validation.json;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.sampler.sample.restservice.rest.utils.gson.JsonUtil;
import hu.icellmobilsoft.sampler.sample.restservice.rest.validation.xml.exception.XsdProcessingException;

//@Provider
//@Consumes({ MediaType.APPLICATION_JSON })
//@Priority(Priorities.ENTITY_CODER)
public class JsonMessageBodyReader<BaseRequestType> 
//extends JsonMessageBodyReaderBase<BaseRequestType>  
{


//
////    @Inject
////    private MyC myc;
//    
//    /**
//     * {@inheritDoc}
//     *
//     * Use project specific Json util
//     * 
//     * @see JsonUtil
//     */
//    @Override
//    protected BaseRequestType deserializeJson(Class<BaseRequestType> type, InputStream entityStream) throws XsdProcessingException {
//        try {
////            System.out.println(myc);
//            return JsonUtil.toObjectGson(new InputStreamReader(entityStream), type);
//        } catch (Exception e) {
//            throw new XsdProcessingException(CoffeeFaultType.INVALID_INPUT, e.getMessage(), e);
//        }
//    }


}
