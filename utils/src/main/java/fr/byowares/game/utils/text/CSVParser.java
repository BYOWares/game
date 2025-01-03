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
 * A simple recyclable CSV (Character Separated Value) parser:
 * <ul>
 *     <li>Character separating fields can be chosen, but cannot be {@link #BACK_SLASH} or {@link #SPACE};</li>
 *     <li>{@link #BACK_SLASH} is used to escape the separator character and itself;</li>
 *     <li>{@link #SPACE} is used to join collection's elements together;</li>
 *     <li>String field cannot contain new line character;</li>
 *     <li>Double and simple quote hold no special meaning here.</li>
 * </ul>
 * When instantiated or reset, the first field position is still unknown. This prevents throwing any exception when
 * creating a new instance.
 * <blockquote>
 * <pre>
 *     CSVParser parser = new CSVParser('#', "valid\\"); // Always valid
 *     parser.asLong(); // Throws an IllegalStateException
 *     parser.asInt(); // Throws a IllegalStateException
 *     parser.asCharacterIterator(); // Throws a IllegalStateException
 *     parser.nextField(); // Throws an IllegalArgumentException because the line ends with a '\'
 *
 *     parser.setText("Still valid\\"); // Always valid
 *     // Behaves exactly the same as above.
 * </pre>
 * </blockquote>
 *
 * @since XXX
 */
public class CSVParser {

    /** Escape character. */
    static final char BACK_SLASH = '\\';
    /** List joiner character. */
    static final char SPACE = ' ';

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
    public CSVParser(
            final char separator,
            final CharSequence text
    ) {
        validateSeparator(separator);
        this.separator = separator;
        this.text = text;
        this.resetBeginEnd();
    }

    /**
     * @param separator The separator character to use.
     *
     * @throws java.lang.IllegalArgumentException If the provided separator is {@link #BACK_SLASH} or {@link #SPACE}.
     */
    static void validateSeparator(final char separator) {
        validateSeparator(separator, BACK_SLASH);
        validateSeparator(separator, SPACE);
    }

    private static void validateSeparator(
            final char separator,
            final char c
    ) {
        if (areCharEquals(separator, c)) throw new IllegalArgumentException("Separator cannot be '" + c + "'");
    }

    /**
     * @param a The first character to compare.
     * @param b The second character to compare.
     *
     * @return {@code true} if the two characters are equal, {@code false} otherwise.
     */
    static boolean areCharEquals(
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

    private void resetBeginEnd() {
        this.begin = -1;
        this.end = -1;
    }

    /**
     * Reset this parser to point to a new text. Separator cannot be changed. This method avoid allocating a new
     * {@link CSVParser} each time this method is called.
     *
     * @param text The {@code CharSequence} to parse.
     */
    public void setText(final CharSequence text) {
        this.text = text;
        this.resetBeginEnd();
    }

    /**
     * @return {@code true} if another there is another field after the current one, {@code false} otherwise.
     */
    public boolean hasNext() {
        return nextSeparatorPosition(this.text, this.separator, this.end + 1) > this.end;
    }

    /**
     * Move to the next field if any, and return it as a long value.
     *
     * @return The next field as a long value if possible.
     *
     * @see #nextField()
     * @see #asLong()
     */
    public long nextFieldAsLong() {
        this.nextField();
        return this.asLong();
    }

    /**
     * Move to the next field if any.
     *
     * @throws java.util.NoSuchElementException if the current field was already the last one.
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
        this.assertBeginEnd();
        return Long.parseLong(this.text, this.begin, this.end, 10);
    }

    private void assertBeginEnd() {
        if (this.begin == -1) throw new IllegalStateException(
                "Uninitialized CSVCharSequence, call nextField() or similar (nextAsLong(), etc.) to init it.");
    }

    /**
     * Move to the next field if any, and return it as an int value.
     *
     * @return The next field as an int value if possible.
     *
     * @see #nextField()
     * @see #asInt()
     */
    public int nextFieldAsInt() {
        this.nextField();
        return this.asInt();
    }

    /**
     * @return The current field as an int value if possible.
     *
     * @throws java.lang.NumberFormatException if the current field cannot be parsed as an int.
     */
    public int asInt() {
        this.assertBeginEnd();
        return Integer.parseInt(this.text, this.begin, this.end, 10);
    }

    /**
     * Move to the next field if any, and return it as a {@link java.text.CharacterIterator}.
     *
     * @return The next field as a CharacterIterator if possible.
     *
     * @see #nextField()
     * @see #asCharacterIterator()
     */
    public CharacterIterator nextFieldAsCharacterIterator() {
        this.nextField();
        return this.asCharacterIterator();
    }

    /**
     * @return A {@link java.text.CharacterIterator} allowing to iterate over oll character defined in this field,
     * except {@link #BACK_SLASH} used to escape the separator or itself.
     */
    public CharacterIterator asCharacterIterator() {
        this.assertBeginEnd();
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
        return "CSVCharSequence{" + //
                "separator=" + this.separator + //
                ", text=" + this.text + //
                ", begin=" + this.begin + //
                ", end=" + this.end + //
                (this.begin == -1 ? "" : ", currentValue=" + this.text.subSequence(this.begin, this.end)) + //
                '}';
    }
}
