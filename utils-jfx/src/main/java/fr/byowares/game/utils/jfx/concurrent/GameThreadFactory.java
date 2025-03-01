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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A factory naming threads automatically as we want {@code {groupName}-th-{n}}:
 * <ul>
 *     <li><b>groupName:</b> the name of the group the thread belongs to;</li>
 *     <li><b>n:</b> The  {@code n}th thread created using this factory (starting from 1).</li>
 * </ul>
 *
 * @since XXX
 */
public class GameThreadFactory
        implements ThreadFactory {

    private static final ConcurrentHashMap<String, Integer> GROUP_NAMES = new ConcurrentHashMap<>();

    private static final int MAX_THREADS = Math.max(Math.min(Runtime.getRuntime().availableProcessors() / 2, 4), 1);
    private static final String GAME_WORKER = "GameWorker";
    private static final GameThreadFactory FACTORY = new GameThreadFactory(GAME_WORKER);
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(MAX_THREADS, FACTORY);
    private static final ExecutorService ACCESSIBLE_EXECUTOR = Executors.unconfigurableExecutorService(EXECUTOR);

    private final ThreadGroup group;
    private final AtomicInteger threadCounter;

    /**
     * Creates a new factory as well as a new {@link java.lang.ThreadGroup} whose name will be {@code {group}-{n}}
     * where {@code n} is the {@code n}th thread group with this base group name {@code group} (starting from 1).
     *
     * @param group The base group name.
     */
    GameThreadFactory(final String group) {
        this.group = new ThreadGroup(group + "-" + GROUP_NAMES.compute(group, (g, c) -> (c == null) ? 1 : c + 1));
        this.threadCounter = new AtomicInteger();
    }

    /**
     * @return The default {@link java.util.concurrent.ExecutorService} in charge of handling workload task (thus
     * keeping UI responsive).
     */
    public static ExecutorService defaultExecutorService() {
        return ACCESSIBLE_EXECUTOR;
    }

    @Override
    public Thread newThread(final Runnable r) {
        return new Thread(this.group, r, this.group.getName() + "-th-" + this.threadCounter.incrementAndGet());
    }
}
