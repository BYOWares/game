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

import fr.byowares.game.miq.core.model.song.Album;
import fr.byowares.game.miq.core.model.song.Library;
import fr.byowares.game.miq.core.serial.library.LibrarySerializer;
import fr.byowares.game.miq.jfx.fxml.wizard.WizardAlbum;
import fr.byowares.game.miq.jfx.fxml.wizard.WizardItem;
import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.source.Source;
import javafx.stage.Stage;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * An {@link fr.byowares.game.miq.core.model.song.Library} {@link fr.byowares.game.miq.jfx.editor.tree.MIQItem} wrapper.
 *
 * @since XXX
 */
public class ItemLibrary
        extends ItemNamed<Library> {

    /**
     * @param library The {@link fr.byowares.game.miq.core.model.song.Library} to wrap.
     */
    public ItemLibrary(final Library library) {
        super(library);
    }

    @Override
    Serializer<Library> getSerializer() {
        return LibrarySerializer.INSTANCE;
    }

    @Override
    public MIQItem wizardChild(
            final Stage stage,
            final Source parentSource
    ) {
        /*
         * parentSource can have two origins:
         *  - Librairies.getSource() (this.getSource() == IN_MEMORY), when we want to add an Album to its __UNDEFINED__
         * library. In this scenario, the parentSource can be used as is (the given directory is where the Album must
         *  be added).
         *  - Library.getSource() (== this.getSource()) when we want to add an Album to an existing Library. In this
         * case, we must use the parent source of the provided one (which is the miq.yml file containing the data).
         */
        final Source parentDirectory = this.getSource() == parentSource ? parentSource.getParent() : parentSource;
        final Album album = WizardItem.open(new WizardAlbum(parentDirectory), stage);
        return album == null ? null : new ItemAlbum(album);
    }

    @Override
    public FontIcon newFontIcon() {
        return new FontIcon(BootstrapIcons.MUSIC_PLAYER);
    }

    @Override
    public boolean hasDefaultChild() {
        return true;
    }
}
