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

import java.util.Map;
import java.util.Objects;

/**
 * Generic versioned deserializer, that used a map to build an object. The strategy is to remove every key from the
 * map when using them. Thus, if at the end of the process, the map is not empty, it means some data are ill-formed.
 *
 * @param <T> The type it can deserialize.
 *
 * @since XXX
 */
public abstract class VersionedDeserializer<T> {

    /**
     * @param map The map containing all data.
     * @param key The key required in the map.
     *
     * @return The value associated to the {@code key}, or an {@code null} if none.
     *
     * @see #remove(java.util.Map, String, Class, Object)
     */
    protected static char removeAsCharacter(
            final Map<String, Object> map,
            final String key
    ) {
        final String v = Objects.requireNonNull(removeAsString(map, key, null));
        if (v.length() != 1) throw new IllegalArgumentException("A single character is expected here (" + v + ")");
        return v.charAt(0);
    }

    /**
     * @param map          The map containing all data.
     * @param key          The key required in the map.
     * @param defaultValue The default value when not found in the map.
     *
     * @return The value associated to the {@code key}, or {@code defaultValue} if none.
     *
     * @see #remove(java.util.Map, String, Class, Object)
     */
    protected static String removeAsString(
            final Map<String, Object> map,
            final String key,
            final String defaultValue
    ) {
        return remove(map, key, String.class, defaultValue);
    }

    /**
     * Remove the {@code key} from the {@code map}, and return {@code defaultValue} if not defined, its value if it
     * matches the expected type, throws an exception otherwise.
     *
     * @param map          The map containing all data.
     * @param key          The key required in the map.
     * @param valueClass   The expected class of the value.
     * @param defaultValue A default value when the key is not present in the map (can be {@code null}).
     * @param <T>          Value's expected type.
     *
     * @return The value associated to the {@code key}, or the provided default value.
     *
     * @throws java.lang.IllegalArgumentException If the {@code valueClass} and the current value class does not match.
     */
    protected static <T> T remove(
            final Map<String, Object> map,
            final String key,
            final Class<T> valueClass,
            final T defaultValue
    ) {
        final Object v = map.remove(key);
        if (v == null) return defaultValue;
        if (valueClass.isAssignableFrom(v.getClass())) return valueClass.cast(v);
        throw new IllegalArgumentException(
                "Invalid type. Expected " + valueClass.getTypeName() + ", but was " + v.getClass().getTypeName() + " (" + v + ")");
    }

    /**
     * @param map The map containing all data.
     * @param key The key required in the map.
     *
     * @return The value associated to the {@code key}, or an {@code null} if none.
     *
     * @see #remove(java.util.Map, String, Class, Object)
     */
    protected static String removeAsString(
            final Map<String, Object> map,
            final String key
    ) {
        return removeAsString(map, key, null);
    }

    /**
     * @param map The map used to build the object.
     *
     * @return The object build using this map.
     */
    protected abstract T buildFromMap(final Map<String, Object> map);

    /**
     * @return The versions of this deserializer.
     */
    protected abstract Version getVersion();
}
