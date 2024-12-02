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

import java.io.IOException;
import java.io.InputStream;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import org.apache.commons.io.IOUtils;

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.se.api.exception.TechnicalException;
import hu.icellmobilsoft.coffee.se.util.string.RandomUtil;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadRequest;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadResponse;

/**
 * Abstract file upload action.
 *
 * @author attila-kiss-it
 * @since 2.0.0
 */
@Model
public class FileUploadAction {

    @Inject
    @ThisLogger
    private AppLogger log;

    /**
     * Processing the {@link PostFileUploadRequest}.
     *
     * @param postFileUploadRequest
     *            {@link PostFileUploadRequest}
     * @return {@link PostFileUploadResponse}
     */
    public PostFileUploadResponse processPostFileUploadRequest(PostFileUploadRequest postFileUploadRequest) {

        log.info(
                ">>> body processed: fileName [{0}], fileDescription [{1}]",
                postFileUploadRequest.getFileName(),
                postFileUploadRequest.getFileDescription());

        return new PostFileUploadResponse().withFileUploadId(RandomUtil.generateId());
    }

    /**
     * Porcessing the uploaded {@link InputStream}.
     *
     * @param fileUploadId
     *            {@link PostFileUploadResponse#getFileUploadId()}
     * @param inputStream
     *            the {@link InputStream} of the file
     * @throws BaseException
     *             in case of error
     */
    public void processInputStream(String fileUploadId, InputStream inputStream) throws BaseException {

        try (inputStream) {

            long numberOfBytes = IOUtils.consume(inputStream);

            log.info(">>> file processed: fileUploadId [{0}], numberOfBytes [{1}]", fileUploadId, numberOfBytes);

        } catch (IOException e) {
            throw new TechnicalException(CoffeeFaultType.OPERATION_FAILED, "Failed to process input stream!", e);
        }
    }

}
