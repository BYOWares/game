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

import java.util.List;

/**
 * Offer the ability to convert a text into a Line.
 *
 * @since XXX
 */
@FunctionalInterface
public interface LineParser {

    /**
     * @param input The text to parse and convert into a Line.
     *
     * @return The list of lines parsed using the options.
     */
    List<Line> parse(CharSequence input);
}
