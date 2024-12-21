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
package fr.byowares.game.miq.core.model.lyrics;

import fr.byowares.game.miq.core.option.Bracket;
import fr.byowares.game.miq.core.option.LyricsParsingOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing the {@link SpaceCleanerLineParser} and the
 * {@link fr.byowares.game.miq.core.model.lyrics.OptionsLineParser} when no bracket are defined in the
 * {@link fr.byowares.game.miq.core.option.LyricsParsingOptions}.
 */
class SpaceCleanerLineParserTest {

    final static String WHITESPACES_STRING = "" + '\t' + '\n' + '\u000B' + '\f' + " " + '\r' + '\u001C' + '\u001D' + '\u001E' + '\u001F';
    private static final OptionsLineParser OLP_NO_BRACKET = new OptionsLineParser(
            new LyricsParsingOptions().removeSingerBracket().removeChoirBracket().removeScatBracket());

    private static Stream<Arguments> bracketProvider() {
        final Bracket[] values = Bracket.values();
        final List<Arguments> res = new ArrayList<>(values.length * 2);
        for (final Bracket b : values) {
            res.add(Arguments.of(b, true));
            res.add(Arguments.of(b, false));
        }
        return res.stream();
    }

    @Test
    public void testSimpleLine() {
        doTest("My name is what? Who? Unknown!", true, //
               "My", " ", "name", " ", "is", " ", "what", "? ", "Who", "? ", "Unknown", "!");
    }

    private static void doTest(
            final CharSequence input,
            final boolean firstIsWord,
            final CharSequence... expectedArray
    ) {
        final List<LineElement> lineElements = new ArrayList<>(expectedArray.length);
        final int modulo = firstIsWord ? 0 : 1;
        for (int i = 0; i < expectedArray.length; i++) {
            final LineElement elt = i % 2 == modulo ? new Word(expectedArray[i]) : new Punctuation(expectedArray[i]);
            lineElements.add(elt);
        }
        final List<Line> expected = List.of(new SimpleLine(lineElements, Set.of(), false, false));
        final List<Line> actual = SpaceCleanerLineParser.INSTANCE.parse(input);
        final List<Line> actualOLP = OLP_NO_BRACKET.parse(input);
        assertEquals(expected, actual);
        assertEquals(expected, actualOLP);
    }

    @Test
    public void testSimpleLineWithBracket() {
        doTest("My [name] is {what}? <Who>? (Unknown)!", true, //
               "My", " [", "name", "] ", "is", " {", "what", "}? <", "Who", ">? (", "Unknown", ")!");
    }

    @Test
    public void testBlankLine() {
        final String empty = "";
        assertEquals(BlankLine.ONE_BLANK_LINE, SpaceCleanerLineParser.INSTANCE.parse(empty));
        assertEquals(BlankLine.ONE_BLANK_LINE, OLP_NO_BRACKET.parse(empty));
    }

    @Test
    public void testWhiteSpaceLine() {
        assertNotEquals(0, WHITESPACES_STRING.length());
        assertEquals(BlankLine.ONE_BLANK_LINE, SpaceCleanerLineParser.INSTANCE.parse(WHITESPACES_STRING));
        assertEquals(BlankLine.ONE_BLANK_LINE, OLP_NO_BRACKET.parse(WHITESPACES_STRING));
    }

    @Test
    public void testConsecutiveWhitespacesMergedInto1Space() {
        doTest("I" + WHITESPACES_STRING + "am", true, "I", " ", "am");
        doTest("I!" + WHITESPACES_STRING + "am", true, "I", "! ", "am");
        doTest("I" + WHITESPACES_STRING + "!am", true, "I", " !", "am");
        doTest("I!" + WHITESPACES_STRING + "!am", true, "I", "! !", "am");
        doTest("I!" + WHITESPACES_STRING + '!' + WHITESPACES_STRING + "!am", true, "I", "! ! !", "am");
    }

    @Test
    public void testLeadingWhiteSpacesArePruned() {
        doTest(WHITESPACES_STRING + "I am", true, "I", " ", "am");
        doTest(WHITESPACES_STRING + "!I am", false, "!", "I", " ", "am");
    }

    @Test
    public void testTrailingWhiteSpacesArePruned() {
        doTest("I am" + WHITESPACES_STRING, true, "I", " ", "am");
        doTest("I am!" + WHITESPACES_STRING, true, "I", " ", "am", "!");
    }

    @Test
    public void testNoWords() {
        assertEquals(BlankLine.ONE_BLANK_LINE,
                     SpaceCleanerLineParser.INSTANCE.parse(WHITESPACES_STRING + ".+/[" + WHITESPACES_STRING));
    }

    @ParameterizedTest(name = "[{index}] Bracket={0}, open={1}")
    @MethodSource("bracketProvider")
    void testBracketFirst(
            final Bracket bracket,
            final boolean open
    ) {
        final char c = open ? bracket.getOpen() : bracket.getClose();
        doTest(c + "Whatever man!", false, Character.toString(c), "Whatever", " ", "man", "!");
    }

    @ParameterizedTest(name = "[{index}] Bracket={0}, open={1}")
    @MethodSource("bracketProvider")
    void testBracketMiddle(
            final Bracket bracket,
            final boolean open
    ) {
        final char c = open ? bracket.getOpen() : bracket.getClose();
        doTest("Whatever" + c + "man!", true, "Whatever", Character.toString(c), "man", "!");
    }

    @ParameterizedTest(name = "[{index}] Bracket={0}, open={1}")
    @MethodSource("bracketProvider")
    void testBracketLast(
            final Bracket bracket,
            final boolean open
    ) {
        final char c = open ? bracket.getOpen() : bracket.getClose();
        doTest("Whatever man!" + c, true, "Whatever", " ", "man", "!" + c);
    }
}
