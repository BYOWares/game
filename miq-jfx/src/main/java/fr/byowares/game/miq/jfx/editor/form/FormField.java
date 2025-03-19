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
import javafx.beans.property.ReadOnlyBooleanProperty;
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
     * @return A read only observable boolean set to {@code true} when an error is detected in the form field and
     * this form field is not ignored, {@code false} otherwise.
     */
    ReadOnlyBooleanProperty inErrorProperty();

    /**
     * @param isIgnored Whether this form field must be ignored while validating inputs.
     */
    void setIsIgnored(boolean isIgnored);

    /**
     * @param hasError Whether this form field has error.
     */
    void setHasError(boolean hasError);

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


    /**
     * The level to give to the Form Field. The lower the value, the higher the precedence, and the more visible it
     * will be to the user.
     *
     * @param level The level to configure (must be strictly positive).
     */
    void setLevel(final int level);

    /**
     * Method to call when the FormField is not used anymore.
     */
    void dispose();
}
