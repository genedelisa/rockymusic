package com.rockhoppertech.music.examples.chord;

/*
 * #%L
 * Rocky Music Examples
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pattern;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.chord.Chord;
import com.rockhoppertech.music.chord.ChordFactory;
import com.rockhoppertech.music.chord.ChordProgression;
import com.rockhoppertech.music.midi.js.MIDIPerformer;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.modifiers.ArpeggiateModifier;
import com.rockhoppertech.music.modifiers.Modifier;
import com.rockhoppertech.music.modifiers.NoteModifier;
import com.rockhoppertech.music.modifiers.PitchModifier;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * Examples of using Chords
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ChordExamples {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordExamples.class);

    static void ex01() {
        Chord chord = ChordFactory.getChordByFullSymbol("C");

        logger.debug("here is the chord\n{}", chord);
        MIDITrack track = chord.createMIDITrack();
        // the pitches are in octave 0. transpose it to middle c
        PitchModifier mod = new PitchModifier(Modifier.Operation.ADD, Pitch.C5);
        track.map(mod);
        track.play();
        logger.debug("here is the track\n{}", track);
    }

    static void showAll() {

        for (Chord chord : ChordFactory.getAllSet()) {
            logger.debug("{}", chord);
        }
    }

    static void majorScaleChords() {
        Scale scale = ScaleFactory.createFromName("Major");
        // show ninths, but not 11 and 13
        System.err.println("ninths");
        Chord[] chords = ChordFactory
                .getChords(scale, true, true, false, false);
        for (Chord c : chords) {
            System.err.println(c.getDisplayName());
        }
        chords = ChordFactory.getChords(scale, true, false, false, false);
        System.err.println("sevenths");
        for (Chord c : chords) {
            System.err.println(c.getDisplayName());
        }
    }

    static void chordVoicing() {
        String chordVoicing = "3 5 +1";
        Chord chord = ChordFactory.getChordByFullSymbol("C");
        chord.setChordVoicing(chordVoicing);
        MIDITrack track = chord.createMIDITrack();
        // the pitches are in octave 0. transpose it to middle c
        PitchModifier mod = new PitchModifier(Modifier.Operation.ADD, Pitch.C5);
        track.map(mod);
        track.play();
        logger.debug("here is the track\n{}", track);
    }

    static void inversion() {
        Chord chord = ChordFactory.getChordByFullSymbol("C");
        logger.debug("here is the chord\n{}", chord);
        chord.setInversion(1);
        logger.debug("here is the chord in 1st inversion\n{}", chord);
        logger.debug(
                "here is the chord voicing in 1st inversion\n{}",
                chord.getChordVoicing());
        MIDITrack track = chord.createMIDITrack();
        logger.debug("here is the track\n{}", track);

        chord.setInversion(2);
        logger.debug("here is the chord in 2nd inversion\n{}", chord);
        logger.debug(
                "here is the chord voicing in 2nd inversion\n{}",
                chord.getChordVoicing());
        track = chord.createMIDITrack();
        logger.debug("here is the track\n{}", track);

        // oops, no 3rd inversion. No problem though, You're back in root
        // position.
        chord.setInversion(3);
        logger.debug("here is the chord in 3rd inversion\n{}", chord);
        logger.debug(
                "here is the chord voicing in 3rd inversion\n{}",
                chord.getChordVoicing());
        track = chord.createMIDITrack();
        logger.debug("here is the track\n{}", track);
    }

    static void progression() {
        String input = "C G7 | Dm G7 | C";
        ChordProgression chordProgression = ChordProgression
                .createFromNames(input);
        for (Chord c : chordProgression) {
            logger.debug("chord: {}", c);
        }
        Chord g7 = chordProgression.get(1);
        g7.setInversion(2);
        logger.debug("This should be g7 in 2nd inversion {}", g7);
        MIDITrack track = chordProgression.createMIDITrack();
        track.play();
        logger.debug("track:\n{}", track);
        // track = g7.createMIDITrack();

    }

    static void roman() {
        String input = "I V7 | II V7 | I";
        ChordProgression chordProgression = ChordProgression
                .createFromRoman(input);
        logger.debug(
                "chordprogression in default C major: \n{}",
                chordProgression);
        for (Chord c : chordProgression) {
            logger.debug("chord: {}", c);
        }

        chordProgression = ChordProgression
                .createFromRoman(input, "Major", "D");
        logger.debug("chordprogression in D major:\n{}", chordProgression);

        // MIDITrack track = chordProgression.createMIDITrack();
        // track.play();
        // logger.debug("track:\n{}", track);
    }

    static void fromFile() {
        ChordProgression chordProgression = ChordProgression
                .create(new File("src/test/resources/giantSteps.changes"));
        logger.debug("Giant Steps:\n{}", chordProgression);
        MIDITrack track = chordProgression.createMIDITrack();
        MIDIPerformer mp = new MIDIPerformer();
        mp.track(track).atTempo(240).play();
    }

    static void pattern() {
        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7");

        // indexes into the chord degrees
        // so this just arpeggiates all the pitches
        int[] pattern = { 0, 1, 2, 3 };

        Pattern p = new Pattern(chord, pattern, Pitch.C5);

        MIDITrack track = p.createTrack();
        track.play();
        logger.debug("track:\n{}", track);
    }

    static void scales() {
        Chord chord = ChordFactory.getChordByFullSymbol("C");
        Set<Scale> scales = chord.getScales();
        for (Scale s : scales) {
            logger.debug("scale {}", s.getName());
        }
        Scale scale = ScaleFactory.getScaleByName("Major");
        scale.setKey("C");

    }

    static void arpeggiate() {
        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7");
        MIDITrack track = chord.createMIDITrack();
        logger.debug("track:\n{}", track);

        List<Double> series = new ArrayList<Double>();
        series.add(.25);
        series.add(.5);
        series.add(.75);        
        series.add(1d);
        double startBeat = 1d;
        double duration = 4d;
        track.map(new ArpeggiateModifier(startBeat, duration, series,
                NoteModifier.Operation.ADD));
        logger.debug("arp track:\n{}", track);

    }

    /**
     * Lots of appends
     */
    static void lvb() {
        String input = "I V7";
        ChordProgression chordProgression = ChordProgression
                .createFromRoman(input);
        logger.debug(
                "chordprogression in default C major: \n{}",
                chordProgression);
        for (Chord c : chordProgression) {
            logger.debug("chord: {}", c);
        }

        logger.debug("chord progression: \n{}", chordProgression);
        logger.debug(
                "chord progression end beat: \n{}",
                chordProgression.getEndBeat());
        chordProgression.append(chordProgression);
        logger.debug("chord progression after append: \n{}", chordProgression);

        chordProgression.append(chordProgression).append(chordProgression);

        // append a C major
        chordProgression.add(ChordFactory.getChordByFullSymbol("C"));

        logger.debug(
                "chord progression after more appends: \n{}",
                chordProgression);

        MIDITrack track = chordProgression.createMIDITrack();
        track.play();
        // logger.debug("track:\n{}", track);
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        select();
    }

    static void select() {
        final String[] choices = new String[] { "ex1",
                "chordVoicing",
                "majorScaleChords",
                "Show All",
                "arpeggiate",
                "inversion",
                "progression",
                "Roman",
                "append",
                "From File",
                "Pattern",
                "Scales"
        };
        final String choice = (String) JOptionPane
                .showInputDialog(null,
                        "Which Example?",
                        "Example chooser",
                        JOptionPane.QUESTION_MESSAGE,
                        null, // icon
                        choices,
                        choices[0]);
        if (choice.equals("ex1")) {
            ex01();
        } else if (choice.equals("majorScaleChords")) {
            majorScaleChords();
        } else if (choice.equals("chordVoicing")) {
            chordVoicing();
        } else if (choice.equals("progression")) {
            progression();

        } else if (choice.equals("Roman")) {
            roman();

        } else if (choice.equals("append")) {
            lvb();

        } else if (choice.equals("Scales")) {
            scales();
        } else if (choice.equals("arpeggiate")) {
            arpeggiate();
        } else if (choice.equals("")) {

        } else if (choice.equals("")) {

        } else if (choice.equals("")) {

        } else if (choice.equals("")) {

        } else if (choice.equals("")) {

        } else if (choice.equals("Pattern")) {
            pattern();

        } else if (choice.equals("Show All")) {
            showAll();
        } else if (choice.equals("inversion")) {
            inversion();
        } else if (choice.equals("From File")) {
            fromFile();
        } else if (choice.equals("")) {

        }
    }

}
