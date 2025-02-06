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

import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractSerializerTest<T extends NamedSourcedObject<T>>
        extends AbstractDesSerTest<T> {

    private static final List<StringInput> STRING_NON_NULL_VALUES = //
            List.of( //
                     new StringInput("A number", "23"), //
                     new StringInput("With punctuation", "Hey! Do *you* want _some_ punctuations ?"), //
                     new StringInput("With a tab", "A\ttab"), //
                     new StringInput("Leading and trailing space", "  Weird Spacing  "), //
                     new StringInput("Line feed", "New\nLine"), //
                     new StringInput("Carriage return and line feed", "New\r\nLine"), //
                     new StringInput("Line feed and extra spacing", "  New  \n  Line  "), //
                     new StringInput("Carriage return and line feed and extra spacing", "  New  \r\n  Line  "), //
                     new StringInput("'null' text", "null") //
            );
    private static final List<StringInput> STRING_ALL_VALUES = new ArrayList<>(STRING_NON_NULL_VALUES.size() + 1);

    static {
        STRING_ALL_VALUES.add(new StringInput("null", null));
        STRING_ALL_VALUES.addAll(STRING_NON_NULL_VALUES);
    }

    protected static Stream<StringInput> getStringNonNullValues() {
        return STRING_NON_NULL_VALUES.stream();
    }

    protected static Stream<StringInput> getStringValues() {
        return STRING_ALL_VALUES.stream();
    }

    protected void assertConversion(
            final T input,
            final T output
    ) {
        final StringWriter sw = new StringWriter();
        this.getSerializer().serialize(input, sw);
        final T actual = this.getDeserializers().deserialize(inputStream(sw.toString()));
        assertEquals(output, actual);
    }

    protected void assertBijection(final T input) {
        this.assertConversion(input, input);
    }

    protected <V> void assertBijection(
            final T input,
            final BiConsumer<T, V> updater,
            final V update
    ) {
        final T copy = input.copyInMemory();
        assertEquals(input, copy);
        updater.accept(input, update);
        assertNotEquals(input, copy); // We make sure the updated actually changed something
        this.assertBijection(input);
    }

    protected abstract Serializer<T> getSerializer();

    public record StringInput(
            String description,
            String value
    ) {
        @Override
        public String toString() {
            return "{desc='" + this.description + "', value='" + this.value + "'}";
        }
    }
}
