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
package hu.icellmobilsoft.sampler.ts.sample.grpc.dto;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.health.HealthCheckResponse;

/**
 * Health check DTO
 *
 * @author karoly.tammas
 * @since 2.0.0
 */
public class HealthCheck {

    private String name;
    private HealthCheckResponse.Status status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HealthCheckResponse.Status getStatus() {
        return status;
    }

    public void setStatus(HealthCheckResponse.Status status) {
        this.status = status;
    }
}
