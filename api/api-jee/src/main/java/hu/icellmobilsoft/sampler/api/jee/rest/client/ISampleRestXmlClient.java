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
package hu.icellmobilsoft.sampler.api.jee.rest.client;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.api.jee.rest.ISampleRest;
import hu.icellmobilsoft.sampler.dto.path.SamplePath;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;

/**
 * Microprofile rest client implementation of {@link ISampleRest} for XML communication
 * 
 * @author Imre Scheffer
 * @since 2.0.0
 */
@Path(SamplePath.REST_SAMPLE_SERVICE)
@RegisterRestClient
public interface ISampleRestXmlClient extends ISampleRest {

    @Override
    @GET
    @Produces(value = { MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    SampleResponse getSample() throws BaseException;

    @Override
    @POST
    @Produces(value = { MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    @Consumes(value = { MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    SampleResponse postSample(SampleRequest sampleRequest) throws BaseException;

}
