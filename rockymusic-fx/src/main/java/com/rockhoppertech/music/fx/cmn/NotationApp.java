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

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.AnchorPaneBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.model.StaffModel;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class NotationApp extends Application {

    private static final Logger logger = LoggerFactory
            .getLogger(NotationApp.class);
    Stage stage;
    Scene scene;
    Pane root;

    private NotationController controller;
    private StaffModel staffModel;
    // private NotationView view;
    // private NotationCanvas view;
    private StaffRegion view;

    // private StaffControl view;

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
                .durations(1, 1.5, .5, .75, .25, .25)
                .sequential()
                .build();
        System.out.println(track);
        this.staffModel.setTrack(track);
        // this.view = new NotationCanvas(this.staffModel);
        this.view = new StaffRegion(this.staffModel);
        this.controller = new NotationController(staffModel, view);
        this.view.drawShapes();

        this.configureScene();
        this.configureStage();
        logger.debug("started");
    }

    private void configureStage() {
        stage.setTitle("Music Notation");

        // fullScreen();

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
        // TextField text = new TextField();
        // text.setId("noteStringText");
        // text.setEditable(true);
        // text.setPromptText("Enter a note string");
        // controller.setTextField(text);

        TextArea textArea = new TextArea();
        textArea.setId("noteStringText");
        textArea.setEditable(true);
        textArea.setPromptText("Enter a note string");
        textArea.setWrapText(true);
        textArea.setText(MIDITrack
                .getPitchesAsString(this.staffModel.getTrackProperty().get()));
        controller.setTextArea(textArea);
        
        Button b = ButtonBuilder.create()
                .id("noteStringButton")
                .style("-fx-font: 22 arial; -fx-base: #1055FF;")
                .text("Evaluate note string")
                .build();
        controller.setNoteStringButton(b);
        Button pb = ButtonBuilder.create()
                .id("playButton")
                .style("-fx-font: 22 arial; -fx-base: #1055FF;")
                .text("Play")
                .build();
        controller.setPlayButton(pb);

        final ComboBox<String> clefComboBox = new ComboBox<>();
        clefComboBox.getItems().addAll(
                "Treble",
                "Bass",
                "Alto"
                );
        clefComboBox.getSelectionModel().selectFirst();
        clefComboBox.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends String> observable,
                            String oldValue, String newValue) {

                    }
                });
        ;
        controller.setClefCombBox(clefComboBox);

        final ComboBox<Double> fontSizeComboBox = new ComboBox<>();
        fontSizeComboBox.getItems().addAll(12d, 24d, 36d, 48d, 72d, 96d);
        fontSizeComboBox.getSelectionModel().select(3);
        controller.setFontSizeComboBox(fontSizeComboBox);

        FXTextAreaReceiver receiver = new FXTextAreaReceiver();
        controller.addReceiver(receiver);

        HBox hbox = HBoxBuilder.create()
                .padding(new Insets(20))
                .children(clefComboBox, fontSizeComboBox)
                .build();
        HBox buttonbox = HBoxBuilder.create()
                .padding(new Insets(20))
                .children(b, pb)
                .build();

        VBox vbox = VBoxBuilder.create()
                .padding(new Insets(20))
                .children(textArea, buttonbox, hbox, receiver)
                .build();

        ScrollPane sp = new ScrollPane();
        // sp.setContent(view.getCanvas());
        sp.setContent(view);
        sp.setPrefSize(1300, 300);

        BorderPane bp = BorderPaneBuilder.create()
                .id("rootpane")
                // .padding(new Insets(20))
                .style("-fx-padding: 30")
                .top(sp)
                .center(vbox)
                .build();
        AnchorPane.setTopAnchor(bp, 10.0);
        AnchorPane.setBottomAnchor(bp, 10.0);
        AnchorPane.setLeftAnchor(bp, 10.0);
        AnchorPane.setRightAnchor(bp, 65.0);

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
