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
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static javafx.beans.binding.Bindings.min;
import static javafx.beans.binding.Bindings.size;

/**
 * A Form used to edit a text field with suggestion.
 *
 * @param <N> Type of object whose field must be edited.
 *
 * @since XXX
 */
public class FFTextSuggestion<N extends NamedSourcedObject>
        extends FormField<N, CharSequence, String> {

    private static final int LINE_HEIGHT = 37;
    private static final int POPUP_MARGIN = 12;
    private static final int MAX_LINE = 10;

    private final TextField field;

    /**
     * @param setter     The setter method to update the object.
     * @param i18nBinder The binder for the label of the Form Field.
     * @param items      The list of suggestion.
     */
    public FFTextSuggestion(
            final BiConsumer<N, CharSequence> setter,
            final Consumer<StringProperty> i18nBinder,
            final ObservableList<String> items
    ) {
        super(setter, i18nBinder);
        this.field = new TextField();

        final var binding = TextFields.bindAutoCompletion(this.field, new SuggestionProvider(items));
        binding.setVisibleRowCount(MAX_LINE);
        binding.minWidthProperty().bind(this.getFFContainer().widthProperty());
        final PopupControl popup = binding.getAutoCompletionPopup();
        popup.getStyleClass().add("combo-box-popup");
        popup.getStyleClass().remove("auto-complete-popup"); // Coming from controlsfx jar

        popup.skinProperty().addListener((obs, ov, nv) -> {
            // Override the prefHeightDefined in AutoCompletePopupSkin (Line height is too small for our themes).
            final Node node = nv.getNode();
            if (node instanceof final ListView<?> listView) {
                final var add = min(MAX_LINE, size(listView.getItems())).multiply(LINE_HEIGHT).add(POPUP_MARGIN);
                listView.prefHeightProperty().bind(add);
            }
        });

        this.field.textProperty().addListener((o, ov, nv) -> this.runValidators(nv));
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

    /** A simple suggestion provider wrapping an {@link javafx.collections.ObservableList}. */
    private static class SuggestionProvider
            implements Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>> {

        private final FilteredList<String> suggestions;

        private SuggestionProvider(final ObservableList<String> suggestions) {
            this.suggestions = new FilteredList<>(suggestions);
        }

        @Override
        public Collection<String> call(final AutoCompletionBinding.ISuggestionRequest request) {
            final String i = request.getUserText();
            final Predicate<String> p = i.isEmpty() ? x -> true : x -> x.toLowerCase().contains(i.toLowerCase());
            this.suggestions.setPredicate(p);
            return this.suggestions;
        }
    }
}
