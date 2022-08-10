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
package hu.icellmobilsoft.sampler.sample.restservice.rest.validation.xml.utils;

import java.io.InputStream;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Model;

import org.apache.commons.lang3.StringUtils;
//import org.apache.deltaspike.core.util.PropertyFileUtils;
import org.w3c.dom.DOMImplementationSource;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import hu.icellmobilsoft.coffee.se.logging.Logger;

/**
 * XSD schemalocation based resource resolver.<br>
 * resolveResource This class implements a SAX EntityResolver, StAX XMLResolver, Schema Validation LSResourceResolver and Transform URIResolver. <br>
 * For multi module projekt {@link Alternative} activation need own class like:
 * 
 * <pre>
 * &#64;Priority(100)
 * &#64;Alternative
 * public class ProjectXsdResourceResolver extends XsdResourceResolver {
 * }
 * </pre>
 *
 * @author cstamas
 * @author robert.kaplar
 * @since 1.0.0
 */
@Model
public class CustomXsdResourceResolver implements LSResourceResolver, IXsdResourceResolver {
    private static Logger log = Logger.getLogger(CustomXsdResourceResolver.class);
    private String xsdDirPath;

    /**
     * {@inheritDoc}
     *
     * Xsd feloldás
     */
    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {

//        if (StringUtils.isBlank(systemId)) {
//            return null;
//        }
        DOMImplementationLS domImplLS = getDOMImplementationLS();
        final LSInput input = domImplLS.createLSInput();
        
//        ClassLoader classLoader = null;
        
        InputStream resStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(xsdDirPath);
//        InputStream resStream = classLoader.getResourceAsStream(xsdDirPath);
        input.setByteStream(resStream);
        input.setSystemId(systemId);
        return input;
        // ClassLoader classLoader = ClassUtils.getClassLoader(null);

    }

    private DOMImplementationLS getDOMImplementationLS() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
            final DOMImplementationRegistry registry;
            registry = DOMImplementationRegistry.newInstance();
            return (DOMImplementationLS) registry.getDOMImplementation("LS");
        } catch (ClassNotFoundException cnfe) {
            // This is an ugly workaround of this bug: https://issues.jboss.org/browse/WFLY-4416
            try {
                Class<?> sysImpl = classLoader.loadClass("com.sun.org.apache.xerces.internal.dom.DOMXSImplementationSourceImpl");
                DOMImplementationSource source = (DOMImplementationSource) sysImpl.newInstance();
                return (DOMImplementationLS) source.getDOMImplementation("LS");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setXsdDirPath(String xsdDirPath) {
        this.xsdDirPath = xsdDirPath;
    }
}
