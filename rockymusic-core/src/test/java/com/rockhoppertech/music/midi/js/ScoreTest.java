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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

//import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ScoreTest {
    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory
            .getLogger(ScoreTest.class);

    /**
	 * 
	 */
    @Test
    public void shouldHaveDefaultPitch() {

        Score score = new Score();
        MIDITrack track = new MIDITrack();
        score.add(track);
        MIDINote note = MIDINoteBuilder.create().build();
        track.add(note);
        note = MIDINoteBuilder.create().pitch(Pitch.E5).build();
        track.append(note);
        logger.debug("score with E5 \n{}", score);

        assertThat("The score is not null.", score, notNullValue());
        assertThat("the pitch is E5", note.getPitch().getMidiNumber(),
                equalTo(Pitch.E5));
        // Score automatically creates a metaevent track
        assertThat("there are 2 tracks", score.getTracks().size(), equalTo(2));
        assertThat(
                "there are 2 notes on the track",
                track.getNotes().size(),
                equalTo(2));
    }

    @Test
    public void shouldHaveToString() {

        Score score = new Score();
        String actual = score.toString();
        assertThat("The score is not null.", score, is(notNullValue()));
        assertThat("The string is not null.", actual, is(notNullValue()));
        logger.debug("to string is '{}'", actual);
        String expected = "Score Name:null\nResolution:256\nTrack Name:meta\nInstrument:Instrument [patch=PIANO, name=Piano, minPitch=21, maxPitch=96]\nno notes\nno events\n";
        assertThat("string is correct", actual, is(equalTo(expected)));
    }

    @Test
    public void shouldRemoveTrack() {
        Score score = new Score();
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .build();
        MIDITrack track2 = MIDITrackBuilder.create()
                .name("track 2")
                .build();
        score.add(track);
        score.add(track2);
        logger.debug("score {}", score);

        score.remove(track2);
        logger.debug("score {}", score);
    }

    @Test
    public void shouldRemoveTrackAtIndex() {
        Score score = new Score();
        assertThat("The score is not null.", score, is(notNullValue()));
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .build();
        MIDITrack track2 = MIDITrackBuilder.create()
                .name("track 2")
                .build();
        score.add(track);
        score.add(track2);
        logger.debug("score {}", score);
        MIDITrack t = score.getTrackWithName("track 1");
        assertThat(
                "The track with name track 1 is not null.",
                t,
                is(notNullValue()));
        // removes track 1
        // track 0 is the meta track
        score.remove(1);
        logger.debug("score {}", score);
        t = score.getTrackWithName("track 1");
        assertThat(
                "The track with name track 1 is  null.",
                t,
                is(nullValue()));
    }

}
