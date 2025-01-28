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
package fr.byowares.game.app.fxml;

import fr.byowares.game.app.ResourcesApp;
import fr.byowares.game.app.i18n.I18NHomePage;
import fr.byowares.game.utils.jfx.ConRoot;
import fr.byowares.game.utils.jfx.theme.ThemeManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

/**
 * @since XXX
 */
public class GameAccess {

    @FXML
    private Button gameButton;
    @FXML
    private Text gameTitle;
    @FXML
    private Text gameDescription;

    /**
     * Load a new instance of the GameAccess element.
     *
     * @return The pair (Controller, HBox) making the GameAccess.
     *
     * @throws java.io.IOException If the resource could not be loaded.
     */
    public static ConRoot<GameAccess, HBox> load()
            throws IOException {
        final var loader = new FXMLLoader(Objects.requireNonNull(GameAccess.class.getResource("GameAccess.fxml")));
        final GameAccess ga = loader.getController();
        final HBox root = loader.load();
        return new ConRoot<>(ga, root);
    }

    @FXML
    public void initialize() {
        I18NHomePage.get().bind(this.gameTitle.textProperty(), "homepage.miq.title");
        I18NHomePage.get().bind(this.gameDescription.textProperty(), "homepage.miq.description");

        this.gameButton.getStylesheets().add(ResourcesApp.MIQ_CSS);
        ThemeManager.subscribe(this.gameButton);
    }
}
