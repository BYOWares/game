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

/**
 * Object representing volume’s level allowed within the application, instead of using numerical values directly.
 * This object represent a linear scaling volume.<p/>
 * Minimum authorized value is {@link #MIN_VOLUME_INT}, and maximum is {@link #MAX_VOLUME_INT}. The minimal variation
 * is {@link #STEP}.<p/>
 * 100 is the nominal value for no gain in dB.
 *
 * @since XXX
 */
public class Volume {

    /** Minimum volume level to set in the application. */
    public static final int MIN_VOLUME_INT = 0;
    /** Maximum volume level to set in the application. */
    public static final int MAX_VOLUME_INT = 100;
    /** Minimal variation of volume allowed in the application */
    public static final int STEP = 1;

    private static final int CARDINALITY = ((MAX_VOLUME_INT - MIN_VOLUME_INT) / STEP) + 1;
    private static final Volume[] VALUES;
    private static final double NO_GAIN_VALUE = 100d;

    static {
        VALUES = new Volume[CARDINALITY];
        for (int i = MIN_VOLUME_INT; i <= MAX_VOLUME_INT; i += STEP) {
            VALUES[(i - MIN_VOLUME_INT) / STEP] = new Volume(i);
        }
    }

    private final int value;

    private Volume(final int value) {
        this.value = value;
    }

    /**
     * @param target the desired volume ({@link #MIN_VOLUME_INT} <= {@code target} <= {@link #MAX_VOLUME_INT})
     *
     * @return the volume represented as an object
     */
    public static Volume of(final int target) {
        if (target < MIN_VOLUME_INT) throw new IllegalArgumentException(
                "Target value must be higher than minimal value (target=" + target + " < " + MIN_VOLUME_INT + "=MIN_VOLUME_INT)");
        if (target > MAX_VOLUME_INT) throw new IllegalArgumentException(
                "Target value must be lower than maximal value (target=" + target + " > " + MAX_VOLUME_INT + "=MAX_VOLUME_INT)");
        return VALUES[(target - MIN_VOLUME_INT) / STEP];
    }

    /**
     * @return the ratio of the current volume compared to the one with no gain in dB (hence a value of 1 means no
     * gain in dB).
     */
    public double asRatio() {
        return (double) this.value / NO_GAIN_VALUE;
    }
}
