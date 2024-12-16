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

import fr.byowares.game.miq.core.model.volume.Volume;

/**
 * A music player with extended capabilities.
 * <ul>
 *     <li>Control the voice and music’s volume separately.</li>
 *     <li>Rewind or Fast-forward the time.</li>
 * </ul>
 *
 * @since XXX
 */
public interface AudioPlayer
        extends AutoCloseable {

    /**
     * @return the duration of the audio in microseconds.
     */
    long getDurationInMicroseconds();

    /**
     * Pause the playing of the audio.
     */
    void stop();

    /**
     * Resume the playing of the audio.
     */
    void play();

    /**
     * @return the player’s position in microseconds.
     */
    long getPosition();

    /**
     * Set the player’s position at the given time.
     * <p>
     * <b>Note:</b> once the position is set, the audio will resume play if and only if {@link #shouldBePlaying()}
     * returns {@code true}.
     *
     * @param positionInMicroseconds The new position of the player. If this value is lower than 0 (respectively
     *                               greater than {@link #getDurationInMicroseconds() the audio duration}), it shall
     *                               be set to 0 (resp. to the audio duration).
     */
    void setPosition(long positionInMicroseconds);

    /**
     * Set the player’s position before the current position.
     *
     * @param durationInMicroseconds The time in microseconds used to rewind the audio.
     */
    void moveBackward(long durationInMicroseconds);

    /**
     * Set the player’s position after the current position.
     *
     * @param durationInMicroseconds The time in microseconds used to fast-forward the audio.
     */
    void moveForward(long durationInMicroseconds);

    /**
     * @param target The volume to set for the voice audio.
     */
    void setVoiceVolume(Volume target);

    /**
     * @param target The volume to set for the music audio.
     */
    void setMusicVolume(Volume target);

    /**
     * @return {@code true} if and only if the volume of the voice and of the music can be operated separately,
     * {@code false} otherwise.
     */
    boolean areVoiceAndMusicSeparated();

    /**
     * The difference between this method and {@link #isPlaying()} is the intention.
     * <p>
     * For example, If you play the audio (by calling {@link #play()}), both methods will return {@code true}.<br>
     * However, when the end of the audio is reached, {@link #isPlaying()} will return {@code false} while this
     * method will still return {@code true} (because {@link #stop()} has not been called).
     *
     * @return {@code true} if the audio should be playing, {@code false} otherwise.
     */
    boolean shouldBePlaying();

    /**
     * @return {@code true} if the audio is playing, {@code false} otherwise.
     *
     * @see javax.sound.sampled.LineListener#update(javax.sound.sampled.LineEvent) LineListener#update(LineEvent)
     * @see #shouldBePlaying()
     */
    boolean isPlaying();
}
