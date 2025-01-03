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
package fr.byowares.game.miq.core.serial;

import fr.byowares.game.miq.core.model.Lyrics;
import fr.byowares.game.miq.core.model.Range;
import fr.byowares.game.miq.core.model.lyrics.BlankLine;
import fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse;
import fr.byowares.game.miq.core.serial.v1.LyricsDeserializerV1Test;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ensure that the current {@link fr.byowares.game.miq.core.model.Lyrics} serializer/deserializer consecutive
 * transformations are bijective.
 */
class LyricsSerializerTest {

    private static final List<TimeCodedVerse> OBL = List.of(
            new TimeCodedVerse(new Range(0L, 1L), BlankLine.ONE_BLANK_LINE));

    private static void assertBijection(
            final CharSequence comment,
            final List<TimeCodedVerse> verses
    ) {
        final Lyrics input = new Lyrics(comment, verses);
        final StringWriter sw = new StringWriter();
        LyricsSerializer.INSTANCE.serialize(input, sw);
        final Lyrics output = LyricsDeserializers.INSTANCE.deserialize(inputStream(sw));
        assertEquals(input, output);
    }

    private static InputStream inputStream(final StringWriter sw) {
        return new ByteArrayInputStream(sw.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testNullComment() {
        assertBijection(null, OBL);
    }

    @Test
    public void testStringNullComment() {
        assertBijection("null", OBL);
    }

    @Test
    public void testStringNumberComment() {
        assertBijection("23", OBL);
    }

    @Test
    public void testTabComment() {
        assertBijection("A\ttab", OBL);
    }

    @Test
    public void testWeirdSpacingComment() {
        assertBijection("  Weird Spacing  ", OBL);
    }

    @Test
    public void testNewLineComment() {
        assertBijection("New\nLine", OBL);
    }

    @Test
    public void testCarriageReturnNewLineComment() {
        assertBijection("New\r\nLine", OBL);
    }

    @Test
    public void testNewLineWeirdSpacingComment() {
        assertBijection("  New  \n  Line  ", OBL);
    }

    @Test
    public void testCarriageReturnNewLineWeirdSpacingComment() {
        assertBijection("  New  \r\n  Line  ", OBL);
    }

    @Test
    public void testEmptyLyrics() {
        assertBijection("Some comments", List.of());
    }

    @Test
    void serialize() {
        assertBijection("Song's complex lyrics are not an issue", LyricsDeserializerV1Test.COMPLEX_V1.lyrics());
    }
}
