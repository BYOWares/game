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

import fr.byowares.game.miq.jfx.i18n.I18NMIQ;

import java.util.List;

/**
 * A validator that ensure a minimum number of {@link fr.byowares.game.miq.jfx.editor.form.FFSourceAudio} are selected.
 *
 * @since XXX
 */
public class ValidatorSourcesMinimumSelected
        implements FormFieldValidator<Void> {

    private final List<FFSourceAudio<?>> ffSources;
    private final long minSelected;
    private final String i18n;

    /**
     * @param ffSources   The list of {@link fr.byowares.game.miq.jfx.editor.form.FFSourceAudio} to check.
     * @param minSelected The minimum of element in the list that must be selected.
     * @param i18n        The i18n key for the error message (first parameter is {@code minSelected}, second is actual
     *                    count).
     */
    public ValidatorSourcesMinimumSelected(
            final List<FFSourceAudio<?>> ffSources,
            final long minSelected,
            final String i18n
    ) {
        this.ffSources = ffSources;
        this.minSelected = minSelected;
        this.i18n = i18n;
    }

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
