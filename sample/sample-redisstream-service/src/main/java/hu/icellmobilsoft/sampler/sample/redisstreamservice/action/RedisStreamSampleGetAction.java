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

import jakarta.enterprise.inject.Model;

import hu.icellmobilsoft.coffee.dto.common.commonservice.ContextType;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.se.util.string.RandomUtil;
import hu.icellmobilsoft.coffee.tool.utils.date.DateUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleCoreType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;

/**
 * Service for rest
 *
 * @author czencz
 * @since 2.0.0
 */
@Model
public class RedisStreamSampleGetAction extends BaseAction {

    /**
     * Dummy sample response
     *
     * @return SampleResponse Sample response with random id
     * @throws BaseException
     *             if error
     */
    public SampleResponse sample() throws BaseException {

        SampleResponse response = new SampleResponse();
        SampleType sampleType = new SampleType();
        sampleType.withColumnA("SAMPLE_VALUE");
        sampleType.withColumnB(SampleValueEnumType.VALUE_A);
        sampleType.withSampleId(RandomUtil.generateId());
        sampleType.withSampleStatus(SampleStatusEnumType.DONE);
        response.setSample(sampleType);

        SampleRequest req = new SampleRequest();
        req.withContext(new ContextType().withRequestId("ddd").withTimestamp(DateUtil.nowUTCTruncatedToMillis()));
        req.withSample(new SampleCoreType().withColumnA("ccc").withColumnB(SampleValueEnumType.VALUE_B));

        handleSuccessResultType(response);
        return response;
    }

}
