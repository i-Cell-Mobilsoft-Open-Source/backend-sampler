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
package hu.icellmobilsoft.ts.sample.jpa.batch.service.java.date.and.time.rest;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import hu.icellmobilsoft.coffee.dto.common.commonservice.BaseResponse;
import hu.icellmobilsoft.coffee.dto.common.commonservice.FunctionCodeType;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.roaster.api.TestSuiteGroup;
import hu.icellmobilsoft.sampler.api.jee.batch.rest.IJavaDateAndTimeRest;
import hu.icellmobilsoft.ts.sample.jpa.batch.service.java.date.and.time.rest.base.BaseJavaDateAndTimeRestIT;

/**
 * IT tests for {@link IJavaDateAndTimeRest#deleteAllJavaDateAndTimeEntitiesWithBatchService}.
 *
 * @author csaba.balogh
 * @since 2.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testing JavaDateAndTimeRest.deleteAllJavaDateAndTimeEntitiesWithBatchService()")
@Tag(TestSuiteGroup.RESTASSURED)
class DeleteAllJavaDateAndTimeEntitiesWithBatchServiceIT extends BaseJavaDateAndTimeRestIT {

    @Test
    @Order(1)
    void deleteAllJavaDateAndTimeEntitiesWithBatchServiceTest() throws BaseException {
        // given
        createJavaDateAndTime(OffsetDateTime.now());

        // when
        BaseResponse response = getRestClient().deleteAllJavaDateAndTimeEntitiesWithBatchService();

        // then
        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode(), "functionCode");
    }
}