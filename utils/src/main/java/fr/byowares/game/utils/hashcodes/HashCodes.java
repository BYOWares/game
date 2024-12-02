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
public final class HashCodes {

    private HashCodes() {
        throw new AssertionError("No fr.byowares.game.utils.Objects instances for you!");
    }

    private static int hashAux(
            final int res,
            final Object o
    ) {
        return (res << 5) - res + hash(o);
    }

    /**
     * @param o the object on which to compute the hashcode
     *
     * @return the hashCode of the object, or 0 iff {@code null}
     */
    public static int hash(final Object o) {
        return java.util.Objects.hashCode(o);
    }

    /**
     * @param o1 object part of the hashcode to compute
     * @param o2 object part of the hashcode to compute
     *
     * @return a hashcode combining all objects
     */
    public static int hash(
            final Object o1,
            final Object o2
    ) {
        int result = hash(o1);
        result = hashAux(result, o2);
        return result;
    }

    /**
     * @param o1 object part of the hashcode to compute
     * @param o2 object part of the hashcode to compute
     * @param o3 object part of the hashcode to compute
     *
     * @return a hashcode combining all objects
     */
    public static int hash(
            final Object o1,
            final Object o2,
            final Object o3
    ) {
        int result = hash(o1);
        result = hashAux(result, o2);
        result = hashAux(result, o3);
        return result;
    }

    /**
     * @param o1 object part of the hashcode to compute
     * @param o2 object part of the hashcode to compute
     * @param o3 object part of the hashcode to compute
     * @param o4 object part of the hashcode to compute
     *
     * @return a hashcode combining all objects
     */
    public static int hash(
            final Object o1,
            final Object o2,
            final Object o3,
            final Object o4
    ) {
        int result = hash(o1);
        result = hashAux(result, o2);
        result = hashAux(result, o3);
        result = hashAux(result, o4);
        return result;
    }

    /**
     * @param o1 object part of the hashcode to compute
     * @param o2 object part of the hashcode to compute
     * @param o3 object part of the hashcode to compute
     * @param o4 object part of the hashcode to compute
     * @param o5 object part of the hashcode to compute
     *
     * @return a hashcode combining all objects
     */
    public static int hash(
            final Object o1,
            final Object o2,
            final Object o3,
            final Object o4,
            final Object o5
    ) {
        int result = hash(o1);
        result = hashAux(result, o2);
        result = hashAux(result, o3);
        result = hashAux(result, o4);
        result = hashAux(result, o5);
        return result;
    }

    /**
     * @param o1 object part of the hashcode to compute
     * @param o2 object part of the hashcode to compute
     * @param o3 object part of the hashcode to compute
     * @param o4 object part of the hashcode to compute
     * @param o5 object part of the hashcode to compute
     * @param o6 object part of the hashcode to compute
     *
     * @return a hashcode combining all objects
     */
    public static int hash(
            final Object o1,
            final Object o2,
            final Object o3,
            final Object o4,
            final Object o5,
            final Object o6
    ) {
        int result = hash(o1);
        result = hashAux(result, o2);
        result = hashAux(result, o3);
        result = hashAux(result, o4);
        result = hashAux(result, o5);
        result = hashAux(result, o6);
        return result;
    }

    /**
     * @param o1 object part of the hashcode to compute
     * @param o2 object part of the hashcode to compute
     * @param o3 object part of the hashcode to compute
     * @param o4 object part of the hashcode to compute
     * @param o5 object part of the hashcode to compute
     * @param o6 object part of the hashcode to compute
     * @param o7 object part of the hashcode to compute
     *
     * @return a hashcode combining all objects
     */
    public static int hash(
            final Object o1,
            final Object o2,
            final Object o3,
            final Object o4,
            final Object o5,
            final Object o6,
            final Object o7
    ) {
        int result = hash(o1);
        result = hashAux(result, o2);
        result = hashAux(result, o3);
        result = hashAux(result, o4);
        result = hashAux(result, o5);
        result = hashAux(result, o6);
        result = hashAux(result, o7);
        return result;
    }

    /**
     * @param o1 object part of the hashcode to compute
     * @param o2 object part of the hashcode to compute
     * @param o3 object part of the hashcode to compute
     * @param o4 object part of the hashcode to compute
     * @param o5 object part of the hashcode to compute
     * @param o6 object part of the hashcode to compute
     * @param o7 object part of the hashcode to compute
     * @param o8 object part of the hashcode to compute
     *
     * @return a hashcode combining all objects
     */
    public static int hash(
            final Object o1,
            final Object o2,
            final Object o3,
            final Object o4,
            final Object o5,
            final Object o6,
            final Object o7,
            final Object o8
    ) {
        int result = hash(o1);
        result = hashAux(result, o2);
        result = hashAux(result, o3);
        result = hashAux(result, o4);
        result = hashAux(result, o5);
        result = hashAux(result, o6);
        result = hashAux(result, o7);
        result = hashAux(result, o8);
        return result;
    }

    /**
     * @param o1 object part of the hashcode to compute
     * @param o2 object part of the hashcode to compute
     * @param o3 object part of the hashcode to compute
     * @param o4 object part of the hashcode to compute
     * @param o5 object part of the hashcode to compute
     * @param o6 object part of the hashcode to compute
     * @param o7 object part of the hashcode to compute
     * @param o8 object part of the hashcode to compute
     * @param o9 object part of the hashcode to compute
     *
     * @return a hashcode combining all objects
     */
    public static int hash(
            final Object o1,
            final Object o2,
            final Object o3,
            final Object o4,
            final Object o5,
            final Object o6,
            final Object o7,
            final Object o8,
            final Object o9
    ) {
        int result = hash(o1);
        result = hashAux(result, o2);
        result = hashAux(result, o3);
        result = hashAux(result, o4);
        result = hashAux(result, o5);
        result = hashAux(result, o6);
        result = hashAux(result, o7);
        result = hashAux(result, o8);
        result = hashAux(result, o9);
        return result;
    }
}
