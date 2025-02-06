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
package fr.byowares.game.miq.core.serial.library;

import fr.byowares.game.miq.core.model.song.Library;
import fr.byowares.game.miq.core.serial.AbstractDesSerTest;
import fr.byowares.game.miq.core.serial.Constants;
import fr.byowares.game.utils.serial.Deserializers;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryDeserializerV1Test
        extends AbstractDesSerTest<Library> {

    static final Library LIBRARY_V1 = new Library("Library 1");
    static final String VERSION_V1 = "version: 1";
    static final String COMMENT_V1 = "comment: Comment 1";
    static final String NAME = "title: Library 1";
    static final String INPUT_V1 = """
            version: 1
            title: Library 1
            comment: Comment 1
            """;

    static {
        LIBRARY_V1.setComment("Comment 1");
    }

    @Test
    public void testValidBasicInputV1() {
        assertEquals(LIBRARY_V1, this.getDeserializers().deserialize(inputStream(INPUT_V1)));
    }

    @Test
    public void testInvalidNameMissing() {
        this.assertInvalidWhenMissing(INPUT_V1, NAME, Constants.TITLE);
    }

    @Test
    public void testValidCommentMissing() {
        this.assertValidWhenMissing(INPUT_V1, COMMENT_V1, Constants.COMMENT, NamedSourcedObject::getComment);
    }

    @Override
    protected Deserializers<Library> getDeserializers() {
        return LibraryDeserializers.INSTANCE;
    }
}
