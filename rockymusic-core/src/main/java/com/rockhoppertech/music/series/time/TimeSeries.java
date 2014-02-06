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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.collections.CircularList;
import com.rockhoppertech.collections.ListUtils;
import com.rockhoppertech.music.DurationParser;
import com.rockhoppertech.music.Note;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDIPerformer;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackFactory;
import com.rockhoppertech.music.modifiers.DurationModifier;
import com.rockhoppertech.music.modifiers.Modifier.Operation;
import com.rockhoppertech.music.modifiers.StartBeatModifier;
import com.rockhoppertech.music.modifiers.TimedModifier;

//import com.rockhoppertech.series.Permutations;
//import com.rockhoppertech.series.time.modifiers.criteria.TimeModifierCriteria;

/**
 * Class <code>TimeSeries</code> is a series of {@code TimeEvents}.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 */
public class TimeSeries implements Iterable<TimeEvent>, Serializable {
    /**
     * Serialization.
     */
    private static final long serialVersionUID = 3559349459462266457L;
    static Logger logger = LoggerFactory.getLogger(TimeSeries.class);

    public static final String END_BEAT_CHANGE = "TimeSeries.END_BEAT_CHANGE";
    public static final String DURATION_CHANGE = "TimeSeries.DURATION_CHANGE";
    public static final String START_BEAT_CHANGE = "TimeSeries.START_BEAT_CHANGE";
    public static final String MODIFIED = "TimeSeries.MODIFIED";

    private transient RangeSet<Double> rangeSet = TreeRangeSet.create();
    private CircularArrayList<TimeEvent> list = new CircularArrayList<TimeEvent>();
    private List<Range<Double>> ranges;
    private List<Range<Double>> exactRanges;
    private String name = "Untitled";
    private String description;
    // private int index;
    private transient PropertyChangeSupport changes;

    /**
     * Series are sorted when a time event is added.
     */
    private transient Comparator<? super TimeEvent> timeComparator = new TimeComparator();

    /**
     * Initialize a new empty series.
     */
    public TimeSeries() {
        // this.list = Collections.synchronizedList( new LinkedList() );
        // this.list = new ArrayList<TimeEvent>(); // we do lots of gets/sets
        this.ranges = new ArrayList<Range<Double>>();
        this.exactRanges = new ArrayList<Range<Double>>();
        this.changes = new PropertyChangeSupport(this);
    }

    /**
     * Extract time events from a track to initialize this series.
     * 
     * @param track
     *            a {@code MIDITrack}
     */
    public TimeSeries(final MIDITrack track) {
        this();
        // this.list = new ArrayList<TimeEvent>();
        for (final Note n : track) {
            this.add(new TimeEvent(n.getStartBeat(), n.getDuration()));
        }
    }

    /**
     * Copy Constructor. Clone is evil.
     * 
     * @param timeSeries
     *            a {@code TimeSeries} to copy
     */
    public TimeSeries(final TimeSeries timeSeries) {
        this();
        for (final TimeEvent te : timeSeries.list) {
            this.list.add(new TimeEvent(te.getStartBeat(), te.getDuration()));
        }
        this.sort();
    }

    /**
     * Initialize a series from a duration string.
     * 
     * @param durationString
     *            a duration string
     * @see DurationParser
     */
    public TimeSeries(String durationString) {
        this();
        TimeSeriesFactory.createFromDurationString(this, durationString);
    }

   

    /**
     * Creates a new {@code TimeEvent} with the specified duration, and adds it
     * to the series.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param duration
     * @return this to cascade calls
     */
    public TimeSeries add(final double duration) {
        final double start = this.getEndBeat();
        return this.add(new TimeEvent(start, duration));
    }

    /**
     * Creates a new {@code TimeEvent} with the specified start andduration, and
     * adds it to the series.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param start a start time
     * @param duration a duration
     * @return this to cascade calls
     */
    public TimeSeries add(final double start, final double duration) {
        logger.debug(
                "adding a new time event, start {} durtion {}",
                start,
                duration);
        return this.add(new TimeEvent(start, duration));
    }

    /**
     * Adds the TimeEvent at its given time. (i.e. the series is resorted)
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param t
     */
    public TimeSeries add(final TimeEvent t) {
        logger.debug("adding a new time event {}", t);
        this.list.add(t);
        Collections.sort(this.list,
                this.timeComparator);
        this.addRange(t.getOpenRange());
        // this.addExactRange(t.getClosedRange());
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
        return this;
    }

    /**
     * Append a {@code TimeSeries} to another. Neither series are modified. A
     * new instance is returned.
     * 
     * @param ts1
     *            the first {@code TimeSeries}
     * @param ts2
     *            the second {@code TimeSeries}
     * @return a new {@code TimeSeries}
     */
    public static TimeSeries append(final TimeSeries ts1, final TimeSeries ts2) {
        final TimeSeries ts = new TimeSeries(ts1);
        return ts.append(ts2);
    }

