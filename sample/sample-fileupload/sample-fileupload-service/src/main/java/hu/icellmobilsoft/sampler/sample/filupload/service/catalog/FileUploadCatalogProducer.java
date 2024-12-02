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
package hu.icellmobilsoft.sampler.sample.filupload.service.catalog;

import java.net.URI;
import java.text.MessageFormat;

import javax.xml.catalog.Catalog;
import javax.xml.catalog.CatalogFeatures;
import javax.xml.catalog.CatalogManager;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;

import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.rest.validation.catalog.CatalogProducer;
import hu.icellmobilsoft.coffee.rest.validation.xml.exception.XsdProcessingException;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.sample.fileupload.schema.FileUploadXsdConstants;

/**
 * {@link CatalogProducer} for file upload xml catalog.
 *
 * @author attila-kiss-it
 * @since 2.0.0
 */
@Alternative
@Priority(2010)
@Model
public class FileUploadCatalogProducer extends CatalogProducer {

    @Alternative
    @Priority(2010)
    @Override
    @Produces
    public Catalog publicCatalogResolver() throws BaseException {

        String catalogPath = FileUploadXsdConstants.SUPER_CATALOG;
        URI catalogUri;

        try {
            catalogUri = Thread.currentThread().getContextClassLoader().getResource(catalogPath).toURI();
        } catch (Exception e) {
            throw new XsdProcessingException(
                    CoffeeFaultType.OPERATION_FAILED,
                    MessageFormat.format("Can not resolve catalog:[{0}], [{1}]", catalogPath, e.getLocalizedMessage()),
                    e);
        }

        return CatalogManager.catalog(CatalogFeatures.defaults(), catalogUri);
    }

}
