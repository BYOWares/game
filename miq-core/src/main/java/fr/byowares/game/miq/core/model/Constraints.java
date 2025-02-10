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
package fr.byowares.game.miq.core.model;

/**
 * Set of constraints on model.
 *
 * @since XXX
 */
public final class Constraints {

    /** Copyright minimal length. */
    public static final int COPYRIGHT_MIN_LENGTH = 0;
    /** Copyright maximal length. */
    public static final int COPYRIGHT_MAX_LENGTH = 16 * 1024;

    /** Raw lyrics minimal length. */
    public static final int RAW_LYRICS_MIN_LENGTH = 0;
    /** Raw lyrics maximal length. */
    public static final int RAW_LYRICS_MAX_LENGTH = 256 * 1024;

    private static final int NAME_MIN_LENGTH = 8;
    /** Artist's name minimal length. */
    public static final int ARTIST_MIN_LENGTH = NAME_MIN_LENGTH;
    /** Library's name minimal length. */
    public static final int LIB_NAME_MIN_LENGTH = NAME_MIN_LENGTH;
    /** Album's title minimal length. */
    public static final int ALBUM_TITLE_MIN_LENGTH = NAME_MIN_LENGTH;
    /** Song's title minimal length. */
    public static final int SONG_TITLE_MIN_LENGTH = NAME_MIN_LENGTH;
    /** Lyrics' name minimal length. */
    public static final int LYRICS_NAME_MIN_LENGTH = NAME_MIN_LENGTH;


    private static final int NAME_MAX_LENGTH = 255;
    /** Artist's name maximal length. */
    public static final int ARTIST_MAX_LENGTH = NAME_MAX_LENGTH;
    /** Library's name maximal length. */
    public static final int LIB_NAME_MAX_LENGTH = NAME_MAX_LENGTH;
    /** Album's title maximal length. */
    public static final int ALBUM_TITLE_MAX_LENGTH = NAME_MAX_LENGTH;
    /** Song's title maximal length. */
    public static final int SONG_TITLE_MAX_LENGTH = NAME_MAX_LENGTH;
    /** Lyrics' name maximal length. */
    public static final int LYRICS_NAME_MAX_LENGTH = NAME_MAX_LENGTH;


    private static final int CMT_MIN_LENGTH = 0;
    /** Library's comment minimal length. */
    public static final int LIB_CMT_MIN_LENGTH = CMT_MIN_LENGTH;
    /** Album's comment minimal length. */
    public static final int ALBUM_CMT_MIN_LENGTH = CMT_MIN_LENGTH;
    /** Song's comment minimal length. */
    public static final int SONG_CMT_MIN_LENGTH = CMT_MIN_LENGTH;
    /** Lyrics' comment minimal length. */
    public static final int LYRICS_CMT_MIN_LENGTH = CMT_MIN_LENGTH;


    private static final int CMT_MAX_LENGTH = 1023;
    /** Library's comment maximal length. */
    public static final int LIB_CMT_MAX_LENGTH = CMT_MAX_LENGTH;
    /** Album's comment maximal length. */
    public static final int ALBUM_CMT_MAX_LENGTH = CMT_MAX_LENGTH;
    /** Song's comment maximal length. */
    public static final int SONG_CMT_MAX_LENGTH = CMT_MAX_LENGTH;
    /** Lyrics' comment maximal length. */
    public static final int LYRICS_CMT_MAX_LENGTH = CMT_MAX_LENGTH;

    private Constraints() {
        // Utility class
    }
}
