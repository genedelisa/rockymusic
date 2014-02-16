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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFormat;
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

    private List<StaffSymbol> symbols = new ArrayList<>();

    /**
     * these shapes are filled.
     */
    private List<Shape> shapes = new ArrayList<>();
    /**
     * these shapes are stroked.
     */
    private List<Shape> strokedShapes = new ArrayList<>();
    private StaffModel staffModel;
    private ObservableList<MIDINote> noteList;

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
     * Clear out the existing shapes, then calculate and add new ones.
     */
    public void refresh() {
        if (staffModel == null) {
            return;
        }
        double x = staffModel.getStartX() + 1d * staffModel.getFontSize();
        symbols.clear();
        shapes.clear();
        if (noteList == null) {
            return;
        }
        for (MIDINote note : noteList) {
            x = createSymbol(note, x);
            // some padding between the symbols
            x += staffModel.getFontSize() / 2d;
        }
    }

    // public static final void setMIDITrack(MIDITrack track) {
    // double x = staffModel.getStartX() + 1d * staffModel.getFontSize();
    // symbols.clear();
    // for (MIDINote note : track) {
    //
    // x = createSymbol(note, x);
    // // damn. the ledger will be on an augmentation dot since the
    // // returned x includes that.
    // // addLedgers(note, x);
    //
    // // some padding between the symbols
    // x += staffModel.getFontSize() / 2d;
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

        // 1 get accidental
        // 2 determine stem direction
        // 3 get duration

        // for non accidentals.
        double y = staffModel.getYpositionForPitch(pitch, true);
        Text text;

        if (isSpellingFlat(note)) {
            glyph = SymbolFactory.flat();
            logger.debug("is flat");
            y = staffModel.getYpositionForPitch(pitch, true);
            // x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));

            text = new Text(x, y, glyph);
            text.setFont(staffModel.getFont());
            text.setStyle("-fx-cursor: hand; -fx-font-smoothing-type: lcd;");
            text.setFontSmoothingType(FontSmoothingType.LCD);
            text.autosize();
            x += text.getLayoutBounds().getWidth();
            shapes.add(text);
        }

        if (isSpellingSharp(note)) {
            glyph = SymbolFactory.sharp();
            logger.debug("is sharp");
            y = staffModel.getYpositionForPitch(pitch, false);
            // x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));

            text = new Text(x, y, glyph);
            text.setFont(staffModel.getFont());
            text.setStyle("-fx-cursor: hand; -fx-font-smoothing-type: lcd;");
            text.setFontSmoothingType(FontSmoothingType.LCD);
            text.autosize();
            x += text.getLayoutBounds().getWidth();
            shapes.add(text);
        }

        double center = staffModel.getStaffCenterLine();
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
            // x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
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
            x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();

            // get the x for the note and add it, not the dot's x
            // x += staffModel.stringWidth(glyph);
            glyph = SymbolFactory.augmentationDot();
            // now add a bit of space between the note and the dot
            // x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
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

        // half
        if (duration - 2d >= 0d) {
            duration -= 2d;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadHalf();
                addStemHalf(x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteHalfUp();
                } else {
                    glyph = SymbolFactory.noteHalfDown();
                }
            }

            symbols.add(new StaffSymbol(x, y, glyph));
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
                addStem(x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();
            // x += staffModel.stringWidth(glyph);

            // now add the dot
            glyph = SymbolFactory.augmentationDot();
            // space between note and dot
            x += text.getLayoutBounds().getWidth() / 2d;
            symbols.add(new StaffSymbol(x, y, glyph));
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
                addStem(x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.noteQuarterUp();
                } else {
                    glyph = SymbolFactory.noteQuarterDown();
                }
            }

            logger.debug("quarter note. remainder {}", duration);

            symbols.add(new StaffSymbol(x, y, glyph));

            text = addText(x, y, glyph);

            // x += text.getBoundsInLocal().getWidth();
            // shapes.add(new Rectangle(x,y,text.getBoundsInParent().getWidth(),
            // text.getBoundsInParent().getHeight()));
            // shapes.add(new Rectangle(x,y,text.getLayoutBounds().getWidth(),
            // text.getLayoutBounds().getHeight()));

            logger.debug("width local {} parent {} layout {} stringwidth {}",
                    text.getBoundsInLocal().getWidth(),
                    text.getBoundsInParent().getWidth(),
                    text.getLayoutBounds().getWidth(),
                    staffModel.stringWidth(glyph)
                    );

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

            // x += staffModel.stringWidth(glyph);
        }

        // dotted eighth
        if (duration - .75 >= 0d) {
            duration -= .75;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(x, y, stemUp);
                add8thFlag(x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            // x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();

            // now add the dot
            glyph = SymbolFactory.augmentationDot();
            // space between note and dot
            x += text.getLayoutBounds().getWidth() / 2d;
            symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            x += text.getLayoutBounds().getWidth();

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
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();
        }

        // eighth
        if (duration - .5 >= 0d) {
            duration -= .5;
            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(x, y, stemUp);
                add8thFlag(x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note8thUp();
                } else {
                    glyph = SymbolFactory.note8thDown();
                }
            }

            logger.debug("eighth note. remainder {}", duration);
            symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            // x += staffModel.stringWidth(glyph);
            x += text.getLayoutBounds().getWidth();
        }

        // 16th
        if (duration - .25 >= 0d) {
            duration -= .25;

            // if the pitch is more than an octave from the center line, draw a
            // notehead and a stem.
            if (shouldDrawStem(pitch)) {
                glyph = SymbolFactory.noteheadBlack();
                addStem(x, y, stemUp);
                add16thFlag(x, y, stemUp);
            } else {
                if (stemUp) {
                    glyph = SymbolFactory.note16thUp();
                } else {
                    glyph = SymbolFactory.note16thDown();
                }
            }

            symbols.add(new StaffSymbol(x, y, glyph));
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
            // x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();
        }

        // 32nd
        if (duration - .1875 >= 0) {
            duration -= .1875;
            if (stemUp) {
                glyph = SymbolFactory.noteheadBlack();
            } else {
                glyph = SymbolFactory.noteheadBlack();
            }
            // x += staffModel.stringWidth(glyph);
            symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            addLedgers(note, x);
            x += text.getLayoutBounds().getWidth();
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
        slur.setStartY(y + staffModel.getLineInc());
        slur.setEndY(y + staffModel.getLineInc());
        // control x is set when added
        // slur.setControlY(y + 25d); // + is under
        slur.setControlY(y + staffModel.getLineInc() * 2d); // + is under
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
        //slur.setStartY(y + staffModel.getYSpacing());
        slur.setStartY(y - staffModel.getLineInc());
        slur.setEndY(y - staffModel.getLineInc());
        // control x is set when added
        slur.setControlY(y - staffModel.getLineInc() * 2d); // + is under
        slur.setFill(null);
        slur.setStroke(Color.BLACK);
        slur.setStrokeWidth(1d);
        return slur;
    }
    
