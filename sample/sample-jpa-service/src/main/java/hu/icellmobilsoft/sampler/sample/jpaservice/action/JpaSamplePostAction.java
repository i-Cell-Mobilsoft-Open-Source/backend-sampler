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

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.jpa.helper.TransactionHelper;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.exception.SamplerException;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.model.sample.SampleEntity;
import hu.icellmobilsoft.sampler.model.sample.enums.SampleStatus;
import hu.icellmobilsoft.sampler.model.sample.enums.SampleValue;
import hu.icellmobilsoft.sampler.sample.jpaservice.converter.SampleTypeConverter;
import hu.icellmobilsoft.sampler.sample.jpaservice.service.SampleEntityService;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

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

    @Inject
    private TransactionHelper transactionHelper;

    @Inject
    @ThisLogger
    AppLogger log;

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

        try {
            CDI.current().select(JpaSamplePostAction.class).get().createOneNeedTransaction();
            throw new SamplerException("Unexpected successful save without @Transactional");
        } catch (SamplerException e) {
            throw e;
        } catch (BaseException e) {
            log.info("Expected exception - no transaction: [{0}]", e.getLocalizedMessage());
        }
        // create entity
        SampleEntity created = transactionHelper.executeWithTransaction(this::createOneNeedTransaction);

        // modify entity
        created.setStatus(SampleStatus.DONE);
        created = transactionHelper.executeWithTransaction(sampleEntityService::save, created);

        SampleEntity readed = sampleEntityService.findById(created.getId(), SampleEntity.class);
        if (!created.getId().equals(readed.getId()) || created.getCreationDate() == null
                || !created.getCreationDate().equals(readed.getCreationDate()) || created.getCreatorUser() == null
                || !created.getCreatorUser().equals(readed.getCreatorUser())) {
            throw new TechnicalException("Unexpected data integrity error, some mandatory field is empty or not equal!");
        }

        SampleResponse response = new SampleResponse();
        response.setSample(sampleTypeConverter.convert(readed));

        handleSuccessResultType(response, sampleRequest);
        return response;
    }

    /**
     * Create one entity with dummy data. Need transaction for success.
     *
     * @return created, persisted entity
     * @throws BaseException
     *             if error
     */
    public SampleEntity createOneNeedTransaction() throws BaseException {
        SampleEntity entity = new SampleEntity();
        entity.setLocalDateTime(LocalDateTime.now());
        entity.setStatus(SampleStatus.DONE);
        entity.setStatus(SampleStatus.PROCESSING);
        entity.setInputValue("Generated");
        entity.setValue(SampleValue.VALUE_B);
        return sampleEntityService.save(entity);
    }

}
