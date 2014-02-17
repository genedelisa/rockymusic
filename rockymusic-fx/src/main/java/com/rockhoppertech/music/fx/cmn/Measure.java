package com.rockhoppertech.music.fx.cmn;

import java.util.BitSet;
import java.util.NavigableMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.midi.js.KeySignature;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.TimeSignature;

/**
 * A measure has nothing to do with the track but is just a notational
 * convention.
 * 
 * if tied notes are added to a measure, then you cannot recreate the original
 * track
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 */
public class Measure {

    private static final Logger logger = LoggerFactory
            .getLogger(Measure.class);
    private MIDITrack track;

    // private int number; // need this?

    private TimeSignature timeSignature;

    private double startBeat; // in relation to the beginning

    private NavigableMap<Double, MIDINote> beatMap;
    private NavigableMap<Double, KeySignature> keySignatureMap;
    private double divisions;

    /**
     * Initialize an empty {@code Measure}.
     */
    public Measure() {
        this(new MIDITrack());
    }

    /**
     * Create a {@code Measure} from the {@code MIDITrack}.
     * 
     * @param midiTrack
     *            Source of the measure data
     */
    public Measure(MIDITrack midiTrack) {
        this.track = midiTrack;
        this.beatMap = new TreeMap<Double, MIDINote>();
        this.keySignatureMap = new TreeMap<Double, KeySignature>();

        KeySignature ks = this.track.getKeySignatureAtBeat(1d);
        if (ks == null) {
            this.keySignatureMap.put(0d, KeySignature.CMAJOR);
        } else {
            this.keySignatureMap.put(0d, ks);
        }

        TimeSignature ts = this.track.getTimeSignatureAtBeat(1d);
        if (ts == null) {
            this.timeSignature = new TimeSignature(4, 4);
        } else {
            this.timeSignature = ts;
        }

    }

    public void add(MIDINote n) {
        this.track.add(n);
    }

    public void add(double beat, KeySignature keysig) {
        this.keySignatureMap.put(beat,
                keysig);
    }

    public KeySignature getKeySignatureAtBeat(double beat) {
        KeySignature ks = null;
        Double key = this.keySignatureMap.floorKey(beat);
        if (key != null) {
            ks = this.keySignatureMap.get(key);
        }
        return ks;
    }

    /**
     * @return the endBeat
     */
    public double getEndBeat() {
        // TODO not right. the end time is wrong

        return this.startBeat + ((double) this.timeSignature.getNumerator());
    }

    /**
     * @return the startBeat
     */
    public double getStartBeat() {
        return this.startBeat;
    }

    /**
     * @param startBeat
     *            the startBeat to set
     */
    public void setStartBeat(double startBeat) {
        this.startBeat = startBeat;
    }

    /**
     * @return the timeSignature
     */
    public TimeSignature getTimeSignature() {
        return this.timeSignature;
    }

