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
package fr.byowares.game.miq.core.model.lyrics;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * @param name The name of the singer.
 *
 * @since XXX
 */
public record Singer(CharSequence name)
        implements Comparable<Singer> {

    /**
     * @return An empty mutable set.
     */
    public static Set<Singer> emptySet() {
        return new TreeSet<>();
    }

    /**
     * Convert each element from the {@code collection} as a {@code Singer}.
     *
     * @param collection The collection to convert in a set of {@code Singer}.
     *
     * @return The set of {@code Singer}.
     */
    public static Set<Singer> from(final Collection<CharSequence> collection) {
        final var set = emptySet();
        for (final CharSequence seq : collection)
            set.add(new Singer(seq));
        return set;
    }

    @Override
    public int compareTo(final Singer o) {
        return CharSequence.compare(this.name, o.name);
    }
}
