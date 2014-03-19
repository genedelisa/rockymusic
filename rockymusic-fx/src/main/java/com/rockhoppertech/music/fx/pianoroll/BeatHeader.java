package com.rockhoppertech.music.fx.pianoroll;

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

import javafx.geometry.Orientation;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class BeatHeader extends Region {

    private static final Logger logger = LoggerFactory
            .getLogger(BeatHeader.class);

    private PianorollPane pianorollPane;

    public BeatHeader() {
        getStyleClass().setAll("beatheader-control");
        this.setStyle("-fx-background-color: gray;");
        // this.setWidth(this.beatWidth * 64d);
        // this.setPrefWidth(this.beatWidth * 64d);
        this.setHeight(40);
        this.setPrefHeight(40);
        this.setWidth(800d);
        this.orientation = Orientation.HORIZONTAL;
        // this.units = 60d;
        // this.increment = units / 2;
    }

    /**
     * @return the pianorollPane
     */
    public PianorollPane getPianorollPane() {
        return pianorollPane;
    }

    /**
     * @param pianorollPane
     *            the pianorollPane to set
     */
    public void setPianorollPane(PianorollPane pianorollPane) {
        this.pianorollPane = pianorollPane;
        this.setLayoutX(pianorollPane.getLayoutX());
        // this.setPrefWidth(this.pianorollPane.getPrefWidth());
        this.setPrefWidth(this.pianorollPane.getWidth());
        this.prefWidthProperty().bindBidirectional(
                this.pianorollPane.prefWidthProperty());

        // this.layoutXProperty().bindBidirectional(
        // this.pianorollPane.layoutXProperty());

        this.units = pianorollPane.getBeatWidth();
        this.increment = units / 2d;

        this.drawLines();
        // this.drawSnapGrid();
    }

    void drawSnapGrid() {

        final double width = this.getLayoutBounds().getWidth();
        final double height = this.getLayoutBounds().getHeight();
        logger.debug("creating snapgrid {}", width);

        final double yoffset = 0d;
        for (double x = pianorollPane.getSnapX(); x < width; x += pianorollPane
                .getSnapX()) {
            Line line = new Line(x,
                    yoffset,
                    x,
                    height);
            line.setStroke(Color.RED);
            logger.debug("created line {}", line.getLayoutX());
            this.getChildren().add(line);
        }
    }

    Orientation orientation;
    public static final double SIZE = 35;
    private double increment;
    private double units;

    public void drawLines() {

        // some vars we need
        double end = 0;
        double start = 0;
        double tickLength = 0;
        String text = null;

        // use clipping bounds to calculate first tick and last tick location
        if (orientation == Orientation.HORIZONTAL) {
            start = (this.getLayoutX() / increment) * increment;
            end = (((this.getLayoutX() + getLayoutBounds().getWidth()) / increment) + 1)
                    * increment;
        } else {
            start = (this.getLayoutY() / increment) * increment;
            end = (((this.getLayoutY() + getLayoutBounds().getHeight()) / increment) + 1)
                    * increment;
        }
        logger.debug("lines start {} end {}", start, end);

        // Make a special case of 0 to display the number
        // within the rule and draw a units label
        if (start == 0) {
            // text = Integer.toString(0) + (isMetric ? " cm" : " in");
            tickLength = 10;
            if (orientation == Orientation.HORIZONTAL) {
                Line line = new Line(0,
                        SIZE - 1,
                        0,
                        SIZE - tickLength - 1);
                line.setStroke(new Color(1, 1, 1, 1));
                this.getChildren().add(line);
                // g.drawString(text, 2, 21);
            } else {
                Line line = new Line(SIZE - 1,
                        0,
                        SIZE - tickLength - 1,
                        0);
                // g.drawString(text, 9, 10);
                this.getChildren().add(line);
            }
            text = null;
            start = increment;
        }

        // ticks and labels
        for (double i = start; i < end; i += increment) {
            // FIXME some rounding error here
            // logger.debug("mod " + (i % units));
            if (i % units == 0) {
                tickLength = 10;
                // beats are 1 based
                text = String.format("%3.0f", (i / units) + 1);
                logger.debug("header text {}", text);
            } else {
                // logger.debug("null text ");
                tickLength = 7;
                text = null;
            }

            if (tickLength != 0) {
                if (orientation == Orientation.HORIZONTAL) {
                    Line line = new Line(i,
                            SIZE - 1,
                            i,
                            SIZE - tickLength - 1);
                    this.getChildren().add(line);
                    if (text != null) {
                        Text txt = new Text(text);
                        txt.setLayoutX(i - 3);
                        txt.setLayoutY(21);
                        this.getChildren().add(txt);
                    }
                } else {
                    Line line = new Line(SIZE - 1,
                            i,
                            SIZE - tickLength - 1,
                            i);
                    this.getChildren().add(line);
                    if (text != null) {
                        Text txt = new Text(text);
                        txt.setLayoutX(9);
                        txt.setLayoutY(i + 3);
                        this.getChildren().add(txt);
                    }
                }
            }
        }
    }

}
