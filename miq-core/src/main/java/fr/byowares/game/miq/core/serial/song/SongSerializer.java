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
import fr.byowares.game.miq.core.serial.Constants;
import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.Version;

import java.util.Map;

/**
 * Offer the capacity to serialize {@link fr.byowares.game.miq.core.model.song.Song} in Yaml format.
 *
 * @since XXX
 */
public class SongSerializer
        extends Serializer<Song> {

    /** Singleton pattern. */
    public static final SongSerializer INSTANCE = new SongSerializer();

    private SongSerializer() {
        // Singleton pattern
    }

    @Override
    public void doSerialize(
            final Song song,
            final Map<String, Object> map
    ) {
        map.put(Constants.TYPE, Constants.TYPE_SONG);
        map.put(Constants.COPYRIGHT, song.getCopyright());
        map.put(Constants.TITLE, song.getTitle());
        map.put(Constants.ARTIST, song.getArtist());
        map.put(Constants.ALBUM, song.getAlbumName());
        map.put(Constants.COMMENT, song.getComment());
        if (song.getDuoSource() != null) {
            map.put(Constants.VOICE, song.getDuoSource().voiceSource().getName());
            map.put(Constants.MUSIC, song.getDuoSource().musicSource().getName());
        }
        if (song.getSingleSource() != null) map.put(Constants.BOTH, song.getSingleSource().audioSource().getName());
        map.put(Constants.LYRICS, song.getRawLyrics());
    }

    @Override
    protected Version getVersion() {
        return Constants.V1;
    }
}
