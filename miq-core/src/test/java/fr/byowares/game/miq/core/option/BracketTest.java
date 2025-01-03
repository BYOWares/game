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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BracketTest {

    private static final Map<Character, List<Pair>> MAP = new TreeMap<>();

    static {
        final Bracket[] values = Bracket.values();
        for (final Bracket b : values) {
            final Pair open = new Pair(b, true);
            MAP.computeIfAbsent(b.getOpen(), x -> new ArrayList<>()).add(open);

            final Pair close = new Pair(b, false);
            MAP.computeIfAbsent(b.getClose(), x -> new ArrayList<>()).add(close);
        }
    }

    private static Stream<Character> getAllChars() {
        return MAP.keySet().stream();
    }

    @ParameterizedTest(name = "[{index}] Character={0}")
    @MethodSource("getAllChars")
    public void testCharacterIsUnique(final Character c) {
        final List<Pair> l = MAP.get(c);
        assertEquals(1, l.size(), "Character " + c + " is defined in more than one bracket: " + l);
    }

    private record Pair(
            Bracket bracket,
            boolean isOpen
    ) {}
}
