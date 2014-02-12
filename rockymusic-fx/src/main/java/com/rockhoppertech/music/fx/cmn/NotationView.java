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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.model.StaffModel;
import com.rockhoppertech.music.fx.cmn.model.StaffSymbol;
import com.rockhoppertech.music.fx.cmn.model.SymbolFactory;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.sun.javafx.tk.Toolkit;

/**
 * A drawing Canvas with a staff and symbols.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class NotationView {
    private static final Logger logger = LoggerFactory
            .getLogger(NotationView.class);

    private Canvas canvas;
    private Font font;
    private Pane canvasPane;
    private StaffModel staffModel;

    // private NotationController controller;

    public NotationView(StaffModel staffModel) {
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

        this.canvas = new Canvas(2300, 300);
        this.canvas.setCursor(Cursor.CROSSHAIR);
        this.canvas.setOpacity(1d);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(font);

        // this.canvasPane = new Pane();

        canvasPane = new AnchorPane();
        // canvas should stretch as anchorpane is resized
        AnchorPane.setTopAnchor(canvas, 10.0);
        AnchorPane.setLeftAnchor(canvas, 10.0);
        AnchorPane.setRightAnchor(canvas, 65.0);
        canvasPane.setPrefSize(1000, 200);

        // ScrollPane sp = new ScrollPane();
        // sp.setContent(this.canvas);
        // canvasPane.getChildren().add(canvas);
        // canvas.widthProperty().bind(canvasPane.widthProperty());
        // canvas.heightProperty().bind(canvasPane.heightProperty());

        drawStaff();

        // // test data
        // double yy = y;
        // // ascending scale
        // for (int i = 0; i < 12; i++, yy -= yspacing) {
        // gc.fillText(SymbolFactory.noteQuarterUp(), x += fontSize, yy);
        // }
        //
        // yy = staffModel.getYpositionForPitch(Pitch.D6, true);
        // gc.fillText(SymbolFactory.noteQuarterDown(), x += fontSize, yy);
        //
        // yy = staffModel.getYpositionForPitch(Pitch.D5, true);
        // gc.fillText(SymbolFactory.noteQuarterUp(), x += fontSize, yy);

    }

    private void drawStaff() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        double x = staffModel.getStartX();
        double y = staffModel.getStaffBottom();
        // draw the staff
        double yspacing = staffModel.getYSpacing();
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

        logger.debug("canvas width {}", canvas.getWidth());
        String staff = SymbolFactory.staff5Lines();
        double inc = staffModel.getFontSize() / 2d;
        double width = canvas.getWidth();
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

    public Pane getCanvasPane() {
        return this.canvasPane;
    }

    public void repaintCanvas() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);

        this.drawStaff();

        // gc.setStroke(Color.RED);
        // gc.setFill(Color.RED);

        List<StaffSymbol> symbols = staffModel.getSymbols();
        for (StaffSymbol symbol : symbols) {
            gc.fillText(symbol.getSymbol(), symbol.getX(), symbol.getY());
            // gc.strokeText(symbol.getSymbol(), symbol.getX(), symbol.getY());
            logger.debug(
                    "drawing symbol '{}' at x {} y {}",
                    symbol.getSymbol(),
                    symbol.getX(),
                    symbol.getY());
            // logger.debug("width {}", stringWidth(symbol.getSymbol()));
        }

        // gc.setFill(controller.getBackgroundColor());
        // gc.fillRect(0, 0, drawingCanvas.getWidth(),
        // drawingCanvas.getHeight());
        // for (Shape shape: shapeManager.getAll()) {
        // shapeRenderer.render(shape, gc);
        // }
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

}
