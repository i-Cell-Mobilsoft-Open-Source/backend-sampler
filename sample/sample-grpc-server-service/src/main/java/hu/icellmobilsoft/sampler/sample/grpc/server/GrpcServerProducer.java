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

import java.util.Objects;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.se.logging.Logger;

/**
 * Produce {@link GrpcServerManager}
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
@ApplicationScoped
public class GrpcServerProducer {

    @Inject
    private Logger log;

    /**
     * Produce GrpcServerManager
     * 
     * @return GrpcServerManager
     * @throws BaseException
     *             on error
     */
    @Produces
    @GrpcServer(configKey = "")
    public GrpcServerManager produceServer() throws BaseException {
        // note config kulcsot nem hasznaljuk jelenleg semmire
        Instance<GrpcServerManager> instance = CDI.current().select(GrpcServerManager.class);

        GrpcServerManager serverManager = instance.get();
        serverManager.init();

        log.info("Grpc server initialized");

        return serverManager;
    }

    /**
     * Disposes GrpcServerManager
     * 
     * @param grpcServerManager
     *            to dispose
     */
    public void returnResource(@Disposes @GrpcServer(configKey = "") GrpcServerManager grpcServerManager) {
        if (Objects.nonNull(grpcServerManager)) {
            grpcServerManager.stopServer();
            CDI.current().destroy(grpcServerManager);
        }
    }

}
