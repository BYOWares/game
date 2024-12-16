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
 * Description of a song using two tracks: one for the vocals and one for the music.
 *
 * @since XXX
 */
public class DuoSource
        implements AudioSource {

    private final File voiceTrack;
    private final File musicTrack;

    /**
     * @param voiceTrack The file containing the voice audio.
     * @param musicTrack The file containing the music audio.
     */
    public DuoSource(
            final File voiceTrack,
            final File musicTrack
    ) {
        this.voiceTrack = voiceTrack;
        this.musicTrack = musicTrack;
    }

    @Override
    public AudioPlayer load() {
        try {
            final AudioInputStream voice = AudioSystem.getAudioInputStream(this.voiceTrack);
            final Clip voiceClip = AudioSystem.getClip();
            voiceClip.open(voice);
            final AudioInputStream music = AudioSystem.getAudioInputStream(this.musicTrack);
            final Clip musicClip = AudioSystem.getClip();
            musicClip.open(music);
            return new DuoPlayer(voiceClip, musicClip);

        } catch (final IOException |
                       UnsupportedAudioFileException |
                       LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
