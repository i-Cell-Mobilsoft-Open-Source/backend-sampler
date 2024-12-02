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

import jakarta.inject.Inject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.roaster.api.TestSuiteGroup;
import hu.icellmobilsoft.roaster.restassured.BaseConfigurableWeldIT;
import hu.icellmobilsoft.sampler.ts.sample.fileupload.io.RandomInputStream;

@DisplayName("Testing Sample FileUpload - Stream")
@Tag(TestSuiteGroup.RESTASSURED)
class PostStreamUploadIT extends BaseConfigurableWeldIT {

    @Inject
    private IFileUploadStreamClient fileUploadStreamClient;

    @Test
    @DisplayName("POST /fileupload/multipart/upload")
    void testUpload() throws BaseException {

        // PostFileUploadRequest postFileUploadRequest = new PostFileUploadRequest().withFileName("test.txt").withFileDescription("test description");
        //
        // PostFileUploadResponse postFileUploadResponse = fileUploadStreamClient.postStreamInit(postFileUploadRequest);
        //
        // Assertions.assertNotNull(postFileUploadResponse);
        // Assertions.assertNotNull(postFileUploadResponse.getFileUploadId());
        //
        // String fileUploadId = postFileUploadResponse.getFileUploadId();

        String fileUploadId = RandomUtil.generateId();

        InputStream inputStream = new RandomInputStream(500 * 1024 * 1024);

        fileUploadStreamClient.postStreamUpload(fileUploadId, inputStream);

    }

}
