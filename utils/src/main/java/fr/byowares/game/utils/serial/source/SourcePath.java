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

import org.agrona.LangUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Simple {@link java.nio.file.Path} wrapper to implement the {@link fr.byowares.game.utils.serial.source.Source}
 * interface.
 *
 * @param path The path used as source.
 *
 * @since XXX
 */
public record SourcePath(Path path)
        implements Source {

    private static final CopyOption[] COPY_OPTIONS = {REPLACE_EXISTING};

    /**
     * @param path The path to be considered as a source.
     *
     * @throws java.lang.NullPointerException If {@code path} is {@code null}.
     */
    public SourcePath {
        Objects.requireNonNull(path);
    }

    @Override
    public String toString() {
        return Objects.toString(this.path);
    }

    @Override
    public InputStream load()
            throws IOException {
        return new BufferedInputStream(Files.newInputStream(this.path));
    }

    @Override
    public Writer newWriter()
            throws IOException {
        Files.createDirectories(this.path.getParent());
        return Files.newBufferedWriter(this.path);
    }

    @Override
    public Source getParent() {
        return new SourcePath(this.path.getParent());
    }

    @Override
    public Source resolve(final String childName) {
        return new SourcePath(this.path.resolve(childName));
    }

    @Override
    public List<Source> getChildSources() {
        final List<Source> res = new ArrayList<>();
        try (final Stream<Path> stream = Files.list(this.path)) {
            stream.filter(Files::isRegularFile).map(SourcePath::new).forEach(res::add);
        } catch (final IOException e) {
            LangUtil.rethrowUnchecked(e);
        }
        return res;
    }

    @Override
    public String getName() {
        return this.path.getFileName().toString();
    }

    @Override
    public Source createTempSource()
            throws IOException {
        final Path parent = this.path.getParent();
        if (parent == null)
            throw new IOException("No parent found to " + this + ", could not create a temporary file.");
        Files.createDirectories(parent);
        if (Files.isRegularFile(this.path)) return new SourcePath(Files.createTempFile(parent, this.getName(), ".tmp"));
        return new SourcePath(Files.createTempDirectory(parent, this.getName()));
    }

    @Override
    public Source rename(final String newName)
            throws IOException {
        if (!Files.exists(this.path)) throw new IOException("");

        final Path newPath = this.path.getParent().resolve(newName);
        Files.move(this.path, newPath, COPY_OPTIONS);
        return new SourcePath(newPath);
    }

    @Override
    public void copyFileContent(final Path path)
            throws IOException {
        if (!Files.exists(path)) throw new IOException("'" + path + "' does not exist.");
        if (!Files.isRegularFile(path)) throw new IOException("'" + path + "' is not a regular file.");
        Files.copy(path, this.path, COPY_OPTIONS);
    }
}
