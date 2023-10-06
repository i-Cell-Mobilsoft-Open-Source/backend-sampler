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
package hu.icellmobilsoft.sampler.sample.kafka.service.mpreactive;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Optional;

import jakarta.enterprise.context.Dependent;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.eclipse.microprofile.reactive.messaging.Message;

import hu.icellmobilsoft.coffee.dto.common.LogConstants;
import hu.icellmobilsoft.coffee.se.logging.mdc.MDC;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.sample.kafka.service.kafka.interceptor.IncomingHeaderInterceptor;
import hu.icellmobilsoft.sampler.sample.kafka.service.kafka.interceptor.OutgoingHeaderInterceptor;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

/**
 * Kafka Message customization
 * 
 * @author Imre Scheffer
 * @since 2.0.0
 */
@Dependent
public class KafkaMessageHandler {

    /**
     * Read and set MDC value from message into thread constant<br>
     * This usage is recommended in developing, debugging and testing, in production is preferred {@link IncomingHeaderInterceptor}
     * 
     * @param <T>
     *            Message Payload Type
     * @param message
     *            Incoming reactive message
     */
    public <T> void handleIncomingMdc(Message<T> message) {
        Optional<IncomingKafkaRecordMetadata<?, ?>> ikrmOptional = message.getMetadata(IncomingKafkaRecordMetadata.class)
                .map(x -> (IncomingKafkaRecordMetadata<?, ?>) x);
        String sessionId = null;
        // smallrye kafka message must have this type metadata
        if (ikrmOptional.isPresent()) {
            Iterator<Header> it = ikrmOptional.get().getHeaders().headers(LogConstants.LOG_SESSION_ID).iterator();
            if (it.hasNext()) {
                sessionId = new String(it.next().value(), StandardCharsets.UTF_8);
            }
        }
        if (sessionId == null) {
            sessionId = RandomUtil.generateId();
        }
        Vertx.currentContext().put(LogConstants.LOG_SESSION_ID, sessionId);
        MDC.put(LogConstants.LOG_SESSION_ID, sessionId);
    }

    /**
     * Read and set MDC value from thread constant into message header.<br>
     * This usage is recommended in developing, debugging and testing, in production is preferred {@link OutgoingHeaderInterceptor}
     * 
     * @param <T>
     *            Message Payload Type
     * @param message
     *            Outgoing reactive message
     * @return input message copy with injected MDC
     */
    public <T> Message<T> handleOutgoingMdc(Message<T> message) {
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
            Optional<OutgoingKafkaRecordMetadata<?>> okrmOptional = message.getMetadata(OutgoingKafkaRecordMetadata.class)
                    .map(x -> (OutgoingKafkaRecordMetadata<?>) x);
            if (okrmOptional.isPresent()) {
                OutgoingKafkaRecordMetadata<?> metadata = okrmOptional.get();
                metadata.getHeaders().add(sessionIdHeader);
                return message;
            } else {
                Headers headers = new RecordHeaders();
                headers.add(sessionIdHeader);
                OutgoingKafkaRecordMetadata<Object> metadata = OutgoingKafkaRecordMetadata.builder().withHeaders(headers).build();
                return KafkaMetadataUtil.writeOutgoingKafkaMetadata(message, metadata);
            }
        }
        return message;
    }
}
