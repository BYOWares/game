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
package fr.byowares.game.utils.serial.source;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * An abstraction of hierarchical document of files and folder.
 *
 * @since XXX
 */
public interface Source {

    /**
     * @return An {@link java.io.InputStream} used to read the content of this source.
     *
     * @throws java.io.IOException If an I/O error occurs while loading this source.
     */
    InputStream load()
            throws IOException;


    /**
     * @return The parent of this source.
     */
    Source getParent();

    /**
     * @param childName The name of the child element.
     *
     * @return The child source with the given name (might not exist).
     */
    Source resolve(final String childName);

    /**
     * @return The List of child sources of this one.
     */
    List<Source> getChildSources();

    /**
     * @return The name of this source.
     */
    String getName();
}
