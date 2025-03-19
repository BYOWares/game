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
module fr.byowares.game.miq.jfx {
    requires atlantafx.base;
    requires fr.byowares.game.miq.core;
    requires fr.byowares.game.utils.jfx;
    requires fr.byowares.game.utils;
    requires javafx.fxml;
    requires javafx.media;
    requires org.agrona.core;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.bootstrapicons;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.feather;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.slf4j;

    exports fr.byowares.game.miq.jfx;
    exports fr.byowares.game.miq.jfx.fxml;

    opens fr.byowares.game.miq.jfx.fxml to javafx.fxml, fr.byowares.game.utils.jfx;
    opens fr.byowares.game.miq.jfx.fxml.wizard to javafx.fxml, fr.byowares.game.utils.jfx;
    opens fr.byowares.game.miq.jfx.i18n to fr.byowares.game.utils.jfx;
}
