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
import fr.byowares.game.utils.jfx.Resources;
import fr.byowares.game.utils.jfx.i18n.I18NLocaleManager;
import fr.byowares.game.utils.jfx.theme.ThemeManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Locale;

/**
 * Entry point of the Java FX application.
 *
 * @since XXX
 */
public class App
        extends Application {

    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final double DEFAULT_SIZE = 12.0;

    /**
     * @param args Args used to start the application.
     */
    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * @param stage The stage to which icons must be added.
     */
    public static void addIcons(final Stage stage) {
        final ObservableList<Image> icons = stage.getIcons();
        icons.add(ResourcesApp.GAME_16);
        icons.add(ResourcesApp.GAME_32);
        icons.add(ResourcesApp.GAME_64);
        icons.add(ResourcesApp.GAME_128);
        icons.add(ResourcesApp.GAME_256);
        icons.add(ResourcesApp.GAME_512);
        icons.add(ResourcesApp.GAME_1024);
    }

    private static void loadFonts() {
        loadFont(Resources.FONT_BARECAST);
        loadFont(Resources.FONT_JETBRAINS_LIGHT);
        loadFont(Resources.FONT_JETBRAINS_MEDIUM);
    }

    private static void loadFont(final String url) {
        log.info("Loading font {} ...", url);
        final Font[] fonts = Font.loadFonts(url, DEFAULT_SIZE);
        if (fonts == null) {
            log.error("Failed to load {}", url);
            throw new RuntimeException("Failed to load " + url);
        }
        log.info("{} successfully loaded", Arrays.toString(fonts));
    }

    @Override
    public void start(final Stage stage) {
        log.info("Starting {}", AppInfo.TO_STRING);
        addIcons(stage);
        loadFonts();

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

        Platform.runLater(() -> {
            stage.show();
            stage.requestFocus();
        });
    }
}
