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

/**
 * A line that is associated with one or several singers
 *
 * @since XXX
 */
public class ComposedLine {

    private final SingerLine[] underlyingLines;

    private ComposedLine(
            final SingerLine[] lines
    ) {
        if (lines == null || lines.length == 0) throw new IllegalArgumentException(); // TODO
        this.underlyingLines = lines;
    }

    private SingerLine[] getUnderlyingLines() {
        return this.underlyingLines;
    }
}
