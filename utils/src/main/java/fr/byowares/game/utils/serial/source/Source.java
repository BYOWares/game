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
import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;
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
     * @return A new writer to this source.
     *
     * @throws IOException If the writer could not be created.
     */
    Writer newWriter()
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

    /**
     * Create a temporary file based on this current source (a temporary directory if a directory, or a temporary
     * file of a regular file).
     *
     * @return The source representing the temporary file created.
     *
     * @throws java.io.IOException If an I/ O error occurs.
     */
    Source createTempSource()
            throws IOException;

    /**
     * @param newName The new file name to which the content must be moved.
     *
     * @throws IOException If an I/O error occurs.
     */
    void move(String newName)
            throws IOException;

    /**
     * Copy the content of the file {@code path} in the current source.
     *
     * @param path The path to the file whose content must be copied.
     *
     * @throws IOException If an I/O error occurs.
     */
    void copyFileContent(final Path path)
            throws IOException;

    /**
     * @return The {@link java.net.URI} to this source.
     */
    URI toURI();
}
