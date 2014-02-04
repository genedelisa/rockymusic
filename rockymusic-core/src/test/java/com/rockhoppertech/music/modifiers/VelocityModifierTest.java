/**
 * 
 */
package com.rockhoppertech.music.modifiers;

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
public class VelocityModifierTest {
    private static final Logger logger = LoggerFactory
            .getLogger(VelocityModifierTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.VelocityModifier#getDescription()}
     * .
     */
    @Test
    public void testGetDescription() {
        VelocityModifier modifier = new VelocityModifier();
        String actual = modifier.getDescription();
        assertThat("The value is not null.", actual, notNullValue());
        String expected = "Velocity modifier";
        assertThat("the value is correct",
                actual, equalTo(expected));
        logger.debug("actual '{}'", actual);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.VelocityModifier#getName()}.
     */
    @Test
    public void testGetName() {
        VelocityModifier modifier = new VelocityModifier();
        String actual = modifier.getName();
        assertThat("The value is not null.", actual, notNullValue());
        String expected = "Velocity";
        assertThat("the value is correct",
                actual, equalTo(expected));
        logger.debug("actual '{}'", actual);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.VelocityModifier#VelocityModifier()}
     * .
     */
    @Test
    public void testVelocityModifier() {
        VelocityModifier modifier = new VelocityModifier();
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
                    "velocity is correct",
                    note.getVelocity(),
                    is(equalTo(expected)));
        }

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.VelocityModifier#VelocityModifier(java.util.List)}
     * .
     */
    @Test
    public void testVelocityModifierListOfNumber() {
        List<Number> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        VelocityModifier modifier = new VelocityModifier(list);
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
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 1;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 0;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.VelocityModifier#VelocityModifier(java.lang.Number[])}
     * .
     */
    @Test
    public void testVelocityModifierNumberArray() {
        Number[] array = new Number[] { 0, 1 };
        VelocityModifier modifier = new VelocityModifier(array);
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
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 1;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 0;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.VelocityModifier#VelocityModifier(com.rockhoppertech.music.modifiers.Modifier.Operation, java.util.List)}
     * .
     */
    @Test
    public void testVelocityModifierOperationListOfNumber() {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // default operaiton is Operation.SET
        VelocityModifier modifier = new VelocityModifier(Operation.ADD, list);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default velocity is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        // default velocity on a note is 64;
        int expected = 64 + 1;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 64 + 2;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 64 + 1;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.VelocityModifier#VelocityModifier(com.rockhoppertech.music.modifiers.Modifier.Operation, java.lang.Number[])}
     * .
     */
    @Test
    public void testVelocityModifierOperationNumberArray() {
        Number[] array = new Number[] { 1, 2 };

        // default operaiton is Operation.SET
        VelocityModifier modifier = new VelocityModifier(Operation.ADD, array);
        assertThat("The modifier is not null.", modifier, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .build();
        assertThat("The track is not null.", track, notNullValue());
        // default velocity is 0
        track.map(modifier);
        MIDINote note = track.get(0);
        int expected = 64 + 1;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));

        note = track.get(1);
        expected = 64 + 2;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));

        note = track.get(2);
        expected = 64 + 1;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.modifiers.VelocityModifier#modify(com.rockhoppertech.music.midi.js.MIDINote)}
     * .
     */
    @Test
    public void testModify() {
        // default velocity is 1
        VelocityModifier modifier = new VelocityModifier();
        MIDINote note = new MIDINote();
        note.setVelocity(10);
        modifier.modify(note);
        logger.debug("note after {}", note);
        int expected = 1;
        assertThat(
                "velocity is correct",
                note.getVelocity(),
                is(equalTo(expected)));
    }

}
