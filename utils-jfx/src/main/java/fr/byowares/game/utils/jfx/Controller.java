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
package fr.byowares.game.utils.jfx;

/**
 * Common interface for all controllers used in this application.
 *
 * @since XXX
 */
public interface Controller {

    /**
     * Called when this controller is (or is owned by) a {@link fr.byowares.game.utils.jfx.SceneUniqueActor} that
     * took control of the scene.
     */
    void onDisplay();

    /**
     * Called when this controller is (or is owned by) a {@link fr.byowares.game.utils.jfx.SceneUniqueActor} just
     * lost control of the scene.
     */
    void onHide();

}
