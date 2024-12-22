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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A basic line that can have singers, and is not a back vocals or a non-lexical vocables one.
 *
 * @param elements     The LineElements composing the Line.
 * @param singers      The set of singers in charge of singing this Line.
 * @param isBackVocals Whether they are sung by back vocalists.
 * @param isNonLexical Whether it's made of non-lexical vocables (e.g.: La la la lala, la la la lala).
 *
 * @since XXX
 */
public record SimpleLine(
        List<LineElement> elements,
        Set<Singer> singers,
        boolean isBackVocals,
        boolean isNonLexical
)
        implements Line {

    private static final String SPACE = " ";

    /**
     * Take a list of {@code SimpleLine}, merge all consecutive lines that can be merged into a single one, and then
     * return the new list of merged lines.
     *
     * @param lines The list of {@code SimpleLine} to try to merge.
     *
     * @return A new list with elements merged if possible.
     *
     * @see #canBeMerged(SimpleLine, SimpleLine)
     * @see #merge(SimpleLine, SimpleLine)
     */
    public static List<Line> merge(final List<SimpleLine> lines) {
        if (lines.isEmpty()) return List.of();
        final List<Line> res = new ArrayList<>(lines.size());
        final Iterator<SimpleLine> it = lines.iterator();
        SimpleLine lastAdded = it.next();
        res.add(lastAdded);
        while (it.hasNext()) {
            final SimpleLine next = it.next();
            if (canBeMerged(lastAdded, next)) {
                res.removeLast();
                lastAdded = merge(lastAdded, next);
            } else lastAdded = next;
            res.add(lastAdded);
        }
        return res;
    }

    /**
     * @param sl1 A {@code SimpleLine}.
     * @param sl2 Another {@code SimpleLine}.
     *
     * @return {@code true} if the two {@code SimpleLine} are equals except for the text, {@code false} otherwise.
     */
    private static boolean canBeMerged(
            final SimpleLine sl1,
            final SimpleLine sl2
    ) {
        return Objects.equals(sl1.singers, sl2.singers) && //
                sl1.isBackVocals == sl2.isBackVocals && //
                sl1.isNonLexical == sl2.isNonLexical;
    }

    /**
     * Merge two {@code SimpleLine} together. Make sure that {@link fr.byowares.game.miq.core.model.lyrics.Word} and
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
     * @param sl1 The first {@code SimpleLine} to merge (its text will be first).
     * @param sl2 The second {@code SimpleLine} to merge (its text will be last).
     *
     * @return The merged {@code SimpleLine}.
     */
    private static SimpleLine merge(
            final SimpleLine sl1,
            final SimpleLine sl2
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
        return new SimpleLine(newElements, sl1.singers, sl1.isBackVocals, sl1.isNonLexical);
    }

    @Override
    public Set<Singer> getSingers() {
        return this.singers;
    }

    @Override
    public boolean isBackVocals() {
        return false;
    }

    @Override
    public boolean isNonLexicalVocables() {
        return false;
    }
}
