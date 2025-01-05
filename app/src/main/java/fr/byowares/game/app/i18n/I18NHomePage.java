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
package fr.byowares.game.app.i18n;

import fr.byowares.game.utils.jfx.i18n.I18NResourceBundle;

/**
 * Factory of {@link javafx.beans.binding.Binding} based on {@link java.util.ResourceBundle} of name
 * {@link fr.byowares.game.app.i18n.I18NHomePage#NAME}.
 *
 * @since XXX
 */
public final class I18NHomePage
        extends I18NResourceBundle {

    private static final String NAME = "fr.byowares.game.app.i18n.homepage";
    private static final I18NHomePage INSTANCE = new I18NHomePage();

    private I18NHomePage() {
        super(NAME);
    }

    /**
     * @return The unique instance
     */
    public static I18NHomePage get() {
        return INSTANCE;
    }
}
