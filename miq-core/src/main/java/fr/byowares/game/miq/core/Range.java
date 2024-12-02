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
package fr.byowares.game.miq.core;

import fr.byowares.game.utils.hashcodes.HashCodesLong;

/**
 * Representation of a time range.
 *
 * @since XXX
 */
public class Range
        implements Comparable<Range> {

    private final long start;
    private final long end;

    private Range(
            final long start,
            final long end
    ) {
        if (start > end)
            throw new IllegalArgumentException("start (" + start + ") must be inferior or equals to end (" + end + ")");
        this.start = start;
        this.end = end;
    }

    /**
     * @param start the starting time of the range
     * @param end   the ending time of the range
     *
     * @return the {@link fr.byowares.game.miq.core.Range} defined by the two given time
     *
     * @throws java.lang.IllegalArgumentException if and only if the ending time is strictly lower than the starting
     *                                            time
     */
    public static Range fromAbsoluteTimes(
            final long start,
            final long end
    ) {
        return new Range(start, end);
    }

    /**
     * @param start    the starting time of the range
     * @param duration the duration of the range
     *
     * @return the {@link fr.byowares.game.miq.core.Range} starting at {@code start} and lasting {@code duration}
     *
     * @throws java.lang.IllegalArgumentException if and only if the duration is negative
     */
    public static Range fromRelativeTimes(
            final long start,
            final long duration
    ) {
        return new Range(start, start + duration);
    }

    @Override
    public int compareTo(final Range o) {
        final int res = Long.compare(this.start, o.start);
        if (res != 0) return res;
        return Long.compare(this.end, o.end);
    }

    @Override
    public int hashCode() {
        return HashCodesLong.hash(this.start, this.end);
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Range && this.compareTo((Range) o) == 0;
    }

    @Override
    public String toString() {
        return "[" + this.start + ", " + this.end + "]";
    }
}
