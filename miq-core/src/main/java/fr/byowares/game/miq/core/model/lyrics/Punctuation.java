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
 * @param punctuations The punctuation characters (expect everything but alphanumerical characters).
 *
 * @since XXX
 */
public record Punctuation(CharSequence punctuations)
        implements LineElement {


    @Override
    public int length(final LyricsDisplayOptions options) {
        return options.whileGuessingShowPunctuation() ? this.punctuations.length() : 1;
    }

    @Override
    public boolean isWord() {
        return false;
    }

    @Override
    public boolean isWhiteSpaceOnly() {
        return this.punctuations.chars().allMatch(Character::isWhitespace);
    }

    @Override
    public CharSequence getText() {
        return this.punctuations;
    }
}
