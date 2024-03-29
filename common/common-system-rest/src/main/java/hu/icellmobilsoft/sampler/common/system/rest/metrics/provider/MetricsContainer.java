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
package hu.icellmobilsoft.sampler.common.system.rest.metrics.provider;

import java.time.LocalDateTime;

import jakarta.enterprise.inject.Model;

/**
 * Microprofile-metrics RequestScope container
 * 
 * @author imre.scheffer
 * 
 * @since 0.1.0
 */
@Model
public class MetricsContainer {

    private LocalDateTime startTime;

    /**
     * Get start time for metrics
     * 
     * @return start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Set start time for metrics
     * 
     * @param startTime
     *            start time
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

}
