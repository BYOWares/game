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
package fr.byowares.game.app;

import fr.byowares.game.app.fxml.HomePage;
import fr.byowares.game.app.i18n.I18NApp;
import fr.byowares.game.app.info.AppInfo;
import fr.byowares.game.utils.jfx.i18n.I18NLocaleManager;
import fr.byowares.game.utils.jfx.theme.ThemeManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * Entry point of the Java FX application.
 *
 * @since XXX
 */
public class App
        extends Application {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    /**
     * @param args Args used to start the application.
     */
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage)
            throws Exception {
        log.info("Starting {}", AppInfo.TO_STRING);
        I18NLocaleManager.updateLocale(Locale.ENGLISH);
        I18NApp.get().bind(stage.titleProperty(), "title", AppInfo.VERSION);
        stage.setMaximized(true);

        final StackPane root = new StackPane();
        root.setAlignment(Pos.TOP_LEFT);
        final Scene scene = new Scene(root);
        stage.setScene(scene);
        ThemeManager.subscribe(scene);

        final HomePage homepageController = HomePage.load(stage, root);
        homepageController.takeControlOfScene();

        stage.getIcons().add(ResourcesApp.GAME_16);
        stage.getIcons().add(ResourcesApp.GAME_32);
        stage.getIcons().add(ResourcesApp.GAME_64);
        stage.getIcons().add(ResourcesApp.GAME_128);
        stage.getIcons().add(ResourcesApp.GAME_256);
        stage.getIcons().add(ResourcesApp.GAME_512);
        stage.getIcons().add(ResourcesApp.GAME_1024);

        Platform.runLater(() -> {
            stage.show();
            stage.requestFocus();
        });
    }
}
