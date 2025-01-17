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

import fr.byowares.game.miq.core.option.LyricsDisplayOptions;
import fr.byowares.game.utils.hashcodes.HashCodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Represent a homogeneous part of text to sing:
 * <ul>
 *     <li>{@code elements}: the actual text to sing;</li>
 *     <li>{@code singers}: the singers in charge of singing this text;</li>
 *     <li>{@code isBackVocals}: whether it is back vocals;</li>
 *     <li>{@code elements}: whether the text shall be considered as non-lexical vocables (e.g.: La la la lala, la la la lala).</li>
 * </ul>
 *
 * @since XXX
 */
public final class Line {

    /** Immutable empty list. */
    public static final List<Line> EMPTY_LIST = List.of();
    private static final String SPACE = " ";
    private static final String HIDDEN_CHAR = "_";

    private final List<LineElement> elements;
    private final Set<Singer> singers;
    private final boolean isBackVocals;
    private final boolean isNonLexical;

    /**
     * @param elements     The LineElements composing the Line.
     * @param singers      The set of singers in charge of singing this Line.
     * @param isBackVocals Whether they are sung by back vocalists.
     * @param isNonLexical Whether it's made of non-lexical vocables (e.g.: La la la lala, la la la lala).
     */
    public Line(
            final List<LineElement> elements,
            final Set<Singer> singers,
            final boolean isBackVocals,
            final boolean isNonLexical
    ) {
        this.elements = elements;
        this.singers = singers;
        this.isBackVocals = isBackVocals;
        this.isNonLexical = isNonLexical;
    }

    /**
     * Take a list of {@code Line}, merge all consecutive lines that can be merged into a single one, and then
     * return the new list of merged lines.
     *
     * @param lines The list of {@code Line} to try to merge.
     *
     * @return A new list with elements merged if possible.
     *
     * @see #canBeMerged (Line, Line)
     * @see #merge (Line, Line)
     */
    public static List<Line> merge(final List<Line> lines) {
        final List<Line> res = new ArrayList<>(lines.size());
        if (lines.isEmpty()) return res;

        for (final Line line : lines) {
            if (res.isEmpty()) res.add(line);
            else if (canBeMerged(res.getLast(), line)) res.add(merge(res.removeLast(), line));
            else res.add(line);
        }
        return res;
    }

    /**
     * @param sl1 A {@code Line}.
     * @param sl2 Another {@code Line}.
     *
     * @return {@code true} if the two {@code Line} are equals except for the text, {@code false} otherwise.
     */
    private static boolean canBeMerged(
            final Line sl1,
            final Line sl2
    ) {
        return Objects.equals(sl1.singers, sl2.singers) && //
                sl1.isBackVocals == sl2.isBackVocals && //
                sl1.isNonLexical == sl2.isNonLexical;
    }

