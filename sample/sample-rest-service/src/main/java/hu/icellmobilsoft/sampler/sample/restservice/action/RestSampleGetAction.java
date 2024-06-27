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
package hu.icellmobilsoft.sampler.sample.restservice.action;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.dto.common.LogConstants;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.se.logging.mdc.MDC;
import hu.icellmobilsoft.coffee.se.util.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleValueEnumType;
import hu.icellmobilsoft.sampler.sample.restservice.action.async.TraceAsyncEvent;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;

/**
 * Sample action
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Model
public class RestSampleGetAction extends BaseAction {

    @Inject
    private Tracer tracer;

    @Inject
    private Event<TraceAsyncEvent> event;

    /**
     * Dummy sample reponse
     * 
     * @return SampleResponse Sample response with random id
     * @throws BaseException
     *             if error
     */
    public SampleResponse sample() throws BaseException {
        Span span = tracer.spanBuilder("Sample GET action span").startSpan();

        SampleResponse response = new SampleResponse();

        SampleType sampleType = new SampleType();
        sampleType.setSampleId(RandomUtil.generateId());
        sampleType.setSampleStatus(SampleStatusEnumType.DONE);
        sampleType.setColumnA("A");
        sampleType.setColumnB(SampleValueEnumType.VALUE_A);
        response.setSample(sampleType);
        span.addEvent("construct response type", Attributes.of(AttributeKey.stringKey("sample.id"), sampleType.getSampleId()));

        // event, we use a new thred to demonstrate link example between traces
        TraceAsyncEvent traceEvent = new TraceAsyncEvent();
        traceEvent.setExtSessionId(MDC.get(LogConstants.LOG_SESSION_ID));
        traceEvent.setSpan(span);
        event.fireAsync(traceEvent);

        handleSuccessResultType(response);
        span.end();
        return response;
    }

    /**
     * Observe {@link TraceAsyncEvent}
     * 
     * @param event
     *            sample async event
     */
    public void observe(@ObservesAsync TraceAsyncEvent event) {
        MDC.put(LogConstants.LOG_SESSION_ID, event.getExtSessionId());
        SpanBuilder builder = tracer.spanBuilder("Sample Async event span");
        Span rootSpan = event.getSpan();
        // This link can be found in the Jaeger UI under the 'Span References' section.
        builder.addLink(rootSpan.getSpanContext());
        Span span = builder.startSpan();
        span.addEvent("sample event in async thread");
        span.end();
    }
}
