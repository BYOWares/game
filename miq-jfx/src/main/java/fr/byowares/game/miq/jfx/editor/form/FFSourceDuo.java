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

import fr.byowares.game.miq.core.model.audio.DuoSource;
import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.utils.serial.source.Source;

import java.util.List;
import java.util.function.BiConsumer;


/**
 * A Form Field that allows to configure a {@link fr.byowares.game.miq.core.model.audio.DuoSource}.
 *
 * @since XXX
 */
public class FFSourceDuo
        extends FFSourceAudio<DuoSource> {

    private final FFFilePicker<Song> voiceFile;
    private final FFFilePicker<Song> musicFile;

    /**
     * A new Form Field to configure a {@link fr.byowares.game.miq.core.model.audio.DuoSource} for a
     * {@link fr.byowares.game.miq.core.model.song.Song}.
     */
    public FFSourceDuo() {
        super(Song::setDuoSource, "wizard.ff.config_voice_music",
              List.of(newPicker("wizard.ff.select_voice_file"), newPicker("wizard.ff.select_music_file")));
        this.voiceFile = this.getFilePicker(0);
        this.musicFile = this.getFilePicker(1);
    }

    @Override
    void doSet(
            final BiConsumer<Song, DuoSource> setter,
            final Song song
    ) {
        if (!this.isSelected()) return;
        final Source parent = song.getSource().getParent();
        setter.accept(song, new DuoSource(this.voiceFile.asSource(parent), this.musicFile.asSource(parent)));
    }

    @Override
    void runValidatorsOnError() {
        // Nothing to do
    }

    @Override
    void runValidatorOnSuccess() {
        // Nothing to do
    }

    @Override
    public void init(final DuoSource input) {
        if (input == null) {
            this.setSelected(false);
            this.voiceFile.init(null);
            this.musicFile.init(null);
        } else {
            this.setSelected(true);
            this.voiceFile.init(input.voiceSource());
            this.musicFile.init(input.musicSource());
        }
    }
}
