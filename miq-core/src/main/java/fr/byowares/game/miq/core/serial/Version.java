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
package fr.byowares.game.miq.core.serial;

/**
 * @param version The numerical version.
 *
 * @since XXX
 */
public record Version(int version)
        implements Comparable<Version> {

    /**
     * Convert a {@link java.lang.CharSequence} into a version
     *
     * @param text The {@code CharSequence} to convert.
     *
     * @return The converted Version.
     *
     * @throws java.lang.NumberFormatException If the version cannot be parsed as an int.
     */
    public static Version from(final CharSequence text) {
        return new Version(Integer.parseInt(text, 0, text.length(), 10));
    }

    @Override
    public String toString() {
        return "Version{" + this.version + '}';
    }

    @Override
    public int compareTo(final Version o) {
        return Integer.compare(this.version, o.version);
    }
}
