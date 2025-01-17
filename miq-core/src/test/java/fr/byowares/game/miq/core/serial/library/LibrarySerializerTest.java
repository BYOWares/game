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
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.Serializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test that latest {@link fr.byowares.game.miq.core.model.song.Library} serializer/deserializer consecutive
 * transformations are bijective.
 */
class LibrarySerializerTest
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
}
