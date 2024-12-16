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

import fr.byowares.game.miq.core.model.volume.MasterGainController;
import fr.byowares.game.miq.core.model.volume.SoundController;
import fr.byowares.game.miq.core.model.volume.Volume;
import fr.byowares.game.miq.core.model.volume.VolumeController;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * A player that manipulates one audio: voice and music cannot be controlled separately.
 *
 * @since XXX
 */
public class SinglePlayer
        extends StatusPlayer {

    private final Clip clip;
    private final SoundController soundController;

    /**
     * @param clip The {@link javax.sound.sampled.Clip Clip} for the audio (voice + music).
     */
    public SinglePlayer(final Clip clip) {
        this.clip = clip;
        this.clip.addLineListener(this.listener);
        this.soundController = getSoundControllerFromClip(clip);
    }

    /**
     * @param clip The clip from which the controls are extracted.
     *
     * @return A controller with unified control on mute and volume.
     */
    static SoundController getSoundControllerFromClip(
            final Clip clip
    ) {
        final BooleanControl muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            final FloatControl c = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            return new MasterGainController(c, muteControl);
        } else {
            final FloatControl c = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            return new VolumeController(c, muteControl);
        }
    }

    @Override
    public void close() {
        this.clip.close();
    }

    @Override
    public void doStop() {
        this.clip.stop();
    }

    @Override
    public void doPlay() {
        this.clip.start();
    }

    @Override
    public void doSetPosition(final long positionInMicroseconds) {
        this.clip.setMicrosecondPosition(positionInMicroseconds);
    }

    @Override
    public long getDurationInMicroseconds() {
        return this.clip.getMicrosecondLength();
    }

    @Override
    public long getPosition() {
        return this.clip.getMicrosecondPosition();
    }

    @Override
    public void setVoiceVolume(final Volume target) {
        this.soundController.setVolume(target);
    }

    @Override
    public void setMusicVolume(final Volume target) {
        this.soundController.setVolume(target);
    }

    @Override
    public boolean areVoiceAndMusicSeparated() {
        return false;
    }
}
