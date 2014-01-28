package com.rockhoppertech.music;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
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

/*
 * $Id$
 *
 * Copyright 1998,1999,2000,2001 by Rockhopper Technologies, Inc.,
 * 75 Trueman Ave., Haddonfield, New Jersey, 08033-2529, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Rockhopper Technologies, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with RTI.
 */

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * The <code>Note</code> class is a utility. The implicit unit is "beats". I
 * suppose that seconds might be useful too.
 * 
 * The XML stuff is in here. I should move it. although it's a delegate.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com"></a>
 * @version 1.0
 * @since 1.0
 * @see Comparable
 * @see Serializable
 * @see MIDINote
 */
public class Note implements Comparable<Note>, Timed, Serializable {
    /**
     * Class <code>BeatComparator</code> compares start beats.
     * 
     * 
     * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
     * @version $Revision$, $Date$
     * @since 1.0
     * @see Comparator
     */
    class BeatComparator implements Comparator<Note> {
        @Override
        public int compare(final Note o, final Note o2) {
            final double t1 = o.getStartBeat();
            final double t2 = o2.getStartBeat();
            if (t1 < t2) {
                return -1;
            } else if (t1 == t2) {
                return 0;
            } else if (t1 > t2) {
                return 1;
            }
            return 0;
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(Note.class);
    private Pitch pitch;
    private double startBeat;
    private double duration; // in beats
    /* is this a sounding note or a rest? */
    private boolean rest = false;
    // these are optional
    private double endBeat;

    protected PropertyChangeSupport changes = new PropertyChangeSupport(this);
    /**
     * Name of the bound property, also XML attributes.
     */
    public static final String PITCH = "pitch";
    public static final String DURATION = "duration";
    public static final String START_BEAT = "startBeat";
    public static final String END_BEAT = "endBeat";
    // public static final String MIDI_NUMBER = "midiNumber";
    public static final String NOTE = "note";

    /**
     * Initializes a new <code>Note</code> instance. The startBeat is 1, the
     * duration is 1 and the pitch is Middle C.
     * 
     */
    public Note() {
        setStartBeat(1d);
        duration = 1d;
        pitch = PitchFactory.getPitch(Pitch.C5);
        endBeat = startBeat + duration;
    }

    /**
     * Initializes a new <code>Note</code> instance given just a MIDI number.
     * 
     * @param midiNumber
     *            an <code>int</code> representing a MIDI note number
     */
    public Note(final int midiNumber) {
        this(PitchFactory.getPitch(midiNumber), 1d, 1d);
    }

    /**
     * Initializes a new <code>Note</code> instance.
     * 
     * @param midiNumber
     *            an <code>int</code> representing a MIDI note number
     * @param startBeat
     *            a <code>double</code> representing the start beat
     * @param duration
     *            a <code>double</code> duration value
     */
    public Note(final int midiNumber, final double startBeat,
            final double duration) {
        this(PitchFactory.getPitch(midiNumber), startBeat, duration);
    }

    /**
     * Initializes a new <code>Note</code> instance.
     * 
     * @param pitch
     *            a <code>Pitch</code> value
     * @param startBeat
     *            a <code>double</code> value
     * @param duration
     *            a <code>double</code> value
     */
    public Note(final Pitch pitch, final double startBeat, final double duration) {
        this.pitch = pitch;
        setStartBeat(startBeat);
        endBeat = startBeat + duration;
        this.duration = duration;
    }

    /**
     * Initializes a new <code>Note</code> instance.
     * 
     * @param pitch
     *            a <code>String</code> value
     * @param startBeat
     *            a <code>double</code> value
     * @param duration
     *            a <code>double</code> value
     */
    public Note(final String pitch, final double startBeat,
            final double duration) {
        this.pitch = PitchFactory.getPitch(pitch);
        setStartBeat(startBeat);
        endBeat = startBeat + duration;
        this.duration = duration;
    }

    /*
     * GoF visitor design pattern
     * 
     * @see
     * com.rockhoppertech.music.MusicalElement#accept(com.rockhoppertech.music
     * .MusicVisitor)
     */
    // public void accept(final MusicVisitor v) {
    // v.visit(this);
    // }
    // TODO FIXME visitor

    /**
     * <code>addPropertyChangeListener</code>
     * 
     * @param l
     *            a <code>PropertyChangeListener</code> value
     */
    public void addPropertyChangeListener(final PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    /**
     * <code>compareTo</code> checks the startBeat.
     * 
     * @param n a note to compare
     *            
     * @return an <code>int</code> value
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Note n) {
        final double nt = n.getStartBeat();
        int r = 0;
        if (startBeat < nt) {
            r = -1;
        } else if (startBeat == nt) {
            r = 0;
        } else if (startBeat > nt) {
            r = 1;
        }
        return r;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o instanceof Note) == false) {
            return false;
        }

        final Note n = (Note) o;

        if (n.pitch.equals(pitch) == false) {
            return false;
        }
        if (n.startBeat != startBeat) {
            return false;
        }
        if (n.duration != duration) {
            return false;
        }
        if (n.endBeat != endBeat) {
            return false;
        }

        return true;
    }

    /**
     * <code>getBeat</code>
     * 
     * @return a <code>double</code> value
     */
    public double getBeat() {
        return startBeat;
    }

    /**
     * <code>getDuration</code>
     * 
     * @return a <code>double</code> value
     */
    @Override
    public double getDuration() {
        return duration;
    }

    /**
     * <code>getEndBeat</code>
     * 
     * @return a <code>double</code> value
     */
    @Override
    public double getEndBeat() {
        return endBeat;
    }

    /**
     * <code>getMidiNumber</code>
     * 
     * @return an <code>int</code> value
     */
    public int getMidiNumber() {
        return pitch.getMidiNumber();
    }

    /**
     * Get the value of pitch.
     * 
     * @return Value of pitch.
     */
    public Pitch getPitch() {
        return pitch;
    }

    /**
     * <code>getStartBeat</code>
     * 
     * @return a <code>double</code> value
     */
    @Override
    public double getStartBeat() {
        return startBeat;
    }

    /*
     * <p>See Effective Java. </p>
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + pitch.getMidiNumber();
        return result;
    }

    /**
     * Determines if the time range of the given note (start beat to end beat)
     * overlaps the time range of this note.
     * <p>
     * Uses a tiny fudge factor to exclude start times that match end times.
     * 
     * @param note
     *            - the guy to compare to
     * @return
     */
    // public boolean isOverlapping(final Note note) {
    // final double fudge = .0000001;
    // Range r;
    // final DoubleRange range = new DoubleRange(this.getStartBeat(), this
    // .getEndBeat());
    // final DoubleRange range2 = new DoubleRange(note.getStartBeat() +fudge,
    // note
    // .getEndBeat());
    // return range.overlapsRange(range2);
    //
    // // NumberRange range = new NumberRange(this.getStartBeat(), this
    // // .getEndBeat());
    // // NumberRange range2 = new NumberRange(note.getStartBeat() +
    // Double.MIN_VALUE, note
    // // .getEndBeat());
    // // return range.overlapsRange(range2);
    // }

    /**
     * @return the rest
     */
    public boolean isRest() {
        return rest;
    }

    /**
     * <code>removePropertyChangeListener</code>
     * 
     * @param l
     *            a <code>PropertyChangeListener</code> value
     */
    public void removePropertyChangeListener(final PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    /**
     * <code>setBeat</code> is really setStartBeat
     * 
     * @param b
     *            a <code>double</code> value
     */
    public void setBeat(final double b) {
        setStartBeat(b);
    }

    /**
     * <code>setDuration</code>
     * 
     * @param d
     *            a <code>double</code> value
     */
    @Override
    public void setDuration(final double d) {
        final double old = duration;
        duration = d;
        endBeat = startBeat + duration;

        logger.debug("new duration {} new end beat {}",
                duration, endBeat);

        changes.firePropertyChange(Note.DURATION, new Double(old),
                new Double(duration));
    }

    /**
     * <code>setEndBeat</code>
     * 
     * @param b
     *            a <code>double</code> value
     */
    public void setEndBeat(final double b) {
        // final double old = this.endBeat;
        endBeat = b;
        duration = endBeat - startBeat;
        if (duration <= 0d) {
            throw new IllegalArgumentException("duration cannot be <=0");
        }
        // changes.firePropertyChange(END_BEAT, new Double(old), new Double(
        // this.endBeat));
    }

    /**
     * <code>setMidiNumber</code> sets the pitch property.
     * 
     * @param n
     *            an <code>int</code> value
     */
    public void setMidiNumber(final int n) {
        if ((n <= 0) || (n > 127)) {
            throw new IllegalArgumentException(
                    "value must be between 0 and 127");
        }

        this.setPitch(PitchFactory.getPitch(n));

    }

    /**
     * Set the value of pitch. This is a bound property.
     * 
     * @param p
     *            v Value to assign to pitch.
     */
    public void setPitch(final Pitch p) {
        final Pitch old = pitch;
        pitch = p;
        changes.firePropertyChange(Note.PITCH, old, pitch);
    }

    /**
     * <code>setPitch</code> This is a bound property.
     * 
     * @param s
     *            a <code>String</code> value
     */
    public void setPitch(final String s) {
        final Pitch old = pitch;
        pitch = new Pitch(s);
        changes.firePropertyChange(Note.PITCH, old, pitch);
    }

    /**
     * @param rest
     *            the rest to set
     */
    public void setRest(final boolean rest) {
        this.rest = rest;
    }

    /**
     * <code>setStartBeat</code> sets the start beat. The minimum value is 1
     * which is the beginning of the sequence.
     * 
     * @param b
     *            a <code>double</code> value
     */
    @Override
    public void setStartBeat(final double b) {
        if (b < 1d) {
            throw new IllegalArgumentException(
                    "Start beat cannot be before 1! " + b);
        }
        final double old = startBeat;
        startBeat = b;
        endBeat = startBeat + duration;

        logger.debug("new start beat {} new end beat {}",
                startBeat, endBeat);
        changes.firePropertyChange(Note.START_BEAT, new Double(old),
                new Double(startBeat));

    }

    /**
     * @param minorSeconds
     *            can be + or -
     * @return the transposed Note
     */
    public Note transpose(int minorSeconds) {
        pitch = pitch.transpose(minorSeconds);
        return this;
    }

    /**
     * <code>toString</code>
     * 
     * @return a <code>String</code> value
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumIntegerDigits(3);
        nf.setMinimumIntegerDigits(3);
        sb.append(this.getClass().getSimpleName()).append('[');
        sb.append("startBeat: ").append(nf.format(startBeat));
        sb.append(" pitch: ").append(pitch);
        sb.append(" dur: ").append(nf.format(duration));
        sb.append(" : ").append(getDurationString());

        sb.append(" endBeat: ").append(nf.format(endBeat));
        sb.append(" midinum: ").append(pitch.getMidiNumber());
        sb.append(" rest: ").append(rest);
        sb.append(']');
        return sb.toString();
    }

    public String getDurationString() {
        return DurationParser.getDurationString(duration);
    }

    public String getString() {
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumIntegerDigits(3);
        // nf.setMinimumIntegerDigits(3);
        PitchFormat pf = PitchFormat.getInstance();
        return String.format("%s,%s,%s", pf.format(pitch).trim(),
                nf.format(startBeat),
                DurationParser.getDurationString(duration));
    }

}
