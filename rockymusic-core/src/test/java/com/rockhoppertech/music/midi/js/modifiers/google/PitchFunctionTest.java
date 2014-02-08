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
import com.rockhoppertech.music.midi.js.function.PitchFunction;
import com.rockhoppertech.music.midi.js.function.AbstractMusicFunction.Operation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class PitchFunctionTest {
    static Logger logger = LoggerFactory
            .getLogger(PitchFunctionTest.class);

    MIDITrack track;

    @Before
    public void setUp() {
        track = MIDITrackBuilder.create()
                .noteString("C5 CS F FS E F4")
                .build();
    }

    @Test
    public final void testApplyWithAdd() {
        PitchFunction function = new PitchFunction();
        function.setOperation(Operation.ADD);

        // Predicate<MIDINote> p = Predicates.compose(
        // Predicates.alwaysTrue(),
        // function);

        MIDINote note = function.apply(new MIDINote(Pitch.C5));
        int actual = note.getMidiNumber();
        int expected = Pitch.CS5;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        note = function.apply(new MIDINote(Pitch.C4));
        actual = note.getMidiNumber();
        expected = Pitch.CS4;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        note = function.apply(new MIDINote(Pitch.F5));
        actual = note.getMidiNumber();
        expected = Pitch.FS5;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public final void testApplyWithAddValues() {
        PitchFunction function = new PitchFunction(1, 2, 3);
        function.setOperation(Operation.ADD);

        MIDINote note = function.apply(new MIDINote(Pitch.C5));
        int actual = note.getMidiNumber();
        int expected = Pitch.CS5;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        note = function.apply(new MIDINote(Pitch.C5));
        actual = note.getMidiNumber();
        expected = Pitch.D5;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        note = function.apply(new MIDINote(Pitch.C5));
        actual = note.getMidiNumber();
        expected = Pitch.EF5;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));
        
        // now repeat to test circularList
        note = function.apply(new MIDINote(Pitch.C5));
        actual = note.getMidiNumber();
        expected = Pitch.CS5;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));
        
        note = function.apply(new MIDINote(Pitch.C5));
        actual = note.getMidiNumber();
        expected = Pitch.D5;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        note = function.apply(new MIDINote(Pitch.C5));
        actual = note.getMidiNumber();
        expected = Pitch.EF5;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public final void transform() {
        PitchFunction function = new PitchFunction();
        function.setOperation(Operation.ADD);
        List<MIDINote> newnotes = Lists.transform(track.getNotes(), function);
        List<MIDINote> expectedNotes = Lists.newArrayList(
                new MIDINote(Pitch.CS5),
                new MIDINote(Pitch.D5),
                new MIDINote(Pitch.FS5),
                new MIDINote(Pitch.G5),
                new MIDINote(Pitch.F5),
                new MIDINote(Pitch.FS4));

        for (int index = 0; index < 6; index++) {
            assertThat(
                    "note is correct",
                    newnotes.get(index).getMidiNumber(),
                    is(equalTo(expectedNotes.get(index).getMidiNumber())));
        }

    }

    @Test
    public final void trackFactory() {
        PitchFunction function = new PitchFunction();
        function.setOperation(Operation.ADD);

        MIDITrack newTrack = MIDITrackFactory
                .create(track, function);
        assertThat("the new track is not null",
                newTrack, is(notNullValue()));

        List<MIDINote> expectedNotes = Lists.newArrayList(
                new MIDINote(Pitch.CS5),
                new MIDINote(Pitch.D5),
                new MIDINote(Pitch.FS5),
                new MIDINote(Pitch.G5),
                new MIDINote(Pitch.F5),
                new MIDINote(Pitch.FS4));

        for (int index = 0; index < 6; index++) {
            assertThat(
                    "note is correct",
                    newTrack.get(index).getMidiNumber(),
                    is(equalTo(expectedNotes.get(index).getMidiNumber())));
        }

    }

}
