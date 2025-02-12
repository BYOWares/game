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

import fr.byowares.game.utils.serial.source.Source;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

/**
 * Interface for all objects contained in a {@link javafx.scene.control.TreeView} (the value in the
 * {@link javafx.scene.control.TreeItem}).
 *
 * @since XXX
 */
public interface MIQItem {

    /**
     * @return The name of this object.
     */
    String getName();

    /**
     * @return The source of this object.
     */
    Source getSource();

    /**
     * @return A new {@link org.kordamp.ikonli.javafx.FontIcon} used to describe this item.
     */
    FontIcon newFontIcon();

    /**
     * @return A {@link javafx.scene.control.TreeItem} containing this object.
     */
    TreeItem<MIQItem> toTreeItem();

    /**
     * Persist this object.
     *
     * @throws IOException If this object could not be persisted.
     */
    void persist()
            throws IOException;

    /**
     * @return {@code true} whether this item accept children items, {@code false} otherwise.
     */
    default boolean canHaveChildren() {
        return true;
    }

    /**
     * @param stage        The stage in which the createChild action takes place.
     * @param cachedData   The data cached (to be used in Combo Box to help filling some fields).
     * @param parentSource The first {@link fr.byowares.game.utils.serial.source.Source} in the child to create
     *                     ancestry which is not {@link fr.byowares.game.utils.serial.source.SourceInMemory}.
     *
     * @return The child item created, {@code null} if it could not be created.
     */
    MIQItem createChild(
            final Stage stage,
            final CachedData cachedData,
            final Source parentSource
    );

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
     * @return {@code true} If this element have a default child, {@code false} otherwise.
     */
    boolean hasDefaultChild();
}
