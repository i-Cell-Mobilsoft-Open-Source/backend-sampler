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
package hu.icellmobilsoft.sampler.sample.filupload.rest;

import java.io.InputStream;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import hu.icellmobilsoft.coffee.rest.log.annotation.LogSpecifier;
import hu.icellmobilsoft.coffee.rest.log.annotation.LogSpecifiers;
import hu.icellmobilsoft.coffee.rest.log.annotation.enumeration.LogSpecifierTarget;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadRequest;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadResponse;
import hu.icellmobilsoft.sampler.sample.filupload.rest.url.FileUploadPath;

/**
 * Sample file upload REST API.
 *
 * @author attila-kiss-it
 * @since 2.0.0
 */
@Path(FileUploadPath.FILEUPLOAD)
public interface IFileUploadRest {

    /**
     * Max entity log size.
     */
    int MAX_ENTITY_LOG_SIZE = 1000;

    /**
     * File upload using multipart/form-data request.
     *
     * @param multipartFormDataInput
     *            {@link MultipartFormDataInput}
     * @return {@link PostFileUploadResponse}
     * @throws BaseException
     *             in case of error
     */
    @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PostFileUploadResponse.class)))
    @Operation(summary = "File upload using multipart/form-data request.")
    @POST
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.APPLICATION_XML })
    @LogSpecifiers({ @LogSpecifier(target = LogSpecifierTarget.REQUEST, maxEntityLogSize = MAX_ENTITY_LOG_SIZE),
            @LogSpecifier(target = LogSpecifierTarget.CLIENT_REQUEST, maxEntityLogSize = MAX_ENTITY_LOG_SIZE) })
    @Path(FileUploadPath.MULTIPART_UPLOAD)
    PostFileUploadResponse postMultpartUpload(@RequestBody(description = "MultipartFormDataInput", required = true, content = {
            @Content(schema = @Schema(implementation = PostFileUploadRequest.class), mediaType = MediaType.APPLICATION_XML),
                    @Content(schema = @Schema(implementation = MultipartFormDataInput.class),
                    mediaType = MediaType.MULTIPART_FORM_DATA) }) MultipartFormDataInput multipartFormDataInput)
            throws BaseException;

    /**
     * Two phase file upload - step 1: initialization.
     *
     * @param postFileUploadRequest
     *            {@link PostFileUploadRequest}
     * @return {@link PostFileUploadResponse}
     * @throws BaseException
     *             in case of error
     */
    @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PostFileUploadResponse.class)))
    @Operation(summary = "Two phase file upload - step 1: initialization")
    @POST
    @Consumes({ MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML })
    @LogSpecifiers({ @LogSpecifier(target = LogSpecifierTarget.REQUEST, maxEntityLogSize = MAX_ENTITY_LOG_SIZE),
            @LogSpecifier(target = LogSpecifierTarget.CLIENT_REQUEST, maxEntityLogSize = MAX_ENTITY_LOG_SIZE) })
    @Path(FileUploadPath.STREAM_INIT)
    PostFileUploadResponse postStreamInit(PostFileUploadRequest postFileUploadRequest) throws BaseException;

    /**
     * Two phase file upload - step 2: application/octet-stream upload
     *
     * @param fileUploadId
     *            {@link PostFileUploadResponse#getFileUploadId()}
     * @param inputStream
     *            the {@link InputStream} of the file
     * @return {@link PostFileUploadResponse}
     * @throws BaseException
     *             in case of error
     */
    @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PostFileUploadResponse.class)))
    @Operation(summary = "Two phase file upload - step 2: application/octet-stream upload")
    @POST
    @Consumes({ MediaType.APPLICATION_OCTET_STREAM })
    @Produces({ MediaType.APPLICATION_XML })
    @LogSpecifiers({ @LogSpecifier(target = LogSpecifierTarget.REQUEST, maxEntityLogSize = MAX_ENTITY_LOG_SIZE),
            @LogSpecifier(target = LogSpecifierTarget.CLIENT_REQUEST, maxEntityLogSize = MAX_ENTITY_LOG_SIZE) })
    @Path(FileUploadPath.STREAM_UPLOAD)
    PostFileUploadResponse postStreamUpload(
            @PathParam(FileUploadPath.PARAM_FILE_UPLOAD_ID) @Parameter(name = FileUploadPath.PARAM_FILE_UPLOAD_ID,
                    description = "The fileUploadId from the /fileupload/stream/init response") String fileUploadId,
            InputStream inputStream) throws BaseException;

}
