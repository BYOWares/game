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

import javafx.scene.control.TreeItem;

/**
 * Interface for all objects contained in a {@link javafx.scene.control.TreeView} (the value in the
 * {@link javafx.scene.control.TreeItem}.
 *
 * @since XXX
 */
public interface MIQItem {

    /**
     * @return {@code true} whether this item accept children items, {@code false} otherwise.
     */
    default boolean canHaveChildren() {
        return true;
    }

    /**
     * @return {@code true} whether this item can be edited, {@code false} otherwise.
     */
    boolean canBeEdited();

    /**
     * @return {@code true} whether this item can be deleted from its parent, {@code false} otherwise.
     */
    default boolean canBeDeleted() {
        return this.canBeEdited();
    }

    /**
     * @return The name of this object.
     */
    String getName();

    /**
     * @return A {@link javafx.scene.control.TreeItem} containing this object.
     */
    TreeItem<MIQItem> toTreeItem();
}
