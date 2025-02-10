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
 * A validator that ensures the input is not null.
 *
 * @since XXX
 */
public class ValidatorNotNull
        implements FormFieldValidator<String> {

    /** Singleton pattern. */
    public static final ValidatorNotNull INSTANCE = new ValidatorNotNull();

    private ValidatorNotNull() {
        // Singleton pattern
    }

    @Override
    public boolean canContinueAnalysis(
            final CallableList errors,
            final String input
    ) {
        if (input == null) {
            errors.add(I18NMIQ.get().buildCallable("form_field.not_null"));
            return false;
        }
        return true;
    }
}
