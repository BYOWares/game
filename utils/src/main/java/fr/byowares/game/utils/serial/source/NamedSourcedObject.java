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
package fr.byowares.game.utils.serial.source;

import java.util.Objects;

/**
 * A generic object associated with a source. The source can be updated.
 *
 * @since XXX
 */
public abstract class NamedSourcedObject {

    private Source source;
    private CharSequence name;
    private CharSequence comment;

    /**
     * @param name   The name of this object.
     * @param source The source of this object to instantiate.
     *
     * @throws java.lang.NullPointerException If the {@code name} is {@code null}.
     */
    public NamedSourcedObject(
            final CharSequence name,
            final Source source
    ) {
        this.setName(name);
        this.source = source;
    }

    /**
     * A new sourced object whose source is {@link fr.byowares.game.utils.serial.source.SourceInMemory}.
     *
     * @param name The name of this object.
     *
     * @throws java.lang.NullPointerException If the {@code name} is {@code null}.
     */
    public NamedSourcedObject(final CharSequence name) {
        this(name, SourceInMemory.INSTANCE);
    }

    public static <N extends NamedSourcedObject> N copy(final N object) {
        final NamedSourcedObject copy = object.buildCopy(object.getName());
        if (Objects.equals(object.getClass(), copy.getClass())) {
            @SuppressWarnings("unchecked") final N n = (N) copy;
            n.setSource(object.getSource());
            n.setComment(object.getComment());
            return n;
        }
        throw new IllegalArgumentException("Unable to copy object: " + object + " (bad copy: " + copy + ")");
    }

    /**
     * @return The name of this object.
     */
    public CharSequence getName() {
        return this.name;
    }

    /**
     * @param name The new name of this object.
     *
     * @throws java.lang.NullPointerException If the {@code name} is {@code null}.
     */
    public void setName(final CharSequence name) {
        this.name = Objects.requireNonNull(name);
    }

    /**
     * @return The source of this object.
     */
    public Source getSource() {
        return this.source;
    }

    /**
     * @param source The new source for this object.
     */
    public void setSource(final Source source) {
        this.source = Objects.requireNonNull(source);
    }

    /**
     * @return Some text describing this object.
     */
    public CharSequence getComment() {
        return this.comment;
    }

    /**
     * @param comment Some text describing this object.
     */
    public void setComment(final CharSequence comment) {
        this.comment = comment;
    }

    /**
     * @param name The not nullable name of this object.
     *
     * @return A copy of this object whose source is {@link fr.byowares.game.utils.serial.source.SourceInMemory}.
     */
    protected abstract NamedSourcedObject buildCopy(final CharSequence name);
}
