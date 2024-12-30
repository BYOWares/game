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
package fr.byowares.game.miq.core.serial;

/**
 * Utility class holding constants.
 *
 * @since XXX
 */
public final class Constants {

    /** A version to identify format */
    public static final String VERSION = "version";
    /** A text. */
    public static final String COMMENT = "comment";
    /** A character. */
    public static final String SEPARATOR = "separator";
    /** List of lyrics in the MIQ model. */
    public static final String LYRICS = "lyrics";

    /** Version 1. */
    public static final Version V1 = new Version(1);

    private Constants() {
        // Utility class
    }
}
