package com.rockhoppertech.music.midi.js.modifiers.google;

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

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.MIDITrackFactory;
import com.rockhoppertech.music.midi.js.predicate.PitchGreaterThanPredicate;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class PitchGreaterThanPredicateTest {
    static Logger logger = LoggerFactory
            .getLogger(PitchGreaterThanPredicateTest.class);

    MIDITrack track;

    @Before
    public void setUp() {
        track = MIDITrackBuilder.create()
                .noteString("C5 CS F FS E F4")
                .build();
    }

    @Test
    public final void testApply() {
        PitchGreaterThanPredicate p = new PitchGreaterThanPredicate(
                Pitch.E5);
        boolean actual = p.apply(new MIDINote(Pitch.C5));
        boolean expected = false;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        actual = p.apply(new MIDINote(Pitch.C4));
        expected = false;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        actual = p.apply(new MIDINote(Pitch.F5));
        expected = true;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public final void trackFactory() {
        PitchGreaterThanPredicate p = new PitchGreaterThanPredicate(
                Pitch.E5);
        MIDITrack newTrack = MIDITrackFactory
                .createTrackFromPredicate(track, p);
        assertThat("the new track is not null",
                newTrack, is(notNullValue()));

        List<MIDINote> expectedNotes = Lists.newArrayList(
                new MIDINote(Pitch.F5),
                new MIDINote(Pitch.FS5));

        logger.debug("notes size is {}", newTrack.getNotes().size());
        logger.debug("notes {}", newTrack.getNotes());

        int index = 0;
        assertThat(
                "note is correct",
                newTrack.getNotes().get(index).getMidiNumber(),
                is(equalTo(expectedNotes.get(index).getMidiNumber())));
        index++;
        assertThat(
                "note is correct",
                newTrack.getNotes().get(index).getMidiNumber(),
                is(equalTo(expectedNotes.get(index).getMidiNumber())));

    }

}
