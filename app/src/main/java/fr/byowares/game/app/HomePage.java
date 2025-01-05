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

import fr.byowares.game.app.info.AppInfo;
import fr.byowares.game.utils.jfx.theme.ThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Entry point of the Java FX application.
 *
 * @since XXX
 */
public class HomePage
        extends Application {

    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

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
        final Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("fxml/HomePage.fxml")));
        final Scene scene = new Scene(root);
        ThemeManager.subscribe(scene);
        stage.setTitle("BYOWares Games " + AppInfo.VERSION);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

}
