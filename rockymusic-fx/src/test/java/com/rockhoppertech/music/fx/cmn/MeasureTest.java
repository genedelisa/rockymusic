package com.rockhoppertech.music.fx.cmn;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.TimeSignature;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class MeasureTest {
    private static final Logger logger = LoggerFactory
            .getLogger(MeasureTest.class);

    @Test
    public void quartersOneMeasureIn44() {
        MIDITrack track = new MIDITrack();
        double startBeat = 1d;
        int pitch = Pitch.C6;
        double dur = 1d;
        double lastBeat = startBeat * 5; // 4 quarters

        while (startBeat < lastBeat) {
            track.add(new MIDINote(pitch, startBeat, dur));
            startBeat += dur;
        }

        // Map<Double, TimeSignature> tsMap = new TreeMap<Double,
        // TimeSignature>();
        // tsMap.put(0d, new TimeSignature(4, 4));

        track.addTimeSignatureAtBeat(1d, 4, 4);

        logger.debug("calcing measures");
        NavigableMap<Double, Measure> measures = Measure.createMeasures(track);
        for (Double d : measures.keySet()) {
            logger.debug("measure {}", measures.get(d));
        }

        assertThat("measures is not null", measures, notNullValue());
        assertThat("size is correct",
                measures.size(),
                equalTo(1));

        Measure m = measures.get(1d);
        assertThat("measure 1 is not null", m, notNullValue());

        MIDITrack track2 = m.getMIDITrack();
        assertThat("track for m 1 is not null", track2, notNullValue());

        assertThat("track size is correct",
                track2.size(),
                equalTo(4));
    }

    @Test
    public void quartersTwoMeasuresIn44() {
        MIDITrack track = new MIDITrack();
        double startBeat = 1d;
        int pitch = Pitch.C6;
        double dur = 1d;
        double lastBeat = 9d;

        while (startBeat < lastBeat) {
            track.add(new MIDINote(pitch, startBeat, dur));
            startBeat += dur;
        }
        track.addTimeSignatureAtBeat(1d, 4, 4);

        logger.debug("calcing measures");
        NavigableMap<Double, Measure> measures = Measure.createMeasures(track);
        for (Double d : measures.keySet()) {
            logger.debug("m {}", measures.get(d));
        }

        assertThat("measures is not null", measures, notNullValue());
        assertThat("size is correct",
                measures.size(),
                equalTo(2));

        Measure m = measures.get(1d);
        assertThat("measure 1 is not null", m, notNullValue());

        MIDITrack track2 = m.getMIDITrack();
        assertThat("track for m 1 is not null", track2, notNullValue());
        assertThat("track size is correct",
                track2.size(),
                equalTo(4));

        m = measures.get(5d);
        assertThat("measure 5 is not null", m, notNullValue());

        track2 = m.getMIDITrack();
        assertThat("track for m 1 is not null", track2, notNullValue());
        assertThat("track size is correct",
                track2.size(),
                equalTo(4));
    }

    @Test
    public void quartersWithGap() {
        MIDITrack track = new MIDITrack();
        double startBeat = 1d;
        int pitch = Pitch.C6;
        double dur = 1d;
        double lastBeat = 5d;
        
        track.addTimeSignatureAtBeat(1d, 4, 4);
        
        while (startBeat < lastBeat) {
            track.add(new MIDINote(pitch, startBeat, dur));
            startBeat += dur;
        }
        
        startBeat = 16d;
        lastBeat = 32d;
        while (startBeat < lastBeat) {
            track.add(new MIDINote(pitch, startBeat, dur));
            startBeat += dur;
        }

        logger.debug("calcing measures {}", track);
        NavigableMap<Double, Measure> measures = Measure.createMeasures(track);
        for (Double d : measures.keySet()) {
            logger.debug("measure: {}", measures.get(d));
        }

        assertThat("measures is not null", measures, notNullValue());
        assertThat("measures size is correct",
                measures.size(),
                equalTo(8)); // 32 is last beat, 4/4 is the ts, 32/4=8
        
        
        
        // assertEquals(2, measures.size());
        // Measure m = measures.get(1d);
        // assertNotNull(m);
        // MIDITrack nl = m.getMIDITrack();
        // assertNotNull(nl);
        // assertEquals(4, nl.size());
        //
        // m = measures.get(5d);
        // assertNotNull(m);
        // nl = m.getMIDITrack();
        // assertNotNull(nl);
        // assertEquals(4, nl.size());
    }

    @Test
    public void wholesTiedTwoMeasuresIn44() {
        MIDITrack track = new MIDITrack();
        double startBeat = 1d;
        int pitch = Pitch.C6;
        double dur = 5d;
        double lastBeat = 9d;

        while (startBeat < lastBeat) {
            track.add(new MIDINote(pitch, startBeat, dur));
            startBeat += dur;
        }

        logger.debug("calcing measures for {}", track);
        NavigableMap<Double, Measure> measures = Measure
                .createMeasures(track);
        for (Double d : measures.keySet()) {
            logger.debug("m {}", measures.get(d));
        }

        assertThat("measures is not null", measures, notNullValue());
        assertThat("size is correct",
                measures.size(),
                equalTo(2));

        Measure m = measures.get(1d);
        assertThat("measure 1 is not null", m, notNullValue());

        MIDITrack track2 = m.getMIDITrack();
        assertThat("track for m 1 is not null", track2, notNullValue());
        assertThat("track size is correct",
                track2.size(),
                equalTo(1));

        m = measures.get(5d);
        assertThat("measure 5 is not null", m, notNullValue());

        track2 = m.getMIDITrack();
        assertThat("track for m 5 is not null", track2, notNullValue());
        assertThat("track size is correct",
                track2.size(),
                equalTo(1));
        assertThat("start beat is correct",
                track2.get(0).getStartBeat(),
                equalTo(6d));
    }

    @Test
    public final void testMeasure() {

    }

    @Test
    public final void testMeasureMIDITrack() {

    }

    @Test
    public final void testAddMIDINote() {

    }

    @Test
    public final void testAddDoubleKeySignature() {

    }

    @Test
    public final void testGetKeySignatureAtBeat() {

    }

    @Test
    public final void testGetEndBeat() {

    }

    @Test
    public final void testGetStartBeat() {

    }

    @Test
    public final void testSetStartBeat() {

    }

    @Test
    public final void testGetTimeSignature() {

    }

    @Test
    public final void testSetTimeSignature() {

    }

    @Test
    public final void testGetBeatInMeasure() {

    }

    @Test
    public final void testAssignToMeasures() {

    }

    @Test
    public final void testCreateMeasuresMIDITrack() {

    }

    @Test
    public final void testCreateMeasuresDoubleNavigableMapOfDoubleTimeSignature() {

    }

    @Test
    public final void testGetNotesAtBeat() {

    }

    @Test
    public final void testGetStartTimesInBeat() {

    }

    @Test
    public final void testGetMIDITrack() {

    }

    @Test
    public final void testGettrack() {

    }

    @Test
    public final void testSettrack() {

    }

    @Test
    public final void testSetDivisions() {

    }

    @Test
    public final void testGetDivisions() {

    }

    @Test
    public final void testSetKeySignatureAtBeat() {

    }

}
