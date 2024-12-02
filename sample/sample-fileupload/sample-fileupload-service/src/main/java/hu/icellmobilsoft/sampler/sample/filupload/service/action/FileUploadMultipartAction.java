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
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.rest.validation.xml.JaxbTool;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.se.api.exception.TechnicalException;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadRequest;
import hu.icellmobilsoft.sampler.sample.fileupload.PostFileUploadResponse;
import hu.icellmobilsoft.sampler.sample.fileupload.schema.FileUploadXsdConstants;

/**
 * Multipart file upload action.
 *
 * @author attila-kiss-it
 * @since 2.0.0
 */
@Model
public class FileUploadMultipartAction {

    @Inject
    private JaxbTool jaxbTool;

    @Inject
    private FileUploadAction fileUploadAction;

    /**
     * Prcesses the multipart request.
     *
     * @param multipartFormDataInput
     *            {@link MultipartFormDataInput}
     * @return {@link PostFileUploadResponse}
     * @throws BaseException
     *             in case of failure
     */
    public PostFileUploadResponse process(MultipartFormDataInput multipartFormDataInput) throws BaseException {

        List<InputPart> bodyList = multipartFormDataInput.getFormDataMap().get("body");
        List<InputPart> fileList = multipartFormDataInput.getFormDataMap().get("file");

        PostFileUploadRequest postFileUploadRequest;

        try (InputStream inputStream = IOUtils.toInputStream(bodyList.get(0).getBodyAsString(), StandardCharsets.UTF_8)) {
            postFileUploadRequest = jaxbTool.unmarshalXML(PostFileUploadRequest.class, inputStream, FileUploadXsdConstants.SUPER_XSD);
        } catch (IOException e) {
            throw new TechnicalException(CoffeeFaultType.OPERATION_FAILED, "Failed to process body", e);
        }

        PostFileUploadResponse postFileUploadResponse = fileUploadAction.processPostFileUploadRequest(postFileUploadRequest);

        try (InputStream inputStream = fileList.get(0).getBody(InputStream.class, null)) {

            fileUploadAction.processInputStream(postFileUploadResponse.getFileUploadId(), inputStream);

        } catch (IOException e) {
            throw new TechnicalException(CoffeeFaultType.OPERATION_FAILED, "Failed to process file list!", e);
        }

        return postFileUploadResponse;
    }

}
