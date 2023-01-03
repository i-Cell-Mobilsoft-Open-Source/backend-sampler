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
package hu.icellmobilsoft.sampler.sample.jpaservice.converter;

import java.util.Objects;

import jakarta.enterprise.inject.Model;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.system.jpa.converter.IResponseConverter;
import hu.icellmobilsoft.coffee.tool.utils.enums.EnumUtil;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;
import hu.icellmobilsoft.sampler.model.sample.SampleEntity;

/**
 * Sample {@link SampleEntity} to {@link SampleType} converter
 * 
 * @author Imre Scheffer
 * @since 0.1.0
 */
@Model
public class SampleTypeConverter implements IResponseConverter<SampleEntity, SampleType> {

    @Override
    public SampleType convert(SampleEntity entity) throws BaseException {
        SampleType result = new SampleType();
        convert(result, entity);
        return result;
    }

    @Override
    public void convert(SampleType destinationDto, SampleEntity sourceEntity) throws BaseException {
        if (Objects.isNull(destinationDto) || Objects.isNull(sourceEntity)) {
            return;
        }
        destinationDto.setColumnA(sourceEntity.getLocalDateTime().toString());
        destinationDto.setColumnB(EnumUtil.convert(sourceEntity.getValue(), SampleValueEnumType.class));
        destinationDto.setSampleId(sourceEntity.getId());
        destinationDto.setSampleStatus(EnumUtil.convert(sourceEntity.getStatus(), SampleStatusEnumType.class));
    }
}
