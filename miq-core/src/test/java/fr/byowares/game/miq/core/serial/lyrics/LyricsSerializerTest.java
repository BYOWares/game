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
package fr.byowares.game.miq.core.serial.lyrics;

import fr.byowares.game.miq.core.model.Range;
import fr.byowares.game.miq.core.model.lyrics.Line;
import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse;
import fr.byowares.game.miq.core.serial.AbstractSerializerTest;
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

/**
 * Test that latest {@link fr.byowares.game.miq.core.model.lyrics.Lyrics} serializer/deserializer consecutive
 * transformations are bijective.
 */
public class LyricsSerializerTest
        extends AbstractSerializerTest<Lyrics> {

    private static final List<TimeCodedVerse> OBL = List.of(new TimeCodedVerse(new Range(0L, 1L), Line.EMPTY_LIST));

    private static Lyrics getGenericLyrics() {
        final Lyrics lyrics = new Lyrics("Name", OBL);
        lyrics.setComment("Some comment");
        lyrics.setAuthor("Some author");
        return lyrics;
    }

    @Override
    protected Serializer<Lyrics> getSerializer() {
        return LyricsSerializer.INSTANCE;
    }

    @Override
    protected Deserializers<Lyrics> getDeserializers() {
        return LyricsDeserializers.INSTANCE;
    }

    @Test
    public void testEmptyLyrics() {
        final Lyrics lyrics = new Lyrics("Lyrics", List.of());
        lyrics.setAuthor("author");
        lyrics.setComment("comment");
        this.assertBijection(lyrics);
    }

    @Test
    void serialize() {
        this.assertBijection(LyricsDeserializerV1Test.COMPLEX_V1);
    }

    @ParameterizedTest(name = "[{index}] input={0}")
    @MethodSource("getStringNonNullValues")
    public void testValidName(final StringInput input) {
        this.assertBijection(getGenericLyrics(), NamedSourcedObject::setName, input.value());
    }

    @ParameterizedTest(name = "[{index}] input={0}")
    @MethodSource("getStringValues")
    public void testValidComment(final StringInput input) {
        this.assertBijection(getGenericLyrics(), NamedSourcedObject::setComment, input.value());
    }

    @ParameterizedTest(name = "[{index}] input={0}")
    @MethodSource("getStringValues")
    public void testValidAuthor(final StringInput input) {
        this.assertBijection(getGenericLyrics(), Lyrics::setAuthor, input.value());
    }
}
