package com.rockhoppertech.music.examples;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.midi.js.Instrument;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.Score;
import com.rockhoppertech.music.midi.js.ScoreFactory;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class Chords {
    private static final Logger logger = LoggerFactory
            .getLogger(Chords.class);

    public static void main(String[] args) {
        example();
        //createScore();
    }

    static void example() {
        Score score = new Score("Chord");
        MIDITrack violin = MIDITrackBuilder.create()
                .name("Violin I")
                .instrument(Instrument.VIOLIN)
                .noteString("C6 E G")
                .startBeat(4.5)
                .durations(Duration.SIXTEENTH_NOTE, Duration.EIGHTH_NOTE)
                .sequential()
                .build();
        score.add(violin);
        logger.debug("score is {}", score);

    }

    static void createScore() {
        Score score = new Score("Chords");

        MIDITrack violin = MIDITrackBuilder.create()
                .name("Violin I")
                .instrument(Instrument.VIOLIN)
                .noteString("C6 E G")
                .durations(4d)
                .build();
        score.add(violin);

        MIDITrack chord = MIDITrackBuilder.create()
                .noteString("G4 B D5")
                .durations(4d)
                .instrument(Instrument.VIOLIN)
                // otherwise, it defaults to Piano, and is then remapped.
                .build();
        violin.append(chord);

        MIDITrack tmp = violin.append("G4 B D5");
        logger.debug("tmp is {}", tmp);

        score.play();
        System.out.println(score);

        String filename = "src/test/resources/midifiles/chords.mid";
        ScoreFactory.writeToMIDIFile(filename, score);
    }
}
