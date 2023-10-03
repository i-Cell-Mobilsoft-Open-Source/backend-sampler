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

import java.util.Optional;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import org.apache.kafka.common.header.Header;
import org.eclipse.microprofile.reactive.messaging.Message;

import hu.icellmobilsoft.coffee.rest.log.RequestResponseLogger;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;

/**
 * Kafka Message printer/logger
 * 
 * @author Imre Scheffer
 * @since 2.0.0
 */
@Dependent
public class KafkaMessageLogger {

    @Inject
    private Logger log;

    /**
     * Tab space
     */
    public static String TAB = "  ";
    /**
     * Tab space
     */
    public static String VALUE = ": ";

    /**
     * New line char
     */
    public static char NEWLINE = '\n';

    /**
     * Print Reactive Kafka message
     * 
     * @param <T>
     *            Type of message
     * @param message
     *            Income reactive message
     */
    public <T> void printIncomingMessage(Message<T> message) {
        StringBuffer sb = new StringBuffer();
        sb.append("* Reactive Messaging Incoming").append(NEWLINE);
        printMetadata(sb, message);
        Optional<IncomingKafkaRecordMetadata> ikrmOptional = message.getMetadata(IncomingKafkaRecordMetadata.class);
        if (ikrmOptional.isPresent()) {
            printIncomingKafkaRecordMetadata(sb, ikrmOptional.get());
        }
        printPayload(sb, message.getPayload());
        log.info(sb.toString());
    }

    /**
     * Print algorithm of message payload
     * 
     * @param <T>
     *            Type os message payload
     * @param sb
     *            Printing buffer
     * @param payload
     *            message payload
     */
    protected <T> void printPayload(StringBuffer sb, T payload) {
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(" Payload [" + payload + "]");
    }

    /**
     * Print algorithm of message common metadata
     * 
     * @param <T>
     *            Type os message payload
     * @param sb
     *            Printing buffer
     * @param message
     *            Income reactive message
     */
    protected <T> void printMetadata(StringBuffer sb, Message<T> message) {
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append("-- Metadata classes:").append(NEWLINE);
        for (Object item : message.getMetadata()) {
            sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append(item).append(NEWLINE);
        }
    }

    /**
     * Print algorithm of message Smallrye metadata
     * 
     * @param sb
     *            Printing buffer
     * @param metadata
     *            Smallrye specific metadata
     */
    protected void printIncomingKafkaRecordMetadata(StringBuffer sb, IncomingKafkaRecordMetadata<?, ?> metadata) {
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append("-- IncomingKafkaRecordMetadata:").append(NEWLINE);
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append("-- Channel").append(VALUE).append(metadata.getChannel()).append(NEWLINE);
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append("-- ConsumerIndex").append(VALUE).append(metadata.getConsumerIndex())
                .append(NEWLINE);
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append("-- Key").append(VALUE).append(metadata.getKey()).append(NEWLINE);
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append("-- Offset").append(VALUE).append(metadata.getOffset()).append(NEWLINE);
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append("-- Partition").append(VALUE).append(metadata.getPartition())
                .append(NEWLINE);
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append("-- Record").append(VALUE).append(metadata.getRecord()).append(NEWLINE);
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append("-- Timestamp").append(VALUE).append(metadata.getTimestamp())
                .append(NEWLINE);
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append("-- TimestampType").append(VALUE).append(metadata.getTimestampType())
                .append(NEWLINE);
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append("-- Topic").append(VALUE).append(metadata.getTopic()).append(NEWLINE);
        sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append("-- Headers:").append(NEWLINE);
        for (Header header : metadata.getHeaders()) {
            sb.append(RequestResponseLogger.REQUEST_PREFIX).append(TAB).append(header.key()).append(VALUE).append(header.value()).append(NEWLINE);
        }
    }
}
