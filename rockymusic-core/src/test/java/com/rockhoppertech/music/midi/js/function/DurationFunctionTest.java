package com.rockhoppertech.music.midi.js.function;

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
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.MIDITrackFactory;
import com.rockhoppertech.music.midi.js.function.AbstractMusicFunction.Operation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 *
 */
public class DurationFunctionTest {
    static Logger logger = LoggerFactory
            .getLogger(DurationFunctionTest.class);

    MIDITrack track;

    @Before
    public void setUp() {
        track = MIDITrackBuilder.create()
                .noteString("C5 CS F FS E F4")
                .build();
    }

    @Test
    public final void testApplyWithAdd() {
        DurationFunction function = new DurationFunction();
        function.setOperation(Operation.ADD);

        Timed note = function.apply(new MIDINote(Pitch.C5));
        double actual = note.getDuration();
        double expected = 2d;
        assertThat("duration is correct",
                actual, is(equalTo(expected)));

        note = function.apply(new MIDINote(Pitch.C4));
        actual = note.getDuration();
        expected = 2d;
        assertThat("duration is correct",
                actual, is(equalTo(expected)));
    }

    @Test
    public final void testApplyWithAddValues() {
        DurationFunction function = new DurationFunction(1, 2, 3);
        function.setOperation(Operation.ADD);

        Timed note = function.apply(new MIDINote(Pitch.C5));
        double actual = note.getDuration();
        double expected = 2d;
        assertThat("duration is correct",
                actual, is(equalTo(expected)));

        note = function.apply(new MIDINote(Pitch.C5));
        actual = note.getDuration();
        expected = 3d;
        assertThat("duration is correct",
                actual, is(equalTo(expected)));

        note = function.apply(new MIDINote(Pitch.C5));
        actual = note.getDuration();
        expected = 4d;
        assertThat("duration is correct",
                actual, is(equalTo(expected)));

        // circular list
        note = function.apply(new MIDINote(Pitch.C5));
        actual = note.getDuration();
        expected = 2d;
        assertThat("duration is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public final void transform() {
        DurationFunction function = new DurationFunction();
        function.setOperation(Operation.ADD);
        List<Timed> newnotes = Lists.transform(track.getNotes(), function);
        List<Double> expectedNotes = Lists.newArrayList(
                2d, 2d, 2d, 2d, 2d, 2d);

        for (int index = 0; index < 6; index++) {
            assertThat(
                    "duration is correct",
                    newnotes.get(index).getDuration(),
                    is(equalTo(expectedNotes.get(index))));
        }

    }

    @Test
    public final void trackFactory() {
        DurationFunction function = new DurationFunction();
        function.setOperation(Operation.ADD);

        MIDITrack newTrack = MIDITrackFactory
                .createFromTimed(track, function);
        assertThat("the new track is not null",
                newTrack, is(notNullValue()));

        List<Double> expectedNotes = Lists.newArrayList(
                2d, 2d, 2d, 2d, 2d, 2d);

        for (int index = 0; index < 6; index++) {
            assertThat(
                    "duration is correct",
                    newTrack.get(index).getDuration(),
                    is(equalTo(expectedNotes.get(index))));
        }

    }

}
