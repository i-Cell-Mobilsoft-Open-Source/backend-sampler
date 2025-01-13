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
package hu.icellmobilsoft.sampler.sample.comsumer.model.constant;

/**
 * Redis stream configuration constants.
 *
 * @author attila.kiss
 * @since 2.0.0
 */
public interface RedisStreamConstant {

    /**
     * Car production redis connection configuraiton.
     */
    interface CarProductionConnection {

        /**
         * Car production redis connection config key.
         */
        String CONFIG_KEY = "carProductionStream";

    }

    /**
     * Car production redis stream configuration.
     */
    interface CarProduction {

        /**
         * Painter process.
         */
        interface Painter extends CarProductionConnection {

            /**
             * Painter process redis stream group name.
             */
            String GROUP = "painter";
        }

        /**
         * Assembler process.
         */
        interface Assembler extends CarProductionConnection {

            /**
             * Assembler process redis stream group name.
             */
            String GROUP = "assembler";
        }
    }
}
