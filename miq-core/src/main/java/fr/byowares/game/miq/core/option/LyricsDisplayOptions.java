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
 * All options regarding lyrics displaying option.
 *
 * @param whileGuessingShowPunctuation Whether punctuation shall be revealed even when guessing lyrics.
 * @param whileGuessingShowTrueLength  Whether length of words shall be revealed while guessing.
 * @param whileGuessingWordLength      The count of char to use to hide word to guess.
 *
 * @since XXX
 */
public record LyricsDisplayOptions(
        boolean whileGuessingShowPunctuation,
        boolean whileGuessingShowTrueLength,
        int whileGuessingWordLength
) {}
