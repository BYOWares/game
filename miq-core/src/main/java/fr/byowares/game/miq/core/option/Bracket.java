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
package fr.byowares.game.miq.core.option;

/**
 * Represents bracket pair.
 *
 * @since XXX
 */
public enum Bracket {
    /** Round brackets also named parenthesis. */
    ROUND('(', ')'),
    /** Square brackets. */
    SQUARE('[', ']'),
    /** Curly brackets also named braces. */
    CURLY('{', '}'),
    /** Angle brackets also named chevron. */
    ANGLE('<', '>');

    private final char open;
    private final char close;

    Bracket(
            final char open,
            final char close
    ) {
        this.open = open;
        this.close = close;
    }

    /**
     * @return The opening character of the bracket’s pair.
     */
    public char getOpen() {
        return this.open;
    }

    /**
     * @return The closing character of the bracket’s pair.
     */
    public char getClose() {
        return this.close;
    }

    /**
     * @return A detailed representation of this (not just its name).
     */
    public String detailedToString() {
        return "Bracket{" + "open=" + this.open + ", close=" + this.close + '}';
    }
}
