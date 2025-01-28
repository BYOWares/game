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
package fr.byowares.game.utils.log4j;

import org.apache.logging.log4j.kit.recycler.RecyclerFactory;
import org.apache.logging.log4j.kit.recycler.RecyclerFactoryProvider;
import org.apache.logging.log4j.plugins.di.ConfigurableInstanceFactory;
import org.apache.logging.log4j.plugins.di.Key;
import org.apache.logging.log4j.plugins.di.spi.ConfigurableInstanceFactoryPostProcessor;

/**
 * @see https://github.com/apache/logging-log4j2/issues/3250
 * @since XXX
 */
public class Log4jCorePostProcessor
        implements ConfigurableInstanceFactoryPostProcessor {
    @Override
    public void postProcessFactory(final ConfigurableInstanceFactory factory) {
        factory.registerBinding(Key.forClass(RecyclerFactory.class),
                                () -> RecyclerFactoryProvider.getInstance().createForEnvironment(null));
    }
}
