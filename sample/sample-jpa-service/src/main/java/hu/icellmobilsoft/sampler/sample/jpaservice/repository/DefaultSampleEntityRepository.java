/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2023 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.jpaservice.repository;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.criteria.CriteriaBuilder.Trimspec;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import org.apache.deltaspike.data.api.criteria.Criteria;
import org.apache.deltaspike.data.api.criteria.QuerySelection;

import hu.icellmobilsoft.sampler.model.sample.SampleEntity;

/**
 * @author Imre Scheffer
 *
 */
@Dependent
public class DefaultSampleEntityRepository implements SampleEntityRepository {

    @Override
    public SampleEntity findBy(String primaryKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<SampleEntity> findOptionalBy(String primaryKey) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<SampleEntity> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SampleEntity> findAll(int start, int max) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SampleEntity> findBy(SampleEntity example, SingularAttribute<SampleEntity, ?>... attributes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SampleEntity> findBy(SampleEntity example, int start, int max, SingularAttribute<SampleEntity, ?>... attributes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SampleEntity> findByLike(SampleEntity example, SingularAttribute<SampleEntity, ?>... attributes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SampleEntity> findByLike(SampleEntity example, int start, int max, SingularAttribute<SampleEntity, ?>... attributes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SampleEntity save(SampleEntity entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SampleEntity saveAndFlush(SampleEntity entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SampleEntity saveAndFlushAndRefresh(SampleEntity entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(SampleEntity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAndFlush(SampleEntity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void attachAndRemove(SampleEntity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh(SampleEntity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getPrimaryKey(SampleEntity example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long count() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long count(SampleEntity example, SingularAttribute<SampleEntity, ?>... attributes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long countLike(SampleEntity example, SingularAttribute<SampleEntity, ?>... attributes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Criteria<SampleEntity, SampleEntity> criteria() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> Criteria<T, T> where(Class<T> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> Criteria<T, T> where(Class<T> clazz, JoinType joinType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <X> QuerySelection<SampleEntity, X> attribute(SingularAttribute<? super SampleEntity, X> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <N extends Number> QuerySelection<SampleEntity, N> abs(SingularAttribute<? super SampleEntity, N> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <N extends Number> QuerySelection<SampleEntity, N> avg(SingularAttribute<? super SampleEntity, N> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, Long> count(SingularAttribute<? super SampleEntity, ?> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, Long> countDistinct(SingularAttribute<? super SampleEntity, ?> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <N extends Number> QuerySelection<SampleEntity, N> max(SingularAttribute<? super SampleEntity, N> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <N extends Number> QuerySelection<SampleEntity, N> min(SingularAttribute<? super SampleEntity, N> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <N extends Number> QuerySelection<SampleEntity, N> neg(SingularAttribute<? super SampleEntity, N> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <N extends Number> QuerySelection<SampleEntity, N> sum(SingularAttribute<? super SampleEntity, N> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, Integer> modulo(SingularAttribute<? super SampleEntity, Integer> attribute, Integer modulo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, String> upper(SingularAttribute<? super SampleEntity, String> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, String> lower(SingularAttribute<? super SampleEntity, String> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, String> substring(SingularAttribute<? super SampleEntity, String> attribute, int from) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, String> substring(SingularAttribute<? super SampleEntity, String> attribute, int from, int length) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, String> trim(SingularAttribute<? super SampleEntity, String> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, String> trim(Trimspec trimspec, SingularAttribute<? super SampleEntity, String> attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, Date> currDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, Time> currTime() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QuerySelection<SampleEntity, Timestamp> currTStamp() {
        // TODO Auto-generated method stub
        return null;
    }

}
