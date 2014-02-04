/**
 * 
 */
package com.rockhoppertech.music.scale;

/*
 * #%L
 * Rocky Music Core
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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ScaleTrackBuilderTest {

    private static final Logger logger = LoggerFactory
            .getLogger(ScaleTrackBuilderTest.class);

    @Test
    public void name() {
        MIDITrack track = ScaleTrackBuilder.create()
                .name("Major")
                .build();
        // it's already sequential
        logger.debug("track:\n{}", track);
        assertThat("track is not null", track, is(notNullValue()));
        MIDINote note = null;

        note = track.get(0);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.C5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(1d)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(4d)));

        note = track.get(1);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.D5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(5d)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(4d)));

        note = track.get(2);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.E5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(9d)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(4d)));

        note = track.get(3);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.F5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(13d)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(4d)));

        note = track.get(4);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.G5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(17d)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(4d)));

        note = track.get(5);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.A5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(21d)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(4d)));

        note = track.get(6);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.B5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(25d)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(4d)));

        note = track.get(7);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.C6)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(29d)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(4d)));

    }

    @Test
    public void nameAndDuration() {
        MIDITrack track = ScaleTrackBuilder.create()
                .name("Major")
                .duration(Duration.SIXTEENTH_NOTE)
                .build();
        // it's already sequential
        logger.debug("track:\n{}", track);
        assertThat("track is not null", track, is(notNullValue()));
        MIDINote note = null;
        double expectedDuration = Duration.SIXTEENTH_NOTE;
        double expectedStartBeat = 1d;

        note = track.get(0);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.C5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        expectedStartBeat += expectedDuration;
        note = track.get(1);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.D5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(2);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.E5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(3);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.F5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(4);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.G5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(5);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.A5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(6);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.B5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(7);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.C6)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

    }

    @Test
    public void nameAndStartBeat() {
        double expectedStartBeat = 5.5d;
        MIDITrack track = ScaleTrackBuilder.create()
                .name("Major")
                .startBeat(expectedStartBeat)
                .build();
        // it's already sequential
        logger.debug("track:\n{}", track);
        assertThat("track is not null", track, is(notNullValue()));
        MIDINote note = null;
        double expectedDuration = Duration.WHOLE_NOTE;

        note = track.get(0);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.C5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        expectedStartBeat += expectedDuration;
        note = track.get(1);
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.D5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(2);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.E5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(3);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.F5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(4);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.G5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(5);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.A5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(6);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.B5)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

        note = track.get(7);
        expectedStartBeat += expectedDuration;
        assertThat("note is not null", note, is(notNullValue()));
        assertThat("note midi number is correct",
                note.getMidiNumber(), is(equalTo(Pitch.C6)));
        assertThat("note start beat is correct",
                note.getStartBeat(), is(equalTo(expectedStartBeat)));
        assertThat("note duration is correct",
                note.getDuration(), is(equalTo(expectedDuration)));

    }

}
