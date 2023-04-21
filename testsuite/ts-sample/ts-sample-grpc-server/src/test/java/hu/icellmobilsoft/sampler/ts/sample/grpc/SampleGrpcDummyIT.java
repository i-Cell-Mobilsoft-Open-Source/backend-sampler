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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;

import org.apache.commons.lang3.EnumUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.weld.junit5.ExplicitParamInjection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;

import hu.icellmobilsoft.coffee.dto.exception.AccessDeniedException;
import hu.icellmobilsoft.coffee.dto.exception.BONotFoundException;
import hu.icellmobilsoft.coffee.dto.exception.BusinessException;
import hu.icellmobilsoft.coffee.dto.exception.DtoConversionException;
import hu.icellmobilsoft.coffee.dto.exception.ServiceUnavailableException;
import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.roaster.restassured.BaseConfigurableWeldIT;
import hu.icellmobilsoft.sampler.grpc.api.service.error.ErrorServiceGrpc;
import hu.icellmobilsoft.sampler.grpc.api.service.error.RequestForError;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.BaseMessage;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyRequest;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyResponse;
import hu.icellmobilsoft.sampler.grpc.api.service.sample.DummyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;

/**
 * Sample grpc test
 *
 * @author czenczl
 * @since 2.0.0
 */
@DisplayName("Testing Sample grpc server")
@Tag("grpc")
@TestInstance(Lifecycle.PER_CLASS)
class SampleGrpcDummyIT extends BaseConfigurableWeldIT {

    private static final Logger LOGGER = Logger.getLogger(SampleGrpcDummyIT.class);

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

        DummyRequest.Builder reqBuilder = DummyRequest.newBuilder();

        BaseMessage.Builder baseMessageBuilder = BaseMessage.newBuilder();
        baseMessageBuilder.setAmount(3.14);
        baseMessageBuilder.setFirstName("first");
        baseMessageBuilder.setIsActive(true);
        baseMessageBuilder.setCount(50);

        LocalDate date = LocalDate.of(2022, 04, 14);
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(date.toEpochSecond(LocalTime.now(), ZoneOffset.UTC)).build();

        baseMessageBuilder.setDate(timestamp);
        reqBuilder.setRequest(baseMessageBuilder.build());

        DummyRequest dummyRequest = reqBuilder.build();
        DummyServiceGrpc.DummyServiceBlockingStub stub = DummyServiceGrpc.newBlockingStub(channel);
        DummyResponse helloResponse = stub.getDummy(dummyRequest);

