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
public class ChannelModifierTest {
    private static final Logger logger = LoggerFactory
            .getLogger(ChannelModifierTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.ChannelModifier#getDescription()}
     * .
     */
    @Test
    public void testGetDescription() {
        ChannelModifier modifier = new ChannelModifier();
        String actual = modifier.getDescription();
        assertThat("The value is not null.", actual, notNullValue());
        String expected = "Channel modifier changes the MIDI channel";
        assertThat("the value is correct",
                actual, equalTo(expected));
        logger.debug("actual '{}'", actual);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.ChannelModifier#getName()}.
     */
    @Test
    public void testGetName() {
        ChannelModifier modifier = new ChannelModifier();
        String actual = modifier.getName();
        assertThat("The value is not null.", actual, notNullValue());
        String expected = "Channel Modifier";
        assertThat("the value is correct",
                actual, equalTo(expected));
        logger.debug("actual '{}'", actual);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.ChannelModifier#ChannelModifier()}
     * .
     */
    @Test
    public void testChannelModifier() {
        ChannelModifier modifier = new ChannelModifier();
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .build();
        assertThat("The track is not null.", track, notNullValue());
        track.map(modifier);
        int expected = 1;
        for (MIDINote note : track) {
            assertThat("The note is not null.", note, notNullValue());
            assertThat(
                    "channel is correct",
                    note.getChannel(),
                    is(equalTo(expected)));
        }

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.ChannelModifier#ChannelModifier(java.util.List)}
     * .
     */
    @Test
    public void testChannelModifierListOfNumber() {
        List<Number> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        ChannelModifier modifier = new ChannelModifier(list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .build();
        assertThat("The track is not null.", track, notNullValue());
        track.map(modifier);
        MIDINote note = track.get(0);
        int expected = 0;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 1;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 0;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.ChannelModifier#ChannelModifier(java.lang.Number[])}
     * .
     */
    @Test
    public void testChannelModifierNumberArray() {
        Number[] array = new Number[] { 0, 1 };
        ChannelModifier modifier = new ChannelModifier(array);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .build();
        assertThat("The track is not null.", track, notNullValue());
        track.map(modifier);
        MIDINote note = track.get(0);
        int expected = 0;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 1;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 0;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.ChannelModifier#ChannelModifier(com.rockhoppertech.music.modifiers.Modifier.Operation, java.util.List)}
     * .
     */
    @Test
    public void testChannelModifierOperationListOfNumber() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        ChannelModifier modifier = new ChannelModifier(Operation.ADD, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        int expected = 1;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 2;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 1;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.ChannelModifier#ChannelModifier(com.rockhoppertech.music.modifiers.Modifier.Operation, java.lang.Number[])}
     * .
     */
    @Test
    public void testChannelModifierOperationNumberArray() {
        Number[] array = new Number[] { 1, 2 };

        // default operaiton is Operation.SET
        ChannelModifier modifier = new ChannelModifier(Operation.ADD, array);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default channel is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        int expected = 1;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 2;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 1;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.ChannelModifier#modify(com.rockhoppertech.music.midi.js.MIDINote)}
     * .
     */
    @Test
    public void testModify() {
        // default channel is 1
        ChannelModifier modifier = new ChannelModifier();
        MIDINote note = new MIDINote();
        note.setChannel(10);
        modifier.modify(note);
        logger.debug("note after {}", note);
        int expected = 1;
        assertThat(
                "channel is correct",
                note.getChannel(),
                is(equalTo(expected)));
    }

}
