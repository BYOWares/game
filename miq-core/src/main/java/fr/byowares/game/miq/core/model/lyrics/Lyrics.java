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

import fr.byowares.game.miq.core.model.Range;
import fr.byowares.game.utils.hashcodes.HashCodes;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A mutable object representing lyrics for the song.
 *
 * @since XXX
 */
public final class Lyrics
        extends NamedSourcedObject<Lyrics> {

    private static final Range NO_RANGE = Range.fromRelativeValues(Long.MAX_VALUE, 0L);

    private final List<TimeCodedVerse> verses;
    private CharSequence author;

    /**
     * A default Lyrics (comment is empty, author is null, and verses are also empty).
     *
     * @param name   The name used to identify those lyrics.
     * @param verses The verses (text + time codes) making those lyrics.
     */
    public Lyrics(
            final CharSequence name,
            final List<TimeCodedVerse> verses
    ) {
        super(name);
        this.verses = Objects.requireNonNull(verses);
    }

    private static List<LineElement> todoLineElements() {
        final List<LineElement> elements = new ArrayList<>();
        elements.add(new Word("TODO"));
        return elements;
    }

    /**
     * @return The author of those lyrics.
     */
    public CharSequence getAuthor() {
        return this.author;
    }

    /**
     * @param author Update the author of those lyrics.
     */
    public void setAuthor(final CharSequence author) {
        this.author = author;
    }

    /**
     * @return The list of {@link fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse} composing those lyrics.
     */
    public List<TimeCodedVerse> getVerses() {
        return this.verses;
    }

    /******************************************************************************************************************
     *                                                VERSE METHODS                                                   *
     ******************************************************************************************************************/
    private TimeCodedVerse verse(final int verseIndex) {
        return this.verses.get(verseIndex);
    }

    /**
     * @param verseIndex The index of the Verse to remove.
     *
     * @see java.util.List#remove(int)
     */
    public void removeVerse(final int verseIndex) {
        this.verses.remove(verseIndex);
    }

    /**
     * Duplicate the Verse at the given position (the copy is inserted right after the original).
     *
     * @param verseIndex The index of the Verse to duplicate.
     */
    public void duplicateVerse(final int verseIndex) {
        final TimeCodedVerse verse = this.verse(verseIndex).copy();
        this.verses.add(verseIndex, verse);
    }

    /**
     * Append a new Verse with a single line at the end.
     */
    public void appendTodoVerse() {
        final List<Line> lines = new ArrayList<>();
        lines.add(new Line(todoLineElements(), Set.of(), false, false));
        this.verses.add(new TimeCodedVerse(NO_RANGE, lines));
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
        this.verses.set(verseIndex, new TimeCodedVerse(verse.range(), merged));
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

        final Line newLine = parsed.getFirst().copy(singers, isBackupVocals, isNonLexical);
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

        final Line newLine = parsed.getFirst().copy(singers, isBackupVocals, isNonLexical);
        this.lines(verseIndex).set(lineIndex, newLine);
        return this.mergeLines(verseIndex);
    }

    /******************************************************************************************************************
     *                                                LINE METHODS                                                    *
     ******************************************************************************************************************/

    @Override
    public int hashCode() {
        return HashCodes.hash(this.verses, this.author, this.getComment());
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Lyrics lyrics)) return false;
        return Objects.equals(this.getName(), lyrics.getName()) //
                && Objects.equals(this.verses, lyrics.verses) //
                && Objects.equals(this.author, lyrics.author) //
                && Objects.equals(this.getComment(), lyrics.getComment());
    }

    @Override
    public String toString() {
        return "Lyrics{" + "name=" + this.getName() + ", author=" + this.author + ", comment=" + this.getComment() + '}';
    }

    @Override
    public Lyrics copyInMemory() {
        final Lyrics lyrics = new Lyrics(this.getName(), new ArrayList<>(this.getVerses()));
        lyrics.setAuthor(this.getAuthor());
        lyrics.setComment(this.getComment());
        return lyrics;
    }
}
