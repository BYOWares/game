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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

/**
 * {@link java.util.Locale} manager.
 *
 * @since XXX
 */
public final class I18NLocaleManager {

    private static final Logger log = LoggerFactory.getLogger(I18NLocaleManager.class);
    private static final I18NLocaleManager INSTANCE = new I18NLocaleManager();

    private final ObjectProperty<Locale> locale;
    private final List<Locale> supportedLocales;

    private I18NLocaleManager() {
        this.locale = new SimpleObjectProperty<>(Locale.ENGLISH);
        this.supportedLocales = List.of(Locale.ENGLISH, Locale.FRENCH);
    }

    /**
     * @param locale The new locale to use through the application, if among the supported ones.
     *
     * @see #getSupportedLocales()
     */
    public static void updateLocale(final Locale locale) {
        if (INSTANCE.supportedLocales.contains(locale)) INSTANCE.locale.set(locale);
        else log.warn("Could not configure locale to {} as it is not supported (supported={})", locale,
                      INSTANCE.supportedLocales);
    }

    /**
     * @return The list of supported Locales by this application.
     */
    public static List<Locale> getSupportedLocales() {
        return INSTANCE.supportedLocales;
    }

    /**
     * @return The Locale in use.
     */
    public static Locale get() {
        return getProperty().get();
    }

    /**
     * @return The {@link javafx.beans.property.Property} used to manage the Locale.
     */
    static ObjectProperty<Locale> getProperty() {
        return INSTANCE.locale;
    }
}
