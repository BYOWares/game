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
package fr.byowares.game.miq.core.serial;

import fr.byowares.game.miq.core.model.Lyrics;
import fr.byowares.game.miq.core.model.lyrics.Line;
import fr.byowares.game.miq.core.model.lyrics.Singer;
import fr.byowares.game.miq.core.model.lyrics.TimeCodedVerse;
import fr.byowares.game.miq.core.serial.v1.LyricsDeserializerV1;
import fr.byowares.game.utils.text.CSVBuilder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Offer the capacity to serialize {@link fr.byowares.game.miq.core.model.Lyrics} in Yaml format.
 *
 * @since XXX
 */
public class LyricsSerializer
        implements Serializer<Lyrics> {

    /** Singleton pattern. */
    public static final LyricsSerializer INSTANCE = new LyricsSerializer();

    private static final Version VERSION = Constants.V1;
    private static final char SEP = '#';
    private static final DumperOptions DUMPER_OPTIONS = new DumperOptions();

    static {
        DUMPER_OPTIONS.setIndent(2);
        DUMPER_OPTIONS.setPrettyFlow(true);
        DUMPER_OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    }

    private LyricsSerializer() {
        // Singleton pattern
    }

    @Override
    public void serialize(
            final Lyrics lyrics,
            final Writer writer
    ) {
        final Map<String, Object> map = new LinkedHashMap<>();
        final List<CharSequence> verses = new ArrayList<>();
        final CSVBuilder builder = new CSVBuilder(SEP);
        for (final TimeCodedVerse verse : lyrics.lyrics()) {
            builder.newField(verse.range().start());
            builder.newField(verse.range().end());
            if (Line.EMPTY_LIST.equals(verse.lines())) builder.newField(0L);
            else {
                builder.newField(verse.lines().size());
                for (final Line line : verse.lines()) {
                    final int mask = (line.isBackVocals() ? LyricsDeserializerV1.BACK_VOCALS_BIT : 0) | //
                            (line.isNonLexical() ? LyricsDeserializerV1.NON_LEXICAL_BIT : 0);
                    builder.newField(mask);
                    builder.newField(line.singers(), Singer::name);
                    builder.newField(line.getClearText());
                }
            }
            verses.add(builder.asCharSequenceAndReset());
        }
        map.put(Constants.VERSION, VERSION.version());
        map.put(Constants.COMMENT, lyrics.comment());
        map.put(Constants.SEPARATOR, SEP);
        map.put(Constants.LYRICS, verses);

        new Yaml(DUMPER_OPTIONS).dump(map, writer);
    }
}
