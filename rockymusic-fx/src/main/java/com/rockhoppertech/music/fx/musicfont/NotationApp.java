package com.rockhoppertech.music.fx.musicfont;

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
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.AnchorPaneBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.musicfont.model.StaffModel;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class NotationApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(NotationApp.class);
    Stage stage;
    Scene scene;
    Pane root;

    private NotationController controller;
    private StaffModel staffModel;
    private NotationView view;

    // private static ObservableList<MIDINote> tableDataList;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        this.staffModel = new StaffModel();
        MIDITrack track = MIDITrackBuilder
                .create()
                .noteString(
                        "E5 F G Ab G# A B C C6 D Eb F# G A B C7 B4 Bf4 A4 Af4")
                .durations(1, 1.5, .5)
                .build();
        System.out.println(track);
        this.staffModel.setTrack(track);
        this.view = new NotationView(this.staffModel);
        this.controller = new NotationController(staffModel, view);
        this.view.repaintCanvas();

        this.configureScene();
        this.configureStage();
        logger.debug("started");
    }

    private void configureStage() {
        stage.setTitle("Music Notation");

        fullScreen();

        stage.setScene(this.scene);
        controller.setStage(this.stage);
        stage.show();
    }

    private void fullScreen() {
        // make it full screen
        stage.setX(0);
        stage.setY(0);
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
    }

    private void configureScene() {
        TextField text = TextFieldBuilder.create()
                .id("noteStringText")
                .editable(true)
                .promptText("Enter a note string")
                .build();
        controller.setTextField(text);
         
        BorderPane bp = BorderPaneBuilder.create()
                .id("rootpane")
                // .padding(new Insets(20))
                // .style("-fx-padding: 30")
                .center(view.getCanvasPane())
                .bottom(text)
                .build();
        AnchorPane.setTopAnchor(bp, 10.0);
        AnchorPane.setBottomAnchor(bp, 10.0);
        AnchorPane.setLeftAnchor(bp, 10.0);
        //AnchorPane.setRightAnchor(bp, 65.0);

        root = AnchorPaneBuilder.create()
                .children(bp)
                .build();
        
        this.scene = SceneBuilder.create()
                .root(root)
                .fill(Color.web("#1030F0"))
                // .stylesheets("/styles/app2styles.css")
                .build();
    }

    // private void configureSceneold() {
    //
    // // double fontSize = 24d;
    // // double fontSize = 36d;
    // double fontSize = 48d;
    // // double fontSize = 96d;
    // Font font = Font.loadFont(
    // FontApp.class.getResource("/fonts/Bravura.otf")
    // .toExternalForm(),
    // fontSize);
    // // FontMetrics fm;
    //
    // // Font font = new Font("Bravura", fontSize);
    //
    // Canvas canvas = new Canvas(1300, 250);
    // canvas.setOpacity(100);
    //
    // GraphicsContext gc = canvas.getGraphicsContext2D();
    // gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    // gc.setFill(Color.WHITE);
    // gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    // gc.setFill(Color.BLACK);
    // gc.setTextBaseline(VPos.CENTER);
    // gc.setFont(font);
    //
    // double x = 50d;
    // double y = 125d;
    // // double yinc = fontSize / 10d + 1.2; // magic number since we lack
    // // font
    // // metrics. works for 48. too much
    // // for 24 or 36
    //
    // this.yspacing = fontSize / 8d;
    //
    // gc.fillText(getGlyph("gClef"), x, y - (this.yspacing * 2d));
    // // gc.fillText(getGlyph("fClef"), x, y - (this.yspacing * 6d));
    //
    // this.trebleStaffBottom = y;
    // String staff = getGlyph("staff5Lines");
    // for (double xx = x; xx < 1250; xx += fontSize / 2d) {
    // gc.fillText(staff, xx, y);
    // }
    //
    // gc.fillText(getGlyph("noteQuarterUp"), x += fontSize, y);
    // gc.fillText(getGlyph("accidentalFlat"), x += fontSize, y);
    // gc.fillText(getGlyph("noteQuarterDown"), x += fontSize / 3d, y);
    // gc.fillText(getGlyph("noteHalfUp"), x += fontSize, y);
    //
    // double yy = y;
    // // ascending scale
    // for (int i = 0; i < 12; i++, yy -= this.yspacing) {
    // gc.fillText(SymbolFactory.noteQuarterUp(), x += fontSize, yy);
    // }
    //
    // setupStaff();
    //
    // yy = this.trebleFlatYpositions[Pitch.D5];
    // gc.fillText(SymbolFactory.noteQuarterDownFlat(), x += fontSize, yy);
    //
    // yy = this.trebleFlatYpositions[Pitch.E5];
    // gc.fillText(SymbolFactory.noteQuarterDownFlat(), x += fontSize, yy);
    //
    // yy = this.trebleFlatYpositions[Pitch.F5];
    // gc.fillText(SymbolFactory.noteQuarterDownFlat(), x += fontSize, yy);
    //
    // yy = this.trebleFlatYpositions[Pitch.G5];
    // gc.fillText(SymbolFactory.noteQuarterDownFlat(), x += fontSize, yy);
    //
    // yy = this.trebleFlatYpositions[Pitch.A5];
    // gc.fillText(SymbolFactory.noteQuarterDownFlat(), x += fontSize, yy);
    //
    // yy = this.trebleFlatYpositions[Pitch.B5];
    // gc.fillText(SymbolFactory.noteQuarterDownFlat(), x += fontSize, yy);
    //
    // yy = this.trebleFlatYpositions[Pitch.C6];
    // gc.fillText(SymbolFactory.noteQuarterDownFlat(), x += fontSize, yy);
    //
    // int pitch = Pitch.EF6;
    // if (needFlats(pitch)) {
    // String ns = SymbolFactory.noteQuarterDownFlat();
    // yy = this.trebleFlatYpositions[pitch];
    // gc.fillText(ns, x += fontSize, yy);
    // }
    // // ledger? "staff1Line"
    //
    // /*
    // * "noteheadBlack": { "stemDownNW": [ 0.0, -0.184 ], "stemUpSE": [
    // * 1.328, 0.184 ] },
    // */
    //
    // // gc.fillText("abcedfghijklmnopqrstuvwxyz", 50d, 150d);
    //
    // /*
    // * "gClef": { "alternateCodepoint": "U+1D11E", "codepoint": "U+E050" },
    // */
    //
    // // Image bgimage = new Image(getClass().getResourceAsStream(
    // // "/images/background.jpg"));
    // // ImageView imageView = ImageViewBuilder.create()
    // // .image(bgimage)
    // // .build();
    //
    // Button b = ButtonBuilder.create()
    // .id("someButton")
    // .text("Button")
    // .style("-fx-font: 22 arial; -fx-base: #b6e7c9;")
    // // .onAction(new EventHandler<ActionEvent>() {
    // // @Override
    // // public void handle(ActionEvent e) {
    // // logger.debug("local button pressed {}", e);
    // // }
    // // })
    // .build();
    //
    // // not a singleton: logger.debug("button builder {}",
    // // ButtonBuilder.create());
    //
    // // the controller has the action handler
    // //this.controller.setButton(b);
    // //BorderPane.setAlignment(b, Pos.CENTER);
    //
    // BorderPane borderPane = new BorderPane();
    // // borderPane.setTop(toolbar);
    // // borderPane.setLeft(actionPane);
    // // borderPane.setRight(colorPane);
    //
    // borderPane.setCenter(view.getCanvasPane());
    //
    // // borderPane.setBottom(statusBar);
    //
    // Group group = new Group();
    // group.getChildren().add(canvas);
    // // group.getChildren().add(b);
    //
    // root =
    // BorderPaneBuilder
    // .create()
    // .id("rootpane")
    // .padding(new Insets(20))
    // // .style("-fx-padding: 30")
    // .center(group)
    // .build();
    //
    // this.scene = SceneBuilder.create()
    // // .root(root)
    // .root(borderPane)
    // .fill(Color.web("#103000"))
    // // .stylesheets("/styles/app2styles.css")
    // .build();
    //
    // // MIDITrack track = MIDITrackBuilder.create()
    // // .noteString("C D E")
    // // .build();
    // // track.sequential();
    //
    // }

}
