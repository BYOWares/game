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

import fr.byowares.game.miq.core.model.song.Libraries;
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

/**
 * Specific Form Field for directory source editing with a
 * {@link fr.byowares.game.miq.core.model.song.Libraries#MIQ_FILE_NAME} at its root to describe a new object.
 *
 * @param <N> Type of object whose field must be edited.
 *
 * @since XXX
 */
public class FFSourceDir<N extends NamedSourcedObject>
        extends ComposedFormField<VBox, N, Source> {

    private static final int MIN_DIR_LENGTH = 1;
    private static final int MAX_DIR_LENGTH = 255;

    private final FFLabel<N> filePath;
    private final FFText<N> dirName;

    /**
     * @param root         The {@link fr.byowares.game.utils.serial.source.Source}
     * @param setter       The setter method to update the object.
     * @param i18nFilePath The binder for the label of the file path {@link fr.byowares.game.miq.jfx.editor.form.FormField}.
     * @param i18nDirName  The binder for the label of the Directory's name {@link fr.byowares.game.miq.jfx.editor.form.FormField}.
     */
    public FFSourceDir(
            final Source root,
            final BiConsumer<N, Source> setter,
            final Consumer<StringProperty> i18nFilePath,
            final Consumer<StringProperty> i18nDirName
    ) {
        super(SimpleFormField.newVBoxContainer(WizardItem.WIZARD_SPACING), setter);
        this.filePath = new FFLabel<>(noOpBiConsumer(), i18nFilePath);

        this.filePath.addValidator(ValidatorNotNull.INSTANCE);
        this.filePath.addValidator(ValidatorSource.INSTANCE);

        this.dirName = new FFText<>(noOpBiConsumer(), i18nDirName);
        this.dirName.addFieldTextChangeListener((obs, ov, nv) -> {
            this.filePath.init(root.toString() + File.separator + nv + File.separator + Libraries.MIQ_FILE_NAME);
        });
        this.dirName.addValidator(new ValidatorLength(MIN_DIR_LENGTH, MAX_DIR_LENGTH));
        this.dirName.addValidator(new ValidatorPath());

        this.addFormField(this.dirName);
        this.addFormField(this.filePath);
    }

    @Override
    public void init(final Source input) {
        this.dirName.init(input.getName()); // filePath is bound to dirName, just need to init this one.
    }


    @Override
    void doSet(
            final BiConsumer<N, Source> setter,
            final N n
    ) {
        setter.accept(n, new SourcePath(Paths.get(this.filePath.getCurrentInput())));
    }
}
