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

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * @since XXX
 */
public class AppExceptionHandler
        implements Thread.UncaughtExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AppExceptionHandler.class);
    private final Stage stage;

    /**
     * @param stage The stage to use to open an Alert pop up.
     */
    public AppExceptionHandler(final Stage stage) {
        this.stage = stage;
    }

    @Override
    public void uncaughtException(
            final Thread t,
            final Throwable e
    ) {
        log.error("Uncaught exception", e);
        final var dialog = this.createExceptionDialog(e);
        if (dialog != null) {
            dialog.showAndWait();
        }
    }

    private Alert createExceptionDialog(final Throwable throwable) {
        Objects.requireNonNull(throwable);

        final var alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(throwable.getMessage());

        try (final var sw = new StringWriter();
             final var printWriter = new PrintWriter(sw)) {
            throwable.printStackTrace(printWriter);

            final Label label = new Label("The exception stacktrace was:");
            final TextArea textArea = new TextArea(sw.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            final GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
            alert.initOwner(this.stage);

            return alert;
        } catch (final IOException e) {
            log.error("Failed to close StringWriter", e);
            return null;
        }
    }
}
