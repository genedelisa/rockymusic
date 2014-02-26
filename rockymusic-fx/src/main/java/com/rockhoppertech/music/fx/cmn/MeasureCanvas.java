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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
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
    private BooleanProperty drawBeatsProperty = new SimpleBooleanProperty(false);
    private BooleanProperty selectedProperty = new SimpleBooleanProperty(false);

    private BooleanProperty drawKeysignatureProperty = new SimpleBooleanProperty(
            true);
    private BooleanProperty drawTimeSignatureProperty = new SimpleBooleanProperty(
            true);
    private BooleanProperty drawClefsProperty = new SimpleBooleanProperty(true);
    private BooleanProperty drawBracesProperty = new SimpleBooleanProperty(true);

    
    // draw rectangles where the beats are
   // private boolean showBeats = true;

    public MeasureCanvas() {
        this.setWidth(1800d);
        this.setHeight(480d);
        this.setPrefHeight(480d); // fontsize * 10
        this.setPrefWidth(1800d);
        this.model = new MeasureModel();
        this.model.setFontSize(48d);
        this.model.setStaffWidth(this.getWidth() - 10d);

        this.model
                .staffWidthProperty().addListener(
                        new ChangeListener<Number>() {
                            @Override
                            public void changed(
                                    ObservableValue<? extends Number> arg0,
                                    Number arg1, Number newval) {
                                setWidth(newval.doubleValue());
                                setPrefWidth(newval.doubleValue());
                                // requestLayout();
                                logger.debug("new staff width {}", newval);
                            }
                        });
        
//        private BooleanProperty drawBeatsProperty = new SimpleBooleanProperty(true);

        this.drawBeatsProperty.bindBidirectional(this.model.drawBeatRectanglesProperty());
        
        this.model.drawBeatRectanglesProperty().addListener(
                new ChangeListener<Boolean>() {

                    @Override
                    public void changed(
                            ObservableValue<? extends Boolean> arg0,
                            Boolean arg1, Boolean arg2) {
                        logger.debug("draw beats changed {}", arg2);
                        drawShapes();
                    }
                });

        // so we can see where things are. debugging
        this.setStyle("-fx-background-color: antiquewhite; -fx-border-color: black; -fx-border-width: 1px;");

        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                selectedProperty.set(!selectedProperty.get());
                if(selectedProperty.get()) {
                    setSelectedStyle();
                } else {
                    setNormalStyle();
                }

                logger.debug("Mouse pressed at x {} y {}", me.getX(), me.getY());
                double beat = model.getBeatForX(me.getX());
                logger.debug("beat {}", beat);
                logger.debug(
                        "beat in measure {}",
                        model.getBeatInMeasureForX(me.getX()));
                logger.debug(
                        "pitch {}",
                        model.whichNote(me.getY()));
            }
        });

    }
    public void setNormalStyle() {
        this.setStyle("-fx-background-color: antiquewhite; -fx-border-color: black; -fx-border-width: 1px;");
    }
    
    public void setSelectedStyle() {
        this.setStyle("-fx-background-color: antiquewhite; -fx-border-color: red; -fx-border-width: 2px;");
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

     public int whichNote(double y) {
        return this.model.whichNote(y);
    }

    public void addNote(int pitch) {
        this.model.addNote(pitch);
    }

    public void setFontSize(double size) {
        this.model.setFontSize(size);
        this.setPrefHeight(size * 10d);
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
        // this.drawShapes();
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
        // this.drawShapes();
    }

    /**
     * @param drawClefs
     *            the drawClefs to set
     */
    public void setDrawClefs(boolean drawClefs) {
        this.model.setDrawClefs(drawClefs);
        // this.drawShapes();
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
        // this.drawShapes();
    }

    public void setDrawBraces(boolean selected) {
        this.model.setDrawBraces(selected);
        // this.drawShapes();
    }

    
    public BooleanProperty selectedProperty() {
        return selectedProperty;
    }
    public boolean isSelected() {
        return selectedProperty.get();
    }
    
    public BooleanProperty drawKeysignatureProperty() {
        return drawKeysignatureProperty;
    }
    public BooleanProperty drawTimeSignatureProperty() {
        return drawTimeSignatureProperty;
    }
    public BooleanProperty drawClefsProperty() {
        return drawClefsProperty;
    }
    public BooleanProperty drawBracesProperty() {
        return drawBracesProperty;
    }
    
    public BooleanProperty drawBeatsProperty() {
        return drawBeatsProperty;
    }

    /**
     * @return the drawBeatsProperty
     */
    public boolean getDrawBeats() {
        return drawBeatsProperty.get();
    }

    /**
     * @param drawBeatsProperty
     *            the drawBeatsProperty to set
     */
    public void setDrawBeats(boolean drawBeats) {
        this.drawBeatsProperty.set(drawBeats);
       // if(drawBeats) this.model.setShowBeats(drawBeats);
    }
    /**
     * @return the model
     */
    public MeasureModel getModel() {
        return model;
    }
    /**
     * @param model the model to set
     */
    public void setModel(MeasureModel model) {
        this.model = model;
    }
}
