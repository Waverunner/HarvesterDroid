package com.waverunnah.swg.harvesterdroid.gui;

import com.waverunnah.swg.harvesterdroid.HarvesterDroid;
import com.waverunnah.swg.harvesterdroid.data.resources.GalaxyResource;
import com.waverunnah.swg.harvesterdroid.gui.converters.ResourceValueConverter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class ResourceListItem extends HBox {

	//region FXML Components
	@FXML
	ImageView resourceImage;
	@FXML
	private Label resourceName;
	@FXML
	private Label resourceType;
	@FXML
	private HBox resourceStatsBox;
	//endregion

	private SimpleObjectProperty<GalaxyResource> galaxyResource = new SimpleObjectProperty<>();

	public ResourceListItem() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("resource_list_item.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		galaxyResource.addListener((observable, old, val) -> {
			handleGalaxyResourceSet(val);
		});
	}

	public void handleGalaxyResourceSet(GalaxyResource val) {
		// TODO Update image using group id
		if (val == null) {
			resourceName.textProperty().unbind();
			resourceType.textProperty().unbind();
			resourceImage.imageProperty().unbind();
			resourceStatsBox.getChildren().clear();
			return;
		}

		resourceName.textProperty().bindBidirectional(val.nameProperty());
		resourceType.textProperty().bindBidirectional(val.resourceTypeProperty());

		resourceImage.imageProperty().set(getImage(val.containerProperty().get()));
		Bindings.bindBidirectional(val.containerProperty(), resourceImage.imageProperty(), new StringConverter<Image>() {
			@Override
			public String toString(Image object) {
				// proper way to get a URL from an Image (method that exists is deprecated API)?
				return null;
			}

			@Override
			public Image fromString(String string) {
				return getImage(string);
			}
		});

		// Ensures no duplicates are made
		resourceStatsBox.getChildren().clear();


		val.getAttributes().forEach((modifier, value) -> {
			// TODO Create a 2-way mapping between the name and abbreviated name
			switch(modifier) {
				case "entangle_resistance":
					createAttributeUI("ER", value);
					break;
				case "cold_resistance":
					createAttributeUI("CR", value);
					break;
				case "conductivity":
					createAttributeUI("CD", value);
					break;
				case "decay_resistance":
					createAttributeUI("DR", value);
					break;
				case "flavor":
					createAttributeUI("FL", value);
					break;
				case "heat_resistance":
					createAttributeUI("HR", value);
					break;
				case "malleability":
					createAttributeUI("MA", value);
					break;
				case "potential_energy":
					createAttributeUI("PE", value);
					break;
				case "overall_quality":
					createAttributeUI("OQ", value);
					break;
				case "shock_resistance":
					createAttributeUI("SR", value);
					break;
				case "unit_toughness":
					createAttributeUI("UT", value);
					break;
			}
		});
	}

	private void createAttributeUI(String simple, IntegerProperty valueProperty) {
		VBox group = new VBox();
		group.setAlignment(Pos.CENTER);
		group.setPadding(new Insets(5.0, 0, 0, 0));
		group.disableProperty().bind(valueProperty.isEqualTo(-1));

		Label nameLabel = new Label(simple);
		nameLabel.setContentDisplay(ContentDisplay.CENTER);
		group.getChildren().add(nameLabel);

		Label valueLabel = new Label("--");
		valueLabel.setContentDisplay(ContentDisplay.CENTER);
		group.getChildren().add(valueLabel);

		Bindings.bindBidirectional(valueLabel.textProperty(), valueProperty, new ResourceValueConverter());

		resourceStatsBox.getChildren().add(group);
	}

	private Image getImage(String container) {
		if (container == null)
			return null;
		URL url = getClass().getResource("images/resources/" + container + ".png");
		if (url == null) {
			container = container.split("_")[0];
			url = getClass().getResource("images/resources/" + container + ".png");
			if (url == null)
				return null;
		}
		return new Image(url.toString());
	}

	public GalaxyResource getGalaxyResource() {
		return galaxyResource.get();
	}

	public void setGalaxyResource(GalaxyResource galaxyResource) {
		this.galaxyResource.set(galaxyResource);
	}

	public SimpleObjectProperty<GalaxyResource> galaxyResourceProperty() {
		return galaxyResource;
	}
}
