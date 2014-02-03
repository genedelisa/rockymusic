/**
 * 
 */
package com.rockhoppertech.music.scale;

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

import com.google.common.collect.Lists;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.chord.Chord;
import com.rockhoppertech.music.chord.ChordFactory;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ScaleTest {

    private static final Logger logger = LoggerFactory
            .getLogger(ScaleTest.class);

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#Scale()}.
     */
    @Test
    @Ignore
    public void testScale() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#Scale(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testScaleStringString() {
        fail("Not yet implemented");
        String name = "Major";
        String spelling = "";
        Scale actual = new Scale(name, spelling);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#Scale(java.lang.String, int[])}.
     */
    @Test
    @Ignore
    public void testScaleStringIntArray() {
        fail("Not yet implemented");
        int[] intervals = new int[] { 1 };
        String name = "Major";
        Scale actual = new Scale(name, intervals);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#Scale(java.lang.String, java.lang.Integer[], java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testScaleStringIntegerArrayString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#isDiatonic(int)}.
     */
    @Test
    @Ignore
    public void testIsDiatonic() {
        fail("Not yet implemented");
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#getIntervalsAsString()}.
     */
    @Test
    public void testGetIntervalsAsString() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        String actual = scale.getIntervalsAsString();
        logger.debug("actual {}", actual);
        String expected = "2 2 1 2 2 2 1";
        assertThat("intervals string is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#getDescendingIntervalsAsString()}.
     */
    @Test
    public void testGetDescendingIntervalsAsString() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        String actual = scale.getDescendingIntervalsAsString();
        logger.debug("actual {}", actual);
        String expected = ""; // no descendng for Major
        assertThat("intervals string is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        // this scale does have descending intervals
        scale = ScaleFactory.createFromName("Melodic Minor");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        actual = scale.getDescendingIntervalsAsString();
        logger.debug("actual {}", actual);
        expected = "2 1 2 2 1 2 2";
        assertThat("intervals string is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#getDegreesAsString()}.
     */
    @Test
    public void testGetDegreesAsString() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        String actual = null;
        String expected = null;
        actual = scale.getDegreesAsString();
        logger.debug("actual {}", actual);
        expected = "0 2 4 5 7 9 11 12";
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#getDegreesAsPitchString()}.
     */
    @Test
    public void testGetDegreesAsPitches() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        String actual = scale.getDegreesAsPitchString();
        logger.debug("actual {}", actual);
        assertThat("actual is not null",
                actual, is(notNullValue()));
        String expected = "C D E F G A B C";
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#getDegreesAsPitches(java.lang.String)}
     * .
     */
    @Test
    public void testGetDegreesAsPitchesString() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        List<Pitch> pits = scale.getDegreesAsPitches("C");
        assertNotNull(pits);
        assertThat(pits.get(0).getPitchClass(), equalTo(PitchFactory.getPitch(
                "C").getPitchClass()));
        assertThat(pits.get(1).getPitchClass(), equalTo(PitchFactory.getPitch(
                "D").getPitchClass()));
        assertThat(pits.get(2).getPitchClass(), equalTo(PitchFactory.getPitch(
                "E").getPitchClass()));
        assertThat(pits.get(3).getPitchClass(), equalTo(PitchFactory.getPitch(
                "F").getPitchClass()));
        assertThat(pits.get(4).getPitchClass(), equalTo(PitchFactory.getPitch(
                "G").getPitchClass()));
        assertThat(pits.get(5).getPitchClass(), equalTo(PitchFactory.getPitch(
                "A").getPitchClass()));
        assertThat(pits.get(6).getPitchClass(), equalTo(PitchFactory.getPitch(
                "B").getPitchClass()));
        for (Pitch p : pits) {
            logger.debug("{} ", p);
        }

        pits = scale.getDegreesAsPitches("Eb");
        assertNotNull(pits);
        assertThat(pits.get(0).getPitchClass(), equalTo(PitchFactory.getPitch(
                "Eb").getPitchClass()));
        assertThat(pits.get(1).getPitchClass(), equalTo(PitchFactory.getPitch(
                "F").getPitchClass()));
        assertThat(pits.get(2).getPitchClass(), equalTo(PitchFactory.getPitch(
                "G").getPitchClass()));
        assertThat(pits.get(3).getPitchClass(), equalTo(PitchFactory.getPitch(
                "Ab").getPitchClass()));
        assertThat(pits.get(4).getPitchClass(), equalTo(PitchFactory.getPitch(
                "Bb").getPitchClass()));
        assertThat(pits.get(5).getPitchClass(), equalTo(PitchFactory.getPitch(
                "C").getPitchClass()));
        assertThat(pits.get(6).getPitchClass(), equalTo(PitchFactory.getPitch(
                "D").getPitchClass()));
        for (Pitch p : pits) {
            logger.debug("{} ", p);
        }

        scale = ScaleFactory.createFromName("Harmonic Minor");
        pits = scale.getDegreesAsPitches("C");
        assertNotNull(pits);
        assertThat(pits.get(0).getPitchClass(), equalTo(PitchFactory.getPitch(
                "C").getPitchClass()));
        assertThat(pits.get(1).getPitchClass(), equalTo(PitchFactory.getPitch(
                "D").getPitchClass()));
        assertThat(pits.get(2).getPitchClass(), equalTo(PitchFactory.getPitch(
                "Eb").getPitchClass()));
        assertThat(pits.get(3).getPitchClass(), equalTo(PitchFactory.getPitch(
                "F").getPitchClass()));
        assertThat(pits.get(4).getPitchClass(), equalTo(PitchFactory.getPitch(
                "G").getPitchClass()));
        assertThat(pits.get(5).getPitchClass(), equalTo(PitchFactory.getPitch(
                "Ab").getPitchClass()));
        assertThat(pits.get(6).getPitchClass(), equalTo(PitchFactory.getPitch(
                "B").getPitchClass()));
        for (Pitch p : pits) {
            logger.debug("{} ", p);
        }

    }

    @Test
    public void pitchToDegree() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        String[] pcs = { "A", "B", "C", "D", "E", "F", "G" };
        for (String p : pcs) {
            int d = scale.pitchToDegree("C", p);
            logger.debug("d={} for '{}'\n", d, p);
        }
        pcs = new String[] { "A", "B", "C", "D", "E", "F#", "G" };
        for (String p : pcs) {
            int d = scale.pitchToDegree("C", p);
            logger.debug("d={} for '{}'\n", d, p);
        }

    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#getDegree(int)}.
     */
    @Test
    public void testGetDegree() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        int index = 0;
        int degree = scale.getDegree(index);
        assertThat("the value is correct", degree, is(equalTo(Pitch.C0)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#getDegreesWithinOctave(int)}.
     */
    @Test
    public void testGetDegreeWithinOctave() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));

        int[] actual = scale.getDegreesWithinOctave();
        int[] expected = new int[] { 0, 2, 4, 5, 7, 9, 11 };
        logger.debug("actual {}", actual);
        assertThat("description is not null",
                actual, is(notNullValue()));
        assertThat("the scale degrees are correct",
                actual, equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#getDegreesAsMIDINumbers()}.
     */
    @Test
    public void getDegreesAsMIDINumbers() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));

        List<Integer> actual = scale.getDegreesAsMIDINumbers();
        List<Integer> expected = Lists.newArrayList(0, 2, 4, 5, 7, 9, 11, 12);
        logger.debug("actual {}", actual);
        assertThat("description is not null",
                actual, is(notNullValue()));
        assertThat("the scale degrees are correct",
                actual, equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#getDegreesAsMIDINumbers(String)}.
     */
    @Test
    public void getDegreesAsMIDINumbersString() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));

        List<Integer> actual = scale.getDegreesAsMIDINumbers("c5");
        List<Integer> expected = Lists.newArrayList(0, 2, 4, 5, 7, 9, 11, 12);
        logger.debug("actual {}", actual);
        assertThat("description is not null",
                actual, is(notNullValue()));
        assertThat("the scale degrees are correct",
                actual, equalTo(expected));

        actual = scale.getDegreesAsMIDINumbers("D5");
        expected = Lists.newArrayList(2, 4, 6, 7, 9, 11, 13, 14);
        logger.debug("actual {}", actual);
        assertThat("description is not null",
                actual, is(notNullValue()));
        assertThat("the scale degrees are correct",
                actual, equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#getDegreesAsPitches()}.
     */
    @Test
    public void getDegreesAsPitches() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));

        List<Pitch> actual = scale.getDegreesAsPitches();
        List<Pitch> expected = Lists.newArrayList(
                PitchFactory.getPitch(Pitch.C5),
                PitchFactory.getPitch(Pitch.D5),
                PitchFactory.getPitch(Pitch.E5),
                PitchFactory.getPitch(Pitch.F5),
                PitchFactory.getPitch(Pitch.G5),
                PitchFactory.getPitch(Pitch.A5),
                PitchFactory.getPitch(Pitch.B5),
                PitchFactory.getPitch(Pitch.C6)
                );
        logger.debug("actual {}", actual);
        assertThat("description is not null",
                actual, is(notNullValue()));
        assertThat("the scale degrees are correct",
                actual, equalTo(expected));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#getIntervals()}.
     */
    @Test
    public void testGetIntervals() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        int[] actual = scale.getIntervals();
        int[] expected = new int[] { 2, 2, 1, 2, 2, 2, 1 };

        logger.debug("actual {}", actual);
        assertThat("actual is not null",
                actual, is(notNullValue()));
        assertThat("actual length", actual.length > 0, is(true));

        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#setIntervals(int[])}.
     */
    @Test
    @Ignore
    public void testSetIntervals() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#getName()}.
     */
    @Test
    public void testGetName() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        String name = scale.getName();
        assertThat("name is not null",
                name, is(notNullValue()));
        assertThat("the scale name correct",
                name, equalTo("Major"));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#setName(java.lang.String)}.
     */
    @Test
    public void testSetName() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        String name = scale.getName();
        assertThat("name is not null",
                name, is(notNullValue()));
        assertThat("the scale name correct",
                name, equalTo("Major"));
        scale.setName("flobble");
        name = scale.getName();
        assertThat("name is not null",
                name, is(notNullValue()));
        assertThat("the scale name correct",
                name, equalTo("flobble"));

    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#getSpelling()}.
     */
    @Test
    public void testGetSpelling() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        String actual = scale.getSpelling();
        assertThat("scale spelling is not null",
                actual, is(notNullValue()));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#setSpelling(java.lang.String)}.
     */
    @Test
    @Ignore
    public void testSetSpelling() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#getDescription()}.
     */
    @Test
    public void testGetDescription() {
        Scale scale = ScaleFactory.createFromName("Melodic Minor");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        String actual = scale.getDescription();
        String expected = "different up and down";
        assertThat("description is not null",
                actual, is(notNullValue()));
        assertThat("the scale name correct",
                actual, equalTo(expected));

        // major has no description. see scaledefs.xml
        scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        actual = scale.getDescription();
        assertThat("description is null",
                actual, is(nullValue()));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#setDescription(java.lang.String)}.
     */
    @Test
    public void testSetDescription() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        scale.setDescription("aka ionian");

        String actual = scale.getDescription();
        String expected = "aka ionian";
        assertThat("description is not null",
                actual, is(notNullValue()));
        assertThat("the scale description correct",
                actual, equalTo(expected));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#getDegrees()}.
     */
    @Test
    public void testGetDegrees() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));

        int[] actual = scale.getDegrees();
        int[] expected = new int[] { 0, 2, 4, 5, 7, 9, 11, 12 };
        logger.debug("actual {}", actual);
        assertThat("description is not null",
                actual, is(notNullValue()));
        assertThat("the scale degrees are correct",
                actual, equalTo(expected));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#setDegrees(int[])}.
     */
    @Test
    @Ignore
    public void testSetDegrees() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#getLength()}.
     */
    @Test
    @Ignore
    public void testGetLength() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));

        int actual = scale.getLength();
        int expected = 8;
        logger.debug("actual {}", actual);
        assertThat("description is not null",
                actual, is(notNullValue()));
        assertThat("the scale length is correct",
                actual, equalTo(expected));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#setLength(int)}.
     */
    @Test
    @Ignore
    public void testSetLength() {

    }

    /**
     * Test method for {@link com.rockhoppertech.music.Scale#getOctave()} and
     * {@link com.rockhoppertech.music.Scale#setOctave(int)}.
     */
    @Test
    public void testGetOctave() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));

        int actual = scale.getOctave();
        int expected = 0;
        logger.debug("actual {}", actual);
        assertThat("description is not null",
                actual, is(notNullValue()));
        assertThat("the scale octave is correct",
                actual, equalTo(expected));

        scale.setOctave(5);
        actual = scale.getOctave();
        expected = 5;
        assertThat("the scale octave is correct",
                actual, equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#getDescendingIntervals()}.
     */
    @Test
    public void testGetDescendingIntervals() {
        Scale scale = ScaleFactory.createFromName("Melodic Minor");
        assertThat("scale is not null",
                scale, is(notNullValue()));

        int[] actual = scale.getDescendingIntervals();
        int[] expected = new int[] { 2, 1, 2, 2, 1, 2, 2 };
        logger.debug("actual {}", actual);
        assertThat("descending intervals are not null",
                actual, is(notNullValue()));
        assertThat("the scale descending intervals are correct",
                actual, equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#setDescendingIntervals(int[])}.
     */
    @Test
    @Ignore
    public void testSetDescendingIntervalsIntArray() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#setDescendingIntervals(java.lang.Integer[])}
     * .
     */
    @Test
    @Ignore
    public void testSetDescendingIntervalsIntegerArray() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#isDescendingDifferent()}.
     */
    @Test
    public void testIsDescendingDifferent() {
        Scale scale = ScaleFactory.createFromName("Melodic Minor");
        assertThat("scale is not null",
                scale, is(notNullValue()));

        boolean actual = scale.isDescendingDifferent();
        boolean expected = true;
        logger.debug("actual {}", actual);
        assertThat("descending intervals are not null",
                actual, is(notNullValue()));
        assertThat("the scale descending intervals are correct",
                actual, equalTo(expected));

        scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));

        actual = scale.isDescendingDifferent();
        expected = false;
        logger.debug("actual {}", actual);
        assertThat("descending intervals are not null",
                actual, is(notNullValue()));
        assertThat("the scale descending intervals are correct",
                actual, equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Scale#setDescendingDifferent(boolean)}.
     */
    @Test
    @Ignore
    public void testSetDescendingDifferent() {

    }

    @Test
    public void contains() {
        Scale scale = ScaleFactory.getScaleByKeyAndName("C", "Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        String name = scale.getName();
        assertThat("scale name is not null",
                name, is(notNullValue()));

        assertThat("the scale name is correct",
                name, is(equalTo("Major")));

        Chord chord = null;
        chord = ChordFactory.getChordBySymbol("maj");
        assertThat("chord is not null",
                chord, is(notNullValue()));

        assertThat(scale.contains("c", chord), equalTo(true));
        assertThat(scale.contains("c#", chord), equalTo(false));
        assertThat(scale.contains("d", chord), equalTo(false));
        assertThat(scale.contains("ef", chord), equalTo(false));
        assertThat(scale.contains("e", chord), equalTo(false));
        assertThat(scale.contains("f", chord), equalTo(true));
        assertThat(scale.contains("f#", chord), equalTo(false));
        assertThat(scale.contains("g", chord), equalTo(true));
        assertThat(scale.contains("af", chord), equalTo(false));
        assertThat(scale.contains("a", chord), equalTo(false));
        assertThat(scale.contains("bf", chord), equalTo(false));
        assertThat(scale.contains("b", chord), equalTo(false));

    }

    @Test
    public void equals() {
        Scale scale = ScaleFactory.getScaleByKeyAndName("C", "Major");
        Scale scale2 = ScaleFactory.getScaleByKeyAndName("C", "Major");
        boolean actual = scale.equals(scale2);
        boolean expected = true;
        assertThat("the scales are equal",
                actual, equalTo(expected));

        actual = scale.equals(scale);
        assertThat("the scales are equal",
                actual, equalTo(expected));

        actual = scale.equals(null);
        expected = false;
        assertThat("the scales are equal",
                actual, equalTo(expected));

        scale2 = ScaleFactory.getScaleByKeyAndName("G", "Major");
        actual = scale.equals(scale2);
        expected = false;
        assertThat("the scales are equal",
                actual, equalTo(expected));

        scale2 = ScaleFactory.getScaleByKeyAndName("C", "Melodic Minor");
        actual = scale.equals(scale2);
        expected = false;
        assertThat("the scales are equal",
                actual, equalTo(expected));
    }
}
