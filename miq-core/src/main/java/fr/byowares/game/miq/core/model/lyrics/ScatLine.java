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
 * A line made of non-lexical vocals (e.g.: La la la lala, la la la lala).
 *
 * @since XXX
 */
public class ScatLine
        extends SimpleLine {

    /**
     * @param elements The elements making the
     */
    ScatLine(final LineElement[] elements) {
        super(elements);
    }

    @Override
    public boolean isScat() {
        return true;
    }
}
