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

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.dto.common.LogConstants;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.se.logging.mdc.MDC;
import hu.icellmobilsoft.coffee.tool.utils.validation.ParamValidatorUtil;

/**
 * Event dispatcher for asynchronous redis stream publish operations.
 *
 * @author attila.kiss
 * @since 2.0.0
 *
 * @see AsyncRedisPublishEventObserver
 */
@Dependent
public class AsyncRedisPublishEventDispatcher {

    @Inject
    @ThisLogger
    private AppLogger log;

    @Inject
    private Event<AsyncRedisPublishEvent> event;

    /**
     * Fires an asynchronous CDI {@link Event} to publish a message to a redis stream.
     *
     * @param streamMessage
     *            the stream message to publish
     * @param redisConfigKey
     *            the configuration key of the connection to use
     * @param redisGroup
     *            the name of the stream group
     * @throws BaseException
     *             in case of error
     *
     * @see Event#fireAsync
     * @see AsyncRedisPublishEventObserver#observeAsyncRedisPublishEvent(AsyncRedisPublishEvent)
     */
    public void fireAsyncRedisPublishEvent(String streamMessage, String redisConfigKey, String redisGroup) throws BaseException {

        ParamValidatorUtil.requireNonBlank(streamMessage, "streamMessage");
        ParamValidatorUtil.requireNonBlank(redisConfigKey, "redisConfigKey");
        ParamValidatorUtil.requireNonBlank(redisGroup, "redisGroup");

        AsyncRedisPublishEvent asyncRedisPublishEvent = new AsyncRedisPublishEvent(
                streamMessage,
                redisConfigKey,
                redisGroup,
                MDC.get(LogConstants.LOG_SESSION_ID));

        event.fireAsync(asyncRedisPublishEvent);

        log.debug("Async redis publish event fired [{0}]!", asyncRedisPublishEvent);
    }
}
