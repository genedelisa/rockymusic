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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.js.KeySignature;
import com.rockhoppertech.music.midi.js.MIDISender;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class MeasureContainerAppController {
    private static final Logger logger = LoggerFactory
            .getLogger(MeasureContainerAppController.class);

    private int pitch;
    private MIDISender midiSender;

    @FXML
    // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML
    // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    // fx:id="evaluateButton"
    private Button evaluateButton; // Value injected by FXMLLoader

    @FXML
    // fx:id="fontSizeCombo"
    private ComboBox<Double> fontSizeCombo; // Value injected by FXMLLoader


    @FXML
    // fx:id="noteStringTextArea"
    private TextArea noteStringTextArea; // Value injected by FXMLLoader

    @FXML
    private MeasureContainer measureParent;

    @FXML
    private ScrollPane staffScrollPane;

    @FXML
    private CheckBox sequentialCheckBox;

    @FXML
    private CheckBox drawKeySignatureKeySignatureCheckBox;

    @FXML
    private CheckBox drawClefCheckBox;

    @FXML
    private CheckBox drawTimeSignatureCheckBox;

    @FXML
    private CheckBox drawBeatsCheckBox;

    @FXML
    private CheckBox drawBracesCheckBox;

    @FXML
    private ComboBox<String> keyComboBox;
    
    @FXML
    private TextArea topTextArea;

    @FXML
    void keySignatureAction(ActionEvent event) {
        String k = keyComboBox.getSelectionModel().getSelectedItem();
    }

    @FXML
    void drawBraces(ActionEvent event) {
        this.measureParent.setDrawBraces(drawBracesCheckBox.isSelected());
    }

    @FXML
    void drawBeats(ActionEvent event) {
        drawBeatsCheckBox.selectedProperty().bindBidirectional(
                measureParent.drawBeatsProperty());
        this.measureParent
                .setDrawBeatRectangles(drawBeatsCheckBox.isSelected());
    }

    @FXML
    void drawKeySignature(ActionEvent event) {
        this.measureParent
                .setDrawKeySignature(drawKeySignatureKeySignatureCheckBox
                        .isSelected());

    }

    @FXML
    void drawClefs(ActionEvent event) {
        this.measureParent.setDrawClefs(drawClefCheckBox.isSelected());
    }

    @FXML
    void drawTimeSignature(ActionEvent event) {
        this.measureParent.setDrawTimeSignature(drawTimeSignatureCheckBox
                .isSelected());
    }

    // Handler for Button[fx:id="evaluateButton"] onAction
    @FXML
    void evaluateNoteString(ActionEvent event) {
        String ns = noteStringTextArea.getText();
        MIDITrack track = MIDITrackBuilder
                .create()
                .noteString(ns)
                .build();
        if (sequentialCheckBox.isSelected()) {
            track.sequential();
        }
        
        String k = keyComboBox.getSelectionModel().getSelectedItem();
        if (k != null) {

            if (k.equals("C#")) {
                track.addKeySignatureAtBeat(1d, KeySignature.CSMAJOR);
            } else if (k.equals("Cb")) {
                track.addKeySignatureAtBeat(1d, KeySignature.CFMAJOR);
            } else if (k.equals("Db")) {
                track.addKeySignatureAtBeat(1d, KeySignature.DFMAJOR);
            } else if (k.equals("D")) {
                track.addKeySignatureAtBeat(1d, KeySignature.DMAJOR);
            } else if (k.equals("Eb")) {
                track.addKeySignatureAtBeat(1d, KeySignature.EFMAJOR);
                // } else if(k.equals("D#")) {
                // track.addKeySignatureAtBeat(1d, KeySignature.d);
            } else if (k.equals("E")) {
                track.addKeySignatureAtBeat(1d, KeySignature.EMAJOR);
            } else if (k.equals("F")) {
                track.addKeySignatureAtBeat(1d, KeySignature.FMAJOR);
            } else if (k.equals("F#")) {
                track.addKeySignatureAtBeat(1d, KeySignature.FSMAJOR);
            } else if (k.equals("Gb")) {
                track.addKeySignatureAtBeat(1d, KeySignature.GFMAJOR);
            } else if (k.equals("G")) {
                track.addKeySignatureAtBeat(1d, KeySignature.GMAJOR);
                // } else if(k.equals("G#")) {
                // track.addKeySignatureAtBeat(1d, KeySignature.GSMAJOR);
            } else if (k.equals("Ab")) {
                track.addKeySignatureAtBeat(1d, KeySignature.AFMAJOR);
            } else if (k.equals("A")) {
                track.addKeySignatureAtBeat(1d, KeySignature.AMAJOR);
            } else if (k.equals("Bb")) {
                track.addKeySignatureAtBeat(1d, KeySignature.BFMAJOR);
                // } else if(k.equals("A#")) {
                // track.addKeySignatureAtBeat(1d, KeySignature.ASMAJOR);
            } else if (k.equals("B")) {
                track.addKeySignatureAtBeat(1d, KeySignature.BMAJOR);
            }
        }

        topTextArea.setText(track.toString());
        logger.debug("track from string\n{}", track);
        measureParent.setTrack(track);
        measureParent.draw();

        // NavigableMap<Double, Measure> measures =
        // Measure.createMeasures(track);
        // logger.debug("measures keyset {}", measures.keySet());
        //
        // Measure measure = measures.get(1d);
        // logger.debug("measure 0 {}", measure);
        //
        // measureCanvas.setMeasure(measure);
        //
        // measureCanvas.drawShapes();
        //
        // MeasureCanvas mc = new MeasureCanvas();
        // mc.setMeasure(measures.get(5d));
        // mc.drawShapes();
        // measureParent.getChildren().add(mc);
    }

    // Handler for ComboBox[fx:id="fontSizeCombo"] onAction
    @FXML
    void fontSizeAction(ActionEvent event) {
        double size = fontSizeCombo.getSelectionModel().getSelectedItem();
        this.measureParent.setFontSize(size);
        this.measureParent.draw();

       // measureCanvas.setFontSize(size);
       // measureCanvas.drawShapes();
        // measureCanvas.setPrefSize(size * 1000d, size * 10d);
        // staffScrollPane.setPrefSize(size * 1000d, size * 10d);

        // TODO what should I really do?
        staffScrollPane.setContent(measureParent);
    }

    @FXML
    void staffMousePressed(MouseEvent event) {
        //pitch = measureParent.whichNote(event.getY());
        pitch = measureParent.whichNote(event.getY());
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
        noteStringTextArea.appendText(preferredSpelling);
//        measureParent.addNote(pitch);
    }

    @FXML
    void staffMouseReleased(MouseEvent event) {
        this.midiSender.sendNoteOff(pitch);
        // if you draw in the mouse down, the Text will grab the mouseReleased
        // event
        measureParent.draw();
    }

    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert evaluateButton != null : "fx:id=\"evaluateButton\" was not injected: check your FXML file 'MeasureApp.fxml'.";
        assert fontSizeCombo != null : "fx:id=\"fontSizeCombo\" was not injected: check your FXML file 'MeasureApp.fxml'.";
        // assert midiReceiver != null :
        // "fx:id=\"midiReceiver\" was not injected: check your FXML file 'GrandStaffPanel.fxml'.";
        assert noteStringTextArea != null : "fx:id=\"noteStringTextArea\" was not injected: check your FXML file 'MeasureApp.fxml'.";
        //assert measureCanvas != null : "fx:id=\"measureCanvas\" was not injected: check your FXML file 'MeasureApp.fxml'.";
        assert measureParent != null : "fx:id=\"measureParent\" was not injected: check your FXML file 'MeasureApp.fxml'.";

        // Initialize your logic here: all @FXML variables will have been
        // injected

        // noteStringTextArea.setText("s+ c,s c#,s df,s d,s ef,s e,s f,s f#,s gf,s g,s g#,s af,s a,s a#,s bb,s b,s");
        noteStringTextArea
                .setText("s+ c,x c,x c,x c,x c,x c,x c,x c,x c,x c,x c,x c,x c,x c,x c,x c,x c,x ");
        
        noteStringTextArea
        .setText("X15 (c d e) ");

      
        fontSizeCombo.getItems().addAll(12d, 18d, 24d, 36d, 48d, 72d, 96d);
        fontSizeCombo.getSelectionModel().select(4);

        //measureCanvas.setFontSize(48d);
        //measureCanvas.drawShapes();

        this.measureParent.setFontSize(48d);
        this.measureParent.draw();
    }

}
