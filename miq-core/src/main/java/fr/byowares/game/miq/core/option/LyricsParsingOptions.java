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
 * @param singerBracket     Brackets used to identify a singer in a line.
 * @param backVocalsBracket Brackets used to identify a backup vocals part in a line.
 * @param nonLexicalBracket Brackets used to identify non-lexical vocables in a line.
 *
 * @since XXX
 */
public record LyricsParsingOptions(
        Bracket singerBracket,
        Bracket backVocalsBracket,
        Bracket nonLexicalBracket
) {
    /**
     * Initialize with all default options:
     * <ul>
     *     <li>{@code singerBracket} = {@link Bracket#SQUARE SQUARE}</li>
     *     <li>{@code backVocalsBracket} = {@link Bracket#ROUND PARENTHESIS}</li>
     *     <li>{@code nonLexicalBracket} = {@link Bracket#CURLY CURLY}</li>
     * </ul>
     */
    public LyricsParsingOptions() {
        this(Bracket.SQUARE, Bracket.ROUND, Bracket.CURLY);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code backVocalsBracket} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOptions removeBackVocals() {
        return this.backVocals(null);
    }

    /**
     * @param bracket The new bracket parsing definition for the {@code backVocalsBracket} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code backVocalsBracket} which is set
     * to {@code bracket}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOptions backVocals(final Bracket bracket) {
        if (this.backVocalsBracket == bracket) return this;
        return new LyricsParsingOptions(this.singerBracket, bracket, this.nonLexicalBracket);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code singerBracket} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOptions removeSinger() {
        return this.singer(null);
    }

    /**
     * @param bracket The new bracket parsing definition for the {@code singerBracket} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code singerBracket} which is set
     * to {@code bracket}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOptions singer(final Bracket bracket) {
        if (this.singerBracket == bracket) return this;
        return new LyricsParsingOptions(bracket, this.backVocalsBracket, this.nonLexicalBracket);
    }

    /**
     * @return A new option whose fields are copied from {@code this}, except {@code nonLexicalBracket} which is set
     * to {@code null}. If the value is already equal to {@code null}, {@code this} is returned.
     */
    public LyricsParsingOptions removeNonLexical() {
        return this.nonLexical(null);
    }

    /**
     * @param bracket The new bracket parsing definition for the {@code nonLexicalBracket} field.
     *
     * @return A new option whose fields are copied from {@code this}, except {@code nonLexicalBracket} which is set
     * to {@code bracket}. If the value to set is equal to the already set value, {@code this} is returned.
     */
    public LyricsParsingOptions nonLexical(final Bracket bracket) {
        if (this.nonLexicalBracket == bracket) return this;
        return new LyricsParsingOptions(this.singerBracket, this.backVocalsBracket, bracket);
    }

    /**
     * @return {@code false} if the same non-null bracket is defined to identify two part (among singer,
     * backup vocals, non-lexical vocables), {@code true} otherwise.
     */
    public boolean isValid() {
        final boolean singerBackVocalsDiff = this.singerBracket == null || this.singerBracket != this.backVocalsBracket;
        final boolean singerNonLexicalDiff = this.singerBracket == null || this.singerBracket != this.nonLexicalBracket;
        final boolean backVocalsNonLexicalDiff = this.backVocalsBracket == null || this.backVocalsBracket != this.nonLexicalBracket;
        return singerBackVocalsDiff && singerNonLexicalDiff && backVocalsNonLexicalDiff;
    }
}
