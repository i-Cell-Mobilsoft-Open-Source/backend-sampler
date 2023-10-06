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
import java.util.Optional;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import org.apache.kafka.common.header.Header;
import org.eclipse.microprofile.reactive.messaging.Message;

import hu.icellmobilsoft.coffee.se.logging.Logger;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;

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
     * Incoming row prefix
     */
    public static String INCOMING = ">> ";
    /**
     * Outgoing row prefix
     */
    public static String OUTGOING = "<< ";

    /**
     * Print Incoming Reactive Kafka message and handling MDC value for logging
     * 
     * @param <T>
     *            Message Payload Type
     * @param message
     *            Incoming reactive message
     */
    public <T> void printIncomingMessage(Message<T> message) {
        StringBuffer sb = new StringBuffer();
        sb.append("* Reactive Messaging Incoming").append(NEWLINE);
        printMetadata(INCOMING, sb, message);
        @SuppressWarnings("rawtypes")
        Optional<IncomingKafkaRecordMetadata> ikrmOptional = message.getMetadata(IncomingKafkaRecordMetadata.class);
        if (ikrmOptional.isPresent()) {
            printIncomingKafkaRecordMetadata(sb, ikrmOptional.get());
        }
        printPayload(INCOMING, sb, message.getPayload());
        log.info(sb.toString());
    }

    /**
     * Print Outgoing Reactive Kafka message
     * 
     * @param <T>
     *            Message Payload Type
     * @param message
     *            Outgoing reactive message
     */
    public <T> void printOutgoingMessage(Message<T> message) {
        StringBuffer sb = new StringBuffer();
        sb.append("* Reactive Messaging Outgoing").append(NEWLINE);
        printMetadata(OUTGOING, sb, message);
        @SuppressWarnings("rawtypes")
        Optional<OutgoingKafkaRecordMetadata> okrmOptional = message.getMetadata(OutgoingKafkaRecordMetadata.class);
        if (okrmOptional.isPresent()) {
            printOutgoingKafkaRecordMetadata(sb, okrmOptional.get());
        }
        printPayload(OUTGOING, sb, message.getPayload());
        log.info(sb.toString());
    }

    /**
     * Print algorithm of message payload
     * 
     * @param <T>
     *            Message Payload Type
     * @param rowPrefix
     *            prefix to print in every new row
     * @param sb
     *            Printing buffer
     * @param payload
     *            message payload
     */
    protected <T> void printPayload(String rowPrefix, StringBuffer sb, T payload) {
        sb.append(rowPrefix).append("-- Payload [" + payload + "]");
    }

    /**
     * Print algorithm of message common metadata
     * 
     * @param <T>
     *            Message Payload Type
     * @param rowPrefix
     *            prefix to print in every new row
     * @param sb
     *            Printing buffer
     * @param message
     *            Income reactive message
     */
    protected <T> void printMetadata(String rowPrefix, StringBuffer sb, Message<T> message) {
        sb.append(rowPrefix).append("-- Metadata:").append(NEWLINE);
        for (Object item : message.getMetadata()) {
            sb.append(rowPrefix).append(TAB).append(item).append(NEWLINE);
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
        sb.append(INCOMING).append("-- IncomingKafkaRecordMetadata:").append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Channel").append(VALUE).append(metadata.getChannel()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- ConsumerIndex").append(VALUE).append(metadata.getConsumerIndex()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Key").append(VALUE).append(metadata.getKey()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Offset").append(VALUE).append(metadata.getOffset()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Partition").append(VALUE).append(metadata.getPartition()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Record").append(VALUE).append(metadata.getRecord()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Timestamp").append(VALUE).append(metadata.getTimestamp()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- TimestampType").append(VALUE).append(metadata.getTimestampType()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Topic").append(VALUE).append(metadata.getTopic()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Headers:").append(NEWLINE);
        for (Header header : metadata.getHeaders()) {
            sb.append(INCOMING).append(TAB).append(TAB).append(header.key()).append(VALUE).append(new String(header.value(), StandardCharsets.UTF_8))
                    .append(NEWLINE);
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
    protected void printOutgoingKafkaRecordMetadata(StringBuffer sb, OutgoingKafkaRecordMetadata<?> metadata) {
        sb.append(OUTGOING).append("-- OutgoingKafkaRecordMetadata:").append(NEWLINE);
        sb.append(OUTGOING).append(TAB).append("-- Key").append(VALUE).append(metadata.getKey()).append(NEWLINE);
        sb.append(OUTGOING).append(TAB).append("-- Partition").append(VALUE).append(metadata.getPartition()).append(NEWLINE);
        sb.append(OUTGOING).append(TAB).append("-- Timestamp").append(VALUE).append(metadata.getTimestamp()).append(NEWLINE);
        sb.append(OUTGOING).append(TAB).append("-- Topic").append(VALUE).append(metadata.getTopic()).append(NEWLINE);
        sb.append(OUTGOING).append(TAB).append("-- Headers:").append(NEWLINE);
        for (Header header : metadata.getHeaders()) {
            sb.append(OUTGOING).append(TAB).append(TAB).append(header.key()).append(VALUE).append(new String(header.value(), StandardCharsets.UTF_8))
                    .append(NEWLINE);
        }
    }
}
