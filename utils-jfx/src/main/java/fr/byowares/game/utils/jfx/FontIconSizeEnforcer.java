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

import javafx.scene.Parent;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * A {@code UserAgentStylesheet} is used to style our whole application. This stylesheet contains default styles for
 * icons. Since <a href="https://github.com/kordamp/ikonli/issues/121">issue #121</a>, the defaults style cannot be
 * overridden by calling setter (like {@link org.kordamp.ikonli.javafx.FontIcon#setIconSize(int)}  for example).
 * <p>
 * The solution to override those values is to define a new CSS class with the wanted values, apply this new
 * class to the wanted elements. There exist 2 ways of doing it:
 * <ul>
 *     <li>Define the style in a file (that will be persisted). This solution is the more robust and shall be used when
 *     lots of elements are expected to be customized.</li>
 *     <li>Define on the fly the new CSS class.</li>
 * </ul>
 * This class is here to implement the second solution.
 *
 * @see javafx.application.Application#setUserAgentStylesheet(String)
 * @since XXX
 */
public final class FontIconSizeEnforcer {

    private FontIconSizeEnforcer() {
        // Utility class
    }

    /**
     * @param parent       The parent that will hold the CSS information.
     * @param cssClassName The CSS class name to define.
     * @param sizeInPx     The expected size in pixel of the icon.
     *
     * @return {@code cssClassName}.
     */
    public static String enforceIconSizeCSS(
            final Parent parent,
            final String cssClassName,
            final int sizeInPx
    ) {
        final String css = "." + cssClassName + " { -fx-icon-size: " + sizeInPx + "px; }";
        parent.getStylesheets().add(
                "data:text/css;base64," + Base64.getEncoder().encodeToString(css.getBytes(StandardCharsets.UTF_8)));
        return cssClassName;
    }
}
