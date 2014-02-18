package com.rockhoppertech.music.fx.cmn;

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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.sound.midi.Receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.fx.cmn.model.StaffModel;
import com.rockhoppertech.music.fx.cmn.model.StaffModel.Clef;
import com.rockhoppertech.music.midi.js.MIDISender;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class GrandStaffController {
    private static final Logger logger = LoggerFactory
            .getLogger(GrandStaffController.class);

    StaffModel model;
    Stage stage;
    MIDISender midiSender;
    int pitch;
    
    // GUI. maybe fxml sometime

    private GrandStaff canvas;
    private TextField textField;

    public GrandStaffController(StaffModel staffModel, GrandStaff gs) {
        this.model = staffModel;
        this.midiSender = new MIDISender();
        this.setCanvas(gs);
    }

    public void setFontSize(double fontSize) {
        this.model.setFontSize(fontSize);
    }

    public void mousePressed(MouseEvent evt) {
        pitch = model.whichNote(evt.getY());
        if (pitch < 0 || pitch > 127) {
            return;
        }
        this.midiSender.sendNoteOn(pitch, 64);
        Pitch p = PitchFactory.getPitch(pitch);
        String preferredSpelling = p.getPreferredSpelling();
        if (preferredSpelling == null || preferredSpelling.equals("")
                || preferredSpelling.equals("null")) {
            logger.debug(
                    "preferred spelling empty for pitch {}",
                    pitch);
            // preferredSpelling = PitchFormat.getInstance().format(
            // pitch);
        }
        preferredSpelling += " ";

        logger.debug("pitch {} spelling '{}'", p, preferredSpelling);
        textArea.appendText(preferredSpelling);
        model.addNote(pitch);
        // canvas.repaintCanvas();

    }

    public void mouseReleased(MouseEvent evt) {
        this.midiSender.sendNoteOff(pitch);
        // if you draw in the mouse down, the Text will grab the mouseReleased
        // event
        canvas.drawShapes();
    }

    public void mouseDragged(MouseEvent evt) {

    }

    /**
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @param stage
     *            the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCanvas(GrandStaff view2) {
        this.canvas = view2;
        this.canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                mousePressed(evt);
            }
        });
        this.canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                mouseReleased(evt);
            }
        });
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                mouseDragged(evt);
            }
        });

    }

    public void notestringAction(ActionEvent event) {
        String ns = textArea.getText();
        MIDITrack track = MIDITrackBuilder
                .create()
                .noteString(ns)
                .sequential()
                .build();
        logger.debug("track from string\n{}", track);
        this.model.setTrack(track);
        // canvas.repaintCanvas();
        canvas.drawShapes();
    }

    /**
     * @param textField
     *            the textField to set
     */
    public void setTextField(TextField textField) {
        this.textField = textField;
        this.textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                notestringAction(event);
            }
        });
    }

    TextArea textArea;

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
        // this.textField.setOnAction(new EventHandler<ActionEvent>() {
        // @Override
        // public void handle(ActionEvent event) {
        // notestringAction(event);
        // }
        // });
    }

    Button noteStringButton;

    public void setNoteStringButton(Button b) {
        this.noteStringButton = b;
        this.noteStringButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                notestringAction(event);
            }
        });

    }

    ComboBox<String> clefComboBox;

    public void setClefCombBox(ComboBox<String> clefComboBox) {
        this.clefComboBox = clefComboBox;
        clefComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String sel = GrandStaffController.this.clefComboBox
                        .getSelectionModel().getSelectedItem();
                if (sel.equals("Treble")) {
                    model.setClef(Clef.TREBLE);
                } else if (sel.equals("Bass")) {
                    model.setClef(Clef.BASS);
                } else if (sel.equals("Alto")) {
                    model.setClef(Clef.ALTO);
                }
                // canvas.repaintCanvas();
                canvas.drawShapes();
            }
        });

    }

    public void addReceiver(Receiver receiver) {
        this.midiSender.addReceiver(receiver);
    }

    public void setPlayButton(Button b) {
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.getTrackProperty().get().play();
            }
        });

    }

    private ComboBox<Double> fontSizeComboBox;

    public void setFontSizeComboBox(ComboBox<Double> fontSizeComboBox) {
        this.fontSizeComboBox = fontSizeComboBox;
        fontSizeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Double sel = GrandStaffController.this.fontSizeComboBox
                        .getSelectionModel().getSelectedItem();
                model.setFontSize(sel);
                canvas.drawShapes();
            }
        });
    }

}
