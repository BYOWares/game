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
package fr.byowares.game.utils.jfx.concurrent;

import atlantafx.base.controls.ModalPane;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * A collection of method to run long task in background.
 *
 * @since XXX
 */
public final class BackgroundTasks {

    private static final Logger log = LoggerFactory.getLogger(BackgroundTasks.class);

    private BackgroundTasks() {
        // Utility class
    }

    private static ModalPane findModalPane(final Scene scene) {
        if (scene.getRoot() instanceof final StackPane p) {
            for (final Node node : p.getChildren()) {
                if (node instanceof final ModalPane modalPane) return modalPane;
            }
        }
        log.warn("Could not find any ModalPane in scene {}", scene);
        return null;
    }

    /**
     * Run a UI blocking task. Progress will be displayed in a persistent ModalPane.
     *
     * @param scene      The Scene whose UI must be blocked.
     * @param i18nBinder The text do display while performing the background task.
     * @param work       The actual task to perform.
     * @param onSuccess  Code to execute if the task is successful.
     * @param onFailure  Code to execute if the task is failed.
     * @param onCancel   Code to execute if the task is canceled.
     * @param <T>        Type of the object created by the task.
     */
    public static <T> void runBlocking(
            final Scene scene,
            final Consumer<StringProperty> i18nBinder,
            final TaskCallable<T> work,
            final Consumer<T> onSuccess,
            final Consumer<T> onFailure,
            final Consumer<T> onCancel
    ) {
        final ModalPane modalPane = findModalPane(scene);
        final Task<T> task = new Task<>() {

            @Override
            protected T call()
                    throws Exception {
                return work.call(this::updateProgress, this::isCancelled);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                this.updateMessage("succeeded");
                onSuccess.accept(this.getValue());
                this.hideModalPane();
            }

            @Override
            protected void cancelled() {
                super.cancelled();
                this.updateMessage("cancelled");
                onCancel.accept(this.getValue());
                this.hideModalPane();
            }

            @Override
            protected void failed() {
                super.failed();
                this.updateMessage("failed");
                onFailure.accept(this.getValue());
                this.hideModalPane();
            }

            private void hideModalPane() {
                if (modalPane != null) {
                    modalPane.setPersistent(false);
                    modalPane.hide(true);
                }
            }
        };

        if (modalPane != null) {
            final LoadingVBox loadingVBox = new LoadingVBox(i18nBinder);
            loadingVBox.bindProgress(task.progressProperty());
            modalPane.show(loadingVBox);
            modalPane.setPersistent(true);
        }

        GameThreadFactory.defaultExecutorService().submit(task);
    }

    /** A simple box to show a loading element. */
    private static class LoadingVBox
            extends VBox {

        private static final double PANE_SPACING = 3.0;

        private final ProgressBar progressBar;

        private LoadingVBox(final Consumer<StringProperty> i18nBinder) {
            super();

            this.setSpacing(PANE_SPACING);
            this.setPadding(new Insets(PANE_SPACING));
            this.setAlignment(Pos.CENTER);
            this.setStyle("-fx-background-color: -color-bg-default;"); // Transparent by default.

            final Label label = new Label();
            i18nBinder.accept(label.textProperty());
            HBox.setHgrow(label, Priority.ALWAYS);
            this.progressBar = new ProgressBar();
            final ReadOnlyDoubleProperty widthProp = label.widthProperty();
            final ReadOnlyDoubleProperty heightProp = label.heightProperty();

            this.progressBar.minWidthProperty().bind(widthProp);
            this.progressBar.maxWidthProperty().bind(widthProp);
            this.progressBar.minHeightProperty().bind(heightProp.divide(5));
            this.progressBar.maxHeightProperty().bind(heightProp.divide(5));

            this.minHeightProperty().bind(label.heightProperty().multiply(2));
            this.maxHeightProperty().bind(label.heightProperty().multiply(2));

            this.getChildren().addAll(label, this.progressBar);
        }

        private void bindProgress(final ReadOnlyDoubleProperty progress) {
            this.progressBar.progressProperty().bind(progress);
        }
    }
}
