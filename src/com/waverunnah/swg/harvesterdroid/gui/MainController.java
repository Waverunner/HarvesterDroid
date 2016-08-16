package com.waverunnah.swg.harvesterdroid.gui;

import com.waverunnah.swg.harvesterdroid.Launcher;
import com.waverunnah.swg.harvesterdroid.app.HarvesterDroid;
import com.waverunnah.swg.harvesterdroid.data.resources.GalaxyResource;
import com.waverunnah.swg.harvesterdroid.data.schematics.Schematic;
import com.waverunnah.swg.harvesterdroid.gui.callbacks.GalaxyResourceListCell;
import com.waverunnah.swg.harvesterdroid.gui.dialog.ExceptionDialog;
import com.waverunnah.swg.harvesterdroid.gui.dialog.ResourceDialog;
import com.waverunnah.swg.harvesterdroid.gui.dialog.SchematicDialog;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import org.controlsfx.control.StatusBar;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
	// TODO Split each panel into it's own component (inventory, schematics, best resources)

	private HarvesterDroid app;

	@FXML
	TitledPane bestResourcesPane;
	@FXML
	ListView<Schematic> schematicsListView;
	@FXML
	ListView<GalaxyResource> inventoryListView;
	@FXML
	ListView<GalaxyResource> bestResourcesListView;
	@FXML
	ComboBox<String> groupComboBox;
	@FXML
	StatusBar statusBar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		app = Launcher.getApp();
		initResources();
		initInventory();
		initSchematics();
		initGroups();
	}

	private void initInventory() {
		inventoryListView.setCellFactory(param -> new GalaxyResourceListCell());

		inventoryListView.disableProperty().bind(app.inventoryProperty().emptyProperty());
		inventoryListView.setItems(app.getFilteredInventory());
	}

	private void initResources() {
		bestResourcesPane.setText("Best Resources as of " + Launcher.getLastUpdate());
		bestResourcesListView.disableProperty().bind(app.resourcesProperty().emptyProperty());
		bestResourcesListView.setCellFactory(param -> new GalaxyResourceListCell());
		bestResourcesListView.setItems(app.getFilteredResources());
	}

	private void initGroups() {
		groupComboBox.itemsProperty().bind(app.groupsProperty());
		app.activeGroupProperty().bind(groupComboBox.getSelectionModel().selectedItemProperty());

		// Select a group if it's added to the list, change selected group if existing was deleted
		groupComboBox.getItems().addListener((ListChangeListener<? super String>) c -> {
			while (c.next()) {
				String selected = groupComboBox.getSelectionModel().getSelectedItem();
				if (c.wasAdded() && selected == null && groupComboBox.getItems().size() > 0) {
					// Nothing was selected previously, time to select it!
					groupComboBox.getSelectionModel().select(0);
				} else if (c.wasRemoved()) {
					boolean oldValueSelected = selected != null && c.getRemoved().contains(selected);
					if (oldValueSelected && groupComboBox.getItems().size() > 0)
						groupComboBox.getSelectionModel().selectFirst();
				}
			}
		});
	}

	private void initSchematics() {
		schematicsListView.disableProperty().bind(app.schematicsProperty().emptyProperty());
		app.selectedSchematicProperty().bind(schematicsListView.getSelectionModel().selectedItemProperty());

		// Clear the selected item and jump to the next available item to select
		schematicsListView.getItems().addListener((ListChangeListener<? super Schematic>) c -> {
			while (c.next()) {
				if (c.wasRemoved()) {
					List<? extends Schematic> removed = c.getRemoved();
					int size = schematicsListView.getItems().size();
					int selected = schematicsListView.getSelectionModel().getSelectedIndex();
					if (size != 0 && selected > size) {
						schematicsListView.getSelectionModel().select(selected - removed.size());
					} else {
						// Nothing to select
						schematicsListView.getSelectionModel().clearSelection();
					}
				}
			}
		});
		schematicsListView.setItems(app.getFilteredSchematics());
	}

	public void editSelectedSchematic() {
		if (schematicsListView.getSelectionModel().getSelectedItem() == null)
			displaySchematicDialog();
		else
			displaySchematicDialog(schematicsListView.getSelectionModel().getSelectedItem());
	}

	public void displaySchematicDialog() {
		SchematicDialog dialog = new SchematicDialog();
		dialog.setTitle("Create Schematic");
		Optional<Schematic> result = dialog.showAndWait();
		if (!result.isPresent())
			return;

		Schematic schematic = result.get();
		if (schematic != null && !schematic.isIncomplete())
			app.getSchematics().add(schematic);
	}

	public void displaySchematicDialog(Schematic schematic) {
		SchematicDialog dialog = new SchematicDialog(schematic);
		dialog.setTitle("Edit Schematic");
		Optional<Schematic> result = dialog.showAndWait();
		if (!result.isPresent())
			return;

		Schematic changed = result.get();
		if (changed == schematic && schematic != null && !schematic.isIncomplete()) {
			app.getSchematics().add(schematic);
		} else {
			app.getSchematics().remove(schematic);
			app.getSchematics().add(changed);
		}
	}

	public void addInventoryResource() {
		GalaxyResource selectedItem = bestResourcesListView.getSelectionModel().getSelectedItem();
		if (selectedItem != null && !app.getInventory().contains(selectedItem)) {
			app.getInventory().add(selectedItem);
		} else {
			ResourceDialog dialog = new ResourceDialog();
			dialog.setTitle("New Inventory Resource");
			Optional<GalaxyResource> result = dialog.showAndWait();
			if (!result.isPresent())
				return;

			GalaxyResource galaxyResource = result.get();
			if (galaxyResource != null && !galaxyResource.getName().isEmpty() && !galaxyResource.getResourceType().isEmpty())
				app.getInventory().add(galaxyResource);
		}
	}

	public void removeInventoryResource() {
		GalaxyResource selectedItem = inventoryListView.getSelectionModel().getSelectedItem();
		if (selectedItem == null)
			return;

		app.removeInventoryResource(selectedItem);
	}

	public void save() {
		try {
			app.save();
		} catch (IOException | TransformerException e) {
			new ExceptionDialog(e).show();
		}
	}
}
