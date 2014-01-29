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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Interval;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackFactory;
import com.rockhoppertech.music.midi.js.xml.ScaleFactoryXMLHelper;

/**
 * Reads Scale definitions from an XML file (scaledefs.xml by default) and
 * caches them. You can retrieve a Scale clone or create a MIDITrack from a
 * specified Scale.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ScaleFactory {
    private final static Logger logger = LoggerFactory
            .getLogger(ScaleFactory.class);
    private static Set<Scale> scales = new HashSet<Scale>();
    static Map<String, Scale> scaleMap;
    static String definitionFileName = "scaledefs.xml";

    // TODO finish this
    /**
     * <pre>
     * MIDITrack notelist = new ScaleFactory.Builder().name(&quot;Major&quot;).root(
     *         Pitch.C5).track().sequential();
     * </pre>
     * 
     * @author gene
     * 
     */
    public static class Builder {
        String name;
        int rootMidiNum = -1;

        int[] intervals;
        double startBeat;
        double duration;
        boolean upAndDown;
        int nOct;

        public Scale build() {
            Scale result = null;
            result = createFromName(name);

            reset();
            return result;
        }

        public MIDITrack track() {
            MIDITrack result = null;
            // Scale scale = null;
//            result = createMIDITrack(name);
            // result = createMIDITrack(name, rootMidiNum);
            // result = createMIDITrack(scale);
             //result = createMIDITrack(scale, rootMidiNum, startBeat,
             //duration, upAndDown, nOct);
             result = createMIDITrack(intervals, rootMidiNum, startBeat, duration, nOct, upAndDown);
             result.setName(this.name);
             
            reset();
            return result;
        }

        public Builder name(String s) {
            name = s;
            return this;
        }

        public Builder root(int n) {
            rootMidiNum = n;
            return this;
        }

        public Builder nOct(int n) {
            this.nOct = n;
            return this;
        }

        public Builder intervals(int[] n) {
            this.intervals = Arrays.copyOf(n, n.length);
            return this;
        }

        public Builder startBeat(double n) {
            this.startBeat = n;
            return this;
        }

        public Builder duration(double n) {
            this.duration = n;
            return this;
        }

        public Builder upAndDown(boolean b) {
            this.upAndDown = b;
            return this;
        }

        private void reset() {
            name = null;
        }
    }

    // TODO better way?
    static Map<String, String[]> majorsSpellingMap;
    static {
        majorsSpellingMap = new LinkedHashMap<String, String[]>();
        majorsSpellingMap.put("C", new String[] { "C", "D", "E", "F", "G", "A",
                "B" });
        majorsSpellingMap.put("Db", new String[] { "Db", "Eb", "F", "Gb", "Ab",
                "Bb", "C" });
        majorsSpellingMap.put("C#", new String[] { "C#", "D#", "E#", "F#",
                "G#", "A#", "B#" });
        majorsSpellingMap.put("D", new String[] { "C#", "D", "E", "F#", "G",
                "A", "B" });
        majorsSpellingMap.put("E", new String[] { "C#", "D#", "E", "F#", "G#",
                "A", "B" });
        majorsSpellingMap.put("Eb", new String[] { "C", "D", "Eb", "F", "G",
                "Ab", "Bb" });
        majorsSpellingMap.put("F", new String[] { "C", "D", "E", "F", "G", "A",
                "Bb" });
        majorsSpellingMap.put("F#", new String[] { "C#", "D#", "E#", "F#",
                "G#", "A#", "B" });
        majorsSpellingMap.put("Gb", new String[] { "Cb", "Db", "Eb", "F", "Gb",
                "Ab", "Bb" });
        majorsSpellingMap.put("G", new String[] { "C", "D", "E", "F#", "G",
                "A", "B" });
        majorsSpellingMap.put("G#", new String[] { "C", "D", "E", "F", "G",
                "A", "B" });
        majorsSpellingMap.put("Ab", new String[] { "C", "Db", "Eb", "F", "G",
                "Ab", "Bb" });
        majorsSpellingMap.put("A", new String[] { "C#", "D", "E", "F#", "G#",
                "A", "B" });
        majorsSpellingMap.put("B", new String[] { "C#", "D#", "E", "F#", "G#",
                "A#", "B" });
    }

    // don't know where we're reading from at this point
    // static {
    // scaleMap = new HashMap<String, Scale>();
    //
    // // these two the first time
    // //addScales();
    // //saveDefinitions();
    //
    // try {
    // loadDefinitions();
    // } catch (Exception e) {
    //
    // }
    // // displayAll();
    // }

    public static void add(final Scale scale) {
        scales.add(scale);
    }

    /**
     * Return a clone of a registered Scale that matches the specified
     * intervals.
     * 
     * @param someIntervals
     * @return
     */
    public static Scale createFromIntervals(final int[] someIntervals) {
        if (scaleMap == null) {
            ScaleFactory.init();
        }
        for (final String name : scaleMap.keySet()) {
            final Scale scale = scaleMap.get(name);
            if (Arrays.equals(scale.getIntervals(), someIntervals)) {
                return (Scale) scale.clone();
            }
        }
        throw new UnknownScaleException("Scale with intervals not recognized ");
    }

    /**
     * Retrieve a clone of a predefined Scale.
     * 
     * @param name
     *            the registered scale name
     * @return the scale
     */

    public static Scale createFromName(final String name) {
        if (scaleMap == null) {
            ScaleFactory.init();
        }
        final Scale scale = scaleMap.get(name);
        if (scale.getName().equalsIgnoreCase(name)) {
            return (Scale) scale.clone();
        }

        // for (Scale scale : ScaleFactory.scales) {
        // if (scale.getName().equals(name))
        // return scale;
        // }
        throw new UnknownScaleException("Scale with name not recognized "
                + name);
    }

    // public static MIDITrack getScale(int[] scale, int startPitch,
    // int numOctaves, double duration) {
    // return getNoteListFromScale(scale, startPitch, numOctaves, duration);
    // }
    public static Scale createFromSpelling(final String spelling) {
        for (final String name : scaleMap.keySet()) {
            final Scale scale = scaleMap.get(name);
            if (scale.getSpelling().equals(spelling)) {
                return (Scale) scale.clone();
            }
        }

        // for (Scale scale : ScaleFactory.scales) {
        // if (scale.getSpelling().equals(spelling))
        // return scale;
        // }
        throw new UnknownScaleException("Scale with spelling not recognized "
                + spelling);
    }

    public static MIDITrack createMIDITrack(final int[] intervals,
            int rootMidiNum, double startBeat, final double duration,
            final int nOctaves, final boolean down) {

        logger.debug("root " + rootMidiNum);

        final MIDITrack notelist = new MIDITrack();

        MIDINote note = new MIDINote(rootMidiNum, startBeat, duration);
        notelist.add(note);

        int previous = rootMidiNum;
        for (int oct = 0; oct < nOctaves; oct++) {
            for (final int interval : intervals) {
                // FIXME
                if (down) {
                    note = new MIDINote(previous - interval,
                            startBeat += duration, duration);
                } else {
                    note = new MIDINote(previous + interval,
                            startBeat += duration, duration);
                }
                notelist.add(note);
                previous = note.getMidiNumber();
            }
            if (down) {
                rootMidiNum -= 12;
            } else {
                rootMidiNum += 12;
            }

            previous = rootMidiNum;
        }
        return notelist;
    }

    /**
     * Create a one octave scale based on the scale's key and octave. Startbeat
     * and duration are 1.
     * 
     * @param scale
     * @return
     */
    public static MIDITrack createMIDITrack(final Scale scale) {
        final int rootMidiNum = PitchFactory.getPitch(
                scale.getKey() + scale.getOctave()).getMidiNumber();
        return ScaleFactory.createMIDITrack(scale, rootMidiNum, 1d, 1d, 1);
    }

    /**
     * @param scale
     * @param startBeat
     * @param duration
     * @return
     */
    public static MIDITrack createMIDITrack(final Scale scale,
            final double startBeat, final double duration) {

        final MIDITrack track = new MIDITrack();
        track.setName(scale.getName());
        String s = String.format(
                "created track from scale %s, start beat %f duration %f",
                scale.getName(),
                startBeat,
                duration);
        track.addMetaText(1d, s);

        final int rootMidiNum = PitchFactory.getPitch(
                scale.getKey() + scale.getOctave()).getMidiNumber();
        MIDINote note = new MIDINote(rootMidiNum, startBeat, duration);
        track.add(note);
        final int[] intervals = scale.getIntervals();
        int previous = rootMidiNum;
        for (final int interval : intervals) {
            note = new MIDINote(previous + interval, startBeat, duration);
            track.add(note);
            previous = note.getMidiNumber();
        }
        return track;
    }

    /**
     * Ignore the scale's key and octave.
     * 
     * @param scale
     * @param rootMidiNum
     * @return
     */
    public static MIDITrack createMIDITrack(final Scale scale,
            final int rootMidiNum) {
        return ScaleFactory.createMIDITrack(scale, rootMidiNum, 1d, 1d);
    }

    public static MIDITrack createMIDITrack(final Scale scale,
            final String rootPitchString) {
        return ScaleFactory.createMIDITrack(scale, PitchFactory.getPitch(
                rootPitchString).getMidiNumber(), 1d, 1d);
    }

    /**
     * Create a MIDI notelist from a given scale.
     * 
     * @param scale
     *            an unregistered scale
     * @param rootMidiNum
     *            the starting pitch as a MIDI key number
     * @param startBeat
     * @param duration
     * 
     * @return a MIDITrack
     */
    public static MIDITrack createMIDITrack(final Scale scale,
            final int rootMidiNum, double startBeat, final double duration) {

        logger.debug("root " + rootMidiNum);
        logger.debug("scale " + scale);
        if (scale == null) {
            return null;
            // throw new IllegalArgumentException("Null scale");
        }

        final MIDITrack track = new MIDITrack();
        track.setName(scale.getName());
        String s = String
                .format(
                        "created track from scale %s, root %d start beat %f duration %f",
                        scale.getName(),
                        rootMidiNum,
                        startBeat,
                        duration);
        track.addMetaText(1d, s);

        final int[] intervals = scale.getIntervals();

        MIDINote note = new MIDINote(rootMidiNum, startBeat, duration);
        track.add(note);

        int previous = rootMidiNum;
        for (final int interval : intervals) {
            note = new MIDINote(previous + interval, startBeat += duration,
                    duration);
            track.add(note);
            previous = note.getMidiNumber();
        }

        return track;
    }

    public static MIDITrack createMIDITrack(final Scale scale,
            final int rootMidiNum, final double startBeat,
            final double duration, final boolean upAndDown, final int nOct) {

        final MIDITrack notelist = ScaleFactory.createMIDITrack(scale,
                rootMidiNum, startBeat, duration, nOct);

        if (upAndDown) {
            if (scale.isDescendingDifferent()) {
                final MIDITrack reverse = ScaleFactory.createMIDITrack(
                        scale.getDescendingIntervals(), rootMidiNum + 12, 1d,
                        duration, nOct, true);
                notelist.append(reverse);
                notelist.sequential();
            } else {
                final MIDITrack reverse = notelist.retrograde();
                notelist.append(reverse);
                notelist.sequential();
            }
        }
        return notelist;
    }

    public static MIDITrack createMIDITrack(final Scale scale,
            int rootMidiNum, double startBeat, final double duration,
            final int nOctaves) {

        logger.debug("root " + rootMidiNum);
        logger.debug("scale " + scale);

        final MIDITrack notelist = new MIDITrack();
        final int[] intervals = scale.getIntervals();

        MIDINote note = new MIDINote(rootMidiNum, startBeat, duration);
        notelist.add(note);

        int previous = rootMidiNum;
        for (int oct = 0; oct < nOctaves; oct++) {
            for (final int interval : intervals) {
                note = new MIDINote(previous + interval, startBeat += duration,
                        duration);
                notelist.add(note);
                previous = note.getMidiNumber();
            }
            rootMidiNum += 12;
            previous = rootMidiNum;
        }
        return notelist;
    }

    public static MIDITrack createMIDITrack(final String name) {
        return ScaleFactory.createMIDITrack(name, Pitch.C5, 1d, 1d);
    }

    /**
     * Create a MIDI notelist from a known scale name. The start beat and
     * duration are set to 1.0.
     * 
     * @param name
     *            registered scale name
     * @param rootMidiNum
     *            the starting pitch as a MIDI key number
     * 
     * @return a MIDITrack
     */
    public static MIDITrack createMIDITrack(final String name,
            final int rootMidiNum) {
        return ScaleFactory.createMIDITrack(name, rootMidiNum, 1d, 1d);
    }

    /**
     * @param name
     * @param rootMidiNum
     * @param startBeat
     * @param duration
     * @return
     */
    public static MIDITrack createMIDITrack(final String name,
            final int rootMidiNum, final double startBeat, final double duration) {
        if (scaleMap == null) {
            ScaleFactory.init();
        }
        final MIDITrack notelist = new MIDITrack();
        final Scale scale = ScaleFactory.createFromName(name);
        MIDINote note = new MIDINote(rootMidiNum, startBeat, duration);
        notelist.add(note);
        final int[] intervals = scale.getIntervals();
        int previous = rootMidiNum;
        for (final int interval : intervals) {
            note = new MIDINote(previous + interval, startBeat, duration);
            notelist.add(note);
            previous = note.getMidiNumber();
        }
        return notelist;
    }

    /**
     * @param scale
     * @return
     */
    public static MIDITrack createMIDITrackOverEntireRange(
            final Scale scale) {
        return ScaleFactory.createMIDITrack(scale, Pitch.C0, 1d, 1d, 10);
    }

    public static void displayAll() {
        if (scaleMap == null) {
            ScaleFactory.init();
        }
        for (final String name : scaleMap.keySet()) {
            final Scale scale = scaleMap.get(name);
            System.out.println(scale);
        }

        // for (Scale scale : ScaleFactory.scales) {
        // System.out.println(scale);
        // }
    }

    /**
     * 
     * @param size
     * @return
     */
    public static List<Scale> findBySize(final int size) {
        if (scaleMap == null) {
            ScaleFactory.init();
        }
        final List<Scale> s = new ArrayList<Scale>();
        for (final String name : scaleMap.keySet()) {
            final Scale scale = scaleMap.get(name);
            if (scale.getIntervals().length == size) {
                s.add((Scale) scale.clone());
            }
        }
        return s;
    }

    /**
     * Get all the scales in the map
     * 
     * @return
     */
    public static List<Scale> getAll() {
        if (scaleMap == null) {
            ScaleFactory.init();
        }
        return new ArrayList<Scale>(scaleMap.values());
    }

    /**
     * @return the definitionFileName
     */
    public static String getDefinitionFileName() {
        return definitionFileName;
    }

    // public static MIDITrack getNoteListFromScale(final int[] intervals,
    // final int startPitch, final int numOctaves, final double duration) {
    //
    // final int unit = Pitch.MINOR_SECOND;
    // final boolean absoluteIntervals = false; // all in relation to base
    // final MIDITrack notelist = MIDITrack
    // .createFromIntervals(intervals,
    // startPitch,
    // unit,
    // absoluteIntervals,
    // numOctaves);
    // notelist.map(new DurationModifier(duration));
    // notelist.sequential();
    // return notelist;
    // }

    public static MIDITrack getNoteListPattern(final Scale scale,
            final int[] pattern, final int limit, final int startingMIDINumber,
            final int nOctaves, final double duration, final boolean reverse,
            final double restBetweenPatterns, final boolean upAndDown) {

        final int numOcts = (Pitch.MAX - startingMIDINumber) / 12 + 1;
        final int[] degrees = Interval.intervalsToDegrees(scale.getIntervals(),
                numOcts);
        return MIDITrackFactory.getTrackPattern(degrees, pattern, limit,
                startingMIDINumber, nOctaves, duration, reverse,
                restBetweenPatterns, upAndDown);

        // return getNoteListPattern(scale.getIntervals(), pattern, limit,
        // startingMIDINumber, nOctaves, duration, reverse,
        // restBetweenPatterns, upAndDown);

    }

    public static List<Pitch> getPitches(final String name,
            final int rootMidiNum) {
        if (scaleMap == null) {
            ScaleFactory.init();
        }
        final List<Pitch> pitches = new ArrayList<Pitch>();
        final Scale scale = ScaleFactory.createFromName(name);
        Pitch pitch = PitchFactory.getPitch(rootMidiNum);
        pitches.add(pitch);
        final int[] intervals = scale.getIntervals();
        int previous = rootMidiNum;
        for (final int interval : intervals) {
            pitch = PitchFactory.getPitch(previous + interval);
            pitches.add(pitch);
            previous = pitch.getMidiNumber();
        }
        return pitches;
    }

    public static Scale getScaleByKeyAndName(final String key, final String name) {
        final Scale scale = ScaleFactory.getScaleByName(name);
        scale.setKey(key);
        return scale;
    }

    public static Scale getScaleByName(final String name) {
        if (scaleMap == null) {
            ScaleFactory.init();
        }
        if (scaleMap.containsKey(name) == false) {
            throw new IllegalArgumentException("What is '" + name + "'");
        }
        final Scale scale = scaleMap.get(name);
        final Scale clone = (Scale) scale.clone();
        return clone;
    }

    public static String[] getScaleNameArray() {
        String[] choices = new String[1];
        choices = ScaleFactory.getScaleNames().toArray(choices);
        return choices;
    }

    public static List<String> getScaleNames() {
        if (scaleMap == null) {
            ScaleFactory.init();
        }
        final List<String> list = new ArrayList<String>();
        for (final String name : scaleMap.keySet()) {
            list.add(name);
        }
        return list;
    }

    static void init() {
        scaleMap = new HashMap<String, Scale>();
        try {
            ScaleFactoryXMLHelper
                    .setScaleDefinitionFileName(definitionFileName);
            ScaleFactory.loadDefinitions();
        } catch (final Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("loading defs", e);
            }
        }
    }

    /**
     * Read scale definitions from an XML file. this will call the
     * <code>registerScale</code> method
     */
    public static void loadDefinitions() {
        ScaleFactoryXMLHelper.readScales();
        // ScaleFactoryXMLHelper.readScales(definitionFileName);
    }

    public static void main(final String[] args) {
        ScaleFactory.displayAll();

        // getNoteListPattern()

        /*
         * try { ChordFactoryXMLHelper.writeChordXML(new
         * FileOutputStream("chorddefs.xml")); } catch (FileNotFoundException e)
         * { e.printStackTrace(); }
         * ChordFactoryXMLHelper.writeChordXML(System.out);
         */

    }

    public static void oldmain(final String[] args) {
        final Scale chord = ScaleFactory.createFromSpelling("1 3 5");
        // chord = ScaleFactory.createFromName(ScaleFactory.MAJOR_SEVENTH);
        final MIDITrack MIDITrack = ScaleFactory.createMIDITrack(
                chord, 60, 1d, 4d);
        System.out.println(MIDITrack);
    }

    /**
     * This is called by the XMLHelper.
     * 
     * @param scale
     */
    public static void registerScale(final Scale scale) {
        scales.add(scale);
        if (scaleMap == null) {
            ScaleFactory.init();
        }
        scaleMap.put(scale.getName(), scale);
    }

    public static void saveDefinitions() {
        try {
            ScaleFactoryXMLHelper.writeScaleXML(new FileOutputStream(
                    definitionFileName));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    // static void addScales() {
    // Scale s = new Scale("Hungarian Gypsy", "1, 2, b3, #4, 5, b6, b7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Pelog", Scale.PELOG);
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Algerian", "1, 2, b3, #4, 5, b6, 7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Arabian", "1, 2, 3, 4, b5, b6, b7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Byzantine", "1, b2, 3, 4, 5, b6, 7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Ethiopian", "1, 2, b3, 4, 5, b6, b7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Gypsy", "1, b2, 3, 4, 5, b6, 7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Hawaiian", "1, 2, b3, 4, 5, 6, 7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Persian", "1, b2, 3, 4, b5, b6, 7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Oriental", "1, b2, 3, 4, b5, 6, b7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Mohammedan", "1, 2, b3, 4, 5, b6, 7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Jewish", "1, b2, 3, 4, 5, b6, b7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Javanese", "1, b2, b3, 4, 5, 6, b7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Hungarian minor", "1, 2, b3, #4, 5, b6, 7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Hungarian Major", "1, #2, 3, #4, 5, 6, b7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Hungarian Gypsy", "1, 2, b3, #4, 5, b6, b7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Hindu", "1, 2, 3, 4, 5, b6, b7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("diminished minor", "1, 2, b3, 4, b5, b6, bb7, b8");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Bebop half-diminished", "1, b2, b3, 4, b5, 5, b6, b7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Bebop minor", "1, 2, b3, 3, 4, 5, 6, b7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Bebop Dominant", "1, 2, 3, 4, 5, 6, b7, 7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Eight-tone Spanish", "1, b2, #2, 3, 4, b5, b6, b7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Spanish", "1, b2, 3, 4, 5, b6, b7");
    // scaleMap.put(s.getName(), s);
    //
    // // Symetric scaleMap
    // s = new Scale("Augmented", "1, #2, 3, 5, #5, 7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Whole-Tone b5", "1, 2, 3, b5, b6, b7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Whole-Tone", "1, 2, 3, #4, #5, b7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("diminished dominant", "1, b2, #2, 3, #4, 5, 6, b7");
    // scaleMap.put(s.getName(), s);
    // //
    //
    // // pentatonic
    // s = new Scale("Pelog", "1, b2, b3, 5, b6");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Pelog2", "1, b2, b3, 5, b7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Mongolian", "1, 2, 3, 5, 6");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Kumoi", "1, b2, 4, 5, b6");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Japanese", "1, b2, 4, 5, b6");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Iwato", "1, b2, 4, b5, b7");
    // scaleMap.put(s.getName(), s);
    // s = new Scale("Hirajoshi", "1, 2, b3, 5, b6");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Egyptian", "1, 2, 4, 5, b7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Chinese", "1, 3, #4, 5, 7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Balinese", "1, b2, b3, 5, b6");
    // scaleMap.put(s.getName(), s);
    // //
    //
    // s = new Scale("Major Phrygian", "1, b2, 3, 4, 5, b6, b7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Major Locrian", "1, 2, 3, 4, b5, b6, b7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Lydian minor", "1, 2, 3, #4, 5, b6, b7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Leading Whole Tone", "1, 2, 3, #4, #5, #6, 7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Harmonic Minor", "1, 2, b3, 4, 5, b6, 7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Harmonic Major", "1, 2, 3, 4, 5, b6, 7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Enigmatic", "1, b2, 3, #4, #5, #6, 7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Double Harmonic", "1, b2, 3, 4, 5, b6, 7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Neapolitan minor", "1, b2, b3, 4, 5, b6, 7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Neapolitan Major", "1, b2, b3, 4, 5, 6, 7");
    // scaleMap.put(s.getName(), s);
    //
    // s = new Scale("Overtone", "1, 2, 3, #4, 5, 6, b7");
    // scaleMap.put(s.getName(), s);
    // }

    public static void saveDefinitions(final String filename) {
        try {
            ScaleFactoryXMLHelper.writeScaleXML(new FileOutputStream(filename));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * This will also load the definitions.
     * 
     * @param definitionFileName
     *            the definitionFileName to set
     */
    public static void setDefinitionFileName(final String definitionFileName) {
        ScaleFactory.definitionFileName = definitionFileName;
        ScaleFactoryXMLHelper.setScaleDefinitionFileName(definitionFileName);
        ScaleFactory.loadDefinitions();
    }

    /**
     * This will also load the definitions.
     * 
     * @param is
     */
    public static void setDefinitionInputStream(final InputStream is) {
        ScaleFactoryXMLHelper.setDefinitionInputStream(is);
        ScaleFactory.loadDefinitions();

    }

    /**
     * E.g. for a c major scale, mode 1 will be d dorian. The returned scale
     * will have the same octave as the one passed in. The key will of course be
     * different (e.g. for mode 1 in c major it will be d)
     * 
     * @param scale
     * @param mode
     * @return the mode for the scale
     */
    public static Scale getMode(final Scale scale, int mode) {
        List<Pitch> degrees = scale.getDegreesAsPitches();
        Pitch key = degrees.get(mode);

        // see how rotate works. for mode 1 you'd have to pass in -1 otherwise.
        // not especially intuitive from the caller's pov.
        mode *= -1;

        int[] i = scale.getIntervals();
        List<Integer> intervals = new ArrayList<Integer>();
        for (int element : i) {
            intervals.add(element);
        }
        Collections.rotate(intervals, mode);
        i = new int[intervals.size()];
        int j = 0;
        for (Integer in : intervals) {
            i[j++] = in;
        }

        String name = String.format("%s mode %d", scale.getName(), mode);
        Scale scaleMode = new Scale(name, i);
        scaleMode.setKey(PitchFormat.getPitchString(key));
        scaleMode.setOctave(scale.getOctave());

        logger.debug("scale {}", scale);
        logger.debug("scale mode {}", scaleMode);

        return scaleMode;
    }
}
