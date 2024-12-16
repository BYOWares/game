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

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An abstract player that can keep track of playing statuses:
 * <ul>
 *     <li>Whether we want the audio to be played.</li>
 *     <li>Whether the audio is played.</li>
 * </ul>
 *
 * @since XXX
 */
public abstract class StatusPlayer
        implements AudioPlayer {

    /** The {@link javax.sound.sampled.LineListener} that keep tracks of playing statuses */
    final StatusListener listener;

    /** A new StatusPlayer with its own {@link javax.sound.sampled.LineListener} */
    public StatusPlayer() {
        this.listener = new StatusListener();
    }

    /**
     * LineListener that aggregates playing status:
     * <ul>
     *     <li>Whether we want the audio to be played.</li>
     *     <li>Whether the audio is played.</li>
     * </ul>
     */
    private static class StatusListener
            implements LineListener {

        private final AtomicBoolean shouldBePlaying;
        private final AtomicBoolean isPlaying;

        private StatusListener() {
            this.shouldBePlaying = new AtomicBoolean();
            this.isPlaying = new AtomicBoolean();
        }

        @Override
        public void update(final LineEvent event) {
            this.isPlaying.set(event.getType() == LineEvent.Type.START);
        }

        private boolean isPlaying() {
            return this.isPlaying.get();
        }

        private boolean shouldBePlaying() {
            return this.shouldBePlaying.get();
        }
    }

    /**
     * Set {@link #shouldBePlaying()} to {@code false} and then delegate the actual stop.
     */
    @Override
    public final void stop() {
        this.listener.shouldBePlaying.set(false);
        this.doStop();
    }

    /**
     * Pause the playing of the audio.
     */
    abstract void doStop();

    /**
     * Set {@link #shouldBePlaying()} to {@code true} and then delegate the actual resume.
     */
    @Override
    public final void play() {
        this.listener.shouldBePlaying.set(true);
        this.doPlay();
    }

    /**
     * Resume the playing of the audio.
     */
    abstract void doPlay();

    /**
     * Makes sure the position is withing the range of the audio (else, set it to its closest boundary).
     * The playing status ({@link #shouldBePlaying()}) is saved before changing the position. Playing is resume once
     * the new position is set, accordingly to the saved playing status.
     */
    @Override
    public final void setPosition(final long positionInMicroseconds) {
        final long newPos = Math.min(Math.max(0L, positionInMicroseconds), this.getDurationInMicroseconds());
        final boolean shouldBePlaying = this.shouldBePlaying();
        this.stop();
        this.doSetPosition(newPos);
        if (shouldBePlaying) this.play();
    }

    /**
     * @param positionInMicroSeconds The new position of the player (always in the range of possible values).
     */
    abstract void doSetPosition(final long positionInMicroSeconds);

    @Override
    public final void moveBackward(final long durationInMicroseconds) {
        if (durationInMicroseconds == 0L) return;
        this.setPosition(this.getPosition() - durationInMicroseconds);
    }

    @Override
    public final void moveForward(final long durationInMicroseconds) {
        if (durationInMicroseconds == 0L) return;
        this.setPosition(this.getPosition() + durationInMicroseconds);
    }

    @Override
    public boolean shouldBePlaying() {
        return this.listener.shouldBePlaying();
    }

    @Override
    public final boolean isPlaying() {
        return this.listener.isPlaying();
    }
}
