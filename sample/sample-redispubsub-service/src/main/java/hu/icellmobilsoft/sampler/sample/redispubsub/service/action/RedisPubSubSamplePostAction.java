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

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import hu.icellmobilsoft.coffee.dto.common.LogConstants;
import hu.icellmobilsoft.coffee.dto.exception.BONotFoundException;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.module.redispubsub.bundle.PubSubMessage;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.rest.cdi.ApplicationContainer;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleCoreType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;
import hu.icellmobilsoft.sampler.sample.redispubsub.service.config.RedisPubSubConfig;

/**
 * Sample redis pub-sub action
 *
 * @author mark.petrenyi
 * @since 0.1.0
 */
@Model
public class RedisPubSubSamplePostAction extends BaseAction {

    private static final long EXPIRE_MINUTES = Duration.ofMinutes(5).toSeconds();


    @Inject
    private Logger log;

    @Inject
    @Channel(RedisPubSubConfig.SamplePost.MP_CHANNEL_OUT)
    private Emitter<SampleType> emitter;

    @Inject
    @Channel(RedisPubSubConfig.NoSub.REDIS_CHANNEL)
    private Emitter<PubSubMessage> emitterWithoutSubscriber;

    @Inject
    private ApplicationContainer applicationContainer;

    /**
     * Dummy sample write and read data from Redis PUB-SUB
     *
     * @param sampleRequest validated http entity body
     * @return Sample response with random readed data
     * @throws BaseException if error
     */
    public SampleResponse sampleWriteRead(SampleRequest sampleRequest) throws BaseException {

        // create dummy
        SampleType dummy = createDummy(sampleRequest.getSample());
        //publish event
        emitter.send(dummy);
        //publish and wait
        waitForPublish(emitterWithoutSubscriber.send(PubSubMessage.of(dummy.getSampleId(), Map.of(LogConstants.LOG_SESSION_ID, "customSID"))));

        //sleep 1 second, meanwhile the PostInListener must get the message and store it in application cache.
        sleep();
        SampleType sampleType = readDummy();

        SampleResponse response = new SampleResponse();
        response.setSample(sampleType);

        handleSuccessResultType(response, sampleRequest);
        return response;
    }

    private void waitForPublish(CompletionStage<Void> publishStage) throws TechnicalException {
        try {
            publishStage.toCompletableFuture().get();
        } catch (InterruptedException ex) {
            handleInterrupt(ex);
        } catch (ExecutionException e) {
            log.error("Exception occured", e);
            throw new TechnicalException(CoffeeFaultType.REDIS_OPERATION_FAILED, "Unkown redis publish error", e);
        }
    }

    private void sleep() {
        try {
            // It is important to pause so that, for example, the disconnection of the connection does not flood the log
            // And don't engage in unnecessary unlimited infinite attempts
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            handleInterrupt(ex);
        }
    }

    private void handleInterrupt(InterruptedException ex) {
        log.warn("Interrupted sleep.", ex);
        // sonar: "InterruptedException" should not be ignored (java:S2142)
        try {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.warn("Exception during interrupt.", ex);
        }
    }

    private SampleType createDummy(SampleCoreType sample) {
        SampleType dummy = new SampleType();
        dummy.setSampleId(RandomUtil.generateId());
        dummy.setSampleStatus(SampleStatusEnumType.DONE);
        dummy.setColumnA(sample.getColumnA());
        dummy.setColumnB(SampleValueEnumType.VALUE_B);
        return dummy;
    }

    private SampleType readDummy() throws BaseException {
        Map<String, Object> objectMap = applicationContainer.getObjectMap();
        if (objectMap.containsKey(RedisPubSubConfig.SamplePost.DUMMY_KEY)) {
            return (SampleType) objectMap.remove(RedisPubSubConfig.SamplePost.DUMMY_KEY);
        }
        throw new BONotFoundException("Could not find dummy in application container");
    }
}
