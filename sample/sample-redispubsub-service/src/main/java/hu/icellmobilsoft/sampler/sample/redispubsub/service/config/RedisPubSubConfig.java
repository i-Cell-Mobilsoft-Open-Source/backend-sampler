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
package hu.icellmobilsoft.sampler.sample.redispubsub.service.config;

/**
 * Redis konfig kulcsok gyűjtője
 *
 * @author mark.petrenyi
 * @since 0.1.0
 */
public interface RedisPubSubConfig {

    /**
     * Mp reactive redis connector keys for {@link hu.icellmobilsoft.sampler.sample.redispubsub.service.action.RedisPubSubSamplePostAction} and {@link hu.icellmobilsoft.sampler.sample.redispubsub.service.consumer.PostInListener}
     */
    interface SamplePost {
        /**
         * Outgoing mp reactive stream, configured via {@code mp.messaging.outgoing.post-out} key
         */
        String MP_CHANNEL_OUT = "post-out";
        /**
         * Incoming mp reactive stream, configured via {@code mp.messaging.incoming.post-in} key
         */
        String MP_CHANNEL_IN = "post-in";

        /**
         * Key to store last incoming message in {@link hu.icellmobilsoft.sampler.common.rest.cdi.ApplicationContainer}
         */
        String DUMMY_KEY = "post-dummy";

    }

    /**
     * Constants for redis channel without subscribers
     */
    public interface NoSub {
        /**
         * Redis channel
         */
        String REDIS_CHANNEL = "no-sub";
    }

    /**
     * Mp reactive redis connector keys for {@link hu.icellmobilsoft.sampler.sample.redispubsub.service.action.RedisPubSubSampleGetAction} and {@link hu.icellmobilsoft.sampler.sample.redispubsub.service.consumer.GetListener}
     */
    public interface SampleGet {
        /**
         * Incoming mp reactive stream, configured via {@code mp.messaging.incoming.sample-get} key, also the name of the redis channel it connects to.
         */
        String REDIS_CHANNEL = "sample-get";

        /**
         * Key to store last incoming message in {@link hu.icellmobilsoft.sampler.common.rest.cdi.ApplicationContainer}
         */
        String DUMMY_KEY = "get-dummy";
    }
}
