package com.rockhoppertech.music.fx.cmn;

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

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class DurationToggleApp extends Application {

    private static final Logger logger = LoggerFactory
            .getLogger(DurationToggleApp.class);
    private Stage stage;
    private Scene scene;
    private Pane root;

    // private InputStaffAppController controller;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    void loadRootFxml() {
        String fxmlFile = "/fxml/DurationToggles.fxml";
        logger.debug("Loading FXML for main view from: {}", fxmlFile);
        try {
            FXMLLoader loader = new FXMLLoader(
                    DurationToggleApp.class.getResource(fxmlFile));
            root = (AnchorPane) loader.load();
            // controller = loader.getController();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        loadRootFxml();
        this.scene = SceneBuilder.create()
                .root(root)
                .build();
        this.configureStage();
        logger.debug("started");
    }

    private void configureStage() {
        stage.setTitle("Toggles");
        // fullScreen();
        stage.setScene(this.scene);
        stage.show();
    }

    private void fullScreen() {
        // make it full screen
        stage.setX(0);
        stage.setY(0);
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
    }
}
