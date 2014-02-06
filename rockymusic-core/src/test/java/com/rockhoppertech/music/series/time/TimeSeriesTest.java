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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.collections.CircularList;
import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.MIDITrackFactory;
import com.rockhoppertech.music.modifiers.Modifier.Operation;
import com.rockhoppertech.music.modifiers.StartBeatModifier;
import com.rockhoppertech.music.modifiers.TimedModifier;

import static com.rockhoppertech.music.Duration.*;

import static com.rockhoppertech.music.Pitch.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class TimeSeriesTest {
    static Logger logger = LoggerFactory.getLogger(TimeSeriesTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#append(com.rockhoppertech.music.series.time.TimeSeries, com.rockhoppertech.music.series.time.TimeSeries)}
     * .
     */
    @Test
    public void testAppendTimeSeriesTimeSeries() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#extract(com.rockhoppertech.music.midi.js.MIDITrack)}
     * .
     */
    @Test
    public void testExtract() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C D E")
                .durations(.5, .25, .25)
                .build();
        TimeSeries ts = TimeSeries.extract(track);
        assertThat("ts is not null",
                ts, is(notNullValue()));
        List<Double> durs = ts.getDurations();
        List<Double> starts = ts.getStartTimes();
        List<Double> expected = Lists.newArrayList(.5, .25, .25);
        assertThat("durs are equal", durs, is(equalTo(expected)));
        expected = Lists.newArrayList(1d, 1d, 1d);
        assertThat("starts are equal", starts, is(equalTo(expected)));

        track.sequential();
        ts = TimeSeries.extract(track);
        starts = ts.getStartTimes();
        expected = Lists.newArrayList(1d, 1.5, 1.75);
        assertThat("starts are equal", starts, is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#TimeSeries(com.rockhoppertech.music.series.time.TimeSeries)}
     * .
     */
    @Test
    public void testTimeSeriesTimeSeries() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#TimeSeries(java.lang.String)}
     * .
     */
    @Test
    public void testTimeSeriesString() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#add(double)}.
     */
    @Test
    public void testAddDouble() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#add(double, double)}
     * .
     */
    @Test
    public void testAddDoubleDouble() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#add(com.rockhoppertech.music.series.time.TimeEvent)}
     * .
     */
    @Test
    public void testAddTimeEvent() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#addPropertyChangeListener(java.beans.PropertyChangeListener)}
     * .
     */
    @Test
    public void testAddPropertyChangeListenerPropertyChangeListener() {
        fired = false;
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        ts.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                logger.debug("event fired {}", evt);
                fired = true;
            }
        });
        ts.changeStartBeat(0, 10.5);
        assertThat("fired", fired, is(equalTo(true)));
        fired = false;
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)}
     * .
     */
    @Test
    public void testAddPropertyChangeListenerStringPropertyChangeListener() {
        fired = false;
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        ts.addPropertyChangeListener(
                TimeSeries.START_BEAT_CHANGE,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        logger.debug("event fired {}", evt);
                        if (evt.getPropertyName().equals(
                                TimeSeries.START_BEAT_CHANGE)) {
                            fired = true;
                        }
                    }
                });
        ts.changeStartBeat(0, 10.5);
        assertThat("fired", fired, is(equalTo(true)));
        fired = false;
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#modifyDurations(com.rockhoppertech.music.modifiers.Modifier.Operation, java.lang.Double[])}
     * .
     */
    @Test
    public void testModifyDurations() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#modifyStarts(com.rockhoppertech.music.modifiers.Modifier.Operation, java.lang.Double[])}
     * .
     */
    @Test
    public void testModifyStarts() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#addToSilences(double)}
     * .
     */
    @Test
    public void testAddToSilences() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        ts.addToSilences(5d);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#append(com.rockhoppertech.music.series.time.TimeSeries)}
     * .
     */
    @Test
    public void testAppendTimeSeries() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#apply(com.rockhoppertech.music.midi.js.MIDITrack)}
     * .
     */
    @Test
    public void testApplyMIDITrack() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#apply(com.rockhoppertech.music.midi.js.MIDITrack, com.rockhoppertech.collections.CircularList)}
     * .
     */
    @Test
    public void testApplyMIDITrackCircularListOfInteger() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#applyToBoth(com.rockhoppertech.music.midi.js.MIDITrack, com.rockhoppertech.collections.CircularList)}
     * .
     */
    @Test
    public void testApplyToBoth() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#clear()}.
     */
    @Test
    public void testClear() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        ts.clear();
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#computeRanges()}.
     */
    @Test
    public void testComputeRanges() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#fireSeriesChanged()}
     * .
     */
    @Test
    public void testFireSeriesChanged() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#fireSeriesChanged(com.rockhoppertech.music.series.time.TimeSeriesEvent)}
     * .
     */
    @Test
    public void testFireSeriesChangedTimeSeriesEvent() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getDescription()}.
     */
    @Test
    public void testGetDescription() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getDurations()}.
     */
    @Test
    public void testGetDurations() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getEndBeat()}.
     */
    @Test
    public void testGetEndBeat() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getName()}.
     */
    @Test
    public void testGetName() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getExactRange(int)}
     * .
     */
    @Test
    public void testGetExactRange() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getRange(int)}.
     */
    @Test
    public void testGetRange() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getRanges()}.
     */
    @Test
    public void testGetRanges() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getRangesIterator()}
     * .
     */
    @Test
    public void testGetRangesIterator() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getSilences()}.
     */
    @Test
    public void testGetSilences() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getStartBeat()}.
     */
    @Test
    public void testGetStartBeat() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#getStartTimes()}.
     */
    @Test
    public void testGetStartTimes() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#insertAtIndex(int, com.rockhoppertech.music.series.time.TimeSeries)}
     * .
     */
    @Test
    public void testInsertAtIndex() {
        TimeSeries ts = TimeSeriesFactory
                .createFromDurationString("e e q e e q e e");
        TimeSeries ts2 = TimeSeriesFactory.createFromDurationString("s s s s");
        int expectedLength = ts.getSize() + ts2.getSize();
        logger.debug("orig ts \n{}", ts);
        logger.debug("orig ts2 \n{}", ts2);
        ts.insertAtIndex(1, ts2);
        logger.debug("ts \n{}", ts);
        logger.debug("ts2 \n{}", ts2);

        assertThat(
                "the size is correct",
                ts.getSize(),
                is(equalTo(expectedLength)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#iterator()}.
     */
    @Test
    public void testIterator() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#listIterator(int)}
     * .
     */
    @Test
    public void testListIterator() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#map(com.rockhoppertech.music.modifiers.TimedModifier)}
     * .
     */
    @Test
    public void testMapTimedModifier() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#map(com.rockhoppertech.music.modifiers.TimedModifier, double)}
     * .
     */
    @Test
    public void testMapTimedModifierDouble() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#map(com.rockhoppertech.music.modifiers.TimedModifier, double, double)}
     * .
     */
    @Test
    public void testMapTimedModifierDoubleDouble() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#mapDurations(java.util.List)}
     * .
     */
    @Test
    public void testMapDurationsListOfTimeEvent() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#mapDurations(com.rockhoppertech.music.series.time.TimeSeries)}
     * .
     */
    @Test
    public void testMapDurationsTimeSeries() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#nCopies(int)}.
     */
    @Test
    public void testNCopies() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#isAfter(com.rockhoppertech.music.Timed, com.rockhoppertech.music.Timed)}
     * .
     */
    @Test
    public void testIsAfter() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#isBefore(com.rockhoppertech.music.Timed, com.rockhoppertech.music.Timed)}
     * .
     */
    @Test
    public void testIsBefore() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#nextTimeEvent()}.
     */
    @Test
    public void testNextTimeEvent() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#toMIDITrack()}.
     */
    @Test
    public void testToMIDITrack() {
        TimeSeries ts = TimeSeriesFactory
                .createFromDurationString("e e q e e q e e");
        MIDITrack track = ts.toMIDITrack();
        assertThat("track is ot null",
                track, is(notNullValue()));

        MIDINote note = track.get(0);
        double actual = note.getStartBeat();
        double expected = 1d;
        assertThat("beat is correct",
                actual, is(equalTo(expected)));
        actual = note.getDuration();
        expected = Duration.E;
        assertThat("beat is correct",
                actual, is(equalTo(expected)));

        note = track.get(1);
        actual = note.getDuration();
        expected = Duration.E;
        assertThat("beat is correct",
                actual, is(equalTo(expected)));

        note = track.get(2);
        actual = note.getDuration();
        expected = Duration.Q;
        assertThat("beat is correct",
                actual, is(equalTo(expected)));

        note = track.get(3);
        actual = note.getDuration();
        expected = Duration.E;
        assertThat("beat is correct",
                actual, is(equalTo(expected)));

        note = track.get(4);
        actual = note.getDuration();
        expected = Duration.E;
        assertThat("beat is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#play()}.
     */
    @Test
    public void testPlay() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#play(boolean)}.
     */
    @Test
    public void testPlayBoolean() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#quantizeStartBeats(double)}
     * .
     */
    @Test
    public void testQuantizeStartBeats() {
        TimeSeries ts = TimeSeriesFactory
                .createFromDurationString("e e q e e q e e");
        List<Double> expected = Lists.newArrayList(
                1.0, 1.5, 2.0, 3.0, 3.5, 4.0, 5.0, 5.5);
        assertThat("start beats", ts.getStartTimes(), equalTo(expected));
        ts.quantizeStartBeats(1d);
        expected = Lists.newArrayList(1.0, 1.0, 2.0, 3.0, 3.0, 4.0, 5.0, 5.0);
        assertThat("start beats", ts.getStartTimes(), equalTo(expected));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#remove(com.rockhoppertech.music.Timed)}
     * .
     */
    @Test
    public void testRemoveTimed() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#removePropertyChangeListener(java.beans.PropertyChangeListener)}
     * .
     */
    @Test
    public void testRemovePropertyChangeListenerPropertyChangeListener() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)}
     * .
     */
    @Test
    public void testRemovePropertyChangeListenerStringPropertyChangeListener() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#setDescription(java.lang.String)}
     * .
     */
    @Test
    public void testSetDescription() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        ts.setDescription("foo");
        assertThat(
                "description is set",
                ts.getDescription(),
                is(equalTo("foo")));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#setName(java.lang.String)}
     * .
     */
    @Test
    public void testSetName() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        ts.setName("foo");
        assertThat("name is set", ts.getName(), is(equalTo("foo")));
    }

    /**
     * /** Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#setStart(double)}.
     */
    @Test
    public void testSetStart() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#sort()}.
     */
    @Test
    public void testSort() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#toString()}.
     */
    @Test
    public void testToString() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#changeStartBeat(int, double)}
     * .
     */
    @Test
    public void testChangeStartBeatIntDouble() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        ts.changeStartBeat(1, 5.5);

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#changeStartBeat(double)}
     * .
     */
    @Test
    public void testChangeStartBeatDouble() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        ts.changeStartBeat(5.5);
        logger.debug("track\n{}", ts);
        TimeEvent e = ts.get(0);
        assertThat("start beat", e.getStartBeat(), is(equalTo(5.5)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#changeDuration(int, double)}
     * .
     */
    @Test
    public void testChangeDuration() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        ts.changeDuration(0, 5.5);
        logger.debug("track\n{}", ts);
        TimeEvent e = ts.get(0);
        assertThat("duration", e.getDuration(), is(equalTo(5.5)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#sequential()}.
     */
    @Test
    public void testSequential() {

        CircularList<Double> startTimes = new CircularArrayList<Double>();
        startTimes.add(2d);
        startTimes.add(3d);
        List<Double> durations = Lists.newArrayList(.5, .25, 1.25, .25);
        TimeSeries ts = TimeSeriesFactory.create(startTimes, durations);
        logger.debug("time series \n{}", ts);

        ts.sequential();
        logger.debug("time series \n{}", ts);

        List<Double> actual = ts.getStartTimes();
        List<Double> expected = Lists.newArrayList(2d, 2.5, 3.75, 4d);
        assertThat("start beats", actual, is(equalTo(expected)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#sequential(int)}.
     */
    @Test
    public void testSequentialInt() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#changeEndBeat(int, double)}
     * .
     */
    @Test
    public void testChangeEndBeat() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        ts.changeEndBeat(0, 5.5);
        logger.debug("track\n{}", ts);
        TimeEvent e = ts.get(0);
        assertThat("duration", e.getEndBeat(), is(equalTo(5.5)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeSeries#setStartBeat(double)}
     * .
     */
    @Test
    public void testSetStartBeat() {

    }

    private TimeSeries get4Sixteenths() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);
        return ts;
    }

    private TimeSeries get4Eighths() {
        return new TimeSeries().add(EIGHTH_NOTE).add(EIGHTH_NOTE)
                .add(EIGHTH_NOTE).add(EIGHTH_NOTE);
    }

    @Test
    public void that4EighthsAreCorrect() {
        TimeSeries ts = this.get4Eighths();
        double expectedStart = 1d;
        Timed e = ts.get(0);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(EIGHTH_NOTE));

        expectedStart += EIGHTH_NOTE;
        e = ts.get(1);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(EIGHTH_NOTE));

        expectedStart += EIGHTH_NOTE;
        e = ts.get(2);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(EIGHTH_NOTE));

        expectedStart += EIGHTH_NOTE;
        e = ts.get(3);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(EIGHTH_NOTE));
    }

    @Test
    public void that4SixteenthsAreCorrect() {
        TimeSeries ts = this.get4Sixteenths();
        double expectedStart = 1d;
        Timed e = ts.get(0);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        expectedStart += SIXTEENTH_NOTE;
        e = ts.get(1);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        expectedStart += SIXTEENTH_NOTE;
        e = ts.get(2);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        expectedStart += SIXTEENTH_NOTE;
        e = ts.get(3);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));
    }

    @Test
    public void testTimeSeries() {
        TimeSeries ts = new TimeSeries();
        for (int i = 1; i <= 10; i++) {
            ts.add(new TimeEvent(i, .5));
        }
        assertThat(ts.get(0).getStartBeat(),
                equalTo(1d));

        for (int i = 1; i <= 10; i++) {
            assertThat(ts.get(i - 1).getDuration(),
                    equalTo(.5));
        }
    }

    @Test
    public void testTimeSeriesMIDITrack() {
        MIDITrack track = new MIDITrack();
        track.add(new MIDINote(Pitch.C5, 1.5, .5));
        TimeSeries ts = new TimeSeries(track);
        Timed e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1.5));
        assertThat(e.getDuration(),
                equalTo(.5));
    }

    @Test
    public void testAdd() {
        TimeSeries ts = new TimeSeries();
        ts.add(new TimeEvent(5.5, .5));

        Timed e = null;
        e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(5.5));
        assertThat(e.getDuration(),
                equalTo(.5));

        // add should sort
        ts = new TimeSeries();
        ts.add(new TimeEvent(5.5, .5));
        ts.add(new TimeEvent(2.5, .5));
        e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(2.5));
        assertThat(e.getDuration(),
                equalTo(.5));
        e = ts.get(1);
        assertThat(e.getStartBeat(),
                equalTo(5.5));
        assertThat(e.getDuration(),
                equalTo(.5));
    }

    @Test
    public void setStart() {
        TimeSeries ts = TimeSeriesFactory.create(4,
                .5);
        logger.debug("time series\n{}", ts);
        Timed e = null;
        e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(.5));
        e = ts.get(1);
        assertThat(e.getStartBeat(),
                equalTo(2d));
        assertThat(e.getDuration(),
                equalTo(.5));
        e = ts.get(2);
        assertThat(e.getStartBeat(),
                equalTo(3d));
        assertThat(e.getDuration(),
                equalTo(.5));
        e = ts.get(3);
        assertThat(e.getStartBeat(),
                equalTo(4d));
        assertThat(e.getDuration(),
                equalTo(.5));

        // /////////// after current
        ts.setStart(10d);
        e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(10d));
        assertThat(e.getDuration(),
                equalTo(.5));
        e = ts.get(1);
        assertThat(e.getStartBeat(),
                equalTo(11d));
        assertThat(e.getDuration(),
                equalTo(.5));
        e = ts.get(2);
        assertThat(e.getStartBeat(),
                equalTo(12d));
        assertThat(e.getDuration(),
                equalTo(.5));
        e = ts.get(3);
        assertThat(e.getStartBeat(),
                equalTo(13d));
        assertThat(e.getDuration(),
                equalTo(.5));

        // /////////// before current
        ts.setStart(1d);
        e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(.5));
        e = ts.get(1);
        assertThat(e.getStartBeat(),
                equalTo(2d));
        assertThat(e.getDuration(),
                equalTo(.5));
        e = ts.get(2);
        assertThat(e.getStartBeat(),
                equalTo(3d));
        assertThat(e.getDuration(),
                equalTo(.5));
        e = ts.get(3);
        assertThat(e.getStartBeat(),
                equalTo(4d));
        assertThat(e.getDuration(),
                equalTo(.5));

    }

    private boolean fired = false;

    // @Test
    // public void testAddTimeSeriesListener() {
    // TimeSeries ts = new TimeSeries();
    // fired = false;
    // TimeSeriesListener listener = new TimeSeriesListener() {
    // @Override
    // public void seriesChanged(TimeSeriesEvent e) {
    // logger.debug("event {}", e);
    // fired = true;
    // }
    // };
    // ts.addTimeSeriesListener(listener);
    // ts.add(new TimeEvent(1d, .5));
    // assertThat("event was fired", fired, is(equalTo(true)));
    // fired = false;
    // }

    @Test
    public void testAddToDuration() {
        TimeSeries ts = TimeSeriesFactory.create(4,
                .5);
        logger.debug("time series\n{}", ts);
        Timed e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(.5));

        ts.addToDuration(1.25);
        logger.debug("time series\n{}", ts);

        e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(1.75));

        e = ts.get(1);
        assertThat(e.getStartBeat(),
                equalTo(2d));
        assertThat(e.getDuration(),
                equalTo(1.75));

        e = ts.get(2);
        assertThat(e.getStartBeat(),
                equalTo(3d));
        assertThat(e.getDuration(),
                equalTo(1.75));

        e = ts.get(3);
        assertThat(e.getStartBeat(),
                equalTo(4d));
        assertThat(e.getDuration(),
                equalTo(1.75));

    }

    @Test
    public void addToSilences() {
        TimeSeries ts = TimeSeriesFactory.create(4,
                .5);
        logger.debug("time series\n{}", ts);
        logger.debug("Silences\n{}", ts.getSilences());

        ts.addToSilences(2.5);
        logger.debug("time series\n{}", ts);
        logger.debug("new Silences\n{}", ts.getSilences());

        Timed e;

        e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(.5));

        e = ts.get(1);
        assertThat(e.getStartBeat(),
                equalTo(4.5));
        assertThat(e.getDuration(),
                equalTo(.5));

        e = ts.get(2);
        assertThat(e.getStartBeat(),
                equalTo(8d));
        assertThat(e.getDuration(),
                equalTo(.5));

        e = ts.get(3);
        assertThat(e.getStartBeat(),
                equalTo(11.5));
        assertThat(e.getDuration(),
                equalTo(.5));

    }

    // @Test
    // public void testComputeRanges() {
    // fail("Not yet implemented");
    // }

    @Test
    public void testDivideDuration() {
        TimeSeries ts = TimeSeriesFactory.create(4,
                .5);
        ts.divideDuration(2);
        logger.debug("time series\n{}", ts);
        Timed e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(.25));
    }

    @Test
    public void testGet() {
        TimeSeries ts = TimeSeriesFactory.create(4,
                .5);
        Timed e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(.5));

    }

    @Test
    public void testGetEndTime() {
        // four eighth notes with start time 1
        // starts increment by 1
        TimeSeries ts = TimeSeriesFactory.create(4,
                EIGHTH_NOTE);

        logger.debug("tse\n{}", ts);
        assertThat(ts.getEndBeat(),
                equalTo(4.5));

        // modifies starts to be prev start + prev duration
        ts.sequential();
        logger.debug("tse\n{}", ts);
        assertThat(ts.getEndBeat(),
                equalTo(3.0));

    }

    @Test
    public void getRanges() {
        TimeSeries ts = TimeSeriesFactory.create(4,
                EIGHTH_NOTE);
        for (Range<Double> r : ts.getRanges()) {
            logger.debug("{}", r);
        }

        Range<Double> range = null;
        range = ts.getRange(0);
        assertThat(range.lowerEndpoint(),
                equalTo(1.0));
        assertThat(range.upperEndpoint(),
                equalTo(1.5));

        // range = ts.getRange(1);
        // assertThat(range.getMin(),
        // equalTo(2.0));
        // assertThat(range.getMax(),
        // equalTo(2.5 - TimeEvent.rangeFudge));

    }

    // @Test
    // public void testGetSeries() {
    // TimeSeries ts = new TimeSeries();
    // ts.add(new TimeEvent(1d, .5));
    // ts.add(new TimeEvent(2d, .5));
    // ts.add(new TimeEvent(3d, .5));
    // ts.add(new TimeEvent(4d, .5));
    // for(Iterator<TimeEvent>it = ts.getSeries(); it.hasNext();) {
    // TimeEvent r = it.next();
    // }
    // }

    @Test
    public void getSilences() {
        TimeSeries ts = new TimeSeries();
        ts.add(1.5,
                EIGHTH_NOTE);
        ts.add(5.5,
                EIGHTH_NOTE);
        ts.add(8.25,
                EIGHTH_NOTE);
        ts.add(10d,
                EIGHTH_NOTE);

        TimeSeries s = ts.getSilences();
        logger.debug("time series {}", s);
        Timed e = s.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(EIGHTH_NOTE));
        e = s.get(1);
        assertThat(e.getStartBeat(),
                equalTo(2d));
        assertThat(e.getDuration(),
                equalTo(3.5));
        e = s.get(2);
        assertThat(e.getStartBeat(),
                equalTo(6d));
        assertThat(e.getDuration(),
                equalTo(2.25));
        e = s.get(3);
        assertThat(e.getStartBeat(),
                equalTo(8.75));
        assertThat(e.getDuration(),
                equalTo(1.25));

        // not starting at 1, i.e. silence at the beginning
        // ts = new TimeSeries();
        // ts.add(new TimeEvent(2d, .5));
        // ts.add(new TimeEvent(3d, .5));
        // ts.add(new TimeEvent(4d, .5));
        ts.setStart(2d);
        s = ts.getSilences();
        logger.debug("silences {}", s);
        e = s.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(1d));
        e = s.get(1);
        assertThat(e.getStartBeat(),
                equalTo(2.5));
        assertThat(e.getDuration(),
                equalTo(3.5));
    }

    // @Test
    // public void mapCriteria() {
    // TimeSeries ts = new TimeSeries();
    // ts.add(new TimeEvent(1d, .5));
    // ts.add(new TimeEvent(2d, 1.5));
    // ts.add(new TimeEvent(3d, .5));
    // ts.add(new TimeEvent(4d, .5));
    // // before or equal to start beat 3 and the duration is lower than or
    // // equal to 1
    // TimeModifierCriteria criteria = new StartTimeCriteria(3d,
    // TimeModifierCriteria.Operator.LT_EQ, new DurationCriteria(1d,
    // TimeModifierCriteria.Operator.LT_EQ, null));
    //
    // final TimedModifier stm = new StartBeatModifier(10d);
    // stm.setOperation(Operation.SET);
    // ts.map(stm,
    // criteria);
    // logger.debug("time series\n{}", ts);
    // }

    @Test
    public void map() {
        // TimeSeries ts = new TimeSeries();
        // ts.add(new TimeEvent(1d, .5));
        // ts.add(new TimeEvent(2d, 1.5));
        // ts.add(new TimeEvent(3d, .5));
        // ts.add(new TimeEvent(4d, .5));
        // ts.add(new TimeEvent(5d, .5));
        // ts.add(new TimeEvent(6d, .5));

        TimeSeries ts = TimeSeriesFactory.create("1 2 3 4 5 6", "e q. e e e e");
        logger.debug("original time series\n{}", ts);

        final TimedModifier stm = new StartBeatModifier(Operation.SET,
                new Double[] { 2d, 3d, 4d });

        ts.map(stm);
        logger.debug("time series\n{}", ts);

        TimeEvent e = ts.get(0);
        double actual = e.getStartBeat();
        double expected = 2d;
        assertThat("start is correct",
                actual, is(equalTo(expected)));

        e = ts.get(1);
        actual = e.getStartBeat();
        expected = 3d;
        assertThat("start is correct",
                actual, is(equalTo(expected)));

        e = ts.get(2);
        actual = e.getStartBeat();
        expected = 4d;
        assertThat("start is correct",
                actual, is(equalTo(expected)));

        // it wrapped
        e = ts.get(3);
        actual = e.getStartBeat();
        expected = 2d;
        assertThat("start is correct",
                actual, is(equalTo(expected)));

        e = ts.get(4);
        actual = e.getStartBeat();
        expected = 3d;
        assertThat("start is correct",
                actual, is(equalTo(expected)));

        e = ts.get(5);
        actual = e.getStartBeat();
        expected = 4d;
        assertThat("start is correct",
                actual, is(equalTo(expected)));
    }

    @Test
    public void testGetSize() {
        TimeSeries ts = new TimeSeries();
        ts.add(EIGHTH_NOTE).add(SIXTEENTH_NOTE).add(HALF_NOTE).add(EIGHTH_NOTE);
        assertThat("size is correct",
                ts.getSize(),
                equalTo(4));
        ts.remove(0);
        assertThat("size is correct",
                ts.getSize(),
                equalTo(3));
    }

    @Test
    public void testInsertSilence() {
        TimeSeries ts = TimeSeriesFactory.create(6,
                .5);
        logger.debug("series before\n{}", ts);
        // insert .5 beats of silence before each event
        ts.insertSilence(.5);
        logger.debug("series with inserted silence\n{}", ts);
        Timed e = null;
        e = ts.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1.5));
        assertThat(e.getDuration(),
                equalTo(.5));

        e = ts.get(1);
        assertThat(e.getStartBeat(),
                equalTo(2.5));

        e = ts.get(2);
        assertThat(e.getStartBeat(),
                equalTo(3.5));

    }

    @Test
    public void testMapDurations() {
        TimeSeries ts = new TimeSeries();
        ts.add(EIGHTH_NOTE).add(SIXTEENTH_NOTE).add(HALF_NOTE).add(EIGHTH_NOTE);
        List<TimeEvent> durations = new ArrayList<TimeEvent>();
        durations.add(new TimeEvent(1, 4));
        durations.add(new TimeEvent(1, 4));
        durations.add(new TimeEvent(1, 4));
        durations.add(new TimeEvent(1, 4));
        ts.mapDurations(durations);
        logger.debug("time series\n{}", ts);
        for (Timed t : ts) {
            assertThat("durations are correct",
                    t.getDuration(),
                    equalTo(4d));
        }

    }

    @Test
    public void testMapDurationsTs() {
        TimeSeries ts = new TimeSeries();
        ts.add(EIGHTH_NOTE).add(SIXTEENTH_NOTE).add(HALF_NOTE).add(EIGHTH_NOTE);
        TimeSeries durations = new TimeSeries();
        durations.add(WHOLE_NOTE);
        durations.add(WHOLE_NOTE);
        durations.add(WHOLE_NOTE);
        durations.add(WHOLE_NOTE);
        ts.mapDurations(durations);
        logger.debug("time series\n{}", ts);
        for (Timed t : ts) {
            assertThat("durations are correct",
                    t.getDuration(),
                    equalTo(WHOLE_NOTE));
        }

    }

    @Test
    public void testMultiplyDuration() {
        TimeSeries ts = new TimeSeries();
        ts.add(EIGHTH_NOTE).add(SIXTEENTH_NOTE).add(HALF_NOTE).add(EIGHTH_NOTE);
        ts.multiplyDuration(2d);
        logger.debug("time series\n{}", ts);
    }

    @Test
    @Ignore
    public void testPermute() {

    }

    @Test
    public void testQuantizeDurations() {
        TimeSeries ts = new TimeSeries();
        double crap = .03;
        ts.add(EIGHTH_NOTE).add(SIXTEENTH_NOTE + crap).add(HALF_NOTE + crap)
                .add(EIGHTH_NOTE);
        logger.debug("time series\n{}", ts);
        ts.quantizeDurations(SIXTEENTH_NOTE);
        logger.debug("time series\n{}", ts);

        TimeEvent te = ts.get(0);
        assertThat("duration is correct",
                te.getDuration(),
                equalTo(EIGHTH_NOTE));
        te = ts.get(1);
        assertThat("duration is correct",
                te.getDuration(),
                equalTo(SIXTEENTH_NOTE));
        te = ts.get(2);
        assertThat("duration is correct",
                te.getDuration(),
                equalTo(HALF_NOTE));
        te = ts.get(3);
        assertThat("duration is correct",
                te.getDuration(),
                equalTo(EIGHTH_NOTE));

        // the starts are not affected

    }

    @Test
    public void testRemoveInt() {
        TimeSeries ts = new TimeSeries();
        ts.add(EIGHTH_NOTE).add(SIXTEENTH_NOTE).add(HALF_NOTE).add(EIGHTH_NOTE);
        logger.debug("time series\n{}", ts);
        ts.remove(2);
        logger.debug("time series\n{}", ts);

        TimeEvent te = ts.get(2);
        assertThat("start is correct",
                te.getStartBeat(),
                equalTo(1d + EIGHTH_NOTE + SIXTEENTH_NOTE + HALF_NOTE));
        assertThat("duration is correct",
                te.getDuration(),
                equalTo(EIGHTH_NOTE));
    }

    @Test
    public void testRemoveTimeEvent() {
        TimeSeries ts = new TimeSeries();
        ts.add(EIGHTH_NOTE).add(EIGHTH_NOTE).add(EIGHTH_NOTE).add(EIGHTH_NOTE);
        TimeEvent te = new TimeEvent(10, SIXTEENTH_NOTE);
        ts.add(te);
        logger.debug("time series\n{}", ts);
        ts.remove(te);
        logger.debug("time series\n{}", ts);
    }

    @Test
    public void testRemoveFromDuration() {
        TimeSeries ts = new TimeSeries();
        ts.add(EIGHTH_NOTE).add(EIGHTH_NOTE).add(EIGHTH_NOTE).add(EIGHTH_NOTE);
        ts.removeFromDuration(SIXTEENTH_NOTE);

        for (Timed t : ts) {
            assertThat("durations are correct",
                    t.getDuration(),
                    equalTo(SIXTEENTH_NOTE));
        }

    }

    @Test
    public void testRemoveFromSilences() {
        TimeSeries ts = new TimeSeries();
        double start = 1d;
        ts.add(start++,
                EIGHTH_NOTE);
        ts.add(start++,
                EIGHTH_NOTE);
        ts.add(start++,
                EIGHTH_NOTE);
        ts.add(start++,
                EIGHTH_NOTE);
        logger.debug("time series\n{}", ts);
        TimeSeries silences = ts.getSilences();
        assertThat("there are 3 silences",
                silences.getSize(),
                equalTo(3));
        ts.removeFromSilences(SIXTEENTH_NOTE);
        logger.debug("time series\n{}", ts);

        start = 1d;
        for (TimeEvent te : ts) {
            // TODO check the start times
            // assertThat("start is correct",
            // te.getStartBeat(),
            // equalTo(start));
            // start++;
            // start -= SIXTEENTH_NOTE;
            assertThat("duration is correct",
                    te.getDuration(),
                    equalTo(EIGHTH_NOTE));
        }
    }

    @Test
    public void testRemoveOverlaps() {
        TimeSeries ts = new TimeSeries();
        double start = 1d;
        ts.add(start++,
                HALF_NOTE);
        ts.add(start++,
                HALF_NOTE);
        ts.add(start++,
                HALF_NOTE);
        ts.add(start++,
                HALF_NOTE);
        logger.debug("time series\n{}", ts);
        ts.removeOverlaps();
        logger.debug("time series\n{}", ts);

        start = 1d;
        for (TimeEvent te : ts) {
            assertThat("start is correct",
                    te.getStartBeat(),
                    equalTo(start));
            start += HALF_NOTE;
            assertThat("duration is correct",
                    te.getDuration(),
                    equalTo(HALF_NOTE));
        }

    }

    @Test
    public void testRemoveSilences() {
        TimeSeries ts = new TimeSeries();
        double start = 1d;
        ts.add(start++,
                EIGHTH_NOTE);
        ts.add(start++,
                EIGHTH_NOTE);
        ts.add(start++,
                EIGHTH_NOTE);
        ts.add(start++,
                EIGHTH_NOTE);
        logger.debug("time series\n{}", ts);
        TimeSeries silences = ts.getSilences();
        assertThat("there are 3 silences",
                silences.getSize(),
                equalTo(3));

        logger.debug("silences {}", silences);

        logger.debug("removed");
        ts.removeSilences();
        logger.debug("time series\n{}", ts);
        silences = ts.getSilences();
        logger.debug("silences {}", silences);
        assertThat("there are no silences",
                silences.getSize(),
                equalTo(0));

    }

    // @Test
    // public void testRemoveTimeSeriesListener() {
    // fail("Not yet implemented");
    // }

    @Test
    public void testSet() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);

        TimeEvent te = new TimeEvent(1, EIGHTH_NOTE);
        ts.set(te,
                0);

        TimeEvent te2 = ts.get(0);
        assertThat("te are equal",
                te2,
                equalTo(te));
        assertThat("duration is correct",
                te2.getDuration(),
                equalTo(EIGHTH_NOTE));

    }

    @Test
    public void changeStartBeat() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);

        fired = false;
        ts.addPropertyChangeListener(new PropertyChangeListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                assertThat(evt.getPropertyName(),
                        anyOf(equalTo(TimeSeries.MODIFIED),
                                equalTo(TimeSeries.START_BEAT_CHANGE)));
                if (evt.getPropertyName().equals(TimeSeries.MODIFIED)) {
                    fired = true;
                }
            }
        });

        ts.changeStartBeat(10);

        TimeEvent te2 = ts.get(0);
        assertThat("start is correct",
                te2.getStartBeat(),
                equalTo(10d));
        assertThat("duration is correct",
                te2.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        assertThat("property change event was fired",
                fired,
                equalTo(true));

        logger.debug("time series\n{}", ts);

    }

    @Test
    public void changeStartBeatIndex() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);

        ts.addPropertyChangeListener(new PropertyChangeListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                assertThat(evt.getPropertyName(),
                        anyOf(equalTo(TimeSeries.MODIFIED),
                                equalTo(TimeSeries.START_BEAT_CHANGE)));
            }
        });

        ts.changeStartBeat(0,
                10);

        TimeEvent te2 = ts.get(0);
        assertThat("start is correct",
                te2.getStartBeat(),
                equalTo(10d));
        assertThat("duration is correct",
                te2.getDuration(),
                equalTo(SIXTEENTH_NOTE));
        logger.debug("time series\n{}", ts);

    }

    @Test
    public void testSetDuration() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);
        ts.setDuration(HALF_NOTE);
        logger.debug("time series\n{}", ts);
        for (TimeEvent te : ts) {
            assertThat("duration is correct",
                    te.getDuration(),
                    equalTo(HALF_NOTE));
        }

    }

    @Test
    public void testSetSilences() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);
        TimeSeries silences = ts.getSilences();
        assertThat("no silences",
                silences.getSize(),
                equalTo(0));
        logger.debug("time series\n{}", ts);
        logger.debug("silences \n{}", silences);

        ts.setSilences(HALF_NOTE);
        logger.debug("time series\n{}", ts);
        silences = ts.getSilences();
        logger.debug("silences \n{}", silences);
        assertThat("no silences",
                silences.getSize(),
                equalTo(3));
    }

    // its already sorted
    // @Test
    // public void testSort() {
    // TimeSeries ts = new TimeSeries();
    // ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
    // .add(SIXTEENTH_NOTE);
    // ts.sort();
    // }

    /**
     * import static com.rockhoppertech.music.Pitch.*; import static
     * com.rockhoppertech.music.Duration.*;
     */
    @Test
    public void applyWithMask() {
        MIDITrack source = new MIDITrack("C D E");
        int numberOfRepeats = 3;
        MIDITrack notelist = MIDITrackFactory.repeat(source,
                numberOfRepeats);

        TimeSeries ts = new TimeSeries("s e");
        assertThat(ts.getStartTimes().get(0),
                equalTo(1d));

        CircularList<Integer> mask = new CircularArrayList<Integer>();
        mask.add(2);
        mask.add(1);
        // notelist is just a bunch of pitches.
        // now repeat some pitches according to the mask and apply the time
        // series' durations
        MIDITrack applied = ts.apply(notelist,
                mask);
        logger.debug("applied {}", applied);
        // logger.debug();
        // logger.debug(applied.sequential());

    }

    @Test
    public void applyToBoth() {

        MIDITrack source = new MIDITrack("C D E");
        int numberOfRepeats = 3;
        MIDITrack notelist = MIDITrackFactory.repeat(source,
                numberOfRepeats);

        TimeSeries ts = new TimeSeries("e st");
        assertThat(ts.getStartTimes().get(0),
                equalTo(1d));

        CircularList<Integer> mask = new CircularArrayList<Integer>();
        mask.add(1);
        mask.add(3);
        // logger.debug("masked:");
        // TimeSeries masked = TimeSeriesFactory.createRepeated(ts, mask);
        // logger.debug(masked);

        // logger.debug("copies:");
        // TimeSeries copies = masked.nCopies(3);
        // logger.debug(copies);

        logger.debug("result:");
        MIDITrack applied = ts.applyToBoth(notelist,
                mask);
        logger.debug("applied {}", applied);

    }

    @Test
    public void ncopies() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);
        ts.add(EIGHTH_NOTE).add(EIGHTH_NOTE);
        logger.debug("time series\n{}", ts);
        logger.debug("end beat {}", ts.getEndBeat());
        logger.debug("size {}", ts.getSize());

        TimeSeries appended = ts.nCopies(5);
        assertThat("Not null",
                appended,
                notNullValue());
        logger.debug("appended\n{}", appended);
        logger.debug("appended end {}", appended.getEndBeat());
        logger.debug("appended size {}", appended.getSize());

        // 6 notes * 5
        assertThat(appended.getSize(),
                equalTo(30));

        for (int i = 1; i < appended.getSize(); i++) {
            Timed t1 = appended.get(i - 1);
            Timed t2 = appended.get(i);
            assertThat("start beats are in order",
                    t1.getStartBeat(),
                    lessThan(t2.getStartBeat()));

            assertThat("sequence is correct",
                    TimeSeries.isAfter(t1,
                            t2),
                    equalTo(true));
        }
    }

    @Test
    public void shouldModifyJustDurationsByAddingSingleValue() {
        TimeSeries ts = this.get4Sixteenths();

        double value = 1d;
        ts.modifyDurations(Operation.ADD,
                value);
        double expectedDuration = SIXTEENTH_NOTE + value;

        double expectedStart = 1d;
        Timed e = ts.get(0);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(expectedDuration));

        expectedStart += SIXTEENTH_NOTE;
        e = ts.get(1);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(expectedDuration));

        expectedStart += SIXTEENTH_NOTE;
        e = ts.get(2);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(expectedDuration));

        expectedStart += SIXTEENTH_NOTE;
        e = ts.get(3);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(expectedDuration));
    }

    @Test
    public void shouldModifyJustDurationsByAddingMultipleValues() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);

        Timed e = ts.get(0);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1d));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));
        e = ts.get(1);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1.25));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = ts.get(2);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1.5));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = ts.get(3);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1.75));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        ts.modifyDurations(Operation.ADD,
                1d,
                1.5);
        double expectedDuration = SIXTEENTH_NOTE + 1d;
        double expectedDuration2 = SIXTEENTH_NOTE + 1.5;

        e = ts.get(0);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1d));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(expectedDuration));
        e = ts.get(1);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1.25));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(expectedDuration2));

        e = ts.get(2);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1.5));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(expectedDuration));
        e = ts.get(3);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1.75));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(expectedDuration2));
    }

    @Test
    public void shouldModifyJustStartsByAddingSingleValue() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);

        Timed e = ts.get(0);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1d));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));
        e = ts.get(1);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1.25));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = ts.get(2);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1.5));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = ts.get(3);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(1.75));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        ts.modifyStarts(Operation.ADD,
                1d);

        e = ts.get(0);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(2d));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = ts.get(1);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(2.25));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = ts.get(2);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(2.5));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = ts.get(3);
        assertThat("start is correct",
                e.getStartBeat(),
                equalTo(2.75));
        assertThat("duration is correct",
                e.getDuration(),
                equalTo(SIXTEENTH_NOTE));
    }

    @Test
    public void append() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);
        TimeSeries ts2 = new TimeSeries();
        ts2.add(EIGHTH_NOTE).add(EIGHTH_NOTE);

        TimeSeries appended = ts.append(ts2);
        assertThat("Not null",
                appended,
                notNullValue());
        logger.debug("appended\n{}", appended);

        Timed e = appended.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = appended.get(1);
        assertThat(e.getStartBeat(),
                equalTo(1.25));
        assertThat(e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = appended.get(2);
        assertThat(e.getStartBeat(),
                equalTo(1.5));
        assertThat(e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = appended.get(3);
        assertThat(e.getStartBeat(),
                equalTo(1.75));
        assertThat(e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = appended.get(4);
        assertThat(e.getStartBeat(),
                equalTo(2d));
        assertThat(e.getDuration(),
                equalTo(EIGHTH_NOTE));

        e = appended.get(5);
        assertThat(e.getStartBeat(),
                equalTo(2.5));
        assertThat(e.getDuration(),
                equalTo(EIGHTH_NOTE));

    }

    @Test
    public void appendStatic() {
        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);
        TimeSeries ts2 = new TimeSeries();
        ts2.add(EIGHTH_NOTE).add(EIGHTH_NOTE);

        TimeSeries appended = TimeSeries.append(ts,
                ts2);
        assertThat("Not null",
                appended,
                notNullValue());
        logger.debug("track \n{}", appended);

        Timed e = appended.get(0);
        assertThat(e.getStartBeat(),
                equalTo(1d));
        assertThat(e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = appended.get(1);
        assertThat(e.getStartBeat(),
                equalTo(1.25));
        assertThat(e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = appended.get(2);
        assertThat(e.getStartBeat(),
                equalTo(1.5));
        assertThat(e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = appended.get(3);
        assertThat(e.getStartBeat(),
                equalTo(1.75));
        assertThat(e.getDuration(),
                equalTo(SIXTEENTH_NOTE));

        e = appended.get(4);
        assertThat(e.getStartBeat(),
                equalTo(2d));
        assertThat(e.getDuration(),
                equalTo(EIGHTH_NOTE));

        e = appended.get(5);
        assertThat(e.getStartBeat(),
                equalTo(2.5));
        assertThat(e.getDuration(),
                equalTo(EIGHTH_NOTE));

    }

    @Test
    public void apply() {
        MIDITrack notelist = new MIDITrack();
        notelist.add(C5,
                D5,
                E5);
        notelist.add(C5,
                D5,
                E5);
        notelist.add(C5,
                D5,
                E5);
        TimeSeries ts = new TimeSeries();
        ts.add(EIGHTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE);
        ts.apply(notelist);

        logger.debug("track \n{}", notelist);

        notelist.clear();
        notelist.add(C5,
                D5,
                E5,
                F5);
        notelist.add(C5,
                D5,
                E5,
                F5);
        ts.apply(notelist);
        logger.debug("track \n{}", notelist);

    }

    @Test
    public void mask() {
        MIDITrack notelist = new MIDITrack();
        notelist.add(C5,
                D5,
                E5);
        notelist.add(C5,
                D5,
                E5);
        notelist.add(C5,
                D5,
                E5);
        CircularList<Integer> mask = new CircularArrayList<Integer>();
        mask.add(2);
        mask.add(1);

        TimeSeries ts = new TimeSeries();
        ts.add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE).add(SIXTEENTH_NOTE)
                .add(SIXTEENTH_NOTE);
        ts.add(EIGHTH_NOTE).add(EIGHTH_NOTE);

        logger.debug("track \n{}", notelist);
        MIDITrack applied = ts.apply(notelist,
                mask);
        assertThat("Not null",
                applied,
                notNullValue());

        logger.debug("{}", ts);
        logger.debug("{}", applied);

    }

    @Test
    public void apply2() {
        MIDITrack notelist = new MIDITrack();
        notelist.add(new MIDINote(Pitch.C5, 1.5, EIGHTH_NOTE));
        notelist.add(new MIDINote(Pitch.C5, 2.0, SIXTEENTH_NOTE));
        notelist.add(new MIDINote(Pitch.C5, 2.5, SIXTEENTH_NOTE));
        notelist.add(new MIDINote(Pitch.C5, 5.5, Duration
                .getDotted(QUARTER_NOTE)));
        TimeSeries ts = new TimeSeries();
        ts.add(new TimeEvent(1d, EIGHTH_NOTE));
        ts.add(new TimeEvent(2d, EIGHTH_NOTE));
        ts.add(new TimeEvent(3d, EIGHTH_NOTE));
        ts.add(new TimeEvent(4d, EIGHTH_NOTE));

        // now the thing we're testing
        ts.apply(notelist);

        Timed e = ts.get(0);
        MIDINote note = notelist.get(0);
        assertThat(e.getStartBeat(),
                equalTo(note.getStartBeat()));
        assertThat(e.getDuration(),
                equalTo(note.getDuration()));

        e = ts.get(1);
        note = notelist.get(1);
        assertThat(e.getStartBeat(),
                equalTo(note.getStartBeat()));
        assertThat(e.getDuration(),
                equalTo(note.getDuration()));

        e = ts.get(2);
        note = notelist.get(2);
        assertThat(e.getStartBeat(),
                equalTo(note.getStartBeat()));
        assertThat(e.getDuration(),
                equalTo(note.getDuration()));

        e = ts.get(3);
        note = notelist.get(3);
        assertThat(e.getStartBeat(),
                equalTo(note.getStartBeat()));
        assertThat(e.getDuration(),
                equalTo(note.getDuration()));
        // /////////
        // unqual size
        ts = new TimeSeries();
        ts.add(new TimeEvent(1d, .5));
        ts.add(new TimeEvent(2d, 2.5));
        ts.add(new TimeEvent(3d, .5));
        ts.apply(notelist);
        logger.debug("track\n{}", notelist);

        e = ts.get(0);
        note = notelist.get(0);
        assertThat(e.getStartBeat(),
                equalTo(note.getStartBeat()));
        assertThat(e.getDuration(),
                equalTo(note.getDuration()));

        e = ts.get(1);
        note = notelist.get(1);
        assertThat(e.getStartBeat(),
                equalTo(note.getStartBeat()));
        assertThat(e.getDuration(),
                equalTo(note.getDuration()));

        e = ts.get(2);
        note = notelist.get(2);
        assertThat(e.getStartBeat(),
                equalTo(note.getStartBeat()));
        assertThat(e.getDuration(),
                equalTo(note.getDuration()));

    }

    @Test
    public void serialize() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString(".5 1");
        File f = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        TimeSeries ts2 = null;
        try {
            f = File.createTempFile("tempTimeSeries", "ser");
            out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(ts);
            out.close();
            in = new ObjectInputStream(new FileInputStream(f));
            ts2 = (TimeSeries) in.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.debug("ts \n{}", ts);
        logger.debug("ts2 \n{}", ts2);
        assertThat("the serialization worked",
                ts,
                equalTo(ts2));

    }

    @Test
    public void equals() {
        TimeSeries ts = TimeSeriesFactory.createFromDurationString("e e e e");
        TimeSeries ts2 = TimeSeriesFactory.createFromDurationString("e e e e");
        boolean actual = ts.equals(ts2);
        boolean expected = true;
        assertThat("equals worked",
                actual, is(equalTo(expected)));

        actual = ts.equals(ts);
        expected = true;
        assertThat("equals worked",
                actual, is(equalTo(expected)));

        actual = ts.equals(null);
        expected = false;
        assertThat("equals worked",
                actual, is(equalTo(expected)));

        actual = ts.equals("this is not a time series");
        expected = false;
        assertThat("equals worked",
                actual, is(equalTo(expected)));
    }
}
