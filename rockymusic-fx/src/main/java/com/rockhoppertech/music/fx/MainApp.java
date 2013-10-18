package com.rockhoppertech.music.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MainApp extends Application {

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage stage) throws Exception {

		log.info("Starting JavaFX");

		String fxmlFile = "/fxml/Scene.fxml";
		log.debug("Loading FXML for main view from: {}", fxmlFile);
		// FXMLLoader loader = new FXMLLoader();
		// Parent rootNode = (Parent)
		// loader.load(getClass().getResourceAsStream(
		// fxmlFile));
		// Parent rootNode =
		// FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
		log.debug("Showing JFX scene");
		// Scene scene = new Scene(rootNode, 400, 200);

		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"/fxml/Scene.fxml"));
		Parent rootNode = (Parent) loader.load();
		FXMLController controller = (FXMLController) loader.getController();
		controller.setStage(stage);
		Scene scene = new Scene(rootNode);
		scene.getStylesheets().add("/styles/styles.css");

		stage.setTitle("MIDI String Parser");
		stage.setScene(scene);

		// make it full screen
		stage.setX(0);
		stage.setY(0);
		stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
		stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());

		stage.show();
	}
}
