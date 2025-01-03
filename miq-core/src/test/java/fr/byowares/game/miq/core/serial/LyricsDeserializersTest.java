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
package fr.byowares.game.miq.core.serial;

import fr.byowares.game.miq.core.model.Lyrics;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static fr.byowares.game.miq.core.serial.v1.LyricsDeserializerV1Test.INPUT_V1;
import static fr.byowares.game.miq.core.serial.v1.LyricsDeserializerV1Test.LYRICS_V1;
import static fr.byowares.game.miq.core.serial.v1.LyricsDeserializerV1Test.VERSION_V1;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test version field. Other fields shall be parsed in versioned deserializers.
 */
public class LyricsDeserializersTest {

    public static ByteArrayInputStream inputStream(final String input) {
        return new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    }

    public static Lyrics assertDeserializeDoesNotThrow(
            final String input,
            final String target,
            final String replacement
    ) {
        return assertDoesNotThrow(
                () -> LyricsDeserializers.INSTANCE.deserialize(inputStream(input.replace(target, replacement))));
    }

    public static <T extends Throwable> T assertDeserializeThrows(
            final Class<T> expectedType,
            final String input,
            final String target,
            final String replacement
    ) {
        return assertThrows(expectedType, () -> LyricsDeserializers.INSTANCE.deserialize(
                inputStream(input.replace(target, replacement))));
    }

    /******************************************************************************************************************
     *                                                VERSION TESTS                                                   *
     ******************************************************************************************************************/
    @Test
    public void testValid() {
        assertEquals(LYRICS_V1, assertDeserializeDoesNotThrow(INPUT_V1, "XXX", ""));
    }

    @Test
    public void testVersionInvalidMissing() {
        assertDeserializeThrows(NullPointerException.class, INPUT_V1, VERSION_V1, "");
        assertDeserializeThrows(NullPointerException.class, INPUT_V1, VERSION_V1, "version:");
    }

    @Test
    public void testVersionInvalid0() {
        final var e = assertDeserializeThrows(IllegalArgumentException.class, INPUT_V1, VERSION_V1, "version: 0");
        assertEquals("No deserializer matched the given version: " + new Version(0), e.getMessage());
    }

    @Test
    public void testVersionInvalid2() {
        final var e = assertDeserializeThrows(IllegalArgumentException.class, INPUT_V1, VERSION_V1, "version: 2");
        assertEquals("No deserializer matched the given version: " + new Version(2), e.getMessage());
    }

    @Test
    public void testVersionInvalidA() {
        final var e = assertDeserializeThrows(IllegalArgumentException.class, INPUT_V1, VERSION_V1, "version: A");
        assertEquals("Error at index 0 in: \"A\"", e.getMessage());
    }
}
