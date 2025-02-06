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
package fr.byowares.game.utils.jfx.fxml;

import fr.byowares.game.utils.jfx.Controller;
import fr.byowares.game.utils.jfx.Pair;
import fr.byowares.game.utils.jfx.SceneUniqueActor;
import javafx.scene.Node;
import org.agrona.LangUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * A utility class to load FXML files. Module using this class shall open its package using it to this module.
 *
 * @since XXX
 */
public final class FXMLLoader {

    private FXMLLoader() {
        // Utility class
    }

    /**
     * @param controller The {@link fr.byowares.game.utils.jfx.Controller} to associate to the graphical elements.
     * @param cl         The class from which the resource must be loaded.
     * @param filename   FXML resource's file name without the {@code .fxml} extension
     * @param <C>        The type of the {@code controller}.
     * @param <R>        The type of the root Node of the graphical elements.
     *
     * @return The pair (Controller, Root node).
     */
    public static <C extends Controller, R> Pair<C, R> load(
            final C controller,
            final Class<?> cl,
            final String filename
    ) {
        return new Pair<>(controller, loadAux(controller, cl, filename));
    }

    /**
     * @param actor    The {@link fr.byowares.game.utils.jfx.SceneUniqueActor} to associate to the graphical elements.
     * @param cl       The class from which the resource must be loaded.
     * @param filename FXML resource's file name without the {@code .fxml} extension
     * @param <A>      The type of the {@code actor}.
     *
     * @return The actor associated with the graphical elements from the {@code filename} FXML file. The root of them
     * will be set as the root of the {@code actor}.
     *
     * @see fr.byowares.game.utils.jfx.SceneUniqueActor#setActorRootNode(javafx.scene.Node)
     */
    public static <A extends SceneUniqueActor> A loadActor(
            final A actor,
            final Class<?> cl,
            final String filename
    ) {
        final Node root = loadAux(actor, cl, filename);
        actor.setActorRootNode(root);
        return actor;
    }

    private static <C, R> R loadAux(
            final C controller,
            final Class<?> cl,
            final String filename
    ) {
        final var loader = new javafx.fxml.FXMLLoader();
        loader.setLocation(Objects.requireNonNull(cl.getResource(filename + ".fxml")));
        loader.setController(controller);

        try {
            return loader.load();
        } catch (final IOException e) {
            LangUtil.rethrowUnchecked(e);
            throw new RuntimeException(e); // Unreachable
        }
    }
}
