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
package fr.byowares.game.app;

import fr.byowares.game.app.i18n.I18NApp;
import fr.byowares.game.app.i18n.I18NHomePage;
import fr.byowares.game.utils.jfx.Styles;
import fr.byowares.game.utils.jfx.i18n.I18NLocaleManager;
import fr.byowares.game.utils.jfx.i18n.Lang;
import fr.byowares.game.utils.jfx.theme.Theme;
import fr.byowares.game.utils.jfx.theme.ThemeManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

import java.util.Objects;
import java.util.Random;

/**
 * Controller for the welcoming page.
 *
 * @since XXX
 */
public class HomePageController {

    private final ToggleGroup themeGroup = new ToggleGroup();
    @FXML
    private Button miqButton;
    @FXML
    private ToggleButton darkTheme;
    @FXML
    private ToggleButton lightTheme;
    @FXML
    private ComboBox<Lang> language;
    @FXML
    private Menu options;
    @FXML
    private Menu about;

    /** FXML handle for initialization. */
    @FXML
    public void initialize() {
        // TODO Retrieve those values from saved options
        final boolean isDarkTheme = new Random().nextBoolean();
        if (isDarkTheme) this.selectDarkTheme();
        else this.selectLightTheme();

        final Lang currentLang = isDarkTheme ? Lang.LANG_UK : Lang.LANG_FR;
        I18NLocaleManager.updateLocale(currentLang.locale());

        /* Menu initialization */
        I18NHomePage.get().bind(this.about.textProperty(), "homepage.menu.about");
        I18NHomePage.get().bind(this.options.textProperty(), "homepage.menu.options");

        /* Theme button initialization */
        this.darkTheme.getStyleClass().add(Styles.LEFT_PILL);
        this.lightTheme.getStyleClass().add(Styles.RIGHT_PILL);

        this.themeGroup.getToggles().addAll(this.darkTheme, this.lightTheme);
        this.darkTheme.selectedProperty().addListener((obs, old, isSelected) -> {
            if (isSelected) this.selectDarkTheme();
            else if (!this.lightTheme.isSelected()) this.lightTheme.setSelected(true); // Always one selected
        });
        this.lightTheme.selectedProperty().addListener((obs, old, isSelected) -> {
            if (isSelected) this.selectLightTheme();
            else if (!this.darkTheme.isSelected()) this.darkTheme.setSelected(true); // Always one selected
        });

        this.darkTheme.setTooltip(new Tooltip());
        this.lightTheme.setTooltip(new Tooltip());
        I18NApp.get().bind(this.darkTheme.getTooltip().textProperty(), "app.theme.dark");
        I18NApp.get().bind(this.lightTheme.getTooltip().textProperty(), "app.theme.light");

        /* Language initialization */
        this.language.getStyleClass().add(Styles.ALT_ICON);
        this.language.getItems().addAll(Lang.LANG_FR, Lang.LANG_UK);
        this.language.setButtonCell(Lang.listCell(false));
        this.language.setCellFactory(c -> Lang.listCell(true));
        this.language.getSelectionModel().select(currentLang);
        this.language.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue)) I18NLocaleManager.updateLocale(newValue.locale());
        });

        /* MIQ section */
        this.miqButton.getStyleClass().add(Styles.FLAT);
    }

    private void selectDarkTheme() {
        ThemeManager.updateTheme(Theme.DARK_THEME);
        this.darkTheme.setSelected(true);
        this.darkTheme.setGraphic(new ImageView(ResourcesApp.ICO_MOON_ON));
        this.lightTheme.setGraphic(new ImageView(ResourcesApp.ICO_SUN_OFF));
    }

    private void selectLightTheme() {
        ThemeManager.updateTheme(Theme.LIGHT_THEME);
        this.lightTheme.setSelected(true);
        this.darkTheme.setGraphic(new ImageView(ResourcesApp.ICO_MOON_OFF));
        this.lightTheme.setGraphic(new ImageView(ResourcesApp.ICO_SUN_ON));
    }

    /** Open a new window to manage options. */
    @FXML
    public void openOptions() {

    }

    /** Open a new window showing information about the platform. */
    @FXML
    public void openAbout() {

    }
}
