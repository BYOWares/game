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

import fr.byowares.game.miq.core.model.audio.DuoSource;
import fr.byowares.game.miq.core.model.audio.SingleSource;
import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.utils.hashcodes.HashCodes;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A representation of a Song that can be played for a game.
 *
 * @since XXX
 */
public class Song
        extends NamedSourcedObject<Song> {

    private final List<CharSequence> rawLyrics = new ArrayList<>();
    private final List<Lyrics> lyrics = new ArrayList<>();

    private CharSequence copyright;
    private CharSequence artist;
    private CharSequence albumName;

    private DuoSource duoSource;
    private SingleSource singleSource;

    /**
     * @param title The title of the song.
     */
    public Song(final CharSequence title) {
        super(title);
    }

    /**
     * @return The text as a list of text.
     */
    public List<CharSequence> getRawLyrics() {
        return this.rawLyrics;
    }

    /**
     * @return The list of {@link fr.byowares.game.miq.core.model.lyrics.Lyrics} made for this song.
     */
    public List<Lyrics> getLyrics() {
        return this.lyrics;
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
     * @return The artist singing this song.
     */
    public CharSequence getArtist() {
        return this.artist;
    }

    /**
     * @param artist The artist singing this song.
     */
    public void setArtist(final CharSequence artist) {
        this.artist = artist;
    }

    /**
     * @return The name of the album this song is in.
     */
    public CharSequence getAlbumName() {
        return this.albumName;
    }

    /**
     * @param albumName The name of the album this song is in.
     */
    public void setAlbumName(final CharSequence albumName) {
        this.albumName = albumName;
    }

    /**
     * @return The {@link fr.byowares.game.miq.core.model.audio.DuoSource} used for this song.
     */
    public DuoSource getDuoSource() {
        return this.duoSource;
    }

    /**
     * @param duoSource The {@link fr.byowares.game.miq.core.model.audio.DuoSource} to use for this song.
     */
    public void setDuoSource(final DuoSource duoSource) {
        this.duoSource = duoSource;
    }

    /**
     * @return The {@link fr.byowares.game.miq.core.model.audio.SingleSource} used for this song.
     */
    public SingleSource getSingleSource() {
        return this.singleSource;
    }

    /**
     * @param singleSource The {@link fr.byowares.game.miq.core.model.audio.SingleSource} to use for this song.
     */
    public void setSingleSource(final SingleSource singleSource) {
        this.singleSource = singleSource;
    }

    @Override
    public int hashCode() {
        return HashCodes.hash(this.rawLyrics, //
                              this.lyrics, //
                              this.copyright, //
                              this.getName(), //
                              this.artist, //
                              this.albumName, //
                              this.getComment(), //
                              this.duoSource, //
                              this.singleSource //
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Song song)) return false;
        return Objects.equals(this.rawLyrics, song.rawLyrics) //
                && Objects.equals(this.lyrics, song.lyrics) //
                && Objects.equals(this.copyright, song.copyright) //
                && Objects.equals(this.getName(), song.getName()) //
                && Objects.equals(this.artist, song.artist) //
                && Objects.equals(this.albumName, song.albumName) //
                && Objects.equals(this.getComment(), song.getComment()) //
                && Objects.equals(this.duoSource, song.duoSource) //
                && Objects.equals(this.singleSource, song.singleSource) //
                ;
    }

    @Override
    public String toString() {
        return "{Song=" + this.getName() + ", source=" + this.getSource() + ", comment=" + this.getComment() //
                + ", albumName=" + this.albumName + ", artist=" + this.artist + '}';
    }

    @Override
    public Song copyInMemory() {
        final Song song = new Song(this.getName());
        song.getLyrics().addAll(this.getLyrics());
        song.getRawLyrics().addAll(this.getRawLyrics());
        song.setAlbumName(this.getAlbumName());
        song.setArtist(this.getArtist());
        song.setComment(this.getComment());
        song.setCopyright(this.getCopyright());
        song.setDuoSource(this.duoSource);
        song.setSingleSource(this.singleSource);
        return song;
    }
}
