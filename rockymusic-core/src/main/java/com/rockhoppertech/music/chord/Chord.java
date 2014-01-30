/**
 * 
 */
package com.rockhoppertech.music.chord;

/*
 * #%L
 * Rocky Music Core
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Interval;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * Models a more or less simultineity that can be created and modified using the
 * concepts (e.g. inversion, drop voicing etc.) of a Chord.
 * <p>
 * The {@link ChordFactory} serves as a registry and contains several factory
 * methods.
 * <p>
 * Each instance contains a {@link ChordVoicing} to which is delegated most
 * operations.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @see ChordFactory
 * @see ChordVoicing
 * @see MIDITrack
 */
public class Chord implements Cloneable, Comparable<Chord> {
    /**
     * for logging.
     */
    private static final Logger logger = LoggerFactory
            .getLogger(Chord.class);
    // bound properties
    /**
     * JavaBean property.
     */
    public static final String NOTELIST = "Chord.NOTELIST";

    /**
     * @param symbol
     *            a chord symbol.
     * @return a {@code String} with the default voicing
     */
    public static String defaultVoicing(final String symbol) {
        String v = null;
        if (symbol.indexOf("13") != -1) {
            v = "1 3 5 7 9 11 13";
        } else if (symbol.indexOf("11") != -1) {
            v = "1 3 5 7 9 11";
        } else if (symbol.indexOf("9") != -1) {
            v = "1 3 5 7 9";
        } else if (symbol.indexOf("7") != -1) {
            v = "1 3 5 7";
        } else {
            v = "1 3 5";
        }
        if (symbol.equals("unknown")) {
            v = "1";
        }
        return v;
    }

    /**
     * Given an interval measured by number of half steps return the
     * corresponding chord degree. e.g. For an interval of 3 or 4 half steps (a
     * minor or major third) return 3. Intervals greater than an octave are
     * handled.
     * 
     * @param mn
     *            number of half steps
     * @return 1 or 3 or 5 or 7 or 9 or 11 or 13 or -1
     */
    static int intervalToChordDegree(final int mn) {
        int in = mn % 12;
        while (in > 12) {
            in -= 12;
        }
        switch (in) {
        case 0:
            Chord.logger.debug(String.format("returning %d for %d", 1, in));
            return 1;
        case 1:
            Chord.logger.debug(String.format("returning %d for %d", 1, in));
            return 1;
        case 2:
            Chord.logger.debug(String.format("returning %d for %d", 9, in));
            return 9;
        case 3:
            Chord.logger.debug(String.format("returning %d for %d", 3, in));
            return 3;
        case 4:
            Chord.logger.debug(String.format("returning %d for %d", 3, in));
            return 3;
        case 5:
            Chord.logger.debug(String.format("returning %d for %d", 11, in));
            return 11;
        case 6:
            Chord.logger.debug(String.format("returning %d for %d", 11, in));
            return 11;
        case 7:
            Chord.logger.debug(String.format("returning %d for %d", 5, in));
            return 5;
        case 8:
            Chord.logger.debug(String.format("returning %d for %d", 5, in));
            return 5;
        case 9:
            Chord.logger.debug(String.format("returning %d for %d", 13, in));
            return 13;
        case 10:
            Chord.logger.debug(String.format("returning %d for %d", 7, in));
            return 7;
        case 11:
            Chord.logger.debug(String.format("returning %d for %d", 7, in));
            return 7;
        case 12:
            Chord.logger.debug(String.format("returning %d for %d", 1, in));
            return 1;
        default:
            logger.debug("dont know {}", in);
            return -1;
        }
    }

    /**
     * Default root is middle C.
     */
    private int root = Pitch.C5;
    /**
     * The quality of the chord. e.g. m7 or 7b5. Does not contain the root e.g.
     * cm7
     */
    private String symbol;

    /**
     * A description of the chord.
     */
    private String description;
    /**
     * The chord's spelling, e.g. "1 3 5".
     */
    private String spelling;
    /**
     * The intervals are absolute from the root. e.g. Cmaj = 4 7
     */
    private int[] intervals;
    /**
     * Alias names for this chord.
     */
    private final List<String> aliases = new ArrayList<String>();
    /**
     * How this chord is voiced.
     */
    private ChordVoicing chordVoicing;
    /**
     * Default start beat is 1.
     */
    private double startBeat = 1d;
    /**
     * Default duration is 4 beats.
     */
    private double duration = 4d;
    /**
     * Default is 0, i.e. no drop.
     */
    private int drop = 0;
    /**
     * Default is 0, i.e. no inversion.
     */
    private int inversion = 0;

