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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.collections.CircularList;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.midi.parse.MIDIStringParser;

import static com.rockhoppertech.music.Pitch.*;

import static com.rockhoppertech.music.midi.gm.MIDIGMPatch.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDITrackFactory {
    private static final Logger logger = LoggerFactory
            .getLogger(MIDITrackFactory.class);

    /**
     * You would usually use the ScoreFactory scoreToSequence() method to get a
     * Sequence. This might be useful for testing just one MIDITrack.
     * 
     * @param mt
     *            - a MIDITrack which already has notes and events.
     * @param resolution
     * @return
     */
    public static Sequence trackToSequence(MIDITrack mt, int resolution) {
        Sequence sequence = null;
        try {
            sequence = new Sequence(Sequence.PPQ, resolution);
            Track track = trackFromMIDITrack(mt, sequence);

            // MIDIUtils.insertTempo(track, 0, 128);
            if (mt.getName() != null) {
                MIDIUtils.insertSequenceName(track, 0, mt.getName());
            }

        } catch (InvalidMidiDataException e) {
            logger.error(e.getMessage(), e);
        }
        return sequence;
    }

    /**
     * <code>trackFromMIDITrack</code> creates a new JavaSound Track that
     * belongs to the supplied Sequence. The MIDITrack is iterated and
     * <code>MidiEvent</code> objects (patch, note on/off, eotrack) are created
     * and placed into the Track.
     * 
     * MIDIUtils is used to insert events into the Track.
     * 
     * Both the MIDITrack's MIDIEvents and MIDINotes are iterated.
     * 
     * Beats are 1 based, but ticks are zero based. This does the conversion.
     * 
     * Each MIDINote's bank and program are used. There will be a bank select
     * control message and a program change message if the current values are
     * different from the previous values (i.e. each MIDINote has a bank and
     * program. The first MIDINote will send the change messages. Those messages
     * are sent from succeeding MIDINotes only if the values are different.)
     * 
     * Called from the ScoreFactory method scoreToSequence().
     * 
     * The new Track is returned, but it is already part of the Sequence, so
     * that's just for debugging or some other use.
     * 
     * @param sequence
     *            a JavaSound <code>Sequence</code> instance
     * 
     * @return a JavaSound <code>Track</code> instance, which is already part of
     *         the Sequence
     * 
     * @see ScoreFactory#scoreToSequence(Score)
     * @see MIDIUtils
     */
    public static Track trackFromMIDITrack(MIDITrack mt, Sequence sequence) {
        logger.debug("Enter trackFromMIDITrack");

        if (sequence.getDivisionType() == Sequence.PPQ) {
            logger.debug("ppq: ");
        }

        logger.debug("sequence resolution: " + sequence.getResolution());
        int resolution = sequence.getResolution();

        Track track = sequence.createTrack();
        if (mt.getName() != null) {
            MIDIUtils.insertSequenceName(track, 0, mt.getName());
        }

        if (mt.getDescription() != null) {
            MIDIUtils.insertText(track, 0, mt.getDescription());
        }

        NavigableMap<Double, KeySignature> keys = mt.getKeySignatures();
        for (Double time : keys.keySet()) {
            KeySignature key = keys.get(time);
            long tick = (long) ((time - 1) * resolution);
            MIDIUtils.insertKeySignature(track, tick, key.getSf(), key.getMm());
        }

        NavigableMap<Double, TimeSignature> timeSigs = mt.getTimeSignatures();
        for (Double time : timeSigs.keySet()) {
            TimeSignature ts = timeSigs.get(time);
            long tick = (long) ((time - 1) * resolution);
            MIDIUtils.insertTimeSignature(
                    track,
                    tick,
                    ts.getNumerator(),
                    ts.getDenominator());
        }

        NavigableMap<Double, Integer> tempoMap = mt.getTempoMap();
        for (Double time : tempoMap.keySet()) {
            Integer tempo = tempoMap.get(time);
            long tick = (long) ((time - 1) * resolution);
            MIDIUtils.insertTempo(track, tick, tempo);
        }

        /*
         * in the Java Sound API, the ticks aren't delta values; they're the
         * previous event's time value plus the delta value. In other words, in
         * the Java Sound API the timing value for each event is always greater
         * than that of the previous event in the sequence (or equal, if the
         * events are supposed to be simultaneous). Each event's timing value
         * measures the time elapsed since the beginning of the sequence.
         */
        for (MIDIEvent event : mt.getEvents()) {
            // events are based on beats in this way
            MidiEvent sun = event.toMidiEvent(resolution);
            // MidiMessage sunmess = sun.getMessage();
            track.add(sun);
        }

        int bank = -1;
        int program = -1;
        long tick = -1;
        for (MIDINote n : mt) {

            ShortMessage se = null;
            try {
                int p = n.getProgram();
                int b = n.getBank();
                // do the bank/program change only if the value is different
                // from the last note
                if (p != program || b != bank) {

                    logger.debug(
                            "old new program {} {} bank {} {} for note {}",
                            program,
                            p,
                            bank,
                            b,
                            n);

                    program = p;
                    bank = b;

                    se = new ShortMessage();
                    se.setMessage(
                            ShortMessage.CONTROL_CHANGE + n.getChannel(),
                            n.getChannel(),
                            0, // bank select
                            n.getBank());
                    // event ticks are 0 based but Notes are 1 based.
                    // Musicicans don't say "start at the zeroth beat"
                    tick = (long) ((n.getStartBeat() - 1) * resolution);
                    MidiEvent event = new MidiEvent(se, tick);
                    track.add(event);

                    se = new ShortMessage();
                    se.setMessage(
                            ShortMessage.PROGRAM_CHANGE,
                            n.getChannel(),
                            n.getProgram(),
                            n.getBank());
                    tick = (long) ((n.getStartBeat() - 1) * resolution);
                    event = new MidiEvent(se, tick);
                    track.add(event);

                }

                // the on message
                se = new ShortMessage();
                se.setMessage(ShortMessage.NOTE_ON + n.getChannel(), n
                        .getPitch().getMidiNumber(), n.getVelocity());
                // event ticks are 0 based but Notes are 1 based.
                // Musicicans don't say "start at the zeroth beat"
                tick = (long) ((n.getStartBeat() - 1) * resolution);
                MidiEvent event = new MidiEvent(se, tick);
                // this is zero based: (long) (n.getStartBeat() * division));
                track.add(event);
                logger.info(String.format(
                        "sb %f tick %d %s",
                        n.getStartBeat(),
                        tick,
                        MIDIUtils.toString(se)));

                // now the off message
                se = new ShortMessage();
                se.setMessage(ShortMessage.NOTE_ON + n.getChannel(), n
                        .getPitch().getMidiNumber(), 0); // velocity of 0
                // means off
                tick = (long) ((n.getStartBeat() - 1 + n.getDuration()) * resolution);
                logger.info(String.format(
                        "sb %f tick %d %s",
                        n.getStartBeat(),
                        tick,
                        MIDIUtils.toString(se)));

                event = new MidiEvent(se, tick);
                track.add(event);
            } catch (InvalidMidiDataException e) {
                logger.error(e.getLocalizedMessage(), e);
                MIDIUtils.print(se);
            }
        }

        // logger.debug("Adding end of track message");
        // try {
        // MetaMessage me = new MetaMessage();
        // me.setMessage(0x2F, new byte[] { 0 }, 1);
        // MidiEvent event = new MidiEvent(me, track.ticks());
        // track.add(event);
        // } catch (InvalidMidiDataException e) {
        // System.err.println(e);
        // }
        logger.debug("leaving trackFromMIDITrack");
        return track;
    }

    /**
     * @param track
     * @param resolution
     * @return a channelized Score
     */
    public Score createChannelizedMIDITracks(Track track, int resolution) {
        List<List<MidiEvent>> channelList = new ArrayList<List<MidiEvent>>(16);
        for (int i = 0; i < 16; i++) {
            channelList.add(i, new ArrayList<MidiEvent>());
        }
        trackToChannelList(channelList, track);
        trimChannelList(channelList);
        logChannelList(channelList);

        Score score = new Score();
        score.setResolution(resolution);
        // go through each channel create a track for each channel
        for (List<MidiEvent> list : channelList) {
            MIDITrack mtrack = eventsToMIDITrack(list, resolution);
            score.add(mtrack);
        }
        return score;
    }

    /**
     * Add a new MIDITrack to the Score for each channel in the JavaSound Track.
     * 
     * @param score
     * @param track
     * @return a Score
     */
    public Score createChannelizedMIDITracks(Score score, Track track) {
        List<List<MidiEvent>> channelList = new ArrayList<List<MidiEvent>>(16);
        for (int i = 0; i < 16; i++) {
            channelList.add(i, new ArrayList<MidiEvent>());
        }
        trackToChannelList(channelList, track);
        trimChannelList(channelList);
        logChannelList(channelList);

        int resolution = score.getResolution();
        // go through each channel create a track for each channel
        for (List<MidiEvent> list : channelList) {
            MIDITrack mtrack = eventsToMIDITrack(list, resolution);
            score.add(mtrack);
        }
        return score;
    }

    /**
     * for debugging. Simply dumps the info to the logger.
     * 
     * @param channelList
     *            a List of a List of MidiEvents
     */
    private static void logChannelList(final List<List<MidiEvent>> channelList) {

        logger.debug("Dumping channel list");

        for (List<MidiEvent> elist : channelList) {
            for (MidiEvent me : elist) {
                logger.debug(MIDIUtils.toString(me));
            }
        }
        /*
         * for (Iterator i = channelList.iterator(); i.hasNext();) {
         * List<MidiEvent> elist = (List<MidiEvent>) i.next();
         * logger.debug("Channel list"); if (elist != null && elist.isEmpty() ==
         * false) { int elsize = elist.size(); for (int j = 0; j < elsize; j++)
         * { MidiEvent se = (MidiEvent) elist.get(j);
         * logger.debug(MidiUtils.toString(se)); } } }
         */

    }

    /**
     * <code>trackToChannelList</code> puts all <code>ShortMessages</code> into
     * individual lists by channel.
     * 
     * @param channelList
     *            a <code>List</code> containing the channel lists. The results
     *            are put in this list of lists.
     * @param t
     *            a JavaSound <code>Track</code>
     */
    private static void trackToChannelList(List<List<MidiEvent>> channelList,
            Track t) {
        int ts = t.size();
        for (int i = 0; i < ts; i++) {
            MidiEvent me = t.get(i);
            MidiMessage mm = me.getMessage();
            if (mm instanceof ShortMessage) {
                ShortMessage se = (ShortMessage) mm;
                int chan = se.getChannel();
                List<MidiEvent> elist = null;
                if ((elist = channelList.get(chan)) == null) {
                    // this shouldn't happen
                    logger.debug("null list, creating new for " + chan);
                    elist = new ArrayList<MidiEvent>();
                    channelList.add(chan, elist);
                }
                logger.debug("adding " + MIDIUtils.toString(me));
                elist.add(me);
            }
        }
    }

    /**
     * <code>trimChannelList</code> removes the empty lists
     * 
     * @param channelList
     *            a <code>List</code> value
     */
    private static void trimChannelList(List<List<MidiEvent>> channelList) {

        for (Iterator<List<MidiEvent>> iterator = channelList.iterator(); iterator
                .hasNext();) {
            List<MidiEvent> list = iterator.next();
            if (list.isEmpty() == true) {
                iterator.remove();
            }
        }
        // remove as above. otherwise you get a concurrent modification problem

        // synchronized (channelList) {
        // for (List<MidiEvent> list : channelList) {
        // if (list.isEmpty() == true) {
        // channelList.remove(list);
        // }
        // }
        // }
    }

    /**
     * <code>trackToMIDITrack</code> turns a collection of MidiEvents containing
     * ShortMessages only (e.g. no sysex) into a MIDITrack. The Note Messages
     * are placed in notes as <code>MIDINotes</code>, other messages wrapped and
     * are placed in a separate list - the instance variable <code>events</code>
     * 
     * <p>
     * You might get the division like this: double div = (double)
     * sequence.getResolution();
     * </p>
     * 
     * @param t
     *            a {@code Track} to be converted
     * @param resolution
     *            a <code>int</code> sequence PPQ resolution aka division
     * @return a MIDITrack
     */
    public static MIDITrack trackToMIDITrack(Track t, int resolution) {
        List<MidiEvent> events = new ArrayList<>();

        int ts = t.size();
        for (int i = 0; i < ts; i++) {
            MidiEvent me = t.get(i);
            // MidiMessage mm = me.getMessage();
            // if (mm instanceof ShortMessage) {
            events.add(me);
            logger.debug("adding " + MIDIUtils.toString(me));
            // }
        }
        // List<MetaMessage> textevents = MIDIUtils.getMetaTextEvents(t);
        // for(MetaMessage me: textevents) {
        // MIDIUtils.print(me);
        // }

        MIDITrack track = eventsToMIDITrack(events, resolution);
        return track;
    }

    private static MIDITrack eventsToMIDITrack(List<MidiEvent> events,
            int resolution) {
        MIDITrack track = new MIDITrack();
        double div = resolution;
        int program = 0;
        // int channel = 0;
        // need to match note ons with note offs.
        Map<Integer, MidiEvent> outstanding = new HashMap<Integer, MidiEvent>();

        for (MidiEvent me : events) {

            if (me.getMessage() instanceof MetaMessage) {
                MetaMessage mm = (MetaMessage) me.getMessage();
                if (mm.getType() == MIDIUtils.META_TEXT) {
                    logger.debug("meta text");
                    logger.debug(MIDIUtils.bytesToText(mm.getData()));
                    // logger.debug(new String(mm.getData()));
                }
                if (mm.getType() == MIDIUtils.META_TIME_SIG) {
                    logger.debug("time sig");
                    logger.debug(MIDIUtils.getTimeSignature(mm));
                }
                if (mm.getType() == MIDIUtils.META_COPYRIGHT) {
                    logger.debug("copyright");
                    logger.debug(MIDIUtils.bytesToText(mm.getData()));
                }
                if (mm.getType() == MIDIUtils.META_KEY_SIG) {
                    logger.debug("Key signature");
                    logger.debug(MIDIUtils.getTimeSignature(mm));

                }
                if (mm.getType() == MIDIUtils.META_TEMPO) {

                }
                // In a format 1 midifile, track 0 will contain the sequence
                // name.
                // in Sibelius, this would be the title
                // Track 1 will be the instrument name, e.g. Piano
                if (mm.getType() == MIDIUtils.META_NAME) {
                    logger.debug("name");
                    logger.debug(MIDIUtils.bytesToText(mm.getData()));
                    track.setName(MIDIUtils.bytesToText(mm.getData()));
                }
            }

            if (me.getMessage() instanceof ShortMessage) {

                ShortMessage se = (ShortMessage) me.getMessage();

                // ShortMessage se = (ShortMessage) i.next();
                int command = se.getCommand();
                int chan = se.getChannel();
                int d1 = se.getData1();
                int d2 = se.getData2();
                long tick = me.getTick();

                logger.debug("Command:" + Integer.toHexString(command));
                logger.debug(MIDIUtils.toString(se));
                if (command == ShortMessage.CONTROL_CHANGE) {
                    logger.debug("control change ", se);
                    track.add(new MIDIEvent(me, track));
                }
                if (command == ShortMessage.CHANNEL_PRESSURE) {
                    logger.debug("channel pressure ", se);
                    track.add(new MIDIEvent(me, track));
                }
                if (command == ShortMessage.POLY_PRESSURE) {
                    logger.debug("poly pressure ", se);
                    track.add(new MIDIEvent(me, track));
                }

                if (command == ShortMessage.PROGRAM_CHANGE) {
                    logger.debug("patch:", program);
                    program = d1;
                    track.add(new MIDIEvent(me, track));
                }
                if (command == ShortMessage.PITCH_BEND) {
                    logger.debug("pitch bend: {} d2 {}", d1, d2);
                    track.add(new MIDIEvent(me, track));
                }

                if (command == ShortMessage.NOTE_ON && d2 != 0) {
                    logger.debug("Note on {} tick {}", d1, tick);
                    outstanding.put(Integer.valueOf(d1), me);
                }

                if ((command == ShortMessage.NOTE_ON && d2 == 0)
                        || command == ShortMessage.NOTE_OFF) {
                    logger.debug("Note off {} tick {}", d1, tick);
                    // find the event with the same d1
                    if (outstanding.isEmpty()) {
                        logger.error("no outstanding:");
                        continue;
                    }
                    me = outstanding.remove(Integer.valueOf(d1));
                    ShortMessage out = null;
                    if (me != null) {
                        out = (ShortMessage) me.getMessage();
                    }

                    if (out != null) {
                        long tickdelta = tick - me.getTick();
                        double duration = tickdelta / div;
                        double start = me.getTick();
                        if (start != 0d) {
                            start = start / div;
                        }

                        logger.debug("Note off tick delta:", tickdelta);
                        logger.debug("Note off start:", start);
                        logger.debug("Note off duration:", duration);

                        // add one to start since Events are 0 based but Notes
                        // are 1
                        // based.
                        MIDINote n = new MIDINote(d1, start + 1, duration,
                                chan, out.getData2(), program);
                        track.add(n);
                    } else {
                        logger.error("Note off no match:");
                    }
                } // end of note (noteoff or noteone+0vel)
            }

        }
        return track;
    }

    /**
     * <pre>
     * import static com.rockhoppertech.music.Pitch.*;
     * ...
     * MIDITrack nl = MIDITrackFactory.createFromPitches(C5, D3, EF4);
     * </pre>
     * 
     * @param pitches
     * @return
     */
    public static MIDITrack createFromPitches(int... pitches) {
        MIDITrack track = new MIDITrack();
        for (int pitche : pitches) {
            MIDINote note = new MIDINote(pitche);
            track.add(note);
        }
        return track;
    }

    // public static MIDITrack createFromPitches(Integer[] a) {
    // return null;
    // }

    public static MIDITrack createRepeated(final MIDITrack track,
            final CircularList<Integer> mask) {
        MIDITrack repeated = new MIDITrack();
        mask.reset();
        int maskValue = 0;
        for (final MIDINote note : track) {
            maskValue = mask.next();
            for (int i = 0; i < maskValue; i++) {
                repeated.add((MIDINote) note.duplicate());
            }
        }
        return repeated.sequential();
    }

    public static MIDITrack createRepeated(final MIDITrack track,
            Integer... maskInts) {
        CircularList<Integer> mask = new CircularArrayList<Integer>();
        for (Integer i : maskInts) {
            mask.add(i);
        }

        MIDITrack repeated = new MIDITrack();
        mask.reset();
        int maskValue = 0;
        for (final MIDINote note : track) {
            maskValue = mask.next();
            for (int i = 0; i < maskValue; i++) {
                repeated.add((MIDINote) note.duplicate());
            }
        }
        return repeated.sequential();
    }

    public static MIDITrack repeat(final MIDITrack track,
            final int numberOfRepeats) {
        MIDITrack repeated = new MIDITrack();

        for (int i = 0; i < numberOfRepeats; i++) {
            repeated.append(track);
        }
        return repeated.sequential();
    }

    static MIDIStringParser parser = new MIDIStringParser();

    public static MIDITrack createFromString(String s) {
        MIDITrack track = parser.parseString(s);
        return track;
    }

    public static MIDITrack createFromIntervalsChained(boolean mirrored,
            int... intervals) {
        MIDITrack track = createFromIntervals(intervals);
        List<Integer> ints = track.getPitchesAsIntegers();
        track.clear();
        for (Integer midiNumber : ints) {
            MIDITrack tmpTrack = MIDITrackFactory.createFromIntervals(
                    intervals, midiNumber, 1, false, 1);
            if (mirrored) {
                final MIDITrack inversion = tmpTrack.getInversion();
                inversion.remove(0);
                tmpTrack = inversion.append(tmpTrack);
                tmpTrack.sortByAscendingPitches();
            }
            track = track.append(tmpTrack);
        }
        return track;
    }

    public static MIDITrack createFromIntervals(int... intervals) {
        int baseInt = C5;
        return MIDITrackFactory.createFromIntervals(intervals, baseInt, 1,
                false);
    }

    public static MIDITrack createFromIntervals(Pitch basePitch,
            int... intervals) {
        int baseInt = basePitch.getMidiNumber();
        return MIDITrackFactory.createFromIntervals(intervals, baseInt, 1,
                false);
    }

    public static MIDITrack createFromIntervals(int[] intervals, int baseInt) {
        return MIDITrackFactory.createFromIntervals(intervals, baseInt, 1,
                false);
    }

    /**
     * Create a {@code MIDITrack} from intervals.
     * 
     * @param intervals
     *            an array of intervals
     * @param baseInt
     *            the pitch from which succeeding pitches are calculated
     * @param unit
     *            how many halfs steps
     * @param absolute
     *            are the intervals
     * @param numOctaves
     *            how many octaves
     * @return a {@code MIDITrack}
     */
    public static MIDITrack createFromIntervals(int[] intervals,
            int baseInt, int unit, boolean absolute, int numOctaves) {
        MIDITrack track = new MIDITrack();
        int toIndex = 0;
        int gap = -1;
        int fromIndex = 1;

        for (int i = 0; i < numOctaves; i++) {
            if (i == 0) {
                fromIndex = 0;
                gap = 0;
            } else {
                fromIndex = 1;
                gap = -1;
            }
            MIDITrack current = MIDITrackFactory.createFromIntervals(
                    intervals, baseInt, unit, absolute);
            current.sequential();
            toIndex = current.size();
            logger.debug("before append '{}''", current);

            track.append(current, gap, fromIndex, toIndex);
            baseInt += 12;
        }
        return track;
    }

    /**
     * Create a {@code MIDITrack} from intervals.
     * 
     * @param intervals
     *            a list of intervals
     * @param baseInt
     *            the pitch from which succeeding pitches are calculated
     * @param unit
     *            how many halfs steps
     * @param absolute
     *            are the intervals
     * @param numOctaves
     *            how many octaves
     * @return a {@code MIDITrack}
     */
    public static MIDITrack createFromIntervals(List<Integer> intervals,
            int baseInt, int unit, boolean absolute, int numOctaves) {
        int[] a = Ints.toArray(intervals);
        return createFromIntervals(a,
                baseInt, unit, absolute, numOctaves);
    }

    /**
     * <code>createFromIntervals</code> generates a MIDITrack from the provided
     * intervals. Only the pitches in the Notes are set.
     * 
     * 
     * @param intervals
     *            an <code>int[]</code> containing the intervals.
     * @param baseInt
     *            an <code>int</code> that will be the first Note pitch in the
     *            series.
     * @param unit
     *            an <code>int</code> that each interval is multiplied
     * @param absolute
     *            a <code>boolean</code> whether the intervals are in relation
     *            to the baseInt
     * @return a <code>MIDITrack</code>
     */
    public static MIDITrack createFromIntervals(int[] intervals,
            int baseInt, int unit, boolean absolute) {
        List<MIDINote> newCollection = new ArrayList<MIDINote>(
                intervals.length + 1);

        // this was set instead of add
        if (absolute) {
            newCollection.add(0, new MIDINote(baseInt));
            for (int i = 0; i < intervals.length; i++) {
                MIDINote newNote = new MIDINote(baseInt + intervals[i] * unit);
                newCollection.add(i + 1, newNote);
            }
        } else {
            newCollection.add(0, new MIDINote(baseInt));
            for (int i = 0; i < intervals.length; i++) {
                int num = newCollection.get(i).getMidiNumber();
                int pitch = num + (intervals[i] * unit);

                logger.debug("pitch ", pitch);

                newCollection.add(i + 1, new MIDINote(pitch));
            }
        }
        return new MIDITrack(newCollection);
    }

    // nah, these will be ambiguous.

    // public static MIDITrack createFromIntervals(int baseInt, int unit,
    // boolean absolute, int numOctaves, int... intervals) {
    // MIDITrack list = new MIDITrack();
    // int toIndex = 0;
    // int gap = -1;
    // int fromIndex = 1;
    //
    // for (int i = 0; i < numOctaves; i++) {
    // if (i == 0) {
    // fromIndex = 0;
    // gap = 0;
    // } else {
    // fromIndex = 1;
    // gap = -1;
    // }
    // MIDITrack current = MIDITrack.createFromIntervals(intervals,
    // baseInt,
    // unit,
    // absolute);
    // current.sequential();
    // toIndex = current.size();
    // if (logger.isDebugEnabled()) {
    // String s = String.format("before append '%s'",
    // current);
    // logger.debug(s);
    // }
    // list.append(current,
    // gap,
    // fromIndex,
    // toIndex);
    // baseInt += 12;
    // }
    // return list;
    // }
    //
    // public static MIDITrack createFromIntervals(int baseInt, int unit,
    // boolean absolute, int... intervals) {
    // int numOctaves = 1;
    // return createFromIntervals(baseInt,
    // unit,
    // absolute,
    // numOctaves,
    // intervals);
    // }
    //
    // public static MIDITrack createFromIntervals(int baseInt, int unit,
    // int... intervals) {
    // int numOctaves = 1;
    // boolean absolute = true;
    // return createFromIntervals(baseInt,
    // unit,
    // absolute,
    // numOctaves,
    // intervals);
    // }

    /**
     * Iteratively apply a pattern to an array of scale degrees. Actually any
     * interval pattern; you can use a Chord or a Scale for example.
     * 
     * If you want the degrees you can do this: <code>
      int[] degrees = Interval.intervalsToDegrees(chord.getIntervals(),
                numOcts);
       </code> or if it is a scale this
     * <code>scale.getDegrees()</code>
     * 
     * The pattern for the first Hanon exercise would be 0 2 3 4 5 4 3 2
     * 
     * @param degrees
     *            the degrees
     * @param pattern
     *            the pattern
     * @param limit
     *            - max index into degrees
     * @param startingMIDINumber
     *            starting MIDI pitch
     * @param nOctaves
     *            number of octaves
     * @param duration
     *            duration
     * @param reverse
     *            should reverse the pattern
     * @param restBetweenPatterns
     *            gap between patterns
     * @param upAndDown
     *            make it go up and down
     * @return a MIDITrack
     */

    public static MIDITrack getTrackPattern(final int[] degrees,
            final int[] pattern, final int limit, int startingMIDINumber,
            final int nOctaves, final double duration, final boolean reverse,
            final double restBetweenPatterns, final boolean upAndDown) {

        final MIDITrack track = new MIDITrack();
        MIDINote newnote = null;

        // int numOcts = (Pitch.MAX - startingMIDINumber) / 12 + 1;
        // logger.debug("num octs " + numOcts);

        // int[] degrees = Interval.intervalsToDegrees(scale.getIntervals(),
        // numOcts);

        logger.debug("limit {} noct {} reverse {}", limit,
                nOctaves, reverse);
        logger.debug("dlen {} plen {} ", degrees.length,
                pattern.length);

        final List<Integer> dlist = new ArrayList<Integer>();
        for (final Integer i : degrees) {
            dlist.add(i);
            logger.debug("adding degree to list ", i);
        }
        // if (upAndDown) {
        // List<Integer> degs = new ArrayList<Integer>();
        // degs.addAll(dlist);
        // Collections.reverse(dlist);
        // degs.addAll(dlist);
        // dlist = degs;
        //
        // }

        // 1.5 returns a List<int[]>
        // List<Integer> dlist = Arrays.asList(degrees);

        double startBeat = 1d;
        if (reverse) {
            for (int oct = 0; oct < nOctaves; oct++) {
                for (int i = 0; i < limit; i++) {
                    for (int j = pattern.length - 1; j >= 0; j--) {
                        // int n = startingMIDINumber + degrees[pattern[j] + i];

                        logger.debug("reverse i {}", i);
                        logger.debug("reverse j {}", j);
                        logger.debug("reverse pattern {}", pattern[j]);
                        logger.debug("reverse pattern length {}",
                                pattern.length);

                        final int d = (dlist.get(pattern[j] + i)).intValue();
                        int n = 0;
                        n = startingMIDINumber + d;
                        logger.debug("n {}", n);
                        // newnote = new MIDINote(n);
                        newnote = new MIDINote(n, startBeat, duration);
                        startBeat += duration;
                        track.add(newnote);
                        logger.debug("new note {}", newnote);
                    }
                    startBeat += restBetweenPatterns;
                    if (upAndDown) {
                        for (final int element : pattern) {
                            final int d = (dlist.get(element + i)).intValue();
                            newnote = new MIDINote(startingMIDINumber + d,
                                    startBeat, duration);
                            startBeat += duration;
                            track.add(newnote);
                        }
                        startBeat += restBetweenPatterns;
                    }
                }
                startingMIDINumber += 12;

            }
        } else {
            for (int oct = 0; oct < nOctaves; oct++) {
                for (int i = 0; i < limit; i++) {
                    for (int j = 0; j < pattern.length; j++) {

                        logger.debug(
                                "j {} < plen {}, i {} < limit {}, dlist size {}",
                                j,
                                pattern.length,
                                i,
                                limit,
                                dlist.size());

                        int index = pattern[j] + i;

                        logger.debug("index into dlist {}",
                                index);

                        if (index == dlist.size()) {

                            logger.debug("continue {} {}",
                                    index, dlist.size());

                            continue;
                        }

                        final int d = (dlist.get(index)).intValue();
                        int n = 0;
                        n = startingMIDINumber + d;
                        logger.debug("n {} d {}", n, d);
                        newnote = new MIDINote(n, startBeat, duration);
                        logger.debug("note {}", newnote);
                        startBeat += duration;
                        track.add(newnote);
                    }
                    startBeat += restBetweenPatterns;
                    logger.debug("bumped up sb with rest between ", startBeat);
                    if (upAndDown) {
                        for (int j = pattern.length - 1; j >= 0; j--) {
                            final int d = (dlist.get(pattern[j] + i))
                                    .intValue();
                            newnote = new MIDINote(startingMIDINumber + d,
                                    startBeat, duration);
                            startBeat += duration;
                            track.add(newnote);
                        }
                        startBeat += restBetweenPatterns;
                        logger.debug("bumped up sb with rest between ",
                                startBeat);
                    }
                }

                startingMIDINumber += 12;

            }
        }
        // list.map(new DurationModifier(duration));
        // list.sequential();
        return track;
    }

    /**
     * the pattern is a collection of indices into the notelist. no bounds
     * checking
     * 
     * {@code Integer[] pattern = new Integer[] 0, 0, 3, 2, 1 }; MIDITrack
     * patterned = MIDITrackFactory .applyPattern( notelist, pattern); }
     * 
     * A cleaner replacement for getNoteListPattern(blah blah) but doesn't do
     * the hanon like sequencing - which you can do externally
     * 
     * @param track
     *            a track
     * @param patternArray
     *            a pattern array
     * @param reverse
     *            reverse the pattern
     * @return a MIDITrack
     */
    public static MIDITrack applyPattern(MIDITrack track,
            final Integer[] patternArray, boolean reverse) {
        // MIDITrack result = new MIDITrack();
        //
        // List<Integer> pattern = Arrays.asList(patternArray);
        // if (reverse) {
        // Collections.sort(pattern, Collections.reverseOrder());
        // }
        // for (int index : pattern) {
        // System.err.println(index);
        // MIDINote note = (MIDINote) notelist.get(index).clone();
        // result.add(note);
        // }
        // return result;

        return applyPattern(track, patternArray, track.get(0)
                .getMidiNumber(), 1, reverse, false);

    }

    public static MIDITrack applyPattern(final MIDITrack track,
            final Integer[] patternArray, int startingMIDINumber,
            final int nOctaves, final boolean reverse, boolean upAndDown) {

        // PitchModifier pm = new PitchModifier(Operation.ADD, trans);
        // result.map(pm);
        // List<Integer> pattern = Arrays.asList(patternArray);
        List<Integer> pattern = new ArrayList<Integer>(patternArray.length);
        Collections.addAll(pattern, patternArray);
        return applyPattern(track, pattern, startingMIDINumber, nOctaves,
                reverse, upAndDown);

        // pattern.addAll(patternArray);

        // if (reverse) {
        // Collections.reverse(pattern);
        // } else if (upAndDown) {
        // List<Integer> patternud = new ArrayList<Integer>(2 * pattern.size());
        // patternud.addAll(pattern);
        // Collections.reverse(pattern);
        // patternud.addAll(pattern);
        // pattern = patternud;
        // System.out.println(pattern);
        // }
        //
        // for (int oct = 0; oct < nOctaves; oct++) {
        // int trans = startingMIDINumber - notelist.get(0).getMidiNumber();
        // for (int index : pattern) {
        // System.err.println(index);
        // MIDINote note = (MIDINote) notelist.get(index).clone();
        // note.transpose(trans);
        // result.add(note);
        // }
        // startingMIDINumber += 12;
        // }
        //
        // return result;
    }

    public static MIDITrack applyPattern(MIDITrack track,
            List<Integer> pattern, int startingMIDINumber, final int nOctaves,
            final boolean reverse, boolean upAndDown) {

        MIDITrack result = new MIDITrack();

        if (reverse) {
            // Collections.sort(pattern, Collections.reverseOrder());
            Collections.reverse(pattern);
        } else if (upAndDown) {
            List<Integer> patternud = new ArrayList<Integer>(2 * pattern.size());
            // Collections.copy(patternud, pattern);
            patternud.addAll(pattern);
            Collections.reverse(pattern);
            // Collections.sort(pattern, Collections.reverseOrder());
            patternud.addAll(pattern);
            pattern = patternud;
            logger.debug("pattern {}", pattern);
        }

        for (int oct = 0; oct < nOctaves; oct++) {
            int trans = startingMIDINumber - track.get(0).getMidiNumber();
            for (int index : pattern) {
                logger.debug("index {}", index);
                MIDINote note = (MIDINote) track.get(index).duplicate();
                note.transpose(trans);
                result.add(note);
            }
            startingMIDINumber += 12;
        }

        return result;
    }

    public static MIDITrack applyPattern(Integer[] midiNumberArray,
            List<Integer> pattern, int startingMIDINumber, final int nOctaves,
            final boolean reverse, boolean upAndDown) {

        List<Integer> midiNumberList = new ArrayList<Integer>(
                midiNumberArray.length);
        Collections.addAll(midiNumberList, midiNumberArray);

        return applyPattern(midiNumberList, pattern, startingMIDINumber,
                nOctaves, reverse, upAndDown);
    }

    public static MIDITrack applyPattern(List<Integer> midiNumbers,
            List<Integer> pattern, int startingMIDINumber, final int nOctaves,
            final boolean reverse, boolean upAndDown) {

        MIDITrack result = new MIDITrack();

        if (reverse) {
            // Collections.sort(pattern, Collections.reverseOrder());
            Collections.reverse(pattern);
        } else if (upAndDown) {
            List<Integer> patternud = new ArrayList<Integer>(2 * pattern.size());
            // Collections.copy(patternud, pattern);
            patternud.addAll(pattern);
            Collections.reverse(pattern);
            // Collections.sort(pattern, Collections.reverseOrder());
            patternud.addAll(pattern);
            pattern = patternud;
            logger.debug("pattern {}", pattern);
        }

        for (int oct = 0; oct < nOctaves; oct++) {
            int trans = startingMIDINumber - midiNumbers.get(0);
            for (int index : pattern) {
                logger.debug("index {}", index);
                int pit = midiNumbers.get(index) + trans;
                result.add(new MIDINote(pit));
            }
            startingMIDINumber += 12;
        }

        return result;
    }

    /**
     * Every note in the original is followed by new notes based on the
     * intervals.
     * 
     * Start beats are not calculated. Do something else for those.
     * 
     * <pre>
     * Scale major = ScaleFactory.createFromName(&quot;Major&quot;);
     * major.setOctave(5);
     * int[] intervalPattern = { 0, 2, 0, -1 };
     * MIDITrack scale = ScaleFactory.createMIDITrack(major);
     * MIDITrack embellished = embellish(scale, intervalPattern);
     * Staffer.showTrebleStaff(embellished, &quot;Major scale embellished&quot;);
     * </pre>
     * 
     * @param original
     * @param intervalPattern
     * @return
     */
    public static MIDITrack embellish(MIDITrack original,
            int[] intervalPattern) {
        MIDITrack list = new MIDITrack();

        for (MIDINote n : original) {
            logger.debug("embellishing " + n);
            list.add(n);
            for (int element : intervalPattern) {
                MIDINote newnote = new MIDINote(n.getMidiNumber()
                        + element);
                list.add(newnote);
                logger.debug("new note {}", newnote);
            }
        }
        return list;
    }

    public static MIDITrack createMirror(MIDITrack original) {
        return createMirror(original, true);
    }

    public static MIDITrack createMirror(MIDITrack original,
            boolean sortAscending) {
        MIDITrack notelist = new MIDITrack(original);
        final MIDITrack inversion = notelist.getInversion();
        inversion.remove(0);
        notelist = inversion.append(notelist);
        if (sortAscending) {
            notelist.sortByAscendingPitches();
        }
        return notelist;
    }

    public static MIDITrack createMirrorRetrograde(MIDITrack original) {
        MIDITrack notelist = new MIDITrack(original);
        MIDITrack inversion = notelist.getInversion();
        inversion.remove(0);
        // inversion = inversion.retrograde();
        notelist = inversion.append(notelist);
        return notelist;
    }

    public static String getPitchMaskAsString(MIDITrack track) {
        StringBuilder sb = new StringBuilder();
        Integer[] a = MIDITrackFactory.getPitchMask(track);
        for (Integer i : a) {
            sb.append(i).append(' ');
        }
        return sb.toString();
    }

    public static String getDurationMaskAsString(MIDITrack track) {
        StringBuilder sb = new StringBuilder();
        Integer[] a = MIDITrackFactory.getDurationMask(track);
        for (Integer i : a) {
            sb.append(i).append(' ');
        }
        return sb.toString();
    }

    public static Integer[] getPitchMask(MIDITrack track) {
        int count = 1;
        List<Integer> mask = new ArrayList<Integer>();
        MIDINote lastNote = null;
        for (int i = 0; i < track.size(); i++) {
            MIDINote n = track.get(i);

            if (lastNote != null) {
                if (n.getPitch().equals(lastNote.getPitch())) {
                    count++;
                    logger.debug(
                            "Incrementing count to {} for {}", count, n
                                    .getPitch());
                } else {
                    mask.add(count);
                    count = 1;
                    logger.debug(
                            "Resetting for pitches {} and {}", lastNote
                                    .getPitch(), n.getPitch());
                }
            }
            if (i == track.size() - 1) {
                logger.debug(
                        "last note was alone, adding {} for {}", count, n);
                mask.add(count);
            }
            lastNote = n;

        }
        // for (MIDINote n : notelist) {
        // if (lastNote != null) {
        // if (n.getPitch().equals(lastNote.getPitch())) {
        // count++;
        // System.err.println(String.format(
        // "Incrementing count to %d for %s", count, n
        // .getPitch()));
        // } else {
        // mask.add(count);
        // count = 1;
        // System.err.println(String.format(
        // "Resetting for pitches %s and %s", lastNote.getPitch(), n
        // .getPitch()));
        // }
        // }
        // lastNote = n;
        //
        // // if(lastNote == null) {
        // // count++;
        // // } else {
        // // if(n.getPitch().equals(lastNote.getPitch())) {
        // // count++;
        // // } else {
        // // mask.add(count);
        // // count = 1;
        // // }
        // // }
        //
        // }

        return mask.toArray(new Integer[mask.size()]);
    }

    public static Integer[] getDurationMask(MIDITrack track) {
        int count = 1;
        List<Integer> mask = new ArrayList<Integer>();
        MIDINote lastNote = null;
        for (int i = 0; i < track.size(); i++) {
            MIDINote n = track.get(i);
            if (lastNote != null) {
                if (Math.abs(n.getDuration() - lastNote.getDuration()) < .0000001) {
                    // if (n.getDuration() == lastNote.getDuration()) {
                    count++;
                    logger.debug(
                            "Incrementing count to {} for {}", count, n
                                    .getPitch());
                } else {
                    mask.add(count);
                    count = 1;
                    logger.debug(
                            "Resetting for pitches {} and {}", lastNote
                                    .getPitch(), n.getPitch());

                }
            }
            if (i == track.size() - 1) {
                logger.debug(
                        "last note was alone, adding {} for {}", count, n);
                mask.add(count);
            }
            lastNote = n;
        }
        return mask.toArray(new Integer[mask.size()]);
    }

    /**
     * {@code
     * Track ints = MIDITrackFactory.createFromIntervals(1, 2, 3, 4,
				5);
		
		CircularList<Integer> values = new CircularArrayList<Integer>();
		values.add(1);
		MIDITrack modints = MIDITrackFactory.modifyPitchIntervals(ints,
				values, true);
				
		// so intervals 1 2 3 4 5 become 2 3 4 5 6
		}
     * 
     * @param track
     *            the track
     * @param values
     *            the values added to the intervals
     * @param absolute
     *            is absolute
     * @return a MIDITrack created from the intervals
     */
    public static MIDITrack modifyPitchIntervals(MIDITrack track,
            CircularList<Integer> values, boolean absolute) {
        MIDITrack result = null;
        int[] intervals = null;
        if (absolute) {
            intervals = track.getPitchIntervals();
        } else {
            intervals = track.getPitchIntervalsAbsolute();
        }
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] += values.next();
        }
        result = createFromIntervals(intervals, track.get(0).getMidiNumber());

        // public static MIDITrack createFromIntervals(int[] intervals,
        // int baseInt, int unit, boolean absolute, int numOctaves)

        // Interval.
        return result;
    }

    /**
     * Filter the source track by the Guava predicate.
     * 
     * @param track
     *            the source track
     * @param p
     *            a Guava predicate
     * @return a new {@code MIDITrack}
     */
    public static MIDITrack createTrackFromPredicate(MIDITrack track,
            Predicate<MIDINote> p) {
        MIDITrack newTrack = new MIDITrack(Iterables.filter(track, p));
        return newTrack;
    }

    public static MIDITrack createTrackFromTimedPredicate(MIDITrack track,
            Predicate<Timed> p) {
        MIDITrack newTrack = new MIDITrack(Iterables.filter(track, p));
        return newTrack;
    }

    /**
     * Create a new track by applying the function to the supplied track.
     * 
     * @param track
     *            a {@code MIDITrack}
     * @param function
     *            a Guava Function
     * @return a new {@code MIDITrack}
     */
    public static MIDITrack create(MIDITrack track,
            Function<MIDINote, MIDINote> function) {
        List<MIDINote> newnotes = Lists.transform(track.getNotes(), function);
        MIDITrack newTrack = new MIDITrack(newnotes);
        return newTrack;
    }

    /**
     * Create a new track by applying the function on the supplied track. If the
     * Timed objects are MIDINotes, they are duplicated and added. If they are
     * not MIDINotes, a new MIDINote will be created with middle c, and the
     * start and duration of the Timed.
     * 
     * @param track
     *            a {@code MIDITrack}
     * @param function
     *            a Guava Function
     * @return a new {@code MIDITrack}
     */
    public static MIDITrack createFromTimed(MIDITrack track,
            Function<Timed, Timed> function) {
        List<Timed> newnotes = Lists.transform(track.getNotes(), function);

        List<MIDINote> notes = new ArrayList<>();
        for (Timed timed : newnotes) {
            if (timed instanceof MIDINote) {
                notes.add((MIDINote) timed.duplicate());
            } else {
                notes.add(new MIDINote(Pitch.C5, timed.getStartBeat(), timed
                        .getDuration()));
            }
        }
        MIDITrack newTrack = new MIDITrack(notes);
        return newTrack;
    }

    /**
     * Create a click track. Uses the length and time signatures of the provided
     * track.
     * 
     * @param track
     *            a {@code MIDITrack}
     * @return a new {@code MIDITrack}
     */
    public static MIDITrack createClickTrack(MIDITrack track) {
        MIDITrack clickTrack = new MIDITrack();
        clickTrack.setName("click track");
        int channel = 9;
        // int hi = HI_WOOD_BLOCK_PERC.getProgram();
        // int low = MIDIGMPatch.LOW_WOOD_BLOCK_PERC.getProgram();
        NavigableMap<Double, TimeSignature> ts = track.getTimeSignatures();
        double endBeat = track.getEndBeat();
        int num = 4;
        MIDINote nn = null;

        int beatInMeasure = 1;
        for (double beat = 1d; beat < endBeat; beat++) {
            if (ts.containsKey(beat)) {
                TimeSignature sig = ts.get(beat);
                num = sig.getNumerator();
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("new numerator %d at beat %f",
                            num, beat));
                }
                beatInMeasure = 0;
            }

            if (beatInMeasure == 0) {
                nn = new MIDINote(HI_WOOD_BLOCK_PERC, beat, 1d, channel);
            } else {
                nn = new MIDINote(LOW_WOOD_BLOCK_PERC, beat, 1d, channel);
            }

            // if (beat % num == 1) {
            // nn = new MIDINote(HI_WOOD_BLOCK_PERC, beat, 1d, channel);
            // } else {
            // nn = new MIDINote(LOW_WOOD_BLOCK_PERC, beat, 1d, channel);
            // }

            clickTrack.add(nn);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("beat %f num %d bim %d", beat, num,
                        beatInMeasure));
            }

            beatInMeasure++;
            beatInMeasure %= num;
        }
        return clickTrack;
    }
}
