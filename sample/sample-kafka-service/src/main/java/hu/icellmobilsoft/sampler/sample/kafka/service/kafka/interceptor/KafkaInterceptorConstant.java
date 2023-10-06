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
package hu.icellmobilsoft.sampler.sample.kafka.service.kafka.interceptor;

/**
 * Kfaka interceptor constant register
 * 
 * @author Imre Scheffer
 */
public interface KafkaInterceptorConstant {
    /**
     * Tab space
     */
    public static String TAB = "  ";
    /**
     * Tab space
     */
    public static String VALUE = ": ";
    /**
     * New line char
     */
    public static char NEWLINE = '\n';
    /**
     * Incoming row prefix
     */
    public static String INCOMING = ">> ";
    /**
     * Outgoing row prefix
     */
    public static String OUTGOING = "<< ";

}
