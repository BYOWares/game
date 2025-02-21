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
import fr.byowares.game.utils.jfx.i18n.I18NResourceBundle;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A simple Form Field, whose root is a {@link javafx.scene.layout.VBox}. First element of the {@code VBox} is a label
 * describing this field. When an error is detected, a label is added at the end of the {@code VBox} children.
 * Custom field shall be added right after the label.
 *
 * @param <P>   Type of the Pane containing the SimpleFormField
 * @param <N>   Type of object whose field must be edited.
 * @param <FFT> The raw data's type managed by this SimpleFormField.
 * @param <T>   The type of the attribute managed by this SimpleFormField.
 *
 * @since XXX
 */
public abstract class SimpleFormField<P extends Pane, N extends NamedSourcedObject, T, FFT>
        extends AbstractFormField<P, N, T> {

    /** Very unlikely init value for field. This help trigger validation upon init. */
    protected static final String IMPROBABLE_INIT_VALUE = "PLEASE, INITIALIZE ME WITH A DIFFERENT VALUE";
    private static final double VBOX_CONTAINER_SPACING = 1.0;

    private final Label label;
    private final Label error;
    private final List<FormFieldValidator<FFT>> validators;

    /**
     * @param root       The root element containing the Form Field.
     * @param setter     The setter method to update the object.
     * @param i18nBinder The binder for the label of the Form Field.
     */
    SimpleFormField(
            final P root,
            final BiConsumer<N, T> setter,
            final Consumer<StringProperty> i18nBinder
    ) {
        super(root, setter);
        this.label = new Label();
        this.label.getStyleClass().add(Styles.ACCENT);
        i18nBinder.accept(this.label.textProperty());
        this.getRoot().getChildren().add(this.label);

        this.error = new Label(null, new FontIcon(BootstrapIcons.EXCLAMATION_TRIANGLE));
        this.error.getStyleClass().add(Styles.DANGER);
        this.error.maxWidthProperty().bind(this.getRoot().widthProperty());
        final MenuItem menuItem = new MenuItem();
        I18NMIQ.get().bind(menuItem.textProperty(), "wizard.error.copy");
        menuItem.setOnAction(e -> {
            final ClipboardContent content = new ClipboardContent();
            content.putString(this.error.getText());
            Clipboard.getSystemClipboard().setContent(content);
        });
        this.error.setContextMenu(new ContextMenu(menuItem));

        /* Validation part */
        this.validators = new ArrayList<>();
    }

    /**
     * @return A VBox to contain all elements of a {@link SimpleFormField} (to be used
     * in the {@code super} constructor) with a spacing of {@link #VBOX_CONTAINER_SPACING}.
     */
    static VBox newVBoxContainer() {
        return newVBoxContainer(VBOX_CONTAINER_SPACING);
    }

    /**
     * @param spacing Spacing of the {@code VBox}.
     *
     * @return A VBox to contain all elements of a {@link SimpleFormField} (to be used
     * in the {@code super} constructor).
     */
    static VBox newVBoxContainer(final double spacing) {
        return new VBox(spacing);
    }

    /**
     * @param object The object to normalize.
     * @param <T>    The type of the object to normalize.
     *
     * @return An empty {@code String} if the input is {@code null}, a {@code object.toString()} otherwise.
     */
    static <T> String normalizeInput(final T object) {
        return object == null ? "" : object.toString();
    }

    /**
     * @param validator The validator to add to this Form Field.
     */
    public void addValidator(final FormFieldValidator<FFT> validator) {
        this.validators.add(validator);
    }

    /**
     * @return The current value hold by the Form Field, in its raw format.
     */
    abstract FFT getCurrentInput();

    /**
     * Run all validators on the given input. Update {@link #inError()} and show the {@link #error} if needed.
     *
     * @param input The input to validate.
     */
    final void runValidators(final FFT input) {
        final CallableList errors = new CallableList();
        for (final var validator : this.validators)
            if (!validator.canContinueAnalysis(errors, input)) break;

        final ObservableList<Node> children = this.getRoot().getChildren();
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "label=" + this.label.getText() + ", inError=" + this.inError().get() + ", " + "error=" + this.error.getText() + '}';
    }
}
