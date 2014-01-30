package com.rockhoppertech.music.chord;

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

/**
 * 
 */

import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Interval;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author edelisa
 * 
 */
public class ChordTest {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordTest.class);

    // ignoring constructor tests so far

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#Chord(int, int[], java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testChordIntIntArrayString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#Chord(int, java.lang.String, int[], java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testChordIntStringIntArrayString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#Chord(java.lang.String, int[], java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testChordStringIntArrayString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#Chord(java.lang.String, java.lang.Integer[], java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testChordStringIntegerArrayString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#Chord(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testChordStringStringString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getDefaultVoicingString()}.
     */
    @Test
    public void testGetDefaultVoicing() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        String cv = c.getChordVoicing();
        ;
        assertThat("chord voicing is not null",
                cv, is(notNullValue()));
        assertThat(c.isMajor(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(false));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.C0));
        assertThat(c.getThird(), equalTo(Pitch.E0));
        assertThat(c.getFifth(), equalTo(Pitch.G0));
        // assertThat(c.getSeventh(), equalTo(Pitch.B0));
        assertThat(c.getSymbol(), equalTo("maj"));
        String expected = "1 3 5";
        System.out.printf("'%s' and '%s'\n", cv, expected);
        assertThat(cv.trim(), equalTo(expected));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#intervalToChordDegree(int)}.
     */
    @Test
    public void testIntervalToChordDegree() {
        assertThat(Chord.intervalToChordDegree(0), equalTo(1));
        assertThat(Chord.intervalToChordDegree(1), equalTo(1));
        assertThat(Chord.intervalToChordDegree(2), equalTo(9));
        assertThat(Chord.intervalToChordDegree(3), equalTo(3));
        assertThat(Chord.intervalToChordDegree(4), equalTo(3));
        assertThat(Chord.intervalToChordDegree(5), equalTo(11));
        assertThat(Chord.intervalToChordDegree(6), equalTo(11));
        assertThat(Chord.intervalToChordDegree(7), equalTo(5));
        assertThat(Chord.intervalToChordDegree(8), equalTo(5));
        assertThat(Chord.intervalToChordDegree(9), equalTo(13));
        assertThat(Chord.intervalToChordDegree(10), equalTo(7));
        assertThat(Chord.intervalToChordDegree(11), equalTo(7));
        assertThat(Chord.intervalToChordDegree(12), equalTo(1));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#setClosed()}.
     */
    @Test
    public void testSetClosed() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        c.setClosed();
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getChordVoicing()}.
     */
    @Test
    public void testGetChordVoicing() {
        Chord c = null;
        c = ChordFactory.getChordByFullSymbol("C");

        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat("chord  voicing is not null",
                c.getChordVoicing(), is(notNullValue()));

        for (Chord chord : ChordFactory.getAll()) {
            assertThat("chord  voicing is not null",
                    chord.getChordVoicing(), is(notNullValue()));
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#setChordVoicing(com.rockhoppertech.music.chord.ChordVoicing)}
     * .
     */
    @Test
    public void testSetChordVoicing() {
        Chord c = null;
        c = ChordFactory.getChordByFullSymbol("C");

        assertThat("chord is not null",
                c, is(notNullValue()));
        String chordVoicing = "3 5 +1";
        c.setChordVoicing(chordVoicing);
        assertThat("voicing matches",
                c.getChordVoicing(), is(equalTo(chordVoicing)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getDisplayName()}.
     */
    @Test
    public void testGetDisplayName() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getDisplayName(), equalTo("C"));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getDisplayNameWithAliases()}.
     */
    @Test
    @Ignore
    public void testGetDisplayNameWithAliases() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getNumberOfInversions()}.
     */
    @Test
    public void testGetNumberOfInversions() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getNumberOfInversions(), equalTo(3));
        try {
            c = ChordFactory.getChordByFullSymbol("C7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getNumberOfInversions(), equalTo(4));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#getSize()}.
     */
    @Test
    public void testGetSize() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getSize(), equalTo(3));
        try {
            c = ChordFactory.getChordByFullSymbol("C7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getSize(), equalTo(4));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#setRoot(int)}
     * .
     */
    @Test
    public void testSetRoot() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        c.setRoot(Pitch.C5);
        assertThat(c.getRoot(), equalTo(Pitch.C5));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getDuration()}.
     */
    @Test
    public void testGetDuration() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getDuration(), equalTo(4d));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#setDuration(double)}.
     */
    @Test
    public void testSetDuration() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        c.setDuration(1d);
        assertThat(c.getDuration(), equalTo(1d));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#setRootAndDuration(int, double)}
     * .
     */
    @Test
    public void testSetRootAndDuration() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        c.setRootAndDuration(Pitch.EF4, 8d);
        assertThat(c.getDuration(), equalTo(8d));
        assertThat(c.getRoot(), equalTo(Pitch.EF4));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#createMIDITrack()}.
     */
    @Test
    public void testGetNotelist() {
        Chord c = null;
        c = ChordFactory.getChordByFullSymbol("C");
        assertThat("chord is not null",
                c, is(notNullValue()));
        MIDITrack notelist = null;
        notelist = c.createMIDITrack();
        assertThat("track is not null",
                notelist, is(notNullValue()));
        assertThat(notelist.size(), equalTo(3));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getVoicedNotelist()}.
     */
    @Test
    public void testGetVoicedNotelist() {
        Chord c = null;
        c = ChordFactory.getChordByFullSymbol("C");
        assertThat("chord is not null",
                c, is(notNullValue()));
        MIDITrack notelist = null;
        notelist = c.createMIDITrack();
        assertThat("track is not null",
                notelist, is(notNullValue()));
        assertThat(notelist.size(), equalTo(3));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#appendNoteLists(com.rockhoppertech.music.chord.Chord)}
     * .
     */
    @Test
    @Ignore
    public void testAppendNoteLists() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        Chord c2 = null;
        try {
            c2 = ChordFactory.getChordByFullSymbol("G7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("G7 chord is not null",
                c2, is(notNullValue()));
        // MIDITrack appended = c.appendNoteLists(c2);
        // assertNotNull(appended);

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#appendToNoteList(com.rockhoppertech.music.midi.js.MIDITrack)}
     * .
     */
    @Test
    @Ignore
    public void testAppendToNoteList() {
        Chord c = null;

        c = ChordFactory.getChordByFullSymbol("C");
        assertThat("chord is not null",
                c, is(notNullValue()));

        Chord c2 = null;
        c2 = ChordFactory.getChordByFullSymbol("G7");
        assertThat("G7 chord is not null",
                c2,
                is(notNullValue()));

        MIDITrack notelist = c2.createMIDITrack();
        assertThat("track is not null",
                notelist,
                is(notNullValue()));

        // MIDITrack appended = c.appendToNoteList(notelist);
        // assertNotNull(appended);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getInversion()}.
     */
    @Test
    public void testGetInversion() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));

        Chord c2 = null;
        try {
            c2 = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        logger.debug("identity hash {}", System.identityHashCode(c));
        logger.debug("identity hash {}", System.identityHashCode(c2));
        logger.debug("c == c2", c == c2);
        assertThat(c.getInversion(), equalTo(c2.getInversion()));

        assertThat(c.getInversion(), equalTo(0));
        c.setInversion(1);
        assertThat(c.getInversion(), equalTo(1));
        assertThat(c.getInversion(), not(equalTo(c2.getInversion())));
        c.setInversion(2);
        assertThat(c.getInversion(), equalTo(2));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#setInversion(int)}.
     */
    @Test
    @Ignore
    public void testSetInversion() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        String cv = c.getChordVoicing();
        assertThat("chord voicing is not null",
                cv, is(notNullValue()));
        assertThat(c.isMajor(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(false));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.C0));
        assertThat(c.getThird(), equalTo(Pitch.E0));
        assertThat(c.getFifth(), equalTo(Pitch.G0));
        // assertThat(c.getSeventh(), equalTo(Pitch.B0));
        assertThat(c.getSymbol(), equalTo("maj"));
        // no inversion
        MIDITrack list = null;
        c.setInversion(0);
        String expected = "1 3 5";
        System.out.printf("'%s' and '%s'\n", cv, expected);
        assertThat(cv.trim(), equalTo(expected));
        list = c.createMIDITrack();
        assertThat("track is not null",
                list, is(notNullValue()));
        assertThat(list.get(0).getMidiNumber(), equalTo(Pitch.C0));
        assertThat(list.get(1).getMidiNumber(), equalTo(Pitch.E0));
        assertThat(list.get(2).getMidiNumber(), equalTo(Pitch.G0));

        c.setInversion(1);
        expected = "3 5 +1";
        assertThat(c.getChordVoicing().trim(), equalTo(expected));
        list = c.createMIDITrack();
        assertThat("track is not null",
                list, is(notNullValue()));
        assertThat(list.get(0).getMidiNumber(), equalTo(Pitch.E0));
        assertThat(list.get(1).getMidiNumber(), equalTo(Pitch.G0));
        assertThat(list.get(2).getMidiNumber(), equalTo(Pitch.C1));

        c.setInversion(2);
        expected = "5 +1 +3";
        assertThat(c.getChordVoicing().trim(), equalTo(expected));
        list = c.createMIDITrack();
        assertThat("track is not null",
                list, is(notNullValue()));
        assertThat(list.get(0).getMidiNumber(), equalTo(Pitch.G0));
        assertThat(list.get(1).getMidiNumber(), equalTo(Pitch.C1));
        assertThat(list.get(2).getMidiNumber(), equalTo(Pitch.E1));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getStartBeat()}.
     */
    @Test
    public void testGetStartBeat() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getStartBeat(), equalTo(1d));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#setStartBeat(double)}.
     */
    @Test
    public void testSetStartBeat() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getStartBeat(), equalTo(1d));
        double startBeat = 2.5;
        c.setStartBeat(startBeat);
        assertThat(c.getStartBeat(), equalTo(startBeat));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#getSymbol()}.
     */
    @Test
    public void testGetSymbol() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getSymbol(), equalTo("maj"));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#setSymbol(java.lang.String)}.
     */
    @Test
    public void testSetSymbol() {
        MIDITrack list = null;
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        // the root will be in the 0th octave
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getSymbol(), equalTo("maj"));
        list = c.createMIDITrack();
        assertThat("track is not null",
                list, is(notNullValue()));
        // assertThat(c.getInversion(), not(equalTo(c2.getInversion())));
        logger.debug("Voiced notelist");
        logger.debug("chord {}", c);
        logger.debug(c.getChordVoicing());
        logger.debug("list {}", list);
        assertThat(list.size(), equalTo(3));
        assertThat(list.get(0).getMidiNumber(), equalTo(Pitch.C0));
        assertThat(list.get(1).getMidiNumber(), equalTo(Pitch.E0));
        assertThat(list.get(2).getMidiNumber(), equalTo(Pitch.G0));
        // setting the symbol essentially creates a new chord.
        c.setSymbol("maj7");
        assertThat(c.getSymbol(), equalTo("maj7"));
        list = c.createMIDITrack();
        assertThat("track is not null",
                list, is(notNullValue()));
        assertThat(list.size(), equalTo(4));
        assertThat(list.get(0).getMidiNumber(), equalTo(Pitch.C0));
        assertThat(list.get(1).getMidiNumber(), equalTo(Pitch.E0));
        assertThat(list.get(2).getMidiNumber(), equalTo(Pitch.G0));
        assertThat(list.get(3).getMidiNumber(), equalTo(Pitch.B0));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getIntervals()}.
     */
    @Test
    public void testGetIntervals() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        int[] intervals = c.getIntervals();
        assertThat("intervals is not null",
                intervals, is(notNullValue()));
        int[] expected = new int[] { 4, 7 };
        assertArrayEquals(expected, intervals);

        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        intervals = c.getIntervals();
        expected = new int[] { 4, 7, 11 };
        assertArrayEquals(expected, intervals);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getIntervalsAsString()}.
     */
    @Test
    public void testGetIntervalsAsString() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        // int[] intervals = c.getIntervals();
        // assertNotNull(intervals);
        String expected = String.format("%d %d", 4, 7);
        assertThat(c.getIntervalstoString(), equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getMaxInterval()}.
     */
    @Test
    public void testGetMaxInterval() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        int expected = 7;
        assertThat(c.getMaxInterval(), equalTo(expected));

        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        expected = 11;
        assertThat(c.getMaxInterval(), equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#setIntervals(int[])}.
     */
    @Test
    @Ignore
    public void testSetIntervals() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getSpelling()}.
     */
    @Test
    public void testGetSpelling() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        String expected = "1 3 5";
        assertThat(c.getSpelling(), equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#setOctave(int)}.
     */
    @Test
    public void testSetOctave() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        c.setOctave(5);
        assertThat(c.getRoot(), equalTo(Pitch.C5));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#pitchToChordDegree()}.
     */
    @Test
    public void pitchToChordDegree() {
        Chord c = null;
        c = ChordFactory.getChordByFullSymbol("C");
        assertThat("chord is not null",
                c, is(notNullValue()));
        int degree = 0;
        degree = c.pitchToChordDegree(Pitch.C5);
        assertThat(degree, equalTo(1));
        degree = c.pitchToChordDegree(Pitch.CS5);
        assertThat(degree, equalTo(-1));
        degree = c.pitchToChordDegree(Pitch.D5);
        assertThat(degree, equalTo(-1));
        degree = c.pitchToChordDegree(Pitch.EF5);
        assertThat(degree, equalTo(-1));
        degree = c.pitchToChordDegree(Pitch.E5);
        assertThat(degree, equalTo(3));
        degree = c.pitchToChordDegree(Pitch.F5);
        assertThat(degree, equalTo(-1));
        degree = c.pitchToChordDegree(Pitch.FS5);
        assertThat(degree, equalTo(-1));
        degree = c.pitchToChordDegree(Pitch.G5);
        assertThat(degree, equalTo(5));
        degree = c.pitchToChordDegree(Pitch.GS5);
        assertThat(degree, equalTo(-1));
        degree = c.pitchToChordDegree(Pitch.A5);
        assertThat(degree, equalTo(-1));
        degree = c.pitchToChordDegree(Pitch.BF5);
        assertThat(degree, equalTo(-1));
        degree = c.pitchToChordDegree(Pitch.B5);
        assertThat(degree, equalTo(-1));
        degree = c.pitchToChordDegree(Pitch.C6);
        assertThat(degree, equalTo(1));

    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#setDrop(int)}
     * .
     */
    @Test
    @Ignore
    public void testSetDrop() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#getDrop()}.
     */
    @Test
    @Ignore
    public void testGetDrop() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#isMajor()}.
     */
    @Test
    public void testIsMajor() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.isMajor(), equalTo(true));
        assertThat(c.isMinor(), equalTo(false));

        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.isMajor(), equalTo(true));
        assertThat(c.isMinor(), equalTo(false));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#isMinor()}.
     */
    @Test
    public void testIsMinor() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cm");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.isMajor(), equalTo(false));
        assertThat(c.isMinor(), equalTo(true));

        try {
            c = ChordFactory.getChordByFullSymbol("Cm7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.isMajor(), equalTo(false));
        assertThat(c.isMinor(), equalTo(true));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#isDiminished()}.
     */
    @Test
    public void testIsDiminished() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cdim");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.isDiminished(), equalTo(true));
        assertThat(c.isMajor(), equalTo(false));
        assertThat(c.isMinor(), equalTo(true));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#isDominant()}
     * .
     */
    @Test
    public void testIsDominant() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.isDominant(), equalTo(true));
        assertThat(c.isMajor(), equalTo(true));
        assertThat(c.isMinor(), equalTo(false));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#isDiatonic(int[])}.
     */
    @Test
    public void testIsDiatonic() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        // essentially "are the intervals exactly the same"?
        int[] array = new int[] { 4, 7 };
        assertThat(c.isDiatonic(array), equalTo(true));
        array = new int[] { 4 };
        assertThat(c.isDiatonic(array), equalTo(false));
        array = new int[] { 7 };
        assertThat(c.isDiatonic(array), equalTo(false));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getVoicingString(com.rockhoppertech.music.midi.js.MIDITrack)}
     * .
     */
    @Test
    public void testGetVoicingString() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj13");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));

        MIDITrack notelist = c.createMIDITrack();
        assertThat(c.getVoicingString(notelist), equalTo("1 3 5 7 +9 +11 +13"));

        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.getVoicingString(c.createMIDITrack()), equalTo("1 3 5"));

        try {
            c = ChordFactory.getChordByFullSymbol("C9");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(
                c.getVoicingString(c.createMIDITrack()),
                equalTo("1 3 5 7 +9"));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getChordDegree(int)}.
     */
    @Test
    public void testGetChordDegree() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj13");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        int expected = c.getRoot();
        assertThat(c.getChordDegree(1), equalTo(expected));
        expected = c.getRoot() + Interval.MAJOR_THIRD;
        assertThat(c.getChordDegree(3), equalTo(expected));
        expected = c.getRoot() + Interval.PERFECT_FIFTH;
        assertThat(c.getChordDegree(5), equalTo(expected));
        expected = c.getRoot() + Interval.MAJOR_SEVENTH;
        assertThat(c.getChordDegree(7), equalTo(expected));
        expected = c.getRoot() + 14;
        assertThat(c.getChordDegree(9), equalTo(expected));
        expected = c.getRoot() + 17;
        assertThat(c.getChordDegree(11), equalTo(expected));
        expected = c.getRoot() + 21;
        assertThat(c.getChordDegree(13), equalTo(expected));

    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#getThird()}.
     */
    @Test
    public void testGetThird() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj13");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        int expected = c.getRoot() + Interval.MAJOR_THIRD;
        assertThat(c.getThird(), equalTo(expected));

        try {
            c = ChordFactory.getChordByFullSymbol("Cm");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        expected = c.getRoot() + Interval.MINOR_THIRD;
        assertThat(c.getThird(), equalTo(expected));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#getFifth()}.
     */
    @Test
    public void testGetFifth() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj13");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        int expected = c.getRoot() + Interval.PERFECT_FIFTH;
        assertThat(c.getFifth(), equalTo(expected));

        try {
            c = ChordFactory.getChordByFullSymbol("Cm");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        expected = c.getRoot() + Interval.PERFECT_FIFTH;
        assertThat(c.getFifth(), equalTo(expected));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#getSeventh()}
     * .
     */
    @Test
    public void testGetSeventh() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        int expected = c.getRoot() + Interval.MAJOR_SEVENTH;
        assertThat(c.getSeventh(), equalTo(expected));

        try {
            c = ChordFactory.getChordByFullSymbol("C7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        expected = c.getRoot() + Interval.MINOR_SEVENTH;
        assertThat(c.getSeventh(), equalTo(expected));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#getNinth()}.
     */
    @Test
    public void testGetNinth() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj9");
        } catch (UnknownChordException e1) {
            e1.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        int expected = c.getRoot() + Interval.MAJOR_NINTH;
        assertThat(c.getNinth(), equalTo(expected));

        try {
            c = ChordFactory.getChordByFullSymbol("C9");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        expected = c.getRoot() + Interval.MAJOR_NINTH;
        assertThat(c.getNinth(), equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getEleventh()}.
     */
    @Test
    public void testGetEleventh() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj11");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        int expected = c.getRoot() + Interval.MAJOR_ELEVENTH;
        assertThat(c.getEleventh(), equalTo(expected));

        try {
            c = ChordFactory.getChordByFullSymbol("C11");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        expected = c.getRoot() + Interval.MAJOR_ELEVENTH;
        assertThat(c.getEleventh(), equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#getThirteenth()}.
     */
    @Test
    public void testGetThirteenth() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj13");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        int expected = c.getRoot() + Interval.MAJOR_THIRTEENTH;
        assertThat(c.getThirteenth(), equalTo(expected));

        try {
            c = ChordFactory.getChordByFullSymbol("C13");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        expected = c.getRoot() + Interval.MAJOR_THIRTEENTH;
        assertThat(c.getThirteenth(), equalTo(expected));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#hasSeventh()}
     * .
     */
    @Test
    public void testHasSeventh() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasSeventh(), equalTo(true));
        try {
            c = ChordFactory.getChordByFullSymbol("C7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasSeventh(), equalTo(true));
        try {
            c = ChordFactory.getChordByFullSymbol("C9");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasSeventh(), equalTo(true));
        try {
            c = ChordFactory.getChordByFullSymbol("C11");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasSeventh(), equalTo(true));
        try {
            c = ChordFactory.getChordByFullSymbol("C13");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasSeventh(), equalTo(true));
        //

        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasSeventh(), equalTo(false));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#hasNinth()}.
     */
    @Test
    public void testHasNinth() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasNinth(), equalTo(false));
        try {
            c = ChordFactory.getChordByFullSymbol("C7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasNinth(), equalTo(false));
        try {
            c = ChordFactory.getChordByFullSymbol("C9");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasNinth(), equalTo(true));
        try {
            c = ChordFactory.getChordByFullSymbol("C11");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasNinth(), equalTo(true));
        try {
            c = ChordFactory.getChordByFullSymbol("C13");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasNinth(), equalTo(true));
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasNinth(), equalTo(false));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#hasEleventh()}.
     */
    @Test
    public void testHasEleventh() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasEleventh(), equalTo(false));
        try {
            c = ChordFactory.getChordByFullSymbol("C7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasEleventh(), equalTo(false));
        try {
            c = ChordFactory.getChordByFullSymbol("C9");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasEleventh(), equalTo(false));
        try {
            c = ChordFactory.getChordByFullSymbol("C11");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasEleventh(), equalTo(true));
        try {
            c = ChordFactory.getChordByFullSymbol("C13");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasEleventh(), equalTo(true));
        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasEleventh(), equalTo(false));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#hasThirteenth()}.
     */
    @Test
    public void testHasThirteenth() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasThirteenth(), equalTo(false));

        c = ChordFactory.getChordByFullSymbol("C7");

        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasThirteenth(), equalTo(false));

        c = ChordFactory.getChordByFullSymbol("C9");

        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasThirteenth(), equalTo(false));

        c = ChordFactory.getChordByFullSymbol("C11");

        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasThirteenth(), equalTo(false));

        c = ChordFactory.getChordByFullSymbol("C13");

        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasThirteenth(), equalTo(true));

        c = ChordFactory.getChordByFullSymbol("C");

        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.hasThirteenth(), equalTo(false));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#toRoman(com.rockhoppertech.music.Scale, java.lang.String)}
     * .
     */
    @Test
    public void testToRoman() {
        Chord c = null;

        c = ChordFactory.getChordByFullSymbol("C");

        assertThat("chord is not null",
                c, is(notNullValue()));
        Scale scale = ScaleFactory.getScaleByName("Major");
        assertThat("chord is not null",
                c, is(notNullValue()));
        String key = "C";
        assertThat(c.toRoman(scale, key), equalTo("Imaj"));

        c = ChordFactory.getChordByFullSymbol("C#");

        assertThat(c.toRoman(scale, key), equalTo("#Imaj"));

        c = ChordFactory.getChordByFullSymbol("Dm");
        assertThat(c.toRoman(scale, key), equalTo("IIm"));

        c = ChordFactory.getChordByFullSymbol("Em");
        assertThat(c.toRoman(scale, key), equalTo("IIIm"));

        c = ChordFactory.getChordByFullSymbol("F");
        assertThat(c.toRoman(scale, key), equalTo("IVmaj"));

        c = ChordFactory.getChordByFullSymbol("G7");
        assertThat(c.toRoman(scale, key), equalTo("V7"));

        c = ChordFactory.getChordByFullSymbol("Am");
        assertThat(c.toRoman(scale, key), equalTo("VIm"));

        c = ChordFactory.getChordByFullSymbol("Bdim");
        assertThat(c.toRoman(scale, key), equalTo("VIIdim"));

    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#getAliases()}
     * .
     */
    @Test
    public void testGetAliases() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C7b5");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        List<String> aliases = c.getAliases();
        assertThat(aliases, hasItem("7-5"));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.Chord#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        Chord c = null;

        c = ChordFactory.getChordByFullSymbol("C7b5");

        assertThat("chord is not null",
                c, is(notNullValue()));
        Chord c2 = null;

        c2 = ChordFactory.getChordByFullSymbol("C7b5");
        assertThat("chord is not null",
                c, is(notNullValue()));
        assertThat(c.equals(c2), equalTo(true));

        c2.setStartBeat(2d);
        assertThat(c.equals(c2), equalTo(false));

        c2 = ChordFactory.getChordByFullSymbol("C7b5");

        c2.setDuration(1.5);
        assertThat(c.equals(c2), equalTo(false));

        c2 = ChordFactory.getChordByFullSymbol("C7b5");

        c2.setRoot(Pitch.EF5);
        assertThat(c.equals(c2), equalTo(false));

        c2 = ChordFactory.getChordByFullSymbol("C7b5");
        c2.setRoot(Pitch.E5);
        c2.setDrop(2);
        assertThat(c.equals(c2), equalTo(false));

        c2 = ChordFactory.getChordByFullSymbol("C7b5");
        c2.setChordVoicing("3 5");
        assertThat(c.equals(c2), equalTo(false));

    }

    /**
     * Test method for {@link com.rockhoppertech.music.chord.Chord#clone()}.
     */
    @Test
    public void testClone() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("C7");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                c, is(notNullValue()));
        Chord clone = (Chord) c.clone();
        assertThat(c.equals(clone), equalTo(true));

        assertThat(clone.getInversion(), equalTo(0));
        c.setInversion(1);
        assertThat(clone.getInversion(), equalTo(0));

    }

    @Test
    public void getScales() {
        Chord chord = null;
        try {
            // chord = ChordFactory.getChordByFullSymbol("Cmaj");
            chord = ChordFactory.getChordBySymbol("maj");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat("chord is not null",
                chord, is(notNullValue()));
        Set<Scale> scales = chord.getScales();
        assertThat("scales is not null",
                scales, is(notNullValue()));
        for (Scale scale : scales) {
            System.out.printf("%s %s\n", scale.getKey(), scale.getName());
        }
        Scale scale = ScaleFactory.getScaleByKeyAndName("C", "Major");
        assertThat(scales, hasItem(scale));
        scale = ScaleFactory.getScaleByKeyAndName("E", "Octatonic");
        assertThat(scales, hasItem(scale));
    }
}
