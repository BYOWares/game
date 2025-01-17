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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
}
