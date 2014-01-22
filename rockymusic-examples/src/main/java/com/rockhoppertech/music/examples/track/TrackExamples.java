package com.rockhoppertech.music.examples.track;

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

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.Instrument;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

import static com.rockhoppertech.music.Pitch.*;

/**
 * Examples of using MIDITrack.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class TrackExamples {
    private static final Logger logger = LoggerFactory
            .getLogger(TrackExamples.class);

    static void ex01() {
        MIDITrack track = new MIDITrack();
        track.add(Pitch.C5);
        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void adds() {
        MIDITrack track = new MIDITrack();
        track.add(Pitch.C5).add(D5).add(E5).add(F5).add(G5);
        track.sequential();
        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void noteString() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C5 D E F G")
                .sequential()
                .build();
        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void emptyTrack() {
        // just an empty track
        MIDITrack track = MIDITrackBuilder.create()
                .build();
        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void chordProgression() {
        MIDITrack track = new MIDITrack();
        MIDITrack c = MIDITrackBuilder.create()
                .noteString("C4 C5 E G")
                .build();
        MIDITrack f = MIDITrackBuilder.create()
                .noteString("F4 C5 F A")
                .build();
        MIDITrack g7 = MIDITrackBuilder.create()
                .noteString("G4 B D5 F G")
                .build();

        track.append(c).append(f).append(g7).append(c);
        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void chordify() {
        MIDITrack track = new MIDITrack();
        MIDITrack c = MIDITrackBuilder.create()
                .noteString("C4 C5 E G")
                .build();

        MIDITrack g7 = MIDITrackBuilder.create()
                .noteString("G4 B D5 F G")
                .build();

        track.append(c);
        g7.sequential();
        track.append(g7);
        g7.chordify();
        track.append(g7);
        track.append(c);

        g7.chordify(12d);
        // ignores g7's start beat!
        // track.append(g7);
        track.merge(g7);

        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void ex07() {
        MIDITrack track = MIDITrackBuilder.create()
                .name("Violin I")
                .instrument(Instrument.VIOLIN)
                .noteString("C6 E G")
                .startBeat(4.5)
                .durations(Duration.SIXTEENTH_NOTE, Duration.EIGHTH_NOTE)
                .sequential()
                .build();
        logger.debug("here is the track\b{}", track);
    }

    static void durs() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C6 E G")
                .startBeat(4.5)
                .durations(Duration.SIXTEENTH_NOTE, Duration.EIGHTH_NOTE)
                .sequential()
                .build();
        logger.debug("here is the track\b{}", track);
    }

    static void dursAndVelocities() {
        MIDITrack track = MIDITrackBuilder
                .create()
                .noteString("C6 E G")
                .startBeat(4.5)
                .durations(
                        Duration.SIXTEENTH_NOTE,
                        Duration.EIGHTH_NOTE,
                        Duration.Q)
                .velocities(32, 64, 127)
                .sequential()
                .build();
        logger.debug("here is the track\b{}", track);
    }

    static void startBeatsDursAndVelocities() {
        MIDITrack track = MIDITrackBuilder
                .create()
                .name("start beats, durs, and velocities")
                .noteString("C6 E G")
                .startBeats(1, 3.5, 4)
                .durations(
                        Duration.SIXTEENTH_NOTE,
                        Duration.EIGHTH_NOTE,
                        Duration.Q)
                .velocities(32, 64, 127)
                .build();
        logger.debug("here is the track\b{}", track);
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        select();
    }

    static void select() {
        final String[] choices = new String[] { "ex1", "adds", "noteString",
                "emptyTrack",
                "chordProgression", "chordify", "durations",
                "durations and velocities",
                "start beats, durations and velocities"
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
        } else if (choice.equals("adds")) {
            adds();
        } else if (choice.equals("noteString")) {
            noteString();
        } else if (choice.equals("emptyTrack")) {
            emptyTrack();
        } else if (choice.equals("chordProgression")) {
            chordProgression();
        } else if (choice.equals("chordify")) {
            chordify();
        } else if (choice.equals("durations")) {
            durs();
        } else if (choice.equals("durations and velocities")) {
            dursAndVelocities();
        } else if (choice.equals("start beats, durations and velocities")) {
            startBeatsDursAndVelocities();
        } else if (choice.equals("ex2")) {
            adds();
        } else if (choice.equals("ex2")) {
            adds();
        } else if (choice.equals("ex2")) {
            adds();
        } else if (choice.equals("ex2")) {
            adds();
        } else if (choice.equals("ex2")) {
            adds();

        }
    }

}
