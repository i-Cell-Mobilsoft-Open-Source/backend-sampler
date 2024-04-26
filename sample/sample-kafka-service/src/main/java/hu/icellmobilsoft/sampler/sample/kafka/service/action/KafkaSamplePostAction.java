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

import hu.icellmobilsoft.coffee.dto.exception.InvalidParameterException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.date.DateUtil;
import hu.icellmobilsoft.coffee.tool.utils.enums.EnumUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.SampleKafkaDto;
import hu.icellmobilsoft.sampler.dto.ValuesKafkaDto;
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
        if (sampleRequest == null) {
            throw new InvalidParameterException(CoffeeFaultType.INVALID_INPUT, "sampleRequest is missing");
        }

        SampleResponse response = new SampleResponse();
        kafkaPublisher.toKafkaString("sample-" + DateUtil.nowUTC());
        kafkaPublisher.toKafkaAvro(convertToDto(sampleRequest));
        handleSuccessResultType(response, sampleRequest);
        return response;
    }

    private SampleKafkaDto convertToDto(SampleRequest sampleRequest) {
        return SampleKafkaDto.newBuilder().setColumnA(sampleRequest.getSample().getColumnA() + "-" + DateUtil.nowUTC())
                .setColumnB(EnumUtil.convert(sampleRequest.getSample().getColumnB(), ValuesKafkaDto.class))
                .setColumnC(sampleRequest.getContext().getTimestamp().toLocalDate()).build();
    }

}
