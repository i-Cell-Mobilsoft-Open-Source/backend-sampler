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
package hu.icellmobilsoft.sampler.common.rest.cdi;

import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.rest.cdi.BaseRequestContainer;
import hu.icellmobilsoft.sampler.common.rest.header.ProjectHeader;

/**
 * Common request scope container
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Model
public class RequestContainer {

    /**
     * Http request parsed http header object
     */
    private ProjectHeader projectHeader;

    /**
     * Default Http request parsed http header object
     */
    private ProjectHeader defaultProjectHeader = new ProjectHeader();

    @Inject
    private BaseRequestContainer baseRequestContainer;

    /**
     * Get http request parsed http header object
     * 
     * @return parsed http header object
     */
    @Produces
    @RequestScoped
    public ProjectHeader getProjectHeader() {
        // There are cases when the header is not loaded, yet we still access the dictionary
        // In this case, WELD-000052 error occurred
        if (projectHeader == null) {
            return defaultProjectHeader;
        }
        return projectHeader;
    }

    /**
     * Set parsed http header object
     * 
     * @param projectHeader
     *            parsed http header object
     */
    public void setProjectHeader(ProjectHeader projectHeader) {
        this.projectHeader = projectHeader;
    }

    /**
     * Get all container object from all scope storage
     * 
     * @return all key/value from all scope storage
     * @see BaseRequestContainer#getObjectMap()
     */
    public Map<String, Object> getObjectMap() {
        return baseRequestContainer.getObjectMap();
    }

    /**
     * Getter for the field requestObject.
     * 
     * @return Http request object.
     * @see BaseRequestContainer#getRequestObject()
     */
    public Object getRequestObject() {
        return baseRequestContainer.getRequestObject();
    }

    /**
     * Setter for the field requestObject
     * 
     * @param requestObject
     *            Http request object
     * @see BaseRequestContainer#setRequestObject(Object)
     */
    public void setRequestObject(Object requestObject) {
        baseRequestContainer.setRequestObject(requestObject);
    }
}
