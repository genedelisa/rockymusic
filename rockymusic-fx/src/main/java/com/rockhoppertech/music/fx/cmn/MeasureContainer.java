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
import java.util.NavigableMap;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class MeasureContainer extends HBox {
    final static Logger logger = LoggerFactory
            .getLogger(MeasureContainer.class);

    /**
     * The actual data.
     */
    private MIDITrack track;

    private MeasureCanvas selectedMeasure;

    private BooleanProperty drawKeysignatureProperty = new SimpleBooleanProperty(
            true);
    private BooleanProperty drawTimeSignatureProperty = new SimpleBooleanProperty(
            true);
    private BooleanProperty drawClefsProperty = new SimpleBooleanProperty(true);
    private BooleanProperty drawBracesProperty = new SimpleBooleanProperty(true);
    private BooleanProperty drawBeatsProperty = new SimpleBooleanProperty(true);

    private ChangeListener<? super Boolean> selectedMeasureListener;

    public MeasureContainer() {
        // setManaged(true);

        this.setWidth(1800d);
        this.setHeight(480d);
        this.setPrefHeight(480d); // fontsize * 10
        this.setPrefWidth(1800d);
        this.setStyle("-fx-background-color: aliceblue; -fx-border-color: black; -fx-border-width: 1px;");

        this.drawBeatsProperty().addListener(
                new ChangeListener<Boolean>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends Boolean> arg0,
                            Boolean arg1, Boolean arg2) {
                        logger.debug("draw beats changed {}", arg2);
                        if (arg2) {
                            for (Node n : getChildren()) {
                                MeasureCanvas c = (MeasureCanvas) n;
                                c.setDrawBeatRectangles(true);
                                // c.drawShapes();
                            }
                        }
                    }
                });

        this.selectedMeasureListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observable,
                    Boolean oldValue, Boolean newValue) {
                for (Node c : getChildren()) {
                    MeasureCanvas mc = (MeasureCanvas) c;
                    if (mc.isSelected()) {
                        selectedMeasure = mc;
                        logger.debug("selected measure {}", selectedMeasure);
                        break;
                    }
                }
            }
        };
    }

    public void setMeasures(List<MeasureCanvas> c) {
        this.getChildren().addAll(c);
    }

    /**
     * Set the track, create {@code Measures} with {@code MeasureCanvas}es, and
     * add them as children.
     * 
     * @param track
     *            a {@code MIDITrack}
     */
    public void setTrack(MIDITrack track) {
        this.track = track;

        NavigableMap<Double, Measure> measures = Measure
                .createMeasures(this.track);
        logger.debug("measures keyset {}", measures.keySet());

        this.getChildren().clear();
        this.getChildren().removeAll(getManagedChildren());
        for (Measure m : measures.values()) {
            logger.debug("measure  {}", m);
            MeasureCanvas mc = new MeasureCanvas();
            mc.setFontSize(48d);
            mc.setId("mc-" + m.getStartBeat());
            mc.setMeasure(m);
            mc.setManaged(true);
            HBox.setHgrow(mc, Priority.ALWAYS);
            mc.selectedProperty().addListener(selectedMeasureListener);
            mc.drawBeatsProperty().bindBidirectional(this.drawBeatsProperty());
            this.getChildren().add(mc);
        }
        MeasureCanvas firstMeasure = (MeasureCanvas) this.getChildren().get(0);
        firstMeasure.setDrawClefs(true);
        firstMeasure.setDrawKeySignature(true);
        firstMeasure.setDrawTimeSignature(true);

        // TODO the width of the staff gets messed up with this
        // firstMeasure.setDrawBraces(true);
    }

    public void setFontSize(double d) {
        this.setPrefHeight(d * 10d); // fontsize * 10
        for (Node n : this.getChildren()) {
            MeasureCanvas c = (MeasureCanvas) n;
            c.setFontSize(d);
        }
    }

    public void draw() {
        for (Node n : this.getChildren()) {
            MeasureCanvas c = (MeasureCanvas) n;
            c.drawShapes();
        }
    }

    public Property<Boolean> drawKeysignatureProperty() {
        return this.drawKeysignatureProperty;
    }

    public Property<Boolean> drawBracesProperty() {
        return this.drawBracesProperty;
    }

    public Property<Boolean> drawTimeSignatureProperty() {
        return drawTimeSignatureProperty;
    }

    public Property<Boolean> drawBeatsProperty() {
        return drawBeatsProperty;
    }

    public Property<Boolean> drawClefsProperty() {
        return drawClefsProperty;
    }

    public boolean getDrawKeysignature() {
        return this.drawKeysignatureProperty.get();
    }

    public boolean getDrawBraces() {
        return this.drawBracesProperty.get();
    }

    public boolean getDrawTimeSignature() {
        return drawTimeSignatureProperty.get();
    }

    public boolean getDrawBeats() {
        return drawBeatsProperty.get();
    }

    public boolean getDrawClefs() {
        return drawClefsProperty.get();
    }

    public void setDrawBraces(boolean selected) {
        this.drawBracesProperty.set(selected);
    }

    public void setDrawBeatRectangles(boolean selected) {
        this.drawBeatsProperty.set(selected);
        this.selectedMeasure.getModel().setDrawBeatRectangles(selected);
//        for (Node n : this.getChildren()) {
//            MeasureCanvas c = (MeasureCanvas) n;
//            c.setDrawBeatRectangles(selected);
//            c.drawShapes();
//        }
    }

    public void setDrawKeySignature(boolean selected) {
        this.drawKeysignatureProperty.set(selected);
    }

    public void setDrawClefs(boolean selected) {
        this.drawClefsProperty.set(selected);
    }

    public void setDrawTimeSignature(boolean selected) {
        this.drawTimeSignatureProperty.set(selected);
    }

    public int whichNote(double y) {
        // TODO Auto-generated method stub
        return 0;
    }

}
