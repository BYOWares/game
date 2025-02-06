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
import fr.byowares.game.miq.core.serial.AbstractSerializerTest;
import fr.byowares.game.miq.core.serial.lyrics.LyricsDeserializerV1Test;
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test that latest {@link fr.byowares.game.miq.core.model.song.Song} serializer/deserializer consecutive
 * transformations are bijective.
 */
public class SongSerializerTest
        extends AbstractSerializerTest<Song> {

    private static Song getGenericSong() {
        final Song song = new Song("Some title");
        song.setComment("A useful comment");
        song.setAlbumName("Fancy album name");
        song.setArtist("Nice artist");
        song.setCopyright("Complicated copyright");
        song.getRawLyrics().addAll(List.of("First line", "", "Third line"));
        return song;
    }

    @Override
    protected Serializer<Song> getSerializer() {
        return SongSerializer.INSTANCE;
    }

    @Override
    protected Deserializers<Song> getDeserializers() {
        return SongDeserializers.INSTANCE;
    }


    @Test
    void serialize() {
        this.assertBijection(SongDeserializerV1Test.SONG_V1);
    }


    @ParameterizedTest(name = "testValidTitle [{index}] input={0}")
    @MethodSource("getStringNonNullValues")
    public void testValidTitle(final StringInput input) {
        this.assertBijection(getGenericSong(), NamedSourcedObject::setName, input.value());
    }

    @ParameterizedTest(name = "testValidComment [{index}] input={0}")
    @MethodSource("getStringValues")
    public void testValidComment(final StringInput input) {
        this.assertBijection(getGenericSong(), NamedSourcedObject::setComment, input.value());
    }

    @ParameterizedTest(name = "testValidArtist [{index}] input={0}")
    @MethodSource("getStringValues")
    public void testValidArtist(final StringInput input) {
        this.assertBijection(getGenericSong(), Song::setArtist, input.value());
    }

    @ParameterizedTest(name = "testValidCopyright [{index}] input={0}")
    @MethodSource("getStringValues")
    public void testValidCopyright(final StringInput input) {
        this.assertBijection(getGenericSong(), Song::setCopyright, input.value());
    }

    @ParameterizedTest(name = "testValidAlbumName [{index}] input={0}")
    @MethodSource("getStringValues")
    public void testValidAlbumName(final StringInput input) {
        this.assertBijection(getGenericSong(), Song::setAlbumName, input.value());
    }

    @Test
    public void testLyricsAreNotSerialized() {
        final Song input = getGenericSong();
        input.getLyrics().add(LyricsDeserializerV1Test.LYRICS_V1);
        final Song output = getGenericSong();
        assertFalse(input.getLyrics().isEmpty());
        assertTrue(output.getLyrics().isEmpty());
        this.assertConversion(input, output);
    }
}
