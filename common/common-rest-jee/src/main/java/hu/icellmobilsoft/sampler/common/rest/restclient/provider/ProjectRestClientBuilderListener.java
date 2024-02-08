/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.common.rest.restclient.provider;

import hu.icellmobilsoft.coffee.module.mp.restclient.provider.DefaultRestClientBuilderListener;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

/**
 * Project REST client listener
 *
 * @author imre.scheffer
 * @see DefaultRestClientBuilderListener
 * @since 0.1.0
 */
public class ProjectRestClientBuilderListener extends DefaultRestClientBuilderListener {

    @Override
    public void onNewBuilder(RestClientBuilder builder) {
        super.onNewBuilder(builder);
        builder.register(ProjectBaseExceptionResponseExceptionMapper.class);
        builder.register(ProjectSettingClientRequestFilter.class);
    }
}
