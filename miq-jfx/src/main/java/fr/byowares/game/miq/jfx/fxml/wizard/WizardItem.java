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
package fr.byowares.game.miq.jfx.fxml.wizard;

import fr.byowares.game.miq.jfx.editor.form.FormField;
import fr.byowares.game.utils.jfx.Controller;
import fr.byowares.game.utils.jfx.Pair;
import fr.byowares.game.utils.jfx.fxml.FXMLLoader;
import fr.byowares.game.utils.jfx.theme.ThemeManager;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Main pattern to create a new object
 *
 * @param <N> The type of object created by this wizard.
 *
 * @since XXX
 */
public abstract class WizardItem<N extends NamedSourcedObject<N>>
        implements Controller {

    private static final double VBOX_PADDING = 30.0;

    private final Consumer<StringProperty> i18nTitleBinder;
    private final List<FormField<N, ?, ?>> formFields;

    /** The Label to indicate which kind of object is being created. */
    @FXML protected Label label;
    @FXML private Button bCancel;
    @FXML private Button bCreate;
    @FXML private SplitPane splitPane;

    private N returnedObject;

    /**
     * @param i18nTitleBinder The binder for the new window title.
     */
    WizardItem(final Consumer<StringProperty> i18nTitleBinder) {
        this.i18nTitleBinder = i18nTitleBinder;
        this.formFields = new ArrayList<>();
        this.returnedObject = null;
    }

    /**
     * Open a new window to show this app information.
     *
     * @param wizard The wizard controlling the object creation.
     * @param stage  The stage used to open this new about dialog.
     * @param <T>    The type object created by this wizard.
     *
     * @return The object created, {@code null} if creation is cancelled.
     */
    public static <T extends NamedSourcedObject<T>> T open(
            final WizardItem<T> wizard,
            final Stage stage
    ) {
        final Pair<WizardItem<T>, BorderPane> pair = FXMLLoader.load(wizard, WizardItem.class, "WizardItem");
        final Scene scene = new Scene(pair.root());
        ThemeManager.subscribe(scene);
        final Stage newStage = new Stage();
        wizard.i18nTitleBinder.accept(newStage.titleProperty());
        newStage.setScene(scene);
        newStage.setResizable(true);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(stage);
        newStage.getIcons().addAll(stage.getIcons());
        wizard.configureStageSize(newStage);
        newStage.showAndWait();
        return wizard.returnedObject;
    }

    /**
     * @param newStage The stage to configure.
     */
    abstract void configureStageSize(final Stage newStage);


    /** FXML handle for initialization. */
    @FXML
    public final void initialize() {
        this.i18nTitleBinder.accept(this.label.textProperty());
        this.doInitialize();
        this.onDisplay();
    }

    /** Initialize child */
    abstract void doInitialize();


    /** Called when the button Cancel is activated. */
    @FXML
    final void onCancel() {
        this.onHide();
        final Scene scene = this.bCancel.getScene();
        ((Stage) scene.getWindow()).close();
        ThemeManager.unsubscribe(scene);
    }

    /** Called when the button Create is activated. */
    @FXML
    final void onCreate() {
        this.returnedObject = this.newEmptyObject();
        for (final var ff : this.formFields) {
            ff.set(this.returnedObject);
        }
        this.onCancel();
    }

    /**
     * Add the Form Field {@code ff} to the list of children of the first split which is a
     * {@link javafx.scene.layout.VBox}.
     *
     * @param ff    The Form Field to add to the first split of the Split Pane
     * @param input The Default value of the Form Field.
     * @param <FFT> The type of the raw data in the Form Field.
     */
    <FFT> void addFormFieldToSplitPane(
            final FormField<N, ?, FFT> ff,
            final FFT input
    ) {
        this.addFormFieldToSplitPane(0, ff, input);
    }

    /**
     * Add the Form Field {@code ff} to the list of children of the required split. Create split on demand. All split
     * are {@link javafx.scene.layout.VBox}.
     *
     * @param paneIndex The index of the SplitPane to which the Form Field must be added.
     * @param ff        The Form Field to add to the {@code paneIndex}th split of the Split Pane.
     * @param input     The Default value of the Form Field.
     * @param <FFT>     The type of the raw data in the Form Field.
     */
    <FFT> void addFormFieldToSplitPane(
            final int paneIndex,
            final FormField<N, ?, FFT> ff,
            final FFT input
    ) {
        this.formFields.add(ff);
        if (paneIndex < 0) throw new IllegalArgumentException("pane index must be strictly positive");
        while (paneIndex >= this.splitPane.getItems().size()) {
            final VBox vBox = new VBox(10.0);
            vBox.setPadding(new Insets(VBOX_PADDING));
            this.splitPane.getItems().add(vBox);
        }

        ((VBox) this.splitPane.getItems().get(paneIndex)).getChildren().add(ff.getFFContainer());
        ff.inErrorProperty().addListener((obs, ov, nv) -> this.updateButtonAdd());
        ff.init(input);
    }

    private void updateButtonAdd() {
        final boolean atLeastOneError = this.formFields.stream() //
                .map(FormField::inErrorProperty) //
                .anyMatch(ObservableBooleanValue::get)
                ;
        this.bCreate.setDisable(atLeastOneError);
    }

    /**
     * @return An empty object that the {@link #formFields} can set.
     */
    abstract N newEmptyObject();
}
