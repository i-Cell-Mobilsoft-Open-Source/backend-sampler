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
package hu.icellmobilsoft.sampler.sample.kafka.service.action;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;

/**
 * Sample Kafka Publisher
 * 
 * @author Imre Scheffer
 */
@ApplicationScoped
public class KafkaPublisher extends BaseAction {

    @Inject
    private Logger log;

    @Inject
    @Channel("to-kafka")
    private Emitter<String> emitter;

    /**
     * Kafka Stream producer
     * 
     * @return message payload to send
     */
    // @Outgoing("to-kafka")
    public String toKafkaOutgoing() {
        String message = "sample";
        log.info("Sample Outgoing: [{0}]", message);
        return message;
    }

    /**
     * Send message to kafka
     * 
     * @param message
     *            message payload to publis
     * @throws BaseException
     *             error
     */
    public void toKafka(String message) throws BaseException {
        log.info("Sample Outgoing: [{0}]", message);
        waitForPublish(emitter.send(message));
    }

    private void waitForPublish(CompletionStage<Void> publishStage) throws TechnicalException {
        try {
            publishStage.toCompletableFuture().get();
        } catch (InterruptedException ex) {
            handleInterrupt(ex);
        } catch (ExecutionException e) {
            log.error("Exception occured", e);
            throw new TechnicalException(CoffeeFaultType.OPERATION_FAILED, "Unkown Kafka publish error", e);
        }
    }

    private void handleInterrupt(InterruptedException ex) {
        log.warn("Interrupted sleep.", ex);
        try {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.warn("Exception during interrupt.", ex);
        }
    }
}
