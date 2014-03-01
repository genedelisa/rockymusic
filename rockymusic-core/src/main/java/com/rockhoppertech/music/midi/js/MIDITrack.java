package com.rockhoppertech.music.midi.js;

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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.sound.midi.MidiEvent;

import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.midi.parse.MIDIStringParser;
import com.rockhoppertech.music.modifiers.ChannelModifier;
import com.rockhoppertech.music.modifiers.DurationModifier;
import com.rockhoppertech.music.modifiers.InstrumentModifier;
import com.rockhoppertech.music.modifiers.MIDINoteModifier;
import com.rockhoppertech.music.modifiers.Modifier;
import com.rockhoppertech.music.modifiers.Modifier.Operation;
import com.rockhoppertech.music.modifiers.NoteModifier;
import com.rockhoppertech.music.modifiers.StartBeatModifier;
import com.rockhoppertech.music.modifiers.VelocityModifier;
import com.rockhoppertech.music.series.time.TimeSeries;

/**
 * 
 * A collection of {@code MIDINote}s and {@code MIDIEvent}s along with a pile of
 * things you can do to them
 * 
 * <p>
 * A rewrite of my ancient {@code MIDITrack} and {@code MIDINoteList} from the
 * 1990s.
 * <p>
 * I removed the JavaBean event code, thinking that JavaFX could handle events
 * easily. I've put it back in since I don't want JavaFX classes e.g.
 * {@code javafx.beans.property.DoubleProperty} here. So, the onus is on JavaFX
 * authors to handle property change events. Take a look at
 * {@code javafx.beans.property.adapter.JavaBeanStringPropertyBuilder} for
 * example.
 * 
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * @see javafx.beans.property.adapter.JavaBeanStringPropertyBuilder
 */

public class MIDITrack implements Serializable, Iterable<MIDINote> {

    private static final class AscendingPitchComparator implements
            Comparator<MIDINote>, Serializable {
        /**
         * Serialization.
         */
        private static final long serialVersionUID = 171644120355550199L;

        @Override
        public int compare(MIDINote o1, MIDINote o2) {
            if (o1.getMidiNumber() > o2.getMidiNumber()) {
                return 1;
            } else if (o1.getMidiNumber() < o2.getMidiNumber()) {
                return -1;
            }
            return 0;
        }
    }

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory
            .getLogger(MIDITrack.class);

    /**
     * JavaBean property.
     */
    public static final String MODIFIED = "MIDITrack.MODIFIED";
    /**
     * JavaBean property.
     */
    public static final String ADD = "MIDITrack.ADD";

    /**
     * JavaBean property.
     */
    public static final String REMOVE = "MIDITrack.REMOVED";

    /**
     * Meta text track name.
     */
    private String name;

    /**
     * Meta text inserted at tick 0.
     */
    private String description;

    /**
     * The events.
     */
    private List<MIDIEvent> events;

    /**
     * The notes will be turned into note on/note off messages.
     */
    private List<MIDINote> notes;

    /**
     * The GM patch to use.
     */
    private Instrument instrument = Instrument.PIANO;

    /**
     * The Score to which this track is attached. Might be null.
     */
    private Score score;

    /**
     * The time signatures.
     */
    private NavigableMap<Double, TimeSignature> timeSignatures =
            new TreeMap<Double, TimeSignature>();
    /**
     * The key signatures.
     */
    private NavigableMap<Double, KeySignature> keySignatureMap =
            new TreeMap<Double, KeySignature>();

    /**
     * The tempi.
     */
    private NavigableMap<Double, Integer> tempoMap =
            new TreeMap<Double, Integer>();

    /**
     * To parse midi strings into notes etc.
     */
    private transient MIDIStringParser midiStringParser =
            new MIDIStringParser();

    /**
     * JavaBean property event helper.
     */
    private transient PropertyChangeSupport changes = new PropertyChangeSupport(
            this);

    /**
     * Initializes a new {@code MIDITrack} with no {@code MIDINote}s nor
     * {@code MIDIEvent}s.
     */
    public MIDITrack() {
        this.events = new ArrayList<>();
        this.notes = new ArrayList<>();
    }

    /**
     * Initializes a new {@code MIDITrack} instance as a deep copy of specified
     * MIDITrack. Sort of like a C++ copy constructor (- but without C++ crap
     * like virtual destructors and overloaded operators...)
     * 
     * 
     * <pre>
     * {@code
     * MIDITrack track = new MIDITrack();
     * MIDINote note = new MIDINote(Pitch.C5);
     * track.add(note);
     * MIDITrack trackCopy = new MIDITrack(track);
     * }
     * </pre>
     * 
     * 
     * @param orig
     *            The {@code MIDITrack} that will be "cloned".
     */
    public MIDITrack(final MIDITrack orig) {
        this();
        if (orig.getName() != null) {
            name = orig.getName();
        }
        if (orig.getDescription() != null) {
            description = orig.getDescription();
        }

        for (MIDINote n : orig.notes) {
            notes.add(new MIDINote(n));
        }
        for (MIDIEvent n : orig.events) {
            events.add(new MIDIEvent(n.toMidiEvent(), this));
        }

        // NavigableMap<Double, TimeSignature> ts = orig.getTimeSignatures();
        // for (Iterator<Double> i = ts.keySet().iterator(); i.hasNext();) {
        // double time = i.next();
        // TimeSignature sig = ts.get(time);
        // this.timeSignatures.put(time, sig);
        // }
        //
        // NavigableMap<Double, KeySignature> ks = orig.keySignatureMap;
        // for (Double time : ks.keySet()) {
        // KeySignature sig = ks.get(time);
        // this.keySignatureMap.put(time, sig);
        // }
        // for (Iterator<Double> i = ts.keySet().iterator(); i.hasNext();) {
        // double time = i.next();
        // KeySignature sig = ks.get(time);
        // this.keySignatureMap.put(time, sig);
        // }

    }

    /**
     * Initializes a new <code>MIDITrack</code> instance. The MIDINotes from the
     * provided collection are copied - not cloned.
     * 
     * @param c
     *            a {@code Collection<MIDINote>} value
     */
    public MIDITrack(Collection<MIDINote> c) {
        this();
        for (MIDINote n : c) {
            notes.add(n);
        }
    }

    /**
     * Parses a note string. It is not sequential by default since the
     * noteString may contain start beats and durations.
     * 
     * @param noteString
     *            a note string
     * @see MIDIStringParser
     */
    public MIDITrack(final String noteString) {
        this();
        midiStringParser.parseString(this, noteString);
    }

    /**
     * Initialize a track from a Guava Iterable of MIDINote.
     * 
     * @param iterable
     *            a Guava Iterable
     */
    public MIDITrack(final Iterable<MIDINote> iterable) {
        this(Lists.newArrayList(iterable));
    }

