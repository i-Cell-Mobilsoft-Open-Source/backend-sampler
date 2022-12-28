/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.redispubsub.service.consumer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.gson.JsonUtil;
import hu.icellmobilsoft.sampler.common.rest.cdi.ApplicationContainer;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.sample.redispubsub.service.config.RedisPubSubConfig;

/**
 * Listener a {@link hu.icellmobilsoft.sampler.sample.redispubsub.service.action.RedisPubSubSamplePostAction}-höz
 *
 * @author mark.petrenyi
 * @since 0.1.0
 */
@ApplicationScoped
public class PostInListener {

    @Inject
    private Logger log;


    @Inject
    private ApplicationContainer applicationContainer;

    /**
     * Consume message from {@value RedisPubSubConfig.SamplePost#MP_CHANNEL_IN}  micro profile channel
     * which connects to {@code sample-post} redis channel, saves message to {@link ApplicationContainer},
     * then forwards to {@code no-sub} redis channel.
     *
     * @param dummy the dummy message received from {@code sample-post} redis channel
     */
    @Incoming(RedisPubSubConfig.SamplePost.MP_CHANNEL_IN)
    @Outgoing(RedisPubSubConfig.NoSub.REDIS_CHANNEL)
    String consume(String dummy) {
        log.info(">> incoming dummy: [{0}]", dummy);
        applicationContainer.getObjectMap().put(RedisPubSubConfig.SamplePost.DUMMY_KEY, JsonUtil.toObject(dummy, SampleType.class));
        return dummy;
    }
}
