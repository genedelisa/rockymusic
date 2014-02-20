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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.model.GrandStaffModel;
import com.rockhoppertech.music.fx.cmn.model.SymbolFactory;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.TimeSignature;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class MeasureCanvas extends Region {
    private final static Logger logger = LoggerFactory
            .getLogger(MeasureCanvas.class);

    private Measure measure;

    private boolean clefDrawn = false;
    private boolean timeSigDrawn = false;
    private boolean keySigDrawn = false;
    private double beatSpacing = 10d;
    private List<Rectangle> beatXlocations;
    private int currentInsertBeat = 1;
    private boolean beatsCreated = false;
    // draw lines where the beats are
    private boolean showBeats = true;
    // show every beat?
    private int showBeatModulus = 1;
    private double inputBeatQuantization = .125d;

    private List<Shape> shapes = new LinkedList<>();

    // private double x;
    // private double y;

    double quarterNoteWidth;

    private GrandStaffModel staffModel;

    public MeasureCanvas() {
        this.setWidth(500d);
        this.setHeight(200d);
        this.staffModel = new GrandStaffModel();
        this.staffModel.setFontSize(48d);
        this.quarterNoteWidth = this.staffModel.getQuarterNoteWidth();
        this.beatXlocations = new ArrayList<Rectangle>();

        // so we can see where things are. debugging
        this.setStyle("-fx-background-color: antiquewhite; -fx-border-color: black; -fx-border-width: 1px;");
    }

    // TODO fix this
    void createBeats(double x, int nbeats) {
        this.beatsCreated = true;
        if (this.beatXlocations == null)
            this.beatXlocations = new LinkedList<Rectangle>();

        double height = this.getLayoutBounds().getWidth();

        for (int i = 0; i < nbeats; i++) {
            Rectangle r = new Rectangle(x, 0d, this.beatSpacing,
                    height);
            this.beatXlocations.add(r);
            x += this.beatSpacing;
        }
    }

    double getXofBeatN(double beat) {
        if (beat < 0 || beat > this.beatXlocations.size())
            throw new IllegalArgumentException("bad beat " + beat);

        int beatIndex = (int) Math.floor(beat);
        Rectangle r = (Rectangle) this.beatXlocations.get(beatIndex);

        double x = r.getX();
        double mant = beat - Math.floor(beat);
        if (mant != 0d) {
            mant = this.quantize(mant, inputBeatQuantization);
            x += (mant * this.beatSpacing);
        }
        return x;
    }

    double quantize(double value, double q) {
        return Math.floor(value / q) * q;
    }

    /**
     * @return the measure
     */
    public Measure getMeasure() {
        return measure;
    }

    /**
     * @param measure
     *            the measure to set
     */
    public void setMeasure(Measure measure) {
        this.measure = measure;
        this.staffModel.setTrack(measure.gettrack());
        this.beatXlocations.clear();

        TimeSignature t = this.measure.getTimeSignature();

        // set up beat widths. create a Rectangle for each beat.
        
        double x = this.staffModel.getFirstNoteX();
        //double y = this.staffModel.getBassStaffBottom();
        double y = this.staffModel.getTrebleStaffTop();
        double height = this.staffModel.getBassStaffBottom()
                - this.staffModel.getTrebleStaffTop();
        
        // double lastBeat = 0;
        for (double bbeat = 1d; bbeat <= t.getNumerator(); bbeat += 1d) {
            logger.debug("looping beat {}", bbeat);
            // KeySignature ks = mm.getKeySignatureAtBeat(bbeat);
            MIDITrack nlAtBeat = this.measure.getNotesAtBeat(bbeat);

            // determine width of this beat
            double beatWidth = (nlAtBeat.size() + 1d) * this.quarterNoteWidth;
            Rectangle rect = new Rectangle(x, y, beatWidth, height);
            rect.setFill(Color.web("blue", 0.1));
            rect.setStroke(Color.RED);
            logger.debug(
                    "rectangle x {} y {} w {} h {}",
                    rect.getX(),
                    rect.getY(),
                    rect.getWidth(),
                    rect.getHeight());
            this.beatXlocations.add(rect);
            this.getChildren().add(rect);
            x += beatWidth;

            if (nlAtBeat != null && nlAtBeat.size() != 0) {
                double shortest = nlAtBeat.getShortestDuration();
            }
        }
    }

    /**
     * @return the showBeats
     */
    public boolean isShowBeats() {
        return showBeats;
    }

    /**
     * @param showBeats
     *            the showBeats to set
     */
    public void setShowBeats(boolean showBeats) {
        this.showBeats = showBeats;
    }

    public int whichNote(double y) {
        return this.staffModel.whichNote(y);
    }

    public void addNote(int pitch) {
        this.staffModel.addNote(pitch);
    }

    public void setFontSize(double size) {

        this.staffModel.setFontSize(size);
        this.quarterNoteWidth = this.staffModel.getQuarterNoteWidth();

    }

    public void drawShapes() {
        this.getChildren().clear();
        drawStaves();
        List<Shape> shapes = staffModel.getShapes();
        logger.debug("drawing shapes {}", shapes.size());
        this.getChildren().addAll(shapes);
        this.getChildren().addAll(beatXlocations);
        
//        for (Rectangle r : beatXlocations) {
//
//        }
//        shapes = this.staffModel.getShapes();
//        for (Shape s : shapes) {
//
//        }

    }
    private void drawStaves() {
        logger.debug("drawing the staves");
        double x = staffModel.getStartX();
        double y = staffModel.getStaffBottom();
        double yspacing = staffModel.getYSpacing();
        Font font = staffModel.getFont();

        y = staffModel.getBassStaffBottom();
        Text brace = new Text(x, y,
                SymbolFactory.brace());
        brace.setScaleY(2.8);
        brace.setScaleX(3d);
        brace.setFont(font);
        brace.setFontSmoothingType(FontSmoothingType.LCD);
        this.getChildren().add(brace);
        x += (4d * brace.getLayoutBounds().getWidth());

        Line barline = new Line(x, staffModel.getBassStaffBottom(),
                x, staffModel.getTrebleStaffBottom() - staffModel.getLineInc()
                        * 4);
        this.getChildren().add(barline);
        x += (barline.getLayoutBounds().getWidth());

        // just some spacing
        // x += staffModel.getFontSize();
        y = staffModel.getTrebleStaffBottom();
        // double clefX = x+10d;
        double clefX = x + staffModel.getFontSize() / 4d;
        Text trebleClef = new Text(clefX, y - (yspacing * 2d),
                SymbolFactory.gClef());
        trebleClef.setFont(font);
        trebleClef.setFontSmoothingType(FontSmoothingType.LCD);
        this.getChildren().add(trebleClef);

        logger.debug("canvas width {}", this.getWidth());
        String staff = SymbolFactory.staff5Lines();
        double staffStringIncrement = staffModel.getFontSize() / 2d;
        double width = this.getWidth();

        Text text;

        // draw the treble staff
        for (double xx = x; xx < width; xx += staffStringIncrement) {
            text = new Text(xx, y, staff);
            text.setFont(font);
            this.getChildren().add(text);
        }

        y = staffModel.getBassStaffBottom();
        Text bassClef = new Text(clefX, y - (yspacing * 6d),
                SymbolFactory.fClef());
        bassClef.setFont(font);
        bassClef.setFontSmoothingType(FontSmoothingType.LCD);
        this.getChildren().add(bassClef);

        // draw the bass staff
        for (double xx = x; xx < width; xx += staffStringIncrement) {
            text = new Text(xx, y, staff);
            text.setFont(font);
            this.getChildren().add(text);
        }
    }
}
