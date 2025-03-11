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

import fr.byowares.game.miq.core.model.Range;
import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.model.lyrics.OptionsLineParser;
import fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse;
import fr.byowares.game.miq.core.option.LyricsParsingOptions;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A Form Field that helps the creation of {@link fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse}.
 *
 * @since XXX
 */
public class FFLyricsCreator
        extends ComposedFormField<VBox, Lyrics, List<TimeCodedVerse>> {

    private static final BiConsumer<Lyrics, List<TimeCodedVerse>> SETTER = //
            (lyrics, list) -> list.forEach(tcv -> lyrics.getVerses().add(tcv));
    private static final Range NO_RANGE = new Range(Long.MAX_VALUE, Long.MAX_VALUE);


    private final Label mainLabel;
    private final FFParsingOptions ffOptions;
    private final Label versesLabel;
    private final List<FFFullVerse> verses;
    private final SimpleIntegerProperty rawTextLengthProperty;
    private final ChangeListener<String> rawTextChangeListener;
    private final SimpleObjectProperty<OptionsLineParser> parserProp;
    private final VBox childrenPane;
    private boolean addToRoot;

    /** Default constructor. */
    public FFLyricsCreator() {
        super(SimpleFormField.newVBoxContainer(), SETTER, MIN_SIZE_HUGE, MIN_SIZE_LARGE);
        this.mainLabel = new Label();
        I18NMIQ.get().bind(this.mainLabel.textProperty(), "form_field.lyrics.configuration");
        this.getRoot().getChildren().add(this.mainLabel);

        this.ffOptions = new FFParsingOptions();
        final LyricsParsingOptions defaultOptions = new LyricsParsingOptions();
        this.ffOptions.init(defaultOptions);
        this.parserProp = new SimpleObjectProperty<>(new OptionsLineParser(defaultOptions));
        this.ffOptions.setApplyHandler(opt -> this.parserProp.setValue(new OptionsLineParser(opt)));

        this.versesLabel = new Label();
        I18NMIQ.get().bind(this.versesLabel.textProperty(), "edit_view.cell.lyrics");
        this.verses = new ArrayList<>();
        this.rawTextLengthProperty = new SimpleIntegerProperty(0);
        this.rawTextChangeListener = (obs, ov, nv) -> this.updateVersesLengthProp();

        this.addToRoot = true;
        this.registerFormField(this.ffOptions);

        this.addToRoot = false;
        this.childrenPane = SimpleFormField.newVBoxContainer();
        HBox.setHgrow(this.childrenPane, Priority.ALWAYS);
        final ScrollPane scrollPane = new ScrollPane(this.childrenPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        this.getRoot().getChildren().addAll(this.versesLabel, scrollPane);
    }

    @Override
    VBox getChildrenPane() {
        return this.addToRoot ? super.getRoot() : this.childrenPane;
    }

    @Override
    void onLevelUpdate(
            final int ov,
            final int nv
    ) {
        SimpleFormField.updateStyleClass(this.mainLabel.getStyleClass(), ov, nv);
        this.ffOptions.setLevel(nv + 1);

        final int oldValue = ov < 0 ? ov : ov + 1; // negative value has special meaning, keeping it.
        SimpleFormField.updateStyleClass(this.versesLabel.getStyleClass(), oldValue, nv + 1);
    }

    @Override
    void doSet(
            final BiConsumer<Lyrics, List<TimeCodedVerse>> setter,
            final Lyrics lyrics
    ) {
        setter.accept(lyrics, this.verses.stream().map(FFFullVerse::getCurrentInput).toList());
    }

    @Override
    void runValidatorsOnError() {
        // Nothing to do.
    }

    @Override
    void runValidatorOnSuccess() {
        // Nothing to do.
    }

    @Override
    public void init(final List<TimeCodedVerse> input) {
        // Shall not be used. Indeed, TimeCodedVerse are the result of parsing the raw text.
    }

    /**
     * @param rawVerses The list of lyrics used ot initialize this Form Field.
     */
    public void specialInit(final List<CharSequence> rawVerses) {
        for (final CharSequence raw : rawVerses) {
            final FFFullVerse verse = this.newVerse();
            verse.init(SimpleFormField.normalizeInput(raw));
            verse.addRawTextChangeListener(this.rawTextChangeListener);
            this.registerFormField(verse);
            this.verses.add(verse);
        }
        this.updateVersesLengthProp();
    }

    private FFFullVerse newVerse() {
        return new FFFullVerse(this.parserProp, this.rawTextLengthProperty, this);
    }

    private void updateVersesLengthProp() {
        this.rawTextLengthProperty.set(
                this.verses.stream().mapToInt(FFFullVerse::getTextPropertyLength).max().orElse(0));
    }

    /**
     * @param refVerse The Verse to use to find where to add a new Verse.
     */
    void createVerseBefore(final FFFullVerse refVerse) {
        final int index = this.verses.indexOf(refVerse);
        final FFFullVerse newVerse = this.newVerse();
        this.verses.add(index, newVerse);
        this.registerFormFieldBefore(newVerse, refVerse);
    }

    /**
     * @param refVerse The Verse to use to find where to add a new Verse.
     */
    void createVerseAfter(final FFFullVerse refVerse) {
        final int index = this.verses.indexOf(refVerse);
        final FFFullVerse newVerse = this.newVerse();
        this.verses.add(index + 1, newVerse);
        this.registerFormFieldAfter(newVerse, refVerse);
    }

    /**
     * @param refVerse The verse to delete.
     */
    void delete(final FFFullVerse refVerse) {
        this.verses.remove(refVerse);
        this.unregisterFormField(refVerse);
    }
}
