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
            for (final Bracket backVocals : allBrackets) {
                final boolean sbValid = singer == null || singer != backVocals;
                for (final Bracket nonLexical : allBrackets) {
                    final boolean bnValid = backVocals == null || backVocals != nonLexical;
                    final boolean snValid = singer == null || singer != nonLexical;
                    res.add(Arguments.of(singer, backVocals, nonLexical, sbValid && bnValid && snValid));
                }
            }
        }
        return res.stream();
    }

    @ParameterizedTest(name = "[{index}] Singer={0}, BackVocals={1}, NonLexical={2}, IsValid={3}")
    @MethodSource("provideBracketOptions")
    void testIsValidMethod(
            final Bracket singer,
            final Bracket backVocals,
            final Bracket nonLexical,
            final boolean isValid
    ) {
        final var options = new LyricsParsingOptions().singer(singer).backVocals(backVocals).nonLexical(nonLexical);
        assertEquals(isValid, options.isValid());
    }
}
