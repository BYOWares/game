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
import fr.byowares.game.miq.core.serial.Constants;
import fr.byowares.game.utils.serial.Version;
import fr.byowares.game.utils.serial.VersionedDeserializer;

import java.util.Map;
import java.util.Objects;

/**
 * Version 1 for {@link fr.byowares.game.miq.core.model.song.Album} deserializer.
 *
 * @since XXX
 */
public class AlbumDeserializerV1
        extends VersionedDeserializer<Album> {

    /** Singleton pattern. */
    public static final AlbumDeserializerV1 INSTANCE = new AlbumDeserializerV1();

    private AlbumDeserializerV1() {
        // Singleton pattern
    }

    @Override
    protected Album doDeserialize(final Map<String, Object> map) {
        final String title = Objects.requireNonNull(removeAsString(map, Constants.TITLE));
        final Album album = new Album(title);
        album.setArtist(removeAsString(map, Constants.ARTIST));
        album.setCopyright(removeAsString(map, Constants.COPYRIGHT));
        return album;
    }

    @Override
    protected Version getVersion() {
        return Constants.V1;
    }
}
