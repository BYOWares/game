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
package fr.byowares.game.miq.jfx.fxml.wizard;

import fr.byowares.game.miq.core.model.song.Library;
import fr.byowares.game.miq.jfx.editor.form.FFSourceDir;
import fr.byowares.game.miq.jfx.editor.form.FFText;
import fr.byowares.game.miq.jfx.editor.form.ValidatorLength;
import fr.byowares.game.miq.jfx.editor.form.ValidatorNotNull;
import fr.byowares.game.miq.jfx.editor.tree.ItemLibrary;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.serial.source.Source;
import fr.byowares.game.utils.serial.source.SourcePath;

import java.nio.file.Paths;

import static fr.byowares.game.miq.core.model.Constraints.LIB_CMT_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.LIB_CMT_MIN_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.LIB_NAME_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.LIB_NAME_MIN_LENGTH;

/**
 * A Library creation wizard.
 *
 * @since XXX
 */
public class WizardLibrary
        extends WizardItem<Library> {

    private final FFSourceDir<Library> ffSourceDir;
    private final FFText<Library> ffName;
    private final FFText<Library> ffComment;

    /**
     * @param parentDirectory The parent source.
     */
    public WizardLibrary(final Source parentDirectory) {
        super(I18NMIQ.binder(I18NMIQ.get(), "wizard.new.library"), ItemLibrary.getFontIcon());
        this.ffSourceDir = new FFSourceDir<>(parentDirectory, Library::setSource, //
                                             I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.location"),
                                             I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.directory_name"));

        this.ffName = new FFText<>(Library::setName, I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.name"));
        this.ffName.addValidator(ValidatorNotNull.INSTANCE);
        this.ffName.addValidator(new ValidatorLength(LIB_NAME_MIN_LENGTH, LIB_NAME_MAX_LENGTH));

        this.ffComment = new FFText<>(Library::setComment, I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.comment"));
        this.ffComment.addValidator(new ValidatorLength(LIB_CMT_MIN_LENGTH, LIB_CMT_MAX_LENGTH));
    }

    /** FXML handle for initialization. */
    @Override
    void doInitialize() {
        final String newText = this.label.getText();
        this.addFormFieldToSplitPane(this.ffName, newText);
        this.addFormFieldToSplitPane(this.ffComment, null);
        this.addFormFieldToSplitPane(this.ffSourceDir, new SourcePath(Paths.get(newText)));
    }

    @Override
    Library newEmptyObject() {
        return new Library("");
    }

    @Override
    public void onDisplay() {
        // Nothing to do
    }

    @Override
    public void onHide() {
        this.ffName.dispose();
        this.ffComment.dispose();
        this.ffSourceDir.dispose();
    }
}
