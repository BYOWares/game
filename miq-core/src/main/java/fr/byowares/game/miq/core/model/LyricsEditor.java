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
package fr.byowares.game.miq.core.model;

import fr.byowares.game.miq.core.model.lyrics.Line;
import fr.byowares.game.miq.core.model.lyrics.LineElement;
import fr.byowares.game.miq.core.model.lyrics.Singer;
import fr.byowares.game.miq.core.model.lyrics.SpaceCleanerLineParser;
import fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse;
import fr.byowares.game.miq.core.model.lyrics.Word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * An object that help edit text of {@link fr.byowares.game.miq.core.model.Lyrics}.
 *
 * @since XXX
 */
public class LyricsEditor {

    private static final Range NO_RANGE = new Range(0L, 0L);

    private Lyrics lyrics;

    private static List<LineElement> todoLineElements() {
        final List<LineElement> elements = new ArrayList<>();
        elements.add(new Word("TODO"));
        return elements;
    }

    /**
     * @param comment The new comment (create a new instance of the underlying object).
     */
    public void updateComment(final CharSequence comment) {
        this.lyrics = new Lyrics(comment, this.verses());
    }

    /******************************************************************************************************************
     *                                                VERSE METHODS                                                   *
     ******************************************************************************************************************/
    private List<TimeCodedVerse> verses() {
        return this.lyrics.lyrics();
    }

    private TimeCodedVerse verse(final int verseIndex) {
        return this.lyrics.lyrics().get(verseIndex);
    }

    /**
     * @param verseIndex The index of the Verse to remove.
     *
     * @see java.util.List#remove(int)
     */
    public void removeVerse(final int verseIndex) {
        this.verses().remove(verseIndex);
    }

    /**
     * Duplicate the Verse at the given position (the copy is inserted right after the original).
     *
     * @param verseIndex The index of the Verse to duplicate.
     */
    public void duplicateVerse(final int verseIndex) {
        final TimeCodedVerse verse = this.verse(verseIndex).copy();
        this.verses().add(verseIndex, verse);
    }

    /**
     * Append a new Verse with a single line at the end.
     */
    public void appendTodoVerse() {
        final List<Line> lines = new ArrayList<>();
        lines.add(new Line(todoLineElements(), Set.of(), false, false));
        this.verses().add(new TimeCodedVerse(NO_RANGE, lines));
    }

    /******************************************************************************************************************
     *                                                LINE METHODS                                                    *
     ******************************************************************************************************************/
    private List<Line> lines(final int verseIndex) {
        return this.verse(verseIndex).lines();
    }

    /**
     * Try to merge all lines of the given verse.
     *
     * @param verseIndex The index of the Verse in the Lyrics.
     *
     * @return The new list of Line after trying to merge all lines in the verse, or {@code null} if the verse was
     * remove (empty one).
     */
    private List<Line> mergeLines(final int verseIndex) {
        final TimeCodedVerse verse = this.verse(verseIndex);
        final List<Line> merged = Line.merge(verse.lines());
        if (merged.isEmpty()) {
            this.removeVerse(verseIndex);
            return null;
        }
        this.verses().set(verseIndex, new TimeCodedVerse(verse.range(), merged));
        return this.lines(verseIndex);
    }

    /**
     * @param verseIndex The index of the Verse in the Lyrics.
     * @param lineIndex  The index of the Line in the Verse.
     *
     * @return The new list of Line composing the Verse, or {@code null} if the Verse is removed (empty one).
     */
    public List<Line> removeLine(
            final int verseIndex,
            final int lineIndex
    ) {
        this.lines(verseIndex).remove(lineIndex);
        return this.mergeLines(verseIndex);
    }

    /**
     * @param verseIndex     The index of the Verse in the Lyrics.
     * @param lineIndex      The index to add the new line in the Verse.
     * @param text           The text to parse and add.
     * @param isBackupVocals Whether this line shall be considered as Backup Vocals.
     * @param isNonLexical   Whether this line shall be considered as non-lexical vocables.
     * @param singers        The set of singers in charge of this line.
     *
     * @return The new list of Line for this Verse.
     */
    public List<Line> addLine(
            final int verseIndex,
            final int lineIndex,
            final CharSequence text,
            final boolean isBackupVocals,
            final boolean isNonLexical,
            final Collection<CharSequence> singers
    ) {
        final List<Line> parsed = SpaceCleanerLineParser.INSTANCE.parse(text);
        if (parsed.isEmpty()) return this.lines(verseIndex);

        final Line newLine = new Line(parsed.getFirst().elements(), Singer.from(singers), isBackupVocals, isNonLexical);
        this.lines(verseIndex).add(lineIndex, newLine);
        return this.mergeLines(verseIndex);
    }

    /**
     * @param verseIndex     The index of the Verse in the Lyrics.
     * @param lineIndex      The index of the line to update in the Verse.
     * @param text           The text to parse and set as the new text.
     * @param isBackupVocals Whether this line shall be considered as Backup Vocals.
     * @param isNonLexical   Whether this line shall be considered as non-lexical vocables.
     * @param singers        The set of singers in charge of this line.
     *
     * @return The new list of Line for this Verse.
     */
    public List<Line> updateLine(
            final int verseIndex,
            final int lineIndex,
            final CharSequence text,
            final boolean isBackupVocals,
            final boolean isNonLexical,
            final Collection<CharSequence> singers
    ) {
        final List<Line> parsed = SpaceCleanerLineParser.INSTANCE.parse(text);
        if (parsed.isEmpty()) return this.removeLine(verseIndex, lineIndex);

        final Line newLine = new Line(parsed.getFirst().elements(), Singer.from(singers), isBackupVocals, isNonLexical);
        this.lines(verseIndex).set(lineIndex, newLine);
        return this.mergeLines(verseIndex);
    }
}
