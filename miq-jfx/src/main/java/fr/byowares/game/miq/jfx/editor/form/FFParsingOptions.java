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

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.option.Bracket;
import fr.byowares.game.miq.core.option.LyricsParsingOptions;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Form Field to configure the {@link fr.byowares.game.miq.core.option.LyricsParsingOptions}.
 *
 * @since XXX
 */
public class FFParsingOptions
        extends SimpleFormField<VBox, Lyrics, LyricsParsingOptions, LyricsParsingOptions> {

    private static final double COMBO_WIDTH = 80.0;
    private static final double LEFT_PADDING = 35.0;
    private final ComboBox<BracketI18N> singer;
    private final ComboBox<BracketI18N> backVocal;
    private final ComboBox<BracketI18N> nonLexical;
    private final Button apply;
    private LyricsParsingOptions lastAppliedOptions;

    /** Default instance. */
    FFParsingOptions() {
        super(SimpleFormField.newVBoxContainer(), noOpBiConsumer(),
              I18NMIQ.binder(I18NMIQ.get(), "form_field.options_parser.name"), MIN_SIZE_MEDIUM, MIN_SIZE_NONE);

        final ObservableList<BracketI18N> items = FXCollections.observableArrayList(BracketI18N.VALUES);
        this.singer = newComboBox(items);
        this.backVocal = newComboBox(items);
        this.nonLexical = newComboBox(items);
        final ChangeListener<BracketI18N> listener = (obs, ov, nv) -> {
            final LyricsParsingOptions options = this.getCurrentInput();
            this.runValidators(options);
            this.updateApplyEnable(options);
        };
        this.singer.valueProperty().addListener(listener);
        this.backVocal.valueProperty().addListener(listener);
        this.nonLexical.valueProperty().addListener(listener);

        this.addValidator(new Validator());

        final GridPane gridPane = new GridPane(10.0, 1.0);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        addToGridPane(gridPane, this.singer, 0, "form_field.options_parser.delimiter.singer");
        addToGridPane(gridPane, this.backVocal, 1, "form_field.options_parser.delimiter.backup_vocal");
        addToGridPane(gridPane, this.nonLexical, 2, "form_field.options_parser.delimiter.non_lexical");

        final HBox hBox = new HBox(gridPane);
        hBox.setPadding(new Insets(0.0, 0.0, 0.0, LEFT_PADDING));

        this.apply = new Button();
        this.apply.getStyleClass().addAll(Styles.FLAT, Styles.TEXT_UNDERLINED);
        I18NMIQ.get().bind(this.apply.textProperty(), "form_field.options_parser.apply");
        this.apply.setDisable(true);

        this.getRoot().getChildren().addAll(hBox, this.apply);
    }

    private static void addToGridPane(
            final GridPane gridPane,
            final ComboBox<BracketI18N> comboBox,
            final int rowIndex,
            final String i18n
    ) {
        final Label label = new Label();
        label.setTooltip(new Tooltip());
        I18NMIQ.get().bind(label.textProperty(), i18n);
        I18NMIQ.get().bind(label.getTooltip().textProperty(), i18n + ".desc");
        gridPane.add(label, 0, rowIndex);
        gridPane.add(comboBox, 1, rowIndex);
    }

    private static ComboBox<BracketI18N> newComboBox(final ObservableList<BracketI18N> items) {
        final ComboBox<BracketI18N> comboBox = new ComboBox<>(items);
        comboBox.setButtonCell(new BracketCell(false));
        comboBox.setCellFactory(l -> new BracketCell(true));
        comboBox.setMaxWidth(COMBO_WIDTH);
        comboBox.setMinWidth(COMBO_WIDTH);
        comboBox.getStyleClass().addAll(Tweaks.ALT_ICON);
        return comboBox;
    }

    private static Bracket extractBracketFromCombo(final ComboBox<BracketI18N> bracket) {
        return (bracket.getValue() == null) ? null : bracket.getValue().bracket;
    }

    private void updateApplyEnable(final LyricsParsingOptions options) {
        final boolean optionIsEqual = Objects.equals(options, this.lastAppliedOptions);
        final boolean hasAnError = this.inErrorProperty().get();
        this.apply.setDisable(optionIsEqual || hasAnError);
    }

    /**
     * @param optionsConsumer The action to perform when the new set of options is applied.
     */
    void setApplyHandler(final Consumer<LyricsParsingOptions> optionsConsumer) {
        this.apply.setOnAction(e -> {
            this.lastAppliedOptions = this.getCurrentInput();
            this.apply.setDisable(true);
            optionsConsumer.accept(this.lastAppliedOptions);
        });
        this.apply.fire();
    }

    @Override
    LyricsParsingOptions getCurrentInput() {
        return new LyricsParsingOptions() //
                .singer(extractBracketFromCombo(this.singer)) //
                .backVocals(extractBracketFromCombo(this.backVocal)) //
                .nonLexical(extractBracketFromCombo(this.nonLexical)) //
                ;
    }

    @Override
    void doSet(
            final BiConsumer<Lyrics, LyricsParsingOptions> setter,
            final Lyrics lyrics
    ) {
        // Nothing to do
    }

    @Override
    void runValidatorsOnError() {
        // Nothing to do
    }

    @Override
    void runValidatorOnSuccess() {
        // Nothing to do
    }

    @Override
    public void init(final LyricsParsingOptions input) {
        this.singer.setValue(BracketI18N.fromBracket(input.singerBracket()));
        this.backVocal.setValue(BracketI18N.fromBracket(input.backVocalsBracket()));
        this.nonLexical.setValue(BracketI18N.fromBracket(input.nonLexicalBracket()));
    }

    /** Bracket enum extended with some I18N content. */
    enum BracketI18N {
        /** No bracket. */
        NONE(null, "form_field.bracket.none"), //
        /** Round brackets also named parenthesis. */
        PARENTHESES(Bracket.ROUND, "form_field.bracket.parentheses"), //
        /** Square brackets. */
        BRACKETS(Bracket.SQUARE, "form_field.bracket.brackets"), //
        /** Curly brackets also named braces. */
        BRACES(Bracket.CURLY, "form_field.bracket.braces"), //
        /** Angle brackets also named chevron. */
        CHEVRONS(Bracket.ANGLE, "form_field.bracket.chevrons"), //
        ;
        private static final BracketI18N[] VALUES = BracketI18N.values();

        private final Bracket bracket;
        private final String i18n;
        private final String pair;

        BracketI18N(
                final Bracket bracket,
                final String i18n
        ) {
            this.bracket = bracket;
            this.i18n = i18n;
            this.pair = (bracket == null ? "   " : bracket.getOpen() + " " + bracket.getClose());
        }

        private static BracketI18N fromBracket(final Bracket bracket) {
            for (final BracketI18N value : VALUES) {
                if (bracket == value.bracket) return value;
            }
            return null;
        }

        private void bind(final StringProperty prop) {
            if (this.bracket == null) I18NMIQ.get().bind(prop, this.i18n);
            else I18NMIQ.get().bind(prop, this.i18n, this.pair);
        }
    }

    /** BracketI18N Cell display object. */
    private static class BracketCell
            extends ListCell<BracketI18N> {

        private final boolean showI18N;

        private BracketCell(final boolean showI18N) {
            this.showI18N = showI18N;
        }

        @Override
        protected void updateItem(
                final BracketI18N bracket,
                final boolean isEmpty
        ) {
            super.updateItem(bracket, isEmpty);
            if (isEmpty) {
                this.textProperty().unbind();
                this.setText(null);
            } else {
                final ObservableList<String> styleClass = this.getStyleClass();
                if (this.showI18N) {
                    bracket.bind(this.textProperty());
                    styleClass.remove(Styles.TEXT_BOLD);
                } else {
                    if (!styleClass.contains(Styles.TEXT_BOLD)) styleClass.add(Styles.TEXT_BOLD);
                    this.textProperty().unbind();
                    this.setText(bracket.pair);
                }
            }
        }
    }

    /** A validator dedicated to delimiter selection. */
    private class Validator
            implements FormFieldValidator<LyricsParsingOptions> {

        private final EnumMap<BracketI18N, List<ComboBox<BracketI18N>>> counter = new EnumMap<>(BracketI18N.class);
        private final List<ComboBox<BracketI18N>> comboBoxes = List.of(FFParsingOptions.this.singer,
                                                                       FFParsingOptions.this.backVocal,
                                                                       FFParsingOptions.this.nonLexical);

        private List<ComboBox<BracketI18N>> newList() {
            return new ArrayList<>(this.comboBoxes.size());
        }

        @Override
        public boolean canContinueAnalysis(
                final CallableList errors,
                final LyricsParsingOptions input
        ) {
            this.counter.forEach((b, l) -> l.clear());
            for (final ComboBox<BracketI18N> cb : this.comboBoxes) {
                final BracketI18N key = cb.getValue() == null ? BracketI18N.NONE : cb.getValue();
                this.counter.computeIfAbsent(key, b -> this.newList()).add(cb);
            }
            this.counter.forEach((b, l) -> l.forEach(
                    c -> c.pseudoClassStateChanged(Styles.STATE_DANGER, l.size() > 1 && b != BracketI18N.NONE)));
            if (!input.isValid()) errors.add(I18NMIQ.get().buildCallable("form_field.options_parser.delimiter.error"));
            return true;
        }
    }
}