    /**
     * Create a series from a track.
     * 
     * @param track
     *            the source {@code MIDITrack}
     * @return a new {@code TimeSeries}
     */
    public static TimeSeries extract(final MIDITrack track) {
        final TimeSeries ts = new TimeSeries();
        for (final MIDINote note : track) {
            final TimeEvent te = new TimeEvent(note.getStartBeat(), note
                    .getDuration());
            ts.add(te);
        }
        return ts;
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.changes.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(final String propertyName,
            final PropertyChangeListener listener) {
        this.changes.addPropertyChangeListener(propertyName,
                listener);
    }

    /**
     * Adds a range. No TimeEvents are created here. This is called internally
     * when an event is added.
     * 
     * @param r
     *            a {@code Range}
     * @return this to cascade calls
     */
    private TimeSeries addRange(final Range<Double> r) {
        this.ranges.add(r);
        rangeSet.add(r);
        return this;
    }

    // private TimeSeries addRange(final Range<Double> r) {
    //
    // // first one
    // if (this.ranges.isEmpty()) {
    // this.ranges.add(r);
    // } else {
    // // if the new even overlaps an existing event,
    // // merge the 2 ranges into one
    // boolean added = false;
    // for (final Range<Double> r1 : this.ranges) {
    // // logger.debug("r1 " + r1 + " new r " + r);
    //
    // if (r1.overlaps(r)) {
    // logger.debug("overlaps");
    // r1.setMax(Math.max(r1.upperEndpoint(),
    // r.upperEndpoint()));
    // added = true;
    // }
    // }
    // // otherwise just add it
    // if (added == false) {
    // this.ranges.add(r);
    // // logger.debug("just adding r2 " + r);
    // }
    // }
    // return this;
    // }

    // private TimeSeries addExactRange(final Range<Double> r) {
    // // first one
    // if (this.exactRanges.isEmpty()) {
    // this.exactRanges.add(r);
    // } else {
    //
    // r.containsAll(r.);
    //
    // r.containsAll(Ints.asList(1, 2, 3));
    // //Range.closed(3, 5).span(Range.open(5, 10)); // returns [3, 10)
    //
    // Range
    // // if the new even overlaps an existing event,
    // // merge the 2 ranges into one
    // boolean added = false;
    // for (final Range<Double> r1 : this.exactRanges) {
    // // logger.debug("r1 " + r1 + " new r " + r);
    // if (r1.overlaps(r)) {
    // logger.debug("overlaps");
    // r1.setMax(Math.max(r1.getMax(),
    // r.getMax()));
    // added = true;
    // }
    // }
    // // otherwise just add it
    // if (added == false) {
    // this.exactRanges.add(r);
    // // logger.debug("just adding r2 " + r);
    // }
    // }
    // return this;
    // }

    /**
     * Creates a DurationModifier to add a value to each duration.
      * <p>
     * Uses the {@link #modifyDurations(com.rockhoppertech.music.modifiers.Modifier.Operation, Double[]) modifyDurations}
     * method.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * Same as doing this:
     * 
     * <pre> 
     *  <code>
        DurationModifier dm = new DurationModifier(Operation.ADD, d);
        ts.map(dm);
        </code>
        </pre>
     * 
     * @param d
     *            an amount to add
     * @see DurationModifier
     */
    public void addToDuration(final double d) {
        logger.debug("addToDuration {}", d);
        modifyDurations(Operation.ADD, d);

        // final DurationModifier dm = new DurationModifier(Operation.ADD, d);
        // this.map(dm);
        // this.computeRanges();
        // this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Creates a {@code DurationModifier} to perform the specified operation
     * with the specified values.
     
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param operation
     *            a modifier operation
     * @param values
     *            the values to use
     * @see com.rockhoppertech.music.modifiers.Modifier.Operation
     * @see DurationModifier
     */
    public void modifyDurations(Operation operation, Double... values) {
        final DurationModifier dm = new DurationModifier(operation, values);
        this.map(dm);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Createa a {@code StartBeatModifier} to perform the specified operation
     * with the specified values.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param operation
     *            modifier operation
     * @param values
     *            values to use
     * @see StartBeatModifier
     */
    public void modifyStarts(Operation operation, Double... values) {
        final StartBeatModifier dm = new StartBeatModifier(operation, values);
        this.map(dm);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param d
     *            an amount to add
     * @return this to cascade calls
     */
    public TimeSeries addToSilences(final double d) {
        logger.debug("addToSilences Before" + d);
        logger.debug("list {}", this.list);
        double lastTime = 1d;
        double newTime = 0d;
        for (final Timed te : this.list) {
            final double t = te.getStartBeat();
            logger.debug("doing " + te);

            // currrent time minus the end time of the last note
            final double delta = t - lastTime;
            logger.debug("delta " + delta);
            if (delta > 0) {
                final double time = newTime + delta + d;
                te.setStartBeat(time);
                logger.debug("changed time to " + time);
            }
            logger.debug("done " + te);

            // the new time
            newTime = (te.getStartBeat() + te.getDuration());
            // the original time, not the new one
            lastTime = (t + te.getDuration());

            logger.debug("new lt " + lastTime);
        }
        logger.debug("addToSilences After");
        logger.debug("list {}", this.list);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
        return this;
    }

    /**
     * A deep copy of the series is appended to this.
     * 
     * {@code ts.append(ts2).append(ts3);}
     * 
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param ts1
     *            not modified at all
     * @return this to cascade calls
     */
    public TimeSeries append(final TimeSeries ts1) {
        // don't change the one passed in
        final TimeSeries ts = new TimeSeries(ts1);
        final StartBeatModifier m = new StartBeatModifier(this.getEndBeat()
                - ts.getStartBeat());
        m.setOperation(Operation.ADD);
        ts.map(m);

        for (final TimeEvent te : ts.list) {
            this.list.add(new TimeEvent(te.getStartBeat(), te.getDuration()));
        }
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
        return this;
    }

    /**
     * Apply this time series to the track. The track's events have their start
     * and durations changed.
     * 
     * @param track
     *            is modified according to the timeseries
     */
    public void apply(final MIDITrack track) {
        final double nsize = track.size();
        final double tsize = this.getSize();
        final int n = (int) Math.round(nsize / tsize + .5);
        logger.debug("nsize {} tsize {} n {}\n",
                nsize,
                tsize,
                n);
        final TimeSeries ts = this.nCopies(n);
        // ts.sequential();
        for (final MIDINote note : track) {
            final Timed te = ts.nextTimeEvent();
            note.setStartBeat(te.getStartBeat());
            note.setDuration(te.getDuration());
        }
    }

    /**
     * @param track
     *            is NOT modified.
     * @param mask
     *            number of repeats for each pitch in the notelist
     * @return a new {@code MIDITrack}
     */
    public MIDITrack apply(final MIDITrack track,
            final CircularList<Integer> mask) {

        final MIDITrack applied = new MIDITrack(track);
        TimeSeries masked = TimeSeriesFactory.createRepeated(this, mask);
        masked.apply(applied);
        return applied;

    }

    /**
     * Create from repeat Mask for both the track and the timeseries.
     * 
     * @param track
     *            a track to repeat
     * @param mask
     *            a repeat mask
     * @return a new {@code MIDITrack}
     */
    public MIDITrack applyToBoth(final MIDITrack track,
            final CircularList<Integer> mask) {

        final MIDITrack applied = MIDITrackFactory
                .createRepeated(track,
                        mask);

        TimeSeries masked = TimeSeriesFactory.createRepeated(this, mask);
        masked.apply(applied);
        return applied;

    }

    /**
     * Removes all events and ranges.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     */
    public void clear() {
        this.list.clear();
        this.ranges.clear();
        this.exactRanges.clear();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Used internally
     */
    private void computeRanges() {
        if (this.ranges == null) {
            this.ranges = new LinkedList<Range<Double>>();
        }

        if (this.ranges.isEmpty() == false) {
            this.ranges.clear();
        }

        for (final TimeEvent te : this.list) {
            this.addRange(te.getOpenRange());
        }

    }

    /**
     * Creates a {@code DurationModifier} to divide each duration by the
     * divisor. The start time remains intact but the durations are altered.
     *   <p>
     * Uses the {@link #modifyDurations(com.rockhoppertech.music.modifiers.Modifier.Operation, Double[]) modifyDurations}
     * method.
     * 
     * @param d
     *            the divisor. e.g. 2 will halve the durations.
     */
    public void divideDuration(final double d) {
        logger.debug("divideDuration " + d);
        this.modifyDurations(Operation.DIVIDE, d);
    }

    /**
     * Get the a {@code TimeEvent} at the specified index.
     * 
     * @param index
     *            an index
     * @return a {@code TimeEvent}
     */
    public TimeEvent get(final int index) {
        if (this.list.isEmpty()) {
            return null;
        }
        return this.list.get(index);
    }

    /**
     * Get the description or null if not set.
     * 
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the durations as a List.
     * 
     * @return a List of Doubles
     */
    public List<Double> getDurations() {
        final List<Double> starts = new ArrayList<Double>();
        for (final Timed e : this) {
            starts.add(e.getDuration());
        }
        return starts;
    }

    // public Iterator<TimeEvent> getSeries() {
    // return this.list.iterator();
    // }

    /**
     * Finds the greatest end time of any time event in the series.
     * 
     * @return the end time
     */
    public double getEndBeat() {
        double lastTime = 1d;
        for (final Timed te : this.list) {
            final double t = te.getStartBeat() + te.getDuration();
            if (t > lastTime) {
                lastTime = t;
            }
        }
        return lastTime;
    }

    /**
     * Get the series' name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    public Range<Double> getExactRange(final int index) {
        return this.exactRanges.get(index);
    }

    public Range<Double> getRange(final int index) {
        return this.ranges.get(index);
    }

    public List<Range<Double>> getRanges() {
        return this.ranges;
    }

    public Iterator<Range<Double>> getRangesIterator() {
        return this.ranges.iterator();
    }

    public TimeSeries getSilences() {
        final TimeSeries ts = new TimeSeries();
        double lastTime = 1d;
        logger.debug("number of ranges {}", this.ranges.size());
        for (final Range<Double> r1 : this.ranges) {
            logger.debug("range {}", r1);
            final double before = r1.lowerEndpoint() - lastTime;
            if (before > 0d) {
                ts.add(new TimeEvent(lastTime, before));
                // ts.add(new TimeEvent(lastTime + TimeEvent.rangeFudge, before
                // + TimeEvent.rangeFudge));
            }
            lastTime = r1.upperEndpoint();
        }
        return ts;
    }

    /*
     * public TimeSeries findSilences() { logger.debug(this.list);
     * logger.debug(this.ranges);
     * 
     * TimeSeries ts = new TimeSeries(); double lastTime = 1d;
     * 
     * for (final Range r1 : this.ranges) { logger.debug("start " +
     * r1.getMin()); final double before = r1.getMin() - lastTime;
     * logger.debug("before " + before); logger.debug("lasttime " + lastTime);
     * if (before >= 0d) { logger.debug("silence " + before + " at time " +
     * lastTime); if(before > 0d) ts.add(new TimeEvent(lastTime, before)); }
     * lastTime = r1.getMax(); logger.debug("new lt " + lastTime); } return ts;
     * }
     */

    /**
     * Get the number of time events in this series.
     * 
     * @return the number of events
     */
    public int getSize() {
        return this.list.size();
    }

    /**
     * Get the earliest start time of any time event in this series.
     * 
     * @return the start beat
     */
    public double getStartBeat() {
        double start = 1d;
        for (final Timed te : this.list) {
            final double s = te.getStartBeat();
            if (s < start) {
                start = s;
            }
        }
        return start;
    }

    /**
     * Get all the start times.
     * 
     * @return a List<Double> of start times
     */
    public List<Double> getStartTimes() {
        final List<Double> starts = new ArrayList<Double>();
        for (final Timed e : this) {
            starts.add(e.getStartBeat());
        }
        return starts;
    }

    /**
     * Insert another {@code TimeSeries} at the specified index to this series.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param index
     *            the index in this series
     * @param timeSeries
     *            the {@code TimeSeries} to insert
     */
    public void insertAtIndex(final int index, final TimeSeries timeSeries) {
        final Timed e = this.list.get(index);
        final TimedModifier stm = new StartBeatModifier(e.getStartBeat());
        stm.setOperation(Operation.ADD);
        TimeSeries dupe = new TimeSeries(timeSeries);
        dupe.map(stm);
        // this.list.addAll(index,
        // timeSeries.list);
        this.list.addAll(index,
                ListUtils.newCircularArrayList(dupe.list));
        this.sort();
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Ignores current rests and inserts the value before each start time.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param silence
     *            an amount to insert
     */
    public void insertSilence(final double silence) {
        logger.debug("insertSilenceBefore " + silence);
        logger.debug("list {}", this.list);
        for (final Timed te : this.list) {
            te.setStartBeat(te.getStartBeat() + silence);
        }
        // double lastTime = silence;
        // if (silence < 1d) {
        // lastTime = 1d + silence;
        // }
        //
        // for (final TimeEvent te : this.list) {
        // final double t = te.getStart();
        //
        // te.setStart(lastTime);
        //
        // logger.debug("start time " + t);
        // logger.debug("changed time to " + (t + silence));
        // lastTime = t + silence;
        //
        // // lastTime += te.getDuration() + silence;
        // }
        // logger.debug("After");
        // logger.debug(this.list);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    public Iterator<TimeEvent> iterator() {
        return this.list.iterator();
    }

    public ListIterator<TimeEvent> listIterator(final int index) {
        return this.list.listIterator(index);
    }

    /**
     * <code>map</code> is the GoF visitor(331) design pattern. It loops through
     * all the TimeEvent and applies the modifier to each event. Sort of like
     * mapcar in LISP (but Java does not have lambdas so you need the
     * TimeEventModifier interface).
     * 
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * 
     * <pre>
     * {@code
     * TimeSeries series = new TimeSeries();
     * TimeEvent e = new TimeEvent(1,2);
     * series.add(e);
     * series.map(new DurationModifier(1d, TimeEventModifier.Operation.ADD));
     * }
     * </pre>
     * 
     * 
     * @param mod
     *            a <code>TimeEventModifier</code> implementation.
     * 
     * 
     * @see com.rockhoppertech.music.modifiers.StartBeatModifier
     */
    public void map(final TimedModifier mod) {
        for (final Timed n : this) {
            mod.modify(n);
        }
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * <code>map</code> calls map(TimeEventModifier) only if the note's start
     * beat is after the specified value.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param mod
     *            a <code>TimeEventModifier</code> implementation.
     * @param after
     *            a <code>double</code> value. Modify notes only after this
     *            start beat.
     * 
     * 
     */
    public void map(final TimedModifier mod, final double after) {
        for (final Timed n : this) {
            if (n.getStartBeat() >= after) {
                mod.modify(n);
            }
        }
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * <code>map</code> affects only events after <b>after</b> and before
     * <b>before</b>.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param mod
     *            a <code>TimeEventModifier</code> value
     * @param after
     *            a <code>double</code> value
     * @param before
     *            a <code>double</code> value
     */
    public void map(final TimedModifier mod, final double after,
            final double before) {
        for (final Timed n : this) {
            final double s = n.getStartBeat();
            if ((s >= after) && (s <= before)) {
                mod.modify(n);
            }
        }
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * This is the preferred way. MIDITrack has two special criteria for start
     * beats. With this you can build arbitrary criteria. If the specified
     * critera are true then the note is modified.
     * 
     * <blockquote>
     * 
     * <pre>
     * TimeSeries notelist = new TimeSeries();
     * TimeEvent event = new TimeEvent(Pitch.C5);
     * notelist.add(event);
     * etc.
     * // before or equal to start beat 3 and the pitch is lower than or equal to E5
     * TimeModifierCriteria criteria = new StartBeatCriteria(3d,
     *         TimeModifierCriteria.Operator.LT_EQ, new PitchCriteria(Pitch.E5,
     *                 TimeModifierCriteria.Operator.LT_EQ, null));
     * notelist.map(new StartBeatModifier(1d, TimeModifierCriteria.Operation.SET), criteria);
     * </pre>
     * 
     * </blockquote>
     * 
     * @param mod
     *            The TimeEventModifier
     * @param criteria
     *            The TimeModifierCriteria
     * 
     * @see com.rockhoppertech.series.time.modifiers.criteria.TimeModifierCriteria
     */
    // TODO make sthe criteria a guava predicate
    // public void map(final TimedModifier mod, final TimeModifierCriteria
    // criteria) {
    // for (final Timed n : this) {
    // if (criteria.test(n)) {
    // logger.debug("Passed " + n);
    // mod.modify(n);
    // } else {
    // logger.debug("Failed " + n);
    // }
    // }
    // this.computeRanges();
    // this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    // }

    /**
     * Copy the durations from the List and apply them to the events in this
     * series.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param durations
     *            a List<Double> of durations
     */
    public void mapDurations(final List<TimeEvent> durations) {
        logger.debug("mapDurations ");
        if (durations.size() < this.list.size()) {
            logger.debug("not enough durations");
            return;
        }
        final Iterator<TimeEvent> d = durations.iterator();
        for (final Timed te : this.list) {
            if (d.hasNext() == false) {
                break;
            }
            final Timed newd = d.next();
            te.setDuration(newd.getDuration());
        }
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Copy the durations from the provided series and apply them to the events
     * in this series.
     * 
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param durations
     */
    public void mapDurations(final TimeSeries durations) {
        logger.debug("mapDurations ");
        if (durations.getSize() < this.list.size()) {
            logger.debug("not enough durations");
            return;
        }
        final Iterator<TimeEvent> d = durations.iterator();
        for (final Timed te : this.list) {
            if (d.hasNext() == false) {
                break;
            }
            final Timed newd = d.next();
            te.setDuration(newd.getDuration());
        }
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Each duration is multiplied by this value.
      * <p>
     * Uses the {@link #modifyDurations(com.rockhoppertech.music.modifiers.Modifier.Operation, Double[]) modifyDurations}
     * method.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param d
     *            multiplier
     * @see DurationModifier
     */
    public void multiplyDuration(final double d) {
        logger.debug("multiplyDuration " + d);
        modifyDurations(Operation.MULTIPLY, d);
    }

    /**
     * Create the specified number of copies of this series.
     * <p>
     * The receiver is not modified at all. You get a brand new TimeSeries. it
     * is sequential.
     * 
     * @param n
     *            number of copies
     * @return a new {@code TimeSeries}
     */
    public TimeSeries nCopies(final int n) {
        final TimeSeries ts = new TimeSeries(this);
        for (int i = 0; i < n - 1; i++) {
            ts.append(this);
        }
        return ts;
    }

    /**
     * Does t2 occur after t1's start plus duration?
     * 
     * @param t1
     *            a time event
     * @param t2
     *            a time event
     * @return whether t2 occurs after t1
     */
    public static boolean isAfter(Timed t1, Timed t2) {
        return t2.getStartBeat() >= t1.getStartBeat() + t1.getDuration();
    }

    /**
     * Does t2 occur before t1's start plus duration?
     * 
     * @param t1
     *            a {@code Timed} object
     * @param t2
     *            a {@code Timed} object
     * @return whether t2 occurs before t1
     */
    public static boolean isBefore(Timed t1, Timed t2) {
        return t2.getStartBeat() <= t1.getStartBeat() + t1.getDuration();
    }

    /**
     * Get the next event.
     * <p>
     * This will wrap when the end of the list of events is reached.
     * 
     * @return the next {@code TimeEvent}
     */
    public Timed nextTimeEvent() {
        return this.list.next();
        // if (this.index + 1 > this.list.size()) {
        // this.index = 0;
        // }
        // final TimeEvent v = this.list.get(this.index++);
        // return v;
    }

    // TODO port Permutations
    // public void permute() {
    // final long np = Permutations.getNumberOfPermutations(this.list.size(),
    // this.list.size());
    // logger.debug("Number of permutations: " + np);
    // final List<int[]> list = Permutations.getPermutations(this.list.size());
    // if (logger.isDebugEnabled()) {
    // logger.debug(String.format("permutation list %d",
    // list.size()));
    // for (final int[] a : list) {
    // ArrayUtils.printArray(a,
    // logger);
    // }
    // }
    // double time = this.getEndBeat();
    // for (final int[] array : list) {
    // for (final int element : array) {
    // final TimeEvent te = (TimeEvent) this.get(element).clone();
    // time += te.getStartBeat();
    // te.setStartBeat(time);
    // this.list.add(te);
    // }
    // }
    // logger.debug("permute new end: " + this.getEndBeat());
    // logger.debug("permute new size: " + this.getSize());
    // this.computeRanges();
    // this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    // }

    /**
     * Turn the series into a track.
     * 
     * @return a new {@code MIDITrack}
     */
    public MIDITrack toMIDITrack() {
        final MIDITrack notelist = new MIDITrack();
        int channel = 0;
        for (final Timed te : this) {
            final MIDINote nn = new MIDINote(Pitch.C5, te.getStartBeat(), te
                    .getDuration(), channel);
            notelist.add(nn);
        }
        return notelist;
    }

    public void play() {
        final MIDITrack track = toMIDITrack();

        // click track
        int channel = 9;
        final double end = this.getEndBeat();
        final int hi = MIDIGMPatch.HI_WOOD_BLOCK_PERC.getProgram();
        final int low = MIDIGMPatch.LOW_WOOD_BLOCK_PERC.getProgram();
        for (double i = 1d; i < end; i++) {
            MIDINote nn = null;
            if (i % 4 == 1) {
                nn = new MIDINote(hi, i, 1d, channel);
            } else {
                nn = new MIDINote(low, i, 1d, channel);
            }
            track.add(nn);
        }
        track.play();
    }

    public void play(boolean withClickTrack) {
        final MIDITrack track = toMIDITrack();
        MIDIPerformer mp = new MIDIPerformer();
        mp.play(track, withClickTrack);
    }

    /**
     * Quantize durations.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param q
     *            quantization value
     * @see DurationModifier
     * @see com.rockhoppertech.music.modifiers.Modifier.Operation
     */
    public void quantizeDurations(final double q) {
        logger.debug("quantizeDurations " + q);

        DurationModifier mod = new DurationModifier(Operation.QUANTIZE, q);
        this.map(mod);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Quantize start times.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param q
     *            quantization value
     * @see StartBeatModifier
     * @see com.rockhoppertech.music.modifiers.Modifier.Operation
     */
    public void quantizeStartBeats(final double q) {
        logger.debug("quantizeStartBeats " + q);

        StartBeatModifier mod = new StartBeatModifier(Operation.QUANTIZE, q);
        this.map(mod);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Remove the time event at the specified index.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param index
     *            the index of the Timed event to remove
     */
    public void remove(final int index) {
        final Timed t = this.get(index);
        this.remove(t);
    }

    /**
     * Removes this exact Timed instance.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param t
     */
    public void remove(final Timed t) {
        this.list.remove(t);
        Collections.sort(this.list,
                new TimeComparator());
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Subtracts the values from each duration.
     * <p>
     * Uses the {@link #modifyDurations(com.rockhoppertech.music.modifiers.Modifier.Operation, Double[]) modifyDurations}
     * method.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param d
     *            amount to remove
     * 
     * @see DurationModifier
     * @see com.rockhoppertech.music.modifiers.Modifier.Operation
     */
    public void removeFromDuration(final double d) {
        logger.debug("removeFromDuration " + d);
        modifyDurations(Operation.SUBTRACT, d);
    }

    /**
     * Remove the specified amount from each silence.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param d
     *            amount to remove
     */
    public void removeFromSilences(final double d) {
        logger.debug("removeFromSilences Before");
        logger.debug("list {}", this.list);
        double lastTime = 1d;
        double newTime = 1d;
        for (final Timed te : this.list) {
            final double t = te.getStartBeat();
            logger.debug("doing " + te);
            final double delta = t - lastTime;
            if (delta > 0) {
                final double time = newTime + delta - d;
                if (time >= 0) {
                    te.setStartBeat(time);
                    logger.debug("changed time to " + time);
                }
            }
            logger.debug("new lt " + lastTime);
            // the new time
            newTime = (te.getStartBeat() + te.getDuration());
            // the original time, not the new one
            lastTime = (t + te.getDuration());
        }
        logger.debug("removeFromSilences after");
        logger.debug("list {}", this.list);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Modifies the start times of each time event to remove overlaps.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     */
    public void removeOverlaps() {
        logger.debug("removeOverlaps Before");
        logger.debug("list {}", this.list);
        double lastTime = 1d;
        double newTime = 1d;
        for (final Timed te : this.list) {
            final double t = te.getStartBeat();
            logger.debug("doing " + te);
            final double delta = t - newTime;
            logger.debug("delta " + delta);
            if (delta < 0) {
                // double time = t + delta;
                final double time = newTime;
                if (time >= 0) {
                    te.setStartBeat(time);
                    logger.debug("changed time to " + time);
                }
            }
            logger.debug("new lt " + lastTime);
            // the new time
            newTime = (te.getStartBeat() + te.getDuration());
            // the original time, not the new one
            lastTime = (t + te.getDuration());
        }
        logger.debug("removeOverlaps after");
        logger.debug("list {}", this.list);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Removes the property change listener.
     * 
     * @param listener
     *            the listner to remove
     */
    public void removePropertyChangeListener(
            final PropertyChangeListener listener) {
        this.changes.removePropertyChangeListener(listener);
    }

    /**
     * Removes the property change listener.
     * 
     * @param propertyName
     *            the property name
     * @param listener
     *            the listener
     */
    public void removePropertyChangeListener(final String propertyName,
            final PropertyChangeListener listener) {
        this.changes.removePropertyChangeListener(propertyName,
                listener);
    }

    /**
     * Changes start times to remove silences.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     */
    public void removeSilences() {
        // TimeSeries silences = this.getSilences();
        // int index = 0;
        // for (final Timed te : this.list) {
        // Timed ste = silences.get(index++);
        // te.setStartBeat(ste.getStartBeat());
        // }
        // no, the silences change after the first one
        // for(int i = 1; i < this.list.size(); i++) {
        // Timed te = this.list.get(i);
        // Timed ste = silences.get(index++);
        // // te.setStartBeat(te.getStartBeat() - ste.getDuration());
        // te.setStartBeat(ste.getStartBeat());
        // }

        logger.debug("Before");
        logger.debug("list {}", this.list);
        double lastTime = 1d;
        for (final Timed te : this.list) {
            final double t = te.getStartBeat();
            double delta = t - lastTime;
            logger.debug(String.format("start time %f delta %f",
                    t,
                    delta));
            if (delta > 0d) {
                te.setStartBeat(lastTime);
                logger.debug("changed start to " + lastTime);
            }

            lastTime += te.getDuration();
            logger.debug("new lastTime with added dur " + lastTime);
        }
        logger.debug("After");
        logger.debug("list {}", this.list);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Replace the time event at the specified index with the specified time
     * event.
     * 
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param e a {@code TimeEvent}
     * @param index the index
     */
    public void set(final TimeEvent e, final int index) {
        logger.debug("Timeseries set " + e);
        this.list.set(index,
                e);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Set an optional description of the series.
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Sets each time event's duration to the specified value.
     *  <p>
     * Uses the {@link #modifyDurations(com.rockhoppertech.music.modifiers.Modifier.Operation, Double[]) modifyDurations}
     * method.
     * @param d
     *            new duration
     * 
     */
    public void setDuration(final double d) {
        logger.debug("setDuration " + d);
        modifyDurations(Operation.SET, d);
    }

    /**
     * Set the series' optional name.
     * 
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Change all silences to this value. Modifies the start times of the time
     * events.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     * 
     * @param d
     */
    public void setSilences(final double d) {

        logger.debug("setSilences Before" + d);
        logger.debug("list {}", this.list);
        double lastTime = 1d;
        double newTime = 1d;
        for (final Timed te : this.list) {
            final double t = te.getStartBeat();
            logger.debug("doing " + te);

            // currrent time minus the end time of the last note
            final double delta = t - lastTime;
            logger.debug("delta " + delta);
            if (delta > 0) {
                // double time = newTime + delta + d;
                final double time = newTime + d;
                te.setStartBeat(time);
                logger.debug("changed time to " + time);
            }
            logger.debug("done " + te);

            // the new time

            newTime = (te.getStartBeat() + te.getDuration());
            // the original time, not the new one
            // lastTime = (t + te.getDuration());
            lastTime = t;

            logger.debug("new lt " + lastTime);
        }
        logger.debug("addToSilences After");
        logger.debug("list {}", this.list);
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    /**
     * Change the start beat of each time event by getting the difference
     * between the specified start and the first event's start.
      * <p>
     * Uses the {@link #modifyStarts(com.rockhoppertech.music.modifiers.Modifier.Operation, Double[]) modifyDurations}
     * method.
     * 
     * @param start
     *            new start time
     */
    public void setStart(final double start) {
        final Timed e = this.get(0);
        final double delta = start - e.getStartBeat();
        // final Operation op = Operation.ADD;
        // final TimedModifier stm = new StartBeatModifier(delta);
        // stm.setOperation(op);
        // this.map(stm);
        modifyStarts(Operation.ADD, delta);
    }

    /**
     * Sorts by start time.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event, with this as
     * the new value and no old value.
     */
    public void sort() {
        Collections.sort(this.list,
                new TimeComparator());
        this.computeRanges();
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
    }

    @Override
    public String toString() {
        final StringBuilder sw = new StringBuilder();
        for (final Timed te : this.list) {
            sw.append(te).append('\n');
        }
        for (final Range<Double> r : this.ranges) {
            sw.append(r).append('\n');
        }

        return sw.toString();
    }

    /**
     * Change the start beat of the event at the specified index.
     * <p>
     * Fires a {@code TimeSeries.START_BEAT_CHANGE} property change event.
     * 
     * @param selectedIndex
     *            index of the event to change
     * @param startBeat
     *            the new start
     */
    public void changeStartBeat(int selectedIndex, double startBeat) {
        TimeEvent n = this.get(selectedIndex);
        n.setStartBeat(startBeat);
        this.changes.firePropertyChange(TimeSeries.START_BEAT_CHANGE,
                null,
                n);
    }

    /**
     * Change the start beat of the first event, then make the series
     * sequential.
     * <p>
     * Fires a {@code TimeSeries.START_BEAT_CHANGE} property change event.
     * 
     * @param startBeat
     *            the new start beat
     */
    public void changeStartBeat(double startBeat) {
        TimeEvent n = this.get(0);
        n.setStartBeat(startBeat);
        this.sequential();

        this.changes.firePropertyChange(TimeSeries.START_BEAT_CHANGE,
                null,
                n);
    }

    /**
     * Change the duration of the event at the specified index.
     * <p>
     * Fires a {@code TimeSeries.DURATION_CHANGE} property change event.
     * 
     * @param selectedIndex
     *            index of the event to change
     * @param duration
     *            the new duration
     */
    public void changeDuration(int selectedIndex, double duration) {
        TimeEvent n = this.get(selectedIndex);
        n.setDuration(duration);
        this.changes.firePropertyChange(TimeSeries.DURATION_CHANGE,
                null,
                n);
    }

    /**
     * Make the events sequenial with a 0 pad between events.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event.
     * 
     * @return this {@code TimeSeries}
     */
    public TimeSeries sequential() {
        return this.sequential(0);
    }

    /**
     * Reset each note's startbeat in the series to be sequential based on the
     * duration. The first event is unaffected.
     * <p>
     * Fires a {@code TimeSeries.MODIFIED} property change event.
     * 
     * @param pad
     *            amount added between the notes
     */
    public TimeSeries sequential(int pad) {
        int size = this.getSize();
        Timed n = this.list.get(0);
        double s = n.getStartBeat();
        double d = n.getDuration();

        logger.debug("startbeat0:" + s);
        logger.debug("dur0:" + d);

        for (int i = 1; i < size; i++) {
            Timed nn = this.list.get(i);
            logger.debug("old startbeat:" + nn.getStartBeat());
            nn.setStartBeat(s + d + pad);
            logger.debug("new startbeat:" + nn.getStartBeat());
            s = nn.getStartBeat();
            d = nn.getDuration();

        }
        this.changes.firePropertyChange(TimeSeries.MODIFIED, null, this);
        return this;
    }

    /**
     * Change the endBeat of the event at the specified index.
     * <p>
     * Fires a {@code TimeSeries.END_BEAT_CHANGE} property change event.
     * 
     * @param selectedIndex
     *            index of the event to change
     * @param endBeat
     *            the new endBeat
     */
    public void changeEndBeat(int selectedIndex, double endBeat) {
        TimeEvent n = this.get(selectedIndex);
        n.setEndBeat(endBeat);
        this.changes.firePropertyChange(TimeSeries.END_BEAT_CHANGE,
                null,
                n);
    }

   /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((list == null) ? 0 : list.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TimeSeries other = (TimeSeries) obj;
        if (list == null) {
            if (other.list != null) {
                return false;
            }

        } else if (!list.equals(other.list)) {
            logger.debug("lists dont match ");
            logger.debug("this {}", list);
            logger.debug("other {}", other.list);
            return false;
        }
        // if (name == null) {
        // if (other.name != null) {
        // return false;
        // }
        // } else if (!name.equals(other.name)) {
        // return false;
        // }
        return true;
    }

    /**
     * For serialization.
     * 
     * @param out
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * For Serialization.
     * 
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        // our "pseudo-constructor"
        in.defaultReadObject();
        // now we are a "live" object again
        this.changes = new PropertyChangeSupport(this);
        this.ranges = new ArrayList<Range<Double>>();
        this.exactRanges = new ArrayList<Range<Double>>();
        this.timeComparator = new TimeComparator();
        this.rangeSet = TreeRangeSet.create();
    }

}

/*
 * insert silence series map over list and change start times keep existing
 * silences?
 * 
 * expand/contract current silence series if non overlapping notes, it's easy
 */