    /**
     * Merge two {@code Line} together. Make sure that {@link fr.byowares.game.miq.core.model.lyrics.Word} and
     * {@link fr.byowares.game.miq.core.model.lyrics.Punctuation} are alternated. When the last
     * {@link fr.byowares.game.miq.core.model.lyrics.LineElement} of the first one, and the first element of the
     * second one are:
     * <ul>
     *     <li>Both {@code Word}: a {@code Punctuation} containing a simple space is added between the two lines;</li>
     *     <li>A {@code Word} and a {@code Punctuation}: a space is added to the existing punctuation, next to the
     *     word (at the end of the existing punctuation if it comes from the first line, or at the start of the
     *     existing punctuation if it comes from the second line);</li>
     *     <li>Both {@code Punctuation}: a new {@code Punctuation} is made concatenating them with a space between.</li>
     * </ul>
     *
     * @param sl1 The first {@code Line} to merge (its text will be first).
     * @param sl2 The second {@code Line} to merge (its text will be last).
     *
     * @return The merged {@code Line}.
     */
    private static Line merge(
            final Line sl1,
            final Line sl2
    ) {
        if (sl1.elements.isEmpty()) return sl2;
        if (sl2.elements.isEmpty()) return sl1;
        final boolean sl1LastIsWord = sl1.elements.getLast().isWord();
        final boolean sl2FirstIsWord = sl2.elements.getFirst().isWord();
        final List<LineElement> newElements = new ArrayList<>(sl1.elements.size() + sl2.elements.size() + 1);
        newElements.addAll(sl1.elements);
        // Use a Stream because removeFirst would not work on immutable lists.
        Stream<LineElement> sl2Stream = sl2.elements.stream();
        if (sl1LastIsWord) {
            if (sl2FirstIsWord) {
                newElements.add(new Punctuation(SPACE));
            } else {
                final LineElement pf2 = sl2.elements.getFirst();
                sl2Stream = sl2Stream.skip(1L);
                newElements.add(new Punctuation(SPACE + pf2.getText()));
            }
        } else {
            final LineElement pl1 = newElements.removeLast();
            if (sl2FirstIsWord) {
                newElements.add(new Punctuation(pl1.getText() + SPACE));
            } else {
                final LineElement pf2 = sl2.elements.getFirst();
                sl2Stream = sl2Stream.skip(1L);
                newElements.add(new Punctuation(pl1.getText() + SPACE + pf2.getText()));
            }
        }
        sl2Stream.forEach(newElements::add);
        return new Line(newElements, sl1.singers, sl1.isBackVocals, sl1.isNonLexical);
    }

    private static CharSequence hideWord(final int length) {
        return HIDDEN_CHAR.repeat(length);
    }

    /**
     * @return {@code true} if this line represent back vocals, {@code false} otherwise.
     */
    public boolean isBackVocals() {
        return this.isBackVocals;
    }

    /**
     * @return {@code true} if this line represent non-lexical vocables, {@code false} otherwise.
     */
    public boolean isNonLexical() {
        return this.isNonLexical;
    }

    /**
     * @return The singers in charge of this line.
     */
    public Set<Singer> getSingers() {
        return this.singers;
    }

    /**
     * @param options The set of display options.
     *
     * @return The text value representing this line when hidden.
     */
    public CharSequence getHiddenText(final LyricsDisplayOptions options) {
        final var hidden = options.whileGuessingShowTrueLength() ? null : hideWord(options.whileGuessingWordLength());
        final StringBuilder sb = new StringBuilder();
        for (final LineElement elt : this.elements) {
            if (elt.isWord()) sb.append(hidden == null ? hideWord(elt.getText().length()) : hidden);
            else sb.append(options.whileGuessingShowPunctuation() ? elt.getText() : SPACE);
        }
        return sb.toString();
    }

    /**
     * @return The text represented by this line when nothing is hidden.
     */
    public CharSequence getClearText() {
        final StringBuilder sb = new StringBuilder();
        for (final LineElement elt : this.elements) {
            sb.append(elt.getText());
        }
        return sb.toString();
    }

    /**
     * @return A Shallow copy of this instance.
     */
    public Line copy() {
        return new Line(this.elements, this.singers, this.isBackVocals, this.isNonLexical);
    }

    /**
     * Copy this line text, but update the fields as follow.
     *
     * @param singers      The set of singers in charge of singing this Line.
     * @param isBackVocals Whether they are sung by back vocalists.
     * @param isNonLexical Whether it's made of non-lexical vocables (e.g.: La la la lala, la la la lala).
     *
     * @return A shallow copy of this instance.
     */
    public Line copy(
            final Collection<CharSequence> singers,
            final boolean isBackVocals,
            final boolean isNonLexical
    ) {
        return new Line(this.elements, Singer.from(singers), isBackVocals, isNonLexical);
    }

    @Override
    public int hashCode() {
        return HashCodes.hash(this.elements, this.singers, this.isBackVocals, this.isNonLexical);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Line line)) return false;
        return this.isBackVocals == line.isBackVocals //
                && this.isNonLexical == line.isNonLexical //
                && Objects.equals(this.elements, line.elements) //
                && Objects.equals(this.singers, line.singers);
    }
}
