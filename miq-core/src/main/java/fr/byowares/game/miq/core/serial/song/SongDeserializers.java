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
package fr.byowares.game.miq.core.serial.song;

import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.SourcedVersionedDeserializer;
import fr.byowares.game.utils.serial.Version;
import fr.byowares.game.utils.serial.VersionedDeserializer;
import fr.byowares.game.utils.serial.source.Source;

import java.util.List;
import java.util.SortedMap;

/**
 * A collection of deserializer of {@link fr.byowares.game.miq.core.model.song.Song}.
 *
 * @since XXX
 */
public class SongDeserializers
        extends Deserializers<Song> {

    /** Singleton pattern */
    public static final SongDeserializers INSTANCE = new SongDeserializers();
    private static final ThreadLocal<SortedMap<Version, SourcedVersionedDeserializer<Song>>> THREAD_LOCAL = //
            ThreadLocal.withInitial(SongDeserializers::getVersionsMap);

    static {
        // Check deserializers version during class init
        getVersionsMap();
    }

    private SongDeserializers() {
        // Singleton pattern
    }

    private static SortedMap<Version, SourcedVersionedDeserializer<Song>> getVersionsMap() {
        final List<SourcedVersionedDeserializer<Song>> allInstances = List.of(new SongDeserializerV1());
        return buildVersionMap(allInstances);
    }

    @Override
    protected SortedMap<Version, ? extends VersionedDeserializer<Song>> getAllDeserializersAsMap() {
        return THREAD_LOCAL.get();
    }

    /**
     * @param source The new source to consider while deserializing.
     *
     * @return {@code this}.
     */
    public SongDeserializers updateSource(final Source source) {
        THREAD_LOCAL.get().forEach((v, d) -> d.setSource(source));
        return this;
    }
}
