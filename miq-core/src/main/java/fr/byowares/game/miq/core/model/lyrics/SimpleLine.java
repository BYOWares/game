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
 * A basic line that can have singers, and is not a back vocals or a non-lexical vocables one.
 *
 * @param elements     The LineElements composing the Line.
 * @param singers      The set of singers in charge of singing this Line.
 * @param isBackVocals Whether they are sung by back vocalists.
 * @param isNonLexical Whether it's made of non-lexical vocables (e.g.: La la la lala, la la la lala).
 *
 * @since XXX
 */
public record SimpleLine(
        List<LineElement> elements,
        Set<Singer> singers,
        boolean isBackVocals,
        boolean isNonLexical
)
        implements Line {


    @Override
    public Set<Singer> getSingers() {
        return this.singers;
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
