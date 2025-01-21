/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2025 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.service.rest;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.api.jee.rest.ISampleRest;
import hu.icellmobilsoft.sampler.common.system.rest.rest.BaseRestService;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.sample.service.action.FileWatcherAction;

/**
 * Sample service FileWatcher implementation
 *
 * @author zsdoma
 * @since 2.0.0
 */
@Model
public class FileWatcherSampleRest extends BaseRestService implements ISampleRest {

    @Inject
    private FileWatcherAction fileWatcherAction;

    @Override
    public SampleResponse getSample() throws BaseException {
        return wrapNoParam(fileWatcherAction::sample, "getSample");
    }

    @Override
    public SampleResponse postSample(final SampleRequest sampleRequest) throws BaseException {
        return wrapNoParam(fileWatcherAction::sample, "postSample");
    }

}
