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

/**
 * A {@link java.lang.CharSequence} {@link java.text.CharacterIterator} (a plain copy of
 * {@link java.text.StringCharacterIterator} to be able to work directly on CharSequence instead of String).
 *
 * @since XXX
 */
public class CharSequenceIterator
        implements CharacterIterator {

    private CharSequence text;
    private int begin;
    private int end;
    // invariant: begin <= pos <= end
    private int pos;

    /**
     * Constructs an iterator with an initial index of 0.
     *
     * @param text The {@code CharSequence} to be iterated over.
     */
    public CharSequenceIterator(final CharSequence text) {
        this(text, 0);
    }

    /**
     * Constructs an iterator with the specified initial index.
     *
     * @param text The {@code CharSequence} to be iterated over.
     * @param pos  Initial iterator position.
     */
    public CharSequenceIterator(
            final CharSequence text,
            final int pos
    ) {
        this(text, 0, text.length(), pos);
    }

    /**
     * Constructs an iterator over the given range of the given {@code CharSequence}, with the
     * index set at the specified position.
     *
     * @param text  The {@code CharSequence} to be iterated over
     * @param begin Index of the first character.
     * @param end   Index of the character following the last character.
     * @param pos   Initial iterator position.
     */
    public CharSequenceIterator(
            final CharSequence text,
            final int begin,
            final int end,
            final int pos
    ) {
        if (text == null) throw new NullPointerException();
        this.text = text;

        if (begin < 0 || begin > end || end > text.length())
            throw new IllegalArgumentException("Invalid substring range");

        if (pos < begin || pos > end) throw new IllegalArgumentException("Invalid position");

        this.begin = begin;
        this.end = end;
        this.pos = pos;
    }

    /**
     * Reset this iterator to point to a new CharSequence. This method avoid allocating new CharSequenceIterator objects
     * every time their setText method is called.
     *
     * @param text  The {@code CharSequence} to be iterated over.
     * @param begin Index of the first character.
     * @param end   Index of the character following the last character.
     */
    public void setText(
            final CharSequence text,
            final int begin,
            final int end
    ) {
        if (text == null) throw new NullPointerException();
        this.text = text;

        if (begin < 0 || begin > end || end > text.length())
            throw new IllegalArgumentException("Invalid substring range");

        this.begin = begin;
        this.end = end;
        this.pos = begin;
    }

    @Override
    public char first() {
        this.pos = this.begin;
        return this.current();
    }

    @Override
    public char last() {
        if (this.end != this.begin) {
            this.pos = this.end - 1;
        } else {
            this.pos = this.end;
        }
        return this.current();
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
        if (this.pos < this.end - 1) {
            this.pos++;
            return this.text.charAt(this.pos);
        } else {
            this.pos = this.end;
            return DONE;
        }
    }

    @Override
    public char previous() {
        if (this.pos > this.begin) {
            this.pos--;
            return this.text.charAt(this.pos);
        } else {
            return DONE;
        }
    }

    @Override
    public char setIndex(final int p) {
        if (p < this.begin || p > this.end) throw new IllegalArgumentException("Invalid index");
        this.pos = p;
        return this.current();
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
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof final CharSequenceIterator that)) return false;

        if (this.hashCode() != that.hashCode()) return false;
        if (!this.text.equals(that.text)) return false;
        return this.pos == that.pos && this.begin == that.begin && this.end == that.end;
    }

    @Override
    public int hashCode() {
        return this.text.hashCode() ^ this.pos ^ this.begin ^ this.end;
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
