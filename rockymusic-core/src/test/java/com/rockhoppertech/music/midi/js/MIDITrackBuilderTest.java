package com.rockhoppertech.music.midi.js;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDITrackBuilderTest {
    private static final Logger logger = LoggerFactory
            .getLogger(MIDITrackBuilderTest.class);

    /**
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldCreateSequenceWithInstrument() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C E G")
                .sequential()
                .instrument(MIDIGMPatch.VIOLIN)
                .build();
        logger.debug("the track is {}", track);

        int expectedSize = 3;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));

        MIDIGMPatch gmpatch = track.getGmpatch();
        assertThat("gmpatch is not null", gmpatch, notNullValue());
        assertThat("gmpatch is correct", gmpatch,
                equalTo(MIDIGMPatch.VIOLIN));

        MIDINote note = track.get(0);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(C5));
        assertThat("the note patch program is correct", note.getProgram(),
                equalTo(MIDIGMPatch.VIOLIN.getProgram()));

        note = track.get(1);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E5));
        assertThat("the note patch program is correct", note.getProgram(),
                equalTo(MIDIGMPatch.VIOLIN.getProgram()));

        note = track.get(2);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(G5));
        assertThat("the note patch program is correct", note.getProgram(),
                equalTo(MIDIGMPatch.VIOLIN.getProgram()));

    }

    @Test
    public void shouldCreateSequenceWithDuration() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C E G")
                .durations(4d)
                .sequential()
                .instrument(MIDIGMPatch.VIOLIN)
                .build();

        logger.debug("the track is {}", track);

        int expectedSize = 3;
        double expectedStartBeat = 1d;
        double expectedDuration = 4d;

        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));

        MIDINote note = track.get(0);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(C5));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat));
        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        note = track.get(1);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E5));
        // add 1 which is the default duration
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat + expectedDuration));
        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        note = track.get(2);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(G5));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat + 2 * expectedDuration));
        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

    }

    @Test
    public void shouldCreateChord() {
        // a chord on beat 2
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C, E, G")
                .startBeat(2d)
                .build();
        logger.debug("the track is {}", track);

        int expectedSize = 3;
        double expectedStartBeat = 2d;

        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));

        MIDINote note = track.get(0);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(C5));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat));

        note = track.get(1);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E5));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat));

        note = track.get(2);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(G5));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat));

    }

    @Test
    public void shouldCreateSequence() {
        // a sequence that starts on beat 2
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C E G")
                .startBeat(2d)
                .sequential()
                .build();
        logger.debug("the track is {}", track);

        int expectedSize = 3;
        double expectedStartBeat = 2d;

        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));

        MIDINote note = track.get(0);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(C5));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat));

        note = track.get(1);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E5));
        // add 1 which is the default duration
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat + 1));

        note = track.get(2);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(G5));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat + 2));

    }

    @Test
    public void shouldCreateSequenceWithDurations() {

        // the durations should cycle.
        // in this case that would be: 4, .5, 4
        double expectedDuration = 4d;
        // or expectedDuration = Duration.WHOLE_NOTE;
        double expectedDuration2 = .5;
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C E G")
                .durations(expectedDuration, expectedDuration2)
                .sequential()
                .instrument(MIDIGMPatch.VIOLIN)
                .build();

        logger.debug("the track is {}", track);

        int expectedSize = 3;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));

        MIDINote note = track.get(0);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(C5));
        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        note = track.get(1);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E5));
        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration2));

        note = track.get(2);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(G5));
        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

    }

    @Test
    public void shouldCreateSequenceFromScale() {

        MIDITrack track = MIDITrackBuilder.create()
                .scaleName("Major")
                .build();
        logger.debug("the track is {}", track);
        // it's already sequential.

        for (String scalename : ScaleFactory.getScaleNames()) {
            logger.debug(scalename);
        }

        int expectedSize = 8;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));

        int noteIndex = 0;
        MIDINote note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(C5));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(D5));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E5));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(F5));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(G5));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(A5));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(B5));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(C6));

    }

    @Test
    public void shouldCreateSequenceFromScaleAndKey() {

        MIDITrack track = MIDITrackBuilder.create()
                .scaleName("Major")
                .startPitch("D3")
                .build();
        logger.debug("D major is {}", track);

        int expectedSize = 8;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));

        int noteIndex = 0;
        MIDINote note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(D3));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E3));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(FS3));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(G3));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(A3));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(B3));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(CS4));

        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(D4));

    }

    @Test
    public void shouldCreateSequenceFromScaleAndKeyNOctaves() {
    
        MIDITrack track = MIDITrackBuilder.create()
                .scaleName("Major")
                .startPitch("D3")
                .nScaleOctaves(3)
                .build();
        logger.debug("D major is {}", track);
    
        int expectedSize = 22;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));
    
        int noteIndex = 0;
        MIDINote note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(D3));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E3));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(FS3));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(G3));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(A3));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(B3));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(CS4));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(D4));
        
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E4));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(FS4));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(G4));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(A4));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(B4));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(CS5));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(D5));
        
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E5));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(FS5));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(G5));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(A5));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(B5));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(CS6));
    
        note = track.get(noteIndex++);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(D6));
    
    }

}
