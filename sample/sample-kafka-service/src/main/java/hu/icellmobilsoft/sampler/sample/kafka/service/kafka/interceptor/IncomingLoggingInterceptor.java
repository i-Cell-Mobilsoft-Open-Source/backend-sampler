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

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;

import hu.icellmobilsoft.coffee.se.logging.Logger;

/**
 * Listener for Incoming kafka message - logging<br>
 * Usage with congiration:
 * 
 * <pre>
 *mp:
 *  messaging:
 *    incoming:
 *      __channel__:
 *        interceptor:
 *          classes:
 *            - hu.icellmobilsoft.sampler.sample.kafka.service.kafka.interceptor.IncomingLoggingInterceptor
 * </pre>
 * 
 * @param <K>
 *            Message Key Type (default String)
 * @param <V>
 *            Message Payload Type
 */
public class IncomingLoggingInterceptor<K, V> implements ConsumerInterceptor<K, V>, KafkaInterceptorConstant {

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public ConsumerRecords<K, V> onConsume(ConsumerRecords<K, V> records) {
        Logger log = Logger.getLogger(getClass());
        StringBuffer sb = new StringBuffer();
        sb.append("* Reactive Messaging Incoming").append(NEWLINE);
        for (ConsumerRecord<K, V> record : records) {
            printConsumerRecord(sb, record);
        }
        log.info(sb.toString());
        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
    }

    @Override
    public void close() {
    }

    private void printConsumerRecord(StringBuffer sb, ConsumerRecord<K, V> record) {
        sb.append(INCOMING).append("-- ConsumerRecord:").append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Key").append(VALUE).append(record.key()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- LeaderEpoch").append(VALUE).append(record.leaderEpoch()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Offset").append(VALUE).append(record.offset()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Partition").append(VALUE).append(record.partition()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- SerializedKeySize").append(VALUE).append(record.serializedKeySize()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- SerializedValueSize").append(VALUE).append(record.serializedValueSize()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Timestamp").append(VALUE).append(Instant.ofEpochMilli(record.timestamp()).toString())
                .append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- TimestampType").append(VALUE).append(record.timestampType()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Topic").append(VALUE).append(record.topic()).append(NEWLINE);
        sb.append(INCOMING).append(TAB).append("-- Headers:").append(NEWLINE);
        for (Header header : record.headers()) {
            sb.append(INCOMING).append(TAB).append(TAB).append(header.key()).append(VALUE).append(new String(header.value(), StandardCharsets.UTF_8))
                    .append(NEWLINE);
        }
        sb.append(INCOMING).append(TAB).append("-- Value").append(VALUE).append(record.value()).append(NEWLINE);
    }
}
