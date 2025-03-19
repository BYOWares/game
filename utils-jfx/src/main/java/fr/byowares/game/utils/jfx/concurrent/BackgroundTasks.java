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
import atlantafx.base.controls.RingProgressIndicator;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
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
import java.util.function.Supplier;

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

    private static <T> void runBlocking(
            final ReadOnlyObjectProperty<Scene> sceneProp,
            final Supplier<LoadingVBox> loadingVBoxSupplier,
            final TaskCallable<T> work,
            final Consumer<T> onSuccess,
            final Consumer<T> onFailure,
            final Consumer<T> onCancel
    ) {
        final SimpleObjectProperty<ModalPane> modalPane = new SimpleObjectProperty<>();
        final Consumer<Scene> modalPaneFinder = scene -> {
            if (scene.getRoot() instanceof final StackPane stackPane) {
                for (final Node node : stackPane.getChildren()) {
                    if (node instanceof final ModalPane mp) {
                        modalPane.setValue(mp);
                        return;
                    }
                }
            }
            log.warn("Could not find any ModalPane in scene {}", scene);
        };
        final ChangeListener<Scene> sceneListener = doOrAddListener(sceneProp, modalPaneFinder);
        final Task<T> task = createTask(sceneProp, sceneListener, modalPane, work, onSuccess, onFailure, onCancel);

        final Consumer<ModalPane> showModalPane = p -> {
            final var loadingBox = loadingVBoxSupplier.get();
            loadingBox.bindProgress(task.progressProperty());
            p.show(loadingBox);
            p.setPersistent(true);
        };
        doOrAddListener(modalPane, showModalPane);

        GameThreadFactory.defaultExecutorService().submit(task);
    }

    /**
     * Run a UI blocking task. Progress will be displayed in a persistent ModalPane.
     *
     * @param sceneProp  The Scene whose UI must be blocked.
     * @param i18nBinder The text do display while performing the background task.
     * @param work       The actual task to perform.
     * @param onSuccess  Code to execute if the task is successful.
     * @param onFailure  Code to execute if the task is failed.
     * @param onCancel   Code to execute if the task is canceled.
     * @param <T>        Type of the object created by the task.
     */
    public static <T> void runBlocking(
            final ReadOnlyObjectProperty<Scene> sceneProp,
            final Consumer<StringProperty> i18nBinder,
            final TaskCallable<T> work,
            final Consumer<T> onSuccess,
            final Consumer<T> onFailure,
            final Consumer<T> onCancel
    ) {
        runBlocking(sceneProp, () -> new DescLoadingVBox(i18nBinder), work, onSuccess, onFailure, onCancel);
    }

    /**
     * Run a UI blocking task. Progress will be displayed in a persistent ModalPane.
     *
     * @param sceneProp The Scene whose UI must be blocked.
     * @param work      The actual task to perform.
     * @param onSuccess Code to execute if the task is successful.
     * @param onFailure Code to execute if the task is failed.
     * @param onCancel  Code to execute if the task is canceled.
     * @param <T>       Type of the object created by the task.
     */
    public static <T> void runBlocking(
            final ReadOnlyObjectProperty<Scene> sceneProp,
            final TaskCallable<T> work,
            final Consumer<T> onSuccess,
            final Consumer<T> onFailure,
            final Consumer<T> onCancel
    ) {
        runBlocking(sceneProp, RingEmptyLoadingVBox::new, work, onSuccess, onFailure, onCancel);
    }

    private static <T> Task<T> createTask(
            final ReadOnlyObjectProperty<Scene> sceneProp,
            final ChangeListener<Scene> sceneListener,
            final SimpleObjectProperty<ModalPane> modalPane,
            final TaskCallable<T> work,
            final Consumer<T> onSuccess,
            final Consumer<T> onFailure,
            final Consumer<T> onCancel
    ) {
        return new Task<>() {

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
                final ModalPane mp = modalPane.get();
                if (mp != null) {
                    mp.setPersistent(false);
                    mp.hide(true);
                }
                if (sceneListener != null) sceneProp.removeListener(sceneListener);
            }
        };
    }

    private static <T> ChangeListener<T> doOrAddListener(
            final ReadOnlyObjectProperty<T> prop,
            final Consumer<T> consumer
    ) {
        final T value = prop.getValue();
        if (value == null) {
            final ChangeListener<T> listener = (obs, ov, nv) -> consumer.accept(nv);
            prop.addListener(listener);
            return listener;
        } else {
            consumer.accept(value);
            return null;
        }
    }

    /** A simple box to show a loading element. */
    private static abstract class LoadingVBox
            extends VBox {

        private static final double PANE_SPACING = 3.0;

        private LoadingVBox() {
            super();
            this.setPadding(new Insets(PANE_SPACING));
            this.setAlignment(Pos.CENTER);
            this.setStyle("-fx-background-color: -color-bg-default;"); // Transparent by default.
        }

        abstract void bindProgress(final ReadOnlyDoubleProperty progress);
    }

    /** A simple box to show a loading element. */
    private static class RingEmptyLoadingVBox
            extends LoadingVBox {

        private static final double BOX_WIDTH = 250d;
        private static final double BOX_HEIGHT = 150d;
        private static final double INDICATOR_SIZE = 75d;

        private final RingProgressIndicator indicator;

        private RingEmptyLoadingVBox() {
            super();
            this.setMinSize(BOX_WIDTH, BOX_HEIGHT);
            this.setMaxSize(BOX_WIDTH, BOX_HEIGHT);

            this.indicator = new RingProgressIndicator();
            this.indicator.setMinSize(INDICATOR_SIZE, INDICATOR_SIZE);
            this.indicator.setMaxSize(INDICATOR_SIZE, INDICATOR_SIZE);

            this.getChildren().addAll(this.indicator);
        }

        @Override
        void bindProgress(final ReadOnlyDoubleProperty progress) {
            this.indicator.progressProperty().bind(progress);
        }
    }

    /** A simple box to show a loading element. */
    private static class DescLoadingVBox
            extends LoadingVBox {


        private final ProgressBar progressBar;

        private DescLoadingVBox(final Consumer<StringProperty> i18nBinder) {
            super();

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

        @Override
        void bindProgress(final ReadOnlyDoubleProperty progress) {
            this.progressBar.progressProperty().bind(progress);
        }
    }
}
