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
package hu.icellmobilsoft.sampler.sample.filupload.service.action;

import java.io.InputStream;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadRequest;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadResponse;

/**
 * Two phase file upload action.
 *
 * @author attila-kiss-it
 * @since 2.0.0
 */
@Model
public class FileUploadStreamAction {

    @Inject
    private FileUploadAction fileUploadAction;

    /**
     * File upload initialization.
     *
     * @param postFileUploadRequest
     *            {@link PostFileUploadRequest}
     * @return {@link PostFileUploadResponse}
     */
    public PostFileUploadResponse initStream(PostFileUploadRequest postFileUploadRequest) {

        return fileUploadAction.processPostFileUploadRequest(postFileUploadRequest);
    }

    /**
     * File upload.
     *
     * @param fileUploadId
     *            {@link PostFileUploadResponse#getFileUploadId()}
     * @param inputStream
     *            the {@link InputStream} of the file
     * @return void
     * @throws BaseException
     *             in case of failure
     */
    public PostFileUploadResponse uploadStream(String fileUploadId, InputStream inputStream) throws BaseException {

        fileUploadAction.processInputStream(fileUploadId, inputStream);

        return new PostFileUploadResponse().withFileUploadId(fileUploadId);
    }

}
