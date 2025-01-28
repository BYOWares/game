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
package fr.byowares.game.app;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * A collection of resources provided by this module.
 *
 * @since XXX
 */
public final class ResourcesApp {

    /** Icon for the Moon unselected. */
    public static final Image ICO_MOON_OFF = getIcon("moon_off_32");
    /** Icon for the Moon selected. */
    public static final Image ICO_MOON_ON = getIcon("moon_on_32");
    /** Icon for the Sun unselected. */
    public static final Image ICO_SUN_OFF = getIcon("sun_off_32");
    /** Icon for the Sun selected. */
    public static final Image ICO_SUN_ON = getIcon("sun_on_32");
    /** MIQ Specific CSS. */
    public static final String MIQ_CSS = getCss("MIQGameAccess");

    private ResourcesApp() {
        // Utility class
    }

    private static Image getIcon(final String fileName) {
        return new Image(get("icon", fileName + ".png"));
    }

    private static String getCss(final String fileName) {
        return get("css", fileName + ".css");
    }

    private static String get(
            final String dir,
            final String filename
    ) {
        return Objects.requireNonNull(ResourcesApp.class.getResource(dir + "/" + filename)).toExternalForm();
    }
}
