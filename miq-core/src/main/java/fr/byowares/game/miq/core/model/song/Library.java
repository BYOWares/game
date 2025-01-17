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

import fr.byowares.game.utils.serial.source.SourcedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Collection of {@link fr.byowares.game.miq.core.model.song.Album}, offering the basic operations to manipulate them.
 *
 * @since XXX
 */
public class Library
        extends SourcedObject {

    private final Album undefined;
    private final List<Album> albums;
    private CharSequence name;

    /**
     * A new library with a dangling album (used to group all song not attached to any album).
     *
     * @param name The name of this library.
     */
    public Library(final CharSequence name) {
        this.undefined = new Album("__UNDEFINED__");
        this.albums = new ArrayList<>();
        this.albums.add(this.undefined);
        this.setName(name);
    }

    /**
     * @return The name of this Library.
     */
    public CharSequence getName() {
        return this.name;
    }

    /**
     * @param name The name to set for this Library.
     */
    public void setName(final CharSequence name) {
        this.name = Objects.requireNonNull(name);
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
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Library library)) return false;
        return Objects.equals(this.name, library.name);
    }

    @Override
    public String toString() {
        return "{Library=" + this.name + ", source=" + this.getSource() + ", albums=" + this.albums.size() + '}';
    }
}
