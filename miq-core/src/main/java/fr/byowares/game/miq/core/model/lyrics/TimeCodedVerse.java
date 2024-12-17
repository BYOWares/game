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

/**
 * @param line  The line (words) sung during that range
 * @param range The period during which the line is sung
 *
 * @since XXX
 */
public record TimeCodedVerse(SingerLine line, Range range)
        implements Comparable<TimeCodedVerse> {

    @Override
    public int compareTo(final TimeCodedVerse o) {
        return this.range.compareTo(o.range);
    }
}