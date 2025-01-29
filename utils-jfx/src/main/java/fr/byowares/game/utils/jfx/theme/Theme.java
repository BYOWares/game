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
package fr.byowares.game.utils.jfx.theme;

import fr.byowares.game.utils.hashcodes.HashCodes;
import fr.byowares.game.utils.jfx.Resources;

import java.util.Objects;

/**
 * Representation of a Theme used to customize the appearance of the application.
 *
 * @since XXX
 */
public final class Theme {

    /** The basic Dark Theme. */
    public static final Theme DARK_THEME = new Theme("Dark", true, Resources.CSS_DARK_THEME);
    /** The basic Light Theme. */
    public static final Theme LIGHT_THEME = new Theme("Light", false, Resources.CSS_LIGHT_THEME);

    private final String name;
    private final boolean isDarkMode;
    private final String styleSheets;

    private Theme(
            final String name,
            final boolean isDarkMode,
            final String styleSheets
    ) {
        this.name = name;
        this.isDarkMode = isDarkMode;
        this.styleSheets = Objects.requireNonNull(styleSheets);
    }

    /**
     * @return {@code true} if this theme is a dark mode, {@code false} otherwise.
     */
    public boolean isDarkMode() {return this.isDarkMode;}

    /**
     * @return The style sheet used to skin everything.
     */
    public String styleSheets() {return this.styleSheets;}

    @Override
    public int hashCode() {
        return HashCodes.hash(this.name, this.isDarkMode, this.styleSheets);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final var that = (Theme) obj;
        return Objects.equals(this.name, that.name) && this.isDarkMode == that.isDarkMode && Objects.equals(
                this.styleSheets, that.styleSheets);
    }

    @Override
    public String toString() {
        return "Theme[" + "name=" + this.name + ", " + "isDarkMode=" + this.isDarkMode + ", " + "styleSheets=" + this.styleSheets + ']';
    }
}
