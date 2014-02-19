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
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class GrandStaffApp extends Application {

    private static final Logger logger = LoggerFactory
            .getLogger(GrandStaffApp.class);
    private Stage stage;
    private Scene scene;
    private Pane root;
    private GrandStaffAppController controller;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    void loadRootFxml() {
        String fxmlFile = "/fxml/GrandStaffPanel.fxml";
        logger.debug("Loading FXML for main view from: {}", fxmlFile);
        try {
            FXMLLoader loader = new FXMLLoader(
                    GrandStaffApp.class.getResource(fxmlFile));
            root = (AnchorPane) loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(),e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

//        this.staffModel = new StaffModel();
//        MIDITrack track = MIDITrackBuilder
//                .create()
//                .noteString(
//                        "E5 F G Ab G# A B C C6 D Eb F# G A B C7 B4 Bf4 A4 Af4")
//                .durations(5, 4, 3, 2, 1, 1.5, .5, .75, .25, .25)
//                .sequential()
//                .build();
//        this.staffModel.setTrack(track);

        loadRootFxml();

        this.scene = SceneBuilder.create()
                .root(root)
                .fill(Color.web("#1030F0"))
                .stylesheets("/styles/grandStaffApp.css")
                .build();
        this.configureStage();
        logger.debug("started");
    }

    private void configureStage() {
        stage.setTitle("Music Notation");
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
