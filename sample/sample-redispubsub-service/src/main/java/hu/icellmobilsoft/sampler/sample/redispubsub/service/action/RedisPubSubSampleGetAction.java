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
package hu.icellmobilsoft.sampler.sample.redispubsub.service.action;

import java.util.Map;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.rest.cdi.ApplicationContainer;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;
import hu.icellmobilsoft.sampler.sample.redispubsub.service.config.RedisPubSubConfig;

/**
 * Service for Redis read
 *
 * @author mark.petrenyi
 * @since 0.1.0
 */
@Model
public class RedisPubSubSampleGetAction extends BaseAction {

    @Inject
    private Logger log;


    @Inject
    private ApplicationContainer applicationContainer;

    /**
     * Dummy sample reponse
     *
     * dummy can be set with redis-cli:
     * <pre>
     * docker exec -it bs-sample-redis redis-cli
     * 127.0.0.1:6379&gt; PUBLISH sample-get test-dummy
     * </pre>
     *
     * @return SampleResponse Sample response with random id
     * @throws BaseException if error
     */
    public SampleResponse sample() throws BaseException {

        SampleResponse response = new SampleResponse();
        SampleType sampleType = new SampleType();
        sampleType.withColumnB(SampleValueEnumType.VALUE_A);
        sampleType.withColumnA(readDummy());
        sampleType.withSampleId(RandomUtil.generateId());
        sampleType.withSampleStatus(SampleStatusEnumType.DONE);
        response.setSample(sampleType);

        handleSuccessResultType(response);
        return response;
    }

    private String readDummy() throws BaseException {
        Map<String, Object> objectMap = applicationContainer.getObjectMap();
        return (String) objectMap.get(RedisPubSubConfig.SampleGet.DUMMY_KEY);
    }
}
