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
package fr.byowares.game.miq.core.model.song;

import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.serial.Constants;
import fr.byowares.game.miq.core.serial.album.AlbumDeserializers;
import fr.byowares.game.miq.core.serial.library.LibraryDeserializers;
import fr.byowares.game.miq.core.serial.lyrics.LyricsDeserializers;
import fr.byowares.game.miq.core.serial.song.SongDeserializers;
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import fr.byowares.game.utils.serial.source.Source;
import fr.byowares.game.utils.serial.source.SourcePath;
import org.agrona.LangUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Collection of {@link fr.byowares.game.miq.core.model.song.Library}, offering the basic operations to manipulate them.
 *
 * @since XXX
 */
public class Libraries
        extends NamedSourcedObject {

    /** The file name storing object data. */
    public static final String MIQ_FILE_NAME = "miq.yml";

    private static final Logger log = LoggerFactory.getLogger(Libraries.class);
    private final Library undefined;
    private final List<Library> libraries;

    private Libraries(final Path root) {
        super(Objects.requireNonNull(root).getFileName().toString(), new SourcePath(root));
        this.undefined = new Library("__UNDEFINED__");
        this.libraries = new ArrayList<>();
        this.libraries.add(this.undefined);
    }

    /**
     * Recursively analyze the {@code root} path to create an instance of
     * {@link fr.byowares.game.miq.core.model.song.Libraries}.
     *
     * @param root The Path to recursively analyze.
     *
     * @return A new {@link fr.byowares.game.miq.core.model.song.Libraries}
     */
    public static Libraries from(final Path root) {
        final Libraries libraries = new Libraries(root);
        if (!Files.isDirectory(root)) return libraries;
        from(libraries, libraries.undefined, libraries.undefined.getUndefined(), root);
        return libraries;
    }

    private static void from(
            final Libraries libraries,
            Library library,
            Album album,
            final Path path
    ) {

        final Path miqPath = path.resolve(MIQ_FILE_NAME);
        if (Files.isRegularFile(miqPath)) {
            final SourcePath miqSource = new SourcePath(miqPath);
            final Map<String, Object> load = Deserializers.load(miqPath);
            final String type = Objects.toString(Objects.requireNonNull(load.remove(Constants.TYPE)));

            switch (type) {
                case Constants.TYPE_LIBRARY -> {
                    library = LibraryDeserializers.INSTANCE.deserialize(load);
                    library.setSource(miqSource);
                    libraries.libraries.add(library);
                    album = library.getUndefined();
                }
                case Constants.TYPE_ALBUM -> {
                    album = AlbumDeserializers.INSTANCE.deserialize(load);
                    album.setSource(miqSource);
                    library.getAlbums().add(album);
                }
                case Constants.TYPE_SONG -> {
                    final SourcePath source = new SourcePath(path);
                    final Song song = SongDeserializers.INSTANCE.updateSource(source).deserialize(load);
                    song.setSource(miqSource);
                    album.getSongs().add(song);
                    retrieveLyrics(source, miqSource, song);
                }
                default -> {
                    log.warn("Unknown type '{}' while parsing file {}", type, miqPath);
                    throw new IllegalArgumentException("Unknown type: '" + type + "' [path=" + miqPath + "]");
                }
            }
        }
        final Album a = album;
        final Library l = library;
        try (final Stream<Path> stream = Files.list(path)) {
            stream.filter(Files::isDirectory).forEach(p -> from(libraries, l, a, p));
        } catch (final IOException e) {
            log.warn("Failed to list files under path {}", path, e);
            LangUtil.rethrowUnchecked(e);
        }
    }

    private static void retrieveLyrics(
            final Source source,
            final Source miqsource,
            final Song song
    ) {
        final List<Source> children = source.getChildSources();
        children.remove(miqsource);
        if (song.getSingleSource() != null) children.remove(song.getSingleSource().audioSource());
        if (song.getDuoSource() != null) {
            children.remove(song.getDuoSource().voiceSource());
            children.remove(song.getDuoSource().musicSource());
        }
        for (final Source childSource : children) {
            try {
                final Lyrics lyrics = LyricsDeserializers.INSTANCE.deserialize(childSource.load());
                lyrics.setSource(childSource);
                song.getLyrics().add(lyrics);
            } catch (final IOException e) {
                log.warn("Failed to load {}", childSource, e);
                LangUtil.rethrowUnchecked(e);
            }
        }
    }

    /**
     * @return The list of {@link fr.byowares.game.miq.core.model.song.Library} found in this collection.
     */
    public List<Library> getLibraries() {
        return this.libraries;
    }

    /**
     * @return The {@link fr.byowares.game.miq.core.model.song.Library} used for Albums & Songs without Library.
     */
    public Library getUndefined() {
        return this.undefined;
    }

    @Override
    public String toString() {
        return "{Libraries=" + this.getName() + ", source=" + this.getSource() + '}';
    }

    @Override
    public Libraries buildCopy(final CharSequence name) {
        throw new UnsupportedOperationException("This method makes no sense on this object");
    }
}
