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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class BracketTest {

    @Test
    public void testAllCharAreUnique() {
        final Map<Character, List<Pair>> map = new TreeMap<>();
        final Bracket[] values = Bracket.values();
        for (final Bracket b : values) {
            map.computeIfAbsent(b.getOpen(), x -> new ArrayList<>()).add(new Pair(b, true));
            map.computeIfAbsent(b.getClose(), x -> new ArrayList<>()).add(new Pair(b, false));
        }
        map.forEach(
                (b, l) -> assertEquals(1, l.size(), "Character " + b + " is defined in more than one bracket: " + l));
    }


    private record Pair(
            Bracket bracket,
            boolean isOpen
    ) {}
}
