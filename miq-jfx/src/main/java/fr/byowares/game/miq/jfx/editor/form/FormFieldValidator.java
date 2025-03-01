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

/**
 * A simple Form Field validator. Validators are expected to be chained.
 *
 * @param <T> Type managed by Form Field (the Form Field can be a Text field, but representing a Path: in such a
 *            case, {@code T} is expected to be of String type).
 *
 * @since XXX
 */
@FunctionalInterface
public interface FormFieldValidator<T> {

    /**
     * Perform some checks on the input. All errors detected shall be added to the list of errors.
     *
     * @param errors The list of errors (in Callable, dependent of the locale, format).
     * @param input  The current value in the Form Field.
     *
     * @return {@code true} whether the chain analysis can proceed, {@code false} if it must stop.
     */
    boolean canContinueAnalysis(
            CallableList errors,
            T input
    );

}
