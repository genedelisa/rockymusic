/**
 * 
 */
package com.rockhoppertech.music.fx.cmn.model;

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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDINote;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

/**
 * A thing that is drawn on a staff. Might be a note, clef, etc.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class StaffSymbol extends Text {
    private static final Logger logger = LoggerFactory
            .getLogger(StaffSymbol.class);

    // or just separate Text objects?

    private String symbol;
    private String accidental;
    private String augmentationDots;
    private Line stem;
    private QuadCurve tie;

    private List<Text> ledgers;

    private boolean stemmed;

    private boolean tied;
    private MIDINote note;

    // private List<Character> articulation;

    /**
     * @return the stemmed
     */
    public boolean isStemmed() {
        return this.stemmed;
    }

    /**
     * @param stemmed
     *            the stemmed to set
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
     * @param tied
     *            the tied to set
     */
    public void setTied(boolean tied) {
        this.tied = tied;
    }

    public StaffSymbol(double x, double y, String symbol) {
        this();
        this.setX(x);
        this.setX(y);
        this.symbol = symbol;
    }

    protected void updateLedgers(double newx) {
        
        for(Text t: this.ledgers) {
            double lx = newx - (t.getLayoutBounds().getWidth() / 4d);
            logger.debug("updating ledgers {}", lx);
            t.setX(lx);
        }
    }

    public StaffSymbol() {
        this.ledgers = new ArrayList<>();
        this.setStyle("-fx-cursor: hand; -fx-font-smoothing-type: lcd;");
        this.setFontSmoothingType(FontSmoothingType.LCD);
        this.setManaged(false);
        this.xProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                updateLedgers(newValue.doubleValue());
            }
        });
    }

    public void addLedger(Text ledger) {
        this.ledgers.add(ledger);
    }

    public double getWidth() {
        double width = this.getLayoutBounds().getWidth() * 1d;
        return width;
    }
    
    /**
     * @return the symbol
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * @param symbol
     *            the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
        this.updateText();
    }
    
    private void updateText() {
        StringBuilder sb = new StringBuilder();
        
        if(this.accidental != null) {
            sb.append(this.accidental);
        }
        if(this.symbol != null) {
            sb.append(this.symbol);
        }
        if(this.augmentationDots != null) {
            sb.append(this.augmentationDots);
        }
        
        // some spacing at the end
        sb.append(" ");
        this.setText(sb.toString());
        
    }

    /**
     * @return the stem
     */
    public Line getStem() {
        return this.stem;
    }

    /**
     * @param stem
     *            the stem to set
     */
    public void setStem(Line stem) {
        this.stem = stem;
    }

    /**
     * @return the accidental
     */
    public String getAccidental() {
        return accidental;
    }

    /**
     * @param accidental
     *            the accidental to set
     */
    public void setAccidental(String accidental) {
        this.accidental = accidental;
        this.updateText();
    }

    /**
     * @return the augmentationDots
     */
    public String getAugmentationDots() {
        return augmentationDots;
    }

    /**
     * @param augmentationDots
     *            the augmentationDots to set
     */
    public void setAugmentationDots(String augmentationDots) {
        this.augmentationDots = augmentationDots;
        this.updateText();
    }

    /**
     * @return the tie
     */
    public QuadCurve getTie() {
        return tie;
    }

    /**
     * @param tie
     *            the tie to set
     */
    public void setTie(QuadCurve tie) {
        this.tie = tie;
    }

    /**
     * @return the ledgers
     */
    public List<Text> getLedgers() {
        return ledgers;
    }

    /**
     * @param ledgers
     *            the ledgers to set
     */
    public void setLedgers(List<Text> ledgers) {
        this.ledgers = ledgers;
    }

    public void setMIDINote(MIDINote note) {
        this.note = note;
    }

    public MIDINote getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(MIDINote note) {
        this.note = note;
    }

    private Text flag;
    public void setFlag(Text flag) {
        this.flag = flag;
    }
    public Text getFlag() {
        return flag;
    }
}
