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
import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.Pane;

/**
 * Form Fields used in wizard and editors.
 *
 * @param <P> Type of the Pane containing the SimpleFormField
 * @param <N> Type of object whose field must be edited.
 * @param <T> Type of the attribute managed by this SimpleFormField.
 *
 * @since XXX
 */
public interface FormField<P extends Pane, N extends NamedSourcedObject, T> {

    /**
     * @return An observable boolean set to {@code true} when an error is detected in the form field, {@code false}
     * otherwise.
     */
    BooleanProperty inErrorProperty();

    /**
     * @return The {@link javafx.scene.layout.Pane} that contains this whole Form Field.
     */
    P getRoot();

    /**
     * Initialize this form field with the given input.
     *
     * @param input The input to initialize the Form Field with.
     */
    void init(final T input);

    /**
     * Update the object with the current Form Field input.
     *
     * @param n The object to update.
     *
     * @throws java.lang.IllegalStateException If the Form Field is in error while calling this method.
     */
    void set(final N n);
}
