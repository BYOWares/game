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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class CSVBuilderTest {

    public static final int _123 = 123;
    public static final long _123L = 123L;
    private final CSVBuilder builder = new CSVBuilder('#');

    @BeforeEach
    public void before() {
        assertEquals("", this.builder.asCharSequenceAndReset());
    }

    @Test
    void testConstructorInvalid() {
        final var e = assertThrows(IllegalArgumentException.class, () -> new CSVBuilder('\\'));
        assertEquals("Separator cannot be \\", e.getMessage());
    }

    @Test
    void testNewFieldInt() {
        this.builder.newField(_123);
        assertEquals("123", this.builder.asCharSequenceAndReset());
        this.builder.newField(_123);
        this.builder.newField(_123);
        assertEquals("123#123", this.builder.asCharSequenceAndReset());
    }

    @Test
    void testNewFieldLong() {
        this.builder.newField(_123L);
        assertEquals("123", this.builder.asCharSequenceAndReset());
        this.builder.newField(_123L);
        this.builder.newField(_123L);
        assertEquals("123#123", this.builder.asCharSequenceAndReset());
    }

    @Test
    void testNewFieldCharSequenceEndingWithSeparator() {
        this.builder.newField("John #");
        assertEquals("John \\#", this.builder.asCharSequenceAndReset());
        this.builder.newField("John #");
        this.builder.newField("John #");
        assertEquals("John \\##John \\#", this.builder.asCharSequenceAndReset());
    }

    @Test
    void testNewFieldCharSequenceEndingWithBackSlash() {
        this.builder.newField("John\\");
        assertEquals("John\\\\", this.builder.asCharSequenceAndReset());
        this.builder.newField("John\\");
        this.builder.newField("John\\");
        assertEquals("John\\\\#John\\\\", this.builder.asCharSequenceAndReset());
    }

    @Test
    void testNewFieldCollection() {
        final List<CharSequence> l1 = List.of("David\\", "Peter#", "John");
        this.builder.newField(l1, Function.identity());
        assertEquals("David\\\\ Peter\\# John", this.builder.asCharSequenceAndReset());

        this.builder.newField(l1, Function.identity());
        final List<CharSequence> l2 = List.of("Some Space", "remains unescaped", "!");
        this.builder.newField(l2, Function.identity());
        assertEquals("David\\\\ Peter\\# John#Some Space remains unescaped !", this.builder.asCharSequenceAndReset());
    }
}
