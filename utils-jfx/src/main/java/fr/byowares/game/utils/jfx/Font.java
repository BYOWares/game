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
package fr.byowares.game.utils.jfx;

/**
 * List of fonts available withing this package.
 *
 * @since XXX
 */
public enum Font {
    /** Barecast [Regular]. */ BARECAST("Barecast"), //
    /** Inter Black [Italic, Regular]. */ INTER_BLACK("Inter Black"), //
    /** Inter Display Black [Italic, Regular]. */ INTER_DISPLAY_BLACK("Inter Display Black"), //
    /** Inter Display ExtraBold [Italic, Regular]. */ INTER_DISPLAY_EXTRABOLD("Inter Display ExtraBold"), //
    /** Inter Display ExtraLight [Italic, Regular]. */ INTER_DISPLAY_EXTRALIGHT("Inter Display ExtraLight"), //
    /** Inter Display Light [Italic, Regular]. */ INTER_DISPLAY_LIGHT("Inter Display Light"), //
    /** Inter Display Medium [Italic, Regular]. */ INTER_DISPLAY_MEDIUM("Inter Display Medium"), //
    /** Inter Display SemiBold [Italic, Regular]. */ INTER_DISPLAY_SEMIBOLD("Inter Display SemiBold"), //
    /** Inter Display Thin [Italic, Regular]. */ INTER_DISPLAY_THIN("Inter Display Thin"), //
    /** Inter Display [Bold, Bold Italic, Italic, Regular]. */ INTER_DISPLAY("Inter Display"), //
    /** Inter ExtraBold [Italic, Regular]. */ INTER_EXTRABOLD("Inter ExtraBold"), //
    /** Inter ExtraLight [Italic, Regular]. */ INTER_EXTRALIGHT("Inter ExtraLight"), //
    /** Inter Light [Italic, Regular]. */ INTER_LIGHT("Inter Light"), //
    /** Inter Medium [Italic, Regular]. */ INTER_MEDIUM("Inter Medium"), //
    /** Inter SemiBold [Italic, Regular]. */ INTER_SEMIBOLD("Inter SemiBold"), //
    /** Inter Thin [Italic, Regular]. */ INTER_THIN("Inter Thin"), //
    /** Inter [Bold, Bold Italic, Italic, Regular]. */ INTER("Inter"), //
    /** JetBrains Mono ExtraBold [Italic, Regular]. */ JETBRAINS_MONO_EXTRABOLD("JetBrains Mono ExtraBold"), //
    /** JetBrains Mono ExtraLight [Italic, Regular]. */ JETBRAINS_MONO_EXTRALIGHT("JetBrains Mono ExtraLight"), //
    /** JetBrains Mono Light [Italic, Regular]. */ JETBRAINS_MONO_LIGHT("JetBrains Mono Light"), //
    /** JetBrains Mono Medium [Italic, Regular]. */ JETBRAINS_MONO_MEDIUM("JetBrains Mono Medium"), //
    /** JetBrains Mono NL ExtraBold [Italic, Regular]. */ JETBRAINS_MONO_NL_EXTRABOLD("JetBrains Mono NL ExtraBold"), //
    /** JetBrains Mono NL ExtraLight [Italic, Regular]. */
    JETBRAINS_MONO_NL_EXTRALIGHT("JetBrains Mono NL ExtraLight"), //
    /** JetBrains Mono NL Light [Italic, Regular]. */ JETBRAINS_MONO_NL_LIGHT("JetBrains Mono NL Light"), //
    /** JetBrains Mono NL Medium [Italic, Regular]. */ JETBRAINS_MONO_NL_MEDIUM("JetBrains Mono NL Medium"), //
    /** JetBrains Mono NL SemiBold [Italic, Regular]. */ JETBRAINS_MONO_NL_SEMIBOLD("JetBrains Mono NL SemiBold"), //
    /** JetBrains Mono NL Thin [Italic, Regular]. */ JETBRAINS_MONO_NL_THIN("JetBrains Mono NL Thin"), //
    /** JetBrains Mono NL [Bold, Bold Italic, Italic, Regular]. */ JETBRAINS_MONO_NL("JetBrains Mono NL"), //
    /** JetBrains Mono SemiBold [Italic, Regular]. */ JETBRAINS_MONO_SEMIBOLD("JetBrains Mono SemiBold"), //
    /** JetBrains Mono Thin [Italic, Regular]. */ JETBRAINS_MONO_THIN("JetBrains Mono Thin"), //
    /** JetBrains Mono [Bold, Bold Italic, Italic, Regular]. */ JETBRAINS_MONO("JetBrains Mono"), //
    ;

    private final String family;

    private Font(final String family) {
        this.family = family;
    }

    /**
     * @return The Style to add to an FX element to use this font.
     */
    public String toFxFont() {
        return "-fx-font-family: '" + this.family + "';";
    }
}
