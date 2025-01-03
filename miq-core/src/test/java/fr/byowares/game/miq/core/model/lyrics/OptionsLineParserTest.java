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

    private static final List<OptionsLineParser> ALL_VALID_OLP = new ArrayList<>();

    static {
        final List<Bracket> allBrackets = new ArrayList<>(Arrays.asList(Bracket.values()));
        allBrackets.add(null);
        final var options = new LyricsParsingOptions();
        for (final Bracket singer : allBrackets) {
            for (final Bracket back : allBrackets) {
                if (!(singer == null || singer != back)) continue;
                for (final Bracket non : allBrackets) {
                    if (!(back == null || back != non)) continue;
                    if (!(singer == null || singer != non)) continue;
                    ALL_VALID_OLP.add(new OptionsLineParser(options.singer(singer).backVocals(back).nonLexical(non)));
                }
            }
        }
    }

    public static Stream<OptionsLineParser> generateOLPAllValid() {
        return ALL_VALID_OLP.stream();
    }

    /**
     * @param singerNull           {@code null} if not filter must be applied on the singer bracket option, {@code true}
     *                             if it must be {@code null}, {@code false} if it must be non-null.
     * @param backVocalsMustBeNull {@code null} if not filter must be applied on the backup vocals bracket option,
     *                             {@code true} if it must be {@code null}, {@code false} if it must be non-null.
     * @param NonLexicalMustBeNull {@code null} if not filter must be applied on the non-lexical vocables bracket
     *                             option, {@code true} if it must be {@code null}, {@code false} if it must be non-null.
     *
     * @return The stream of valid {@link fr.byowares.game.miq.core.model.lyrics.OptionsLineParser} with filters
     * applied.
     */
    public static Stream<OptionsLineParser> generateOLP(
            final Boolean singerNull,
            final Boolean backVocalsMustBeNull,
            final Boolean NonLexicalMustBeNull
    ) {
        return ALL_VALID_OLP.stream().filter(p -> {
            final LyricsParsingOptions options = p.options();
            if (singerNull != null && Objects.isNull(options.singerBracket()) != singerNull) return false;
            if (backVocalsMustBeNull != null && Objects.isNull(options.backVocalsBracket()) != backVocalsMustBeNull)
                return false;
            return (NonLexicalMustBeNull == null || Objects.isNull(
                    options.nonLexicalBracket()) == NonLexicalMustBeNull);
        });
    }

    public static Stream<OptionsLineParser> generateOLPSingerDefined() {
        return generateOLP(false, null, null);
    }

    public static Stream<OptionsLineParser> generateOLPBackVocalsDefined() {
        return generateOLP(null, false, null);
    }

    public static Stream<OptionsLineParser> generateOLPNonLexicalDefined() {
        return generateOLP(null, null, false);
    }

    public static Stream<OptionsLineParser> generateOLPSingerBackVocalsDefined() {
        return generateOLP(false, false, null);
    }

    public static Stream<OptionsLineParser> generateOLPSingerNonLexicalDefined() {
        return generateOLP(false, null, false);
    }

    public static Stream<OptionsLineParser> generateOLPBackVocalsNonLexicalDefined() {
        return generateOLP(null, false, false);
    }

    public static Stream<OptionsLineParser> generateOLPAllDefined() {
        return generateOLP(false, false, false);
    }

    private static Line buildLine(
            final Set<Singer> singers,
            final String... array
    ) {
        return buildLine(singers, false, false, array);
    }

    private static void assertThrowsISE(
            final OptionsLineParser parser,
            final CharSequence input,
            final String message
    ) {
        final IllegalStateException e = assertThrows(IllegalStateException.class, () -> parser.parse(input));
        if (message != null) assertEquals(message, e.getMessage());
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
    public void testInvalidOption() {
        final var options = new LyricsParsingOptions().singer(Bracket.ANGLE).backVocals(Bracket.ANGLE);
        final var e = assertThrows(IllegalStateException.class, () -> new OptionsLineParser(options));
        assertTrue(e.getMessage().startsWith("LyricsParsingOptions are not valid: "));
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPAllValid")
    public void testEmptyLine(final OptionsLineParser parser) {
        assertEquals(Line.EMPTY_LIST, parser.parse(""));
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPSingerDefined")
    public void testOnlySingerContext(final OptionsLineParser parser) {
        final char open = parser.options().singerBracket().getOpen();
        final char close = parser.options().singerBracket().getClose();
        final String ws = SpaceCleanerLineParserTest.WHITESPACES_STRING;
        final String name = "David";
        assertEquals(Line.EMPTY_LIST, parser.parse(open + name + close));
        assertEquals(Line.EMPTY_LIST, parser.parse(open + name + close + ws));
        assertEquals(Line.EMPTY_LIST, parser.parse(ws + open + name + close));
        assertEquals(Line.EMPTY_LIST, parser.parse(ws + open + name + close + ws));
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPSingerDefined")
    public void testSingerContext(final OptionsLineParser parser) {
        final String open = Character.toString(parser.options().singerBracket().getOpen());
        final String close = Character.toString(parser.options().singerBracket().getClose());
        final String pair = open + close;
        final String ws = SpaceCleanerLineParserTest.WHITESPACES_STRING;

        final String n1 = open + "David" + close;
        final String n2 = open + "David...Roger|Richard" + close;
        final String n3 = open + "Roger,Richard" + ws + "David" + close;
        final Set<Singer> s0 = Set.of();
        final Set<Singer> s1 = Set.of(new Singer("David"));
        final Set<Singer> s2 = Set.of(new Singer("David"), new Singer("Richard"), new Singer("Roger"));

        final String l1 = "Hey you!";
        final String l2 = "out there";
        final String l3 = "in the cold,";

        final var e1 = List.of(buildLine(s0, "Hey", " ", "you", "!"));
        assertEquals(e1, parser.parse(l1));
        assertEquals(e1, parser.parse(pair + l1));
        assertEquals(e1, parser.parse(open + ".-/" + close + l1));
        assertEquals(e1, parser.parse(l1 + pair));
        assertEquals(e1, parser.parse("Hey " + pair + " you!"));
        assertEquals(e1, parser.parse("Hey" + pair + "you!"));

        final var e2 = List.of(buildLine(s2, "Hey", " ", "you", "!"));
        assertEquals(e2, parser.parse(n2 + l1));
        assertEquals(e2, parser.parse(n3 + l1));
        assertEquals(e2, parser.parse(n2 + "Hey" + n3 + "you!"));
        assertEquals(e2, parser.parse(n2 + "Hey " + n3 + " you!"));
        assertEquals(e2, parser.parse(n3 + "Hey" + n2 + "you!"));
        assertEquals(e2, parser.parse(n3 + "Hey " + n2 + " you!"));

        final var e3 = List.of(buildLine(s2, "Hey", " ", "you", "!"), //
                               buildLine(s1, "out", " ", "there"), //
                               buildLine(s2, "in", " ", "the", " ", "cold", ","));
        assertEquals(e3, parser.parse(n2 + l1 + n1 + l2 + n3 + l3));

        final String o = "Opening bracket (singer) does not match any closing one: {bracket=";
        final String c = "Closing bracket (singer) does not match any opening one: {bracket=";
        assertThrowsISE(parser, open + "Dave Hey you", o + open + ", position=0}");
        assertThrowsISE(parser, "Dave Hey you" + open, o + open + ", position=12}");
        assertThrowsISE(parser, open + "Dave" + open + " Hey you",
                        "Next bracket shall close this (singer) one " + "(current: {bracket=" + open + ", position=0}, next: {bracket=" + open + ", position=5})");
        assertThrowsISE(parser, "Dave" + close + " Hey you", c + close + ", position=4}");
        assertThrowsISE(parser, "Dave Hey you" + close, c + close + ", position=12}");
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPBackVocalsDefined")
    public void testBackupVocalsContext(final OptionsLineParser parser) {
        final String open = Character.toString(parser.options().backVocalsBracket().getOpen());
        final String close = Character.toString(parser.options().backVocalsBracket().getClose());
        final String pair = open + close;
        final String ws = SpaceCleanerLineParserTest.WHITESPACES_STRING;

        final Set<Singer> s0 = Set.of();
        final String l1 = "Hey you!";

        final var e1 = List.of(buildLine(s0, "Hey", " ", "you", "!"));
        assertEquals(e1, parser.parse(l1));
        assertEquals(e1, parser.parse(pair + l1));
        assertEquals(e1, parser.parse(l1 + pair));
        assertEquals(e1, parser.parse("Hey " + pair + " you!"));
        assertEquals(e1, parser.parse("Hey" + pair + "you!"));

        final var e2 = List.of(buildLine(s0, true, false, "Hey", " ", "you", "!"));
        assertEquals(e2, parser.parse(open + l1 + close));
        assertEquals(e2, parser.parse(open + "Hey" + close + open + "you!" + close));
        assertEquals(e2, parser.parse(open + "Hey " + close + open + " you!" + close));
        assertEquals(e2, parser.parse(open + "Hey" + close + ws + open + "you!" + close));
        assertEquals(e2, parser.parse(open + "Hey " + close + ws + open + " you!" + close));

        // Only punctuation line are ignored.
        assertEquals(Line.EMPTY_LIST, parser.parse(open + ".-/!" + close));

        final String o = "Opening bracket (backup vocals) does not match any closing one: {bracket=";
        final String c = "Closing bracket (backup vocals) does not match any opening one: {bracket=";
        assertThrowsISE(parser, open + "Dave Hey you", o + open + ", position=0}");
        assertThrowsISE(parser, "Dave Hey you" + open, o + open + ", position=12}");
        assertThrowsISE(parser, open + "Dave" + open + " Hey you",
                        "Opening bracket (backup vocals) found: {bracket=" + open + ", position=0}, but previous one was not closed: {bracket=" + open + ", position=5}");
        assertThrowsISE(parser, "Dave" + close + " Hey you", c + close + ", position=4}");
        assertThrowsISE(parser, "Dave Hey you" + close, c + close + ", position=12}");
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPNonLexicalDefined")
    public void testNonLexicalVocablesContext(final OptionsLineParser parser) {
        final String open = Character.toString(parser.options().nonLexicalBracket().getOpen());
        final String close = Character.toString(parser.options().nonLexicalBracket().getClose());
        final String pair = open + close;
        final String ws = SpaceCleanerLineParserTest.WHITESPACES_STRING;

        final Set<Singer> s0 = Set.of();
        final String l1 = "Hey you!";

        final var e1 = List.of(buildLine(s0, "Hey", " ", "you", "!"));
        assertEquals(e1, parser.parse(l1));
        assertEquals(e1, parser.parse(pair + l1));
        assertEquals(e1, parser.parse(l1 + pair));
        assertEquals(e1, parser.parse("Hey " + pair + " you!"));
        assertEquals(e1, parser.parse("Hey" + pair + "you!"));

        final var e2 = List.of(buildLine(s0, false, true, "Hey", " ", "you", "!"));
        assertEquals(e2, parser.parse(open + l1 + close));
        assertEquals(e2, parser.parse(open + "Hey" + close + open + "you!" + close));
        assertEquals(e2, parser.parse(open + "Hey " + close + open + " you!" + close));
        assertEquals(e2, parser.parse(open + "Hey" + close + ws + open + "you!" + close));
        assertEquals(e2, parser.parse(open + "Hey " + close + ws + open + " you!" + close));

        // Only punctuation line are ignored.
        assertEquals(Line.EMPTY_LIST, parser.parse(open + ".-/!" + close));

        final String o = "Opening bracket (non-lexical vocables) does not match any closing one: {bracket=";
        final String c = "Closing bracket (non-lexical vocables) does not match any opening one: {bracket=";
        assertThrowsISE(parser, open + "Dave Hey you", o + open + ", position=0}");
        assertThrowsISE(parser, "Dave Hey you" + open, o + open + ", position=12}");
        assertThrowsISE(parser, open + "Dave" + open + " Hey you",
                        "Opening bracket (non-lexical vocables) found: {bracket=" + open + ", position=0}, but previous one was not closed: {bracket=" + open + ", position=5}");
        assertThrowsISE(parser, "Dave" + close + " Hey you", c + close + ", position=4}");
        assertThrowsISE(parser, "Dave Hey you" + close, c + close + ", position=12}");
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPSingerBackVocalsDefined")
    public void testSingerBackupVocalsContext(final OptionsLineParser parser) {
        final String so = Character.toString(parser.options().singerBracket().getOpen());
        final String sc = Character.toString(parser.options().singerBracket().getClose());
        final String sp = so + sc;
        final String co = Character.toString(parser.options().backVocalsBracket().getOpen());
        final String cc = Character.toString(parser.options().backVocalsBracket().getClose());

        final String l1 = "Hey you, out there";
        final String n1 = so + "David" + sc;
        final Set<Singer> s0 = Set.of();
        final Set<Singer> s1 = Set.of(new Singer("David"));

        final var e0 = List.of(buildLine(s0, true, false, "Hey", " ", "you", ", ", "out", " ", "there"));
        assertEquals(e0, parser.parse(n1 + co + l1 + cc));
        assertEquals(e0, parser.parse(n1 + " " + co + l1 + cc));
        assertEquals(e0, parser.parse(n1 + " .-; " + co + l1 + cc));

        final var e1 = List.of(buildLine(s1, true, false, "Hey", " ", "you", ", ", "out", " ", "there"));
        assertEquals(e1, parser.parse(co + n1 + l1 + cc));
        assertEquals(e1, parser.parse(co + " " + n1 + " " + l1 + " " + cc));
        assertEquals(e1, parser.parse(co + n1 + l1 + cc));
        assertEquals(e1, parser.parse(co + n1 + "Hey you," + sp + " .... " + n1 + "out there" + cc));

        final String ise1 = "Next bracket does not match this (singer) one (current: {bracket=";
        final String ise21 = ", position=0}, next: {bracket=";
        final String ise22 = ", position=1}, next: {bracket=";
        final String ise31 = ", position=1})";
        final String ise32 = ", position=2})";
        assertThrowsISE(parser, so + co, ise1 + so + ise21 + co + ise31);
        assertThrowsISE(parser, co + so + cc, ise1 + so + ise22 + cc + ise32);
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPSingerNonLexicalDefined")
    public void testSingerNonLexicalVocablesContext(final OptionsLineParser parser) {
        final String so = Character.toString(parser.options().singerBracket().getOpen());
        final String sc = Character.toString(parser.options().singerBracket().getClose());
        final String sp = so + sc;
        final String co = Character.toString(parser.options().nonLexicalBracket().getOpen());
        final String cc = Character.toString(parser.options().nonLexicalBracket().getClose());

        final String l1 = "Hey you, out there";
        final String n1 = so + "David" + sc;
        final Set<Singer> s0 = Set.of();
        final Set<Singer> s1 = Set.of(new Singer("David"));

        final var e0 = List.of(buildLine(s0, false, true, "Hey", " ", "you", ", ", "out", " ", "there"));
        assertEquals(e0, parser.parse(n1 + co + l1 + cc));
        assertEquals(e0, parser.parse(n1 + " " + co + l1 + cc));
        assertEquals(e0, parser.parse(n1 + " .-; " + co + l1 + cc));

        final var e1 = List.of(buildLine(s1, false, true, "Hey", " ", "you", ", ", "out", " ", "there"));
        assertEquals(e1, parser.parse(co + n1 + l1 + cc));
        assertEquals(e1, parser.parse(co + " " + n1 + " " + l1 + " " + cc));
        assertEquals(e1, parser.parse(co + n1 + l1 + cc));
        assertEquals(e1, parser.parse(co + n1 + "Hey you," + sp + " .... " + n1 + "out there" + cc));

        final String ise1 = "Next bracket does not match this (singer) one (current: {bracket=";
        final String ise21 = ", position=0}, next: {bracket=";
        final String ise22 = ", position=1}, next: {bracket=";
        final String ise31 = ", position=1})";
        final String ise32 = ", position=2})";
        assertThrowsISE(parser, so + co, ise1 + so + ise21 + co + ise31);
        assertThrowsISE(parser, co + so + cc, ise1 + so + ise22 + cc + ise32);
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPBackVocalsNonLexicalDefined")
    public void testBackupVocalsNonLexicalVocablesContext(final OptionsLineParser parser) {
        final String so = Character.toString(parser.options().nonLexicalBracket().getOpen());
        final String sc = Character.toString(parser.options().nonLexicalBracket().getClose());
        final String sp = so + sc;
        final String co = Character.toString(parser.options().backVocalsBracket().getOpen());
        final String cc = Character.toString(parser.options().backVocalsBracket().getClose());
        final String cp = co + cc;

        final String l1 = "Hey you, out there";
        final Set<Singer> s0 = Set.of();

        final var e0 = List.of(buildLine(s0, true, true, "Hey", " ", "you", ", ", "out", " ", "there"));
        assertEquals(e0, parser.parse(so + co + l1 + cc + sc));
        assertEquals(e0, parser.parse(co + so + l1 + sc + cc));
        assertEquals(e0, parser.parse(co + so + "Hey you," + sc + cc + cp + sp + so + co + "out there" + cc + sc));

        final String i1 = "Closing bracket (";
        final String i2 = ") found: {bracket=";
        final String i3 = ", position=2}, but last opening bracket (";
        final String i4 = ") does not match: {bracket=";
        final String i5 = ", position=1}";

        final String back = "backup vocals";
        final String non = "non-lexical vocables";
        assertThrowsISE(parser, co + so + cc, i1 + back + i2 + cc + i3 + non + i4 + so + i5);
        assertThrowsISE(parser, so + co + sc, i1 + non + i2 + sc + i3 + back + i4 + co + i5);
    }

    @ParameterizedTest(name = "[{index}] Options={0}")
    @MethodSource("generateOLPAllDefined")
    public void testAllContext(final OptionsLineParser parser) {
        final String io = Character.toString(parser.options().singerBracket().getOpen());
        final String ic = Character.toString(parser.options().singerBracket().getClose());
        final String so = Character.toString(parser.options().nonLexicalBracket().getOpen());
        final String sc = Character.toString(parser.options().nonLexicalBracket().getClose());
        final String co = Character.toString(parser.options().backVocalsBracket().getOpen());
        final String cc = Character.toString(parser.options().backVocalsBracket().getClose());

        final String l1 = "Hey you, out there";
        final String n1 = io + "David" + ic;
        final Set<Singer> s0 = Set.of();
        final Set<Singer> s1 = Set.of(new Singer("David"));

        final var e0 = List.of(buildLine(s0, true, true, "Hey", " ", "you", ", ", "out", " ", "there"));
        final var e1 = List.of(buildLine(s1, true, true, "Hey", " ", "you", ", ", "out", " ", "there"));
        assertEquals(e0, parser.parse(n1 + so + co + l1 + cc + sc));
        assertEquals(e0, parser.parse(n1 + co + so + l1 + sc + cc));
        assertEquals(e0, parser.parse(so + n1 + co + l1 + cc + sc));
        assertEquals(e0, parser.parse(co + n1 + so + l1 + sc + cc));
        assertEquals(e1, parser.parse(so + co + n1 + l1 + cc + sc));
        assertEquals(e1, parser.parse(co + so + n1 + l1 + sc + cc));
        assertEquals(e1, parser.parse(so + co + n1 + l1 + cc + sc));

        assertThrowsISE(parser, co + so + ic, null);
        assertThrowsISE(parser, co + so + ic, null);
        assertThrowsISE(parser, co + so + io + cc, null);
        assertThrowsISE(parser, co + so + io + sc, null);
        assertThrowsISE(parser, so + co + io + cc, null);
        assertThrowsISE(parser, so + co + io + sc, null);
    }
}
