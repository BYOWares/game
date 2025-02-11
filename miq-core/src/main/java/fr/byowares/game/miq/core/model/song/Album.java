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
import fr.byowares.game.utils.serial.source.NamedSourcedObject;

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
        extends NamedSourcedObject {

    private final List<Song> songs;
    private CharSequence copyright;
    private CharSequence artist;

    /**
     * Create a new instance with no songs, no title, no artist.
     *
     * @param title The title of this album.
     *
     * @throws java.lang.NullPointerException If {@code title} is {@code null}.
     */
    public Album(final CharSequence title) {
        super(title);
        this.songs = new ArrayList<>();
    }


    @Override
    public NamedSourcedObject buildCopy(final CharSequence name) {
        final Album album = new Album(name);
        album.setArtist(this.getArtist());
        album.setCopyright(this.getCopyright());
        return album;
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
        return HashCodes.hash(this.copyright, this.getName(), this.getComment(), this.artist);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Album album)) return false;
        return Objects.equals(this.copyright, album.copyright) //
                && Objects.equals(this.getName(), album.getName()) //
                && Objects.equals(this.getComment(), album.getComment()) //
                && Objects.equals(this.artist, album.artist);
    }

    @Override
    public String toString() {
        return "{Album=" + this.getName() + ", source=" + this.getSource() + ", comment=" + this.getComment() + //
                ", artist=" + this.artist + ", songs=" + this.songs.size() + '}';
    }
}