//    private void endTieOver(double x, double y, QuadCurve slur, double width) {
//        double endx = x - width / 2d;
//        slur.setEndX(endx);
//        slur.setEndY(y);
//        // the center of the slur
//        double cx = slur.getStartX() + (endx - slur.getStartX()) / 2d;
//        slur.setControlX(cx);
//        shapes.add(slur);
//    }

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

        double fy = staffModel.getStaffCenterLine() + p.getY();
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

        double fy = staffModel.getStaffCenterLine() + p.getY();
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

    private void addStemHalf(double x, double y, boolean stemUp) {
        if (stemUp) {
            Point2D p = SymbolFactory.getStemUpSE("noteheadHalf");
            double lx = x + p.getX() + this.quarterNoteWidth;
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, staffModel.getStaffCenterLine());
            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            shapes.add(line);
        } else {
            Point2D p = SymbolFactory.getStemDownNW("noteheadHalf");
            double lx = x + p.getX();
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, staffModel.getStaffCenterLine());
            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            shapes.add(line);
        }
    }

    private void addStem(double x, double y, boolean stemUp) {
        if (stemUp) {
            Point2D p = SymbolFactory.getStemUpSE("noteheadBlack");
            double lx = x + p.getX() + this.quarterNoteWidth;
            // double lx = x + p.getX();
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, staffModel.getStaffCenterLine());
            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            shapes.add(line);
            logger.debug(
                    "added stem x {} y {}, center {}",
                    lx,
                    ly,
                    staffModel.getStaffCenterLine());
        } else {
            Point2D p = SymbolFactory.getStemDownNW("noteheadBlack");
            double lx = x + p.getX();
            double ly = y + p.getY();
            Line line = new Line(lx, ly, lx, staffModel.getStaffCenterLine());
            // line.setStrokeWidth(SymbolFactory.getStemThickness());
            shapes.add(line);
            logger.debug(
                    "added stem x {} y {}, center {}",
                    lx,
                    ly,
                    staffModel.getStaffCenterLine());
        }
        logger.debug("added stem");
    }

    /*
     * "noteheadBlack": { "stemDownNW": [ 0.0, -0.184 ], "stemUpSE": [ 1.328,
     * 0.184 ] },
     */

    private Text addText(double x, double y, String glyph) {
        Text text;
        text = new Text(x, y, glyph);
        text.setFont(staffModel.getFont());
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.autosize();
        shapes.add(text);
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
        if (staffModel.getClef() == Clef.TREBLE) {
            if (pitch > Pitch.C7) {
                draw = true;
            }
            if (pitch < Pitch.B4) {
                draw = true;
            }
        }
        if (staffModel.getClef() == Clef.BASS) {

            if (pitch > Pitch.D5) {
                draw = true;
            }
            if (pitch < Pitch.D3) {
                draw = true;
            }
            logger.debug("bass clef pith {} draw {}", pitch, draw);
        }
        if (staffModel.getClef() == Clef.ALTO) {

            if (pitch > Pitch.C6) {
                draw = true;
            }
            if (pitch < Pitch.C4) {
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
                    addText(x - lx, ly, line);
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
                    addText(x - lx, ly, line);
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
                    addText(x - lx, ly, line);
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
                    addText(x - lx, ly, line);
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
                    addText(x - lx, ly, line);
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
                    addText(x - lx, ly, line);
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
    public StaffModel getStaffModel() {
        return staffModel;
    }

    // metrics
    double quarterNoteWidth;
    double gclefWidth;
    double beatSpacing;
    double timeSignatureWidth;

    void calcMetrics() {
        // do some metrics
        Text text = new Text(SymbolFactory.noteQuarterUp());
        text.setFont(staffModel.getFont());
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

    /**
     * @param staffModel
     *            the staffModel to set
     */
    public void setStaffModel(StaffModel staffModel) {
        this.staffModel = staffModel;

        setNoteList(staffModel.getNoteList());

        calcMetrics();

        staffModel.getFontSizeProperty().addListener(
                new ChangeListener<Number>() {

                    @Override
                    public void changed(
                            ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        logger.debug("new font size {}", newValue);
                        calcMetrics();
                    }
                });

        staffModel.getNoteListProperty().addListener(
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

        staffModel.getTrackProperty().addListener(
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
    public List<StaffSymbol> getSymbols() {
        return symbols;
    }

    /**
     * @return the shapes
     */
    public List<Shape> getShapes() {
        return shapes;
    }

    /**
     * @return the strokedShapes
     */
    public List<Shape> getStrokedShapes() {
        return strokedShapes;
    }

    /*
     * fontMap.put(DOT, new Character((char) 46));
     * 
     * fontMap.put(LEDGERLINE, new Character((char) 94));
     */
}
