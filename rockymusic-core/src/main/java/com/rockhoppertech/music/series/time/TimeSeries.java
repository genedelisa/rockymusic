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

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.collections.CircularList;
import com.rockhoppertech.music.Note;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackFactory;
import com.rockhoppertech.music.modifiers.DurationModifier;
import com.rockhoppertech.music.modifiers.Modifier.Operation;
import com.rockhoppertech.music.modifiers.StartBeatModifier;
import com.rockhoppertech.music.modifiers.TimedModifier;

//import com.rockhoppertech.series.Permutations;
//import com.rockhoppertech.series.time.modifiers.criteria.TimeModifierCriteria;

/**
 * Class <code>TimeSeries</code>
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version 1.0
 * @since 1.0
 */
public class TimeSeries implements Iterable<TimeEvent>, Serializable {
    /**
     * 
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
    private transient EventListenerList listenerList;
    private transient TimeSeriesEvent timeSeriesEvent;
    private String name = "Untitled";
    private String description;
    // private int index;
    private transient PropertyChangeSupport changes;

    private transient Comparator<? super TimeEvent> timeComparator = new TimeComparator();

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        // our "pseudo-constructor"
        in.defaultReadObject();
        // now we are a "live" object again
        this.changes = new PropertyChangeSupport(this);
        this.ranges = new ArrayList<Range<Double>>();
        this.exactRanges = new ArrayList<Range<Double>>();
        this.listenerList = new EventListenerList();
        this.timeComparator = new TimeComparator();
        this.rangeSet = TreeRangeSet.create();
    }

    public TimeSeries() {
        // this.list = Collections.synchronizedList( new LinkedList() );
        // this.list = new ArrayList<TimeEvent>(); // we do lots of gets/sets
        this.ranges = new ArrayList<Range<Double>>();
        this.exactRanges = new ArrayList<Range<Double>>();
        this.listenerList = new EventListenerList();
        this.changes = new PropertyChangeSupport(this);
    }

    public TimeSeries(final MIDITrack notelist) {
        this();
        // this.list = new ArrayList<TimeEvent>();
        for (final Note n : notelist) {
            this.add(new TimeEvent(n.getStartBeat(), n.getDuration()));
        }
    }

    public TimeSeries(final TimeSeries timeSeries) {
        this();
        for (final TimeEvent te : timeSeries.list) {
            this.list.add(new TimeEvent(te.getStartBeat(), te.getDuration()));
        }
        this.sort();
    }

    public TimeSeries(String durationString) {
        this();
        TimeSeriesFactory.createFromDurationString(this, durationString);
    }

    public TimeSeries add(final double duration) {
        final double start = this.getEndBeat();
        return this.add(new TimeEvent(start, duration));
    }

    public TimeSeries add(final double start, final double duration) {
        logger.debug(
                "adding a new time event, start {} durtion {}",
                start,
                duration);
        return this.add(new TimeEvent(start, duration));
    }

    /**
     * Adds the TimeEvent at its given time. (i.e. the series is resorted)
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
        this.fireSeriesChanged();
        return this;
    }

    /**
     * Neither series are modified. A new instance is returned.
     * 
     * @param ts1
     * @param ts2
     * @return
     */
    public static TimeSeries append(final TimeSeries ts1, final TimeSeries ts2) {
        final TimeSeries ts = new TimeSeries(ts1);
        return ts.append(ts2);
    }

