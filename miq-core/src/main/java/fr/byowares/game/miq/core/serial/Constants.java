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

import fr.byowares.game.utils.serial.Version;

/**
 * Utility class holding constants used for serialization and deserialization.
 *
 * @since XXX
 */
public final class Constants {

    /** An object type. */
    public static final String TYPE = "type";
    /** Type: library. */
    public static final String TYPE_LIBRARY = "library";
    /** Type: album. */
    public static final String TYPE_ALBUM = "album";
    /** Type: song. */
    public static final String TYPE_SONG = "song";


    /** A Copyright. */
    public static final String COPYRIGHT = "copyright";
    /** Song's title. */
    public static final String TITLE = "title";
    /** An artist. */
    public static final String ARTIST = "artist";
    /** An author. */
    public static final String AUTHOR = "author";
    /** Album's name. */
    public static final String ALBUM = "album";
    /** A text. */
    public static final String COMMENT = "comment";


    /** A character. */
    public static final String SEPARATOR = "separator";
    /** List of lyrics in the MIQ model. */
    public static final String LYRICS = "lyrics";


    /** Duo Source: voice file. */
    public static final String VOICE = "voice";
    /** Duo Source: music file. */
    public static final String MUSIC = "music";
    /** Single source. */
    public static final String BOTH = "both";


    /** Version 1. */
    public static final Version V1 = new Version(1);


    private Constants() {
        // Utility class
    }
}
