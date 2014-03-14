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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.PitchFormat;
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
    private TextField resultText; // Value injected by FXMLLoader

    @FXML
    private InputStaff staff; // Value injected by FXMLLoader
    
    @FXML
    private Label pitchLabel;

    @FXML
    void staffKeyTyped(KeyEvent event) {
        logger.debug("key typed {}", event);
        logger.debug("key typed code {}", event.getCode());
        if (event.getCode() == KeyCode.LEFT) {
            event.consume();
        }
    }

    @FXML
    void staffMouseDragged(MouseEvent event) {
        logger.debug("dragged {}", event);

        int currentPitch = inputNote.getMidiNumber();
        this.midiSender.sendNoteOff(currentPitch);
        
        int pitch = staff.getStaffModel().whichNote(event.getY());
        if (pitch < 0 || pitch > 127) {
            return;
        }
        
        if (pitch != currentPitch) {
            staff.getStaffModel().setPitch(pitch);
            this.midiSender.sendNoteOn(pitch, 64);
            logger.debug("note {}", inputNote);
            staff.updateSymbol();
            staff.draw();
        }
    }

    @FXML
    void staffMousePressed(MouseEvent event) {
        // staff.requestFocus();

        int pitch = staff.getStaffModel().whichNote(event.getY());
        if (pitch < 0 || pitch > 127) {
            return;
        }

        int currentPitch = inputNote.getMidiNumber();
        if (pitch != currentPitch) {
            staff.getStaffModel().setPitch(pitch);
            this.midiSender.sendNoteOn(pitch, 64);
            logger.debug("note {}", inputNote);
            staff.updateSymbol();
            staff.draw();
        }
    }

    @FXML
    void staffMouseReleased(MouseEvent event) {
        this.midiSender.sendNoteOff(inputNote.getMidiNumber());
        staff.draw();
    }

    @FXML
    void initialize() {
        assert resultText != null : "fx:id=\"resultText\" was not injected: check your FXML file 'InputStaffPanel.fxml'.";
        assert staff != null : "fx:id=\"staff\" was not injected: check your FXML file 'InputStaffPanel.fxml'.";

        this.midiSender = new MIDISender();

        staff.requestFocus();
        inputNote = this.staff.getNote();
        staff.draw();
        
        //pitchLabel.textProperty().bind(staff.getStaffModel().pitchProperty());
        staff.getStaffModel().pitchProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> arg0,
                    Number arg1, Number newPitch) {
                pitchLabel.setText(PitchFormat.midiNumberToString(newPitch.intValue()));
            }});
        

        staff.addEventFilter(
                MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        staffMouseDragged(mouseEvent);
                    }
                });
        staff.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        staffMousePressed(mouseEvent);
                    }
                });
        staff.addEventFilter(
                MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        staffMouseReleased(mouseEvent);
                    }
                });

    }

}
