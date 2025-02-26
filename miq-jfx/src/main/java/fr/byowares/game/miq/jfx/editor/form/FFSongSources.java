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

import fr.byowares.game.miq.core.model.song.Song;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * A Form Field that allows to configure a {@link fr.byowares.game.miq.core.model.audio.SingleSource} and a
 * {@link fr.byowares.game.miq.core.model.audio.DuoSource}, and at least one must be configured.
 *
 * @since XXX
 */
public class FFSongSources
        extends ComposedFormField<VBox, Song, Song> {

    private final FFSourceSingle single;
    private final FFSourceDuo duo;

    /** Default instance. */
    public FFSongSources() {
        super(SimpleFormField.newVBoxContainer(), ComposedFormField.noOpBiConsumer());

        this.single = new FFSourceSingle();
        this.duo = new FFSourceDuo();
        final ChangeListener<Boolean> listener = (obs, ov, nv) -> this.runValidators(null);
        this.single.addSelectedListener(listener);
        this.duo.addSelectedListener(listener);

        final List<FFSourceAudio<?>> sources = List.of(this.single, this.duo);
        this.addValidator(new ValidatorSourcesMinimumSelected(sources, 1L, "form_field.source_min.configuration"));
        sources.forEach(this::addFormField);
    }

    @Override
    public void init(final Song input) {
        if (input == null) {
            this.single.init(null);
            this.duo.init(null);
        } else {
            this.single.init(input.getSingleSource());
            this.duo.init(input.getDuoSource());
        }
        this.runValidators(null);
    }

    @Override
    final void onLevelUpdate(
            final int oldValue,
            final int newValue
    ) {
        this.single.setLevel(newValue);
        this.duo.setLevel(newValue);
    }

    @Override
    void doSet(
            final BiConsumer<Song, Song> setter,
            final Song song
    ) {
        this.single.set(song);
        this.duo.set(song);
    }

    @Override
    void runValidatorsOnError() {

    }

    @Override
    void runValidatorOnSuccess() {

    }
}
