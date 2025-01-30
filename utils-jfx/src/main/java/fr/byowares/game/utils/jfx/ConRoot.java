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
package fr.byowares.game.utils.jfx;

import java.util.Objects;

/**
 * @param controller The controller managing graphical elements.
 * @param root       The root of those elements.
 * @param <C>        Type of the controller.
 * @param <R>        Type of the root.
 *
 * @since XXX
 */
public record ConRoot<C extends Controller, R>(
        C controller,
        R root
) {

    /**
     * @param controller The controller managing graphical elements.
     * @param root       The root of those elements.
     *
     * @throws NullPointerException If any of the argument is null.
     */
    public ConRoot(
            final C controller,
            final R root
    ) {
        this.controller = controller;
        this.root = Objects.requireNonNull(root);
    }

    /** @return The controller. */
    @Override
    public C controller() {
        return this.controller;
    }

    /** @return The root. */
    @Override
    public R root() {
        return this.root;
    }
}
