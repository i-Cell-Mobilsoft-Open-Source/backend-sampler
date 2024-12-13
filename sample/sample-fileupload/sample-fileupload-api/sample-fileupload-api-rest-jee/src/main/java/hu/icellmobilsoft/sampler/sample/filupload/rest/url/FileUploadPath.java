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
package hu.icellmobilsoft.sampler.sample.filupload.rest.url;

/**
 * Path constants of the file upload sample REST API.
 *
 * @author attila-kiss-it
 * @since 2.0.0
 */
public interface FileUploadPath {

    /**
     * Path for /fileupload
     */
    String FILEUPLOAD = "/fileupload";

    /**
     * Path for /upload
     */
    String UPLOAD = "/upload";

    /**
     * Path for /multipart/upload
     */
    String MULTIPART_UPLOAD = "/multipart" + UPLOAD;

    /**
     * Path for /stream
     */
    String STREAM = "/stream";

    /**
     * Path for /init
     */
    String STREAM_INIT = STREAM + "/init";

    /**
     * Path parameter name for fileUploadId
     */
    String PARAM_FILE_UPLOAD_ID = "fileUploadId";

    /**
     * Path for /stream/upload/{fileUploadId}
     */
    String STREAM_UPLOAD = STREAM + "/upload/{" + PARAM_FILE_UPLOAD_ID + "}";

}
