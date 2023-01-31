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

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import hu.icellmobilsoft.coffee.se.logging.Logger;

/**
 * Sample gRPC server executor
 * 
 * @author czenczl
 * @since 2.0.0
 *
 */
@Dependent
public class GrpcSampleServerExecutor implements IGrpcServerExecutor {

    @Inject
    private Logger log;

    @Inject
    @GrpcServer(configKey = "sample.grpc")
    private GrpcServerManager grpcServerManager;

    @Override
    public void run() {

        // sajat szerver kezeles implementacio, ennek a kezelése folyamatban van JakartaEE alatt
        // egy mienkhez hasonlo minta server management kezeles, amig nincs előrelépés Jakarta-ban
        // https://projects.eclipse.org/projects/ee4j.rpc/reviews/creation-review
        grpcServerManager.startServer();

    }

}
