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
import fr.byowares.game.miq.core.serial.AbstractDeserializersTest;
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.Version;
import org.junit.jupiter.api.Test;

import static fr.byowares.game.miq.core.serial.song.SongDeserializerV1Test.INPUT_V1;
import static fr.byowares.game.miq.core.serial.song.SongDeserializerV1Test.SONG_V1;
import static fr.byowares.game.miq.core.serial.song.SongDeserializerV1Test.VERSION_V1;

/**
 * Testing the version.<br>
 * Other fields shall be tested in versioned deserializers.
 */
public class SongDeserializersTest
        extends AbstractDeserializersTest<Song> {

    @Override
    protected Song getObjectT() {
        return SONG_V1;
    }

    @Override
    protected String getInput() {
        return INPUT_V1;
    }

    @Override
    protected String getVersion() {
        return VERSION_V1;
    }

    @Override
    protected Deserializers<Song> getDeserializers() {
        return SongDeserializers.INSTANCE;
    }

    @Test
    public void testVersionInvalid2() {
        this.testInvalidVersion("version: 2", "No deserializer matched the given version: " + new Version(2));
    }
}
