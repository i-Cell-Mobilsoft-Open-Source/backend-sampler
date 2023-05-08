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
package hu.icellmobilsoft.sampler.grpc.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.grpc.core.extension.api.IGrpcService;
import hu.icellmobilsoft.sampler.grpc.core.extension.metric.api.IMetricInterceptor;
import hu.icellmobilsoft.sampler.grpc.core.extension.metric.api.ServerMetricInterceptorQualifier;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.IOpentracingInterceptor;
import hu.icellmobilsoft.sampler.grpc.core.extension.opentracing.api.ServerOpentracingInterceptorQualifier;
import hu.icellmobilsoft.sampler.grpc.server.config.GrpcServerConfig;
import hu.icellmobilsoft.sampler.grpc.server.config.GrpcServerConnection;
import hu.icellmobilsoft.sampler.grpc.server.interceptor.ErrorHandlerInterceptor;
import hu.icellmobilsoft.sampler.grpc.server.interceptor.ServerRequestInterceptor;
import hu.icellmobilsoft.sampler.grpc.server.interceptor.ServerResponseInterceptor;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;

/**
 * Sample gRPC server manager
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
@Dependent
public class GrpcServerManager {

    @Inject
    private Logger log;

    @Inject
    private BeanManager beanManager;

    @Inject
    @GrpcServerConnection(configKey = "server")
    private GrpcServerConfig serverConfig;

    @Resource
    private ManagedExecutorService managedExecutorService;

    private Server server;

    /**
     * szerver inicializacio, port bind, servicek hozzáadása a szerverhez, interceptorok definialasa
     * 
     * @throws BaseException
     *             on error
     */
    public void init() throws BaseException {
        // grpc servicek gyujtese
        Set<Bean<?>> beans = beanManager.getBeans(IGrpcService.class);
        log.info("Found [{0}] grpc service", beans.size());
        // bind to port
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(serverConfig.getPort());
        // configure server threadpool
        configureServerPool(serverBuilder);
        // configure server
        configureServer(serverBuilder);
        // add interceptor
        addInterceptor(serverBuilder);
        // add services
        beans.forEach((i) -> addService(i, serverBuilder));
        // build server
        server = serverBuilder.build();
    }

    private void configureServer(ServerBuilder<?> serverBuilder) throws BaseException {
        // NettyServerBuilder
        // server config
        serverBuilder.maxConnectionAge(serverConfig.getMaxConnectionAge(), TimeUnit.SECONDS);
        serverBuilder.maxConnectionAgeGrace(serverConfig.getMaxConnectionAgeGrace(), TimeUnit.SECONDS);
        serverBuilder.keepAliveTime(serverConfig.getKeepAliveTime(), TimeUnit.MINUTES);
        serverBuilder.keepAliveTimeout(serverConfig.getKeepAliveTimeout(), TimeUnit.SECONDS);
        serverBuilder.maxConnectionIdle(serverConfig.getMaxConnectionIdle(), TimeUnit.SECONDS);
        serverBuilder.maxInboundMessageSize(serverConfig.getMaxInboundMessageSize());
        serverBuilder.maxInboundMetadataSize(serverConfig.getMaxInboundMetadataSize());
        serverBuilder.permitKeepAliveTime(serverConfig.getPermitKeepAliveTime(), TimeUnit.MINUTES);
        serverBuilder.permitKeepAliveWithoutCalls(serverConfig.isPermitKeepAliveWithoutCalls());
    }

    private void configureServerPool(ServerBuilder<?> serverBuilder) throws BaseException {
        if (serverConfig.isThreadPoolJakartaActive()) {
            serverBuilder.executor(managedExecutorService);
            log.info("gRPC server using Jakarta ManagedExecutorService.");
        } else {
            serverBuilder.executor(createThreadPool());
            log.info("gRPC server using default ThreadPoolExecutor.");
        }
    }

    // simple executor to control server threads
    private Executor createThreadPool() throws BaseException {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        return new ThreadPoolExecutor(
                serverConfig.getThreadPoolCorePoolSize(),
                serverConfig.getThreadPoolMaximumPoolSize(),
                serverConfig.getThreadPoolKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory);
    }

    /**
     * servicek hozzaadasa a szerverhez, innentől figyelik a grpc muveleteket
     * 
     * @param bean
     *            gRPC service-t implementalo bean
     * @param serverBuilder
     *            szerver builder
     */
    private void addService(Bean<?> bean, ServerBuilder<?> serverBuilder) {
        IGrpcService service = (IGrpcService) beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean));
        Class<? extends BindableService> grpcImpl = service.bindableDelegator();
        Constructor<? extends BindableService> constructor = findConstructor(bean, grpcImpl);
        if (constructor != null) {
            try {
                BindableService bindableService = constructor.newInstance(service);
                serverBuilder.addService(bindableService);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.warn(
                        MessageFormat.format(
                                "Could not call constructor of BindableService [{0}], it must have a public constructor with one parameter of [{1}]",
                                grpcImpl,
                                bean.getBeanClass()),
                        e);
            }
        } else {
            log.warn(
                    "Could not find constructor of BindableService [{0}], it must have a public constructor with one parameter of [{1}]",
                    grpcImpl,
                    bean.getBeanClass());
        }

    }

    private Constructor<? extends BindableService> findConstructor(Bean<?> bean, Class<? extends BindableService> grpcImpl) {
        if (grpcImpl == null) {
            return null;
        }
        Constructor<?>[] constructors = grpcImpl.getConstructors();
        if (ArrayUtils.isEmpty(constructors)) {
            return null;
        }
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0] != null
                    && constructor.getParameterTypes()[0].isAssignableFrom(bean.getBeanClass())) {
                return (Constructor<? extends BindableService>) constructor;
            }
        }

        return null;
    }

    /**
     * request/response minta interceptor, ezzel ovatosan kell banni, mert fura módon fordított sorrendben hivodik a hívási lanc, és többször is
     * befuthat ide egy folyamatban
     * 
     * @param serverBuilder
     *            szerver builder
     */
    private void addInterceptor(ServerBuilder<?> serverBuilder) {
        serverBuilder.intercept(new ErrorHandlerInterceptor()); // 5
        serverBuilder.intercept(new ServerResponseInterceptor()); // 4
        serverBuilder.intercept(new ServerRequestInterceptor()); // 3
        // modul szintu dependency, mp-metric, micrometer, telemetry...
        Instance<IMetricInterceptor> instanceMetric = CDI.current().select(IMetricInterceptor.class, new ServerMetricInterceptorQualifier.Literal());
        if (instanceMetric.isResolvable()) {
            serverBuilder.intercept((ServerInterceptor) instanceMetric.get()); // 2
        } else {
            log.warn("Could not find Metric interceptor implementation for gRPC server.");
        }
        Instance<IOpentracingInterceptor> instanceOpentrace = CDI.current()
                .select(IOpentracingInterceptor.class, new ServerOpentracingInterceptorQualifier.Literal());
        if (instanceOpentrace.isResolvable()) {
            serverBuilder.intercept((ServerInterceptor) instanceOpentrace.get()); // 1
        } else {
            log.warn("Could not find Opentracing interceptor implementation for gRPC server.");
        }
    }

    /**
     * szerver indítás, és várakozás, mivel a szerver nem teljeskörűen managelt, ezert ha nem vart hiba történik valamiert akkor nem indul ujra
     */
    public void startServer() {
        try {
            server.start();
            log.info("grpc server runnning");
            server.awaitTermination();
        } catch (InterruptedException | IOException e) {
            log.error("grpc server error", e);
        } finally {
            server.shutdownNow();
        }
    }

    /**
     * stop
     */
    public void stopServer() {
        server.shutdownNow();
    }
}
