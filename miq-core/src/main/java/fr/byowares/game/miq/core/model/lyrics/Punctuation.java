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

import fr.byowares.game.miq.core.option.LyricsDisplayOptions;

/**
 * @param chars The punctuation characters (expect everything but alphanumerical characters).
 *
 * @since XXX
 */
public record Punctuation(CharSequence chars)
        implements LineElement {

    @Override
    public int length(final LyricsDisplayOptions options) {
        return options.whileGuessingShowPunctuation() ? this.chars.length() : 1;
    }

    @Override
    public boolean isWord() {
        return false;
    }

    @Override
    public boolean isWhiteSpaceOnly() {
        return this.chars.chars().allMatch(Character::isWhitespace);
    }

    @Override
    public CharSequence getText() {
        return this.chars;
    }

    @Override
    public LineElement trimHead() {
        int i = 0;
        for (; i < this.chars.length(); i++) {
            if (!Character.isWhitespace(this.chars.charAt(i))) break;
        }
        if (i == 0) return this;
        return new Punctuation(this.chars.subSequence(i, this.chars.length()));
    }

    @Override
    public LineElement trimTail() {
        int i = this.chars.length() - 1;
        for (; i >= 0; i--) {
            if (!Character.isWhitespace(this.chars.charAt(i))) break;
        }
        if (i == this.chars.length() - 1) return this;
        return new Punctuation(this.chars.subSequence(0, i + 1));
    }
}