    public static TimeSeries extract(final MIDITrack notelist) {
        final TimeSeries ts = new TimeSeries();
        for (final MIDINote note : notelist) {
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
     * Apply the TimeEvents to the track. The list of TimeEvents will wrap if
     * they are fewer in number than the notes in the notelist. The notelist is
     * not sorted. that is up to the caller: call notelist.sequential();
     * 
     * @param notelist
     */
    // public void apply(final MIDITrack notelist) {
    // // this.index = 0;
    // this.list.reset();
    // double start = 1d;
    // double end = this.getEndBeat();
    // for (final MIDINote note : notelist) {
    // final Timed te = this.nextTimeEvent();
    // if (this.list.isFirst()) {
    // start = te.getStartBeat();
    // start += end;
    // } else {
    // start = te.getStartBeat();
    // }
    // note.setStartBeat(te.getStartBeat());
    // note.setDuration(te.getDuration());
    // }
    // }

    public void addTimeSeriesListener(final TimeSeriesListener l) {
        this.listenerList.add(TimeSeriesListener.class,
                l);
    }

    /**
     * @param d
     *            do this instead: <code>
        DurationModifier dm = new DurationModifier(dv, Operation.ADD);
        ts.map(dm);
        </code>
     * 
     *            or hell, why don't i just wrap it?
     */
    // @Deprecated
    public void addToDuration(final double d) {
        logger.debug("addToDuration " + d);
        // for (final Timed te : this.list) {
        // final double t = te.getDuration();
        // te.setDuration(t + d);
        // }

        final DurationModifier dm = new DurationModifier(Operation.ADD, d);
        this.map(dm);

        this.computeRanges();
        this.fireSeriesChanged();
    }

    public void modifyDurations(Operation operation, Double... values) {
        final DurationModifier dm = new DurationModifier(operation, values);
        this.map(dm);
        this.computeRanges();
        this.fireSeriesChanged();
    }

    public void modifyStarts(Operation operation, Double... values) {
        final StartBeatModifier dm = new StartBeatModifier(operation, values);
        this.map(dm);
        this.computeRanges();
        this.fireSeriesChanged();
    }

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
        this.fireSeriesChanged();
        return this;
    }

    /**
     * A deep copy of the series is appended to this.
     * 
     * ts.append(ts2).append(ts3);
     * 
     * @param ts1
     *            not modified at all
     * @return this
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
        this.fireSeriesChanged();
        return this;
    }

    /**
     * apply this time series to the notelist. The notelist's notes have their
     * start and durations changed.
     * 
     * @param notelist
     *            is modified according to the timeseries
     */
    public void apply(final MIDITrack notelist) {
        final double nsize = notelist.size();
        final double tsize = this.getSize();
        final int n = (int) Math.round(nsize / tsize + .5);
        System.err.printf("nsize %f tsize %f n %d\n",
                nsize,
                tsize,
                n);
        final TimeSeries ts = this.nCopies(n);
        // ts.sequential();
        for (final MIDINote note : notelist) {
            final Timed te = ts.nextTimeEvent();
            note.setStartBeat(te.getStartBeat());
            note.setDuration(te.getDuration());
        }
    }

    /**
     * @param notelist
     *            is NOT modified.
     * @param mask
     *            number of repeats for each pitch in the notelist
     * @return a new notelist
     */
    public MIDITrack apply(final MIDITrack notelist,
            final CircularList<Integer> mask) {

        final MIDITrack applied = new MIDITrack(notelist);
        TimeSeries masked = TimeSeriesFactory.createRepeated(this, mask);
        masked.apply(applied);
        return applied;

    }

    /**
     * create from Mask for both the notelist and the timeseries.
     * 
     * @param notelist
     * @param mask
     * @return
     */
    public MIDITrack applyToBoth(final MIDITrack notelist,
            final CircularList<Integer> mask) {

        final MIDITrack applied = MIDITrackFactory
                .createRepeated(notelist,
                        mask);

        TimeSeries masked = TimeSeriesFactory.createRepeated(this, mask);
        masked.apply(applied);
        return applied;

    }

    public void clear() {
        this.list.clear();
        this.ranges.clear();
        this.exactRanges.clear();
        this.fireSeriesChanged();
    }

    public void computeRanges() {
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
     * The start time remains intact but the duractions are altered.
     * 
     * @param d
     *            the divisor. e.g. 2 will halve the durations.
     * @deprecated use a DurationModifier
     */
    @Deprecated
    public void divideDuration(final double d) {
        logger.debug("divideDuration " + d);
        for (final Timed te : this.list) {
            final double t = te.getDuration();
            te.setDuration(t / d);
        }
        this.computeRanges();
        this.fireSeriesChanged();
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
    // Notify all listeners that have registered interest for
    // notification on this event type. The event instance
    // is lazily created using the parameters passed into
    // the fire method.
    protected void fireSeriesChanged() {
        // Guaranteed to return a non-null array
        final Object[] listeners = this.listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        // the listenerList stores pairs since it can handle all types:
        // the interface is on even indexes and the listener
        // on odd indexes
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TimeSeriesListener.class) {
                // Lazily create the event:
                if (this.timeSeriesEvent == null) {
                    this.timeSeriesEvent = new TimeSeriesEvent(this);
                }
                ((TimeSeriesListener) listeners[i + 1])
                        .seriesChanged(this.timeSeriesEvent);
            }
        }
    }

    protected void fireSeriesChanged(final TimeSeriesEvent timeSeriesEvent) {
        // Guaranteed to return a non-null array
        final Object[] listeners = this.listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        // the listenerList stores pairs since it can handle all types:
        // the interface is on even indexes and the listener
        // on odd indexes
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TimeSeriesListener.class) {
                ((TimeSeriesListener) listeners[i + 1])
                        .seriesChanged(timeSeriesEvent);
            }
        }
    }

    public TimeEvent get(final int index) {
        if (this.list.isEmpty()) {
            return null;
        }
        return this.list.get(index);
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

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

    public int getSize() {
        return this.list.size();
    }

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

    public List<Double> getStartTimes() {
        final List<Double> starts = new ArrayList<Double>();
        for (final Timed e : this) {
            starts.add(e.getStartBeat());
        }
        return starts;
    }

    public void insertAtIndex(final int index, final TimeSeries timeSeries) {
        final Timed e = this.list.get(index);
        final TimedModifier stm = new StartBeatModifier(e.getStartBeat());
        stm.setOperation(Operation.ADD);
        timeSeries.map(stm);
        this.list.addAll(index,
                timeSeries.list);
        this.sort();
        this.computeRanges();
        this.fireSeriesChanged();
    }

    // ignores current rests and inserts the constant before each start time
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
        this.fireSeriesChanged();
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
     * <blockquote>
     * 
     * <pre>
     * TimeSeries series = new TimeSeries();
     * TimeEvent e = new TimeEvent(1,2);
     * series.add(e);
     * series.map(new DurationModifier(1d, TimeEventModifier.Operation.ADD));
     * </pre>
     * 
     * </blockquote>
     * 
     * @param mod
     *            a <code>TimeEventModifier</code> implementation.
     * 
     * 
     * @see com.rockhoppertech.music.modifiers.StartBeatModifier
     */
    // public void map(final TimeEventModifier mod) {
    // for (final Timed n : this) {
    // mod.modify(n);
    // }
    // this.computeRanges();
    // this.fireSeriesChanged();
    // }

    public void map(final TimedModifier mod) {
        for (final Timed n : this) {
            mod.modify(n);
        }
        this.computeRanges();
        this.fireSeriesChanged();
    }

    /**
     * <code>map</code> calls map(TimeEventModifier) only if the note's start
     * beat is after the specified value.
     * 
     * @param mod
     *            a <code>TimeEventModifier</code> implementation.
     * @param after
     *            a <code>double</code> value. Modify notes only after this
     *            start beat.
     * 
     * @see com.rockhoppertech.series.TimeSeries#map(TimeEventModifier)
     */
    public void map(final TimedModifier mod, final double after) {
        for (final Timed n : this) {
            if (n.getStartBeat() >= after) {
                mod.modify(n);
            }
        }
        this.computeRanges();
        this.fireSeriesChanged();
    }

    /**
     * <code>map</code> affects only events after <b>after</b> and before
     * <b>before</b>
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
        this.fireSeriesChanged();
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
    // this.fireSeriesChanged();
    // }

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
        this.fireSeriesChanged();
    }

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
        this.fireSeriesChanged();
    }

    public void multiplyDuration(final double d) {
        logger.debug("multiplyDuration " + d);
        for (final Timed te : this.list) {
            final double t = te.getDuration();
            te.setDuration(t * d);
        }
        this.computeRanges();
        this.fireSeriesChanged();
    }

    /**
     * The receiver is not modified at all. You get a brand new TimeSeries. it
     * is sequential.
     * 
     * @param n
     * @return
     */
    public TimeSeries nCopies(final int n) {
        final TimeSeries ts = new TimeSeries(this);
        for (int i = 0; i < n - 1; i++) {
            ts.append(this);
        }
        return ts;
    }

    /**
     * does t2 occur after t1's start plus duration?
     * 
     * @param t1
     * @param t2
     * @return
     */
    public static boolean isAfter(Timed t1, Timed t2) {
        return t2.getStartBeat() >= t1.getStartBeat() + t1.getDuration();
    }

    public static boolean isBefore(Timed t1, Timed t2) {
        return t2.getStartBeat() <= t1.getStartBeat() + t1.getDuration();
    }

    /**
     * This will wrap when the end of the list of events is reached
     * 
     * @return
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
    // this.fireSeriesChanged();
    // }

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
        final MIDITrack notelist = toMIDITrack();

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
            notelist.add(nn);
        }
        notelist.play();
    }

    public void play(boolean withClickTrack) {
        final MIDITrack notelist = toMIDITrack();
        notelist.play();
        // TODO port the click track to midi performer
        // notelist.play(withClickTrack);
    }

    public void quantizeDurations(final double q) {
        logger.debug("quantizeDurations " + q);

        DurationModifier mod = new DurationModifier(Operation.QUANTIZE, q);
        this.map(mod);
        this.computeRanges();
        this.fireSeriesChanged();
    }

    public void quantizeStartBeats(final double q) {
        logger.debug("quantizeStartBeats " + q);

        StartBeatModifier mod = new StartBeatModifier(Operation.QUANTIZE, q);
        this.map(mod);
        this.computeRanges();
        this.fireSeriesChanged();
    }

    public void remove(final int index) {
        final Timed t = this.get(index);
        this.remove(t);
    }

    /**
     * removes this exact Timed instance.
     * 
     * @param t
     */
    public void remove(final Timed t) {
        this.list.remove(t);
        Collections.sort(this.list,
                new TimeComparator());
        this.computeRanges();
        this.fireSeriesChanged();
    }

    public void removeFromDuration(final double d) {
        logger.debug("removeFromDuration " + d);
        // for (final Timed te : this.list) {
        // final double t = te.getDuration();
        // if (t - d <= 0) {
        // logger.debug("removeFromDuration: invalid dur" + d);
        // continue;
        // }
        // te.setDuration(t - d);
        // }

        DurationModifier mod = new DurationModifier(Operation.SUBTRACT, d);
        this.map(mod);

        this.computeRanges();
        this.fireSeriesChanged();
    }

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
        this.fireSeriesChanged();
    }

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
        this.fireSeriesChanged();
    }

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
        this.fireSeriesChanged();
    }

    public void removeTimeSeriesListener(final TimeSeriesListener l) {
        this.listenerList.remove(TimeSeriesListener.class,
                l);
    }

    public void set(final TimeEvent e, final int index) {
        logger.debug("Timeseries set " + e);
        this.list.set(index,
                e);
        this.computeRanges();
        this.fireSeriesChanged();
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @param d
     * @deprecated
     * 
     *             DurationModifier dm = new DurationModifier(dv,
     *             Operation.SET); ts.map(dm);
     */
    @Deprecated
    public void setDuration(final double d) {
        logger.debug("setDuration " + d);
        for (final Timed te : this.list) {
            // final double t = te.getDuration();
            te.setDuration(d);
        }
        this.computeRanges();
        this.fireSeriesChanged();
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * change all silences to this value
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
        this.fireSeriesChanged();
    }

    public void setStart(final double start) {
        final Timed e = this.get(0);
        final double delta = start - e.getStartBeat();
        final Operation op = Operation.ADD;
        final TimedModifier stm = new StartBeatModifier(delta);
        stm.setOperation(op);
        this.map(stm);
    }

    public void sort() {
        Collections.sort(this.list,
                new TimeComparator());
        this.computeRanges();
        this.fireSeriesChanged();
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

    public void changeStartBeat(int selectedIndex, double startBeat) {
        TimeEvent n = this.get(selectedIndex);
        n.setStartBeat(startBeat);
        this.changes.firePropertyChange(TimeSeries.START_BEAT_CHANGE,
                null,
                n);
    }

    public void changeStartBeat(double startBeat) {
        TimeEvent n = this.get(0);
        n.setStartBeat(startBeat);
        this.sequential();
        
        this.changes.firePropertyChange(TimeSeries.START_BEAT_CHANGE,
                null,
                n);
    }

    public void changeDuration(int selectedIndex, double duration) {
        TimeEvent n = this.get(selectedIndex);
        n.setDuration(duration);
        this.changes.firePropertyChange(TimeSeries.DURATION_CHANGE,
                null,
                n);
    }

    public TimeSeries sequential() {
        return this.sequential(0);
    }

    /**
     * Reset each note's startbeat in the notelist to be sequential based on the
     * duration. The first event is unaffected.
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
        this.changes.firePropertyChange(TimeSeries.MODIFIED,
                null,
                this);
        return this;
    }

    /**
     * <p>
     * </p>
     * 
     * @param selectedIndex
     * @param endBeat
     */
    public void changeEndBeat(int selectedIndex, double endBeat) {
        TimeEvent n = this.get(selectedIndex);
        n.setEndBeat(endBeat);
        this.changes.firePropertyChange(TimeSeries.END_BEAT_CHANGE,
                null,
                n);
    }

    public void setStartBeat(double beat) {
        // TODO
        // final StartBeatModifier m = new StartBeatModifier(this.getEndBeat()
        // - ts.getStartBeat());
        // m.setOperation(Operation.ADD);
        // this.map(m);

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

}

/*
 * insert silence series map over list and change start times keep existing
 * silences?
 * 
 * expand/contract current silence series if non overlapping notes, it's easy
 */
