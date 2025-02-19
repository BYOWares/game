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
package fr.byowares.game.app.fxml;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import fr.byowares.game.app.ResourcesApp;
import fr.byowares.game.app.i18n.I18NApp;
import fr.byowares.game.app.info.AppInfo;
import fr.byowares.game.miq.jfx.fxml.GameAccessMIQ;
import fr.byowares.game.utils.jfx.FontIconSizeEnforcer;
import fr.byowares.game.utils.jfx.Pair;
import fr.byowares.game.utils.jfx.SceneUniqueActor;
import fr.byowares.game.utils.jfx.fxml.FXMLLoader;
import fr.byowares.game.utils.jfx.i18n.I18NLocaleManager;
import fr.byowares.game.utils.jfx.i18n.Lang;
import fr.byowares.game.utils.jfx.theme.ThemeManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Random;

/**
 * SceneUniqueActor for the HomePage.
 *
 * @since XXX
 */
public class HomePage
        extends SceneUniqueActor {

    private static final Logger log = LoggerFactory.getLogger(HomePage.class);
    private static final int SIZE_IN_PX = 52;

    @FXML private ToggleButton darkTheme;
    @FXML private ToggleButton lightTheme;
    @FXML private ComboBox<Lang> language;
    @FXML private VBox mainVBox;
    @FXML private Button about;
    @FXML private Label build;

    private HomePage(
            final Stage stage,
            final StackPane root
    ) {
        super(stage, root);
    }

    /**
     * Load a new instance of the HomePage.
     *
     * @param stage     The stage
     * @param sceneRoot The node
     *
     * @return The controller in charge of the HomePage.
     */
    public static HomePage load(
            final Stage stage,
            final StackPane sceneRoot
    ) {
        return FXMLLoader.loadActor(new HomePage(stage, sceneRoot), HomePage.class, "HomePage");
    }

    /** FXML handle for initialization. */
    @FXML
    public void initialize() {
        final boolean isDarkTheme = new Random().nextBoolean();
        if (isDarkTheme) this.selectDarkTheme();
        else this.selectLightTheme();

        final Lang currentLang = isDarkTheme ? Lang.LANG_UK : Lang.LANG_FR;
        I18NLocaleManager.updateLocale(currentLang.locale());

        /* Theme button initialization */
        this.darkTheme.getStyleClass().add(Styles.LEFT_PILL);
        this.lightTheme.getStyleClass().add(Styles.RIGHT_PILL);

        new ToggleGroup().getToggles().addAll(this.darkTheme, this.lightTheme);
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
        I18NApp.get().bind(this.darkTheme.getTooltip().textProperty(), "theme.dark");
        I18NApp.get().bind(this.lightTheme.getTooltip().textProperty(), "theme.light");

        /* Language initialization */
        this.language.getStyleClass().add(Tweaks.ALT_ICON);
        this.language.getItems().addAll(Lang.LANG_FR, Lang.LANG_UK);
        this.language.setButtonCell(Lang.listCell(false));
        this.language.setCellFactory(c -> Lang.listCell(true));
        this.language.getSelectionModel().select(currentLang);
        this.language.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue)) I18NLocaleManager.updateLocale(newValue.locale());
        });

        this.about.setTooltip(new Tooltip());
        final FontIcon fontIcon = new FontIcon(Feather.INFO);
        fontIcon.getStyleClass().add(FontIconSizeEnforcer.enforceIconSizeCSS(this.about, "about-icon", SIZE_IN_PX));
        this.about.setGraphic(fontIcon);
        I18NApp.get().bind(this.about.getTooltip().textProperty(), "homepage.about");

        this.build.setText("Version: " + AppInfo.VERSION + "   Build: " + AppInfo.REVISION);

        final Pair<GameAccessMIQ, HBox> miqAccess = GameAccessMIQ.load(this);
        this.addController(miqAccess.controller());
        this.mainVBox.getChildren().add(miqAccess.root());
    }

    private void selectDarkTheme() {
        ThemeManager.updateTheme(new PrimerDark());
        this.darkTheme.setSelected(true);
        this.darkTheme.setGraphic(new ImageView(ResourcesApp.ICO_MOON_ON));
        this.lightTheme.setGraphic(new ImageView(ResourcesApp.ICO_SUN_OFF));
    }

    private void selectLightTheme() {
        ThemeManager.updateTheme(new PrimerLight());
        this.lightTheme.setSelected(true);
        this.darkTheme.setGraphic(new ImageView(ResourcesApp.ICO_MOON_OFF));
        this.lightTheme.setGraphic(new ImageView(ResourcesApp.ICO_SUN_ON));
    }

    /** Open the About Dialog. */
    @FXML
    public void openAboutDialog() {
        About.open(this.getStage());
    }

    @Override
    public void onDisplay() {
        ThemeManager.subscribe(this.about);
    }

    @Override
    public void onHide() {
        ThemeManager.unsubscribe(this.about);
    }
}
