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
package hu.icellmobilsoft.sampler.sample.filupload.service.rest;

import java.io.InputStream;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.common.system.rest.rest.BaseRestService;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadRequest;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadResponse;
import hu.icellmobilsoft.sampler.sample.filupload.rest.IFileUploadRest;
import hu.icellmobilsoft.sampler.sample.filupload.service.action.FileUploadMultipartAction;
import hu.icellmobilsoft.sampler.sample.filupload.service.action.FileUploadStreamAction;

/**
 * Sample file upload REST implementation.
 *
 * @author attila-kiss-it
 * @since 2.0.0
 */
@Model
public class FileUploadRest extends BaseRestService implements IFileUploadRest {

    @Inject
    private FileUploadMultipartAction fileUploadMultipartAction;

    @Inject
    private FileUploadStreamAction fileUploadStreamAction;

    @Override
    public PostFileUploadResponse postMultpartUpload(MultipartFormDataInput multipartFormDataInput) throws BaseException {
        return wrapPathParam1(fileUploadMultipartAction::process, multipartFormDataInput, "postFileUpload", "multipartFormDataInput");
    }

    @Override
    public PostFileUploadResponse postStreamInit(PostFileUploadRequest postFileUploadRequest) throws BaseException {
        return wrapPathParam1(fileUploadStreamAction::initStream, postFileUploadRequest, "postStreamInit", "postFileUploadRequest");
    }

    @Override
    public PostFileUploadResponse postStreamUpload(String fileUploadId, InputStream inputStream) throws BaseException {
        return wrapPathParam2(fileUploadStreamAction::uploadStream, fileUploadId, inputStream, "postStreamUpload", "fileUploadId", "inputStream");
    }

}
