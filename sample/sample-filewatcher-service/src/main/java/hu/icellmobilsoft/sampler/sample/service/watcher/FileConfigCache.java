/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2025 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.service.watcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Represents config map loaded from config file.
 *
 * @author janos.hamrak
 * @since 2.0.0
 */
@ApplicationScoped
public class FileConfigCache {

    private Map<String, String> configMap = new HashMap<>();

    /**
     * Getter for {@code configMap}.
     *
     * @return file config map
     */
    public Map<String, String> getConfigMap() {
        return Collections.unmodifiableMap(configMap);
    }

    /**
     * Setter for {@code configMap}.
     *
     * @param configMap
     *            file config map
     */
    public void setConfigMap(Map<String, String> configMap) {
        this.configMap = configMap;
    }
}
