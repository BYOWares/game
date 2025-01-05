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

import javafx.css.PseudoClass;
import javafx.scene.control.TabPane;

/**
 * A collection of CSS styles that can be used through this application.
 *
 * @since XXX
 */
public final class Styles {

    // Colors
    /**  */
    public static final String ACCENT = "accent";
    /**  */
    public static final String SUCCESS = "success";
    /**  */
    public static final String WARNING = "warning";
    /**  */
    public static final String DANGER = "danger";

    // Controls
    /**  */
    public static final String TEXT = "text";
    /**  */
    public static final String FONT_ICON = "font-icon";

    /**  */
    public static final String BUTTON_CIRCLE = "button-circle";
    /**  */
    public static final String BUTTON_ICON = "button-icon";
    /**  */
    public static final String BUTTON_OUTLINED = "button-outlined";
    /**  */
    public static final String LEFT_PILL = "left-pill";
    /**  */
    public static final String CENTER_PILL = "center-pill";
    /**  */
    public static final String RIGHT_PILL = "right-pill";
    /**  */
    public static final String SMALL = "small";
    /**  */
    public static final String MEDIUM = "medium";
    /**  */
    public static final String LARGE = "large";
    /**  */
    public static final String TOP = "top";
    /**  */
    public static final String RIGHT = "right";
    /**  */
    public static final String BOTTOM = "bottom";
    /**  */
    public static final String LEFT = "left";
    /**  */
    public static final String CENTER = "center";
    /**  */
    public static final String FLAT = "flat";
    /**  */
    public static final String BORDERED = "bordered";
    /**  */
    public static final String DENSE = "dense";
    /**  */
    public static final String ELEVATED_1 = "elevated-1";
    /**  */
    public static final String ELEVATED_2 = "elevated-2";
    /**  */
    public static final String ELEVATED_3 = "elevated-3";
    /**  */
    public static final String ELEVATED_4 = "elevated-4";
    /**  */
    public static final String INTERACTIVE = "interactive";
    /**  */
    public static final String ROUNDED = "rounded";
    /**  */
    public static final String STRIPED = "striped";
    /**  */
    public static final String TABS_CLASSIC = "classic";
    /**  */
    public static final String TABS_FLOATING = TabPane.STYLE_CLASS_FLOATING;
    /**  */
    public static final String TITLE_1 = "title-1";

    // Text
    /**  */
    public static final String TITLE_2 = "title-2";
    /**  */
    public static final String TITLE_3 = "title-3";
    /**  */
    public static final String TITLE_4 = "title-4";
    /**  */
    public static final String TEXT_CAPTION = "text-caption";
    /**  */
    public static final String TEXT_SMALL = "text-small";
    /**  */
    public static final String TEXT_BOLD = "text-bold";
    /**  */
    public static final String TEXT_BOLDER = "text-bolder";
    /**  */
    public static final String TEXT_NORMAL = "text-normal";
    /**  */
    public static final String TEXT_LIGHTER = "text-lighter";
    /**  */
    public static final String TEXT_ITALIC = "text-italic";
    /**  */
    public static final String TEXT_OBLIQUE = "text-oblique";
    /**  */
    public static final String TEXT_STRIKETHROUGH = "text-strikethrough";
    /**  */
    public static final String TEXT_UNDERLINED = "text-underlined";
    /**  */
    public static final String TEXT_MUTED = "text-muted";
    /**  */
    public static final String TEXT_SUBTLE = "text-subtle";
    /**  */
    public static final String TEXT_ON_EMPHASIS = "text-on-emphasis";
    /**  */
    public static final PseudoClass STATE_ACCENT = PseudoClass.getPseudoClass(ACCENT);

    // Pseudo-classes
    /**  */
    public static final PseudoClass STATE_SUCCESS = PseudoClass.getPseudoClass(SUCCESS);
    /**  */
    public static final PseudoClass STATE_WARNING = PseudoClass.getPseudoClass(WARNING);
    /**  */
    public static final PseudoClass STATE_DANGER = PseudoClass.getPseudoClass(DANGER);
    /**  */
    public static final PseudoClass STATE_INTERACTIVE = PseudoClass.getPseudoClass(INTERACTIVE);
    /**  */
    public static final String BG_DEFAULT = "bg-default";

    // Backgrounds
    /**  */
    public static final String BG_INSET = "bg-inset";
    /**  */
    public static final String BG_SUBTLE = "bg-subtle";
    /**  */
    public static final String BG_NEUTRAL_EMPHASIS_PLUS = "bg-neutral-emphasis-plus";
    /**  */
    public static final String BG_NEUTRAL_EMPHASIS = "bg-neutral-emphasis";
    /**  */
    public static final String BG_NEUTRAL_MUTED = "bg-neutral-muted";
    /**  */
    public static final String BG_NEUTRAL_SUBTLE = "bg-neutral-subtle";
    /**  */
    public static final String BG_ACCENT_EMPHASIS = "bg-accent-emphasis";
    /**  */
    public static final String BG_ACCENT_MUTED = "bg-accent-muted";
    /**  */
    public static final String BG_ACCENT_SUBTLE = "bg-accent-subtle";
    /**  */
    public static final String BG_WARNING_EMPHASIS = "bg-warning-emphasis";
    /**  */
    public static final String BG_WARNING_MUTED = "bg-warning-muted";
    /**  */
    public static final String BG_WARNING_SUBTLE = "bg-warning-subtle";
    /**  */
    public static final String BG_SUCCESS_EMPHASIS = "bg-success-emphasis";
    /**  */
    public static final String BG_SUCCESS_MUTED = "bg-success-muted";
    /**  */
    public static final String BG_SUCCESS_SUBTLE = "bg-success-subtle";
    /**  */
    public static final String BG_DANGER_EMPHASIS = "bg-danger-emphasis";
    /**  */
    public static final String BG_DANGER_MUTED = "bg-danger-muted";
    /**  */
    public static final String BG_DANGER_SUBTLE = "bg-danger-subtle";
    /**  */
    public static final String BORDER_DEFAULT = "border-default";

    // Borders
    /**  */
    public static final String BORDER_MUTED = "border-muted";
    /**  */
    public static final String BORDER_SUBTLE = "border-subtle";

    /** Removes or hides dropdown arrow button. */
    public static final String NO_ARROW = "no-arrow";

    /** Removes external control borders. */
    public static final String EDGE_TO_EDGE = "edge-to-edge";

    /** Removes control header. */
    public static final String NO_HEADER = "no-header";

    /** Alignment left. */
    public static final String ALIGN_LEFT = "align-left";
    public static final String ALIGN_CENTER = "align-center";
    public static final String ALIGN_RIGHT = "align-right";

    /** Forces a control to use alternative icon, if available. */
    public static final String ALT_ICON = "alt-icon";

    private Styles() {
        // Utility class
    }
}
