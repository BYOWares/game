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

import java.text.CharacterIterator;
import java.util.BitSet;
import java.util.NoSuchElementException;

/**
 * A simple recyclable CSV (Character separated value) parser. Character separating fields can be chosen, but cannot be
 * {@link #BACK_SLASH}.
 *
 * @since XXX
 */
public class CSVCharSequence {

    private static final char BACK_SLASH = '\\';
    private static final ThreadLocal<BitSet> TL_BITSET = ThreadLocal.withInitial(BitSet::new);
    private static final ThreadLocal<CharSequenceIterator> TL_CSI = //
            ThreadLocal.withInitial(() -> new CharSequenceIterator(""));
    private static final ThreadLocal<HoledCharSequenceIterator> TL_HOLED_CSI = //
            ThreadLocal.withInitial(() -> new HoledCharSequenceIterator(""));

    private final char separator;
    private CharSequence text;
    private int begin;
    private int end;

    /**
     * @param separator The separator used to distinguish fields.
     * @param text      The text to parse as a CSV text.
     */
    public CSVCharSequence(
            final char separator,
            final CharSequence text
    ) {
        if (areCharEquals(separator, BACK_SLASH)) throw new IllegalArgumentException("Separator cannot be \\");
        this.separator = separator;
        this.text = text;
        this.begin = 0;
        this.end = nextSeparatorPosition(text, separator, 0);
    }

    private static boolean areCharEquals(
            final char a,
            final char b
    ) {
        return (int) a == (int) b;
    }

    private static int nextSeparatorPosition(
            final CharSequence text,
            final char sep,
            final int startingIndex
    ) {
        boolean isLastCharBackSlash = false;
        for (int index = startingIndex; index < text.length(); index++) {
            final char current = text.charAt(index);
            if (isLastCharBackSlash) {
                if (!areCharEquals(current, BACK_SLASH) && !areCharEquals(current, sep)) {
                    throw new IllegalArgumentException(
                            "Backslash shall precede only backslash or separator (" + sep + "), not: '" + current + "'");
                }
                isLastCharBackSlash = false;
                continue;
            }
            isLastCharBackSlash = areCharEquals(current, BACK_SLASH);
            if (areCharEquals(current, sep)) return index;
        }
        if (isLastCharBackSlash)
            throw new IllegalArgumentException("Last character on line was a single \\, which is forbidden");

        return text.length();
    }

    /**
     * Reset this parser to point to a new text. Separator cannot be changed. This method avoid allocating a new
     * {@link fr.byowares.game.utils.text.CSVCharSequence} each time this method is called.
     *
     * @param text The {@code CharSequence} to parse.
     */
    public void setText(final CharSequence text) {
        this.text = text;
        this.begin = 0;
        this.end = nextSeparatorPosition(text, this.separator, 0);
    }

    /**
     * @return {@code true} if another there is another field after the current one, {@code false} otherwise.
     */
    public boolean hasNext() {
        return nextSeparatorPosition(this.text, this.separator, this.end + 1) > this.end;
    }

    /**
     * Move to the next element if any.
     *
     * @throws java.util.NoSuchElementException if the current element was already the last one.
     */
    public void nextField() {
        final int nextEnd = nextSeparatorPosition(this.text, this.separator, this.end + 1);
        if (nextEnd == this.end) throw new NoSuchElementException();
        this.begin = this.end + 1;
        this.end = nextEnd;
    }

    /**
     * @return The current field as a long value if possible.
     *
     * @throws java.lang.NumberFormatException if the current field cannot be parsed as a long.
     */
    public long asLong() {
        return Long.parseLong(this.text, this.begin, this.end, 10);
    }

    /**
     * @return A {@link java.text.CharacterIterator} allowing to iterate over oll character defined in this field,
     * except {@link #BACK_SLASH} used to escape the separator or itself.
     */
    public CharacterIterator asCharacterIterator() {
        final BitSet bitSet = TL_BITSET.get();
        bitSet.clear();
        boolean isLastCharBackSlash = false;
        for (int index = this.begin; index < this.end; index++) {
            if (isLastCharBackSlash) {
                isLastCharBackSlash = false;
                continue;
            }
            if (areCharEquals(this.text.charAt(index), BACK_SLASH)) {
                bitSet.set(index);
                isLastCharBackSlash = true;
            }
        }

        if (bitSet.isEmpty()) {
            final CharSequenceIterator csi = TL_CSI.get();
            csi.setText(this.text, this.begin, this.end);
            return csi;
        } else {
            final HoledCharSequenceIterator csi = TL_HOLED_CSI.get();
            final BitSet copy = new BitSet();
            copy.or(bitSet);
            csi.setText(this.text, this.begin, this.end, copy);
            return csi;
        }
    }

    @Override
    public String toString() {
        return this.text.subSequence(this.begin, this.end).toString();
    }
}
