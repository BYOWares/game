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
import fr.byowares.game.utils.jfx.Utils;
import fr.byowares.game.utils.jfx.i18n.I18NLocaleManager;
import fr.byowares.game.utils.jfx.i18n.I18NResourceBundle;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Common part for all Form Fields.
 *
 * @param <P> Type of the Pane containing the SimpleFormField
 * @param <N> Type of object whose field must be edited.
 * @param <T> Type of the attribute managed by this SimpleFormField.
 * @param <V> Type of the input on which the validation must be performed.
 *
 * @since XXX
 */
public abstract class AbstractFormField<P extends Pane, N extends NamedSourcedObject, T, V>
        implements FormField<P, N, T> {

    private final P root;
    private final BiConsumer<N, T> setter;

    private final BooleanProperty inError;
    private final BooleanProperty isIgnored;
    /** Automatically updated by {@link #validators}. */
    private final BooleanProperty hasValidationError;
    /** Does not depend on {@link #validators}. */
    private final BooleanProperty hasError;
    private final List<FormFieldValidator<V>> validators;
    private final Label error;

    private final IntegerProperty level;

    /**
     * @param root   The root element containing the Form Field.
     * @param setter The setter method to update the object.
     */
    AbstractFormField(
            final P root,
            final BiConsumer<N, T> setter
    ) {
        this.root = root;
        this.setter = setter;

        this.inError = new SimpleBooleanProperty(false);
        this.isIgnored = new SimpleBooleanProperty(false);
        this.hasValidationError = new SimpleBooleanProperty(false);
        this.hasError = new SimpleBooleanProperty(false);

        final ChangeListener<Boolean> updateInError = (obs, ov, nv) -> //
                this.inError.set(!this.isIgnored.get() && (this.hasValidationError.get() || this.hasError.get()));
        this.isIgnored.addListener(updateInError);
        this.hasValidationError.addListener(updateInError);
        this.hasError.addListener(updateInError);

        this.error = new Label(null, new FontIcon(BootstrapIcons.EXCLAMATION_TRIANGLE));
        this.error.getStyleClass().add(Styles.DANGER);
        this.error.maxWidthProperty().bind(this.getRoot().widthProperty());
        final MenuItem copyToClipboard = new MenuItem();
        I18NMIQ.get().bind(copyToClipboard.textProperty(), "wizard.menu_item.error.copy");
        copyToClipboard.setOnAction(e -> Utils.copyToClipboard(this.error.getText()));
        this.error.setContextMenu(new ContextMenu(copyToClipboard));
        this.error.setTooltip(new Tooltip());
        this.error.getTooltip().textProperty().bind(this.error.textProperty());

        this.validators = new ArrayList<>();
        this.level = new SimpleIntegerProperty(-1);
        this.level.addListener((obs, ov, nv) -> this.onLevelUpdate(ov.intValue(), nv.intValue()));
    }

    /**
     * @param parent The Pane to which the {@code child} must be added.
     * @param child  The Node to add to the {@code parent} with a transition.
     *
     * @return The transition to play while adding the node.
     */
    static Transition addTransition(
            final Pane parent,
            final Node child
    ) {
        final FadeTransition transition = new FadeTransition(Duration.millis(250.0), child);
        transition.setFromValue(0.0);
        transition.setToValue(1.0);
        transition.setInterpolator(Interpolator.EASE_OUT);
        parent.getChildren().add(child);
        return transition;
    }

    @Override
    public final ReadOnlyBooleanProperty inErrorProperty() {
        return this.inError;
    }

    @Override
    public final void setIsIgnored(final boolean isIgnored) {
        this.isIgnored.set(isIgnored);
    }

    @Override
    public final void setHasError(final boolean hasError) {
        this.hasError.set(hasError);
    }

    @Override
    public final P getRoot() {
        return this.root;
    }

    @Override
    public final void set(final N n) {
        if (this.isIgnored.get()) return;
        if (this.inError.get()) throw new IllegalStateException(
                "Cannot set this FormField because it is considered in error (locale " + I18NLocaleManager.get() + "): " + this);
        this.doSet(this.setter, n);
    }

    @Override
    public final void setLevel(final int level) {
        this.level.set(level);
    }

    /**
     * Action performed when the level is updated.
     *
     * @param oldValue The old level value (negative value means not set).
     * @param newValue The new level value.
     */
    abstract void onLevelUpdate(
            final int oldValue,
            final int newValue
    );

    private boolean hasValidationError(final boolean hasError) {
        final boolean previous = this.hasValidationError.get();
        this.hasValidationError.set(hasError);
        return previous;
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
     * @param validator The validator to add to this Form Field.
     */
    public final void addValidator(final FormFieldValidator<V> validator) {
        this.validators.add(validator);
    }

    /**
     * Run all validators on the given input. Update {@link #inErrorProperty()} and show the {@link #error} if needed.
     *
     * @param input The input to validate.
     */
    final void runValidators(final V input) {
        final CallableList errors = new CallableList();
        for (final var validator : this.validators)
            if (!validator.canContinueAnalysis(errors, input)) break;

        final Label error = this.error;
        final boolean hasError = !errors.isEmpty();
        final boolean hadError = this.hasValidationError(hasError);
        if (hasError) {
            if (!hadError) addTransition(this.getRoot(), error).play();
            I18NResourceBundle.bind(error.textProperty(), errors);
            this.runValidatorsOnError();
        } else {
            if (hadError) this.getRoot().getChildren().remove(error);
            this.runValidatorOnSuccess();
        }
    }

    /** Specific code to run when this Form Field is invalid. */
    abstract void runValidatorsOnError();

    /** Specific code to run when this Form Field is valid. */
    abstract void runValidatorOnSuccess();

    /**
     * @param sb The StringBuilder used to add the textual information related to error.
     */
    void addErrorData(final StringBuilder sb) {
        sb.append("inError=").append(this.inError.get());
        sb.append(" (hasError=").append(this.hasValidationError.get());
        sb.append(", isIgnored=").append(this.isIgnored.get());
        sb.append("), errors=").append(this.error.getText());
    }
}
