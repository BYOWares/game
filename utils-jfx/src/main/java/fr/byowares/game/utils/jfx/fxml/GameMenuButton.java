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
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

/**
 * A Simple button inside a {@link javafx.scene.layout.HBox}, whose text is managed by I18N.
 *
 * @since XXX
 */
public class GameMenuButton
        implements Controller {

    private final Consumer<StringProperty> binder;
    private final Runnable onAction;

    @FXML private Button button;

    private GameMenuButton(
            final Consumer<StringProperty> binder,
            final Runnable onAction
    ) {
        this.binder = binder;
        this.onAction = onAction;
    }

    /**
     * @param binder   The {@link javafx.beans.property.StringProperty} binder to the button's text.
     * @param onAction The action to be trigger when this button is fired.
     *
     * @return The pair (
     */
    public static Pair<GameMenuButton, HBox> load(
            final Consumer<StringProperty> binder,
            final Runnable onAction
    ) {
        return FXMLLoader.load(new GameMenuButton(binder, onAction), GameMenuButton.class, "GameMenuButton");
    }

    /**
     * @param value Whether the button is disabled or not.
     */
    public void setDisable(final boolean value) {
        this.button.setDisable(value);
    }

    /** FXML handle for initialization. */
    @FXML
    public void initialize() {
        this.binder.accept(this.button.textProperty());
    }

    /** FXML handle for clicked button */
    @FXML
    public void onAction() {
        this.onAction.run();
    }

    @Override
    public void onDisplay() {
        // Nothing to do
    }

    @Override
    public void onHide() {
        // Nothing to do
    }
}
