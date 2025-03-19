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

import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.IntUnaryOperator;

/**
 * A collection of Form Fields. Its error property is automatically bound to its children.
 *
 * @param <P> Type of the Pane containing the ComposedFormField
 * @param <N> Type of object whose field must be edited.
 * @param <T> The type of the attribute managed by this ComposedFormField.
 *
 * @since XXX
 */
public abstract class ComposedFormField<P extends Pane, N extends NamedSourcedObject, T>
        extends AbstractFormField<P, N, T, Void> {

    private final List<FormField<?, N, ?>> formFields;
    private final ChangeListener<Boolean> inErrorListener;

    /**
     * @param root      The root element containing the Form Field.
     * @param setter    The setter method to update the object.
     * @param minWidth  Minimal width for the Form Field (or {@code <0>} if none).
     * @param minHeight Minimal height for the Form Field (or {@code <0>} if none).
     */
    ComposedFormField(
            final P root,
            final BiConsumer<N, T> setter,
            final double minWidth,
            final double minHeight
    ) {
        super(root, setter, minWidth, minHeight);
        this.formFields = new ArrayList<>();
        this.inErrorListener = (obs, ov, nv) -> this.updateHasError();
    }

    /**
     * @return The Pane to which the children are automatically added through {@link #registerFormField(FormField)}.
     */
    Pane getChildrenPane() {
        return this.getRoot();
    }

    /**
     * Add the given Form Field at the end of {@link #getChildrenPane()}, and bind its {@link #inErrorProperty()}.
     *
     * @param ff The Form Field to register.
     */
    final void registerFormField(final FormField<?, N, ?> ff) {
        this.doRegisterFormField(ff, this.getChildrenPane().getChildren().size());
    }

    /**
     * @param newFF The new Form Field to register.
     * @param ffRef The reference Form Field to find where to add the new Form Field.
     */
    final void registerFormFieldAfter(
            final FormField<?, N, ?> newFF,
            final FormField<?, N, ?> ffRef
    ) {
        this.doRegisterFormField(newFF, ffRef, x -> x + 1);
    }

    /**
     * @param newFF The new Form Field to register.
     * @param ffRef The reference Form Field to find where to add the new Form Field.
     */
    final void registerFormFieldBefore(
            final FormField<?, N, ?> newFF,
            final FormField<?, N, ?> ffRef
    ) {
        this.doRegisterFormField(newFF, ffRef, x -> x);
    }

    private void doRegisterFormField(
            final FormField<?, N, ?> newFF,
            final FormField<?, N, ?> ffRef,
            final IntUnaryOperator operator
    ) {
        final int index = this.getChildrenPane().getChildren().indexOf(ffRef.getRoot());
        if (index < 0) throw new IllegalArgumentException("Could not find " + ffRef + " in " + this);
        this.doRegisterFormField(newFF, operator.applyAsInt(index));
    }

    private void doRegisterFormField(
            final FormField<?, N, ?> newFF,
            final int index
    ) {
        this.formFields.add(newFF);
        newFF.inErrorProperty().addListener(this.inErrorListener);
        this.getChildrenPane().getChildren().add(index, newFF.getRoot());
    }

    /**
     * @param ff The Form Field to remove from the list of managed Form Fields, and from the graphical elements.
     */
    final void unregisterFormField(final FormField<?, N, ?> ff) {
        this.formFields.remove(ff);
        ff.inErrorProperty().removeListener(this.inErrorListener);
        this.getChildrenPane().getChildren().remove(ff.getRoot());
        this.updateHasError();
    }

    private void updateHasError() {
        final boolean hasAtLeastOneError = this.formFields.stream() //
                .map(FormField::inErrorProperty) //
                .anyMatch(ObservableBooleanValue::get) //
                ;
        this.setHasError(hasAtLeastOneError);
    }

    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append("{root=").append(this.getRoot()).append(", ");
        this.addErrorData(sb);
        sb.append(", formFields=[");
        for (int i = 0; i < this.formFields.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(this.formFields.get(i).toString());
        }
        sb.append("]}");
        return sb.toString();
    }

    @Override
    public void dispose() {
        this.formFields.forEach(FormField::dispose);
    }
}
