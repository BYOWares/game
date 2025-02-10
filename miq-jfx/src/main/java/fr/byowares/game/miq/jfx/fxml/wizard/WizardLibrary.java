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
import fr.byowares.game.miq.jfx.editor.form.ValidatorSource;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.serial.source.Source;
import javafx.stage.Stage;

import java.util.List;

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

    private static final double WINDOW_MIN_WIDTH = 700.0;
    private static final double WINDOW_MIN_HEIGHT = 650.0;


    private final FFSourceDir<Library> ffSourceDir;
    private final FFText<Library> ffName;
    private final FFText<Library> ffComment;

    /**
     * @param parent The parent source.
     */
    public WizardLibrary(final Source parent) {
        super(I18NMIQ.binder(I18NMIQ.get(), "wizard.library"));
        this.ffSourceDir = new FFSourceDir<>(parent, Library::setSource,
                                             I18NMIQ.binder(I18NMIQ.get(), "wizard.library.location"),
                                             I18NMIQ.binder(I18NMIQ.get(), "wizard.library.directory_name"));
        this.ffSourceDir.addValidators(List.of(ValidatorNotNull.INSTANCE, ValidatorSource.INSTANCE));

        this.ffName = new FFText<>(Library::setName, I18NMIQ.binder(I18NMIQ.get(), "wizard.library.name"));
        this.ffName.addValidators(
                List.of(ValidatorNotNull.INSTANCE, new ValidatorLength(LIB_NAME_MIN_LENGTH, LIB_NAME_MAX_LENGTH)));

        this.ffComment = new FFText<>(Library::setComment, I18NMIQ.binder(I18NMIQ.get(), "wizard.library.comment"));
        this.ffComment.addValidators(List.of(new ValidatorLength(LIB_CMT_MIN_LENGTH, LIB_CMT_MAX_LENGTH)));
    }

    @Override
    void configureStageSize(final Stage newStage) {
        newStage.setMinHeight(WINDOW_MIN_HEIGHT);
        newStage.setMinWidth(WINDOW_MIN_WIDTH);
    }

    /** FXML handle for initialization. */
    @Override
    void doInitialize() {
        final String newText = this.label.getText();
        this.addFormFieldToSplitPane(this.ffName, newText);
        this.addFormFieldToSplitPane(this.ffComment, "");
        this.addFormFieldToSplitPane(this.ffSourceDir.getFFDir(), newText);
        this.addFormFieldToSplitPane(this.ffSourceDir, null);
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
        // Nothing to do
    }
}
