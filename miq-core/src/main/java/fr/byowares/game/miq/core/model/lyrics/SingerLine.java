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

import java.util.Set;

/**
 * A line that is associated with one or several singers
 *
 * @since XXX
 */
public class SingerLine
        implements Line {

    private final Set<Singer> singers;
    private final SimpleLine underlyingLine;

    private SingerLine(
            final Set<Singer> singers,
            final SimpleLine underlyingLine
    ) {
        this.singers = singers == null ? Set.of() : singers;
        this.underlyingLine = underlyingLine;
    }

    private SimpleLine getUnderlyingLine() {
        return this.underlyingLine;
    }

    @Override
    public boolean hasSingers() {
        return !this.singers.isEmpty();
    }

    @Override
    public Set<Singer> getSingers() {
        return this.singers;
    }

    @Override
    public boolean isChorus() {
        return this.underlyingLine.isChorus();
    }

    @Override
    public boolean isScat() {
        return this.underlyingLine.isScat();
    }
}
