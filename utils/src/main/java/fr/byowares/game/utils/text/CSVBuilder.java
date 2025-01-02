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
package fr.byowares.game.utils.text;

import java.util.Collection;
import java.util.function.Function;

import static fr.byowares.game.utils.text.CSVParser.BACK_SLASH;
import static fr.byowares.game.utils.text.CSVParser.areCharEquals;
import static fr.byowares.game.utils.text.CSVParser.validateSeparator;

/**
 * Offer the capacity to build CSV text that can be parsed by a {@link CSVParser}. An internal buffer is updated as
 * we build the CSV text value.
 *
 * @since XXX
 */
public class CSVBuilder {

    private final char separator;
    private final StringBuilder buffer;

    /**
     * @param separator The separator used to separate fields.
     *
     * @see fr.byowares.game.utils.text.CSVParser#validateSeparator(char)
     */
    public CSVBuilder(final char separator) {
        validateSeparator(separator);
        this.separator = separator;
        this.buffer = new StringBuilder();
    }

    /**
     * Add a {@link #separator} character if not the first field, then the long value as text.
     *
     * @param value The long value to append.
     */
    public void newField(final long value) {
        this.appendSeparatorIfNeeded(this.buffer);
        this.buffer.append(value);
    }

    private void appendSeparatorIfNeeded(final StringBuilder sb) {
        if (!sb.isEmpty()) sb.append(this.separator);
    }

    /**
     * Add a {@link #separator} character if not the first field, then the int value as text.
     *
     * @param value The int value to append.
     */
    public void newField(final int value) {
        this.appendSeparatorIfNeeded(this.buffer);
        this.buffer.append(value);
    }

    /**
     * Add a {@link #separator} character if not the first field, then the text. Any
     * {@link CSVParser#BACK_SLASH} and {@link #separator} characters found in the text are escaped (using
     * {@link CSVParser#BACK_SLASH}).
     *
     * @param text The text to append.
     */
    public void newField(final CharSequence text) {
        this.appendSeparatorIfNeeded(this.buffer);
        this.appendText(text);
    }

    private void appendText(final CharSequence text) {
        for (int index = 0; index < text.length(); index++) {
            final char c = text.charAt(index);
            if (areCharEquals(c, BACK_SLASH) || areCharEquals(c, this.separator)) this.buffer.append(BACK_SLASH);
            this.buffer.append(c);
        }
    }

    /**
     * Add a {@link #separator} character if not the first field, then add each element of the collection, each
     * separated by a single space.
     *
     * @param collection     The collection to add.
     * @param toCharSequence The function transforming each element of the collection to {@link java.lang.CharSequence}.
     * @param <T>            The type of each element in the collection.
     */
    public <T> void newField(
            final Collection<T> collection,
            final Function<T, CharSequence> toCharSequence
    ) {
        this.appendSeparatorIfNeeded(this.buffer);
        boolean isFirst = true;
        for (final T t : collection) {
            if (!isFirst) this.buffer.append(' ');
            this.appendText(toCharSequence.apply(t));
            isFirst = false;
        }
    }

    /**
     * @return The text build so far, and reset it (so that it can be reused).
     */
    public CharSequence asCharSequenceAndReset() {
        final String res = this.buffer.toString();
        this.buffer.setLength(0);
        return res;
    }
}
