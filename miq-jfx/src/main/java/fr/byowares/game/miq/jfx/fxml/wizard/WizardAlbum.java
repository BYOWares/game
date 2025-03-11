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
import fr.byowares.game.miq.jfx.editor.form.FFMLText;
import fr.byowares.game.miq.jfx.editor.form.FFSourceDir;
import fr.byowares.game.miq.jfx.editor.form.FFText;
import fr.byowares.game.miq.jfx.editor.form.FFTextSuggestion;
import fr.byowares.game.miq.jfx.editor.form.ValidatorLength;
import fr.byowares.game.miq.jfx.editor.form.ValidatorNotNull;
import fr.byowares.game.miq.jfx.editor.tree.CachedData;
import fr.byowares.game.miq.jfx.editor.tree.ItemAlbum;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.serial.source.Source;
import fr.byowares.game.utils.serial.source.SourcePath;

import java.nio.file.Paths;

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

    private final FFSourceDir<Album> ffSourceDir;
    private final FFText<Album> ffName;
    private final FFText<Album> ffComment;
    private final FFTextSuggestion<Album> ffArtist;
    private final FFMLText<Album> ffCopyright;

    /**
     * @param parentDirectory The directory to contain the Album to create.
     * @param cachedData      The data cached (to be used in Combo Box to help filling some fields).
     */
    public WizardAlbum(
            final Source parentDirectory,
            final CachedData cachedData
    ) {
        super(I18NMIQ.binder(I18NMIQ.get(), "wizard.new.album"), ItemAlbum.getFontIcon());
        this.ffSourceDir = new FFSourceDir<>(parentDirectory, Album::setSource, //
                                             I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.location"),
                                             I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.directory_name"));

        this.ffName = new FFText<>(Album::setName, I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.name"));
        this.ffName.addValidator(ValidatorNotNull.INSTANCE);
        this.ffName.addValidator(new ValidatorLength(ALBUM_TITLE_MIN_LENGTH, ALBUM_TITLE_MAX_LENGTH));

        this.ffComment = new FFText<>(Album::setComment, I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.comment"));
        this.ffComment.addValidator(new ValidatorLength(ALBUM_CMT_MIN_LENGTH, ALBUM_CMT_MAX_LENGTH));

        this.ffArtist = new FFTextSuggestion<>(Album::setArtist, I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.artist"),
                                               cachedData.getArtistNames());
        this.ffArtist.addValidator(new ValidatorLength(ARTIST_MIN_LENGTH, ARTIST_MAX_LENGTH));

        this.ffCopyright = new FFMLText<>(400.0, Album::setCopyright,
                                          I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.copyright"));
        this.ffCopyright.addValidator(ValidatorNotNull.INSTANCE);
        this.ffCopyright.addValidator(new ValidatorLength(COPYRIGHT_MIN_LENGTH, COPYRIGHT_MAX_LENGTH));
    }

    /** FXML handle for initialization. */
    @Override
    void doInitialize() {
        final String newText = this.label.getText();
        this.addFormFieldToSplitPane(this.ffName, newText);
        this.addFormFieldToSplitPane(this.ffComment, null);
        this.addFormFieldToSplitPane(this.ffArtist, null);
        this.addFormFieldToSplitPane(this.ffSourceDir, new SourcePath(Paths.get(newText)));
        this.addFormFieldToSplitPane(1, this.ffCopyright, null);
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
