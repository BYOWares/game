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

import fr.byowares.game.utils.hashcodes.HashCodes;
import fr.byowares.game.utils.jfx.Resources;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Locale;

/**
 * @param locale The locale.
 * @param name   The associated name to the locale.
 * @param flag   The associated flag to the locale.
 *
 * @since XXX
 */
public record Lang(
        Locale locale,
        String name,
        Image flag
) {

    /** The French instance. */
    public static final Lang LANG_FR = new Lang(Locale.FRENCH, "Français", Resources.FLAG_FRA);
    /** The United Kingdom instance. */
    public static final Lang LANG_UK = new Lang(Locale.ENGLISH, "English", Resources.FLAG_UK);

    /**
     * @param showName Whether the name of the language mush be shown.
     *
     * @return An object in charge of displaying {@link fr.byowares.game.utils.jfx.i18n.Lang} in a Java FX list.
     */
    public static ListCell<Lang> listCell(final boolean showName) {
        return new LangCell(showName);
    }

    /**
     * @return The {@link javafx.scene.image.Image} of the Flag related to that {@link java.util.Locale}.
     */
    @Override
    public Image flag() {return this.flag;}

    @Override
    public int hashCode() {
        return HashCodes.hash(this.name, this.locale, this.flag);
    }

    @Override
    public String toString() {
        return "Lang[" + "locale=" + this.locale + "name=" + this.name + ", " + ']';
    }

    /** Lang Cell display object. */
    private static class LangCell
            extends ListCell<Lang> {

        private final boolean showName;

        private LangCell(final boolean showName) {
            this.showName = showName;
        }

        @Override
        protected void updateItem(
                final Lang lang,
                final boolean isEmpty
        ) {
            super.updateItem(lang, isEmpty);
            if (isEmpty) {
                this.setGraphic(null);
                if (this.showName) this.setText(null);
            } else {
                this.setGraphic(new ImageView(lang.flag()));
                if (this.showName) this.setText(lang.name());
            }
        }
    }
}
