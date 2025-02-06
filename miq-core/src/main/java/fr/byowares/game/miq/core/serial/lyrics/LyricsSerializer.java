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

import fr.byowares.game.miq.core.model.lyrics.Line;
import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.model.lyrics.Singer;
import fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse;
import fr.byowares.game.miq.core.serial.Constants;
import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.Version;
import fr.byowares.game.utils.text.CSVBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Offer the capacity to serialize {@link fr.byowares.game.miq.core.model.lyrics.Lyrics} in Yaml format.
 *
 * @since XXX
 */
public class LyricsSerializer
        extends Serializer<Lyrics> {

    /** Singleton pattern. */
    public static final LyricsSerializer INSTANCE = new LyricsSerializer();

    private static final char SEP = '#';

    private LyricsSerializer() {
        // Singleton pattern
    }

    @Override
    public void doSerialize(
            final Lyrics lyrics,
            final Map<String, Object> map
    ) {
        final List<CharSequence> verses = new ArrayList<>();
        final CSVBuilder builder = new CSVBuilder(SEP);
        for (final TimeCodedVerse verse : lyrics.getVerses()) {
            builder.newField(verse.range().start());
            builder.newField(verse.range().end());
            if (Line.EMPTY_LIST.equals(verse.lines())) builder.newField(0L);
            else {
                builder.newField(verse.lines().size());
                for (final Line line : verse.lines()) {
                    final int mask = (line.isBackVocals() ? LyricsDeserializerV1.BACK_VOCALS_BIT : 0) | //
                            (line.isNonLexical() ? LyricsDeserializerV1.NON_LEXICAL_BIT : 0);
                    builder.newField(mask);
                    builder.newField(line.getSingers(), Singer::name);
                    builder.newField(line.getClearText());
                }
            }
            verses.add(builder.asCharSequenceAndReset());
        }

        map.put(Constants.TITLE, lyrics.getName());
        map.put(Constants.COMMENT, lyrics.getComment());
        map.put(Constants.AUTHOR, lyrics.getAuthor());
        map.put(Constants.SEPARATOR, SEP);
        map.put(Constants.LYRICS, verses);
    }

    @Override
    protected Version getVersion() {
        return Constants.V1;
    }
}
