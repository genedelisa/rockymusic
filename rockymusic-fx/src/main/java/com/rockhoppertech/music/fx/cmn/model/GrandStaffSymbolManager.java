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
import java.util.Scanner;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.fx.cmn.model.GrandStaffModel.Clef;
import com.rockhoppertech.music.midi.js.KeySignature;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * The things that are drawn on the notation canvas.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class GrandStaffSymbolManager {
    private static final Logger logger = LoggerFactory
            .getLogger(GrandStaffSymbolManager.class);

    // private List<StaffSymbol> symbols = new ArrayList<>();

    /**
     * these shapes are filled.
     */
    private List<Shape> shapes = new ArrayList<>();

    private List<StaffSymbol> symbols = new ArrayList<>();

    /**
     * these shapes are stroked.
     */
    // private List<Shape> strokedShapes = new ArrayList<>();

    private GrandStaffModel model;

    private ObservableList<MIDINote> noteList;

    // metrics
    double quarterNoteWidth;
    double gclefWidth;
    double beatSpacing;
    double timeSignatureWidth;

    private boolean drawKeySignature = false;

    private boolean drawClefs = true;

    private boolean drawBrace = true;

    private boolean drawTimeSignature = false;

    private DoubleProperty staffWidthProperty = new SimpleDoubleProperty();

    /**
     * @param noteList
     *            the noteList to set
     */
    public void setNoteList(ObservableList<MIDINote> list) {
        noteList = list;
        noteList.addListener(new ListChangeListener<MIDINote>() {
            @Override
            public void onChanged(
                    javafx.collections.ListChangeListener.Change<? extends MIDINote> c) {
                refresh();
            }
        });
    }

    /**
     * Clear out the existing shapes, then calculate and add new ones. Draws a
     * simple representation. Does not pay any attention to the note's start
     * time. That what the MeasureCanvas will do when finished.
     */
    public void refresh() {
        if (model == null) {
            return;
        }
        double x = model.getStartX() + 1d
                * model.getFontSize();

        shapes.clear();
        symbols.clear();

        // this will be at model startx
        // sets first note x
        x = createStaves();

        // symbols.clear();

        if (noteList == null) {
            return;
        }

        x += model.getFontSize() / 2d;
        if (this.drawTimeSignature)
            x = addTimeSignature(x, 4, 4);

        // spacing between ts and first note
        x += model.getFontSize() / 1d;

        if (noteList.isEmpty()) {
            return;
        }

        model.setFirstNoteX(x);

        // TODO this doesn't cover initial rests i.e. when start beat > 1
        // TODO rests > 5 beats long don't work well
        MIDINote previousNote = noteList.get(0);
        MIDINote firstNote = noteList.get(0);
        double gap = firstNote.getStartBeat() - 1d;
        double eb = 1d;

        for (MIDINote note : noteList) {

            double sb = note.getStartBeat();

            if (sb > 1d) {
                eb = previousNote.getEndBeat();
            }
            gap = sb - eb;

            logger.debug("sb {} eb {} gap {} x {}", sb, eb, gap, x);
            if (gap > 0) {
                int pitch = note.getPitch().getMidiNumber();
                x = addRests(x, gap, pitch);
                x += model.getFontSize() / 2d;
                logger.debug("x {} after adding rest", x);
            } else {
                x += model.getFontSize() / 2d;
            }

            // x = createSymbol(note, x);
            x = createStaffSymbol(note, x);
            logger.debug("x {} after adding symbol", x);
            if (gap >= 0) {
                logger.debug("adding padding");
                // some padding between the symbols
                x += model.getFontSize() / 2d;
                logger.debug("x {} after adding gap 0 spacingl", x);
            }

            gap = 0;
            previousNote = note;
        }

        logger.debug("Setting staff width property");
        this.setStaffWidth(x + this.model.getFontSize());
        // staffWidthProperty.set(x + this.grandStaffModel.getFontSize());
    }

    double addRests(double x, double gap, int pitch) {

        String restString = Duration.getRestString(gap);
        logger.debug("rest string {}", restString);

        Scanner scanner = new Scanner(restString);
        scanner.useDelimiter(",");
        while (scanner.hasNext()) {
            String rest = scanner.next().trim();
            Double restDur = Double.parseDouble(rest);
            logger.debug("rest {} dur {}", rest, restDur);

            String glyph = SymbolFactory.restWhole();

            // position is middle line
            double restYposition = 0d;
            // TODO quarter rest is ok. fix the other positions
            if (restDur == 4d) {
                glyph = SymbolFactory.restWhole();
                if (pitch < 60) {
                    restYposition = model.bassMidiNumToY(
                            Pitch.E4,
                            false);
                } else {
                    restYposition = model.trebleMidiNumToY(
                            Pitch.C6,
                            false);
                }
                Text text = addText(x, restYposition, glyph);
                double width = text.getLayoutBounds().getWidth();
                x += (width * 1d);
            } else if (restDur == 3d) {
                // same symbol as whole rest but location is different
                glyph = SymbolFactory.restWhole();
                if (pitch < 60) {
                    restYposition = model.bassMidiNumToY(
                            Pitch.D4,
                            false);
                } else {
                    restYposition = model.trebleMidiNumToY(
                            Pitch.B5,
                            false);
                }
                Text text = addText(x, restYposition, glyph);
                double width = text.getLayoutBounds().getWidth();
                x += (width * 2d);

                glyph = SymbolFactory.restQuarter();
                text = addText(x, restYposition, glyph);
                x += (width * 1d);

            } else if (restDur == 2d) {
                // same symbol as whole rest but location is different
                glyph = SymbolFactory.restWhole();
                if (pitch < 60) {
                    restYposition = model.bassMidiNumToY(
                            Pitch.D4,
                            false);
                } else {
                    restYposition = model.trebleMidiNumToY(
                            Pitch.B5,
                            false);
                }
                Text text = addText(x, restYposition, glyph);
                double width = text.getLayoutBounds().getWidth();
                x += (width * 1d);
            } else if (restDur == 1d) {
                glyph = SymbolFactory.restQuarter();
                if (pitch < 60) {
                    restYposition = model.bassMidiNumToY(
                            Pitch.D4,
                            false);
                } else {
                    restYposition = model.trebleMidiNumToY(
                            Pitch.B5,
                            false);
                }
                Text text = addText(x, restYposition, glyph);
                double width = text.getLayoutBounds().getWidth();
                x += (width * 1d);
            } else if (restDur == .5d) {
                glyph = SymbolFactory.rest8th();

                if (pitch < 60) {
                    restYposition = model.bassMidiNumToY(
                            Pitch.E4,
                            false);
                } else {
                    restYposition = model.trebleMidiNumToY(
                            Pitch.C6,
                            false);
                }
                Text text = addText(x, restYposition, glyph);
                double width = text.getLayoutBounds().getWidth();
                x += (width * 1d);
                // punt on the rest of them
            } else if (restDur < .5d) {
                glyph = SymbolFactory.rest16th();
                if (pitch < 60) {
                    restYposition = model.bassMidiNumToY(
                            Pitch.C4,
                            false);
                } else {
                    restYposition = model.trebleMidiNumToY(
                            Pitch.A5,
                            false);
                }
                Text text = addText(x, restYposition, glyph);
                double width = text.getLayoutBounds().getWidth();
                x += (width * 1d);
            }

        } // while scanner
        scanner.close();
        return x;

    }

    boolean isTreble(int pitch) {
        return pitch >= Pitch.C5;
    }

    // public static final void setMIDITrack(MIDITrack track) {
    // double x = grandStaffModel.getStartX() + 1d *
    // grandStaffModel.getFontSize();
    // symbols.clear();
    // for (MIDINote note : track) {
    //
    // x = createSymbol(note, x);
    // // damn. the ledger will be on an augmentation dot since the
    // // returned x includes that.
    // // addLedgers(note, x);
    //
    // // some padding between the symbols
    // x += grandStaffModel.getFontSize() / 2d;
    // }
    // }

    /**
     * Create a StaffSymbol for the {@code MIDINote}.
     * 
     * @param note
     *            a MIDINote
     * @param x
     *            the x locaiton where this symbol will appear
     * @return a StaffSymbol
     */
    private double createStaffSymbol(final MIDINote note, double x) {
        int pitch = note.getPitch().getMidiNumber();
        double duration = note.getDuration();
        String glyph = "";

        // int index = (int) Math.floor(note.getStartBeat()) - 1;
        // int index = (int) Math.floor(measure.getBeatInMeasure(note)) - 1;

        logger.debug("creating symbol at x {} for note {}", x, note);

        // 1 get accidental
        // 2 determine stem direction
        // 3 get duration

        // for non accidentals.
        double y = model.getYpositionForPitch(pitch, true);

        StaffSymbol staffSymbol = new StaffSymbol();
        staffSymbol.setFont(this.model.getFont());
        staffSymbol.setMIDINote(note);
        staffSymbol.setX(x);
        staffSymbol.setY(y);
        // shapes.add(staffSymbol);
        symbols.add(staffSymbol);

        if (isSpellingFlat(note)) {
            glyph = SymbolFactory.flat();
            logger.debug("is flat");
            y = model.getYpositionForPitch(pitch, true);

            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setAccidental(glyph);
            x += staffSymbol.getLayoutBounds().getWidth();
        }

        if (isSpellingSharp(note)) {
            glyph = SymbolFactory.sharp();
            logger.debug("is sharp");
            y = model.getYpositionForPitch(pitch, false);

            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setAccidental(glyph);
            x += staffSymbol.getLayoutBounds().getWidth();
        }

        double center = 0d;
        if (isTreble(pitch)) {
            center = model.getTrebleStaffCenter();
        } else {
            center = model.getBassStaffCenter();
        }

        boolean stemUp = true;
        if (y < center) {
            stemUp = false;
        } else {
            stemUp = true;
        }
        logger.debug("is stem up? {}", stemUp);

        QuadCurve tie = null;

        // Now pick the duration. not complete right now.
        // this ignores beaming completely.

        // qt. ==1 et. = .5, so skip t.
        // durrake = Duration.getDotted(Duration.QUARTER_TRIPLET_NOTE);

        double durrake = Duration.getDoubleDotted(Duration.WHOLE_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            glyph = SymbolFactory.noteWhole();
            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            // add two dots
            staffSymbol.setAugmentationDots(glyph + glyph);
            width = staffSymbol.getLayoutBounds().getWidth();

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
            logger.debug("Added whole.. x {} y {} durleft {}", x, y, duration);

        }

        durrake = Duration.getDotted(Duration.WHOLE_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            glyph = SymbolFactory.noteWhole();
            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;

            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
            logger.debug("Added whole. x {} y {} durleft {}", x, y, duration);
        }

        durrake = Duration.WHOLE_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;

            glyph = SymbolFactory.noteWhole();
            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
            logger.debug("Added whole x {} y {} durleft {}", x, y, duration);
        }

        durrake = Duration.getDoubleDotted(Duration.HALF_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            if (stemUp) {
                glyph = SymbolFactory.noteHalfUp();
            } else {
                glyph = SymbolFactory.noteHalfDown();
            }

            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;

            glyph = SymbolFactory.augmentationDot();
            // add two dots
            staffSymbol.setAugmentationDots(glyph + glyph);
            width = staffSymbol.getLayoutBounds().getWidth();

            if (tie != null) {
                endTie(x, y, tie, width);
                staffSymbol.setTie(tie);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
            logger.debug(
                    "Added dotted half x {} y {} durleft {}",
                    x,
                    y,
                    duration);
        }

        durrake = Duration.getDotted(Duration.HALF_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            if (stemUp) {
                glyph = SymbolFactory.noteHalfUp();
            } else {
                glyph = SymbolFactory.noteHalfDown();
            }

            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;

            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();

            if (tie != null) {
                endTie(x, y, tie, width);
                staffSymbol.setTie(tie);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
            logger.debug(
                    "Added dotted half x {} y {} durleft {}",
                    x,
                    y,
                    duration);
        }

        durrake = Duration.HALF_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadHalf();
                addStemHalf(staffSymbol, center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteHalfUp();
                } else {
                    glyph = SymbolFactory.noteHalfDown();
                }
            }

            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();

            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
            logger.debug("Added half x {} y {} durleft {}", x, y, duration);
        }

//        durrake = Duration.getDoubleDotted(Duration.HALF_TRIPLET_NOTE);
//        if (duration - durrake >= 0d) {
//            duration -= durrake;
//        }
//        durrake = Duration.getDotted(Duration.HALF_TRIPLET_NOTE);
//        if (duration - durrake >= 0d) {
//            duration -= durrake;
//        }
//        durrake = Duration.HALF_TRIPLET_NOTE;
//        if (duration - durrake >= 0d) {
//            duration -= durrake;
//        }

        //
        durrake = Duration.getDoubleDotted(Duration.QUARTER_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph+" "+glyph);
            width = staffSymbol.getLayoutBounds().getWidth();

            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }
            logger.debug("Added quarter.. x {} y {} durleft {}", x, y, duration);
        }
        
        
        durrake = Duration.getDotted(Duration.QUARTER_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();


            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }
            logger.debug("Added quarter. x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.getDoubleDotted(Duration.QUARTER_TRIPLET_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph+" "+glyph);
            width = staffSymbol.getLayoutBounds().getWidth();


            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }
            logger.debug("Added quarter t.. x {} y {} durleft {}", x, y, duration);
        }

        durrake = Duration.QUARTER_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;

            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }
            logger.debug("Added quarter x {} y {} durleft {}", x, y, duration);
        }

        durrake = Duration.getDoubleDotted(Duration.EIGHTH_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add8thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            logger.debug("eighth note. remainder {}", duration);

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph+" "+glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x+=width;
            logger.debug("Added 8th .. x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.getDotted(Duration.EIGHTH_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add8thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            logger.debug("eighth note. remainder {}", duration);

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 8th . x {} y {} durleft {}", x, y, duration);
        }
        
        durrake = Duration.QUARTER_TRIPLET_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;

            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }
            logger.debug("Added quarter trip x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.getDoubleDotted(Duration.EIGHTH_TRIPLET_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add8thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            logger.debug("eighth note. remainder {}", duration);

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph+" "+glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 8th t .. x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.EIGHTH_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add8thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            logger.debug("eighth note. remainder {}", duration);

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 8th x {} y {} durleft {}", x, y, duration);
        }

        durrake = Duration.getDoubleDotted(Duration.SIXTEENTH_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add16thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note16thUp();
                } else {
                    glyph = SymbolFactory.note16thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph + " " + glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 16th .. x {} y {} durleft {}", x, y, duration);
        }
        
        durrake = Duration.getDotted(Duration.SIXTEENTH_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add16thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note16thUp();
                } else {
                    glyph = SymbolFactory.note16thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 16th . x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.EIGHTH_TRIPLET_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
         // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add8thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            //TODO triplet flag
            
            logger.debug("eighth note. remainder {}", duration);

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 8th trip x {} y {} durleft {}", x, y, duration);
        }
        
        durrake = Duration.getDoubleDotted(Duration.SIXTEENTH_TRIPLET_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add16thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note16thUp();
                } else {
                    glyph = SymbolFactory.note16thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph + " " + glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 16th .. x {} y {} durleft {}", x, y, duration);
        }
        
        durrake = Duration.SIXTEENTH_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add16thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note16thUp();
                } else {
                    glyph = SymbolFactory.note16thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 16th x {} y {} durleft {}", x, y, duration);
        }

        durrake = Duration.getDoubleDotted(Duration.THIRTY_SECOND_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add32ndFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note32ndUp();
                } else {
                    glyph = SymbolFactory.note32ndDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph+" " + glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
        }
        
        durrake = Duration.getDotted(Duration.THIRTY_SECOND_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add32ndFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note32ndUp();
                } else {
                    glyph = SymbolFactory.note32ndDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
        }
        
        durrake = Duration.SIXTEENTH_TRIPLET_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add16thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note16thUp();
                } else {
                    glyph = SymbolFactory.note16thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 16th triplet x {} y {} durleft {}", x, y, duration);
        }
        
        durrake = Duration.getDoubleDotted(Duration.THIRTY_SECOND_TRIPLET_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
         // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add32ndFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note32ndUp();
                } else {
                    glyph = SymbolFactory.note32ndDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph+" " + glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 32nd x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.THIRTY_SECOND_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
         // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add32ndFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note32ndUp();
                } else {
                    glyph = SymbolFactory.note32ndDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 32nd x {} y {} durleft {}", x, y, duration);
        }

        durrake = Duration.getDoubleDotted(Duration.SIXTY_FOURTH_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note64thUp();
                } else {
                    glyph = SymbolFactory.note64thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph + " " + glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 64th x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.getDotted(Duration.SIXTY_FOURTH_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note64thUp();
                } else {
                    glyph = SymbolFactory.note64thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 64th x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.THIRTY_SECOND_TRIPLET_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
         // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add32ndFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note32ndUp();
                } else {
                    glyph = SymbolFactory.note32ndDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 32nd x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.getDoubleDotted(Duration.SIXTY_FOURTH_TRIPLET_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note64thUp();
                } else {
                    glyph = SymbolFactory.note64thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph + " " + glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 64th x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.SIXTY_FOURTH_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note64thUp();
                } else {
                    glyph = SymbolFactory.note64thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 64th x {} y {} durleft {}", x, y, duration);
        }

        durrake = Duration.getDoubleDotted(Duration.ONE_TWENTY_EIGHTH_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
         // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note128thUp();
                } else {
                    glyph = SymbolFactory.note128thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph + " " + glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 128th x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.getDotted(Duration.ONE_TWENTY_EIGHTH_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
         // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note128thUp();
                } else {
                    glyph = SymbolFactory.note128thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 128th x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration.SIXTY_FOURTH_TRIPLET_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note64thUp();
                } else {
                    glyph = SymbolFactory.note64thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 64th x {} y {} durleft {}", x, y, duration);
        }
        durrake = Duration
                .getDoubleDotted(Duration.ONE_TWENTY_EIGHTH_TRIPLET_NOTE);
        if (duration - durrake >= 0d) {
            duration -= durrake;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note128thUp();
                } else {
                    glyph = SymbolFactory.note128thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph + " " + glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 128th x {} y {} durleft {}", x, y, duration);
            
        }
        durrake = Duration.ONE_TWENTY_EIGHTH_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
         // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note128thUp();
                } else {
                    glyph = SymbolFactory.note128thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 128th x {} y {} durleft {}", x, y, duration);
        }

        durrake = Duration.ONE_TWENTY_EIGHTH_TRIPLET_NOTE;
        if (duration - durrake >= 0d) {
            duration -= durrake;
         // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note128thUp();
                } else {
                    glyph = SymbolFactory.note128thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 128th x {} y {} durleft {}", x, y, duration);
        }
        logger.debug("Added staff symbol {}", staffSymbol);
        return x;
    }
    
    double foo() {
        double duration = 0;
        String glyph;
        StaffSymbol staffSymbol = null;
        boolean stemUp = false;
        QuadCurve tie = null;
        double x = 0;
        double y = 0;
        MIDINote note = null;
        int pitch = 0;
        double center = 0;
        
        // whole
        if (duration - 4d >= 0d) {
            duration -= 4d;
            glyph = SymbolFactory.noteWhole();

            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
            logger.debug("Added whole x {} y {} durleft {}", x, y, duration);
        }

        // dotted half
        if (duration - 3d >= 0d) {
            duration -= 3d;
            if (stemUp) {
                glyph = SymbolFactory.noteHalfUp();
            } else {
                glyph = SymbolFactory.noteHalfDown();
            }

            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();

            x += width;

            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();

            if (tie != null) {
                endTie(x, y, tie, width);
                staffSymbol.setTie(tie);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
            logger.debug(
                    "Added dotted half x {} y {} durleft {}",
                    x,
                    y,
                    duration);
        }

       
        // half
        if (duration - 2d >= 0d) {
            duration -= 2d;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadHalf();
                addStemHalf(staffSymbol, center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteHalfUp();
                } else {
                    glyph = SymbolFactory.noteHalfDown();
                }
            }

            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();

            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
            logger.debug("Added half x {} y {} durleft {}", x, y, duration);
        }

        // dotted quarter
        if (duration - 1.5 >= 0d) {
            duration -= 1.5;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();

            x += width;
            // x += grandStaffModel.stringWidth(glyph);
           // beatRectangle.setWidth(beatRectangle.getWidth() + width);

            // now add the dot
            glyph = SymbolFactory.augmentationDot();
            // space between note and dot
            x += width / 2d;

            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();

            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
            logger.debug(
                    "Added dotted quarter x {} y {} durleft {}",
                    x,
                    y,
                    duration);
        }

        // quarter
        if (duration - 1d >= 0d) {
            duration -= 1d;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            logger.debug("quarter note. remainder {} x {}", duration, x);

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();

            x += width;

            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }
            logger.debug("Added quarter x {} y {} durleft {}", x, y, duration);

        }

        // dotted eighth
        if (duration - .75 >= 0d) {
            duration -= .75;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add8thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;

            // now add the dot
            glyph = SymbolFactory.augmentationDot();
            // space between note and dot
            x += width / 2d;
            // symbols.add(new StaffSymbol(x, y, glyph));
            glyph = SymbolFactory.augmentationDot();
            staffSymbol.setAugmentationDots(glyph);
            width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug(
                    "Added dotted 8th x {} y {} durleft {}",
                    x,
                    y,
                    duration);

        }

        // TODO
        // quarter triplet
        double qtriplet = 2d / 3d;
        if (duration - qtriplet >= 0d) {
            duration -= qtriplet;
            if (stemUp) {
                glyph = SymbolFactory.noteQuarterUp();
            } else {
                glyph = SymbolFactory.noteQuarterDown();
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added qtriplet x {} y {} durleft {}", x, y, duration);
        }

        // eighth
        if (duration - .5 >= 0d) {
            duration -= .5;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add8thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            logger.debug("eighth note. remainder {}", duration);

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 8th x {} y {} durleft {}", x, y, duration);
        }

        // 16th
        if (duration - Duration.SIXTEENTH_NOTE >= 0d) {
            duration -= Duration.SIXTEENTH_NOTE;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(staffSymbol, center, x, y, stemUp);
                add16thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note16thUp();
                } else {
                    glyph = SymbolFactory.note16thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 16th x {} y {} durleft {}", x, y, duration);
        }

        double etriplet = 1d / 3d;
        if (duration - etriplet >= 0d) {
            duration -= etriplet;
            if (stemUp) {
                glyph = SymbolFactory.note8thUp();
            } else {
                glyph = SymbolFactory.note8thDown();
            }
            // x += grandStaffModel.stringWidth(glyph);
            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added etriplet x {} y {} durleft {}", x, y, duration);
        }

        // .1875 dotted 32nd

        // 32nd
        // s+ c,t c,t c,t c,t c,t c,t c,t c,t
        if (duration - Duration.THIRTY_SECOND_NOTE >= 0d) {
            duration -= Duration.THIRTY_SECOND_NOTE;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add32ndFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note32ndUp();
                } else {
                    glyph = SymbolFactory.note32ndDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 32nd x {} y {} durleft {}", x, y, duration);

        }

        // 64th
        // s+ c,x c,x c,x c,x c,x c,x c,x c,x
        if (duration - Duration.SIXTY_FOURTH_NOTE >= 0d) {
            duration -= Duration.SIXTY_FOURTH_NOTE;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(staffSymbol, center, x, y, stemUp);
                add64thFlag(staffSymbol, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note64thUp();
                } else {
                    glyph = SymbolFactory.note64thDown();
                }
            }

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
            logger.debug("Added 64th x {} y {} durleft {}", x, y, duration);
        }

        logger.debug("Added staff symbol {}", staffSymbol);
        return x;
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
    private double createSymbol(final MIDINote note, double x) {
        int pitch = note.getPitch().getMidiNumber();
        double duration = note.getDuration();
        String glyph = "";

        // 1 get accidental
        // 2 determine stem direction
        // 3 get duration

        // for non accidentals.
        double y = model.getYpositionForPitch(pitch, true);
        Text text;

        if (isSpellingFlat(note)) {
            glyph = SymbolFactory.flat();
            logger.debug("is flat");
            y = model.getYpositionForPitch(pitch, true);
            // x += grandStaffModel.stringWidth(glyph);
            // symbols.add(new StaffSymbol(x, y, glyph));

            text = new Text(x, y, glyph);
            text.setFont(model.getFont());
            text.setStyle("-fx-cursor: hand; -fx-font-smoothing-type: lcd;");
            text.setFontSmoothingType(FontSmoothingType.LCD);
            text.autosize();
            x += text.getLayoutBounds().getWidth();
            shapes.add(text);
        }

        if (isSpellingSharp(note)) {
            glyph = SymbolFactory.sharp();
            logger.debug("is sharp");
            y = model.getYpositionForPitch(pitch, false);
            // x += grandStaffModel.stringWidth(glyph);
            // symbols.add(new StaffSymbol(x, y, glyph));

            text = new Text(x, y, glyph);
            text.setFont(model.getFont());
            text.setStyle("-fx-cursor: hand; -fx-font-smoothing-type: lcd;");
            text.setFontSmoothingType(FontSmoothingType.LCD);
            text.autosize();
            x += text.getLayoutBounds().getWidth();
            shapes.add(text);
        }

        double center = 0d;
        if (isTreble(pitch)) {
            center = model.getTrebleStaffCenter();
        } else {
            center = model.getBassStaffCenter();
        }

        boolean stemUp = true;
        if (y < center) {
            stemUp = false;
        } else {
            stemUp = true;
        }
        logger.debug("is stem up? {}", stemUp);

        QuadCurve tie = null;

        // Now pick the duration. not complete right now.
        // this ignores beaming completely.

        // whole
        if (duration - 4d >= 0d) {
            duration -= 4d;
            glyph = SymbolFactory.noteWhole();
            // x += grandStaffModel.stringWidth(glyph);
            // symbols.add(new StaffSymbol(x, y, glyph));
            addLedgers(note, x);
            text = addText(x, y, glyph);

            // double height = text.getLayoutBounds().getWidth();

            double width = text.getLayoutBounds().getWidth();
            // if (slur != null) {
            // endSlur(x, y, slur, width);
            // slur = null;
            // }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += width;
        }

        // dotted half
        if (duration - 3d >= 0d) {
            duration -= 3d;
            if (stemUp) {
                glyph = SymbolFactory.noteHalfUp();
            } else {
                glyph = SymbolFactory.noteHalfDown();
            }

            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();

            x += width;

            // get the x for the note and add it, not the dot's x
            // x += grandStaffModel.stringWidth(glyph);
            glyph = SymbolFactory.augmentationDot();
            // now add a bit of space between the note and the dot
            // x += grandStaffModel.stringWidth(glyph);
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);

            width = text.getLayoutBounds().getWidth();
            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += text.getLayoutBounds().getWidth();
        }

        // half
        if (duration - 2d >= 0d) {
            duration -= 2d;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadHalf();
                addStemHalf(center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteHalfUp();
                } else {
                    glyph = SymbolFactory.noteHalfDown();
                }
            }

            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);

            double width = text.getLayoutBounds().getWidth();
            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += text.getLayoutBounds().getWidth();
        }

        // dotted quarter
        if (duration - 1.5 >= 0d) {
            duration -= 1.5;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();
            // x += grandStaffModel.stringWidth(glyph);

            // now add the dot
            glyph = SymbolFactory.augmentationDot();
            // space between note and dot
            x += text.getLayoutBounds().getWidth() / 2d;
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);

            double width = text.getLayoutBounds().getWidth();
            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            x += text.getLayoutBounds().getWidth();
        }

        // quarter
        if (duration - 1d >= 0d) {
            duration -= 1d;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            logger.debug("quarter note. remainder {}", duration);

            // symbols.add(new StaffSymbol(x, y, glyph));

            text = addText(x, y, glyph);

            // x += text.getBoundsInLocal().getWidth();
            // shapes.add(new Rectangle(x,y,text.getBoundsInParent().getWidth(),
            // text.getBoundsInParent().getHeight()));
            // shapes.add(new Rectangle(x,y,text.getLayoutBounds().getWidth(),
            // text.getLayoutBounds().getHeight()));

            // logger.debug("width local {} parent {} layout {} stringwidth {}",
            // text.getBoundsInLocal().getWidth(),
            // text.getBoundsInParent().getWidth(),
            // text.getLayoutBounds().getWidth(),
            // grandStaffModel.stringWidth(glyph)
            // );

            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;

            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }

            // x += grandStaffModel.stringWidth(glyph);
        }

        // dotted eighth
        if (duration - .75 >= 0d) {
            duration -= .75;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(center, x, y, stemUp);
                add8thFlag(x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            // x += grandStaffModel.stringWidth(glyph);
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();

            // now add the dot
            glyph = SymbolFactory.augmentationDot();
            // space between note and dot
            x += text.getLayoutBounds().getWidth() / 2d;
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            x += text.getLayoutBounds().getWidth();

            logger.debug("dotted eighth note. remainder {}", duration);

        }

        // TODO
        // quarter triplet
        double qtriplet = 2d / 3d;
        if (duration - qtriplet >= 0d) {
            duration -= qtriplet;
            if (stemUp) {
                glyph = SymbolFactory.noteQuarterUp();
            } else {
                glyph = SymbolFactory.noteQuarterDown();
            }

            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;
        }

        // eighth
        if (duration - .5 >= 0d) {
            duration -= .5;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(center, x, y, stemUp);
                add8thFlag(x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            logger.debug("eighth note. remainder {}", duration);
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            // x += grandStaffModel.stringWidth(glyph);
            x += text.getLayoutBounds().getWidth();
        }

        // 16th
        if (duration - .25 >= 0d) {
            duration -= .25;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(center, x, y, stemUp);
                add16thFlag(x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note16thUp();
                } else {
                    glyph = SymbolFactory.note16thDown();
                }
            }

            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();
        }

        double etriplet = 1d / 3d;
        if (duration - etriplet >= 0d) {
            duration -= etriplet;
            if (stemUp) {
                glyph = SymbolFactory.note8thUp();
            } else {
                glyph = SymbolFactory.note8thDown();
            }
            // x += grandStaffModel.stringWidth(glyph);
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();
        }

        // dotted 32nd
        if (duration - .1875 >= 0) {
            duration -= .1875;
            if (stemUp) {
                glyph = SymbolFactory.noteheadBlack();
            } else {
                glyph = SymbolFactory.noteheadBlack();
            }
            // x += grandStaffModel.stringWidth(glyph);
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();
        }

        // 32nd
        // s+ c,t c,t c,t c,t c,t c,t c,t c,t
        if (duration - Duration.THIRTY_SECOND_NOTE >= 0d) {
            duration -= Duration.THIRTY_SECOND_NOTE;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                logger.debug("shoud draw stem at x {} y {}", x, y);
                addStem(center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note32ndUp();
                } else {
                    glyph = SymbolFactory.note32ndDown();
                }
            }

            text = addText(x, y, glyph);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;

            if (tie != null) {
                endTie(x, y, tie, width);
                tie = null;
            }

            if (duration > 0d) {
                if (stemUp) {
                    tie = startTieUnder(x, y, width);
                } else {
                    tie = startTieOver(x, y, width);
                }
                x += width;
            }
        }

        return x;
    }

    private void endTie(double x, double y, QuadCurve slur, double width) {
        double endx = x - width / 2d;
        slur.setEndX(endx);
        // the center of the slur
        double cx = slur.getStartX() + (endx - slur.getStartX()) / 2d;
        slur.setControlX(cx);
        shapes.add(slur);
    }

    private QuadCurve startTieUnder(double x, double y, double width) {
        QuadCurve slur;
        slur = new QuadCurve();
        slur.setStartX(x + width / 2d);
        // slur.setStartX(x + width );
        slur.setStartY(y + model.getLineInc());
        slur.setEndY(y + model.getLineInc());
        // control x is set when added
        // slur.setControlY(y + 25d); // + is under
        slur.setControlY(y + model.getLineInc() * 2d); // + is under
        slur.setFill(null);
        slur.setStroke(Color.BLACK);
        // slur.setStroke(Color.web("#b9c0c5"));
        slur.setStrokeWidth(1d);
        return slur;
    }

    private QuadCurve startTieOver(double x, double y, double width) {
        QuadCurve slur;
        slur = new QuadCurve();
        slur.setStartX(x + width / 2d);
        // slur.setStartY(y + grandStaffModel.getYSpacing());
        slur.setStartY(y - model.getLineInc());
        slur.setEndY(y - model.getLineInc());
        // control x is set when added
        slur.setControlY(y - model.getLineInc() * 2d); // + is under
        slur.setFill(null);
        slur.setStroke(Color.BLACK);
        slur.setStrokeWidth(1d);
        return slur;
    }

    // private void endTieOver(double x, double y, QuadCurve slur, double width)
    // {
    // double endx = x - width / 2d;
    // slur.setEndX(endx);
    // slur.setEndY(y);
    // // the center of the slur
    // double cx = slur.getStartX() + (endx - slur.getStartX()) / 2d;
    // slur.setControlX(cx);
    // shapes.add(slur);
    // }

    // private void add8thFlag(double x, double y, boolean stemUp) {
    // String glyph;
    // Point2D p = null;
    // double fx;
    // if (stemUp) {
    // p = SymbolFactory.getStemUpNW("flag8thUp");
    // glyph = SymbolFactory.flag8thUp();
    // fx = x + p.getX() + this.quarterNoteWidth;
    // } else {
    // p = SymbolFactory.stemDownSW("flag8thDown");
    // glyph = SymbolFactory.flag8thDown();
    // fx = x + p.getX();
    // }
    //
    // double fy = model.getStaffCenterLine() + p.getY();
    // logger.debug("adding flag at x {} y {}", x + fx, fy);
    // addText(fx,
    // fy,
    // glyph);
    // }
    //
    // private void add16thFlag(double x, double y, boolean stemUp) {
    // String glyph;
    // Point2D p = null;
    // double fx;
    // if (stemUp) {
    // p = SymbolFactory.getStemUpNW("flag16thUp");
    // glyph = SymbolFactory.flag16thUp();
    // fx = x + p.getX() + this.quarterNoteWidth;
    // } else {
    // p = SymbolFactory.stemDownSW("flag16thDown");
    // glyph = SymbolFactory.flag16thDown();
    // fx = x + p.getX();
    // }
    //
    // double fy = model.getStaffCenterLine() + p.getY();
    // logger.debug("adding flag at x {} y {}", x + p.getX(), fy);
    // addText(fx,
    // fy,
    // glyph);
    // }

    /*
     * "flag16thDown": { "stemDownSW": [ 0.0, 0.128 ] }, "flag16thUp": {
     * "stemUpNW": [ 0.0, -0.088 ] },
     */

    /*
     * "flag8thDown": { "stemDownSW": [ 0.0, 0.132 ] }, "flag8thUp": {
     * "stemUpNW": [ 0.0, -0.048 ] },
     */

    // private void addStemHalf(double center, double x, double y, boolean
    // stemUp) {
    // if (stemUp) {
    // Point2D p = SymbolFactory.getStemUpSE("noteheadHalf");
    // double lx = x + p.getX() + this.quarterNoteWidth;
    // double ly = y + p.getY();
    // Line line = new Line(lx, ly, lx, center);
    // // line.setStrokeWidth(SymbolFactory.getStemThickness());
    // shapes.add(line);
    // } else {
    // Point2D p = SymbolFactory.getStemDownNW("noteheadHalf");
    // double lx = x + p.getX();
    // double ly = y + p.getY();
    // Line line = new Line(lx, ly, lx, center);
    // // line.setStrokeWidth(SymbolFactory.getStemThickness());
    // shapes.add(line);
    // }
    // }
    //
    // private void addStem(double center, double x, double y, boolean stemUp) {
    // if (stemUp) {
    // Point2D p = SymbolFactory.getStemUpSE("noteheadBlack");
    // double lx = x + p.getX() + this.quarterNoteWidth;
    // // double lx = x + p.getX();
    // double ly = y + p.getY();
    // Line line = new Line(lx, ly, lx, center);
    //
    // // line.setStrokeWidth(SymbolFactory.getStemThickness());
    // shapes.add(line);
    // logger.debug(
    // "added stem x {} y {}, center {}",
    // lx,
    // ly,
    // center);
    // } else {
    // Point2D p = SymbolFactory.getStemDownNW("noteheadBlack");
    // double lx = x + p.getX();
    // double ly = y + p.getY();
    // Line line = new Line(lx, ly, lx, center);
    //
    // // line.setStrokeWidth(SymbolFactory.getStemThickness());
    // shapes.add(line);
    // logger.debug(
    // "added stem x {} y {}, center {}",
    // lx,
    // ly,
    // center);
    // }
    // logger.debug("added stem");
    // }

    /*
     * "noteheadBlack": { "stemDownNW": [ 0.0, -0.184 ], "stemUpSE": [ 1.328,
     * 0.184 ] },
     */

    private Text addText(double x, double y, String glyph) {
        Text text = new Text(x, y, glyph);
        text.setFont(model.getFont());
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.setCursor(Cursor.HAND);
        text.autosize();
        shapes.add(text);
        // return the Text so the caller can query for the width etc.
        return text;
    }

    public static final boolean isSpellingFlat(MIDINote note) {
        String spelling = note.getSpelling();
        if (spelling == null) {
            spelling = PitchFormat.getInstance().format(note.getMidiNumber());
        }
        spelling = spelling.toUpperCase(Locale.ENGLISH);
        if (spelling.charAt(1) == 'B' || spelling.charAt(1) == 'F') {
            return true;
        }
        return false;
    }

    public static final boolean isSpellingSharp(MIDINote note) {
        String spelling = note.getSpelling();
        if (spelling == null) {
            spelling = PitchFormat.getInstance().format(note.getMidiNumber());
        }
        if (spelling.charAt(1) == '#' || spelling.charAt(1) == 'S') {
            return true;
        }
        return false;
    }

    public boolean shouldDrawStem(int pitch) {
        boolean draw = false;
        if (isTreble(pitch)) {
            if (pitch > Pitch.C7) {
                draw = true;
            }
            if (pitch < Pitch.B4) {
                draw = true;
            }
        } else {
            if (pitch > Pitch.D5) {
                draw = true;
            }
            if (pitch < Pitch.D3) {
                draw = true;
            }
            logger.debug("bass clef pith {} draw {}", pitch, draw);
        }

        return draw;
    }

    public final void addLedgers(MIDINote note, double x) {
        int pitch = note.getPitch().getMidiNumber();
        // String line = SymbolFactory.unicodeToString(0x005A);
        // String line = SymbolFactory.unicodeToString(94);
        // line = "___";

        // these seem to be the only ones that actually draw
        String line = SymbolFactory.staff1Line();
        // String line = SymbolFactory.staff1LineWide();

        double lineinc = model.getLineInc();
        double staffBottom = 0d;
        double staffTop = 0d;
        boolean useFlat = true;

        // lacking fontmetrics, we guess at centering the ledger
        // double lx = grandStaffModel.getFontSize() / 4.3;
        // double lx = grandStaffModel.stringWidth(line) / 4d;
        double lx = 0d;

        // double width = line.getLayoutBounds().getWidth();

        if (isSpellingFlat(note)) {
            useFlat = true;
        }
        if (isSpellingSharp(note)) {
            useFlat = false;
        }

        if (isTreble(pitch)) {
            staffBottom = model.getTrebleStaffBottom();
            staffTop = model.getTrebleStaffTop();
            if (pitch < Pitch.CS5) {
                int nledgers = model.getNumberOfLedgers(pitch,
                        useFlat, Clef.TREBLE);
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
                    // StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    // symbols.add(symbol);
                    Text text = addText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x - lx);

                }
            }

            if (pitch > Pitch.FS6) {
                int nledgers = model.getNumberOfLedgers(pitch,
                        useFlat, Clef.TREBLE);
                logger.debug("there are {} ledgers for pitch {}",
                        nledgers, pitch);

                for (int i = 0; i < nledgers; i++) {
                    double ly = (staffTop - lineinc - lineinc * i);
                    ly += lineinc * 2d; // 1linestaff kludge
                    logger.debug("ledger y {}", ly);
                    // StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    // symbols.add(symbol);

                    Text text = addText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x - lx);

                }
            }
        } else {
            staffBottom = model.getBassStaffBottom();
            staffTop = model.getBassStaffTop();
            if (pitch < Pitch.F3) {
                int nledgers = model.getNumberOfLedgers(pitch,
                        useFlat, Clef.BASS);
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
                    // StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    // symbols.add(symbol);
                    // addText(x - lx, ly, line);
                    Text text = addText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x - lx);
                }
            }

            if (pitch > Pitch.B4) {
                int nledgers = model.getNumberOfLedgers(pitch,
                        useFlat, Clef.BASS);
                logger.debug("there are {} ledgers for pitch {}",
                        nledgers, pitch);

                for (int i = 0; i < nledgers; i++) {
                    double ly = (staffTop - lineinc - lineinc * i);
                    ly += lineinc * 2d; // 1linestaff kludge
                    logger.debug("ledger y {}", ly);
                    // StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    // symbols.add(symbol);
                    // addText(x - lx, ly, line);
                    Text text = addText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x - lx);
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
     * @return the grandStaffModel
     */
    public GrandStaffModel getGrandStaffModel() {
        return model;
    }

    void calcMetrics() {
        // do some metrics
        Text text = new Text(SymbolFactory.noteQuarterUp());
        text.setFont(model.getFont());
        text.setFontSmoothingType(FontSmoothingType.LCD);
        this.quarterNoteWidth = text.getLayoutBounds().getWidth();

        text.setText(SymbolFactory.gClef());
        this.gclefWidth = text.getLayoutBounds().getWidth();

        text.setText(SymbolFactory.timeSig4());
        this.timeSignatureWidth = text.getLayoutBounds().getWidth();

        this.beatSpacing = quarterNoteWidth * 2d;

        logger.debug("quarter note width {}", quarterNoteWidth);
        logger.debug("gclefWidth  width {}", gclefWidth);
        logger.debug("beatSpacing  width {}", beatSpacing);
    }

    public void setGrandStaffModel(GrandStaffModel grandgrandStaffModel) {
        this.model = grandgrandStaffModel;

        setNoteList(model.getNoteList());

        calcMetrics();

        model.getFontSizeProperty().addListener(
                new ChangeListener<Number>() {

                    @Override
                    public void changed(
                            ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        logger.debug("new font size {}", newValue);
                        calcMetrics();
                    }
                });

        model.getNoteListProperty().addListener(
                new ListChangeListener<MIDINote>() {
                    @Override
                    public void onChanged(
                            javafx.collections.ListChangeListener.Change<? extends MIDINote> change) {
                        // if(change.wasReplaced()) {
                        //
                        // }
                        logger.debug("list was changed");
                        refresh();
                    }
                });

        refresh();

        model.getTrackProperty().addListener(
                new ChangeListener<MIDITrack>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends MIDITrack> observable,
                            MIDITrack oldValue, MIDITrack newValue) {
                        logger.debug("staff model track changed. using new value");
                        // setMIDITrack(newValue);
                    }
                });
    }

    /**
     * @return the symbols
     */
    // public List<StaffSymbol> getSymbols() {
    // return symbols;
    // }

    /**
     * @return the shapes
     */
    public List<Shape> getShapes() {
        return shapes;
    }

    /*
     * fontMap.put(DOT, new Character((char) 46));
     * 
     * fontMap.put(LEDGERLINE, new Character((char) 94));
     */

    public double addTimeSignature(double x, int timeSigNum,
            int timeSigDen) {

        double advance = x;
        // which ts number string is the longest?

        Text numerator;
        Text numerator1;
        switch (timeSigNum) {
        case 0:
            numerator = new Text(SymbolFactory.timeSig0());
            numerator1 = new Text(SymbolFactory.timeSig0());
            break;
        case 1:
            numerator = new Text(SymbolFactory.timeSig1());
            numerator1 = new Text(SymbolFactory.timeSig1());
            break;
        case 2:
            numerator = new Text(SymbolFactory.timeSig2());
            numerator1 = new Text(SymbolFactory.timeSig2());
            break;
        case 3:
            numerator = new Text(SymbolFactory.timeSig3());
            numerator1 = new Text(SymbolFactory.timeSig3());
            break;
        case 4:
            numerator = new Text(SymbolFactory.timeSig4());
            numerator1 = new Text(SymbolFactory.timeSig4());
            break;
        case 5:
            numerator = new Text(SymbolFactory.timeSig5());
            numerator1 = new Text(SymbolFactory.timeSig5());
            break;
        case 6:
            numerator = new Text(SymbolFactory.timeSig6());
            numerator1 = new Text(SymbolFactory.timeSig6());
            break;
        case 7:
            numerator = new Text(SymbolFactory.timeSig7());
            numerator1 = new Text(SymbolFactory.timeSig7());
            break;
        case 8:
            numerator = new Text(SymbolFactory.timeSig8());
            numerator1 = new Text(SymbolFactory.timeSig8());
            break;
        case 9:
            numerator = new Text(SymbolFactory.timeSig9());
            numerator1 = new Text(SymbolFactory.timeSig9());
            break;
        default:
            numerator = new Text(SymbolFactory.timeSig0());
            numerator1 = new Text(SymbolFactory.timeSig0());
            break;
        }
        Text denomenator;
        Text denomenator1;
        switch (timeSigDen) {
        case 0:
            denomenator = new Text(SymbolFactory.timeSig0());
            denomenator1 = new Text(SymbolFactory.timeSig0());
            break;
        case 1:
            denomenator = new Text(SymbolFactory.timeSig1());
            denomenator1 = new Text(SymbolFactory.timeSig1());
            break;
        case 2:
            denomenator = new Text(SymbolFactory.timeSig2());
            denomenator1 = new Text(SymbolFactory.timeSig2());
            break;
        case 3:
            denomenator = new Text(SymbolFactory.timeSig3());
            denomenator1 = new Text(SymbolFactory.timeSig3());
            break;
        case 4:
            denomenator = new Text(SymbolFactory.timeSig4());
            denomenator1 = new Text(SymbolFactory.timeSig4());
            break;
        case 5:
            denomenator = new Text(SymbolFactory.timeSig5());
            denomenator1 = new Text(SymbolFactory.timeSig5());
            break;
        case 6:
            denomenator = new Text(SymbolFactory.timeSig6());
            denomenator1 = new Text(SymbolFactory.timeSig6());
            break;
        case 7:
            denomenator = new Text(SymbolFactory.timeSig7());
            denomenator1 = new Text(SymbolFactory.timeSig7());
            break;
        case 8:
            denomenator = new Text(SymbolFactory.timeSig8());
            denomenator1 = new Text(SymbolFactory.timeSig8());
            break;
        case 9:
            denomenator = new Text(SymbolFactory.timeSig9());
            denomenator1 = new Text(SymbolFactory.timeSig9());
            break;
        default:
            denomenator = new Text(SymbolFactory.timeSig0());
            denomenator1 = new Text(SymbolFactory.timeSig0());
            break;
        }
        numerator.setFont(model.getFont());
        numerator.setFontSmoothingType(FontSmoothingType.LCD);
        denomenator.setFont(model.getFont());
        denomenator.setFontSmoothingType(FontSmoothingType.LCD);

        numerator1.setFont(model.getFont());
        numerator1.setFontSmoothingType(FontSmoothingType.LCD);
        denomenator1.setFont(model.getFont());
        denomenator1.setFontSmoothingType(FontSmoothingType.LCD);

        double numwidth = numerator.getLayoutBounds().getWidth();
        double denomwidth = denomenator.getLayoutBounds().getWidth();
        double offset = 0d;

        double ty = model.getTrebleStaffBottom();
        double by = model.getBassStaffBottom();

        if (numwidth > denomwidth) {
            offset = numwidth / 2d;
            offset -= denomwidth / 2d;
            numerator.setX(x);
            numerator.setY(ty - 3d * model.getLineInc());
            denomenator.setX(x + offset);
            denomenator.setY(ty - 1d * model.getLineInc());

            numerator1.setX(x);
            numerator1.setY(by - 3d * model.getLineInc());
            denomenator1.setX(x + offset);
            denomenator1.setY(by - 1d * model.getLineInc());

            advance += numwidth;

        } else if (numwidth < denomwidth) {
            offset = denomwidth / 2d;
            offset -= numwidth / 2d;
            numerator.setX(x + offset);
            numerator.setY(ty - 3d * model.getLineInc());
            denomenator.setX(x);
            denomenator.setY(ty - 1d * model.getLineInc());

            numerator1.setX(x + offset);
            numerator1.setY(by - 3d * model.getLineInc());
            denomenator1.setX(x);
            denomenator1.setY(by - 1d * model.getLineInc());
            advance += numwidth;

        } else if (numwidth == denomwidth) {
            numerator.setX(x);
            numerator.setY(ty - 3d * model.getLineInc());
            denomenator.setX(x);
            denomenator.setY(ty - 1d * model.getLineInc());

            numerator1.setX(x);
            numerator1.setY(by - 3d * model.getLineInc());
            denomenator1.setX(x);
            denomenator1.setY(by - 1d * model.getLineInc());
            advance += numwidth;

        }
        shapes.add(numerator);
        shapes.add(denomenator);
        shapes.add(numerator1);
        shapes.add(denomenator1);

        return advance;
    }

    public DoubleProperty staffWidthProperty() {
        return staffWidthProperty;
    }

    double getStaffWidth() {
        return this.staffWidthProperty.get();
    }

    public void setStaffWidth(double w) {
        this.staffWidthProperty.set(w);
    }

    double createStaves() {
        logger.debug("staffwidth {}", this.getStaffWidth());
        return createStaves(this.getStaffWidth());
    }

    public void setDrawTimeSignature(boolean drawTimeSignature) {
        this.drawTimeSignature = drawTimeSignature;
    }

    public void setDrawBrace(boolean drawBrace) {
        this.drawBrace = drawBrace;
    }

    public void setDrawClefs(boolean drawClefs) {
        this.drawClefs = drawClefs;
    }

    public void setDrawKeySignature(boolean drawKeySignature) {
        this.drawKeySignature = drawKeySignature;
    }

    /**
     * Create the staves.
     * 
     * The model's first note x is set to either the opening barline plus .5 of
     * the fontsize or a bit after the clef.
     * 
     * @param staffWidth
     *            how wide
     * @return the advance
     */
    double createStaves(double staffWidth) {
        logger.debug("drawing the staves {}", staffWidth);

        double x = model.getStartX();
        double y = model.getStaffBottom();
        double yspacing = model.getYSpacing();
        Font font = model.getFont();

        y = model.getBassStaffBottom();
        if (this.drawBrace) {
            Text brace = new Text(x, y,
                    SymbolFactory.brace());
            brace.setScaleY(2.8);
            brace.setScaleX(3d);
            brace.setFont(font);
            brace.setFontSmoothingType(FontSmoothingType.LCD);
            this.shapes.add(brace);
            x += (4d * brace.getLayoutBounds().getWidth());
        }

        model.setBeginningBarlineX(x);
        model.setFirstNoteX(x + model.getFontSize() / 2d);

        Line barline = new Line(x, model.getBassStaffBottom(),
                x, model.getTrebleStaffBottom()
                        - model.getLineInc()
                        * 4);
        this.shapes.add(barline);
        x += (barline.getLayoutBounds().getWidth());

        double clefX = x + model.getFontSize() / 4d;
        y = model.getTrebleStaffBottom();

        double trebleClefWidth = 0;
        if (this.drawClefs) {
            // just some spacing
            // x += staffModel.getFontSize();
            // double clefX = x+10d;
            Text trebleClef = new Text(clefX, y - (yspacing * 2d),
                    SymbolFactory.gClef());
            trebleClef.setFont(font);
            trebleClef.setFontSmoothingType(FontSmoothingType.LCD);
            this.shapes.add(trebleClef);
            trebleClefWidth = trebleClef.getLayoutBounds().getWidth();
            clefX = x + trebleClefWidth / 2d;
            trebleClef.setX(clefX);

            model.setFirstNoteX(clefX + trebleClefWidth
                    + model.getFontSize()
                    / 2d);
        }

        String staff = SymbolFactory.staff5Lines();
        // double staffStringIncrement = model.getFontSize() / 2d;
        Text text = new Text(staff);
        text.setFont(font);
        double staffStringIncrement = text.getLayoutBounds().getWidth();

        logger.debug(
                "drawing the treble staff at x {}, width {}",
                x,
                staffWidth);
        // draw the treble staff
        // for (double xx = x; xx < staffWidth - staffStringIncrement; xx +=
        // staffStringIncrement) {
        for (double xx = x; xx < staffWidth; xx += staffStringIncrement) {
            text = new Text(xx, y, staff);
            text.setFont(font);
            this.shapes.add(text);
        }

        y = model.getBassStaffBottom();
        if (this.drawClefs) {
            y = model.getBassStaffBottom();
            Text bassClef = new Text(clefX, y - (yspacing * 6d),
                    SymbolFactory.fClef());
            bassClef.setFont(font);
            bassClef.setFontSmoothingType(FontSmoothingType.LCD);
            this.shapes.add(bassClef);
        }

        // draw the bass staff
        for (double xx = x; xx < staffWidth; xx += staffStringIncrement) {
            text = new Text(xx, y, staff);
            text.setFont(font);
            this.shapes.add(text);
        }

        if (this.drawKeySignature) {
            logger.debug("drawing ks");
            x = drawKeySignature(x + trebleClefWidth
                    + model.getFontSize() / 2d);
            model.setFirstNoteX(x + model.getFontSize()
                    / 2d);

        }

        return x + trebleClefWidth;
    }

    private double drawKeySignature(double x) {
        GrandStaffModel model = this.model;

        MIDITrack track = this.model.getTrackProperty().get();
        KeySignature ks = null;
        if (track != null) {
            ks = track.getKeySignatureAtBeat(1d);
        }
        if (ks == null)
            ks = KeySignature.CMAJOR;

        logger.debug("drawing ks " + ks);
        double y = 0d;
        double startx = x;
        Text text;
        if (ks != null) {
            String glyph = null;
            int n = ks.getSf();
            if (n < 0) {
                glyph = SymbolFactory.flat();

            } else {
                glyph = SymbolFactory.sharp();
            }

            logger.debug("n accidentals {}", n);
            switch (n) {
            case -7:
                y = model.getYpositionForPitch(Pitch.BF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DF6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.GF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.C6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.F5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                //
                x = startx;
                y = model.getYpositionForPitch(Pitch.BF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DF4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.GF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.C4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.F3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();

                break;
            case -6:
                y = model.getYpositionForPitch(Pitch.BF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DF6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.GF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.C6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                //
                x = startx;
                y = model.getYpositionForPitch(Pitch.BF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DF4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.GF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.C4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();

                break;
            case -5:
                y = model.getYpositionForPitch(Pitch.BF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DF6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.GF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                //
                x = startx;
                y = model.getYpositionForPitch(Pitch.BF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DF4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.GF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();

                break;
            case -4:
                y = model.getYpositionForPitch(Pitch.BF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DF6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                //
                x = startx;
                y = model.getYpositionForPitch(Pitch.BF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DF4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                break;

            case -3:
                y = model.getYpositionForPitch(Pitch.BF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                //
                x = startx;
                y = model.getYpositionForPitch(Pitch.BF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                break;
            case -2:
                y = model.getYpositionForPitch(Pitch.BF5, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF6, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                //
                x = startx;
                y = model.getYpositionForPitch(Pitch.BF3, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.EF4, true);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();

                break;
            case -1:
                y = model.getYpositionForPitch(Pitch.BF5, true);
                text = addText(x, y, glyph);
                //
                x = startx;
                y = model.getYpositionForPitch(Pitch.BF3, true);
                text = addText(x, y, glyph);
                break;
            case 0:
                break;
            case 1:
                y = model.getYpositionForPitch(Pitch.FS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                x = startx;
                y = model.getYpositionForPitch(Pitch.FS4, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                break;
            case 2:
                y = model.getYpositionForPitch(Pitch.FS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.CS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                break;
            case 3:
                y = model.getYpositionForPitch(Pitch.FS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.CS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.GS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                break;
            case 4:
                y = model.getYpositionForPitch(Pitch.FS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.CS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.GS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                break;
            case 5:
                y = model.getYpositionForPitch(Pitch.FS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.CS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.GS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AS5, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                break;
            case 6:
                y = model.getYpositionForPitch(Pitch.FS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.CS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.GS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.DS6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.AS5, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.E6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                break;
            case 7:
                y = model.getYpositionForPitch(Pitch.F6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.C6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.G6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.D6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.A5, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.E6, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                y = model.getYpositionForPitch(Pitch.B5, false);
                text = addText(x, y, glyph);
                x += text.getLayoutBounds().getWidth();
                break;
            default:
                break;
            }
        }
        return x;

    }

    private void add8thFlag(double x, double y, boolean stemUp) {
        String glyph;
        Point2D p = null;
        double fx;
        if (stemUp) {
            p = SymbolFactory.getStemUpNW("flag8thUp");
            glyph = SymbolFactory.flag8thUp();
            fx = x + p.getX() + this.quarterNoteWidth;
        } else {
            p = SymbolFactory.stemDownSW("flag8thDown");
            glyph = SymbolFactory.flag8thDown();
            fx = x + p.getX();
        }

        double fy = model.getStaffCenterLine() + p.getY();
        logger.debug("adding flag at x {} y {}", x + fx, fy);
        addText(fx,
                fy,
                glyph);
    }

    private void add16thFlag(double x, double y, boolean stemUp) {
        String glyph;
        Point2D p = null;
        double fx;
        if (stemUp) {
            p = SymbolFactory.getStemUpNW("flag16thUp");
            glyph = SymbolFactory.flag16thUp();
            fx = x + p.getX() + this.quarterNoteWidth;
        } else {
            p = SymbolFactory.stemDownSW("flag16thDown");
            glyph = SymbolFactory.flag16thDown();
            fx = x + p.getX();
        }

        double fy = model.getStaffCenterLine() + p.getY();
        logger.debug("adding flag at x {} y {}", x + p.getX(), fy);
        addText(fx,
                fy,
                glyph);
    }

    private void add8thFlag(StaffSymbol symbol, double x, double y,
            boolean stemUp) {
        String glyph;
        Point2D p = null;
        double fx;
        if (stemUp) {
            p = SymbolFactory.getStemUpNW("flag8thUp");
            glyph = SymbolFactory.flag8thUp();
            fx = x + p.getX() + this.quarterNoteWidth;
        } else {
            p = SymbolFactory.stemDownSW("flag8thDown");
            glyph = SymbolFactory.flag8thDown();
            fx = x + p.getX();
        }

        double fy = model.getStaffCenterLine() + p.getY();
        logger.debug("adding flag at x {} y {}", x + fx, fy);
        Text flag = newText(fx,
                fy,
                glyph);
        symbol.setFlag(flag);
    }

    private void add16thFlag(StaffSymbol symbol, double x, double y,
            boolean stemUp) {
        String glyph;
        Point2D p = null;
        double fx;
        if (stemUp) {
            p = SymbolFactory.getStemUpNW("flag16thUp");
            glyph = SymbolFactory.flag16thUp();
            fx = x + p.getX() + this.quarterNoteWidth;
        } else {
            p = SymbolFactory.stemDownSW("flag16thDown");
            glyph = SymbolFactory.flag16thDown();
            fx = x + p.getX();
        }

        double fy = model.getStaffCenterLine() + p.getY();
        logger.debug("adding flag at x {} y {}", x + p.getX(), fy);
        Text flag = newText(fx,
                fy,
                glyph);
        symbol.setFlag(flag);
    }

    private void add32ndFlag(StaffSymbol symbol, double x, double y,
            boolean stemUp) {
        String glyph;
        Point2D p = null;
        double fx;
        if (stemUp) {
            p = SymbolFactory.getStemUpNW("flag32ndUp");
            glyph = SymbolFactory.flag32ndUp();
            fx = x + p.getX() + this.quarterNoteWidth;
        } else {
            p = SymbolFactory.stemDownSW("flag32ndDown");
            glyph = SymbolFactory.flag32ndDown();
            fx = x + p.getX();
        }

        double fy = model.getStaffCenterLine() + p.getY();
        logger.debug("adding flag at x {} y {}", x + p.getX(), fy);
        Text flag = newText(fx,
                fy,
                glyph);
        symbol.setFlag(flag);
    }

    private void add64thFlag(StaffSymbol symbol, double x, double y,
            boolean stemUp) {
        String glyph;
        Point2D p = null;
        double fx;
        if (stemUp) {
            p = SymbolFactory.getStemUpNW("flag64thUp");
            glyph = SymbolFactory.flag64thUp();
            fx = x + p.getX() + this.quarterNoteWidth;
        } else {
            p = SymbolFactory.stemDownSW("flag64thDown");
            glyph = SymbolFactory.flag64thDown();
            fx = x + p.getX();
        }

        double fy = model.getStaffCenterLine() + p.getY();
        logger.debug("adding flag at x {} y {}", x + p.getX(), fy);
        Text flag = newText(fx,
                fy,
                glyph);
        symbol.setFlag(flag);
    }

    /*
     * "flag16thDown": { "stemDownSW": [ 0.0, 0.128 ] }, "flag16thUp": {
     * "stemUpNW": [ 0.0, -0.088 ] },
     */

    /*
     * "flag8thDown": { "stemDownSW": [ 0.0, 0.132 ] }, "flag8thUp": {
     * "stemUpNW": [ 0.0, -0.048 ] },
     */

    private void addStemHalf(double center, double x, double y, boolean stemUp) {
        if (stemUp) {
            Point2D p = SymbolFactory.getStemUpSE("noteheadHalf");
            double lx = x + p.getX() + this.quarterNoteWidth;
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, center);
            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            shapes.add(line);
        } else {
            Point2D p = SymbolFactory.getStemDownNW("noteheadHalf");
            double lx = x + p.getX();
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, center);
            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            shapes.add(line);
        }
    }

    private void addStemHalf(StaffSymbol symbol, double center, double x,
            double y, boolean stemUp) {
        if (stemUp) {
            Point2D p = SymbolFactory.getStemUpSE("noteheadHalf");
            double lx = x + p.getX() + this.quarterNoteWidth;
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, center);
            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            symbol.setStem(line);
        } else {
            Point2D p = SymbolFactory.getStemDownNW("noteheadHalf");
            double lx = x + p.getX();
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, center);
            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            symbol.setStem(line);
        }
    }

    private void addStem(double center, double x, double y, boolean stemUp) {
        if (stemUp) {
            Point2D p = SymbolFactory.getStemUpSE("noteheadBlack");
            double lx = x + p.getX() + this.quarterNoteWidth;
            // double lx = x + p.getX();
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, center);

            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            shapes.add(line);
            logger.debug(
                    "added stem x {} y {}, center {}",
                    lx,
                    ly,
                    center);
        } else {
            Point2D p = SymbolFactory.getStemDownNW("noteheadBlack");
            double lx = x + p.getX();
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, center);

            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            shapes.add(line);
            logger.debug(
                    "added stem x {} y {}, center {}",
                    lx,
                    ly,
                    center);
        }
        logger.debug("added stem");
    }

    private void addStem(StaffSymbol symbol, double center, double x, double y,
            boolean stemUp) {
        if (stemUp) {
            Point2D p = SymbolFactory.getStemUpSE("noteheadBlack");
            double lx = x + p.getX() + this.quarterNoteWidth;
            // double lx = x + p.getX();
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, center);

            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            symbol.setStem(line);

            logger.debug(
                    "added stem x {} y {}, center {}",
                    lx,
                    ly,
                    center);
        } else {
            Point2D p = SymbolFactory.getStemDownNW("noteheadBlack");
            double lx = x + p.getX();
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, center);

            // line.setStrokeWidth(SymbolFactory.getStemThickness());

            symbol.setStem(line);
            logger.debug(
                    "added stem x {} y {}, center {}",
                    lx,
                    ly,
                    center);
        }
        logger.debug("added stem");
    }

    public final void addLedgers(StaffSymbol staffSymbol, MIDINote note,
            double x) {
        int pitch = note.getPitch().getMidiNumber();
        // String line = SymbolFactory.unicodeToString(0x005A);
        // String line = SymbolFactory.unicodeToString(94);
        // line = "___";

        // these seem to be the only ones that actually draw
        String line = SymbolFactory.staff1Line();
        // String line = SymbolFactory.staff1LineWide();

        double lineinc = model.getLineInc();
        double staffBottom = 0d;
        double staffTop = 0d;
        boolean useFlat = true;

        // lacking fontmetrics, we guess at centering the ledger
        // double lx = grandStaffModel.getFontSize() / 4.3;
        // double lx = grandStaffModel.stringWidth(line) / 4d;
        double lx = 0d;

        // double width = line.getLayoutBounds().getWidth();

        if (isSpellingFlat(note)) {
            useFlat = true;
        }
        if (isSpellingSharp(note)) {
            useFlat = false;
        }

        if (isTreble(pitch)) {
            staffBottom = model.getTrebleStaffBottom();
            staffTop = model.getTrebleStaffTop();
            if (pitch < Pitch.CS5) {
                int nledgers = model.getNumberOfLedgers(pitch,
                        useFlat, Clef.TREBLE);
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
                    // StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    // symbols.add(symbol);
                    Text text = newText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x - lx);
                    staffSymbol.addLedger(text);
                    logger.debug("added ledger x {} y {}", x - lx, ly);

                }
            }

            if (pitch > Pitch.FS6) {
                int nledgers = model.getNumberOfLedgers(pitch,
                        useFlat, Clef.TREBLE);
                logger.debug("there are {} ledgers for pitch {}",
                        nledgers, pitch);

                for (int i = 0; i < nledgers; i++) {
                    double ly = (staffTop - lineinc - lineinc * i);
                    ly += lineinc * 2d; // 1linestaff kludge
                    logger.debug("ledger y {}", ly);
                    // StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    // symbols.add(symbol);

                    Text text = newText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x - lx);
                    staffSymbol.addLedger(text);
                }
            }
        } else {
            staffBottom = model.getBassStaffBottom();
            staffTop = model.getBassStaffTop();
            if (pitch < Pitch.F3) {
                int nledgers = model.getNumberOfLedgers(pitch,
                        useFlat, Clef.BASS);
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
                    // StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    // symbols.add(symbol);
                    // addText(x - lx, ly, line);
                    Text text = newText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x - lx);
                    staffSymbol.addLedger(text);
                }
            }

            if (pitch > Pitch.B4) {
                int nledgers = model.getNumberOfLedgers(pitch,
                        useFlat, Clef.BASS);
                logger.debug("there are {} ledgers for pitch {}",
                        nledgers, pitch);

                for (int i = 0; i < nledgers; i++) {
                    double ly = (staffTop - lineinc - lineinc * i);
                    ly += lineinc * 2d; // 1linestaff kludge
                    logger.debug("ledger y {}", ly);
                    // StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    // symbols.add(symbol);
                    // addText(x - lx, ly, line);
                    Text text = newText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x - lx);
                    staffSymbol.addLedger(text);
                }
            }
        }

    }

    private Text newText(double x, double y, String glyph) {
        Text text = new Text(x, y, glyph);
        text.setFont(model.getFont());
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.setCursor(Cursor.HAND);
        text.autosize();

        text.setOnMousePressed(new EventHandler<MouseEvent>() {
            // text.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                Text r = (Text) me.getSource();
                MIDINote note = (MIDINote) r.getUserData();
                if (note != null) {
                    logger.debug("note for text is {}", note);
                } else {
                    logger.debug("note for text is null");
                }
            }
        });

        // return the Text so the caller can query for the width etc.
        return text;
    }

    public List<StaffSymbol> getSymbols() {
        return symbols;
    }
}
