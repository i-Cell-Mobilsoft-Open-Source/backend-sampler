/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2023 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.common;

import java.time.LocalDateTime;

import jakarta.enterprise.inject.spi.CDI;

import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;
import org.eclipse.microprofile.metrics.Tag;

import hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.bundle.MetricBundle;
import io.grpc.MethodDescriptor;

/**
 * Abstract class for metric interceptors to help collect metrics. Handles request/response/duration metrics.
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
public abstract class AbstractMetricInterceptor {

    private static final String TAG_METHOD = "method";
    private static final String TAG_METHOD_TYPE = "methodType";
    private static final String TAG_SERVICE = "service";
    private static final String TAG_STATUS = "status";

    /**
     * Create metric bundle for the interceptors, metric based on mp-metrics
     * 
     * @param methodDescriptor
     *            to fill metric data
     * @return created MetricBundle with microprofile metric data
     */
    protected MetricBundle createMetricBundle(MethodDescriptor<?, ?> methodDescriptor) {
        MetricBundle metricBundle = new MetricBundle();
        metricBundle.setStartTime(LocalDateTime.now());

        // microprofile metric
        MetricRegistry metricRegistry = CDI.current().select(MetricRegistry.class).get();

        // lehetséges hogy cache fog itt kelleni hogy gyorsabb legyen
        // request counter
        Tag method = new Tag(TAG_METHOD, methodDescriptor.getBareMethodName());
        Tag methodType = new Tag(TAG_METHOD_TYPE, methodDescriptor.getType().name());
        Tag serviceName = new Tag(TAG_SERVICE, methodDescriptor.getServiceName());
        Metadata requestMeta = Metadata.builder().withName(getRequestMetadataName()).withDescription(getRequestMetadataName())
                .withType(MetricType.COUNTER).build();
        metricBundle.setRequestCounter(metricRegistry.counter(requestMeta, method, methodType, serviceName));

        // response counter
        Metadata responseMeta = Metadata.builder().withName(getResponseMetadataName()).withDescription(getResponseMetadataName())
                .withType(MetricType.COUNTER).build();
        metricBundle.setResponseCounter(metricRegistry.counter(responseMeta, method, methodType, serviceName));

        // timer
        Metadata timerMeta = Metadata.builder().withName(getTimerMetadataName()).withDescription(getTimerMetadataName()).withType(MetricType.TIMER)
                .build();
        metricBundle
                .setTimerCodeFunction((code) -> metricRegistry.timer(timerMeta, method, methodType, serviceName, new Tag(TAG_STATUS, code.name())));

        return metricBundle;

    }

    protected abstract String getRequestMetadataName();

    protected abstract String getResponseMetadataName();

    protected abstract String getTimerMetadataName();

}
