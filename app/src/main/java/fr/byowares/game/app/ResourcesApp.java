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

    /** 16 pixel icon for the App. */
    public static final Image GAME_16 = getIcon("GAME__ico__all_16");
    /** 32 pixel icon for the App. */
    public static final Image GAME_32 = getIcon("GAME__ico__all_32");
    /** 64 pixel icon for the App. */
    public static final Image GAME_64 = getIcon("GAME__ico__all_64");
    /** 128 pixel icon for the App. */
    public static final Image GAME_128 = getIcon("GAME__ico__all_128");
    /** 256 pixel icon for the App. */
    public static final Image GAME_256 = getIcon("GAME__ico__all_256");
    /** 512 pixel icon for the App. */
    public static final Image GAME_512 = getIcon("GAME__ico__all_512");
    /** 1024 pixel icon for the App. */
    public static final Image GAME_1024 = getIcon("GAME__ico__all_1024");

    /** Icon for the Moon unselected. */
    public static final Image ICO_MOON_OFF = getIcon("moon_off_32");
    /** Icon for the Moon selected. */
    public static final Image ICO_MOON_ON = getIcon("moon_on_32");
    /** Icon for the Sun unselected. */
    public static final Image ICO_SUN_OFF = getIcon("sun_off_32");
    /** Icon for the Sun selected. */
    public static final Image ICO_SUN_ON = getIcon("sun_on_32");

    private ResourcesApp() {
        // Utility class
    }

    private static Image getIcon(final String fileName) {
        return new Image(get("icon", fileName + ".png"));
    }

    private static String get(
            final String dir,
            final String filename
    ) {
        return Objects.requireNonNull(ResourcesApp.class.getResource(dir + "/" + filename)).toExternalForm();
    }
}
