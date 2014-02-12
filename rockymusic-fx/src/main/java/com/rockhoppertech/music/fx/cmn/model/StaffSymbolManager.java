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
import com.rockhoppertech.music.fx.cmn.model.StaffModel.Clef;
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

            x = createSymbol(note, x);
            // damn. the ledger will be on an augmentation dot since the
            // returned x includes that.
            // addLedgers(note, x);

            // some padding between the symbols
            x += staffModel.getFontSize() / 2d;
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
    private static double createSymbol(final MIDINote note, double x) {
        int pitch = note.getPitch().getMidiNumber();
        double duration = note.getDuration();
        String glyph = "";

        // 1 get accidental
        // 2 determine stem direction
        // 3 get duration

        // for non accidentals.
        double y = staffModel.getYpositionForPitch(pitch, true);

        if (isSpellingFlat(note)) {
            glyph = glyph + SymbolFactory.flat();
            logger.debug("is flat");
            y = staffModel.getYpositionForPitch(pitch, true);
            x += staffModel.stringWidth(SymbolFactory.flat());
            symbols.add(new StaffSymbol(x, y, SymbolFactory.flat()));
        }

        if (isSpellingSharp(note)) {
            glyph = glyph + SymbolFactory.sharp();
            logger.debug("is sharp");
            y = staffModel.getYpositionForPitch(pitch, false);
        }

        double center = staffModel.getStaffCenterLine();
        boolean stemUp = true;
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
            glyph = SymbolFactory.noteWhole();
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);
        }

        // dotted half
        if (duration - 3d >= 0d) {
            duration -= 3d;
            if (stemUp) {
                glyph = SymbolFactory.noteHalfUp();
            } else {
                glyph = SymbolFactory.noteHalfDown();
            }
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);

            // get the x for the note and add it, not the dot's x
            x += staffModel.stringWidth(glyph);
            glyph = SymbolFactory.augmentationDot();
            // now add a bit of space between the note and the dot
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
        }

        // half
        if (duration - 2d >= 0d) {
            duration -= 2d;
            if (stemUp) {
                glyph = SymbolFactory.noteHalfUp();
            } else {
                glyph = SymbolFactory.noteHalfDown();
            }
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);
        }

        // dotted quarter
        if (duration - 1.5 >= 0d) {
            duration -= 1.5;
            if (stemUp) {
                glyph = SymbolFactory.noteQuarterUp();
            } else {
                glyph = SymbolFactory.noteQuarterDown();
            }
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));

            addLedgers(note, x);

            // get the x for the note and add it, not the dot's x
            x += staffModel.stringWidth(glyph);
            glyph = SymbolFactory.augmentationDot();
            // now add a bit of space between the note and the dot
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
        }

        // quarter
        if (duration - 1d >= 0d) {
            duration -= 1d;
            if (stemUp) {
                glyph = SymbolFactory.noteQuarterUp();
            } else {
                glyph = SymbolFactory.noteQuarterDown();
            }
            logger.debug("quarter note. remainder {}", duration);
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);
        }
        // dotted eighth
        if (duration - .75 >= 0d) {
            duration -= .75;
            if (stemUp) {
                glyph = SymbolFactory.note8thUp();
            } else {
                glyph = SymbolFactory.note8thDown();
            }

            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);

            // get the x for the note and add it, not the dot's x
            x += staffModel.stringWidth(glyph);
            glyph = SymbolFactory.augmentationDot();
            // now add a bit of space between the note and the dot
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            logger.debug("dotted eighth note. remainder {}", duration);
        }

        // quarter triplet
        double qtriplet = 2d / 3d;
        if (duration - qtriplet >= 0d) {
            duration -= qtriplet;
            if (stemUp) {
                glyph = SymbolFactory.noteQuarterUp();
            } else {
                glyph = SymbolFactory.noteQuarterDown();
            }
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);
        }

        // eighth
        if (duration - .5 >= 0d) {
            duration -= .5;
            if (stemUp) {
                glyph = SymbolFactory.note8thUp();
            } else {
                glyph = SymbolFactory.note8thDown();
            }
            logger.debug("eighth note. remainder {}", duration);
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);
        }

        // 16th
        if (duration - .25 >= 0d) {
            duration -= .25;
            if (stemUp) {
                glyph = SymbolFactory.note16thUp();
            } else {
                glyph = SymbolFactory.note16thDown();
            }
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);
        }

        double etriplet = 1d / 3d;
        if (duration - etriplet >= 0d) {
            duration -= etriplet;
            if (stemUp) {
                glyph = SymbolFactory.note8thUp();
            } else {
                glyph = SymbolFactory.note8thDown();
            }
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);
        }

        // 32nd
        if (duration - .1875 >= 0) {
            duration -= .1875;
            if (stemUp) {
                glyph = SymbolFactory.noteheadBlack();
            } else {
                glyph = SymbolFactory.noteheadBlack();
            }
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);
        }

        return x;
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

        // these seem to be the only ones that actually draw
        String line = SymbolFactory.staff1Line();
        // String line = SymbolFactory.staff1LineWide();

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

        // lacking fontmetrics, we guess at centering the ledger
        // double lx = staffModel.getFontSize() / 4.3;
        double lx = staffModel.stringWidth(line) / 4d;

        if (staffModel.getClef() == Clef.TREBLE) {
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
        } else if (staffModel.getClef() == Clef.BASS) {
            if (pitch < Pitch.F3) {
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

            if (pitch > Pitch.B4) {
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
        } else if (staffModel.getClef() == Clef.ALTO) {
            if (pitch < Pitch.E4) {
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

            if (pitch > Pitch.A5) {
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
