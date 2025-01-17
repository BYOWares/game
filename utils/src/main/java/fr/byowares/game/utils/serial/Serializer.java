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

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Offer the capacity to serialize object of type {@code T} in Yaml format.
 *
 * @param <T> The type of object it can serialize.
 *
 * @since XXX
 */
public abstract class Serializer<T> {

    /** A version to identify format */
    static final String VERSION = "version";

    /** Default {@link org.yaml.snakeyaml.DumperOptions} while serializing yaml data. */
    private static final DumperOptions DUMPER_OPTIONS = new DumperOptions();

    static {
        DUMPER_OPTIONS.setIndent(2);
        DUMPER_OPTIONS.setPrettyFlow(true);
        DUMPER_OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    }

    /**
     * @param t      The object to serialize.
     * @param writer The object to write the serialization of the object.
     */
    public final void serialize(
            final T t,
            final Writer writer
    ) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(VERSION, this.getVersion().version());
        this.doSerialize(t, map);
        new Yaml(DUMPER_OPTIONS).dump(map, writer);
    }

    /**
     * @param t   The object to serialize.
     * @param map The map used to serialize the object.
     */
    protected abstract void doSerialize(
            final T t,
            final Map<String, Object> map
    );

    /**
     * @return The version of this serializer.
     */
    protected abstract Version getVersion();
}
