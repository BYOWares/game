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

import static org.junit.jupiter.api.Assertions.*;

class CharSequenceIteratorTest {

    @Test
    public void testPositionTooBig() {
        final var e1 = assertThrows(IllegalArgumentException.class, () -> new CharSequenceIterator("123456", 2, 3, 4));
        assertEquals("Invalid position (begin[2]<=pos[4]<=end[3])", e1.getMessage());
    }

    @Test
    public void testPositionTooSmall() {
        final var e1 = assertThrows(IllegalArgumentException.class, () -> new CharSequenceIterator("123456", 2, 3, 1));
        assertEquals("Invalid position (begin[2]<=pos[1]<=end[3])", e1.getMessage());
    }

    @Test
    public void testBeginNegative() {
        final CharSequenceIterator valid = new CharSequenceIterator("123456789");
        final var e1 = assertThrows(IllegalArgumentException.class, () -> new CharSequenceIterator("123456", -1, 4, 3));
        final var e2 = assertThrows(IllegalArgumentException.class, () -> valid.setText("123456", -1, 4));
        assertEquals(e1.getMessage(), e2.getMessage());
        assertEquals("Invalid range (0<=begin[-1]<=end[4]<=length[6])", e2.getMessage());
    }

    @Test
    public void testEndTooBig() {
        final CharSequenceIterator valid = new CharSequenceIterator("123456789");
        final var e1 = assertThrows(IllegalArgumentException.class, () -> new CharSequenceIterator("123456", 2, 7, 3));
        final var e2 = assertThrows(IllegalArgumentException.class, () -> valid.setText("123456", 2, 7));
        assertEquals(e1.getMessage(), e2.getMessage());
        assertEquals("Invalid range (0<=begin[2]<=end[7]<=length[6])", e2.getMessage());
    }

    @Test
    public void testSetPositionTooSmall() {
        final CharSequenceIterator valid = new CharSequenceIterator("123456789", 2, 4, 3);
        final var e1 = assertThrows(IllegalArgumentException.class, () -> valid.setIndex(1));
        assertEquals("Invalid position (begin[2]<=pos[1]<=end[4])", e1.getMessage());
    }

    @Test
    public void testSetPositionTooBig() {
        final CharSequenceIterator valid = new CharSequenceIterator("123456789", 2, 4, 3);
        final var e1 = assertThrows(IllegalArgumentException.class, () -> valid.setIndex(5));
        assertEquals("Invalid position (begin[2]<=pos[5]<=end[4])", e1.getMessage());
    }

    @Test
    public void testBasicOperations() {
        final String number = "0123456789";
        final CharSequenceIterator valid = new CharSequenceIterator(number);
        assertEquals('0', valid.current());
        assertEquals(0, valid.getIndex());
        for (int i = 1; i < 10; i++) {
            assertEquals(number.charAt(i), valid.next());
            assertEquals(i, valid.getIndex());
        }
        assertEquals(CharacterIterator.DONE, valid.next());
        assertEquals(CharacterIterator.DONE, valid.next());
        for (int i = 9; i >= 0; i--) {
            assertEquals(number.charAt(i), valid.previous());
            assertEquals(i, valid.getIndex());
        }
        assertEquals(CharacterIterator.DONE, valid.previous());

        valid.setIndex(5);
        assertEquals('5', valid.current());
    }
}
