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
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.fx.cmn.model.GrandStaffModel.Clef;
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

  //  private List<StaffSymbol> symbols = new ArrayList<>();

    /**
     * these shapes are filled.
     */
    private List<Shape> shapes = new ArrayList<>();
    /**
     * these shapes are stroked.
     */
    private List<Shape> strokedShapes = new ArrayList<>();

    private GrandStaffModel grandStaffModel;
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
        if (grandStaffModel == null) {
            return;
        }
        double x = grandStaffModel.getStartX() + 1d
                * grandStaffModel.getFontSize();
       // symbols.clear();
        shapes.clear();
        if (noteList == null) {
            return;
        }

        
        x += grandStaffModel.getFontSize() /2d;
        x = addTimeSignature(x, 4, 4);

        // spacing between ts and first note
        x += grandStaffModel.getFontSize() / 1d;
        for (MIDINote note : noteList) {
            x = createSymbol(note, x);
            // some padding between the symbols
            x += grandStaffModel.getFontSize() / 2d;
        }
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
        double y = grandStaffModel.getYpositionForPitch(pitch, true);
        Text text;

        if (isSpellingFlat(note)) {
            glyph = SymbolFactory.flat();
            logger.debug("is flat");
            y = grandStaffModel.getYpositionForPitch(pitch, true);
            // x += grandStaffModel.stringWidth(glyph);
           // symbols.add(new StaffSymbol(x, y, glyph));

            text = new Text(x, y, glyph);
            text.setFont(grandStaffModel.getFont());
            text.setStyle("-fx-cursor: hand; -fx-font-smoothing-type: lcd;");
            text.setFontSmoothingType(FontSmoothingType.LCD);
            text.autosize();
            x += text.getLayoutBounds().getWidth();
            shapes.add(text);
        }

        if (isSpellingSharp(note)) {
            glyph = SymbolFactory.sharp();
            logger.debug("is sharp");
            y = grandStaffModel.getYpositionForPitch(pitch, false);
            // x += grandStaffModel.stringWidth(glyph);
           // symbols.add(new StaffSymbol(x, y, glyph));

            text = new Text(x, y, glyph);
            text.setFont(grandStaffModel.getFont());
            text.setStyle("-fx-cursor: hand; -fx-font-smoothing-type: lcd;");
            text.setFontSmoothingType(FontSmoothingType.LCD);
            text.autosize();
            x += text.getLayoutBounds().getWidth();
            shapes.add(text);
        }

        double center = 0d;
        if (isTreble(pitch)) {
            center = grandStaffModel.getTrebleStaffCenter();
        } else {
            center = grandStaffModel.getBassStaffCenter();
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
          //  symbols.add(new StaffSymbol(x, y, glyph));
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

          //  symbols.add(new StaffSymbol(x, y, glyph));
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

          //  symbols.add(new StaffSymbol(x, y, glyph));
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

         //   symbols.add(new StaffSymbol(x, y, glyph));

            text = addText(x, y, glyph);

            // x += text.getBoundsInLocal().getWidth();
            // shapes.add(new Rectangle(x,y,text.getBoundsInParent().getWidth(),
            // text.getBoundsInParent().getHeight()));
            // shapes.add(new Rectangle(x,y,text.getLayoutBounds().getWidth(),
            // text.getLayoutBounds().getHeight()));

//            logger.debug("width local {} parent {} layout {} stringwidth {}",
//                    text.getBoundsInLocal().getWidth(),
//                    text.getBoundsInParent().getWidth(),
//                    text.getLayoutBounds().getWidth(),
//                    grandStaffModel.stringWidth(glyph)
//                    );

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
          //  symbols.add(new StaffSymbol(x, y, glyph));
            text = addText(x, y, glyph);
            x += text.getLayoutBounds().getWidth();

            logger.debug("dotted eighth note. remainder {}", duration);

        }

        //TODO
        // quarter triplet
        double qtriplet = 2d / 3d;
        if (duration - qtriplet >= 0d) {
            duration -= qtriplet;
            if (stemUp) {
                glyph = SymbolFactory.noteQuarterUp();
            } else {
                glyph = SymbolFactory.noteQuarterDown();
            }

          //  symbols.add(new StaffSymbol(x, y, glyph));
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
          //  symbols.add(new StaffSymbol(x, y, glyph));
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

          //  symbols.add(new StaffSymbol(x, y, glyph));
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
        //    symbols.add(new StaffSymbol(x, y, glyph));
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
            // x += grandStaffModel.stringWidth(glyph);
         //   symbols.add(new StaffSymbol(x, y, glyph));
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
        slur.setStartY(y + grandStaffModel.getLineInc());
        slur.setEndY(y + grandStaffModel.getLineInc());
        // control x is set when added
        // slur.setControlY(y + 25d); // + is under
        slur.setControlY(y + grandStaffModel.getLineInc() * 2d); // + is under
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
        slur.setStartY(y - grandStaffModel.getLineInc());
        slur.setEndY(y - grandStaffModel.getLineInc());
        // control x is set when added
        slur.setControlY(y - grandStaffModel.getLineInc() * 2d); // + is under
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

        double fy = grandStaffModel.getStaffCenterLine() + p.getY();
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

        double fy = grandStaffModel.getStaffCenterLine() + p.getY();
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
            Line line = new Line(lx, ly, lx,center);
                    
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
        text.setFont(grandStaffModel.getFont());
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

        double lineinc = grandStaffModel.getLineInc();
        double staffBottom = 0d;
        double staffTop = 0d;
        boolean useFlat = true;

        // lacking fontmetrics, we guess at centering the ledger
        // double lx = grandStaffModel.getFontSize() / 4.3;
        //double lx = grandStaffModel.stringWidth(line) / 4d;
        double lx = 0d;
        
       // double width = line.getLayoutBounds().getWidth();

        if (isSpellingFlat(note)) {
            useFlat = true;
        }
        if (isSpellingSharp(note)) {
            useFlat = false;
        }

        if (isTreble(pitch)) {
            staffBottom = grandStaffModel.getTrebleStaffBottom();
            staffTop = grandStaffModel.getTrebleStaffTop();
            if (pitch < Pitch.CS5) {
                int nledgers = grandStaffModel.getNumberOfLedgers(pitch,
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
                    //StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    //symbols.add(symbol);
                    Text text = addText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x-lx);

                }
            }

            if (pitch > Pitch.FS6) {
                int nledgers = grandStaffModel.getNumberOfLedgers(pitch,
                        useFlat, Clef.TREBLE);
                logger.debug("there are {} ledgers for pitch {}",
                        nledgers, pitch);

                for (int i = 0; i < nledgers; i++) {
                    double ly = (staffTop - lineinc - lineinc * i);
                    ly += lineinc * 2d; // 1linestaff kludge
                    logger.debug("ledger y {}", ly);
//                    StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    //symbols.add(symbol);
                    
                    Text text = addText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x-lx);
                    
                }
            }
        } else {
            staffBottom = grandStaffModel.getBassStaffBottom();
            staffTop = grandStaffModel.getBassStaffTop();
            if (pitch < Pitch.F3) {
                int nledgers = grandStaffModel.getNumberOfLedgers(pitch,
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
                    //StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    //symbols.add(symbol);
                    //addText(x - lx, ly, line);
                    Text text = addText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x-lx);
                }
            }

            if (pitch > Pitch.B4) {
                int nledgers = grandStaffModel.getNumberOfLedgers(pitch,
                        useFlat, Clef.BASS);
                logger.debug("there are {} ledgers for pitch {}",
                        nledgers, pitch);

                for (int i = 0; i < nledgers; i++) {
                    double ly = (staffTop - lineinc - lineinc * i);
                    ly += lineinc * 2d; // 1linestaff kludge
                    logger.debug("ledger y {}", ly);
                    //StaffSymbol symbol = new StaffSymbol(x - lx, ly, line);
                    //symbols.add(symbol);
                    //addText(x - lx, ly, line);
                    Text text = addText(x - lx, ly, line);
                    double width = text.getLayoutBounds().getWidth();
                    lx = width / 4d;
                    text.setX(x-lx);
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
        return grandStaffModel;
    }

    // metrics
    double quarterNoteWidth;
    double gclefWidth;
    double beatSpacing;
    double timeSignatureWidth;

    void calcMetrics() {
        // do some metrics
        Text text = new Text(SymbolFactory.noteQuarterUp());
        text.setFont(grandStaffModel.getFont());
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
        this.grandStaffModel = grandgrandStaffModel;

        setNoteList(grandStaffModel.getNoteList());

        calcMetrics();

        grandStaffModel.getFontSizeProperty().addListener(
                new ChangeListener<Number>() {

                    @Override
                    public void changed(
                            ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        logger.debug("new font size {}", newValue);
                        calcMetrics();
                    }
                });

        grandStaffModel.getNoteListProperty().addListener(
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

        grandStaffModel.getTrackProperty().addListener(
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
//    public List<StaffSymbol> getSymbols() {
//        return symbols;
//    }

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
        numerator.setFont(grandStaffModel.getFont());
        numerator.setFontSmoothingType(FontSmoothingType.LCD);
        denomenator.setFont(grandStaffModel.getFont());
        denomenator.setFontSmoothingType(FontSmoothingType.LCD);
        
        numerator1.setFont(grandStaffModel.getFont());
        numerator1.setFontSmoothingType(FontSmoothingType.LCD);
        denomenator1.setFont(grandStaffModel.getFont());
        denomenator1.setFontSmoothingType(FontSmoothingType.LCD);

        double numwidth = numerator.getLayoutBounds().getWidth();
        double denomwidth = denomenator.getLayoutBounds().getWidth();
        double offset = 0d;
        
        double ty = grandStaffModel.getTrebleStaffBottom();
        double by = grandStaffModel.getBassStaffBottom();
        
        if (numwidth > denomwidth) {
            offset = numwidth / 2d;
            offset -= denomwidth / 2d;
            numerator.setX(x);
            numerator.setY(ty - 3d * grandStaffModel.getLineInc());
            denomenator.setX(x + offset);
            denomenator.setY(ty - 1d * grandStaffModel.getLineInc());
            
            numerator1.setX(x);
            numerator1.setY(by - 3d * grandStaffModel.getLineInc());
            denomenator1.setX(x + offset);
            denomenator1.setY(by - 1d * grandStaffModel.getLineInc());
            
            advance += numwidth;
           
            
            
        } else if (numwidth < denomwidth) {
            offset = denomwidth / 2d;
            offset -= numwidth / 2d;
            numerator.setX(x + offset);
            numerator.setY(ty - 3d * grandStaffModel.getLineInc());
            denomenator.setX(x);
            denomenator.setY(ty - 1d * grandStaffModel.getLineInc());
            
            numerator1.setX(x + offset);
            numerator1.setY(by - 3d * grandStaffModel.getLineInc());
            denomenator1.setX(x);
            denomenator1.setY(by - 1d * grandStaffModel.getLineInc());
            advance += numwidth;
            
            
        } else if (numwidth == denomwidth) {
            numerator.setX(x);
            numerator.setY(ty - 3d * grandStaffModel.getLineInc());
            denomenator.setX(x);
            denomenator.setY(ty - 1d * grandStaffModel.getLineInc());
            
            numerator1.setX(x);
            numerator1.setY(by - 3d * grandStaffModel.getLineInc());
            denomenator1.setX(x);
            denomenator1.setY(by - 1d * grandStaffModel.getLineInc());
            advance += numwidth;
            
            
        }
        shapes.add(numerator);
        shapes.add(denomenator);
        shapes.add(numerator1);
        shapes.add(denomenator1);
       
        return advance;
    }

}