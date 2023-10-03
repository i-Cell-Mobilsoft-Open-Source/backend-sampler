/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2023 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.kafka.service.action;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.date.DateUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;

/**
 * Service for Kafka write-read <br>
 * <a href=
 * "https://github.com/wildfly/quickstart/blob/27.x/microprofile-reactive-messaging-kafka/README.adoc">microprofile-reactive-messaging-kafka</a><br>
 * <a href="https://smallrye.io/smallrye-reactive-messaging/4.0.0/kafka/kafka/">smallrye.io/smallrye-reactive-messaging</a>
 * 
 * @author Imre Scheffer
 * @since 2.0.0
 */
@Model
public class KafkaSamplePostAction extends BaseAction {

    @Inject
    private KafkaPublisher kafkaPublisher;

    /**
     * Sample action
     * 
     * @param sampleRequest
     *            request
     * @return response
     * @throws BaseException
     *             error
     */
    public SampleResponse sample(SampleRequest sampleRequest) throws BaseException {
        SampleResponse response = new SampleResponse();
        kafkaPublisher.toKafka("sample-" + DateUtil.nowUTC());
        handleSuccessResultType(response, sampleRequest);
        return response;
    }

}
