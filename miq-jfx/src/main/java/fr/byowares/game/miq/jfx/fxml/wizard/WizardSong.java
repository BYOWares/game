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
import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.miq.jfx.editor.form.FFFileSelector;
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
import javafx.stage.Stage;

import java.nio.file.Paths;

import static fr.byowares.game.miq.core.model.Constraints.ALBUM_TITLE_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.ALBUM_TITLE_MIN_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.ARTIST_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.ARTIST_MIN_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.COPYRIGHT_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.COPYRIGHT_MIN_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.RAW_LYRICS_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.RAW_LYRICS_MIN_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.SONG_CMT_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.SONG_CMT_MIN_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.SONG_TITLE_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.SONG_TITLE_MIN_LENGTH;
import static fr.byowares.game.utils.jfx.i18n.I18NResourceBundle.binder;

/**
 * An Song creation wizard.
 *
 * @since XXX
 */
public class WizardSong
        extends WizardItem<Song> {

    private static final double WINDOW_MIN_WIDTH = 700.0 * 2.0;
    private static final double WINDOW_MIN_HEIGHT = 650.0;


    private final Album album;

    private final FFSourceDir<Song> ffSourceDir;
    private final FFText<Song> ffName;
    private final FFText<Song> ffComment;
    private final FFTextSuggestion<Song> ffAlbum;
    private final FFTextSuggestion<Song> ffArtist;
    private final FFMLText<Song> ffCopyright;
    private final FFMLText<Song> ffLyrics;

    /**
     * @param album           The album in which the song is to be created defined.
     * @param parentDirectory The directory to contain the Album to create.
     * @param cachedData      The data cached (to be used in Combo Box to help filling some fields).
     */
    public WizardSong(
            final Album album,
            final Source parentDirectory,
            final CachedData cachedData
    ) {
        super(binder(I18NMIQ.get(), "wizard.new.song"), ItemAlbum.getFontIcon());
        final I18NMIQ i18n = I18NMIQ.get();
        this.album = album;

        this.ffSourceDir = new FFSourceDir<>(parentDirectory, Song::setSource, //
                                             binder(i18n, "wizard.ff.location"),
                                             binder(i18n, "wizard.ff.directory_name"));

        this.ffName = new FFText<>(Song::setName, binder(i18n, "wizard.ff.title"));
        this.ffName.addValidator(ValidatorNotNull.INSTANCE);
        this.ffName.addValidator(new ValidatorLength(SONG_TITLE_MIN_LENGTH, SONG_TITLE_MAX_LENGTH));

        this.ffComment = new FFText<>(Song::setComment, binder(i18n, "wizard.ff.comment"));
        this.ffComment.addValidator(new ValidatorLength(SONG_CMT_MIN_LENGTH, SONG_CMT_MAX_LENGTH));

        this.ffAlbum = new FFTextSuggestion<>(Song::setAlbumName, binder(i18n, "wizard.ff.album"),
                                              cachedData.getAlbumNames());
        this.ffAlbum.addValidator(ValidatorNotNull.INSTANCE);
        this.ffAlbum.addValidator(new ValidatorLength(ALBUM_TITLE_MIN_LENGTH, ALBUM_TITLE_MAX_LENGTH));

        this.ffArtist = new FFTextSuggestion<>(Song::setArtist, binder(i18n, "wizard.ff.artist"),
                                               cachedData.getArtistNames());
        this.ffArtist.addValidator(ValidatorNotNull.INSTANCE);
        this.ffArtist.addValidator(new ValidatorLength(ARTIST_MIN_LENGTH, ARTIST_MAX_LENGTH));

        this.ffCopyright = new FFMLText<>(200.0, Song::setCopyright, binder(i18n, "wizard.ff.copyright"));
        this.ffCopyright.addValidator(ValidatorNotNull.INSTANCE);
        this.ffCopyright.addValidator(new ValidatorLength(COPYRIGHT_MIN_LENGTH, COPYRIGHT_MAX_LENGTH));

        this.ffLyrics = new FFMLText<>(600.0, Song::setCopyright, binder(i18n, "wizard.ff.lyrics"));
        this.ffLyrics.addValidator(ValidatorNotNull.INSTANCE);
        this.ffLyrics.addValidator(new ValidatorLength(RAW_LYRICS_MIN_LENGTH, RAW_LYRICS_MAX_LENGTH));
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
        this.addFormFieldToSplitPane(this.ffComment, null);
        this.addFormFieldToSplitPane(this.ffArtist, this.album.getArtist());
        this.addFormFieldToSplitPane(this.ffAlbum, this.album.getName());
        this.addFormFieldToSplitPane(this.ffSourceDir, new SourcePath(Paths.get(newText)));

        final FFFileSelector<Song> fffs = new FFFileSelector<>(null, binder(I18NMIQ.get(), "wizard.ff.location"),
                                                               binder(I18NMIQ.get(), "wizard.ff.select_audio_file"));
        this.addFormFieldToSplitPane(fffs, null);

        this.addFormFieldToSplitPane(1, this.ffCopyright, this.album.getCopyright());
        this.addFormFieldToSplitPane(1, this.ffLyrics, null);
    }

    @Override
    Song newEmptyObject() {
        return new Song("");
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
