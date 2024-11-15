/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2024 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.jpa.batch.service.action;

import java.util.List;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import hu.icellmobilsoft.coffee.dto.common.commonservice.BaseRequestType;
import hu.icellmobilsoft.coffee.dto.common.commonservice.BaseResponse;
import hu.icellmobilsoft.coffee.dto.exception.InvalidParameterException;
import hu.icellmobilsoft.coffee.jpa.helper.TransactionHelper;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.frappee.jpa.batch.IJpaBatchService;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.batch.jpaassociation.JpaAssociationInsertRequest;
import hu.icellmobilsoft.sampler.dto.sample.batch.jpaassociation.JpaAssociationResponse;
import hu.icellmobilsoft.sampler.dto.sample.batch.jpaassociation.JpaAssociationType;
import hu.icellmobilsoft.sampler.dto.sample.batch.jpaassociation.JpaAssociationUpdateRequest;
import hu.icellmobilsoft.sampler.model.sample.batch.EmptyEntity;
import hu.icellmobilsoft.sampler.model.sample.batch.JpaAssociation;
import hu.icellmobilsoft.sampler.sample.jpa.batch.service.service.EmptyEntityService;
import hu.icellmobilsoft.sampler.sample.jpa.batch.service.service.JpaAssociationService;

/**
 * Action class for {@link JpaAssociation}.
 *
 * @author csaba.balogh
 * @since 2.0.0
 */
@Model
public class JpaAssociationAction extends BaseAction {

    @Inject
    private IJpaBatchService jpaBatchService;

    @Inject
    private TransactionHelper transactionHelper;

    @Inject
    private EmptyEntityService emptyEntityService;

    @Inject
    private JpaAssociationService jpaAssociationService;

    /**
     * Creates and inserts a {@link JpaAssociation} entity with BatchService.
     *
     * @param jpaAssociationInsertRequest
     *            {@link JpaAssociationInsertRequest}.
     * @return {@link JpaAssociationResponse}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    public JpaAssociationResponse insertJpaAssociation(JpaAssociationInsertRequest jpaAssociationInsertRequest) throws BaseException {
        if (jpaAssociationInsertRequest == null) {
            throw new InvalidParameterException("jpaAssociationInsertRequest is NULL!");
        }

        String manyToOneId = jpaAssociationInsertRequest.getJpaAssociation().getManyToOneId();
        EmptyEntity emptyEntity = getEmptyEntity(manyToOneId);

        JpaAssociation jpaAssociation = new JpaAssociation();
        jpaAssociation.setManyToOne(emptyEntity);

        List<JpaAssociation> jpaAssociationList = List.of(jpaAssociation);

        transactionHelper.executeWithTransaction(jpaBatchService::batchInsertNative, jpaAssociationList, JpaAssociation.class);

        return createResponse(jpaAssociationInsertRequest, jpaAssociation.getId(), manyToOneId);
    }

    /**
     * Updates a {@link JpaAssociation} entity with BatchService.
     *
     * @param jpaAssociationId
     *            ID of {@link JpaAssociation}.
     * @param jpaAssociationUpdateRequest
     *            {@link JpaAssociationUpdateRequest}.
     * @return {@link JpaAssociationResponse}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    public JpaAssociationResponse updateJpaAssociation(String jpaAssociationId, JpaAssociationUpdateRequest jpaAssociationUpdateRequest)
            throws BaseException {
        if (StringUtils.isBlank(jpaAssociationId)) {
            throw new InvalidParameterException("jpaAssociationId is BLANK!");
        }
        if (jpaAssociationUpdateRequest == null) {
            throw new InvalidParameterException("jpaAssociationUpdateRequest is NULL!");
        }

        String manyToOneId = jpaAssociationUpdateRequest.getJpaAssociation().getManyToOneId();
        EmptyEntity emptyEntity = getEmptyEntity(manyToOneId);

        JpaAssociation jpaAssociation = jpaAssociationService.findById(jpaAssociationId);
        jpaAssociation.setManyToOne(emptyEntity);

        List<JpaAssociation> jpaAssociationList = List.of(jpaAssociation);

        transactionHelper.executeWithTransaction(jpaBatchService::batchUpdateNative, jpaAssociationList, JpaAssociation.class);

        return createResponse(jpaAssociationUpdateRequest, jpaAssociationId, manyToOneId);
    }

    /**
     * Deletes all {@link JpaAssociation} entities with BatchService.
     *
     * @return {@link BaseResponse}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    public BaseResponse deleteAllJpaAssociation() throws BaseException {
        List<JpaAssociation> jpaAssociationList = jpaAssociationService.findAll();
        transactionHelper.executeWithTransaction(jpaBatchService::batchDeleteNative, jpaAssociationList, JpaAssociation.class);
        return createBaseResponse();
    }

    private EmptyEntity getEmptyEntity(String emptyEntityId) throws BaseException {
        if (StringUtils.isBlank(emptyEntityId)) {
            return null;
        }
        return emptyEntityService.findById(emptyEntityId);
    }

    private JpaAssociationResponse createResponse(BaseRequestType request, String jpaAssociationId, String manyToOneId) {
        JpaAssociationType jpaAssociation = new JpaAssociationType().withJpaAssociationId(jpaAssociationId).withManyToOneId(manyToOneId);
        JpaAssociationResponse response = new JpaAssociationResponse().withJpaAssociation(jpaAssociation);
        handleSuccessResultType(response, request);
        return response;
    }
}
