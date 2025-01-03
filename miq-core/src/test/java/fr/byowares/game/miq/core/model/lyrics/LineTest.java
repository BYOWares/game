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

class LineTest {

    public static final List<LineElement> HEY = List.of(new Word("Hey"));
    public static final List<LineElement> YA = List.of(new Word("Ya"));
    public static final List<LineElement> HEY_YA = List.of(HEY.getFirst(), new Punctuation(" "), YA.getFirst());
    public static final List<LineElement> YA_HEY = List.of(YA.getFirst(), new Punctuation(" "), HEY.getFirst());
    public static final Set<Singer> DAVE = Set.of(new Singer("Dave"), new Singer("evaD"));

    @Test
    void testCannotBeMergedSingers() {
        final Line sl1 = new Line(HEY, Set.of(), false, false);
        final Line sl2 = new Line(HEY, DAVE, false, false);
        final List<Line> l12 = List.of(sl1, sl2);
        final List<Line> l21 = List.of(sl1, sl2);
        assertEquals(l12, Line.merge(l12));
        assertEquals(l21, Line.merge(l21));
    }

    @Test
    void testCannotBeMergedBackupVocals() {
        final Line sl1 = new Line(HEY, Set.of(), false, false);
        final Line sl2 = new Line(HEY, Set.of(), true, false);
        final List<Line> l12 = List.of(sl1, sl2);
        final List<Line> l21 = List.of(sl1, sl2);
        assertEquals(l12, Line.merge(l12));
        assertEquals(l21, Line.merge(l21));
    }

    @Test
    void testCannotBeMergedNonLexical() {
        final Line sl1 = new Line(HEY, Set.of(), false, false);
        final Line sl2 = new Line(HEY, Set.of(), false, true);
        final List<Line> l12 = List.of(sl1, sl2);
        final List<Line> l21 = List.of(sl1, sl2);
        assertEquals(l12, Line.merge(l12));
        assertEquals(l21, Line.merge(l21));
    }

    @Test
    void testCanBeMergedSingers() {
        final Line sl1 = new Line(HEY, DAVE, false, false);
        final Line sl2 = new Line(YA, DAVE, false, false);
        final List<Line> l12 = List.of(sl1, sl2);
        final List<Line> l21 = List.of(sl1, sl2);
        final List<Line> ml12 = Line.merge(l12);
        final List<Line> ml21 = Line.merge(l21);
        assertNotEquals(l12, ml12);
        assertEquals(1, ml12.size());
        assertNotEquals(l21, ml21);
        assertEquals(1, ml21.size());
    }

    @Test
    void testCanBeMergedBackVocals() {
        final Line sl1 = new Line(HEY, DAVE, true, false);
        final Line sl2 = new Line(YA, DAVE, true, false);
        final List<Line> l12 = List.of(sl1, sl2);
        final List<Line> l21 = List.of(sl1, sl2);
        final List<Line> ml12 = Line.merge(l12);
        final List<Line> ml21 = Line.merge(l21);
        assertNotEquals(l12, ml12);
        assertEquals(1, ml12.size());
        assertNotEquals(l21, ml21);
        assertEquals(1, ml21.size());
    }

    @Test
    void testCanBeMergedNonLexical() {
        final Line sl1 = new Line(HEY, DAVE, true, true);
        final Line sl2 = new Line(YA, DAVE, true, true);
        final List<Line> l12 = List.of(sl1, sl2);
        final List<Line> l21 = List.of(sl1, sl2);
        final List<Line> ml12 = Line.merge(l12);
        final List<Line> ml21 = Line.merge(l21);
        assertNotEquals(l12, ml12);
        assertEquals(1, ml12.size());
        assertNotEquals(l21, ml21);
        assertEquals(1, ml21.size());
    }

    @Test
    void testMergeEmpty() {
        assertEquals(List.of(), Line.merge(List.of()));
    }

    @Test
    void testMergeOneElement() {
        final Line sl1 = new Line(HEY, Set.of(), true, true);
        assertEquals(List.of(sl1), Line.merge(List.of(sl1)));
    }

    @Test
    void testMergeWords() {
        final Line sl1 = new Line(HEY, Set.of(), true, true);
        final Line sl2 = new Line(YA, Set.of(), true, true);

        final Line sl12 = new Line(HEY_YA, Set.of(), true, true);
        final Line sl21 = new Line(YA_HEY, Set.of(), true, true);
        assertEquals(List.of(sl12), Line.merge(List.of(sl1, sl2)));
        assertEquals(List.of(sl21), Line.merge(List.of(sl2, sl1)));
    }

    @Test
    void testMergeWordPunctuation() {
        final List<LineElement> HEY = List.of(new Word("Hey"));
        final List<LineElement> SUP = List.of(new Punctuation("'"), new Word("Sup"));
        final List<LineElement> HEY_SUP = List.of(new Word("Hey"), new Punctuation(" '"), new Word("Sup"));
        final Line sl1 = new Line(HEY, Set.of(), true, true);
        final Line sl2 = new Line(SUP, Set.of(), true, true);

        final Line sl1sl2 = new Line(HEY_SUP, Set.of(), true, true);
        assertEquals(List.of(sl1sl2), Line.merge(List.of(sl1, sl2)));
    }

    @Test
    void testMergePunctuations() {
        final List<LineElement> HEY = List.of(new Word("Hey"), new Punctuation("!"));
        final List<LineElement> SUP = List.of(new Word("Sup"));
        final List<LineElement> HEY_SUP = List.of(new Word("Hey"), new Punctuation("! "), new Word("Sup"));
        final Line sl1 = new Line(HEY, Set.of(), true, true);
        final Line sl2 = new Line(SUP, Set.of(), true, true);

        final Line sl1sl2 = new Line(HEY_SUP, Set.of(), true, true);
        assertEquals(List.of(sl1sl2), Line.merge(List.of(sl1, sl2)));
    }

    @Test
    void testMergeSeveralElement() {
        final Line sl1 = new Line(HEY, DAVE, true, true);
        final Line sl2 = new Line(YA, Set.of(), false, false);
        final Word hey = new Word("Hey");
        final Word ya = new Word("Ya");
        final Punctuation s = new Punctuation(" ");
        final Line sl111 = new Line(List.of(hey, s, hey, s, hey), DAVE, true, true);
        final Line sl222 = new Line(List.of(ya, s, ya, s, ya), Set.of(), false, false);
        assertEquals(List.of(sl111), Line.merge(List.of(sl1, sl1, sl1)));
        assertEquals(List.of(sl111, sl2), Line.merge(List.of(sl1, sl1, sl1, sl2)));
        assertEquals(List.of(sl111, sl222), Line.merge(List.of(sl1, sl1, sl1, sl2, sl2, sl2)));
        assertEquals(List.of(sl1, sl222), Line.merge(List.of(sl1, sl2, sl2, sl2)));
    }
}
