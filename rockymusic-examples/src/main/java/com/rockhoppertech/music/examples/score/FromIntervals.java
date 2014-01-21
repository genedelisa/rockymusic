package com.rockhoppertech.music.examples.score;

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

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.js.Instrument;
import com.rockhoppertech.music.midi.js.MIDIPerformer;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackFactory;
import com.rockhoppertech.music.midi.js.MIDIUtils;
import com.rockhoppertech.music.midi.js.Score;
import com.rockhoppertech.music.midi.js.ScoreFactory;

public class FromIntervals {

    static String filename = "src/test/resources/midifiles/fromIntervals.mid";
    private static final Logger logger = LoggerFactory
            .getLogger(FromIntervals.class);

    public static void main(String[] args) {

        create();
        readAndPlay();
    }

    static void readAndPlay() {
        Score score = null;
        try {
            score = ScoreFactory.readSequence(filename);
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
            return;
        }
        logger.debug("Read score {}", score);

        Sequence sequence = ScoreFactory.scoreToSequence(score);
        if (sequence == null) {
            logger.error("oops. cannot create Sequence");
            return;
        }
        MIDIUtils.print(sequence);

        MIDIPerformer perf = new MIDIPerformer();
        perf.play(sequence);
    }

    static void create() {
        Score score = new Score("From Intervals");
        score.setTempoAtBeat(1d, 120);

        MIDITrack track = MIDITrackFactory.createFromIntervals(1, 4);
        // track.useInstrument(MIDIGMPatch.VIOLIN);
       // track.setName("from intervals 1, 4 from default C5, relative to previous note");
        track.addMetaText(1, "from intervals 1, 4 from default C5, relative to previous note");        
        track.sequential();
        double endbeat = track.getEndBeat();
        score.add(track);

        track = MIDITrackFactory.createFromIntervals(
                PitchFactory.getPitch("Eb5"),
                1,
                4);
        track.useInstrument(Instrument.VIOLIN);
        track.setName("Violin");
        track.addMetaText(endbeat, "from intervals 1, 4 from ef5");                
        track.setStartBeat(endbeat);
        track.sequential();
        track.setChannel(1);
        track.setVelocity(70);
        endbeat = track.getEndBeat();
        score.add(track);

        boolean absolute = true;
        int numOctaves = 1;
        int intervalUnit = 1;
        track = MIDITrackFactory.createFromIntervals(
                new int[] { 3, -1, -5 },
                Pitch.C5,
                intervalUnit,
                absolute,
                numOctaves);
        track.setName("whatever");
        track.addMetaText(endbeat, "from intervals 3 -1 -5 absolute");        
        track.setStartBeat(endbeat);
        track.sequential();
        track.setChannel(2);
        endbeat = track.getEndBeat();
        score.add(track);

        intervalUnit = 2;
        numOctaves = 2;
        track = MIDITrackFactory.createFromIntervals(
                new int[] { 3, -1, -5 },
                Pitch.C5,
                intervalUnit,
                absolute,
                numOctaves);
        //track.setName("from intervals 3 -1 -5 absolute unit = 2");
        track.addMetaText(endbeat, "from intervals 3 -1 -5 absolute unit = 2");
        track.setStartBeat(endbeat);
        track.sequential();
        track.setChannel(3);
        track.useInstrument(Instrument.HARP);
        endbeat = track.getEndBeat();
        score.add(track);

        logger.debug("Generated score {}", score);

        ScoreFactory.writeToMIDIFile(filename, score);
    }

}
