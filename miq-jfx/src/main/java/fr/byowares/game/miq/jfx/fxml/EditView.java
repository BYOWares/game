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
package fr.byowares.game.miq.jfx.fxml;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.model.song.Album;
import fr.byowares.game.miq.core.model.song.Libraries;
import fr.byowares.game.miq.core.model.song.Library;
import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.miq.jfx.editor.tree.CachedData;
import fr.byowares.game.miq.jfx.editor.tree.ItemAlbum;
import fr.byowares.game.miq.jfx.editor.tree.ItemLibraries;
import fr.byowares.game.miq.jfx.editor.tree.ItemLibrary;
import fr.byowares.game.miq.jfx.editor.tree.ItemLyrics;
import fr.byowares.game.miq.jfx.editor.tree.ItemSong;
import fr.byowares.game.miq.jfx.editor.tree.MIQItem;
import fr.byowares.game.miq.jfx.editor.tree.MIQItemCell;
import fr.byowares.game.miq.jfx.i18n.I18NMIQ;
import fr.byowares.game.utils.jfx.FontIconSizeEnforcer;
import fr.byowares.game.utils.jfx.SceneUniqueActor;
import fr.byowares.game.utils.jfx.fxml.FXMLLoader;
import fr.byowares.game.utils.serial.source.Source;
import fr.byowares.game.utils.serial.source.SourceInMemory;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The {@link fr.byowares.game.utils.jfx.SceneUniqueActor} controlling the global edit page.
 *
 * @since XXX
 */
