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
 * Sound controller using the {@link javax.sound.sampled.FloatControl.Type#MASTER_GAIN MASTER_GAIN} control.
 *
 * @since XXX
 */
public class MasterGainController
        extends MuteController {

    private final FloatControl masterGainControl;
    private final float minimum;
    private final float maximum;

    /**
     * @param masterGainControl The control of the volume in dB of the audio.
     * @param muteControl       The control of the mute status of the audio.
     */
    public MasterGainController(
            final FloatControl masterGainControl,
            final BooleanControl muteControl
    ) {
        super(muteControl);
        this.masterGainControl = masterGainControl;
        this.minimum = masterGainControl.getMinimum();
        this.maximum = masterGainControl.getMaximum();
    }

    @Override
    public void setVolume(final Volume target) {
        // Cf documentation of javax.sound.sampled.FloatControl.Type#MASTER_GAIN
        // linearScalar = pow(10.0, gainDB/ 20.0)
        // Here, we have the linearScalar, we need to compute the gain.
        final float updatedValue = (float) (20d * Math.pow(10d, target.asRatio()));
        final float withinRange = Math.min(Math.max(this.minimum, updatedValue), this.maximum);
        this.masterGainControl.setValue(withinRange);
    }
}
