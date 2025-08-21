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
package hu.icellmobilsoft.sampler.sample.comsumer.model.process;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

import hu.icellmobilsoft.sampler.sample.comsumer.model.CarProduction;

/**
 * Abstract process entity for car production.
 *
 * @author attila.kiss
 * @since 2.0.0
 */
@MappedSuperclass
public abstract class AbstractProcessCarProductionEntity extends AbstractProcessEntity {

    /**
     * The {@link CarProduction} entity that belongs to this process.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_PRODUCTION_ID", unique = true, nullable = false, updatable = false)
    @NotNull
    private CarProduction carProduction;

    /**
     * Returns the {@link #carProduction} field.
     *
     * @return the {@link #carProduction} field
     */
    public CarProduction getCarProduction() {
        return carProduction;
    }

    /**
     * Sets the {@link #carProduction} field.
     *
     * @param carProduction
     *            the {@link #carProduction} to set
     */
    public void setCarProduction(CarProduction carProduction) {
        this.carProduction = carProduction;
    }

}
