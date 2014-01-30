/**
 * 
 */
package com.rockhoppertech.music;

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

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ScaleFactoryTest {
    private static final Logger logger = LoggerFactory
            .getLogger(ScaleFactoryTest.class);
    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createFromSpelling(java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testCreateFromSpelling() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createFromName(java.lang.String)}
     * .
     */
    @Test
    public void testCreateFromName() {
        Scale scale = null;
        scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null", scale, notNullValue());
        assertThat("the length is correct", scale.getLength(), equalTo(8));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createFromIntervals(int[])}.
     */
    @Test
    @Ignore
    public void testCreateFromIntervals() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#findBySize(int)}.
     */
    @Test
    public void testFindBySize() {
        List<Scale> scales = null;
        scales = ScaleFactory.findBySize(7);
        assertNotNull(scales);
        logger.debug("scales \n{}",scales);

        Scale scale = null;
        scale = ScaleFactory.createFromName("Major");
        assertNotNull(scale);
        assertThat(scales, hasItem(scale));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#create(int, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testCreate() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#getPitches(java.lang.String, int)}
     * .
     */
    @Test
    @Ignore
    public void testGetPitches() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#getNoteListFromScale(int[], int, int, double)}
     * .
     */
    @Test
    @Ignore
    public void testGetNoteListFromScale() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createMIDITrack(java.lang.String, int)}
     * .
     */
    @Test
    @Ignore
    public void testCreateMIDITrackIntString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createMIDITrack(java.lang.String, int, double, double)}
     * .
     */
    @Test
    @Ignore
    public void testCreateMIDITrackIntStringDoubleDouble() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createMIDITrack(com.rockhoppertech.music.Scale, double, double)}
     * .
     */
    @Test
    public void testCreateMIDITrackScaleDoubleDouble() {
        MIDITrack notelist = null;
        Scale scale = null;
        scale = ScaleFactory.createFromName("Major");
        assertNotNull(scale);
        int rootMidiNum = PitchFactory.getPitch(
                scale.getKey() + scale.getOctave()).getMidiNumber();
        assertThat(rootMidiNum, equalTo(0));
        // now test that bad boy
        notelist = ScaleFactory.createMIDITrack(scale, 1d, 4d);
        assertNotNull(notelist);
        assertThat(notelist.size(), equalTo(scale.getLength()));
        MIDINote note = notelist.get(0);
        assertNotNull(note);
        int noteMIDINumber = note.getPitch().getMidiNumber();
        assertThat(noteMIDINumber, equalTo(rootMidiNum));
        assertThat(note.getStartBeat(), equalTo(1d));
        assertThat(note.getDuration(), equalTo(4d));

        // assertThat(scales, hasItem(scale));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createMIDITrack(com.rockhoppertech.music.Scale, int)}
     * .
     */
    @Test
    @Ignore
    public void testCreateMIDITrackIntScale() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createMIDITrack(com.rockhoppertech.music.Scale, int, double, double)}
     * .
     */
    @Test
    @Ignore
    public void testCreateMIDITrackIntScaleDoubleDouble() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createMIDITrack(com.rockhoppertech.music.Scale, int, double, double, boolean, int)}
     * .
     */
    @Test
    @Ignore
    public void testCreateMIDITrackIntScaleDoubleDoubleBooleanInt() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createMIDITrackOverEntireRange(com.rockhoppertech.music.Scale)}
     * .
     */
    @Test
    @Ignore
    public void testCreateMIDITrackScale() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createMIDITrack(com.rockhoppertech.music.Scale, int, double, double, int)}
     * .
     */
    @Test
    @Ignore
    public void testCreateMIDITrackIntScaleDoubleDoubleInt() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#createMIDITrack(int[], int, double, double, int, boolean)}
     * .
     */
    @Test
    @Ignore
    public void testCreateMIDITrackIntIntArrayDoubleDoubleIntBoolean() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#getNoteListPattern(com.rockhoppertech.music.Scale, int[], int, int, int, double, boolean, double, boolean)}
     * .
     */
    @Test
    @Ignore
    public void testGetNoteListPatternScaleIntArrayIntIntIntDoubleBooleanDoubleBoolean() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#getNoteListPattern(int[], int[], int, int, int, double, boolean, double, boolean)}
     * .
     */
    @Test
    @Ignore
    public void testGetNoteListPatternIntArrayIntArrayIntIntIntDoubleBooleanDoubleBoolean() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#getScaleByKeyAndName(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testGetScaleByKeyAndName() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.ScaleFactory#getScaleByName(java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testGetScaleByName() {
        fail("Not yet implemented");
    }

}
