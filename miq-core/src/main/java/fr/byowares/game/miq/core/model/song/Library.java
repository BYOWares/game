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
package fr.byowares.game.miq.core.model.song;

import fr.byowares.game.utils.hashcodes.HashCodes;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Collection of {@link fr.byowares.game.miq.core.model.song.Album}, offering the basic operations to manipulate them.
 *
 * @since XXX
 */
public class Library
        extends NamedSourcedObject<Library> {

    private final Album undefined;
    private final List<Album> albums;

    /**
     * A new library with a dangling album (used to group all song not attached to any album).
     *
     * @param name The name of this library.
     */
    public Library(final CharSequence name) {
        super(name);
        this.undefined = new Album("__UNDEFINED__");
        this.albums = new ArrayList<>();
        this.albums.add(this.undefined);
        this.setName(name);
    }

    /**
     * @return The list of albums handled by this Library.
     */
    public List<Album> getAlbums() {
        return this.albums;
    }

    /**
     * @return The dangling album.
     */
    public Album getUndefined() {
        return this.undefined;
    }

    @Override
    public int hashCode() {
        return HashCodes.hash(this.getName(), this.getComment());
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Library library)) return false;
        return Objects.equals(this.getName(), library.getName()) //
                && Objects.equals(this.getComment(), library.getComment());
    }

    @Override
    public String toString() {
        return "{Library=" + this.getName() + ", source=" + this.getSource() + ", comment=" + this.getComment() + //
                ", albums=" + this.albums.size() + '}';
    }

    @Override
    public Library copyInMemory() {
        final Library library = new Library(this.getName());
        library.setComment(this.getComment());
        return library;
    }
}
