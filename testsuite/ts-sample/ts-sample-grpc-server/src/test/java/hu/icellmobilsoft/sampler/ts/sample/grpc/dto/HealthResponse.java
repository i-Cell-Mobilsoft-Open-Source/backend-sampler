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
 * Health response DTO
 *
 * @author karoly.tammas
 * @since 2.0.0
 */
public class HealthResponse {

    private HealthCheckResponse.Status status;
    private List<HealthCheck> checks;

    public HealthCheckResponse.Status getStatus() {
        return status;
    }

    public List<HealthCheck> getChecks() {
        if (checks == null) {
            checks = new ArrayList<>();
        }
        return checks;
    }
}
