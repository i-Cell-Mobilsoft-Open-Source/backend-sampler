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
package hu.icellmobilsoft.sampler.sample.restservice.rest;

import jakarta.enterprise.inject.Model;

import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;

/**
 * Sample query action
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Model
public class RestSamplePostAction extends BaseAction {

    /**
     * Dummy sample query reponse
     * 
     * @param sampleRequest
     *            validated http entity body
     * @return Sample response with random 1. page data
     * @throws BaseException
     *             if error
     */
    public SampleResponse postSample(SampleRequest sampleRequest) throws BaseException {
        SampleResponse response = new SampleResponse();

        SampleType sampleType = new SampleType();
        sampleType.setSampleId(RandomUtil.generateId());
        sampleType.setSampleStatus(SampleStatusEnumType.DONE);
        sampleType.setColumnA(sampleRequest.getSample().getColumnA());
        sampleType.setColumnB(sampleRequest.getSample().getColumnB());
        response.setSample(sampleType);

        handleSuccessResultType(response, sampleRequest);
        return response;
    }
}
