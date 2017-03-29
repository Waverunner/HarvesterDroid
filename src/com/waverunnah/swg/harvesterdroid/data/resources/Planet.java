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

package com.waverunnah.swg.harvesterdroid.data.resources;

public enum Planet {
	CORELLIA,
	DANTOOINE,
	DATHOMIR,
	ENDOR,
	KASHYYYK,
	LOK,
	NABOO,
	RORI,
	TALUS,
	TATOOINE,
	YAVIN_IV,
	UNKNOWN;


	public static Planet toPlanet(String planet) {
		switch (planet) {
			case "Corellia":
				return CORELLIA;
			case "Dantooine":
				return DANTOOINE;
			case "Dathomir":
				return DATHOMIR;
			case "Endor":
				return ENDOR;
			case "Kashyyyk":
				return KASHYYYK;
			case "Lok":
				return LOK;
			case "Naboo":
				return NABOO;
			case "Rori":
				return RORI;
			case "Talus":
				return TALUS;
			case "Tatooine":
				return TATOOINE;
			case "Yavin IV":
				return YAVIN_IV;
			default:
				return UNKNOWN;
		}
	}

	public static String toString(Planet planet) {
		switch (planet) {
			case CORELLIA:
				return "corellia";
			case DANTOOINE:
				return "dantooine";
			case DATHOMIR:
				return "dathomir";
			case ENDOR:
				return "endor";
			case KASHYYYK:
				return "kashyyyk";
			case LOK:
				return "lok";
			case NABOO:
				return "naboo";
			case RORI:
				return "rori";
			case TALUS:
				return "talus";
			case TATOOINE:
				return "tatooine";
			case YAVIN_IV:
				return "yavin_iv";
			default:
				return "unknown";
		}
	}
}
