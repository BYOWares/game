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
package fr.byowares.game.miq.core.serial.song;

import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.miq.core.serial.AbstractDesSerTest;
import fr.byowares.game.miq.core.serial.Constants;
import fr.byowares.game.utils.serial.Deserializers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SongDeserializerV1Test
        extends AbstractDesSerTest<Song> {

    public static final Song SONG_V1 = new Song("Some fancy title");
    static final String INPUT_V1 = """
            version: 1
            title: Some fancy title
            artist: Super cool artist
            copyright: No copyright
            comment: Some comments
            album: Album's name
            lyrics:
              - First line
              - ''
              - Third line
            """;
    static final String VERSION_V1 = "version: 1";
    private static final String TITLE = "title: Some fancy title";
    private static final String ARTIST = "artist: Super cool artist";
    private static final String COPYRIGHT = "copyright: No copyright";
    private static final String COMMENT = "comment: Some comments";
    private static final String NAME = "album: Album's name";

    static {
        SONG_V1.setArtist("Super cool artist");
        SONG_V1.setCopyright("No copyright");
        SONG_V1.setComment("Some comments");
        SONG_V1.setAlbumName("Album's name");
        SONG_V1.getRawLyrics().addAll(List.of("First line", "", "Third line"));
    }

    @Test
    public void testBasicInputV1() {
        assertEquals(SONG_V1, this.getDeserializers().deserialize(inputStream(INPUT_V1)));
    }

    @Test
    public void testInvalidTitleMissing() {
        this.assertInvalidWhenMissing(INPUT_V1, TITLE, Constants.TITLE);
    }

    @Test
    public void testValidArtistMissing() {
        this.assertValidWhenMissing(INPUT_V1, ARTIST, Constants.ARTIST, Song::getArtist);
    }

    @Test
    public void testValidCopyrightMissing() {
        this.assertValidWhenMissing(INPUT_V1, COPYRIGHT, Constants.COPYRIGHT, Song::getCopyright);
    }

    @Test
    public void testValidCommentMissing() {
        this.assertValidWhenMissing(INPUT_V1, COMMENT, Constants.COMMENT, Song::getComment);
    }

    @Test
    public void testValidAlbumNameMissing() {
        this.assertValidWhenMissing(INPUT_V1, NAME, Constants.ALBUM, Song::getAlbumName);
    }

    @Override
    protected Deserializers<Song> getDeserializers() {
        return SongDeserializers.INSTANCE;
    }
}
