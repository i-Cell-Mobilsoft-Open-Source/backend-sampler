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
package hu.icellmobilsoft.sampler.dto.path;

/**
 * PATHS for jpa-batch-service
 * 
 * @author csaba.balogh
 * @since 2.0.0
 */
public interface JpaBatchServicePath {

    /**
     * /rest
     */
    String REST = "/rest";

    /**
     * /rest/jpaBatchService
     */
    String REST_JPA_BATCH_SERVICE = REST + "/jpaBatchService";

    /**
     * /rest/jpaBatchService/emptyEntity
     */
    String REST_JPA_BATCH_SERVICE_EMPTY_ENTITY = REST_JPA_BATCH_SERVICE + "/emptyEntity";

    /**
     * /rest/jpaBatchService/jpaAssociation
     */
    String REST_JPA_BATCH_SERVICE_JPA_ASSOCIATION = REST_JPA_BATCH_SERVICE + "/jpaAssociation";

    /**
     * /rest/jpaBatchService/javaData
     */
    String REST_JPA_BATCH_SERVICE_JAVA_DATA = REST_JPA_BATCH_SERVICE + "/javaData";

    /**
     * /rest/jpaBatchService/javaEnumTypes
     */
    String REST_JPA_BATCH_SERVICE_JAVA_ENUM_TYPES = REST_JPA_BATCH_SERVICE + "/javaEnumTypes";

    /**
     * /rest/jpaBatchService/javaBaseTypes
     */
    String REST_JPA_BATCH_SERVICE_JAVA_BASE_TYPES = REST_JPA_BATCH_SERVICE + "/javaBaseTypes";

    /**
     * /rest/jpaBatchService/javaDateAndTime
     */
    String REST_JPA_BATCH_SERVICE_JAVA_DATE_AND_TIME = REST_JPA_BATCH_SERVICE + "/javaDateAndTime";

    /**
     * /rest/jpaBatchService/jpaConverterEntity
     */
    String REST_JPA_BATCH_SERVICE_JPA_CONVERTER_ENTITY = REST_JPA_BATCH_SERVICE + "/jpaConverterEntity";

    /**
     * /insert
     */
    String INSERT = "/insert";

    /**
     * /update
     */
    String UPDATE = "/update";

    /**
     * /delete
     */
    String DELETE = "/delete";

    /**
     * jpaAssociationId
     */
    String PARAM_JPA_ASSOCIATION_ID = "jpaAssociationId";

    /**
     * /update/{jpaAssociationId}
     */
    String UPDATE_JPA_ASSOCIATION_ID = UPDATE + "/{" + PARAM_JPA_ASSOCIATION_ID + "}";

    /**
     * javaDataId
     */
    String PARAM_JAVA_DATA_ID = "javaDataId";

    /**
     * /update/{javaDataId}
     */
    String UPDATE_JAVA_DATA_ID = UPDATE + "/{" + PARAM_JAVA_DATA_ID + "}";

    /**
     * javaEnumTypesId
     */
    String PARAM_JAVA_ENUM_TYPES_ID = "javaEnumTypesId";

    /**
     * /update/{javaEnumTypesId}
     */
    String UPDATE_JAVA_ENUM_TYPES_ID = UPDATE + "/{" + PARAM_JAVA_ENUM_TYPES_ID + "}";

    /**
     * javaBaseTypesId
     */
    String PARAM_JAVA_BASE_TYPES_ID = "javaBaseTypesId";

    /**
     * /update/{javaBaseTypesId}
     */
    String UPDATE_JAVA_BASE_TYPES_ID = UPDATE + "/{" + PARAM_JAVA_BASE_TYPES_ID + "}";

    /**
     * javaDateAndTimeId
     */
    String PARAM_JAVA_DATE_AND_TIME_ID = "javaDateAndTimeId";

    /**
     * /update/{javaDateAndTimeId}
     */
    String UPDATE_JAVA_DATE_AND_TIME_ID = UPDATE + "/{" + PARAM_JAVA_DATE_AND_TIME_ID + "}";

    /**
     * jpaConverterEntityId
     */
    String PARAM_JPA_CONVERTER_ENTITY_ID = "jpaConverterEntityId";

    /**
     * /update/{jpaConverterEntityId}
     */
    String UPDATE_JPA_CONVERTER_ENTITY_ID = UPDATE + "/{" + PARAM_JPA_CONVERTER_ENTITY_ID + "}";
}
