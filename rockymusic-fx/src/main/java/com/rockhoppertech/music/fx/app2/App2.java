package com.rockhoppertech.music.fx.app2;

/*
 * #%L
 * rockymusic-fx
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBuilder;
import javafx.scene.control.Tab;
import javafx.scene.control.TabBuilder;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPaneBuilder;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class App2 extends Application {

	private static final Logger logger = LoggerFactory.getLogger(App2.class);
	Stage stage;
	Scene scene;
	BorderPane root;
	Controller controller;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		this.controller = new Controller();
		this.configureScene();
		this.configureStage();
		logger.debug("started");
	}

	private void configureStage() {
		stage.setTitle("App 2");

		// make it full screen
		stage.setX(0);
		stage.setY(0);
		stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
		stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());

		stage.setScene(this.scene);
		controller.setStage(this.stage);
		stage.show();
	}

	private void configureScene() {

		// Image bgimage = new Image(getClass().getResourceAsStream(
		// "/images/background.jpg"));
		// ImageView imageView = ImageViewBuilder.create()
		// .image(bgimage)
		// .build();

		Button b = ButtonBuilder.create()
				.id("someButton")
				.text("Button")
				.style("-fx-font: 22 arial; -fx-base: #b6e7c9;")
				// .onAction(new EventHandler<ActionEvent>() {
				// @Override
				// public void handle(ActionEvent e) {
				// logger.debug("local button pressed {}", e);
				// }
				// })
				.build();
		
        //not a singleton: logger.debug("button builder {}", ButtonBuilder.create());

		// the controller has the action handler
		this.controller.setButton(b);
		BorderPane.setAlignment(b, Pos.CENTER);

		root =
				BorderPaneBuilder
						.create()
						.id("rootpane")
						.padding(new Insets(20))
						// .style("-fx-padding: 30")
						.center(b)
						.build();

		this.scene = SceneBuilder.create()
				.root(root)
				.fill(Color.web("#103000"))
				.stylesheets("/styles/app2styles.css")
				.build();

		configureCombo();
		configureTabs();
	}

	void configureCombo() {
		ObservableList<String> list = FXCollections.observableArrayList();
		for (int i = 1; i < 100; i++) {
			list.add("Option: " + i);
		}

		final ComboBox<String> combo =
				ComboBoxBuilder.<String> create()
						.promptText("Select")
						.items(list)
						.style("-fx-border-color: black; -fx-border-width: 1")
						.build();
		combo.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Object>() {
					public void changed(
							ObservableValue<? extends Object> source,
							Object oldValue, Object newValue) {
						logger.debug("You selected: " + newValue);
					}
				});
		combo.getSelectionModel().select(0);

		BorderPane.setAlignment(combo, Pos.CENTER);
		this.root.setTop(combo);
	}

	void configureTabs() {
		TextArea content = TextAreaBuilder.create()
				.text("Here is some\ncontent")
				.build();
		Tab tab = TabBuilder.create()
				.text("A tab")
				.content(content)
				.build();
		TabPane tp = TabPaneBuilder.create()
				.tabs(tab)
				.build();
		BorderPane.setAlignment(tp, Pos.CENTER);
		this.root.setBottom(tp);
	}

}
