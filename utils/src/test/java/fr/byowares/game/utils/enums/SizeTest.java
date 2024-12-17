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
package fr.byowares.game.utils.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SizeTest {

    private static final long BINARY_1K = 1024L;
    private static final long ZERO = 0L;
    private static final long BB_4 = 4L;
    private static final long KB_4 = BB_4 * BINARY_1K;
    private static final long MB_4 = KB_4 * BINARY_1K;
    private static final long GB_4 = MB_4 * BINARY_1K;
    private static final long TB_4 = GB_4 * BINARY_1K;
    private static final long PB_4 = TB_4 * BINARY_1K;
    private static final long EB_4 = PB_4 * BINARY_1K;
    private static final long[] INPUTS = {BB_4, KB_4, MB_4, GB_4, TB_4, PB_4, EB_4};
    private static final long L_MV = Long.MAX_VALUE;
    private static final NamedFunction TO_BB = new NamedFunction("toBytes", Size::toBytes);
    private static final NamedFunction TO_KB = new NamedFunction("toKibiBytes", Size::toKibiBytes);
    private static final NamedFunction TO_MB = new NamedFunction("toMebiBytes", Size::toMebiBytes);
    private static final NamedFunction TO_GB = new NamedFunction("toGibiBytes", Size::toGibiBytes);
    private static final NamedFunction TO_TB = new NamedFunction("toTebiBytes", Size::toTebiBytes);

    public static Stream<Arguments> provideData() {
        final List<Arguments> data = new ArrayList<>(15 * 5 * 5);
        // Byte cases
        addCase(data, Size.BYTE, TO_BB, BB_4, KB_4, MB_4, GB_4, TB_4, PB_4, EB_4);
        addCase(data, Size.BYTE, TO_KB, ZERO, BB_4, KB_4, MB_4, GB_4, TB_4, PB_4);
        addCase(data, Size.BYTE, TO_MB, ZERO, ZERO, BB_4, KB_4, MB_4, GB_4, TB_4);
        addCase(data, Size.BYTE, TO_GB, ZERO, ZERO, ZERO, BB_4, KB_4, MB_4, GB_4);
        addCase(data, Size.BYTE, TO_TB, ZERO, ZERO, ZERO, ZERO, BB_4, KB_4, MB_4);

        // KibiByte cases
        addCase(data, Size.KB, TO_BB, KB_4, MB_4, GB_4, TB_4, PB_4, EB_4, L_MV);
        addCase(data, Size.KB, TO_KB, BB_4, KB_4, MB_4, GB_4, TB_4, PB_4, EB_4);
        addCase(data, Size.KB, TO_MB, ZERO, BB_4, KB_4, MB_4, GB_4, TB_4, PB_4);
        addCase(data, Size.KB, TO_GB, ZERO, ZERO, BB_4, KB_4, MB_4, GB_4, TB_4);
        addCase(data, Size.KB, TO_TB, ZERO, ZERO, ZERO, BB_4, KB_4, MB_4, GB_4);

        // MebiByte cases
        addCase(data, Size.MB, TO_BB, MB_4, GB_4, TB_4, PB_4, EB_4, L_MV, L_MV);
        addCase(data, Size.MB, TO_KB, KB_4, MB_4, GB_4, TB_4, PB_4, EB_4, L_MV);
        addCase(data, Size.MB, TO_MB, BB_4, KB_4, MB_4, GB_4, TB_4, PB_4, EB_4);
        addCase(data, Size.MB, TO_GB, ZERO, BB_4, KB_4, MB_4, GB_4, TB_4, PB_4);
        addCase(data, Size.MB, TO_TB, ZERO, ZERO, BB_4, KB_4, MB_4, GB_4, TB_4);

        // GibiByte cases
        addCase(data, Size.GB, TO_BB, GB_4, TB_4, PB_4, EB_4, L_MV, L_MV, L_MV);
        addCase(data, Size.GB, TO_KB, MB_4, GB_4, TB_4, PB_4, EB_4, L_MV, L_MV);
        addCase(data, Size.GB, TO_MB, KB_4, MB_4, GB_4, TB_4, PB_4, EB_4, L_MV);
        addCase(data, Size.GB, TO_GB, BB_4, KB_4, MB_4, GB_4, TB_4, PB_4, EB_4);
        addCase(data, Size.GB, TO_TB, ZERO, BB_4, KB_4, MB_4, GB_4, TB_4, PB_4);

        // TebiByte cases
        addCase(data, Size.TB, TO_BB, TB_4, PB_4, EB_4, L_MV, L_MV, L_MV, L_MV);
        addCase(data, Size.TB, TO_KB, GB_4, TB_4, PB_4, EB_4, L_MV, L_MV, L_MV);
        addCase(data, Size.TB, TO_MB, MB_4, GB_4, TB_4, PB_4, EB_4, L_MV, L_MV);
        addCase(data, Size.TB, TO_GB, KB_4, MB_4, GB_4, TB_4, PB_4, EB_4, L_MV);
        addCase(data, Size.TB, TO_TB, BB_4, KB_4, MB_4, GB_4, TB_4, PB_4, EB_4);

        return data.stream();
    }

    private static void addCase(
            final List<Arguments> data,
            final Size size,
            final NamedFunction namedFunction,
            final long... expected
    ) {
        if (INPUTS.length != expected.length) throw new IllegalArgumentException(); // TODO
        data.add(Arguments.of(ZERO, size, namedFunction, ZERO));
        for (int i = 0; i < INPUTS.length; i++) {
            final long pExp = expected[i];
            final long nExp = (pExp == L_MV) ? Long.MIN_VALUE : -pExp;
            data.add(Arguments.of(INPUTS[i], size, namedFunction, pExp));
            data.add(Arguments.of(-INPUTS[i], size, namedFunction, nExp));
        }
    }

    @ParameterizedTest(name = "[{index}] value={0}, size={1}, method={2}, expected={3}")
    @MethodSource("provideData")
    void testConversionToAllSizeAndOverflow(
            final long value,
            final Size currentSize,
            final NamedFunction namedFunction,
            final long converted
    ) {
        assertEquals(converted, namedFunction.function.apply(currentSize, value));
    }

    @Test
    void testTruncationWithPositiveSize() {
        assertEquals(0L, Size.BYTE.toKibiBytes(1L));
        assertEquals(0L, Size.BYTE.toKibiBytes((BINARY_1K / 2L) - 1L));
        assertEquals(0L, Size.BYTE.toKibiBytes(BINARY_1K / 2L));
        assertEquals(0L, Size.BYTE.toKibiBytes((BINARY_1K / 2L) + 1L));
        assertEquals(0L, Size.BYTE.toKibiBytes(BINARY_1K - 1L));

        assertEquals(1L, Size.BYTE.toKibiBytes(BINARY_1K));
        assertEquals(1L, Size.BYTE.toKibiBytes(BINARY_1K + 1L));
        assertEquals(1L, Size.BYTE.toKibiBytes(BINARY_1K + BINARY_1K / 2L));
        assertEquals(1L, Size.BYTE.toKibiBytes(BINARY_1K + (BINARY_1K / 2L) - 1L));
        assertEquals(1L, Size.BYTE.toKibiBytes(BINARY_1K + (BINARY_1K / 2L) + 1L));
    }

    @Test
    void testTruncationWithNegativeSize() {
        assertEquals(0L, Size.BYTE.toKibiBytes(-1L));
        assertEquals(0L, Size.BYTE.toKibiBytes(-((BINARY_1K / 2L) - 1L)));
        assertEquals(0L, Size.BYTE.toKibiBytes(-(BINARY_1K / 2L)));
        assertEquals(0L, Size.BYTE.toKibiBytes(-((BINARY_1K / 2L) + 1L)));
        assertEquals(0L, Size.BYTE.toKibiBytes(-BINARY_1K + 1L));

        assertEquals(-1L, Size.BYTE.toKibiBytes(-BINARY_1K));
        assertEquals(-1L, Size.BYTE.toKibiBytes(-(BINARY_1K + 1L)));
        assertEquals(-1L, Size.BYTE.toKibiBytes(-(BINARY_1K + BINARY_1K / 2L)));
        assertEquals(-1L, Size.BYTE.toKibiBytes(-(BINARY_1K + (BINARY_1K / 2L) - 1L)));
        assertEquals(-1L, Size.BYTE.toKibiBytes(-(BINARY_1K + (BINARY_1K / 2L) + 1L)));
    }

    @FunctionalInterface
    private static interface Function {
        long apply(
                final Size size,
                final long value
        );
    }

    private record NamedFunction(
            String name,
            Function function
    ) {
        @Override
        public String toString() {
            return this.name;
        }
    }
}
