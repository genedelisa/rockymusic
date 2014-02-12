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
import java.util.Locale;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.fx.cmn.NotationView;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * The things that are drawn on the notation canvas.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class StaffSymbolManager {
    private static final Logger logger = LoggerFactory
            .getLogger(StaffSymbolManager.class);

    private static List<StaffSymbol> symbols = new ArrayList<>();
    private static StaffModel staffModel;

    public static final void setMIDITrack(MIDITrack track) {
        double x = staffModel.getStartX() + 1d * staffModel.getFontSize();
        symbols.clear();
        for (MIDINote note : track) {
            StaffSymbol symbol = createSymbol(
                    note,
                    x);
            symbols.add(symbol);
            addLedgers(note, x);
            x += staffModel.getFontSize();
        }
    }

    // to really get the x correct, we should use the Measure class and get the
    // beat x.
    // this is really a simple kludge.
    /**
     * Create a StaffSymbol for the {@code MIDINote}.
     * 
     * @param note
     *            a MIDINote
     * @param x
     *            the x locaiton where this symbol will appear
     * @return a StaffSymbol
     */
    private static StaffSymbol createSymbol(final MIDINote note, double x) {
        int pitch = note.getPitch().getMidiNumber();
        double duration = note.getDuration();
        String glyph = "";
        float advance = 0f;

        // 1 determine stem direction
        // 2 get accidental
        // 3 get duration

        boolean stemUp = true;
        // if (pitch < Pitch.C5) {
        // if (pitch < Pitch.C4) {
        // stemUp = true;
        // } else {
        // stemUp = false;
        // }
        // } else {
        // if (pitch < Pitch.A5) {
        // stemUp = true;
        // } else {
        // stemUp = false;
        // }
        // }

        // for non accidentals.
        double y = staffModel.getYpositionForPitch(pitch, true);

        if (isSpellingFlat(note)) {
            glyph = glyph + SymbolFactory.flat();
            logger.debug("is flat");
            y = staffModel.getYpositionForPitch(pitch, true);

            // TODO try this instead
            // advance += fm.charWidth(flat);
            advance += staffModel.stringWidth(SymbolFactory.flat());
            // symbols.add(new StaffSymbol(x, y, SymbolFactory.flat()));
        }

        if (isSpellingSharp(note)) {
            glyph = glyph + SymbolFactory.sharp();
            logger.debug("is sharp");
            y = staffModel.getYpositionForPitch(pitch, false);
        }

        double center = staffModel.getStaffCenterLine();
        if (y < center) {
            stemUp = false;
        } else {
            stemUp = true;
        }
        logger.debug("is stem up? {}", stemUp);

        // Now pick the duration. not complete right now.
        // this ignores beaming completely.

        // whole
        if (duration - 4d >= 0d) {
            duration -= 4d;
            glyph = glyph + SymbolFactory.noteWhole();
        }

        // dotted half
        if (duration - 3d >= 0d) {
            duration -= 3d;
            if (stemUp) {
                glyph = glyph + SymbolFactory.noteHalfUp();
            } else {
                glyph = glyph + SymbolFactory.noteHalfDown();
            }
            glyph = glyph + SymbolFactory.augmentationDot();
        }

        // half
        if (duration - 2d >= 0d) {
            duration -= 2d;
            if (stemUp) {
                glyph = glyph + SymbolFactory.noteHalfUp();
            } else {
                glyph = glyph + SymbolFactory.noteHalfDown();
            }
        }

        // dotted quarter
        if (duration - 1.5 >= 0d) {
            duration -= 1.5;
            if (stemUp) {
                glyph = glyph + SymbolFactory.noteQuarterUp();
            } else {
                glyph = glyph + SymbolFactory.noteQuarterDown();
            }
            glyph = glyph + SymbolFactory.augmentationDot();
        }

        // quarter
        if (duration - 1d >= 0d) {
            duration -= 1d;
            if (stemUp) {
                glyph = glyph + SymbolFactory.noteQuarterUp();
            } else {
                glyph = glyph + SymbolFactory.noteQuarterDown();
            }
            logger.debug("quarter note. remainder {}", duration);
        }
        // dotted eighth
        if (duration - .75 >= 0d) {
            duration -= .75;
            if (stemUp) {
                glyph = glyph + SymbolFactory.note8thUp();
            } else {
                glyph = glyph + SymbolFactory.note8thDown();
            }
            glyph = glyph + SymbolFactory.augmentationDot();
            logger.debug("dotted eighth note. remainder {}", duration);
        }

        // quarter triplet
        double qtriplet = 2d / 3d;
        if (duration - qtriplet >= 0d) {
            duration -= qtriplet;
            if (stemUp) {
                glyph = glyph + SymbolFactory.noteQuarterUp();
            } else {
                glyph = glyph + SymbolFactory.noteQuarterDown();
            }
        }

        // eighth
        if (duration - .5 >= 0d) {
            duration -= .5;
            if (stemUp) {
                glyph = glyph + SymbolFactory.note8thUp();
            } else {
                glyph = glyph + SymbolFactory.note8thDown();
            }
            logger.debug("eighth note. remainder {}", duration);
        }

        // 16th
        if (duration - .25 >= 0d) {
            duration -= .25;
            if (stemUp) {
                glyph = glyph + SymbolFactory.note16thUp();
            } else {
                glyph = glyph + SymbolFactory.note16thDown();
            }
        }

        double etriplet = 1d / 3d;
        if (duration - etriplet >= 0d) {
            duration -= etriplet;
            if (stemUp) {
                glyph = glyph + SymbolFactory.note8thUp();
            } else {
                glyph = glyph + SymbolFactory.note8thDown();
            }
        }

        // 32nd
        if (duration - .1875 >= 0) {
            duration -= .1875;
            if (stemUp) {
                glyph = glyph + SymbolFactory.noteheadBlack();
            } else {
                glyph = glyph + SymbolFactory.noteheadBlack();
            }
        }

        StaffSymbol symbol = new StaffSymbol(x, y, glyph);
        return symbol;
    }

    public static final boolean isSpellingFlat(MIDINote note) {
        String spelling = note.getSpelling().toUpperCase(Locale.ENGLISH);
        if (spelling.charAt(1) == 'B' || spelling.charAt(1) == 'F') {
            return true;
        }
        return false;
    }

    public static final boolean isSpellingSharp(MIDINote note) {
        String spelling = note.getSpelling().toUpperCase(Locale.ENGLISH);
        if (spelling.charAt(1) == '#' || spelling.charAt(1) == 'S') {
            return true;
        }
        return false;
    }

    public static final void addLedgers(MIDINote note, double x) {
        int pitch = note.getPitch().getMidiNumber();
        // String line = SymbolFactory.unicodeToString(0x005A);
        // String line = SymbolFactory.unicodeToString(94);
        // line = "___";

        // this seems to be the only one that actually draws
        String line = SymbolFactory.staff1LineWide();

        double lineinc = staffModel.getLineInc();
        double staffBottom = staffModel.getStaffBottom();
        double staffTop = staffModel.getStaffTop();
        logger.debug("staffBottom {} staffTop {} lineinc {}",
                staffBottom, staffTop, lineinc);

        boolean useFlat = true;
        if (isSpellingFlat(note)) {
            useFlat = true;
        }
        if (isSpellingSharp(note)) {
            useFlat = false;
        }

        // TODO the ledger x is wrong if there is an accidental

        // lacking fontmetrics, we guess at centering the ledger
        double lx = staffModel.getFontSize() / 4.3;

        if (pitch < Pitch.CS5) {
            int nledgers = staffModel.getNumberOfLedgers(pitch,
                    useFlat);
            logger.debug("there are {} ledgers for pitch {}",
                    nledgers, pitch);

            for (int i = 0; i < nledgers; i++) {
                double ly = (staffBottom + lineinc + lineinc * i);
                logger.debug("ledger y {}", ly);
                // StaffSymbol symbol = new StaffSymbol(x, ly,
                // SymbolFactory.staff1Line());
                // StaffSymbol symbol = new StaffSymbol(x, ly,
                // SymbolFactory.unicodeToString(0x005F));

                ly += lineinc * 2d; // 1linestaff kludge
                StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                symbols.add(symbol);
            }
        }

        if (pitch > Pitch.FS6) {
            int nledgers = staffModel.getNumberOfLedgers(pitch,
                    useFlat);
            logger.debug("there are {} ledgers for pitch {}",
                    nledgers, pitch);

            for (int i = 0; i < nledgers; i++) {
                double ly = (staffTop - lineinc - lineinc * i);
                ly += lineinc * 2d; // 1linestaff kludge
                logger.debug("ledger y {}", ly);
                StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                symbols.add(symbol);
            }
        }

    }

    /**
     * Enh. kiudge.
     * 
     * @param noteNum
     * @return if the note needs flats
     */
    private static boolean needFlats(int noteNum) {
        int pc;

        pc = noteNum % 12;
        switch (pc) {
        case 1:
        case 3:
        case 6:
        case 8:
        case 10:
            return (true);
        }
        return (false);
    }

    /**
     * @return the staffModel
     */
    public static StaffModel getStaffModel() {
        return staffModel;
    }

    /**
     * @param staffModel
     *            the staffModel to set
     */
    public static void setStaffModel(StaffModel staffModel) {
        StaffSymbolManager.staffModel = staffModel;
        staffModel.getTrackProperty().addListener(
                new ChangeListener<MIDITrack>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends MIDITrack> observable,
                            MIDITrack oldValue, MIDITrack newValue) {
                        logger.debug("staff model track changed. using new value");
                        setMIDITrack(newValue);
                    }
                });
    }

    /**
     * @return the symbols
     */
    public static List<StaffSymbol> getSymbols() {
        return symbols;
    }

    /*
     * fontMap.put(DOT, new Character((char) 46));
     * 
     * fontMap.put(LEDGERLINE, new Character((char) 94));
     */
}
