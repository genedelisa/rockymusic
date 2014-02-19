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

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDISender;

public class InputStaffAppController {

    private static final Logger logger = LoggerFactory
            .getLogger(InputStaffAppController.class);

    MIDINote inputNote;
    MIDISender midiSender;

    @FXML
    // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML
    // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    // fx:id="resultText"
    private TextField resultText; // Value injected by FXMLLoader

    @FXML
    // fx:id="staff"
    private Staff staff; // Value injected by FXMLLoader

    // Handler for StaffRegion[fx:id="staff"] onKeyTyped
    @FXML
    void staffKeyTyped(KeyEvent event) {
        logger.debug("key typed", event);
        if (event.getCode() == KeyCode.LEFT) {
            event.consume();
        }
    }

    // Handler for StaffRegion[fx:id="staff"] onMouseDragged
    @FXML
    void staffMouseDragged(MouseEvent event) {
        logger.debug("dragged {}", event);
        
        int currentPitch = inputNote.getMidiNumber();
        this.midiSender.sendNoteOff(currentPitch);
        // if you draw in the mouse down, the Text will grab the mouseReleased
        // event

        
        int pitch = staff.getStaffModel().whichNote(event.getY());
        if(pitch != currentPitch) {
            this.inputNote.setMidiNumber(pitch);
            this.midiSender.sendNoteOn(pitch, 64);
            logger.debug("note {}", inputNote);
            staff.drawShapes();
        }
       
        
       
    }

    // Handler for StaffRegion[fx:id="staff"] onMousePressed
    @FXML
    void staffMousePressed(MouseEvent event) {
        staff.requestFocus();
        
        int pitch = staff.getStaffModel().whichNote(event.getY());
        if (pitch < 0 || pitch > 127) {
            return;
        }
        
        int currentPitch = inputNote.getMidiNumber();
        if(pitch != currentPitch) {
            this.inputNote.setMidiNumber(pitch);
            this.midiSender.sendNoteOn(pitch, 64);
            logger.debug("note {}", inputNote);
            staff.drawShapes();
        }

    }

    // Handler for StaffRegion[fx:id="staff"] onMouseReleased
    @FXML
    void staffMouseReleased(MouseEvent event) {
        this.midiSender.sendNoteOff(inputNote.getMidiNumber());
        // if you draw in the mouse down, the Text will grab the mouseReleased
        // event
        staff.drawShapes();
    }

    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert resultText != null : "fx:id=\"resultText\" was not injected: check your FXML file 'InputStaffPanel.fxml'.";
        assert staff != null : "fx:id=\"staff\" was not injected: check your FXML file 'InputStaffPanel.fxml'.";

        // Initialize your logic here: all @FXML variables will have been
        // injected
        this.midiSender = new MIDISender();
        staff.requestFocus();
        inputNote = new MIDINote(Pitch.C6);
        staff.getStaffModel().addNote(inputNote);
        staff.drawShapes();

    }

}
