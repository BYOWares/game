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
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A simple Form Field, whose root is a {@link javafx.scene.layout.VBox}. First element of the {@code VBox} is a label
 * describing this field. When an error is detected, a label is added at the end of the {@code VBox} children.
 * Custom field shall be added right after the label.
 *
 * @param <P> Type of the Pane containing the SimpleFormField
 * @param <N> Type of object whose field must be edited.
 * @param <T> Type of the attribute managed by this SimpleFormField.
 * @param <V> Type of the input on which the validation must be performed.
 *
 * @since XXX
 */
public abstract class SimpleFormField<P extends Pane, N extends NamedSourcedObject, T, V>
        extends AbstractFormField<P, N, T, V> {

    /** Very unlikely init value for field. This help trigger validation upon init. */
    protected static final String IMPROBABLE_INIT_VALUE = "PLEASE, INITIALIZE ME WITH A DIFFERENT VALUE";
    private static final String[] LEVEL1_STYLE = {Styles.ACCENT, Styles.TITLE_4};
    private static final double VBOX_CONTAINER_SPACING = 1.0;

    private final Label label;

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
        i18nBinder.accept(this.label.textProperty());
        this.getRoot().getChildren().add(this.label);
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
     * Update style classes depending on the level given (the lower the value, the more visible it shall be).
     *
     * @param styleClass The list of Style classes.
     * @param oldValue   The old level value (<0 means it was not set).
     * @param newValue   The new level value.
     */
    static void updateStyleClass(
            final ObservableList<String> styleClass,
            final int oldValue,
            final int newValue
    ) {
        if (oldValue < 0) {
            styleClass.add(Styles.TEXT_BOLD);
            if (newValue <= 1) styleClass.addAll(LEVEL1_STYLE);
        } else {
            if (oldValue <= 1 && newValue > 1) styleClass.removeAll(LEVEL1_STYLE);
            if (oldValue > 1 && newValue <= 1) styleClass.addAll(LEVEL1_STYLE);
        }
    }

    /**
     * @return The current value hold by the Form Field, in its raw format.
     */
    abstract V getCurrentInput();

    @Override
    final void onLevelUpdate(
            final int oldValue,
            final int newValue
    ) {
        final ObservableList<String> styleClass = this.label.getStyleClass();
        updateStyleClass(styleClass, oldValue, newValue);
    }

    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append("{");
        sb.append("label=").append(this.label.getText());
        this.addErrorData(sb);
        sb.append("}");
        return sb.toString();
    }
}
