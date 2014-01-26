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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ChordProgressionParser {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordFactory.class);

    /**
     * Create a Chord sequence based on fake book style chord names. Measures
     * are delimited by the character |. The meter is 4/4. <code>
     * List<Chord> progression = 
     * ChordProgressionParser.chordsFromStdNames("F7 | Bb7 | F7 | Cm7 F7 | Bb7 | Bm7 E7 | F7 E7 | Eb7 D7 | Gm7 | C7 Bb7 | Am7 D7 | Gm7 C7");
     * </code>
     * 
     * @param inputString
     * @return
     * @throws UnknownChordException
     */
    public static ChordProgression chordsFromStdNames(String inputString)
            throws UnknownChordException {
        return chordsFromStdNames(inputString, 4);
    }

    public static ChordProgression chordsFromStdNames(String inputString,
            int beatsPerMeasure) throws UnknownChordException {
        ChordProgression chordProgression = new ChordProgression();
        inputString = stripCommands(inputString);
        chordProgression.setKey(key);
        chordProgression.setBeatsPerMeasure(beatsPerMeasure);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("stripped '%s'", inputString));
        }
        Scanner scanner = new Scanner(inputString.trim());
        scanner.useDelimiter("\\|");
        int mnum = 1;
        List<Chord> measure = null;
        while (scanner.hasNext()) {
            String measureString = scanner.next().trim();
            boolean hasSlashes = false;
            if (measureString.indexOf('/') != -1) {
                hasSlashes = true;
            }
            Scanner measureScanner = new Scanner(measureString);
            measure = new ArrayList<Chord>(8);
            Chord previousChord = null;
            Chord chord = null;
            while (measureScanner.hasNext()) {
                String symbol = measureScanner.next().trim();

                // e.g. | c / / g |
                if (symbol.equals("/") && previousChord != null) {
                    double pduration = previousChord.getDuration() + 1d;
                    previousChord.setDuration(pduration);
                } else {
                    chord = ChordFactory.getChordByFullSymbol(symbol);
                    chord.setDuration(1d);
                    chord.setOctave(5);
                    measure.add(chord);
                    previousChord = chord;
                }

                String msg = String.format("m%d '%s'\n", mnum, chord
                        .getDisplayName());
                logger.debug(msg);
                msg = String.format("symbol '%s'\n", symbol);
                logger.debug(msg);
            }
            double duration = (double) beatsPerMeasure
                    / (double) measure.size();
            for (Chord ch : measure) {
                if (hasSlashes == false)
                    ch.setDuration(duration);
                chordProgression.add(ch);
                String msg = String.format("add m%d '%s'\n", mnum, ch
                        .getDisplayName());
                logger.debug(msg);
            }
            // progression.addAll(measure);
            mnum++;
            // try {
            // logger.debug("0 " + progression.get(0));
            // logger.debug("1 " + progression.get(1));
            // logger.debug("2 " + progression.get(2));
            // } catch (Exception e) {
            // }

        }
        return chordProgression;

    }

    public static String chordsToRomanString(ChordProgression chords,
            String key, Scale scale, int beatsPerMeasure, boolean useSymbol) {
        StringBuilder sb = new StringBuilder();
        double beat = 1d;
        for (Chord chord : chords) {
            if (beat > beatsPerMeasure) {
                beat = 1d;
                sb.append(" | ");
            }
            String root = RomanChordParser.pitchNameToRoman(scale, PitchFormat
                    .getPitchString(chord.getRoot()), key);
            if (useSymbol) {
                sb.append(root).append(chord.getSymbol()).append(' ');
            } else {
                sb.append(root).append(' ');
            }

            beat += chord.getDuration();
        }
        return sb.toString().trim();
    }

    public static String romanStringToStdString(String roman, String key,
            Scale scale, int beatsPerMeasure) {
        StringBuilder sb = new StringBuilder();
        RomanChordParser.setDefaultScaleAndKey(scale, key);
        ChordProgression chords = chordsFromRoman(roman, beatsPerMeasure);
        double beat = 1d;
        for (Chord chord : chords) {
            if (beat > beatsPerMeasure) {
                beat = 1d;
                sb.append(" | ");
            }
            sb.append(chord.getDisplayName()).append(' ');
            beat += chord.getDuration();
        }
        return sb.toString().trim();
    }

    public static ChordProgression chordsFromRoman(String inputString) {
        return chordsFromRoman(inputString, 4);
    }

    public static ChordProgression chordsFromRoman(String inputString,
            int beatsPerMeasure) {
        ChordProgression chordProgression = new ChordProgression();
        inputString = stripCommands(inputString);
        chordProgression.setKey(key);
        chordProgression.setBeatsPerMeasure(beatsPerMeasure);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("stripped '%s'", inputString));
        }
        Scanner scanner = new Scanner(inputString.trim());
        scanner.useDelimiter("\\|");
        int mnum = 1;
        List<Chord> measure = null;
        while (scanner.hasNext()) {
            String measureString = scanner.next().trim();
            boolean hasSlashes = false;
            if (measureString.indexOf('/') != -1) {
                hasSlashes = true;
            }
            Scanner measureScanner = new Scanner(measureString);
            measure = new ArrayList<Chord>(8);
            Chord previousChord = null;
            Chord chord = null;
            while (measureScanner.hasNext()) {
                String romanString = measureScanner.next().trim();

                if (romanString.equals("/") && previousChord != null) {
                    double pduration = previousChord.getDuration() + 1d;
                    previousChord.setDuration(pduration);
                } else {
                    chord = RomanChordParser.getChord(romanString);
                    chord.setOctave(5);
                    measure.add(chord);
                    chord.setDuration(1d);
                    previousChord = chord;
                }

                String msg = String.format("roman '%s'\n", romanString);
                logger.debug(msg);
                // msg = String.format("chord identity %d\n", System
                // .identityHashCode(chord));
                // logger.debug(msg);
            }
            double duration = (double) beatsPerMeasure
                    / (double) measure.size();
            for (Chord ch : measure) {
                if (hasSlashes == false)
                    ch.setDuration(duration);
                chordProgression.add(ch);
                String msg = String.format("add m%d '%s'\n", mnum, ch
                        .getDisplayName());
                logger.debug(msg);
            }
            mnum++;
        }
        return chordProgression;

    }

    public static MIDITrack createMIDITrack(List<Chord> progression) {
        MIDITrack notelist = new MIDITrack();
        for (Chord chord : progression) {
            String msg = String.format("%s %f", chord.getDisplayName(), chord
                    .getDuration());
            logger.debug(msg);
            notelist.append(chord.createMIDITrack());
        }
        logger.debug("{}", notelist);
        return notelist;
    }

    public static String readFromFile(String filename) {
        // BufferedReader r = null;
        // try {
        // //r = new BufferedReader(new FileReader(filename));
        // r = getReader(filename);
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // throw new IllegalArgumentException(e);
        // }
        BufferedReader r = getReader(filename);
        Scanner scanner = new Scanner(r);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            logger.debug("file line '{}'", line);
            if (line != null && line.equals("") == false) {
                if (line.charAt(0) == '!') {
                    logger.debug("Skipping comment " + line);
                    continue;
                }
                sb.append(line).append('\n');
            }
        }

        try {
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().trim();
    }

    /**
     * File may be in a jar.
     * 
     * @param filename
     * @return
     */
    private static BufferedReader getReader(String filename) {
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            if (logger.isDebugEnabled()) {
                String s = String.format("Bad fileinputstream filename '%s'",
                        filename);
                logger.debug(s);
            }
            is = ChordProgressionParser.class.getResourceAsStream(filename);
            if (is == null) {
                String s = String
                        .format("Bad resource filename '%s'", filename);
                if (logger.isDebugEnabled()) {
                    logger.debug(s);
                }
                throw new IllegalArgumentException(s);
            }
        }

        return new BufferedReader(new InputStreamReader(is));
    }

    public static ChordProgression createFromFile(File file)
            throws UnknownChordException {
        logger.debug("reading from file '{}'", file.getAbsolutePath());

        String input = readFromFile(file.getAbsolutePath());
        logger.debug("creating from file input '{}'", input);
        return create(input);
    }

    public static ChordProgression create(String input)
            throws UnknownChordException {

        input = stripCommands(input);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("stripped '%s'", input));
        }
        ChordProgression progression = null;
        boolean isRoman = ChordProgressionParser.isRoman(input);
        if (isRoman)
            progression = ChordProgressionParser.chordsFromRoman(input,
                    beatsPerMeasure);
        else
            progression = ChordProgressionParser.chordsFromStdNames(input,
                    beatsPerMeasure);
        progression.setKey(key);
        progression.setBeatsPerMeasure(beatsPerMeasure);
        progression.setName(name);
        progression.setScaleName(scaleName);
        return progression;
    }

    static String stripCommands(String input) {
        if (input.indexOf(':') == -1) {
            if (logger.isDebugEnabled()) {
                String s = String.format("no commands");
                logger.debug(s);
            }
            return input;
        }
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.indexOf(':') == -1) {
                sb.append(line);
            } else {
                processCommand(line);
            }
        }
        return sb.toString().trim();
    }

    static String key = "C";
    static int beatsPerMeasure = 4;
    static String name = "untitled";
    static String scaleName = "Major";

    private static void processCommand(String line) {
        int index = line.indexOf(':') + 1;
        String value = line.substring(index);
        if (line.startsWith("key")) {
            key = value;
        } else if (line.startsWith("beatsPerMeasure")) {
            beatsPerMeasure = Integer.parseInt(value);
        } else if (line.startsWith("name")) {
            name = value;
        } else if (line.startsWith("scaleName")) {
            scaleName = value;
        }
    }

    /**
     * Checks first chord to determine if the progression uses roman numerals.
     * That means you cannot mix roman and standard input.
     * 
     * @param input
     * @return
     */
    public static boolean isRoman(String input) {
        Scanner scanner = new Scanner(input);
        String first = scanner.next();
        boolean isRoman = RomanChordParser.isRoman(first);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("first '%s' from '%s' roman? %b", first,
                    input, isRoman));
        }
        return isRoman;
    }
}
