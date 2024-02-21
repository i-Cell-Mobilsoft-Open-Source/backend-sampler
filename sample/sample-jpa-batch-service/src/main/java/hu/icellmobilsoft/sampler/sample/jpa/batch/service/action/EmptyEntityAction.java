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

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.dto.common.commonservice.BaseRequest;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.InvalidParameterException;
import hu.icellmobilsoft.coffee.jpa.annotation.Transactional;
import hu.icellmobilsoft.coffee.jpa.helper.TransactionHelper;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.batch.emptyentity.EmptyEntityResponse;
import hu.icellmobilsoft.sampler.dto.sample.batch.emptyentity.EmptyEntityType;
import hu.icellmobilsoft.sampler.model.sample.batch.EmptyEntity;
import hu.icellmobilsoft.sampler.sample.jpa.batch.service.service.EmptyEntityService;

/**
 * Action class for {@link EmptyEntity}.
 * 
 * @author csaba.balogh
 * @since 2.0.0
 */
@Model
public class EmptyEntityAction extends BaseAction {

    @Inject
    private TransactionHelper transactionHelper;

    @Inject
    private EmptyEntityService emptyEntityService;

    /**
     * Creates an {@link EmptyEntity}.
     * 
     * @param baseRequest
     *            {@link BaseRequest}.
     * @return {@link EmptyEntityResponse}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    @Transactional
    public EmptyEntityResponse createEmptyEntity(BaseRequest baseRequest) throws BaseException {
        if (baseRequest == null) {
            throw new InvalidParameterException("baseRequest is NULL!");
        }

        EmptyEntity emptyEntity = emptyEntityService.save(new EmptyEntity());

        EmptyEntityType emptyEntityType = new EmptyEntityType().withEmptyEntityId(emptyEntity.getId());

        EmptyEntityResponse response = new EmptyEntityResponse().withEmptyEntity(emptyEntityType);
        handleSuccessResultType(response, baseRequest);
        return response;
    }
}
