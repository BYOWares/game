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

import fr.byowares.game.miq.core.model.song.Libraries;
import javafx.scene.control.TreeItem;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * An {@link fr.byowares.game.miq.core.model.song.Libraries} {@link fr.byowares.game.miq.jfx.editor.tree.MIQItem} wrapper.
 *
 * @since XXX
 */
public class ItemLibraries
        extends ItemNamed<Libraries> {

    private static final FontIcon FONT_ICON = new FontIcon(BootstrapIcons.HDD);

    /**
     * @param libraries The {@link fr.byowares.game.miq.core.model.song.Libraries} to wrap.
     */
    public ItemLibraries(final Libraries libraries) {
        super(libraries);
    }

    @Override
    public boolean canBeDeleted() {
        return false;
    }

    @Override
    public TreeItem<MIQItem> toTreeItem() {
        return new TreeItem<>(this, FONT_ICON);
    }

    @Override
    public boolean canBeEdited() {
        return false;
    }
}
