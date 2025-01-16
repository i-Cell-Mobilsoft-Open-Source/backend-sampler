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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import hu.icellmobilsoft.coffee.se.api.exception.JsonConversionException;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.utils.json.JsonUtil;

/**
 * {@code FileWatcher} loads an initial configuration map from a file and monitors the file for changes. When a change is detected, the configuration
 * map is automatically reloaded, ensuring that the application always works with the latest settings.
 *
 * @author mate.biro
 * @author zsdoma
 * @since 2.0.0
 */
@ApplicationScoped
public class FileWatcher {

    /**
     * Config property delimiter.
     */
    public static final String KEY_VALUE_DELIMITER = "=";

    private static final String CONFIG_PROPERTIES_FILE_PATH = "CONFIG_PROPERTIES_FILE_PATH";

    private static final Logger log = Logger.getLogger(FileWatcher.class);

    @Inject
    @ConfigProperty(name = CONFIG_PROPERTIES_FILE_PATH, defaultValue = "/home/customConfig/config.properties")
    private String configFilePathString;

    @Inject
    private FileConfigCache fileConfigCache;

    private static final String WATCH_SERVICE_ERROR_MESSAGE = "Could not instantiate WatchService, or directory could not be registered!";
    private static final String WATCH_SERVICE_INTERRUPT_MESSAGE = "WatchService was interrupted while waiting for next watch key!";
    private static final String WATCH_SERVICE_ERROR_LOADING_FILE_MESSAGE = "Error loading configuration file!";
    private static final String WATCH_SERVICE_INVALID_WATCH_KEY_MESSAGE = "Invalid Watch key on reset! Cannot continue watching!";
    private static final String WATCH_SERVICE_MISSING_VALUE_FOR_A_KEY_MESSAGE = "Invalid configuration file, no value present for keys: [{0}]";

    @Resource
    private ManagedExecutorService managedExecutorService;

    /**
     * Starts file watching on application startup.
     *
     * @param obj
     *            startup event object, not used
     */
    public void init(@Observes @Initialized(ApplicationScoped.class) Object obj) {
        managedExecutorService.submit(() -> {
            log.info("Loading initial configuration file: [{0}]", configFilePathString);
            loadConfigFile();

            try {
                watchConfigFileForChanges();
            } catch (IOException e) {
                log.error(WATCH_SERVICE_ERROR_MESSAGE, e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(WATCH_SERVICE_INTERRUPT_MESSAGE, e);
            }
        });

        log.info("FileWatcher started!");
    }

    private void watchConfigFileForChanges() throws IOException, InterruptedException {
        Path configFilePath = Paths.get(configFilePathString);

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path parentDir = configFilePath.getParent();

        // Register the directory to watch for changes in the file
        parentDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        log.debug("parentDir [{0}] registered for watching!", parentDir);

        WatchKey key = watchService.take();
        log.info("Waiting for changes in: [{0}]", configFilePath);
        while (key != null) {
            List<WatchEvent<?>> events = key.pollEvents();
            log.debug("Watch event occurred in [{0}]!", parentDir);
            for (WatchEvent<?> event : events) {
                WatchEvent.Kind<?> kind = event.kind();
                log.debug("event kind [{0}] : [{1}]", kind.name(), kind.type());

                // Check if it's a modify event
                if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    // Reload the configuration
                    log.info("Configuration file [{0}] changed! Reloading...", configFilePath);
                    loadConfigFile();
                }
            }

            // Reset the key to continue watching
            boolean valid = key.reset();
            if (!valid) {
                log.error(WATCH_SERVICE_INVALID_WATCH_KEY_MESSAGE);
                break;
            }
            key = watchService.take();
        }
    }

    private void loadConfigFile() {
        Map<String, String> configMap = new HashMap<>();
        List<String> invalidValueKeys = new ArrayList<>();

        Path configFile = Paths.get(configFilePathString);
        try {
            List<String> lines = Files.readAllLines(configFile);
            lines.forEach(line -> processLine(line, configMap, invalidValueKeys));

            if (CollectionUtils.isNotEmpty(invalidValueKeys)) {
                log.error(MessageFormat.format(WATCH_SERVICE_MISSING_VALUE_FOR_A_KEY_MESSAGE, invalidValueKeys));
                return;
            }
            updateConfigCache(configMap);

        } catch (IOException | JsonConversionException e) {
            log.error(WATCH_SERVICE_ERROR_LOADING_FILE_MESSAGE, e);
        }
    }

    private void processLine(String line, Map<String, String> configMap, List<String> invalidValueKeys) {
        String[] keyValue = line.split(KEY_VALUE_DELIMITER, 2);
        if (keyValue.length > 1 && StringUtils.isNotBlank(keyValue[1])) {
            configMap.putIfAbsent(keyValue[0], keyValue[1]);
        } else {
            invalidValueKeys.add(keyValue[0]);
        }
    }

    private void updateConfigCache(Map<String, String> configMap) throws JsonConversionException {
        fileConfigCache.setConfigMap(configMap);
        log.info(
                "Configuration file loaded! [{0}] distinct keys found:\n[{1}]",
                fileConfigCache.getConfigMap().size(),
                JsonUtil.toJson(fileConfigCache.getConfigMap()));
    }
}
