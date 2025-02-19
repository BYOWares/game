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

import fr.byowares.game.utils.serial.source.Source;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Description of a song using a single track, which means that vocals and music share the same source.
 *
 * @param audioSource The source containing both voice & music audios.
 *
 * @since XXX
 */
public record SingleSource(Source audioSource)
        implements AudioSource {

    private static final float SAMPLE_RATE = 44100.0f;
    private static final int SAMPLE_SIZE_IN_BITS = 16;

    private static AudioFormat getCDQualityFormat(final AudioFormat origin) {
        return new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, origin.getChannels(), true, origin.isBigEndian());
    }

    /**
     * @param source The source of the data.
     *
     * @return The clip containing the audio data contained in the {@code source}, in a readable format.
     *
     * @throws IOException                   If an I/ O exception occurs.
     * @throws LineUnavailableException      If the line cannot be opened due to resource restrictions.
     * @throws UnsupportedAudioFileException If the stream does not point to valid audio file data recognized by the
     *                                       system.
     */
    static Clip getClip(final Source source)
            throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        final AudioInputStream ais = AudioSystem.getAudioInputStream(source.load());
        final AudioFormat format = ais.getFormat();
        final AudioFormat.Encoding encoding = format.getEncoding();

        final Clip clip = AudioSystem.getClip();
        if (encoding == AudioFormat.Encoding.PCM_SIGNED || encoding == AudioFormat.Encoding.PCM_UNSIGNED)
            clip.open(ais);
        else clip.open(AudioSystem.getAudioInputStream(getCDQualityFormat(format), ais));
        return clip;
    }

    @Override
    public AudioPlayer load() {
        try {
            return new SinglePlayer(getClip(this.audioSource));

        } catch (final IOException |
                       UnsupportedAudioFileException |
                       LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
