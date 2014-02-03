/**
 * 
 */
package com.rockhoppertech.music.scale;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
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
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.rockhoppertech.music.Interval;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.chord.Chord;
import com.rockhoppertech.music.chord.RomanChordParser;
import com.rockhoppertech.music.midi.js.KeySignature;

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
        name = "Unset";
        spelling = "Unset";
        intervals = null;
        degrees = null;
        aliases = new ArrayList<String>();
        key = "C";
    }

    public Scale(final String name, final int[] intervals) {
        setName(name);
        this.intervals = Arrays.copyOf(intervals, intervals.length);
        degrees = Interval.intervalsToDegrees(this.intervals);
        spelling = Interval.spellScale(degrees);
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
        setName(name);
        intervals = new int[intervals2.length];
        for (int i = 0; i < intervals2.length; i++) {
            intervals[i] = intervals2[i].intValue();
        }
        degrees = Interval.intervalsToDegrees(intervals);
        spelling = Interval.spellScale(degrees);
        this.description = description;

        // String s = String
        // .format("scale %s has intervals %s \n", name, ArrayUtils
        // .asString(intervals));
        // System.err.println(s);

    }

    /**
     * <p>
     * Construct a Scale from the name and spelling.
     * </p>
     * 
     * @param name
     *            the name to set
     * @param spelling
     *            the spelling to use
     */
    public Scale(final String name, final String spelling) {
        setName(name);
        this.spelling = spelling;
        intervals = Interval.getIntervalsFromSpelling(this.spelling);
        degrees = Interval.intervalsToDegrees(intervals);
    }

    @Override
    public Object clone() {
        Scale result = null;
        try {
            // copy the bitwise primitives
            result = (Scale) super.clone();
            if (spelling != null) {
                result.spelling = spelling;
            }
            if (name != null) {
                result.name = name;
            }
            if (description != null) {
                result.description = description;
            }
            result.aliases.addAll(aliases);

        } catch (final CloneNotSupportedException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * Clone is evil.
     * 
     * @return a duplicate
     */
    public Scale duplicate() {
        Scale dupe = new Scale();
        dupe.name = this.name;
        dupe.spelling = this.spelling;
        dupe.description = this.description;
        dupe.aliases = Lists.newArrayList(this.aliases);
        dupe.degrees = Arrays.copyOf(this.degrees, this.degrees.length);
        dupe.descendingDifferent = this.descendingDifferent;
        if (this.descendingIntervals != null) {
            dupe.descendingIntervals = Arrays.copyOf(
                    this.descendingIntervals,
                    this.descendingIntervals.length);
        }
        dupe.intervals = Arrays.copyOf(this.intervals, this.intervals.length);
        dupe.key = this.key;
        dupe.keySignature = this.keySignature;
        dupe.octave = this.octave;
        return dupe;
    }

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
        if (aliases == null) {
            if (other.aliases != null) {
                return false;
            }
        } else if (!aliases.equals(other.aliases)) {
            return false;
        }
        if (!Arrays.equals(degrees,
                other.degrees)) {
            return false;
        }
        if (descendingDifferent != other.descendingDifferent) {
            return false;
        }
        if (!Arrays.equals(descendingIntervals,
                other.descendingIntervals)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (!Arrays.equals(intervals,
                other.intervals)) {
            return false;
        }
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (octave != other.octave) {
            return false;
        }
        if (spelling == null) {
            if (other.spelling != null) {
                return false;
            }
        } else if (!spelling.equals(other.spelling)) {
            return false;
        }
        return true;
    }

    public int getDegree(int index) {
        return degrees[index];
    }

    /**
     * @return the degrees
     */
    public int[] getDegrees() {
        return Arrays.copyOf(degrees, degrees.length);
    }

    /**
     * Omit the last interval. So, for major return c d e f g a b and not c d e
     * f b a b c6
     * 
     * @return the degrees without the root at the end
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
        return this.getDegreesAsMIDINumbers(key);
    }

    public List<Integer> getDegreesAsMIDINumbers(final String key) {
        final Pitch kp = PitchFactory.getPitch(key);
        final List<Integer> pitches = new ArrayList<Integer>();
        for (final int degree : degrees) {
            final Pitch p = PitchFactory.getPitch(degree + kp.getMidiNumber()
                    % 12);
            pitches.add(p.getMidiNumber());
        }
        return pitches;
    }

    public List<Integer> getDegreesAsPitchClasses() {
        return this.getDegreesAsPitchClasses(key);
    }

    public List<Integer> getDegreesAsPitchClasses(final String key) {
        final Pitch kp = PitchFactory.getPitch(key);
        final List<Integer> pitches = new ArrayList<Integer>();
        for (final int degree : degrees) {
            final Pitch p = PitchFactory.getPitch(degree + kp.getMidiNumber()
                    % 12);
            pitches.add(p.getMidiNumber() % 12);
        }
        return pitches;
    }

    public List<Pitch> getDegreesAsPitches() {
        final Pitch kp = PitchFactory.getPitch(key);
        final List<Pitch> pitches = new ArrayList<Pitch>();
        // TODO fix this spelling nonsense
        for (final int degree : degrees) {
            final Pitch p = PitchFactory.getPitch(degree + kp.getMidiNumber());
            if (keySignature != null) {
                String preferredSpelling = null;
                if (keySignature.getSf() < 0) {
                    preferredSpelling = PitchFormat.getPitchString(p,
                            false);
                } else if (keySignature.getSf() > 0) {
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
        for (final int degree : degrees) {
            final Pitch p = PitchFactory.getPitch(degree + kp.getMidiNumber());
            pitches.add(p);
        }
        return pitches;
    }

    public String getDegreesAsPitchString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < degrees.length; i++) {
            final Pitch p = PitchFactory.getPitch(degrees[i]);
            sb.append(PitchFormat.getPitchString(p));
            if (i < degrees.length - 1) {
                sb.append(' ');
            }
        }
        // FIXME
        if (isDescendingDifferent()) {
            final int[] down = Interval
                    .intervalsToDegrees(descendingIntervals);
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
        for (int i = 0; i < degrees.length; i++) {
            sb.append(degrees[i]);
            if (i < degrees.length - 1) {
                sb.append(' ');
            }
        }
        // FIXME
        if (isDescendingDifferent()) {
            final int[] down = Interval
                    .intervalsToDegrees(descendingIntervals);
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
        return Arrays.copyOf(descendingIntervals, descendingIntervals.length);
    }

    public String getDescendingIntervalsAsString() {
        if (descendingIntervals == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < descendingIntervals.length; i++) {
            sb.append(descendingIntervals[i]);
            if (i < descendingIntervals.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the intervals
     */
    public int[] getIntervals() {
        final int[] copy = new int[intervals.length];
        System.arraycopy(intervals,
                0,
                copy,
                0,
                intervals.length);
        return copy;
    }

    public String getIntervalsAsString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < intervals.length; i++) {
            sb.append(intervals[i]);
            if (i < intervals.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>
     * The number of degrees
     * </p>
     * 
     * @return the number of degrees
     */
    public int getLength() {
        return degrees.length;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the octave
     */
    public int getOctave() {
        return octave;
    }

    /*
     * public String getRoman(final Chord chord) { return
     * this.getRoman(this.key, chord); }
     * 
     * public String getRoman(final String key, final Chord chord) { final
     * String sym = chord.getSymbol(); // String[] sa =
     * ChordFactory.getChordSymbols(this); // sym = sa[chord.getRoot() % 12];
     * final String s = String .format("%s%s", RomanChordParser
     * .pitchNameToRoman(this, PitchFormat .getPitchString(chord .getRoot()),
     * key), sym); return s; }
     * 
     * 
     * public String getRoman(final String key, final Chord chord, final boolean
     * omitSymbol) { final String sym = chord.getSymbol(); final String s =
     * String .format("%s%s", RomanChordParser .pitchNameToRoman(this,
     * PitchFormat .getPitchString(chord .getRoot()), key), omitSymbol ? "" :
     * sym); return s; }
     */
    /**
     * @return the spelling
     */
    public String getSpelling() {
        return spelling;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((aliases == null) ? 0 : aliases.hashCode());
        result = prime * result + Arrays.hashCode(degrees);
        result = prime * result + (descendingDifferent ? 1231 : 1237);
        result = prime * result + Arrays.hashCode(descendingIntervals);
        result = prime
                * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + Arrays.hashCode(intervals);
        result = prime * result
                + ((key == null) ? 0 : key.hashCode());
        result = prime * result
                + ((name == null) ? 0 : name.hashCode());
        result = prime * result + octave;
        result = prime * result
                + ((spelling == null) ? 0 : spelling.hashCode());
        return result;
    }

    /**
     * @return the descendingDifferent
     */
    public boolean isDescendingDifferent() {
        return descendingDifferent;
    }

    public boolean isDiatonic(int p) {
        p = p % 12;
        final int[] degrees = getDegrees();
        if (ArrayUtils.contains(degrees,
                p)) {
            return true;
        }
        return false;
    }

    public int pitchToDegree(final String pitch) {
        return this.pitchToDegree(key,
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
        this.degrees = Arrays.copyOf(degrees, degrees.length);
        spelling = Interval.spellScale(this.degrees);
        intervals = Interval.getIntervalsFromSpelling(spelling);
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
        this.descendingIntervals = Arrays.copyOf(
                descendingIntervals,
                descendingIntervals.length);
        // append to this.degrees too? nah

        setDescendingDifferent(true);
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
        this.intervals = Arrays.copyOf(intervals, intervals.length);
        degrees = Interval.intervalsToDegrees(this.intervals);
        spelling = Interval.spellScale(degrees);
    }

    /**
     * Pitch names without the octave. C, D, E, F# etc.
     * 
     * @param key
     *            the key to set
     */
    public void setKey(final String key) {
        this.key = key;
        if (getName().equals("Major")) {
            keySignature = KeySignature.get(key,
                    KeySignature.MAJOR);
        } else if (getName().equals("Harmonic Minor")) {
            keySignature = KeySignature.get(key,
                    KeySignature.MINOR);
        }
    }

    private KeySignature keySignature;

    /**
     * Works only if the Scale's name is "Major" or "Minor". Throws an
     * IllegalArgumentException if not.
     * <p>
     * Also sets this.key.
     * 
     * @param keySignature
     *            the key signature
     */
    public void setKeySignature(KeySignature keySignature) {
        if (getName().equals("Major")
                || getName().equals("Harmonic Minor")) {
            this.keySignature = keySignature;
            this.key = PitchFormat.getInstance().format(
                    this.keySignature.getRoot()).trim();
            logger.debug("this key={}", this.key);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public KeySignature getKeySignature() {
        return keySignature;
    }

    /**
     * make the introspector happy.
     * 
     * @param length
     */
    public void setLength(final int length) {
        final int[] a = new int[length];
        System.arraycopy(degrees,
                0,
                a,
                0,
                a.length);
        degrees = a;
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
                aliases.add(s.next());
            }
            s.close();
        } else {
            this.name = name;
        }
    }

    /**
     * <p>
     * Set the octave.
     * 
     * 
     * @param octave
     *            an octave
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
        sb.append(" name=").append(name).append(ls);
        sb.append(" description=").append(description).append(ls);
        sb.append(" key=").append(key).append(ls);
        sb.append(" oct=").append(octave).append(ls);
        sb.append(" spelling=").append(spelling).append(ls);
        sb.append(" intervals=").append(getIntervalsAsString()).append(ls);
        sb.append(" degrees=").append(getDegreesAsString()).append(ls);
        sb.append(" degAsPit=").append(getDegreesAsPitchString());
        sb.append(" isdescending=").append(isDescendingDifferent())
                .append(ls);
        if (isDescendingDifferent()) {
            sb.append(" descending=").append(getDescendingIntervalsAsString())
                    .append(ls);
        }

        return sb.toString();
    }

    public boolean contains(final Chord chord) {
        return this.contains(this.key,
                chord);
    }

    public boolean contains(final String key, final Chord chord) {
        boolean result = true;
        final int[] chordPits = chord.getPitchClasses();
        final List<Integer> pits = this.getDegreesAsPitchClasses(key);

        logger.info(String.format("key: '%s'",
                key));
        logger.info(String.format("chord pits: '%s'",
                ArrayUtils.toString(chordPits)));
        logger.info(String.format("scale: '%s'",
                pits));

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

        logger.info(String.format("returning %b",
                result));

        return result;
    }

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

    /**
     * 
     * @param key
     * @param chord
     * @param omitSymbol
     * @return the Roman representation
     */
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

    /**
     * @return the aliases
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * @param aliases
     *            the aliases to set
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

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
