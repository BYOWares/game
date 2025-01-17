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
package fr.byowares.game.utils.serial;

import org.agrona.LangUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A collection of {@link VersionedDeserializer} of the same type.
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
     * @param <D> The type of the deserializer.
     *
     * @return The map (deserializer.getVersion(), deserializer) using the given list.
     *
     * @throws IllegalArgumentException If two deserializer share the same version.
     */
    public static <T, D extends VersionedDeserializer<T>> SortedMap<Version, D> buildVersionMap(final List<D> all) {
        final SortedMap<Version, D> allAsMap = new TreeMap<>();
        for (final D ad : all) {
            final var des = allAsMap.putIfAbsent(ad.getVersion(), ad);
            if (des != null) {
                log.error("2 deserializers share the same version, which is forbidden: {} & {}", ad, des);
                throw new IllegalStateException("2 deserializers share the same version, which is forbidden");
            }
        }
        return allAsMap;
    }

    /**
     * @param path The {@link java.nio.file.Path} to the Yaml data.
     *
     * @return The map representing the content found in the {@code path}.
     */
    public static Map<String, Object> load(final Path path) {
        try (final InputStream is = new BufferedInputStream(Files.newInputStream(path))) {
            return load(is);
        } catch (final IOException e) {
            log.warn("Fail to load {}", path, e);
            LangUtil.rethrowUnchecked(e);
        }
        return Map.of(); // Unreachable
    }

    /**
     * @param is The {@link java.io.InputStream} containing Yaml data.
     *
     * @return The map representing the Yaml data found in the {@code is}.
     */
    public static Map<String, Object> load(final InputStream is) {
        return new Yaml().load(is);
    }

    /**
     * @param is The input stream used to get data.
     *
     * @return The deserialized object.
     */
    public final T deserialize(final InputStream is) {
        return this.deserialize(load(is));
    }

    /**
     * @param map The map containing data to build an object.
     *
     * @return The object build using the {@code map}.
     *
     * @throws java.lang.IllegalArgumentException If no deserializer were found to match the version.
     */
    public final T deserialize(final Map<String, Object> map) {
        final Version version = Version.from(Objects.toString(Objects.requireNonNull(map.remove(Serializer.VERSION))));
        final VersionedDeserializer<T> des = this.getAllDeserializersAsMap().get(version);
        if (des == null) {
            log.warn("No deserializer matched this version: {} (map={})", version, map);
            throw new IllegalArgumentException("No deserializer matched the given version: " + version);
        }

        final T t = des.doDeserialize(map);
        if (!map.isEmpty()) {
            log.warn("Some fields were not consumed while deserializing an object. Map={}, Object={}", map, t);
        }
        return t;
    }

    /**
     * @return The map (deserializer.getVersion(), deserializer) managed by this deserializers.
     */
    protected abstract SortedMap<Version, ? extends VersionedDeserializer<T>> getAllDeserializersAsMap();
}
