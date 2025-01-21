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
package hu.icellmobilsoft.sampler.sample.comsumerservice.action;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.jpa.helper.TransactionHelper;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.validation.ParamValidatorUtil;
import hu.icellmobilsoft.sampler.sample.comsumer.model.CarProduction;
import hu.icellmobilsoft.sampler.sample.comsumer.model.constant.RedisStreamConstant;
import hu.icellmobilsoft.sampler.sample.comsumer.model.enums.ProductionStatus;
import hu.icellmobilsoft.sampler.sample.comsumer.model.process.ProcessCarPaintedToAssembled;
import hu.icellmobilsoft.sampler.sample.comsumer.model.process.ProcessCarWeldedToPainted;
import hu.icellmobilsoft.sampler.sample.comsumerservice.redis.async.AsyncRedisPublishEventDispatcher;
import hu.icellmobilsoft.sampler.sample.comsumerservice.rest.ICarOrderRestClient;
import hu.icellmobilsoft.sampler.sample.comsumerservice.service.CarProductionService;
import hu.icellmobilsoft.sampler.sample.comsumerservice.service.process.ProcessCarProductionService;

/**
 * Action responsible for painting and finishing processes.
 *
 * @author attila.kiss
 * @since 2.0.0
 */
@Model
public class PainterAction {

    private static final ProductionStatus OUTGOING_STATUS = ProductionStatus.PAINTED;

    @Inject
    @ThisLogger
    private AppLogger logger;

    @Inject
    private CarProductionService carProductionService;

    @Inject
    private ProcessCarProductionService processCarProductionService;

    @Inject
    private ICarOrderRestClient carOrderRestClient;

    @Inject
    private TransactionHelper transactionHelper;

    @Inject
    private AsyncRedisPublishEventDispatcher asyncRedisPublishEventDispatcher;

    /**
     * Painting and finishing business process.
     *
     * @param carProductionId
     *            {@link CarProduction#getId()}
     * @throws BaseException
     *             in case of error
     */
    public void paint(String carProductionId) throws BaseException {

        // input parameter validation
        ParamValidatorUtil.requireNonBlank(carProductionId, "carProductionId");

        // ===========================================
        // ===== STEP-1: INITIALIZE AND VALIDATE =====
        // ===========================================

        // data query for validation
        CarProduction carProduction = carProductionService.findById(carProductionId, CarProduction.class);

        ProductionStatus actualStatus = carProduction.getProductionStatus();

        // validate the data based on the state the consumer accepts for processing
        if (actualStatus != ProductionStatus.WELDED) {
            logger.warn(MessageFormat.format("Unsupported car production [{0}] status [{1}]!", carProductionId, actualStatus));
            return;
        }

        // ... more initialization and validation

        // ============================================
        // ===== STEP-2: COLLECT AND PROCESS DATA =====
        // ============================================

        // prepare the stream message of the next consumer

        // REST call for external data query
        String color = carOrderRestClient.getColor("carOrderId");

        // business logic and calculations
        List<String> layers = new ArrayList<>();
        layers.add("primer");
        layers.add(color);
        layers.add("varnish");

        // ... more business logic

        // entity field modifications based on the business logic
        carProduction.setLayers(layers.stream().collect(Collectors.joining("-")));
        carProduction.setColor(color);
        // set the outgoing status
        carProduction.setProductionStatus(OUTGOING_STATUS);

        // create the process record of the next consumer
        ProcessCarPaintedToAssembled processCarPaintedToAssembled = new ProcessCarPaintedToAssembled();
        processCarPaintedToAssembled.setCarProduction(carProduction);
        processCarPaintedToAssembled.setStreamMessage(carProduction.getId());

        // ... create and modify entities

        // ====================================================
        // ===== STEP-3: SAVE ALL DATA IN ONE TRANSACTION =====
        // ====================================================

        transactionHelper.executeWithTransaction(() -> {

            // !!! DO ONLY DATABASE OPERATIONS ON THE PREPARED ENTITIES !!!

            // delete the process record of the actual process
            processCarProductionService.deleteByCarProductionId(ProcessCarWeldedToPainted.class, carProduction.getId());

            // insert the process record of the next process
            processCarProductionService.save(processCarPaintedToAssembled);

            // update the business record
            carProductionService.save(carProduction);

            // ... more database operations
        });

        // =========================================
        // ===== STEP-4: PUBLISH NEXT EVENT(S) =====
        // =========================================

        // fire an async event to publish the stream message to the next redis stream
        // the consumer execution is successful either if the redish operation fails, the failover solution will retry it later
        asyncRedisPublishEventDispatcher.fireAsyncRedisPublishEvent(
                processCarPaintedToAssembled.getStreamMessage(),
                RedisStreamConstant.CarProduction.Assembler.CONFIG_KEY,
                RedisStreamConstant.CarProduction.Assembler.GROUP);
    }

}
