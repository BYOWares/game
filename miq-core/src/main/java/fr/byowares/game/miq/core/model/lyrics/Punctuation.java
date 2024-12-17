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
 * @param punctuationChar The actual punctuation character.
 * @param spaceBefore     Whether a space must precede the character.
 * @param spaceAfter      Whether a space must follow the character.
 *
 * @since XXX
 */
public record Punctuation(char punctuationChar, boolean spaceBefore, boolean spaceAfter)
        implements LineElement {

    public static final Punctuation COMMA = new Punctuation(',', false, true);

    @Override
    public int length(final LyricsDisplayOptions options) {
        return 1 + (this.spaceBefore ? 1 : 0) + (this.spaceAfter ? 1 : 0);
    }

    @Override
    public boolean isWord() {
        return false;
    }
}