    /**
     * The bass pitch specified C/G for example.
     */
    private String bass;

    /**
     * Initialize a {@codeChord} without a symbol.
     * 
     * @param root
     *            MIDI number of the root
     * @param intervals
     *            the intervals
     * @param description
     *            a description
     */
    public Chord(final int root, final int[] intervals,
            final String description) {
        this(root, "unknown", intervals, description);
    }

    /**
     * Initialize a {@codeChord} with these values.
     * 
     * @param root
     *            MIDI number of the root pitch.
     * @param symbol
     *            the symbol
     * @param intervals
     *            the intervals in the Chord
     * @param description
     *            a description
     */
    public Chord(final int root, final String symbol, final int[] intervals,
            final String description) {
        this.root = root;
        if (symbol != null) {
            this.symbol = symbol;
        }
        if (description != null) {
            this.description = description;
        }
        if (intervals != null) {
            this.intervals = new int[intervals.length];
            System
                    .arraycopy(intervals, 0, this.intervals, 0,
                            this.intervals.length);
        }

        this.chordVoicing = new ChordVoicing(this.root / 12, Chord
                .defaultVoicing(this.symbol));
    }

    /**
     * ChordFactory has static constants for many descriptions.
     * 
     * @param symbol
     *            a chord symbol
     * @param intervals
     *            the intervals
     * @param description
     *            a description
     */
    public Chord(final String symbol, final int[] intervals,
            final String description) {
        this(Pitch.C5, symbol, intervals, description);
    }

    /**
     * Called from ChordFactoryXMLHelper if the intervals are specified in the
     * XML file.
     * 
     * @param symbol
     *            the Chord symbol
     * @param intervals2
     *            the intervals to use
     * @param description
     *            the Chord's description
     */
    public Chord(final String symbol, final Integer[] intervals2,
            final String description) {
        this.symbol = symbol;
        this.description = description;
        this.intervals = new int[intervals2.length];
        int j = 0;
        for (final Integer i : intervals2) {
            this.intervals[j++] = i.intValue();
        }
        this.chordVoicing = new ChordVoicing(Chord.defaultVoicing(symbol));
    }

    /**
     * e.g. {@code Chord("maj", "1 3 5", "major");}. Called from
     * ChordFactoryXMLHelper only if the intervals are not in the XML file but
     * the spelling is.
     * 
     * @param symbol
     *            a chord symbol
     * @param spelling
     *            the spelling
     * @param description
     *            a description
     */
    public Chord(final String symbol, final String spelling,
            final String description) {
        this.symbol = symbol;
        this.description = description;
        this.spelling = spelling;
        this.intervals = ChordFactory.degreesToIntervals(spelling);
        // this.notelist = ChordFactory.createMIDITrack(this);
        this.chordVoicing = new ChordVoicing(Chord.defaultVoicing(symbol));
    }

    /**
     * Copy constructor. clone() is evil.
     * 
     * @param c
     *            the {@code Chord} to copy.
     */
    public Chord(final Chord c) {
        this(c.root, c.symbol, c.intervals, c.description);
        this.duration = c.duration;
        this.inversion = c.inversion;
        // this.aliases
        this.bass = c.bass;
        this.drop = c.drop;
        this.startBeat = c.startBeat;

        // this.root = c.root;
        // this.symbol = c.symbol;
        // this.description = c.description;
        // this.intervals = new int[c.intervals.length];
        // System
        // .arraycopy(c.intervals, 0, this.intervals, 0,
        // this.intervals.length);

        // this.createNotelist();
        // this.notelist = ChordFactory.createMIDITrack(this);
        // this.chordVoicing = new ChordVoicing(this.root / 12, Chord
        // .defaultVoicing(c.symbol));

        // this.symbol = new String(c.symbol);
        // this.setSymbol(c.symbol);
        // this.description = new String(c.description);
        // this.spelling = c.spelling == null ? "" : new String(c.spelling);
        // this.intervals = ChordFactory.degreesToIntervals(c.spelling);
        // this.chordVoicing = new ChordVoicing(Chord.defaultVoicing(c.symbol));
    }

