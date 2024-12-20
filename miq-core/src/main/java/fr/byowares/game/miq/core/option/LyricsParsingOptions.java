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
 * @param singerBracket    Brackets used to identify a singer in a line.
 * @param choirBracket     Brackets used to identify a backup vocals part in a line.
 * @param scatBracket      Brackets used to identify non-lexical vocables in a line.
 *
 * @since XXX
 */
public record LyricsParsingOptions(
        String copyrightPattern,
        Bracket singerBracket,
        Bracket choirBracket,
        Bracket scatBracket
) {
    /** Default copyright pattern while parsing a lyrics file */
    public static final String DEFAULT_COPYRIGHT_PATTERN = "@Copyright@";

    /**
     * Initialize with all default options:
     * <ul>
     *     <li>{@code copyrightPattern} = {@link #DEFAULT_COPYRIGHT_PATTERN}.</li>
     *     <li>{@code choirBracket} = {@link Bracket#ROUND PARENTHESIS}</li>
     *     <li>{@code singerBracket} = {@link Bracket#SQUARE SQUARE}</li>
     *     <li>{@code scatBracket} = {@link Bracket#CURLY CURLY}</li>
     * </ul>
     */
    public LyricsParsingOptions() {
        this(DEFAULT_COPYRIGHT_PATTERN, Bracket.SQUARE, Bracket.ROUND, Bracket.CURLY);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code copyrightPattern} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOptions removeCopyrightPattern() {
        return this.copyrightPattern(null);
    }

    /**
     * @param pattern The new pattern for the {@code copyrightPattern} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code copyrightPattern} which is set
     * to {@code pattern}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOptions copyrightPattern(final String pattern) {
        if (Objects.equals(this.copyrightPattern, pattern)) return this;
        return new LyricsParsingOptions(pattern, this.singerBracket, this.choirBracket, this.scatBracket);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code choirBracket} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOptions removeChoirBracket() {
        return this.choirBracket(null);
    }

    /**
     * @param bracket The new bracket parsing definition for the {@code choirBracket} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code choirBracket} which is set
     * to {@code bracket}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOptions choirBracket(final Bracket bracket) {
        if (this.choirBracket == bracket) return this;
        return new LyricsParsingOptions(this.copyrightPattern, this.singerBracket, bracket, this.scatBracket);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code singerBracket} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOptions removeSingerBracket() {
        return this.singerBracket(null);
    }

    /**
     * @param bracket The new bracket parsing definition for the {@code singerBracket} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code singerBracket} which is set
     * to {@code bracket}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOptions singerBracket(final Bracket bracket) {
        if (this.singerBracket == bracket) return this;
        return new LyricsParsingOptions(this.copyrightPattern, bracket, this.choirBracket, this.scatBracket);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code scatBracket} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOptions removeScatBracket() {
        return this.scatBracket(null);
    }

    /**
     * @param bracket The new bracket parsing definition for the {@code scatBracket} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code scatBracket} which is set
     * to {@code bracket}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOptions scatBracket(final Bracket bracket) {
        if (this.scatBracket == bracket) return this;
        return new LyricsParsingOptions(this.copyrightPattern, this.singerBracket, this.choirBracket, bracket);
    }

    /**
     * @return {@code false} if the same non-null bracket is defined to identify two part (among singer, choir and
     * scat), {@code true} otherwise.
     */
    public boolean isValid() {
        final boolean areSingerAndChoirDifferent = this.singerBracket == null || this.singerBracket != this.choirBracket;
        final boolean areSingerAndScatDifferent = this.singerBracket == null || this.singerBracket != this.scatBracket;
        final boolean areChoirAndScatDifferent = this.choirBracket == null || this.choirBracket != this.scatBracket;
        return areSingerAndChoirDifferent && areSingerAndScatDifferent && areChoirAndScatDifferent;
    }
}
