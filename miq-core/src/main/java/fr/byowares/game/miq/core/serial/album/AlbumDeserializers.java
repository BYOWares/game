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
package fr.byowares.game.miq.core.serial.album;

import fr.byowares.game.miq.core.model.song.Album;
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.Version;
import fr.byowares.game.utils.serial.VersionedDeserializer;

import java.util.List;
import java.util.SortedMap;

/**
 * A collection of deserializer of {@link fr.byowares.game.miq.core.model.song.Album}.
 *
 * @since XXX
 */
public class AlbumDeserializers
        extends Deserializers<Album> {

    /** Singleton pattern */
    public static final AlbumDeserializers INSTANCE = new AlbumDeserializers();

    private static final List<VersionedDeserializer<Album>> ALL = List.of(AlbumDeserializerV1.INSTANCE);
    private static final SortedMap<Version, VersionedDeserializer<Album>> ALL_AS_MAP = buildVersionMap(ALL);

    private AlbumDeserializers() {
        // Singleton pattern
    }

    @Override
    protected SortedMap<Version, VersionedDeserializer<Album>> getAllDeserializersAsMap() {
        return ALL_AS_MAP;
    }
}