    /**
     * Add an alias for the chord.
     * 
     * @param alias
     *            an alias.
     */
    public final void addAlias(final String alias) {
        this.aliases.add(alias);
    }

    /*
     * We're using the prototype design pattern.
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        Chord result = null;
        try {
            // copy the bitwise primitives
            result = (Chord) super.clone();
            result.symbol = this.symbol;
            result.description = this.description;
            if (this.spelling != null) {
                result.spelling = this.spelling;
                // what about intervals?
            }

            // result.chordVoicing = new ChordVoicing(this.root / 12,
            // this.chordVoicing.getDisplayName());
            result.chordVoicing = new ChordVoicing(this.chordVoicing
                    .getDisplayName());
            // to hell with the rest
            // if (this.notelist != null)
            // result.notelist = new MIDITrack(this.notelist);
        } catch (final CloneNotSupportedException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    @Override
    public int compareTo(final Chord o) {
        return this.symbol.compareTo(o.symbol);
    }

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
        final Chord other = (Chord) obj;
        if (this.drop != other.drop) {
            Chord.logger.debug(String.format("drop not the same %d != %d",
                    this.drop, other.drop));
            return false;
        }
        if (Math.abs(this.startBeat - other.startBeat) > .0000001) {
            Chord.logger.debug(String.format("startBeat not the same %f != %f",
                    this.startBeat, other.startBeat));
            return false;
        }

        if (Math.abs(this.duration - other.duration) > .0000001) {
            Chord.logger.debug(String.format("duration not the same %f != %f",
                    this.duration, other.duration));
            return false;
        }
        if (this.chordVoicing.equals(other.chordVoicing) == false) {
            Chord.logger.debug(String.format(
                    "chordVoicing not the same %s != %s", this.chordVoicing,
                    other.chordVoicing));
            return false;
        }
        if (!Arrays.equals(this.intervals, other.intervals)) {
            Chord.logger.debug(String.format("intervals are not the same"));
            return false;
        }
        if (this.root != other.root) {
            Chord.logger.debug(String.format("root not the same %d != %d",
                    this.root, other.root));
            return false;
        }
        if (this.symbol == null) {
            if (other.symbol != null) {
                return false;
            }
        } else if (!this.symbol.equals(other.symbol)) {
            Chord.logger.debug(String.format("symbol not the same %s != %s",
                    this.symbol, other.symbol));
            return false;
        }
        return true;
    }

    // public ChordVoicing getChordVoicing() {
    // return this.chordVoicing;
    // }
    //
    // public void setChordVoicing(ChordVoicing chordVoicing) {
    // logger.debug("Setting voicing to " + chordVoicing.getDisplayName());
    // this.chordVoicing = chordVoicing;
    // this.chordVoicing.setOctave(this.getOctave());
    // // this.updateNotelist();
    // }

    /**
     * Get the aliases for this chord.
     * 
     * @return a List of aliases
     */
    public List<String> getAliases() {
        return this.aliases;
    }

    /**
     * When a chord was created with a String containing the bass.
     * 
     * @return the bass note as a string
     */
    public String getBass() {
        return this.bass;
    }

    /**
     * Return the MIDI number of the pitch that is the specified chord degree.
     * Given 13 for a C13 chord you get back the A in the octave above the
     * root's octave.
     * 
     * <pre>
     * int theSeventh = chord.getChordDegree(7);
     * </pre>
     * 
     * If the chord does not contain the specified degree -1 is returned. (e.g.
     * getChordDegree(13) for a triad).
     * 
     * @param degree
     *            1,3,5,7,9,11, or 13
     * @return a MIDI number or -1 for an invalid degree
     */
    public final int getChordDegree(final int degree) {
        int cd = -1;
        switch (degree) {
        case 1:
            cd = this.root;
            break;
        case 3:
            cd = this.getThird();
            break;
        case 5:
            cd = this.getFifth();
            break;
        case 7:
            if (this.hasSeventh()) {
                cd = this.getSeventh();
            }
            break;
        case 9:
            if (this.hasNinth()) {
                cd = this.getNinth();
            }
            break;
        case 11:
            if (this.hasEleventh()) {
                cd = this.getEleventh();
            }
            break;
        case 13:
            if (this.hasThirteenth()) {
                cd = this.getThirteenth();
            }
            break;

        default:
            cd = -1;
        }
        return cd;
    }

