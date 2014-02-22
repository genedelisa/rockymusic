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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.model.MeasureModel;
import com.rockhoppertech.music.fx.cmn.model.StaffSymbol;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class MeasureCanvas extends Region {
    private final static Logger logger = LoggerFactory
            .getLogger(MeasureCanvas.class);

    private Measure measure;
    private MeasureModel model;

    // draw rectangles where the beats are
    private boolean showBeats = true;

    public MeasureCanvas() {
        this.setWidth(800d);
        this.setHeight(200d);
        this.model = new MeasureModel();
        this.model.setFontSize(48d);
        this.model.setStaffWidth(this.getWidth() - 10d);

        // so we can see where things are. debugging
        this.setStyle("-fx-background-color: antiquewhite; -fx-border-color: black; -fx-border-width: 1px;");
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
        this.model.setMeasure(measure);
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
        this.model.setShowBeats(showBeats);
    }

    public int whichNote(double y) {
        return this.model.whichNote(y);
    }

    public void addNote(int pitch) {
        this.model.addNote(pitch);
    }

    public void setFontSize(double size) {
        this.model.setFontSize(size);
    }

    public void drawShapes() {
        this.getChildren().clear();
        // double width = this.getWidth();
        // this.model.setStaffWidth(this.getWidth());

        List<Shape> shapes = model.getShapes();
        logger.debug("drawing shapes {}", shapes.size());
        // staves, clefs, time sig
        this.getChildren().addAll(shapes);

        List<StaffSymbol> symbols = model.getSymbols();
        this.getChildren().addAll(symbols);
        for (StaffSymbol s : symbols) {
            Line stem = s.getStem();
            if (stem != null) {
                this.getChildren().add(stem);
            }
            for (Text ledger : s.getLedgers()) {
                this.getChildren().add(ledger);
            }
            QuadCurve tie = s.getTie();
            if (tie != null) {
                this.getChildren().add(tie);
            }
            Text flag = s.getFlag();
            if (flag != null) {
                this.getChildren().add(flag);
            }
        }
    }

    /**
     * @return the drawBeatRectangles
     */
    public boolean isDrawBeatRectangles() {
        return this.model.isDrawBeatRectangles();
    }

    /**
     * @param drawBeatRectangles
     *            the drawBeatRectangles to set
     */
    public void setDrawBeatRectangles(boolean drawBeatRectangles) {
        this.model.setDrawBeatRectangles(drawBeatRectangles);
        this.drawShapes();
    }

    /**
     * @return the drawTimeSignature
     */
    public boolean isDrawTimeSignature() {
        return this.model.isDrawTimeSignature();
    }

    /**
     * @param drawTimeSignature
     *            the drawTimeSignature to set
     */
    public void setDrawTimeSignature(boolean drawTimeSignature) {
        this.model.setDrawTimeSignature(drawTimeSignature);
        this.drawShapes();
    }

    /**
     * @param drawClefs
     *            the drawClefs to set
     */
    public void setDrawClefs(boolean drawClefs) {
        this.model.setDrawClefs(drawClefs);
        this.drawShapes();
    }

    /**
     * @return the drawKeySignature
     */
    public boolean isDrawClefs() {
        return this.model.isDrawClefs();
    }

    /**
     * @return the drawKeySignature
     */
    public boolean isDrawKeySignature() {
        return this.model.isDrawKeySignature();
    }

    /**
     * @param drawKeySignature
     *            the drawKeySignature to set
     */
    public void setDrawKeySignature(boolean drawKeySignature) {
        this.model.setDrawKeySignature(drawKeySignature);
        this.drawShapes();
    }

    BooleanProperty drawBeatsProperty = new SimpleBooleanProperty();

    public BooleanProperty drawBeatsProperty() {
        return drawBeatsProperty;
    }

    public void setDrawBraces(boolean selected) {
        this.model.setDrawBraces(selected);
        this.drawShapes();
    }
}
