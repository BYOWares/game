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
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class OptionsLineParserTest {

    private static final LyricsParsingOptions DEFAULT_OPTIONS = new LyricsParsingOptions();
    private static final OptionsLineParser DEFAULT_PARSER = new OptionsLineParser(DEFAULT_OPTIONS);
    private static final Punctuation SPACE = new Punctuation(" ");
    private static final List<OptionsLineParser> ALL_VALID_OLP = new ArrayList<>();

    static {
        final List<Bracket> allBrackets = new ArrayList<>(Arrays.asList(Bracket.values()));
        allBrackets.add(null);
        final var options = new LyricsParsingOptions().removeCopyrightPattern();
        for (final Bracket singer : allBrackets) {
            for (final Bracket choir : allBrackets) {
                if (!(singer == null || singer != choir)) continue;
                for (final Bracket scat : allBrackets) {
                    if (!(choir == null || choir != scat)) continue;
                    if (!(singer == null || singer != scat)) continue;
                    ALL_VALID_OLP.add(
                            new OptionsLineParser(options.singerBracket(singer).choirBracket(choir).scatBracket(scat)));
                }
            }
        }
    }

    public static Stream<OptionsLineParser> generateOLPAllValid() {
        return ALL_VALID_OLP.stream();
    }

    public static Stream<OptionsLineParser> generateOLPSingerDefined() {
        return generateOLP(false, null, null);
    }

    /**
     * @param singerMustBeNull {@code null} if not filter must be applied on the singer bracket option, {@code true}
     *                         if it must be {@code null}, {@code false} if it must be non-null.
     * @param choirMustBeNull  {@code null} if not filter must be applied on the choir bracket option, {@code true}
     *                         if it must be {@code null}, {@code false} if it must be non-null.
     * @param scatMustBeNull   {@code null} if not filter must be applied on the scat bracket option, {@code true}
     *                         if it must be {@code null}, {@code false} if it must be non-null.
     *
     * @return The stream of valid {@link fr.byowares.game.miq.core.model.lyrics.OptionsLineParser} with filters
     * applied.
     */
    public static Stream<OptionsLineParser> generateOLP(
            final Boolean singerMustBeNull,
            final Boolean choirMustBeNull,
            final Boolean scatMustBeNull
    ) {
        return ALL_VALID_OLP.stream().filter(p -> {
            final LyricsParsingOptions options = p.options();
            if (singerMustBeNull != null && Objects.isNull(options.singerBracket()) != singerMustBeNull) return false;
            if (choirMustBeNull != null && Objects.isNull(options.choirBracket()) != choirMustBeNull) return false;
            return (scatMustBeNull == null || Objects.isNull(options.scatBracket()) == scatMustBeNull);
        });
    }

    @Test
    public void testInvalidOption() {
        final var options = new LyricsParsingOptions().singerBracket(Bracket.ANGLE).choirBracket(Bracket.ANGLE);
        final var e = assertThrows(IllegalStateException.class, () -> new OptionsLineParser(options));
        assertTrue(e.getMessage().startsWith("LyricsParsingOptions are not valid: "));
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPAllValid")
    public void testEmptyLine(final OptionsLineParser parser) {
        assertEquals(BlankLine.ONE_BLANK_LINE, parser.parse(""));
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPSingerDefined")
    public void testOnlySingerContext(final OptionsLineParser parser) {
        final char open = parser.options().singerBracket().getOpen();
        final char close = parser.options().singerBracket().getClose();
        final String ws = SpaceCleanerLineParserTest.WHITESPACES_STRING;
        final String name = "David";
        assertEquals(BlankLine.ONE_BLANK_LINE, parser.parse(open + name + close));
        assertEquals(BlankLine.ONE_BLANK_LINE, parser.parse(open + name + close + ws));
        assertEquals(BlankLine.ONE_BLANK_LINE, parser.parse(ws + open + name + close));
        assertEquals(BlankLine.ONE_BLANK_LINE, parser.parse(ws + open + name + close + ws));
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPSingerDefined")
    public void testSingerContext(final OptionsLineParser parser) {
        final String open = Character.toString(parser.options().singerBracket().getOpen());
        final String close = Character.toString(parser.options().singerBracket().getClose());
        final String pair = open + close;
        final String ws = SpaceCleanerLineParserTest.WHITESPACES_STRING;
        final String n1 = "David";
        final String n2 = "David...Roger|Richard";
        final String n3 = "David,Roger" + ws + "Richard";
        final Set<Singer> s0 = Set.of();
        final Set<Singer> s1 = Set.of(new Singer("David"));
        final Set<Singer> s2 = Set.of(new Singer("David"), new Singer("Roger"), new Singer("Richard"));

        final String space = " ";
        final String l1 = "Hey you!";
        final String l2 = "out there";
        final String l3 = "in the cold,";

        final var e1 = List.of(buildLine(s0, "Hey", " ", "you", "!"));
        assertEquals(e1, parser.parse(l1));
        assertEquals(e1, parser.parse(pair + l1));
        assertEquals(e1, parser.parse(l1 + pair));
        assertEquals(e1, parser.parse("Hey " + pair + " you!"));
        assertEquals(e1, parser.parse("Hey" + pair + "you!"));
    }

    private static SimpleLine buildLine(
            final Set<Singer> singers,
            final String... array
    ) {
        return buildLine(singers, false, false, array);
    }

    private static SimpleLine buildLine(
            final Set<Singer> singers,
            final boolean choir,
            final boolean scat,
            final String... array
    ) {
        final List<LineElement> elements = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++) {
            final LineElement elt = i % 2 == 0 ? new Word(array[i]) : new Punctuation(array[i]);
            elements.add(elt);
        }
        return new SimpleLine(elements, singers, choir, scat);
    }

    // TODO remove following tests

    @Test
    public void test0() {
        final OptionsLineParser parser = new OptionsLineParser(DEFAULT_OPTIONS);
        final List<Line> parsed = parser.parse("My name is unknown");
        final List<Line> expected = List.of(new SimpleLine(
                List.of(new Word("My"), SPACE, new Word("name"), SPACE, new Word("is"), SPACE, new Word("unknown")),
                Set.of(), false, false));
        assertEquals(expected, parsed);
        System.out.println("expected = " + expected);
        System.out.println("parsed = " + parsed);
    }

    @Test
    public void test1() {
        final OptionsLineParser parser = new OptionsLineParser(DEFAULT_OPTIONS);
        final List<Line> parsed = parser.parse("[John]My name is unknown");
        final List<Line> expected = List.of(new SimpleLine(
                List.of(new Word("My"), SPACE, new Word("name"), SPACE, new Word("is"), SPACE, new Word("unknown")),
                Set.of(new Singer("John")), false, false));
        assertEquals(expected, parsed);
        System.out.println("expected = " + expected);
        System.out.println("parsed = " + parsed);
    }

    @Test
    public void test() {
        final OptionsLineParser parser = new OptionsLineParser(DEFAULT_OPTIONS);
        parser.parse("[John] My name is ([Dave, Nick] His name is) unknown (unknown) {la la la la (la la la la)}");
    }

    @Test
    public void testOpeningSinger1() {
        assertThrowsISE(DEFAULT_PARSER, "[John My name is John",
                        "Opening bracket (singer) does not match any closing one: {bracket=[, position=0}");
    }

    private static void assertThrowsISE(
            final OptionsLineParser parser,
            final CharSequence input,
            final String message
    ) {
        final IllegalStateException e = assertThrows(IllegalStateException.class, () -> parser.parse(input));
        if (message != null) assertEquals(message, e.getMessage());
    }

    @Test
    public void testOpeningSinger2() {
        assertThrowsISE(DEFAULT_PARSER, "John My name is John[",
                        "Opening bracket (singer) does not match any closing one: {bracket=[, position=20}");
    }

    @Test
    public void testOpeningSinger3() {
        assertThrowsISE(DEFAULT_PARSER, "John] My name is John",
                        "Closing bracket (singer) does not match any opening one: {bracket=], position=4}");
    }

    @Test
    public void testOpeningSinger4() {
        assertThrowsISE(DEFAULT_PARSER, "[John[ My name is John",
                        "Next bracket shall close this (singer) one (current: {bracket=[, position=0}, next: {bracket=[, position=5})");
    }
}
