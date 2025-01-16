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
package hu.icellmobilsoft.sampler.sample.comsumer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import hu.icellmobilsoft.coffee.model.base.AbstractIdentifiedEntity;
import hu.icellmobilsoft.sampler.sample.comsumer.model.enums.ProductionStatus;

/**
 * Car produciton entity.
 *
 * @author attila.kiss
 * @since 2.0.0
 */
@Entity
@Table(name = "CAR_PRODUCTION")
public class CarProduction extends AbstractIdentifiedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * The status of the car production process.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PRODUCTION_STATUS", length = 30, nullable = false)
    @NotNull
    private ProductionStatus productionStatus;

    /**
     * The painted layers.
     */
    @Column(name = "LAYERS", length = 100)
    @Size(max = 100)
    private String layers;

    /**
     * The painted color.
     */
    @Column(name = "COLOR", length = 30)
    @Size(max = 30)
    private String color;

    /**
     * Return the {@link #productionStatus} field.
     *
     * @return the {@link #productionStatus} field
     */
    public ProductionStatus getProductionStatus() {
        return productionStatus;
    }

    /**
     * Sets the {@link #productionStatus} field.
     *
     * @param productionStatus
     *            the {@link #productionStatus} to set
     */
    public void setProductionStatus(ProductionStatus productionStatus) {
        this.productionStatus = productionStatus;
    }

    /**
     * Return the {@link #layers} field.
     *
     * @return the {@link #layers} field
     */
    public String getLayers() {
        return layers;
    }

    /**
     * Sets the {@link #layers} field.
     *
     * @param layers
     *            the {@link #layers} to set
     */
    public void setLayers(String layers) {
        this.layers = layers;
    }

    /**
     * Return the {@link #color} field.
     *
     * @return the {@link #color} field
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the {@link #color} field.
     *
     * @param color
     *            the {@link #color} to set
     */
    public void setColor(String color) {
        this.color = color;
    }

}
