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
import fr.byowares.game.utils.serial.source.NamedSourcedObject;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A Form used to show a text. This text is expected to be bound to a
 * {@link fr.byowares.game.miq.jfx.editor.form.FFText}. {@link javafx.scene.control.Label} have the advantage of
 * allowing ellipsis when they are too long, which {@link javafx.scene.control.TextField} doesn't.
 *
 * @param <N> Type of object whose field must be edited.
 *
 * @since XXX
 */
public class FFLabel<N extends NamedSourcedObject>
        extends SimpleFormField<VBox, N, CharSequence, String> {

    private final Label field;

    /**
     * @param setter     The setter method to update the object.
     * @param i18nBinder The binder for the label of the Form Field.
     */
    public FFLabel(
            final BiConsumer<N, CharSequence> setter,
            final Consumer<StringProperty> i18nBinder
    ) {
        super(SimpleFormField.newVBoxContainer(), setter, i18nBinder);
        this.field = newLabel(this.getRoot());
        this.field.textProperty().addListener((o, ov, nv) -> this.runValidators(nv));
        this.getRoot().getChildren().add(this.field);
    }

    /**
     * Create a new {@link javafx.scene.control.Label} as follow:
     * <ul>
     *     <li>Must look like a TextField disabled.</li>
     *     <li>Ellipsis must be at the start of the field if any.</li>
     *     <li>The width must always fill as much as possible.</li>
     * </ul>
     *
     * @param parent The {@link javafx.scene.layout.Pane} that will contain the {@link javafx.scene.control.Label}.
     *
     * @return A {@link javafx.scene.control.Label} with default configuration.
     */
    static Label newLabel(final Pane parent) {
        final Label label = new Label(IMPROBABLE_INIT_VALUE);
        // The next 2 lines mimic a TextField disabled.
        label.getStyleClass().add("text-input");
        label.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), true);
        label.setStyle("-fx-text-overrun: leading-ellipsis;");
        // Make the label fill the width
        HBox.setHgrow(label, Priority.ALWAYS);
        label.maxWidthProperty().bind(parent.widthProperty());

        final MenuItem menuItem = new MenuItem();
        I18NMIQ.get().bind(menuItem.textProperty(), "wizard.menu_item.label.copy");
        menuItem.setOnAction(e -> Utils.copyToClipboard(label.getText()));
        label.setContextMenu(new ContextMenu(menuItem));
        return label;
    }

    @Override
    String getCurrentInput() {
        return this.field.getText();
    }

    @Override
    public void init(final CharSequence input) {
        this.field.setText(SimpleFormField.normalizeInput(input));
    }

    @Override
    void doSet(
            final BiConsumer<N, CharSequence> setter,
            final N n
    ) {
        setter.accept(n, this.getCurrentInput());
    }

    @Override
    void runValidatorsOnError() {
        this.field.pseudoClassStateChanged(Styles.STATE_DANGER, true);
    }

    @Override
    void runValidatorOnSuccess() {
        this.field.pseudoClassStateChanged(Styles.STATE_DANGER, false);
    }
}
