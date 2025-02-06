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
import fr.byowares.game.miq.core.model.lyrics.LineElement;
import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.model.lyrics.Punctuation;
import fr.byowares.game.miq.core.model.lyrics.Singer;
import fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse;
import fr.byowares.game.miq.core.model.lyrics.Word;
import fr.byowares.game.miq.core.serial.AbstractDesSerTest;
import fr.byowares.game.miq.core.serial.Constants;
import fr.byowares.game.utils.serial.Deserializers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LyricsDeserializerV1Test
        extends AbstractDesSerTest<Lyrics> {

    static final TimeCodedVerse VERSE = new TimeCodedVerse(new Range(0L, 1L), Line.EMPTY_LIST);
    public static final Lyrics LYRICS_V1 = new Lyrics("Name", List.of(VERSE));
    static final String INPUT_V1 = """
            title: Name
            version: 1
            comment: 'null'
            author: Eubehi
            separator: '#'
            lyrics:
            - 0#1#0""";
    static final String TITLE_V1 = "title: Name";
    static final String VERSION_V1 = "version: 1";
    static final String SEPARATOR_V1 = "separator: '#'";
    static final String COMMENT_V1 = "comment: 'null'";
    static final String COMPLEX_V1_AS_STRING = """
            title: Complex lyrics
            version: 1
            comment: 'No comment'
            author: Eubehi
            separator: '#'
            lyrics:
            - 0#1#0
            - 1#2#4#0##I don't fear \\# \\\\ chars#1##He doesn't fear special chars#0##I don't fear \\# \\\\ chars#1##He doesn't fear special chars
            - 2#3#2#0#Roger#I don't fear \\# \\\\ chars#1#Nick Richard David#He doesn't fear special chars
            - 3#4#2#0#Nick Richard David#I don't fear \\# \\\\ chars#1#Roger#He doesn't fear special chars
            - 4#5#0
            - 5#6#4#2##La la la...#3##Le le le...#2##La la la...#3##Le le le...
            - 6#7#2#2#Roger#La la la...#3#Roger#Le le le...
            - 7#8#2#2#Nick Richard David#La la la...#3#Nick Richard David#Le le le...
            - 8#9#0""";
    static final Lyrics COMPLEX_V1;

    static {
        final long ts0 = 0L;
        final long ts1 = 1L;
        final long ts2 = 2L;
        final long ts3 = 3L;
        final long ts4 = 4L;
        final long ts5 = 5L;
        final long ts6 = 6L;
        final long ts7 = 7L;
        final long ts8 = 8L;
        final long ts9 = 9L;

        final Set<Singer> s0 = Set.of();
        final Set<Singer> s1 = Set.of(new Singer("Roger"));
        final Set<Singer> s3 = Set.of(new Singer("David"), new Singer("Richard"), new Singer("Nick"));
        final String[] wordsI = {"I", " ", "don", "'", "t", " ", "fear", " # \\ ", "chars"};
        final String[] wordsHe = {"He", " ", "doesn", "'", "t", " ", "fear", " ", "special", " ", "chars"};
        final String[] scatI = {"La", " ", "la", " ", "la", "..."};
        final String[] scatHe = {"Le", " ", "le", " ", "le", "..."};

        final Line l0ff = buildLine(s0, false, false, wordsI);
        final Line l1ff = buildLine(s1, false, false, wordsI);
        final Line l3ff = buildLine(s3, false, false, wordsI);

        final Line l0ft = buildLine(s0, false, true, scatI);
        final Line l1ft = buildLine(s1, false, true, scatI);
        final Line l3ft = buildLine(s3, false, true, scatI);

        final Line l0tf = buildLine(s0, true, false, wordsHe);
        final Line l1tf = buildLine(s1, true, false, wordsHe);
        final Line l3tf = buildLine(s3, true, false, wordsHe);

        final Line l0tt = buildLine(s0, true, true, scatHe);
        final Line l1tt = buildLine(s1, true, true, scatHe);
        final Line l3tt = buildLine(s3, true, true, scatHe);

        final List<TimeCodedVerse> verses = new ArrayList<>();
        verses.add(new TimeCodedVerse(new Range(ts0, ts1), Line.EMPTY_LIST));
        verses.add(new TimeCodedVerse(new Range(ts1, ts2), List.of(l0ff, l0tf, l0ff, l0tf)));
        verses.add(new TimeCodedVerse(new Range(ts2, ts3), List.of(l1ff, l3tf)));
        verses.add(new TimeCodedVerse(new Range(ts3, ts4), List.of(l3ff, l1tf)));
        verses.add(new TimeCodedVerse(new Range(ts4, ts5), Line.EMPTY_LIST));
        verses.add(new TimeCodedVerse(new Range(ts5, ts6), List.of(l0ft, l0tt, l0ft, l0tt)));
        verses.add(new TimeCodedVerse(new Range(ts6, ts7), List.of(l1ft, l1tt)));
        verses.add(new TimeCodedVerse(new Range(ts7, ts8), List.of(l3ft, l3tt)));
        verses.add(new TimeCodedVerse(new Range(ts8, ts9), Line.EMPTY_LIST));
        COMPLEX_V1 = new Lyrics("Complex lyrics", verses);
        COMPLEX_V1.setAuthor("Eubehi");
        COMPLEX_V1.setComment("No comment");

        LYRICS_V1.setAuthor("Eubehi");
        LYRICS_V1.setComment("null");
    }

    private static Line buildLine(
            final Set<Singer> singers,
            final boolean isBackupVocals,
            final boolean isNonLexicalVocables,
            final String... array
    ) {
        final List<LineElement> elements = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++) {
            final LineElement elt = i % 2 == 0 ? new Word(array[i]) : new Punctuation(array[i]);
            elements.add(elt);
        }
        return new Line(elements, singers, isBackupVocals, isNonLexicalVocables);
    }

    @Test
    public void testBasicInputV1() {
        assertEquals(COMPLEX_V1, this.getDeserializers().deserialize(inputStream(COMPLEX_V1_AS_STRING)));
    }

    /* ****************************************************************************************************************
     *                                               SEPARATOR TESTS                                                  *
     ******************************************************************************************************************/
    @Test
    public void testSeparatorInvalidMissing() {
        this.assertDeserializeThrows(NullPointerException.class, INPUT_V1, SEPARATOR_V1, "");
        this.assertDeserializeThrows(NullPointerException.class, INPUT_V1, SEPARATOR_V1, "separator: ");
        this.assertDeserializeThrows(NullPointerException.class, INPUT_V1, SEPARATOR_V1, "separator: null");
    }

    @Test
    public void testSeparatorInvalidEmpty() {
        final var e = this.assertDeserializeThrows(IAE_CLASS, INPUT_V1, SEPARATOR_V1, "separator: ''");
        assertEquals("A single character is expected here ()", e.getMessage());
    }

    @Test
    public void testSeparatorInvalid2Chars() {
        final var e = this.assertDeserializeThrows(IAE_CLASS, INPUT_V1, SEPARATOR_V1, "separator: '##'");
        assertEquals("A single character is expected here (##)", e.getMessage());
    }

    @Test
    public void testSeparatorInvalidBackSlash() {
        final var e = this.assertDeserializeThrows(IAE_CLASS, INPUT_V1, SEPARATOR_V1, "separator: '\\'");
        assertEquals("Separator cannot be '\\'", e.getMessage());
    }

    @Test
    public void testSeparatorInvalidSpace() {
        final var e = this.assertDeserializeThrows(IAE_CLASS, INPUT_V1, SEPARATOR_V1, "separator: ' '");
        assertEquals("Separator cannot be ' '", e.getMessage());
    }

    /* ****************************************************************************************************************
     *                                                COMMENT TESTS                                                   *
     ******************************************************************************************************************/


    @Test
    public void testInvalidTitleMissing() {
        this.assertInvalidWhenMissing(INPUT_V1, TITLE_V1, Constants.TITLE);
    }

    @Test
    public void testCommentValidMissing() {
        final var l1 = this.assertDeserializeDoesNotThrow(INPUT_V1, COMMENT_V1, "");
        final var l2 = this.assertDeserializeDoesNotThrow(INPUT_V1, COMMENT_V1, "comment: ");
        final var l3 = this.assertDeserializeDoesNotThrow(INPUT_V1, COMMENT_V1, "comment: null");
        assertNull(l1.getComment());
        assertNull(l2.getComment());
        assertNull(l3.getComment());
    }

    @Test
    public void testCommentInvalidNumber() {
        final var e = this.assertDeserializeThrows(IAE_CLASS, INPUT_V1, COMMENT_V1, "comment: 2");
        assertEquals("Invalid type. Expected java.lang.String, but was java.lang.Integer (2)", e.getMessage());
    }

    @Override
    protected Deserializers<Lyrics> getDeserializers() {
        return LyricsDeserializers.INSTANCE;
    }
}
