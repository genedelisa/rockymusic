package com.rockhoppertech.music.midi.js.modifiers.google;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
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
public class MIDINumberBandPassPredicateTest {
    static Logger logger = LoggerFactory
            .getLogger(MIDINumberBandPassPredicateTest.class);

    MIDITrack track;

    @Before
    public void setUp() {
        track = MIDITrackBuilder.create()
                .noteString("C5 CS F FS E F4")
                .build();
    }

    @Test
    public final void testApply() {
        MIDINumberBandPassPredicate p = new MIDINumberBandPassPredicate(
                Pitch.C5, Pitch.E5);
        boolean actual = p.apply(new MIDINote(Pitch.C5));
        boolean expected = true;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        actual = p.apply(new MIDINote(Pitch.C4));
        expected = false;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        List<MIDINote> notes = Lists.newArrayList(Iterables.filter(track, p));
        List<MIDINote> expectedNotes = Lists.newArrayList(
                new MIDINote(Pitch.C5),
                new MIDINote(Pitch.CS5),
                new MIDINote(Pitch.E5));

        logger.debug("notes {}", notes);
        assertThat("notes correct",
                notes, is(equalTo(expectedNotes)));
    }

    @Test
    public final void open() {
        MIDINumberBandPassPredicate p = new MIDINumberBandPassPredicate(
                Pitch.C5, Pitch.E5);
        p.useOpenRange();

        // open range does not include the bounds
        boolean actual = p.apply(new MIDINote(Pitch.C5));
        boolean expected = false;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));
        actual = p.apply(new MIDINote(Pitch.E5));
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        actual = p.apply(new MIDINote(Pitch.CS5));
        expected = true;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));
        actual = p.apply(new MIDINote(Pitch.F4));
        expected = false;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));
    }

    @Test
    public final void closed() {
        MIDINumberBandPassPredicate p = new MIDINumberBandPassPredicate(
                Pitch.C5, Pitch.E5);
        p.useClosedRange();
        boolean actual = p.apply(new MIDINote(Pitch.C5));
        boolean expected = true;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));
        actual = p.apply(new MIDINote(Pitch.E5));
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));

        actual = p.apply(new MIDINote(Pitch.F4));
        expected = false;
        assertThat("pitch is correct",
                actual, is(equalTo(expected)));
    }

    @Test
    public final void track() {
        MIDINumberBandPassPredicate p = new MIDINumberBandPassPredicate(
                Pitch.C5, Pitch.E5);
        MIDITrack newTrack = new MIDITrack(Iterables.filter(track, p));
        assertThat("the new track is not null",
                newTrack, is(notNullValue()));

        List<MIDINote> expectedNotes = Lists.newArrayList(
                new MIDINote(Pitch.C5),
                new MIDINote(Pitch.CS5),
                new MIDINote(Pitch.E5));

        assertThat("the track's notes are correct",
                newTrack.getNotes(), is(equalTo(expectedNotes)));
    }

    @Test
    public final void trackFactory() {
        MIDINumberBandPassPredicate p = new MIDINumberBandPassPredicate(
                Pitch.C5, Pitch.E5);
        MIDITrack newTrack = MIDITrackFactory
                .createTrackFromPredicate(track, p);
        assertThat("the new track is not null",
                newTrack, is(notNullValue()));

        List<MIDINote> expectedNotes = Lists.newArrayList(
                new MIDINote(Pitch.C5),
                new MIDINote(Pitch.CS5),
                new MIDINote(Pitch.E5));

        assertThat("the track's notes are correct",
                newTrack.getNotes(), is(equalTo(expectedNotes)));
    }

}
