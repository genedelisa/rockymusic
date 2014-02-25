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
    private BooleanProperty drawKeyignatureProperty = new SimpleBooleanProperty(
            true);
    private BooleanProperty drawTimeSignatureProperty = new SimpleBooleanProperty(
            true);
    private BooleanProperty drawClefsProperty = new SimpleBooleanProperty(true);
    private BooleanProperty drawBracesProperty = new SimpleBooleanProperty(true);
    private BooleanProperty drawBeatsProperty = new SimpleBooleanProperty(true);

    public MeasureContainer() {
       // setManaged(true);
        
        this.setWidth(1800d);
        this.setHeight(480d);
        this.setPrefHeight(480d); // fontsize * 10
        this.setPrefWidth(1800d);
        
        this.setStyle("-fx-background-color: aliceblue; -fx-border-color: black; -fx-border-width: 1px;");

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
            // mc.drawShapes();
            mc.setManaged(true);
            HBox.setHgrow(mc, Priority.ALWAYS);
            this.getChildren().add(mc);
        }
        MeasureCanvas firstMeasure = (MeasureCanvas) this.getChildren().get(0);
        firstMeasure.setDrawClefs(true);
        firstMeasure.setDrawKeySignature(true);
        firstMeasure.setDrawTimeSignature(true);
       // firstMeasure.setDrawBraces(true);
        
      //  this.layout();
//        this.requestLayout();
        //this.setNeedsLayout(true);

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

    public void setDrawBraces(boolean selected) {
        // TODO Auto-generated method stub

    }

    public Property<Boolean> drawBeatsProperty() {
        return drawBeatsProperty;
    }

    public void setDrawBeatRectangles(boolean selected) {
        // TODO Auto-generated method stub

    }

    public void setDrawKeySignature(boolean selected) {
        // TODO Auto-generated method stub

    }

    public void setDrawClefs(boolean selected) {
        // TODO Auto-generated method stub

    }

    public void setDrawTimeSignature(boolean selected) {
        // TODO Auto-generated method stub

    }

    public int whichNote(double y) {
        // TODO Auto-generated method stub
        return 0;
    }

}
