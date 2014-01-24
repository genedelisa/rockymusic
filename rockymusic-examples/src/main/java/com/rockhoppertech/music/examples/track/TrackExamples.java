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
import com.rockhoppertech.music.midi.js.ConsoleReceiver;
import com.rockhoppertech.music.midi.js.Instrument;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDIPerformer;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.Score;
import com.rockhoppertech.music.midi.js.ScoreFactory;
import com.rockhoppertech.music.modifiers.AbstractMIDINoteModifier;
import com.rockhoppertech.music.modifiers.MIDINoteModifier;
import com.rockhoppertech.music.modifiers.Modifier;
import com.rockhoppertech.music.modifiers.PitchModifier;

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

    static void emptyTrack() {
        // just an empty track
        MIDITrack track = MIDITrackBuilder.create()
                .build();
        track.play();
        logger.debug("here is the track\n{}", track);
    }

    static void ex01() {
        MIDITrack track = new MIDITrack();
        track.add(Pitch.C5);
        track.play();
        logger.debug("here is the track\n{}", track);
    }

    static void midiNote() {
        MIDITrack track = new MIDITrack();
        MIDINote note = new MIDINote(Pitch.C5);
        note.setStartBeat(1d);
        note.setDuration(1d);
        track.add(note);
        MIDIPerformer perf = new MIDIPerformer();
        perf.play(track);
        logger.debug("here is the track\n{}", track);
    }

    static void adds() {
        MIDITrack track = new MIDITrack();
        track.add(Pitch.C5).add(D5).add(E5).add(F5).add(G5);
        track.sequential();
        track.play();
        logger.debug("here is the track\n{}", track);
    }

    static void noteString() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C5 D E F G")
                .sequential()
                .build();
        track.play();
        logger.debug("here is the track\n{}", track);
    }

    static void append() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C5 D E F G")
                .sequential()
                .build();
        MIDITrack track2 = MIDITrackBuilder.create()
                .noteString("F5 e d c")
                .sequential()
                .build();

        logger.debug("here is the track\n{}", track);
        track.append(track2);
        logger.debug("here is the second track\n{}", track2);
        logger.debug("here is the track after the append\n{}", track);
        logger.debug(
                "here is the track after the append\n{}",
                track.toBriefMIDIString());
        logger.debug(
                "here is the track after the append\n{}",
                track.toMIDIString());
        // track.play();
        track2 = MIDITrackBuilder
                .create()
                .noteString(
                        "S+ c5,1.0,1.0 d5,2.0,1.0 e5,3.0,1.0 F5,4.0,1.0 G5,5.0,1.0 F5,6.0,1.0 e5,7.0,1.0 d5,8.0,1.0 c5,9.0,1.0")
                .sequential()
                .build();
        logger.debug("here is the parsed track\n{}", track2);

    }

    static void merge() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C5 D E F G")
                .sequential()
                .build();
        MIDITrack track2 = MIDITrackBuilder.create()
                .noteString("F5 e d c")
                .sequential()
                .build();

        logger.debug("here is the track\n{}", track);
        track.merge(track2);
        logger.debug("here is the second track\n{}", track2);
        logger.debug("here is the track after the merge\n{}", track);
        track.play();

        track.unmerge(track2);
        logger.debug("here is the track after the unmerge\n{}", track);
        logger.debug("here is the track 2 after the unmerge\n{}", track2);
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
        logger.debug("here is the track\n{}", track);
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
        logger.debug("here is the track\n{}", track);
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
        logger.debug("here is the track\n{}", track);
    }

    static void durs() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C6 E G")
                .startBeat(4.5)
                .durations(Duration.SIXTEENTH_NOTE, Duration.EIGHTH_NOTE)
                .sequential()
                .build();
        logger.debug("here is the track\n{}", track);
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
        logger.debug("here is the track\n{}", track);
    }

    static void startBeatsDursAndVelocities() {
        MIDITrack track = MIDITrackBuilder
                .create()
                .name("sdv")
                .description(
                        "created with a series of start beats, durations, and velocities")
                .noteString("C6 E G")
                .startBeats(1, 3.5, 4)
                .durations(
                        Duration.SIXTEENTH_NOTE,
                        Duration.EIGHTH_NOTE,
                        Duration.Q)
                .velocities(32, 64, 127)
                .build();
        logger.debug("here is the track\n{}", track);

        String filename = "src/test/resources/midifiles/sdv.mid";
        Score score = new Score("SDV");
        score.add(track);
        ScoreFactory.writeToMIDIFile(filename, score);
    }

    static void transpose() {
        MIDITrack track = MIDITrackBuilder
                .create()
                .name("transpose")
                .noteString("C6 E G")
                .build();
        logger.debug("here is the track\n{}", track);

        PitchModifier mod = new PitchModifier(Modifier.Operation.ADD, 12);
        track.map(mod);
        logger.debug("here is the transposed track\n{}", track);
    }

    static void pan() {
        MIDITrack track = MIDITrackBuilder
                .create()
                .name("pan modifier")
                .noteString("C6 E G")
                .build();
        logger.debug("here is the track\n{}", track);

        MIDINoteModifier mod = new AbstractMIDINoteModifier(32) {
            @Override
            public void modify(MIDINote n) {
                int pan = values.next().intValue();
                logger.debug("pan value is {}", pan);
                n.setPan(pan);
            }
        };
        track.map(mod);
        logger.debug("here is the modified track\n{}", track);
    }

    static void retro() {
        MIDITrack track = MIDITrackBuilder.create()
                .name("retrograde")
                .noteString("C6 E G")
                .sequential()
                .build();
        logger.debug("here is the track\n{}", track);
        MIDITrack retro = track.retrograde();
        // the start beat is the start beat of the last note in the original

        logger.debug("here is the track\n{}", track);
        logger.debug("here is the retrograde track\n{}", retro);

        retro.sequential();
        logger.debug("here is the sequential retrograde track\n{}", retro);
        retro.setStartBeat(6d);
        logger.debug("here is the retrograde track at beat 6\n{}", retro);
        retro.setStartBeat(1d);
        logger.debug("here is the retrograde track at beat 1\n{}", retro);

        // create a retrograde, but make it start at beat 1
        // retro = track.retrograde(1d);
        // logger.debug("here is the retrograde track on beat 1\n{}", retro);
    }

    static void inversion() {
        MIDITrack track = MIDITrackBuilder.create()
                .name("original")
                .noteString("C5 F G D G A F# G# B A# D# C#")
                .sequential()
                .build();
        logger.debug("here is the track\n{}", track);
        MIDITrack inversion = track.getInversion();
        inversion.setName("inversion");
        inversion.sequential();

        logger.debug("here is the track\n{}", track);
        logger.debug("here is the inversion track\n{}", inversion);
        track.play();
        // inversion.play();
    }

    static void performer() {
        MIDITrack track = MIDITrackBuilder.create()
                .name("original")
                .noteString("C5 F G D G A F# G# B A# D# C#")
                .sequential()
                .build();
        logger.debug("here is the track\n{}", track);

        MIDIPerformer mp = new MIDIPerformer();
        ConsoleReceiver receiver = new ConsoleReceiver();
        mp.track(track)
                .atTempo(120)
                .receiver(receiver)
                .play();
    }

    static void juke() {
        MIDITrack track = MIDITrackBuilder.create()
                .name("original")
                .noteString("C5 F G D G A F# G# B A# D# C#")
                .sequential()
                .build();
        logger.debug("here is the track\n{}", track);

        MIDIPerformer mp = new MIDIPerformer();
        mp.add(track).add(track);
        mp.start();
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        select();
    }

    static void select() {
        final String[] choices = new String[] { "ex1", "midiNote", "adds",
                "noteString",
                "emptyTrack",
                "chordProgression", "chordify", "durations", "append", "merge",
                "durations and velocities",
                "start beats, durations and velocities", "transpose",
                "pan modifier", "retrograde", "inversion", "performer", "juke"
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
        } else if (choice.equals("append")) {
            append();
        } else if (choice.equals("merge")) {
            merge();
        } else if (choice.equals("transpose")) {
            transpose();
        } else if (choice.equals("pan modifier")) {
            pan();
        } else if (choice.equals("retrograde")) {
            retro();
        } else if (choice.equals("midiNote")) {
            midiNote();
        } else if (choice.equals("inversion")) {
            inversion();
        } else if (choice.equals("performer")) {
            performer();
        } else if (choice.equals("juke")) {
            juke();

        }
    }

}
