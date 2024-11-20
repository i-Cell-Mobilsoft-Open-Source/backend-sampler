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
package hu.icellmobilsoft.sampler.sample.jpaservice.action;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.se.util.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;
import hu.icellmobilsoft.sampler.model.sample.SampleEntity;
import hu.icellmobilsoft.sampler.sample.jpaservice.service.SampleEntityService;

/**
 * Service for JPA querying. Represents only DB operations.
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Model
public class JpaSampleGetAction extends BaseAction {

    @Inject
    private SampleEntityService sampleEntityService;

    /**
     * Dummy sample reponse
     * 
     * @return SampleResponse Sample response with random id
     * @throws BaseException
     *             if error
     */
    public SampleResponse sample() throws BaseException {

        var none = sampleEntityService.findByQueryParams("aaaaaa", SampleEntity.class);

        SampleResponse response = new SampleResponse();
        SampleType sampleType = new SampleType();
        sampleType.withColumnA(none.toString());
        sampleType.withColumnB(SampleValueEnumType.VALUE_A);
        sampleType.withSampleId(RandomUtil.generateId());
        sampleType.withSampleStatus(SampleStatusEnumType.DONE);
        response.setSample(sampleType);

        handleSuccessResultType(response);
        return response;
    }
}
