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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.Pitch;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDINoteBuilderTest {
    private static final Logger logger = LoggerFactory
            .getLogger(MIDINoteBuilderTest.class);

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
    public void shouldCreateMIDINote() {

        int expectedPitch = Pitch.C5;
        double expectedStartBeat = 1d;
        double expectedDuration = 1d;
        int expectedChannel = 0;
        int expectedVelocity = 64;
        int expectedPitchBend = 0;
        int expectedVoice = 0;
        int expectedProgram = 0;
        int expectedPan = 64;

        MIDINote note = MIDINoteBuilder.create()
                .build();
        logger.debug("the note is {}", note);

        assertThat("note is not null", note, notNullValue());

        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(expectedPitch));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat));

        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        assertThat("the expectedChannel is correct", note.getChannel(),
                equalTo(expectedChannel));

        assertThat("the expectedVelocity is correct", note.getVelocity(),
                equalTo(expectedVelocity));

        assertThat("the expectedPitchBend is correct", note.getPitchBend(),
                equalTo(expectedPitchBend));

        assertThat("the expectedVoice is correct", note.getVoice(),
                equalTo(expectedVoice));

        assertThat("the expectedProgram is correct", note.getProgram(),
                equalTo(expectedProgram));

        assertThat("the expectedPan is correct", note.getPan(),
                equalTo(expectedPan));

    }

    @Test
    public void shouldCreateMIDINoteWithPitch() {
        int expectedPitch = Pitch.EF5;
        double expectedStartBeat = 1d;
        double expectedDuration = 1d;
        int expectedChannel = 0;
        int expectedVelocity = 64;
        int expectedPitchBend = 0;
        int expectedVoice = 0;
        int expectedProgram = 0;
        int expectedPan = 64;

        MIDINote note = MIDINoteBuilder.create()
                .pitch(expectedPitch)
                .build();
        logger.debug("the note is {}", note);

        assertThat("note is not null", note, notNullValue());

        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(expectedPitch));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat));

        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        assertThat("the expectedChannel is correct", note.getChannel(),
                equalTo(expectedChannel));

        assertThat("the expectedVelocity is correct", note.getVelocity(),
                equalTo(expectedVelocity));

        assertThat("the expectedPitchBend is correct", note.getPitchBend(),
                equalTo(expectedPitchBend));

        assertThat("the expectedVoice is correct", note.getVoice(),
                equalTo(expectedVoice));

        assertThat("the expectedProgram is correct", note.getProgram(),
                equalTo(expectedProgram));

        assertThat("the expectedPan is correct", note.getPan(),
                equalTo(expectedPan));

    }

    @Test
    public void shouldCreateMIDINoteWithPitchAndStartBeat() {
        int expectedPitch = Pitch.EF5;
        double expectedStartBeat = 4.5d;
        double expectedDuration = 1d;
        int expectedChannel = 0;
        int expectedVelocity = 64;
        int expectedPitchBend = 0;
        int expectedVoice = 0;
        int expectedProgram = 0;
        int expectedPan = 64;

        MIDINote note = MIDINoteBuilder.create()
                .pitch(expectedPitch)
                .startBeat(expectedStartBeat)
                .build();
        logger.debug("the note is {}", note);

        assertThat("note is not null", note, notNullValue());

        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(expectedPitch));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat));

        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        assertThat("the expectedChannel is correct", note.getChannel(),
                equalTo(expectedChannel));

        assertThat("the expectedVelocity is correct", note.getVelocity(),
                equalTo(expectedVelocity));

        assertThat("the expectedPitchBend is correct", note.getPitchBend(),
                equalTo(expectedPitchBend));

        assertThat("the expectedVoice is correct", note.getVoice(),
                equalTo(expectedVoice));

        assertThat("the expectedProgram is correct", note.getProgram(),
                equalTo(expectedProgram));

        assertThat("the expectedPan is correct", note.getPan(),
                equalTo(expectedPan));

    }

    @Test
    public void shouldCreateMIDINoteWithPitchAndStartBeatAndDuration() {
        int expectedPitch = Pitch.EF5;
        double expectedStartBeat = 4.5d;
        double expectedDuration = Duration.EIGHTH_NOTE;
        int expectedChannel = 0;
        int expectedVelocity = 64;
        int expectedPitchBend = 0;
        int expectedVoice = 0;
        int expectedProgram = 0;
        int expectedPan = 64;

        MIDINote note = MIDINoteBuilder.create()
                .pitch(expectedPitch)
                .startBeat(expectedStartBeat)
                .duration(expectedDuration)
                .build();
        logger.debug("the note is {}", note);

        assertThat("note is not null", note, notNullValue());

        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(expectedPitch));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat));

        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        assertThat("the expectedChannel is correct", note.getChannel(),
                equalTo(expectedChannel));

        assertThat("the expectedVelocity is correct", note.getVelocity(),
                equalTo(expectedVelocity));

        assertThat("the expectedPitchBend is correct", note.getPitchBend(),
                equalTo(expectedPitchBend));

        assertThat("the expectedVoice is correct", note.getVoice(),
                equalTo(expectedVoice));

        assertThat("the expectedProgram is correct", note.getProgram(),
                equalTo(expectedProgram));

        assertThat("the expectedPan is correct", note.getPan(),
                equalTo(expectedPan));

    }

    @Test
    public void shouldCreateMIDINoteWithAll() {
        int expectedPitch = Pitch.EF5;
        double expectedStartBeat = 4.5d;
        double expectedDuration = Duration.EIGHTH_NOTE;
        int expectedChannel = 4;
        int expectedVelocity = 120;
        int expectedPitchBend = 32;
        int expectedVoice = 1;
        int expectedProgram = 4;
        int expectedPan = 127;

        MIDINote note = MIDINoteBuilder.create()
                .pitch(expectedPitch)
                .startBeat(expectedStartBeat)
                .duration(expectedDuration)
                .channel(expectedChannel)
                .velocity(expectedVelocity)
                .pitchBend(expectedPitchBend)
                .voice(expectedVoice)
                .program(expectedProgram)
                .pan(expectedPan)
                .build();
        logger.debug("the note is {}", note);

        assertThat("note is not null", note, notNullValue());

        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(expectedPitch));
        assertThat("the start beat is correct", note.getStartBeat(),
                equalTo(expectedStartBeat));

        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        assertThat("the duration is correct", note.getDuration(),
                equalTo(expectedDuration));

        assertThat("the expectedChannel is correct", note.getChannel(),
                equalTo(expectedChannel));

        assertThat("the expectedVelocity is correct", note.getVelocity(),
                equalTo(expectedVelocity));

        assertThat("the expectedPitchBend is correct", note.getPitchBend(),
                equalTo(expectedPitchBend));

        assertThat("the expectedVoice is correct", note.getVoice(),
                equalTo(expectedVoice));

        assertThat("the expectedProgram is correct", note.getProgram(),
                equalTo(expectedProgram));

        assertThat("the expectedPan is correct", note.getPan(),
                equalTo(expectedPan));

    }

}
