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
import java.util.Iterator;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;

import hu.icellmobilsoft.coffee.dto.common.LogConstants;
import hu.icellmobilsoft.coffee.se.logging.mdc.MDC;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

/**
 * Listener for Incoming kafka message - header handling<br>
 * Usage with congiration:
 * 
 * <pre>
 *mp:
 *  messaging:
 *    incoming:
 *      __channel__:
 *        interceptor:
 *          classes:
 *            - hu.icellmobilsoft.sampler.sample.kafka.service.kafka.interceptor.IncomingHeaderInterceptor
 * </pre>
 * 
 * @param <K>
 *            Message Key Type (default String)
 * @param <V>
 *            Message Payload Type
 */
public class IncomingHeaderInterceptor<K, V> implements ConsumerInterceptor<K, V>, KafkaInterceptorConstant {

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public ConsumerRecords<K, V> onConsume(ConsumerRecords<K, V> records) {
        String sessionId = null;
        for (ConsumerRecord<K, V> record : records) {
            Iterator<Header> it = record.headers().headers(LogConstants.LOG_SESSION_ID).iterator();
            if (it.hasNext()) {
                sessionId = new String(it.next().value(), StandardCharsets.UTF_8);
                break;
            }
        }
        if (sessionId == null) {
            sessionId = RandomUtil.generateId();
        }
        Context vertxContext = Vertx.currentContext();
        // check existing vertx context
        if (vertxContext != null) {
            vertxContext.put(LogConstants.LOG_SESSION_ID, sessionId);
        }
        MDC.put(LogConstants.LOG_SESSION_ID, sessionId);
        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
    }

    @Override
    public void close() {
    }
}
