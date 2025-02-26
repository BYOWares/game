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

import atlantafx.base.controls.ToggleSwitch;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import fr.byowares.game.utils.serial.source.Source;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static fr.byowares.game.utils.jfx.i18n.I18NResourceBundle.binder;

/**
 * A Form Field that allow to select of file from disk.
 *
 * @param <N> Type of object whose field must be edited.
 *
 * @since XXX
 */
public class FFFilePicker<N extends NamedSourcedObject>
        extends ComposedFormField<HBox, N, Source> {

    private static final double SPACING = 5.0;

    private final FFFileSelector<N> fileSelector;
    private final FFText<N> fileName;
    private final ToggleSwitch copyFile;
    private final Label copyLabel;

    /**
     * @param setter     The setter method to update the object.
     * @param i18nLabel  The binder for the Label.
     * @param i18nDialog The binder for the open dialog.
     */
    public FFFilePicker(
            final BiConsumer<N, Source> setter,
            final Consumer<StringProperty> i18nLabel,
            final Consumer<StringProperty> i18nDialog
    ) {
        super(new HBox(SPACING), setter);
        this.fileSelector = new FFFileSelector<>(setter, i18nLabel, i18nDialog);

        final I18NMIQ i18n = I18NMIQ.get();
        this.fileName = new FFText<>(ComposedFormField.noOpBiConsumer(), binder(i18n, "wizard.ff.file_name"));
        this.fileName.addValidator(ValidatorNotNull.INSTANCE);
        this.fileName.addValidator(new ValidatorPath());
        this.fileName.addValidator(new ValidatorLength(FFSourceDir.MIN_FILE_LENGTH, FFSourceDir.MAX_FILE_LENGTH));

        this.fileSelector.addFieldTextChangeListener((obs, ov, nv) -> {
            final String fileName = this.fileName.getCurrentInput();
            if (fileName == null || fileName.isBlank() || Objects.equals(fileName, getLastName(ov)))
                this.fileName.init(getLastName(nv));
        });

        final VBox vBox = SimpleFormField.newVBoxContainer();
        this.copyLabel = new Label();
        I18NMIQ.get().bind(this.copyLabel.textProperty(), "wizard.ff.copy");

        this.copyFile = new ToggleSwitch();
        this.copyFile.setSelected(false);
        this.copyFile.setTooltip(new Tooltip());
        I18NMIQ.get().bind(this.copyFile.getTooltip().textProperty(), "wizard.ff.copy_tooltip");
        vBox.getChildren().addAll(this.copyLabel, this.copyFile);

        // Aligning the copy button with the text areas to its left.
        // Spacing needed = ((text.height - button.height) / 2) + ((VBox)text.parent).spacing
        final double textParentSpacing = vBox.getSpacing(); // The same is expected by default
        final ReadOnlyDoubleProperty textHeight = this.fileName.getTextAreaHeightProperty();
        final ReadOnlyDoubleProperty buttonHeight = this.copyFile.heightProperty();
        vBox.spacingProperty().bind(textHeight.subtract(buttonHeight).divide(2).add(textParentSpacing));

        this.addFormField(this.fileSelector);
        this.addFormField(this.fileName);
        this.getRoot().getChildren().add(vBox);

        final ReadOnlyDoubleProperty rootWidth = this.getRoot().widthProperty();
        HBox.setHgrow(this.fileSelector.getRoot(), Priority.ALWAYS);
        HBox.setHgrow(this.fileName.getRoot(), Priority.ALWAYS);
        final var rootTwoThird = rootWidth.multiply(2).divide(3).subtract(SPACING).subtract(vBox.widthProperty());
        this.fileSelector.getRoot().maxWidthProperty().bind(rootTwoThird);
        this.fileName.getRoot().maxWidthProperty().bind(rootWidth.divide(3).subtract(SPACING));
    }

    private static String getLastName(final String input) {
        if (input == null) return "";
        final int lastIndexOf = input.lastIndexOf(File.separator);
        return lastIndexOf < 0 ? input : input.substring(lastIndexOf + 1);
    }

    @Override
    public void init(final Source input) {
        this.fileSelector.init(input);
    }

    @Override
    final void onLevelUpdate(
            final int oldValue,
            final int newValue
    ) {
        final int next = newValue + 1;
        this.fileSelector.setLevel(next);
        this.fileName.setLevel(next);
        SimpleFormField.updateStyleClass(this.copyLabel.getStyleClass(), oldValue, next);
    }

    @Override
    void doSet(
            final BiConsumer<N, Source> setter,
            final N n
    ) {
    }

    @Override
    void runValidatorsOnError() {
        // Nothing to do
    }

    @Override
    void runValidatorOnSuccess() {
        // Nothing to do
    }
}
