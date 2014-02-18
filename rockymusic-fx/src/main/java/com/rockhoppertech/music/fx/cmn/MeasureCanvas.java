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

import java.util.LinkedList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.model.StaffModel;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.TimeSignature;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class MeasureCanvas extends Canvas {
    private final static Logger logger = LoggerFactory.getLogger(MeasureCanvas.class);

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

    //private double x;
    //private double y;
    
    private StaffModel staffModel;

    public MeasureCanvas() {

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

        TimeSignature t = this.measure.getTimeSignature();

        double lastBeat = 0;
        for (double bbeat = 1d; bbeat <= t.getNumerator(); bbeat += 1d) {
            logger.debug("looping beat {}", bbeat);
            // KeySignature ks = mm.getKeySignatureAtBeat(bbeat);
            MIDITrack nlAtBeat = this.measure.getNotesAtBeat(bbeat);
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
}
