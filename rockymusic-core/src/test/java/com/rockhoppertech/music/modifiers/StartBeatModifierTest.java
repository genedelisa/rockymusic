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
public class StartBeatModifierTest {
    private static final Logger logger = LoggerFactory
            .getLogger(StartBeatModifierTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.StartBeatModifier#getDescription()}
     * .
     */
    @Test
    public void testGetDescription() {
        StartBeatModifier modifier = new StartBeatModifier();
        String actual = modifier.getDescription();
        assertThat("The value is not null.", actual, notNullValue());
        String expected = "Start Beat modifier";
        assertThat("the value is correct",
                actual, equalTo(expected));
        logger.debug("actual '{}'", actual);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.StartBeatModifier#getName()}.
     */
    @Test
    public void testGetName() {
        StartBeatModifier modifier = new StartBeatModifier();
        String actual = modifier.getName();
        assertThat("The value is not null.", actual, notNullValue());
        String expected = "Start Beat";
        assertThat("the value is correct",
                actual, equalTo(expected));
        logger.debug("actual '{}'", actual);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.StartBeatModifier#StartBeatModifier()}
     * .
     */
    @Test
    public void testStartBeatModifier() {
        // default operation is Operation.SET
        // without values, the startBeat will be 1.0
        StartBeatModifier modifier = new StartBeatModifier();
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .startBeat(2d)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        track.map(modifier);
        double expected = 1d;
        for (MIDINote note : track) {
            assertThat("The note is not null.", note, notNullValue());
            assertThat(
                    "startBeat is correct",
                    note.getStartBeat(),
                    is(equalTo(expected)));
        }

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.StartBeatModifier#StartBeatModifier(java.util.List)}
     * .
     */
    @Test
    public void testStartBeatModifierListOfNumber() {
        List<Number> list = new ArrayList<>();
        list.add(1d);
        list.add(4d);
        StartBeatModifier modifier = new StartBeatModifier(list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .startBeat(5.5)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = 1d;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 4d;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 1d;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.StartBeatModifier#StartBeatModifier(java.lang.Number[])}
     * .
     */
    @Test
    public void testStartBeatModifierNumberArray() {
        Number[] array = new Number[] { 1d, 4d };
        StartBeatModifier modifier = new StartBeatModifier(array);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .startBeat(5.5)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = 1d;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 4d;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 1d;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.StartBeatModifier#StartBeatModifier(com.rockhoppertech.music.modifiers.Modifier.Operation, java.util.List)}
     * .
     */
    @Test
    public void testStartBeatModifierOperationListOfNumber() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        StartBeatModifier modifier = new StartBeatModifier(Operation.ADD, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .startBeat(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration + 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration + 2;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration + 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

    }
    
    @Test
    public void testStartBeatModifierOperationListOfNumberMultiply() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        StartBeatModifier modifier = new StartBeatModifier(Operation.MULTIPLY, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .startBeat(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration * 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration * 2;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration * 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

    }
    @Test
    public void testStartBeatModifierOperationListOfNumberSubtract() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        StartBeatModifier modifier = new StartBeatModifier(Operation.SUBTRACT, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .startBeat(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration - 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration - 2;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration - 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

    }
    @Test
    public void testStartBeatModifierOperationListOfNumberDivide() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        StartBeatModifier modifier = new StartBeatModifier(Operation.DIVIDE, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .startBeat(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration / 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration / 2;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration / 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

    }
    
    @Test
    public void testStartBeatModifierOperationListOfNumberMod() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        StartBeatModifier modifier = new StartBeatModifier(Operation.MOD, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .startBeat(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        // rounds to 1
        double expected = 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration % 2;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

    }
    
    @Test
    public void testStartBeatModifierOperationListOfNumberQuantize() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        StartBeatModifier modifier = new StartBeatModifier(Operation.QUANTIZE, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .startBeat(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = 5d;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 4d;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 5d;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.StartBeatModifier#StartBeatModifier(com.rockhoppertech.music.modifiers.Modifier.Operation, java.lang.Number[])}
     * .
     */
    @Test
    public void testStartBeatModifierOperationNumberArray() {
        Number[] array = new Number[] { 1, 2 };
        // default operaiton is Operation.SET
        double defaultDuration = 5.5;
        StartBeatModifier modifier = new StartBeatModifier(Operation.ADD, array);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .startBeat(defaultDuration)
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        double expected = defaultDuration + 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = defaultDuration + 2;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = defaultDuration + 1;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.StartBeatModifier#modify(com.rockhoppertech.music.Note)}
     * .
     */
    @Test
    public void testModifyNote() {
        // default startBeat is 1
        StartBeatModifier modifier = new StartBeatModifier();
        Note note = new MIDINote();
        note.setStartBeat(5.5);
        modifier.modify(note);
        logger.debug("note after {}", note);
        double expected = 1d;
        assertThat(
                "start beat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.StartBeatModifier#modify(com.rockhoppertech.music.Timed)}
     * .
     */
    @Test
    public void testModifyTimed() {
        // default startBeat is 1
        StartBeatModifier modifier = new StartBeatModifier();
        Timed note = new MIDINote();
        note.setStartBeat(5.5);
        modifier.modify(note);
        logger.debug("note after {}", note);
        double expected = 1d;
        assertThat(
                "startBeat is correct",
                note.getStartBeat(),
                is(equalTo(expected)));
    }

}
