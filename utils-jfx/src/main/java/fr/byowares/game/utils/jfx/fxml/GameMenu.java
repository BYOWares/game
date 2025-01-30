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
package fr.byowares.game.utils.jfx.fxml;

import fr.byowares.game.utils.jfx.SceneUniqueActor;
import fr.byowares.game.utils.jfx.i18n.I18NResourceBundle;
import fr.byowares.game.utils.jfx.i18n.I18NUtils;
import fr.byowares.game.utils.jfx.theme.ThemeManager;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

/**
 * @since XXX
 */
public class GameMenu
        extends SceneUniqueActor {

    private final Consumer<StringProperty> titleBinder;
    private final String styleSheetURL;

    @FXML
    private HBox holderBackMenu;
    @FXML
    private VBox holderMenus;
    @FXML
    private Label icon;
    @FXML
    private Text title;

    /**
     * @param sceneUniqueActor The SceneUniqueActor in which this menu is used.
     * @param titleBinder      The {@link javafx.beans.property.StringProperty} binder to the title text.
     * @param styleSheetURL    The Custom CSS to use (to skin the button).
     */
    public GameMenu(
            final SceneUniqueActor sceneUniqueActor,
            final Consumer<StringProperty> titleBinder,
            final String styleSheetURL
    ) {
        super(sceneUniqueActor);
        this.titleBinder = titleBinder;
        this.styleSheetURL = styleSheetURL;
    }

    /**
     * @param gm  The {@link fr.byowares.game.utils.jfx.fxml.GameMenu} to
     * @param <G> The type of the Controller.
     *
     * @return {@code gm}.
     */
    protected static <G extends GameMenu> G load(final G gm) {
        return FXMLLoader.loadActor(gm, GameMenu.class, "GameMenu");
    }

    /** FXML handle for initialization. */
    @FXML
    public void initialize() {
        this.titleBinder.accept(this.title.textProperty());
        this.icon.getStylesheets().add(this.styleSheetURL);
        final var p = GameMenuButton.load(I18NUtils.binder(I18NUtils.get(), "menu.back"), this::giveBackControlOfScene);
        this.holderBackMenu.getChildren().add(p.root());
    }

    /**
     * @param bundle   The {@link java.util.ResourceBundle} containing the i18n key.
     * @param i18n     The i18n key for the button's text.
     * @param onAction The action to perform when actioning this button.
     */
    protected void addMenuButton(
            final I18NResourceBundle bundle,
            final String i18n,
            final Runnable onAction
    ) {
        final var pair = GameMenuButton.load(I18NResourceBundle.binder(bundle, i18n), onAction);
        this.holderMenus.getChildren().add(pair.root());
        this.addController(pair.controller());
    }

    @Override
    public void onDisplay() {
        ThemeManager.subscribe(this.icon);
    }

    @Override
    public void onHide() {
        ThemeManager.unsubscribe(this.icon);
    }
}
