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
import fr.byowares.game.miq.core.serial.AbstractSerializerTest;
import fr.byowares.game.miq.core.serial.song.SongDeserializerV1Test;
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.Serializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test that latest {@link fr.byowares.game.miq.core.model.song.Album} serializer/deserializer consecutive
 * transformations are bijective.
 */
public class AlbumSerializerTest
        extends AbstractSerializerTest<Album> {

    private static Album getGenericAlbum() {
        final Album album = new Album("Some title");
        album.setArtist("Nice artist");
        album.setCopyright("Complicated copyright");
        return album;
    }

    @Override
    protected Serializer<Album> getSerializer() {
        return AlbumSerializer.INSTANCE;
    }

    @Override
    protected Deserializers<Album> getDeserializers() {
        return AlbumDeserializers.INSTANCE;
    }

    @Test
    void serialize() {
        this.assertBijection(AlbumDeserializerV1Test.ALBUM_V1);
        this.assertBijection(AlbumDeserializerV1Test.ALBUM_V1_NO_ML);
    }

    @ParameterizedTest(name = "[{index}] input={0}")
    @MethodSource("getStringNonNullValues")
    public void testValidTitle(final StringInput input) {
        this.assertBijection(getGenericAlbum(), Album::setTitle, input.value());
    }

    @ParameterizedTest(name = "[{index}] input={0}")
    @MethodSource("getStringValues")
    public void testValidArtist(final StringInput input) {
        this.assertBijection(getGenericAlbum(), Album::setArtist, input.value());
    }

    @ParameterizedTest(name = "[{index}] input={0}")
    @MethodSource("getStringValues")
    public void testValidCopyright(final StringInput input) {
        this.assertBijection(getGenericAlbum(), Album::setCopyright, input.value());
    }

    @Test
    public void testSongAreNotSerialized() {
        final Album input = getGenericAlbum();
        input.getSongs().add(SongDeserializerV1Test.SONG_V1);
        final Album output = getGenericAlbum();
        assertFalse(input.getSongs().isEmpty());
        assertTrue(output.getSongs().isEmpty());
        this.assertConversion(input, output);
    }
}
