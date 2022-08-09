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

import java.lang.reflect.Type;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.module.mongodb.extension.MongoClientConfiguration;
import hu.icellmobilsoft.coffee.module.mongodb.extension.MongoDbClient;
import hu.icellmobilsoft.coffee.module.mongodb.util.MongoJsonUtil;
import hu.icellmobilsoft.coffee.module.mongodb.util.MongoUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.sample.mongoservice.constant.MongoConstants;

/**
 * Sample mongo save action
 *
 * @author jozsef.kelemen
 * @author imre.scheffer
 * @since 0.1.0
 */
@Model
public class MongoSamplePostAction extends BaseAction {

    @Inject
    @MongoClientConfiguration(configKey = MongoConstants.CONFIG_SAMPLE)
    private MongoDbClient mongoDbClient;

    /**
     * Dummy sample write and read data from DB
     * 
     * @param sampleRequest
     *            validated http entity body
     * @return Sample response with random readed data
     * @throws BaseException
     *             if error
     */
    public SampleResponse sampleWriteRead(SampleRequest sampleRequest) throws BaseException {
        mongoDbClient.initRepositoryCollection(MongoConstants.TABLE_SAMPLE);
        SampleType sampleData = createSampleData(sampleRequest);
        String dtoJson = MongoJsonUtil.toJson(sampleData);
        BasicDBObject dtoDocument = MongoUtil.jsonToBasicDbObject(dtoJson);
        mongoDbClient.insertOne(dtoDocument);

        String mongoId = dtoDocument.getString(MongoConstants.COLUMN_MONGO_ID);
        BasicDBObject result = mongoDbClient.findById(mongoId);
        SampleType sampleType = convertJSONToPojo(result.toJson());

        SampleResponse response = new SampleResponse();
        response.setSample(sampleType);
        handleSuccessResultType(response);
        return response;
    }

    private SampleType convertJSONToPojo(String json) {
        Type type = new TypeToken<SampleType>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    private SampleType createSampleData(SampleRequest sampleRequest) {
        SampleType sampleType = new SampleType();
        sampleType.setSampleStatus(SampleStatusEnumType.DONE);
        sampleType.setColumnA(sampleRequest.getSample().getColumnA());
        sampleType.setColumnB(sampleRequest.getSample().getColumnB());
        return sampleType;
    }

}
