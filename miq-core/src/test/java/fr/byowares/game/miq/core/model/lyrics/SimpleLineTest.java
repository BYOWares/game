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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SimpleLineTest {

    public static final List<LineElement> HEY = List.of(new Word("Hey"));
    public static final List<LineElement> YA = List.of(new Word("Ya"));
    public static final List<LineElement> HEY_YA = List.of(HEY.getFirst(), new Punctuation(" "), YA.getFirst());
    public static final List<LineElement> YA_HEY = List.of(YA.getFirst(), new Punctuation(" "), HEY.getFirst());
    public static final Set<Singer> DAVE = Set.of(new Singer("Dave"), new Singer("evaD"));

    @Test
    void testCannotBeMergedSingers() {
        final SimpleLine sl1 = new SimpleLine(HEY, Set.of(), false, false);
        final SimpleLine sl2 = new SimpleLine(HEY, DAVE, false, false);
        final List<SimpleLine> l12 = List.of(sl1, sl2);
        final List<SimpleLine> l21 = List.of(sl1, sl2);
        assertEquals(l12, SimpleLine.merge(l12));
        assertEquals(l21, SimpleLine.merge(l21));
    }

    @Test
    void testCannotBeMergedBackupVocals() {
        final SimpleLine sl1 = new SimpleLine(HEY, Set.of(), false, false);
        final SimpleLine sl2 = new SimpleLine(HEY, Set.of(), true, false);
        final List<SimpleLine> l12 = List.of(sl1, sl2);
        final List<SimpleLine> l21 = List.of(sl1, sl2);
        assertEquals(l12, SimpleLine.merge(l12));
        assertEquals(l21, SimpleLine.merge(l21));
    }

    @Test
    void testCannotBeMergedNonLexical() {
        final SimpleLine sl1 = new SimpleLine(HEY, Set.of(), false, false);
        final SimpleLine sl2 = new SimpleLine(HEY, Set.of(), false, true);
        final List<SimpleLine> l12 = List.of(sl1, sl2);
        final List<SimpleLine> l21 = List.of(sl1, sl2);
        assertEquals(l12, SimpleLine.merge(l12));
        assertEquals(l21, SimpleLine.merge(l21));
    }

    @Test
    void testCanBeMergedSingers() {
        final SimpleLine sl1 = new SimpleLine(HEY, DAVE, false, false);
        final SimpleLine sl2 = new SimpleLine(YA, DAVE, false, false);
        final List<SimpleLine> l12 = List.of(sl1, sl2);
        final List<SimpleLine> l21 = List.of(sl1, sl2);
        final List<Line> ml12 = SimpleLine.merge(l12);
        final List<Line> ml21 = SimpleLine.merge(l21);
        assertNotEquals(l12, ml12);
        assertEquals(1, ml12.size());
        assertNotEquals(l21, ml21);
        assertEquals(1, ml21.size());
    }

    @Test
    void testCanBeMergedBackVocals() {
        final SimpleLine sl1 = new SimpleLine(HEY, DAVE, true, false);
        final SimpleLine sl2 = new SimpleLine(YA, DAVE, true, false);
        final List<SimpleLine> l12 = List.of(sl1, sl2);
        final List<SimpleLine> l21 = List.of(sl1, sl2);
        final List<Line> ml12 = SimpleLine.merge(l12);
        final List<Line> ml21 = SimpleLine.merge(l21);
        assertNotEquals(l12, ml12);
        assertEquals(1, ml12.size());
        assertNotEquals(l21, ml21);
        assertEquals(1, ml21.size());
    }

    @Test
    void testCanBeMergedNonLexical() {
        final SimpleLine sl1 = new SimpleLine(HEY, DAVE, true, true);
        final SimpleLine sl2 = new SimpleLine(YA, DAVE, true, true);
        final List<SimpleLine> l12 = List.of(sl1, sl2);
        final List<SimpleLine> l21 = List.of(sl1, sl2);
        final List<Line> ml12 = SimpleLine.merge(l12);
        final List<Line> ml21 = SimpleLine.merge(l21);
        assertNotEquals(l12, ml12);
        assertEquals(1, ml12.size());
        assertNotEquals(l21, ml21);
        assertEquals(1, ml21.size());
    }

    @Test
    void testMergeEmpty() {
        assertEquals(List.of(), SimpleLine.merge(List.of()));
    }

    @Test
    void testMergeOneElement() {
        final SimpleLine sl1 = new SimpleLine(HEY, Set.of(), true, true);
        assertEquals(List.of(sl1), SimpleLine.merge(List.of(sl1)));
    }

    @Test
    void testMergeWords() {
        final SimpleLine sl1 = new SimpleLine(HEY, Set.of(), true, true);
        final SimpleLine sl2 = new SimpleLine(YA, Set.of(), true, true);

        final SimpleLine sl12 = new SimpleLine(HEY_YA, Set.of(), true, true);
        final SimpleLine sl21 = new SimpleLine(YA_HEY, Set.of(), true, true);
        assertEquals(List.of(sl12), SimpleLine.merge(List.of(sl1, sl2)));
        assertEquals(List.of(sl21), SimpleLine.merge(List.of(sl2, sl1)));
    }

    @Test
    void testMergeWordPunctuation() {
        final List<LineElement> HEY = List.of(new Word("Hey"));
        final List<LineElement> SUP = List.of(new Punctuation("'"), new Word("Sup"));
        final List<LineElement> HEY_SUP = List.of(new Word("Hey"), new Punctuation(" '"), new Word("Sup"));
        final SimpleLine sl1 = new SimpleLine(HEY, Set.of(), true, true);
        final SimpleLine sl2 = new SimpleLine(SUP, Set.of(), true, true);

        final SimpleLine sl1sl2 = new SimpleLine(HEY_SUP, Set.of(), true, true);
        assertEquals(List.of(sl1sl2), SimpleLine.merge(List.of(sl1, sl2)));
    }

    @Test
    void testMergePunctuations() {
        final List<LineElement> HEY = List.of(new Word("Hey"), new Punctuation("!"));
        final List<LineElement> SUP = List.of(new Word("Sup"));
        final List<LineElement> HEY_SUP = List.of(new Word("Hey"), new Punctuation("! "), new Word("Sup"));
        final SimpleLine sl1 = new SimpleLine(HEY, Set.of(), true, true);
        final SimpleLine sl2 = new SimpleLine(SUP, Set.of(), true, true);

        final SimpleLine sl1sl2 = new SimpleLine(HEY_SUP, Set.of(), true, true);
        assertEquals(List.of(sl1sl2), SimpleLine.merge(List.of(sl1, sl2)));
    }

    @Test
    void testMergeSeveralElement() {
        final SimpleLine sl1 = new SimpleLine(HEY, DAVE, true, true);
        final SimpleLine sl2 = new SimpleLine(YA, Set.of(), false, false);
        final Word hey = new Word("Hey");
        final Word ya = new Word("Ya");
        final Punctuation s = new Punctuation(" ");
        final SimpleLine sl111 = new SimpleLine(List.of(hey, s, hey, s, hey), DAVE, true, true);
        final SimpleLine sl222 = new SimpleLine(List.of(ya, s, ya, s, ya), Set.of(), false, false);
        assertEquals(List.of(sl111), SimpleLine.merge(List.of(sl1, sl1, sl1)));
        assertEquals(List.of(sl111, sl2), SimpleLine.merge(List.of(sl1, sl1, sl1, sl2)));
        assertEquals(List.of(sl111, sl222), SimpleLine.merge(List.of(sl1, sl1, sl1, sl2, sl2, sl2)));
        assertEquals(List.of(sl1, sl222), SimpleLine.merge(List.of(sl1, sl2, sl2, sl2)));
    }
}
