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
import fr.byowares.game.miq.core.serial.album.AlbumSerializer;
import fr.byowares.game.utils.serial.Serializer;
import fr.byowares.game.utils.serial.source.Source;
import javafx.stage.Stage;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * An {@link fr.byowares.game.miq.core.model.song.Album} {@link fr.byowares.game.miq.jfx.editor.tree.MIQItem} wrapper.
 *
 * @since XXX
 */
public class ItemAlbum
        extends ItemNamed<Album> {

    /**
     * @param album The {@link fr.byowares.game.miq.core.model.song.Album} to wrap.
     */
    public ItemAlbum(final Album album) {
        super(album);
    }

    /**
     * @return The {@link org.kordamp.ikonli.javafx.FontIcon} that describe this object.
     */
    public static FontIcon getFontIcon() {
        return new FontIcon(BootstrapIcons.DISC);
    }

    @Override
    Serializer<Album> getSerializer() {
        return AlbumSerializer.INSTANCE;
    }

    @Override
    MIQItem wizardChild(
            final Stage stage,
            final Source parentSource
    ) {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO
    }

    @Override
    public FontIcon newFontIcon() {
        return getFontIcon();
    }

    @Override
    public boolean hasDefaultChild() {
        return false;
    }
}
