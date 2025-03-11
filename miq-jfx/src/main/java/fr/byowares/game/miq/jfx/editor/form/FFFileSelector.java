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
import fr.byowares.game.miq.core.model.audio.SingleSource;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import fr.byowares.game.utils.serial.source.Source;
import fr.byowares.game.utils.serial.source.SourcePath;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.agrona.LangUtil;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static fr.byowares.game.utils.jfx.concurrent.BackgroundTasks.runBlocking;

/**
 * A Form Field that allow to select an audio file from disk.
 *
 * @param <N> Type of object whose field must be edited.
 *
 * @since XXX
 */
public class FFFileSelector<N extends NamedSourcedObject>
        extends SimpleFormField<VBox, N, Source, String> {

    private static final Callable<String> I18N_ALL = I18NMIQ.get().buildCallable("wizard.ff.ext.all");
    private static final Callable<String> I18N_AUDIO = I18NMIQ.get().buildCallable("wizard.ff.ext.audio");

    private static final String ALL = "*.*";
    private static final String[] AUDIO = Stream.of("wav", "au", "mp2", "mp3", "au", "aif", "aiff", "aifc") //
            .sorted() //
            .map(x -> "*." + x) //
            .toArray(String[]::new) //
            ;
    private static final double BUTTON_WIDTH = 25.0;
    private static final Logger log = LoggerFactory.getLogger(FFFileSelector.class);

    private final Label field;
    private final Button bSelect;
    private final Consumer<StringProperty> i18nDialog;
    private final ValidatorSettable<String> validator;

    /**
     * @param setter     The setter method to update the object.
     * @param i18nLabel  The binder for the label of the Form Field.
     * @param i18nDialog The binder for open file dialog title.
     */
    public FFFileSelector(
            final BiConsumer<N, Source> setter,
            final Consumer<StringProperty> i18nLabel,
            final Consumer<StringProperty> i18nDialog
    ) {
        super(SimpleFormField.newVBoxContainer(), setter, i18nLabel, MIN_SIZE_SMALL, MIN_SIZE_NONE);
        this.i18nDialog = i18nDialog;

        this.field = FFLabel.newLabel(this.getRoot());
        // LEFT_PILL prevent the danger from being applied.
        // this.field.getStyleClass().add(Styles.LEFT_PILL);
        this.field.textProperty().addListener((o, ov, nv) -> this.runValidators(nv));

        this.bSelect = new Button(null, new FontIcon(BootstrapIcons.THREE_DOTS_VERTICAL));
        this.bSelect.setOnAction(this::openFileChooser);
        this.bSelect.getStyleClass().addAll(Styles.RIGHT_PILL, Styles.ACCENT);
        this.bSelect.setPrefWidth(BUTTON_WIDTH);
        this.bSelect.setMinWidth(BUTTON_WIDTH);
        this.getRoot().getChildren().add(new HBox(0.0, this.field, this.bSelect));

        this.validator = new ValidatorSettable<>();
        this.addValidator(ValidatorSourceIsFile.INSTANCE);
        this.addValidator(this.validator);
    }

    private void openFileChooser(final ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        this.i18nDialog.accept(fileChooser.titleProperty());

        try {
            final var audioFiles = new FileChooser.ExtensionFilter(I18N_AUDIO.call(), AUDIO);
            final var allFiles = new FileChooser.ExtensionFilter(I18N_ALL.call(), ALL);
            fileChooser.getExtensionFilters().addAll(audioFiles, allFiles);
            // TODO make something configurable.
            fileChooser.setInitialDirectory(Path.of(System.getProperty("user.home"), "Desktop").toFile());
            final File selectedFile = fileChooser.showOpenDialog(this.bSelect.getScene().getWindow());
            if (selectedFile == null) return;

            final I18NMIQ i18n = I18NMIQ.get();
            runBlocking(this.field.getScene(), //
                        I18NMIQ.binder(i18n, "wizard.ff.analyze_file", selectedFile.toString()), //
                        // Actual blocking task
                        (u, c) -> {
                            u.accept(-1L, 1L); // Indeterminate progress
                            this.validator.clearErrors();
                            final var singleSource = new SingleSource(new SourcePath(selectedFile.toPath()));
                            try (final var ignored = singleSource.load()) {
                                // All good, the file could be loaded
                                return null;

                            } catch (final Exception e) {
                                log.info("File selected by user could not be loaded {}", selectedFile, e);
                                final var error = i18n.buildCallable("wizard.ff.analyze_file_error", e.getMessage());
                                this.validator.addError(error);
                                throw e;
                            }
                        }, //
                        n -> this.field.setText(selectedFile.toString()), // On success
                        n -> this.field.setText(selectedFile.toString()), // On failure
                        n -> this.field.setText(null) // On cancel
            );

        } catch (final Exception e) {
            LangUtil.rethrowUnchecked(e);
        }
    }

    @Override
    void doSet(
            final BiConsumer<N, Source> setter,
            final N n
    ) {
        setter.accept(n, new SourcePath(Paths.get(this.getCurrentInput())));
    }

    @Override
    void runValidatorsOnError() {
        this.field.pseudoClassStateChanged(Styles.STATE_DANGER, true);
    }

    @Override
    void runValidatorOnSuccess() {
        this.field.pseudoClassStateChanged(Styles.STATE_DANGER, false);
    }

    @Override
    String getCurrentInput() {
        return this.field.getText();
    }

    @Override
    public void init(final Source input) {
        this.field.setText(SimpleFormField.normalizeInput(input));
    }

    /**
     * Add a {@link javafx.beans.value.ChangeListener} listening to the Text SimpleFormField.
     *
     * @param listener The listener to add.
     */
    void addFieldTextChangeListener(final ChangeListener<String> listener) {
        this.field.textProperty().addListener(listener);
    }
}
