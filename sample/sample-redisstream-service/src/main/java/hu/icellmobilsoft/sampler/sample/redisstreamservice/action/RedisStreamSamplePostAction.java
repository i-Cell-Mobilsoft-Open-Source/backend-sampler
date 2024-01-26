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
package hu.icellmobilsoft.sampler.sample.redisstreamservice.action;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.module.redisstream.annotation.RedisStreamProducer;
import hu.icellmobilsoft.coffee.module.redisstream.publisher.RedisStreamPublisher;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;
import hu.icellmobilsoft.sampler.sample.redisstreamservice.config.RedisStreamConfig;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

/**
 * Sample post action to publish message
 *
 * @author czenczl
 * @since 2.0.0
 */
@Model
public class RedisStreamSamplePostAction extends BaseAction {

    @Inject
    private Logger log;

    @Inject
    @RedisStreamProducer(configKey = RedisStreamConfig.REDIS_KEY, group = RedisStreamConfig.REDIS_KEY)
    private RedisStreamPublisher publusher;

    /**
     * Dummy sample to publish message
     *
     * @param sampleRequest
     *            validated http entity body
     * @return Sample response
     * @throws BaseException
     *             if error
     */
    public SampleResponse sampleWriteRead(SampleRequest sampleRequest) throws BaseException {

        // create dummy
        SampleType dummy = createDummy();

        publusher.publish("dummy");

        SampleResponse response = new SampleResponse();
        response.setSample(dummy);

        handleSuccessResultType(response, sampleRequest);
        return response;
    }

    private SampleType createDummy() {
        SampleType dummy = new SampleType();
        dummy.setSampleId(RandomUtil.generateId());
        dummy.setSampleStatus(SampleStatusEnumType.DONE);
        dummy.setColumnA("Message published");
        dummy.setColumnB(SampleValueEnumType.VALUE_B);
        return dummy;
    }

}
