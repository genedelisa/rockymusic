/**
 * 
 */
package com.rockhoppertech.music.scale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Interval;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.PitchFormat;
//import com.rockhoppertech.music.chord.Chord;
//import com.rockhoppertech.music.chord.RomanChordParser;
import com.rockhoppertech.music.midi.js.KeySignature;
import com.rockhoppertech.music.midi.js.MIDITrack;

//TODO add the Chord stuff back in

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class Scale implements Cloneable {

    // property names
    public static final String NAME = "NAME";

    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String INTERVALS = "INTERVALS";
    public static final String DESCENDING_INTERVALS = "DESCENDING_INTERVALS";
    public static final String SPELLING = "SPELLING";
    public static final String DEGREES = "DEGREES";
    private static Logger logger = LoggerFactory.getLogger(Scale.class);

    private String name;
    private int[] intervals;
    private int[] degrees;
    private String spelling;
    private String description;
    /**
     * e.g. melodic minor has a different descending form
     */
    private boolean descendingDifferent = false;
    private int[] descendingIntervals;

    private int octave;
    private String key = "C";
    private List<String> aliases = new ArrayList<String>();

    public Scale() {
        this.name = "Unset";
        this.spelling = "Unset";
        this.intervals = null;
        this.degrees = null;
        this.aliases = new ArrayList<String>();
        this.key = "C";
    }

    public Scale(final String name, final int[] intervals) {
        this.setName(name);
        this.intervals = intervals;
        this.degrees = Interval.intervalsToDegrees(this.intervals);
        this.spelling = Interval.spellScale(this.degrees);
    }

    /**
     * The XML helper calls this one iff spelling is not specified in the config
     * file.
     * 
     * @param name
     * @param intervals2
     * @param description
     */
    public Scale(final String name, final Integer[] intervals2,
            final String description) {
        this.setName(name);
        this.intervals = new int[intervals2.length];
        for (int i = 0; i < intervals2.length; i++) {
            this.intervals[i] = intervals2[i].intValue();
        }
        this.degrees = Interval.intervalsToDegrees(this.intervals);
        this.spelling = Interval.spellScale(this.degrees);
        this.description = description;

        // String s = String
        // .format("scale %s has intervals %s \n", name, ArrayUtils
        // .asString(intervals));
        // System.err.println(s);

    }

    /**
     * <p>
     * </p>
     * 
     * @param string
     */
    public Scale(final String name, final String spelling) {
        this.setName(name);
        this.spelling = spelling;
        this.intervals = Interval.getIntervalsFromSpelling(this.spelling);
        this.degrees = Interval.intervalsToDegrees(this.intervals);
    }

    @Override
    public Object clone() {
        Scale result = null;
        try {
            // copy the bitwise primitives
            result = (Scale) super.clone();
            if (this.spelling != null) {
                result.spelling = new String(this.spelling);
            }
            if (this.name != null) {
                result.name = new String(this.name);
            }
            if (this.description != null) {
                result.description = new String(this.description);
            }
            result.aliases.addAll(this.aliases);

        } catch (final CloneNotSupportedException e) {

        }
        return result;
    }

    /*
    public boolean contains(final Chord chord) {
        return this.contains(this.key,
                             chord);
    }

    public boolean contains(final String key, final Chord chord) {
        boolean result = true;
        final int[] chordPits = chord.getPitchClasses();
        final List<Integer> pits = this.getDegreesAsPitchClasses(key);
        if (Scale.logger.isInfoEnabled()) {
            Scale.logger.info(String.format("key: '%s'",
                                            key));
            Scale.logger.info(String.format("chord pits: '%s'",
                                            ArrayUtils.toString(chordPits)));
            Scale.logger.info(String.format("scale: '%s'",
                                            pits));
        }
        for (final int cp : chordPits) {
            if (pits.contains(cp) == false) {
                result = false;
                if (Scale.logger.isInfoEnabled()) {
                    Scale.logger.info(String
                            .format("does not contain chord pit %d",
                                    cp));
                }
                break;
            }
        }
        if (Scale.logger.isInfoEnabled()) {
            Scale.logger.info(String.format("returning %b",
                                            result));
        }

        return result;
    }
*/
    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Scale other = (Scale) obj;
        if (this.aliases == null) {
            if (other.aliases != null) {
                return false;
            }
        } else if (!this.aliases.equals(other.aliases)) {
            return false;
        }
        if (!Arrays.equals(this.degrees,
                           other.degrees)) {
            return false;
        }
        if (this.descendingDifferent != other.descendingDifferent) {
            return false;
        }
        if (!Arrays.equals(this.descendingIntervals,
                           other.descendingIntervals)) {
            return false;
        }
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (!Arrays.equals(this.intervals,
                           other.intervals)) {
            return false;
        }
        if (this.key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!this.key.equals(other.key)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.octave != other.octave) {
            return false;
        }
        if (this.spelling == null) {
            if (other.spelling != null) {
                return false;
            }
        } else if (!this.spelling.equals(other.spelling)) {
            return false;
        }
        return true;
    }

    public int getDegree(int index) {
    	if(index > this.degrees.length) {
    		index -= this.degrees.length;
    	}
        return this.degrees[index];
    }

    /**
     * @return the degrees
     */
    public int[] getDegrees() {
        return this.degrees;
    }

    /**
     * Omit the last interval.
     * So, for major return c d e f g a b and not c d e f b a b c6
     * @return
     */
    public int[] getDegreesWithinOctave() {
        int[] r = new int[degrees.length - 1];
        System.arraycopy(degrees,
                         0,
                         r,
                         0,
                         r.length);
        return r;
    }

    public List<Integer> getDegreesAsMIDINumbers() {
        return this.getDegreesAsMIDINumbers(this.key);
    }

    public List<Integer> getDegreesAsMIDINumbers(final String key) {
        final Pitch kp = PitchFactory.getPitch(key);
        final List<Integer> pitches = new ArrayList<Integer>();
        for (final int degree : this.degrees) {
            final Pitch p = PitchFactory.getPitch(degree + kp.getMidiNumber()
                    % 12);
            pitches.add(p.getMidiNumber());
        }
        return pitches;
    }

    public List<Integer> getDegreesAsPitchClasses() {
        return this.getDegreesAsPitchClasses(this.key);
    }

    public List<Integer> getDegreesAsPitchClasses(final String key) {
        final Pitch kp = PitchFactory.getPitch(key);
        final List<Integer> pitches = new ArrayList<Integer>();
        for (final int degree : this.degrees) {
            final Pitch p = PitchFactory.getPitch(degree + kp.getMidiNumber()
                    % 12);
            pitches.add(p.getMidiNumber() % 12);
        }
        return pitches;
    }

    public List<Pitch> getDegreesAsPitches() {
        final Pitch kp = PitchFactory.getPitch(this.key);
        final List<Pitch> pitches = new ArrayList<Pitch>();
        // TODO fix this spelling nonsense
        for (final int degree : this.degrees) {
            final Pitch p = PitchFactory.getPitch(degree + kp.getMidiNumber());
            if (this.keySignature != null) {
                String preferredSpelling = null;
                if (this.keySignature.getSf() < 0) {
                    preferredSpelling = PitchFormat.getPitchString(p,
                                                                   false);
                } else if (this.keySignature.getSf() > 0) {
                    preferredSpelling = PitchFormat.getPitchString(p,
                                                                   true);
                }
                p.setPreferredSpelling(preferredSpelling);
            }

            pitches.add(p);
        }
        return pitches;
    }

    public List<Pitch> getDegreesAsPitches(final String key) {
        final Pitch kp = PitchFactory.getPitch(key);
        final List<Pitch> pitches = new ArrayList<Pitch>();
        for (final int degree : this.degrees) {
            final Pitch p = PitchFactory.getPitch(degree + kp.getMidiNumber());
            pitches.add(p);
        }
        return pitches;
    }

    public String getDegreesAsPitchString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.degrees.length; i++) {
            final Pitch p = PitchFactory.getPitch(this.degrees[i]);
            sb.append(PitchFormat.getPitchString(p));
            if (i < this.degrees.length - 1) {
                sb.append(' ');
            }
        }
        // FIXME
        if (this.isDescendingDifferent()) {
            final int[] down = Interval
                    .intervalsToDegrees(this.descendingIntervals);
            for (int i = 0; i < down.length; i++) {
                final Pitch p = PitchFactory.getPitch(down[i]);
                sb.append(PitchFormat.getPitchString(p));
                if (i < down.length - 1) {
                    sb.append(' ');
                }
            }
        }
        return sb.toString();
    }

    public String getDegreesAsString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.degrees.length; i++) {
            sb.append(this.degrees[i]);
            if (i < this.degrees.length - 1) {
                sb.append(' ');
            }
        }
        // FIXME
        if (this.isDescendingDifferent()) {
            final int[] down = Interval
                    .intervalsToDegrees(this.descendingIntervals);
            for (int i = 0; i < down.length; i++) {
                sb.append(down[i]);
                if (i < down.length - 1) {
                    sb.append(' ');
                }
            }
        }
        return sb.toString();
    }

    /**
     * @return the descendingIntervals
     */
    public int[] getDescendingIntervals() {
        return this.descendingIntervals;
    }

    public String getDescendingIntervalsAsString() {
        if (this.descendingIntervals == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.descendingIntervals.length; i++) {
            sb.append(this.descendingIntervals[i]);
            if (i < this.descendingIntervals.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return the intervals
     */
    public int[] getIntervals() {
        final int[] copy = new int[this.intervals.length];
        System.arraycopy(this.intervals,
                         0,
                         copy,
                         0,
                         this.intervals.length);
        return copy;
    }

    public String getIntervalsAsString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.intervals.length; i++) {
            sb.append(this.intervals[i]);
            if (i < this.intervals.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * <p>
     * The number of degrees
     * </p>
     * 
     * @return
     */
    public int getLength() {
        return this.degrees.length;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the octave
     */
    public int getOctave() {
        return this.octave;
    }

    /*
    public String getRoman(final Chord chord) {
        return this.getRoman(this.key,
                             chord);
    }

    public String getRoman(final String key, final Chord chord) {
        final String sym = chord.getSymbol();
        // String[] sa = ChordFactory.getChordSymbols(this);
        // sym = sa[chord.getRoot() % 12];
        final String s = String
                .format("%s%s",
                        RomanChordParser
                                .pitchNameToRoman(this,
                                                  PitchFormat
                                                          .getPitchString(chord
                                                                  .getRoot()),
                                                  key),
                        sym);
        return s;
    }

    
    public String getRoman(final String key, final Chord chord,
            final boolean omitSymbol) {
        final String sym = chord.getSymbol();
        final String s = String
                .format("%s%s",
                        RomanChordParser
                                .pitchNameToRoman(this,
                                                  PitchFormat
                                                          .getPitchString(chord
                                                                  .getRoot()),
                                                  key),
                        omitSymbol ? "" : sym);
        return s;
    }
*/
    /**
     * @return the spelling
     */
    public String getSpelling() {
        return this.spelling;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.aliases == null) ? 0 : this.aliases.hashCode());
        result = prime * result + Arrays.hashCode(this.degrees);
        result = prime * result + (this.descendingDifferent ? 1231 : 1237);
        result = prime * result + Arrays.hashCode(this.descendingIntervals);
        result = prime
                * result
                + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + Arrays.hashCode(this.intervals);
        result = prime * result
                + ((this.key == null) ? 0 : this.key.hashCode());
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + this.octave;
        result = prime * result
                + ((this.spelling == null) ? 0 : this.spelling.hashCode());
        return result;
    }

    /**
     * @return the descendingDifferent
     */
    public boolean isDescendingDifferent() {
        return this.descendingDifferent;
    }

    public boolean isDiatonic(int p) {
        p = p % 12;
        final int[] degrees = this.getDegrees();
        if (ArrayUtils.contains(degrees,
                                p)) {
            return true;
        }
        return false;
    }

    public int pitchToDegree(final String pitch) {
        return this.pitchToDegree(this.key,
                                  pitch);
    }

    public int pitchToDegree(final String key, final String pitch) {
        // Pitch kp = PitchFactory.getPitch(key);
        final Pitch p = PitchFactory.getPitch(pitch);
        final List<Pitch> pits = this.getDegreesAsPitches(key);
        String msg = null;
        int degree = 0;

        if (pits.contains(p)) {
            degree = pits.indexOf(p);
            degree++; // music is not zero based
            msg = String.format("contains at index/degree=%d",
                                degree);
            Scale.logger.debug(msg);
            return degree;
        }

        for (int i = 0; i < pits.size(); i++) {
            final Pitch cp = pits.get(i);
            if (cp.getMidiNumber() == p.getMidiNumber() + 1) {
                msg = String.format("raised at index=%d",
                                    i);
                Scale.logger.debug(msg);
                return i;
            }
        }

        // for (int i = 0; i < this.degrees.length; i++) {
        // if(this.degrees[i] + kp.getMidiNumber() == p.getMidiNumber()) {
        // degree = 0;
        // break;
        // }
        // }
        return degree;
    }

    /**
     * @param degrees
     *            the degrees to set
     */
    public void setDegrees(final int[] degrees) {
        this.degrees = degrees;
        this.spelling = Interval.spellScale(this.degrees);
        this.intervals = Interval.getIntervalsFromSpelling(this.spelling);
    }

    /**
     * @param descendingDifferent
     *            the descendingDifferent to set
     */
    public void setDescendingDifferent(final boolean descendingDifferent) {
        this.descendingDifferent = descendingDifferent;
    }

    /**
     * @param descendingIntervals
     *            the descendingIntervals to set
     */
    public void setDescendingIntervals(final int[] descendingIntervals) {
        this.descendingIntervals = descendingIntervals;
        // append to this.degrees too? nah

        this.setDescendingDifferent(true);
    }

    /**
     * <p>
     * </p>
     * 
     * @param dintervals
     */
    public void setDescendingIntervals(final Integer[] dintervals) {
        final int[] a = new int[dintervals.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = dintervals[i].intValue();
        }
        this.setDescendingIntervals(a);
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @param intervals
     *            the intervals to set
     */
    public void setIntervals(final int[] intervals) {
        this.intervals = intervals;
        this.degrees = Interval.intervalsToDegrees(this.intervals);
        this.spelling = Interval.spellScale(this.degrees);
    }

    /**
     * Pitch names without the octave. C, D, E, F# etc.
     * 
     * @param key
     *            the key to set
     */
    public void setKey(final String key) {
        this.key = key;
        if (this.getName().equals("Major")) {
            this.keySignature = KeySignature.get(key,
                                                 KeySignature.MAJOR);
        } else if (this.getName().equals("Harmonic Minor")) {
            this.keySignature = KeySignature.get(key,
                                                 KeySignature.MINOR);
        }
    }

    private KeySignature keySignature;

    public void setKeySignature(KeySignature keySignature) {
        if (this.getName().equals("Major")
                || this.getName().equals("Harmonic Minor")) {
            this.keySignature = keySignature;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public KeySignature getKeySignature() {
        return this.keySignature;
    }

    /**
     * make the introspector happy.
     * 
     * @param length
     */
    public void setLength(final int length) {
        final int[] a = new int[length];
        System.arraycopy(this.degrees,
                         0,
                         a,
                         0,
                         a.length);
        this.degrees = a;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        if (name.contains(",")) {
            Scanner s = new Scanner(name);
            s.useDelimiter(",");
            this.name = s.next();
            while (s.hasNext()) {
                this.aliases.add(s.next());
            }
            s.close();
        } else {
            this.name = name;
        }
    }

    /**
     * <p>
     * </p>
     * 
     * @param octave
     */
    public void setOctave(final int octave) {
        this.octave = octave;
    }

    /**
     * @param spelling
     *            the spelling to set
     */
    public void setSpelling(final String spelling) {
        this.spelling = spelling;
        // this.intervals = Interval.getIntervalsFromSpelling(this.spelling);
        // this.degrees = Interval.intervalsToDegrees(this.intervals);
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final String ls = System.getProperty("line.separator");
        final StringBuilder sb = new StringBuilder(this.getClass().getName())
                .append(ls);
        sb.append(" name=").append(this.name).append(ls);
        sb.append(" description=").append(this.description).append(ls);
        sb.append(" key=").append(this.key).append(ls);
        sb.append(" oct=").append(this.octave).append(ls);
        sb.append(" spelling=").append(this.spelling).append(ls);
        sb.append(" intervals=").append(this.getIntervalsAsString()).append(ls);
        sb.append(" degrees=").append(this.getDegreesAsString()).append(ls);
        sb.append(" degAsPit=").append(this.getDegreesAsPitchString());
        sb.append(" isdescending=").append(this.isDescendingDifferent())
                .append(ls);
        if (this.isDescendingDifferent()) {
            sb.append(" descending=").append(this
                    .getDescendingIntervalsAsString()).append(ls);
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Scale scale = ScaleFactory.getScaleByName("Major");
        System.err.println(scale);
        int[] major = scale.getDegrees();
        System.err.println(org.apache.commons.lang3.ArrayUtils.toString(major));

        MIDITrack nl = ScaleFactory.createMIDITrackOverEntireRange(scale);
        System.err.println(nl);
    }
    /*
     * moved to scalefactory static void dump() { for (String name :
     * scales.keySet()) { System.out.println(scales.get(name)); } } static { }
     * 
     * static void addScales() { Scale s = new Scale("Hungarian Gypsy",
     * "1, 2, b3, #4, 5, b6, b7"); scales.put(s.getName(), s); s = new
     * Scale("Pelog", Scale.PELOG); scales.put(s.getName(), s); s = new
     * Scale("Algerian", "1, 2, b3, #4, 5, b6, 7"); scales.put(s.getName(), s);
     * s = new Scale("Arabian", "1, 2, 3, 4, b5, b6, b7");
     * scales.put(s.getName(), s); s = new Scale("Byzantine",
     * "1, b2, 3, 4, 5, b6, 7"); scales.put(s.getName(), s); s = new
     * Scale("Ethiopian", "1, 2, b3, 4, 5, b6, b7"); scales.put(s.getName(), s);
     * s = new Scale("Gypsy", "1, b2, 3, 4, 5, b6, 7"); scales.put(s.getName(),
     * s); s = new Scale("Hawaiian", "1, 2, b3, 4, 5, 6, 7");
     * scales.put(s.getName(), s); s = new Scale("Persian",
     * "1, b2, 3, 4, b5, b6, 7"); scales.put(s.getName(), s); s = new
     * Scale("Oriental", "1, b2, 3, 4, b5, 6, b7"); scales.put(s.getName(), s);
     * s = new Scale("Mohammedan", "1, 2, b3, 4, 5, b6, 7");
     * scales.put(s.getName(), s); s = new Scale("Jewish",
     * "1, b2, 3, 4, 5, b6, b7"); scales.put(s.getName(), s); s = new
     * Scale("Javanese", "1, b2, b3, 4, 5, 6, b7"); scales.put(s.getName(), s);
     * s = new Scale("Hungarian minor", "1, 2, b3, #4, 5, b6, 7");
     * scales.put(s.getName(), s); s = new Scale("Hungarian Major",
     * "1, #2, 3, #4, 5, 6, b7"); scales.put(s.getName(), s); s = new
     * Scale("Hungarian Gypsy", "1, 2, b3, #4, 5, b6, b7");
     * scales.put(s.getName(), s); s = new Scale("Hindu",
     * "1, 2, 3, 4, 5, b6, b7"); scales.put(s.getName(), s);
     * 
     * s = new Scale("diminished minor", "1, 2, b3, 4, b5, b6, bb7, b8");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Bebop half-diminished", "1, b2, b3, 4, b5, 5, b6, b7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Bebop minor", "1, 2, b3, 3, 4, 5, 6, b7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Bebop Dominant", "1, 2, 3, 4, 5, 6, b7, 7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Eight-tone Spanish", "1, b2, #2, 3, 4, b5, b6, b7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Spanish", "1, b2, 3, 4, 5, b6, b7");
     * scales.put(s.getName(), s);
     * 
     * //Symetric Scales s = new Scale("Augmented", "1, #2, 3, 5, #5, 7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Whole-Tone b5", "1, 2, 3, b5, b6, b7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Whole-Tone", "1, 2, 3, #4, #5, b7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("diminished dominant", "1, b2, #2, 3, #4, 5, 6, b7");
     * scales.put(s.getName(), s); //
     * 
     * // pentatonic s = new Scale("Pelog", "1, b2, b3, 5, b6");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Pelog2", "1, b2, b3, 5, b7"); scales.put(s.getName(), s);
     * s = new Scale("Mongolian", "1, 2, 3, 5, 6"); scales.put(s.getName(), s);
     * 
     * s = new Scale("Kumoi", "1, b2, 4, 5, b6"); scales.put(s.getName(), s);
     * 
     * s = new Scale("Japanese", "1, b2, 4, 5, b6"); scales.put(s.getName(), s);
     * 
     * s = new Scale("Iwato", "1, b2, 4, b5, b7"); scales.put(s.getName(), s); s
     * = new Scale("Hirajoshi", "1, 2, b3, 5, b6"); scales.put(s.getName(), s);
     * 
     * s = new Scale("Egyptian", "1, 2, 4, 5, b7"); scales.put(s.getName(), s);
     * 
     * s = new Scale("Chinese", "1, 3, #4, 5, 7"); scales.put(s.getName(), s);
     * 
     * s = new Scale("Balinese", "1, b2, b3, 5, b6"); scales.put(s.getName(),
     * s); //
     * 
     * s = new Scale("Major Phrygian", "1, b2, 3, 4, 5, b6, b7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Major Locrian", "1, 2, 3, 4, b5, b6, b7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Lydian minor", "1, 2, 3, #4, 5, b6, b7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Leading Whole Tone", "1, 2, 3, #4, #5, #6, 7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Harmonic Minor", "1, 2, b3, 4, 5, b6, 7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Harmonic Major", "1, 2, 3, 4, 5, b6, 7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Enigmatic", "1, b2, 3, #4, #5, #6, 7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Double Harmonic", "1, b2, 3, 4, 5, b6, 7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Neapolitan minor", "1, b2, b3, 4, 5, b6, 7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Neapolitan Major", "1, b2, b3, 4, 5, 6, 7");
     * scales.put(s.getName(), s);
     * 
     * s = new Scale("Overtone", "1, 2, 3, #4, 5, 6, b7");
     * scales.put(s.getName(), s); dump();
     * 
     * }
     */

}
/*

 */

// static Map<String, Scale> scales = new HashMap<String, Scale>();
//
// public static final int[] CHROMATIC = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
// };
// public static final int[] MAJOR = { 2, 2, 1, 2, 2, 2, 1 };
// public static final int[] HARMONIC_MINOR = { 2, 1, 2, 2, 1, 3, 1 };
// public static final int[] MELODIC_MINOR = { 2, 1, 2, 2, 1, 2, 2 };
// public static final int[] OCTATONIC = { 2, 1, 2, 1, 2, 1, 2, 1 };
//
// public static final int[] BLUES_SCALE = { 3, 2, 1, 1, 3, 2 };
//
// public static final int[] WHOLE_TONE = { 2, 2, 2, 2, 2 };
//
// public static final int[] MESSIAEN_MODE1 = WHOLE_TONE;
// public static final int[] MESSIAEN_MODE2 = OCTATONIC;
// public static final int[] MESSIAEN_MODE3 = { 2, 1, 1, 2, 1, 1 };
// public static final int[] MESSIAEN_MODE4_1 = { 1, 1, 3, 1, 1, 1, 3, 1 };
// public static final int[] MESSIAEN_MODE4_2 = { 1, 4, 1, 1, 4, 1 };
// public static final int[] MESSIAEN_MODE4_3 = { 2, 2, 1, 1, 2, 2, 1, 1 };
// public static final int[] MESSIAEN_MODE4_4 = { 1, 1, 1, 2, 1, 1, 1, 1, 2,
// 1 };
//
// public static final int[] IONIAN = MAJOR;
// public static final int[] DORIAN = { 2, 1, 2, 2, 2, 1, 2 };
// public static final int[] PHRYGIAN = { 1, 2, 2, 2, 1, 2, 2 };
// public static final int[] LYDIAN = { 2, 2, 2, 1, 2, 2, 1 };
// public static final int[] MIXOLYDIAN = { 2, 2, 1, 2, 2, 1, 2 };
// public static final int[] AEOLIAN = { 2, 1, 2, 2, 1, 2, 2 };
// public static final int[] LOCRIAN = { 1, 2, 2, 1, 2, 2, 2 };
//
// public static final int[] IONIAN_PENTATONIC = { 2, 2, 3, 2, 3 };
// public static final int[] DORIAN_PENTATONIC = { 2, 1, 4, 2, 3 };
// public static final int[] PHRYGIAN_PENTATONIC = { 1, 2, 4, 1, 4 };
// public static final int[] LYDIAN_PENTATONIC = { 2, 2, 3, 2, 3 };
// public static final int[] MIXOLYDIAN_PENTATONIC = { 2, 2, 3, 2, 3 };
// public static final int[] AEOLIAN_PENTATONIC = { 2, 1, 4, 1, 4 };
// public static final int[] LOCRIAN_PENTATONIC = { 1, 2, 3, 2, 4 };
//
// public static final int[] JAPANESE = { 2, 1, 4, 1, 4 };
// public static final int[] GYPSY = { 1, 3, 1, 2, 1, 3, 1 };
// public static final int[] NEAPOLITAN = { 1, 2, 2, 2, 2, 2, 1 };
// public static final int[] NEAPOLITAN_MINOR = { 1, 2, 2, 2, 1, 3, 1 };
// public static final int[] PELOG = { 1, 2, 4, 1 };
// public static final int[] ROMANIAN_MINOR = { 2, 1, 3, 1, 2, 1, 2 };
// public static final int[] DOUBLE_HARMONIC = { 1, 2, 1, 2, 1, 3, 1 };
// public static final int[] HUNGARIAN_MINOR = { 2, 1, 3, 1, 1, 3, 1 };
// public static final int[] ARABIAN = { 2, 1, 2, 1, 1, 1, 2, 1 };
// public static final int[] PROMETHEUS = { 2, 2, 2, 4, 2 };
// public static final int[] SCRIABIN = { 1, 3, 3, 2, 3 };
// public static final int[] EGYPTIAN = { 2, 3, 2, 3, 2 };
// public static final int[] ENIGMATIC = { 1, 3, 2, 2, 2, 1, 1 };
// public static final int[] ORIENTAL = { 1, 3, 1, 1, 2, 2, 2 };
/*
 * Hindustan Ritusen Arabian Leading Whole Tone Lydian Minor Iwato Piongio
 * Hirajoshi Kumoi Chinese
 */