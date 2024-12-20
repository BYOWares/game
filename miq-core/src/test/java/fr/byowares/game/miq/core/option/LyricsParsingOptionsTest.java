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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LyricsParsingOptionsTest {

    public static Stream<Arguments> provideBracketOptions() {
        final List<Arguments> res = new ArrayList<>();
        final List<Bracket> allBrackets = new ArrayList<>(Arrays.asList(Bracket.values()));
        allBrackets.add(null);
        for (final Bracket singer : allBrackets) {
            for (final Bracket choir : allBrackets) {
                final boolean scValid = singer == null || singer != choir;
                for (final Bracket scat : allBrackets) {
                    final boolean csValid = choir == null || choir != scat;
                    final boolean ssValid = singer == null || singer != scat;
                    res.add(Arguments.of(singer, choir, scat, scValid && csValid && ssValid));
                }
            }
        }
        return res.stream();
    }

    @ParameterizedTest(name = "[{index}] Singer={0}, Choir={1}, Scat={2}, IsValid={3}")
    @MethodSource("provideBracketOptions")
    void testIsValidMethod(
            final Bracket singer,
            final Bracket choir,
            final Bracket scat,
            final boolean isValid
    ) {
        final var options = new LyricsParsingOptions().singerBracket(singer).choirBracket(choir).scatBracket(scat);
        assertEquals(isValid, options.isValid());
    }
}