        Assertions.assertEquals("first", helloResponse.getResponse().getFirstName());
        Assertions.assertEquals(true, helloResponse.getResponse().getIsActive());
        Assertions.assertEquals(3.14, helloResponse.getResponse().getAmount());
        Assertions.assertEquals(50, helloResponse.getResponse().getCount());
        Assertions.assertNotNull(helloResponse.getResponse().getDate());
    }

    @Test
    @DisplayName("test dummy grpc service multithread")
    void testDummyGrpcServiceMulti() throws InterruptedException {
        int thread = 2;
        ExecutorService service = Executors.newFixedThreadPool(thread);
        CountDownLatch latch = new CountDownLatch(thread);
        Instant start = Instant.now();
        for (int i = 0; i < thread; i++) {
            service.submit(() -> {

                DummyRequest.Builder reqBuilder = DummyRequest.newBuilder();

                BaseMessage.Builder baseMessageBuilder = BaseMessage.newBuilder();
                baseMessageBuilder.setAmount(3.14);
                baseMessageBuilder.setFirstName("first");
                baseMessageBuilder.setIsActive(true);
                baseMessageBuilder.setCount(50);

                LocalDate date = LocalDate.of(2022, 04, 14);
                Timestamp timestamp = Timestamp.newBuilder().setSeconds(date.toEpochSecond(LocalTime.now(), ZoneOffset.UTC)).build();

                baseMessageBuilder.setDate(timestamp);
                reqBuilder.setRequest(baseMessageBuilder.build());

                DummyRequest dummyRequest = reqBuilder.build();
                DummyServiceGrpc.DummyServiceBlockingStub stub = DummyServiceGrpc.newBlockingStub(channel);
                DummyResponse helloResponse = stub.getDummy(dummyRequest);

                latch.countDown();

            });
        }
        latch.await();

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        long sec = TimeUnit.MILLISECONDS.toSeconds(timeElapsed);
        LOGGER.info("duration: " + sec);

    }

    @Test
    @DisplayName("test dummy grpc service request scoped multithread")
    void testDummyGrpcServiceRequestMulti() throws InterruptedException {
        int thread = 2;
        ExecutorService service = Executors.newFixedThreadPool(thread);
        CountDownLatch latch = new CountDownLatch(thread);
        Instant start = Instant.now();
        for (int i = 0; i < thread; i++) {
            service.submit(() -> {

                DummyRequest.Builder reqBuilder = DummyRequest.newBuilder();

                BaseMessage.Builder baseMessageBuilder = BaseMessage.newBuilder();
                baseMessageBuilder.setAmount(3.14);
                baseMessageBuilder.setFirstName("first");
                baseMessageBuilder.setIsActive(true);
                baseMessageBuilder.setCount(50);

                LocalDate date = LocalDate.of(2022, 04, 14);
                Timestamp timestamp = Timestamp.newBuilder().setSeconds(date.toEpochSecond(LocalTime.now(), ZoneOffset.UTC)).build();

                baseMessageBuilder.setDate(timestamp);
                reqBuilder.setRequest(baseMessageBuilder.build());

                DummyRequest dummyRequest = reqBuilder.build();
                DummyServiceGrpc.DummyServiceBlockingStub stub = DummyServiceGrpc.newBlockingStub(channel);
                DummyResponse helloResponse = stub.getDummyRequestScope(dummyRequest);

                latch.countDown();

            });
        }
        latch.await();

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        long sec = TimeUnit.MILLISECONDS.toSeconds(timeElapsed);
        LOGGER.info("duration: " + sec);

    }

    @DisplayName("Test exception handling")
    @ParameterizedTest(name = "Testing exception: {0}, expecting status code {1}")
    @MethodSource("givenWeHaveExceptions")
    @ExplicitParamInjection
    void testErrorGrpc(Class<Exception> exceptionToThrow, Code expectedCode) throws InvalidProtocolBufferException {
        // given
        ErrorServiceGrpc.ErrorServiceBlockingStub stub = ErrorServiceGrpc.newBlockingStub(channel);

        String requestId = RandomUtil.generateId();
        RequestForError request = RequestForError.newBuilder()//
                .setRequestId(requestId)//
                .setExceptionMessage("Test error grpc")//
                .setRequestedExceptionClass(exceptionToThrow.getName())//
                .build();
        // when
        StatusRuntimeException thrown = Assertions.assertThrows(StatusRuntimeException.class, () -> stub.error(request));
        LOGGER.error("thrown: " + thrown.getMessage(), thrown);
        Throwable cause = thrown.getCause();
        if(cause != null) {
            LOGGER.error("cause: " + cause.getMessage(), cause);
        }
        // then
        com.google.rpc.Status status = StatusProto.fromThrowable(thrown);
        Assertions.assertEquals(expectedCode, Code.forNumber(status.getCode()));
        Assertions.assertNotNull(EnumUtils.getEnum(CoffeeFaultType.class, status.getMessage()));

        for (Any any : status.getDetailsList()) {
            if (any.is(ErrorInfo.class)) {
                ErrorInfo errorInfo = any.unpack(ErrorInfo.class);
                Assertions.assertNotNull(EnumUtils.getEnum(CoffeeFaultType.class, errorInfo.getReason()));
                Assertions.assertEquals("sample-service", errorInfo.getDomain());
                Assertions.assertEquals("Test error grpc", errorInfo.getMetadataOrThrow("exception"));
            }
        }
    }

    public Stream<Arguments> givenWeHaveExceptions() {
        return Stream.of(
                // BaseExceptionMapper
                Arguments.of(AccessDeniedException.class, Code.UNAUTHENTICATED), //
                Arguments.of(BONotFoundException.class, Code.NOT_FOUND), //
                Arguments.of(DtoConversionException.class, Code.INVALID_ARGUMENT), //
                Arguments.of(ServiceUnavailableException.class, Code.UNAVAILABLE), //
                Arguments.of(BusinessException.class, Code.FAILED_PRECONDITION), //
                Arguments.of(TechnicalException.class, Code.INTERNAL), //

                // GeneralExceptionMapper
                Arguments.of(ForbiddenException.class, Code.PERMISSION_DENIED), //
                Arguments.of(IllegalArgumentException.class, Code.INVALID_ARGUMENT), //
                Arguments.of(RuntimeException.class, Code.INTERNAL) //
        );
    }
}
