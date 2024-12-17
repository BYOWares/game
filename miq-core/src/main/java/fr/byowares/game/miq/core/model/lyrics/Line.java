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
 * A representation of words' sequence
 *
 * @since XXX
 */
public interface Line {

    /**
     * @return {@code true} if this line has specific singers, {@code false} otherwise.
     */
    boolean hasSingers();

    /**
     * @return The set of singers associated with this line.
     */
    Set<Singer> getSingers();

    /**
     * @return {@code true} if this line is sung by a chorus, {@code false} otherwise.
     */
    boolean isChorus();

    /**
     * @return {@code true} if this line is only non-lexical vocables, {@code false} otherwise.
     */
    boolean isScat();
}
