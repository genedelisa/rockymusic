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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.model.InputStaffModel;
import com.rockhoppertech.music.fx.cmn.model.StaffSymbol;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDISender;

/**
 * A staff that will respond to mouse events. A single note is shown on a single
 * staff. The mouse will change the pitch of the note. Bind to the pitch
 * property.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class InputStaff extends Region {
    final static Logger logger = LoggerFactory.getLogger(InputStaff.class);
    private InputStaffModel staffModel;
    private MIDISender midiSender;

    /**
     * This one is used when specified in fxml.
     */
    public InputStaff() {
        this(new InputStaffModel());
    }

    public InputStaff(InputStaffModel staffModel) {
        getStyleClass().setAll("inputstaff-control");

        this.staffModel = staffModel;
        this.setCursor(Cursor.CROSSHAIR);
        this.setOpacity(1d);
        this.midiSender = new MIDISender();

        pitchProperty.bindBidirectional(staffModel.pitchProperty());

        // this is ignored
        this.setWidth(2300d);
        this.staffModel.setStaffWidth(2300d);
        this.setFontSize(48d);

        this.staffModel.staffWidthProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends Number> arg0,
                            Number arg1, Number newval) {
                        setMinWidth(newval.doubleValue());

                        if (newval.doubleValue() > getWidth()) {
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
        this.setStyle("-fx-background-color: antiquewhite; -fx-border-color: black; -fx-border-width: 1px;");
        this.addEventFilter(
                MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        staffMouseDragged(mouseEvent);
                    }
                });
        this.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        staffMousePressed(mouseEvent);
                    }
                });
        this.addEventFilter(
                MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        staffMouseReleased(mouseEvent);
                    }
                });

    }

    void staffMouseDragged(MouseEvent event) {
        logger.debug("dragged {}", event);

        int currentPitch = getStaffModel().getNote().getMidiNumber();
        this.midiSender.sendNoteOff(currentPitch);

        int pitch = this.getStaffModel().whichNote(event.getY());
        if (pitch < 0 || pitch > 127) {
            return;
        }

        if (pitch != currentPitch) {
            this.getStaffModel().setPitch(pitch);
            this.updateSymbol();
            this.draw();
            this.midiSender.sendNoteOn(pitch, 64);
        }
    }

    void staffMousePressed(MouseEvent event) {
        // staff.requestFocus();

        int pitch = this.getStaffModel().whichNote(event.getY());
        if (pitch < 0 || pitch > 127) {
            return;
        }

        int currentPitch = getStaffModel().getNote().getMidiNumber();
        if (pitch != currentPitch) {
            this.getStaffModel().setPitch(pitch);
            this.midiSender.sendNoteOn(pitch, 64);
            this.updateSymbol();
            this.draw();
        }
    }

    void staffMouseReleased(MouseEvent event) {
        this.draw();
        this.midiSender.sendNoteOff(getStaffModel().getNote().getMidiNumber());
    }

    MIDINote getNote() {
        return this.staffModel.getNote();
    }

    void updateSymbol() {
        this.staffModel.updateSymbol();
    }

    private IntegerProperty pitchProperty = new SimpleIntegerProperty(60);

    public IntegerProperty pitchProperty() {
        return pitchProperty;
    }

    public void setPitch(int p) {
        pitchProperty.set(p);
    }

    public int getPitch() {
        return pitchProperty.get();
    }

    /**
     * Removes all current shapes, gets the shapes from the model, then draws
     * them.
     */
    public void draw() {
        this.getChildren().clear();

        List<Shape> shapes = staffModel.getShapes();
        logger.debug("drawing shapes {}", shapes.size());
        this.getChildren().addAll(shapes);

        StaffSymbol symbol = this.staffModel.getStaffSymbol();
        this.getChildren().add(symbol);
        for (Text ledger : symbol.getLedgers()) {
            this.getChildren().add(ledger);
        }

    }

    public void setFontSize(double size) {
        this.getChildren().clear();
        this.getStaffModel().setFontSize(size);
        this.setHeight(size * 10d); // fontsize * 10
        this.setMinHeight(size * 10d); // fontsize * 10
        this.setPrefHeight(size * 10d); // fontsize * 10
        this.setPrefWidth(size * 6d);
    }

    /**
     * @param y
     *            the y location
     * @return which note is at y
     */
    public int whichNote(double y) {
        return getStaffModel().whichNote(y);
    }

    /**
     * @return the staffModel
     */
    public InputStaffModel getStaffModel() {
        return staffModel;
    }

    /**
     * @param staffModel
     *            the staffModel to set
     */
    public void setStaffModel(InputStaffModel staffModel) {
        this.staffModel = staffModel;
    }
}
