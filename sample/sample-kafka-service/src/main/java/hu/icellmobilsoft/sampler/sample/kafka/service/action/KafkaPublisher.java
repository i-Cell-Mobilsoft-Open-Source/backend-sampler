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

import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.SampleKafkaDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Sample Kafka Publisher
 * 
 * @author Imre Scheffer
 */
@ApplicationScoped
public class KafkaPublisher extends BaseAction {

    private static final String CHANNEL_NAME = "to-kafka";
    @Inject
    private Logger log;

    @Inject
    @Channel(CHANNEL_NAME)
    private Emitter<ProducerRecord<Integer, SampleKafkaDto>> emitter;

    /**
     * Kafka Stream producer
     * 
     * @return message payload to send
     */
    // Ebben a formaban vegtelen uzenet keletkezik
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
    public void toKafka(SampleKafkaDto message) throws BaseException {
        log.info("Sample Outgoing: [{0}]", message);
        String topic = getTopic();
        waitForPublish(emitter.send(new ProducerRecord<>(topic, message)));
    }

    private String getTopic() {
        return ConfigProvider.getConfig().getConfigValue("mp.messaging.outgoing." + CHANNEL_NAME + ".topic").getValue();
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
