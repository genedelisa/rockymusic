package com.rockhoppertech.music.midi.js;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.modifiers.MIDINoteModifier;
import com.rockhoppertech.music.modifiers.NoteModifier;
import com.rockhoppertech.music.modifiers.StartBeatModifier;


/**
 * A rewrite of my ancient MIDITrack from the 1990s.
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class MIDITrack implements Serializable, Iterable<MIDINote> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory
			.getLogger(MIDITrack.class);

	private String name;
	private String description;	
	

	private List<MIDIEvent> events;
	private List<MIDINote> notes;

	public MIDITrack() {
		this.events = new ArrayList<>();
		this.notes = new ArrayList<>();
	}
	
	/**
	 * Initializes a new MIDITrack instance as a deep copy of specified MIDITrack.
	 * Sort of like a C++ copy constructor (- but without C++ crap like virtual
	 * destructors and overloaded operators...) 
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * MIDITrack notelist = new MIDITrack();
	 * MIDINote note = new MIDINote(Pitch.C5);
	 * notelist.add(note);
	 * MIDITrack notelistCopy = new MIDITrack(notelist);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param orig
	 *            The MIDITrack that will be "cloned".
	 */
	public MIDITrack(MIDITrack orig) {
		this();
		this.name = new String(orig.getName());
		if (orig.getDescription() != null)
			this.description = new String(orig.getDescription());

		for (MIDINote n : orig.notes) {
			this.notes.add(new MIDINote(n));
		}
		for (MIDIEvent n : orig.events) {
			this.events.add(new MIDIEvent(n.toMidiEvent()));
		}

//		NavigableMap<Double, TimeSignature> ts = orig.getTimeSignatures();
//		for (Iterator<Double> i = ts.keySet().iterator(); i.hasNext();) {
//			double time = i.next();
//			TimeSignature sig = ts.get(time);
//			this.timeSignatures.put(time, sig);
//		}
//
//		NavigableMap<Double, KeySignature> ks = orig.keySignatureMap;
//		for (Double time : ks.keySet()) {
//			KeySignature sig = ks.get(time);
//			this.keySignatureMap.put(time, sig);
//		}
		// for (Iterator<Double> i = ts.keySet().iterator(); i.hasNext();) {
		// double time = i.next();
		// KeySignature sig = ks.get(time);
		// this.keySignatureMap.put(time, sig);
		// }

	}
	
	/**
	 * Initializes a new <code>MIDITrack</code> instance. The MIDINotes from
	 * the provided collection are copied - not cloned.
	 * 
	 * @param c
	 *            a <code>Collection<MIDINote></code> value
	 */
	public MIDITrack(Collection<MIDINote> c) {
		this();
		for (MIDINote n : c) {
			this.notes.add(n);
		}
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public MIDITrack add(MIDIEvent event) {
		this.events.add(event);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MIDIEvent> getEvents() {
		return events;
	}

	public void setEvents(List<MIDIEvent> events) {
		this.events = events;
	}

	public List<MIDINote> getNotes() {
		return notes;
	}

	public void setNotes(List<MIDINote> notes) {
		this.notes = notes;
	}

	@Override
	public Iterator<MIDINote> iterator() {
		return this.notes.iterator();
	}

	

	public MIDITrack append(MIDINote note) {
		double end = this.getEndBeat();
		note.setStartBeat(end);
		this.notes.add(note);
		return this;
	}

	public MIDITrack append(int midiNumber) {
		MIDINote note = new MIDINote(midiNumber);
		return this.append(note);
	}

	public Integer size() {
		return this.notes.size();
	}

	public MIDINote get(int i) {
		return this.notes.get(i);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Track Name:").append(this.name).append('\n');

		for (MIDINote n : this.notes) {
			sb.append(n).append('\n');
		}
		for (MIDIEvent n : this.events) {
			sb.append(n.toReadableString()).append('\n');
		}

		return sb.toString();
	}
	
	
	/**
	 * returns the last MIDINote in the list
	 * 
	 * @return a MIDINote that is last in the list
	 */
	public MIDINote getLastNote() {
		return this.get(this.notes.size() - 1);
	}

	/**
	 * Create and append the specified {@code MIDINote} to the list. The Note's
	 * timing is unchanged. Use <code>append</code> if you want the timing
	 * changed.
	 * <p>
	 * {@code PropertyChange MIDITrack.ADD} is fired.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * MIDITrack notelist = new MIDITrack();
	 * MIDINote note = new MIDINote(Pitch.C5);
	 * notelist.add(note);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param note
	 *            The MIDINote instance to append to the list.
	 * 
	 * @see com.rockhoppertech.music.midi.js.MIDINote
	 * @see MIDITrack#append(MIDINote)
	 */
	public MIDITrack add(MIDINote note) {
		this.notes.add(note);
		return this;
	}

	public MIDITrack add(MIDINote... notes) {
		for (MIDINote note : notes) {
			this.notes.add(note);
		}
		return this;
	}

	/**
	 * Create and append a new {@code MIDINote} to the list. All the default
	 * values besides pitch are set (e.g. startbeat = 1, duration = 1.)
	 * <p>
	 * {@code PropertyChange MIDITrack.ADD} is fired.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * MIDITrack notelist = new MIDITrack();
	 * notelist.add(&quot;C5&quot;);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param pitch
	 *            The pitch name as a String e.g. C5 to be parsed by
	 *            PitchFactory
	 * 
	 * @see com.rockhoppertech.music.midi.js.MIDINote
	 * @see com.rockhoppertech.music.PitchFactory
	 */
	public MIDITrack add(String pitch) {
		MIDINote n = new MIDINote(PitchFactory.getPitch(pitch).getMidiNumber());
		this.notes.add(n);
		return this;
	}

	public MIDITrack add(int midiNumber) {
		MIDINote n = new MIDINote(midiNumber);
		this.notes.add(n);
		return this;
	}

	public MIDITrack add(int... midiNumbers) {
		// MIDINote[] a = new MIDINote[midiNumbers.length];
		// int count = 0;
		for (int mn : midiNumbers) {
			MIDINote n = new MIDINote(mn);
			// a[count++] = n;
			this.notes.add(n);
		}
		return this;
	}

	/**
	 * Create and append a new {@code MIDINote} to the list.
	 * <p>
	 * {@code PropertyChange MIDITrack.ADD} is fired.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * MIDITrack notelist = new MIDITrack();
	 * notelist.add(Pitch.C5, 2d);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param pitch
	 *            The pitch name e.g. C5 to be parsed by PitchFactory
	 * @param duration
	 *            The new MIDINote's duration
	 * 
	 * @see com.rockhoppertech.music.midi.js.MIDINote
	 * @see com.rockhoppertech.music.PitchFactory
	 */
	public MIDITrack add(String pitch, double duration) {
		MIDINote n = new MIDINote(PitchFactory.getPitch(pitch).getMidiNumber());
		n.setDuration(duration);
		this.notes.add(n);
		return this;
	}

	/**
	 * Create and append a new {@code MIDINote} to the list.
	 * <p>
	 * {@code PropertyChange MIDITrack.ADD} is fired.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * MIDITrack notelist = new MIDITrack();
	 * notelist.add(&quot;C5&quot;, 1.5, 2d);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param pitch
	 *            The pitch name e.g. C5 to be parsed by PitchFactory
	 * @param startBeat
	 *            The new MIDINote's start beat
	 * @param duration
	 *            The new MIDINote's duration
	 * 
	 * @see com.rockhoppertech.music.midi.js.MIDINote
	 * @see com.rockhoppertech.music.PitchFactory
	 */
	public MIDITrack add(String pitch, double startBeat, double duration) {
		MIDINote n = new MIDINote(PitchFactory.getPitch(pitch).getMidiNumber());
		n.setStartBeat(startBeat);
		n.setDuration(duration);
		this.notes.add(n);

		return this;
	}

	/**
	 * Removes the specified MIDINote from the notelist.
	 * <p>
	 * {@code PropertyChange MIDITrack.REMOVE} is fired. <blockquote>
	 * 
	 * <pre>
	 * MIDITrack notelist = new MIDITrack();
	 * MIDINote note = new MIDINote(Pitch.C5);
	 * notelist.add(note);
	 * notelist.remove(note);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param n
	 *            The MIDINote to remove
	 * @see com.rockhoppertech.music.midi.js.MIDINote
	 */
	public void remove(MIDINote n) {
		boolean b = false;
		b = this.notes.remove(n);
		if (!b) {
			logger.debug(String.format("%s did not exist in this notelist\n", n));
			return;
		}
	}

	public void removePitch(Pitch p) {
		for (Iterator<MIDINote> i = this.iterator(); i.hasNext();) {
			MIDINote n = i.next();
			if (n.getMidiNumber() == p.getMidiNumber()) {
				i.remove();
			}
		}

		// you'll get a concurrent modification exception if you do this:
		// for(MIDINote n : this) {
		// if(n.getMidiNumber() == p.getMidiNumber()) {
		// this.remove(n);
		// }
		// }
	}

	/**
	 * <code>remove</code> a MIDINote at the specified index.
	 * <p>
	 * {@code PropertyChange MIDITrack.REMOVE} is fired. <blockquote>
	 * 
	 * <pre>
	 * MIDITrack notelist = new MIDITrack();
	 * MIDINote note = new MIDINote(Pitch.C5);
	 * notelist.add(note);
	 * notelist.remove(0);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param index
	 *            an <code>int</code> value
	 * @see com.rockhoppertech.music.midi.js.MIDINote
	 */
	public void remove(int index) {
		MIDINote n = this.get(index);
		this.notes.remove(n);
	}

	

	/**
	 * <code>remove</code>
	 * 
	 * @param n
	 *            a <code>Note</code> value
	 */
	public void remove(MIDIEvent n) {
		this.events.remove(n);
	}

	/**
	 * <code>merge</code> merges and sorts the additions based on start time.
	 * Notes are comparable.
	 * 
	 * @param l
	 *            a <code>MIDITrack</code> value
	 */
	public MIDITrack merge(MIDITrack l) {
		this.notes.addAll(l.notes);
		Collections.sort(this.notes);
		return this;
	}

	public void sort(Comparator<MIDINote> c) {
		Collections.sort(this.notes, c);
	}

	/**
	 * Note implements comparable which compares start beats.
	 */
	public void sortByStartBeat() {
		Collections.sort(this.notes);
	}

	/**
	 * This does not change the start beats. It orders the list by pitch. This
	 * is useful for chord production.
	 */
	public void sortByAscendingPitches() {
		Comparator<MIDINote> comp = new Comparator<MIDINote>() {
			public int compare(MIDINote o1, MIDINote o2) {
				if (o1.getMidiNumber() > o2.getMidiNumber()) {
					return 1;
				} else if (o1.getMidiNumber() < o2.getMidiNumber()) {
					return -1;
				}
				return 0;
			}
		};
		Collections.sort(this.notes, comp);
	}

	public MIDITrack insertListAtIndex(MIDITrack newlist, int index) {
		// this.notes.addAll(index, newlist.notes);
		newlist.sortByStartBeat();

		List<MIDINote> beginning = this.notes.subList(0, index);
		List<MIDINote> end = this.notes.subList(index, this.notes.size());
		MIDITrack n = new MIDITrack(beginning);
		MIDITrack endlist = new MIDITrack(end);

		double endBeat = n.getEndBeat();
		newlist.map(new StartBeatModifier(NoteModifier.Operation.ADD, endBeat));
		for (MIDINote note : newlist.notes) {
			n.notes.add((MIDINote) note.clone());
		}
		endBeat = n.getEndBeat();
		endlist.map(new StartBeatModifier(NoteModifier.Operation.ADD, endBeat));
		for (MIDINote note : endlist.notes) {
			n.notes.add((MIDINote) note.clone());
		}
		return n;

		// this.sequential();
	}

	public MIDITrack merge(MIDITrack l, int index, boolean sort) {
		this.notes.addAll(index, l.notes);
		if (sort)
			Collections.sort(this.notes);
		return this;
	}

	/**
	 * this is cumulative.
	 * 
	 * @param notelist
	 * @param n
	 * @return
	 */
	public MIDITrack appendNTimes(MIDITrack notelist, int n) {
		MIDITrack r = null;
		for (int i = 0; i < n; i++) {
			r = this.append(notelist, 0);
		}
		return r;
	}

	/**
	 * Appends the notelist to the current one. The start beats of the parameter
	 * notelist are modified to start after the current notelist.
	 * 
	 * @param notelist
	 *            the notelist to append
	 */
	public MIDITrack append(MIDITrack notelist) {
		return this.append(notelist, 0);
	}

	public MIDITrack append(MIDITrack notelist, double gap) {
		int fromIndex = 0;
		int toIndex = notelist.size();
		return this.append(notelist, gap, fromIndex, toIndex);
	}

	/**
	 * Appends the parameter notelist to the current one with an optional gap
	 * between them. A negative value for the gap will overlap the notelists.
	 * nl.append(nl2, 0).append(nl3,0);
	 * 
	 * @param notelist
	 * @param gap
	 *            the durational space between the notelists in beats
	 * @param fromIndex
	 *            low endpoint (inclusive) of the subList.
	 * @param toIndex
	 *            high endpoint (exclusive) of the subList.
	 */
	public MIDITrack append(MIDITrack notelist, double gap,
			int fromIndex, int toIndex) {
		if (this == notelist) {
			notelist = new MIDITrack(this);
		}
		double end = this.getEndBeat() + gap;
		if (logger.isDebugEnabled()) {
			String s = String.format("notelist '%s'", notelist);
			logger.debug(s);
			logger.debug("end " + end);
			logger.debug("gap " + gap);
			logger.debug("this.size() " + this.size());
			logger.debug("this.getEndBeat() " + this.getEndBeat());
		}
		// case if the notelist appended to is empty.
		if (this.size() == 0) {
			end = 1d;
			notelist
					.map(new StartBeatModifier(NoteModifier.Operation.SET, end));
			logger.debug("setting to " + end);
		} else {
			notelist
					.map(new StartBeatModifier(NoteModifier.Operation.ADD, end));
			logger.debug("adding to " + end);
		}
		if (logger.isDebugEnabled()) {
			String s = String.format("notelist after start beat mod'%s'",
					notelist);
			logger.debug(s);
		}
		List<MIDINote> sub = notelist.notes.subList(fromIndex, toIndex);
		if (logger.isDebugEnabled()) {
			String s = String.format("sublist '%s'", notelist);
			logger.debug(s);
		}
		for (MIDINote note : sub) {
			this.notes.add((MIDINote) note.clone());
		}
		// this.notes.addAll(sub);
		return this;
	}

	/**
	 * <code>remove</code>
	 * 
	 * @param l
	 *            a <code>MIDITrack</code> value
	 */
	public void remove(MIDITrack l) {
		this.notes.removeAll(l.notes);
	}

	/**
	 * <code>retain</code> discards all MIDINotes that are not in the specified
	 * list.
	 * 
	 * @param l
	 *            a <code>MIDITrack</code> value
	 */
	public boolean retain(MIDITrack l) {
		boolean ret = this.notes.retainAll(l.notes);
		if (logger.isDebugEnabled()) {
			logger.debug("retainAll returned " + ret);
		}
		return ret;
	}

	/**
	 * <code>clear</code> emptys the notelist.
	 * 
	 */
	public void clear() {
		this.notes.clear();
	}

	/**
	 * <code>getStartBeat</code> returns the start beat of the first Note.
	 * Notelist is sorted on the start beat because Note implements Comparable.
	 * 
	 * @return a <code>double</code> value
	 */
	public double getStartBeat() {
		if (this.notes.isEmpty()) {
			return 0d;
		}
		MIDINote first = (MIDINote) this.notes.get(0);
		return first.getStartBeat();
	}

	/**
	 * <code>getEndBeat</code>
	 * 
	 * @return a <code>double</code> value
	 */
	public double getEndBeat() {
		double endBeat = 1d;
		for (MIDINote n : this.notes) {
			double d = n.getEndBeat();
			if (d > endBeat) {
				endBeat = d;
			}
		}
		return endBeat;
	}

	public double getShortestDuration() {
		double shortest = Double.MAX_VALUE;
		for (MIDINote n : this.notes) {
			double d = n.getDuration();
			if (d < shortest) {
				shortest = d;
			}
		}
		return shortest;
	}

	public double getLongestDuration() {
		double longest = Double.MIN_VALUE;
		for (MIDINote n : this.notes) {
			double d = n.getDuration();
			if (d > longest) {
				longest = d;
			}
		}
		return longest;
	}
	
	/**
	 * <code>getPitchIntervals</code> returns intervals in relation to each
	 * other.
	 * 
	 * @return an <code>int[]</code> value
	 */
	public int[] getPitchIntervals() {
		int len = this.notes.size();
		int[] intervals = new int[len - 1];
		for (int i = 0; i < intervals.length; i++) {
			MIDINote i1 = (MIDINote) this.notes.get(i);
			MIDINote i2 = (MIDINote) this.notes.get(i + 1);
			intervals[i] = i2.getMidiNumber() - i1.getMidiNumber();
		}
		return intervals;
	}

	/**
	 * There intervals are in relation to the first MIDINote's MIDI number. Like
	 * a Chord.
	 * 
	 * @return
	 */
	public int[] getPitchIntervalsAbsolute() {
		int len = this.notes.size();
		int[] intervals = new int[len - 1];
		int root = this.notes.get(0).getMidiNumber();
		for (int i = 1; i <= intervals.length; i++) {
			MIDINote i1 = (MIDINote) this.notes.get(i);
			intervals[i - 1] = i1.getMidiNumber() - root;
		}
		return intervals;
	}
	
	/**
	 * Reset each note's startBeat. The pad is 0.
	 */
	public MIDITrack sequential() {
		return this.sequential(0);
	}

	/**
	 * Reset each note's startbeat in the notelist to be sequential based on the
	 * duration. The first note is unaffected.
	 * 
	 * @param pad
	 *            amount added between the notes
	 */
	public MIDITrack sequential(int pad) {
		int size = this.size();
		MIDINote n = (MIDINote) this.notes.get(0);
		double s = n.getStartBeat();
		double d = n.getDuration();
		if (logger.isDebugEnabled()) {
			logger.debug("startbeat0:" + s);
			logger.debug("dur0:" + d);
		}

		for (int i = 1; i < size; i++) {
			MIDINote nn = (MIDINote) this.notes.get(i);
			logger.debug("old startbeat:" + nn.getStartBeat());
			nn.setStartBeat(s + d + pad);
			logger.debug("new startbeat:" + nn.getStartBeat());
			s = nn.getStartBeat();
			d = nn.getDuration();

		}
		return this;
	}

	/**
	 * <code>map</code> is the GoF visitor(331) design pattern. It loops through
	 * all the MIDINotes and applies the modifier to each note. Sort of like
	 * mapcar in LISP (but Java does not have lambdas so you need the
	 * NoteModifier interface).
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * MIDITrack notelist = new MIDITrack();
	 * MIDINote note = new MIDINote(Pitch.C5);
	 * notelist.add(note);
	 * notelist.map(new DurationModifier(1d, NoteModifier.Operation.ADD));
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param mod
	 *            a <code>NoteModifier</code> implementation.
	 * 
	 * 
	 * @see com.rockhoppertech.music.modifiers.NoteModifier
	 * @see com.rockhoppertech.music.modifiers.ChannelModifier
	 * @see com.rockhoppertech.music.modifiers.DurationModifier
	 * @see com.rockhoppertech.music.modifiers.InstrumentModifier
	 * @see com.rockhoppertech.music.modifiers.PitchModifier
	 * @see com.rockhoppertech.music.modifiers.StartBeatModifier
	 * @see com.rockhoppertech.music.midi.js.modifiers.TransposeModifier
	 */
	public MIDITrack map(NoteModifier mod) {
		for (MIDINote n : this) {
			mod.modify(n);
		}
		return this;
	}

	public MIDITrack map(MIDINoteModifier mod) {
		for (MIDINote n : this) {
			mod.modify(n);
		}
		return this;
	}

	/**
	 * <code>map</code> calls map(NoteModifier) only if the note's start beat is
	 * after the specified value.
	 * 
	 * @param mod
	 *            a <code>NoteModifier</code> implementation.
	 * @param after
	 *            a <code>double</code> value. Modify notes only after this
	 *            start beat.
	 * 
	 * @see com.rockhoppertech.music.midi.js.MIDITrack#map(NoteModifier)
	 */
	public MIDITrack map(NoteModifier mod, double after) {
		for (MIDINote n : this) {
			if (n.getStartBeat() >= after) {
				mod.modify(n);
			}
		}
		return this;
	}

	public MIDITrack map(MIDINoteModifier mod, double after) {
		for (MIDINote n : this) {
			if (n.getStartBeat() >= after) {
				mod.modify(n);
			}
		}
		return this;
	}

	/**
	 * <code>map</code> affects only notes after <b>after</b> and before
	 * <b>before</b>
	 * 
	 * @param mod
	 *            a <code>NoteModifier</code> value
	 * @param after
	 *            a <code>double</code> value
	 * @param before
	 *            a <code>double</code> value
	 * @see com.rockhoppertech.music.midi.js.MIDITrack#map(NoteModifier)
	 */
	public MIDITrack map(NoteModifier mod, double after, double before) {
		for (MIDINote n : this) {
			double s = n.getStartBeat();
			if (s >= after && s <= before) {
				mod.modify(n);
			}
		}
		return this;
	}

	public MIDITrack map(MIDINoteModifier mod, double after, double before) {
		for (MIDINote n : this) {
			double s = n.getStartBeat();
			if (s >= after && s <= before) {
				mod.modify(n);
			}
		}
		return this;
	}

	/**
	 * Modify MIDINotes only if the specified criteria tests true.
	 * 
	 * This is the preferred way. MIDITrack has two special criteria for
	 * start beats. With this you can build arbitrary criteria. If the specified
	 * critera are true then the note is modified.
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * MIDITrack notelist = new MIDITrack();
	 * MIDINote note = new MIDINote(Pitch.C5);
	 * notelist.add(note);
	 * etc.
	 * // before or equal to start beat 3 and the pitch is lower than or equal to E5
	 * ModifierCriteria criteria = new StartBeatCriteria(3d,
	 * 		ModifierCriteria.Operator.LT_EQ, new PitchCriteria(Pitch.E5,
	 * 				ModifierCriteria.Operator.LT_EQ, null));
	 * notelist.map(new StartBeatModifier(1d, NoteModifier.Operation.SET), criteria);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * 
	 * <p>
	 * I'm also playing around with commons
	 * <code>org.apache.commons.collections.Predicate</code> in package
	 * com.rockhoppertech.music.midi.js.modifiers.commons. Unfortunately commons
	 * has not been updated for Java 5 yet. And their Closure class; what will
	 * happen when closures are official?
	 * 
	 * @param mod
	 *            The NoteModifier
	 * @param criteria
	 *            The ModifierCritera
	 * 
	 * @see com.rockhoppertech.music.midi.js.MIDITrack#map(NoteModifier)
	 * @see com.rockhoppertech.music.modifiers.criteria.ModifierCriteria
	 */
//	public MIDITrack map(NoteModifier mod, ModifierCriteria criteria) {
//		for (MIDINote n : this) {
//			if (criteria.test(n)) {
//				if (logger.isDebugEnabled()) {
//					logger.debug("Passed " + n);
//				}
//				mod.modify(n);
//			} else {
//				if (logger.isDebugEnabled()) {
//					logger.debug("Failed " + n);
//				}
//			}
//		}
//		return this;
//	}

//	public MIDITrack map(MIDINoteModifier mod, ModifierCriteria criteria) {
//		for (MIDINote n : this) {
//			if (criteria.test(n)) {
//				if (logger.isDebugEnabled()) {
//					logger.debug("Passed " + n);
//				}
//				mod.modify(n);
//			} else {
//				if (logger.isDebugEnabled()) {
//					logger.debug("Failed " + n);
//				}
//			}
//		}
//		return this;
//	}

}
