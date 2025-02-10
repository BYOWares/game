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
package fr.byowares.game.miq.jfx.editor.form;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * A list of Callable. Calling {@link #call()} on this object will join all individual call to {@link #call()} with a
 * {@link System#lineSeparator()}.
 *
 * @since XXX
 */
public class CallableList
        extends ArrayList<Callable<String>>
        implements Callable<String> {

    @Override
    public String call() {
        return this.stream().map(c -> {
            try {
                return c.call();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining(System.lineSeparator(), "", ""));
    }
}
