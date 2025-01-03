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

import fr.byowares.game.miq.core.model.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * @param range The period during which the line is sung.
 * @param lines The lines sung during that period.
 *
 * @since XXX
 */
public record TimeCodedVerse(
        Range range,
        List<Line> lines
)
        implements Comparable<TimeCodedVerse> {

    @Override
    public int compareTo(final TimeCodedVerse o) {
        return this.range.compareTo(o.range);
    }

    /**
     * Make a copy of this instance:
     * <ul>
     *     <li>The list of LineElement is a shallow copy;</li>
     *     <li>The set of Singer is a shallow copy.</li>
     * </ul>
     *
     * @return A copy of this instance as described above.
     */
    public TimeCodedVerse copy() {
        if (this.lines == null) return new TimeCodedVerse(this.range, null);
        final List<Line> lines = new ArrayList<>(this.lines.size());
        for (final Line line : this.lines)
            lines.add(new Line(line.elements(), line.singers(), line.isBackVocals(), line.isNonLexical()));

        return new TimeCodedVerse(this.range, lines);
    }
}
