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
package fr.byowares.game.miq.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RangeTest {
    private static final long TEN = 10L;
    private static final long ELEVEN = 11L;

    @Test
    void testValidAbsoluteRangeDifferent() {
        final Range range = Range.fromAbsoluteValues(TEN, ELEVEN);
        assertNotNull(range);
    }

    @Test
    void testValidAbsoluteRangeSame() {
        final Range range = Range.fromAbsoluteValues(TEN, TEN);
        assertNotNull(range);
    }

    @Test
    void testInvalidAbsoluteRange() {
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                                                        () -> Range.fromAbsoluteValues(ELEVEN, TEN));
        assertEquals("start (11) must be inferior or equals to end (10)", e.getMessage());
    }

    @Test
    void testValidRelativeRangeDifferent() {
        final Range range = Range.fromRelativeValues(ELEVEN, 1L);
        assertNotNull(range);
    }

    @Test
    void testValidRelativeRangeSame() {
        final Range range = Range.fromRelativeValues(ELEVEN, 0L);
        assertNotNull(range);
    }

    @Test
    void testInvalidRelativeRange() {
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                                                        () -> Range.fromRelativeValues(ELEVEN, -1L));
        assertEquals("start (11) must be inferior or equals to end (10)", e.getMessage());
    }

    @Test
    void testEquality() {
        final Range rel = Range.fromRelativeValues(TEN, 1L);
        final Range abs = Range.fromAbsoluteValues(TEN, ELEVEN);

        assertNotSame(rel, abs);
        assertEquals(rel, abs);
        assertEquals(0, rel.compareTo(abs));
        assertEquals(0, abs.compareTo(rel));
        assertEquals(rel.hashCode(), abs.hashCode());
    }

    @Test
    void testInequality() {
        final Range tenEleven = Range.fromRelativeValues(TEN, 1L);
        final Range tenTwelve = Range.fromRelativeValues(TEN, 2L);
        final Range elevenTwelve = Range.fromRelativeValues(ELEVEN, 1L);

        assertNotEquals(tenEleven, tenTwelve);
        assertNotEquals(tenEleven, elevenTwelve);
        assertNotEquals(tenTwelve, elevenTwelve);

        assertTrue(tenEleven.compareTo(tenTwelve) < 0);
        assertTrue(tenEleven.compareTo(elevenTwelve) < 0);
        assertTrue(tenTwelve.compareTo(elevenTwelve) < 0);

        assertTrue(tenTwelve.compareTo(tenEleven) > 0);
        assertTrue(elevenTwelve.compareTo(tenEleven) > 0);
        assertTrue(elevenTwelve.compareTo(tenTwelve) > 0);
    }
}
