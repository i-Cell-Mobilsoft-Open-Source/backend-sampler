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

import hu.icellmobilsoft.sampler.grpc.observability.mp.metrics.bundle.MetricBundle;
import io.grpc.ClientCall;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;

/**
 * gRPC client call that will collect metrics using microprofile-metrics api
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
public class MetricClientCall<ReqT, RespT> extends ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT> {

    private MetricBundle metricBundle;

    /**
     * Creates a new client call to collect metrics
     * 
     * @param delegate
     *            original call
     * @param metricBundle
     *            counter and timer function container
     */
    public MetricClientCall(ClientCall<ReqT, RespT> delegate, MetricBundle metricBundle) {
        super(delegate);
        this.metricBundle = metricBundle;
    }

    @Override
    public void start(Listener<RespT> responseListener, Metadata headers) {
        super.start(new MetricClientCallListener<>(responseListener, metricBundle), headers);
    }

    @Override
    public void sendMessage(ReqT message) {
        metricBundle.getRequestCounter().inc();
        super.sendMessage(message);
    }

}
