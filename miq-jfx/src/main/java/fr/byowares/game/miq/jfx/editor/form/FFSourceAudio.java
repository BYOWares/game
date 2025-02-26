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

import atlantafx.base.controls.ToggleSwitch;
import fr.byowares.game.miq.core.model.audio.AudioSource;
import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import javafx.animation.Transition;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @param <S> Type of the {@link fr.byowares.game.miq.core.model.audio.AudioSource} managed by this Form Field.
 *
 * @since XXX
 */
public abstract class FFSourceAudio<S extends AudioSource>
        extends ComposedFormField<VBox, Song, S> {

    private static final double H_SPACING = 5.0;

    private final Label label;
    private final ToggleSwitch isSelected;
    private final VBox childrenPane;
    private final List<FFFilePicker<Song>> filePickers;

    /**
     * @param setter      The setter method to update the object.
     * @param i18nKey     The i18n key for the label.
     * @param filePickers All {@link FFFilePicker} composing this Form Field.
     */
    FFSourceAudio(
            final BiConsumer<Song, S> setter,
            final String i18nKey,
            final List<FFFilePicker<Song>> filePickers
    ) {
        super(SimpleFormField.newVBoxContainer(), setter);
        this.filePickers = filePickers;

        this.label = new Label();
        I18NMIQ.get().bind(this.label.textProperty(), i18nKey);
        this.isSelected = new ToggleSwitch();

        final HBox labelBox = new HBox(H_SPACING, this.isSelected, this.label);
        labelBox.setAlignment(Pos.CENTER_LEFT);

        /* Easier to configure a VBox than manage the padding. Align sub elements with the ToggleSwitch "isSelected". */
        final VBox paddingBox = SimpleFormField.newVBoxContainer();
        paddingBox.minWidthProperty().bind(this.isSelected.widthProperty());
        paddingBox.maxWidthProperty().bind(this.isSelected.widthProperty());
        /* The VBox that will contain all children. */
        this.childrenPane = SimpleFormField.newVBoxContainer();
        HBox.setHgrow(this.childrenPane, Priority.ALWAYS);
        final HBox hBox = new HBox(H_SPACING, paddingBox, this.childrenPane);
        hBox.maxWidthProperty().bind(this.getRoot().widthProperty());

        this.getRoot().getChildren().addAll(labelBox, hBox);

        /* This will add the FFFilePicker to this.childrenPane ... */
        filePickers.forEach(this::addFormField);

        /* ... But we want this FF unselected by default (children hidden), so we make sure it ends up unselected.
        The listener must be added after setSelected(true), else we will try to add FF whereas they are already there.*/
        this.isSelected.setSelected(true);
        this.isSelected.selectedProperty().addListener((obs, ov, nv) -> {
            this.setIsIgnored(!nv);
            if (nv) filePickers.stream().map(this::addTransition).forEach(Transition::play);
            else filePickers.forEach(f -> this.getChildrenPane().getChildren().remove(f.getRoot()));
        });
        this.isSelected.setSelected(false);
    }

    /**
     * @param i18n The i18n key for the label of the File Picker.
     *
     * @return A {@link fr.byowares.game.miq.jfx.editor.form.FFFilePicker} whose label is managed by {@code i18n}.
     */
    static FFFilePicker<Song> newPicker(final String i18n) {
        final Consumer<StringProperty> binder = I18NMIQ.binder(I18NMIQ.get(), i18n);
        return new FFFilePicker<>(noOpBiConsumer(), binder, binder);
    }

    private Transition addTransition(final FFFilePicker<Song> ff) {
        return AbstractFormField.addTransition(this.getChildrenPane(), ff.getRoot());
    }

    @Override
    final Pane getChildrenPane() {
        return this.childrenPane;
    }

    /**
     * @param index The index of the FilePicker to get.
     *
     * @return The {@link fr.byowares.game.miq.jfx.editor.form.FFFilePicker} at the given position.
     */
    FFFilePicker<Song> getFilePicker(final int index) {
        return this.filePickers.get(index);
    }

    /**
     * @return {@code true} if this Form Field is selected, {@code false} if it is ignored.
     */
    public boolean isSelected() {
        return this.isSelected.isSelected();
    }

    /**
     * @param selected Whether this Form Field must be selected ({@code true}) or ignored ({@code false}).
     */
    void setSelected(final boolean selected) {
        this.isSelected.setSelected(selected);
    }

    /**
     * @param listener The listener to add to the {@link #isSelected()} property.
     */
    public void addSelectedListener(final ChangeListener<? super Boolean> listener) {
        this.isSelected.selectedProperty().addListener(listener);
    }

    @Override
    final void onLevelUpdate(
            final int oldValue,
            final int newValue
    ) {
        SimpleFormField.updateStyleClass(this.label.getStyleClass(), oldValue, newValue);
        this.filePickers.forEach(ff -> ff.setLevel(newValue));
    }
}
