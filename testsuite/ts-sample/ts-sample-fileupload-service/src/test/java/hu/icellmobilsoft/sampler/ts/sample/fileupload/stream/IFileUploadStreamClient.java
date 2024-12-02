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
package hu.icellmobilsoft.sampler.ts.sample.fileupload.stream;

import java.io.InputStream;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadRequest;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadResponse;
import hu.icellmobilsoft.sampler.sample.filupload.rest.url.FileUploadPath;

@RegisterRestClient(configKey = IFileUploadStreamClient.CONFIG_KEY)
@Path(FileUploadPath.FILEUPLOAD)
public interface IFileUploadStreamClient {

    /**
     * Microrpfile Rest client konfiguracios kulcsa, pl. "testsuite.sample.fileupload.client.service/mp-rest/url: http://localhost:8081"
     */
    public static final String CONFIG_KEY = "testsuite.sample.fileupload.client.service";

    @POST
    @Consumes({ MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML })
    @Path(FileUploadPath.STREAM_INIT)
    PostFileUploadResponse postStreamInit(PostFileUploadRequest postFileUploadRequest) throws BaseException;

    @POST
    @Consumes({ MediaType.APPLICATION_OCTET_STREAM })
    @Produces({ MediaType.APPLICATION_XML })
    @Path(FileUploadPath.STREAM_UPLOAD)
    void postStreamUpload(
            @PathParam(FileUploadPath.PARAM_FILE_UPLOAD_ID) String fileUploadId,
            InputStream inputStream) throws BaseException;

}
