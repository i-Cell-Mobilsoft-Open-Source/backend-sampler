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
package hu.icellmobilsoft.sampler.sample.etcdservice.action;

import java.text.MessageFormat;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import hu.icellmobilsoft.coffee.dto.exception.BONotFoundException;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.module.etcd.handler.ConfigEtcdHandler;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;

/**
 * Sample etcd write read action
 *
 * @author zsolt.vasi
 * @since 2.0.0
 */
@Model
public class EtcdSamplePostAction extends BaseAction {

    @Inject
    private ConfigEtcdHandler configEtcdHandler;

    /**
     * Dummy sample write and read data to/from Etcd
     *
     * @param sampleRequest
     *            validated http entity body
     * @return Sample response with readed data
     * @throws BaseException
     *             if error
     */
    public SampleResponse sampleWriteRead(SampleRequest sampleRequest) throws BaseException {

        // create dummy value
        String etcdKey = RandomUtil.generateId();
        String columnA = "COLUMNA_" + etcdKey;

        // save value into etcd
        saveDummy(etcdKey, columnA);

        String readedValue = readValue(etcdKey);

        SampleResponse response = new SampleResponse();
        response.setSample(createSample(etcdKey, readedValue));

        handleSuccessResultType(response, sampleRequest);
        return response;
    }

    private SampleType createSample(String key, String columnA) {
        SampleType dummy = new SampleType();
        dummy.setSampleId(key);
        dummy.setSampleStatus(SampleStatusEnumType.DONE);
        dummy.setColumnA(columnA);
        dummy.setColumnB(SampleValueEnumType.VALUE_B);
        return dummy;
    }

    private void saveDummy(String etcdKey, String columnA) throws BaseException {
        configEtcdHandler.putValue(etcdKey, columnA);
    }

    private String readValue(String etcdKey) throws BaseException {
        Config config = ConfigProvider.getConfig();
        return config.getOptionalValue(etcdKey, String.class)
                .orElseThrow(() -> new BONotFoundException(MessageFormat.format("Etcd value with key:[{0}] not found!", etcdKey)));
    }
}
