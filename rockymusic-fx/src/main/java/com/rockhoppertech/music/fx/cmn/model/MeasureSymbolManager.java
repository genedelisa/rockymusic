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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
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
    private boolean drawBeatRectangles = true;
    private double inputBeatQuantization = .125d;
    // metrics
    double quarterNoteWidth;
    double gclefWidth;
    double beatSpacing;
    double timeSignatureWidth;

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
        
        
        double x = model.getStartX() + 1d
                * model.getFontSize();

        shapes.clear();
        createStaves();

        x += model.getFontSize() / 2d;
        x = addTimeSignature(x, 4, 4);

        // spacing between ts and first note
        x += model.getFontSize() / 1d;

        model.setFirstNoteX(x);
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

            x = getXofBeatN(note.getStartBeat());

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

            x = createSymbol(note, x);
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

        int index = (int) Math.floor(note.getStartBeat()) - 1;
        Rectangle beatRectangle = this.beatRectangles.get(index);
        logger.debug(
                "beat rect for index {} the rectangle x {} y {} w {} h {}",
                index,
                beatRectangle.getX(),
                beatRectangle.getY(),
                beatRectangle.getWidth(),
                beatRectangle.getHeight());
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

            beatRectangle.setWidth(beatRectangle.getWidth() + width);

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
            double width = text.getLayoutBounds().getWidth();
            x += width;

            // now add the dot
            glyph = SymbolFactory.augmentationDot();
            // space between note and dot
            x += width / 2d;
            // symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
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
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
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
            addLedgers(note, x);
            double width = text.getLayoutBounds().getWidth();
            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
        }

        // 32nd
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
            double width = text.getLayoutBounds().getWidth();
            x += width;
            beatRectangle.setWidth(beatRectangle.getWidth() + width);
        }

        // push all the x locations to be the previous x + width
        normalizeRectangles();

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

    double getXofBeatN(double beat) {
        beat -= 1;
        if (beat < 0 || beat > this.beatRectangles.size())
            throw new IllegalArgumentException("bad beat " + beat);

        int beatIndex = (int) Math.floor(beat);
        Rectangle rect = (Rectangle) this.beatRectangles.get(beatIndex);
        logger.debug(
                "for beat {} index {} the rectangle x {} y {} w {} h {}",
                beat,
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
            logger.debug("man quantizedt {} ", mant);
            x += rect.getWidth() * mant;
        }
        logger.debug("returning x {} for beat {}", x, beat);
        return x;
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

        // double lastBeat = 0;
        double beatWidth = this.beatSpacing;
        
        beatWidth = (staffWidth - model.getFirstNoteX()) / t.getNumerator();
        //beatWidth = (staffWidth - model.getStartX()) / t.getNumerator();

        for (double bbeat = 1d; bbeat <= t.getNumerator(); bbeat += 1d) {
            logger.debug("looping beat {}", bbeat);

            if (this.measure != null) {
                MIDITrack nlAtBeat = this.measure.getNotesAtBeat(bbeat);

                // determine width of this beat
                // should be num different start beats within beat * spacing
                beatWidth = (getNAttacks(nlAtBeat) + 1d) * this.beatSpacing;

                KeySignature ks = measure.getKeySignatureAtBeat(bbeat);
                if (ks == null) {
                    ks = KeySignature.CMAJOR;
                }
                for (MIDINote n : nlAtBeat) {
                    if (isAccidental(ks, n.getMidiNumber())) {
                        // now what
                    }
                }
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

            // this.getChildren().add(rect);
            x += beatWidth;

            // if (nlAtBeat != null && nlAtBeat.size() != 0) {
            // double shortest = nlAtBeat.getShortestDuration();
            // }
        }
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

        // get the width of a beat
        String glyph = SymbolFactory.sharp() + SymbolFactory.note8thUp()
                + SymbolFactory.augmentationDot();
        Text t = new Text(glyph);
        t.setFont(this.model.getFont());
        this.beatSpacing = t.getLayoutBounds().getWidth();

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

        return advance;
    }

    public void setShowBeats(boolean showBeats) {
        this.drawBeatRectangles = showBeats;
    }

    double staffWidth = 250d;

    public void setStaffWidth(double width) {
        this.staffWidth = width;
    }

    void createStaves() {
        createStaves(this.staffWidth);
    }

    void createStaves(double staffWidth) {
        logger.debug("drawing the staves {}", staffWidth);
        double x = model.getStartX();
        double y = model.getStaffBottom();
        double yspacing = model.getYSpacing();
        Font font = model.getFont();

        y = model.getBassStaffBottom();
        Text brace = new Text(x, y,
                SymbolFactory.brace());
        brace.setScaleY(2.8);
        brace.setScaleX(3d);
        brace.setFont(font);
        brace.setFontSmoothingType(FontSmoothingType.LCD);
        this.shapes.add(brace);
        x += (4d * brace.getLayoutBounds().getWidth());

        Line barline = new Line(x, model.getBassStaffBottom(),
                x, model.getTrebleStaffBottom() - model.getLineInc()
                        * 4);
        this.shapes.add(barline);
        x += (barline.getLayoutBounds().getWidth());

        // just some spacing
        // x += staffModel.getFontSize();
        y = model.getTrebleStaffBottom();
        // double clefX = x+10d;
        double clefX = x + model.getFontSize() / 4d;
        Text trebleClef = new Text(clefX, y - (yspacing * 2d),
                SymbolFactory.gClef());
        trebleClef.setFont(font);
        trebleClef.setFontSmoothingType(FontSmoothingType.LCD);
        this.shapes.add(trebleClef);

        String staff = SymbolFactory.staff5Lines();
        double staffStringIncrement = model.getFontSize() / 2d;

        Text text;

        // draw the treble staff
        for (double xx = x; xx < staffWidth; xx += staffStringIncrement) {
            text = new Text(xx, y, staff);
            text.setFont(font);
            this.shapes.add(text);
        }

        y = model.getBassStaffBottom();
        Text bassClef = new Text(clefX, y - (yspacing * 6d),
                SymbolFactory.fClef());
        bassClef.setFont(font);
        bassClef.setFontSmoothingType(FontSmoothingType.LCD);
        this.shapes.add(bassClef);

        // draw the bass staff
        for (double xx = x; xx < staffWidth; xx += staffStringIncrement) {
            text = new Text(xx, y, staff);
            text.setFont(font);
            this.shapes.add(text);
        }
    }
}