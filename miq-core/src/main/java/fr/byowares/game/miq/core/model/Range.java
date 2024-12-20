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
package fr.byowares.game.miq.core.model;

import fr.byowares.game.utils.hashcodes.HashCodesLong;

/**
 * Representation of a range (both inclusive boundaries).
 *
 * @param start The start of the range
 * @param end   The end of the range
 *
 * @since XXX
 */
public record Range(long start, long end)
        implements Comparable<Range> {

    /**
     * @param start The start of the range
     * @param end   The end of the range
     */
    public Range {
        if (start > end)
            throw new IllegalArgumentException("start (" + start + ") must be inferior or equals to end (" + end + ")");
    }

    /**
     * @param start The start of the range.
     * @param end   The end of the range.
     *
     * @return The {@link Range} defined by the two given values.
     *
     * @throws java.lang.IllegalArgumentException If and only if the end is strictly lower than the start.
     */
    public static Range fromAbsoluteValues(
            final long start,
            final long end
    ) {
        return new Range(start, end);
    }

    /**
     * @param start start of the range
     * @param width width of the range
     *
     * @return The {@link Range} starting at {@code start} and ending at {@code start + width}.
     *
     * @throws java.lang.IllegalArgumentException If and only if the width is negative.
     */
    public static Range fromRelativeValues(
            final long start,
            final long width
    ) {
        return new Range(start, start + width);
    }

    @Override
    public int hashCode() {
        return HashCodesLong.hash(this.start, this.end);
    }

    @Override
    public String toString() {
        return "[" + this.start + ", " + this.end + "]";
    }

    @Override
    public int compareTo(final Range o) {
        final int res = Long.compare(this.start, o.start);
        if (res != 0) return res;
        return Long.compare(this.end, o.end);
    }
}
