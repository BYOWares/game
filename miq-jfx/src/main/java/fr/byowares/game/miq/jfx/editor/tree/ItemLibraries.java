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
package fr.byowares.game.miq.jfx.editor.tree;

import fr.byowares.game.miq.core.model.song.Libraries;
import fr.byowares.game.miq.core.model.song.Library;
import fr.byowares.game.miq.jfx.fxml.wizard.WizardItem;
import fr.byowares.game.miq.jfx.fxml.wizard.WizardLibrary;
import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.source.Source;
import javafx.stage.Stage;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * An {@link fr.byowares.game.miq.core.model.song.Libraries} {@link fr.byowares.game.miq.jfx.editor.tree.MIQItem} wrapper.
 *
 * @since XXX
 */
public class ItemLibraries
        extends ItemNamed<Libraries> {

    /**
     * @param libraries The {@link fr.byowares.game.miq.core.model.song.Libraries} to wrap.
     */
    public ItemLibraries(final Libraries libraries) {
        super(libraries);
    }

    /**
     * @return The {@link org.kordamp.ikonli.javafx.FontIcon} that describe this object.
     */
    public static FontIcon getFontIcon() {
        return new FontIcon(BootstrapIcons.HDD);
    }

    @Override
    Serializer<Libraries> getSerializer() {
        return null;
    }

    @Override
    public boolean canBeEdited() {
        return false;
    }

    @Override
    public MIQItem wizardChild(
            final Stage stage,
            final CachedData cachedData,
            final Source parentSource
    ) {
        /*
         * parentSource can only have one origin: Librairies.getSource().
         * The parentSource can be used as is (the given directory is where the Library must be added).
         */
        final Source parentDirectory = this.getSource();
        final Library library = WizardItem.open(new WizardLibrary(parentDirectory), stage);
        if (library == null) return null;

        this.getNamedSourcedObject().getLibraries().add(library);
        return new ItemLibrary(library);
    }

    @Override
    public FontIcon newFontIcon() {
        return getFontIcon();
    }

    @Override
    public boolean hasDefaultChild() {
        return true;
    }
}
