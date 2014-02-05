package com.rockhoppertech.music.series.time;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Range;
import com.rockhoppertech.music.Timed;

/**
 * <code>TimeEvent</code> is a musical event, not a JavaBean event. But it does
 * fire JavaBean property changes
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @see Serializable
 * @see Timed
 */
public class TimeEvent implements Serializable, Timed {
    /**
     * Serialization.
     */
    private static final long serialVersionUID = 1L;
    static Logger logger = LoggerFactory.getLogger(TimeEvent.class);
    private double startBeat;
    private double duration;
    private transient PropertyChangeSupport changes;
    /**
     * JavaBean property name.
     */
    public static final String DURATION = "TimeEvent.DURATION";
    /**
     * JavaBean property name.
     */
    public static final String START = "TimeEvent.START";
    /**
     * JavaBean property name.
     */
    public static final String END = "TimeEvent.END";

    /**
     * Initialize a {@code TimeEvent} with start beat = duration = 1d.
     */
    public TimeEvent() {
        this(1d, 1d);
    }

    /**
     * Initialize a {@code TimeEvent} with the given start beat and duration.
     * 
     * @param start
     *            must be >= 1
     * @param duration
     *            the duration
     */
    public TimeEvent(double start, double duration) {
        if (start < 1d)
            throw new IllegalArgumentException("Time cannot be before 1 "
                    + start);
        if (duration <= 0d)
            throw new IllegalArgumentException("Duration cannot be <= 0 "
                    + duration);
        this.startBeat = start;
        this.duration = duration;
        this.changes = new PropertyChangeSupport(this);
    }

    /**
     * Clone is evil.
     * 
     * @return a {@code TimeEvent}
     */
    public TimeEvent duplicate() {
        TimeEvent e = new TimeEvent(this.startBeat, this.duration);
        return e;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        // our "pseudo-constructor"
        in.defaultReadObject();
        // now we are a "live" object again
        this.changes = new PropertyChangeSupport(this);
    }

    public double getStartBeat() {
        return this.startBeat;
    }

    public void setStartBeat(double d) {
        double old = this.startBeat;
        this.startBeat = d;
        this.changes.firePropertyChange(START, old, this.duration);
    }

    public double getDuration() {
        return this.duration;
    }

    public void setDuration(double d) {
        double old = this.duration;
        this.duration = d;
        this.changes.firePropertyChange(DURATION, old, this.duration);
    }

    /**
     * @return an open {@code Range}
     */
    public Range<Double> getOpenRange() {
        return Range.open(this.startBeat, this.startBeat + this.duration);
    }

    /**
     * @return a closed {@code Range}
     */
    public Range<Double> getClosedRange() {
        return Range.closed(this.startBeat, this.startBeat + this.duration);
    }

    // https://code.google.com/p/guava-libraries/wiki/RangesExplained
    public boolean includes(TimeEvent te) {
        boolean b = this.getOpenRange().encloses(te.getOpenRange());

        logger.debug(String.format(
                "%b %s %s",
                b,
                this.getOpenRange(),
                te.getOpenRange()));

        return b;
    }

    public boolean overlaps(TimeEvent te) {
        // boolean b = this.getRange().overlaps(te.getRange());
        boolean b = false;
        Range<Double> range1 = this.getOpenRange();
        Range<Double> range2 = te.getOpenRange();
        if (range1.contains(range2.lowerEndpoint()) ||
                range1.contains(range2.upperEndpoint())) {
            b = true;
        }

        logger.debug(String.format(
                "overlaps %b %s %s",
                b,
                this.getOpenRange(),
                te.getOpenRange()));

        return b;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append(" startbeat=").append(this.startBeat);
        sb.append(" duration=").append(this.duration);
        sb.append(" range=").append(this.getOpenRange());
        sb.append(" exact range=").append(this.getClosedRange());
        sb.append(" end=").append(this.getEndBeat());
        return sb.toString();
    }

    public void setEndBeat(double endTime) {
        double old = this.getEndBeat();
        this.setDuration(endTime - this.startBeat);
        this.changes.firePropertyChange(END, old, this.duration);
    }

    public double getEndBeat() {
        return this.startBeat + this.duration;
    }

    public String getSimpleString() {
        return String.format("%f,%f", this.getStartBeat(), this.getDuration());
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.changes.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(
            final PropertyChangeListener listener) {
        this.changes.removePropertyChangeListener(listener);
    }

}

/*
 * insert silence series map over list and change start times keep existing
 * silences?
 * 
 * expand/contract current silence series if non overlapping notes, it's easy
 */
