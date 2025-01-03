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

import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link fr.byowares.game.miq.core.model.lyrics.LineParser} that treat each line as plain text (no special
 * characters, no special context), except for white spaces:
 * <ul>
 *     <li>White spaces are converted into space (code 'u0020');</li>
 *     <li>Consecutive white spaces are merged into a unique one;</li>
 *     <li>Leading white spaces in the first LineElement are trimmed;</li>
 *     <li>Trailing white spaces in the last LineElement are trimmed.</li>
 * </ul>
 * Result of
 * {@link fr.byowares.game.miq.core.model.lyrics.LineParser#parse(CharSequence) parse(Charsequence} is either
 * {@link fr.byowares.game.miq.core.model.lyrics.Line#EMPTY_LIST} or a list of one
 * {@link fr.byowares.game.miq.core.model.lyrics.Line} that contains at least one word, with no singers, and not
 * considered as backup vocals or non-lexical vocables.
 *
 * @since XXX
 */
public class SpaceCleanerLineParser
        implements LineParser {

    /** Singleton instance of {@link SpaceCleanerLineParser}. */
    public static final SpaceCleanerLineParser INSTANCE = new SpaceCleanerLineParser();

    private static final ThreadLocal<StringBuilder> STRING_BUILDER_TL = ThreadLocal.withInitial(StringBuilder::new);

    private SpaceCleanerLineParser() {
        // Singleton pattern
    }

    /**
     * @param it The {@code CharacterIterator} that iterates over the characters to parse.
     *
     * @return A Set of singers using only the elements who match {@link LineElement#isWord()}.
     */
    public static Set<Singer> parseAsSingers(final CharacterIterator it) {
        final var elements = SpaceCleanerLineParser.parseLineElements(it);
        return parseAsSingers(elements);
    }

    /**
     * Parse a portion of text, which is considered plain (no special characters).
     *
     * @param it The {@code CharacterIterator} that iterates over the characters to parse.
     *
     * @return A list of {@code LineElement}
     */
    public static List<LineElement> parseLineElements(final CharacterIterator it) {
        char current = it.current();
        final List<LineElement> res = new ArrayList<>();
        final StringBuilder sb = STRING_BUILDER_TL.get();
        sb.setLength(0);
        boolean currentlyParsingWord = false;
        boolean isLastCharSpace = false;

        while ((int) current != (int) CharacterIterator.DONE) {
            final boolean isAlphaNum = Character.isAlphabetic((int) current) || Character.isDigit(current);
            final boolean isWhiteSpace = Character.isWhitespace(current);

            if (currentlyParsingWord) {
                if (isAlphaNum) sb.append(current); // Keep parsing as a word
                else { // End of word
                    res.add(new Word(sb.toString()));
                    sb.setLength(0);
                    if (isWhiteSpace) {
                        isLastCharSpace = true;
                        sb.append(' ');
                    } else sb.append(current);
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
                    isLastCharSpace = false;
                } else { // Keep parsing as punctuation
                    if (isWhiteSpace) {
                        if (!isLastCharSpace) { // Sanitize all whitespace in space
                            isLastCharSpace = true;
                            sb.append(' ');
                        } // else Merging all whitespace into one.
                    } else { // Default case, add the character as it is.
                        isLastCharSpace = false;
                        sb.append(current);
                    }
                }
            }

            current = it.next();
        }
        if (!sb.isEmpty()) {
            if (currentlyParsingWord) res.add(new Word(sb.toString()));
            else res.add(new Punctuation(sb.toString()));
        }

        if (!res.isEmpty()) {
            res.set(0, res.getFirst().trimHead());
            res.set(res.size() - 1, res.getLast().trimTail());
        }
        if (!res.isEmpty() && res.getFirst().isWhiteSpaceOnly()) res.removeFirst();
        if (!res.isEmpty() && res.getLast().isWhiteSpaceOnly()) res.removeLast();
        return res;
    }

    /**
     * @param elements The line elements used to be converted in Singer.
     *
     * @return A Set of singers using only the elements who match {@link LineElement#isWord()}.
     */
    static Set<Singer> parseAsSingers(final List<LineElement> elements) {
        if (elements.isEmpty()) return Set.of();
        final Set<Singer> res = Singer.emptySet();
        for (final LineElement elt : elements) {
            if (elt.isWord()) res.add(new Singer(elt.getText()));
        }
        return res;
    }

    @Override
    public List<Line> parse(final CharSequence input) {
        if (input.chars().allMatch(Character::isWhitespace)) return Line.EMPTY_LIST;

        final var it = OptionsLineParser.CHAR_ITERATOR_TL.get();
        it.setText(input, 0, input.length());
        final List<LineElement> list = parseLineElements(it);

        if (list.stream().noneMatch(LineElement::isWord)) return Line.EMPTY_LIST;
        return List.of(new Line(list, Set.of(), false, false));
    }
}
