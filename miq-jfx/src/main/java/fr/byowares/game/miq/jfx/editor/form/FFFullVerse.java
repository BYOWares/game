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
import fr.byowares.game.miq.core.model.Range;
import fr.byowares.game.miq.core.model.lyrics.Line;
import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.model.lyrics.OptionsLineParser;
import fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.jfx.Font;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Graphical representation of a single verse, with the raw text version (editable), and the converted version (not
 * editable, here for consultation only).
 *
 * @since XXX
 */
public class FFFullVerse
        extends SimpleFormField<VBox, Lyrics, String, TimeCodedVerse> {

    private static final Font FONT = Font.JETBRAINS_MONO_NL_MEDIUM;

    private final SimpleObjectProperty<OptionsLineParser> parserProp;
    private final TextField rawText;
    private final FXVerse verse;
    private final ValidatorSettable<TimeCodedVerse> validatorSettable;

    /**
     * @param parserProperty        The {@link fr.byowares.game.miq.core.model.lyrics.OptionsLineParser} to use to convert
     *                              the raw text to the MIQ model.
     * @param rawTextLengthProperty The property in charge of aligning correctly all raw text cells.
     * @param lyricsHandler         The object managing the list of {@link fr.byowares.game.miq.jfx.editor.form.FFFullVerse},
     *                              whose {@code this} belongs to.
     */
    public FFFullVerse(
            final SimpleObjectProperty<OptionsLineParser> parserProperty,
            final SimpleIntegerProperty rawTextLengthProperty,
            final FFLyricsCreator lyricsHandler
    ) {
        super(SimpleFormField.newVBoxContainer(), noOpBiConsumer(), null, MIN_SIZE_HUGE, MIN_SIZE_NONE);

        this.parserProp = parserProperty;
        this.validatorSettable = new ValidatorSettable<>();

        this.rawText = new TextField();
        this.rawText.prefColumnCountProperty().bind(rawTextLengthProperty);
        this.rawText.setStyle(FONT.toFxFont());
        this.rawText.getStyleClass().add(Styles.RIGHT_PILL);
        this.rawText.textProperty().addListener((obs, ov, nv) -> this.update());

        this.verse = new FXVerse(FONT);

        final ChangeListener<OptionsLineParser> listener = (obs, ov, nv) -> this.update();
        this.parserProp.addListener(listener);

        final MenuItem addBefore = new MenuItem();
        final MenuItem addAfter = new MenuItem();
        final MenuItem delete = new MenuItem();
        final I18NMIQ i18n = I18NMIQ.get();
        i18n.bind(addBefore.textProperty(), "form_field.lyrics.add_before");
        i18n.bind(addAfter.textProperty(), "form_field.lyrics.add_after");
        i18n.bind(delete.textProperty(), "form_field.lyrics.delete");
        addBefore.setOnAction(e -> lyricsHandler.createVerseBefore(this));
        addAfter.setOnAction(e -> lyricsHandler.createVerseAfter(this));
        delete.setOnAction(e -> {
            lyricsHandler.delete(this);
            this.parserProp.removeListener(listener);
        });
        final MenuButton lineContext = new MenuButton(null, new FontIcon(BootstrapIcons.GEAR));
        lineContext.getItems().addAll(addBefore, delete, addAfter);
        lineContext.getStyleClass().addAll(Styles.LEFT_PILL, Tweaks.NO_ARROW);
        lineContext.minHeightProperty().bind(this.rawText.heightProperty());

        final HBox rawContainer = new HBox(lineContext, this.rawText);
        final HBox fullVerseContainer = new HBox(10.0, rawContainer, this.verse.getRoot());
        this.getRoot().getChildren().add(fullVerseContainer);
        this.addValidator(this.validatorSettable);
    }

    /** @return The length of the text */
    int getTextPropertyLength() {
        return this.rawText.textProperty().length().get();
    }

    private void update() {
        final OptionsLineParser parser = this.parserProp.get();
        final String text = this.rawText.getText();
        this.validatorSettable.clearErrors();
        try {
            final List<Line> parsed = parser.parse(text);
            this.verse.setLines(parsed);
        } catch (final Exception e) {
            this.verse.setLines(List.of());
            this.validatorSettable.addError(I18NMIQ.get().buildCallable("form_field.lyrics.error", e.getMessage()));
        }
        this.runValidators(null);
    }

    @Override
    TimeCodedVerse getCurrentInput() {
        return new TimeCodedVerse(new Range(0L, 0L), this.verse.getLines());
    }

    @Override
    void doSet(
            final BiConsumer<Lyrics, String> setter,
            final Lyrics lyrics
    ) {
        // Nothing to do. #getCurrentInput is used to get the TimeCodedVerse represented by this object.
    }

    @Override
    void runValidatorsOnError() {
        this.rawText.pseudoClassStateChanged(Styles.STATE_DANGER, true);
    }

    @Override
    void runValidatorOnSuccess() {
        this.rawText.pseudoClassStateChanged(Styles.STATE_DANGER, false);
    }

    @Override
    public void init(final String input) {
        this.rawText.setText(input);
    }

    /**
     * @param rawTextChangeListener The listener to the rawText input change.
     */
    void addRawTextChangeListener(final ChangeListener<String> rawTextChangeListener) {
        this.rawText.textProperty().addListener(rawTextChangeListener);
    }
}
