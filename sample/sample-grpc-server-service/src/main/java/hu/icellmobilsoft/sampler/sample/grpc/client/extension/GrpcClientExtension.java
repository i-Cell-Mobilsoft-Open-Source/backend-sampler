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
package hu.icellmobilsoft.sampler.sample.grpc.client.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.build.compatible.spi.Types;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.ProcessInjectionPoint;

import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.sample.grpc.client.GrpcClient;

/**
 * Extension for gRPC client injection
 * 
 * @author czenczl
 * @since 2.0.0
 */
public class GrpcClientExtension implements Extension {

    private static final Logger LOGGER = Logger.getLogger(GrpcClientExtension.class);

    public List<Types> grpcClientTypes = new ArrayList<>();
    private Map<Type, Annotation> grpcClientMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public void afterBeanDiscovery(@Observes final AfterBeanDiscovery abd, BeanManager beanManager) {
        LOGGER.info("gRPC client extension is active");

        // find producer template
        AnnotatedMethod<? super GrpcClientProducerFactory> producerMethodTemplate = findProducerMethodTemplate(beanManager);
        final BeanAttributes<?> producerAttributes = beanManager.createBeanAttributes(producerMethodTemplate);

        // get grpc client template
        @SuppressWarnings("rawtypes")   
        Bean producerFactory = beanManager.getBeans(GrpcClientProducerFactory.class, new Default.Literal()).iterator().next();

        for (Type type : grpcClientMap.keySet()) {
            Bean<?> bean = beanManager.createBean(new GrpcDelegatingBeanAttributes<>(producerAttributes) {
                @Override
                public final Set<Type> getTypes() {
                    final Set<Type> types = new HashSet<>();
                    types.add(Object.class);
                    types.add(type);
                    return types;
                }

                @Override
                public Class<? extends Annotation> getScope() {
                    // A producer method with a parameterized return type with a type variable must be declared @Dependent scoped
                    return Dependent.class;
                }

            }, GrpcClientProducerFactory.class, beanManager.getProducerFactory(producerMethodTemplate, producerFactory));

            // add producer bean
            abd.addBean(bean);
        }

    }

    public <T, X> void processInjectionTarget(final @Observes ProcessInjectionPoint<T, X> pip) {
        InjectionPoint ip = pip.getInjectionPoint();

        for (Annotation annotation : ip.getQualifiers()) {
            if (annotation.annotationType() == GrpcClient.class) {
                GrpcClient grpcClient = (GrpcClient) annotation;
                grpcClientMap.put(ip.getType(), grpcClient);
            }
        }

    }

    private AnnotatedMethod<? super GrpcClientProducerFactory> findProducerMethodTemplate(BeanManager beanManager) {
        // get producer template method
        AnnotatedType<GrpcClientProducerFactory> factory = beanManager.createAnnotatedType(GrpcClientProducerFactory.class);
        Set<AnnotatedMethod<? super GrpcClientProducerFactory>> methods = factory.getMethods();

        // find method by return type
        return methods.stream().filter(m -> m.getJavaMember().getReturnType() == io.grpc.stub.AbstractBlockingStub.class).findFirst().get();
    }

}
