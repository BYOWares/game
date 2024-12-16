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

import fr.byowares.game.miq.core.model.volume.SoundController;
import fr.byowares.game.miq.core.model.volume.Volume;

import javax.sound.sampled.Clip;

/**
 * A player that manipulates two audio: one for the voice and one for the music.
 *
 * @since XXX
 */
public class DuoPlayer
        extends StatusPlayer {

    private final Clip voiceClip;
    private final Clip musicClip;
    private final long durationInMicro;
    private final SoundController voiceSoundController;
    private final SoundController musicSoundController;

    /**
     * @param voiceClip The {@link javax.sound.sampled.Clip Clip} for the voice.
     * @param musicClip The {@link javax.sound.sampled.Clip Clip} for the music.
     */
    public DuoPlayer(
            final Clip voiceClip,
            final Clip musicClip
    ) {
        super();

        this.voiceClip = voiceClip;
        this.voiceClip.addLineListener(this.listener);
        this.voiceSoundController = SinglePlayer.getSoundControllerFromClip(voiceClip);

        this.musicClip = musicClip;
        this.musicClip.addLineListener(this.listener);
        this.musicSoundController = SinglePlayer.getSoundControllerFromClip(musicClip);

        this.durationInMicro = Math.min(voiceClip.getMicrosecondLength(), musicClip.getMicrosecondLength());
    }

    @Override
    public void close() {
        this.voiceClip.close();
        this.musicClip.close();
    }

    @Override
    public void doStop() {
        this.voiceClip.stop();
        this.musicClip.stop();
    }

    @Override
    public void doPlay() {
        this.voiceClip.start();
        this.musicClip.start();
    }

    @Override
    public void doSetPosition(final long positionInMicroseconds) {
        this.voiceClip.setMicrosecondPosition(positionInMicroseconds);
        this.musicClip.setMicrosecondPosition(positionInMicroseconds);
    }

    @Override
    public long getDurationInMicroseconds() {
        return this.durationInMicro;
    }

    @Override
    public long getPosition() {
        return this.voiceClip.getMicrosecondPosition();
    }

    @Override
    public void setVoiceVolume(final Volume target) {
        this.voiceSoundController.setVolume(target);
    }

    @Override
    public void setMusicVolume(final Volume target) {
        this.musicSoundController.setVolume(target);
    }

    @Override
    public boolean areVoiceAndMusicSeparated() {
        return true;
    }
}
