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
package fr.byowares.game.miq.jfx.audio;

import fr.byowares.game.utils.serial.source.Source;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * A music player with extended capabilities.
 * <ul>
 *     <li>Control the voice and music volume separately.</li>
 *     <li>Rewind or Fast-forward the time.</li>
 * </ul>
 *
 * @since XXX
 */
public class AudioPlayer {
    private final Media voiceMedia;
    private final MediaPlayer voicePlayer;
    private final MediaPlayer musicPlayer;
    private final ChangeListener<Duration> listener;

    /**
     * @param slider      The slider used to control the time of the player.
     * @param voiceSource The source for the voice (must not be {@code null}).
     * @param musicSource The source for the music (can be {@code null}; in such case, method
     *                    {@link #setMusicMute(boolean)} and {@link #setMusicVolume(double)} will impact the voice track).
     */
    public AudioPlayer(
            final Slider slider,
            final Source voiceSource,
            final Source musicSource
    ) {
        if (musicSource == null) this.musicPlayer = null;
        else {
            final Media music = new Media(musicSource.toURI().toString());
            this.musicPlayer = new MediaPlayer(music);
            this.musicPlayer.setAutoPlay(false);
            // TODO add a setOnReady method ?
        }

        this.voiceMedia = new Media(voiceSource.toURI().toString());
        this.voicePlayer = new MediaPlayer(this.voiceMedia);
        this.listener = (obs, ov, nv) -> slider.setValue(nv.toSeconds());
        this.voicePlayer.setOnReady(() -> {
            slider.setMin(0d);
            slider.setMax(this.voicePlayer.getTotalDuration().toSeconds());
            this.voicePlayer.currentTimeProperty().addListener(this.listener);
            slider.setValue(0d);
        });
        this.voicePlayer.setAutoPlay(false);
    }

    private Duration clampDuration(final Duration d) {
        if (d.lessThan(Duration.ZERO)) return Duration.ZERO;
        if (d.greaterThan(this.getDuration())) return this.getDuration();
        return d;
    }

    /**
     * @return The duration of the audio.
     */
    public Duration getDuration() {
        return this.voiceMedia.getDuration();
    }

    /**
     * Pause the playing of the audio.
     */
    public void pause() {
        this.voicePlayer.pause();
        if (this.musicPlayer != null) this.musicPlayer.pause();
    }

    /**
     * Resume the playing of the audio.
     */
    public void play() {
        this.voicePlayer.play();
        if (this.musicPlayer != null) this.musicPlayer.play();
    }

    /**
     * @return The player’s position.
     */
    public Duration getPosition() {
        return this.voicePlayer.getCurrentTime();
    }

    /**
     * Set the player’s position at the given time.
     * <p>
     * <b>Note:</b> once the position is set, the audio will resume play unless it was explicitly paused before
     * calling this method.
     *
     * @param position The new position of the player. If this value is negative (respectively greater than
     *                 {@link #getDuration() the audio duration}), it shall be set to 0 (resp. to the audio duration).
     */
    public void setPosition(final Duration position) {
        final Duration newPosition = this.clampDuration(position);
        this.voicePlayer.seek(newPosition);
        if (this.musicPlayer != null) this.musicPlayer.seek(newPosition);
    }

    /**
     * Set the player’s position before the current position.
     *
     * @param duration The duration used to rewind the audio.
     */
    public void moveBackward(final Duration duration) {
        this.setPosition(this.getPosition().add(duration.negate()));
    }

    /**
     * Set the player’s position after the current position.
     *
     * @param duration The duration used to fast-forward the audio.
     */
    public void moveForward(final Duration duration) {
        this.setPosition(this.getPosition().add(duration));
    }

    /**
     * @param target The volume to set for the voice audio. Its effect will be clamped to the range [0.0, 1.0].
     */
    public void setVoiceVolume(final double target) {
        this.voicePlayer.setVolume(target);
    }

    /**
     * @param target The volume to set for the music audio. Its effect will be clamped to the range [0.0, 1.0].
     */
    public void setMusicVolume(final double target) {
        if (this.musicPlayer == null) this.setVoiceVolume(target);
        else this.musicPlayer.setVolume(target);
    }

    /**
     * @param mute Whether the voice audio shall be muted.
     */
    public void setVoiceMute(final boolean mute) {
        this.voicePlayer.setMute(mute);
    }

    /**
     * @param mute Whether the music audio shall be muted.
     */
    public void setMusicMute(final boolean mute) {
        if (this.musicPlayer == null) this.setVoiceMute(mute);
        else this.musicPlayer.setMute(mute);
    }

    /**
     * @return {@code true} if and only if the volume of the voice and of the music can be operated separately,
     * {@code false} otherwise.
     */
    public boolean areVoiceAndMusicSeparated() {
        return this.musicPlayer != null;
    }

    /**
     * Free all resources associated with player.
     */
    public void dispose() {
        this.voicePlayer.dispose();
        if (this.musicPlayer != null) this.musicPlayer.dispose();
        this.voicePlayer.currentTimeProperty().removeListener(this.listener);
    }
}
