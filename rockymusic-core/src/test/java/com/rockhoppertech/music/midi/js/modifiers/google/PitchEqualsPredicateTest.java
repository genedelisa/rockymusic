package com.rockhoppertech.music.midi.js.modifiers.google;

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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class PitchEqualsPredicateTest {
    static Logger logger = LoggerFactory
            .getLogger(PitchEqualsPredicateTest.class);

    MIDITrack track;

    @Before
    public void setUp() {
        track = MIDITrackBuilder.create()
                .noteString("C5 CS F FS E F4")
                .build();
    }

    @Test
    public final void testApply() {
        PitchEqualsPredicate p = new PitchEqualsPredicate(
                Pitch.C5);
        boolean actual = p.apply(new MIDINote(Pitch.C5));
        boolean expected = true;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        actual = p.apply(new MIDINote(Pitch.C4));
        expected = false;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public final void trackFactory() {
        PitchEqualsPredicate p = new PitchEqualsPredicate(
                Pitch.C5);
        MIDITrack newTrack = MIDITrackFactory
                .createTrackFromPredicate(track, p);
        assertThat("the new track is not null",
                newTrack, is(notNullValue()));

        List<MIDINote> expectedNotes = Lists.newArrayList(
                new MIDINote(Pitch.C5),
                new MIDINote(Pitch.C5));

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