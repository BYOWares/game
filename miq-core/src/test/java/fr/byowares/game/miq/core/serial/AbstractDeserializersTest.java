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

import fr.byowares.game.utils.serial.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractDeserializersTest<T>
        extends AbstractDesSerTest<T> {


    @Test
    public final void testValid() {
        final String input = this.getInput();
        assertEquals(this.getObjectT(), this.assertDeserializeDoesNotThrow(input, input, input));
    }

    @Test
    public void testVersionInvalidMissing() {
        final String version = this.getVersion();
        final String input = this.getInput();
        this.assertDeserializeThrows(NullPointerException.class, input, version, "");
        this.assertDeserializeThrows(NullPointerException.class, input, version, "version:");
        this.assertDeserializeThrows(NullPointerException.class, input, version, "version: null");
    }

    @Test
    public void testVersionInvalid0() {
        this.testInvalidVersion("version: 0", "No deserializer matched the given version: " + new Version(0));
    }

    @Test
    public void testVersionInvalidA() {
        this.testInvalidVersion("version: A", "Error at index 0 in: \"A\"");
    }

    protected void testInvalidVersion(
            final String rep,
            final String msg
    ) {
        final String input = this.getInput();
        final String version = this.getVersion();
        final var e = this.assertDeserializeThrows(IllegalArgumentException.class, input, version, rep);
        assertEquals(msg, e.getMessage());
    }

    protected abstract T getObjectT();

    protected abstract String getInput();

    protected abstract String getVersion();
}
