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

import org.junit.jupiter.api.Test;

import java.text.CharacterIterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CSVCharSequenceTest {

    public static final long _123 = 123L;

    @Test
    public void testInvalidConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new CSVCharSequence('\\', ""));
    }

    @Test
    public void testInvalidEscapingConstructor() {
        final var e = assertThrows(IllegalArgumentException.class, () -> new CSVCharSequence('#', "1\\2"));
        assertEquals("Backslash shall precede only backslash or separator (#), not: '2'", e.getMessage());
    }

    @Test
    public void testInvalidEscapingNextField() {
        final var csv = assertDoesNotThrow(() -> new CSVCharSequence('#', "#1\\2"));
        final var e = assertThrows(IllegalArgumentException.class, csv::hasNext);
        assertEquals("Backslash shall precede only backslash or separator (#), not: '2'", e.getMessage());
    }

    @Test
    public void testInvalidEscapingAtTheEnd() {
        final var csv = assertDoesNotThrow(() -> new CSVCharSequence('#', "#1\\"));
        final var e = assertThrows(IllegalArgumentException.class, csv::hasNext);
        assertEquals("Last character on line was a single \\, which is forbidden", e.getMessage());
    }

    @Test
    public void testEmptyString() {
        final CSVCharSequence csv = new CSVCharSequence('#', "");
        assertEmptyField(csv);
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    private static void assertEmptyField(final CSVCharSequence csv) {
        assertThrows(NumberFormatException.class, csv::asLong);
        final CharacterIterator it = csv.asCharacterIterator();
        assertEquals(CharacterIterator.DONE, it.current());
        assertEquals(CharacterIterator.DONE, it.next());
        assertEquals(CharacterIterator.DONE, it.previous());
    }

    @Test
    public void testPositiveLongOnly() {
        final CSVCharSequence csv = new CSVCharSequence('#', "123");
        assertLong(csv, _123);
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    private static void assertLong(
            final CSVCharSequence csv,
            final long value
    ) {
        assertEquals(value, csv.asLong());
        final CharacterIterator it = csv.asCharacterIterator();
        final String valueStr = Long.toString(value);
        assertEquals(valueStr.charAt(0), it.current());
        for (int i = 1; i < valueStr.length(); i++)
            assertEquals(valueStr.charAt(i), it.next());
    }

    @Test
    public void testNegativeLongOnly() {
        final CSVCharSequence csv = new CSVCharSequence('#', "-123");
        assertLong(csv, -_123);
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    @Test
    public void testTextNoBackSlash() {
        final String text = "Don't ignore me";
        final CSVCharSequence csv = new CSVCharSequence('#', text);
        assertText(csv, text);
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    private static void assertText(
            final CSVCharSequence csv,
            final String expectedValue
    ) {
        assertThrows(NumberFormatException.class, csv::asLong);
        final CharacterIterator it = csv.asCharacterIterator();
        assertEquals(expectedValue.charAt(0), it.current());
        for (int i = 1; i < expectedValue.length(); i++)
            assertEquals(expectedValue.charAt(i), it.next());
    }

    @Test
    public void testTextWithBackSlash() {
        final CSVCharSequence csv = new CSVCharSequence('#', "\\\\Don't\\\\ ignore me");
        assertText(csv, "\\Don't\\ ignore me");
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    @Test
    public void testComplexLine() {
        final CSVCharSequence csv = new CSVCharSequence('#', "#-123#123##char '\\#' is used as\\\\ a separator#");
        assertTrue(csv.hasNext());
        assertEmptyField(csv);

        assertDoesNotThrow(csv::nextField);
        assertTrue(csv.hasNext());
        assertLong(csv, -_123);

        assertDoesNotThrow(csv::nextField);
        assertTrue(csv.hasNext());
        assertLong(csv, _123);

        assertDoesNotThrow(csv::nextField);
        assertTrue(csv.hasNext());
        assertEmptyField(csv);

        assertDoesNotThrow(csv::nextField);
        assertTrue(csv.hasNext());
        System.out.println("csv = " + csv);
        assertText(csv, "char '#' is used as\\ a separator");

        assertDoesNotThrow(csv::nextField);
        assertFalse(csv.hasNext());
        assertEmptyField(csv);
    }
}
