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
import fr.byowares.game.utils.hashcodes.HashCodes;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import fr.byowares.game.utils.serial.source.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A representation of a Song that can be played for a game.
 *
 * @since XXX
 */
public class Song
        extends NamedSourcedObject {

    private final List<CharSequence> rawLyrics = new ArrayList<>();
    private final List<Lyrics> lyrics = new ArrayList<>();

    private CharSequence copyright;
    private CharSequence artist;
    private CharSequence albumName;

    private Source voiceSource;
    private Source musicSource;
    private Source audioSource;

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
     * @return The Source containing both voice and music data.
     */
    public Source getAudioSource() {
        return this.audioSource;
    }

    /**
     * @param audioSource The Source containing the audio (voice and music) data.
     */
    public void setAudioSource(final Source audioSource) {
        this.audioSource = audioSource;
    }

    /**
     * {@code voiceSource} and {@code musicSource} shall both either be equal to {@code null} or an actual source as
     * they work as a pair.
     *
     * @return The Source containing music data.
     */
    public Source getMusicSource() {
        return this.musicSource;
    }

    /**
     * {@code voiceSource} and {@code musicSource} shall both either be equal to {@code null} or an actual source as
     * they work as a pair.
     *
     * @param musicSource The Source containing music data only.
     */
    public void setMusicSource(final Source musicSource) {
        this.musicSource = musicSource;
    }

    /**
     * {@code voiceSource} and {@code musicSource} shall both either be equal to {@code null} or an actual source as
     * they work as a pair.
     *
     * @return The Source containing voice data.
     */
    public Source getVoiceSource() {
        return this.voiceSource;
    }

    /**
     * {@code voiceSource} and {@code musicSource} shall both either be equal to {@code null} or an actual source as
     * they work as a pair.
     *
     * @param voiceSource The Source containing voice data only.
     */
    public void setVoiceSource(final Source voiceSource) {
        this.voiceSource = voiceSource;
    }

    @Override
    public int hashCode() {
        return HashCodes.hash(this.artist, //
                              this.albumName, //
                              this.copyright, //
                              this.getName(), //
                              this.getComment(), //
                              this.rawLyrics, //
                              this.lyrics, //
                              this.audioSource, //
                              this.voiceSource, //
                              this.musicSource //
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
                && Objects.equals(this.audioSource, song.audioSource) //
                && Objects.equals(this.voiceSource, song.voiceSource) //
                && Objects.equals(this.musicSource, song.musicSource) //
                ;
    }

    @Override
    public String toString() {
        return "{Song=" + this.getName() + ", source=" + this.getSource() + ", comment=" + this.getComment() //
                + ", albumName=" + this.albumName + ", artist=" + this.artist + '}';
    }

    @Override
    public Song buildCopy(final CharSequence name) {
        final Song song = new Song(name);
        song.getLyrics().addAll(new ArrayList<>(this.getLyrics()));
        song.getRawLyrics().addAll(new ArrayList<>(this.getRawLyrics()));
        song.setAlbumName(this.getAlbumName());
        song.setArtist(this.getArtist());
        song.setCopyright(this.getCopyright());
        song.setAudioSource(this.getAudioSource());
        song.setVoiceSource(this.getVoiceSource());
        song.setMusicSource(this.getMusicSource());
        return song;
    }
}
