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
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A Form used to edit a text field with a list of proposition.
 *
 * @param <N> Type of object whose field must be edited.
 *
 * @since XXX
 */
public class FFCombo<N extends NamedSourcedObject>
        extends FormField<N, CharSequence, String> {

    private final ComboBox<String> field;

    /**
     * @param setter     The setter method to update the object.
     * @param i18nBinder The binder for the label of the Form Field.
     * @param items      The list of items to populate the ComboBox.
     */
    public FFCombo(
            final BiConsumer<N, CharSequence> setter,
            final Consumer<StringProperty> i18nBinder,
            final ObservableList<String> items
    ) {
        super(setter, i18nBinder);
        this.field = new ComboBox<>(items);
        this.field.setEditable(true);
        this.field.maxWidthProperty().bind(this.getFFContainer().widthProperty());
        final ChangeListener<String> inputListener = (o, ov, nv) -> this.runValidators(nv);
        this.field.getEditor().textProperty().addListener(inputListener);
        this.field.valueProperty().addListener(inputListener);
        this.getFFContainer().getChildren().add(this.field);
    }

    @Override
    String getCurrentInput() {
        return this.field.getValue();
    }

    @Override
    void runValidatorsOnError() {
        this.field.pseudoClassStateChanged(Styles.STATE_DANGER, true);
    }

    @Override
    void runValidatorOnSuccess() {
        this.field.pseudoClassStateChanged(Styles.STATE_DANGER, false);
    }

    @Override
    void doSet(
            final BiConsumer<N, CharSequence> setter,
            final N n
    ) {
        setter.accept(n, this.getCurrentInput());
    }

    @Override
    void doInit(final String input) {
        this.field.setValue(input);
    }
}
