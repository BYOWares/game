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
package fr.byowares.game.miq.jfx.editor.tree;

import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import fr.byowares.game.utils.serial.source.SourceInMemory;

import java.util.Objects;

/**
 * A abstraction for of {@link fr.byowares.game.miq.jfx.editor.tree.MIQItem} based on
 * {@link fr.byowares.game.utils.serial.source.NamedSourcedObject}.
 *
 * @param <N> The type of the {@link fr.byowares.game.utils.serial.source.NamedSourcedObject}.
 *
 * @since XXX
 */
public abstract class ItemNamed<N extends NamedSourcedObject<N>>
        implements MIQItem {

    private final N namedSourcedObject;

    /**
     * @param namedSourcedObject The underlying {@link fr.byowares.game.utils.serial.source.NamedSourcedObject}.
     */
    ItemNamed(final N namedSourcedObject) {
        this.namedSourcedObject = Objects.requireNonNull(namedSourcedObject);
    }

    @Override
    public boolean canBeEdited() {
        return !(this.namedSourcedObject.getSource() instanceof SourceInMemory);
    }

    @Override
    public final String getName() {
        return this.namedSourcedObject.getName().toString();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
