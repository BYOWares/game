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
package fr.byowares.game.utils.jfx;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * A collection of resources provided by this module.
 *
 * @since XXX
 */
public final class Resources {

    /** The Flag for the French language. */
    public static final Image FLAG_FRA = getFlag("france");
    /** The Flag for the English language. */
    public static final Image FLAG_UK = getFlag("united_kingdom");

    /** Barecast font. */
    public static final String FONT_BARECAST = getFont("Barecast-Regular.otf");

    /** The CSS for the Dark theme. */
    public static final String CSS_DARK_THEME = getCSS("primer-dark");
    /** The CSS for the Light theme. */
    public static final String CSS_LIGHT_THEME = getCSS("primer-light");

    private Resources() {
        // Utility class
    }


    private static Image getFlag(final String fileName) {
        return new Image(get("flag", fileName + ".png"));
    }

    private static String getFont(final String fileName) {
        return get("font", fileName);
    }

    private static String getCSS(final String fileName) {
        return get("css", fileName + ".css");
    }

    private static String get(
            final String dir,
            final String filename
    ) {
        return get(Resources.class, dir, filename);
    }

    /**
     * @param cl       The class used as a starting point to look for the resource.
     * @param dir      The subdirectories containing the file.
     * @param filename The name of the file.
     *
     * @return A string representation of the {@link java.net.URL} for the resource.
     */
    public static String get(
            final Class<?> cl,
            final String dir,
            final String filename
    ) {
        return Objects.requireNonNull(cl.getResource(dir + "/" + filename)).toExternalForm();
    }
}
