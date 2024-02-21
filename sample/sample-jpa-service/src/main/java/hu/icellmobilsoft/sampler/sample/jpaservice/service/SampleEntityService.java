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
package hu.icellmobilsoft.sampler.sample.jpaservice.service;

import java.util.List;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.sampler.common.system.jpa.service.BaseService;
import hu.icellmobilsoft.sampler.model.sample.SampleEntity;
import hu.icellmobilsoft.sampler.model.sample.SampleEntity_;
import hu.icellmobilsoft.sampler.model.sample.enums.SampleStatus;
import hu.icellmobilsoft.sampler.sample.jpaservice.repository.SampleEntityRepository;

/**
 * Service for {@link SampleEntity} querying. Represents only DB operations.
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Model
public class SampleEntityService extends BaseService<SampleEntity> {

    @Inject
    @ThisLogger
    private AppLogger log;

    @Inject
    private SampleEntityRepository sampleEntityRepository;

    /**
     * Elements associated with status
     *
     * @param status
     *            sample status
     * @return entity
     * @throws BaseException
     *             on error
     */
    public List<SampleEntity> findAllByStatus(SampleStatus status) throws BaseException {
        return wrapListValidated(sampleEntityRepository::findAllByStatus, status, "findAllByStatus", "status");
    }

    /**
     * Updates all SampleEntity entity with the given params
     *
     * @param sampleStatus new sampleStatus
     * @param inputValue   new inputValue
     * @return status of update
     * @throws BaseException on error
     */
    public int updateAllStatusAndInputValue(SampleStatus sampleStatus, String inputValue) throws BaseException {

        return wrapValidated(
                sampleEntityRepository::updateAllSampleStatusAndInputValue,
                sampleStatus,
                inputValue,
                "updateAllSampleStatusAndInputValue",
                SampleEntity_.STATUS,
                SampleEntity_.INPUT_VALUE
        );
    }
}
