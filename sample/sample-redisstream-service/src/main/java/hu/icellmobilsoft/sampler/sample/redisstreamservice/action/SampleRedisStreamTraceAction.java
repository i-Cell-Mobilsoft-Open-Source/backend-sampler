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
package hu.icellmobilsoft.sampler.sample.redisstreamservice.action;

import hu.icellmobilsoft.coffee.cdi.trace.annotation.Traced;
import hu.icellmobilsoft.coffee.cdi.trace.constants.SpanAttribute;
import hu.icellmobilsoft.coffee.cdi.trace.spi.ITraceHandler;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Sample trace
 * 
 * @author czenczl
 * @since 2.0.0
 */
@ApplicationScoped
public class SampleRedisStreamTraceAction {

    @Inject
    private Logger log;

    @Inject
    private ITraceHandler traceHandler;

    /**
     * sample traced method
     */
    @Traced
    public void tracedMethod() {
        log.debug("method traced");

        Traced traced = new Traced.Literal("thread", SpanAttribute.INTERNAL, "NAN");
        String operation = "tracedMethod2";
        traceHandler.runWithTrace(() -> this.tracedMethod2(), traced, operation);
    }

    /**
     * Sample method
     * 
     * @return sample
     */
    public String tracedMethod2() {
        return "sample";
    }

}
