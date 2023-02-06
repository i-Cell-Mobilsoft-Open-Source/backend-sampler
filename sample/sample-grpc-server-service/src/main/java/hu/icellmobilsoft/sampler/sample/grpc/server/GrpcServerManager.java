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
package hu.icellmobilsoft.sampler.sample.grpc.server;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.sample.grpc.server.config.GrpcServerConfig;
import hu.icellmobilsoft.sampler.sample.grpc.server.config.GrpcServerConnection;
import hu.icellmobilsoft.sampler.sample.grpc.server.service.IGrpcService;
import hu.icellmobilsoft.sampler.sample.grpc.server.service.interceptor.ServerRequestInterceptor;
import hu.icellmobilsoft.sampler.sample.grpc.server.service.interceptor.ServerResponseInterceptor;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

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
    @ConfigProperty(name = "coffee.grpc.server.port", defaultValue = 8199 + "")
    private Integer grpcServerPort;

    private Server server;

    /**
     * szerver inicializacio, port bind, servicek hozzáadása a szerverhez, interceptorok definialasa
     * 
     */
    public void init() throws BaseException {
        // grpc servicek gyujtese
        Set<Bean<?>> beans = beanManager.getBeans(IGrpcService.class);
        log.info("Found [{0}] grpc service", beans.size());
        // bind to port
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(grpcServerPort);
        
        //configure server
        configureServer(serverBuilder);
        // add interceptor
        addInterceptor(serverBuilder);
        // add services
        beans.forEach((i) -> addService(i, serverBuilder));
        // build server
        server = serverBuilder.build();
    }

    private void configureServer(ServerBuilder<?> serverBuilder) throws BaseException {
        // server configs
        Instance<GrpcServerConfig> configInstance = CDI.current().select(GrpcServerConfig.class, new GrpcServerConnection.Literal("server"));
        GrpcServerConfig config = configInstance.get();

        // NettyServerBuilder
        serverBuilder.maxConnectionAge(config.getMaxConnectionAge(), TimeUnit.SECONDS);
        serverBuilder.maxConnectionAgeGrace(config.getMaxConnectionAgeGrace(), TimeUnit.SECONDS);
        serverBuilder.keepAliveTime(config.getKeepAliveTime(), TimeUnit.MINUTES);
        serverBuilder.keepAliveTimeout(config.getKeepAliveTimeout(), TimeUnit.SECONDS);
        serverBuilder.maxConnectionIdle(config.getMaxConnectionIdle(), TimeUnit.SECONDS);
        serverBuilder.maxInboundMessageSize(config.getMaxInboundMessageSize());
        serverBuilder.maxInboundMetadataSize(config.getMaxInboundMetadataSize());
        serverBuilder.permitKeepAliveTime(config.getPermitKeepAliveTime(), TimeUnit.MINUTES);
        serverBuilder.permitKeepAliveWithoutCalls(config.isPermitKeepAliveWithoutCalls());
        
        configInstance.destroy(config);
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
        BindableService service = (BindableService) beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean));
        serverBuilder.addService(service);

    }

    /**
     * request/response minta interceptor, ezzel ovatosan kell banni, mert fura módon fordított sorrendben hivodik a hívási lanc, és többször is
     * befuthat ide egy folyamatban
     * 
     * @param serverBuilder
     *            szerver builder
     */
    private void addInterceptor(ServerBuilder<?> serverBuilder) {
        serverBuilder.intercept(new ServerResponseInterceptor());
        serverBuilder.intercept(new ServerRequestInterceptor());
    }

    /**
     * szerver indítás, és várakozás, mivel a szerver nem teljeskörűen managelt, ezert ha nem vart hiba történik valamiert akkor nem indul ujra
     */
    public void startServer() {
        try {
            server.start();
            server.awaitTermination();
            log.info("grpc server runnning");
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