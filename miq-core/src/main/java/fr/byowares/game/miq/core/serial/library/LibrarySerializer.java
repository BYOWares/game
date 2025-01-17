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
package fr.byowares.game.miq.core.serial.library;

import fr.byowares.game.miq.core.model.song.Library;
import fr.byowares.game.miq.core.serial.Constants;
import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.Version;

import java.util.Map;

/**
 * Offer the capacity to serialize {@link fr.byowares.game.miq.core.model.song.Album} in Yaml format.
 *
 * @since XXX
 */
public class LibrarySerializer
        extends Serializer<Library> {

    /** Singleton pattern. */
    public static final LibrarySerializer INSTANCE = new LibrarySerializer();

    private LibrarySerializer() {
        // Singleton pattern
    }

    @Override
    public void doSerialize(
            final Library library,
            final Map<String, Object> map
    ) {
        map.put(Constants.TYPE, Constants.TYPE_LIBRARY);
        map.put(Constants.TITLE, library.getName());
    }

    @Override
    protected Version getVersion() {
        return Constants.V1;
    }
}
