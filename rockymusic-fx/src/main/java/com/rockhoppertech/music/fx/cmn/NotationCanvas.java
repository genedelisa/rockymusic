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
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.model.StaffModel;
import com.rockhoppertech.music.fx.cmn.model.StaffSymbol;
import com.rockhoppertech.music.fx.cmn.model.SymbolFactory;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * A drawing Canvas with a staff and symbols.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class NotationCanvas extends Canvas {
    private static final Logger logger = LoggerFactory
            .getLogger(NotationCanvas.class);

    private Font font;
    private StaffModel staffModel;
    // private NotationController controller;

    public NotationCanvas(StaffModel staffModel) {
        this.staffModel = staffModel;
        this.font = this.staffModel.getFont();

        this.staffModel.getTrackProperty().addListener(
                new ChangeListener<MIDITrack>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends MIDITrack> observable,
                            MIDITrack oldValue, MIDITrack newValue) {
                        repaintCanvas();
                        logger.debug("staff model track changed. repainting");
                    }
                });

        this.setCursor(Cursor.CROSSHAIR);
        this.setOpacity(1d);
        this.setWidth(2300d);
        this.setHeight(300d);

        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());
        gc.setFill(Color.BLACK);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(font);

        drawStaff();
    }

    private void drawStaff() {
        GraphicsContext gc = this.getGraphicsContext2D();
        double x = staffModel.getStartX();
        double y = staffModel.getStaffBottom();
        double yspacing = staffModel.getYSpacing();

        // draw the clef
        switch (staffModel.getClef()) {
        case TREBLE:
            gc.fillText(SymbolFactory.gClef(), x, y - (yspacing * 2d));
            break;
        case BASS:
            gc.fillText(SymbolFactory.fClef(), x, y - (yspacing * 6d));
            break;
        case ALTO:
            gc.fillText(SymbolFactory.cClef(), x, y - (yspacing * 4d));
            break;
        case BARITONE:
            break;
        case MEZZO_SOPRANO:
            break;
        case SOPRANO:
            break;
        case SUB_BASS:
            break;
        case TENOR:
            break;
        default:
            break;
        }

        // draw the staff
        logger.debug("canvas width {}", this.getWidth());
        String staff = SymbolFactory.staff5Lines();
        double inc = staffModel.getFontSize() / 2d;
        double width = this.getWidth();
        for (double xx = x; xx < width; xx += inc) {
            gc.fillText(staff, xx, y);
        }

        // TODO why is this gray and not black?
        // gc.setLineWidth(SymbolFactory.getStaffLineThickness());
        // gc.setStroke(Color.BLACK);
        // gc.setFill(Color.BLACK);
        // gc.setLineCap(StrokeLineCap.ROUND);
        // gc.setLineJoin(StrokeLineJoin.ROUND);
        // for (int i = 0; i < 5; i++) {
        // double yy = y - i * staffModel.getLineInc();
        // gc.strokeLine(x, yy, 2500d, yy);
        // }
    }

    public void repaintCanvas() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        // gc.setFill(controller.getBackgroundColor());
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());
        gc.setFill(Color.BLACK);

        this.drawStaff();

        // gc.setStroke(Color.RED);
        // gc.setFill(Color.RED);

        List<StaffSymbol> symbols = staffModel.getSymbols();
        for (StaffSymbol symbol : symbols) {
            // maybe a note renderer?
            // noterenderer.render(symbol, gc);
            gc.fillText(symbol.getSymbol(), symbol.getX(), symbol.getY());
            // gc.strokeText(symbol.getSymbol(), symbol.getX(), symbol.getY());
            logger.debug(
                    "drawing symbol '{}' at x {} y {}",
                    symbol.getSymbol(),
                    symbol.getX(),
                    symbol.getY());
        }
    }
}
