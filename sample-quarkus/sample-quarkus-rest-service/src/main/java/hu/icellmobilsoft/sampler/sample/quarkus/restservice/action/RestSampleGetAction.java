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
package hu.icellmobilsoft.sampler.sample.quarkus.restservice.action;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.dto.common.commonservice.FunctionCodeType;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;

/**
 * Sample action
 * 
 * @author czenczl
 * @since 0.1.0
 */
@Model
public class RestSampleGetAction extends BaseAction {

    @ThisLogger
    @Inject AppLogger logger;
    /**
     * Dummy sample reponse
     * 
     * @return SampleResponse Sample response with random id
     * @throws BaseException
     *             if error
     */
    public SampleResponse sample() throws BaseException {
        logger.trace(">> RestSampleGetAction.sample()");
        SampleResponse response = new SampleResponse();

        SampleType sampleType = new SampleType();
        sampleType.setSampleId(RandomUtil.generateId());
        sampleType.setSampleStatus(SampleStatusEnumType.DONE);
        sampleType.setColumnA("A");
        sampleType.setColumnB(SampleValueEnumType.VALUE_A);
        response.setSample(sampleType);
        response.setFuncCode(FunctionCodeType.OK);
        handleSuccessResultType(response);
        logger.trace("<< RestSampleGetAction.sample()");
        return response;
    }
}
