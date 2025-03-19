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

import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.miq.jfx.editor.form.FFLyricsCreator;
import fr.byowares.game.miq.jfx.editor.form.FFSourceFile;
import fr.byowares.game.miq.jfx.editor.form.FFText;
import fr.byowares.game.miq.jfx.editor.form.ValidatorLength;
import fr.byowares.game.miq.jfx.editor.form.ValidatorNotNull;
import fr.byowares.game.miq.jfx.editor.tree.ItemLyrics;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.serial.source.Source;
import fr.byowares.game.utils.serial.source.SourcePath;

import java.nio.file.Paths;
import java.util.ArrayList;

import static fr.byowares.game.miq.core.model.Constraints.LYRICS_CMT_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.LYRICS_CMT_MIN_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.LYRICS_NAME_MAX_LENGTH;
import static fr.byowares.game.miq.core.model.Constraints.LYRICS_NAME_MIN_LENGTH;

/**
 * A wizard to create a new Lyrics object.
 *
 * @since XXX
 */
public class WizardLyrics
        extends WizardItem<Lyrics> {

    private final Song song;
    private final FFText<Lyrics> ffName;
    private final FFSourceFile<Lyrics> ffSource;
    private final FFText<Lyrics> ffComment;

    private final FFLyricsCreator ffLyricsCreator;

    /**
     * @param song         The song for which new Lyrics must be created.
     * @param parentSource The parent source.
     */
    public WizardLyrics(
            final Song song,
            final Source parentSource
    ) {
        super(I18NMIQ.binder(I18NMIQ.get(), "wizard.new.lyrics"), ItemLyrics.getFontIcon());
        this.song = song;

        this.ffSource = new FFSourceFile<>(parentSource, Lyrics::setSource, //
                                           I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.location"),
                                           I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.file_name"));

        this.ffName = new FFText<>(Lyrics::setName, I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.name"));
        this.ffName.addValidator(ValidatorNotNull.INSTANCE);
        this.ffName.addValidator(new ValidatorLength(LYRICS_NAME_MIN_LENGTH, LYRICS_NAME_MAX_LENGTH));

        this.ffComment = new FFText<>(Lyrics::setComment, I18NMIQ.binder(I18NMIQ.get(), "wizard.ff.comment"));
        this.ffComment.addValidator(new ValidatorLength(LYRICS_CMT_MIN_LENGTH, LYRICS_CMT_MAX_LENGTH));

        this.ffLyricsCreator = new FFLyricsCreator(song);
    }

    @Override
    void doInitialize() {
        final String newText = this.label.getText();
        this.addFormFieldToSplitPane(this.ffName, newText);
        this.addFormFieldToSplitPane(this.ffComment, null);
        this.addFormFieldToSplitPane(this.ffSource, new SourcePath(Paths.get(newText)));
        this.addFormFieldToSplitPane(1, this.ffLyricsCreator, null);
        this.ffLyricsCreator.specialInit(this.song);
    }

    @Override
    Lyrics newEmptyObject() {
        return new Lyrics("", new ArrayList<>());
    }

    @Override
    public void onDisplay() {
        // Nothing to do
    }

    @Override
    public void onHide() {
        this.ffName.dispose();
        this.ffComment.dispose();
        this.ffSource.dispose();
        this.ffLyricsCreator.dispose();
    }
}
