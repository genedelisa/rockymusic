package com.rockhoppertech.music;

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
	 * Class <code>BeatComparator</code> compares start beats
	 * 
	 * 
	 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
	 * @version $Revision$, $Date$
	 * @since 1.0
	 * @see Comparator
	 */
	class BeatComparator implements Comparator<Note> {
		public int compare(final Note o, final Note o2) {
			if ((o instanceof Note) == false) {
				throw new ClassCastException("not a Note");
			}
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
		this.setStartBeat(1d);
		this.duration = 1d;
		this.pitch = PitchFactory.getPitch(Pitch.C5);
		this.endBeat = this.startBeat + this.duration;
	}

	/**
	 * Initializes a new <code>Note</code> instance given just a MIDI number
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
		this.setStartBeat(startBeat);
		this.endBeat = startBeat + duration;
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
		this.setStartBeat(startBeat);
		this.endBeat = startBeat + duration;
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
		this.changes.addPropertyChangeListener(l);
	}

	/**
	 * <code>clone</code>
	 * 
	 * @return an <code>Object</code> value
	 */
	@Override
	public Object clone() {
		try {
			super.clone();
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new Note(this.pitch, this.startBeat, this.duration);
	}

	/**
	 * <code>compareTo</code> checks the startBeat.
	 * 
	 * @param o
	 *            an <code>Object</code> value
	 * @return an <code>int</code> value
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final Note n) {
		final double nt = n.getStartBeat();
		int r = 0;
		if (this.startBeat < nt) {
			r = -1;
		} else if (this.startBeat == nt) {
			r = 0;
		} else if (this.startBeat > nt) {
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

		if (n.pitch.equals(this.pitch) == false) {
			return false;
		}
		if (n.startBeat != this.startBeat) {
			return false;
		}
		if (n.duration != this.duration) {
			return false;
		}
		if (n.endBeat != this.endBeat) {
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
		return this.startBeat;
	}

	/**
	 * <code>getDuration</code>
	 * 
	 * @return a <code>double</code> value
	 */
	public double getDuration() {
		return this.duration;
	}

	/**
	 * <code>getEndBeat</code>
	 * 
	 * @return a <code>double</code> value
	 */
	public double getEndBeat() {
		return this.endBeat;
	}

	/**
	 * <code>getMidiNumber</code>
	 * 
	 * @return an <code>int</code> value
	 */
	public int getMidiNumber() {
		return this.pitch.getMidiNumber();
	}

	/**
	 * Get the value of pitch.
	 * 
	 * @return Value of pitch.
	 */
	public Pitch getPitch() {
		return this.pitch;
	}

	/**
	 * <code>getStartBeat</code>
	 * 
	 * @return a <code>double</code> value
	 */
	public double getStartBeat() {
		return this.startBeat;
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
		result = PRIME * result + this.pitch.getMidiNumber();
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
		return this.rest;
	}

	/**
	 * <code>removePropertyChangeListener</code>
	 * 
	 * @param l
	 *            a <code>PropertyChangeListener</code> value
	 */
	public void removePropertyChangeListener(final PropertyChangeListener l) {
		this.changes.removePropertyChangeListener(l);
	}

	/**
	 * <code>setBeat</code> is really setStartBeat
	 * 
	 * @param b
	 *            a <code>double</code> value
	 */
	public void setBeat(final double b) {
		this.setStartBeat(b);
	}

	/**
	 * <code>setDuration</code>
	 * 
	 * @param d
	 *            a <code>double</code> value
	 */
	public void setDuration(final double d) {
		final double old = this.duration;
		this.duration = d;
		this.endBeat = this.startBeat + this.duration;
		if (Note.logger.isDebugEnabled()) {
			final String s = String.format("new duration %f new end beat %f",
					this.duration, this.endBeat);
			Note.logger.debug(s);
		}
		this.changes.firePropertyChange(Note.DURATION, new Double(old),
				new Double(this.duration));
	}

	/**
	 * <code>setEndBeat</code>
	 * 
	 * @param b
	 *            a <code>double</code> value
	 */
	public void setEndBeat(final double b) {
		// final double old = this.endBeat;
		this.endBeat = b;
		this.duration = this.endBeat - this.startBeat;
		if (this.duration <= 0d) {
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
		final Pitch old = this.pitch;
		this.pitch = p;
		this.changes.firePropertyChange(Note.PITCH, old, this.pitch);
	}

	/**
	 * <code>setPitch</code> This is a bound property.
	 * 
	 * @param s
	 *            a <code>String</code> value
	 */
	public void setPitch(final String s) {
		final Pitch old = this.pitch;
		this.pitch = new Pitch(s);
		this.changes.firePropertyChange(Note.PITCH, old, this.pitch);
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
	public void setStartBeat(final double b) {
		if (b < 1d) {
			throw new IllegalArgumentException(
					"Start beat cannot be before 1! " + b);
		}
		final double old = this.startBeat;
		this.startBeat = b;
		this.endBeat = this.startBeat + this.duration;
		if (Note.logger.isDebugEnabled()) {
			final String s = String.format("new start beat %f new end beat %f",
					this.startBeat, this.endBeat);
			Note.logger.debug(s);
		}
		this.changes.firePropertyChange(Note.START_BEAT, new Double(old),
				new Double(this.startBeat));

	}

	/**
	 * @param minorSeconds
	 *            can be + or -
	 * @return
	 */
	public Note transpose(int minorSeconds) {
		this.pitch = this.pitch.transpose(minorSeconds);
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
		sb.append("startBeat: ").append(nf.format(this.startBeat));
		sb.append(" pitch: ").append(this.pitch);
		sb.append(" dur: ").append(nf.format(this.duration));
		sb.append(" : ").append(this.getDurationString());

		sb.append(" endBeat: ").append(nf.format(this.endBeat));
		sb.append(" midinum: ").append(this.pitch.getMidiNumber());
		sb.append(" rest: ").append(this.rest);
		sb.append(']');
		return sb.toString();
	}

	public String getDurationString() {
		return DurationParser.getDurationString(this.duration);
	}

	public String getString() {
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumIntegerDigits(3);
		// nf.setMinimumIntegerDigits(3);
		PitchFormat pf = PitchFormat.getInstance();
		return String.format("%s,%s,%s", pf.format(this.pitch).trim(),
				nf.format(this.startBeat),
				DurationParser.getDurationString(this.duration));
	}

}
