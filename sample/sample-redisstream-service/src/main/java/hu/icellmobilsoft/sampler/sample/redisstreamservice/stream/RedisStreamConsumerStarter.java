/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2024 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.redisstreamservice.stream;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;

import hu.icellmobilsoft.coffee.module.redisstream.bootstrap.BaseRedisConsumerStarter;

/**
 * Start redis stream consuming
 * 
 * @author czenczl
 * @since 2.0.0
 */
@ApplicationScoped
public class RedisStreamConsumerStarter extends BaseRedisConsumerStarter {

    /**
     * CDI event observer
     * 
     * @param init
     *            CDI
     */
    public void begin(@Observes @Initialized(ApplicationScoped.class) Object init) {
        start();
    }
}
