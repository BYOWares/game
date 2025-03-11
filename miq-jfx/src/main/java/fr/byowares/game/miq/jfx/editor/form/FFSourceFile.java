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

import fr.byowares.game.miq.jfx.fxml.wizard.WizardItem;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import fr.byowares.game.utils.serial.source.Source;
import fr.byowares.game.utils.serial.source.SourcePath;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.VBox;

import java.io.File;
import java.nio.file.Paths;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static fr.byowares.game.miq.jfx.editor.form.FFSourceDir.MAX_FILE_LENGTH;
import static fr.byowares.game.miq.jfx.editor.form.FFSourceDir.MIN_FILE_LENGTH;

/**
 * Specific Form Field for file source editing.
 *
 * @param <N> Type of object whose field must be edited.
 *
 * @since XXX
 */
public class FFSourceFile<N extends NamedSourcedObject>
        extends ComposedFormField<VBox, N, Source> {

    private static final String YML = ".yml";

    private final FFLabel<N> filePath;
    private final FFText<N> fileName;

    /**
     * @param root         The {@link fr.byowares.game.utils.serial.source.Source}
     * @param setter       The setter method to update the object.
     * @param i18nFilePath The binder for the label of the file path {@link fr.byowares.game.miq.jfx.editor.form.FormField}.
     * @param i18nDirName  The binder for the label of the Directory's name {@link fr.byowares.game.miq.jfx.editor.form.FormField}.
     */
    public FFSourceFile(
            final Source root,
            final BiConsumer<N, Source> setter,
            final Consumer<StringProperty> i18nFilePath,
            final Consumer<StringProperty> i18nDirName
    ) {
        super(SimpleFormField.newVBoxContainer(WizardItem.WIZARD_SPACING), setter, MIN_SIZE_MEDIUM, MIN_SIZE_NONE);
        this.filePath = new FFLabel<>(noOpBiConsumer(), i18nFilePath);

        this.filePath.addValidator(ValidatorNotNull.INSTANCE);
        this.filePath.addValidator(ValidatorNotExistingSource.INSTANCE);

        this.fileName = new FFText<>(noOpBiConsumer(), i18nDirName);
        this.fileName.addFieldTextChangeListener((obs, ov, nv) -> {
            this.filePath.init(root.toString() + File.separator + nv + YML);
        });
        this.fileName.addValidator(new ValidatorLength(MIN_FILE_LENGTH, MAX_FILE_LENGTH - YML.length()));
        this.fileName.addValidator(new ValidatorPath());

        this.registerFormField(this.fileName);
        this.registerFormField(this.filePath);
    }

    @Override
    public void init(final Source input) {
        this.fileName.init(input.getName()); // filePath is bound to dirName, just need to init this one.
    }

    @Override
    final void onLevelUpdate(
            final int oldValue,
            final int newValue
    ) {
        this.filePath.setLevel(newValue);
        this.fileName.setLevel(newValue);
    }

    @Override
    void doSet(
            final BiConsumer<N, Source> setter,
            final N n
    ) {
        setter.accept(n, new SourcePath(Paths.get(this.filePath.getCurrentInput())));
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
