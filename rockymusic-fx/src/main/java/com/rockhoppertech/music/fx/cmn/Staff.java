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
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.model.StaffModel;
import com.rockhoppertech.music.fx.cmn.model.StaffModel.Clef;
import com.rockhoppertech.music.fx.cmn.model.SymbolFactory;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * A drawing Region with a single staff and symbols.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class Staff extends Region {

    private static final Logger logger = LoggerFactory
            .getLogger(Staff.class);

    private Font font;
    private StaffModel staffModel;

    /**
     * Used by fxml. Creates an empty model.
     */
    public Staff() {
        this(new StaffModel());
    }

    public Staff(StaffModel staffModel) {
        getStyleClass().setAll("staff");

        this.staffModel = staffModel;
        this.font = this.staffModel.getFont();

        this.staffModel.getTrackProperty().addListener(
                new ChangeListener<MIDITrack>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends MIDITrack> observable,
                            MIDITrack oldValue, MIDITrack newValue) {
                        drawShapes();
                        logger.debug("staff model track changed. repainting");
                    }
                });

        this.setCursor(Cursor.CROSSHAIR);
        this.setOpacity(1d);
        this.setWidth(2300d);
        this.setHeight(300d);

        // this call doens't work here
        drawStaff();
    }

    private void drawStaff() {
        logger.debug("drawing the staff");
        double x = staffModel.getStartX();
        double y = staffModel.getStaffBottom();
        double yspacing = staffModel.getYSpacing();

        font = staffModel.getFont();

        // TODO this logic belongs elsewhere
        // draw the clef
        Text text = null;
        switch (staffModel.getClef()) {
        case TREBLE:
            text = new Text(x, y - (yspacing * 2d), SymbolFactory.gClef());
            break;
        case BASS:
            text = new Text(x, y - (yspacing * 6d), SymbolFactory.fClef());
            break;
        case ALTO:
            text = new Text(x, y - (yspacing * 4d), SymbolFactory.cClef());
            break;
        case BARITONE:
            // top line
            text = new Text(x, y - (yspacing * 8d), SymbolFactory.cClef());
            break;
        case MEZZO_SOPRANO:
            text = new Text(x, y - (yspacing * 2d), SymbolFactory.cClef());
            break;
        case SOPRANO:
            // bottom line
            text = new Text(x, y, SymbolFactory.cClef());
            break;
        case SUB_BASS:
            // top line
            text = new Text(x, y - (yspacing * 8d), SymbolFactory.fClef());
            break;
        case TENOR:
            text = new Text(x, y - (yspacing * 6d), SymbolFactory.cClef());
            break;
        default:
            text = new Text(x, y, "What clef?");
            break;
        }
        text.setFont(font);
        // text.setStyle("-fx-background-color: #CCFF99; -fx-stroke: black; -fx-font: 48px Bravura;");
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.autosize();
        this.getChildren().add(text);

        // TODO fill in the rest of the clefs

        // draw the staff
        logger.debug("canvas width {}", this.getWidth());
        String staff = SymbolFactory.staff5Lines();
        double inc = staffModel.getFontSize() / 2d;
        double width = this.getWidth();
        for (double xx = x; xx < width; xx += inc) {
            text = new Text(xx, y, staff);
            text.setFont(font);
            this.getChildren().add(text);
        }
    }

    /**
     * Removes all current shapes, gets the shapes from the model, then draws
     * them.
     */
    public void drawShapes() {
        this.getChildren().clear();
        // this.getManagedChildren().clear();

        drawStaff();
        List<Shape> shapes = staffModel.getShapes();
        logger.debug("drawing shapes {}", shapes.size());
        this.getChildren().addAll(shapes);
    }

    // @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("StaffControl.css").toExternalForm();
    }

    public StaffModel getStaffModel() {
        return staffModel;
    }
    
    public void setTrack(MIDITrack track) {
        this.getStaffModel().setTrack(track);
    }

    public void setFontSize(double size) {
        this.getStaffModel().setFontSize(size);
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

    public void setClef(Clef treble) {
        getStaffModel().setClef(Clef.TREBLE);
    }

    public void addNote(MIDINote inputNote) {
        getStaffModel().addNote(inputNote);
        
    }
}
