package com.rockhoppertech.music.fx.pianoroll;

/*
 * #%L
 * Rocky Music FX
 * %%
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class PianorollApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Pianoroll");
        final Pane root = new Pane();
        // BorderPane bp = new BorderPane();

        //PianorollPane pianoroll = new PianorollPane();
        Pianoroll pianoroll = new Pianoroll();

        ScrollPane sp = new ScrollPane();
        sp.setLayoutX(0);
        sp.setPrefWidth(800);
        sp.setPrefHeight(1000);        
        sp.setContent(pianoroll);
        root.getChildren().add(sp);
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("c c# d d# e f f# g g# a a# b c6")
                .durations(1d, .5, .25, 2)
                .sequential()
                .build();
        pianoroll.setMIDITrack(track);

        final Scene scene = new Scene(root, 800, 1000, Color.rgb(160, 160, 160));
        stage.setScene(scene);
        stage.show();
    }

}
