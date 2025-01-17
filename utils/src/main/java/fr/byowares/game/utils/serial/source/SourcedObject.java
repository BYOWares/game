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
package fr.byowares.game.utils.serial.source;

import java.util.Objects;

/**
 * A generic object associated with a source. The source can be updated.
 *
 * @since XXX
 */
public abstract class SourcedObject {

    private Source source;

    /**
     * @param source The source of this object to instantiate.
     */
    public SourcedObject(final Source source) {
        this.source = source;
    }

    /**
     * A new sourced object whose source is {@link fr.byowares.game.utils.serial.source.SourceInMemory}.
     */
    public SourcedObject() {
        this(SourceInMemory.INSTANCE);
    }

    /**
     * @return The source of this object.
     */
    public Source getSource() {
        return this.source;
    }

    /**
     * @param source The new source for this object.
     */
    public void setSource(final Source source) {
        this.source = Objects.requireNonNull(source);
    }
}
