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

import javafx.scene.Cursor;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.model.GrandStaffModel;
import com.rockhoppertech.music.fx.cmn.model.StaffModel;
import com.rockhoppertech.music.fx.cmn.model.SymbolFactory;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 *
 */
public class GrandStaff extends Region {
    final static Logger logger = LoggerFactory.getLogger(GrandStaff.class);
    private GrandStaffModel staffModel;

    /**
     * This one is used when specified in fxml.
     */
    public GrandStaff() {
        this(new GrandStaffModel());
    }
    
    public GrandStaff(GrandStaffModel staffModel) {
        getStyleClass().setAll("grandstaff-control");

        this.staffModel = staffModel;
        this.setCursor(Cursor.CROSSHAIR);
        this.setOpacity(1d);
        this.setWidth(2300d);
        this.setHeight(300d);
        this.setPrefSize(2300d, 300d);
    }

    /**
     * Removes all current shapes, gets the shapes from the model, then draws
     * them.
     */
    public void drawShapes() {
        this.getChildren().clear();
        drawStaves();
        List<Shape> shapes = staffModel.getShapes();
        logger.debug("drawing shapes {}", shapes.size());
        this.getChildren().addAll(shapes);
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
        brace.setScaleY(4d);
        brace.setScaleX(4d);
        brace.setFont(font);
        brace.setFontSmoothingType(FontSmoothingType.LCD);
        this.getChildren().add(brace);
        x += (4d*brace.getLayoutBounds().getWidth());
        
        Line barline = new Line(x, staffModel.getBassStaffBottom(),
                x, staffModel.getTrebleStaffBottom() - staffModel.getLineInc() * 4);
        this.getChildren().add(barline);
        x += (barline.getLayoutBounds().getWidth());
        
        y = staffModel.getTrebleStaffBottom();
        Text trebleClef = new Text(x, y - (yspacing * 2d),
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
        Text bassClef = new Text(x, y - (yspacing * 6d), SymbolFactory.fClef());
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

    /**
     * @return the staffModel
     */
    public GrandStaffModel getStaffModel() {
        return staffModel;
    }

    /**
     * @param staffModel the staffModel to set
     */
    public void setStaffModel(GrandStaffModel staffModel) {
        this.staffModel = staffModel;
    }
}
