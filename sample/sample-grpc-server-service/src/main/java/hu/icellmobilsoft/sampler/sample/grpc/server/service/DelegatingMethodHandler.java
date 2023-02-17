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
package hu.icellmobilsoft.sampler.sample.grpc.server.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.sampler.common.grpc.core.extension.api.IGrpcService;
import io.grpc.BindableService;
import javassist.util.proxy.MethodHandler;

public class DelegatingMethodHandler implements MethodHandler {

    private final Map<MethodKey, Method> delegateMethods = new HashMap<>();
    private final IGrpcService cdiServiceDelegate;

    public DelegatingMethodHandler(IGrpcService cdiServiceDelegate) throws TechnicalException {
        this.cdiServiceDelegate = cdiServiceDelegate;
        Class<? extends IGrpcService> delegateClass = cdiServiceDelegate.getClass();
        Class<? extends BindableService> delegator = cdiServiceDelegate.bindableDelegator();
        Method[] methods = delegator.getMethods();
        if (ArrayUtils.isEmpty(methods)) {
            throw new TechnicalException(CoffeeFaultType.INVALID_INPUT,
                    MessageFormat.format("GRPC BaseImpl [{0}] does not have any methods!", delegator));
        }
        for (Method method : methods) {
            if (!Modifier.isFinal(method.getModifiers())) {
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                try {
                    Method delegateClassMethod = delegateClass.getMethod(methodName, parameterTypes);
                    delegateMethods.put(MethodKey.forMethod(method), delegateClassMethod);
                } catch (NoSuchMethodException e) {
                    throw new TechnicalException(CoffeeFaultType.INVALID_INPUT,
                            MessageFormat.format("GRPC service [{0}] does not declare delegate for method [{1}] defined in BaseImpl [{2}]!",
                                    delegateClass, method, delegator));
                }
            }
        }
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        Method delegateMethod = findDelegateMethod(thisMethod);
        try {
            return delegateMethod.invoke(cdiServiceDelegate, args);
        } catch (InvocationTargetException e) {
            if (e.getCause() != null) {
                throw e.getCause();
            }
            throw e;
        }
    }

    private Method findDelegateMethod(Method thisMethod) throws TechnicalException {
        MethodKey key = MethodKey.forMethod(thisMethod);
        if (!delegateMethods.containsKey(key)) {
            throw new TechnicalException(CoffeeFaultType.INVALID_INPUT,
                    MessageFormat.format("GRPC service [{0}] does not declare delegate for method [{1}] defined in BaseImpl [{2}]!",
                            cdiServiceDelegate, thisMethod, cdiServiceDelegate.bindableDelegator()));
        }
        Method method = delegateMethods.get(key);
        return method;
    }
}
