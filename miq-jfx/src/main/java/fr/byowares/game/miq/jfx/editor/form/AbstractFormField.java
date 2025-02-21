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

import fr.byowares.game.utils.jfx.i18n.I18NLocaleManager;
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.function.BiConsumer;

/**
 * Common part for all Form Fields.
 *
 * @param <P> Type of the Pane containing the SimpleFormField
 * @param <N> Type of object whose field must be edited.
 * @param <T> Type of the attribute managed by this SimpleFormField.
 *
 * @since XXX
 */
public abstract class AbstractFormField<P extends Pane, N extends NamedSourcedObject, T>
        implements FormField<P, N, T> {

    private final P root;
    private final BiConsumer<N, T> setter;
    private final BooleanProperty inError;

    /**
     * @param root   The root element containing the Form Field.
     * @param setter The setter method to update the object.
     */
    AbstractFormField(
            final P root,
            final BiConsumer<N, T> setter
    ) {
        this.root = root;
        this.root.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE); // TODO needed ?

        this.setter = setter;
        this.inError = new SimpleBooleanProperty(false);
    }

    @Override
    public final BooleanProperty inErrorProperty() {
        return this.inError;
    }

    @Override
    public final P getRoot() {
        return this.root;
    }

    @Override
    public final void set(final N n) {
        if (this.inError.get()) throw new IllegalStateException(
                "Cannot set this FormField because it is considered in error (locale " + I18NLocaleManager.get() + "): " + this);
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
     * @return The {@link javafx.beans.property.BooleanProperty} managing the error status of the
     * {@link fr.byowares.game.miq.jfx.editor.form.FormField}.
     */
    BooleanProperty inError() {
        return this.inError;
    }
}
