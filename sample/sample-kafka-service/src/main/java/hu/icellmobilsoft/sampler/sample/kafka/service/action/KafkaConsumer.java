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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.sample.kafka.service.mpreactive.KafkaMessageHandler;
import hu.icellmobilsoft.sampler.sample.kafka.service.mpreactive.KafkaMessageLogger;

/**
 * Sample Kafka Consumer
 * 
 * @author Imre Scheffer
 */
@ApplicationScoped
public class KafkaConsumer extends BaseAction {

    @Inject
    private Logger log;

    @Inject
    private KafkaMessageLogger kafkaMessageLogger;

    @Inject
    private KafkaMessageHandler kafkaMessageHandler;

    /**
     * Kafka Stream consumer
     * 
     * @param message
     *            message payload
     */
    // @Incoming("from-kafka")
    public void fromKafka(String message) {
        log.info("Sample Incoming: [{0}]", message);
    }

    /**
     * Kafka Stream consumer
     * 
     * @param message
     *            incoming reactive message
     * @return computation stage
     */
    @Incoming("from-kafka")
    public CompletionStage<Void> fromKafka(Message<String> message) {
        // for debug
        // kafkaMessageHandler.handleIncomingMdc(message);
        // kafkaMessageLogger.printIncomingMessage(message);
        return message.ack();
    }
}
