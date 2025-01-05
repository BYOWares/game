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

/**
 * @since XXX
 */
module fr.byowares.game.app {
    requires fr.byowares.game.utils.jfx;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    requires org.slf4j;

    opens fr.byowares.game.app to javafx.fxml;
    opens fr.byowares.game.app.i18n to fr.byowares.game.utils.jfx;

    exports fr.byowares.game.app;
}
