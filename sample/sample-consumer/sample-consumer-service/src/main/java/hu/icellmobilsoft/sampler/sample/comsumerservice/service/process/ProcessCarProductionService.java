/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2025 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.comsumerservice.service.process;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

import hu.icellmobilsoft.coffee.jpa.service.BaseService;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.validation.ParamValidatorUtil;
import hu.icellmobilsoft.sampler.sample.comsumer.model.CarProduction;
import hu.icellmobilsoft.sampler.sample.comsumer.model.CarProduction_;
import hu.icellmobilsoft.sampler.sample.comsumer.model.process.AbstractProcessCarProductionEntity;
import hu.icellmobilsoft.sampler.sample.comsumer.model.process.AbstractProcessCarProductionEntity_;

/**
 * Service class for {@link AbstractProcessCarProductionEntity} entity.
 *
 * @author attila.kiss
 * @since 2.0.0
 */
@Model
public class ProcessCarProductionService extends BaseService<AbstractProcessCarProductionEntity> {

    @Inject
    private EntityManager em;

    /**
     * Deletes the {@link AbstractProcessCarProductionEntity} by the {@link CarProduction#getId()} field.
     *
     * @param <T>
     *            the type of the process entity
     * @param processCarProductionEntityClass
     *            the class of the process entity to delete
     * @param carProductionId
     *            {@link CarProduction#getId()}
     * @return the number of deleted records
     * @throws BaseException
     *             in case of error
     */
    public <T extends AbstractProcessCarProductionEntity> int deleteByCarProductionId(Class<T> processCarProductionEntityClass,
            String carProductionId) throws BaseException {

        ParamValidatorUtil.requireNonNull(processCarProductionEntityClass, "processCarProductionEntityClass");
        ParamValidatorUtil.requireNonNull(carProductionId, "carProductionId");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<T> cd = cb.createCriteriaDelete(processCarProductionEntityClass);

        Root<T> processCarProductionEntity = cd.from(processCarProductionEntityClass);
        Path<String> carProductionIdPath = processCarProductionEntity.get(AbstractProcessCarProductionEntity_.carProduction).get(CarProduction_.id);
        cd.where(cb.equal(carProductionIdPath, carProductionId));

        Query query = em.createQuery(cd);
        return query.executeUpdate();

    }

}
