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
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A collection of Form Fields. Its error property is automatically bound to its children.
 *
 * @param <P> Type of the Pane containing the SimpleFormField
 * @param <N> Type of object whose field must be edited.
 * @param <T> The type of the attribute managed by this SimpleFormField.
 *
 * @since XXX
 */
public abstract class ComposedFormField<P extends Pane, N extends NamedSourcedObject, T>
        extends AbstractFormField<P, N, T, Void> {

    @SuppressWarnings("rawtypes") private static final BiConsumer NOOP_BI_CONSUMER = (v, c) -> {};

    private final List<FormField<?, N, ?>> formFields;

    /**
     * @param root   The root element containing the Form Field.
     * @param setter The setter method to update the object.
     */
    ComposedFormField(
            final P root,
            final BiConsumer<N, T> setter
    ) {
        super(root, setter);
        this.formFields = new ArrayList<>();
    }

    /**
     * @param <T> The type of the first parameter of the {@code BiConsumer}.
     * @param <S> The type of the second parameter of the {@code BiConsumer}.
     *
     * @return A no operation {@link java.util.function.BiConsumer}.
     */
    @SuppressWarnings("unchecked")
    static <T, S> BiConsumer<T, S> noOpBiConsumer() {
        return (BiConsumer<T, S>) NOOP_BI_CONSUMER;
    }

    /**
     * @return The Pane to which the children are automatically added through {@link #addFormField(FormField)}.
     */
    Pane getChildrenPane() {
        return this.getRoot();
    }

    /**
     * Add the given Form Field in the root of this Form Field, and bind its {@link #inErrorProperty()}.
     *
     * @param ff The Form Field to add.
     */
    void addFormField(final FormField<?, N, ?> ff) {
        this.formFields.add(ff);
        ff.inErrorProperty().addListener((obs, ov, nv) -> this.updateHasError());
        this.getChildrenPane().getChildren().add(ff.getRoot());
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
        sb.append(this.getClass().getSimpleName()).append("{");
        this.addErrorData(sb);
        sb.append(", formFields=[");
        for (int i = 0; i < this.formFields.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(this.formFields.get(i).toString());
        }
        sb.append("]}");
        return sb.toString();
    }
}
