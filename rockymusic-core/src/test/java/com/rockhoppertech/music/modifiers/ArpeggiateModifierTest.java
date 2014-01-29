package com.rockhoppertech.music.modifiers;

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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.chord.Chord;
import com.rockhoppertech.music.chord.ChordFactory;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.modifiers.Modifier.Operation;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ArpeggiateModifierTest {
    private static final Logger logger = LoggerFactory
            .getLogger(ArpeggiateModifierTest.class);

    @Test
    public void testArpeggiateModifier() {

        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7");
        MIDITrack track = chord.createMIDITrack();
        logger.debug("track:\n{}", track);

        List<Double> series = new ArrayList<Double>();
        series.add(.25);
        series.add(.5);
        series.add(.75);
        series.add(1d);
        double startBeat = 1d;
        double duration = 4d;
        ArpeggiateModifier mod = new ArpeggiateModifier(startBeat, duration,
                series,
                NoteModifier.Operation.ADD);
        track.map(mod);
        logger.debug("arp track:\n{}", track);

        MIDINote note = track.get(0);
        int expectedPitch = Pitch.C0;
        double expectedStart = 1d;
        assertThat("pitch is C0",
                note.getMidiNumber(),
                equalTo(expectedPitch));
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));

        note = track.get(1);
        expectedStart = 1.25;
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));

        note = track.get(2);
        expectedStart = 1.75;
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));

        note = track.get(3);
        expectedStart = 2.5;
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));
    }

    @Test
    public void scalar() {
        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7");
        MIDITrack track = chord.createMIDITrack();
        double startBeat = 1d;
        double duration = 4d;
        double gap = .25;
        ArpeggiateModifier mod = new ArpeggiateModifier(startBeat, duration,
                gap);
        logger.debug("track:\n{}", track);
        track.map(mod);
        logger.debug("arp track:\n{}", track);

        MIDINote note = track.get(0);
        int expectedPitch = Pitch.C0;
        double expectedStart = 1d;
        double expectedDuration = duration;
        assertThat("pitch is C0",
                note.getMidiNumber(),
                equalTo(expectedPitch));
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                note.getDuration(),
                equalTo(expectedDuration));

        note = track.get(1);
        expectedStart += gap;
        expectedDuration -= gap;
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                note.getDuration(),
                equalTo(expectedDuration));

        note = track.get(2);
        expectedStart += gap;
        expectedDuration -= gap;
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                note.getDuration(),
                equalTo(expectedDuration));

        note = track.get(3);
        expectedStart += gap;
        expectedDuration -= gap;
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                note.getDuration(),
                equalTo(expectedDuration));

    }

    @Test
    public void testGetDescription() {
        ArpeggiateModifier mod = new ArpeggiateModifier(1d, 2d, .25);
        assertThat("mod is not null",
                mod,
                notNullValue());
        assertThat("description is correct",
                mod.getDescription(),
                equalTo("Arpeggiate"));
    }

    @Test
    public void testGetName() {
        ArpeggiateModifier mod = new ArpeggiateModifier(1d, 2d, .25);
        assertThat("mod is not null",
                mod,
                notNullValue());
        String name = mod.getName();
        assertThat("name is correct",
                name,
                equalTo("Arpeggiate"));

    }

    @Test
    public void multiply() {
        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7");
        MIDITrack track = chord.createMIDITrack();
        double startBeat = 1d;
        double duration = 8d;
        double gap = 2d;
        ArpeggiateModifier mod = new ArpeggiateModifier(startBeat, duration,
                gap, Operation.MULTIPLY);
        logger.debug("track:\n{}", track);
        track.map(mod);
        logger.debug("arp track:\n{}", track);

        MIDINote note = track.get(0);
        int expectedPitch = Pitch.C0;
        double expectedStart = 1d;
        double expectedDuration = duration;
        assertThat("pitch is C0",
                note.getMidiNumber(),
                equalTo(expectedPitch));
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                note.getDuration(),
                equalTo(expectedDuration));

        note = track.get(1);
        expectedStart *= gap;
        expectedDuration = duration - expectedStart + 1;
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                note.getDuration(),
                equalTo(expectedDuration));

        note = track.get(2);
        expectedStart *= gap;
        expectedDuration = duration - expectedStart + 1;
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                note.getDuration(),
                equalTo(expectedDuration));

        note = track.get(3);
        expectedStart *= gap;
        expectedDuration = duration - expectedStart + 1;
        assertThat("start beat is correct",
                note.getStartBeat(),
                equalTo(expectedStart));
        assertThat("duration is correct",
                note.getDuration(),
                equalTo(expectedDuration));

    }

}
