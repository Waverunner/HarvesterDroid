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

package com.lewisjmorgan.harvesterdroid.app.ui.schematics;

import javafx.scene.control.TreeCell;

/**
 * Created by Waverunner on 3/23/2017.
 */
public class SchematicsTreeCellFactory extends TreeCell<SchematicsTreeItem> {

  // TODO Context menus
  @Override
  protected void updateItem(SchematicsTreeItem item, boolean empty) {
    super.updateItem(item, empty);

    if (item != null) {
      setText(item.getName());
    } else {
      setGraphic(null);
      setText(null);
    }
  }


}
