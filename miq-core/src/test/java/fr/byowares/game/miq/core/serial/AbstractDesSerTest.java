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
package fr.byowares.game.miq.core.serial;

import fr.byowares.game.utils.serial.Deserializers;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractDesSerTest<T> {

    protected static final Class<IllegalArgumentException> IAE_CLASS = IllegalArgumentException.class;

    protected static ByteArrayInputStream inputStream(final String input) {
        return new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    }

    protected <E extends Throwable> E assertDeserializeThrows(
            final Class<E> type,
            final String input,
            final String target,
            final String rep
    ) {
        return assertThrows(type, () -> this.getDeserializers().deserialize(inputStream(input.replace(target, rep))));
    }

    protected T assertDeserializeDoesNotThrow(
            final String input,
            final String target,
            final String rep
    ) {
        return assertDoesNotThrow(() -> this.getDeserializers().deserialize(inputStream(input.replace(target, rep))));
    }

    protected abstract Deserializers<T> getDeserializers();

    protected void assertInvalidWhenMissing(
            final String input,
            final String line,
            final String field
    ) {
        assertTrue(line.startsWith(field));
        final var e1 = this.assertDeserializeThrows(NullPointerException.class, input, line, "");
        final var e2 = this.assertDeserializeThrows(NullPointerException.class, input, line, field + ": ");
        final var e3 = this.assertDeserializeThrows(NullPointerException.class, input, line, field + ": null");
        assertNull(e1.getMessage());
        assertNull(e2.getMessage());
        assertNull(e3.getMessage());
    }

    protected <V> void assertValidWhenMissing(
            final String input,
            final String line,
            final String field,
            final Function<T, ?> getter
    ) {
        final T t1 = this.assertDeserializeDoesNotThrow(input, line, "");
        final T t2 = this.assertDeserializeDoesNotThrow(input, line, field + ": ");
        final T t3 = this.assertDeserializeDoesNotThrow(input, line, field + ": null");
        assertNull(getter.apply(t1));
        assertNull(getter.apply(t2));
        assertNull(getter.apply(t3));
    }
}
