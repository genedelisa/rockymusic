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

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import com.rockhoppertech.music.midi.js.MIDIUtils;
import com.rockhoppertech.music.midi.js.Score;
import com.rockhoppertech.music.midi.js.ScoreFactory;

public class MIDIUtilRuns {

    public static void main(String[] args) {
        
        //String filename = "src/test/resources/midifiles/chords.mid";
        //String filename = "src/test/resources/midifiles/sib-e-minor.mid";        
        String filename = "src/test/resources/midifiles/stringquartet.mid";                
        Sequence sequence = MIDIUtils.read(new File(filename));
        MIDIUtils.print(sequence);
    }

    static void read() {
        String filename = "test.mid";
        Score score = null;
        try {
            score = ScoreFactory.readSequence(filename);
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(score);

        Sequence sequence = ScoreFactory.scoreToSequence(score);
        if (sequence == null) {
            System.err.println("oops");
            return;
        }
//        MIDIPerformer perf = new MIDIPerformer();
//        perf.play(sequence);
//        ScoreFactory.writeToMIDIFile("foo.mid", score);
    }

}
