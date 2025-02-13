/*
 * Copyright BYOWares
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.byowares.game.utils.jfx.i18n;


import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Allow binding {@link javafx.beans.property.StringProperty} to {@code key} from {@link java.util.ResourceBundle}.
 * On language changed, properties will therefore be automatically be updated.
 *
 * @since XXX
 */
public abstract class I18NResourceBundle {

    private final Map<Locale, ResourceBundle> resourceBundles;

    /**
     * Loads {@link java.util.ResourceBundle} for each supported {@link java.util.Locale}.
     *
     * @param baseName The name of the {@link java.util.ResourceBundle} to load.
     *
     * @see I18NLocaleManager#getSupportedLocales()
     */
    protected I18NResourceBundle(final String baseName) {
        this.resourceBundles = new HashMap<>();
        final Module module = this.getClass().getModule();
        for (final Locale locale : I18NLocaleManager.getSupportedLocales()) {
            final ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, module);
            this.resourceBundles.put(locale, bundle);
        }
    }

    /**
     * Bind a {@link javafx.beans.property.StringProperty} to {@link java.util.concurrent.Callable}. The {@code func}
     * will be called when the current language is modified. Hence, the value returned shall depend on this language.
     *
     * @param prop The {@link javafx.beans.property.StringProperty} to bind.
     * @param func The key to look for in this {@link java.util.ResourceBundle}.
     */
    public static void bind(
            final StringProperty prop,
            final Callable<String> func
    ) {
        prop.bind(Bindings.createStringBinding(func, I18NLocaleManager.getProperty()));
    }

    /**
     * @param bundle The bundle to use to get the text.
     * @param key    The key to the text.
     * @param args   Extra argument to build the text.
     *
     * @return An object that will bind any {@link javafx.beans.property.StringProperty} to this bundle and key.
     */
    public static Consumer<StringProperty> binder(
            final I18NResourceBundle bundle,
            final String key,
            final Object... args
    ) {
        return p -> bundle.bind(p, key, args);
    }

    private String get(
            final String key,
            final Object... args
    ) {
        final ResourceBundle bundle = this.resourceBundles.get(I18NLocaleManager.getProperty().get());
        return MessageFormat.format(bundle.getString(key), args);
    }

    /**
     * Bind a {@link javafx.beans.property.StringProperty} to a {@code key} from a {@link java.util.ResourceBundle}.
     * The text is automatically updated when the language is updated.
     *
     * @param prop The {@link javafx.beans.property.StringProperty} to bind.
     * @param key  The key to look for in this {@link java.util.ResourceBundle}.
     * @param args The arguments used to build the text.
     */
    public final void bind(
            final StringProperty prop,
            final String key,
            final Object... args
    ) {
        bind(prop, () -> this.get(key, args));
    }

    /**
     * @param key  The key to look for in this {@link java.util.ResourceBundle}.
     * @param args The arguments used to build the text.
     *
     * @return The Callable whose result depends on the locale.
     */
    public final Callable<String> buildCallable(
            final String key,
            final Object... args
    ) {
        return () -> this.get(key, args);
    }
}
