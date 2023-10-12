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
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;

import hu.icellmobilsoft.coffee.dto.common.LogConstants;
import hu.icellmobilsoft.coffee.se.logging.mdc.MDC;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

/**
 * Listener for Outgoing kafka message - header handling<br>
 * Usage with congiration:
 * 
 * <pre>
 *mp:
 *  messaging:
 *    outgoing:
 *      __channel__:
 *        interceptor:
 *          classes:
 *            - hu.icellmobilsoft.sampler.sample.kafka.service.kafka.interceptor.OutgoingHeaderInterceptor
 * </pre>
 * 
 * @param <K>
 *            Message Key Type (default String)
 * @param <V>
 *            Message Payload Type
 */
public class OutgoingHeaderInterceptor<K, V> implements ProducerInterceptor<K, V>, KafkaInterceptorConstant {

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        String sessionId = null;
        Context vertxContext = Vertx.currentContext();
        // check first vertx context, maybe is setted
        if (vertxContext != null) {
            sessionId = vertxContext.get(LogConstants.LOG_SESSION_ID);
        }
        // current thread MDC read
        if (sessionId == null) {
            sessionId = MDC.get(LogConstants.LOG_SESSION_ID);
        }
        // if found sessionId value set it to header
        if (sessionId != null) {
            Header sessionIdHeader = new RecordHeader(LogConstants.LOG_SESSION_ID, sessionId.getBytes(StandardCharsets.UTF_8));
            record.headers().add(sessionIdHeader);
        }
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    }

    @Override
    public void close() {
    }
}
