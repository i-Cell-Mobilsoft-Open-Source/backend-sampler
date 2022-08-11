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

import javax.enterprise.inject.Model;

import hu.icellmobilsoft.coffee.dto.common.commonservice.FunctionCodeType;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;
import hu.icellmobilsoft.sampler.sample.quarkus.coffee.utils.RandomUtil;

/**
 * Sample action
 * 
 * @author czenczl
 * @since 0.1.0
 */
@Model
public class RestSampleGetAction {

    /**
     * Dummy sample reponse
     * 
     * @return SampleResponse Sample response with random id
     * @throws BaseException
     *             if error
     */
    public SampleResponse sample() throws BaseException {
        SampleResponse response = new SampleResponse();

        SampleType sampleType = new SampleType();
        sampleType.setSampleId(RandomUtil.generateId());
        sampleType.setSampleStatus(SampleStatusEnumType.DONE);
        sampleType.setColumnA("A");
        sampleType.setColumnB(SampleValueEnumType.VALUE_A);
        response.setSample(sampleType);
        response.setFuncCode(FunctionCodeType.OK);
        // handleSuccessResultType(response);
        return response;
    }
}
