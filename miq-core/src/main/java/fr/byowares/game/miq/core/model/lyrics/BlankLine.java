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
import java.util.Set;

/**
 * Representation of a blank line.
 *
 * @since XXX
 */
public class BlankLine
        implements Line {

    /** Singleton */
    public static final BlankLine INSTANCE = new BlankLine();
    public static final List<Line> ONE_BLANK_LINE = List.of(INSTANCE);

    private BlankLine() {
        // Singleton pattern
    }

    @Override
    public Set<Singer> getSingers() {
        return Set.of();
    }

    @Override
    public boolean isBackVocals() {
        return false;
    }

    @Override
    public boolean isNonLexicalVocables() {
        return false;
    }
}
