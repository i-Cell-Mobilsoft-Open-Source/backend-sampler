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

import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.common.rest.cdi.ApplicationContainer;
import hu.icellmobilsoft.sampler.sample.redispubsub.service.config.RedisPubSubConfig;

/**
 * Listener a {@link hu.icellmobilsoft.sampler.sample.redispubsub.service.action.RedisPubSubSampleGetAction}-höz
 *
 * @author mark.petrenyi
 * @since 0.1.0
 */
@ApplicationScoped
public class GetListener {

    @Inject
    private Logger log;


    @Inject
    private ApplicationContainer applicationContainer;

    /**
     * Consume message from {@value RedisPubSubConfig.SampleGet#REDIS_CHANNEL} channel.
     *
     * @param dummy the dummy message from {@code sample-get} redis channel
     */
    @Incoming(RedisPubSubConfig.SampleGet.REDIS_CHANNEL)
    void consume(String dummy) {
        log.info(">> incoming dummy: [{0}]", dummy);
        applicationContainer.getObjectMap().put(RedisPubSubConfig.SampleGet.DUMMY_KEY, dummy);
    }
}
