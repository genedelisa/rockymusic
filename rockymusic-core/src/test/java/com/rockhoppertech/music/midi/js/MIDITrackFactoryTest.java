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

import static com.rockhoppertech.music.Pitch.C5;
import static com.rockhoppertech.music.Pitch.D5;
import static com.rockhoppertech.music.Pitch.E5;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.collections.CircularList;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.scale.ScaleBuilder;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDITrackFactoryTest {
    private static final Logger logger = LoggerFactory
            .getLogger(MIDITrackFactoryTest.class);

    /**
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createRepeatedWithMask() {
        MIDITrack track = new MIDITrack("C D E f g a b");
        CircularList<Integer> mask = new CircularArrayList<Integer>();
        mask.add(2);
        mask.add(1);
        MIDITrack repeated = MIDITrackFactory.createRepeated(track,
                mask);
        logger.debug("repeated {}", repeated);
        MIDITrack expected = new MIDITrack("c c d e e f g g a b b");
        expected.sequential();

        int expectedSize = 11;
        assertThat("repeated is not null", repeated, notNullValue());
        assertThat("repeated size is correct", repeated.size(),
                equalTo(expectedSize));

        // TODO determine why this doesn't work
        // assertThat("repeated matches", repeated, equalTo(expected));

        MIDINote note = repeated.get(0);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(C5));
        note = repeated.get(1);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(C5));
        note = repeated.get(2);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(D5));
        note = repeated.get(3);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E5));
        note = repeated.get(4);
        assertThat("pitch is correct", note.getMidiNumber(),
                equalTo(E5));
    }

    /**
     */
    @Test
    public void repeats() {

        int numberOfRepeats = 3;
        MIDITrack track = new MIDITrack("C D E");
        MIDITrack repeats = MIDITrackFactory.repeat(track,
                numberOfRepeats);
        int expectedSize = track.size() * numberOfRepeats;
        logger.debug("repeats {}", repeats);
        assertThat("track is not null", repeats, notNullValue());
        assertThat("track is not null", repeats.size(),
                equalTo(expectedSize));

        MIDINote note = repeats.get(0);
        Pitch expectedPitch = PitchFactory.getPitch("C");
        assertThat("note is not null", note, notNullValue());
        assertThat(String.format("note pitch is '%s'", expectedPitch), note
                .getPitch(), equalTo(expectedPitch));

        note = repeats.get(1);
        expectedPitch = PitchFactory.getPitch("D");
        assertThat("note is not null", note, notNullValue());
        assertThat(String.format("note pitch is '%s'", expectedPitch), note
                .getPitch(), equalTo(expectedPitch));

        note = repeats.get(2);
        expectedPitch = PitchFactory.getPitch("E");
        assertThat("note is not null", note, notNullValue());
        assertThat(String.format("note pitch is '%s'", expectedPitch), note
                .getPitch(), equalTo(expectedPitch));

        note = repeats.get(3);
        expectedPitch = PitchFactory.getPitch("C");
        assertThat("note is not null", note, notNullValue());
        assertThat(String.format("note pitch is '%s'", expectedPitch), note
                .getPitch(), equalTo(expectedPitch));

        note = repeats.get(4);
        expectedPitch = PitchFactory.getPitch("D");
        assertThat("note is not null", note, notNullValue());
        assertThat(String.format("note pitch is '%s'", expectedPitch), note
                .getPitch(), equalTo(expectedPitch));

        note = repeats.get(5);
        expectedPitch = PitchFactory.getPitch("E");
        assertThat("note is not null", note, notNullValue());
        assertThat(String.format("note pitch is '%s'", expectedPitch), note
                .getPitch(), equalTo(expectedPitch));

    }

    @Test
    @Ignore
    public void createFromTrack() {
        fail("Not yet implemented");
        // you need to get a Sequence to get a Track.
    }

    @Test
    public void createFromPitches() {
        MIDITrack track = MIDITrackFactory.createFromPitches(C5, D5,
                E5);
        logger.debug("track:\n{}", track);
        int expectedSize = 3;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));

    }

    @Test
    public void createFromString() {
        MIDITrack track = MIDITrackFactory.createFromString("c d e");
        logger.debug("track:\n{}", track);
        int expectedSize = 3;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));
    }

    @Test
    public void createFromIntervalsIntArray() {
        MIDITrack track = MIDITrackFactory.createFromIntervals(1, 2);
        logger.debug("track:\n{}", track);
        int expectedSize = 3;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));
    }

    @Test
    public void createFromIntervalsPitchIntArray() {
        MIDITrack track = MIDITrackFactory.createFromIntervals(
                PitchFactory.getPitch(C5), 1, 2);
        logger.debug("track:\n{}", track);
        int expectedSize = 3;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));

    }

    @Test
    public void createFromIntervalsIntArrayInt() {
        int[] a = { 1, 2 };
        int baseInt = C5;
        MIDITrack track = MIDITrackFactory.createFromIntervals(a,
                baseInt);
        logger.debug("track:\n{}", track);
        int expectedSize = 3;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));
    }

    @Test
    public void createFromIntervalsIntArrayIntIntBooleanInt() {
        int[] a = { 1, 2 };
        int baseInt = C5;
        int unit = 1;
        boolean absolute = false;
        int numOctaves = 1;
        MIDITrack track = MIDITrackFactory.createFromIntervals(a,
                baseInt, unit, absolute, numOctaves);
        logger.debug("track:\n{}", track);
        int expectedSize = 3;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));
    }

    @Test
    public void createFromIntervalsIntArrayIntIntBoolean() {
        int[] a = { 1, 2 };
        int baseInt = C5;
        int unit = 1;
        boolean absolute = false;
        MIDITrack track = MIDITrackFactory.createFromIntervals(a,
                baseInt, unit, absolute);
        logger.debug("track:\n{}", track);
        int expectedSize = 3;
        assertThat("track is not null", track, notNullValue());
        assertThat("track size is correct", track.size(),
                equalTo(expectedSize));
    }

    @Test
    public void applyPatternListDefaults() {
        MIDITrack track = ScaleBuilder.create()
                .name("Major")
                .root(Pitch.C5)
                .track()
                .sequential();
        logger.debug("track:\n", track);
        assertThat("track is not null", track, notNullValue());
        // Staffer.showGrandStaff(track, "original");

        // Integer[] pattern = new Integer[] { 0, 0, 3, 2, 1 };
        List<Integer> patternList = new ArrayList<Integer>();
        patternList.add(0);
        patternList.add(0);
        patternList.add(3);
        patternList.add(2);
        patternList.add(1);
        int startingMIDINumber = Pitch.C6;
        int nOctaves = 1;
        boolean reverse = false;
        boolean upAndDown = false;
        int expectedSize = 5;
        MIDITrack patterned = null;
        patterned = MIDITrackFactory.applyPattern(track, patternList,
                startingMIDINumber, nOctaves, reverse, upAndDown).sequential();
        assertThat("patterned is not null", patterned, notNullValue());
        assertThat("patterned size is correct", patterned.size(),
                equalTo(expectedSize));

        int trans = startingMIDINumber - track.get(0).getMidiNumber();
        for (int i = 0; i < patternList.size(); i++) {
            assertThat("startingMIDINumber is correct " + i, patterned.get(i)
                    .getMidiNumber(), equalTo(trans
                    + track.get(patternList.get(i)).getMidiNumber()));
        }

        logger.debug("patterned {}", patterned);
        // Staffer.showGrandStaff(patterned, "patterned updown");
        //
        // patterned = MIDITrackFactory.applyPattern(track, pattern,
        // Pitch.C6, 1, true, false).sequential();
        // Staffer.showGrandStaff(patterned, "patterned reverse");
        // System.err.println(patterned);

    }
}
