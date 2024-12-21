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

class WordTest {

    @Test
    public void constructorTestAcceptAnything() {
        assertDoesNotThrow(getSupplier("Toto"));
        assertDoesNotThrow(getSupplier(" .-a !]"));
        assertDoesNotThrow(getSupplier("    "));
    }

    private static ThrowingSupplier<Word> getSupplier(final String content) {
        return () -> new Word(content);
    }

    @Test
    public void testIsWord() {
        assertTrue(new Word("Word").isWord());
        assertTrue(new Word(".]-").isWord());
    }

    @Test
    public void testIsWhiteSpaceOnly() {
        assertFalse(new Word(SpaceCleanerLineParserTest.WHITESPACES_STRING).isWhiteSpaceOnly());

    }

    @Test
    public void testTrimHead() {
        final String wso = SpaceCleanerLineParserTest.WHITESPACES_STRING;
        assertTrimHead(wso);
        assertTrimHead(wso + ".");
        assertTrimHead("." + wso);
        assertTrimHead(".");
        assertTrimHead("." + wso + ".");
    }

    private static void assertTrimHead(final String word) {
        final Word w = new Word(word);
        assertEquals(w, w.trimHead());
    }

    @Test
    public void testTrimTail() {
        final String wso = SpaceCleanerLineParserTest.WHITESPACES_STRING;
        assertTrimTail(wso);
        assertTrimTail(wso + ".");
        assertTrimTail("." + wso);
        assertTrimTail(".");
        assertTrimTail("." + wso + ".");
    }

    private static void assertTrimTail(final String word) {
        final Word w = new Word(word);
        assertEquals(w, w.trimTail());
    }
}
