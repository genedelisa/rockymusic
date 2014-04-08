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

import com.rockhoppertech.music.fx.keyboard.KeyboardPanel;
import com.rockhoppertech.music.midi.js.MIDITrack;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 */
public class Pianoroll extends BorderPane {
    private static final Logger logger = LoggerFactory
            .getLogger(Pianoroll.class);

    private KeyboardPanel keyboard;
    private PianorollPane pianorollPane;
    private BeatHeader beatHeader;

    public Pianoroll() {
        this.keyboard = new KeyboardPanel(Orientation.VERTICAL,
                KeyboardPanel.MIN_OCTAVE, KeyboardPanel.MAX_OCTAVE);

        this.pianorollPane = new PianorollPane(this.keyboard);

        beatHeader = new BeatHeader();
        beatHeader.setPianorollPane(pianorollPane);

        stack();
    }

    void grid() {
        GridPane grid = new GridPane();
        //grid.setHgap(10);
        //grid.setVgap(10);
        //grid.setPadding(new Insets(0, 10, 0, 10));

        //grid.add(new Label("label"), 0, 0);
        //grid.add(keyboard, 0, 1);
        //grid.add(beatHeader, 1, 0);
        //grid.add(pianorollPane, 1, 1);


        double h = beatHeader.getHeight();
        grid.add(keyboard, 0, 0);
        VBox box = new VBox();
        box.getChildren().add(beatHeader);
        box.getChildren().add(pianorollPane);
        ScrollPane sp = new ScrollPane(box);
        sp.setPrefViewportWidth(800d);

        grid.add(sp, 1, 0);

        this.setCenter(grid);
    }

    void stack() {
        //StackPane pane = new StackPane();
        Pane pane = new Pane();
        double w = keyboard.getWidth();
        double h = beatHeader.getHeight();

        keyboard.setLayoutX(1d);
        keyboard.setLayoutY(h);
        //  keyboard.relocate(0d, 40d);
        pane.getChildren().add(keyboard);

        VBox box = new VBox();
        box.getChildren().add(beatHeader);
        box.getChildren().add(pianorollPane);
        ScrollPane sp = new ScrollPane(box);
        sp.setPrefViewportWidth(800d);
        sp.setLayoutX(w + 1d);
        sp.setLayoutY(0d);
        sp.setPrefHeight(keyboard.getPrefHeight());
        pane.getChildren().add(sp);

        this.setCenter(pane);
    }

    public void setMIDITrack(MIDITrack track) {
        this.pianorollPane.setTrack(track);
    }

}