public class EditView
        extends SceneUniqueActor {

    private static final double TREE_HEIGHT = // Needed to make the TreeView take the left space in BorderPane.
            Screen.getScreens().stream().map(s -> s.getBounds().getHeight()).max(Double::compareTo).orElse(1080.0);

    private final CachedData cache = new CachedData();

    @FXML private Button bAdd;
    @FXML private Button bBack;
    @FXML private Button bCollapseAll;
    @FXML private Button bDelete;
    @FXML private Button bEdit;
    @FXML private Button bExpandAll;
    @FXML private Button bSelectCurrent;
    @FXML private TabPane tabs;
    @FXML private Text title;
    @FXML private TreeView<MIQItem> tree;

    /**
     * @param previousActor The previous {@link fr.byowares.game.utils.jfx.SceneUniqueActor} (from which this one is
     *                      invoked).
     */
    public EditView(final SceneUniqueActor previousActor) {
        super(previousActor);
    }

    private static TreeItem<MIQItem> addLibrary(
            final TreeItem<MIQItem> parent,
            final Library library
    ) {
        return addItem(parent, new ItemLibrary(library));
    }

    private static TreeItem<MIQItem> addAlbum(
            final TreeItem<MIQItem> parent,
            final Album album
    ) {
        return addItem(parent, new ItemAlbum(album));
    }

    private static TreeItem<MIQItem> addSong(
            final TreeItem<MIQItem> parent,
            final Song song
    ) {
        return addItem(parent, new ItemSong(song));
    }

    private static void addLyrics(
            final TreeItem<MIQItem> parent,
            final Lyrics lyrics
    ) {
        addItem(parent, new ItemLyrics(lyrics));
    }

    private static TreeItem<MIQItem> addItem(
            final TreeItem<MIQItem> parent,
            final MIQItem childMiqItem
    ) {
        final TreeItem<MIQItem> childTreeItem = childMiqItem.toTreeItem();
        final var children = parent.getChildren();
        if (children.isEmpty()) children.add(childTreeItem);
        else {
            int index = parent.getValue().hasDefaultChild() ? 1 : 0;
            for (; index < children.size(); index++) {
                if (childMiqItem.getName().compareTo(children.get(index).getValue().getName()) < 0) break;
            }
            children.add(index, childTreeItem);
        }
        return childTreeItem;
    }

    /**
     * @param editView Load the {@link fr.byowares.game.utils.jfx.SceneUniqueActor} in charge of editing the libraries.
     *
     * @return {@code editView}
     */
    public static EditView load(final EditView editView) {
        return FXMLLoader.loadActor(editView, EditView.class, "EditView");
    }

    private static void expand(final TreeItem<MIQItem> item) {
        item.setExpanded(true);
        item.getChildren().forEach(EditView::expand);
    }

    private static void collapse(
            final Set<TreeItem<MIQItem>> childrenOfSelected,
            final TreeItem<MIQItem> item
    ) {
        childrenOfSelected.add(item);
        item.setExpanded(false);
        item.getChildren().forEach(child -> collapse(childrenOfSelected, child));
    }

    private static void setButtonIcon(
            final Button button,
            final Ikon icon,
            final String i18nKey,
            final String... cssClasses
    ) {
        button.getStyleClass().add(Styles.BUTTON_CIRCLE);
        button.setText(null);
        button.setGraphic(new FontIcon(icon));
        button.setTooltip(new Tooltip());
        I18NMIQ.get().bind(button.getTooltip().textProperty(), i18nKey);
        button.getStyleClass().addAll(cssClasses);
    }

    /**
     * @param item The tree item from which the source must be searched.
     *
     * @return The first {@link fr.byowares.game.utils.serial.source.Source} in its ancestry (including itself) which is
     * not {@link fr.byowares.game.utils.serial.source.SourceInMemory}.
     */
    private static Source getParentSource(final TreeItem<MIQItem> item) {
        if (item == null) return null;
        final Source source = item.getValue().getSource();
        if (source == SourceInMemory.INSTANCE) return getParentSource(item.getParent());
        return source;
    }

    private TreeItem<MIQItem> convert(final Libraries libraries) {
        final Library uLib = libraries.getUndefined();
        final var itemLibs = new ItemLibraries(libraries).toTreeItem();
        final var itemULib = addLibrary(itemLibs, uLib);

        for (final Library library : libraries.getLibraries()) {
            final Album uAlb = library.getUndefined();
            final var itemLib = (library == uLib) ? itemULib : addLibrary(itemLibs, library);
            final var itemUAlb = addAlbum(itemLib, uAlb);

            for (final Album album : library.getAlbums()) {
                this.cache.cacheAlbum(null, album);
                final var itemAlb = (album == uAlb) ? itemUAlb : addAlbum(itemLib, album);

                for (final Song song : album.getSongs()) {
                    this.cache.cacheSong(null, song);
                    final var itemSong = addSong(itemAlb, song);
                    for (final Lyrics lyrics : song.getLyrics()) {
                        addLyrics(itemSong, lyrics);
                    }
                }
            }
        }
        return itemLibs;
    }

    private MultipleSelectionModel<TreeItem<MIQItem>> getTreeSelectionModel() {
        return this.tree.getSelectionModel();
    }

    private ObservableList<TreeItem<MIQItem>> getTreeSelectedItems() {
        return this.getTreeSelectionModel().getSelectedItems();
    }

    /** FXML handle for initialization. */
    @FXML
    public void initialize() {
        // TODO make it in another thread
        final Libraries libraries = Libraries.from(Path.of(System.getProperty("user.home"), "Desktop", "MIQ"));
        this.tree.setRoot(this.convert(libraries));
        this.tree.setPrefHeight(TREE_HEIGHT);
        this.tree.getStyleClass().add(Tweaks.ALT_ICON);
        final var treeSelectionModel = this.getTreeSelectionModel();
        treeSelectionModel.getSelectedItems().addListener(new TreeSelectionListener());
        treeSelectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        treeSelectionModel.select(this.tree.getRoot());
        this.tree.setCellFactory(p -> new MIQItemCell());

        setButtonIcon(this.bEdit, BootstrapIcons.PENCIL_SQUARE, "edit_view.edit", Styles.ACCENT);
        setButtonIcon(this.bAdd, BootstrapIcons.FILE_EARMARK_PLUS, "edit_view.add", Styles.SUCCESS);
        setButtonIcon(this.bDelete, BootstrapIcons.FILE_EARMARK_X, "edit_view.delete", Styles.DANGER);
        setButtonIcon(this.bCollapseAll, BootstrapIcons.CHEVRON_CONTRACT, "edit_view.collapse");
        setButtonIcon(this.bExpandAll, BootstrapIcons.CHEVRON_EXPAND, "edit_view.expand");
        setButtonIcon(this.bSelectCurrent, Feather.CROSSHAIR, "edit_view.select");
        setButtonIcon(this.bBack, BootstrapIcons.ARROW_LEFT_CIRCLE_FILL, "edit_view.back");
        final var css = FontIconSizeEnforcer.enforceIconSizeCSS(this.bBack, "force-size", 32);
        this.bBack.getGraphic().getStyleClass().add(css);
    }

    /** Back to previous menu. */
    @FXML
    void onBack() {
        this.giveBackControlOfScene();
    }

    /** Open editor for selected items. */
    @FXML
    void onEdit(final ActionEvent event) {
        // TODO
    }

    /** Create a child item of the selected one. */
    @FXML
    void onAdd() {
        final var items = this.getTreeSelectedItems();
        if (items.size() != 1) {
            throw new IllegalStateException("More than one element selected: " + items);
        }
        final var treeItem = items.getFirst();
        final MIQItem miqItem = treeItem.getValue();
        if (!miqItem.canHaveChildren()) {
            throw new IllegalStateException("Current element cannot have children: " + miqItem);
        }

        final Source parentSource = Objects.requireNonNull(getParentSource(treeItem));
        final MIQItem child = miqItem.createChild(this.getStage(), this.cache, parentSource);
        if (child == null) return;
        addItem(treeItem, child);
    }

    /** Delete selected items. */
    @FXML
    void onDelete(final ActionEvent event) {
        // TODO
    }

    /** Select the currently opened item. */
    @FXML
    void onSelectCurrent(final ActionEvent event) {
        // TODO
    }

    /** Expand all children nodes of selected ones. */
    @FXML
    void onExpandAll() {
        final ObservableList<TreeItem<MIQItem>> selectedItems = this.getTreeSelectedItems();
        if (selectedItems.isEmpty()) return;
        selectedItems.forEach(EditView::expand);
    }

    /** Collapse all nodes up to the selected ones. Hidden items are removed from selected items. */
    @FXML
    void onCollapseAll() {
        final ObservableList<TreeItem<MIQItem>> selectedItems = this.getTreeSelectedItems();
        if (selectedItems.isEmpty()) return;

        final List<TreeItem<MIQItem>> selected = new ArrayList<>(selectedItems);
        // List of sub nodes visited while collapsing selected ones (shall not be selected after the action).
        final Set<TreeItem<MIQItem>> childrenOfSelected = new HashSet<>();
        for (final TreeItem<MIQItem> selectedItem : selected) {
            if (childrenOfSelected.contains(selectedItem)) continue;
            selectedItem.setExpanded(false);
            selectedItem.getChildren().forEach(item -> collapse(childrenOfSelected, item));
        }
        selected.removeAll(childrenOfSelected);
        selected.forEach(this.getTreeSelectionModel()::select);
    }

    @Override
    public void onDisplay() {
        // Nothing to do
    }

    @Override
    public void onHide() {
        // Nothing to do
    }

    /**
     * Update the enable/disable buttons related tot the tree.
     */
    private class TreeSelectionListener
            implements ListChangeListener<TreeItem<MIQItem>> {

        @Override
        public void onChanged(final Change<? extends TreeItem<MIQItem>> c) {
            final var items = EditView.this.getTreeSelectedItems();
            final boolean canEdit = items.stream().anyMatch(item -> item.getValue().canBeEdited());
            final boolean canDelete = items.stream().anyMatch(item -> item.getValue().canBeDeleted());
            EditView.this.bAdd.setDisable(!(items.size() == 1 && items.getFirst().getValue().canHaveChildren()));
            EditView.this.bDelete.setDisable(!canDelete);
            EditView.this.bEdit.setDisable(!canEdit);
        }
    }
}
