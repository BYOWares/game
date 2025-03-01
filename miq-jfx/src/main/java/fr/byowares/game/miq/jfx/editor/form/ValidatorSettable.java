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

import java.util.concurrent.Callable;

/**
 * A validator whose set of error can be set or clear ahead of the actual validation. It shall be used when the
 * validation must occur on a worker Thread (and that the object required can be costly to get).
 *
 * @param <T> The type of the converted Form Field (the Form Field can be a Text field, but representing a Path: in
 *            such a case, {@code T} is expected to be of Path type).
 *
 * @since XXX
 */
public class ValidatorSettable<T>
        implements FormFieldValidator<T> {

    private final CallableList errors;

    /** A new validator with no error. */
    public ValidatorSettable() {
        this.errors = new CallableList();
    }

    /**
     * @param error The error to add to the list.
     */
    void addError(final Callable<String> error) {
        this.errors.add(error);
    }

    /** Clear all errors. */
    void clearErrors() {
        this.errors.clear();
    }

    @Override
    public boolean canContinueAnalysis(
            final CallableList errors,
            final T input
    ) {
        errors.addAll(this.errors);
        return true;
    }
}
