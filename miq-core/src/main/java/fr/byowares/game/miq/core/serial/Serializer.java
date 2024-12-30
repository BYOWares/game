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

import java.io.Writer;

/**
 * Offer the capacity to serialize object of type {@code T} in Yaml format.
 *
 * @param <T> The type of object it can serialize.
 *
 * @since XXX
 */
@FunctionalInterface
public interface Serializer<T> {

    /**
     * @param t      The object to serialize.
     * @param writer The object to write the serialization of the object.
     */
    void serialize(
            T t,
            Writer writer
    );
}
