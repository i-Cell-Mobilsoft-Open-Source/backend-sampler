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

import java.nio.charset.StandardCharsets;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.sample.kafka.service.mpreactive.KafkaMessageHandler;
import hu.icellmobilsoft.sampler.sample.kafka.service.mpreactive.KafkaMessageLogger;
import io.smallrye.reactive.messaging.MutinyEmitter;
import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;

/**
 * Sample kafka publisher <br>
 * <a href= "https://github.com/eclipse/microprofile-context-propagation">microprofile-context-propagation</a><br>
 * <a href= "https://smallrye.io/smallrye-reactive-messaging/smallrye-reactive-messaging/3.3/emitter/emitter.html">Emitter documentation</a>
 * 
 * @author Imre Scheffer
 */
@ApplicationScoped
public class KafkaPublisher extends BaseAction {

    @Inject
    private Logger log;

    @Inject
    private KafkaMessageLogger kafkaMessageLogger;

    @Inject
    private KafkaMessageHandler kafkaMessageHandler;

    // /**
    // * use {@code MutinyEmitter}
    // */
    // @Inject
    // @Channel("to-kafka")
    // private Emitter<String> emitterString;

    /**
     * send message imperative mode
     */
    @Inject
    @Channel("to-kafka")
    private MutinyEmitter<String> mutinyEmitterString;

    /**
     * Kafka Stream producer
     * 
     * @return message payload to send
     */
    // Endless message loop
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
        // Send message by system handled feature (not recommended)
        sendString(message);

        // Send message by smallrye specific header handling (working, experimental feature)
        sendMessageWithSmallryeMetadata(message);

        // Send message by smallrye specific metadata handling (not working, experimental feature)
        sendMessageWithMetadata(message);
    }

    private void sendString(String message) throws BaseException {
        String payloadString = message + "|String";
        // log.info("Sample Outgoing: [{0}]", payloadString);
        // waitForPublish(emitterString.send(payloadString));

        // By setting the producer timeout and using the imperative client, we can handle the case where if Kafka is not available, a message from the
        // buffer should not be sent in the case of a REST timeout
        try {
            mutinyEmitterString.sendAndAwait(payloadString);
        } catch (org.apache.kafka.common.errors.TimeoutException e) {
            throw new TechnicalException(CoffeeFaultType.OPERATION_FAILED, e.getLocalizedMessage(), e);
        } catch (Throwable e) {
            throw new TechnicalException(CoffeeFaultType.OPERATION_FAILED, e.getLocalizedMessage(), e);
        }
    }

    private void sendMessageWithSmallryeMetadata(String message) {
        Headers headers = new RecordHeaders();
        headers.add("header-1-okr", "value-1-okr".getBytes(StandardCharsets.UTF_8));
        OutgoingKafkaRecordMetadata<Object> okrMetadata = OutgoingKafkaRecordMetadata.builder().withHeaders(headers).build();
        String payloadOkr = message + "|okr";
        Message<String> okrMessage = KafkaMetadataUtil.writeOutgoingKafkaMetadata(Message.of(payloadOkr), okrMetadata);
        // for debug
        // okrMessage = kafkaMessageHandler.handleOutgoingMdc(okrMessage);
        // kafkaMessageLogger.printOutgoingMessage(okrMessage);
        mutinyEmitterString.sendMessageAndAwait(okrMessage);
    }

    private void sendMessageWithMetadata(String message) {
        Metadata metadata = Metadata.of(Map.of("header-2-meta", "value-2-meta"));
        String payloadMeta = message + "|meta";
        Message<String> metaMessage = Message.of(payloadMeta, metadata);
        // for debug
        // metaMessage = kafkaMessageHandler.handleOutgoingMdc(metaMessage);
        // kafkaMessageLogger.printOutgoingMessage(metaMessage);
        mutinyEmitterString.sendMessageAndAwait(metaMessage);
    }

    // private void waitForPublish(CompletionStage<Void> publishStage) throws TechnicalException {
    // try {
    // publishStage.toCompletableFuture().get();
    // } catch (InterruptedException ex) {
    // handleInterrupt(ex);
    // } catch (ExecutionException e) {
    // log.error("Exception occured", e);
    // throw new TechnicalException(CoffeeFaultType.OPERATION_FAILED, "Unkown Kafka publish error", e);
    // }
    // }

    // private void handleInterrupt(InterruptedException ex) {
    // log.warn("Interrupted sleep.", ex);
    // try {
    // Thread.currentThread().interrupt();
    // } catch (Exception e) {
    // log.warn("Exception during interrupt.", ex);
    // }
    // }
}
