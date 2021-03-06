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

package com.lewisjmorgan.harvesterdroid.app.ui.items;

import com.lewisjmorgan.harvesterdroid.api.GalaxyResource;
import de.saxsys.mvvmfx.ViewModel;
import java.io.InputStream;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Waverunner on 4/3/2017.
 */
public class GalaxyResourceItemViewModel implements ViewModel {

  private static final Logger logger = LogManager.getLogger(GalaxyResourceItemViewModel.class);

  private ReadOnlyStringWrapper name = new ReadOnlyStringWrapper();
  private ReadOnlyStringWrapper type = new ReadOnlyStringWrapper();
  private ReadOnlyStringWrapper planets = new ReadOnlyStringWrapper();
  private ReadOnlyStringWrapper planetsTooltip = new ReadOnlyStringWrapper();
  private ReadOnlyObjectWrapper<Image> image = new ReadOnlyObjectWrapper<>();
  private ReadOnlyMapWrapper<String, Integer> attributes = new ReadOnlyMapWrapper<>();
  private GalaxyResource galaxyResource;

  public GalaxyResourceItemViewModel(GalaxyResource galaxyResource) {
    this.galaxyResource = galaxyResource;
    name.set(galaxyResource.getName());
    type.set(galaxyResource.getResourceType().getName());
    if (galaxyResource.getAttributes() == null) {
      logger.warn("Null attributes for {}", galaxyResource.getName());
    }
    attributes.set(FXCollections.observableMap(galaxyResource.getAttributes()));
    image.set(createImage(galaxyResource.getResourceType().getId()));
    if (galaxyResource.getDespawnDate() == null) {
      planets.set("  Spawns on " + galaxyResource.getPlanets().size() + (galaxyResource.getPlanets().size() == 1 ? " planet" : " planets"));
      StringBuilder planetStrings = new StringBuilder();
      for (String s : galaxyResource.getPlanets()) {
        planetStrings.append("\n").append(s);
      }

      planetsTooltip.set("Available on:" + planetStrings.toString());
    }
  }

  private Image createImage(String container) {
    if (container == null) {
      return null;
    }
    InputStream is = getClass().getResourceAsStream("/images/resources/" + container + ".png");
    if (is == null) {
      container = container.split("_")[0];
      is = getClass().getResourceAsStream("/images/resources/" + container + ".png");
      if (is == null) {
        logger.warn("Could not find image /images/resources/" + container + ".png");
        return null;
      }
    }
    return new Image(is);
  }

  public Image getImage() {
    return image.get();
  }

  public ReadOnlyObjectProperty<Image> imageProperty() {
    return image;
  }

  public String getName() {
    return name.get();
  }

  public ReadOnlyStringProperty nameProperty() {
    return name;
  }

  public String getType() {
    return type.get();
  }

  public ReadOnlyStringProperty typeProperty() {
    return type;
  }

  public ObservableMap<String, Integer> getAttributes() {
    return attributes.get();
  }

  public ReadOnlyMapProperty<String, Integer> attributesProperty() {
    return attributes;
  }

  public String getPlanets() {
    return planets.get();
  }

  public ReadOnlyStringProperty planetsProperty() {
    return planets.getReadOnlyProperty();
  }

  public String getPlanetsTooltip() {
    return planetsTooltip.get();
  }

  public ReadOnlyStringProperty planetsTooltipProperty() {
    return planetsTooltip.getReadOnlyProperty();
  }

  public GalaxyResource getGalaxyResource() {
    return galaxyResource;
  }
}