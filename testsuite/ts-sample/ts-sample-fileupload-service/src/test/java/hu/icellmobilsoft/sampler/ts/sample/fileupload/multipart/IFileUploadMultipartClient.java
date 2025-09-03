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
package hu.icellmobilsoft.sampler.ts.sample.fileupload.multipart;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadResponse;
import hu.icellmobilsoft.sampler.sample.filupload.rest.url.FileUploadPath;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST client for multipart upload.
 *
 * @author attila-kiss-it
 * @since 2.0.0
 */
@RegisterRestClient(configKey = IFileUploadMultipartClient.CONFIG_KEY)
@Path(FileUploadPath.FILEUPLOAD)
public interface IFileUploadMultipartClient {

    /**
     * Microrpfile Rest client configuraiton key: for e.g. "testsuite.sample.fileupload.client.service/mp-rest/url: http://localhost:8081"
     */
    public static final String CONFIG_KEY = "testsuite.sample.fileupload.client.service";

    /**
     * Multipart upload.
     *
     * @param multipartFormDataOutput
     *            {@link MultipartFormDataOutput}
     * @return {@link PostFileUploadResponse}
     * @throws BaseException
     *             in case of error
     */
    @POST
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.APPLICATION_XML })
    @Path(FileUploadPath.MULTIPART_UPLOAD)
    PostFileUploadResponse postMultpartUpload(MultipartFormDataOutput multipartFormDataOutput) throws BaseException;

}
