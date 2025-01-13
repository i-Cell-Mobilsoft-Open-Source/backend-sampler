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
package hu.icellmobilsoft.sampler.sample.comsumerservice.stream;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.module.redisstream.annotation.RedisStreamConsumer;
import hu.icellmobilsoft.coffee.module.redisstream.consumer.AbstractStreamConsumer;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.sample.comsumer.model.constant.RedisStreamConstant;
import hu.icellmobilsoft.sampler.sample.comsumerservice.action.PainterAction;

/**
 * Redis stream consumer responsible for the painting and finishing processes in car production.
 *
 * @author attila.kiss
 * @since 2.0.0
 */
@Model
@RedisStreamConsumer(configKey = RedisStreamConstant.CarProduction.Painter.CONFIG_KEY,
        group = RedisStreamConstant.CarProduction.Painter.GROUP)
public class PainterStreamConsumer extends AbstractStreamConsumer {

    // inject the action to invoke by consumer
    @Inject
    private PainterAction painterAction;

    // TODO this method should be protected
    @Override
    public void doWork(String text) throws BaseException {
        // invoke the action
        painterAction.paint(text);
    }

}
