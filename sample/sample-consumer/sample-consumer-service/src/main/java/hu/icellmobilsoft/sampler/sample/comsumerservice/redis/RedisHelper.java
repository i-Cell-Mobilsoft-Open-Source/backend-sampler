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
package hu.icellmobilsoft.sampler.sample.comsumerservice.redis;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.spi.CDI;

import hu.icellmobilsoft.coffee.module.redisstream.annotation.RedisStreamProducer;
import hu.icellmobilsoft.coffee.module.redisstream.config.StreamMessageParameter;
import hu.icellmobilsoft.coffee.module.redisstream.publisher.RedisStreamPublisher;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.validation.ParamValidatorUtil;

/**
 * Helper for redis operations.
 *
 * @author attila.kiss
 * @since 2.0.0
 */
@Model
public class RedisHelper {

    /**
     * Publish the stream message to the stream group on connection defined by the connection config key.
     *
     * @param streamMessage
     *            the stream message to publish
     * @param redisConfigKey
     *            the configuration key of the connection to use
     * @param redisGroup
     *            the name of the stream group
     * @throws BaseException
     *             in case of error
     */
    public void publish(String streamMessage, String redisConfigKey, String redisGroup) throws BaseException {

        ParamValidatorUtil.requireNonBlank(streamMessage, "streamMessage");
        ParamValidatorUtil.requireNonBlank(redisConfigKey, "redisConfigKey");
        ParamValidatorUtil.requireNonBlank(redisGroup, "redisGroup");

        RedisStreamPublisher redisStreamPublisher = CDI.current()
                .select(RedisStreamPublisher.class, new RedisStreamProducer.Literal(redisConfigKey, redisGroup))
                .get();

        long ttl = Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli();

        redisStreamPublisher.publish(streamMessage, Map.ofEntries(RedisStreamPublisher.parameterOf(StreamMessageParameter.TTL, ttl)));
    }

}
