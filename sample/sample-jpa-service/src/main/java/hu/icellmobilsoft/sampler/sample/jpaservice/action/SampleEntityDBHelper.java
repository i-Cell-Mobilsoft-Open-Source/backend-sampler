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

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.sampler.model.sample.SampleEntity;
import hu.icellmobilsoft.sampler.sample.jpaservice.service.SampleEntityService;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Helper class for SampleEntity db actions.
 * 
 * @author zsolt.vasi
 * @since 2.0.0
 */
@Model
public class SampleEntityDBHelper {

    @Inject
    private SampleEntityService sampleEntityService;

    /**
     * Saves the given {@link SampleEntity} into database
     *
     * @param entity
     *            the given entity to save
     * @return saved entity
     * @throws BaseException
     *             if error
     */
    @Transactional
    public SampleEntity save(SampleEntity entity) throws BaseException {
        return sampleEntityService.save(entity);
    }

    /**
     * Saves the given {@link SampleEntity} into database without transaction.
     *
     * @param entity
     *            entity to save
     * @return saved entity
     * @throws BaseException
     *             if error
     */
    public SampleEntity saveWithoutTransaction(SampleEntity entity) throws BaseException {
        return sampleEntityService.save(entity);
    }

}
