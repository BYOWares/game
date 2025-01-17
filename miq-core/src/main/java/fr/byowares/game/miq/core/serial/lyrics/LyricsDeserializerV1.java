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
package fr.byowares.game.miq.core.serial.lyrics;

import fr.byowares.game.miq.core.model.Range;
import fr.byowares.game.miq.core.model.lyrics.Line;
import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.model.lyrics.SpaceCleanerLineParser;
import fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse;
import fr.byowares.game.miq.core.serial.Constants;
import fr.byowares.game.utils.serial.Version;
import fr.byowares.game.utils.serial.VersionedDeserializer;
import fr.byowares.game.utils.text.CSVParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Version 1 for {@link fr.byowares.game.miq.core.model.lyrics.Lyrics} deserializer.
 *
 * @since XXX
 */
public class LyricsDeserializerV1
        extends VersionedDeserializer<Lyrics> {

    /** Singleton pattern. */
    public static final LyricsDeserializerV1 INSTANCE = new LyricsDeserializerV1();

    /** Bit mask for the backup vocal information. */
    public static final int BACK_VOCALS_BIT = 1;
    /** Bit mask for the non-lexical vocables information. */
    public static final int NON_LEXICAL_BIT = 2;

    private LyricsDeserializerV1() {
        // Singleton pattern
    }

    private static TimeCodedVerse parseAsVerse(
            final Object unparsedLyric,
            final CSVParser csv
    ) {
        csv.setText(Objects.toString(unparsedLyric));
        final Range range = new Range(csv.nextFieldAsLong(), csv.nextFieldAsLong());
        final int nbLines = csv.nextFieldAsInt();
        if (nbLines == 0) return new TimeCodedVerse(range, Line.EMPTY_LIST);

        final List<Line> lines = new ArrayList<>(nbLines);
        for (int line = 0; line < nbLines; line++) {
            final int annotations = csv.nextFieldAsInt();
            final boolean isBackupVocals = (annotations & BACK_VOCALS_BIT) != 0;
            final boolean isNonLexicalVocables = (annotations & NON_LEXICAL_BIT) != 0;
            final var singers = SpaceCleanerLineParser.parseAsSingers(csv.nextFieldAsCharacterIterator());
            final var elements = SpaceCleanerLineParser.parseLineElements(csv.nextFieldAsCharacterIterator());
            lines.add(new Line(elements, singers, isBackupVocals, isNonLexicalVocables));
        }
        return new TimeCodedVerse(range, lines);
    }

    @Override
    protected Lyrics doDeserialize(final Map<String, Object> map) {
        final String comment = removeAsString(map, Constants.COMMENT);
        final String author = removeAsString(map, Constants.AUTHOR);
        final char separator = removeAsCharacter(map, Constants.SEPARATOR);
        final List<?> unparsedLyrics = Objects.requireNonNull(remove(map, Constants.LYRICS, List.class, null));
        final CSVParser csv = new CSVParser(separator, "");

        final List<TimeCodedVerse> lyrics = new ArrayList<>(unparsedLyrics.size());
        for (final Object unparsedLyric : unparsedLyrics) {
            lyrics.add(parseAsVerse(unparsedLyric, csv));
        }

        return new Lyrics(comment, author, lyrics);
    }

    @Override
    protected Version getVersion() {
        return Constants.V1;
    }
}
