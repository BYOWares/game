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

import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.utils.serial.source.Source;

import java.util.List;
import java.util.function.BiConsumer;


/**
 * A Form Field that allows to configure a single audio source.
 *
 * @since XXX
 */
public class FFSourceSingle
        extends FFSourceAudio<Source> {

    private final FFFilePicker<Song> audioFile;

    /** Default instance. */
    public FFSourceSingle() {
        super(noOpBiConsumer(), "wizard.ff.config_audio",
              List.of(newPicker(Song::setAudioSource, "wizard.ff.select_audio_file")));
        this.audioFile = this.getFilePicker(0);
    }

    @Override
    void doSet(
            final BiConsumer<Song, Source> setter,
            final Song song
    ) {
        if (!this.isSelected()) return;
        this.audioFile.set(song);
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
    public void init(final Source input) {
        if (input == null) {
            this.setSelected(false);
            this.audioFile.init(null);
        } else {
            this.setSelected(true);
            this.audioFile.init(input);
        }
    }
}
