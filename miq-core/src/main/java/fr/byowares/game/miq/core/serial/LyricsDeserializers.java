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

import fr.byowares.game.miq.core.model.Lyrics;
import fr.byowares.game.miq.core.serial.v1.LyricsDeserializerV1;

import java.util.List;
import java.util.SortedMap;

/**
 * A collection of deserializer of Lyrics.
 *
 * @since XXX
 */
public class LyricsDeserializers
        extends Deserializers<Lyrics> {

    /** Singleton pattern */
    public static final LyricsDeserializers INSTANCE = new LyricsDeserializers();

    private static final List<? extends VersionedDeserializer<Lyrics>> ALL = List.of(LyricsDeserializerV1.INSTANCE);
    private static final SortedMap<Version, VersionedDeserializer<Lyrics>> ALL_AS_MAP = buildVersionMap(ALL);

    private LyricsDeserializers() {
        // Singleton pattern
    }

    @Override
    SortedMap<Version, VersionedDeserializer<Lyrics>> getAllDeserializersAsMap() {
        return ALL_AS_MAP;
    }
}
