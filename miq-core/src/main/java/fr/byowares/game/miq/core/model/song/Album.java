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

import fr.byowares.game.utils.hashcodes.HashCodes;
import fr.byowares.game.utils.serial.source.SourcedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A collection of {@link fr.byowares.game.miq.core.model.song.Song}. An album must always have a title. Artist and
 * copyright are optional.
 *
 * @since XXX
 */
public class Album
        extends SourcedObject {

    private final List<Song> songs;
    private CharSequence copyright;
    private CharSequence title;
    private CharSequence artist;

    /**
     * Create a new instance with no songs, no title, no artist.
     *
     * @param title The title of this album.
     *
     * @throws java.lang.NullPointerException If {@code title} is {@code null}.
     */
    public Album(final CharSequence title) {
        this.setTitle(title);
        this.songs = new ArrayList<>();
    }

    /**
     * @return The list of songs composing this album.
     */
    public List<Song> getSongs() {
        return this.songs;
    }

    /**
     * @return The copyright of this song.
     */
    public CharSequence getCopyright() {
        return this.copyright;
    }

    /**
     * @param copyright The copyright to set for this song.
     */
    public void setCopyright(final CharSequence copyright) {
        this.copyright = copyright;
    }

    /**
     * @return The title of the album.
     */
    public CharSequence getTitle() {
        return this.title;
    }

    /**
     * @param title The title to set for this album.
     */
    public void setTitle(final CharSequence title) {
        this.title = Objects.requireNonNull(title);
    }

    /**
     * @return The artist associated with this album.
     */
    public CharSequence getArtist() {
        return this.artist;
    }

    /**
     * @param artist The artist to associate with this album.
     */
    public void setArtist(final CharSequence artist) {
        this.artist = artist;
    }

    @Override
    public int hashCode() {
        return HashCodes.hash(this.copyright, this.title, this.artist);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Album album)) return false;
        return Objects.equals(this.copyright, album.copyright) //
                && Objects.equals(this.title, album.title) //
                && Objects.equals(this.artist, album.artist);
    }

    @Override
    public String toString() {
        return "{Album=" + this.title + ", source=" + this.getSource() + ", artist=" + this.artist + ", songs=" + this.songs.size() + '}';
    }
}
