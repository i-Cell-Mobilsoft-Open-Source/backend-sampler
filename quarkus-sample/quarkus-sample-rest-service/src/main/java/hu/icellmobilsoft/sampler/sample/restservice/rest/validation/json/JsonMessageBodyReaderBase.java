/*-
 * #%L
 * Coffee
 * %%
 * Copyright (C) 2020 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.restservice.rest.validation.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.MessageFormat;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.ArrayUtils;
import org.jboss.logging.Logger;

import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.sampler.sample.restservice.rest.utils.gson.JsonUtil;
import hu.icellmobilsoft.sampler.sample.restservice.rest.validation.xml.JaxbTool;
import hu.icellmobilsoft.sampler.sample.restservice.rest.validation.xml.XmlMessageBodyReaderBase;
import hu.icellmobilsoft.sampler.sample.restservice.rest.validation.xml.annotation.ValidateXML;
import hu.icellmobilsoft.sampler.sample.restservice.rest.validation.xml.exception.XsdProcessingException;
import hu.icellmobilsoft.sampler.sample.restservice.rest.validation.xml.exception.XsdProcessingExceptionWrapper;
import hu.icellmobilsoft.sampler.sample.restservice.rest.validation.xml.reader.IJsonRequestVersionReader;

/**
 * JSON kiterjesztése az XML/XSD alapon működő megvalósításnak.<br>
 * Működési alapja hogy a JSON inputStream-ből DTO class keletkezik majd annak az értékei validációs XML marshaller-re van küldve.
 *
 * <pre>
 * &#64;Provider
 * &#64;Consumes({ MediaType.APPLICATION_JSON })
 * &#64;Priority(Priorities.ENTITY_CODER)
 * public class JsonRequestMessageBodyReader extends JsonMessageBodyReaderBase&lt;BasicRequestType&gt; {
 * }
 * </pre>
 *
 * <br>
 *
 * @see XmlMessageBodyReaderBase
 * @author m.petrenyi
 * @author imre.scheffer
 * @since 1.0.0
 */
@Provider
@Consumes({ MediaType.APPLICATION_JSON })
@Priority(Priorities.ENTITY_CODER)
public class JsonMessageBodyReaderBase<BaseRequestType> implements MessageBodyReader<BaseRequestType> {

    @Inject
    private Logger log;

    @Inject
    private JaxbTool jaxbTool;

    @Inject
    private IJsonRequestVersionReader jsonRequestVersionReader;

    /**
     * {@inheritDoc}
     *
     * Xsd validálásra kerülhet-e?
     */
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return ArrayUtils.isNotEmpty(XmlMessageBodyReaderBase.getValidateIfPresent(annotations));
    }

    /**
     * {@inheritDoc}
     *
     * Ha van ValidateXML annotáció, akkor azt használja
     */
    @Override
    public BaseRequestType readFrom(Class<BaseRequestType> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) {
        try {
            ValidateXML[] validates = XmlMessageBodyReaderBase.getValidateIfPresent(annotations);
            BaseRequestType obj = deserializeJson(type, entityStream);
            String requestVersion = readRequestVersion(obj);
            String schemaPath = jaxbTool.getXsdPath(validates, requestVersion);
            // TODO LS resolver fel kell oldani az xsdt
            // jaxbTool.marshalXML(obj, schemaPath);
            return obj;
        } catch (XsdProcessingException e) {
            throw new XsdProcessingExceptionWrapper(e);
        }
    }

    /**
     * Reads request version from request entity.
     * 
     * @param object
     *            object to read from
     * @return request version as {@link String}
     * @throws XsdProcessingException
     *             if version cannot be read from the object
     */
    protected String readRequestVersion(Object object) throws XsdProcessingException {
        try {
            return jsonRequestVersionReader.readFromJSON(object);
        } catch (TechnicalException e) {
            throw new XsdProcessingException(CoffeeFaultType.INVALID_INPUT,
                    MessageFormat.format("Error in reading object [class: {0}]: [{1}]", object.getClass(), e.getLocalizedMessage()), e);
        }
    }

    /**
     * Creates object from json inputStream.
     * 
     * @param type
     *            type to deserialize into
     * @param entityStream
     *            input stream of entity
     * @return deserialized object
     * @throws XsdProcessingException
     *             if the json can not be deserialized
     */
    protected BaseRequestType deserializeJson(Class<BaseRequestType> type, InputStream entityStream) throws XsdProcessingException {
        try {
            return JsonUtil.toObjectGson(new InputStreamReader(entityStream), type);
        } catch (Exception e) {
            throw new XsdProcessingException(CoffeeFaultType.INVALID_INPUT, e.getMessage(), e);
        }
    }

}
