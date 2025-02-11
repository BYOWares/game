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
import fr.byowares.game.miq.core.model.song.Libraries;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import fr.byowares.game.utils.serial.source.Source;
import fr.byowares.game.utils.serial.source.SourcePath;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A Form used to edit a Source. This Form is in fact two forms:
 * <ul>
 *     <li>One to edit the directory name.</li>
 *     <li>One to show the actual path of the source file.</li>
 * </ul>
 *
 * @param <N> Type of object whose field must be edited.
 *
 * @since XXX
 */
public class FFSourceDir<N extends NamedSourcedObject>
        extends FormField<N, Source, String> {

    private static final int MAX_DIR_LENGTH = 255;

    private final TextField field;
    private final FFText<N> ffDirName;

    /**
     * @param root          The {@link fr.byowares.game.utils.serial.source.Source}
     * @param setter        The setter method to update the object.
     * @param i18nBinder    The binder for the label of this Form Field.
     * @param i18nBinderDir The binder for the label of the Directory's name Form Field (see {@link #getFFDir()}).
     */
    public FFSourceDir(
            final Source root,
            final BiConsumer<N, Source> setter,
            final Consumer<StringProperty> i18nBinder,
            final Consumer<StringProperty> i18nBinderDir
    ) {
        super(setter, i18nBinder);
        this.field = new TextField(IMPROBABLE_INIT_VALUE);
        this.field.setDisable(true);
        this.field.textProperty().addListener((o, ov, nv) -> {
            this.runValidators(nv);
        });
        this.getFFContainer().getChildren().add(this.field);

        this.ffDirName = new FFText<>((n, c) -> {}, i18nBinderDir);
        this.ffDirName.addFieldTextChangeListener((obs, ov, nv) -> {
            this.field.setText(root.toString() + File.separator + nv + File.separator + Libraries.MIQ_FILE_NAME);
        });
        this.ffDirName.addValidators(List.of(new ValidatorLength(1, MAX_DIR_LENGTH), new ValidatorPath()));
    }

    /**
     * @return The {@link fr.byowares.game.miq.jfx.editor.form.FormField} in charge of the directory name.
     */
    public FFText<N> getFFDir() {
        return this.ffDirName;
    }

    @Override
    String getCurrentInput() {
        return this.field.getText();
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
    void doSet(
            final BiConsumer<N, Source> setter,
            final N n
    ) {
        setter.accept(n, new SourcePath(Paths.get(this.getCurrentInput())));
    }

    @Override
    void doInit(final String input) {
        // Nothing to do, the #ffDirName is expected to be initialized.
    }
}
