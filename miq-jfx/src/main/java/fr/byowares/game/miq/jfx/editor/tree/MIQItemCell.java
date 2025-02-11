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

import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;

/**
 * The cell factory to handle the TreeItem display.
 *
 * @since XXX
 */
public class MIQItemCell
        extends TreeCell<MIQItem> {

    @Override
    protected void updateItem(
            final MIQItem item,
            final boolean empty
    ) {
        super.updateItem(item, empty);
        if (empty) {
            this.setText(null);
            this.setGraphic(null);
            this.setTooltip(null);
        } else {
            this.setText(item.getName());
            this.setGraphic(item.newFontIcon());
            this.setTooltip(new Tooltip());
            this.getTooltip().setText(item.toString());
        }
    }
}
