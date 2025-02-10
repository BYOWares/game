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
package fr.byowares.game.miq.jfx.fxml;

import fr.byowares.game.miq.jfx.ResourcesMIQ;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.jfx.SceneUniqueActor;
import fr.byowares.game.utils.jfx.fxml.GameMenu;
import fr.byowares.game.utils.jfx.i18n.I18NUtils;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

import java.util.function.Consumer;

/**
 * A MIQ skin for the GameMenu graphics.
 *
 * @see fr.byowares.game.utils.jfx.fxml.GameMenu
 * @since XXX
 */
public class GameMenuMIQ
        extends GameMenu {

    private static final Consumer<StringProperty> TITLE_BINDER = I18NMIQ.binder(I18NMIQ.get(), "root.full_name");

    private GameMenuMIQ(final SceneUniqueActor sceneUniqueActor) {
        super(sceneUniqueActor, TITLE_BINDER, ResourcesMIQ.GA_MIQ_CSS);
    }

    /**
     * A MIQ skin {@link fr.byowares.game.utils.jfx.fxml.GameAccess}.
     *
     * @param actor The {@link fr.byowares.game.utils.jfx.SceneUniqueActor} in charge of the scene while this graphic is displayed.
     *
     * @return The {@code SceneUniqueActor} in charge of the menu.
     *
     * @see fr.byowares.game.utils.jfx.fxml.GameMenu#load(fr.byowares.game.utils.jfx.fxml.GameMenu)
     */
    public static GameMenuMIQ load(final SceneUniqueActor actor) {
        return GameMenu.load(new GameMenuMIQ(actor));
    }

    /** FXML handle for initialization. */
    @Override
    @FXML
    public void initialize() {
        super.initialize();
        this.addMenuButton(I18NUtils.get(), "menu.play", () -> {});
        this.addMenuButton(I18NUtils.get(), "menu.configure", () -> {
            EditView.load(new EditView(this)).takeControlOfScene();
        });
        this.addMenuButton(I18NUtils.get(), "menu.options", () -> {});
    }
}
