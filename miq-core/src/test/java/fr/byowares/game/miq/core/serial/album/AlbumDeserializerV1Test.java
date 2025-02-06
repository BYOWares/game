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
package fr.byowares.game.miq.core.serial.album;

import fr.byowares.game.miq.core.model.song.Album;
import fr.byowares.game.miq.core.serial.AbstractDesSerTest;
import fr.byowares.game.miq.core.serial.Constants;
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlbumDeserializerV1Test
        extends AbstractDesSerTest<Album> {

    public static final Album ALBUM_V1 = new Album("Some fancy title");
    static final Album ALBUM_V1_NO_ML = new Album(ALBUM_V1.getName());
    static final String INPUT_V1 = """
            version: 1
            title: Some fancy title
            artist: Super cool artist
            comment: Any comment
            copyright: |-
              A complicated
              Multi lines
              Copyright with @
            """;
    static final String INPUT_V1_NO_ML = """
            version: 1
            title: Some fancy title
            artist: Super cool artist
            comment: Any comment
            copyright: No copyright
            """;
    static final String VERSION_V1 = "version: 1";
    private static final String TITLE = "title: Some fancy title";
    private static final String COMMENT = "comment: Any comment";
    private static final String COPYRIGHT = "copyright: No copyright";
    private static final String ARTIST = "artist: Super cool artist";

    static {
        ALBUM_V1.setComment("Any comment");
        ALBUM_V1.setArtist("Super cool artist");
        ALBUM_V1.setCopyright("A complicated\nMulti lines\nCopyright with @");

        ALBUM_V1_NO_ML.setArtist(ALBUM_V1.getArtist());
        ALBUM_V1_NO_ML.setCopyright("No copyright");
    }

    @Test
    public void testBasicInputV1() {
        assertEquals(ALBUM_V1, this.getDeserializers().deserialize(inputStream(INPUT_V1)));
    }

    @Test
    public void testBasicInputV1NoMultiLines() {
        assertEquals(ALBUM_V1_NO_ML, this.getDeserializers().deserialize(inputStream(INPUT_V1_NO_ML)));
    }

    @Test
    public void testInvalidTitleMissing() {
        this.assertInvalidWhenMissing(INPUT_V1_NO_ML, TITLE, Constants.TITLE);
    }

    @Test
    public void testValidCommentMissing() {
        this.assertValidWhenMissing(INPUT_V1_NO_ML, COMMENT, Constants.COMMENT, NamedSourcedObject::getComment);
    }

    @Test
    public void testValidArtistMissing() {
        this.assertValidWhenMissing(INPUT_V1_NO_ML, ARTIST, Constants.ARTIST, Album::getArtist);
    }

    @Test
    public void testValidCopyrightMissing() {
        this.assertValidWhenMissing(INPUT_V1_NO_ML, COPYRIGHT, Constants.COPYRIGHT, Album::getCopyright);
    }

    @Override
    protected Deserializers<Album> getDeserializers() {
        return AlbumDeserializers.INSTANCE;
    }
}
