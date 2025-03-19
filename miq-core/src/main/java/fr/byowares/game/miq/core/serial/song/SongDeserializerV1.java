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
import fr.byowares.game.utils.serial.SourcedVersionedDeserializer;
import fr.byowares.game.utils.serial.Version;
import fr.byowares.game.utils.serial.source.Source;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Version 1 for Lyrics deserializer.
 *
 * @since XXX
 */
public class SongDeserializerV1
        extends SourcedVersionedDeserializer<Song> {

    @Override
    protected Song doDeserialize(final Map<String, Object> map) {
        final Source src = this.getSource();
        final Song song = new Song(removeAsString(map, Constants.TITLE));
        song.setCopyright(removeAsString(map, Constants.COPYRIGHT));
        song.setArtist(removeAsString(map, Constants.ARTIST));
        song.setAlbumName(removeAsString(map, Constants.ALBUM));
        song.setComment(removeAsString(map, Constants.COMMENT));

        final String voice = removeAsString(map, Constants.VOICE);
        final String music = removeAsString(map, Constants.MUSIC);
        if ((voice == null && music != null) || (voice != null && music == null)) throw new IllegalArgumentException(
                "[source=" + src + "] Voice and Music shall be both null or both non null (voice: " + voice + ", " + "music: " + music + ")");
        if (voice != null /* && music != null */) {
            song.setVoiceSource(src.resolve(voice));
            song.setMusicSource(src.resolve(music));
        }
        final String both = removeAsString(map, Constants.BOTH);
        if (both != null) song.setAudioSource(src.resolve(both));

        for (final Object l : (List<?>) Objects.requireNonNull(remove(map, Constants.LYRICS, List.class, null)))
            song.getRawLyrics().add(Objects.toString(l));

        return song;
    }

    @Override
    protected Version getVersion() {
        return Constants.V1;
    }
}
