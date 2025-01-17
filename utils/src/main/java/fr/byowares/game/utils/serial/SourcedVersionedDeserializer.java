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

import fr.byowares.game.utils.serial.source.Source;

/**
 * A {@link fr.byowares.game.utils.serial.VersionedDeserializer} with a source context.
 *
 * @param <T> The deserialized object type.
 *
 * @since XX
 */
public abstract class SourcedVersionedDeserializer<T>
        extends VersionedDeserializer<T> {

    private Source source;

    /**
     * @return The current context.
     */
    public Source getSource() {
        return this.source;
    }

    /**
     * @param source The source to update.
     */
    public void setSource(final Source source) {
        this.source = source;
    }
}
