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

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Controller in charge of the root element of the scene. An actor is associated to a unique
 * {@link javafx.stage.Stage} and a unique {@link javafx.scene.Scene}. Thus, displaying graphical elements is easy.<br/>
 * An actor can have a preceding actor, from which he can take the stage (i.e. be the only one visible on the scene)
 * using {@link #takeControlOfScene()}, or hand it back using {@link #giveBackControlOfScene()}.
 *
 * @since XXX
 */
public abstract class SceneUniqueActor
        implements Controller {

    private final Stage stage;
    private final StackPane sceneRoot;
    private final SceneUniqueActor previousActor;
    private final List<Controller> controllers = new ArrayList<>();
    private Node actorRootNode;


    private SceneUniqueActor(
            final Stage stage,
            final StackPane sceneRoot,
            final SceneUniqueActor previousActor
    ) {
        this.stage = Objects.requireNonNull(stage);
        this.sceneRoot = Objects.requireNonNull(sceneRoot);
        this.previousActor = previousActor;
        this.addSelf();
    }

    /**
     * New actor with no predecessor (shall be called only once for the first page of the stage).
     *
     * @param stage     The stage on which the scene is displayed.
     * @param sceneRoot The root element of the scene.
     */
    public SceneUniqueActor(
            final Stage stage,
            final StackPane sceneRoot
    ) {
        this(stage, sceneRoot, null);
    }

    /**
     * New actor with {@code previousActor} as a predecessor.
     *
     * @param previousActor The previous actor, from which control can be taken from, or be given back.
     */
    public SceneUniqueActor(final SceneUniqueActor previousActor) {
        this(previousActor.stage, previousActor.sceneRoot, previousActor);
    }

    private static void switchController(
            final SceneUniqueActor newSceneUniqueActor,
            final SceneUniqueActor oldSceneUniqueActor
    ) {
        newSceneUniqueActor.doTakeControlOfScene();
        if (oldSceneUniqueActor != null) oldSceneUniqueActor.doGiveBackControlOfScene();
    }

    /** Dummy method to avoid the warning about escaping this in the private constructor. */
    private void addSelf() {
        this.addController(this);
    }

    /**
     * Add the given {@link fr.byowares.game.utils.jfx.Controller} to the list of controllers managed by this
     * {@link fr.byowares.game.utils.jfx.SceneUniqueActor}.
     *
     * @param controller The {@code Controller} to add.
     */
    public void addController(final Controller controller) {
        this.controllers.add(controller);
    }

    /**
     * @param actorRootNode The root node element of this actor.
     */
    public void setActorRootNode(final Node actorRootNode) {
        this.actorRootNode = Objects.requireNonNull(actorRootNode);
    }

    /**
     * @return The current stage used.
     */
    protected Stage getStage() {
        return this.stage;
    }

    /**
     * Give the control of the scene to the current controller.
     */
    public final void takeControlOfScene() {
        switchController(this, this.previousActor);
    }

    /**
     * Give back the control of the scene to the previous controller.
     */
    public final void giveBackControlOfScene() {
        Objects.requireNonNull(this.previousActor);
        switchController(this.previousActor, this);
    }

    private void doTakeControlOfScene() {
        this.controllers.forEach(Controller::onDisplay);
        this.sceneRoot.getChildren().add(this.actorRootNode);
    }

    private void doGiveBackControlOfScene() {
        this.sceneRoot.getChildren().remove(this.actorRootNode);
        this.controllers.forEach(Controller::onHide);
    }
}
