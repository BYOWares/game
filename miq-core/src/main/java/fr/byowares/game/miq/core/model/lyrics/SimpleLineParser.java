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

import fr.byowares.game.utils.text.CharSequenceIterator;

import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link fr.byowares.game.miq.core.model.lyrics.LineParser} that treat each line as plain text (no special
 * characters, no special context). Thus, the result of
 * {@link fr.byowares.game.miq.core.model.lyrics.LineParser#parse(CharSequence) parse(Charsequence} is always a list
 * of 1 element: either a {@link fr.byowares.game.miq.core.model.lyrics.BlankLine} or a
 * {@link fr.byowares.game.miq.core.model.lyrics.SimpleLine} with no singers, and not considered as backup vocals or
 * non-lexical vocables.
 *
 * @since XXX
 */
public class SimpleLineParser
        implements LineParser {

    private static final ThreadLocal<StringBuilder> STRING_BUILDER_TL = ThreadLocal.withInitial(StringBuilder::new);

    @Override
    public List<Line> parse(final CharSequence input) {
        if (input.chars().allMatch(Character::isWhitespace)) return BlankLine.ONE_BLANK_LINE;
        final CharSequenceIterator charSeqIt = new CharSequenceIterator(input);
        final List<LineElement> elements = parseLineElements(charSeqIt);
        return List.of(new SimpleLine(elements, Set.of(), false, false));
    }

    /**
     * Parse a portion of text, which is considered plain (no special characters).
     *
     * @param it The {@code CharacterIterator} that iterates over the characters to parse.
     *
     * @return A list of {@code LineElement}
     */
    static List<LineElement> parseLineElements(final CharacterIterator it) {
        char current = it.current();
        final List<LineElement> res = new ArrayList<>();
        final StringBuilder sb = STRING_BUILDER_TL.get();
        sb.setLength(0);
        boolean currentlyParsingWord = false;

        while ((int) current != (int) CharacterIterator.DONE) {
            final boolean isAlphaNum = Character.isAlphabetic((int) current) || Character.isDigit(current);

            if (currentlyParsingWord) {
                if (isAlphaNum) sb.append(current); // Keep parsing as a word
                else { // End of word
                    res.add(new Word(sb.toString()));
                    sb.setLength(0);
                    sb.append(current);
                    currentlyParsingWord = false;
                }

            } else {
                if (isAlphaNum) { // End of punctuation
                    if (!sb.isEmpty()) {
                        res.add(new Punctuation(sb.toString()));
                        sb.setLength(0);
                    }
                    sb.append(current);
                    currentlyParsingWord = true;
                } else sb.append(current); // Keep parsing as punctuation
            }

            current = it.next();
        }
        if (!sb.isEmpty()) {
            if (currentlyParsingWord) res.add(new Word(sb.toString()));
            else res.add(new Punctuation(sb.toString()));
        }

        if (!res.isEmpty() && res.getFirst().isWhiteSpaceOnly()) res.removeFirst();
        if (!res.isEmpty() && res.getLast().isWhiteSpaceOnly()) res.removeLast();
        return res;
    }
}