    /**
     * Get the current voicing as a String.
     * 
     * @return the chord voicing
     */
    public String getChordVoicing() {
        return this.chordVoicing.getDisplayName();
    }

    /**
     * Get the default voicing. Looks at this {@code Chord}'s symbol.
     * 
     * @return the default voicing
     */
    public String getDefaultVoicingString() {
        return Chord.defaultVoicing(this.symbol);
    }

    /**
     * Uses the chord's startBeat and durations properties.
     * 
     * @return a MIDITrack
     * @see MIDITrack
     */
    // public MIDITrack getVoicedNotelist() {
    // //return this.notelist;
    // return this.chordVoicing.getNoteList(this);
    // }
    //
    // public MIDITrack getVoicedNotelist(int octave) {
    // return this.chordVoicing.getNoteList(octave, this);
    // }
    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the root pitch and symbol. e.g. Cmaj7.
     * 
     * @return a String
     */
    public String getDisplayName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(PitchFormat.getPitchString(this.root));
        if (this.symbol.equals("maj") == false) {
            sb.append(this.symbol);
        }
        if (this.bass != null) {
            sb.append('/').append(this.bass);
        }
        return sb.toString().trim();
    }

    /**
     * Concatenates the aliases with the main display name.
     * 
     * @return a String
     */
    public String getDisplayNameWithAliases() {
        final StringBuilder sb = new StringBuilder();
        final String pitch = PitchFormat.getPitchString(this.root);

        sb.append(pitch);
        if (this.symbol.equals("maj") == false) {
            sb.append(this.symbol);
        }
        for (final String alias : this.aliases) {
            sb.append(", ").append(pitch).append(alias);
        }
        return sb.toString().trim();
    }

    /**
     * @return the drop
     */
    public int getDrop() {
        return this.drop;
    }

    /**
     * @return the duration
     */
    public double getDuration() {
        return this.duration;
    }

    /**
     * Uses the intervals to determine the eleventh. Use this only if you have
     * an 11th chord.
     * 
     * @return the eleventh
     */
    public int getEleventh() {
        return this.root + this.intervals[4];
    }

    /**
     * Uses the intervals to determine the fifth.
     * 
     * @return the fifth
     */
    public int getFifth() {
        return this.root + this.intervals[1];
    }

    /**
     * Is not a copy.
     * 
     * @return the intervals
     */
    public int[] getIntervals() {
        return Arrays.copyOf(this.intervals, this.intervals.length);
    }

    /**
     * Creates and returns a String representaiton of the Chord's intervals.
     * 
     * @return a String representation
     */
    public String getIntervalstoString() {
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
     * Get the current chord inversion.
     * 
     * @return the inversion
     */
    public int getInversion() {
        return this.inversion;
    }

    /**
     * Find the maximum interval in the chord.
     * 
     * @return the maximum interval
     */
    public int getMaxInterval() {
        int max = Integer.MIN_VALUE;
        for (final int interval : this.intervals) {
            if (interval > max) {
                max = interval;
            }
        }
        return max;
        // List<Integer> list = Arrays.asList(this.intervals);
        // return Collections.max(list);
    }

    /**
     * Uses the intervals to determine the ninth. Must be at least a 9th chord.
     * 
     * @return the ninth
     */
    public int getNinth() {
        return this.root + this.intervals[3];
    }

    /**
     * Turn this {@code Chord} into a {@code MIDITrack} and return it.
     * 
     * @return a new MIDITrack
     */
    public MIDITrack createMIDITrack() {
        final MIDITrack track = this.chordVoicing.getTrack(this);
        track.sortByAscendingPitches();
        return track;
        // return this.notelist;
    }

    /**
     * Determine how many inversions of this chord are possible. For a triad the
     * number is 3.
     * 
     * @return number of inversions
     */
    public int getNumberOfInversions() {
        return this.intervals.length + 1;
    }

    /**
     * Get the MIDI number of a pitch an octave about this {@code Chord}'s root.
     * 
     * @return a MIDI number
     */
    public int getOctave() {
        return this.root / 12;
    }

    /**
     * Get the pitch classes for this {@code Chord}.
     * 
     * @return an array of PCs
     */
    public int[] getPitchClasses() {
        final int root = this.getRoot() % 12;
        final int[] ivls = this.getIntervals();
        final int[] pcs = new int[ivls.length + 1];
        pcs[0] = root;
        for (int i = 0; i < ivls.length; i++) {
            pcs[i + 1] = root + ivls[i];
        }
        return pcs;
    }

    /**
     * Get the MIDI number of the root pitch.
     * 
     * @return the root
     */
    public int getRoot() {
        return this.root;
    }

    /**
     * Find which currently defined {@code Scale}s contain the pitches in this
     * {@code Chord}.
     * 
     * @return a {@code Set} of {@code Scale}s
     */
    public Set<Scale> getScales() {
        final Set<Scale> set = new HashSet<Scale>();
        for (final Scale scale : ScaleFactory.getAll()) {
            for (final String key : PitchFormat.getFlatPitches()) {
                if (scale.contains(key, this)) {
                    final Scale s = (Scale) scale.clone();
                    s.setKey(key);
                    set.add(s);
                }
            }
        }
        return set;
    }

    // TODO
    /**
     * Given a Chord, determine which Scales to use. AKA Jazz improv 101.
     * 
     * @return a {@code Set} of {@code Scale}s
     */
    public Set<Scale> getScalesByChordQuality() {
        return null;
    }

    /**
     * Return the MIDI number of this chord's 7th. Must be at least a 7th chord.
     * 
     * @return the MIDI number of the 7th
     */
    public int getSeventh() {
        return this.root + this.intervals[2];
    }

    /**
     * The size of a triad is 3 (root, third, fifth). A seventh chord is 4 etc.
     * 
     * @return an int representing the size
     */
    public int getSize() {
        return this.intervals.length + 1;
    }

    /**
     * Retrieve the chord's spelling.
     * 
     * @return a String with the spelling
     */
    public String getSpelling() {
        final StringBuilder sb = new StringBuilder();
        sb.append("1 ");
        for (final int interval : this.intervals) {
            sb.append(Interval.getSpelling(interval));
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * @return the startBeat
     */
    public double getStartBeat() {
        return this.startBeat;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Well, ok let's just call it a third. It might not be an actual third
     * since we support non-"traditional" chords.
     * 
     * @return the third of the chord
     */
    public int getThird() {
        return this.root + this.intervals[0];
    }

    /**
     * Return the MIDI number of the 13th of this chord. Must be a 13th chord.
     * 
     * @return the MIDI number of the 13th
     */
    public int getThirteenth() {
        return this.root + this.intervals[5];
    }

    /**
     * Creates a new String with pretty much the default voicing.
     * 
     * @param notelist
     *            a {@code MIDITrack}
     * @return a voicing string
     */
    public String getVoicingString(final MIDITrack notelist) {
        final StringBuilder sb = new StringBuilder();
        final int octave = this.getRoot() / 12;

        for (final MIDINote note : notelist) {
            final int no = note.getMidiNumber() / 12;
            final int n = note.getMidiNumber() % 12;
            for (int i = octave; i < no; i++) {
                sb.append("+");
            }
            if (n == this.getRoot() % 12) {
                sb.append("1 ");
            }
            if (n == this.getThird() % 12) {
                sb.append("3 ");
            }
            if (n == this.getFifth() % 12) {
                sb.append("5 ");
            }
            if (this.hasSeventh() && (n == this.getSeventh() % 12)) {
                sb.append("7 ");
            }
            if (this.hasNinth() && (n == this.getNinth() % 12)) {
                sb.append("9 ");
            }
            if (this.hasEleventh() && (n == this.getEleventh() % 12)) {
                sb.append("11 ");
            }
            if (this.hasThirteenth() && (n == this.getThirteenth() % 12)) {
                sb.append("13 ");
            }
        }
        Chord.logger.debug("Voicing string: " + sb.toString());
        return sb.toString().trim();
    }

    /**
     * Predicate to determine if this is an 11th chord.
     * 
     * @return a boolean
     */
    public boolean hasEleventh() {
        return this.intervals.length >= 5;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.drop;
        result = prime * result + Arrays.hashCode(this.intervals);
        result = prime * result + this.root;
        result = prime * result
                + ((this.symbol == null) ? 0 : this.symbol.hashCode());
        return result;
    }

    /**
     * Predicate to determine if this is an 9th chord.
     * 
     * @return a boolean
     */
    public boolean hasNinth() {
        return this.intervals.length >= 4;
    }

    /**
     * Predicate to determine if this is an 7th chord.
     * 
     * @return a boolean
     */
    public boolean hasSeventh() {
        // uh, er, well...
        return this.intervals.length >= 3;
    }

    /**
     * Predicate to determine if this is an 13th chord.
     * 
     * @return a boolean
     */
    public boolean hasThirteenth() {
        return this.intervals.length >= 6;
    }

    // FIXME
    /**
     * Determine if the given intervals are diatonic.
     * 
     * @param someIntervals
     *            an array of intervals
     * @return a boolean
     */
    public boolean isDiatonic(final int[] someIntervals) {
        if (this.intervals.length != someIntervals.length) {
            // Chord.logger.debug("No size Match");
            return false;
        }

        Chord.logger.debug(String.format("checking this: %s", ArrayUtils
                .toString(this.intervals)));
        Chord.logger.debug(String.format("checking against: %s", ArrayUtils
                .toString(someIntervals)));

        for (int i = 0; i < this.intervals.length; i++) {
            // logger.debug(String.format("checking %d against %d",
            // this.intervals[i], someIntervals[i]));
            if (this.intervals[i] != someIntervals[i]) {
                Chord.logger.debug(String.format("%d != %d", this.intervals[i],
                        someIntervals[i]));
                return false;
            }
        }
        Chord.logger.debug("Match");

        return true;
    }

    /**
     * Predicate to determine if the chord is diminished.
     * 
     * @return a boolean
     */
    public boolean isDiminished() {
        // remember that the intervals are all relative
        // to the root.
        if ((this.intervals.length >= 2)
                && (this.intervals[0] == Interval.MINOR_THIRD)
                && (this.intervals[1] == Interval.AUGMENTED_FOURTH)) {
            return true;
        }
        return false;
    }

    /**
     * Predicate to determine if the chord is dominant.
     * 
     * @return a boolean
     */
    public boolean isDominant() {
        if ((this.intervals.length >= 3)
                && (this.intervals[2] == Interval.MINOR_SEVENTH)
                && this.isMajor()) {
            return true;
        }
        return false;
    }

    /**
     * Predicate to determine if the chord is major.
     * 
     * @return a boolean
     */
    public boolean isMajor() {
        if (Chord.logger.isDebugEnabled()) {
            Chord.logger.debug("1 " + this.intervals[1]);
            Chord.logger.debug("0 " + this.intervals[0]);
        }
        if (this.intervals[0] == Interval.MAJOR_THIRD) {
            return true;
        }
        return false;
    }

    /**
     * Predicate to determine if the chord is minor.
     * 
     * @return a boolean
     */
    public boolean isMinor() {
        if (this.intervals[0] == Interval.MINOR_THIRD) {
            return true;
        }
        return false;
    }

    /**
     * Given a MIDI number return whether it is the root (1), third (3) etc. If
     * it is not in the chord then the return value is -1. The pitch class is
     * used rather than the actual pitch. (i.e. pc 0 instead of 60)
     * 
     * @param mn
     *            a MIDI number.
     * @return 1 or 3 or 5 or 7 or 9 or 11 or 13 or -1
     */
    int pitchToChordDegree(final int mn) {
        int in = mn % 12;

        int degree = -1;
        if (this.root % 12 == in) {
            degree = 1;
        } else if ((this.root + this.intervals[0]) % 12 == in) {
            degree = 3;
        } else if ((this.root + this.intervals[1]) % 12 == in) {
            degree = 5;
        }
        if (this.hasSeventh() && ((this.root + this.intervals[2]) % 12 == in)) {
            degree = 7;
        }
        if (this.hasNinth() && ((this.root + this.intervals[3]) % 12 == in)) {
            degree = 9;
        }
        if (this.hasEleventh()
                && ((this.root + this.intervals[4]) % 12 == in)) {
            degree = 11;
        }
        if (this.hasThirteenth()
                && ((this.root + this.intervals[5]) % 12 == in)) {
            degree = 13;
        }

        Chord.logger.debug(String.format("midi number %d = degree %d", in,
                degree));
        return degree;
    }

    /**
     * Set this chord's voicing to the default.
     */
    public void resetVoicing() {
        this.setChordVoicing(this.getDefaultVoicingString());
    }

    /**
     * For a chord notated like Cmaj7/B where B is in the bass.
     * 
     * @param bass
     *            the bass pitch
     */
    public void setBassPitch(final String bass) {
        this.bass = bass;
        final int in = PitchFactory.getPitch(bass).getMidiNumber();
        final int degree = this.pitchToChordDegree(in);
        if (Chord.logger.isDebugEnabled()) {
            Chord.logger.debug(String.format("bass degree=%d", degree));
        }
        switch (degree) {
        case 3:
            this.setInversion(1);
            break;
        case 5:
            this.setInversion(2);
            break;
        case 7:
            this.setInversion(3);
            break;
        case 9:
            this.setInversion(4);
            break;
        case 11:
            this.setInversion(5);
            break;
        case 13:
            this.setInversion(6);
            break;
        default:
            logger.error("WTF? degree=" + degree);
        }
    }

    // damn propertydescriptor nonsense
    /**
     * get the Bass pitch as a String.
     * 
     * @return a String
     */
    public String getBassPitch() {
        return this.bass;
    }

    /**
     * Creates and sets a new ChordVoicing instance based on the voicing string
     * and the chord's current octave.
     * 
     * @param chordVoicingString
     *            a voicing string
     * @see ChordVoicing
     */
    public void setChordVoicing(final String chordVoicingString) {
        this.chordVoicing = new ChordVoicing(this.getOctave(),
                chordVoicingString);
        Chord.logger.debug("Setting voicing to "
                + this.chordVoicing.getDisplayName());
    }

    /**
     * Set the voicing to closed position.
     */
    public void setClosed() {
        final StringBuilder sb = new StringBuilder("1 ");
        final int[] closed = new int[this.intervals.length];

        // for (int i = 0; i < intervals.length; i++) {
        // int in = intervals[i];
        // while (in > 12) {
        // in -= 12;
        // //sb.append("-");
        // }
        // // sb.append(intervalToDegree(intervals[i])).append(' ');
        // //sb.append(intervalToDegree(in)).append(' ');
        // closed[i] = in;
        // }
        // Arrays.sort(closed);
        // for (int i = 0; i < closed.length; i++) {
        // sb.append(intervalToDegree(closed[i])).append(' ');
        // }

        for (int i = 0; i < this.intervals.length; i++) {
            int in = this.intervals[i];
            while (in > 12) {
                in -= 12;
                sb.append("-");
            }
            sb.append(Chord.intervalToChordDegree(in)).append(' ');
            closed[i] = in;
        }

        Chord.logger.debug(String.format("intervals: %s", ArrayUtils
                .toString(this.intervals)));
        Chord.logger.debug(String.format("closed intervals: %s", ArrayUtils
                .toString(closed)));
        this.setChordVoicing(sb.toString());
        this.setIntervals(closed);
        Chord.logger.debug("Closed Voicing string: " + sb.toString());
    }

    /**
     * Set the {@code Chord}'s description.
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Drop voicing from closed voicing.
     * 
     * @param drop
     *            the drop voicing.
     */
    public void setDrop(final int drop) {
        if (drop > this.getSize() + 1) {
            throw new IllegalArgumentException("Drop too large " + drop);
        }
        this.drop = drop;
        final String chordVoicingString = ChordVoicing
                .getDropString(this, drop);
        if (Chord.logger.isDebugEnabled()) {
            Chord.logger.debug(String.format("new voicing '%s'",
                    chordVoicingString));
        }
        this.setChordVoicing(chordVoicingString);
        // this.notelist = ChordFactory.createMIDITrack(this);
        // return this;
    }

    /**
     * Set the {@code Chord}'s duration.
     * 
     * @param duration
     *            the duration to set
     */
    public void setDuration(final double duration) {
        this.duration = duration;
        // this.notelist = ChordFactory.createMIDITrack(this);
        // return this;
    }

    /**
     * Set the {@code Chord}'s intervals.
     * 
     * @param intervals
     *            the intervals to set
     * @return this to cascade calls
     */
    public Chord setIntervals(final int[] intervals) {
        this.intervals = Arrays.copyOf(intervals, intervals.length);
        // this.notelist = ChordFactory.createMIDITrack(this);
        return this;
    }

    /**
     * Set the {@code Chord}'s inversion.
     * 
     * @param inversion
     *            the inversion to set
     */
    public void setInversion(final int inversion) {
        if (inversion > this.intervals.length + 1) {
            throw new IllegalArgumentException("Inversion too large "
                    + inversion);
        }

        this.inversion = inversion;
        // this.notelist = ChordFactory.createMIDITrack(this);

        this.chordVoicing.setInversion(this, inversion);

        // if the return type is NOT void then this is not recognized as a
        // property
        // by the bean descriptor - unless you write a beaninfo. bleah.
        // return this;
    }

    /**
     * Set the {@code Chord}'s octave of the root.
     * 
     * @param octave
     *            the octave
     */
    public void setOctave(final int octave) {
        final int r = octave * 12 + this.root % 12;
        this.setRoot(r);
        this.chordVoicing.setOctave(octave);
    }

    /**
     * Set the {@code Chord}'s root.
     * 
     * @param root
     *            the root to set
     */
    public void setRoot(final int root) {
        this.root = root;
        // this.updateNotelist();
        // this.notelist = ChordFactory.createMIDITrack(this);
        // return this;
    }

    /**
     * Set the {@code Chord}'s root and duration.
     * 
     * @param root
     *            a MIDI number
     * @param duration
     *            the duration
     * @return this to cascade calls
     */
    public Chord setRootAndDuration(final int root, final double duration) {
        this.root = root;
        this.duration = duration;
        // this.notelist = ChordFactory.createMIDITrack(this);
        return this;
    }

    /**
     * Set the {@code Chord}'s start beat.
     * 
     * @param startBeat
     *            the startBeat to set
     */
    public void setStartBeat(final double startBeat) {
        this.startBeat = startBeat;
        // this.notelist = ChordFactory.createMIDITrack(this);
    }

    /**
     * Tries to set this chord's intervals to a registered chord. If the symbol
     * is not registered with the ChordFactory then this just sets the instance
     * field.
     * 
     * @param symbol
     *            the symbol to set e.g. maj7
     */
    public void setSymbol(final String symbol) {
        this.symbol = symbol;
        Chord c = null;
        try {
            c = ChordFactory.getChordBySymbol(symbol);
            // TODO throw instead
        } catch (final UnknownChordException e) {
            e.printStackTrace();
        }
        if (Chord.logger.isDebugEnabled()) {
            final String s = String.format("symbol '%s' created %s", symbol, c);
            Chord.logger.debug(s);
        }
        if (c != null) {
            this.intervals = new int[c.getIntervals().length];
            System.arraycopy(c.getIntervals(), 0, this.intervals, 0,
                    this.intervals.length);
            this.description = c.getDescription();
            // this.notelist = ChordFactory.createMIDITrack(this);
            // this.chordVoicing = new ChordVoicing(defaultVoicing(symbol));
            // this.chordVoicing = new ChordVoicing(this.root / 12, c
            // .getChordVoicing().getDisplayName());
            this.setChordVoicing(c.getChordVoicing());
            String sp = c.spelling;
            if (sp == null) {
                sp = this.getSpelling();
            }
            this.spelling = sp;
            for (final String alias : c.aliases) {
                this.addAlias(alias);
            }
            // this.startBeat = c.startBeat;
            // this.duration = c.duration;
            // this.drop = c.drop;
            // this.inversion = c.inversion;
            // this.root = c.root;
        } else {
            logger.debug("I don't know that symbol {}", symbol);

        }
    }

    /**
     * Uses the {@code RomanChordParser} to get the Roman numeral for this
     * {@code Chord}'s position in the specified {@code Scale}.
     * 
     * @see Scale
     * @see RomanChordParser
     * @param scale
     *            the Scale to use
     * @param key
     *            the Scale's key.
     * @return a String
     */
    public String toRoman(final Scale scale, final String key) {
        final String s = String.format("%s%s", RomanChordParser
                .pitchNameToRoman(scale, PitchFormat.getPitchString(this
                        .getRoot()), key), this.getSymbol());
        return s;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append(" start=").append(this.startBeat);
        sb.append(" duration=").append(this.duration);
        sb.append(" root=").append(this.root);
        sb.append(" symbol=").append(this.symbol);
        for (final String a : this.aliases) {
            sb.append(" alias=").append(a);
        }
        sb.append(" inversion=").append(this.inversion);
        sb.append(" description=").append(this.description);
        sb.append(" voicing=").append(this.chordVoicing);
        if (this.bass != null) {
            sb.append(" bass=").append(this.bass);
        }
        sb.append(" intervals=");
        for (final int interval : this.intervals) {
            sb.append(interval);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * Get the end beat, which is start beat + duration.
     * 
     * @return when this Chord stops sounding
     */
    public double getEndBeat() {
        return this.startBeat + this.duration;
    }
}
