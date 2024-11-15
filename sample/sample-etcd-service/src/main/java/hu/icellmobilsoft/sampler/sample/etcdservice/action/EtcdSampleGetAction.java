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

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import hu.icellmobilsoft.coffee.se.util.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;

/**
 * Service for Etcd read
 *
 * @author zsolt.vasi
 * @since 2.0.0
 */
@Model
public class EtcdSampleGetAction extends BaseAction {

    private static final String PRIVATE_COLUMN_A = "private.column.a";
    private static final String PRIVATE_COLUMN_A_DEFAULT = "NONE";

    @Inject
    @ConfigProperty(name = PRIVATE_COLUMN_A, defaultValue = PRIVATE_COLUMN_A_DEFAULT)
    private String columnAValue;

    /**
     * Dummy sample response
     *
     * @return SampleResponse with random id
     */
    public SampleResponse sample() {

        SampleResponse response = new SampleResponse();
        SampleType sampleType = new SampleType();
        sampleType.withColumnA(columnAValue);
        sampleType.withColumnB(SampleValueEnumType.VALUE_A);
        sampleType.withSampleId(RandomUtil.generateId());
        sampleType.withSampleStatus(SampleStatusEnumType.DONE);
        response.setSample(sampleType);

        handleSuccessResultType(response);
        return response;
    }
}
