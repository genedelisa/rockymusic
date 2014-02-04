/**
 * 
 */
package com.rockhoppertech.music.modifiers;

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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Note;
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.modifiers.Modifier.Operation;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class DurationModifierTest {
    private static final Logger logger = LoggerFactory
            .getLogger(DurationModifierTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.DurationModifier#getDescription()}
     * .
     */
    @Test
    public void testGetDescription() {
        DurationModifier modifier = new DurationModifier();
        String actual = modifier.getDescription();
        assertThat("The value is not null.", actual, notNullValue());
        String expected = "Duration modifier";
        assertThat("the value is correct",
                actual, equalTo(expected));
        logger.debug("actual '{}'", actual);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.DurationModifier#getName()}.
     */
    @Test
    public void testGetName() {
        DurationModifier modifier = new DurationModifier();
        String actual = modifier.getName();
        assertThat("The value is not null.", actual, notNullValue());
        String expected = "Duration";
        assertThat("the value is correct",
                actual, equalTo(expected));
        logger.debug("actual '{}'", actual);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.DurationModifier#DurationModifier()}
     * .
     */
    @Test
    public void testDurationModifier() {
        // default operation is Operation.SET
        // without values, the duration will be 1.0
        DurationModifier modifier = new DurationModifier();
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .durations(2d)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        track.map(modifier);
        double expected = 1d;
        for (MIDINote note : track) {
            assertThat("The note is not null.", note, notNullValue());
            assertThat(
                    "duration is correct",
                    note.getDuration(),
                    is(equalTo(expected)));
        }

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.DurationModifier#DurationModifier(java.util.List)}
     * .
     */
    @Test
    public void testDurationModifierListOfNumber() {
        List<Number> list = new ArrayList<>();
        list.add(1d);
        list.add(4d);
        DurationModifier modifier = new DurationModifier(list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .durations(5.5)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = 1d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 4d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 1d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.DurationModifier#DurationModifier(java.lang.Number[])}
     * .
     */
    @Test
    public void testDurationModifierNumberArray() {
        Number[] array = new Number[] { 1d, 4d };
        DurationModifier modifier = new DurationModifier(array);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .durations(5.5)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = 1d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 4d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 1d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.DurationModifier#DurationModifier(com.rockhoppertech.music.modifiers.Modifier.Operation, java.util.List)}
     * .
     */
    @Test
    public void testDurationModifierOperationListOfNumber() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        DurationModifier modifier = new DurationModifier(Operation.ADD, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .durations(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration + 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration + 2;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration + 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

    }
    
    @Test
    public void testDurationModifierOperationListOfNumberMultiply() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        DurationModifier modifier = new DurationModifier(Operation.MULTIPLY, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .durations(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration * 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration * 2;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration * 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

    }
    @Test
    public void testDurationModifierOperationListOfNumberSubtract() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        DurationModifier modifier = new DurationModifier(Operation.SUBTRACT, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .durations(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration - 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration - 2;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration - 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

    }
    @Test
    public void testDurationModifierOperationListOfNumberDivide() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        DurationModifier modifier = new DurationModifier(Operation.DIVIDE, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .durations(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration / 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration / 2;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration / 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

    }
    
    @Test
    public void testDurationModifierOperationListOfNumberMod() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        DurationModifier modifier = new DurationModifier(Operation.MOD, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .durations(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration % 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration % 2;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration % 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

    }
    
    @Test
    public void testDurationModifierOperationListOfNumberQuantize() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        DurationModifier modifier = new DurationModifier(Operation.QUANTIZE, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .durations(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = 5d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 4d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 5d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.DurationModifier#DurationModifier(com.rockhoppertech.music.modifiers.Modifier.Operation, java.lang.Number[])}
     * .
     */
    @Test
    public void testDurationModifierOperationNumberArray() {
        Number[] array = new Number[] { 1, 2 };
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        DurationModifier modifier = new DurationModifier(Operation.ADD, array);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .durations(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration + 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration + 2;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration + 1;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.DurationModifier#modify(com.rockhoppertech.music.Note)}
     * .
     */
    @Test
    public void testModifyNote() {
        // default duration is 1
        DurationModifier modifier = new DurationModifier();
        Note note = new MIDINote();
        note.setDuration(5.5);
        modifier.modify(note);
        logger.debug("note after {}", note);
        double expected = 1d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.DurationModifier#modify(com.rockhoppertech.music.Timed)}
     * .
     */
    @Test
    public void testModifyTimed() {
        // default duration is 1
        DurationModifier modifier = new DurationModifier();
        Timed note = new MIDINote();
        note.setDuration(5.5);
        modifier.modify(note);
        logger.debug("note after {}", note);
        double expected = 1d;
        assertThat(
                "duration is correct",
                note.getDuration(),
                is(equalTo(expected)));
    }

}
