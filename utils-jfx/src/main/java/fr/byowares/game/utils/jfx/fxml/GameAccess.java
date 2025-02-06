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
import fr.byowares.game.utils.jfx.theme.ThemeManager;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

/**
 * A sceneUniqueActor to access a game from the HomePage. Graphics manage by this sceneUniqueActor are simple:
 * <ul>
 *     <li>A button styled with the class {@code game-button}. The class must be defined as a background image in the
 *     custom style sheet given:
 *     <blockquote><pre>
 * .game-button      { -fx-background-image: url("../icon/logo.png"); }
 *     </pre></blockquote>
 *     If there is an image for both dark and light themes, the custom CSS should look like this instead:
 *     <blockquote><pre>
 * .game-button      { -fx-background-image: url("../icon/logo_for_light_theme.png"); }
 * .game-button:dark { -fx-background-image: url("../icon/logo_for_dark_theme.png");  }
 *     </pre></blockquote></li>
 *     <li>A title, which is the name of the game. The name is expected to remain in english (font doesn't support
 *     special characters).</li>
 *     <li>A description.</li>
 * </ul>
 *
 * @since XXX
 */
public abstract class GameAccess
        implements Controller {

    private final SceneUniqueActor sceneUniqueActor;
    private final Consumer<StringProperty> titleBinder;
    private final Consumer<StringProperty> descBinder;
    private final String styleSheetURL;

    @FXML private Button icon;
    @FXML private Text title;
    @FXML private Text desc;

    /**
     * @param sceneUniqueActor The {@link fr.byowares.game.utils.jfx.SceneUniqueActor} in charge of the scene while this graphic is displayed.
     * @param titleBinder      The {@link javafx.beans.property.StringProperty} binder to the Title.
     * @param descBinder       The {@link javafx.beans.property.StringProperty} binder to the Description.
     * @param styleSheetURL    The Custom CSS to use (to skin the button).
     */
    protected GameAccess(
            final SceneUniqueActor sceneUniqueActor,
            final Consumer<StringProperty> titleBinder,
            final Consumer<StringProperty> descBinder,
            final String styleSheetURL
    ) {
        this.sceneUniqueActor = sceneUniqueActor;
        this.titleBinder = titleBinder;
        this.descBinder = descBinder;
        this.styleSheetURL = styleSheetURL;
    }

    /**
     * Load the graphics elements in the controller {@code ga}.
     *
     * @param ga  The controller to manage those graphical elements.
     * @param <G> The type of the controller.
     *
     * @return The pair (controller, HBox) making the GameAccess.
     */
    protected static <G extends GameAccess> Pair<G, HBox> load(final G ga) {
        return FXMLLoader.load(ga, GameAccess.class, "GameAccess");
    }

    /**
     * @return The {@link fr.byowares.game.utils.jfx.SceneUniqueActor} in charge of this controller.
     */
    public SceneUniqueActor getActorOwner() {
        return this.sceneUniqueActor;
    }

    /** FXML handle for initialization. */
    @FXML
    public void initialize() {
        this.titleBinder.accept(this.title.textProperty());
        this.descBinder.accept(this.desc.textProperty());

        this.icon.getStylesheets().add(this.styleSheetURL);
    }

    /** Actions to be done when clicked. */
    @FXML
    public abstract void onAction();

    @Override
    public final void onDisplay() {
        ThemeManager.subscribe(this.icon);
    }

    @Override
    public final void onHide() {
        ThemeManager.unsubscribe(this.icon);
    }
}
