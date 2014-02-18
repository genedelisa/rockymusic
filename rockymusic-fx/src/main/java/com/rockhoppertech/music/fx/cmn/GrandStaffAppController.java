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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.js.MIDISender;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class GrandStaffAppController {
    private static final Logger logger = LoggerFactory
            .getLogger(GrandStaffAppController.class);

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
    // fx:id="midiReceiver"
    private FXTextAreaReceiver midiReceiver; // Value injected by FXMLLoader

    @FXML
    // fx:id="noteStringTextArea"
    private TextArea noteStringTextArea; // Value injected by FXMLLoader

    @FXML
    // GrandStaff[fx:id="grandStaff"]
    private GrandStaff grandStaff; // Value injected by FXMLLoader

    @FXML
    private ScrollPane staffScrollPane;

    // Handler for Button[fx:id="evaluateButton"] onAction
    @FXML
    void evaluateNoteString(ActionEvent event) {
        String ns = noteStringTextArea.getText();
        MIDITrack track = MIDITrackBuilder
                .create()
                .noteString(ns)
                .sequential()
                .build();
        logger.debug("track from string\n{}", track);
        grandStaff.getStaffModel().setTrack(track);
        grandStaff.drawShapes();
    }

    // Handler for ComboBox[fx:id="fontSizeCombo"] onAction
    @FXML
    void fontSizeAction(ActionEvent event) {
        double size = fontSizeCombo.getSelectionModel().getSelectedItem();
        grandStaff.getStaffModel().setFontSize(size);
        grandStaff.drawShapes();
        grandStaff.setPrefSize(size * 1000d, size * 10d);
        //staffScrollPane.setPrefSize(size * 1000d, size * 10d);

        // TODO what should I really do?
        staffScrollPane.setContent(grandStaff);

    }

    // Handler for GrandStaff[fx:id="grandStaff"] onMousePressed
    @FXML
    void staffMousePressed(MouseEvent event) {
        pitch = grandStaff.getStaffModel().whichNote(event.getY());
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
        grandStaff.getStaffModel().addNote(pitch);
        // canvas.repaintCanvas();
    }

    // Handler for GrandStaff[fx:id="grandStaff"] onMouseReleased
    @FXML
    void staffMouseReleased(MouseEvent event) {
        this.midiSender.sendNoteOff(pitch);
        // if you draw in the mouse down, the Text will grab the mouseReleased
        // event
        grandStaff.drawShapes();
    }

    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert evaluateButton != null : "fx:id=\"evaluateButton\" was not injected: check your FXML file 'GrandStaffPanel.fxml'.";
        assert fontSizeCombo != null : "fx:id=\"fontSizeCombo\" was not injected: check your FXML file 'GrandStaffPanel.fxml'.";
        assert midiReceiver != null : "fx:id=\"midiReceiver\" was not injected: check your FXML file 'GrandStaffPanel.fxml'.";
        assert noteStringTextArea != null : "fx:id=\"noteStringTextArea\" was not injected: check your FXML file 'GrandStaffPanel.fxml'.";
        assert grandStaff != null : "fx:id=\"topRegion\" was not injected: check your FXML file 'GrandStaffPanel.fxml'.";

        // Initialize your logic here: all @FXML variables will have been
        // injected

        logger.debug("fxml midireceiver {}", midiReceiver);
        this.midiSender = new MIDISender();
        if (this.midiReceiver != null)
            this.midiSender.addReceiver(this.midiReceiver);

        fontSizeCombo.getItems().addAll(12d, 24d, 36d, 48d, 72d, 96d);
        fontSizeCombo.getSelectionModel().select(3);

//        grandStaff.boundsInLocalProperty().addListener(
//                new ChangeListener<Bounds>() {
//                    @Override
//                    public void changed(
//                            ObservableValue<? extends Bounds> observableValue,
//                            Bounds oldBounds, Bounds newBounds) {
//                        logger.debug(
//                                "staff value {} new bounds {}",
//                                observableValue,
//                                newBounds);
//                        //staffScrollPane.setHmax(newBounds.getMaxY());
//                        //staffScrollPane.setH
//                        //staffScrollPane.setViewportBounds(newBounds);
//                    }
//                });
//        grandStaff.boundsInParentProperty().addListener(
//                new ChangeListener<Bounds>() {
//                    @Override
//                    public void changed(
//                            ObservableValue<? extends Bounds> observableValue,
//                            Bounds oldBounds, Bounds newBounds) {
//                        logger.debug(
//                                "staff bounds in parent value {} new bounds {}",
//                                observableValue,
//                                newBounds);
//                        //staffScrollPane.setHmax(newBounds.getMaxY());
//                        //staffScrollPane.setH
//                        //staffScrollPane.setViewportBounds(newBounds);
//                    }
//                });

//        staffScrollPane.viewportBoundsProperty().addListener(
//                new ChangeListener<Bounds>() {
//                    @Override
//                    public void changed(
//                            ObservableValue<? extends Bounds> observableValue,
//                            Bounds oldBounds, Bounds newBounds) {
////                        logger.debug(
////                                "scrollpane value {} new bounds {}",
////                                observableValue,
////                                newBounds);
//                        
//                        
//
//                        
//                    }
//                });
        
        
//        scrollPane.viewportBoundsProperty().addListener(
//                new ChangeListener<Bounds>() {
//                @Override public void changed(ObservableValue<? extends Bounds> observableValue, Bounds oldBounds, Bounds newBounds) {
//                  nodeContainer.setPrefSize(
//                    Math.max(node.getBoundsInParent().getMaxX(), newBounds.getWidth()),
//                    Math.max(node.getBoundsInParent().getMaxY(), newBounds.getHeight())
//                  );
//                }
//              });

    }

}
