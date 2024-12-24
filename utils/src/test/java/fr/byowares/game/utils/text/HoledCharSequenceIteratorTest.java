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
package fr.byowares.game.utils.text;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HoledCharSequenceIteratorTest {

    private static final String TEST_STRING = "01234567";

    private static Stream<Arguments> getHoles0to255Offset0() {
        return getHoles0to255WithOffset(0);
    }

    private static Stream<Arguments> getHoles0to255WithOffset(final int offset) {
        final int nbBits = 8;
        final int size = 1 << nbBits;
        final List<Arguments> res = new ArrayList<>(size);
        for (int value = 0; value < size; value++) {
            final var present = new ArrayList<>(nbBits);
            final var holes = new BitSet(nbBits);
            int mutableValue = value;
            for (int bit = 0; bit < nbBits; bit++) {
                if ((mutableValue & 0x01) == 1) present.add(bit + offset);
                else holes.set(bit + offset);
                mutableValue = mutableValue >> 1;
            }
            res.add(Arguments.of(present, holes));
        }
        return res.stream();
    }

    private static Stream<Arguments> getHoles0to255Offset8() {
        return getHoles0to255WithOffset(8);
    }

    @ParameterizedTest(name = "[{index}] present={0}, holes={1}")
    @MethodSource("getHoles0to255Offset0")
    public void testBasicOperationsFullString(
            final List<Integer> presentValues,
            final BitSet holes
    ) {
        final HoledCharSequenceIterator it = new HoledCharSequenceIterator(TEST_STRING, holes);
        testBasicOperationsRestrictedString(it, TEST_STRING, presentValues, 0, TEST_STRING.length());
    }

    private static void testBasicOperationsRestrictedString(
            final HoledCharSequenceIterator it,
            final String testString,
            final List<Integer> presentValues,
            final int begin,
            final int end
    ) {
        if (presentValues.isEmpty()) {
            assertEquals(CharacterIterator.DONE, it.current());
            assertEquals(end, it.getIndex());
            assertEquals(CharacterIterator.DONE, it.previous());
            assertEquals(begin - 1, it.getIndex());
            assertEquals(CharacterIterator.DONE, it.previous());
            assertEquals(begin - 1, it.getIndex());
            assertEquals(CharacterIterator.DONE, it.next());
            assertEquals(end, it.getIndex());
            assertEquals(CharacterIterator.DONE, it.next());
            assertEquals(end, it.getIndex());

        } else {
            assertEquals(testString.charAt(presentValues.getFirst()), it.current());
            assertEquals(presentValues.getFirst(), it.getIndex());
            for (int i = 1; i < presentValues.size(); i++) {
                assertEquals(testString.charAt(presentValues.get(i)), it.next());
                assertEquals(presentValues.get(i), it.getIndex());
            }
            assertEquals(CharacterIterator.DONE, it.next());
            assertEquals(end, it.getIndex());

            assertEquals(testString.charAt(presentValues.getFirst()), it.first());
            assertEquals(presentValues.getFirst(), it.getIndex());
            assertEquals(testString.charAt(presentValues.getFirst()), it.first());
            assertEquals(presentValues.getFirst(), it.getIndex());

            assertEquals(testString.charAt(presentValues.getLast()), it.last());
            assertEquals(presentValues.getLast(), it.getIndex());
            assertEquals(testString.charAt(presentValues.getLast()), it.last());
            assertEquals(presentValues.getLast(), it.getIndex());

            for (int i = presentValues.size() - 2; i >= 0; i--) {
                assertEquals(testString.charAt(presentValues.get(i)), it.previous());
                assertEquals(presentValues.get(i), it.getIndex());
            }
            assertEquals(CharacterIterator.DONE, it.previous());
            assertEquals(begin - 1, it.getIndex());

            assertThrows(UnsupportedOperationException.class, () -> it.setIndex(begin));
        }
    }

    @ParameterizedTest(name = "[{index}] present={0}, holes={1}")
    @MethodSource("getHoles0to255Offset8")
    public void testBasicOperationsRestrictedString(
            final List<Integer> presentValues,
            final BitSet holes
    ) {
        final String testString = "IgnoreMe" + TEST_STRING + "Please";
        final int offset = 8;
        final int end = offset + TEST_STRING.length();
        assertEquals(offset, testString.indexOf(TEST_STRING));

        final HoledCharSequenceIterator it = new HoledCharSequenceIterator(testString, offset, end, holes);
        testBasicOperationsRestrictedString(it, testString, presentValues, offset, end);
    }
}
