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
package hu.icellmobilsoft.sampler.sample.redisservice.action;

import java.text.MessageFormat;
import java.time.Duration;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.dto.exception.BONotFoundException;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.module.redis.annotation.RedisConnection;
import hu.icellmobilsoft.coffee.module.redis.manager.RedisManager;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.gson.JsonUtil;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;
import hu.icellmobilsoft.sampler.sample.redisservice.config.RedisConfig;
import redis.clients.jedis.Jedis;

/**
 * Sample redis query action
 *
 * @author mark.petrenyi
 * @since 0.1.0
 */
@Model
public class RedisSamplePostAction extends BaseAction {

    private static final long EXPIRE_MINUTES = Duration.ofMinutes(5).toSeconds();
    @Inject
    @RedisConnection(configKey = RedisConfig.REDIS_KEY)
    private RedisManager redisManager;

    @Inject
    private Logger log;

    /**
     * Dummy sample write and read data from Redis
     *
     * @param sampleRequest
     *            validated http entity body
     * @return Sample response with random readed data
     * @throws BaseException
     *             if error
     */
    public SampleResponse sampleWriteRead(SampleRequest sampleRequest) throws BaseException {

        // create dummy
        SampleType dummy = createDummy();
        String redisKey = RandomUtil.generateId();

        // dummy save into redis
        saveDummy(redisKey, dummy);

        SampleType sampleType = readDummy(redisKey);

        SampleResponse response = new SampleResponse();
        response.setSample(sampleType);

        handleSuccessResultType(response, sampleRequest);
        return response;
    }

    private SampleType createDummy() {
        SampleType dummy = new SampleType();
        dummy.setSampleId(RandomUtil.generateId());
        dummy.setSampleStatus(SampleStatusEnumType.DONE);
        dummy.setColumnA("Generated");
        dummy.setColumnB(SampleValueEnumType.VALUE_B);
        return dummy;
    }

    private void saveDummy(String redisKey, SampleType dummy) throws BaseException {
        // It could be setex as well, but this way multiple operations are tested with the same connection.
        try (var con = redisManager.initConnection()) {
            redisManager.run(Jedis::set, "set", redisKey, JsonUtil.toJson(dummy));
            redisManager.run(Jedis::expire, "expire", redisKey, EXPIRE_MINUTES);
        } catch (Exception e) {
            String msg = "Exception occured while saving dummy";
            log.error(msg, e);
            throw new TechnicalException(CoffeeFaultType.REPOSITORY_FAILED, msg, e);
        }
    }

    private SampleType readDummy(String redisKey) throws BaseException {
        String redisObject = redisManager.runWithConnection(Jedis::get, "get", redisKey)
                .orElseThrow(() -> new BONotFoundException(MessageFormat.format("Redis object with key:[{0}] not found!", redisKey)));
        return JsonUtil.toObjectEx(redisObject, SampleType.class);
    }
}
