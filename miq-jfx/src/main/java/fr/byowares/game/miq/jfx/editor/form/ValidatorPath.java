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

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import static java.util.List.of;

/**
 * A validator that ensures the name of file (be it a regular file or a directory).
 *
 * @since XXX
 */
public class ValidatorPath
        implements FormFieldValidator<String> {

    private static final ThreadLocal<StringBuilder> STRING_BUILDER_TL = ThreadLocal.withInitial(StringBuilder::new);
    private static final char[] INVALID_CHARS = {
            '\\', '/', ':', '*', '?', '"', '<', '>', '|', '&', ';', '\'', '`', '(', ')', ',', '='
    };
    private static final List<String> WIN = of("CON", "PRN", "AUX", "NUL",//
                                               "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",//
                                               "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9");

    static {
        Arrays.sort(INVALID_CHARS);
    }

    private final BitSet ctrl;
    private final BitSet chars;
    private final BitSet win;

    /** Instantiate a path validator. */
    public ValidatorPath() {
        this.ctrl = new BitSet();
        this.chars = new BitSet();
        this.win = new BitSet();
    }

    private static String bitSet2String(final BitSet bitSet) {
        final StringBuilder sb = STRING_BUILDER_TL.get();
        sb.setLength(0);
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            if (i == Integer.MAX_VALUE) break; // or (i+1) would overflow
            if (sb.isEmpty()) sb.append("[");
            else sb.append(", ");
            sb.append(i);
        }
        if (!sb.isEmpty()) sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean canContinueAnalysis(
            final CallableList errors,
            final String input
    ) {
        this.ctrl.clear();
        this.chars.clear();
        this.win.clear();

        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (Character.isISOControl(c)) this.ctrl.set(i);
            if (Arrays.binarySearch(INVALID_CHARS, c) >= 0) this.chars.set(i);
        }
        boolean canAnalysisContinue = true;
        if (!this.ctrl.isEmpty()) {
            errors.add(I18NMIQ.get().buildCallable("form_field.path.ctrl", bitSet2String(this.ctrl)));
            canAnalysisContinue = false;
        }
        if (!this.chars.isEmpty()) {
            errors.add(I18NMIQ.get().buildCallable("form_field.path.char", bitSet2String(this.chars)));
            canAnalysisContinue = false;
        }

        final int indexOf = WIN.indexOf(input.toUpperCase());
        if (indexOf >= 0) {
            errors.add(I18NMIQ.get().buildCallable("form_field.path.win", WIN.get(indexOf), WIN));
            canAnalysisContinue = false;
        }

        return canAnalysisContinue;
    }
}
