/**
 * 
 */
package com.rockhoppertech.music.fx.musicfont.model;

import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;

import static com.rockhoppertech.music.Pitch.*;

import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.fx.musicfont.NotationApp;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.sun.javafx.tk.Toolkit;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class StaffModel {
    final static Logger logger = LoggerFactory.getLogger(StaffModel.class);

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
    static {

    }

    // private Font musicFont;
    // private FontMetrics fontMetrics;
    // private Paint musicSymbolPaint = Color.black;
    // private NavigableMap<Double, Measure> measures;
    // private List<MeasurePainter> measurePainters = new
    // LinkedList<MeasurePainter>();
    // private MIDINoteList notelist;
    // private String defaultMusicFontName = "Opus";
    // private List<String> musicFontNames;
    // private BufferedImage offscreen;

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
     * y location of the staff bottom line.
     */
    private double staffBottom;
    private double staffTop;
    private double staffCenter;
    private MIDITrack track;
    private ObjectProperty<MIDITrack> trackProperty;
    private ObjectProperty<Font> fontProperty;
    private DoubleProperty fontSizeProperty;

    public StaffModel() {

        fontSize = 48d;
        font = Font.loadFont(
                NotationApp.class.getResource("/fonts/Bravura.otf")
                        .toExternalForm(),
                fontSize);
        if (font == null) {
            throw new IllegalStateException("music font not found");
        }

        this.setClef(Clef.TREBLE);
        this.startX = 10d;
        this.trackProperty = new SimpleObjectProperty<MIDITrack>();
        this.fontSizeProperty = new SimpleDoubleProperty(this.fontSize);
        this.setFontSize(48d);
        this.fontProperty = new SimpleObjectProperty<Font>(this.font);

        StaffSymbolManager.setStaffModel(this);
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
     * 
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
     * 
     * @return
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
        }
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
     * Get the y location for a given MIDI pitch number.
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
        if (useFlat) {
            y = this.flatYpositions[pitch];
        } else {
            y = this.sharpYpositions[pitch];
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
     * @param fontSize2
     *            the fontSize to set
     */
    public void setFontSize(final double fontSize2) {
        this.fontSize = fontSize2;
        this.fontSizeProperty.setValue(this.fontSize);
        this.staffBottom = 4d * this.fontSize;
        this.calcStaffMetrics();
        setClef(this.clef);
        this.font = new Font("Bravura", fontSize);
        
        // needed the first time.
        if (this.track != null) {
            StaffSymbolManager.setMIDITrack(this.track);
        }
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

    // TODO do this nothing is right here. test it. just a first cut.
    public int altoMidiNumToY(final int num, final boolean sharps) {
        // the "which" arrays are by pitch class
        // e.g. pcs 3 ef and 4 e are on the bottom line so they are both 0

        // c, df, d, ef, e, f, gf,g,af, a, bf, b
        // number of yincs
        final int[] whichLineFlat = { -3, -2, -2, 1, 1, 0, 1, 1, 2, 2, 3, 3 };
        // c, c#, d, d#, e, f, f#, g, G#, a, a#, b
        final int[] whichLineSharp = { -3, -3, -2, -2, 0, 1, 1, 2, 2, 3, 3, 4 };
        final int[] octaveOffset = { -35, -28, -21, -14, -7, 0, 7, 14, 21, 28,
                35 };
        // 0,
        // final int[] alto = { F4, G4, A4, B4, C5, D5, E5 };

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

    /**
     * Find the pitch number for a given y location.
     * 
     * @param y
     *            a location
     * @return the midi pitch
     */
    public int whichNote(final double y) {
        int[] noteNums = null;
        final int[] treble = { Pitch.E5, Pitch.F5, Pitch.G5, Pitch.A5,
                Pitch.B5, Pitch.C6, Pitch.D6, Pitch.E6, Pitch.F6 };
        final int[] bass = { Pitch.G3, Pitch.A3, Pitch.B3, Pitch.C4, Pitch.D4,
                Pitch.E4, Pitch.F4, Pitch.G4, Pitch.A4 };
        final int[] alto = { F4, G4, A4, B4, C5, D5, E5, F5, G5 };

        if (this.clef == Clef.TREBLE) {
            noteNums = treble;
        } else if (this.clef == Clef.BASS) {
            noteNums = bass;
        } else if (this.clef == Clef.ALTO) {
            noteNums = alto;
        }

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

        // above the bottom line i.e. in the staff
        if (y < this.staffBottom) {
            final double d = (this.staffBottom - y) / this.yspacing;
            note = (int) d;
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("y %f is above bottom of staff %f",
                        y,
                        this.staffBottom));
            }
        } else {
            // fudge = modval / 2;
            distance = Math.abs(this.staffBottom - y) / this.yspacing;
            note = (int) (distance * -1);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("note1 is %d",
                    note));
        }

        // high notes
        while (note > 6) {
            oct++;
            note -= 7;
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("note is > 6: oct %d note %d",
                        oct,
                        note));
            }
        }
        // low notes
        while (note < 0) {
            oct--;
            note += 7;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("note adjusted for oct is %d",
                    note));
        }

        note = noteNums[note % 7] + oct * 12;

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Final note is %d or %s",
                    note,
                    PitchFormat.getPitchString(note)));
        }
        return note;
    }

    public double getStaffCenterLine() {
        return this.staffCenter;
    }

    public double getYSpacing() {
        return this.yspacing;
    }

    public void setTrack(MIDITrack track) {
        this.trackProperty.setValue(track);
        this.track = track;
        StaffSymbolManager.setMIDITrack(track);
    }

    /**
     * @return the trackProperty
     */
    public ObjectProperty<MIDITrack> getTrackProperty() {
        return trackProperty;
    }

    public List<StaffSymbol> getSymbols() {
        return StaffSymbolManager.getSymbols();
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

    public final float stringWidth(String string) {
        if (font == null) {
            return 0f;
        }
        return Toolkit.getToolkit().getFontLoader()
                .computeStringWidth(string, font);

    }

}
