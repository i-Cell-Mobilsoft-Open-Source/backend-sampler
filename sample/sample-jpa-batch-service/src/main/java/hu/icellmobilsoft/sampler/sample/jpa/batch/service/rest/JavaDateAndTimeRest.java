/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2024 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.jpa.batch.service.rest;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.dto.common.commonservice.BaseResponse;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.sampler.api.jee.rest.batch.IJavaDateAndTimeRest;
import hu.icellmobilsoft.sampler.common.system.rest.rest.BaseRestService;
import hu.icellmobilsoft.sampler.dto.sample.batch.javadateandtime.JavaDateAndTimeInsertRequest;
import hu.icellmobilsoft.sampler.dto.sample.batch.javadateandtime.JavaDateAndTimeResponse;
import hu.icellmobilsoft.sampler.dto.sample.batch.javadateandtime.JavaDateAndTimeUpdateRequest;
import hu.icellmobilsoft.sampler.sample.jpa.batch.service.action.JavaDateAndTimeAction;

/**
 * Implementation of {@link IJavaDateAndTimeRest}.
 * 
 * @author csaba.balogh
 * @since 2.0.0
 */
@Model
public class JavaDateAndTimeRest extends BaseRestService implements IJavaDateAndTimeRest {

    @Inject
    private JavaDateAndTimeAction javaDateAndTimeAction;

    @Override
    public JavaDateAndTimeResponse postInsertJavaDateAndTimeEntityWithBatchService(JavaDateAndTimeInsertRequest javaDateAndTimeInsertRequest)
            throws BaseException {
        String methodName = "postInsertJavaDateAndTimeEntityWithBatchService";
        return wrapPathParam1(javaDateAndTimeAction::insertJavaDateAndTime, javaDateAndTimeInsertRequest, methodName, "javaDateAndTimeInsertRequest");
    }

    @Override
    public JavaDateAndTimeResponse putUpdateJavaDateAndTimeEntityWithBatchService(String javaDateAndTimeId,
            JavaDateAndTimeUpdateRequest javaDateAndTimeUpdateRequest) throws BaseException {
        return wrapPathParam2(
                javaDateAndTimeAction::updateJavaDateAndTime,
                javaDateAndTimeId,
                javaDateAndTimeUpdateRequest,
                "putUpdateJavaDateAndTimeEntityWithBatchService",
                "javaDateAndTimeId",
                "javaDateAndTimeUpdateRequest");
    }

    @Override
    public BaseResponse deleteAllJavaDateAndTimeEntitiesWithBatchService() throws BaseException {
        return wrapNoParam(javaDateAndTimeAction::deleteAllJavaDateAndTime, "deleteAllJavaDateAndTimeEntitiesWithBatchService");
    }
}
