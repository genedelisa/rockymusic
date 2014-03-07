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

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.layout.Region;
import javafx.scene.shape.Shape;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.model.GrandStaffModel;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class GrandStaff extends Region {
    final static Logger logger = LoggerFactory.getLogger(GrandStaff.class);
    private GrandStaffModel staffModel;
    
    
//    public static void main(String[] args) {
//        GrandStaff gs= new GrandStaff();
//        gs.setFontSize(24d);
//        gs.drawShapes();
//    }

    /**
     * This one is used when specified in fxml.
     */
    public GrandStaff() {
        this(new GrandStaffModel());
    }

    public GrandStaff(GrandStaffModel staffModel) {
        getStyleClass().setAll("grandstaff-control");

        this.staffModel = staffModel;
        this.setCursor(Cursor.CROSSHAIR);
        this.setOpacity(1d);

        this.setHeight(300d);
        this.setWidth(2300d);
        this.staffModel.setStaffWidth(2300d);
        this.setPrefSize(2300d, 300d);

        this.staffModel.setFontSize(48d);
        this.setPrefHeight(480d); // fontsize * 10

        this.staffModel.staffWidthProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends Number> arg0,
                            Number arg1, Number newval) {
                        setMinWidth(newval.doubleValue());
                        
                        if(newval.doubleValue() > getWidth()) {
                            setPrefWidth(newval.doubleValue());
                            logger.debug(
                                    "new pref width from staff width {}",
                                    newval);
                        }
                        
                        logger.debug(
                                "staff width {}",
                                newval);
                    }
                });
    }

    public void setDrawTimeSignature(boolean drawTimeSignature) {
        this.staffModel.setDrawTimeSignature(drawTimeSignature);
    }

    public void setDrawBrace(boolean drawBrace) {
        this.staffModel.setDrawBrace(drawBrace);
    }

    public void setDrawClefs(boolean drawClefs) {
        this.staffModel.setDrawClefs(drawClefs);
    }

    public void setDrawKeySignature(boolean drawKeySignature) {
        this.staffModel.setDrawKeySignature(drawKeySignature);
    }

    /**
     * Removes all current shapes, gets the shapes from the model, then draws
     * them.
     */
    public void drawShapes() {
        this.getChildren().clear();

        List<Shape> shapes = staffModel.getShapes();
        logger.debug("drawing shapes {}", shapes.size());
        this.getChildren().addAll(shapes);
    }

    /**
     * @return the staffModel
     */
    public GrandStaffModel getStaffModel() {
        return staffModel;
    }

    /**
     * @param staffModel
     *            the staffModel to set
     */
    public void setStaffModel(GrandStaffModel staffModel) {
        this.staffModel = staffModel;
    }

    public void setTrack(MIDITrack track) {
        this.getStaffModel().setTrack(track);
    }

    public void setFontSize(double size) {
        this.getStaffModel().setFontSize(size);
        this.setPrefHeight(size * 10d); // fontsize * 10
    }

    /**
     * @param y
     *            the y location
     * @return which note is at y
     */
    public int whichNote(double y) {
        return getStaffModel().whichNote(y);
    }

    public void addNote(int pitch) {
        getStaffModel().addNote(pitch);
    }

    public MIDITrack getMIDITrack() {
        return getStaffModel().getTrackProperty().get();
    }
}
