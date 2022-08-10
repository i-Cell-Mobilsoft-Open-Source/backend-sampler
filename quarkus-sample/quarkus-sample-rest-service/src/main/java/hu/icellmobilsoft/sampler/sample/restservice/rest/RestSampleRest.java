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

import javax.enterprise.inject.Model;

import hu.icellmobilsoft.coffee.dto.common.commonservice.FunctionCodeType;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;

@Model
public class RestSampleRest implements ISampleRest {

    @Override
    public SampleResponse getSample() throws BaseException {
        // return wrapNoParam(restSampleAction::sample, "getSample");
        SampleResponse response = new SampleResponse();
        response.setFuncCode(FunctionCodeType.OK);
        response.setMessage("sample");
        SampleType type = new SampleType();
        type.setColumnA("A");
        response.setSample(type);
        return response;
    }

    @Override
    public SampleResponse postSample(SampleRequest sampleRequest) throws BaseException {
        SampleResponse response = new SampleResponse();
        response.setFuncCode(FunctionCodeType.OK);
        response.setMessage("sample");
        SampleType type = new SampleType();
        type.setColumnA("A");
        response.setSample(type);
        return response;
    }

}
