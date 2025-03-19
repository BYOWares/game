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

import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

/**
 * Represent an in memory origin of the object.
 *
 * @since XXX
 */
public class SourceInMemory
        implements Source {

    /** Unique instance. */
    public static final Source INSTANCE = new SourceInMemory();

    private SourceInMemory() {
        // Singleton pattern
    }

    @Override
    public String toString() {
        return "IN_MEMORY";
    }

    @Override
    public InputStream load() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Writer newWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Source getParent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Source resolve(final String childName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Source> getChildSources() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public Source createTempSource() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void move(final String newName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void copyFileContent(final Path path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI toURI() {
        throw new UnsupportedOperationException();
    }
}
