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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.fx.cmn.model.SymbolFactory;

/**
 * A Pane of Toggle Buttons representing durations.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class DurationToggleController {
    final static Logger logger = LoggerFactory
            .getLogger(DurationToggleController.class);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ToggleButton eighthToggle;

    @FXML
    private ToggleButton halfToggle;

    @FXML
    private ToggleButton quarterToggle;

    @FXML
    private AnchorPane root;

    @FXML
    private ToggleButton sixteenthToggle;

    @FXML
    private ToggleButton thirtySecondToggle;

    @FXML
    private ToggleButton wholeToggle;
    
    @FXML
    private HBox box;

    @FXML
    void durationAction(ActionEvent event) {
        if (event.getSource() == wholeToggle) {
            setDuration(Duration.WHOLE_NOTE);
        } else if (event.getSource() == halfToggle) {
            setDuration(Duration.HALF_NOTE);
        } else if (event.getSource() == quarterToggle) {
            setDuration(Duration.QUARTER_NOTE);
        } else if (event.getSource() == eighthToggle) {
            setDuration(Duration.EIGHTH_NOTE);
        } else if (event.getSource() == sixteenthToggle) {
            setDuration(Duration.SIXTEENTH_NOTE);
        } else if (event.getSource() == thirtySecondToggle) {
            setDuration(Duration.THIRTY_SECOND_NOTE);
        }
    }

    private DoubleProperty durationProperty = new SimpleDoubleProperty(1d);

    public DoubleProperty durationProperty() {
        return durationProperty;
    }

    public void setDuration(double d) {
        this.durationProperty.set(d);
    }

    public double getDuration() {
        return this.durationProperty.get();
    }

    @FXML
    void initialize() {
        assert eighthToggle != null : "fx:id=\"eighthToggle\" was not injected: check your FXML file 'DurationToggles.fxml'.";
        assert halfToggle != null : "fx:id=\"halfToggle\" was not injected: check your FXML file 'DurationToggles.fxml'.";
        assert quarterToggle != null : "fx:id=\"quarterToggle\" was not injected: check your FXML file 'DurationToggles.fxml'.";
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'DurationToggles.fxml'.";
        assert sixteenthToggle != null : "fx:id=\"sixteenthToggle\" was not injected: check your FXML file 'DurationToggles.fxml'.";
        assert thirtySecondToggle != null : "fx:id=\"thirtySecondToggle\" was not injected: check your FXML file 'DurationToggles.fxml'.";
        assert wholeToggle != null : "fx:id=\"wholeToggle\" was not injected: check your FXML file 'DurationToggles.fxml'.";

        double fontSize = 24d;
        Font font = Font.loadFont(
                DurationToggleController.class
                        .getResource("/fonts/Bravura.otf")
                        .toExternalForm(),
                fontSize);
        if (font == null) {
            throw new IllegalStateException("music font not found");
        }

        eighthToggle.setFont(font);
        eighthToggle.setText(
                SymbolFactory.note8thUp());
        
        halfToggle.setFont(font);
        halfToggle.setText(
                SymbolFactory.noteHalfUp());
        
        quarterToggle.setFont(font);
        quarterToggle.setText(
                SymbolFactory.noteQuarterUp());
        
        sixteenthToggle.setFont(font);
        sixteenthToggle.setText(
                SymbolFactory.note16thUp());
        
        thirtySecondToggle.setFont(font);
        thirtySecondToggle.setText(
                SymbolFactory.note32ndUp());
        
        wholeToggle.setFont(font);
        wholeToggle.setText(
                SymbolFactory.noteWhole());
    }

}
