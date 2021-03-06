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

package com.lewisjmorgan.harvesterdroid.trackers.galaxyharvester.xml;

import com.lewisjmorgan.harvesterdroid.api.GalaxyResource;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Waverunner on 4/4/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "galaxy_resources")
public class GalaxyResourcesXml {

  @XmlElement(name = "galaxy_resource")
  private List<GalaxyResource> galaxyResources;

  public List<GalaxyResource> getGalaxyResources() {
    return galaxyResources;
  }

  public void setGalaxyResources(List<GalaxyResource> galaxyResources) {
    this.galaxyResources = galaxyResources;
  }
}
