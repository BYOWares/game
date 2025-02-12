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
package fr.byowares.game.utils.jfx.theme;

import atlantafx.base.theme.Theme;
import fr.byowares.game.utils.jfx.Resources;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A manager to help refresh when Theme is updated. Handle default font as well.
 *
 * @since XXX
 */
public class ThemeManager {

    private static final ThemeManager INSTANCE = new ThemeManager();

    private static final PseudoClass DARK = PseudoClass.getPseudoClass("dark");
    private static final Duration DURATION = Duration.millis(750.0);
    private static final Interpolator EASE = Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0);

    private final Set<Scene> scenes;
    private final Set<Node> nodes;
    private Theme currentTheme = null;

    private ThemeManager() {
        this.scenes = new CopyOnWriteArraySet<>();
        this.nodes = new CopyOnWriteArraySet<>();
    }

    /**
     * Update the theme and refresh all pages.
     *
     * @param theme The new theme to use.
     */
    public static synchronized void updateTheme(final Theme theme) {
        Objects.requireNonNull(theme);
        if (theme == INSTANCE.currentTheme) return;

        if (INSTANCE.currentTheme != null) {
            fadingAnimation();
        }
        Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());

        for (final Scene scene : INSTANCE.scenes)
            updateThemeForScene(scene, theme);

        for (final Node node : INSTANCE.nodes)
            node.pseudoClassStateChanged(DARK, theme.isDarkMode());

        INSTANCE.currentTheme = theme;
    }

    /** Smooth animation for all scenes subscribed to the unique {@link fr.byowares.game.utils.jfx.theme.ThemeManager} */
    public static void fadingAnimation() {
        for (final Scene scene : INSTANCE.scenes)
            animateThemeChange(scene);
    }

    /**
     * Subscribe the {@code scene} from Theme update. Set the default font for this scene as well.
     *
     * @param scene The scene that needs to be updated when the theme is modified.
     */
    public static synchronized void subscribe(final Scene scene) {
        scene.getStylesheets().add(Resources.CSS_DEFAULT_FONT);
        scene.getStylesheets().add(Resources.CSS_BUGFIX_4ADB8FF);
        INSTANCE.scenes.add(scene);
        updateThemeForScene(scene, INSTANCE.currentTheme);
    }

    /**
     * Subscribe the {@code node} from Theme update.
     *
     * @param node The Node that needs to be updated when the theme is modified.
     */
    public static synchronized void subscribe(final Node node) {
        INSTANCE.nodes.add(node);
        updateThemeForNode(node, INSTANCE.currentTheme);
    }

    /**
     * Unsubscribe the {@code scene} from Theme update.
     *
     * @param scene The scene that needs to stop receiving update when the theme is updated.
     */
    public static void unsubscribe(final Scene scene) {
        INSTANCE.scenes.remove(scene);
    }

    /**
     * Unsubscribe the {@code node} from Theme update.
     *
     * @param node The node that needs to stop receiving update when the theme is updated.
     */
    public static void unsubscribe(final Node node) {
        INSTANCE.nodes.remove(node);
    }

    private static void updateThemeForScene(
            final Scene scene,
            final Theme newTheme
    ) {
        if (newTheme == null) return;
        scene.getRoot().pseudoClassStateChanged(DARK, newTheme.isDarkMode());
    }

    private static void updateThemeForNode(
            final Node node,
            final Theme newTheme
    ) {
        if (newTheme == null) return;
        node.pseudoClassStateChanged(DARK, newTheme.isDarkMode());
    }

    /* In order to make the animation pretty, all scene root element shall be StackPane. */
    private static void animateThemeChange(final Scene scene) {
        final Image snapshot = scene.snapshot(null);
        final Pane root = (Pane) scene.getRoot();

        final ImageView imageView = new ImageView(snapshot);
        root.getChildren().add(imageView);

        final var transition = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(imageView.opacityProperty(), 1, EASE)),
                new KeyFrame(ThemeManager.DURATION, new KeyValue(imageView.opacityProperty(), 0, EASE)));
        transition.setOnFinished(e -> root.getChildren().remove(imageView));
        transition.play();
    }
}