    /**
     * @param timeSignature
     *            the timeSignature to set
     */
    public void setTimeSignature(TimeSignature timeSignature) {
        this.timeSignature = timeSignature;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append(" start beat=").append(this.startBeat);
        sb.append(" end beat=").append(this.getEndBeat());
        sb.append(" ts num=").append(this.timeSignature.getNumerator());
        sb.append(" ts den=").append(this.timeSignature.getDenominator());
        for (Double d : keySignatureMap.keySet()) {
            KeySignature ks = keySignatureMap.get(d);
            if (ks != null) {
                String str = String.format(" key sig at beat %f is %s, ",
                        d,
                        ks.getDisplayName());
                sb.append(str);
            }
        }
        sb.append("\n");
        for (MIDINote n : this.track) {
            double beat = n.getBeat();
            double duration = n.getDuration();
            Pitch p = n.getPitch();
            if (p == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("null pitch! " + n);
                }
                continue;
            }
            String fmt = String.format("note %f dur %f mstart %f pitch %s\n",
                    beat,
                    duration,
                    getBeatInMeasure(n),
                    PitchFormat.getInstance().format(p));
            sb.append(fmt);

            // sb.append("note ").append(beat).append(" dur ").append(duration)
            // .append(" m start ").append(getBeatInMeasure(n)).append(
            // "\n");
        }
        return sb.toString();
    }

    public double getBeatInMeasure(MIDINote n) {
        return n.getStartBeat() - this.startBeat + 1d;
    }

    public static void assignToMeasures(MIDITrack track,
            NavigableMap<Double, Measure> measures) {

        // KeySignature initialKs = track.getKeySignatureAtBeat(1d);

        for (Double d : measures.keySet()) {
            Measure m = measures.get(d);
            // TODO loop all beats
            KeySignature ks = track.getKeySignatureAtBeat(m.getStartBeat());
            m.setKeySignatureAtBeat(m.getStartBeat(),
                    ks);
            // track.getTimeSignatures()

            boolean clone = false;
            boolean endInclusive = false;
            MIDITrack nl = track.sublist(m.getStartBeat(),
                    m.getEndBeat(),
                    clone,
                    endInclusive);

            double end = nl.getEndBeat();
            if (end > m.getEndBeat()) {
                // well, ok. the ties are really just a notation convention
                // break the notes up here? how would I then know about the tie
                // when trying to draw?

                logger.debug("track end {} is longer than m end {]",
                        end, m.getEndBeat());

            }
            m.settrack(nl);
        }

    }

    public static NavigableMap<Double, Measure> createMeasures(MIDITrack track) {

        NavigableMap<Double, Measure> measures = createMeasures(
                track.getEndBeat(),
                track.getTimeSignatures());

        assignToMeasures(track, measures);
        return measures;
    }

    /**
     * Create a map of Measures that cover the total beat range. Each Measure
     * has a Time Signature from the provided map. If the map is empty, the
     * signature will be 4/4. You can get a time signature map from a MIDITrack.
     * 
     * 
     * @param totalBeats
     *            number of beat to cover
     * @param tsMap
     *            a time signature map
     * @return a map containing the new measures
     */
    public static NavigableMap<Double, Measure> createMeasures(
            double totalBeats,
            NavigableMap<Double, TimeSignature> tsMap) {

        NavigableMap<Double, Measure> mmap = new TreeMap<Double, Measure>();

        logger.debug("{} beats in track", totalBeats);

        for (double start = 1d; start < totalBeats;) {
            double key = 0d;

            Measure m = new Measure();
            m.setStartBeat(start);
            mmap.put(start, m);

            // <= given key
            Double tsk = tsMap.floorKey(start);
            if (tsk == null) {
                logger.debug("ts key null for beat " + start);
            } else {
                key = tsk;
                logger.debug("ts key {} for beat {}",
                        start, tsk);
            }

            TimeSignature ts = tsMap.get(key);
            if (ts != null) {
                logger.debug("at start {} ts num is {}",
                        start, ts.getNumerator());
            } else {
                logger.debug("ts is null for {}", key);
                ts = new TimeSignature(4, 4);
            }

            m.setTimeSignature(ts);
            start += ts.getNumerator();
        }

        return mmap;
    }

    /*
     * public static List<Measure> assignToMeasurePass1(MIDITrack track,
     * NavigableMap<Double, TimeSignature> tsMap) { List<Measure> measureList =
     * new ArrayList<Measure>(); Measure measure = new Measure();
     * 
     * int currentMeasureNumber = 0; for (MIDINote n : track) { double startBeat
     * = n.getStartBeat() - 1d; // tracks are 1 based double key = 0d; Double
     * tsk = tsMap.lowerKey(startBeat); if (tsk == null) { key = 0d; if
     * (logger.isDebugEnabled()) { logger.debug("ts key null for beat " +
     * startBeat); } } TimeSignature ts = tsMap.get(key);
     * 
     * int measureNumber = (int) (startBeat / ts.getNumerator()); double
     * duration = n.getDuration(); if (logger.isDebugEnabled()) { String s =
     * String.format("processing note sb %f dur %f mn %d", startBeat, duration,
     * measureNumber); logger.debug(s); } if (measureNumber >
     * currentMeasureNumber) { if (logger.isDebugEnabled()) {
     * logger.debug("new measure " + measureNumber); } currentMeasureNumber =
     * measureNumber; measureList.add(measure); measure = new Measure();
     * measure.add(n); } else { if (logger.isDebugEnabled()) {
     * logger.debug("adding note to current measure " + measureNumber); }
     * measure.add(n); } } if (measureList.contains(measure) == false)
     * measureList.add(measure); return measureList; }
     */

    /*
     * public static List<Measure> calcMeasures(MIDITrack track, Map<Double,
     * TimeSignature> tsMap) { List<Measure> measureList = new
     * ArrayList<Measure>(); double prevDuration = -1d; double beat = -1d; int
     * beamGroupSize = -1;
     * 
     * Measure measure = new Measure(); TimeSignature ts = tsMap.get(0d);
     * 
     * // if a note is broken across measures // make a tmp measure. Measure
     * nextMeasure = null; int currentMeasure = 0; double
     * beatsRemainingInMeasure = ts.getNumerator(); double currentBeat = 1;
     * boolean measureAdded = false;
     * 
     * for (MIDINote n : track) { beat = n.getStartBeat(); double duration =
     * n.getDuration(); if (logger.isDebugEnabled()) { String s =
     * String.format("processing note sb %f dur %f", beat, duration);
     * logger.debug(s); }
     * 
     * // does this note belong in the current measure
     * 
     * // does the note extend into the next measure? (tie) int tmpm = 0; if
     * (beat > ts.getNumerator()) { tmpm = (int) (beat - 1) / ts.getNumerator();
     * if (logger.isDebugEnabled()) { String s =
     * String.format("beat %f is > numerator", beat); logger.debug(s); } } else
     * { tmpm = 0; } if (logger.isDebugEnabled()) { String s =
     * String.format("temp m %d", tmpm); logger.debug(s); }
     * 
     * double beatInMeasure; if (beat <= ts.getNumerator()) { beatInMeasure =
     * beat; } else { beatInMeasure = beat - (tmpm * ts.getNumerator()); }
     * double coverage = beatInMeasure + duration;
     * 
     * if (logger.isDebugEnabled()) { String s =
     * String.format("covers beats %f to %f ", beatInMeasure, coverage);
     * logger.debug(s); }
     * 
     * if (tmpm == currentMeasure) { if (logger.isDebugEnabled()) { String s =
     * String.format("belongs in current measure %d", currentMeasure);
     * logger.debug(s); }
     * 
     * if (coverage > (ts.getNumerator() + 1)) { if (logger.isDebugEnabled()) {
     * String s = String .format(
     * "ties to note in next measure, coverage %f and num is %d", coverage,
     * ts.getNumerator() + 1); logger.debug(s); } double beatsHere =
     * ts.getNumerator() - beatInMeasure + 1; double beatsThere = duration -
     * beatsHere; if (logger.isDebugEnabled()) { String s =
     * String.format("here %f there %f", beatsHere, beatsThere);
     * logger.debug(s); }
     * 
     * // make two new notes MIDINote n1 = new MIDINote(n.getPitch(), beat,
     * beatsHere); MIDINote n2 = new MIDINote(n.getPitch(), 1d, beatsThere);
     * 
     * if (nextMeasure == null) { // nextMeasure = new Measure(); //
     * nextMeasure.add( ); } else { nextMeasure = null; }
     * 
     * } else { // measure.add(n); }
     * 
     * measure.add(n); } else {
     * 
     * measureList.add(measure); currentMeasure++; if (logger.isDebugEnabled())
     * { String s = String.format( "note belongs in next measure which is %d",
     * currentMeasure); logger.debug(s); }
     * 
     * if (nextMeasure == null) { } else { nextMeasure = null; }
     * 
     * measure = new Measure(); measure.add(n); measureAdded = false;
     * beatsRemainingInMeasure = ts.getNumerator(); } System.out.println();
     * 
     * // currentBeat += duration;
     * 
     * // System.out.println("cm " + currentMeasure); // beatsRemainingInMeasure
     * = } if (measureAdded == false) { measureList.add(measure); } return
     * measureList; }
     */

    /*
     * beat grouping
     * 
     * 6/8 group 8ths as 3,3,total:6 group 16ths as 6,6, total: 12 subdivide
     * their secondary beams 6,6 group 32nds 12,12 : total 24
     * 
     * 4/4 8ths 4,4 total 8 16ths 4,4,4,4 toal 16 32nds 8,8,8,8 total 32
     * secondary 4,4,4,4,4,4,4,4 toal:32
     */
    int beamEighths[] = new int[] { 4, 4 };
    int beamSixteenths[] = new int[] { 4, 4, 4, 4 };
    int beamThirtyseconds[] = new int[] { 8, 8, 8, 8 };

    public static void main(String[] args) {
        MIDITrack track = new MIDITrack();
        double beat = 1;
        int pitch = Pitch.C6;
        double dur = .5d;
        double nextBeat = beat * 5; // 4 quarters
        nextBeat = beat * 12;

        while (beat < nextBeat) {
            track.add(new MIDINote(pitch, beat, dur));
            beat += dur;
        }

        // nextBeat = beat + 1;
        // dur = .5;
        // while (beat < nextBeat) {
        // track.add(new MIDINote(pitch, beat, dur));
        // beat += dur;
        // }
        // nextBeat = beat + 1;
        // dur = .25;
        // while (beat < nextBeat) {
        // track.add(new MIDINote(pitch, beat, dur));
        // beat += dur;
        // }

        System.out.println(track);
        // Measure m = new Measure(track);
        // m.dump();

        // NavigableMap<Double, TimeSignature> tsMap = new TreeMap<Double,
        // TimeSignature>();
        // tsMap.put(0d, new TimeSignature(4, 4));
        // tsMap.put(4d, new TimeSignature(3, 4));
        // tsMap.put(13d, new TimeSignature(6, 8));
        track.addTimeSignatureAtBeat(1d,
                new TimeSignature(3, 4));
        // track.addTimeSignatureAtBeat(new TimeSignature(4, 4), 4d);
        // track.addTimeSignatureAtBeat(new TimeSignature(3, 4), 10d);// will
        // actually put it at 12 since 10 is stupid here

        NavigableMap<Double, Measure> measures = createMeasures(track);
        for (Double d : measures.keySet()) {
            System.out.println(d);
            Measure mm = measures.get(d);
            System.out.println(mm);
        }

        System.out.println("Iterating beats");
        for (Double d : measures.keySet()) {
            System.out.println("measure at beat " + d);
            Measure mm = measures.get(d);
            TimeSignature t = mm.getTimeSignature();

            double lastBeat = 0;
            for (double bbeat = 1d; bbeat <= t.getNumerator(); bbeat += 1d) {
                System.out.println("looping beat " + bbeat);
                // KeySignature ks = mm.getKeySignatureAtBeat(bbeat);
                MIDITrack nlAtBeat = mm.getNotesAtBeat(bbeat);
                if (nlAtBeat != null && nlAtBeat.size() != 0) {
                    double shortest = nlAtBeat.getShortestDuration();
                    System.out.println(String
                            .format("beat %f shortest duration is: %s",
                                    bbeat,
                                    shortest));
                    BitSet bs = new BitSet((int) (1 / shortest));
                    System.out.println("bitset: " + bs);
                    bs.set(0);

                    for (MIDINote n : nlAtBeat) {
                        String ss = String.format("beat %f note: %s",
                                bbeat,
                                n);
                        System.out.println(ss);
                        double startGap = n.getStartBeat() - bbeat;
                    }
                } else {
                    System.out.println("no notes at beat " + bbeat);
                }
            }
            System.out.println(mm);
        }

        //
        // System.out.println("assigning measures");
        // assignToMeasures(track, measures);
        // for (Double d : measures.keySet()) {
        // System.out.println(measures.get(d));
        // }

        // List<Measure> measureList = m.calcMeasures();
        // List<Measure> measureList = Measure.calcMeasures(track, tsMap);
        // List<Measure> measureList = Measure.assignToMeasurePass1(track,
        // tsMap);
        //
        // for (Measure ms : measureList) {
        // System.out.println(ms);
        // }

    }

    /**
     * Get all notes starting on beat but before next beat. e.g. given 1.0
     * return start beats of 1.0 1.1 1.2 1.4 ... 1.999999
     * 
     * @param beat
     * @return
     */
    public MIDITrack getNotesAtBeat(double beat) {
        double from = beat;
        double to = beat + 1.0;
        MIDITrack nlAtBeat = new MIDITrack();
        for (MIDINote note : this.track) {
            double bim = getBeatInMeasure(note);
            if (bim >= from && bim < to)
                nlAtBeat.add(note);
        }
        return nlAtBeat;
    }

    public SortedSet<Double> getStartTimesInBeat(MIDITrack track) {
        SortedSet<Double> set = new TreeSet<Double>();
        for (MIDINote note : track) {
            double bim = getBeatInMeasure(note);
            set.add(bim);
        }
        return set;
    }

    /**
     * 
     * @return
     */
    public MIDITrack getMIDITrack() {
        return this.track;
    }

    /**
     * @return the track
     */
    public MIDITrack gettrack() {
        return this.track;
    }

    /**
     * @param track
     *            the track to set
     */
    public void settrack(MIDITrack track) {
        this.track = track;
    }

    /**
     * In MusicXML each measure could have a different beat division and the
     * notes have durations in relation to the division to make things more
     * difficult.
     * 
     * @param divisions
     */
    public void setDivisions(double divisions) {
        this.divisions = divisions;
    }

    /**
     * @return the divisions
     */
    public double getDivisions() {
        return this.divisions;
    }

    /**
     * 
     * @param d
     * @param ks
     */
    public void setKeySignatureAtBeat(double d, KeySignature ks) {
        this.keySignatureMap.put(d,
                ks);
    }
}