    /**
     * @return the description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Adds the event. {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * @param event
     *            the event to add
     * @return this to cascade calls
     */
    public final MIDITrack add(final MIDIEvent event) {
        events.add(event);
        event.setTrack(this);
        this.changes.firePropertyChange(ADD, null, this);
        return this;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name
     *            the name
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the score
     */
    public final Score getScore() {
        return score;
    }

    /**
     * @param score
     *            the score to set
     */
    public final void setScore(final Score score) {
        this.score = score;
    }

    /**
     * Returns an unmodifiable {@code List} of the {@code MIDIEvent}s.
     * 
     * @return a {@code List} of {@code MIDIEvent}s
     */
    public final List<MIDIEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    /**
     * Set the events in this track. {@code PropertyChange MIDITrack.MODIFIED}
     * is fired.
     * 
     * @param events
     *            a List of events
     */
    public void setEvents(final List<MIDIEvent> events) {
        this.events = Lists.newArrayList(events);
        this.changes.firePropertyChange(MODIFIED, null, this);
    }

    /**
     * Returns an unmodifiable {@code List} of the {@code MIDINote}s.
     * 
     * @return a {@code List} of {@code MIDINote}s
     */
    public List<MIDINote> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    public void setNotes(List<MIDINote> notes) {
        this.notes = Lists.newArrayList(notes);
        this.changes.firePropertyChange(MODIFIED, null, this);

    }

    @Override
    public Iterator<MIDINote> iterator() {
        return notes.iterator();
    }

    /**
     * Append a {@code MIDINote} to this track.
     * {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * @param note
     *            the note to append
     * @return this to cascade calls
     */
    public MIDITrack append(final MIDINote note) {
        double end = getEndBeat();
        note.setStartBeat(end);
        notes.add(note);
        this.changes.firePropertyChange(ADD, null, this);
        this.changes.firePropertyChange("notes", null, this);
        return this;
    }

    /**
     * Creates a {@code MIDINote} and appends it to this track.
     * {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * @param midiNumber
     *            a MIDI pitch number
     * @return this to cascade calls
     */
    public MIDITrack append(final int midiNumber) {
        MIDINote note = new MIDINote(midiNumber);
        return this.append(note);
    }

    /**
     * Retrieve the number of {@code MIDINote}s in this track.
     * 
     * @return the number of notes in the track
     */
    public int size() {
        return notes.size();
    }

    /**
     * Predicate to test if the specified {@code Pitch} is in this track.
     * 
     * @param p
     *            a {@code Pitch} instance
     * @return true if the pitch is in this track
     */
    public boolean contains(final Pitch p) {
        for (MIDINote n : this) {
            if (n.getPitch().equals(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the {@code MIDINote} at the specified index.
     * 
     * @param i
     *            an index
     * @return a {@code MIDINote}
     */
    public MIDINote get(final int i) {
        return notes.get(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Track Name:").append(name).append('\n');
        if (this.description != null || this.description.equals(""))
            sb.append("Description:").append(this.description).append('\n');
        // sb.append("Instrument:").append(this.gmpatch).append('\n');
        sb.append("Instrument:").append(this.instrument).append('\n');

        if (this.notes.size() > 0) {
            for (MIDINote n : notes) {
                sb.append(n).append('\n');
            }
        } else {
            sb.append("no notes").append('\n');
        }

        if (this.events.size() > 0) {
            sb.append("events").append('\n');
            for (MIDIEvent n : events) {
                sb.append(n.toReadableString()).append('\n');
                sb.append(n.toString()).append('\n');
            }
        } else {
            sb.append("no events").append('\n');
        }

        NavigableMap<Double, KeySignature> keys = this.getKeySignatures();
        for (Double time : keys.keySet()) {
            KeySignature key = keys.get(time);
            sb.append("Key: ").append(key.getDisplayName()).append(" at beat ")
                    .append(time).append('\n');
        }

        NavigableMap<Double, TimeSignature> timeSigs = this
                .getTimeSignatures();
        for (Double time : timeSigs.keySet()) {
            TimeSignature ts = timeSigs.get(time);
            sb.append("Time signature: ").append(ts.getDisplayName())
                    .append(" at beat ").append(time).append('\n');
        }

        NavigableMap<Double, Integer> tempoMap = this.getTempoMap();
        for (Double time : tempoMap.keySet()) {
            Integer tempo = tempoMap.get(time);
            sb.append("Tempo: ").append(tempo).append(" at beat ").append(time)
                    .append('\n');
        }

        return sb.toString();
    }

    /**
     * Creates a {@code MIDIStringParser} compatible string with the pitch,
     * start beat, and duration of each {@code MIDINote} in the track.
     * 
     * @return a string
     * @see MIDIStringParser
     */
    public String toBriefMIDIString() {
        StringBuilder sb = new StringBuilder();

        for (MIDINote note : notes) {

            String spelling = note.getSpelling();
            if (spelling == null) {

                spelling = PitchFormat.getInstance().format(
                        note.getPitch());
            }
            sb.append(spelling.trim()).append(",");

            // sb.append(PitchFormat.getInstance().format(note.getPitch()).trim())
            // .append(",");
            sb.append(note.getStartBeat()).append(",");
            sb.append(note.getDuration()).append(' ');
        }

        // for (MIDIEvent n : this.events) {
        // sb.append(n.toReadableString()).append('\n');
        // }

        return sb.toString();
    }

    public String toBriefMIDIString(String delimiter) {
        StringBuilder sb = new StringBuilder();

        for (MIDINote note : notes) {

            String spelling = note.getSpelling();
            if (spelling == null) {
                spelling = PitchFormat.getInstance().format(
                        note.getPitch());
            }
            sb.append(spelling.trim()).append(",");
            sb.append(note.getStartBeat()).append(",");
            sb.append(note.getDuration()).append(delimiter);
        }
        return sb.toString();
    }

    /**
     * Creates a {@code MIDIStringParser} compatible string with all the
     * properties of each {@code MIDINote} in the track.
     * 
     * Pitch, start beat, duration, velocity, pan, channel, bank, program, pitch
     * bend.
     * 
     * @return a string
     * @see MIDIStringParser
     */
    public String toMIDIString() {
        StringBuilder sb = new StringBuilder();
        for (MIDINote note : notes) {
            String s = String.format("%s,%.3f,%.3f,%d,%d,%d,%d,%d,%d",
                    PitchFormat.getInstance().format(note.getPitch()).trim(),
                    note.getStartBeat(),
                    note.getDuration(),
                    note.getVelocity(),
                    note.getPan(),
                    note.getChannel(),
                    note.getBank(),
                    note.getProgram(),
                    note.getPitchBend()
                    );
            sb.append(s).append("\n");
        }

        return sb.toString();
    }

    /**
     * returns the last {@code MIDINote} in the list
     * 
     * @return a {@code MIDINote} that is last in the list
     */
    public MIDINote getLastNote() {
        return get(notes.size() - 1);
    }

    /**
     * Create and append the specified {@code MIDINote} to the list. The Note's
     * timing is unchanged. Use <code>append</code> if you want the timing
     * changed.
     * <p>
     * {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * 
     * <pre>
     * {@code
     * MIDITrack track = new MIDITrack();
     * MIDINote note = new MIDINote(Pitch.C5);
     * track.add(note);
     * }
     * </pre>
     * 
     * 
     * @param note
     *            The MIDINote instance to append to the list.
     * 
     * @see com.rockhoppertech.music.midi.js.MIDINote
     * @see MIDITrack#append(MIDINote)
     * @return this to cascade
     */
    public MIDITrack add(MIDINote note) {
        notes.add(note);
        note.setMidiTrack(this);
        this.changes.firePropertyChange(ADD, null, this);

        return this;
    }

    /**
     * Add notes to the track. {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * @param notes
     *            notes to add
     * @return this to cascade calls
     */
    public MIDITrack add(MIDINote... notes) {
        for (MIDINote note : notes) {
            this.notes.add(note);
            note.setMidiTrack(this);
        }
        this.changes.firePropertyChange(ADD, null, this);
        return this;
    }

    /**
     * Create and append a new {@code MIDINote} to the list. All the default
     * values besides pitch are set (e.g. startbeat = 1, duration = 1.)
     * <p>
     * {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * 
     * <pre>
     * {@code
     * MIDITrack track = new MIDITrack();
     * track.add("C5").add("D5");
     * }
     * </pre>
     * 
     * 
     * @param pitch
     *            The pitch name as a String e.g. C5 to be parsed by
     *            PitchFactory
     * 
     * @see com.rockhoppertech.music.midi.js.MIDINote
     * @see com.rockhoppertech.music.PitchFactory
     * @return this to cascade
     */
    public MIDITrack add(String pitch) {
        MIDINote note = new MIDINote(PitchFactory.getPitch(pitch)
                .getMidiNumber());
        notes.add(note);
        note.setMidiTrack(this);
        this.changes.firePropertyChange(ADD, null, this);
        return this;
    }

    /**
     * Creates a {@code MIDINote} with the specified pitch and adds it to the
     * track. {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * @param midiNumber
     *            pitch number
     * @return this to cascade
     */
    public MIDITrack add(int midiNumber) {
        MIDINote note = new MIDINote(midiNumber);
        notes.add(note);
        note.setMidiTrack(this);
        this.changes.firePropertyChange(ADD, null, this);
        return this;
    }

    /**
     * Creates {@code MIDINote}s with the specified pitch and adds it to the
     * track. {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * @param midiNumbers
     *            pitches
     * @return this to cascade calls
     */
    public MIDITrack add(int... midiNumbers) {
        // MIDINote[] a = new MIDINote[midiNumbers.length];
        // int count = 0;
        for (int mn : midiNumbers) {
            MIDINote note = new MIDINote(mn);
            // a[count++] = n;
            notes.add(note);
            note.setMidiTrack(this);
        }
        this.changes.firePropertyChange(ADD, null, this);
        return this;
    }

    /**
     * Create and append a new {@code MIDINote} to the list.
     * <p>
     * {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * 
     * <pre>
     * {@code
     * MIDITrack track = new MIDITrack();
     * track.add(Pitch.C5, 2d).add(Pitch.D5, Duration.Q);
     * }
     * </pre>
     * 
     * 
     * @param pitch
     *            The pitch name e.g. C5 to be parsed by PitchFactory
     * @param duration
     *            The new MIDINote's duration
     * 
     * @see com.rockhoppertech.music.midi.js.MIDINote
     * @see com.rockhoppertech.music.PitchFactory
     * @return this to cascade
     */
    public MIDITrack add(String pitch, double duration) {
        MIDINote note = new MIDINote(PitchFactory.getPitch(pitch)
                .getMidiNumber());
        note.setDuration(duration);
        note.setMidiTrack(this);
        notes.add(note);
        this.changes.firePropertyChange(ADD, null, this);
        return this;
    }

    /**
     * Create and append a new {@code MIDINote} to the list.
     * <p>
     * {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * 
     * <pre>
     * {@code
     * MIDITrack track = new MIDITrack();
     * track.add("C5", 1.5, 2d).add("D5", 2.5, 2d);
     * }
     * </pre>
     * 
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
     * @return this to cascade
     */
    public MIDITrack add(String pitch, double startBeat, double duration) {
        MIDINote note = new MIDINote(PitchFactory.getPitch(pitch)
                .getMidiNumber());
        note.setStartBeat(startBeat);
        note.setDuration(duration);
        note.setMidiTrack(this);
        notes.add(note);
        this.changes.firePropertyChange(ADD, null, this);
        return this;
    }

    /**
     * Removes the specified {@code MIDINote} from the track.
     * <p>
     * {@code PropertyChange MIDITrack.REMOVE} is fired.
     * 
     * <pre>
     * {@code
     * MIDITrack track = new MIDITrack();
     * MIDINote note = new MIDINote(Pitch.C5);
     * track.add(note);
     * track.remove(note);
     * }
     * </pre>
     * 
     * 
     * @param note
     *            The MIDINote to remove
     * @see com.rockhoppertech.music.midi.js.MIDINote
     */
    public void remove(MIDINote note) {
        boolean b = false;
        b = notes.remove(note);
        note.setMidiTrack(null);
        if (!b) {
            logger.debug(String
                    .format("%s did not exist in this track%n", note));
            return;
        }
        this.changes.firePropertyChange(REMOVE, null, this);
    }

    public void removePitch(Pitch p) {
        for (Iterator<MIDINote> i = iterator(); i.hasNext();) {
            MIDINote note = i.next();
            if (note.getMidiNumber() == p.getMidiNumber()) {
                i.remove();
                note.setMidiTrack(null);
            }
        }
        this.changes.firePropertyChange(REMOVE, null, this);

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
     * {@code PropertyChange MIDITrack.REMOVE} is fired.
     * 
     * <pre>
     * {@code
     * MIDITrack track = new MIDITrack();
     * MIDINote note = new MIDINote(Pitch.C5);
     * track.add(note);
     * track.remove(0);
     * }
     * </pre>
     * 
     * 
     * @param index
     *            an <code>int</code> value
     * @see com.rockhoppertech.music.midi.js.MIDINote
     */
    public void remove(int index) {
        MIDINote note = get(index);
        notes.remove(note);
        note.setMidiTrack(null);
        this.changes.firePropertyChange(REMOVE, null, this);
    }

    /**
     * <code>remove</code>
     * 
     * @param event
     *            a <code>event</code> value
     */
    public void remove(MIDIEvent event) {
        events.remove(event);
        event.setTrack(null);

    }

    /**
     * <code>merge</code> merges and sorts the additions based on start time.
     * MIDINotes and MIDIEvents are comparable.
     * 
     * Duplicates the notes and events.
     * 
     * {@code PropertyChange MIDITrack.ADD} is fired.
     * 
     * @param l
     *            a <code>MIDITrack</code> value
     * @return this to cascade
     */
    public MIDITrack merge(MIDITrack l) {
        // notes.addAll(l.notes);
        for (MIDINote n : l.notes) {
            MIDINote dupe = n.duplicate();
            this.add(dupe);
        }
        for (MIDIEvent e : l.events) {
            MIDIEvent dupe = e.duplicate();
            this.add(dupe);
        }
        sortByStartBeat();
        this.changes.firePropertyChange(ADD, null, this);
        return this;
    }

    /**
     * Removes the notes and events from this track, and set's the track
     * reference for each back to the parameter track.
     * 
     * @param l
     *            another MIDITrack
     * @return this to cascade
     */
    public MIDITrack unmerge(MIDITrack l) {
        for (MIDINote n : l.notes) {
            this.remove(n);
            n.setMidiTrack(l);
        }
        for (MIDIEvent e : l.events) {
            this.remove(e);
            e.setTrack(l);
        }
        return this;
    }

    // TODO need this?
    // public MIDITrack merge(MIDITrack l, int index, boolean sort) {
    // notes.addAll(index, l.notes);
    //
    //
    //
    // if (sort) {
    // Collections.sort(notes);
    // }
    // return this;
    // }

    public int getResolution() {
        int resolution = 480;
        if (this.score != null) {
            resolution = this.score.getResolution();
        }
        return resolution;
    }

    public void sort(Comparator<MIDINote> c) {
        Collections.sort(notes, c);
    }

    /**
     * Note and MIDIEvent implement comparable which compares start beats.
     */
    public void sortByStartBeat() {
        Collections.sort(notes);
        Collections.sort(events);
    }

    /**
     * This does not change the start beats. It orders the list by pitch. This
     * is useful for chord production.
     */
    public void sortByAscendingPitches() {
        Comparator<MIDINote> comp = new AscendingPitchComparator();
        Collections.sort(notes, comp);
    }

    public MIDITrack insertListAtIndex(MIDITrack newlist, int index) {
        // this.notes.addAll(index, newlist.notes);
        newlist.sortByStartBeat();

        List<MIDINote> beginning = notes.subList(0, index);
        List<MIDINote> end = notes.subList(index, notes.size());
        MIDITrack n = new MIDITrack(beginning);
        MIDITrack endlist = new MIDITrack(end);

        double endBeat = n.getEndBeat();
        newlist.map(new StartBeatModifier(NoteModifier.Operation.ADD, endBeat));
        for (MIDINote note : newlist.notes) {
            MIDINote dupe = note.duplicate();
            n.add(dupe);
        }
        endBeat = n.getEndBeat();
        endlist.map(new StartBeatModifier(NoteModifier.Operation.ADD, endBeat));
        for (MIDINote note : endlist.notes) {
            MIDINote dupe = note.duplicate();
            n.add(dupe);
        }
        return n;

        // this.sequential();
    }

    /**
     * this is cumulative.
     * 
     * @param track
     *            the track
     * @param n
     *            number of times to append
     * @return a MIDITrack
     */
    public MIDITrack appendNTimes(MIDITrack track, int n) {
        MIDITrack r = null;
        for (int i = 0; i < n; i++) {
            r = this.append(track, 0);
        }
        return r;
    }

    /**
     * Appends the track to the current one. The start beats of the parameter
     * track are modified to start after the current track.
     * 
     * @param track
     *            the track to append
     * @return this to cascade
     */
    public MIDITrack append(MIDITrack track) {
        return this.append(track, 0);
    }

    /**
     * Append the entire track with the specified gap.
     * 
     * @param track
     *            the track to append
     * @param gap
     *            the duration between the end of the track and the appended
     *            track
     * @return this to cascade
     */
    public MIDITrack append(MIDITrack track, double gap) {
        int fromIndex = 0;
        int toIndex = track.size();
        return this.append(track, gap, fromIndex, toIndex);
    }

    /**
     * Appends the parameter track to the current one with an optional gap
     * between them. A negative value for the gap will overlap the tracks. The
     * Notes are cloned.
     * 
     * {@code nl.append(nl2, 0).append(nl3, 0);}
     * 
     * @param track
     *            the track
     * @param gap
     *            the durational space between the tracks in beats
     * @param fromIndex
     *            low endpoint (inclusive) of the subList.
     * @param toIndex
     *            high endpoint (exclusive) of the subList.
     * @return this to cascade calls
     */
    public MIDITrack append(MIDITrack track, double gap,
            int fromIndex, int toIndex) {
        if (this == track) {
            track = new MIDITrack(this);
        }
        double end = getEndBeat() + gap;

        logger.debug("track {}", track);
        logger.debug("end which is endbeat + gap {}", end);
        logger.debug("gap {}", gap);
        logger.debug("this.getEndBeat() {}", getEndBeat());
        logger.debug("this.size() {}", size());

        // case if the track appended to is empty.
        if (size() == 0) {
            end = 1d;
            track
                    .map(new StartBeatModifier(NoteModifier.Operation.SET, end));
            logger.debug("setting to end {}", end);
        } else {
            // track
            // .map(new StartBeatModifier(NoteModifier.Operation.ADD, end));
            // logger.debug("adding to end {} ", end);
        }

        logger.debug("track after start beat mod {}", track);

        List<MIDINote> sub = track.notes.subList(fromIndex, toIndex);
        // sub.get(0).setStartBeat(end);
        // logger.debug("sublist {} from {} to {}", sub, fromIndex, toIndex);
        // sub = sequential(sub);
        // logger.debug("sublist after mod {} END {}", sub, end);

        double substart = sub.get(0).getStartBeat();
        for (MIDINote note : sub) {
            MIDINote noteClone = note.duplicate();
            logger.debug("clone {}", noteClone);
            noteClone.setStartBeat(noteClone.getStartBeat() + end - substart);
            this.add(noteClone);
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
        notes.removeAll(l.notes);
    }

    /**
     * <code>retain</code> discards all MIDINotes that are not in the specified
     * list.
     * 
     * @param l
     *            a <code>MIDITrack</code> value
     * @return true on success
     */
    public boolean retain(MIDITrack l) {
        boolean ret = notes.retainAll(l.notes);
        logger.debug("retainAll returned ", ret);
        return ret;
    }

    /**
     * <code>clear</code> emptys the track.
     * 
     */
    public void clear() {
        notes.clear();
        events.clear();
    }

    /**
     * <code>getStartBeat</code> returns the start beat of the first Note. track
     * is sorted on the start beat because Note implements Comparable.
     * 
     * @return a <code>double</code> value
     */
    public double getStartBeat() {
        if (notes.isEmpty()) {
            return 0d;
        }
        MIDINote first = notes.get(0);
        return first.getStartBeat();
    }

    /**
     * <code>getEndBeat</code>
     * 
     * @return a <code>double</code> value
     */
    public double getEndBeat() {
        double endBeat = 1d;
        for (MIDINote n : notes) {
            double d = n.getEndBeat();
            if (d > endBeat) {
                endBeat = d;
            }
        }
        return endBeat;
    }

    public double getShortestDuration() {
        double shortest = Double.MAX_VALUE;
        for (MIDINote n : notes) {
            double d = n.getDuration();
            if (d < shortest) {
                shortest = d;
            }
        }
        return shortest;
    }

    public double getLongestDuration() {
        double longest = Double.MIN_VALUE;
        for (MIDINote n : notes) {
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
        int len = notes.size();
        int[] intervals = new int[len - 1];
        for (int i = 0; i < intervals.length; i++) {
            MIDINote i1 = notes.get(i);
            MIDINote i2 = notes.get(i + 1);
            intervals[i] = i2.getMidiNumber() - i1.getMidiNumber();
        }
        return intervals;
    }

    /**
     * There intervals are in relation to the first MIDINote's MIDI number. Like
     * a Chord.
     * 
     * @return an array of intervals
     */
    public int[] getPitchIntervalsAbsolute() {
        int len = notes.size();
        int[] intervals = new int[len - 1];
        int root = notes.get(0).getMidiNumber();
        for (int i = 1; i <= intervals.length; i++) {
            MIDINote i1 = notes.get(i);
            intervals[i - 1] = i1.getMidiNumber() - root;
        }
        return intervals;
    }

    /**
     * Reset each note's startBeat. The pad is 0.
     * 
     * @return this
     */
    public MIDITrack sequential() {

        return this.sequential(0);
    }

    /**
     * Reset each note's startbeat in the track to be sequential based on the
     * duration. The first note is unaffected.
     * 
     * @param pad
     *            amount added between the notes
     * @return this to cascade calls
     */
    public MIDITrack sequential(int pad) {
        int size = size();
        if (size == 0) {
            return this;
        }
        MIDINote n = notes.get(0);
        double s = n.getStartBeat();
        double d = n.getDuration();
        logger.debug("startbeat0: {}", s);
        logger.debug("dur0: {}", d);

        for (int i = 1; i < size; i++) {
            MIDINote nn = notes.get(i);
            logger.debug("old startbeat:" + nn.getStartBeat());
            nn.setStartBeat(s + d + pad);
            logger.debug("new startbeat:" + nn.getStartBeat());
            s = nn.getStartBeat();
            d = nn.getDuration();

        }

        this.changes.firePropertyChange(MODIFIED, null, this);
        return this;
    }

    /**
     * Modify the start beats of the given list to be sequential.
     * 
     * Fires MODIFIED.
     * 
     * @param list
     *            a {@code List} of {@code MIDINote}s
     * @return a sequential {@code List} of {@code MIDINote}s
     */
    public List<MIDINote> sequential(List<MIDINote> list) {
        int size = list.size();
        MIDINote n = list.get(0);
        double s = n.getStartBeat();
        double d = n.getDuration();
        logger.debug("startbeat0: {}", s);
        logger.debug("dur0: {}", d);

        for (int i = 1; i < size; i++) {
            MIDINote nn = list.get(i);
            logger.debug("old startbeat: {}", nn.getStartBeat());
            nn.setStartBeat(s + d);
            logger.debug("new startbeat: {}", nn.getStartBeat());
            s = nn.getStartBeat();
            d = nn.getDuration();

        }
        this.changes.firePropertyChange(MODIFIED, null, this);
        return list;
    }

    /**
     * <code>map</code> is the GoF visitor(331) design pattern. It loops through
     * all the MIDINotes and applies the modifier to each note. Sort of like
     * mapcar in LISP (but Java does not have lambdas - yet - so you need the
     * NoteModifier interface).
     * 
     * <pre>
     * {@code
     * MIDITrack track = new MIDITrack();
     * MIDINote note = new MIDINote(Pitch.C5);
     * track.add(note);
     * track.map(new DurationModifier(1d, Modifier.Operation.ADD));
     * }
     * </pre>
     * 
     * @param mod
     *            a <code>NoteModifier</code> implementation.
     * 
     * 
     * @see com.rockhoppertech.music.modifiers.Modifier
     * @see com.rockhoppertech.music.modifiers.NoteModifier
     * @see com.rockhoppertech.music.modifiers.ChannelModifier
     * @see com.rockhoppertech.music.modifiers.DurationModifier
     * @see com.rockhoppertech.music.modifiers.InstrumentModifier
     * @see com.rockhoppertech.music.modifiers.PitchModifier
     * @see com.rockhoppertech.music.modifiers.StartBeatModifier
     * @return this to cascade calls
     */
    public MIDITrack map(NoteModifier mod) {
        for (MIDINote n : this) {
            mod.modify(n);
        }
        this.changes.firePropertyChange(MODIFIED, null, this);
        return this;
    }

    /**
     * {@code map} is the GoF visitor(331) design pattern. It loops through all
     * the MIDINotes and applies the modifier to each note. Sort of like mapcar
     * in LISP (but Java does not have lambdas - yet - so you need the
     * {@code MIDINoteModifier} interface).
     * 
     * <pre>
     * {@code
     * MIDITrack track = new MIDITrack();
     * MIDINote note = new MIDINote(Pitch.C5);
     * track.add(note);
     * track.map(new VelocityModifier(1d, Modifier.Operation.ADD));
     * }
     * </pre>
     * 
     * @param mod
     *            a {@code MIDINoteModifier} implementation.
     * 
     * 
     * @see com.rockhoppertech.music.modifiers.Modifier
     * @see com.rockhoppertech.music.modifiers.MIDINoteModifier
     * @see com.rockhoppertech.music.modifiers.ChannelModifier
     * @see com.rockhoppertech.music.modifiers.InstrumentModifier
     * @return this to cascade calls
     */
    public MIDITrack map(MIDINoteModifier mod) {
        for (MIDINote n : this) {
            mod.modify(n);
        }
        this.changes.firePropertyChange(MODIFIED, null, this);

        return this;
    }

    /**
     * {@code map} calls the {@code NoteModifier}'s map only if the note's start
     * beat is after the specified value.
     * 
     * @param mod
     *            a <code>NoteModifier</code> implementation.
     * @param after
     *            a <code>double</code> value. Modify notes only after this
     *            start beat.
     * 
     * @see com.rockhoppertech.music.midi.js.MIDITrack#map(NoteModifier)
     * @return this to cascade calls
     */
    public MIDITrack map(NoteModifier mod, double after) {
        for (MIDINote n : this) {
            if (n.getStartBeat() >= after) {
                mod.modify(n);
            }
        }
        this.changes.firePropertyChange(MODIFIED, null, this);

        return this;
    }

    /**
     * {@code map} calls the {@code MIDINoteModifier}'s map only if the note's
     * start beat is after the specified value.
     * 
     * @param mod
     *            a MIDINoteModifier
     * @param after
     *            modify MIDINotes only after this start beat
     * @return this to cascade calls
     */
    public MIDITrack map(MIDINoteModifier mod, double after) {
        for (MIDINote n : this) {
            if (n.getStartBeat() >= after) {
                mod.modify(n);
            }
        }
        this.changes.firePropertyChange(MODIFIED, null, this);

        return this;
    }

    /**
     * {@code map} calls the {@code NoteModifier}'s map only if the note's start
     * beat is after the specified value and before the before the param.
     * 
     * @param mod
     *            a <code>NoteModifier</code> value
     * @param after
     *            a <code>double</code> value
     * @param before
     *            a <code>double</code> value
     * @see com.rockhoppertech.music.midi.js.MIDITrack#map(NoteModifier)
     * @return this to cascade calls
     */
    public MIDITrack map(NoteModifier mod, double after, double before) {
        for (MIDINote n : this) {
            double s = n.getStartBeat();
            if (s >= after && s <= before) {
                mod.modify(n);
            }
        }
        this.changes.firePropertyChange(MODIFIED, null, this);

        return this;
    }

    /**
     * {@code map} calls the {@code MIDINoteModifier}'s map only if the note's
     * start beat is after the specified value and before the before the param.
     * 
     * @param mod
     *            a <code>MIDINoteModifier</code> instance
     * @param after
     *            a <code>double</code> value
     * @param before
     *            a <code>double</code> value
     * @see com.rockhoppertech.music.midi.js.MIDITrack#map(MIDINoteModifier)
     * @return this to cascade calls
     */
    public MIDITrack map(MIDINoteModifier mod, double after, double before) {
        for (MIDINote n : this) {
            double s = n.getStartBeat();
            if (s >= after && s <= before) {
                mod.modify(n);
            }
        }
        this.changes.firePropertyChange(MODIFIED, null, this);

        return this;
    }

    /*
     * Modify MIDINotes only if the specified criteria tests true.
     * 
     * This is the preferred way. MIDITrack has two special criteria for start
     * beats. With this you can build arbitrary criteria. If the specified
     * critera are true then the note is modified.
     * 
     * <blockquote>
     * 
     * <pre> MIDITrack track = new MIDITrack(); MIDINote note = new
     * MIDINote(Pitch.C5); track.add(note); etc. // before or equal to start
     * beat 3 and the pitch is lower than or equal to E5 ModifierCriteria
     * criteria = new StartBeatCriteria(3d, ModifierCriteria.Operator.LT_EQ, new
     * PitchCriteria(Pitch.E5, ModifierCriteria.Operator.LT_EQ, null));
     * track.map(new StartBeatModifier(1d, NoteModifier.Operation.SET),
     * criteria); </pre>
     * 
     * </blockquote>
     * 
     * 
     * <p> I'm also playing around with commons
     * <code>org.apache.commons.collections.Predicate</code> in package
     * com.rockhoppertech.music.midi.js.modifiers.commons. Unfortunately commons
     * has not been updated for Java 5 yet. And their Closure class; what will
     * happen when closures are official?
     * 
     * @param mod The NoteModifier
     * 
     * @param criteria The ModifierCritera
     * 
     * @see com.rockhoppertech.music.midi.js.MIDITrack#map(NoteModifier)
     * 
     * @see com.rockhoppertech.music.modifiers.criteria.ModifierCriteria
     */
    // public MIDITrack map(NoteModifier mod, ModifierCriteria criteria) {
    // for (MIDINote n : this) {
    // if (criteria.test(n)) {
    // if (logger.isDebugEnabled()) {
    // logger.debug("Passed " + n);
    // }
    // mod.modify(n);
    // } else {
    // if (logger.isDebugEnabled()) {
    // logger.debug("Failed " + n);
    // }
    // }
    // }
    // return this;
    // }

    // public MIDITrack map(MIDINoteModifier mod, ModifierCriteria criteria) {
    // for (MIDINote n : this) {
    // if (criteria.test(n)) {
    // if (logger.isDebugEnabled()) {
    // logger.debug("Passed " + n);
    // }
    // mod.modify(n);
    // } else {
    // if (logger.isDebugEnabled()) {
    // logger.debug("Failed " + n);
    // }
    // }
    // }
    // return this;
    // }

    /**
     * Creates a {@code MIDIPerformer} and plays this track.
     */
    public void play() {
        MIDIPerformer perf = new MIDIPerformer();
        perf.play(this);
    }

    /**
     * <code>retrograde</code> reverses the order of the MIDINotes. The start
     * beats are not in order! It's up to you to change those if you so desire.
     * You might use sequential() followed by setStartBeat().
     * 
     * The original track is not modified either.
     * 
     * @return a new <code>MIDITrack</code>
     */
    public final MIDITrack retrograde() {
        List<MIDINote> retro = new ArrayList<>();

        for (MIDINote n : notes) {
            retro.add((MIDINote) n.duplicate());
        }
        Collections.reverse(retro);
        MIDITrack t = new MIDITrack(retro);
        // double startBeat = t.get(0).getStartBeat() + 1d; // beats are 1 based
        // logger.debug("retro start is {}", startBeat);
        // StartBeatModifier m = new StartBeatModifier(Operation.SUBTRACT,
        // startBeat);
        // t.map(m);
        // t.sequential();
        return t;
    }

    /**
     * Iterate through the track to find the {@code MIDINote} with the lowest
     * pitch midi number.
     * 
     * @return the lowest note
     */
    public MIDINote getLowestPitchedNote() {
        MIDINote n = new MIDINote(Pitch.GS9);
        for (MIDINote e : notes) {
            if (e.getMidiNumber() < n.getMidiNumber()) {
                n = e;
            }
        }
        return n;
    }

    /**
     * Iterate through the track to find the {@code MIDINote} with the highest
     * pitch midi number.
     * 
     * @return the lowest note
     */
    public MIDINote getHighestPitchedNote() {
        MIDINote n = new MIDINote(Pitch.A0);
        for (MIDINote e : notes) {
            if (e.getMidiNumber() > n.getMidiNumber()) {
                n = e;
            }
        }
        return n;
    }

    /**
     * Retrieve the duration of the entire MIDItrack. Literally the end beat
     * minus the startbeat.
     * 
     * @return the duration of the track in beats
     */
    public double getDuration() {
        return getEndBeat() - getStartBeat();
    }

    /**
     * Get a space delimited string representation of the durations of all the
     * {@code MIDINote}s in the track.
     * 
     * @param n
     *            a {@code MIDITrack}
     * @return a string
     */
    public static String getDurationsAsString(MIDITrack n) {
        StringBuilder sb = new StringBuilder();
        for (MIDINote note : n) {
            sb.append(note.getDuration()).append(' ');

        }
        return sb.toString();
    }

    /**
     * Get a space delimited string representation of the pitches of all the
     * {@code MIDINote}s in the track.
     * 
     * @param n
     *            a {@code MIDITrack}
     * @return a string
     */
    public static String getPitchesAsString(MIDITrack n) {
        StringBuilder sb = new StringBuilder();
        for (MIDINote note : n) {
            sb.append(note.getPitch().getPreferredSpelling()).append(' ');
            // sb.append(PitchFormat.getInstance().format(note.getPitch()))
            // .append(' ');
        }
        return sb.toString();
    }

    public static String getPitchesMIDINumbersAsString(MIDITrack n) {
        StringBuilder sb = new StringBuilder();
        for (MIDINote note : n) {
            sb.append(note.getPitch().getMidiNumber()).append(' ');
        }
        return sb.toString();
    }

    /**
     * Get a space delimited string representation of the start beats of all the
     * {@code MIDINote}s in the track.
     * 
     * @param n
     *            a {@code MIDITrack}
     * @return a string
     */
    public static String getStartBeatsAsString(MIDITrack n) {
        StringBuilder sb = new StringBuilder();
        for (MIDINote note : n) {
            sb.append(note.getStartBeat()).append(' ');
        }
        return sb.toString();
    }

    /**
     * <p>
     * Get the index of the specified note.
     * </p>
     * 
     * @param note
     *            a note in the track
     * @return the note's index or -1 if not found
     */
    public int indexOfNote(MIDINote note) {
        return notes.indexOf(note);
    }

    /**
     * {@code sublist} will get MIDINotes with start times between the given
     * times (inclusive). The MIDINotes contained are "live" - not cloned unless
     * you specify the clone parameter
     * 
     * @param after
     *            a <code>double</code> value
     * @param before
     *            a <code>double</code> value
     * @param clone
     *            clone the Notes
     * @return a <code>MIDITrack</code> value
     */
    public MIDITrack sublist(double after, double before, boolean clone) {
        MIDITrack list = new MIDITrack();
        for (MIDINote n : this) {
            double s = n.getStartBeat();
            if (s >= after && s <= before) {
                if (clone) {
                    list.add((MIDINote) n.duplicate());
                } else {
                    list.add(n);
                }
            }
        }
        return list;
    }

    // using apache collections predicates would make this nonsense easier
    /**
     * {@code sublist} will get MIDINotes with start times between the given
     * time.
     * 
     * @param after
     *            after this start beat
     * @param before
     *            before this start beat
     * @param clone
     *            make a clone
     * @param endInclusive
     *            include the end
     * @return
     */
    public MIDITrack sublist(double after, double before, boolean clone,
            boolean endInclusive) {
        MIDITrack list = new MIDITrack();
        for (MIDINote n : this) {
            double s = n.getStartBeat();
            if (endInclusive == false) {
                if (s >= after && s < before) {
                    if (clone) {
                        list.add((MIDINote) n.duplicate());
                    } else {
                        list.add(n);
                    }
                }
            } else {
                if (s >= after && s <= before) {
                    if (clone) {
                        list.add((MIDINote) n.duplicate());
                    } else {
                        list.add(n);
                    }
                }
            }
        }
        return list;
    }

    /**
     * MIDINotes are not cloned.
     * 
     * @param after
     *            a stat beat
     * @param before
     *            an end beat
     * @return a MIDITrack built from the sublist
     */
    public MIDITrack sublist(double after, double before) {
        return sublist(after, before, false);
    }

    /**
     * Create a sublist of the original MIDITrack beginning at the specified
     * index and continuing to the end of the list.
     * 
     * @param index
     *            The index to begin copying from.
     * @return a MIDITrack built from the sublist
     */
    public MIDITrack sublist(int index) {
        List<MIDINote> sl = notes.subList(index, notes.size());
        MIDITrack list = new MIDITrack(sl);
        return list;
    }

    /**
     * <code>sublist</code> is sort of a band pass filter. Returns a MIDITrack
     * with MIDINotes in the given range. No modifications to start times or any
     * other parameters.
     * 
     * @param low
     *            a <code>Pitch</code> value
     * @param high
     *            a <code>Pitch</code> value
     * @return a <code>MIDITrack</code> instance
     */
    public MIDITrack sublist(Pitch low, Pitch high) {
        MIDITrack list = new MIDITrack();
        for (MIDINote n : this) {
            int num = n.getMidiNumber();
            if (num >= low.getMidiNumber() && num <= high.getMidiNumber()) {
                list.add(n);
            }
        }
        // for (Iterator it = this.iterator(); it.hasNext();) {
        // MIDINote n = (MIDINote) it.next();
        // int num = n.getMidiNumber();
        // if (num >= low.getMidiNumber() && num <= high.getMidiNumber()) {
        // list.add(n);
        // }
        // }
        return list;
    }

    /**
     * <code>getInversion</code> creates a MIDITrack from this track's inverted
     * intervals.
     * 
     * @return a <code>MIDITrack</code> instance
     */
    public MIDITrack getInversion() {
        int[] intervals = getPitchIntervals();
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] *= -1;
        }
        int base = notes.get(0).getMidiNumber();

        logger.debug("base ", base);
        // ArrayUtils.printArray(intervals, logger);

        return MIDITrackFactory.createFromIntervals(intervals, base, 1,
                false);
    }

    /**
     * Creates a new List of {@code List<MIDINote>}s organized by the
     * MIDItrack's note's channels. The original MIDItrack is unchanged.
     * 
     * @return A List of a List of MIDINotes organized by channel
     */
    public List<List<MIDINote>> channelize() {
        List<List<MIDINote>> channelList = new ArrayList<List<MIDINote>>(16);
        for (int i = 0; i < 16; i++) {
            channelList.add(i, new ArrayList<MIDINote>());
        }
        for (MIDINote n : notes) {
            int channel = n.getChannel();
            List<MIDINote> list = channelList.get(channel);
            list.add(n);
        }
        // remove all the empty ones
        List<List<MIDINote>> channels = new ArrayList<List<MIDINote>>();
        for (List<MIDINote> list : channelList) {
            if (list.isEmpty() == false) {
                channels.add(list);
            }
        }
        return channels;
    }

    /**
     * <code>setStartBeat</code> sets the start beat of the entire track. This
     * is useful in a GUI where you are moving track representations around. The
     * track needs to be sequential. If the start beats are not in ascending
     * order, it won't work. e.g. When you call retrograde(), the start beats
     * are not in ascending order.
     * 
     * @param b
     *            a <code>double</code> value
     */
    public void setStartBeat(double b) {
        double now = getStartBeat();
        double diff = now - b;

        if (b < 0) {
            logger.error("setstrt < 0");
            throw new IllegalArgumentException("Start beat < 0");
        }

        // TODO use the start beat modifier
        for (MIDINote n : notes) {
            double s = n.getStartBeat();
            double newStart = s - diff;
            logger.debug("old start {} new start {} diff {}", s, newStart, diff);
            n.setStartBeat(newStart);
            // n.setStartBeat(s - newStart);
        }
        this.changes.firePropertyChange(MODIFIED, null, this);

        // StartBeatModifier m = new
        // StartBeatModifier(Modifier.Operation.SUBTRACT);
        // m.setValues(new double[]{diff});
        // this.map(m);
    }

    /**
     * <p>
     * Change the start beat of note at the specified index.
     * </p>
     * 
     * @param selectedIndex
     *            the index
     * @param i
     *            the new start beat
     */
    public void changeStartBeatOfNoteAtIndex(int selectedIndex, double i) {
        MIDINote n = get(selectedIndex);
        n.setStartBeat(i);
        this.changes.firePropertyChange(MODIFIED, null, this);

    }

    /**
     * <p>
     * Change the duration of note at the specified index.
     * </p>
     * 
     * @param selectedIndex
     *            the index
     * @param i
     *            the new duration
     */
    public void changeDurationOfNoteAtIndex(int selectedIndex, double i) {
        MIDINote n = get(selectedIndex);
        n.setDuration(i);
        this.changes.firePropertyChange(MODIFIED, null, this);

    }

    /**
     * <code>getNoteDurations</code> creates and returns an array of the
     * durations of all the {@code MIDINote}s in this track.
     * 
     * @return a <code>double[]</code> value
     */
    public double[] getNoteDurations() {
        int len = notes.size();
        double[] durations = new double[len];
        for (int i = 0; i < len; i++) {
            MIDINote note = notes.get(i);
            durations[i] = note.getDuration();
        }
        return durations;
    }

    /**
     * <p>
     * change the end beat of the note at this index.
     * </p>
     * 
     * @param selectedIndex
     *            the index.
     * @param d
     *            the end beat
     */
    public void changeEndBeatOfNoteAtIndex(int selectedIndex, double d) {
        MIDINote n = get(selectedIndex);
        n.setEndBeat(d);
        this.changes.firePropertyChange(MODIFIED, null, this);

    }

    /**
     * Get the MIDINote at the specified index and set its Pitch to the
     * specified pitch.
     * <p>
     * Fires a MIDItrack.PITCH_CHANGE event.
     * 
     * @param selectedIndex
     *            The index of the MIDINote to be modified.
     * @param num
     *            The MIDI number of the pitch to set
     */
    public void changePitchOfNoteAtIndex(final int selectedIndex, final int num) {
        MIDINote n = get(selectedIndex);
        n.setMidiNumber(num);
        this.changes.firePropertyChange(MODIFIED, null, this);

    }

    /**
     * Returns the {@code Map} of tempi in this {@code MIDITrack}.
     * 
     * Does not create a defensive copy.
     * 
     * @return the tempoMap
     */
    public NavigableMap<Double, Integer> getTempoMap() {
        return tempoMap;
    }

    /**
     * Does not create a defensive copy.
     * 
     * @return the keySignatureMap
     */
    public NavigableMap<Double, KeySignature> getKeySignatures() {
        return keySignatureMap;
    }

    /**
     * Returns the {@code Map} of time signatures in this {@code MIDITrack}.
     * Does not create a defensive copy. Wait until Java 8.
     * 
     * @return the timeSignatures
     */
    public NavigableMap<Double, TimeSignature> getTimeSignatures() {
        // return Collections.unmodifiableMap(this.timeSignatures);
        return timeSignatures;
    }

    /**
     * @param tempoMap
     *            the tempi to set
     */
    public final void setTempoMap(
            NavigableMap<Double, Integer> tempoMap) {
        this.tempoMap = tempoMap;
        this.changes.firePropertyChange(MODIFIED, null, this);

    }

    /**
     * @param timeSignatures
     *            the timeSignatures to set
     */
    public void setTimeSignatures(
            NavigableMap<Double, TimeSignature> timeSignatures) {
        this.timeSignatures = timeSignatures;
        this.changes.firePropertyChange(MODIFIED, null, this);

    }

    /**
     * This does no checking on whether the designated beat actually makes
     * sense.
     * 
     * @param beat
     *            the beat where this should occur.
     * @param tempo
     *            the tempo in BPM
     */
    public void addTempoAtBeat(double beat, Integer tempo) {
        tempoMap.put(beat, tempo);
        this.changes.firePropertyChange(ADD, null, this);

    }

    /**
     * This does no checking on whether the designated beat actually makes
     * sense.
     * 
     * @param beat
     *            the beat where this should occur.
     * @param ts
     *            the time signature
     */
    public final void addTimeSignatureAtBeat(double beat, TimeSignature ts) {
        timeSignatures.put(beat, ts);
        this.changes.firePropertyChange(ADD, null, this);

    }

    /**
     * Add a time signature to the track.
     * 
     * @param beat
     *            the beat to insert the time signature
     * @param numerator
     *            the time signature numerator
     * @param denominator
     *            the time signature denominator
     */
    public void addTimeSignatureAtBeat(double beat, int numerator,
            int denominator) {
        timeSignatures
                .put(beat, new TimeSignature(numerator, denominator));
        this.changes.firePropertyChange(ADD, null, this);

    }

    /**
     * Get the {@code TimeSignature} at the specified beat. Returns null if
     * there is no time signature at that beat.
     * 
     * @param beat
     *            the beat
     * @return the {@code TimeSignature} or null
     */
    public TimeSignature getTimeSignatureAtBeat(double beat) {
        TimeSignature ts = null;
        Double time = timeSignatures.floorKey(beat);
        if (time != null) {
            ts = timeSignatures.get(time);
        }
        return ts;
    }

    /**
     * Insert a key signature into this track.
     * 
     * @param beat
     *            the beat to insert the key signature
     * @param keysig
     *            the keysignature
     */
    public void addKeySignatureAtBeat(double beat, KeySignature keysig) {
        keySignatureMap.put(beat, keysig);
        this.changes.firePropertyChange(ADD, null, this);

    }

    /**
     * Get the tempo message at the specified beat. Returns null if there is no
     * message at that beat.
     * 
     * @param beat
     *            the beat "key" in the tempo map.
     * @return the tempo or null
     */
    public Integer getTempoAtBeat(double beat) {
        Integer tempo = null;
        Double key = tempoMap.floorKey(beat);
        if (key != null) {
            tempo = tempoMap.get(key);
        }
        return tempo;
    }

    /**
     * Get the key signature at the specified beat. Returns null if there is no
     * key signature at that beat.
     * 
     * @param beat
     *            the beat
     * @return the key signature or null
     */
    public KeySignature getKeySignatureAtBeat(double beat) {
        KeySignature ks = null;
        Double key = keySignatureMap.floorKey(beat);
        if (key != null) {
            ks = keySignatureMap.get(key);
        }
        return ks;
    }

    /**
     * Create a {@code List} of all the {@code MIDINote}'s pitch classes.
     * 
     * @return a List of the pitch classes
     */
    public List<Integer> getPitchClasses() {
        List<Integer> pitchClasses = new ArrayList<Integer>();
        for (MIDINote n : this) {
            int pc = n.getPitch().getMidiNumber() % 12;
            pitchClasses.add(pc);
        }
        return pitchClasses;
    }

    /**
     * Create a {@code List} of all the {@code MIDINote}'s MIDI pitch numbers.
     * 
     * @return a List of the pitch's MIDI numbers
     */
    public List<Integer> getPitchesAsIntegers() {
        List<Integer> pitches = new ArrayList<Integer>();
        for (MIDINote n : this) {
            int pc = n.getPitch().getMidiNumber();
            pitches.add(pc);
        }
        return pitches;
    }

    /**
     * Create a {@code List} of all the {@code MIDINote}'s {@code Pitch}es.
     * 
     * @return a list as Pitches
     */
    public List<Pitch> getPitches() {
        List<Pitch> pitches = new ArrayList<Pitch>();
        for (MIDINote n : this) {
            Pitch pc = n.getPitch();
            pitches.add(pc);
        }
        return pitches;
    }

    /**
     * Create and return a {@code Set} of all the {@code MIDINote}'s pitch
     * classes.
     * 
     * @return a Set of the pitch classes.
     */
    public Set<Integer> getPitchClassSet() {
        Set<Integer> pitchClasses = new TreeSet<Integer>();
        for (MIDINote n : this) {
            int pc = n.getPitch().getMidiNumber() % 12;
            pitchClasses.add(pc);
        }
        return pitchClasses;
    }

    /**
     * Modifies all MIDINotes in the Track to use this patch. Use Instrument
     * instead.
     * 
     * @deprecated
     * @param program
     *            The instrument number.
     */
    public void useInstrument(int program) {
        InstrumentModifier mod = new InstrumentModifier(program);
        this.map(mod);
    }

    /**
     * Retrieve the {@code Instrument} used to play this track.
     * 
     * @return the {@code Instrument}
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * This will change the patch on existing and future MIDINotes in this
     * track.
     * 
     * @param instrument
     *            the {@code Instrument} to set
     */
    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
        if (this.notes.size() > 0) {
            this.useInstrument(instrument);
        }
        this.changes.firePropertyChange(MODIFIED, null, this);

    }

    /**
     * Changes the patch on all {@code MIDINote}s in this track.
     * 
     * @param instrument
     *            the {@code Instrument}
     */
    public void useInstrument(Instrument instrument) {
        this.instrument = instrument;
        InstrumentModifier mod = new InstrumentModifier(instrument.getPatch()
                .getProgram());
        this.map(mod);
    }

    /**
     * Modifies all {@code MIDINote}s in the Track to use this patch. Use
     * Instrument instead
     * 
     * @deprecated
     * @param bank
     *            The instrument bank.
     * @param program
     *            The instrument number.
     */
    public void useInstrument(int bank, int program) {
        InstrumentModifier mod = new InstrumentModifier(bank, program);
        this.map(mod);
    }

    /**
     * Create a {@code MIDITrack} from the noteString and append it to this
     * track.
     * 
     * @param noteString
     *            a MIDIString
     * @return this to cascade calls
     */
    public MIDITrack append(String noteString) {
        MIDITrack tmp = new MIDITrack(noteString);
        tmp.useInstrument(this.instrument);
        this.append(tmp);
        return this;
        // returning tmp might be more useful
        // return tmp;
    }

    /**
     * This doesn't change the startBeats.
     * 
     * @param noteString
     *            a MIDIString
     */
    public void insertMIDIString(String noteString) {
        // calls track add or append
        midiStringParser.parseString(this, noteString);
    }

    /**
     * Change the channel of every {@code MIDINote} in this track.
     * 
     * @param channel
     *            the new MIDI channel
     * @see ChannelModifier
     */
    public void setChannel(int channel) {
        ChannelModifier mod = new ChannelModifier(
                Modifier.Operation.SET, channel);
        this.map(mod);
    }

    /**
     * Change the velocity of every {@code MIDINote} in this track.
     * 
     * @param velocity
     *            the new MIDI velocity
     */
    public void setVelocity(int velocity) {
        VelocityModifier mod = new VelocityModifier(
                Modifier.Operation.SET, velocity);
        this.map(mod);
    }

    /**
     * Adds a meta text message at the specified beat.
     * 
     * @param beat
     *            the beat where the text is inserted
     * @param text
     *            the text to insert
     */
    public void addMetaText(double beat, String text) {
        // ignored, since we're setting the beat afterward
        logger.debug("adding meta text '{}'", text);
        long tick = 0;
        // TODO add these factories to MIDIEvent
        MidiEvent event = MIDIUtils.createMetaTextMessage(
                tick,
                MIDIUtils.META_TEXT,
                text);
        MIDIEvent e = new MIDIEvent(event, this);
        // we're used to thinking in beats starting at 1 not zero.
        e.setStartBeat(beat - 1d);
        this.events.add(e);
        this.changes.firePropertyChange(MODIFIED, null, this);

    }

    /**
     * The opposite of sequential. Sets all the start beats to 1.
     */
    public void chordify() {
        StartBeatModifier mod = new StartBeatModifier(Modifier.Operation.SET,
                1d);
        this.map(mod);
    }

    /**
     * Sets all the start beats to the specified beat.
     * 
     * @param beat
     *            the new start beat
     */
    public void chordify(double beat) {
        StartBeatModifier mod = new StartBeatModifier(Modifier.Operation.SET,
                beat);
        this.map(mod);
    }

    /**
     * Get the {@code MIDINote}s at the specified beat.
     * 
     * @param beat
     *            the beat
     * @return a {@code List} of {@code MIDINote}s at this beat
     */
    // public List<MIDINote> getNotesAtBeat(final double beat) {
    // List<MIDINote> notes = new ArrayList<MIDINote>();
    // logger.debug("looking for  sb {}", beat);
    // for (final MIDINote n : this) {
    // final double s = n.getStartBeat();
    // final double e = n.getEndBeat();
    // logger.debug("checking sb {} of note {}", s, n);
    // Range<Double> range = Range.between(s, e - .0001);
    // logger.debug("range {}", range);
    // if (range.contains(beat)) {
    // logger.debug("adding note {}", n);
    // notes.add(n);
    // }
    // }
    // return notes;
    // }

    public List<MIDINote> getNotesAtBeat(final double beat) {
        return getNotesBetweenStartBeatAndEndBeat(beat, beat + 1d);
    }

    /**
     * Return the notes that have a start beat between the specified beats.
     * 
     * @param startBeat
     *            begin of beat range
     * @param endBeat
     *            not inclusive
     * @return a {@code List} of matching {@code MIDINote}s
     */
    public List<MIDINote> getNotesBetweenStartBeatAndEndBeat(
            final double startBeat, final double endBeat) {
        List<MIDINote> notes = new ArrayList<MIDINote>();
        Range<Double> range = Range.between(startBeat, endBeat - .00001);
        logger.debug("range {}", range);
        logger.debug("looking for  sb {}", startBeat);
        for (final MIDINote n : this) {
            final double s = n.getStartBeat();
            logger.debug("checking sb {} of note {}", s, n);
            if (range.contains(s)) {
                logger.debug("adding note {}", n);
                notes.add(n);
            }
        }
        return notes;
    }

    /**
     * Change the duration of the entire track. Changes each {@code MIDINote}'s
     * duration and start beat.
     * 
     * @param duration
     *            the new duration
     */
    public void setDuration(double duration) {
        double now = this.getStartBeat();
        logger.debug("new duration {}", duration);
        double d = this.getDuration();
        logger.debug("current duration {}", d);
        double factor = duration / d;
        logger.debug("factor {}", factor);
        this
                .map(new StartBeatModifier(Operation.MULTIPLY,
                        factor));
        this.map(new DurationModifier(Operation.MULTIPLY, factor));
        logger.debug("final duration {}", this.getDuration());

        this.setStartBeat(now);

        this.changes.firePropertyChange(MODIFIED, null,
                duration);
    }

    /**
     * Set the {@code MIDINote}s in this track to use the start beats and
     * durations in the provided {@code TimeSeries}.
     * 
     * @param timeSeries
     *            a {@code TimeSeries}
     */
    public void apply(final TimeSeries timeSeries) {
        final double nsize = this.size();
        final double tsize = timeSeries.getSize();
        final int n = (int) Math.round(nsize / tsize + .5);
        final TimeSeries ts = timeSeries.nCopies(n);
        ts.sequential();
        for (final MIDINote note : this) {
            final Timed te = ts.nextTimeEvent();
            note.setStartBeat(te.getStartBeat());
            note.setDuration(te.getDuration());
        }
    }

    /**
     * Serialization method.
     * 
     * @param out
     *            and output stream
     * @throws IOException
     *             if this cannot be serialized
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * Serializaiton method.
     * 
     * @param in
     *            the input stream
     * @throws IOException
     *             if something went wrong
     * @throws ClassNotFoundException
     *             if the class is not found
     */
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        // our "pseudo-constructor"
        in.defaultReadObject();
        // now we are a "live" object again
        this.changes = new PropertyChangeSupport(this);
        this.midiStringParser = new MIDIStringParser();
    }

    // JavaBeans event methods.

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.changes.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(final String propertyName,
            final PropertyChangeListener listener) {
        this.changes.addPropertyChangeListener(propertyName,
                listener);
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
}
