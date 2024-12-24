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

/**
 * Similar to {@link fr.byowares.game.utils.text.CharSequenceIterator} with the ability to skip some characters at
 * given positions, also known as holes.
 *
 * @since XXX
 */
public class HoledCharSequenceIterator
        implements CharacterIterator {

    private static final BitSet EMPTY_BITSET = new BitSet();

    private CharSequence text;
    private BitSet holes;
    private int begin;
    private int end;
    // invariant: begin <= pos <= end
    private int pos;

    /**
     * Constructs an iterator with an initial index of 0, wrapping the whole {@code text}, and with no characters to skip.
     *
     * @param text The {@code CharSequence} to be iterated over.
     */
    public HoledCharSequenceIterator(final CharSequence text) {
        this(text, EMPTY_BITSET);
    }

    /**
     * Constructs an iterator with an initial index of 0, wrapping the whole {@code text}, with characters to skip.
     *
     * @param text  The {@code CharSequence} to be iterated over.
     * @param holes The characters' position to skip.
     */
    public HoledCharSequenceIterator(
            final CharSequence text,
            final BitSet holes
    ) {
        this(text, 0, text.length(), holes);
    }


    /**
     * Constructs an iterator over the given range of the given {@code CharSequence}, with the
     * index set at the specified position, and some characters to skip.
     *
     * @param text  The {@code CharSequence} to be iterated over
     * @param begin Index of the first character.
     * @param end   Index of the character following the last character.
     * @param holes The characters' position to skip.
     */
    public HoledCharSequenceIterator(
            final CharSequence text,
            final int begin,
            final int end,
            final BitSet holes
    ) {
        this.setText(text, begin, end, holes);
    }

    /**
     * Reset this iterator to point to a new CharSequence. This method avoid allocating new HoledCharSequenceIterator
     * objects every time their setText method is called.
     *
     * @param text  The {@code CharSequence} to be iterated over.
     * @param begin Index of the first character.
     * @param end   Index of the character following the last character.
     * @param holes Characters' position to skip.
     */
    public void setText(
            final CharSequence text,
            final int begin,
            final int end,
            final BitSet holes
    ) {
        if (text == null) throw new NullPointerException();
        this.text = text;

        if (begin < 0 || begin > end || end > text.length()) throw new IllegalArgumentException(
                "Invalid range (0<=begin[" + begin + "]<=end[" + end + "]<=length[" + text.length() + "])");

        this.begin = begin;
        this.end = end;
        this.pos = begin;
        this.holes = holes;
        this.nextPositionNoHoles();
    }

    private void nextPositionNoHoles() {
        this.pos = this.holes.nextClearBit(this.pos);
    }

    @Override
    public char first() {
        this.pos = this.begin;
        this.nextPositionNoHoles();
        return this.current();
    }

    @Override
    public char last() {
        if (this.end != this.begin) {
            this.pos = this.end - 1;
        } else {
            this.pos = this.end;
            return DONE;
        }
        this.previousNoHoles();
        return this.current();
    }

    private void previousNoHoles() {
        if (this.pos < this.begin) return;
        this.pos = this.holes.previousClearBit(this.pos);
    }

    @Override
    public char current() {
        if (this.pos >= this.begin && this.pos < this.end) {
            return this.text.charAt(this.pos);
        } else {
            return DONE;
        }
    }

    @Override
    public char next() {
        this.pos++;
        this.nextPositionNoHoles();
        if (this.pos < this.end) {
            return this.text.charAt(this.pos);
        } else {
            this.pos = this.end;
            return DONE;
        }
    }

    @Override
    public char previous() {
        this.pos--;
        this.previousNoHoles();
        if (this.pos >= this.begin) {
            return this.text.charAt(this.pos);
        } else {
            this.pos = this.begin - 1;
            return DONE;
        }
    }

    @Override
    public char setIndex(final int p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBeginIndex() {
        return this.begin;
    }

    @Override
    public int getEndIndex() {
        return this.end;
    }

    @Override
    public int getIndex() {
        return this.pos;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}
