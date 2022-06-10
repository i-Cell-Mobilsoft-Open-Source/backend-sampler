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

import java.time.LocalDateTime;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.transaction.Transactional;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.model.sample.SampleEntity;
import hu.icellmobilsoft.sampler.model.sample.enums.SampleStatus;
import hu.icellmobilsoft.sampler.model.sample.enums.SampleValue;
import hu.icellmobilsoft.sampler.sample.jpaservice.converter.SampleTypeConverter;
import hu.icellmobilsoft.sampler.sample.jpaservice.service.SampleEntityService;

/**
 * Sample query action
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Model
public class JpaSamplePostAction extends BaseAction {

    @Inject
    private SampleEntityService sampleEntityService;

    @Inject
    private SampleTypeConverter sampleTypeConverter;

    /**
     * Dummy sample write and read data from DB
     * 
     * @param sampleRequest
     *            validated http entity body
     * @return Sample response with random readed data
     * @throws BaseException
     *             if error
     */
    public SampleResponse sampleWriteRead(SampleRequest sampleRequest) throws BaseException {

        // create entity
        SampleEntity created = CDI.current().select(JpaSamplePostAction.class).get().createOne();

        SampleEntity readed = sampleEntityService.findById(created.getId(), SampleEntity.class);

        SampleResponse response = new SampleResponse();
        response.setSample(sampleTypeConverter.convert(readed));

        handleSuccessResultType(response, sampleRequest);
        return response;
    }

    /**
     * Create one entity with dummy data
     * 
     * @return created, persisted entity
     * @throws BaseException
     *             if error
     */
    @Transactional
    public SampleEntity createOne() throws BaseException {
        SampleEntity entity = new SampleEntity();
        entity.setLocalDateTime(LocalDateTime.now());
        entity.setStatus(SampleStatus.DONE);
        entity.setInputValue("Generated");
        entity.setValue(SampleValue.VALUE_B);
        return sampleEntityService.save(entity);
    }
}
