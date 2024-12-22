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
 * An element of a line (can be a word, or a punctuation).
 *
 * @since XXX
 */
public interface LineElement {

    /**
     * @param options The set of options used to display the lyrics.
     *
     * @return The count of char needed to display this element when hidden.
     */
    int length(LyricsDisplayOptions options);

    /**
     * @return {@code true} if the element is a word (and therefore can be guessed), {@code false} otherwise.
     */
    boolean isWord();

    /**
     * @return {@code true} if the element is made only of white spaces, {@code false} otherwise.
     */
    boolean isWhiteSpaceOnly();

    /**
     * @return The text contained in this element.
     */
    CharSequence getText();

    /**
     * @return A copy of this object without any heading white spaces, or {@code this} if none.
     *
     * @see java.lang.Character#isWhitespace(char)
     */
    LineElement trimHead();

    /**
     * @return A copy of this object without any trailing white spaces, or {@code this} if none.
     *
     * @see java.lang.Character#isWhitespace(char)
     */
    LineElement trimTail();
}
