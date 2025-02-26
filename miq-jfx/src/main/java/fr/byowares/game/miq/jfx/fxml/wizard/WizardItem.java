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
import fr.byowares.game.utils.jfx.FontIconSizeEnforcer;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class WizardItem<N extends NamedSourcedObject>
        implements Controller {

    /** Spacing used in wizard VBoxes. */
    public static final double WIZARD_SPACING = 10.0;
    private static final double VBOX_PADDING = 30.0;
    private static final Logger log = LoggerFactory.getLogger(WizardItem.class);

    private final Consumer<StringProperty> i18nTitleBinder;
    private final List<FormField<?, N, ?>> formFields;
    private final FontIcon fontIcon;

    /** The Label to indicate which kind of object is being created. */
    @FXML protected Label label;
    @FXML private HBox hbox;
    @FXML private Button bCancel;
    @FXML private Button bCreate;
    @FXML private SplitPane splitPane;
    private N returnedObject;

    /**
     * @param i18nTitleBinder The binder for the new window title.
     * @param fontIcon        The {@link org.kordamp.ikonli.javafx.FontIcon} to decorate the title.
     */
    WizardItem(
            final Consumer<StringProperty> i18nTitleBinder,
            final FontIcon fontIcon
    ) {
        this.fontIcon = fontIcon;
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
    public static <T extends NamedSourcedObject> T open(
            final WizardItem<T> wizard,
            final Stage stage
    ) {
        final Pair<WizardItem<T>, StackPane> pair = FXMLLoader.load(wizard, WizardItem.class, "WizardItem");
        final Scene scene = new Scene(pair.root());
        ThemeManager.subscribe(scene);
        final Stage newStage = new Stage();
        newStage.setOnCloseRequest(e -> ThemeManager.unsubscribe(scene));
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
        final var css = FontIconSizeEnforcer.enforceIconSizeCSS(this.hbox, "size-enforcer", 32);
        this.fontIcon.getStyleClass().add(css);
        this.hbox.getChildren().addFirst(this.fontIcon);
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
        try {
            this.returnedObject = this.newEmptyObject();
            for (final var ff : this.formFields) {
                ff.set(this.returnedObject);
            }
        } catch (final Exception e) {
            log.warn("Failed to create the object: {}", this.returnedObject, e);
            this.returnedObject = null;
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
            final FormField<?, N, FFT> ff,
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
            final FormField<?, N, FFT> ff,
            final FFT input
    ) {
        this.formFields.add(ff);
        if (paneIndex < 0) throw new IllegalArgumentException("paneIndex must be strictly positive");
        while (paneIndex >= this.splitPane.getItems().size()) {
            final VBox vBox = new VBox(WIZARD_SPACING);
            vBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            vBox.setPadding(new Insets(VBOX_PADDING));
            this.splitPane.getItems().add(vBox);
            vBox.maxHeightProperty().bind(this.splitPane.heightProperty());
        }

        final VBox vBox = (VBox) this.splitPane.getItems().get(paneIndex);
        vBox.getChildren().add(ff.getRoot());
        ff.inErrorProperty().addListener((obs, ov, nv) -> this.updateButtonCreate());
        ff.init(input);
        ff.getRoot().maxWidthProperty().bind(vBox.widthProperty().subtract(VBOX_PADDING).subtract(VBOX_PADDING));
        ff.setLevel(1);
    }

    private void updateButtonCreate() {
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
