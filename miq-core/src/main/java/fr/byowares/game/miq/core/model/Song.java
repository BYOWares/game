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
package fr.byowares.game.miq.core.model;

import fr.byowares.game.miq.core.model.audio.DuoSource;
import fr.byowares.game.miq.core.model.audio.SingleSource;

import java.util.List;

/**
 * @param comment    Some comment about this song (optional).
 * @param duoSource  The source for the duo audio (voice and music are separated).
 * @param soloSource The source for the mono audio (voice and music can be isolated).
 * @param rawLyrics  The raw lyrics.
 * @param allLyrics  The list of annotated lyrics with timestamps made for this song.
 *
 * @since XXX
 */
public record Song(
        String comment,
        DuoSource duoSource,
        SingleSource soloSource,
        List<String> rawLyrics,
        List<Lyrics> allLyrics
) {}
