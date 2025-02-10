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

/**
 * A validator that ensures the length of a given field.
 *
 * @since XXX
 */
public class ValidatorLength
        implements FormFieldValidator<String> {

    private final int minLength;
    private final int maxLength;

    /**
     * @param minLength The minimum authorized length.
     * @param maxLength The maximum authorized length.
     *
     * @throws java.lang.IllegalArgumentException If {@code minLength} is negative or if {@code maxLength} is
     *                                            strictly lower than {@code minLength}.
     */
    public ValidatorLength(
            final int minLength,
            final int maxLength
    ) {
        if (minLength < 0) throw new IllegalArgumentException("minLength must be higher than 0 (" + minLength + ")");
        if (maxLength < minLength) throw new IllegalArgumentException(
                "maxLength must be higher or equal than minLength (min" + minLength + ", max=" + maxLength + ")");
        this.maxLength = maxLength;
        this.minLength = minLength;
    }


    @Override
    public boolean canContinueAnalysis(
            final CallableList errors,
            final String input
    ) {
        final int l = input.length();
        if (this.minLength > l) errors.add(I18NMIQ.get().buildCallable("form_field.length.min", this.minLength, l));
        if (this.maxLength < l) errors.add(I18NMIQ.get().buildCallable("form_field.length.max", this.maxLength, l));
        return true;
    }
}
