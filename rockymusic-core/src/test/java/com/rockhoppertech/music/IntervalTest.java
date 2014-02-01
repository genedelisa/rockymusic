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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class IntervalTest {
    private static final Logger logger = LoggerFactory
            .getLogger(IntervalTest.class);

    @Test
    public void testGetName() {
        String actual = Interval.getName(1);
        String expected = "Minor second";
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Interval.getName(7);
        expected = "Perfect fifth";
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Interval.getName(50);
        expected = "Unknown";
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testDegreesToIntervals() {
        int[] actual = Interval.degreesToIntervals("1 5");
        int[] expected = { 7 };
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Interval.degreesToIntervals("1 3 5");
        expected = new int[] { 4, 7 };
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testGetIntervalsFromDegreeString() {
        int[] actual = Interval.getIntervalsFromDegreeString("1 5");
        int[] expected = { 7 };
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        // commas
        actual = Interval.getIntervalsFromDegreeString("1, 3,5");
        expected = new int[] { 4, 3 };
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        // spaces
        actual = Interval.getIntervalsFromDegreeString("1 3 5");
        expected = new int[] { 4, 3 };
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
    }

    @Test
    public void testGetIntervalsFromPitchString() {
        int[] actual = Interval.getIntervalsFromPitchString("C G A");
        int[] expected = { 7, 2 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Interval.getIntervalsFromPitchString("C5 G5 A5");
        expected = new int[] { 7, 2 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Interval.getIntervalsFromPitchString("C5, G5, A5");
        expected = new int[] { 7, 2 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testGetIntervalsFromScaleSpelling() {
        int[] actual = Interval.getIntervalsFromScaleSpelling("2 2 1 2");
        int[] expected = { 0, -2, 2 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Interval.getIntervalsFromScaleSpelling("2, 2, 1, 2");
        expected = new int[] { 0, -2, 2 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testGetIntervalsFromSpelling() {
        int[] actual = Interval.getIntervalsFromSpelling("2 2 1 2");
        int[] expected = { 0, -2, 2 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Interval.getIntervalsFromSpelling("2, 2, 1, 2");
        expected = new int[] { 0, -2, 2 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testGetIntervalsFromString() {
        int[] actual = Interval.getIntervalsFromString("2 2 1 2");
        int[] expected = { 0, -1, 1 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Interval.getIntervalsFromString("2, 2, 1, 2");
        // expected = new int[] { 0, -2, 2 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testGetAbsoluteIntervalsFromString() {
        int[] actual = Interval.getAbsoluteIntervalsFromString("1 2 3");
        int[] expected = { 1, 2 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Interval.getAbsoluteIntervalsFromString("1,2,3");
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testGetScaleSpellingInt() {
        String actual = Interval.getScaleSpelling(7);
        String expected = "5";
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testGetScaleSpellingString() {
        int actual = Interval.getScaleSpelling("5");
        int expected = 7;
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testGetSpellingInt() {
        String actual = Interval.getSpelling(5);
        String expected = "4";
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Interval.getSpelling(200);
        logger.debug("actual {}", actual);
        assertThat("the value is correct",
                actual, is(nullValue()));

    }

    @Test
    public void testGetSpellingString() {
        int actual = Interval.getSpelling("5");
        int expected = 7;
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testIntervalsToDegreesIntArray() {
        int[] intervals = { 1 };
        int[] expected = { 0, 1 };
        int[] actual = Interval.intervalsToDegrees(intervals);
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testIntervalsToDegreesIntArrayInt() {
        int[] intervals = { 1 };
        int octaves = 1;
        int[] actual = Interval.intervalsToDegrees(intervals, octaves);
        int[] expected = { 0, 1 };
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testSpell() {
        int[] intervals = { 1 };
        String actual = Interval.spell(intervals);
        String expected = "b2 ";
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        intervals = new int[] { 1, 3, 5 };
        actual = Interval.spell(intervals);
        expected = "b2 b3 4 ";
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        intervals = new int[] { 2, 4, 7 };
        actual = Interval.spell(intervals);
        expected = "2 3 5 ";
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testSpellScale() {
        int[] intervals = { 1 };
        String actual = Interval.spellScale(intervals);
        String expected = "b2 ";
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        intervals = new int[] { 1, 3, 5 };
        actual = Interval.spellScale(intervals);
        expected = "b2 b3 4 ";
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        intervals = new int[] { 2, 4, 7 };
        actual = Interval.spellScale(intervals);
        expected = "2 3 5 ";
        logger.debug("actual {}", actual);
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    // @Test
    // public void testInterval() {
    // Interval actual = new Interval();
    // Interval expected = null;
    // logger.debug("actual {}", actual);
    // assertThat("is not null",
    // actual, is(notNullValue()));
    // assertThat("the value is correct",
    // actual, is(equalTo(expected)));
    //
    // }

}
