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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A collection of {@link fr.byowares.game.miq.core.serial.VersionedDeserializer} of the same type.
 * Since they are all versioned, when the correct one can be used after the version was read. In addition, this
 * deserializer make sure that all field from the provided map are consumed.
 *
 * @param <T> The type of the object deserialized.
 *
 * @since XXX
 */
public abstract class Deserializers<T> {

    private static final Logger log = LoggerFactory.getLogger(Deserializers.class);

    /**
     * @param all List of deserializers known.
     * @param <T> The type of object they can deserialize.
     *
     * @return The map (deserializer.getVersion(), deserializer) using the given list.
     *
     * @throws java.lang.IllegalArgumentException If two deserializer share the same version.
     */
    static <T> SortedMap<Version, VersionedDeserializer<T>> buildVersionMap(final List<? extends VersionedDeserializer<T>> all) {
        final SortedMap<Version, VersionedDeserializer<T>> allAsMap = new TreeMap<>();
        for (final VersionedDeserializer<T> ad : all) {
            final var des = allAsMap.putIfAbsent(ad.getVersion(), ad);
            if (des != null) {
                log.error("2 deserializers share the same version, which is forbidden: {} & {}", ad, des);
                throw new IllegalStateException("2 deserializers share the same version, which is forbidden");
            }
        }
        return allAsMap;
    }

    /**
     * @param is The input stream used to get data.
     *
     * @return The deserialized object.
     */
    public final T load(final InputStream is) {
        final Map<String, Object> map = new Yaml().load(is);
        // Will throw NPE if the version is not found in the map.
        final Version version = Version.from(Objects.toString(Objects.requireNonNull(map.remove(Constants.VERSION))));
        final VersionedDeserializer<T> des = this.getAllDeserializersAsMap().get(version);
        if (des == null) {
            log.warn("No deserializer matched this version: {} (map={})", version, map);
            throw new IllegalArgumentException("No deserializer matched the given version: " + version);
        }

        final T t = des.buildFromMap(map);
        if (!map.isEmpty()) {
            log.warn("Some fields were not consumed while deserializing an object. Map={}, Object={}", map, t);
        }
        return t;
    }

    /**
     * @return The map (deserializer.getVersion(), deserializer) managed by this deserializers.
     */
    abstract SortedMap<Version, VersionedDeserializer<T>> getAllDeserializersAsMap();
}
