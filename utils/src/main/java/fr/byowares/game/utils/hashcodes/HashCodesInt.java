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
package fr.byowares.game.utils.hashcodes;

/**
 * Utility class to enhance performances of {@link java.util.Objects#hash(Object...)} methods.
 *
 * @since XXX
 */
public final class HashCodesInt {

    private HashCodesInt() {
        throw new AssertionError("No fr.byowares.game.utils.hashcodes.HashCodesInt instances for you!");
    }

    private static int hashAux(
            final int res,
            final int i
    ) {
        return (res << 5) - res + hash(i);
    }

    /**
     * @param i the integer on which to compute the hashcode
     *
     * @return the hashCode of the integer, or 0 iff {@code null}
     */
    public static int hash(final int i) {
        return Integer.hashCode(i);
    }

    /**
     * @param i1 integer part of the hashcode to compute
     * @param i2 integer part of the hashcode to compute
     *
     * @return a hashcode combining all integers
     */
    public static int hash(
            final int i1,
            final int i2
    ) {
        int result = hash(i1);
        result = hashAux(result, i2);
        return result;
    }

    /**
     * @param i1 integer part of the hashcode to compute
     * @param i2 integer part of the hashcode to compute
     * @param i3 integer part of the hashcode to compute
     *
     * @return a hashcode combining all integers
     */
    public static int hash(
            final int i1,
            final int i2,
            final int i3
    ) {
        int result = hash(i1);
        result = hashAux(result, i2);
        result = hashAux(result, i3);
        return result;
    }

    /**
     * @param i1 integer part of the hashcode to compute
     * @param i2 integer part of the hashcode to compute
     * @param i3 integer part of the hashcode to compute
     * @param i4 integer part of the hashcode to compute
     *
     * @return a hashcode combining all integers
     */
    public static int hash(
            final int i1,
            final int i2,
            final int i3,
            final int i4
    ) {
        int result = hash(i1);
        result = hashAux(result, i2);
        result = hashAux(result, i3);
        result = hashAux(result, i4);
        return result;
    }

    /**
     * @param i1 integer part of the hashcode to compute
     * @param i2 integer part of the hashcode to compute
     * @param i3 integer part of the hashcode to compute
     * @param i4 integer part of the hashcode to compute
     * @param i5 integer part of the hashcode to compute
     *
     * @return a hashcode combining all integers
     */
    public static int hash(
            final int i1,
            final int i2,
            final int i3,
            final int i4,
            final int i5
    ) {
        int result = hash(i1);
        result = hashAux(result, i2);
        result = hashAux(result, i3);
        result = hashAux(result, i4);
        result = hashAux(result, i5);
        return result;
    }

    /**
     * @param i1 integer part of the hashcode to compute
     * @param i2 integer part of the hashcode to compute
     * @param i3 integer part of the hashcode to compute
     * @param i4 integer part of the hashcode to compute
     * @param i5 integer part of the hashcode to compute
     * @param i6 integer part of the hashcode to compute
     *
     * @return a hashcode combining all integers
     */
    public static int hash(
            final int i1,
            final int i2,
            final int i3,
            final int i4,
            final int i5,
            final int i6
    ) {
        int result = hash(i1);
        result = hashAux(result, i2);
        result = hashAux(result, i3);
        result = hashAux(result, i4);
        result = hashAux(result, i5);
        result = hashAux(result, i6);
        return result;
    }

    /**
     * @param i1 integer part of the hashcode to compute
     * @param i2 integer part of the hashcode to compute
     * @param i3 integer part of the hashcode to compute
     * @param i4 integer part of the hashcode to compute
     * @param i5 integer part of the hashcode to compute
     * @param i6 integer part of the hashcode to compute
     * @param i7 integer part of the hashcode to compute
     *
     * @return a hashcode combining all integers
     */
    public static int hash(
            final int i1,
            final int i2,
            final int i3,
            final int i4,
            final int i5,
            final int i6,
            final int i7
    ) {
        int result = hash(i1);
        result = hashAux(result, i2);
        result = hashAux(result, i3);
        result = hashAux(result, i4);
        result = hashAux(result, i5);
        result = hashAux(result, i6);
        result = hashAux(result, i7);
        return result;
    }

    /**
     * @param i1 integer part of the hashcode to compute
     * @param i2 integer part of the hashcode to compute
     * @param i3 integer part of the hashcode to compute
     * @param i4 integer part of the hashcode to compute
     * @param i5 integer part of the hashcode to compute
     * @param i6 integer part of the hashcode to compute
     * @param i7 integer part of the hashcode to compute
     * @param i8 integer part of the hashcode to compute
     *
     * @return a hashcode combining all integers
     */
    public static int hash(
            final int i1,
            final int i2,
            final int i3,
            final int i4,
            final int i5,
            final int i6,
            final int i7,
            final int i8
    ) {
        int result = hash(i1);
        result = hashAux(result, i2);
        result = hashAux(result, i3);
        result = hashAux(result, i4);
        result = hashAux(result, i5);
        result = hashAux(result, i6);
        result = hashAux(result, i7);
        result = hashAux(result, i8);
        return result;
    }

    /**
     * @param i1 integer part of the hashcode to compute
     * @param i2 integer part of the hashcode to compute
     * @param i3 integer part of the hashcode to compute
     * @param i4 integer part of the hashcode to compute
     * @param i5 integer part of the hashcode to compute
     * @param i6 integer part of the hashcode to compute
     * @param i7 integer part of the hashcode to compute
     * @param i8 integer part of the hashcode to compute
     * @param i9 integer part of the hashcode to compute
     *
     * @return a hashcode combining all integers
     */
    public static int hash(
            final int i1,
            final int i2,
            final int i3,
            final int i4,
            final int i5,
            final int i6,
            final int i7,
            final int i8,
            final int i9
    ) {
        int result = hash(i1);
        result = hashAux(result, i2);
        result = hashAux(result, i3);
        result = hashAux(result, i4);
        result = hashAux(result, i5);
        result = hashAux(result, i6);
        result = hashAux(result, i7);
        result = hashAux(result, i8);
        result = hashAux(result, i9);
        return result;
    }
}
