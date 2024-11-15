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
import hu.icellmobilsoft.coffee.tool.utils.enums.EnumUtil;
import hu.icellmobilsoft.frappee.jpa.batch.IJpaBatchService;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.batch.javaenumtypes.BaseJavaEnumTypesType;
import hu.icellmobilsoft.sampler.dto.sample.batch.javaenumtypes.BatchOperationTypeType;
import hu.icellmobilsoft.sampler.dto.sample.batch.javaenumtypes.JavaEnumTypesInsertRequest;
import hu.icellmobilsoft.sampler.dto.sample.batch.javaenumtypes.JavaEnumTypesInsertType;
import hu.icellmobilsoft.sampler.dto.sample.batch.javaenumtypes.JavaEnumTypesResponse;
import hu.icellmobilsoft.sampler.dto.sample.batch.javaenumtypes.JavaEnumTypesType;
import hu.icellmobilsoft.sampler.dto.sample.batch.javaenumtypes.JavaEnumTypesUpdateRequest;
import hu.icellmobilsoft.sampler.dto.sample.batch.javaenumtypes.JavaEnumTypesUpdateType;
import hu.icellmobilsoft.sampler.model.sample.batch.JavaEnumTypes;
import hu.icellmobilsoft.sampler.model.sample.enums.BatchOperationType;
import hu.icellmobilsoft.sampler.sample.jpa.batch.service.service.JavaEnumTypesService;

/**
 * Action class for {@link JavaEnumTypes}.
 *
 * @author csaba.balogh
 * @since 2.0.0
 */
@Model
public class JavaEnumTypesAction extends BaseAction {

    @Inject
    private IJpaBatchService jpaBatchService;

    @Inject
    private TransactionHelper transactionHelper;

    @Inject
    private JavaEnumTypesService javaEnumTypesService;

    /**
     * Creates and inserts a {@link JavaEnumTypes} entity with BatchService.
     *
     * @param javaEnumTypesInsertRequest
     *            {@link JavaEnumTypesInsertRequest}.
     * @return {@link JavaEnumTypesResponse}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    public JavaEnumTypesResponse insertJavaEnumTypes(JavaEnumTypesInsertRequest javaEnumTypesInsertRequest) throws BaseException {
        if (javaEnumTypesInsertRequest == null) {
            throw new InvalidParameterException("javaEnumTypesInsertRequest is NULL!");
        }

        JavaEnumTypesInsertType javaEnumTypesInsert = javaEnumTypesInsertRequest.getJavaEnumTypes();
        JavaEnumTypes javaEnumTypes = new JavaEnumTypes();
        fillJavaEnumTypes(javaEnumTypes, javaEnumTypesInsert);

        List<JavaEnumTypes> javaEnumTypesList = List.of(javaEnumTypes);

        transactionHelper.executeWithTransaction(jpaBatchService::batchInsertNative, javaEnumTypesList, JavaEnumTypes.class);

        return createResponse(javaEnumTypesInsertRequest, javaEnumTypes);
    }

    /**
     * Updates a {@link JavaEnumTypes} entity with BatchService.
     *
     * @param javaEnumTypesId
     *            ID of {@link JavaEnumTypes}.
     * @param javaEnumTypesUpdateRequest
     *            {@link JavaEnumTypesUpdateRequest}.
     * @return {@link JavaEnumTypesResponse}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    public JavaEnumTypesResponse updateJavaEnumTypes(String javaEnumTypesId, JavaEnumTypesUpdateRequest javaEnumTypesUpdateRequest)
            throws BaseException {
        if (StringUtils.isBlank(javaEnumTypesId)) {
            throw new InvalidParameterException("javaEnumTypesId is BLANK!");
        }
        if (javaEnumTypesUpdateRequest == null) {
            throw new InvalidParameterException("javaEnumTypesUpdateRequest is NULL!");
        }

        JavaEnumTypesUpdateType javaEnumTypesUpdate = javaEnumTypesUpdateRequest.getJavaEnumTypes();
        JavaEnumTypes javaEnumTypes = javaEnumTypesService.findById(javaEnumTypesId);
        fillJavaEnumTypes(javaEnumTypes, javaEnumTypesUpdate);

        List<JavaEnumTypes> javaEnumTypesList = List.of(javaEnumTypes);

        transactionHelper.executeWithTransaction(jpaBatchService::batchUpdateNative, javaEnumTypesList, JavaEnumTypes.class);

        return createResponse(javaEnumTypesUpdateRequest, javaEnumTypes);
    }

    /**
     * Deletes all {@link JavaEnumTypes} entities with BatchService.
     *
     * @return {@link BaseResponse}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    public BaseResponse deleteAllJavaEnumTypes() throws BaseException {
        List<JavaEnumTypes> javaEnumTypesList = javaEnumTypesService.findAll();
        transactionHelper.executeWithTransaction(jpaBatchService::batchDeleteNative, javaEnumTypesList, JavaEnumTypes.class);
        return createBaseResponse();
    }

    private void fillJavaEnumTypes(JavaEnumTypes javaEnumTypes, BaseJavaEnumTypesType baseJavaEnumTypes) {
        javaEnumTypes.setDefaultEnum(EnumUtil.convert(baseJavaEnumTypes.getDefaultEnum(), BatchOperationType.class));
        javaEnumTypes.setOrdinalEnum(EnumUtil.convert(baseJavaEnumTypes.getOrdinalEnum(), BatchOperationType.class));
        javaEnumTypes.setStringEnum(EnumUtil.convert(baseJavaEnumTypes.getStringEnum(), BatchOperationType.class));
    }

    private JavaEnumTypesResponse createResponse(BaseRequestType request, JavaEnumTypes javaEnumTypes) {
        JavaEnumTypesType javaEnumTypesType = new JavaEnumTypesType() //
                .withJavaEnumTypesId(javaEnumTypes.getId())
                .withDefaultEnum(EnumUtil.convert(javaEnumTypes.getDefaultEnum(), BatchOperationTypeType.class))
                .withOrdinalEnum(EnumUtil.convert(javaEnumTypes.getOrdinalEnum(), BatchOperationTypeType.class))
                .withStringEnum(EnumUtil.convert(javaEnumTypes.getStringEnum(), BatchOperationTypeType.class));

        JavaEnumTypesResponse response = new JavaEnumTypesResponse().withJavaEnumTypes(javaEnumTypesType);
        handleSuccessResultType(response, request);
        return response;
    }
}
