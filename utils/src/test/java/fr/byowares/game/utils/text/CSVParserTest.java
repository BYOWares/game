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

class CSVParserTest {

    public static final int _123 = 123;
    public static final long _123L = 123L;

    @Test
    public void testInvalidConstructorBackSlash() {
        final var e = assertThrows(IllegalArgumentException.class, () -> new CSVParser('\\', ""));
        assertEquals("Separator cannot be '\\'", e.getMessage());
    }

    @Test
    public void testInvalidConstructorSpace() {
        final var e = assertThrows(IllegalArgumentException.class, () -> new CSVParser(' ', ""));
        assertEquals("Separator cannot be ' '", e.getMessage());
    }

    @Test
    public void testUninitialized() {
        final var csv = new CSVParser('#', "123");
        testUninitializedAux(csv);
        csv.setText("123");
        testUninitializedAux(csv);
    }

    private static void testUninitializedAux(final CSVParser csv) {
        assertDoesNotThrow(csv::toString);
        assertThrows(IllegalStateException.class, csv::asInt);
        assertThrows(IllegalStateException.class, csv::asLong);
        assertThrows(IllegalStateException.class, csv::asCharacterIterator);
        csv.nextField();
        assertEquals(_123, csv.asInt());
        assertEquals(_123L, csv.asLong());
        assertDoesNotThrow(csv::toString);
    }


    @Test
    public void testInvalidEscapingString() {
        final var csv = assertDoesNotThrow(() -> new CSVParser('#', "1\\2"));
        final var e = assertThrows(IllegalArgumentException.class, csv::nextField);
        assertEquals("Backslash shall precede only backslash or separator (#), not: '2'", e.getMessage());
    }

    @Test
    public void testInvalidEscapingNextField() {
        final var csv = assertDoesNotThrow(() -> new CSVParser('#', "#1\\2"));
        assertDoesNotThrow(csv::nextField);
        final var e = assertThrows(IllegalArgumentException.class, csv::hasNext);
        assertEquals("Backslash shall precede only backslash or separator (#), not: '2'", e.getMessage());
    }

    @Test
    public void testInvalidEscapingAtTheEnd() {
        final var csv = assertDoesNotThrow(() -> new CSVParser('#', "1\\"));
        final var e = assertThrows(IllegalArgumentException.class, csv::hasNext);
        assertEquals("Last character on line was a single \\, which is forbidden", e.getMessage());
    }

    @Test
    public void testEmptyString() {
        final CSVParser csv = new CSVParser('#', "");
        assertDoesNotThrow(csv::nextField);
        assertEmptyField(csv);
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    private static void assertEmptyField(final CSVParser csv) {
        assertThrows(NumberFormatException.class, csv::asLong);
        assertThrows(NumberFormatException.class, csv::asInt);
        final CharacterIterator it = csv.asCharacterIterator();
        assertEquals(CharacterIterator.DONE, it.current());
        assertEquals(CharacterIterator.DONE, it.next());
        assertEquals(CharacterIterator.DONE, it.previous());
    }

    @Test
    public void testPositiveLongOnly() {
        final CSVParser csv = new CSVParser('#', "123");
        assertDoesNotThrow(csv::nextField);
        assertLong(csv, _123L);
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    private static void assertLong(
            final CSVParser csv,
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
        final CSVParser csv = new CSVParser('#', "-123");
        assertDoesNotThrow(csv::nextField);
        assertLong(csv, -_123L);
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    @Test
    public void testPositiveIntOnly() {
        final CSVParser csv = new CSVParser('#', "123");
        assertDoesNotThrow(csv::nextField);
        assertInt(csv, _123);
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    private static void assertInt(
            final CSVParser csv,
            final int value
    ) {
        assertEquals(value, csv.asInt());
        final CharacterIterator it = csv.asCharacterIterator();
        final String valueStr = Integer.toString(value);
        assertEquals(valueStr.charAt(0), it.current());
        for (int i = 1; i < valueStr.length(); i++)
            assertEquals(valueStr.charAt(i), it.next());
    }

    @Test
    public void testNegativeIntOnly() {
        final CSVParser csv = new CSVParser('#', "-123");
        assertDoesNotThrow(csv::nextField);
        assertInt(csv, -_123);
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    @Test
    public void testTextNoBackSlash() {
        final String text = "Don't ignore me";
        final CSVParser csv = new CSVParser('#', text);
        assertDoesNotThrow(csv::nextField);
        assertText(csv, text);
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    private static void assertText(
            final CSVParser csv,
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
        final CSVParser csv = new CSVParser('#', "\\\\Don't\\\\ ignore me");
        assertDoesNotThrow(csv::nextField);
        assertText(csv, "\\Don't\\ ignore me");
        assertFalse(csv.hasNext());
        assertThrows(NoSuchElementException.class, csv::nextField);
    }

    @Test
    public void testComplexLine() {
        final CSVParser csv = new CSVParser('#', "#-123#123##char '\\#' is used as\\\\ a separator#");
        assertTrue(csv.hasNext());
        assertDoesNotThrow(csv::nextField);
        assertTrue(csv.hasNext());
        assertEmptyField(csv);

        assertDoesNotThrow(csv::nextField);
        assertTrue(csv.hasNext());
        assertLong(csv, -_123L);

        assertDoesNotThrow(csv::nextField);
        assertTrue(csv.hasNext());
        assertLong(csv, _123L);

        assertDoesNotThrow(csv::nextField);
        assertTrue(csv.hasNext());
        assertEmptyField(csv);

        assertDoesNotThrow(csv::nextField);
        assertTrue(csv.hasNext());
        assertText(csv, "char '#' is used as\\ a separator");

        assertDoesNotThrow(csv::nextField);
        assertFalse(csv.hasNext());
        assertEmptyField(csv);
    }

    @Test
    public void testNextAs() {
        final String text = "Text Message";
        final var csv = new CSVParser('#', "1#2#" + text);
        assertTrue(csv.hasNext());
        assertEquals(1, csv.nextFieldAsInt());
        assertLong(csv, 1L);

        assertTrue(csv.hasNext());
        assertEquals(2L, csv.nextFieldAsLong());
        assertInt(csv, 2);

        assertTrue(csv.hasNext());
        final var it = csv.nextFieldAsCharacterIterator();
        assertEquals(text.charAt(0), it.current());
        for (int i = 1; i < text.length(); i++)
            assertEquals(text.charAt(i), it.next());
    }
}
