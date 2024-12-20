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
import fr.byowares.game.utils.text.CharSequenceIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A parser that is able to parse text containing bracket delimited part (known as context):
 * <ul>
 *     <li>Singer;</li>
 *     <li>Choir or backup vocals;</li>
 *     <li>Scat or non-lexical vocables.</li>
 * </ul>
 * When a context has no bracket associated to ({@code null} value), it means the parsing capability is disabled for
 * thi context. Unused brackets are considered as punctuation.
 *
 * @since XXX
 */
public class OptionsLineParser
        implements LineParser {

    private static final ThreadLocal<CharSequenceIterator> CHAR_ITERATOR_TL = //
            ThreadLocal.withInitial(() -> new CharSequenceIterator(""));

    private static final String CLOSING_NESTED = "Closing bracket (%s) found: %s, but last opening bracket (%s) does " + "not match: %s";
    private static final String CLOSING_NOT_OPENED = "Closing bracket (%s) does not match any opening one: %s";
    private static final String OPENING_NESTED = "Opening bracket (%s) found: %s, but previous one was not closed: %s ";
    private static final String OPENING_NOT_CLOSED = "Opening bracket (%s) does not match any closing one: %s";
    private static final String SINGER = "singer";
    private static final String CHOIR = "choir";
    private static final String SCAT = "scat";


    private final Bracket singerBracket;
    private final Bracket choirBracket;
    private final Bracket scatBracket;

    /**
     * Initialise a new {@link fr.byowares.game.miq.core.model.lyrics.LineParser} using {@link fr.byowares.game.miq.core.option.LyricsParsingOptions}
     *
     * @param options The set of options used to parse the text.
     */
    public OptionsLineParser(final LyricsParsingOptions options) {
        if (!options.isValid()) isf("LyricsParsingOptions are not valid: %s", options);
        this.singerBracket = options.singerBracket();
        this.choirBracket = options.choirBracket();
        this.scatBracket = options.scatBracket();
    }

    /**
     * @throws java.lang.IllegalStateException Whose message is formatted using {@link java.lang.String#format(String, Object...)}
     */
    private static void isf(
            final String format,
            final Object... objects
    )
            throws IllegalStateException {
        ise(String.format(format, objects));
    }

    /**
     * @throws java.lang.IllegalStateException Whose message the one given in argument.
     */
    private static void ise(final String message)
            throws IllegalStateException {
        throw new IllegalStateException(message);
    }

    @Override
    public List<Line> parse(final CharSequence input) {
        if (input.chars().allMatch(Character::isWhitespace)) return BlankLine.ONE_BLANK_LINE;

        // Will throw an exception if the input is badly formatted
        final List<CharPosition> charPositions = this.analyseInput(input);
        // Adding a fake CharPosition at the end to treat all the input in the loop.
        charPositions.add(new CharPosition(null, true, input.length()));

        final CharSequenceIterator charSeqIt = CHAR_ITERATOR_TL.get();
        // Contextual information used to instantiates LineElements & Singers.
        // Position set to -1, because of the +1 used to ignore the bracket.
        CharPosition lastCp = new CharPosition(null, true, -1);
        boolean isChoir = false;
        boolean isScat = false;
        Set<Singer> singers = Set.of();
        final List<Line> res = new ArrayList<>();

        for (final CharPosition cp : charPositions) {
            if (this.choirBracket != null && this.choirBracket == lastCp.bracket) {
                isChoir = lastCp.open;
                singers = Set.of();
            }
            if (this.scatBracket != null && this.scatBracket == lastCp.bracket) {
                isScat = lastCp.open;
                singers = Set.of();
            }
            final boolean isParsingSinger = this.singerBracket != null && this.singerBracket == lastCp.bracket && lastCp.open;
            // lastCp.position + 1 for ignoring the last character which is a bracket (we don't need to parse it).
            // Hence the first position set to -1 when initialising lastCp.
            charSeqIt.setText(input, lastCp.position + 1, cp.position);
            lastCp = cp;
            if (charSeqIt.getBeginIndex() == charSeqIt.getEndIndex()) continue; // Nothing to parse
            final List<LineElement> lineElements = SimpleLineParser.parseLineElements(charSeqIt);
            if (lineElements.isEmpty()) continue;

            if (isParsingSinger) singers = toSinger(lineElements);
            else res.add(new SimpleLine(lineElements, singers, isChoir, isScat));
        }
        return res;
    }

    /**
     * @param input The input to analyse.
     *
     * @return The list of {@code CharPosition} for all brackets for parsing elements, in order of appearance.
     *
     * @throws java.lang.IllegalStateException if the {@code input} is not correctly formatted.
     */
    private List<CharPosition> analyseInput(final CharSequence input) {
        final List<CharPosition> res = new ArrayList<>();
        extractCharPosition(res, input, this.singerBracket);
        extractCharPosition(res, input, this.choirBracket);
        extractCharPosition(res, input, this.scatBracket);
        Collections.sort(res);

        // Validating format
        // Singer brackets do not allow nested brackets.
        // BackVocals and Scat brackets accept nested brackets, but cannot contain themselves.
        CharPosition lastChoirOpened = null;
        CharPosition lastScatOpened = null;
        final Iterator<CharPosition> it = res.iterator();

        while (it.hasNext()) {
            final CharPosition current = it.next();
            if (current.bracket == this.choirBracket) {
                lastChoirOpened = current.validateBracket(lastChoirOpened, lastScatOpened, CHOIR, SCAT);

            } else if (current.bracket == this.scatBracket) {
                lastScatOpened = current.validateBracket(lastScatOpened, lastChoirOpened, SCAT, CHOIR);

            } else if (current.bracket == this.singerBracket) {
                if (!current.open) isf(CLOSING_NOT_OPENED, SINGER, current);
                if (!it.hasNext()) isf(OPENING_NOT_CLOSED, SINGER, current);
                // Checking directly next bracket, as it does not allow nested brackets.
                final CharPosition next = it.next();
                if (next.bracket != this.singerBracket)
                    ise("Next bracket does not match this (singer) one (current: " + current + ", next: " + next + ")");
                if (next.open)
                    ise("Next bracket shall close this (singer) one (current: " + current + ", next: " + next + ")");

            } else {
                ise("Unknown bracket: " + current.detailedToString());
            }
        }
        final boolean hasChoirOpened = lastChoirOpened != null;
        final boolean hasScatOpened = lastScatOpened != null;
        if (hasChoirOpened) {
            if (hasScatOpened) isf(OPENING_NOT_CLOSED, CHOIR + " & " + SCAT, lastChoirOpened + " & " + lastScatOpened);
            else isf(OPENING_NOT_CLOSED, CHOIR, lastChoirOpened);
        } else if (hasScatOpened) isf(OPENING_NOT_CLOSED, SCAT, lastScatOpened);

        return res;
    }


    /**
     * @param elements The line elements used to be converted in Singer.
     *
     * @return A Set of singers using only the elements who match {@link LineElement#isWord()}.
     */
    private static Set<Singer> toSinger(final List<LineElement> elements) {
        final Set<Singer> res = new TreeSet<>();
        for (final LineElement elt : elements) {
            if (elt.isWord()) res.add(new Singer(elt.getText()));
        }
        return res;
    }

    /**
     * @param l  The list of {@code CharPosition} to add new ones.
     * @param cs The {@code CharSequence} the parse.
     * @param b  The {@code Bracket} to look for in the {@code cs}.
     */
    private static void extractCharPosition(
            final List<CharPosition> l,
            final CharSequence cs,
            final Bracket b
    ) {
        if (b == null) return;
        for (int i = 0; i < cs.length(); i++) {
            if (areCharEquals(b.getOpen(), cs.charAt(i))) l.add(new CharPosition(b, true, i));
            if (areCharEquals(b.getClose(), cs.charAt(i))) l.add(new CharPosition(b, false, i));
        }
    }

    /**
     * @return {@code true} if the two characters are equals, false otherwise.
     */
    private static boolean areCharEquals(
            final char a,
            final char b
    ) {
        return (int) a == (int) b;
    }

    /**
     * @param bracket  The kind of bracket encountered.
     * @param open     Whether it's the opening or closing character.
     * @param position The position of the character in the characters' sequence.
     */
    private record CharPosition(
            Bracket bracket,
            boolean open,
            int position
    )
            implements Comparable<CharPosition> {

        private String detailedToString() {
            final String detailedToString = this.bracket == null ? "null" : this.bracket.detailedToString();
            return "CharPosition{" + "bracket=" + detailedToString + ", open=" + this.open + ", position=" + this.position + '}';
        }

        @Override
        public String toString() {
            final Bracket b = this.bracket;
            final char c = b == null ? 'N' : (this.open ? b.getOpen() : b.getClose());
            return "{bracket=" + c + ", position=" + this.position + '}';
        }

        /**
         * Make sure that opening and closing brackets are well sequenced.
         *
         * @param lastCurrent The last bracket of the same type.
         * @param lastOther   The last bracket of the other type.
         * @param currentKind The context related to the current bracket (for logging purpose).
         * @param otherKind   The context related to the other bracket (for logging purpose).
         *
         * @return {@code this} if it's an opening bracket, {@code null} if it's a closing one.
         *
         * @throws java.lang.IllegalStateException If the brackets are not valid.
         */
        private CharPosition validateBracket(
                final CharPosition lastCurrent,
                final CharPosition lastOther,
                final String currentKind,
                final String otherKind
        ) {
            if (this.open) {
                if (lastCurrent != null) isf(OPENING_NESTED, currentKind, lastCurrent, this);
                return this;
            } else {
                if (lastCurrent == null) isf(CLOSING_NOT_OPENED, currentKind, this);
                if (getMax(lastCurrent, lastOther) == lastOther)
                    isf(CLOSING_NESTED, currentKind, this, otherKind, lastOther);
                return null;
            }
        }

        private static CharPosition getMax(
                final CharPosition a,
                final CharPosition b
        ) {
            if (a == null) return b;
            if (b == null) return a;
            return a.compareTo(b) > 0 ? a : b;
        }

        @Override
        public int compareTo(final CharPosition o) {
            return Integer.compare(this.position, o.position);
        }
    }
}
