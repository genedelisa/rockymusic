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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import com.rockhoppertech.music.fx.cmn.Measure;
import com.rockhoppertech.music.fx.cmn.model.MeasureModel.Clef;
import com.rockhoppertech.music.midi.js.KeySignature;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.TimeSignature;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * The things that are drawn on the notation canvas.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class MeasureSymbolManager {
    private static final Logger logger = LoggerFactory
            .getLogger(MeasureSymbolManager.class);

    /**
     * these shapes are filled.
     */
    private List<Shape> shapes = new ArrayList<>();

    private List<StaffSymbol> symbols = new ArrayList<>();

    /**
     * 
     */
    private MeasureModel model;
    /**
     * 
     */
    private ObservableList<MIDINote> noteList;
    private Measure measure;
    private List<Rectangle> beatRectangles = new ArrayList<>();

    private double inputBeatQuantization = Duration.SIXTY_FOURTH_NOTE;
    // metrics
    double quarterNoteWidth;
    double gclefWidth;
    double beatSpacing;
    double timeSignatureWidth;

    double staffWidth = 250d;

    private boolean drawBeatRectangles = true;

    private boolean drawClefs;

    private boolean drawKeySignature;

    private boolean drawTimeSignature;

    private BooleanProperty drawBeatRectanglesProperty = new SimpleBooleanProperty(
            drawBeatRectangles);
    private BooleanProperty drawTimeSignatureProperty = new SimpleBooleanProperty(
            drawTimeSignature);
    private BooleanProperty drawClefsProperty = new SimpleBooleanProperty(
            drawClefs);

    private boolean drawBrace;
    private BooleanProperty drawBraceProperty = new SimpleBooleanProperty(
            drawBrace);

    private double beatPadding;

    DoubleProperty staffWidthProperty = new SimpleDoubleProperty();

    public MeasureSymbolManager() {

    }

    /**
     * @param noteList
     *            the noteList to set
     */
    public void setNoteList(ObservableList<MIDINote> list) {
        if (list == null) {
            return;
        }
        noteList = list;
        noteList.addListener(new ListChangeListener<MIDINote>() {
            @Override
            public void onChanged(
                    ListChangeListener.Change<? extends MIDINote> c) {
                logger.debug("notelist changed. refreshing");
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
        logger.debug("refreshing");

        if (model == null) {
            return;
        }
        shapes.clear();
        symbols.clear();
        //
        // double x = model.getStartX() + 1d
        // * model.getFontSize();

        double x = model.getBeginningBarlineX();

        // this will be at model startx
        // sets first note x
        x = createStaves();
        // x = this.model.getStartX();

        if (this.drawTimeSignature) {
            if (!this.drawKeySignature)
                x += model.getFontSize() / 2d;
            x = addTimeSignature(x, 4, 4);
        }

        // model.setFirstNoteX(x);
        this.createBeatRectangles();

        if (noteList == null) {
            return;
        }

        if (noteList.isEmpty()) {
            return;
        }

        // TODO this doesn't cover initial rests i.e. when start beat > 1
        // TODO rests > 5 beats long don't work well
        MIDINote previousNote = noteList.get(0);
        MIDINote firstNote = noteList.get(0);
        double gap = firstNote.getStartBeat() - 1d;
        double eb = 1d;

        for (MIDINote note : noteList) {

            // x = getXofBeatN(note.getStartBeat());

            logger.debug(
                    "beat {} beat in measure {}",
                    note.getStartBeat(),
                    measure.getBeatInMeasure(note));

            x = getXofBeatN(measure.getBeatInMeasure(note));

            // double sb = note.getStartBeat();
            //
            // if (sb > 1d) {
            // eb = previousNote.getEndBeat();
            // }
            // gap = sb - eb;
            //
            // logger.debug("sb {} eb {} gap {} x {}", sb, eb, gap, x);
            // if (gap > 0) {
            // int pitch = note.getPitch().getMidiNumber();
            // x = addRests(x, gap, pitch);
            // x += model.getFontSize() / 2d;
            // logger.debug("x {} after adding rest", x);
            // } else {
            // x += model.getFontSize() / 2d;
            // }

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

        // push all the x locations to be the previous x + width
        // make sure all the rects are large enough, adjust widths and x
        // locations.
        enlargeBeatRectangles();
        // resize the staves to the new rectangles
        createStaves();
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

    void normalizeRectangles() {
        for (int index = 0; index < this.beatRectangles.size() - 1; index++) {
            Rectangle beat = this.beatRectangles.get(index);
            Rectangle nextBeat = this.beatRectangles.get(index + 1);
            double x = beat.getX() + beat.getWidth();
            nextBeat.setX(x);
        }
        dumpBeatRectangles();

    }

    void dumpBeatRectangles() {
        for (Rectangle beat : this.beatRectangles) {
            logger.debug(
                    "beat rectangle x {} y {} w {} h {}",
                    beat.getX(),
                    beat.getY(),
                    beat.getWidth(),
                    beat.getHeight());
        }
    }

    /**
     * Here's the problem. The initial size of each beat was simply based on the
     * size of the drawn measure.
     * <p>
     * At this point, there should be shapes for each beat created. Now we
     * iterate across those shopes, get their sizes, and resize the beat
     * accordingly.
     */
    void enlargeBeatRectangles() {

        // double newx = model.getFirstNoteX();

        for (int beat = 0; beat < this.beatRectangles.size(); beat++) {
            int beat1based = beat + 1;

            // the width of the symbols
            double width = getWidthOfBeat(beat1based);

            Rectangle rect = this.beatRectangles.get(beat);
            logger.debug("symbol width is {} for beat {}", width, beat1based);
            logger.debug(
                    "rect width is {} for beat {} rect x is {}",
                    rect.getWidth(),
                    beat1based,
                    rect.getX()
                    );

            // if (beat1based == 1) {
            // newx = model.getFirstNoteX();
            // } else {
            // newx = rect.getX();
            // }
            // newx = rect.getX();
            // logger.debug("new x is {} for beat {}", newx, beat1based);
            if (width > rect.getWidth()) {
                rect.setWidth(width);
            }
            List<StaffSymbol> list = getSymbolsAtBeat(beat1based);
            if (list.size() > 0) {
                // double xinc = (width) / list.size();
                // logger.debug(
                // "xinc is {} for list size {} with width {}",
                // xinc,
                // list.size(),
                // width);
                for (StaffSymbol s : list) {
                    double x = getXofBeatN(s.getNote().getStartBeat());
                    s.setX(x);
                }

                // now fix up the x locations of the rectangles
                normalizeRectangles();
            }
        }
        if (!this.beatRectangles.isEmpty()) {
            Rectangle r = this.beatRectangles
                    .get(this.beatRectangles.size() - 1);
            if (r != null) {
                this.setStaffWidth(r.getX() + r.getWidth());
            }
        }

    }

    /**
     * @param beat
     *            the 1 based beat
     * @return the symbols
     */
    List<StaffSymbol> getSymbolsAtBeat(int beat) {
        // beat++;
        List<StaffSymbol> list = new ArrayList<>();
        logger.debug("iterating symbols of size {}", symbols.size());
        for (StaffSymbol s : this.symbols) {
            MIDINote note = s.getNote();
            if (note != null) {
                int floor = (int) Math.floor(note.getStartBeat());
                logger.debug(
                        "checking beat {} with note floor {} note {}",
                        beat,
                        floor,
                        note);
                if (floor == beat) {
                    logger.debug("adding note {}", note);
                    list.add(s);
                }
            } else {
                logger.debug("note is null for symbol {}", s);
            }
        }

        return list;
    }

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
        int index = (int) Math.floor(measure.getBeatInMeasure(note)) - 1;

        Rectangle beatRectangle = this.beatRectangles.get(index);
        logger.debug(
                "beat rect for index {} the rectangle x {} y {} w {} h {}",
                index,
                beatRectangle.getX(),
                beatRectangle.getY(),
                beatRectangle.getWidth(),
                beatRectangle.getHeight());

        // let's try without. remove calls if this works.
        beatRectangle = new Rectangle();

        // Rectangle nextBeatRectangle = null;
        // index++;
        // if (index < this.beatRectangles.size() - 1) {
        // nextBeatRectangle = this.beatRectangles.get(index);
        // logger.debug(
        // "next beat rect for index {} the rectangle x {} y {} w {} h {}",
        // index,
        // nextBeatRectangle.getX(),
        // nextBeatRectangle.getY(),
        // nextBeatRectangle.getWidth(),
        // nextBeatRectangle.getHeight());
        // }

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
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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
            beatRectangle.setWidth(beatRectangle.getWidth() + width);

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
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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

            logger.debug("quarter note. remainder {}", duration);

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

            // beatRectangle.setWidth(beatRectangle.getWidth() + width);

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

            beatRectangle.setWidth(beatRectangle.getWidth() + width);

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

            staffSymbol.setY(y);
            staffSymbol.setSymbol(glyph);
            addLedgers(staffSymbol, note, x);
            double width = staffSymbol.getLayoutBounds().getWidth();
            x += width;
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
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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

            // beatRectangle.setWidth(beatRectangle.getWidth() + width);
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
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
        }

        // .1875 dotted 32nd
        double d = Duration.getDotted(Duration.THIRTY_SECOND_NOTE);

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
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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
            // beatRectangle.setWidth(beatRectangle.getWidth() + width);
        }

        logger.debug("Added staff symbol {}", staffSymbol);
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

    /*
     * "noteheadBlack": { "stemDownNW": [ 0.0, -0.184 ], "stemUpSE": [ 1.328,
     * 0.184 ] },
     */

    private Text addText(double x, double y, String glyph) {
        Text text = newText(x, y, glyph);
        shapes.add(text);

        // return the Text so the caller can query for the width etc.
        return text;
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

        // this.beatSpacing = quarterNoteWidth * 2d;

        // get the width of a beat
        String glyph = SymbolFactory.sharp() + SymbolFactory.note8thUp()
                + SymbolFactory.augmentationDot();
        Text t = new Text(glyph);
        t.setFont(this.model.getFont());
        this.beatSpacing = t.getLayoutBounds().getWidth();

        this.beatPadding = this.quarterNoteWidth;

        logger.debug("quarter note width {}", quarterNoteWidth);
        logger.debug("gclefWidth  width {}", gclefWidth);
        logger.debug("beatSpacing  width {}", beatSpacing);
    }

    /**
     * @param beat
     *            1-based beat
     * @return the x location of that beat
     */
    double getXofBeatN(double beat) {
        beat -= 1;
        if (beat < 0 || beat > this.beatRectangles.size())
            throw new IllegalArgumentException("bad beat " + beat);

        int beatIndex = (int) Math.floor(beat);
        Rectangle rect = (Rectangle) this.beatRectangles.get(beatIndex);
        logger.debug(
                "for beat {} index {} the rectangle x {} y {} w {} h {}",
                beat + 1,
                beatIndex,
                rect.getX(),
                rect.getY(),
                rect.getWidth(),
                rect.getHeight());

        double x = rect.getX();
        double mant = beat - Math.floor(beat);
        logger.debug("mant {} ", mant);
        if (mant != 0d) {
            mant = this.quantize(mant, inputBeatQuantization);
            logger.debug("man quantized {} ", mant);
            x += rect.getWidth() * mant;
        }
        logger.debug("returning x {} for beat {}", x, beat + 1);
        return x;
    }

    public double getBeatForX(double x) {
        double beat = 0d;
        for (Rectangle r : this.beatRectangles) {
            beat++;
            logger.debug(
                    "x {} for beat {} the rectangle x {} y {} w {} h {}",
                    x,
                    beat,
                    r.getX(),
                    r.getY(),
                    r.getWidth(),
                    r.getHeight());
            if (r.contains(x, model.getStaffBottom())) {
                break;
            }
        }
        return beat;
    }

    // c,1,.25 c,1.25,.25 c,1.5,.25

    double quantize(double value, double q) {
        return Math.floor(value / q) * q;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    /**
     * Determine the number of attacks in a beat. e.g. c,1,q would be one. c,1,e
     * c,1.5,3 would be two.
     * 
     * @param track
     *            a track for a beat
     * @return the number of attacks
     */
    int getNAttacks(MIDITrack track) {
        int a = 0;
        double last = 0d;
        for (MIDINote n : track) {
            if (n.getStartBeat() != last) {
                last = n.getStartBeat();
                a++;
            }
        }
        logger.debug("returning {} attacks", a);
        return a;
    }

    List<Shape> shapesAtBeat(int beat) {
        logger.debug(
                "searching for shapes at beat {} n shapes is {}",
                beat,
                this.shapes.size());
        List<Shape> list = new ArrayList<>();
        for (Shape s : this.shapes) {
            MIDINote note = (MIDINote) s.getUserData();
            if (note != null) {
                int sb = (int) Math.floor(note.getStartBeat());
                logger.debug("checking start beat {}", sb);
                if (sb == beat) {
                    list.add(s);
                }
            } else {
                logger.debug("note is null for shape {}", s);
            }
        }
        return list;
    }

    /**
     * Count the widths of shapes at beat.
     * 
     * @param beat
     *            which beat. 1 based.
     * @return the width
     */
    double getWidthOfBeat(int beat) {

        // double shapePadding = model.getFontSize() / 2d;
        double width = 0;
        // List<Shape> list = shapesAtBeat(beat);
        List<StaffSymbol> list = getSymbolsAtBeat(beat);
        logger.debug("{} shapes at beat {}", list.size(), beat);
        for (StaffSymbol s : list) {
            // width += s.getLayoutBounds().getWidth();
            width += s.getWidth();
            // width += shapePadding;
            // width += this.beatPadding;
        }
        logger.debug("returning Beat {} with width {}", beat, width);
        return width;
    }

    private void createBeatRectangles() {
        this.beatRectangles.clear();

        TimeSignature t = null;
        if (this.measure == null) {
            t = new TimeSignature(4, 4);
        } else {
            t = this.measure.getTimeSignature();
        }

        // set up beat widths. create a Rectangle for each beat.

        double x = this.model.getFirstNoteX();
        // double y = this.staffModel.getBassStaffBottom();
        double y = this.model.getTrebleStaffTop();
        double height = this.model.getBassStaffBottom()
                - this.model.getTrebleStaffTop();

        // equal size rectangles based on the width of the staff
        double beatWidth = (this.staffWidth - model.getFirstNoteX())
                / t.getNumerator();
        logger.debug(
                "beat width is {} for staff width {}",
                beatWidth,
                this.staffWidth);
        logger.debug(
                "first note x {} staffwidth -x {}",
                model.getFirstNoteX(),
                staffWidth - model.getFirstNoteX());

        for (double bbeat = 1d; bbeat <= t.getNumerator(); bbeat += 1d) {
            logger.debug("looping beat {}", bbeat);

            // nah. the beat witdh needs to be a percentage of the emtpy beat
            // width.
            // the rectangle should grow only if there are too many notes to fit
            // within the beat.

            if (this.measure != null) {
                MIDITrack nlAtBeat = this.measure.getNotesAtBeat(bbeat);

                // determine width of this beat
                // should be num different start beats within beat * spacing
                // beatWidth = (getNAttacks(nlAtBeat) + 1d) * this.beatSpacing;

                // KeySignature ks = measure.getKeySignatureAtBeat(bbeat);
                // if (ks == null) {
                // ks = KeySignature.CMAJOR;
                // }
                // for (MIDINote n : nlAtBeat) {
                // if (isAccidental(ks, n.getMidiNumber())) {
                //
                // }
                // }
            }

            Rectangle rect = new Rectangle(x, y, beatWidth, height);
            rect.setFill(Color.web("blue", 0.1));
            rect.setStroke(Color.web("blue", 0.7));
            logger.debug(
                    "rectangle x {} y {} w {} h {}",
                    rect.getX(),
                    rect.getY(),
                    rect.getWidth(),
                    rect.getHeight());
            this.beatRectangles.add(rect);
            // rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            // @Override
            // public void handle(MouseEvent me) {
            // Rectangle r = (Rectangle) me.getSource();
            // int beat = beatRectangles.indexOf(r);
            // List<MIDINote> notes = getNotesAtBeat(beat);
            // for (MIDINote note : notes) {
            //
            // }
            // }
            // });

            // this.getChildren().add(rect);
            x += beatWidth;

            // if (nlAtBeat != null && nlAtBeat.size() != 0) {
            // double shortest = nlAtBeat.getShortestDuration();
            // }
        }
    }

    protected List<MIDINote> getNotesAtBeat(int beat) {
        List<MIDINote> list = new ArrayList<>();
        return list;
    }

    public static boolean isAccidental(KeySignature ks, int pitch) {
        boolean accidental = false;
        Scale scale = getScale(ks);
        accidental = scale.isDiatonic(pitch);
        return accidental;
    }

    static Scale getScale(KeySignature ks) {
        String key;
        String name;
        if (ks.isMajor()) {
            name = "Major";
        } else {
            name = "Melodic Minor";
        }
        key = ks.getRoot().getPreferredSpelling();
        Scale scale = ScaleFactory.getScaleByKeyAndName(key, name);
        logger.debug("scale {}", scale);
        return scale;
    }

    public void setMeasureModel(MeasureModel mm) {
        this.model = mm;
        this.setMeasure(this.model.getMeasure());

        setNoteList(model.getNoteList());

        // KeySignature keysig = this.measure.getKeySignatureAtBeat(1d);

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

        model.getMeasureProperty().addListener(new ChangeListener<Measure>() {
            @Override
            public void changed(ObservableValue<? extends Measure> observable,
                    Measure oldValue, Measure newValue) {
                setMeasure(newValue);
            }

        });

        // this.drawBeatRectanglesProperty.bind(this.model.getDrawBeatRectanglesProperty());
        // this.drawTimeSignatureProperty.bind(this.model.getDrawTimeSignatureProperty());
        // this.drawClefsProperty.bind(this.model.getDrawClefsProperty());

        refresh();
    }

    /**
     * @return the shapes
     */
    public List<Shape> getShapes() {
        if (this.drawBeatRectangles) {
            shapes.addAll(this.beatRectangles);
        }
        return shapes;
    }

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

        // model.setFirstNoteX(x + advance + model.getFontSize() / 2d);
        model.setFirstNoteX(advance + model.getFontSize() / 2d);

        return advance;
    }

    public void setShowBeats(boolean showBeats) {
        this.drawBeatRectangles = showBeats;
    }

    public void setStaffWidth(double width) {
        this.staffWidth = width;
        this.staffWidthProperty.set(width);
        logger.debug("staff width set to {}", staffWidth);
    }

    double createStaves() {
        logger.debug("new staffwidth {}", this.staffWidth);
        return createStaves(this.staffWidth);
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
                x, model.getTrebleStaffBottom() - model.getLineInc()
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

            model.setFirstNoteX(clefX + trebleClefWidth + model.getFontSize()
                    / 2d);
        }

        String staff = SymbolFactory.staff5Lines();
        // double staffStringIncrement = model.getFontSize() / 2d;
        Text text = new Text(staff);
        text.setFont(font);
        double staffStringIncrement = text.getLayoutBounds().getWidth();

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
            x = drawKeySignature(x + trebleClefWidth + model.getFontSize() / 2d);
            model.setFirstNoteX(x + model.getFontSize()
                    / 2d);

        }

        return x + trebleClefWidth;
    }

    private double drawKeySignature(double x) {
        if (this.measure == null) {
            logger.debug("null measure");
            this.measure = this.model.getMeasure();
            // return x;
        }
        KeySignature ks = this.measure.getKeySignatureAtBeat(1d);

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

    /**
     * @return the drawBeatRectangles
     */
    public boolean isDrawBeatRectangles() {
        return drawBeatRectangles;
    }

    /**
     * @param drawBeatRectangles
     *            the drawBeatRectangles to set
     */
    public void setDrawBeatRectangles(boolean drawBeatRectangles) {
        this.drawBeatRectangles = drawBeatRectangles;
        refresh();
    }

    /**
     * @return the drawTimeSignature
     */
    public boolean isDrawTimeSignature() {
        return drawTimeSignature;
    }

    /**
     * @param drawTimeSignature
     *            the drawTimeSignature to set
     */
    public void setDrawTimeSignature(boolean drawTimeSignature) {
        this.drawTimeSignature = drawTimeSignature;
        refresh();
    }

    /**
     * @return the drawClefs
     */
    public boolean isDrawClefs() {
        return drawClefs;
    }

    /**
     * @param drawClefs
     *            the drawClefs to set
     */
    public void setDrawClefs(boolean drawClefs) {
        this.drawClefs = drawClefs;
        refresh();
    }

    /**
     * @return the drawKeySignature
     */
    public boolean isDrawKeySignature() {
        return drawKeySignature;
    }

    /**
     * @param drawKeySignature
     *            the drawKeySignature to set
     */
    public void setDrawKeySignature(boolean drawKeySignature) {
        this.drawKeySignature = drawKeySignature;
        refresh();
    }

    /**
     * @return the drawBeatRectanglesProperty
     */
    public BooleanProperty getDrawBeatRectanglesProperty() {
        return drawBeatRectanglesProperty;
    }

    /**
     * @param drawBeatRectanglesProperty
     *            the drawBeatRectanglesProperty to set
     */
    public void setDrawBeatRectanglesProperty(
            BooleanProperty drawBeatRectanglesProperty) {
        this.drawBeatRectanglesProperty = drawBeatRectanglesProperty;
    }

    /**
     * @return the drawTimeSignatureProperty
     */
    public BooleanProperty getDrawTimeSignatureProperty() {
        return drawTimeSignatureProperty;
    }

    /**
     * @param drawTimeSignatureProperty
     *            the drawTimeSignatureProperty to set
     */
    public void setDrawTimeSignatureProperty(
            BooleanProperty drawTimeSignatureProperty) {
        this.drawTimeSignatureProperty = drawTimeSignatureProperty;
    }

    /**
     * @return the drawClefsProperty
     */
    public BooleanProperty getDrawClefsProperty() {
        return drawClefsProperty;
    }

    /**
     * @param drawClefsProperty
     *            the drawClefsProperty to set
     */
    public void setDrawClefsProperty(BooleanProperty drawClefsProperty) {
        this.drawClefsProperty = drawClefsProperty;
    }

    /**
     * @return the drawBraceProperty
     */
    public BooleanProperty getDrawBraceProperty() {
        return drawBraceProperty;
    }

    /**
     * @param drawBraceProperty
     *            the drawBraceProperty to set
     */
    public void setDrawBraceProperty(BooleanProperty drawBraceProperty) {
        this.drawBraceProperty = drawBraceProperty;
    }

    public void setDrawBraces(boolean selected) {
        this.drawBrace = selected;
        refresh();
    }

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

        int index = (int) Math.floor(note.getStartBeat()) - 1;
        Rectangle beatRectangle = this.beatRectangles.get(index);
        logger.debug(
                "beat rect for index {} the rectangle x {} y {} w {} h {}",
                index,
                beatRectangle.getX(),
                beatRectangle.getY(),
                beatRectangle.getWidth(),
                beatRectangle.getHeight());

        // let's try without. remove calls if this works.
        beatRectangle = new Rectangle();

        // Rectangle nextBeatRectangle = null;
        // index++;
        // if (index < this.beatRectangles.size() - 1) {
        // nextBeatRectangle = this.beatRectangles.get(index);
        // logger.debug(
        // "next beat rect for index {} the rectangle x {} y {} w {} h {}",
        // index,
        // nextBeatRectangle.getX(),
        // nextBeatRectangle.getY(),
        // nextBeatRectangle.getWidth(),
        // nextBeatRectangle.getHeight());
        // }

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
            text.setUserData(note);

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

            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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
            text.setUserData(note);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);

            // get the x for the note and add it, not the dot's x
            // x += grandStaffModel.stringWidth(glyph);
            glyph = SymbolFactory.augmentationDot();
            // now add a bit of space between the note and the dot
            // x += grandStaffModel.stringWidth(glyph);
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            text.setUserData(note);

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

            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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
            text.setUserData(note);
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

            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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
            text.setUserData(note);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;
            // x += grandStaffModel.stringWidth(glyph);
            beatRectangle.setWidth(beatRectangle.getWidth() + width);

            // now add the dot
            glyph = SymbolFactory.augmentationDot();
            // space between note and dot
            x += width / 2d;
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            text.setUserData(note);

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

            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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
            text.setUserData(note);

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

            // beatRectangle.setWidth(beatRectangle.getWidth() + width);

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
            text.setUserData(note);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;

            // now add the dot
            glyph = SymbolFactory.augmentationDot();
            // space between note and dot
            x += width / 2d;
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            text.setUserData(note);
            width = text.getLayoutBounds().getWidth();
            x += width;

            beatRectangle.setWidth(beatRectangle.getWidth() + width);

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
            text.setUserData(note);
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

            text = addText(x, y, glyph);
            text.setUserData(note);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
        }

        // 16th
        if (duration - Duration.SIXTEENTH_NOTE >= 0d) {
            duration -= Duration.SIXTEENTH_NOTE;

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
            text.setUserData(note);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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
            text.setUserData(note);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
        }

        // .1875 dotted 32nd
        double d = Duration.getDotted(Duration.THIRTY_SECOND_NOTE);

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
            text.setUserData(note);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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
                addStem(center, x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note64thUp();
                } else {
                    glyph = SymbolFactory.note64thDown();
                }
            }

            text = addText(x, y, glyph);
            text.setUserData(note);
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;
            // beatRectangle.setWidth(beatRectangle.getWidth() + width);
        }

        return x;
    }

    public List<StaffSymbol> getSymbols() {
        return symbols;
    }

    /**
     * @return the staffWidthProperty
     */
    public DoubleProperty getStaffWidthProperty() {
        return staffWidthProperty;
    }
}
