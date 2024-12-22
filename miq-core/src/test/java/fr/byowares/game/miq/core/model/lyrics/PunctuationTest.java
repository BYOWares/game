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
package fr.byowares.game.miq.core.model.lyrics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import static org.junit.jupiter.api.Assertions.*;

class PunctuationTest {

    @Test
    public void constructorTestAcceptAnything() {
        assertDoesNotThrow(getSupplier("Toto"));
        assertDoesNotThrow(getSupplier(" .-a !]"));
        assertDoesNotThrow(getSupplier("    "));
    }

    private static ThrowingSupplier<Punctuation> getSupplier(final String content) {
        return () -> new Punctuation(content);
    }

    @Test
    public void testIsWord() {
        assertFalse(new Punctuation("Word").isWord());
    }

    @Test
    public void testIsWhiteSpaceOnly() {
        final String wso = SpaceCleanerLineParserTest.WHITESPACES_STRING;
        assertFalse(new Punctuation("." + wso).isWhiteSpaceOnly());
        assertFalse(new Punctuation(wso + ".").isWhiteSpaceOnly());
        assertFalse(new Punctuation(wso + "." + wso).isWhiteSpaceOnly());
        assertTrue(new Punctuation(wso).isWhiteSpaceOnly());

    }

    @Test
    public void testTrimHead() {
        final String wso = SpaceCleanerLineParserTest.WHITESPACES_STRING;
        assertEquals(new Punctuation(""), new Punctuation(wso).trimHead());
        assertEquals(new Punctuation("."), new Punctuation(wso + ".").trimHead());
        assertEquals(new Punctuation("." + wso), new Punctuation("." + wso).trimHead());
        assertEquals(new Punctuation("."), new Punctuation(".").trimHead());
        assertEquals(new Punctuation("." + wso + "."), new Punctuation("." + wso + ".").trimHead());

    }

    @Test
    public void testTrimTail() {
        final String wso = SpaceCleanerLineParserTest.WHITESPACES_STRING;
        assertEquals(new Punctuation(""), new Punctuation(wso).trimTail());
        assertEquals(new Punctuation(wso + "."), new Punctuation(wso + ".").trimTail());
        assertEquals(new Punctuation("."), new Punctuation("." + wso).trimTail());
        assertEquals(new Punctuation("."), new Punctuation(".").trimTail());
        assertEquals(new Punctuation("." + wso + "."), new Punctuation("." + wso + ".").trimTail());
    }
}
