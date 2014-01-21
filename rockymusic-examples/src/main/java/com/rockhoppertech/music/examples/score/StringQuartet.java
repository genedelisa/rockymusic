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

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.midi.js.Instrument;
import com.rockhoppertech.music.midi.js.KeySignature;
import com.rockhoppertech.music.midi.js.MIDIPerformer;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.Score;
import com.rockhoppertech.music.midi.js.ScoreFactory;

public class StringQuartet {

    static String filename = "src/test/resources/midifiles/stringquartet.mid";
    private static final Logger logger = LoggerFactory
            .getLogger(StringQuartet.class);

    public static void main(String[] args) {

        createQuartet();
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
        MIDIPerformer perf = new MIDIPerformer();
        perf.play(sequence);
    }

    static void createQuartet() {
        Score score = new Score("String Quartet");
        score.setTempoAtBeat(1d, 120);
        score.setKeySignatureAtBeat(1d, KeySignature.DMAJOR);
        score.setTimeSignatureAtBeat(1d,3, 4);
        score.setTempoAtBeat(4d, 240);
        score.setKeySignatureAtBeat(4d, KeySignature.BFMINOR);
        score.setTimeSignatureAtBeat(4d, 5, 4);

        MIDITrack violin = MIDITrackBuilder.create()
                .name("Violin I")
                .instrument(Instrument.VIOLIN)
                .noteString("S+ C6 D E")
                .build();
        score.add(violin);

        MIDITrack violin2 = MIDITrackBuilder.create()
                .name("Violin II")
                .instrument(Instrument.VIOLIN)
                .noteString("S+ C5 D E")
                .build();
        score.add(violin2);
        
        MIDITrack repeatedLick = MIDITrackBuilder.create()
                .instrument(Instrument.VIOLIN)
                .noteString("S+ C5,e D,e E,e A,e")
                .repeats(10)
                .build();
        violin2.append(repeatedLick);

        MIDITrack viola = MIDITrackBuilder.create()
                .name("Viola")
                .instrument(Instrument.VIOLA)
                .noteString("S+ C4 D E")
                .build();
        score.add(viola);

        MIDITrack contrabass = MIDITrackBuilder.create()
                // .name("Bass")
                .instrument(Instrument.CONTRABASS)
                .noteString("S+ E2 F E ")
                .build();
        score.add(contrabass);

        MIDITrack tmp = MIDITrackBuilder.create()
                .noteString("S+ X8 (G2,.5)")
                .instrument(Instrument.CONTRABASS)
                // otherwise, it defaults to Piano, and is then remapped to
                // contrabass.
                .build();
        contrabass.append(tmp).append(tmp);

        tmp = MIDITrackBuilder.create()
                .noteString("S+ X8 (G4,.5, A4,.5)")
                .instrument(Instrument.VIOLA)
                .build();
        viola.append(tmp, Duration.DOUBLE_WHOLE_NOTE);

        logger.debug("Generated score {}", score);

        ScoreFactory.writeToMIDIFile(filename, score);
    }

}
