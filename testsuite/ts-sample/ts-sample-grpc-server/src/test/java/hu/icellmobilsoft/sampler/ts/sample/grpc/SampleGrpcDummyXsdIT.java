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
package hu.icellmobilsoft.sampler.ts.sample.grpc;

import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.google.protobuf.Timestamp;

import hu.icellmobilsoft.coffee.dto.common.commonservice.XsdProtoWrapper.ContextType;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.roaster.restassured.BaseConfigurableWeldIT;
import hu.icellmobilsoft.sampler.common.sample.rest.post.XsdProtoWrapper.SampleCoreType;
import hu.icellmobilsoft.sampler.common.sample.rest.post.XsdProtoWrapper.SampleRequestType;
import hu.icellmobilsoft.sampler.common.sample.rest.post.XsdProtoWrapper.SampleType;
import hu.icellmobilsoft.sampler.common.sample.rest.post.XsdProtoWrapper.SampleValueEnumType;
import hu.icellmobilsoft.sampler.common.sample.xsd.grpc.DummyXsdRequest;
import hu.icellmobilsoft.sampler.common.sample.xsd.grpc.DummyXsdResponse;
import hu.icellmobilsoft.sampler.common.sample.xsd.grpc.DummyXsdServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Sample XSD grpc test
 *
 * @author balazs.joo
 * @since 2.0.0
 */
@DisplayName("Testing Sample grpc server")
@Tag("grpc")
@TestInstance(Lifecycle.PER_CLASS)
class SampleGrpcDummyXsdIT extends BaseConfigurableWeldIT {

    private static final Logger LOGGER = Logger.getLogger(SampleGrpcDummyXsdIT.class);

    @Inject
    @ConfigProperty(name = "testsuite.grpc.sampleGrpcServerService.grpcServer.host", defaultValue = "localhost")
    private String grpcServerHost;

    @Inject
    @ConfigProperty(name = "testsuite.grpc.sampleGrpcServerService.grpcServer.port", defaultValue = 8199 + "")
    private Integer grpcServerPort;

    private ManagedChannel channel;

    @BeforeAll
    public void init() {
        channel = ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort).usePlaintext().build();
    }

    @AfterAll
    public void close() {
        channel.shutdown();
    }

    @Test
    @DisplayName("test dummy grpc service")
    void testDummyGrpcService() {

        DummyXsdRequest.Builder reqBuilder = DummyXsdRequest.newBuilder();

        SampleRequestType.Builder sampleReqBuilder = SampleRequestType.newBuilder();
        sampleReqBuilder.setContext(ContextType.newBuilder().setRequestId(RandomUtil.generateId()).setTimestamp(Timestamp.newBuilder().build()));
        SampleCoreType.Builder sampleBuilder = SampleCoreType.newBuilder();
        sampleBuilder.setColumnA("ColumnA");
        sampleBuilder.setColumnB(SampleValueEnumType.SAMPLE_VALUE_ENUM_TYPE_UNSPECIFIED);
        sampleReqBuilder.setSample(sampleBuilder);
        reqBuilder.setRequest(sampleReqBuilder);

        DummyXsdRequest dummyRequest = reqBuilder.build();
        DummyXsdServiceGrpc.DummyXsdServiceBlockingStub stub = DummyXsdServiceGrpc.newBlockingStub(channel);
        DummyXsdResponse helloResponse = stub.getXsdDummy(dummyRequest);

        SampleType sample = helloResponse.getResponse().getSample();

        Assertions.assertEquals("ColumnA", sample.getColumnA());
        Assertions.assertEquals(SampleValueEnumType.SAMPLE_VALUE_ENUM_TYPE_UNSPECIFIED, sample.getColumnB());
        Assertions.assertNotNull(sample.getSampleId());
        Assertions.assertNotNull(sample.getSampleStatus());
    }
}
