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

import fr.byowares.game.app.ResourcesApp;
import fr.byowares.game.app.i18n.I18NApp;
import fr.byowares.game.app.info.AppInfo;
import fr.byowares.game.utils.enums.Size;
import fr.byowares.game.utils.jfx.ConRoot;
import fr.byowares.game.utils.jfx.Controller;
import fr.byowares.game.utils.jfx.fxml.FXMLLoader;
import fr.byowares.game.utils.jfx.i18n.I18NLocaleManager;
import fr.byowares.game.utils.jfx.theme.ThemeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static java.lang.String.format;
import static java.lang.System.getProperty;

/**
 * A controller for the About.
 *
 * @since XXX
 */
public class About
        implements Controller {

    private static final String TEMPLATE_YAML = """
            BYOWares Games:
                build:
                    version: %s
                    revision: %s
                    timestamp: %s
                java:
                    name: %s
                    version: %s
                    vendor: %s
                os:
                    name: %s
                    version: %s
                    arch: %s
                process:
                    gc_algo: %s
                    max_memory: %dMB
            """;
    private static final String TEMPLATE_BODY = """
            Build %s, %s
            
            Runtime version: %s %s
            VM: %s VM by %s
            
            Copyright © BYOWares
            """;

    private String yml = "NOT INITIALIZED";

    @FXML
    private ImageView icon;
    @FXML
    private Text title;
    @FXML
    private Text body;
    @FXML
    private Button copyAndClose;
    @FXML
    private Button close;

    private static String buildTextAsYaml() {
        final StringBuilder sb = new StringBuilder();
        ManagementFactory.getGarbageCollectorMXBeans().forEach(b -> {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(b.getName());
        });
        return format(TEMPLATE_YAML, //
                      AppInfo.VERSION, AppInfo.REVISION, AppInfo.BUILD_TIMESTAMP, //
                      getProperty("java.vm.name"), getProperty("java.runtime.version"), getProperty("java.vendor"), //
                      getProperty("os.name"), getProperty("os.version"), getProperty("os.arch"), //
                      sb, Size.BYTE.toMebiBytes(Runtime.getRuntime().maxMemory()));
    }

    private static String buildText() {
        final var formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG) //
                .withLocale(I18NLocaleManager.get()) //
                .withZone(ZoneOffset.UTC) //
                ;
        return format(TEMPLATE_BODY, //
                      AppInfo.REVISION, formatter.format(Instant.ofEpochMilli(Long.parseLong(AppInfo.BUILD_TIMESTAMP))),
                      getProperty("java.runtime.version"), getProperty("os.arch"), //
                      getProperty("java.vm.name"), getProperty("java.vendor"));
    }

    /**
     * Load a new instance of the About.
     *
     * @return The pair (Controller, BorderPane) in charge of the About.
     */
    public static ConRoot<About, BorderPane> load() {
        return FXMLLoader.load(new About(), About.class, "About");
    }

    /** FXML handle for initialization. */
    @FXML
    public void initialize() {
        this.icon.setImage(ResourcesApp.GAME_64);
        this.yml = buildTextAsYaml();
        I18NApp.get().bind(this.title.textProperty(), "title", AppInfo.VERSION);
        this.body.setText(buildText());
        I18NApp.get().bind(this.close.textProperty(), "about.close");
        I18NApp.get().bind(this.copyAndClose.textProperty(), "about.copy_and_close");
        this.onDisplay();
    }

    /**
     * Close the window. Calls {@link #onHide()} before doing so.
     *
     * @param event The event responsible for the close request.
     */
    @FXML
    public void close(final ActionEvent event) {
        this.onHide();
        ((Stage) this.close.getScene().getWindow()).close();
    }

    /**
     * Copy content in YAML format, before closing the window.
     *
     * @param event The event responsible for the close request.
     *
     * @see #close
     */
    @FXML
    public void copyAndClose(final ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(this.yml);
        Clipboard.getSystemClipboard().setContent(content);
        this.close(event);
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
