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
package hu.icellmobilsoft.sampler.sample.kafka.service.kafka.interceptor;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;

import hu.icellmobilsoft.coffee.se.logging.Logger;

/**
 * Listener for Outgoing kafka message - logging.<br>
 * Usage with congiration:
 * 
 * <pre>
 *mp:
 *  messaging:
 *    outgoing:
 *      __channel__:
 *        interceptor:
 *          classes:
 *            - hu.icellmobilsoft.sampler.sample.kafka.service.kafka.interceptor.OutgoingLoggingInterceptor
 * </pre>
 * 
 * @param <K>
 *            Message Key Type (default String)
 * @param <V>
 *            Message Payload Type
 */
public class OutgoingLoggingInterceptor<K, V> implements ProducerInterceptor<K, V>, KafkaInterceptorConstant {

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        Logger log = Logger.getLogger(getClass());
        StringBuffer sb = new StringBuffer();
        sb.append("* Reactive Messaging Outgoing").append(NEWLINE);
        printProducerRecord(sb, record);
        log.info(sb.toString());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    }

    @Override
    public void close() {
    }

    private void printProducerRecord(StringBuffer sb, ProducerRecord<K, V> record) {
        sb.append(OUTGOING).append("-- ProducerRecord:").append(NEWLINE);
        sb.append(OUTGOING).append(TAB).append("-- Key").append(VALUE).append(record.key()).append(NEWLINE);
        sb.append(OUTGOING).append(TAB).append("-- Partition").append(VALUE).append(record.partition()).append(NEWLINE);
        String timestampString = record.timestamp() != null ? Instant.ofEpochMilli(record.timestamp()).toString() : null;
        sb.append(OUTGOING).append(TAB).append("-- Timestamp").append(VALUE).append(timestampString).append(NEWLINE);
        sb.append(OUTGOING).append(TAB).append("-- Topic").append(VALUE).append(record.topic()).append(NEWLINE);
        sb.append(OUTGOING).append(TAB).append("-- Headers:").append(NEWLINE);
        for (Header header : record.headers()) {
            sb.append(OUTGOING).append(TAB).append(TAB).append(header.key()).append(VALUE).append(new String(header.value(), StandardCharsets.UTF_8))
                    .append(NEWLINE);
        }
        sb.append(OUTGOING).append(TAB).append("-- Value").append(VALUE).append(record.value()).append(NEWLINE);
    }
}
