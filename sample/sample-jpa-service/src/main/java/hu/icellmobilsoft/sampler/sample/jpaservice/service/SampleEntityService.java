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

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.cdi.trace.annotation.Traced;
import hu.icellmobilsoft.coffee.dto.exception.InvalidParameterException;
import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.common.system.jpa.service.BaseService;
import hu.icellmobilsoft.sampler.model.sample.SampleEntity;
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
     * @param status sample status
     * @return entity
     * @throws BaseException on error
     */
    public List<SampleEntity> findAllByStatus(SampleStatus status) throws BaseException {
        return wrapListValidated(sampleEntityRepository::findAllByStatus, status, "findAllByStatus", "status");
    }

    /**
     * Find sample entity by id. It is for testing query method tracing.
     * <a href="https://github.com/i-Cell-Mobilsoft-Open-Source/coffee/issues/550">See coffee issue</a>
     *
     * @param id {@link SampleEntity#getId()}
     * @return entity
     * @throws BaseException on error
     */
    public SampleEntity findById(String id) throws BaseException {
        return wrapValidated(sampleEntityRepository::findById, id, "findById", "id");
    }

    /**
     * Find sample entity by id. It is for testing criteria query method.
     * @param id {@link SampleEntity#getId()}
     * @param clazz Entity class
     * @return {@link Optional}&lt;{@link SampleEntity}&gt;
     * @throws BaseException on error
     */
    @Traced
    public Optional<SampleEntity> findOptionalById(String id, Class<SampleEntity> clazz) throws BaseException {
        if (StringUtils.isBlank(id) || clazz == null) {
            log.warn("Entity Id is blank or clazz is null skipped to load!");
            throw new InvalidParameterException("id is blank or clazz is null!");
        }
        log.trace(">> BaseService.findOptionalById(id: [{0}], class: [{1}])", id, clazz.getCanonicalName());
        SampleEntity entity = null;
        try {
            EntityManager entityManager = getEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<SampleEntity> criteriaQuery = criteriaBuilder.createQuery(clazz);
            Root<SampleEntity> root = criteriaQuery.from(SampleEntity.class);
            criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
            TypedQuery<SampleEntity> query = entityManager.createQuery(criteriaQuery);
            List<SampleEntity> results = query.getResultList();
            if (!results.isEmpty()) {
                entity = results.get(0);
            } else {
                log.debug("No result, id: [{0}].", id);
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("Error occured in finding class: [{0}] by id: [{1}]: [{2}]", clazz.getCanonicalName(), id, e.getLocalizedMessage());
            log.error(msg, e);
            throw new TechnicalException(CoffeeFaultType.REPOSITORY_FAILED, msg, e);
        } finally {
            log.trace("<< BaseService.findOptionalById(id: [{0}], class: [{1}])", id, clazz.getCanonicalName());
        }
        return Optional.ofNullable(entity);
    }
}
