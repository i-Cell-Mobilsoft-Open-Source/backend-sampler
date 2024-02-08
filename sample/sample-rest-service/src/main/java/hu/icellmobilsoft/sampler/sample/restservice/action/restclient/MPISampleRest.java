/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2024 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.restservice.action.restclient;

import hu.icellmobilsoft.sampler.api.jee.rest.ISampleRest;
import hu.icellmobilsoft.sampler.dto.path.SamplePath;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * asdfasdf
 */
@Path(SamplePath.REST_SAMPLE_SERVICE)
@RegisterRestClient(baseUri = "http://localhost:8080")
public interface MPISampleRest extends ISampleRest {
}
