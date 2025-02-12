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
package fr.byowares.game.miq.jfx.editor.tree;

import fr.byowares.game.miq.core.model.song.Album;
import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import fr.byowares.game.utils.serial.source.Source;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * An object to cache:
 * <ul>
 *     <li>Artists' name: using {@link fr.byowares.game.miq.core.model.song.Album} and
 *     {@link fr.byowares.game.miq.core.model.song.Song}.</li>
 *     <li>Albums' name: using {@link fr.byowares.game.miq.core.model.song.Album} and
 *     {@link fr.byowares.game.miq.core.model.song.Song}.</li>
 * </ul>
 *
 * @since XXX
 */
public class CachedData {

    private static final Logger log = LoggerFactory.getLogger(CachedData.class);

    private final Cache artistNames;
    private final Cache albumNames;

    /** New */
    public CachedData() {
        this.artistNames = new Cache();
        this.albumNames = new Cache();
    }

    /**
     * @param t         The object to extract data from.
     * @param extractor The extractor of data.
     * @param <T>       The type of the object to extract data from.
     *
     * @return {@code null} if {@code t} is {@code null}, of if the value returned by applying the {@code extractor}
     * on {@code t} is {@code null} or blank (see {@link String#isBlank()};
     */
    private static <T> String extractNullNormalized(
            final T t,
            final Function<T, CharSequence> extractor
    ) {
        if (t == null) return null;
        final CharSequence c = extractor.apply(t);
        if (c == null) return null;
        final String s = c.toString();
        return s.isBlank() ? null : s;
    }

    private static String extractAlbumArtist(final Album album) {
        return extractNullNormalized(album, Album::getArtist);
    }

    private static String extractAlbumName(final Album album) {
        return extractNullNormalized(album, Album::getName);
    }

    private static String extractSongArtist(final Song song) {
        return extractNullNormalized(song, Song::getArtist);
    }

    private static String extractSongAlbumName(final Song song) {
        return extractNullNormalized(song, Song::getAlbumName);
    }

    private static Source getSource(
            final NamedSourcedObject o,
            final NamedSourcedObject n
    ) {
        if (o == null) {
            if (n == null) return null;
            return n.getSource();
        }
        return o.getSource();
    }

    /**
     * @return A read only observable list of artist names.
     */
    public ObservableList<String> getArtistNames() {
        return new ReadOnlyListWrapper<>(this.artistNames.list);
    }

    /**
     * @return A read only observable list of album names.
     */
    public ObservableList<String> getAlbumNames() {
        return new ReadOnlyListWrapper<>(this.albumNames.list);
    }

    /**
     * Update the cache using two versions of the same Album.
     *
     * @param oldValue The previous values an Album (or {@code null} if it's a new Album).
     * @param newValue The new values for the same Album (or {@code null} if the Album is deleted).
     */
    public void cacheAlbum(
            final Album oldValue,
            final Album newValue
    ) {
        final Source s = getSource(oldValue, newValue);
        if (s == null) return;
        this.artistNames.updateCache(s, oldValue, newValue, CachedData::extractAlbumArtist);
        this.albumNames.updateCache(s, oldValue, newValue, CachedData::extractAlbumName);
    }

    /**
     * Update the cache using two versions of the same Song.
     *
     * @param oldValue The previous values an Song (or {@code null} if it's a new Song).
     * @param newValue The new values for the same Song (or {@code null} if the Song is deleted).
     */
    public void cacheSong(
            final Song oldValue,
            final Song newValue
    ) {
        final Source s = getSource(oldValue, newValue);
        if (s == null) return;
        this.artistNames.updateCache(s, oldValue, newValue, CachedData::extractSongArtist);
        this.albumNames.updateCache(s, oldValue, newValue, CachedData::extractSongAlbumName);
    }

    /**
     * An object to cache data. This is the format of the cache: {@code Map<Data, Set<Origin>>}. The {@code Data} is
     * backed by a set of {@code Origin}. When this set is empty, the {@code Data} is removed.<br/>
     * An {@link javafx.collections.ObservableList} is maintained to always represent the keys defined in the cache
     * (in lexical order).
     */
    private static class Cache {

        private final ObservableList<String> list;
        private final SortedMap<String, Set<Source>> sources;

        private Cache() {
            this.list = FXCollections.observableArrayList();
            this.sources = new TreeMap<>();
        }

        <T> void updateCache(
                final Source source,
                final T oldValue,
                final T newValue,
                final Function<T, String> extractor
        ) {
            final String oldString = extractor.apply(oldValue);
            final String newString = extractor.apply(newValue);
            if (Objects.equals(oldString, newString)) return; // No change.

            if (oldString != null) {
                this.sources.computeIfPresent(oldString, (v, set) -> {
                    set.remove(source);
                    if (set.isEmpty()) {
                        this.list.remove(v);
                        return null;
                    } else return set;
                });
            }
            if (newValue != null) {
                this.sources.compute(newString, (v, set) -> {
                    if (set == null) {
                        // Keeping the list of element sorted
                        final int index = Collections.binarySearch(this.list, v);
                        if (index >= 0)
                            log.warn("{} was already present in the list ({}) but not in the set", v, this.list);
                        else this.list.add(-index - 1, v);
                        set = new HashSet<>();
                    }
                    set.add(source);
                    return set;
                });
            }
        }
    }
}
