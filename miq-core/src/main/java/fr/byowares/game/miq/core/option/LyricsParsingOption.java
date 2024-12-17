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

import java.util.Objects;

/**
 * @param copyrightPattern The exact line content to match the start and end of the Copyright.
 * @param chorusBracket    Brackets used to identify automatically chorus in a line.
 * @param singerBracket    Brackets used to identify automatically singer in a line.
 * @param scatBracket      Brackets used to identify automatically non-lexical vocables in a line.
 *
 * @since XXX
 */
public record LyricsParsingOption(
        String copyrightPattern,
        Bracket chorusBracket,
        Bracket singerBracket,
        Bracket scatBracket
) {
    /** Default copyright pattern while parsing a lyrics file */
    public static final String DEFAULT_COPYRIGHT_PATTERN = "@Copyright@";

    /**
     * Initialize with all default options:
     * <ul>
     *     <li>{@code copyrightPattern} = {@link #DEFAULT_COPYRIGHT_PATTERN}.</li>
     *     <li>{@code chorusBracket} = {@link Bracket#ROUND PARENTHESIS}</li>
     *     <li>{@code singerBracket} = {@link Bracket#CURLY CURLY}</li>
     *     <li>{@code scatBracket} = {@link Bracket#SQUARE SQUARE}</li>
     * </ul>
     */
    public LyricsParsingOption() {
        this(DEFAULT_COPYRIGHT_PATTERN, Bracket.ROUND, Bracket.CURLY, Bracket.SQUARE);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code copyrightPattern} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOption removeCopyrightPattern() {
        return this.copyrightPattern(null);
    }

    /**
     * @param pattern The new pattern for the {@code copyrightPattern} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code copyrightPattern} which is set
     * to {@code pattern}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOption copyrightPattern(final String pattern) {
        if (Objects.equals(this.copyrightPattern, pattern)) return this;
        return new LyricsParsingOption(pattern, this.chorusBracket, this.singerBracket, this.scatBracket);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code chorusBracket} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOption removeChorusBracket() {
        return this.chorusBracket(null);
    }

    /**
     * @param bracket The new bracket parsing definition for the {@code chorusBracket} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code chorusBracket} which is set
     * to {@code bracket}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOption chorusBracket(final Bracket bracket) {
        if (this.chorusBracket == bracket) return this;
        return new LyricsParsingOption(this.copyrightPattern, bracket, this.singerBracket, this.scatBracket);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code singerBracket} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOption removeSingerBracket() {
        return this.singerBracket(null);
    }

    /**
     * @param bracket The new bracket parsing definition for the {@code singerBracket} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code singerBracket} which is set
     * to {@code bracket}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOption singerBracket(final Bracket bracket) {
        if (this.singerBracket == bracket) return this;
        return new LyricsParsingOption(this.copyrightPattern, this.chorusBracket, bracket, this.scatBracket);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code scatBracket} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOption removeScatBracket() {
        return this.scatBracket(null);
    }

    /**
     * @param bracket The new bracket parsing definition for the {@code scatBracket} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code scatBracket} which is set
     * to {@code bracket}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOption scatBracket(final Bracket bracket) {
        if (this.scatBracket == bracket) return this;
        return new LyricsParsingOption(this.copyrightPattern, this.chorusBracket, this.singerBracket, bracket);
    }

    /**
     * Represents bracket pair.
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
    }
}
