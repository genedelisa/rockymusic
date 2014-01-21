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

import com.rockhoppertech.music.midi.js.MIDIPerformer;
import com.rockhoppertech.music.midi.js.Score;
import com.rockhoppertech.music.midi.js.ScoreFactory;

/**
 * Play a standard MIDI file.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class PlayMIDIFile {
    private static final Logger logger = LoggerFactory
            .getLogger(PlayMIDIFile.class);

    public static void main(String[] args) {
        String filename = "src/test/resources/midifiles/test.mid";
        if (args.length > 1)
            filename = args[1];
        play(filename);
    }

    static void play(String filename) {
        logger.debug("playing midi file {}", filename);
        Score score = null;
        try {
            score = ScoreFactory.readSequence(filename);
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
            return;
        }
        logger.debug(score.toString());

        // you can also do this:
        // score.play()

        Sequence sequence = ScoreFactory.scoreToSequence(score);
        if (sequence == null) {
            logger.error("oops. sequence is null");
            return;
        }
        MIDIPerformer perf = new MIDIPerformer();
        perf.atTempo(240f);
        // this is overloaded with MIDITrack, Score, and Sequence.
        // each has a play method. e.g.
        // Score's play creates a performers and sends the play message.
        // just like here.
        perf.play(sequence);
    }

}
