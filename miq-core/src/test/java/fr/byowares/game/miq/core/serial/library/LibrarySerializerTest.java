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
package fr.byowares.game.miq.core.serial.library;

import fr.byowares.game.miq.core.model.song.Library;
import fr.byowares.game.miq.core.serial.AbstractSerializerTest;
import fr.byowares.game.miq.core.serial.album.AlbumDeserializerV1Test;
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test that latest {@link fr.byowares.game.miq.core.model.song.Library} serializer/deserializer consecutive
 * transformations are bijective.
 */
public class LibrarySerializerTest
        extends AbstractSerializerTest<Library> {

    @Override
    protected Serializer<Library> getSerializer() {
        return LibrarySerializer.INSTANCE;
    }

    @Override
    protected Deserializers<Library> getDeserializers() {
        return LibraryDeserializers.INSTANCE;
    }

    @Test
    void serialize() {
        this.assertBijection(LibraryDeserializerV1Test.LIBRARY_V1);
    }

    @ParameterizedTest(name = "[{index}] input={0}")
    @MethodSource("getStringNonNullValues")
    public void testValidTitle(final StringInput input) {
        this.assertBijection(new Library("Who cares ?"), Library::setName, input.value());
    }

    @ParameterizedTest(name = "[{index}] input={0}")
    @MethodSource("getStringValues")
    public void testValidComment(final StringInput input) {
        this.assertBijection(new Library("Who cares ?"), NamedSourcedObject::setComment, input.value());
    }

    @Test
    public void testAlbumAreNotSerialized() {
        final Library input = new Library("Lib");
        input.getAlbums().add(AlbumDeserializerV1Test.ALBUM_V1);
        final Library output = new Library("Lib");
        assertEquals(2, input.getAlbums().size());
        assertEquals(1, output.getAlbums().size()); // Only the undefined one
        this.assertConversion(input, output);
    }
}
