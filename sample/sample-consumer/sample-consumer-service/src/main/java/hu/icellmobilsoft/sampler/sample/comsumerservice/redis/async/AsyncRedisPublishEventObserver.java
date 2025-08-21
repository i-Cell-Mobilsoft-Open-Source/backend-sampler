/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2025 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.comsumerservice.redis.async;

import java.text.MessageFormat;
import java.util.Objects;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.dto.common.LogConstants;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.se.logging.mdc.MDC;
import hu.icellmobilsoft.sampler.sample.comsumerservice.redis.RedisHelper;

/**
 * Observer of the {@link AsyncRedisPublishEvent} CDI {@link Event}.
 *
 * @author attila.kiss
 * @since 2.0.0
 */
@Model
public class AsyncRedisPublishEventObserver {

    @Inject
    private Logger log;

    @Inject
    private RedisHelper redisHelper;

    /**
     * Observes the {@link AsyncRedisPublishEvent} CDI {@link Event} and publish the stream message to the redis stream.
     *
     * @param asyncRedisPublishEvent
     *            {@link AsyncRedisPublishEvent}
     */
    public void observeAsyncRedisPublishEvent(@ObservesAsync AsyncRedisPublishEvent asyncRedisPublishEvent) {

        if (Objects.isNull(asyncRedisPublishEvent)) {
            return;
        }

        try {

            MDC.put(LogConstants.LOG_SESSION_ID, asyncRedisPublishEvent.extSessionId);

            redisHelper.publish(
                    asyncRedisPublishEvent.streamMessage,
                    asyncRedisPublishEvent.redisConfigKey,
                    asyncRedisPublishEvent.redisGroup);

            log.debug(MessageFormat.format("Async redis publish to {0} stream was successful!", asyncRedisPublishEvent.redisGroup));

        } catch (BaseException e) {
            log.error(MessageFormat.format("Async redis publish to {0} stream failed!", asyncRedisPublishEvent.redisGroup));
        } finally {
            MDC.clear();
        }
    }

}
