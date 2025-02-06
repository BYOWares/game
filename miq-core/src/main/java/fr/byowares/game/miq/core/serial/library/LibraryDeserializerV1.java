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
import fr.byowares.game.utils.serial.Version;
import fr.byowares.game.utils.serial.VersionedDeserializer;

import java.util.Map;

/**
 * Version 1 for {@link fr.byowares.game.miq.core.model.song.Library} deserializer.
 *
 * @since XXX
 */
public class LibraryDeserializerV1
        extends VersionedDeserializer<Library> {

    /** Singleton pattern. */
    public static final LibraryDeserializerV1 INSTANCE = new LibraryDeserializerV1();

    private LibraryDeserializerV1() {
        // Singleton pattern
    }

    @Override
    protected Library doDeserialize(final Map<String, Object> map) {
        final Library library = new Library(removeAsString(map, Constants.TITLE));
        library.setComment(removeAsString(map, Constants.COMMENT));
        return library;
    }

    @Override
    protected Version getVersion() {
        return Constants.V1;
    }
}
