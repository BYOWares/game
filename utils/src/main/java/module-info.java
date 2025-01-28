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
module fr.byowares.game.utils {
    requires org.agrona.core;
    requires org.slf4j;
    requires org.yaml.snakeyaml;
    requires org.apache.logging.log4j.plugins;

    exports fr.byowares.game.utils.hashcodes;
    exports fr.byowares.game.utils.info;
    exports fr.byowares.game.utils.enums;
    exports fr.byowares.game.utils.text;
    exports fr.byowares.game.utils.serial;
    exports fr.byowares.game.utils.serial.source;
    // What follow is a temporary hack, see https://github.com/apache/logging-log4j2/issues/3250
    exports fr.byowares.game.utils.log4j;

    provides org.apache.logging.log4j.plugins.di.spi.ConfigurableInstanceFactoryPostProcessor with fr.byowares.game.utils.log4j.Log4jCorePostProcessor;
}
