/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2024 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.restservice.action.async;

import io.opentelemetry.api.trace.Span;

/**
 * Sample trace async event
 * 
 * @author czenczl
 * @since 2.0.0
 */
public class TraceAsyncEvent {
    private Span span;
    private String extSessionId;

    /**
     * Gets the unique session id
     * 
     * @return span ext session id
     */
    public String getExtSessionId() {
        return extSessionId;
    }

    /**
     * Sets the unique session id
     * 
     * @param extSessionId
     *            unique id
     */
    public void setExtSessionId(String extSessionId) {
        this.extSessionId = extSessionId;
    }

    /**
     * Gets the {@link Span}
     * 
     * @return span
     */
    public Span getSpan() {
        return span;
    }

    /**
     * Sets the {@link Span} to use in async trace flow
     * 
     * @param span
     *            trace span
     */

    public void setSpan(Span span) {
        this.span = span;
    }
}
