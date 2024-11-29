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
package fr.byowares.game.utils;

/**
 * Utility class to enhance performances of {@link java.util.Objects#hash(Object...)} methods.
 *
 * @since XXX
 */
public final class HashCodesLong {

    private HashCodesLong() {
        throw new AssertionError("No fr.byowares.game.utils.HashCodesLong instances for you!");
    }

    private static int hashAux(
            final int res,
            final long l
    ) {
        return (res << 5) - res + hash(l);
    }

    /**
     * @param l the long on which to compute the hashcode
     *
     * @return the hashCode of the long, or 0 iff {@code null}
     */
    public static int hash(final long l) {
        return Long.hashCode(l);
    }

    /**
     * @param l1 long part of the hashcode to compute
     * @param l2 long part of the hashcode to compute
     *
     * @return a hashcode combining all longs
     */
    public static int hash(
            final long l1,
            final long l2
    ) {
        int result = hash(l1);
        result = hashAux(result, l2);
        return result;
    }

    /**
     * @param l1 long part of the hashcode to compute
     * @param l2 long part of the hashcode to compute
     * @param l3 long part of the hashcode to compute
     *
     * @return a hashcode combining all longs
     */
    public static int hash(
            final long l1,
            final long l2,
            final long l3
    ) {
        int result = hash(l1);
        result = hashAux(result, l2);
        result = hashAux(result, l3);
        return result;
    }

    /**
     * @param l1 long part of the hashcode to compute
     * @param l2 long part of the hashcode to compute
     * @param l3 long part of the hashcode to compute
     * @param l4 long part of the hashcode to compute
     *
     * @return a hashcode combining all longs
     */
    public static int hash(
            final long l1,
            final long l2,
            final long l3,
            final long l4
    ) {
        int result = hash(l1);
        result = hashAux(result, l2);
        result = hashAux(result, l3);
        result = hashAux(result, l4);
        return result;
    }

    /**
     * @param l1 long part of the hashcode to compute
     * @param l2 long part of the hashcode to compute
     * @param l3 long part of the hashcode to compute
     * @param l4 long part of the hashcode to compute
     * @param l5 long part of the hashcode to compute
     *
     * @return a hashcode combining all longs
     */
    public static int hash(
            final long l1,
            final long l2,
            final long l3,
            final long l4,
            final long l5
    ) {
        int result = hash(l1);
        result = hashAux(result, l2);
        result = hashAux(result, l3);
        result = hashAux(result, l4);
        result = hashAux(result, l5);
        return result;
    }

    /**
     * @param l1 long part of the hashcode to compute
     * @param l2 long part of the hashcode to compute
     * @param l3 long part of the hashcode to compute
     * @param l4 long part of the hashcode to compute
     * @param l5 long part of the hashcode to compute
     * @param l6 long part of the hashcode to compute
     *
     * @return a hashcode combining all longs
     */
    public static int hash(
            final long l1,
            final long l2,
            final long l3,
            final long l4,
            final long l5,
            final long l6
    ) {
        int result = hash(l1);
        result = hashAux(result, l2);
        result = hashAux(result, l3);
        result = hashAux(result, l4);
        result = hashAux(result, l5);
        result = hashAux(result, l6);
        return result;
    }

    /**
     * @param l1 long part of the hashcode to compute
     * @param l2 long part of the hashcode to compute
     * @param l3 long part of the hashcode to compute
     * @param l4 long part of the hashcode to compute
     * @param l5 long part of the hashcode to compute
     * @param l6 long part of the hashcode to compute
     * @param l7 long part of the hashcode to compute
     *
     * @return a hashcode combining all longs
     */
    public static int hash(
            final long l1,
            final long l2,
            final long l3,
            final long l4,
            final long l5,
            final long l6,
            final long l7
    ) {
        int result = hash(l1);
        result = hashAux(result, l2);
        result = hashAux(result, l3);
        result = hashAux(result, l4);
        result = hashAux(result, l5);
        result = hashAux(result, l6);
        result = hashAux(result, l7);
        return result;
    }

    /**
     * @param l1 long part of the hashcode to compute
     * @param l2 long part of the hashcode to compute
     * @param l3 long part of the hashcode to compute
     * @param l4 long part of the hashcode to compute
     * @param l5 long part of the hashcode to compute
     * @param l6 long part of the hashcode to compute
     * @param l7 long part of the hashcode to compute
     * @param l8 long part of the hashcode to compute
     *
     * @return a hashcode combining all longs
     */
    public static int hash(
            final long l1,
            final long l2,
            final long l3,
            final long l4,
            final long l5,
            final long l6,
            final long l7,
            final long l8
    ) {
        int result = hash(l1);
        result = hashAux(result, l2);
        result = hashAux(result, l3);
        result = hashAux(result, l4);
        result = hashAux(result, l5);
        result = hashAux(result, l6);
        result = hashAux(result, l7);
        result = hashAux(result, l8);
        return result;
    }

    /**
     * @param l1 long part of the hashcode to compute
     * @param l2 long part of the hashcode to compute
     * @param l3 long part of the hashcode to compute
     * @param l4 long part of the hashcode to compute
     * @param l5 long part of the hashcode to compute
     * @param l6 long part of the hashcode to compute
     * @param l7 long part of the hashcode to compute
     * @param l8 long part of the hashcode to compute
     * @param l9 long part of the hashcode to compute
     *
     * @return a hashcode combining all longs
     */
    public static int hash(
            final long l1,
            final long l2,
            final long l3,
            final long l4,
            final long l5,
            final long l6,
            final long l7,
            final long l8,
            final long l9
    ) {
        int result = hash(l1);
        result = hashAux(result, l2);
        result = hashAux(result, l3);
        result = hashAux(result, l4);
        result = hashAux(result, l5);
        result = hashAux(result, l6);
        result = hashAux(result, l7);
        result = hashAux(result, l8);
        result = hashAux(result, l9);
        return result;
    }
}
