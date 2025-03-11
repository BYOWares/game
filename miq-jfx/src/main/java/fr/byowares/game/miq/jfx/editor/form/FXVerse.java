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
package fr.byowares.game.miq.jfx.editor.form;

import fr.byowares.game.miq.core.model.lyrics.Line;
import fr.byowares.game.utils.jfx.Font;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;

/**
 * A read only representation of {@link fr.byowares.game.miq.core.model.lyrics.Line}.
 *
 * @since XXX
 */
public class FXVerse {

    private final Font font;
    private final HBox root;
    private final ArrayList<Line> lines;

    /**
     * @param font The font to use for lyrics display.
     */
    FXVerse(final Font font) {
        this.font = font;
        this.root = new HBox(10.0);
        HBox.setHgrow(this.root, Priority.ALWAYS);
        this.lines = new ArrayList<>();
    }

    /** @return The graphical root element. */
    HBox getRoot() {
        return this.root;
    }

    /**
     * @return The list of {@link fr.byowares.game.miq.core.model.lyrics.Line} displayed by this element.
     */
    ArrayList<Line> getLines() {
        return this.lines;
    }

    /**
     * @param lines The new list of {@link fr.byowares.game.miq.core.model.lyrics.Line} to display.
     */
    void setLines(final List<Line> lines) {
        this.lines.clear();
        this.lines.addAll(lines);

        this.root.getChildren().clear();
        lines.forEach(line -> this.root.getChildren().add(new FXLine(this.font, line).getRoot()));
    }
}
