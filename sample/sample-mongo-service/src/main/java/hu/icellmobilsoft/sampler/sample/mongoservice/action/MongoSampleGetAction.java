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
package hu.icellmobilsoft.sampler.sample.mongoservice.action;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;

import hu.icellmobilsoft.coffee.dto.exception.BONotFoundException;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.module.mongodb.extension.MongoServiceConfiguration;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;
import hu.icellmobilsoft.sampler.sample.mongoservice.constant.MongoConstants;

/**
 * Service for MongoDb querying. Represents only DB operations.
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Model
public class MongoSampleGetAction extends BaseAction {

    @Inject
    private Logger log;

    @Inject
    @MongoServiceConfiguration(configKey = MongoConstants.CONFIG_SAMPLE, collectionKey = MongoConstants.TABLE_SAMPLE)
    private SampleMongoService sampleMongoService;

    /**
     * Dummy sample reponse
     * 
     * @return SampleResponse Sample response with random id
     * @throws BaseException
     *             if error
     */
    public SampleResponse sample() throws BaseException {
        ObjectId mongoIdObject = ObjectId.get();
        BasicDBObject filter = new BasicDBObject();
        filter.put(MongoConstants.COLUMN_MONGO_ID, mongoIdObject);
        try {
            sampleMongoService.findFirst(filter);
        } catch (BONotFoundException e) {
            log.info("Object with mongo id [{0}] not exists: [{1}]", mongoIdObject, e.getLocalizedMessage());
        }

        SampleResponse response = new SampleResponse();

        SampleType sampleType = new SampleType();
        sampleType.withColumnA("A");
        sampleType.withColumnB(SampleValueEnumType.VALUE_A);
        sampleType.withSampleId(RandomUtil.generateId());
        sampleType.withSampleStatus(SampleStatusEnumType.DONE);
        response.setSample(sampleType);

        handleSuccessResultType(response);
        return response;
    }
}
