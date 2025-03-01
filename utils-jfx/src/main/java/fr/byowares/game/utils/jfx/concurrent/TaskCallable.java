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
package fr.byowares.game.utils.jfx.concurrent;

import org.agrona.collections.LongLongConsumer;

import java.util.function.BooleanSupplier;

/**
 * An extended {@link java.util.concurrent.Callable} in order to be called inside a {@link javafx.concurrent.Task}.
 *
 * @param <V> Type of object computed.
 *
 * @since XXX
 */
@FunctionalInterface
public interface TaskCallable<V> {

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @param progressUpdater The task progress updater.
     * @param cancelChecker   The cancel checker.
     *
     * @return computed result
     *
     * @throws Exception if unable to compute a result
     */
    V call(
            LongLongConsumer progressUpdater,
            BooleanSupplier cancelChecker
    )
            throws Exception;
}
