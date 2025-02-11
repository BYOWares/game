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

import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import fr.byowares.game.utils.serial.source.Source;
import fr.byowares.game.utils.serial.source.SourceInMemory;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

/**
 * A abstraction for of {@link fr.byowares.game.miq.jfx.editor.tree.MIQItem} based on
 * {@link fr.byowares.game.utils.serial.source.NamedSourcedObject}.
 *
 * @param <N> The type of the {@link fr.byowares.game.utils.serial.source.NamedSourcedObject}.
 *
 * @since XXX
 */
public abstract class ItemNamed<N extends NamedSourcedObject>
        implements MIQItem {

    private final N namedSourcedObject;

    /**
     * @param namedSourcedObject The underlying {@link fr.byowares.game.utils.serial.source.NamedSourcedObject}.
     */
    ItemNamed(final N namedSourcedObject) {
        this.namedSourcedObject = Objects.requireNonNull(namedSourcedObject);
    }

    /**
     * @return The source of this {@link fr.byowares.game.utils.serial.source.NamedSourcedObject}.
     */
    final Source getSource() {
        return this.namedSourcedObject.getSource();
    }

    /**
     * @return A serializer of {@code N} to serialize the underlying object.
     */
    abstract Serializer<N> getSerializer();

    @Override
    public final String getName() {
        return this.namedSourcedObject.getName().toString();
    }

    @Override
    public final TreeItem<MIQItem> toTreeItem() {
        return new TreeItem<>(this, this.newFontIcon());
    }

    @Override
    public final void persist()
            throws IOException {
        final Source source = this.namedSourcedObject.getSource();
        try (final Writer writer = source.newWriter()) {
            this.getSerializer().serialize(this.namedSourcedObject, writer);
        }
    }

    @Override
    public boolean canBeEdited() {
        return !(this.namedSourcedObject.getSource() instanceof SourceInMemory);
    }

    @Override
    public String toString() {
        return "Item{" + this.namedSourcedObject + "}";
    }
}
