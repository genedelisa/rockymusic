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
import java.util.Locale;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.midi.js.MIDINote;

import static com.rockhoppertech.music.Pitch.*;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class InputStaffModel {
    final static Logger logger = LoggerFactory.getLogger(InputStaffModel.class);

    public enum Clef {
        TREBLE, BASS, ALTO, TENOR, SOPRANO, MEZZO_SOPRANO, BARITONE, SUB_BASS
    }

    /**
     * The font size.
     */
    private double fontSize;

    /**
     * The music font.
     */
    private Font font;

    /**
     * Which clef is this staff using.
     */
    private Clef clef = Clef.TREBLE;

    /**
     * The x location of where to draw the staff.
     */
    private double startX;
    // metrics
    /**
     * y distance between lines.
     */
    private double lineInc;
    /**
     * from space to line and vice versa
     */
    private double yspacing;

    /**
     * set to trebleFlatYpositions or bassFlatYpositions depending on clef.
     */
    private int[] flatYpositions;

    /**
     * set to trebleSharpYpositions or bassSharpYpositions depending on clef
     */
    private int[] sharpYpositions;

    private final int[] trebleFlatYpositions = new int[127];
    private final int[] trebleSharpYpositions = new int[127];
    private final int[] bassFlatYpositions = new int[127];
    private final int[] bassSharpYpositions = new int[127];
    private final int[] altoFlatYpositions = new int[127];
    private final int[] altoSharpYpositions = new int[127];

    /**
     * y location of the staff bottom line. This is set in
     * {@link #setFontSize(double)}
     */
    private double staffBottom;
    private double staffTop;
    private double staffCenter;

    private DoubleProperty fontSizeProperty;

    private double firstNoteX;

    private DoubleProperty staffWidthProperty = new SimpleDoubleProperty();

    private double beginningBarlineX;
    
    private StaffSymbol staffSymbol;

    private List<Shape> shapes;
    

    private ObjectProperty<MIDINote> noteProperty = new SimpleObjectProperty<>();

    public ObjectProperty<MIDINote> noteProperty() {
        return noteProperty;
    }

    public void setNote(MIDINote note) {
        this.noteProperty.set(note);
    }

    public MIDINote getNote() {
        return this.noteProperty.get();
    }

    public DoubleProperty staffWidthProperty() {
        return staffWidthProperty;
    }
    
    private IntegerProperty pitchProperty = new SimpleIntegerProperty(60);
    public IntegerProperty pitchProperty() {
        return pitchProperty;
    }
    public void setPitch(int p) {
        pitchProperty.set(p);
    }
    public int getPitch() {
        return pitchProperty.get();
    }

    public InputStaffModel() {
        this.shapes = new ArrayList<>();
        this.setNote( new MIDINote(Pitch.C6));
        
        fontSize = 48d;
        font = Font.loadFont(
                InputStaffModel.class.getResource("/fonts/Bravura.otf")
                        .toExternalForm(),
                fontSize);
        if (font == null) {
            throw new IllegalStateException("music font not found");
        }

        this.startX = 10d;
        this.fontSizeProperty = new SimpleDoubleProperty(this.fontSize);
        this.setFontSize(fontSize);
        this.setClef(Clef.TREBLE);

        //this.createStaves(fontSize * 3);
        //this.createSymbol(this.getNote());
        
        
        this.noteProperty.addListener(new ChangeListener<MIDINote>(){
            @Override
            public void changed(ObservableValue<? extends MIDINote> arg0,
                    MIDINote arg1, MIDINote newNote) {
                logger.debug("note changed new note {}", newNote);
            }});

        this.pitchProperty.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                getNote().setMidiNumber(newValue.intValue());
                updateSymbol();
            }});
    }
    

    /**
     * Called from calcStaffMetrics to fill in the y position array.
     * 
     * @param num
     * @param sharps
     * @return a y location
     */
    public int bassMidiNumToY(final int num, final boolean sharps) {
        // c df d ef e f gf g af a bb b
        final int[] whichLineFlat = { 3, 4, 4, 5, 5, 6, 7, 7, 8, 8, 9, 9 };
        // c cs d ds e f fs g gs a as b
        final int[] whichLineSharp = { 3, 3, 4, 4, 5, 6, 6, 7, 7, 8, 8, 9 };
        final int[] octaveOffset = { -28, -21, -14, -7, 0, 7, 14, 21, 28, 35,
                42 };

        double theY = 0;
        if (sharps) {
            theY = this.staffBottom
                    - (whichLineSharp[num % 12] * this.yspacing)
                    - (octaveOffset[num / 12] * this.yspacing);
        } else {
            theY = this.staffBottom
                    - (whichLineFlat[num % 12] * this.yspacing)
                    - (octaveOffset[num / 12] * this.yspacing);
        }
        return (int) theY;
    }

    /**
     * Calculate metrics based on font size.
     */
    public void calcStaffMetrics() {
        this.lineInc = this.fontSize / 4d;
        this.yspacing = this.fontSize / 8d;
        this.staffTop = this.staffBottom - this.lineInc * 4d;
        this.staffCenter = this.staffBottom - this.lineInc * 2d;

        for (int i = 0; i < 127; i++) {
            this.trebleFlatYpositions[i] = this.trebleMidiNumToY(i,
                    false);
            this.trebleSharpYpositions[i] = this.trebleMidiNumToY(i,
                    true);
            this.bassFlatYpositions[i] = this.bassMidiNumToY(i,
                    false);
            this.bassSharpYpositions[i] = this.bassMidiNumToY(i,
                    true);
            this.altoFlatYpositions[i] = this.altoMidiNumToY(i,
                    false);
            this.altoSharpYpositions[i] = this.altoMidiNumToY(i,
                    true);
        }
    }

    /**
     * @return the clef
     */
    public Clef getClef() {
        return this.clef;
    }

    /**
     * @return the fontSize
     */
    public double getFontSize() {
        return fontSize;
    }

    /**
     * The y spacing between lines. Fontsize / 4.
     * 
     * @return the line increment
     */
    public double getLineInc() {
        return this.lineInc;
    }

    /**
     * Determine the number of ledger lines required by the pitch. Takes the
     * current clef into account.
     * 
     * @param pitch
     *            the MIDI pitch number
     * @param useFlat
     *            is the spelling flat or sharp
     * @return the number of ledger lines
     */
    public int getNumberOfLedgers(final int pitch, final boolean useFlat) {
        int num = 0;
        if (this.clef == Clef.TREBLE) {
            if (useFlat) {
                num = TrebleNote.trebleLedgersFlat[pitch];
            } else {
                num = TrebleNote.trebleLedgersSharp[pitch];
            }
        } else if (this.clef == Clef.BASS) {
            if (useFlat) {
                num = BassNote.bassLedgersFlat[pitch];
            } else {
                num = BassNote.bassLedgersSharp[pitch];
            }
        } else if (this.clef == Clef.ALTO) {
            if (useFlat) {
                num = AltoNote.altoLedgersFlat[pitch];
            } else {
                num = AltoNote.altoLedgersSharp[pitch];
            }
        }
        // TODO other clefs
        return num;
    }

    public int getNumberOfLedgers(final int pitch, final boolean useFlat,
            Clef clef) {
        int num = 0;
        if (clef == Clef.TREBLE) {
            if (useFlat) {
                num = TrebleNote.trebleLedgersFlat[pitch];
            } else {
                num = TrebleNote.trebleLedgersSharp[pitch];
            }
        } else if (clef == Clef.BASS) {
            if (useFlat) {
                num = BassNote.bassLedgersFlat[pitch];
            } else {
                num = BassNote.bassLedgersSharp[pitch];
            }
        } else if (clef == Clef.ALTO) {
            if (useFlat) {
                num = AltoNote.altoLedgersFlat[pitch];
            } else {
                num = AltoNote.altoLedgersSharp[pitch];
            }
        }
        // TODO other clefs
        return num;
    }

    /**
     * @return the sharpYpositions
     */
    public int[] getSharpYpositions() {
        return this.sharpYpositions;
    }

    /**
     * @return the staff Bottom Line
     */
    public double getStaffBottom() {
        return this.staffBottom;
    }

    /**
     * @return the staffTop
     */
    public double getStaffTop() {
        return this.staffTop;
    }

    /**
     * @return the startX
     */
    public double getStartX() {
        return this.startX;
    }

    /**
     * Get the y location for a given MIDI pitch number. If the pitch is middle
     * C or higher, it is on the treble staff. Otherwise it is on the bass
     * staff.
     * 
     * The positions are set according to clef.
     * 
     * @param pitch
     *            is a MIDI pitch value 0-127
     * @param useFlat
     *            if true ,return flat positon, else the sharp position
     * @return
     */
    public double getYpositionForPitch(final int pitch, final boolean useFlat) {
        double y = 0d;
        // if (pitch >= Pitch.C5) {
        // if (useFlat) {
        // y = this.trebleFlatYpositions[pitch];
        // } else {
        // y = this.trebleSharpYpositions[pitch];
        // }
        // } else {
        // if (useFlat) {
        // y = this.bassFlatYpositions[pitch];
        // } else {
        // y = this.bassSharpYpositions[pitch];
        // }
        // }
        if (useFlat) {
            y = this.trebleFlatYpositions[pitch];
        } else {
            y = this.trebleSharpYpositions[pitch];
        }

        return y;
    }

    /**
     * Set the clef and also set the appropriate yposition arrays.
     * 
     * @param clef
     *            the clef to set
     */
    public void setClef(final Clef clef) {
        this.clef = clef;
        if (this.clef == Clef.TREBLE) {
            this.flatYpositions = this.trebleFlatYpositions;
            this.sharpYpositions = this.trebleSharpYpositions;
        } else if (this.clef == Clef.BASS) {
            this.flatYpositions = this.bassFlatYpositions;
            this.sharpYpositions = this.bassSharpYpositions;
        } else if (this.clef == Clef.ALTO) {
            this.flatYpositions = this.altoFlatYpositions;
            this.sharpYpositions = this.altoSharpYpositions;
        } else {
            throw new IllegalArgumentException("Clef not supported yet");
        }

    }

    /**
     * Set the font size and kick off recalculating staff metrics.
     * 
     * Sets treble staffbottom to be 4 x font size.
     * 
     * @param fontSize2
     *            the fontSize to set
     */
    public void setFontSize(final double fontSize2) {
        this.shapes.clear();
        this.fontSize = fontSize2;
        this.font = new Font("Bravura", fontSize);
        // set the font before the fontsize property for listeners who use the
        // font
        this.fontSizeProperty.setValue(this.fontSize);

        //this.staffBottom = (5d * this.fontSize) - lineInc; // so middle c is the
        this.staffBottom = (5d * this.fontSize); // so middle c is the

        this.lineInc = this.fontSize / 4d;

        this.calcStaffMetrics();
        setClef(this.clef);
        this.createStaves(fontSize * 3);
        this.createSymbol(this.getNote());
    }

    /**
     * @param sharpYpositions
     *            the sharpYpositions to set
     */
    public void setSharpYpositions(final int[] sharpYpositions) {
        this.sharpYpositions = sharpYpositions;
    }

    /**
     * @param staffBottomLine
     *            the staffBottomLine to set
     */
    public void setStaffBottomLine(final int staffBottomLine) {
        this.staffBottom = staffBottomLine;
        this.calcStaffMetrics();
    }

    /**
     * @param startX
     *            the startX to set
     */
    public void setStartX(final float startX) {
        this.startX = startX;
    }

    /**
     * Called from calcStaffMetrics to fill in the y position array.
     * 
     * @param num
     *            MIDI pitch number
     * @param sharps
     *            spelled with sharp?
     * @return a y location
     */
    public int trebleMidiNumToY(final int num, final boolean sharps) {
        // the "which" arrays are by pitch class
        // e.g. pcs 3 ef and 4 e are on the bottom line so they are both 0
        final int[] whichLineFlat = { -2, -1, -1, 0, 0, 1, 2, 2, 3, 3, 4, 4 };
        // c, c#, d, d#, e, f, f#, g, G#, a, a#, b
        final int[] whichLineSharp = { -2, -2, -1, -1, 0, 1, 1, 2, 2, 3, 3, 4 };
        final int[] octaveOffset = { -35, -28, -21, -14, -7, 0, 7, 14, 21, 28,
                35 };

        double theY = 0d;
        if (sharps) {
            theY = this.staffBottom
                    - (whichLineSharp[num % 12] * this.yspacing)
                    - (octaveOffset[num / 12] * this.yspacing);
        } else {
            theY = this.staffBottom
                    - (whichLineFlat[num % 12] * this.yspacing)
                    - (octaveOffset[num / 12] * this.yspacing);
            // System.err.printf("num %d y %f\n", num, theY);
        }
        return (int) theY;
    }

    // c cs d ds e f fs g gs a as b
    // c df d ef e f gf g af a bf b
    // TODO just a first cut.
    public int altoMidiNumToY(final int num, final boolean sharps) {
        // the "which" arrays are by pitch class
        // f4 is the bottom line in the alto staff

        // number of yincs, which are line to space or space to line
        // c, df, d, ef, e, f, gf,g, af, a, bf, b
        final int[] whichLineFlat = { -3, -2, -2, -1, -1, 0, 1, 1, 2, 2, 3, 3 };
        // c, c#, d, d#, e, f, f#, g, G#, a, a#, b
        // final int[] whichLineSharp = { -3, -3, -3, -2, -2, -1, 0, 0, 1, 1, 2,
        // 2, 3};
        final int[] whichLineSharp = { -3, -3, -2, -2, -1, 0, 0, 1, 1, 2, 2, 3 };
        final int[] octaveOffset = { -28, -21, -14, -7, 0, 7, 14, 21, 28,
                35, 42 };
        final double oct = octaveOffset[num / 12] * this.yspacing;

        // 0,
        // final int[] alto = { F4, G4, A4, B4, C5, D5, E5 };

        double theY = 0d;
        if (sharps) {
            theY = this.staffBottom
                    - (whichLineSharp[num % 12] * this.yspacing);
            logger.debug(
                    "sharps. y {} for num {} which {}",
                    theY,
                    num,
                    whichLineSharp[num % 12]);
        } else {
            theY = this.staffBottom
                    - (whichLineFlat[num % 12] * this.yspacing);
        }
        // System.err.printf("num %d y %f\n", num, theY);
        theY -= oct;
        logger.debug("sharps. y oct {} staff bottom {}", theY, staffBottom);

        return (int) theY;
    }

    public double getYofMiddleC() {
        return this.staffBottom + lineInc;
    }

    /**
     * Find the pitch number for a given y location.
     * 
     * @param y
     *            a location
     * @return the midi pitch
     */
    public int whichNote(final double y) {
        int[] noteNums = null;
        final int[] treble = { E5, F5, G5, A5, B5, C6, D6, E6, F6 };
        final int[] bass = { G3, A3, B3, C4, D4, E4, F4, G4, A4 };

        if (y <= getYofMiddleC() + lineInc) {
            logger.debug("y {} <= middle c {}", y, getYofMiddleC());
            noteNums = treble;
            clef = Clef.TREBLE;
        } else {
            logger.debug("y {} > middle c {}", y, getYofMiddleC());
            noteNums = bass;
            clef = Clef.BASS;
        }
        noteNums = treble;

        int note = 0;
        // int fudge = 0;
        // so a click just below a line counts for the
        // line
        int oct = 0;
        // final int delta = 0;
        // int modval = 0;

        // modval = (int) this.yspacing;
        // fudge = modval / 2;
        // fudge = 0;

        double distance = 0d;

        // this will not work if there is too much space between the two staves.

        // above the bottom line i.e. in the staff
        if (y < this.staffBottom) {
            final double d = (this.staffBottom - y) / this.yspacing;
            note = (int) d;

            logger.debug("y {} is above bottom of staff {}",
                    y, this.staffBottom);

        } else {
            // fudge = modval / 2;
            distance = Math.abs(this.staffBottom - y) / this.yspacing;
            note = (int) (distance * -1);
        }
        logger.debug("note1 is {}", note);

        // high notes
        while (note > 6) {
            oct++;
            note -= 7;
            logger.debug("note is > 6: oct {} note {}", oct, note);
        }
        // low notes
        while (note < 0) {
            oct--;
            note += 7;
        }

        logger.debug("note adjusted for oct is {}", note);

        note = noteNums[note % 7] + oct * 12;

        logger.debug("Final note is {} or {}",
                note, PitchFormat.getPitchString(note));

        return note;
    }

    public double getStaffCenterLine() {
        return this.staffCenter;
    }

    public double getYSpacing() {
        return this.yspacing;
    }

    /**
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * @return the fontSizeProperty
     */
    public DoubleProperty getFontSizeProperty() {
        return fontSizeProperty;
    }

    

    public void updateSymbol() {
        int pitch = getNote().getPitch().getMidiNumber();
        double y = this.getYpositionForPitch(pitch, true);
        staffSymbol.setY(y);
        addLedgers(staffSymbol, getNote(), getFirstNoteX());
    }

    void createSymbol(MIDINote note) {
        int pitch = note.getPitch().getMidiNumber();
        String glyph = "";
        double x = 100d;
        x = getFirstNoteX();
        double y = this.getYpositionForPitch(pitch, true);

        staffSymbol = new StaffSymbol();
        staffSymbol.setFont(this.getFont());
        staffSymbol.setMIDINote(note);
        staffSymbol.setX(x);
        staffSymbol.setY(y);

        if (isSpellingFlat(note)) {
            glyph = SymbolFactory.flat();
            logger.debug("is flat");
            y = this.getYpositionForPitch(pitch, true);

            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setAccidental(glyph);
            x += staffSymbol.getLayoutBounds().getWidth();
        }

        if (isSpellingSharp(note)) {
            glyph = SymbolFactory.sharp();
            logger.debug("is sharp");
            y = this.getYpositionForPitch(pitch, false);

            staffSymbol.setX(x);
            staffSymbol.setY(y);
            staffSymbol.setAccidental(glyph);
            x += staffSymbol.getLayoutBounds().getWidth();
        }

        double center = 0d;
        center = this.getStaffCenterLine();
        boolean stemUp = true;
        if (y < center) {
            stemUp = false;
        } else {
            stemUp = true;
        }
        if (stemUp) {
            glyph = SymbolFactory.noteQuarterUp();
        } else {
            glyph = SymbolFactory.noteQuarterDown();
        }
        staffSymbol.setY(y);
        staffSymbol.setSymbol(glyph);

        addLedgers(staffSymbol, note, x);
    }

    public final void addLedgers(StaffSymbol staffSymbol, MIDINote note,
            double x) {
        staffSymbol.removeLedgers();
        int pitch = note.getPitch().getMidiNumber();
        String line = SymbolFactory.staff1Line();
        double lineinc = this.getLineInc();
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

        staffBottom = this.getStaffBottom();
        staffTop = this.getStaffTop();
        if (pitch < Pitch.CS5) {
            int nledgers = this.getNumberOfLedgers(pitch,
                    useFlat, Clef.TREBLE);
            logger.debug("there are {} ledgers for pitch {}",
                    nledgers, pitch);

            for (int i = 0; i < nledgers; i++) {
                double ly = (staffBottom + lineinc + lineinc * i);
                logger.debug("ledger y {}", ly);
                ly += lineinc * 2d; // 1linestaff kludge
                Text text = newText(x - lx, ly, line);
                double width = text.getLayoutBounds().getWidth();
                lx = width / 4d;
                text.setX(x - lx);
                staffSymbol.addLedger(text);
                logger.debug("added ledger x {} y {}", x - lx, ly);
            }
        }

        if (pitch > Pitch.FS6) {
            int nledgers = this.getNumberOfLedgers(pitch,
                    useFlat, Clef.TREBLE);
            logger.debug("there are {} ledgers for pitch {}",
                    nledgers, pitch);

            for (int i = 0; i < nledgers; i++) {
                double ly = (staffTop - lineinc - lineinc * i);
                ly += lineinc * 2d; // 1linestaff kludge
                logger.debug("ledger y {}", ly);
                Text text = newText(x - lx, ly, line);
                double width = text.getLayoutBounds().getWidth();
                lx = width / 4d;
                text.setX(x - lx);
                staffSymbol.addLedger(text);
            }
        }

    }

    private Text newText(double x, double y, String glyph) {
        Text text = new Text(x, y, glyph);
        text.setFont(this.getFont());
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

    // void drawBeat(double x) {
    // Line line = new Line(x, staffModel.getStaffBottom(), x,
    // staffModel.getStaffTop());
    // shapes.add(line);
    // }

    public double getQuarterNoteWidth() {
        String glyph = SymbolFactory.noteQuarterUp();
        Text text = new Text(0, 0, glyph);
        return text.getLayoutBounds().getWidth();
    }

    public void setFirstNoteX(double x) {
        this.firstNoteX = x;
    }

    public double getFirstNoteX() {
        return firstNoteX;
    }

    public void setBeginningBarlineX(double x) {
        this.beginningBarlineX = x;

    }

    public double getBeginningBarlineX() {
        return beginningBarlineX;
    }

    public void setStaffWidth(double w) {

    }

    /**
     * @return the staffSymbol
     */
    public StaffSymbol getStaffSymbol() {
        return staffSymbol;
    }

    double createStaves(double staffWidth) {
        logger.debug("drawing the staves {}", staffWidth);

        double x = this.getStartX();
        double y = this.getStaffBottom();
        double yspacing = this.getYSpacing();
        Font font = this.getFont();

        this.setBeginningBarlineX(x);
        this.setFirstNoteX(x + this.getFontSize() / 2d);

        Line barline = new Line(x, this.getStaffBottom(),
                x, this.getStaffTop());

        this.shapes.add(barline);
        x += (barline.getLayoutBounds().getWidth());

        double clefX = x + this.getFontSize() / 4d;
        
        double trebleClefWidth = 0;

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

        this.setFirstNoteX(clefX + trebleClefWidth
                + this.getFontSize()
                / 2d);

        String staff = SymbolFactory.staff5Lines();
        Text text = new Text(staff);
        text.setFont(font);
        text.setFontSmoothingType(FontSmoothingType.LCD);
        double staffStringIncrement = text.getLayoutBounds().getWidth();

        logger.debug(
                "drawing the treble staff at x {}, y {} width {}",
                x, y,
                staffWidth);
        for (double xx = x; xx < staffWidth; xx += staffStringIncrement) {
            text = new Text(xx, y, staff);
            text.setFont(font);
            this.shapes.add(text);
        }

        return x + trebleClefWidth;
    }

    /**
     * @return the shapes
     */
    public List<Shape> getShapes() {
        return shapes;
    }

   

}
