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

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import com.rockhoppertech.music.fx.keyboard.KeyboardPanel;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 *
 */
public class Pianoroll extends BorderPane {

    private KeyboardPanel keyboard;
    private PianorollPane pianorollPane;
    private BeatHeader beatHeader;

    public Pianoroll() {
        this.keyboard = new KeyboardPanel(Orientation.VERTICAL,
                KeyboardPanel.MIN_OCTAVE, KeyboardPanel.MAX_OCTAVE);

        this.pianorollPane = new PianorollPane(this.keyboard);

        beatHeader = new BeatHeader();
        beatHeader.setPianorollPane(pianorollPane);

        GridPane grid = new GridPane();
        //grid.setHgap(10);
        //grid.setVgap(10);
        //grid.setPadding(new Insets(0, 10, 0, 10));

        grid.add(new Label("label"), 0, 0);
        grid.add(keyboard, 0, 1);
        grid.add(beatHeader, 1, 0);
        grid.add(pianorollPane, 1, 1);

        this.setCenter(grid);
    }

    public void setMIDITrack(MIDITrack track) {
        this.pianorollPane.setTrack(track);
    }

}
