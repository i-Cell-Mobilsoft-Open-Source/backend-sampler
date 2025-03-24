/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2025 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.common.rest.locale;

import java.util.Locale;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;
import jakarta.interceptor.Interceptor;
import jakarta.mvc.locale.LocaleResolver;
import jakarta.mvc.locale.LocaleResolverContext;

/**
 * Jakarta MVC locale resolver
 */
@Dependent
@Priority(Interceptor.Priority.APPLICATION + 10)
@Alternative
public class MvcLocaleResolver implements LocaleResolver {

    /**
     * Resolves Locale by MVC LocalResorver
     *
     * @param localeResolverContext
     *            {@link LocaleResolverContext}
     * @return {@link Locale}
     */
    @Override
    public Locale resolveLocale(LocaleResolverContext localeResolverContext) {
        final String queryLang = localeResolverContext.getUriInfo().getQueryParameters().getFirst("lang");
        return queryLang != null ? Locale.forLanguageTag(queryLang) : null;
    }
}
