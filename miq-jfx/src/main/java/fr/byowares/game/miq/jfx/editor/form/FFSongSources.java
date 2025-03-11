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
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.VBox;
import org.agrona.LangUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A Form Field that allows to configure a {@link fr.byowares.game.miq.core.model.audio.SingleSource} and a
 * {@link fr.byowares.game.miq.core.model.audio.DuoSource}, and at least one must be configured.
 *
 * @since XXX
 */
public class FFSongSources
        extends ComposedFormField<VBox, Song, Song> {

    private static final Logger log = LoggerFactory.getLogger(FFSongSources.class);

    private final FFSourceSingle single;
    private final FFSourceDuo duo;

    /** Default instance. */
    public FFSongSources() {
        super(SimpleFormField.newVBoxContainer(), noOpBiConsumer(), MIN_SIZE_LARGE, MIN_SIZE_NONE);

        this.single = new FFSourceSingle();
        this.duo = new FFSourceDuo();

        final List<FFSourceAudio<?>> sources = List.of(this.single, this.duo);
        this.addValidator(new ValidatorSourcesMinimumSelected(sources, 1L, "form_field.source_min.configuration"));
        this.addValidator(new Validator(sources, FFFilePicker::addFileNameToMapCounter, "wizard.ff.filename_error"));
        this.addValidator(new Validator(sources, FFFilePicker::addFilePathToMapCounter, "wizard.ff.filepath_error"));

        final ChangeListener<String> sListener = (obs, ov, nv) -> this.runValidators(null);
        for (final FFSourceAudio<?> source : sources) {
            source.addSelectedListener((obs, ov, nv) -> this.runValidators(null));
            for (int i = 0; i < source.getFilePickersCount(); i++) {
                final FFFilePicker<Song> ffFilePicker = source.getFilePicker(i);
                ffFilePicker.addFileNameListener(sListener);
                ffFilePicker.addFileSelectorListener(sListener);
            }
        }
        sources.forEach(this::registerFormField);
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

        final List<FFFilePicker.FileOperationContext> contexts = new ArrayList<>();
        try {
            if (this.single.isSelected()) {
                contexts.add(this.single.getFilePicker(0).initiateFileOperation(song.getSingleSource().audioSource()));
            }
            if (this.duo.isSelected()) {
                contexts.add(this.duo.getFilePicker(0).initiateFileOperation(song.getDuoSource().voiceSource()));
                contexts.add(this.duo.getFilePicker(1).initiateFileOperation(song.getDuoSource().musicSource()));
            }

            for (final FFFilePicker.FileOperationContext context : contexts) {
                context.finalizeOperation();
            }
        } catch (final IOException e) {
            log.warn("Failed to set audio sources for {}", song, e);
            LangUtil.rethrowUnchecked(e);
        }
    }

    @Override
    void runValidatorsOnError() {
        // Nothing to do
    }

    @Override
    void runValidatorOnSuccess() {
        // Nothing to do
    }

    /**
     * Ensure a minimum {@link fr.byowares.game.miq.jfx.editor.form.FFSourceAudio} are selected.
     */
    private record ValidatorSourcesMinimumSelected(
            List<FFSourceAudio<?>> ffSources,
            long minSelected,
            String i18n
    )
            implements FormFieldValidator<Void> {

        @Override
        public boolean canContinueAnalysis(
                final CallableList errors,
                final Void input
        ) {
            final long count = this.ffSources.stream().filter(FFSourceAudio::isSelected).count();
            if (count < this.minSelected) errors.add(I18NMIQ.get().buildCallable(this.i18n, this.minSelected, count));
            return true;
        }
    }

    /**
     * Ensure the same value inside several {@link FFFilePicker} does not appear more than once.
     */
    private record Validator(
            List<FFSourceAudio<?>> ffSources,
            BiConsumer<FFFilePicker<?>, Map<String, Integer>> mapUpdater,
            String i18n
    )
            implements FormFieldValidator<Void> {

        @Override
        public boolean canContinueAnalysis(
                final CallableList errors,
                final Void input
        ) {
            final Map<String, Integer> map = new HashMap<>();
            for (final FFSourceAudio<?> ffSource : this.ffSources) {
                if (!ffSource.isSelected()) continue;
                for (int i = 0; i < ffSource.getFilePickersCount(); i++) {
                    this.mapUpdater.accept(ffSource.getFilePicker(i), map);
                }
            }
            map.forEach((k, v) -> {
                if (v > 1) errors.add(I18NMIQ.get().buildCallable(this.i18n, k, v));
            });
            return true;
        }
    }
}
