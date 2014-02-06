/**
 * 
 */
package com.rockhoppertech.music.series.time;

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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.collections.CircularList;
import com.rockhoppertech.collections.ListUtils;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class TimeSeriesFactoryTest {
    static Logger logger = LoggerFactory.getLogger(TimeSeriesFactoryTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#create(int)}
     * .
     */
    @Test
    public final void testCreateInt() {
        TimeSeries ts = TimeSeriesFactory.create(5);
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);
        int actual = ts.getSize();
        int expected = 5;
        assertThat("the size is correct",
                actual, is(equalTo(expected)));

        double expectedDuration = 1d;
        for (double d : ts.getDurations()) {
            assertThat("the duration is correct",
                    d, is(equalTo(expectedDuration)));
        }

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#create(int, double)}
     * .
     */
    @Test
    public final void testCreateIntDouble() {
        TimeSeries ts = TimeSeriesFactory.create(5, 5.5);
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);
        int actual = ts.getSize();
        int expected = 5;
        assertThat("the size is correct",
                actual, is(equalTo(expected)));

        double expectedDuration = 5.5;
        for (double d : ts.getDurations()) {
            assertThat("the duration is correct",
                    d, is(equalTo(expectedDuration)));
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#create(int, double, double)}
     * .
     */
    @Test
    public final void testCreateIntDoubleDouble() {
        double expectedIncrement = .5;
        double expectedDuration = 5.5;
        TimeSeries ts = TimeSeriesFactory.create(
                5,
                expectedDuration,
                expectedIncrement);
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);
        int actual = ts.getSize();
        int expected = 5;
        assertThat("the size is correct",
                actual, is(equalTo(expected)));

        List<Double> starts = ts.getStartTimes();
        List<Double> expectedStarts = Lists.newArrayList(1d, 1.5, 2d, 2.5, 3d);
        assertThat("the starts are correct",
                starts, is(equalTo(expectedStarts)));

        for (double d : ts.getDurations()) {
            assertThat("the duration is correct",
                    d, is(equalTo(expectedDuration)));
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#create(int, com.rockhoppertech.collections.CircularList, double)}
     * .
     */
    @Test
    public final void testCreateIntCircularListOfDoubleDouble() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#create(java.lang.Double[])}
     * .
     */
    @Test
    public final void testCreateDoubleArray() {
        Double[] durations = new Double[] { 1d, 3d };
        TimeSeries ts = TimeSeriesFactory.create(durations);
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);
        List<Double> actual = ts.getDurations();
        assertThat("the durations are correct",
                actual, is(equalTo(Arrays.asList(durations))));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#create(java.util.List)}
     * .
     */
    @Test
    public final void testCreateListOfDouble() {
        List<Double> durations = Lists.newArrayList(1d, 1.5, 2d, 2.5, 3d);
        TimeSeries ts = TimeSeriesFactory.create(durations);
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);

        List<Double> actual = ts.getDurations();
        assertThat("the durations are correct",
                actual, is(equalTo(durations)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#create(com.rockhoppertech.collections.CircularList, java.util.List)}
     * .
     */
    @Test
    public final void testCreateCircularListOfDoubleListOfDouble() {
        CircularList<Double> startTimes = new CircularArrayList<Double>();
        startTimes.add(2d);
        startTimes.add(3d);
        List<Double> durations = Lists.newArrayList(.5, .25, 1.25, .25);
        TimeSeries ts = TimeSeriesFactory.create(startTimes, durations);

        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);

        List<Double> actual = ts.getDurations();
        List<Double> expectedDurations = Lists.newArrayList(.5, 1.25, .25, .25);
        assertThat("the durations are correct",
                actual, is(equalTo(expectedDurations)));

        actual = ts.getStartTimes();
        List<Double> expectedStartTimes = Lists.newArrayList(2d, 2d, 3d, 3d);
        assertThat("the durations are correct",
                actual, is(equalTo(expectedStartTimes)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#create(int, java.lang.Double[])}
     * .
     */
    @Test
    public final void testCreateIntDoubleArray() {
        Double[] durations = new Double[] { 1d, 3d };
        TimeSeries ts = TimeSeriesFactory.create(5, durations);
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);
        List<Double> actual = ts.getDurations();
        // we asked for 5 events with 3 durations, they wrapped.
        List<Double> expected = Lists.newArrayList(1d, 3d, 1d, 3d, 1d);
        assertThat("the durations are correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#create(int, com.rockhoppertech.collections.CircularList, com.rockhoppertech.collections.CircularList)}
     * .
     */
    @Test
    public final void testCreateIntCircularListOfDoubleCircularListOfDouble() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#createRepeated(com.rockhoppertech.music.series.time.TimeSeries, com.rockhoppertech.collections.CircularList)}
     * .
     */
    @Test
    public final void testCreateRepeated() {
        // TimeSeries ts = TimeSeriesFactory.create(2, new Double[] { 1.5, .5
        // });
        // this is easier:
        TimeSeries ts = TimeSeriesFactory.create(1.5, .5);
        logger.debug("time series \n{}", ts);
        CircularList<Integer> list = new CircularArrayList<Integer>();
        list.add(2);        // 2x the first dur
        list.add(3);        // 3x the second dur
        TimeSeries repeated = TimeSeriesFactory.createRepeated(ts, list);
        assertThat("time series is not null",
                repeated, is(notNullValue()));
        logger.debug("repeated time series \n{}", repeated);

        // 2x the first dur, 3x the second dur
        List<Double> expectedDurations = Lists.newArrayList(
                1.5,
                1.5,
                .5,
                .5,
                .5);
        assertThat("the durs are correct",
                repeated.getDurations(), is(equalTo(expectedDurations)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#createFromRepeatMask(java.util.List, com.rockhoppertech.collections.CircularList)}
     * .
     */
    @Test
    public final void testCreateFromRepeatMaskListOfDoubleCircularListOfInteger() {
        List<Double> durations = new CircularArrayList<Double>();
        durations.add(1.5);
        durations.add(.5);
        CircularList<Integer> mask = new CircularArrayList<Integer>();
        mask.add(2);        // 2x the first dur
        mask.add(3);        // 3x the second dur
        TimeSeries repeated = TimeSeriesFactory.createFromRepeatMask(
                durations,
                mask);
        assertThat("time series is not null",
                repeated, is(notNullValue()));
        logger.debug("repeated time series \n{}", repeated);

        // 2x the first dur, 3x the second dur
        List<Double> expectedDurations = Lists.newArrayList(
                1.5,
                1.5,
                .5,
                .5,
                .5);
        assertThat("the durs are correct",
                repeated.getDurations(), is(equalTo(expectedDurations)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#createFromRepeatMask(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCreateFromRepeatMaskStringStringString() {
        String startTimes = "2.5 3.5 4 5 6";
        String durations = "1.5 .5";
        String repeats = "2 3";

        TimeSeries repeated = TimeSeriesFactory.createFromRepeatMask(
                startTimes,
                durations,
                repeats);

        assertThat("time series is not null",
                repeated, is(notNullValue()));
        logger.debug("repeated time series \n{}", repeated);

        // 2x the first dur, 3x the second dur
        List<Double> expectedDurations = Lists.newArrayList(
                1.5,
                1.5,
                .5,
                .5,
                .5);
        assertThat("the durs are correct",
                repeated.getDurations(), is(equalTo(expectedDurations)));

        List<Double> starts = ListUtils
                .stringToDoubleList(startTimes);
        assertThat("the starts are correct",
                repeated.getStartTimes(), is(equalTo(starts)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#createFromRepeatMask(com.rockhoppertech.collections.CircularList, java.util.List, com.rockhoppertech.collections.CircularList)}
     * .
     */
    @Test
    public final void testCreateFromRepeatMaskCircularListOfDoubleListOfDoubleCircularListOfInteger() {
        List<Double> durations = new CircularArrayList<Double>();
        durations.add(1.5);
        durations.add(.5);

        CircularList<Double> startTimes = new CircularArrayList<Double>();
        startTimes.add(2.5);
        startTimes.add(3.5);
        startTimes.add(4d);
        startTimes.add(5d);
        startTimes.add(6d);
        CircularList<Integer> mask = new CircularArrayList<Integer>();
        mask.add(2);        // 2x the first dur
        mask.add(3);        // 3x the second dur
        TimeSeries repeated = TimeSeriesFactory.createFromRepeatMask(
                startTimes,
                durations,
                mask);

        assertThat("time series is not null",
                repeated, is(notNullValue()));
        logger.debug("repeated time series \n{}", repeated);

        // 2x the first dur, 3x the second dur
        List<Double> expectedDurations = Lists.newArrayList(
                1.5,
                1.5,
                .5,
                .5,
                .5);
        assertThat("the durs are correct",
                repeated.getDurations(), is(equalTo(expectedDurations)));

        List<Double> starts = startTimes;
        assertThat("the starts are correct",
                repeated.getStartTimes(), is(equalTo(starts)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#createFromDurationString(java.lang.String)}
     * .
     */
    @Test
    public final void testCreateFromDurationStringString() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString("q q e e");
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);
        List<Double> expectedDurations = Lists.newArrayList(
                1d,
                1d,
                .5,
                .5);
        assertThat("the durs are correct",
                ts.getDurations(), is(equalTo(expectedDurations)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#createFromDurationString(com.rockhoppertech.music.series.time.TimeSeries, java.lang.String)}
     * .
     */
    @Test
    public final void testCreateFromDurationStringTimeSeriesString() {
        TimeSeries ts1 = TimeSeriesFactory.create(6.7, 8.1);
        // appends the durations in the string to the provided time series
        // the ts parameter is the same as the returned ts
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(
                ts1,
                "q q e e");
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);
        logger.debug("time series 1 \n{}", ts1);
        List<Double> expectedDurations = Lists.newArrayList(
                6.7,
                8.1,
                1d,
                1d,
                .5,
                .5);
        assertThat("the durs are correct",
                ts.getDurations(), is(equalTo(expectedDurations)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#getStartTimeIntervals(com.rockhoppertech.music.series.time.TimeSeries)}
     * .
     */
    @Test
    public final void testGetStartTimeIntervals() {
        TimeSeries ts = TimeSeriesFactory.create(
                5,
                .5,
                1.5);
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);
        double[] intervals = TimeSeriesFactory.getStartTimeIntervals(ts);
        double[] expected = new double[] { 1.5, 1.5, 1.5, 1.5 };
        logger.debug("intervals \n{}", intervals);
        assertThat("the durs are correct",
                intervals, is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#setStartTimeIntervals(com.rockhoppertech.music.series.time.TimeSeries, java.lang.Double[])}
     * .
     */
    @Test
    public final void testSetStartTimeIntervalsTimeSeriesDoubleArray() {
        TimeSeries ts = TimeSeriesFactory.create(
                5,
                .5,
                1.5);
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);

        Double[] intervals = new Double[] { 1.5, 2d, 1d };
        TimeSeriesFactory.setStartTimeIntervals(ts, intervals);
        logger.debug("time series after \n{}", ts);

        double[] expected = new double[] { 1.5, 1.5, 2d, 1d };
        double[] actual = TimeSeriesFactory.getStartTimeIntervals(ts);
        assertThat("the intervals are correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeriesFactory#setStartTimeIntervals(com.rockhoppertech.music.series.time.TimeSeries, java.lang.Double[], double)}
     * .
     */
    @Test
    public final void testSetStartTimeIntervalsTimeSeriesDoubleArrayDouble() {
        TimeSeries ts = TimeSeriesFactory.create(
                5,
                .5,
                1.5);
        assertThat("time series is not null",
                ts, is(notNullValue()));
        logger.debug("time series \n{}", ts);

        Double[] intervals = new Double[] { 1.5, 2d, 1d };
        double start = 4.5;
        TimeSeriesFactory.setStartTimeIntervals(ts, intervals, start);
        logger.debug("time series after \n{}", ts);

        double[] expected = new double[] { 1.5, 1.5, 2d, 1d };
        double[] actual = TimeSeriesFactory.getStartTimeIntervals(ts);
        assertThat("the intervals are correct",
                actual, is(equalTo(expected)));
    }

}
