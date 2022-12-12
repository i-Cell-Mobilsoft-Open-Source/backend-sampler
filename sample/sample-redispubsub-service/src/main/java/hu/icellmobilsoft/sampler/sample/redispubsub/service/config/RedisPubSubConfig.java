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

    public interface SamplePost{
        String MP_CHANNEL_OUT = "post-out";
        String MP_CHANNEL_IN = "post-in";

        String DUMMY_KEY = "post-dummy";

    }

    public interface NoSub{
        String REDIS_CHANNEL = "no-sub";
    }

    public interface SampleGet{
        String REDIS_CHANNEL = "sample-get";

        String DUMMY_KEY = "get-dummy";
    }
}
