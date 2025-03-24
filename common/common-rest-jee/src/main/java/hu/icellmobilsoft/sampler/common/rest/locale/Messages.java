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

import java.util.ResourceBundle;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.mvc.MvcContext;

/**
 * Provides I18n messages for the UI per request. To get the correct locale, the method {@link MvcContext#getLocale()} is used. This method uses the
 * built-in {@link jakarta.mvc.locale.LocaleResolver} of the used MVC Implementation.
 *
 * @author Tobias Erdle
 * @see MvcContext#getLocale()
 * @see jakarta.mvc.locale.LocaleResolver
 */
@Dependent
public class Messages {

    private static final String BASE_NAME = "messages";

    @Inject
    private MvcContext mvcContext;

    /**
     * Get the assigned message to some key based on the {@link java.util.Locale} of the current request.
     *
     * @param key
     *            the message key to use
     * @return the correct translation assigned to the key for the request locale, a fallback translation or a placeholder for unknown keys.
     */
    public final String get(final String key) {
        final ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, mvcContext.getLocale());

        return bundle.containsKey(key) ? bundle.getString(key) : formatUnknownKey(key);
    }

    private static String formatUnknownKey(final String key) {
        return String.format("???%s???", key);
    }
}
