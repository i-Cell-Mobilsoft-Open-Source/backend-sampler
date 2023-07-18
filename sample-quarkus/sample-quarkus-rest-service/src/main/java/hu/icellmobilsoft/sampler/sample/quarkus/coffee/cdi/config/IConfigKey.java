/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.quarkus.coffee.cdi.config;

import hu.icellmobilsoft.coffee.module.configdoc.ConfigDoc;

/**
 * Collector of coffee-level configuration keys.
 *
 * @author imre.scheffer
 * @since 1.0.0
 */
@ConfigDoc
public interface IConfigKey {

    /**
     * Coffee configuration prefixes
     */
    @ConfigDoc(exclude = true)
    String COFFEE_CONFIG_PREFIX = "coffee.config";

    /**
     * Localization dictionaries access paths (in Java class package format).<br>
     * E.g., "i18n.messages,i18n.validators,i18n.enums" - without spaces
     */
    String RESOURCE_BUNDLES = COFFEE_CONFIG_PREFIX + ".resource.bundles";

    /**
     * Location of the XML Catalog file. E.g., "xsd/hu/icellmobilsoft/project/dto/super.catalog.xml"
     */
    String CATALOG_XML_PATH = COFFEE_CONFIG_PREFIX + ".xml.catalog.path";

    /**
     * Regular expression used for request logging and masking keys during etcd queries. E.g.. Pl. "[\\w\\s]*?secret[\\w\\s]*?",
     * "[\\w\\s]*?pass[\\w\\s]*?"
     */
    String LOG_SENSITIVE_KEY_PATTERN = COFFEE_CONFIG_PREFIX + ".log.sensitive.key.pattern";
}

