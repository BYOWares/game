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

import fr.byowares.game.miq.core.model.lyrics.Lyrics;
import fr.byowares.game.miq.core.model.song.Album;
import fr.byowares.game.miq.core.model.song.Libraries;
import fr.byowares.game.miq.core.model.song.Library;
import fr.byowares.game.miq.core.model.song.Song;
import fr.byowares.game.miq.jfx.editor.tree.ItemAlbum;
import fr.byowares.game.miq.jfx.editor.tree.ItemLibraries;
import fr.byowares.game.miq.jfx.editor.tree.ItemLibrary;
import fr.byowares.game.miq.jfx.editor.tree.ItemLyrics;
import fr.byowares.game.miq.jfx.editor.tree.ItemSong;
import fr.byowares.game.miq.jfx.editor.tree.MIQItem;
import fr.byowares.game.utils.jfx.SceneUniqueActor;
import fr.byowares.game.utils.jfx.fxml.FXMLLoader;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EditView
        extends SceneUniqueActor {

    private static final double TREE_HEIGHT = // Needed to make the TreeView take the left space in BorderPane.
            Screen.getScreens().stream().map(s -> s.getBounds().getHeight()).max(Double::compareTo).orElse(1080.0);

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

    public EditView(final SceneUniqueActor previousActor) {
        super(previousActor);
    }

    private static TreeItem<MIQItem> convert(final Libraries libraries) {
        final TreeItem<MIQItem> itemLibraries = new TreeItem<>(new ItemLibraries(libraries));
        for (final Library library : libraries.getLibraries()) {
            final TreeItem<MIQItem> itemLibrary = new TreeItem<>(new ItemLibrary(library));
            itemLibraries.getChildren().add(itemLibrary);

            for (final Album album : library.getAlbums()) {
                final TreeItem<MIQItem> itemAlbum = new TreeItem<>(new ItemAlbum(album));
                itemLibrary.getChildren().add(itemAlbum);

                for (final Song song : album.getSongs()) {
                    final TreeItem<MIQItem> itemSong = new TreeItem<>(new ItemSong(song));
                    itemAlbum.getChildren().add(itemSong);

                    for (final Lyrics lyrics : song.getLyrics()) {
                        final TreeItem<MIQItem> itemLyrics = new TreeItem<>(new ItemLyrics(lyrics));
                        itemSong.getChildren().add(itemLyrics);
                    }
                }
            }
        }
        return itemLibraries;
    }

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
        this.tree.setRoot(convert(libraries));
        this.tree.setPrefHeight(TREE_HEIGHT);
        final var treeSelectionModel = this.getTreeSelectionModel();
        treeSelectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        treeSelectionModel.select(this.tree.getRoot());
        treeSelectionModel.getSelectedItems().addListener(new TreeSelectionListener());
    }

    @FXML
    void onBack(final ActionEvent event) {
        this.giveBackControlOfScene();
    }

    @FXML
    void onEdit(final ActionEvent event) {

    }

    @FXML
    void onAdd(final ActionEvent event) {

    }

    @FXML
    void onDelete(final ActionEvent event) {

    }

    @FXML
    void onSelectCurrent(final ActionEvent event) {

    }

    @FXML
    void onExpandAll(final ActionEvent event) {
        final ObservableList<TreeItem<MIQItem>> selectedItems = this.getTreeSelectedItems();
        if (selectedItems.isEmpty()) return;

        selectedItems.forEach(EditView::expand);
    }

    @FXML
    void onCollapseAll(final ActionEvent event) {
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

    }

    @Override
    public void onHide() {

    }

    private class TreeSelectionListener
            implements ListChangeListener<TreeItem<MIQItem>> {

        @Override
        public void onChanged(final Change<? extends TreeItem<MIQItem>> c) {
            final var selectedItems = EditView.this.getTreeSelectedItems();
            EditView.this.bAdd.setDisable(selectedItems.size() != 1);
            EditView.this.bDelete.setDisable(selectedItems.isEmpty());
        }
    }
}
