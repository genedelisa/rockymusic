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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.collections.ArrayFunctions;
import com.rockhoppertech.music.Interval;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper;
import com.rockhoppertech.music.scale.Scale;

/**
 * GoF Factory design pattern for chords.
 * 
 * <p>
 * The definitions are stored in an XML file named "choarddefs.xml".
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ChordFactory {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordFactory.class);
    static List<Chord> midiChords;
    /** the key is the chord symbol */
    static Map<String, Chord> midiChordMap;
    static String definitionFileName = "chorddefs.xml";

    static {
        init();
    }

    static void init() {
        midiChords = new ArrayList<Chord>();
        midiChordMap = Collections
                .synchronizedMap(new LinkedHashMap<String, Chord>());
        try {
            ChordFactoryXMLHelper.setDefinitionFileName(definitionFileName);
            loadDefinitions();
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("loading defs", e);
            }
        }
    }

    /** the descriptions */
    public static final String MAJOR = "major";
    public static final String SUSPENDED_FOURTH = "suspended fourth";
    public static final String MAJOR_SEVENTH = "major seventh";
    public static final String MAJOR_SEVENTH_FLAT_5 = "major seventh flat 5";
    public static final String MAJOR_SEVENTH_SHARP_5 = "major seventh sharp 5";
    public static final String MAJOR_SEVENTH_SUSPENDED_FOURTH = "major seventh suspended fourth";
    public static final String ADDED_AUGMENTED_ELEVENTH = "added augmented eleventh";
    public static final String SIX = "six";
    public static final String MAJOR_NINTH = "major ninth";
    public static final String MAJOR_NINTH_AUGMENTED_FIFTH = "major ninth augmented fifth";
    public static final String MAJOR_NINTH_DIMINISHED_FIFTH = "major ninth diminished fifth";
    public static final String MAJOR_SEVENTH_AUGMENTED_ELEVENTH = "major seventh augmented eleventh";
    public static final String SIX_SEVEN = "six seven";
    public static final String MAJOR_SIXTH_ADDED_NINTH = "major sixth added ninth";
    public static final String SIX_SEVEN_SUSPENDED = "six seven suspended";
    public static final String MAJOR_ELEVENTH = "major eleventh";
    public static final String MAJOR_ELEVENTH_AUGMENTED_FIFTH = "major eleventh augmented fifth";
    public static final String MAJOR_NINTH_AUGMENTED_ELEVENTH = "major ninth augmented eleventh";
    public static final String MAJOR_THIRTEENTH = "major thirteenth";
    public static final String MAJOR_THIRTEENTH_AUGMENTED_ELEVENTH = "major thirteenth augmented eleventh";
    public static final String MINOR_MAJOR_SEVENTH = "minor/major seventh";
    public static final String MINOR_MAJOR_NINTH = "minor/major ninth";
    public static final String MINOR_MAJOR_ELEVENTH = "minor/major eleventh";
    public static final String MINOR_MAJOR_THIRTEENTH = "minor/major thirteenth";
    public static final String MINOR = "minor";
    public static final String MINOR_SEVENTH = "minor seventh";
    public static final String HALF_DIMINISHED = "half diminished";
    public static final String MINOR_SIXTH = "minor sixth";
    public static final String MINOR_NINTH = "minor ninth";
    public static final String MINOR_NINTH_DIMINISHED_FIFTH = "minor ninth diminished fifth";
    public static final String MINOR_SEVENTH_FLAT_NINE = "minor seventh flat nine";
    public static final String MINOR_SEVENTH_FLAT_NINE_DIMINISHED_FIFTH = "minor seventh flat nine diminished fifth";
    public static final String MINOR_SEVEN_ELEVEN = "minor seven eleven";
    public static final String MINOR_SIX_SEVEN = "minor six seven";
    public static final String MINOR_SIX_NINE = "minor six nine";
    public static final String MINOR_ELEVENTH = "minor eleventh";
    public static final String MINOR_ELEVENTH_FLAT_NINE = "minor eleventh flat nine";
    public static final String MINOR_ELEVENTH_FLAT_NINE_DIMINISHED_FIFTH = "minor eleventh flat nine diminished fifth";
    public static final String MINOR_ELEVENTH_DIMINISHED_FIFTH = "minor eleventh diminished fifth";
    public static final String MINOR_SIX_SEVEN_ELEVEN = "minor six seven eleven";
    public static final String MINOR_THIRTEEN_ELEVEN = "minor thirteen eleven";
    public static final String MINOR_THIRTEENTH = "minor thirteenth";
    public static final String MINOR_THIRTEENTH_FLAT_NINE = "minor thirteenth flat nine";
    public static final String MINOR_THRITEENTH_DIMINISHED_FIFTH = "minor thriteenth diminished fifth";
    public static final String DIMINISHED = "diminished";
    public static final String DIMINISHED_SEVENTH = "diminished seventh";
    public static final String AUGMENTED = "augmented";
    public static final String SEVENTH = "seventh";
    public static final String SEVENTH_FLAT_5 = "seventh flat 5";
    public static final String SEVENTH_SHARP_5 = "seventh sharp 5";
    public static final String NINTH = "ninth";
    public static final String NINTH_DIMINISHED_FIFTH = "ninth diminished fifth";
    public static final String NINTH_AUGMENTED_FIFTH = "ninth augmented fifth";
    public static final String SEVENTH_FLAT_9 = "seventh flat 9";
    public static final String SEVENTH_FLAT_9_DIMISHED_FIFTH = "seventh flat 9 dimished fifth";
    public static final String SEVENTH_FLAT_9_AUGMENTED_FIFTH = "seventh flat 9 augmented fifth";
    public static final String SEVENTH_AUGMENTED_NINTH = "seventh augmented ninth";
    public static final String SEVENTH_AUGMENTED_NINTH_DIMINISHED_FIFTH = "seventh augmented ninth diminished fifth";
    public static final String SEVENTH_AUGMENTED_NINTH_AUGMENTED_FIFTH = "seventh augmented ninth augmented fifth";
    public static final String SEVEN_ELEVEN = "seven eleven";
    public static final String ELEVENTH = "eleventh";
    public static final String ELEVENTH_AUGMENTED_NINTH = "eleventh augmented ninth";
    public static final String ELEVENTH_AUGMENTED_NINTH_AUGMENTED_FIFTH = "eleventh augmented ninth augmented fifth";
    public static final String ELEVENTH_AUGMENTED_NINTH_DIMINISHED_FIFTH = "eleventh augmented ninth diminished fifth";
    public static final String ELEVENTH_DIMINISHED_NINTH = "eleventh diminished ninth";
    public static final String ELEVENTH_DIMINISHED_NINTH_AUGMENTED_FIFTH = "eleventh diminished ninth augmented fifth";
    public static final String ELEVENTH_DIMINISHED_NINTH_DIMINISHED_FIFTH = "eleventh diminished ninth diminished fifth";
    public static final String SEVENTH_AUGMENTED_ELEVENTH = "seventh augmented eleventh";
    public static final String SEVENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH = "seventh augmented eleventh augmented ninth";
    public static final String SEVENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH_AUGMENTED_FIFTH = "seventh augmented eleventh augmented ninth augmented fifth";
    public static final String SEVENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH_DIMINISHED_FIFTH = "seventh augmented eleventh augmented ninth diminished fifth";
    public static final String SEVENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH = "seventh augmented eleventh diminished ninth";
    public static final String SEVENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH_AUGMENTED_FIFTH = "seventh augmented eleventh diminished ninth augmented fifth";
    public static final String SEVENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH_DIMINISHED_FIFTH = "seventh augmented eleventh diminished ninth diminished fifth";
    public static final String THIRTEENTH = "thirteenth";
    public static final String THIRTEENTH_AUGMENTED_NINTH = "thirteenth augmented ninth";
    public static final String THIRTEENTH_AUGMENTED_NINTH_AUGMENTED_FIFTH = "thirteenth augmented ninth augmented fifth";
    public static final String THIRTEENTH_AUGMENTED_NINTH_DIMINISHED_FIFTH = "thirteenth augmented ninth diminished fifth";
    public static final String THIRTEENTH_DIMINISHED_NINTH = "thirteenth diminished ninth";
    public static final String THIRTEENTH_AUGMENTED_ELEVENTH = "thirteenth augmented eleventh";
    public static final String THIRTEENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH = "thirteenth augmented eleventh augmented ninth";
    public static final String THIRTEENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH_AUGMENTED_FIFTH = "thirteenth augmented eleventh augmented ninth augmented fifth";
    public static final String THIRTEENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH_DIMINISHED_FIFTH = "thirteenth augmented eleventh augmented ninth diminished fifth";
    public static final String THIRTEENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH = "thirteenth augmented eleventh diminished ninth";
    public static final String THIRTEENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH_AUGMENTED_FIFTH = "thirteenth augmented eleventh diminished ninth augmented fifth";
    public static final String THIRTEENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH_DIMINISHED_FIFTH = "thirteenth augmented eleventh diminished ninth diminished fifth";
    public static final String THIRTEENTH_SUSPENDED = "thirteenth suspended";

    /**
     * The intervals are in relation to the root. So "1 3 5" returns "4 7" and
     * not "4 3". <blockquote>
     * 
     * <pre>
     * int[] intervals = ChordFactory.degreesToIntervals(&quot;1 3 5&quot;);
     * </pre>
     * 
     * </blockquote>
     * 
     * @param spelling
     *            A chord spelling as a String
     * @return The intervals as an int[]
     * 
     * @see com.rockhoppertech.music.Interval
     */
    public static int[] degreesToIntervals(String spelling) {
        logger.debug("spelling: \'" + spelling + "\'");
        // \s is A whitespace character: [ \t\n\x0B\f\r]
        // see java.util.regex.Pattern
        String[] degrees = spelling.split("\\s");
        for (int i = 0; i < degrees.length; i++) {
            logger.debug("degrees: " + degrees[i]);
        }
        // given "1 3 5" split will give an array
        // with 1 in index 0 3 in index 2 etc

        int[] intervals = new int[degrees.length - 1];
        // all of the degrees start with 1 so we skip that one
        // for 1 3 5 we get 4 7 for the intervals
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = Interval.getSpelling(degrees[i + 1]);
            logger.debug("{}", intervals[i]);
        }
        return intervals;
    }

    /**
     * Register a new Chord with the Factory's registry. Is actually an alias
     * for <code>registerChord</code>
     * 
     * @param aChord
     *            a Chord to be added to the registry
     */
    // public static void add(Chord aChord) {
    // registerChord(aChord);
    // }

    // public static void addChord(String aSymbol, String aSpelling,
    // String aDescription) {
    // registerChord(new Chord(aSymbol, aSpelling, aDescription));
    // }

    /**
     * Create a new Chord with the specified registered symbol.
     * 
     * @param aSymbol
     *            the symbol to use.
     * @return a Chord based on the registered symbol.
     */
    public static Chord createFromSymbol(String aSymbol) {
        return ChordFactory.midiChordMap.get(aSymbol);

        // for (Chord chord : ChordFactory.midiChords) {
        // if (chord.getSymbol().equals(aSymbol))
        // return chord;
        // }
        // return null;
    }

    /**
     * Loop through registered chords to find a spelling match.
     * 
     * @param spelling
     *            the speling
     * @return a Chord or null
     */
    public static Chord createFromSpelling(String spelling) {
        spelling = spelling.trim();

        for (String key : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(key);
            // System.err.printf("Comparing '%s' with '%s'\n", spelling,
            // chord.getSpelling());
            if (chord.getSpelling().trim().equals(spelling)) {
                return chord;
            }
        }
        return null;
    }

    /**
     * Loop through registered chords to find a description match.
     * 
     * <p>
     * <blockquote>
     * 
     * <pre>
     * Chord chord = ChordFactory.createFromDescription(ChordFactory.MAJOR);
     * </pre>
     * 
     * </blockquote> returns a chord with its root set to Pitch.C5 (middle c),
     * the symbol to "maj" inversion = 0, intervals = 4,7
     * 
     * 
     * @param description
     *            A registered chord description
     * @return a Chord
     * @throws IllegalArgumentException
     *             if the description is not in the Factory's registry
     * 
     * @see ChordFactory#createFromSymbol(String)
     */
    public static Chord createFromDescription(String description) {
        description = description.trim();
        for (String key : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(key);
            // String s = String.format("testing '%s' against '%s'",
            // description,
            // chord.getDescription());
            // System.out.println(s);
            if (chord.getDescription().equalsIgnoreCase(description)) {
                return chord;
            }
        }
        throw new IllegalArgumentException("Unknown chord: " + description);
    }

    /**
     * Loop through registered chords to find a match on intervals.
     * 
     * 
     * <blockquote>
     * 
     * <pre>
     * Chord chord = ChordFactory.createFromIntervals(new int[] { 4, 7 });
     * </pre>
     * 
     * </blockquote>
     * 
     * @param someIntervals
     *            the intervals
     * @return a Chord
     * @throws UnknownChordException
     *             if the chord is unknown
     * @throws IllegalArgumentException
     *             if the intervals are not in the Factory's registry
     */

    public static Chord createFromIntervals(int[] someIntervals)
            throws UnknownChordException {
        for (String key : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(key);
            if (chord.isDiatonic(someIntervals))
                return (Chord) chord.clone();
        }
        throw new UnknownChordException("Unknown chord : "
                + ArrayUtils.toString(someIntervals));
    }

    public static Chord createFromIntervals(int[] someIntervals, int root) {

        for (String key : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(key);
            if (chord.isDiatonic(someIntervals)) {
                Chord c = (Chord) chord.clone();
                c.setRoot(root);
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown chord : "
                + ArrayUtils.toString(someIntervals));
    }

    public static void displayAll() {
        for (String key : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(key);
            logger.debug("{}", chord);
        }
        // for (Chord chord : ChordFactory.midiChords) {
        // logger.debug(chord);
        // }
    }

    public static void displayAllSymbols() {
        for (String key : midiChordMap.keySet()) {
            String s = String.format("'%s'", key);
            logger.debug(s);
        }
    }

    /**
     * Create a MIDITrack based on the parameters.
     * 
     * @param rootMidiNum
     *            the MIDI number of the root
     * @param symbol
     *            the chord symbol
     * @param startBeat
     *            the start beat &gt;= 1.0
     * @param duration
     *            the duration
     * @return a {@code MIDITrack}
     * 
     * @see ChordFactory#createFromSymbol(String)
     */
    public static MIDITrack createMIDITrack(int rootMidiNum,
            String symbol, double startBeat, double duration) {
        MIDITrack notelist = new MIDITrack();
        Chord chord = ChordFactory.createFromSymbol(symbol);
        MIDINote note = new MIDINote(rootMidiNum, startBeat, duration);
        notelist.add(note);
        int[] intervals = chord.getIntervals();
        for (int i = 0; i < intervals.length; i++) {
            note = new MIDINote(rootMidiNum + intervals[i], startBeat, duration);
            notelist.add(note);
        }
        return notelist;
    }

    public static MIDITrack createMIDITrack(int rootMidiNum,
            String symbol, double startBeat, double duration, int inversion,
            int drop) {
        Chord chord = ChordFactory.createFromSymbol(symbol);
        return createMIDITrack(rootMidiNum, chord, startBeat, duration,
                inversion, drop);
    }

    /**
     * this is called from Chord when you change its instance values. So
     * chord.getNotelist() returns the result.
     * 
     * @param chord
     *            the chord instance to use
     * @return a {@code MIDITrack} instance
     * 
     * @see Chord#createMIDITrack()
     */
    public static MIDITrack createMIDITrack(Chord chord) {
        MIDITrack notelist = new MIDITrack();
        int[] intervals = chord.getIntervals();
        int drop = chord.getDrop();
        if (chord.getInversion() == 0) {
            MIDINote note = new MIDINote(chord.getRoot(), chord.getStartBeat(),
                    chord.getDuration());
            notelist.add(note);

            for (int i = 0; i < intervals.length; i++) {
                note = new MIDINote(chord.getRoot() + intervals[i],
                        chord.getStartBeat(), chord.getDuration());
                notelist.add(note);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            int index = chord.getInversion() - 1;
            int leap = 12;
            int max = chord.getMaxInterval();
            if (logger.isDebugEnabled()) {
                logger.debug("max interval " + max);
            }
            while (max > leap)
                leap += 12; // 9th, 11th, 13th chords

            // sb.append(Chord.intervalToChordDegree(leap)).append(" ");
            MIDINote note = new MIDINote(chord.getRoot() + leap,
                    chord.getStartBeat(), chord.getDuration());
            notelist.add(note);

            for (int i = 0; i < index && i < intervals.length; i++) {
                // sb.append(Chord.intervalToChordDegree(intervals[i] +
                // leap)).append(" ");
                note = new MIDINote(chord.getRoot() + intervals[i] + leap,
                        chord.getStartBeat(), chord.getDuration());
                notelist.add(note);
            }

            for (int i = index; i < intervals.length; i++) {
                // sb.append(Chord.intervalToChordDegree(intervals[i] + +
                // intervals[i])).append(" ");
                note = new MIDINote(chord.getRoot() + intervals[i],
                        chord.getStartBeat(), chord.getDuration());
                notelist.add(note);
            }

            notelist.sortByAscendingPitches();
            logger.debug("{}", notelist);
            int p = chord.getRoot() % 12;
            int o = chord.getRoot() / 12;
            for (MIDINote n : notelist) {
                int oo = n.getMidiNumber() / 12;
                int nPluses = oo - o;
                int in = n.getMidiNumber() % 12;
                in = Math.abs(in - p);
                logger.debug(String.format("in %d ", in));
                for (int i = 0; i < nPluses; i++)
                    sb.append('+');
                sb.append(Chord.intervalToChordDegree(in)).append(' ');
            }
            // ChordVoicing chordVoicing = new ChordVoicing(chord.getRoot() /
            // 12,
            // sb.toString());
            chord.setChordVoicing(sb.toString());
            if (logger.isDebugEnabled()) {
                String s = String.format("inversion %d is this voicing %s %s",
                        chord.getInversion(), sb.toString(), sb.toString());
                logger.debug(s);
            }
        }

        if (drop > 0) {
            notelist.sortByAscendingPitches();
            int size = notelist.size();
            MIDINote n = notelist.get(size - drop);
            logger.debug("Dropping " + n);
            if (n.getMidiNumber() >= 12) {
                n.setPitch(n.getPitch().transpose(-12));
                logger.debug("Dropped " + n);
            } else {
                logger.error("Too low to drop {}", n);
            }
        }
        return notelist;
    }

    public static MIDITrack createMIDITrack(int rootMidiNum, Chord chord,
            double startBeat, double duration, int inversion, int drop) {

        MIDITrack notelist = new MIDITrack();
        int[] intervals = chord.getIntervals();
        if (inversion > intervals.length) {
            throw new IllegalArgumentException("Inversion too large "
                    + inversion);
        }

        if (inversion == 0) {
            MIDINote note = new MIDINote(rootMidiNum, startBeat, duration);
            notelist.add(note);

            for (int i = 0; i < intervals.length; i++) {
                note = new MIDINote(rootMidiNum + intervals[i], startBeat,
                        duration);
                notelist.add(note);
            }
        } else {
            int index = inversion - 1;
            MIDINote note = new MIDINote(rootMidiNum + 12, startBeat, duration);
            notelist.add(note);

            for (int i = 0; i < index && i < intervals.length; i++) {
                note = new MIDINote(rootMidiNum + intervals[i] + 12, startBeat,
                        duration);
                notelist.add(note);
            }

            for (int i = index; i < intervals.length; i++) {
                note = new MIDINote(rootMidiNum + intervals[i], startBeat,
                        duration);
                notelist.add(note);
            }
        }

        if (drop > 0) {
            // sort by pitch
            Comparator<MIDINote> comp = new Comparator<MIDINote>() {
                public int compare(MIDINote o1, MIDINote o2) {
                    if (o1.getMidiNumber() > o2.getMidiNumber()) {
                        return 1;
                    } else if (o1.getMidiNumber() < o2.getMidiNumber()) {
                        return -1;
                    }
                    return 0;
                }
            };
            notelist.sort(comp);
            int size = notelist.size();
            MIDINote n = notelist.get(size - drop);
            logger.debug("Dropping " + n);
            n.setPitch(n.getPitch().transpose(-12));
            logger.debug("Dropped " + n);
            // MIDINote h = notelist.getHighestPitchedNote();
        }
        return notelist;
    }

    public static int getRootFromName(String name) {
        logger.debug(name);
        int pc = name.toLowerCase(Locale.ENGLISH).charAt(0);
        switch (pc) {
        case 'c':
            pc = 0;
            break;
        case 'd':
            pc = 2;
            break;
        case 'e':
            pc = 4;
            break;
        case 'f':
            pc = 5;
            break;
        case 'g':
            pc = 7;
            break;
        case 'a':
            pc = 9;
            break;
        case 'b':
            pc = 11;
            break;
        default:
            String msg = String.format("What the hell is that? '%s' = %d",
                    name, pc);
            logger.debug(msg);
            throw new IllegalArgumentException(msg);
        }
        if (name.length() > 1) {
            char accidental = name.charAt(1);
            // System.err.println("Accidental " + accidental);
            switch (accidental) {
            case 'b':
                pc -= 1;
                break;
            case 'f':
                pc -= 1;
                break;
            case '-':
                pc -= 1;
                break;
            case '#':
                pc += 1;
                break;
            case 's':
                pc += 1;
                break;
            // case '+': // no. augmented?
            // pc += 1;
            // break;
            case 'x':
                pc += 2;
                break;
            default:
                break;
            }
        }
        String s = String.format("returning %d for %s%n", pc, name);
        logger.debug(s);
        return pc;
    }

    /**
     * None of the chars used as an accidental can be used in a symbol. e.g. s
     * is not used for sharp since sus is a symbol.
     * 
     * @param name
     *            a pitch name
     * @return if the pitch is an accidental
     */
    public static boolean isAccidental(String name) {
        if (name.length() > 1) {
            char accidental = name.charAt(1);
            switch (accidental) {
            case 'b':
            case 'f':
            case '-':
            case '#':
                // case '+':
            case 'x':
                return true;
            }
        }
        return false;
    }

    public static void loadDefinitions() {
        ChordFactoryXMLHelper.readChords(definitionFileName);
    }

    public static void saveDefinitions() {
        try {
            ChordFactoryXMLHelper.writeChordXML(new FileOutputStream(
                    definitionFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Just the symol; no root. e.g. maj7 GoF prototype design pattern.
     * 
     * @param symbol
     *            a chord symbol
     * @return a {@code Chord} with the specified symbol
     * @throws UnknownChordException
     *             if the symbol is not registered.
     */
    public static Chord getChordBySymbol(String symbol)
            throws UnknownChordException {
        Chord chord = null;
        if (symbol.equals("")) {
            logger.debug("No symbol. defaulting to a major triad symbol 'maj'");
            symbol = "maj";
        }
        // this means all major chords (for example) share the same object.
        chord = midiChordMap.get(symbol);

        if (chord == null) {
            String s = String.format("Unknown symbol '%s' n=%d", symbol,
                    midiChordMap.size());
            logger.debug(s);
            // displayAllSymbols();
            throw new UnknownChordException(s);
        }
        // turn this into the prototype design pattern
        Chord clone = (Chord) chord.clone();
        clone.setStartBeat(1d);
        clone.setDuration(4d);
        return clone;
    }

    /**
     * Return a List of all registered chord symbol names along with aliases.
     * 
     * @return a {@code Set} of symbol names.
     */
    public static Set<String> getSymbolNames() {
        Set<String> list = new TreeSet<String>();
        // Chord chord = null;
        for (String symbol : midiChordMap.keySet()) {
            list.add(symbol);
            // chord = midiChordMap.get(symbol);
            // list.add(chord.getSymbol());
            // for (String a : chord.getAliases()) {
            // list.add(a);
            // }
        }
        /*
         * for (Entry<String, Chord> e : midiChordMap.entrySet()) {
         * list.add(e.getKey()); }
         */
        return list;
    }

    /**
     * This is different from getChordBySymbol by containing a root. Uses the
     * GoF prototype design pattern.
     * 
     * <blockquote>
     * 
     * <pre>
     * Chord test = ChordFactory.getChordByFullSymbol(&quot;Cmaj7+11&quot;);
     * </pre>
     * 
     * </blockquote>
     * 
     * @param name
     *            the chord symbol
     * @return a {@code Chord}
     * @throws UnknownChordException
     *             if the symbol is not registered.
     * @see ChordFactory#getChordBySymbol(String)
     */
    public static Chord getChordByFullSymbol(String name)
            throws UnknownChordException {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("parsing '%s'", name));
        }
        int root = getRootFromName(name);
        String symbol = name.substring(1);
        if (isAccidental(name)) {
            symbol = symbol.substring(1);
        }
        logger.debug("root is " + root);
        String s = String.format("symbol '%s'", symbol);
        logger.debug(s);
        Chord chord = null;
        if (symbol.equals("")) {
            logger.debug("No symbol. defaulting to a major triad symbol 'maj'");
            symbol = "maj";
        }

        // does it specify a root?
        int idx = symbol.indexOf('/');
        logger.debug(String.format("index of / = %d", idx));
        String bass = null;
        if (idx != -1) {
            bass = symbol.substring(idx + 1);
            symbol = symbol.substring(0, idx);
            // TODO do something with it
            logger.debug(String.format("removed / from symbol '%s' bass='%s'",
                    symbol, bass));
            if (symbol.equals("")) {
                symbol = "maj";
            }
        }

        // this means all major chords (for example) share the same object.
        chord = midiChordMap.get(symbol);

        if (chord == null) {
            logger.debug(String.format("Unknown symbol '%s' n=%d", symbol,
                    midiChordMap.size()));
            // displayAllSymbols();
            throw new UnknownChordException(s);
        }
        // turn this into the prototype design pattern
        Chord clone = (Chord) chord.clone();
        clone.setRoot(root);
        clone.setStartBeat(1d);
        clone.setDuration(4d);
        if (bass != null)
            clone.setBassPitch(bass);
        return clone;
    }

    /**
     * @param chord
     */
    // public static void registerChord(Chord chord) {
    // if (logger.isDebugEnabled()) {
    // String s = String.format("Registering '%s'", chord);
    // logger.debug(s);
    // }
    // midiChords.add(chord);
    // midiChordMap.put(chord.getSymbol(), chord);
    // }
    /**
     * Called from ChordFactoryXMLHelper parse method. The key is stored in the
     * midiChordMap.
     * 
     * @param chord
     *            a chord to register
     * @param key
     *            the key of the chord
     */
    public static void registerChord(Chord chord, String key) {
        key = key.trim();
        if (logger.isDebugEnabled()) {
            String s = String.format("Registering '%s' with key '%s'", chord,
                    key);
            logger.debug(s);
            s = String.format("N chords %d", midiChordMap.size());
            logger.debug(s);
        }
        midiChords.add(chord);
        midiChordMap.put(key, chord);
    }

    public static List<Chord> getAll() {
        if (midiChordMap == null) {
            init();
        }
        return new ArrayList<Chord>(midiChordMap.values());
    }

    public static Set<Chord> getAllSet() {
        if (midiChordMap == null) {
            init();
        }
        return new TreeSet<Chord>(midiChordMap.values());
    }

    public static Set<Chord> getDominants() {
        if (midiChordMap == null) {
            init();
        }
        Set<Chord> s = new TreeSet<Chord>();
        for (String name : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(name);
            if (chord.isDominant())
                s.add(chord);
        }
        return s;
    }

    public static Set<Chord> getSevenths() {
        if (midiChordMap == null) {
            init();
        }
        Set<Chord> s = new TreeSet<Chord>();
        for (String name : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(name);
            if (chord.hasSeventh())
                s.add(chord);
        }
        return s;
    }

    public static Set<Chord> getNinths() {
        if (midiChordMap == null) {
            init();
        }
        Set<Chord> s = new TreeSet<Chord>();
        for (String name : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(name);
            if (chord.hasNinth())
                s.add(chord);
        }
        return s;
    }

    public static Set<Chord> getElevenths() {
        if (midiChordMap == null) {
            init();
        }
        Set<Chord> s = new TreeSet<Chord>();
        for (String name : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(name);
            if (chord.hasEleventh())
                s.add(chord);
        }
        return s;
    }

    public static Set<Chord> getThirteenths() {
        if (midiChordMap == null) {
            init();
        }
        Set<Chord> s = new TreeSet<Chord>();
        for (String name : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(name);
            if (chord.hasThirteenth())
                s.add(chord);
        }
        return s;
    }

    /**
     * Return a new List of registered chords by the number of their intervals.
     * 
     * @param number
     *            number of intervals.
     * @return a {@code List} of {@code Chord}s
     */
    public static List<Chord> findByNumberOfIntervals(int number) {
        if (midiChordMap == null) {
            init();
        }
        List<Chord> s = new ArrayList<Chord>();
        for (String name : midiChordMap.keySet()) {
            Chord chord = midiChordMap.get(name);
            if (chord.getIntervals().length == number)
                s.add(chord);
        }
        return s;
    }

    /**
     * 
     * @return s a {@code String} that is the file name of the definitions.
     */
    public static String getDefinitionFileName() {
        return definitionFileName;
    }

    // static String[] majorChords = { "maj", "m", "m", "maj", "maj", "m", "dim"
    // };
    public static String[] getChordSymbols(Scale scale) {

        // int[] idx = { 0, 2, 4 };
        int[] intervals = scale.getIntervals();
        // System.err.println(ArrayUtils.toString(intervals));
        String[] symbols = new String[intervals.length];
        // e.g. if you are on the fifth degree and you
        // want a triad you have already gone past the end of the intervals.
        // so extend them.
        int[] intervalsX2 = ArrayFunctions.appendCopy(intervals);
        // System.err.println(ArrayUtils.toString(intervalsX2));

        for (int root = 0; root < 7; root++) {
            int third = ArrayFunctions.sum(intervalsX2, root, root + 1);
            int fifth = ArrayFunctions.sum(intervalsX2, root, root + 3);
            int[] someIntervals = { third, fifth };
            // System.err.printf("rtf %d %d %d\n", root, third, fifth);
            // System.err.println("for someIntervals "
            // + ArrayUtils.toString(someIntervals));
            try {
                Chord c = createFromIntervals(someIntervals);
                c.setRoot(root);
                symbols[root] = c.getSymbol();
            } catch (UnknownChordException e) {
               // wasn't found, create and register a chord.
                
                logger.error(String.format("exception: %s", e.getMessage()), e);
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("root %d intervals %s", root,
                            ArrayUtils.toString(someIntervals)));
                }
                String newsymbol = "wtf-"
                        + ArrayUtils.toString(someIntervals).replaceAll("\\s",
                                "-");
                Chord c = new Chord(root, newsymbol, someIntervals,
                        "just invented");
                ChordFactory.registerChord(c, c.getSymbol());
                symbols[root] = c.getSymbol();
            } catch (Exception e) {
              
                logger.error(String.format("exception: %s", e.getMessage()), e);
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("root %d intervals %s", root,
                            ArrayUtils.toString(someIntervals)));
                }
            }
        }
        return symbols;
    }

    /**
     * Returns the triads for each degree of the scale.
     * 
     * @param scale
     *            the Scale
     * @return an array of {@code Chord}s
     */
    public static Chord[] getChords(Scale scale) {
        int[] intervals = scale.getIntervals();
        Chord[] chords = new Chord[intervals.length];
        // e.g. if you are on the fifth degree and you
        // want a triad you have already gone past the end of the intervals.
        // so extend them.
        int[] intervalsX2 = ArrayFunctions.appendCopy(intervals);
        for (int root = 0; root < 7; root++) {
            int third = ArrayFunctions.sum(intervalsX2, root, root + 1);
            int fifth = ArrayFunctions.sum(intervalsX2, root, root + 3);
            int[] someIntervals = { third, fifth };
            try {
                Chord c = createFromIntervals(someIntervals);
                c.setRoot(scale.getDegree(root));
                chords[root] = c;
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(),e);
            }
        }
        return chords;
    }

    // or public static Chord[] getChords(Scale, scale, boolean... ext)
    public static Chord[] getChords(Scale scale, boolean seven, boolean nine,
            boolean eleven, boolean thirteen) {
        int[] intervals = scale.getIntervals();
        Chord[] chords = new Chord[intervals.length + 1];
        // e.g. if you are on the fifth degree and you
        // want a triad you have already gone past the end of the intervals.
        // so extend them.
        int[] intervalsX2 = ArrayFunctions.appendCopy(intervals);
        intervalsX2 = ArrayFunctions.appendCopy(intervalsX2);
        List<Integer> intervalList = new LinkedList<Integer>();
        for (int degree = 0; degree < scale.getLength(); degree++) {
            int chordroot = scale.getDegree(degree);
            int third = ArrayFunctions.sum(intervalsX2, degree, degree + 1);
            int fifth = ArrayFunctions.sum(intervalsX2, degree, degree + 3);
            intervalList.add(third);
            intervalList.add(fifth);
            logger.debug(String.format(
                    "chordroot:%d root:%d third:%d fifth:%d", chordroot,
                    degree, third, fifth));

            logger.debug("degree:" + degree);
            logger.debug("chordroot:" + PitchFactory.getPitch(chordroot));

            logger.debug("third:"
                    + PitchFactory.getPitch(chordroot + third));
            logger.debug("fifth:"
                    + PitchFactory.getPitch(chordroot + fifth));

            if (seven) {
                int seventh = ArrayFunctions.sum(
                        intervalsX2,
                        degree,
                        degree + 5);
                intervalList.add(seventh);
                logger.debug("seventh:"
                        + PitchFactory.getPitch(chordroot + seventh));
            }
            if (nine) {
                int ninth = ArrayFunctions.sum(intervalsX2, degree, degree + 7);
                intervalList.add(ninth);
                logger.debug("ninth:"
                        + PitchFactory.getPitch(chordroot + ninth));
            }
            if (eleven) {
                int eleventh = ArrayFunctions.sum(
                        intervalsX2,
                        degree,
                        degree + 9);
                intervalList.add(eleventh);
                logger.debug("eleventh:"
                        + PitchFactory.getPitch(chordroot + eleventh));
            }
            if (thirteen) {
                int thirteenth = ArrayFunctions.sum(intervalsX2, degree,
                        degree + 11);
                intervalList.add(thirteenth);
                logger.debug("thirteenth:"
                        + PitchFactory.getPitch(chordroot + thirteenth));
            }

            Integer[] ints = new Integer[intervalList.size()];
            ints = intervalList.toArray(ints);
            int[] someIntervals = ArrayUtils.toPrimitive(ints);
            logger.debug(ArrayUtils.toString(someIntervals));
            intervalList.clear();
            try {
                Chord c = createFromIntervals(someIntervals);
                c.setRoot(chordroot);
                chords[degree] = c;
            } catch (UnknownChordException e) {
               
                logger.debug(String.format("%s-%d", scale.getName(),
                        chordroot));

                for (int i : someIntervals) {
                    logger.debug("interval {}",PitchFactory.getPitch(chordroot + i));
                }

                Chord mc = new Chord(
                        String.format("wtf-%s-d%d-%b-%b-%b-%b",
                                scale.getName(), degree, seven, nine, eleven,
                                thirteen), intervals, "unregistered chord");
                ChordFactory.registerChord(mc, mc.getSymbol());
                mc.setRoot(chordroot);
                chords[degree] = mc;

            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(),e);
            }
        }
        return chords;
    }

    /**
     * Returns the seventh chords for each degree of the scale. The root of the
     * scale is C.
     * 
     * @param scale
     *            the Scale.
     * @return an array of {@code Chord}s
     */
    public static Chord[] getSeventhChords(Scale scale) {
        int[] intervals = scale.getIntervals();
        Chord[] chords = new Chord[intervals.length+1];
        int[] intervalsX2 = ArrayFunctions.appendCopy(intervals);
        logger.debug("chords:length {}", chords.length);
        logger.debug("scale:length {}", scale.getLength());        
        for (int root = 0; root < scale.getLength(); root++) {
            int third = ArrayFunctions.sum(intervalsX2, root, root + 1);
            int fifth = ArrayFunctions.sum(intervalsX2, root, root + 3);
            int seventh = ArrayFunctions.sum(intervalsX2, root, root + 5);
            int[] someIntervals = { third, fifth, seventh };
           // try {
                Chord c = createFromIntervals(someIntervals);
                logger.debug("created chord {} at root {}", c, root);
                c.setRoot(scale.getDegree(root));
                chords[root] = c;
//            } catch (Exception e) {
//                logger.error(e.getLocalizedMessage(),e);
//            }
        }
        return chords;
    }

    // cruft from here to the end
    public static void main(String[] args) {
        Chord test = null;
        try {
            test = ChordFactory.getChordBySymbol("C#maj7+11");
        } catch (UnknownChordException e) {
            e.printStackTrace();
            return;
        }
        logger.debug("test chord {}", test);
        test.setRoot(Pitch.C5); // now it's Cmaj7+11
        test.setDuration(2d);
        logger.debug("test chord {}", test);
        logger.debug("track {}",test.createMIDITrack());
        Chord chord = ChordFactory
                .createFromDescription(ChordFactory.MAJOR_SEVENTH);
        logger.debug("chord {}", chord);
        MIDITrack cmaj = ChordFactory.createMIDITrack(Pitch.C5,
                ChordFactory.MAJOR_SEVENTH, 1d, .5, 0, 0);
        logger.debug("cmaj chord {}", cmaj);

        // ChordFactory.displayAll();
    }

    // public static void oldmain(String[] args) {
    // Chord chord = ChordFactory.createFromSpelling("1 3 5");
    //
    // //
    // int drop = 0;
    // chord = ChordFactory.createFromDescription(ChordFactory.MAJOR_SEVENTH);
    // MIDITrack midinotelist = ChordFactory.createMIDITrack(60, chord,
    // 1d, 4d, 0, drop);
    //
    // drop = 1;
    // int nInversions = chord.getNumberOfInversions();
    // for (int inversion = 0; inversion < nInversions; inversion++) {
    // midinotelist.append(ChordFactory.createMIDITrack(60, chord, 1d,
    // 4d, inversion, drop));
    //
    // }
    // }

    /**
     * Not used anymore in favor of the XML definitions.
     * 
     * private static void addChords() { addChord("maj", "1 3 5",
     * ChordFactory.MAJOR); addChord("sus4", "1 4 5",
     * ChordFactory.SUSPENDED_FOURTH); addChord("maj7", "1 3 5 7",
     * ChordFactory.MAJOR_SEVENTH); addChord("maj7-5", "1 3 b5 7",
     * ChordFactory.MAJOR_SEVENTH_FLAT_5); addChord("maj7+5", "1 3 #5 7",
     * ChordFactory.MAJOR_SEVENTH_SHARP_5); addChord("maj7sus4", "1 4 5 7",
     * ChordFactory.MAJOR_SEVENTH_SUSPENDED_FOURTH); addChord("add+11",
     * "1 3 5 #11", ChordFactory.ADDED_AUGMENTED_ELEVENTH); addChord("6",
     * "1 3 5 6", ChordFactory.SIX); addChord("maj9", "1 3 5 7 9",
     * ChordFactory.MAJOR_NINTH); addChord("maj9+5", "1 3 #5 7 9",
     * "major ninth augmented fifth"); addChord("maj9-5", "1 3 b5 7 9",
     * "major ninth diminished fifth"); addChord("maj7+11", "1 3 5 7 #11",
     * "major seventh augmented eleventh"); addChord("6/7", "1 3 5 6 7",
     * "six seven"); addChord("6/9", "1 3 5 6 9", "major sixth added ninth");
     * addChord("6/7sus", "1 4 5 6 7", "six seven suspended"); addChord("maj11",
     * "1 3 5 7 9 11", "major eleventh"); addChord("maj11+5", "1 3 #5 7 9 11",
     * "major eleventh augmented fifth"); addChord("maj11-5", "1 3 b5 7 9 11",
     * "major eleventh augmented fifth"); addChord("maj9+11", "1 3 5 7 9 #11",
     * "major ninth augmented eleventh"); addChord("maj13", "1 3 5 7 9 11 13",
     * "major thirteenth"); addChord("maj13+11", "1 3 5 7 9 #11 13",
     * "major thirteenth augmented eleventh"); addChord("min/maj7", "1 b3 5 7",
     * "minor/major seventh"); addChord("min/maj9", "1 b3 5 7 9",
     * "minor/major ninth"); addChord("min/maj11", "1 b3 5 7 9 11",
     * "minor/major eleventh"); addChord("min/maj13", "1 b3 5 7 9 11 13",
     * "minor/major thirteenth"); addChord("m", "1 b3 5", "minor");
     * addChord("m7", "1 b3 5 b7", "minor seventh"); addChord("m7-5",
     * "1 b3 b5 b7", "half diminished"); addChord("m6", "1 b3 5 6",
     * "minor sixth"); addChord("m9", "1 b3 5 b7 9", "minor ninth");
     * addChord("m9-5", "1 b3 b5 b7 9", "minor ninth diminished fifth");
     * addChord("m7-9", "1 b3 5 b7 b9", "minor seventh flat nine");
     * addChord("m7-9-5", "1 b3 b5 b7 b9",
     * "minor seventh flat nine diminished fifth"); addChord("m7/11",
     * "1 b3 5 b7 11", "minor seven eleven"); addChord("m6/7", "1 b3 5 6 b7",
     * "minor six seven"); addChord("m6/9", "1 b3 5 6 9", "minor six nine");
     * addChord("m11", "1 b3 5 b7 9 11", "minor eleventh"); addChord("m11-9",
     * "1 b3 5 b7 b9 11", "minor eleventh flat nine"); addChord("m11-9-5",
     * "1 b3 b5 b7 b9 11", "minor eleventh flat nine diminished fifth");
     * addChord("m11-5", "1 b3 b5 b7 9 11", "minor eleventh diminished fifth");
     * addChord("m6/7/11", "1 b3 5 6 b7 11", "minor six seven eleven");
     * addChord("m13/11", "1 b3 5 9 11 13", "minor thirteen eleven");
     * addChord("m13", "1 b3 5 b7 9 11 13", "minor thirteenth");
     * addChord("m13-9", "1 b3 5 b7 b9 11 13", "minor thirteenth flat nine");
     * addChord("m13-5", "1 b3 b5 b7 9 11 13",
     * "minor thriteenth diminished fifth"); addChord("dim", "1 b3 b5",
     * "diminished"); addChord("dim7", "1 b3 b5 bb7", "diminished seventh");
     * addChord("aug", "1 3 #5", "augmented"); addChord("7", "1 3 5 b7",
     * "seventh"); addChord("7-5", "1 3 b5 b7", "seventh flat 5");
     * addChord("7+5", "1 3 #5 b7", "seventh sharp 5"); addChord("9",
     * "1 3 5 b7 9", "ninth"); addChord("9-5", "1 3 b5 b7 9",
     * "ninth diminished fifth"); addChord("9+5", "1 3 #5 b7 9",
     * "ninth augmented fifth"); addChord("7-9", "1 3 5 b7 b9",
     * "seventh flat 9"); addChord("7-9-5", "1 3 b5 b7 b9",
     * "seventh flat 9 dimished fifth"); addChord("7-9+5", "1 3 #5 b7 b9",
     * "seventh flat 9 augmented fifth"); addChord("7+9", "1 3 5 b7 #9",
     * "seventh augmented ninth"); addChord("7+9-5", "1 3 b5 b7 #9",
     * "seventh augmented ninth diminished fifth"); addChord("7+9+5",
     * "1 3 #5 b7 #9", "seventh augmented ninth augmented fifth");
     * addChord("7/11", "1 3 5 b7 11", "seven eleven"); addChord("11",
     * "1 3 5 b7 9 11", "eleventh"); addChord("11+9", "1 3 5 b7 #9 11",
     * "eleventh augmented ninth"); addChord("11+9+5", "1 3 #5 b7 #9 11",
     * "eleventh augmented ninth augmented fifth"); addChord("11+9-5",
     * "1 3 b5 b7 #9 11", "eleventh augmented ninth diminished fifth");
     * addChord("11-9", "1 3 5 b7 b9 11", "eleventh diminished ninth");
     * addChord("11-9+5", "1 3 #5 b7 b9 11",
     * "eleventh diminished ninth augmented fifth"); addChord("11-9-5",
     * "1 3 b5 b7 b9 11", "eleventh diminished ninth diminished fifth");
     * addChord("7+11", "1 3 5 b7 9 #11", "seventh augmented eleventh");
     * addChord("7+11+9", "1 3 5 b7 #9 #11",
     * "seventh augmented eleventh augmented ninth"); addChord("7+11+9+5",
     * "1 3 #5 b7 #9 #11",
     * "seventh augmented eleventh augmented ninth augmented fifth");
     * addChord("7+11+9-5", "1 3 b5 b7 #9 #11",
     * "seventh augmented eleventh augmented ninth diminished fifth");
     * addChord("7+11-9", "1 3 5 b7 b9 #11",
     * "seventh augmented eleventh diminished ninth"); addChord("7+11-9+5",
     * "1 3 #5 b7 b9 #11",
     * "seventh augmented eleventh diminished ninth augmented fifth");
     * addChord("7+11-9-5", "1 3 b5 b7 b9 #11",
     * "seventh augmented eleventh diminished ninth diminished fifth");
     * addChord("13", "1 3 5 b7 9 11 13", "thirteenth"); addChord("13+9",
     * "1 3 5 b7 #9 11 13", "thirteenth augmented ninth"); addChord("13+9+5",
     * "1 3 #5 b7 #9 11 13", "thirteenth augmented ninth augmented fifth");
     * addChord("13+9-5", "1 3 b5 b7 #9 11 13",
     * "thirteenth augmented ninth diminished fifth"); addChord("13-9",
     * "1 3 5 b7 b9 11 13", "thirteenth diminished ninth"); addChord("13-9+5",
     * "1 3 #5 b7 b9 11 13", "thirteenth diminished ninth"); addChord("13-9-5",
     * "1 3 b5 b7 b9 11 13", "thirteenth diminished ninth"); addChord("13+11",
     * "1 3 5 b7 9 #11 13", "thirteenth augmented eleventh");
     * addChord("13+11+9", "1 3 5 b7 #9 #11 13",
     * "thirteenth augmented eleventh augmented ninth"); addChord("13+11+9+5",
     * "1 3 #5 b7 #9 #11 13",
     * "thirteenth augmented eleventh augmented ninth augmented fifth");
     * addChord("13+11+9-5", "1 3 b5 b7 #9 #11 13",
     * "thirteenth augmented eleventh augmented ninth diminished fifth");
     * addChord("13+11-9", "1 3 5 b7 b9 #11 13",
     * "thirteenth augmented eleventh diminished ninth"); addChord("13+11-9+5",
     * "1 3 #5 b7 b9 #11 13",
     * "thirteenth augmented eleventh diminished ninth augmented fifth");
     * addChord("13+11-9-5", "1 3 b5 b7 b9 #11 13",
     * "thirteenth augmented eleventh diminished ninth diminished fifth");
     * addChord("13sus", "1 4 5 b7 9 13", ChordFactory.THIRTEENTH_SUSPENDED); }
     */
    /**
     * Not used anymore in favor of the XML definitions.
     * 
     * static void register() { registerChord(new Chord("maj", new int[] { 4, 7
     * }, ChordFactory.MAJOR));
     * 
     * registerChord(new Chord("sus4", new int[] { 5, 7 },
     * ChordFactory.SUSPENDED_FOURTH));
     * 
     * registerChord(new Chord("maj7", new int[] { 4, 7, 11 },
     * ChordFactory.MAJOR_SEVENTH));
     * 
     * registerChord(new Chord("maj7-5", new int[] { 4, 6, 11 },
     * ChordFactory.MAJOR_SEVENTH_FLAT_5));
     * 
     * registerChord(new Chord("maj7+5", new int[] { 4, 8, 11 },
     * ChordFactory.MAJOR_SEVENTH_SHARP_5));
     * 
     * registerChord(new Chord("maj7sus4", new int[] { 5, 7, 11 },
     * ChordFactory.MAJOR_SEVENTH_SUSPENDED_FOURTH));
     * 
     * registerChord(new Chord("add+11", new int[] { 4, 7, 18 },
     * ChordFactory.ADDED_AUGMENTED_ELEVENTH));
     * 
     * registerChord(new Chord("6", new int[] { 4, 7, 9 }, ChordFactory.SIX));
     * 
     * registerChord(new Chord("maj9", new int[] { 4, 7, 11, 14 },
     * ChordFactory.MAJOR_NINTH));
     * 
     * registerChord(new Chord("maj9+5", new int[] { 4, 8, 11, 14 },
     * ChordFactory.MAJOR_NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord("maj9-5", new int[] { 4, 6, 11, 14 },
     * ChordFactory.MAJOR_NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("maj7+11", new int[] { 4, 7, 11, 18 },
     * ChordFactory.MAJOR_SEVENTH_AUGMENTED_ELEVENTH));
     * 
     * registerChord(new Chord("6/7", new int[] { 4, 7, 9, 11 },
     * ChordFactory.SIX_SEVEN));
     * 
     * registerChord(new Chord("6/9", new int[] { 4, 7, 9, 14 },
     * ChordFactory.MAJOR_SIXTH_ADDED_NINTH));
     * 
     * registerChord(new Chord("6/7sus", new int[] { 5, 7, 9, 11 },
     * ChordFactory.SIX_SEVEN_SUSPENDED));
     * 
     * registerChord(new Chord("maj11", new int[] { 4, 7, 11, 14, 17 },
     * ChordFactory.MAJOR_ELEVENTH));
     * 
     * registerChord(new Chord("maj11+5", new int[] { 4, 8, 11, 14, 17 },
     * ChordFactory.MAJOR_ELEVENTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord("maj11-5", new int[] { 4, 6, 11, 14, 17 },
     * ChordFactory.MAJOR_ELEVENTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord("maj9+11", new int[] { 4, 7, 11, 14, 18 },
     * ChordFactory.MAJOR_NINTH_AUGMENTED_ELEVENTH));
     * 
     * registerChord(new Chord("maj13", new int[] { 4, 7, 11, 14, 17, 21 },
     * ChordFactory.MAJOR_THIRTEENTH));
     * 
     * registerChord(new Chord("maj13+11", new int[] { 4, 7, 11, 14, 18, 21 },
     * ChordFactory.MAJOR_THIRTEENTH_AUGMENTED_ELEVENTH));
     * 
     * registerChord(new Chord("min/maj7", new int[] { 3, 7, 11 },
     * ChordFactory.MINOR_MAJOR_SEVENTH));
     * 
     * registerChord(new Chord("min/maj9", new int[] { 3, 7, 11, 14 },
     * ChordFactory.MINOR_MAJOR_NINTH));
     * 
     * registerChord(new Chord("min/maj11", new int[] { 3, 7, 11, 14, 17 },
     * ChordFactory.MINOR_MAJOR_ELEVENTH));
     * 
     * registerChord(new Chord("min/maj13", new int[] { 3, 7, 11, 14, 17, 21 },
     * ChordFactory.MINOR_MAJOR_THIRTEENTH));
     * 
     * registerChord(new Chord("m", new int[] { 3, 7 }, ChordFactory.MINOR));
     * 
     * registerChord(new Chord("m7", new int[] { 3, 7, 10 },
     * ChordFactory.MINOR_SEVENTH));
     * 
     * registerChord(new Chord("m7-5", new int[] { 3, 6, 10 },
     * ChordFactory.HALF_DIMINISHED));
     * 
     * registerChord(new Chord("m6", new int[] { 3, 7, 9 },
     * ChordFactory.MINOR_SIXTH));
     * 
     * registerChord(new Chord("m9", new int[] { 3, 7, 10, 14 },
     * ChordFactory.MINOR_NINTH));
     * 
     * registerChord(new Chord("m9-5", new int[] { 3, 6, 10, 14 },
     * ChordFactory.MINOR_NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("m7-9", new int[] { 3, 7, 10, 13 },
     * ChordFactory.MINOR_SEVENTH_FLAT_NINE));
     * 
     * registerChord(new Chord("m7-9-5", new int[] { 3, 6, 10, 13 },
     * ChordFactory.MINOR_SEVENTH_FLAT_NINE_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("m7/11", new int[] { 3, 7, 10, 17 },
     * ChordFactory.MINOR_SEVEN_ELEVEN));
     * 
     * registerChord(new Chord("m6/7", new int[] { 3, 7, 9, 10 },
     * ChordFactory.MINOR_SIX_SEVEN));
     * 
     * registerChord(new Chord("m6/9", new int[] { 3, 7, 9, 14 },
     * ChordFactory.MINOR_SIX_NINE));
     * 
     * registerChord(new Chord("m11", new int[] { 3, 7, 10, 14, 17 },
     * ChordFactory.MINOR_ELEVENTH));
     * 
     * registerChord(new Chord("m11-9", new int[] { 3, 7, 10, 13, 17 },
     * ChordFactory.MINOR_ELEVENTH_FLAT_NINE));
     * 
     * registerChord(new Chord("m11-9-5", new int[] { 3, 6, 10, 13, 17 },
     * ChordFactory.MINOR_ELEVENTH_FLAT_NINE_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("m11-5", new int[] { 3, 6, 10, 14, 17 },
     * ChordFactory.MINOR_ELEVENTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("m6/7/11", new int[] { 3, 7, 9, 10, 17 },
     * ChordFactory.MINOR_SIX_SEVEN_ELEVEN));
     * 
     * registerChord(new Chord("m13/11", new int[] { 3, 7, 14, 17, 21 },
     * ChordFactory.MINOR_THIRTEEN_ELEVEN));
     * 
     * registerChord(new Chord("m13", new int[] { 3, 7, 10, 14, 17, 21 },
     * ChordFactory.MINOR_THIRTEENTH));
     * 
     * registerChord(new Chord("m13-9", new int[] { 3, 7, 10, 13, 17, 21 },
     * ChordFactory.MINOR_THIRTEENTH_FLAT_NINE));
     * 
     * registerChord(new Chord("m13-5", new int[] { 3, 6, 10, 14, 17, 21 },
     * ChordFactory.MINOR_THRITEENTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("dim", new int[] { 3, 6 },
     * ChordFactory.DIMINISHED));
     * 
     * registerChord(new Chord("dim7", new int[] { 3, 6, 9 },
     * ChordFactory.DIMINISHED_SEVENTH));
     * 
     * registerChord(new Chord("aug", new int[] { 4, 8 },
     * ChordFactory.AUGMENTED));
     * 
     * registerChord(new Chord("7", new int[] { 4, 7, 10 },
     * ChordFactory.SEVENTH));
     * 
     * registerChord(new Chord("7-5", new int[] { 4, 6, 10 },
     * ChordFactory.SEVENTH_FLAT_5));
     * 
     * registerChord(new Chord("7b5", new int[] { 4, 6, 10 },
     * ChordFactory.SEVENTH_FLAT_5));
     * 
     * registerChord(new Chord("7+5", new int[] { 4, 8, 10 },
     * ChordFactory.SEVENTH_SHARP_5));
     * 
     * registerChord(new Chord("7#5", new int[] { 4, 8, 10 },
     * ChordFactory.SEVENTH_SHARP_5));
     * 
     * registerChord(new Chord("9", new int[] { 4, 7, 10, 14 },
     * ChordFactory.NINTH));
     * 
     * registerChord(new Chord("9-5", new int[] { 4, 6, 10, 14 },
     * ChordFactory.NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("9b5", new int[] { 4, 6, 10, 14 },
     * ChordFactory.NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("9+5", new int[] { 4, 8, 10, 14 },
     * ChordFactory.NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord("9#5", new int[] { 4, 8, 10, 14 },
     * ChordFactory.NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord("7-9", new int[] { 4, 7, 10, 13 },
     * ChordFactory.SEVENTH_FLAT_9));
     * 
     * registerChord(new Chord("7-9-5", new int[] { 4, 6, 10, 13 },
     * ChordFactory.SEVENTH_FLAT_9_DIMISHED_FIFTH));
     * 
     * registerChord(new Chord("7-9+5", new int[] { 4, 8, 10, 13 },
     * ChordFactory.SEVENTH_FLAT_9_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord("7+9", new int[] { 4, 7, 10, 15 },
     * ChordFactory.SEVENTH_AUGMENTED_NINTH));
     * 
     * registerChord(new Chord("7+9-5", new int[] { 4, 6, 10, 15 },
     * ChordFactory.SEVENTH_AUGMENTED_NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("7+9+5", new int[] { 4, 8, 10, 15 },
     * ChordFactory.SEVENTH_AUGMENTED_NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord("7/11", new int[] { 4, 7, 10, 17 },
     * ChordFactory.SEVEN_ELEVEN));
     * 
     * registerChord(new Chord("11", new int[] { 4, 7, 10, 14, 17 },
     * ChordFactory.ELEVENTH));
     * 
     * registerChord(new Chord("11+9", new int[] { 4, 7, 10, 15, 17 },
     * ChordFactory.ELEVENTH_AUGMENTED_NINTH));
     * 
     * registerChord(new Chord("11+9+5", new int[] { 4, 8, 10, 15, 17 },
     * ChordFactory.ELEVENTH_AUGMENTED_NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord("11+9-5", new int[] { 4, 6, 10, 15, 17 },
     * ChordFactory.ELEVENTH_AUGMENTED_NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("11-9", new int[] { 4, 7, 10, 13, 17 },
     * ChordFactory.ELEVENTH_DIMINISHED_NINTH));
     * 
     * registerChord(new Chord("11-9+5", new int[] { 4, 8, 10, 13, 17 },
     * ChordFactory.ELEVENTH_DIMINISHED_NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord("11-9-5", new int[] { 4, 6, 10, 13, 17 },
     * ChordFactory.ELEVENTH_DIMINISHED_NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("7+11", new int[] { 4, 7, 10, 14, 18 },
     * ChordFactory.SEVENTH_AUGMENTED_ELEVENTH));
     * 
     * registerChord(new Chord("7+11+9", new int[] { 4, 7, 10, 15, 18 },
     * ChordFactory.SEVENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH));
     * 
     * registerChord(new Chord( "7+11+9+5", new int[] { 4, 8, 10, 15, 18 },
     * ChordFactory
     * .SEVENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord( "7+11+9-5", new int[] { 4, 6, 10, 15, 18 },
     * ChordFactory
     * .SEVENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("7+11-9", new int[] { 4, 7, 10, 13, 18 },
     * ChordFactory.SEVENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH));
     * 
     * registerChord(new Chord( "7+11-9+5", new int[] { 4, 8, 10, 13, 18 },
     * ChordFactory
     * .SEVENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord( "7+11-9-5", new int[] { 4, 6, 10, 13, 18 },
     * ChordFactory
     * .SEVENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("13", new int[] { 4, 7, 10, 14, 17, 21 },
     * ChordFactory.THIRTEENTH));
     * 
     * registerChord(new Chord("13+9", new int[] { 4, 7, 10, 15, 17, 21 },
     * ChordFactory.THIRTEENTH_AUGMENTED_NINTH));
     * 
     * registerChord(new Chord("13+9+5", new int[] { 4, 8, 10, 15, 17, 21 },
     * ChordFactory.THIRTEENTH_AUGMENTED_NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord("13+9-5", new int[] { 4, 6, 10, 15, 17, 21 },
     * ChordFactory.THIRTEENTH_AUGMENTED_NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("13-9", new int[] { 4, 7, 10, 13, 17, 21 },
     * ChordFactory.THIRTEENTH_DIMINISHED_NINTH));
     * 
     * registerChord(new Chord("13-9+5", new int[] { 4, 8, 10, 13, 17, 21 },
     * ChordFactory.THIRTEENTH_DIMINISHED_NINTH));
     * 
     * registerChord(new Chord("13-9-5", new int[] { 4, 6, 10, 13, 17, 21 },
     * ChordFactory.THIRTEENTH_DIMINISHED_NINTH));
     * 
     * registerChord(new Chord("13+11", new int[] { 4, 7, 10, 14, 18, 21 },
     * ChordFactory.THIRTEENTH_AUGMENTED_ELEVENTH));
     * 
     * registerChord(new Chord("13+11+9", new int[] { 4, 7, 10, 15, 18, 21 },
     * ChordFactory.THIRTEENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH));
     * 
     * registerChord(new Chord( "13+11+9+5", new int[] { 4, 8, 10, 15, 18, 21 },
     * ChordFactory
     * .THIRTEENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord( "13+11+9-5", new int[] { 4, 6, 10, 15, 18, 21 },
     * ChordFactory
     * .THIRTEENTH_AUGMENTED_ELEVENTH_AUGMENTED_NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("13+11-9", new int[] { 4, 7, 10, 13, 18, 21 },
     * ChordFactory.THIRTEENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH));
     * 
     * registerChord(new Chord( "13+11-9+5", new int[] { 4, 8, 10, 13, 18, 21 },
     * ChordFactory
     * .THIRTEENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH_AUGMENTED_FIFTH));
     * 
     * registerChord(new Chord( "13+11-9-5", new int[] { 4, 6, 10, 13, 18, 21 },
     * ChordFactory
     * .THIRTEENTH_AUGMENTED_ELEVENTH_DIMINISHED_NINTH_DIMINISHED_FIFTH));
     * 
     * registerChord(new Chord("13sus", new int[] { 5, 7, 10, 14, 21 },
     * ChordFactory.THIRTEENTH_SUSPENDED)); }
     */
    // not used. There are the names band in a box uses.
    static final String[] biabTypeNames = { "", /* 1 */
            "Maj", /* 2 */
            "b5", /* 3 */
            "aug", /* 4 */
            "6", /* 5 */
            "Maj7", /* 6 */
            "Maj9", /* 7 */
            "Maj9#11", /* 8 */
            "Maj13#11", /* 9 */
            "Maj13", /* 10 */
            "Maj9(no 3)", /* 11 */
            "+", /* 12 */
            "Maj7#5", /* 13 */
            "69", /* 14 */
            "2", /* 15 */
            "m", /* 16 */
            "maug", /* 17 */
            "mMaj7", /* 18 mM7 */
            "m7", /* 19 */
            "m9", /* 20 */
            "m11", /* 21 */
            "m13", /* 22 */
            "m6", /* 23 */
            "m#5", /* 24 */
            "m7#5", /* 25 */
            "m69", /* 26 */
            "?", /* 27 */
            "?", /* 28 */
            "?", /* 29 */
            "?", /* 30 */
            "?", /* 31 */
            "m7b5", /* 32 */
            "dim", /* 33 */
            "m9b5", /* 34 */
            "?", /* 35 */
            "?", /* 36 */
            "?", /* 37 */
            "?", /* 38 */
            "?", /* 39 */
            "5", /* 40 */
            "?", /* 41 */
            "?", /* 42 */
            "?", /* 43 */
            "?", /* 44 */
            "?", /* 45 */
            "?", /* 46 */
            "?", /* 47 */
            "?", /* 48 */
            "?", /* 49 */
            "?", /* 50 */
            "?", /* 51 */
            "?", /* 52 */
            "?", /* 53 */
            "?", /* 54 */
            "?", /* 55 */
            "7+", /* 56 */
            "9+", /* 57 */
            "13+", /* 58 */
            "?", /* 59 */
            "?", /* 60 */
            "?", /* 61 */
            "?", /* 62 */
            "?", /* 63 */
            "7", /* 64 */
            "13", /* 65 */
            "7b13", /* 66 */
            "7#11", /* 67 */
            "13#11", /* 68 */
            "7#11b13", /* 69 */
            "9", /* 70 */
            "?", /* 71 */
            "9b13", /* 72 */
            "9#11", /* 73 */
            "13#11", /* 74 */
            "9#11b13", /* 75 */
            "7b9", /* 76 */
            "13b9", /* 77 */
            "7b9b13", /* 78 */
            "7b9#11", /* 79 */
            "13b9#11", /* 80 */
            "7b9#11b13", /* 81 */
            "7#9", /* 82 */
            "13#9", /* 83 */
            "7#9b13", /* 84 */
            "9#11", /* 85 */
            "13#9#11", /* 86 */
            "7#9#11b13", /* 87 */
            "7b5", /* 88 */
            "13b5", /* 89 */
            "7b5b13", /* 90 */
            "9b5", /* 91 */
            "9b5b13", /* 92 */
            "7b5b9", /* 93 */
            "13b5b9", /* 94 */
            "7b5b9b13", /* 95 */
            "7b5#9", /* 96 */
            "13b5#9", /* 97 */
            "7b5#9b13", /* 98 */
            "7#5", /* 99 */
            "13#5", /* 100 */
            "7#5#11", /* 101 */
            "13#5#11", /* 102 */
            "9#5", /* 103 */
            "9#5#11", /* 104 */
            "7#5b9", /* 105 */
            "13#5b9", /* 106 */
            "7#5b9#11", /* 107 */
            "13#5b9#11", /* 108 */
            "7#5#9", /* 109 */
            "13#5#9#11", /* 110 */
            "7#5#9#11", /* 111 */
            "13#5#9#11", /* 112 */
            "7alt", /* 113 */
            "?", /* 114 */
            "?", /* 115 */
            "?", /* 116 */
            "?", /* 117 */
            "?", /* 118 */
            "?", /* 119 */
            "?", /* 120 */
            "?", /* 121 */
            "?", /* 122 */
            "?", /* 123 */
            "?", /* 124 */
            "?", /* 125 */
            "?", /* 126 */
            "?", /* 127 */
            "7sus", /* 128 */
            "13sus", /* 129 */
            // "7susb13", /* 130 */
            // "7sus#11", /* 131 */
            // "13sus#11", /* 132 */
            // "7sus#11b13", /* 133 */
            // "9sus", /* 134 */
            "?", /* 135 */
            // "9susb13", /* 136 */
            // "9sus#11", /* 137 */
            // "13sus#11", /* 138 */
            // "9sus#11b13", /* 139 */
            // "7susb9", /* 140 */
            // "13susb9", /* 141 */
            // "7susb913", /* 142 */
            // "7susb9#11", /* 143 */
            // "13susb9#11", /* 144 */
            // "7susb9#11b13", /* 145 */
            // "7sus#9", /* 146 */
            // "13sus#9", /* 147 */
            // "7sus#9b13", /* 148 */
            // "9sus#11", /* 149 */
            // "13sus#9#11", /* 150 */
            // "7sus#9#11b13", /* 151 */
            // "7susb5", /* 152 */
            // "13susb5", /* 153 */
            // "7susb5b13", /* 154 */
            // "9susb5", /* 155 */
            // "9susb5b13", /* 156 */
            // "7susb5b9", /* 157 */
            // "13susb5b9", /* 158 */
            // "7susb5b9b13", /* 159 */
            // "7susb5#9", /* 160 */
            // "13susb5#9", /* 161 */
            // "7susb5#9b13", /* 162 */
            // "7sus#5", /* 163 */
            // "13sus#5", /* 164 */
            // "7sus#5#11", /* 165 */
            // "13sus#5#11", /* 166 */
            // "9sus#5", /* 167 */
            // "9sus#5#11", /* 168 */
            // "7sus#5b9", /* 169 */
            // "13sus#5b9", /* 170 */
            // "7sus#5b9#11", /* 171 */
            // "13sus#5b9#11", /* 172 */
            // "7sus#5#9", /* 173 */
            // "13sus#5#9#11", /* 174 */
            // "7sus#5#9#11", /* 175 */
            // "13sus#5#9#11", /* 176 */
            "4", /* 177 */
            "?", /* 178 */
            "?", /* 179 */
            "?", /* 180 */
            "?", /* 181 */
            "?", /* 182 */
            "?", /* 183 */
            // "sus", /* 184 */
            /* no chord names above 185 */
    };
}
