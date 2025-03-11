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

import atlantafx.base.controls.Popover;
import atlantafx.base.theme.Styles;
import fr.byowares.game.miq.core.model.lyrics.Line;
import fr.byowares.game.miq.core.model.lyrics.Singer;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.jfx.Font;
import fr.byowares.game.utils.jfx.i18n.I18NUtils;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Objects;

/**
 * FX representation of a {@link fr.byowares.game.miq.core.model.lyrics.Line} which cannot be edited.
 *
 * @since XXX
 */
public class FXLine {

    private final HBox root;

    /**
     * @param font The {@link fr.byowares.game.utils.jfx.Font} used to display lyrics.
     * @param line The line to display.
     */
    FXLine(
            final Font font,
            final Line line
    ) {
        final FXLineContext ctx = new FXLineContext(line);
        final Popover pop = new Popover(ctx.gridPane);
        pop.setArrowLocation(Popover.ArrowLocation.RIGHT_CENTER);
        pop.setHeaderAlwaysVisible(false);
        pop.setDetachable(false);

        final Hyperlink hyperlink = new Hyperlink("?", null);
        hyperlink.getStyleClass().addAll("text-input", Styles.LEFT_PILL);
        hyperlink.setOnAction(e -> pop.show(hyperlink));

        final TextField lyric = new TextField(Objects.toString(line.getClearText()));
        lyric.setEditable(false);
        lyric.getStyleClass().add(Styles.RIGHT_PILL);
        lyric.prefColumnCountProperty().bind(lyric.lengthProperty());
        lyric.setStyle(font.toFxFont());

        hyperlink.minHeightProperty().bind(lyric.heightProperty());

        this.root = new HBox(0.0, hyperlink, lyric);
    }

    /** @return The root graphical element. */
    Node getRoot() {
        return this.root;
    }

    /**
     * FX elements to show line's context:
     * <ul>
     *     <li>{@link fr.byowares.game.miq.core.model.lyrics.Line#isNonLexical()}</li>
     *     <li>{@link fr.byowares.game.miq.core.model.lyrics.Line#isBackVocals()}</li>
     *     <li>{@link fr.byowares.game.miq.core.model.lyrics.Line#getSingers()}</li>
     * </ul>
     */
    private static class FXLineContext {

        private final GridPane gridPane;

        private FXLineContext(final Line line) {
            final I18NMIQ i18nMIQ = I18NMIQ.get();
            final I18NUtils i18nUtils = I18NUtils.get();

            this.gridPane = new GridPane(5.0, 2.0);

            final Label nonLexicalLabel = new Label();
            i18nMIQ.bind(nonLexicalLabel.textProperty(), "form_field.lyrics.non_lexical");
            final boolean nonLexical = line.isNonLexical();
            final Label nonLexicalValue = new Label();
            nonLexicalValue.getStyleClass().add(Styles.TEXT_BOLD);
            nonLexicalValue.getStyleClass().add(nonLexical ? Styles.SUCCESS : Styles.DANGER);
            i18nUtils.bind(nonLexicalValue.textProperty(), nonLexical ? "text.Yes" : "text.No");

            final Label backupVocalsLabel = new Label();
            i18nMIQ.bind(backupVocalsLabel.textProperty(), "form_field.lyrics.backup_vocals");
            final boolean backupVocals = line.isBackVocals();
            final Label backupVocalsValue = new Label();
            backupVocalsValue.getStyleClass().add(Styles.TEXT_BOLD);
            backupVocalsValue.getStyleClass().add(backupVocals ? Styles.SUCCESS : Styles.DANGER);
            i18nUtils.bind(backupVocalsValue.textProperty(), backupVocals ? "text.Yes" : "text.No");

            final Label singersLabel = new Label();
            i18nMIQ.bind(singersLabel.textProperty(), "form_field.lyrics.singers");

            GridPane.setHalignment(nonLexicalLabel, HPos.RIGHT);
            GridPane.setHalignment(backupVocalsLabel, HPos.RIGHT);
            GridPane.setHalignment(singersLabel, HPos.RIGHT);
            GridPane.setHalignment(nonLexicalValue, HPos.CENTER);
            GridPane.setHalignment(backupVocalsValue, HPos.CENTER);

            int rowIndex = 0;
            this.gridPane.add(nonLexicalLabel, 0, rowIndex);
            this.gridPane.add(nonLexicalValue, 1, rowIndex);
            this.gridPane.add(backupVocalsLabel, 0, ++rowIndex);
            this.gridPane.add(backupVocalsValue, 1, rowIndex);
            this.gridPane.add(singersLabel, 0, ++rowIndex);

            for (final Singer singer : line.getSingers()) {
                final Label child = new Label(singer.name().toString());
                this.gridPane.add(child, 1, rowIndex++);
                GridPane.setHalignment(child, HPos.LEFT);
                child.getStyleClass().add(Styles.TEXT_BOLD);
            }
        }
    }
}
