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
package fr.byowares.game.miq.jfx.editor.form;

import atlantafx.base.theme.Styles;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.jfx.i18n.I18NLocaleManager;
import fr.byowares.game.utils.jfx.i18n.I18NResourceBundle;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The generic FormField pattern.
 *
 * @param <N>   Type of object whose field must be edited.
 * @param <FFT> The raw data's type managed by this FormField.
 * @param <T>   The type of the attribute managed by this FormField.
 *
 * @since XXX
 */
public abstract class FormField<N extends NamedSourcedObject<N>, T, FFT> {

    /** Very unlikely init value for field. This help trigger validation upon init. */
    protected static final String IMPROBABLE_INIT_VALUE = "PLEASE, INITIALIZE ME WITH A DIFFERENT VALUE";

    private final BiConsumer<N, T> setter;
    private final VBox ffContainer;
    private final Label label;
    private final Label error;
    private final BooleanProperty inError;
    private final List<FormFieldValidator<FFT>> validators;

    /**
     * @param setter     The setter method to update the object.
     * @param i18nBinder The binder for the label of the Form Field.
     */
    public FormField(
            final BiConsumer<N, T> setter,
            final Consumer<StringProperty> i18nBinder
    ) {
        /* Graphical elements */
        this.ffContainer = new VBox(5.0);

        this.label = new Label();
        i18nBinder.accept(this.label.textProperty());
        this.ffContainer.getChildren().add(this.label);

        this.error = new Label(null, new FontIcon(BootstrapIcons.EXCLAMATION_TRIANGLE));
        this.error.getStyleClass().add(Styles.DANGER);
        this.error.maxWidthProperty().bind(this.ffContainer.widthProperty());
        final MenuItem menuItem = new MenuItem();
        I18NMIQ.get().bind(menuItem.textProperty(), "wizard.error.copy");
        menuItem.setOnAction(e -> {
            final ClipboardContent content = new ClipboardContent();
            content.putString(this.error.getText());
            Clipboard.getSystemClipboard().setContent(content);
        });
        this.error.setContextMenu(new ContextMenu(menuItem));

        /* Validation part */
        this.inError = new SimpleBooleanProperty(false);
        this.validators = new ArrayList<>();

        /* Object to create part */
        this.setter = setter;
    }


    /**
     * @return An observable boolean set to {@code true} when an error is detected in the form field, {@code false}
     * otherwise.
     */
    public final BooleanProperty inErrorProperty() {
        return this.inError;
    }

    /**
     * @return The {@link javafx.scene.Node} that contains this whole Form Field.
     */
    public final VBox getFFContainer() {
        return this.ffContainer;
    }

    /**
     * Add validators and then re-run them all.
     *
     * @param validators The list of validators to add to this Form Field.
     */
    public void addValidators(final List<FormFieldValidator<FFT>> validators) {
        this.validators.addAll(validators);
    }

    /**
     * @return The current value hold by the Form Field, in its raw format.
     */
    abstract FFT getCurrentInput();

    /**
     * Run all validators on the given input. Update {@link #inError} and show the {@link #error} if needed.
     *
     * @param input The input to validate.
     */
    final void runValidators(final FFT input) {
        final CallableList errors = new CallableList();
        for (final var validator : this.validators)
            if (!validator.canContinueAnalysis(errors, input)) break;

        final ObservableList<Node> children = this.getFFContainer().getChildren();
        final Label error = this.error;
        if (errors.isEmpty()) {
            this.inErrorProperty().set(false);
            children.remove(error);
            this.runValidatorOnSuccess();

        } else {
            this.inErrorProperty().set(true);
            if (!children.contains(error)) children.add(error);
            I18NResourceBundle.bind(error.textProperty(), errors);
            this.runValidatorsOnError();
        }
    }

    /** Specific code to run when this Form Field is invalid. */
    abstract void runValidatorsOnError();

    /** Specific code to run when this Form Field is valid. */
    abstract void runValidatorOnSuccess();

    /**
     * Update the object with the current Form Field input.
     *
     * @param n The object to update.
     *
     * @throws java.lang.IllegalStateException If the Form Field is in error while calling this method.
     */
    public final void set(final N n) {
        if (this.inError.get()) throw new IllegalStateException(
                "Cannot set field " + this.label.getText() + " (locale: " + I18NLocaleManager.get() + //
                        ") because the Form Field still have errors:" + System.lineSeparator() + this.error.getText());
        this.doSet(this.setter, n);
    }

    /**
     * @param setter The setter method to update the object.
     * @param n      The object to update.
     */
    abstract void doSet(
            final BiConsumer<N, T> setter,
            final N n
    );

    /**
     * Initialize this form field with the given input.
     *
     * @param input The input to initialize the Form Field with.
     */
    public final void init(final FFT input) {
        this.doInit(input);
    }

    /**
     * Initialize this form field with the given input.
     *
     * @param input The input to initialize the Form Field with.
     */
    abstract void doInit(final FFT input);
}
