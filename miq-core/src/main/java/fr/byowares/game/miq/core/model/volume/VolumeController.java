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
package fr.byowares.game.miq.core.model.volume;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;

/**
 * Sound controller using the {@link javax.sound.sampled.FloatControl.Type#VOLUME VOLUME} control.
 *
 * @since XXX
 */
public class VolumeController
        extends MuteController {

    private final FloatControl volumeControl;
    private final float minimum;
    private final float maximum;

    /**
     * @param volumeControl The control of the volume in ratio of the audio.
     * @param muteControl   The control of the mute status of the audio.
     */
    public VolumeController(
            final FloatControl volumeControl,
            final BooleanControl muteControl
    ) {
        super(muteControl);
        this.volumeControl = volumeControl;
        this.minimum = this.volumeControl.getMinimum();
        this.maximum = this.volumeControl.getMaximum();
    }

    @Override
    public void setVolume(final Volume target) {
        final float updatedValue = (float) target.asRatio();
        final float withinRange = Math.min(Math.max(this.minimum, updatedValue), this.maximum);
        this.volumeControl.setValue(withinRange);
    }
}
