/**
 * 
 */
package com.rockhoppertech.music.fx.musicfont.model;

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


import javafx.scene.shape.Line;

/**
 * A thing that is drawn on a staff. Might be a note, clef, etc.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 *
 */
public class StaffSymbol {

    private double x;
    private double y;
    private String symbol;
    private boolean stemmed;
    private Line stem;
    private boolean tied;
    //private FontMetrics fontMetrics;
    //private List<Character> articulation;

    /**
     * @return the stemmed
     */
    public boolean isStemmed() {
        return this.stemmed;
    }

    /**
     * @param stemmed the stemmed to set
     */
    public void setStemmed(boolean stemmed) {
        this.stemmed = stemmed;
    }

    /**
     * @return the tied
     */
    public boolean isTied() {
        return this.tied;
    }

    /**
     * @param tied the tied to set
     */
    public void setTied(boolean tied) {
        this.tied = tied;
    }

    public StaffSymbol(double x, double y, String symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    /**
     * @return the x
     */
    public double getX() {
        return this.x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return this.y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        //TODO update when font metrics are available
        double width = 48d;
//        if (fontMetrics != null) {
//            width += fontMetrics.charWidth(this.symbol);
//            if (this.accidental != Character.MIN_VALUE) {
//                width += fontMetrics.charWidth(this.accidental);
//            }
//        }
        return width;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the stem
     */
    public Line getStem() {
        return this.stem;
    }

    /**
     * @param stem the stem to set
     */
    public void setStem(Line stem) {
        this.stem = stem;
    }
}