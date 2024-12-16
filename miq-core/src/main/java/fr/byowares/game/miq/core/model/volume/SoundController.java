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
 * Basic sound controls over an audio.
 *
 * @since XXX
 */
public interface SoundController {

    /**
     * @param target The volume we want to set.
     */
    void setVolume(Volume target);

    /**
     * To mute the audio. It leaves untouched the volume previously set.
     */
    void mute();

    /**
     * To unmute the audio. It leaves untouched the volume previously set.
     */
    void unmute();
}
