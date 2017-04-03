/*
 * HarvesterDroid - A Resource Tracker for Star Wars Galaxies
 * Copyright (C) 2017  Waverunner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.waverunnah.swg.harvesterdroid.gui.dialog.preferences;

import com.waverunnah.swg.harvesterdroid.DroidProperties;
import com.waverunnah.swg.harvesterdroid.gui.dialog.BaseDialog;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by Waverunner on 3/29/2017
 */
public class PreferencesDialog extends BaseDialog<Properties> implements Initializable {
    //region FXML
    @FXML
    private ChoiceBox<String> trackerComboBox;
    @FXML
    private ChoiceBox<String> galaxyChoiceBox;
    @FXML
    private TextField downloadBufferTextField;
    @FXML
    private CheckBox autosaveCheckBox;
    @FXML
    private CheckBox saveNagCheckBox;
    //endregion

    private ObjectProperty<Properties> properties;
    private MapProperty<String, String> galaxies;

    public PreferencesDialog() {
        super("HarvesterDroid Preferences");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        properties = new SimpleObjectProperty<>();
        galaxies = new SimpleMapProperty<>();
        galaxies.addListener(((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty())
                return;

            galaxyChoiceBox.getItems().clear();
            newValue.forEach((key, value) -> galaxyChoiceBox.getItems().add(value));
            galaxyChoiceBox.getSelectionModel().select(0);
        }));
        trackerComboBox.setItems(FXCollections.observableArrayList("GalaxyHarvester"));
        trackerComboBox.getSelectionModel().select(0);
        trackerComboBox.setDisable(true);

        createListeners();
    }

    private void createListeners() {
        properties.addListener((observable, oldValue, newValue) -> {
            selectGalaxy(newValue.getProperty(DroidProperties.GALAXY));
            downloadBufferTextField.setText(newValue.getProperty(DroidProperties.DOWNLOAD_BUFFER));
            autosaveCheckBox.selectedProperty().set(Boolean.parseBoolean(newValue.getProperty(DroidProperties.AUTOSAVE)));
            saveNagCheckBox.selectedProperty().set(Boolean.parseBoolean(newValue.getProperty(DroidProperties.SAVE_NAG)));
        });

        galaxyChoiceBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (properties.get() == null)
                return;
            String key = getGalaxyKey(newValue);
            if (key != null)
                properties.get().setProperty(DroidProperties.GALAXY, key);
        }));
        downloadBufferTextField.textProperty().addListener((observable, oldValue, newValue) -> properties.get().setProperty(DroidProperties.DOWNLOAD_BUFFER, String.valueOf(newValue)));
        autosaveCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> properties.get().setProperty(DroidProperties.AUTOSAVE, String.valueOf(newValue)));
        saveNagCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> properties.get().setProperty(DroidProperties.SAVE_NAG, String.valueOf(newValue)));
    }

    @Override
    protected ButtonType[] getButtonTypes() {
        return new ButtonType[]{
                ButtonType.APPLY,
                ButtonType.CLOSE
        };
    }

    @Override
    protected void createDialog() {
        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.APPLY)
                return null;

            return properties.get();
        });
    }

    private String getGalaxyKey(String value) {
        for (Map.Entry<String, String> entry : galaxies.entrySet()) {
            if (entry.getValue().equals(value))
                return entry.getKey();
        }
        return null;
    }

    private void selectGalaxy(String key) {
        galaxyChoiceBox.getSelectionModel().select(galaxies.get(key));
    }

    public void setGalaxies(ObservableMap<String, String> galaxies) {
        this.galaxies.set(galaxies);
        galaxies.addListener((MapChangeListener<? super String, ? super String>) change -> {
            if (change.wasAdded())
                galaxyChoiceBox.getItems().add(change.getValueAdded());
            else if (change.wasRemoved())
                galaxyChoiceBox.getItems().remove(change.getValueRemoved());
        });
    }

    public void setProperties(Properties properties) {
        this.properties.set(properties);
    }

    @Override
    protected boolean isController() {
        return true;
    }
}
