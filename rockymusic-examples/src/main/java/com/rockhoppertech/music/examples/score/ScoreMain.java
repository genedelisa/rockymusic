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

import static com.rockhoppertech.music.Pitch.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDINoteBuilder;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.Score;
import com.rockhoppertech.music.midi.parse.MIDIStringParser;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class ScoreMain {
    private static final Logger logger = LoggerFactory
            .getLogger(ScoreMain.class);

    public static void main(String[] args) {

        play();
    }

    static void play() {
        Score score = new Score();
        MIDITrack track = new MIDITrack();
        score.add(track);
        MIDINote note = MIDINoteBuilder.create().build();
        track.add(note);
        note = MIDINoteBuilder.create().pitch(Pitch.D5).build();
        track.append(note);

        // append changes the start beats. Use add if you want the specified
        // start beat.
        // with import static com.rockhoppertech.music.Pitch.*;
        // you can say just E5 instead of Pitch.E5
        track.add(
                MIDINoteBuilder.create()
                        .pitch(E5)
                        .startBeat(2.5)
                        .duration(.5)
                        .build())
                .add(
                        MIDINoteBuilder.create()
                                .pitch(F5)
                                .startBeat(6.5)
                                .duration(1.5)
                                .velocity(90)
                                .build());

        track.append(
                new MIDINote(Pitch.G5)).append(
                new MIDINote(Pitch.A5));

        track.append(Pitch.G5).append(Pitch.A5);
        
        // with static import
        track.append(G5).append(A5);
        
        String ps = MIDIStringParser.createStringBrief(track);
        logger.debug(ps);
        // C5,1.0,1.0 D5,2.0,1.0 E5,2.5,0.5 F5,6.5,1.5 G5,8.0,1.0 A5,9.0,1.0
        // G5,10.0,1.0 A5,11.0,1.0

        // directly. But the track builder will do the same.
        MIDIStringParser parser = new MIDIStringParser();
        // S+ puts it into append mode, so start beats are sequential
        // otherwise, they would all have the same start beat.
        String str = "S+ C5, D, E, F, G, A, B, C6";
        track = parser.parseString(str);

        // same thing
        track = MIDITrackBuilder.create()
                .noteString("S+ C5, D, E, F, G, A, B, C")
                .build();

        track.play();
    }
}
