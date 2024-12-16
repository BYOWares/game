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
package fr.byowares.game.miq.core.model.volume;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class VolumeTest {
    private static final int MIN_VOLUME_ALLOWED = 0;
    private static final int MAX_VOLUME_ALLOWED = 200;

    public static IntStream provideValidVolumes() {
        return IntStream.range(Volume.MIN_VOLUME_INT, Volume.MAX_VOLUME_INT + 1);
    }

    @Test
    void testMinimalValue() {
        assertTrue(Volume.MIN_VOLUME_INT >= MIN_VOLUME_ALLOWED);
    }

    @Test
    void testMaximalValue() {
        assertTrue(Volume.MAX_VOLUME_INT <= MAX_VOLUME_ALLOWED);
    }

    @Test
    void testMinIsLowerThanMax() {
        assertTrue(Volume.MIN_VOLUME_INT <= Volume.MAX_VOLUME_INT);
    }

    @Test
    void testLowerThanMinimalValueThrowsException() {
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                                                        () -> Volume.of(Volume.MIN_VOLUME_INT - 1));
        assertEquals("Target value must be higher than minimal value (target=-1 < 0=MIN_VOLUME_INT)", e.getMessage());
    }

    @Test
    void testHigherThanMaximalValueThrowsException() {
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                                                        () -> Volume.of(Volume.MAX_VOLUME_INT + 1));
        assertEquals("Target value must be lower than maximal value (target=101 > 100=MAX_VOLUME_INT)", e.getMessage());
    }

    @ParameterizedTest(name = "Volume = {0}")
    @MethodSource("provideValidVolumes")
    void testValidAndUniqueObject(final int target) {
        final Volume v1 = Volume.of(target);
        final Volume v2 = Volume.of(target);
        assertSame(v1, v2);
    }
}
