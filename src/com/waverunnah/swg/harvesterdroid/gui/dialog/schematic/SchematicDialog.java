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

package com.waverunnah.swg.harvesterdroid.gui.dialog.schematic;

import com.waverunnah.swg.harvesterdroid.data.schematics.Schematic;
import com.waverunnah.swg.harvesterdroid.gui.dialog.BaseDialog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class SchematicDialog extends BaseDialog<Schematic> {
    private static ButtonType SAVE = new ButtonType("Save", ButtonBar.ButtonData.APPLY);
    private static SchematicDialogController controller;

    public SchematicDialog() {
        this(new Schematic());
    }

    public SchematicDialog(Schematic schematic) {
        super("Schematic Editor");
        if (controller != null)
            controller.readSchematic(schematic);
    }

    public static void setController(SchematicDialogController controller) {
        SchematicDialog.controller = controller;
    }

    @Override
    protected ButtonType[] getButtonTypes() {
        return new ButtonType[]{
                SAVE,
                ButtonType.CANCEL
        };
    }

    @Override
    protected void createDialog() {
        Button saveButton = (Button) getDialogPane().lookupButton(SAVE);
        saveButton.setDefaultButton(true);

        setResultConverter(buttonType -> {
            if (buttonType != SAVE)
                return null;
            return controller.getSchematic();
        });
    }
}