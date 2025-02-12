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
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A Form used to edit a text field.
 *
 * @param <N> Type of object whose field must be edited.
 *
 * @since XXX
 */
public class FFMLText<N extends NamedSourcedObject>
        extends FormField<N, CharSequence, String> {

    private final TextArea field;

    /**
     * @param minHeight  The minimal height for the text area.
     * @param setter     The setter method to update the object.
     * @param i18nBinder The binder for the label of the Form Field.
     */
    public FFMLText(
            final double minHeight,
            final BiConsumer<N, CharSequence> setter,
            final Consumer<StringProperty> i18nBinder
    ) {
        super(setter, i18nBinder);
        this.field = new TextArea(IMPROBABLE_INIT_VALUE);
        this.field.setMinHeight(minHeight);
        this.field.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.field.maxHeightProperty().bind(this.getFFContainer().heightProperty());
        this.field.textProperty().addListener((o, ov, nv) -> {
            this.runValidators(nv);
        });
        this.getFFContainer().getChildren().add(this.field);
    }

    @Override
    String getCurrentInput() {
        return this.field.getText();
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
        this.field.setText(input);
    }
}
