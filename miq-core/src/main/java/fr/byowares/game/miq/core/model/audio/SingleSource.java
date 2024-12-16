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
package fr.byowares.game.miq.core.model.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * Description of a song using a single track, which means that vocals and music share the same source file.
 *
 * @since XXX
 */
public class SingleSource
        implements AudioSource {

    private final File audioTrack;

    public SingleSource(final File audioTrack) {
        this.audioTrack = audioTrack;
    }

    @Override
    public AudioPlayer load() {
        try {
            final AudioInputStream ais = AudioSystem.getAudioInputStream(this.audioTrack);
            final Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return new SinglePlayer(clip);

        } catch (final IOException |
                       UnsupportedAudioFileException |
                       LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
