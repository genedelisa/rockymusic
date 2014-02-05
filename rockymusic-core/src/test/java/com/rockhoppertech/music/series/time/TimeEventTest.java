/**
 * 
 */
package com.rockhoppertech.music.series.time;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Range;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class TimeEventTest {
    static Logger logger = LoggerFactory.getLogger(TimeEventTest.class);
    /**
     * For testing property change events.
     */
    private boolean fired;

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#TimeEvent()}.
     */
    @Test
    public void testTimeEvent() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        double actual = event.getStartBeat();
        double expected = 1d;
        assertThat("the start beat is correct",
                actual, is(equalTo(expected)));
        actual = event.getDuration();
        expected = 1d;
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
        actual = event.getEndBeat();
        expected = 2d;
        assertThat("the end beat is correct",
                actual, is(equalTo(expected)));
        Range<Double> range = event.getOpenRange();
        Range<Double> expectedRange = Range.open(1d, 2d);
        assertThat("the range is correct",
                range, is(equalTo(expectedRange)));
        range = event.getClosedRange();
        expectedRange = Range.closed(1d, 2d);
        assertThat("the range is correct",
                range, is(equalTo(expectedRange)));

        assertThat("the simple string is correct",
                event.getSimpleString(), is(equalTo("1.000000,1.000000")));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#TimeEvent(double, double)}
     * .
     */
    @Test
    public void testTimeEventDoubleDouble() {
        TimeEvent event = new TimeEvent(2.5, 2d);
        assertThat("event is not null",
                event, is(notNullValue()));
        double actual = event.getStartBeat();
        double expected = 2.5;
        assertThat("the start beat is correct",
                actual, is(equalTo(expected)));
        actual = event.getDuration();
        expected = 2d;
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
        actual = event.getEndBeat();
        expected = 4.5;
        assertThat("the end beat is correct",
                actual, is(equalTo(expected)));
        Range<Double> range = event.getOpenRange();
        Range<Double> expectedRange = Range.open(2.5, 4.5);
        assertThat("the range is correct",
                range, is(equalTo(expectedRange)));
        range = event.getClosedRange();
        expectedRange = Range.closed(2.5, 4.5);
        assertThat("the range is correct",
                range, is(equalTo(expectedRange)));

        assertThat("the simple string is correct",
                event.getSimpleString(), is(equalTo("2.500000,2.000000")));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#getStartBeat()}.
     */
    @Test
    public void testGetStartBeat() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        double actual = event.getStartBeat();
        double expected = 1d;
        assertThat("the start is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#setStartBeat(double)}
     * .
     */
    @Test
    public void testSetStartBeat() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        event.setStartBeat(5.5);

        double actual = event.getStartBeat();
        double expected = 5.5;
        assertThat("the start beat is correct",
                actual, is(equalTo(expected)));
        actual = event.getDuration();
        expected = 1d;
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
        actual = event.getEndBeat();
        expected = 6.5;
        assertThat("the end beat is correct",
                actual, is(equalTo(expected)));
        Range<Double> range = event.getOpenRange();
        Range<Double> expectedRange = Range.open(5.5, 6.5);
        assertThat("the range is correct",
                range, is(equalTo(expectedRange)));
        range = event.getClosedRange();
        expectedRange = Range.closed(5.5, 6.5);
        assertThat("the range is correct",
                range, is(equalTo(expectedRange)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#getDuration()}.
     */
    @Test
    public void testGetDuration() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        double actual = event.getDuration();
        double expected = 1d;
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#setDuration(double)}
     * .
     */
    @Test
    public void testSetDuration() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        event.setDuration(8d);
        // also changes end beat and ranges

        double actual = event.getStartBeat();
        double expected = 1d;
        assertThat("the start beat is correct",
                actual, is(equalTo(expected)));
        actual = event.getDuration();
        expected = 8d;
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
        actual = event.getEndBeat();
        expected = 9d;
        assertThat("the end beat is correct",
                actual, is(equalTo(expected)));
        Range<Double> range = event.getOpenRange();
        Range<Double> expectedRange = Range.open(1d, 9d);
        assertThat("the range is correct",
                range, is(equalTo(expectedRange)));
        range = event.getClosedRange();
        expectedRange = Range.closed(1d, 9d);
        assertThat("the range is correct",
                range, is(equalTo(expectedRange)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#getOpenRange()}.
     */
    @Test
    public void testGetOpenRange() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        Range<Double> range = event.getOpenRange();
        Range<Double> expectedRange = Range.open(1d, 2d);
        assertThat("the range is correct",
                range, is(equalTo(expectedRange)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#getClosedRange()}.
     */
    @Test
    public void testGetClosedRange() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        Range<Double> range = event.getClosedRange();
        Range<Double> expectedRange = Range.closed(1d, 2d);
        assertThat("the range is correct",
                range, is(equalTo(expectedRange)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#includes(com.rockhoppertech.music.series.time.TimeEvent)}
     * .
     */
    @Test
    public void testIncludes() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        TimeEvent event2 = new TimeEvent(5d, 1d);
        assertThat("event is not null",
                event2, is(notNullValue()));
        logger.debug("event 1 {}", event);
        logger.debug("event 2 {}", event2);

        boolean actual = event.includes(event2);
        boolean expected = false;
        assertThat("the include is correct",
                actual, is(equalTo(expected)));
        // false (1.0‥2.0) (5.0‥6.0)

        event2 = new TimeEvent(1.5d, 1d);
        assertThat("event is not null",
                event2, is(notNullValue()));
        logger.debug("event 1 {}", event);
        logger.debug("event 2 {}", event2);
        actual = event.includes(event2);
        expected = false;
        assertThat("the include is correct",
                actual, is(equalTo(expected)));
        // false (1.0‥2.0) (1.5‥2.5)

        event2 = new TimeEvent(1.5d, .5d);
        assertThat("event is not null",
                event2, is(notNullValue()));
        logger.debug("event 1 {}", event);
        logger.debug("event 2 {}", event2);
        actual = event.includes(event2);
        expected = true;
        assertThat("the include is correct",
                actual, is(equalTo(expected)));
        // true (1.0‥2.0) (1.5‥2.0)
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#overlaps(com.rockhoppertech.music.series.time.TimeEvent)}
     * .
     */
    @Test
    public void testOverlaps() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        TimeEvent event2 = new TimeEvent(5d, 1d);
        assertThat("event is not null",
                event2, is(notNullValue()));
        logger.debug("event 1 {}", event);
        logger.debug("event 2 {}", event2);
        boolean actual = event.overlaps(event2);
        boolean expected = false;
        assertThat("the overlap is correct",
                actual, is(equalTo(expected)));

        event2 = new TimeEvent(1.5d, 1d);
        assertThat("event is not null",
                event2, is(notNullValue()));
        logger.debug("event 1 {}", event);
        logger.debug("event 2 {}", event2);
        actual = event.overlaps(event2);
        expected = true;
        assertThat("the overlap is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#toString()}.
     */
    @Test
    public void testToString() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("event string is not null",
                event.toString(), is(notNullValue()));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#setEndBeat(double)}
     * .
     */
    @Test
    public void testSetEndBeat() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        event.setEndBeat(5d);
        logger.debug("event {}", event);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#getEndBeat()}.
     */
    @Test
    public void testGetEndBeat() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        double actual = event.getEndBeat();
        double expected = 2d;
        assertThat("the end is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#addPropertyChangeListener(java.beans.PropertyChangeListener)}
     * .
     */

    @Test
    public void testAddPropertyChangeListener() {
        TimeEvent event = new TimeEvent();
        this.fired = false;
        event.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                logger.debug("event {}", evt);
                if (evt.getPropertyName().equals(TimeEvent.DURATION))
                    fired = true;
            }
        });
        event.setDuration(2d);
        assertThat("the event fired a property change event",
                fired, is(equalTo(true)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#removePropertyChangeListener(java.beans.PropertyChangeListener)}
     * .
     */
    @Test
    public void testRemovePropertyChangeListener() {
        TimeEvent event = new TimeEvent();
        this.fired = false;
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                logger.debug("event {}", evt);
                if (evt.getPropertyName().equals(TimeEvent.DURATION))
                    fired = true;
            }
        };
        event.addPropertyChangeListener(listener);
        event.setDuration(2d);
        assertThat("the event fired a property change event",
                fired, is(equalTo(true)));

        this.fired = false;
        event.removePropertyChangeListener(listener);
        event.setDuration(2d);
        assertThat("no one listend to the event's property change event",
                fired, is(equalTo(false)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.series.time.TimeEvent#getSimpleString()}.
     */
    @Test
    public void testGetSimpleString() {
        TimeEvent event = new TimeEvent();
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("event string is not null",
                event.getSimpleString(), is(notNullValue()));
        assertThat("the simple string is correct",
                event.getSimpleString(), is(equalTo("1.000000,1.000000")));
    }

}
