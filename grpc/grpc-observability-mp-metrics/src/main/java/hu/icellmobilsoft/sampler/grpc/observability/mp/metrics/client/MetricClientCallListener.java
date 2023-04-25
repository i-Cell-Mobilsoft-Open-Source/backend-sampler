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
package hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.client;

import java.time.Duration;
import java.time.LocalDateTime;

import hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.bundle.MetricBundle;
import io.grpc.ClientCall.Listener;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.Status;

/**
 * gRPC client listener that will collect metrics using microprofile-metrics api
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
public class MetricClientCallListener<ReqT> extends SimpleForwardingClientCallListener<ReqT> {

    private MetricBundle metricBundle;

    /**
     * Creates a new cleint listener to collect metrics
     * 
     * @param delegate
     *            original call
     * @param metricBundle
     *            counter and timer function container
     */
    public MetricClientCallListener(Listener<ReqT> delegate, MetricBundle metricBundle) {
        super(delegate);
        this.metricBundle = metricBundle;
    }

    @Override
    public void onClose(Status status, Metadata trailers) {
        metricBundle.getTimerCodeFunction().apply(status.getCode()).update(Duration.between(metricBundle.getStartTime(), LocalDateTime.now()));
        super.onClose(status, trailers);
    }

    @Override
    public void onMessage(ReqT message) {

        metricBundle.getResponseCounter().inc();
        super.onMessage(message);
    }

}
