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
package hu.icellmobilsoft.sampler.sample.comsumerservice.service;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.jpa.service.BaseService;
import hu.icellmobilsoft.sampler.sample.comsumer.model.CarProduction;
import hu.icellmobilsoft.sampler.sample.comsumerservice.repository.CarProductionRepository;

/**
 * Service class for {@link CarProduction} entity.
 *
 * @author attila.kiss
 * @since 2.0.0
 */
@Model
public class CarProductionService extends BaseService<CarProduction> {

    @Inject
    private CarProductionRepository carProductionRepository;

}
