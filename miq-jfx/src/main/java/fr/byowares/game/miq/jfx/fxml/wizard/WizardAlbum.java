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

import fr.byowares.game.miq.core.model.song.Album;
import fr.byowares.game.miq.jfx.editor.form.FFCombo;
import fr.byowares.game.miq.jfx.editor.form.FFMLText;
import fr.byowares.game.miq.jfx.editor.form.FFSourceDir;
import fr.byowares.game.miq.jfx.editor.form.FFText;
import fr.byowares.game.miq.jfx.editor.form.ValidatorLength;
import fr.byowares.game.miq.jfx.editor.form.ValidatorNotNull;
import fr.byowares.game.miq.jfx.editor.form.ValidatorSource;
import fr.byowares.game.miq.jfx.editor.tree.CachedData;
import fr.byowares.game.miq.jfx.editor.tree.ItemAlbum;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.serial.source.Source;
import javafx.stage.Stage;

import static fr.byowares.game.miq.core.model.Constraints.ALBUM_CMT_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.ALBUM_CMT_MIN_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.ALBUM_TITLE_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.ALBUM_TITLE_MIN_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.ARTIST_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.ARTIST_MIN_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.COPYRIGHT_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.COPYRIGHT_MIN_LENGTH;

/**
 * An Album creation wizard.
 *
 * @since XXX
 */
public class WizardAlbum
        extends WizardItem<Album> {

    private static final double WINDOW_MIN_WIDTH = 700.0 * 2.0;
    private static final double WINDOW_MIN_HEIGHT = 650.0;


    private final FFSourceDir<Album> ffSourceDir;
    private final FFText<Album> ffName;
    private final FFText<Album> ffComment;
    private final FFCombo<Album> ffArtist;
    private final FFMLText<Album> ffCopyright;

    /**
     * @param parentDirectory The directory to contain the Album to create.
     * @param cachedData      The data cached (to be used in Combo Box to help filling some fields).
     */
    public WizardAlbum(
            final Source parentDirectory,
            final CachedData cachedData
    ) {
        super(I18NMIQ.binder(I18NMIQ.get(), "wizard.album"), ItemAlbum.getFontIcon());
        this.ffSourceDir = new FFSourceDir<>(parentDirectory, Album::setSource, //
                                             I18NMIQ.binder(I18NMIQ.get(), "wizard.location"),
                                             I18NMIQ.binder(I18NMIQ.get(), "wizard.directory_name"));
        this.ffSourceDir.addValidator(ValidatorNotNull.INSTANCE);
        this.ffSourceDir.addValidator(ValidatorSource.INSTANCE);

        this.ffName = new FFText<>(Album::setName, I18NMIQ.binder(I18NMIQ.get(), "wizard.name"));
        this.ffName.addValidator(ValidatorNotNull.INSTANCE);
        this.ffName.addValidator(new ValidatorLength(ALBUM_TITLE_MIN_LENGTH, ALBUM_TITLE_MAX_LENGTH));

        this.ffComment = new FFText<>(Album::setComment, I18NMIQ.binder(I18NMIQ.get(), "wizard.comment"));
        this.ffComment.addValidator(new ValidatorLength(ALBUM_CMT_MIN_LENGTH, ALBUM_CMT_MAX_LENGTH));

        this.ffArtist = new FFCombo<>(Album::setArtist, I18NMIQ.binder(I18NMIQ.get(), "wizard.artist"),
                                      cachedData.getArtistNames());
        this.ffArtist.addValidator(new ValidatorLength(ARTIST_MIN_LENGTH, ARTIST_MAX_LENGTH));

        this.ffCopyright = new FFMLText<>(400.0, Album::setCopyright,
                                          I18NMIQ.binder(I18NMIQ.get(), "wizard" + ".copyright"));
        this.ffCopyright.addValidator(ValidatorNotNull.INSTANCE);
        this.ffCopyright.addValidator(new ValidatorLength(COPYRIGHT_MIN_LENGTH, COPYRIGHT_MAX_LENGTH));
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
        this.addFormFieldToSplitPane(this.ffArtist, "");
        this.addFormFieldToSplitPane(this.ffSourceDir.getFFDir(), newText);
        this.addFormFieldToSplitPane(this.ffSourceDir, null);
        this.addFormFieldToSplitPane(1, this.ffCopyright, "");
    }

    @Override
    Album newEmptyObject() {
        return new Album("");
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
