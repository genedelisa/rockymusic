package com.rockhoppertech.music.chord;

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

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ChordVoicingTest {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordVoicingTest.class);

    @Test
    public void testGetInversion() {
        String chordVoicing = null;

        Chord c = ChordFactory.getChordByFullSymbol("C");
        Chord c2 = ChordFactory.getChordByFullSymbol("C");

        assertNotNull(c);
        assertNotNull(c2);
        logger.debug(
                "identity hash {}",
                System.identityHashCode(c.getChordVoicing()));
        logger.debug(
                "identity hash {}",
                System.identityHashCode(c2.getChordVoicing()));
        chordVoicing = c.getChordVoicing();
        assertNotNull(chordVoicing);
        // String expected = String.format("%d %d", 4, 7);
        String expected = String.format("1 3 5");
        assertThat(chordVoicing, equalTo(expected));
        c.setInversion(1);
        assertThat(c2.getChordVoicing(), equalTo(expected));

    }

    @Test
    @Ignore
    public void testGetNoteListIntChord() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void inversion() {
        Chord c = null;
        MIDITrack notelist = null;
        String chordVoicing = null;
        int octave = 5;

        try {
            c = ChordFactory.getChordByFullSymbol("C11");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(c);
        c.setOctave(octave);
        // notelist = c.getVoicedNotelist();
        // logger.debug(notelist);
        // assertNotNull(notelist);
        // assertThat(notelist.size(), equalTo(6));

        // chordVoicing = c.getChordVoicing();
        // String inv = chordVoicing.getInversion(c, 1);
        // logger.debug("first inv=" + inv);
        // chordVoicing = new ChordVoicing(c.getOctave(), inv);
        // c.setChordVoicing(chordVoicing);
        // logger.debug(c.getVoicedNotelist());
        //
        // // 3 5 7 9 11 +1
        // c = ChordFactory.getChordByFullSymbol("C11");
        // assertNotNull(c);
        // c.setOctave(octave);
        // chordVoicing = new ChordVoicing(c.getOctave(), chordVoicing
        // .getInversion(c, 1));
        // notelist = chordVoicing.getNoteList(c);
        // assertThat(notelist.size(), equalTo(6));
        // assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.E5));
        // assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.G5));
        // assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.BF5));
        // assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.D6));
        // assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.F6));
        // assertThat(notelist.get(5).getMidiNumber(), equalTo(Pitch.C6));

        try {
            c = ChordFactory.getChordByFullSymbol("C11");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(c);
        c.setOctave(octave);
        notelist = c.createMIDITrack();
        logger.debug("track\n{}", notelist);
        c.setInversion(1); // TODO this is broken. moves the C up an octave
        notelist = c.createMIDITrack();
        logger.debug("track\n{}", notelist);
        assertThat(notelist.size(), equalTo(6));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.E5));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.G5));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.BF5));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.D6));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.F6));
        assertThat(notelist.get(5).getMidiNumber(), equalTo(Pitch.C6));

        c.setInversion(2);
        notelist = c.createMIDITrack();
        assertThat(notelist.size(), equalTo(6));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.G5));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.BF5));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.D6));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.F6));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.C6));
        assertThat(notelist.get(5).getMidiNumber(), equalTo(Pitch.E6));

        c.setInversion(3);
        notelist = c.createMIDITrack();
        assertThat(notelist.size(), equalTo(6));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.BF5));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.D6));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.F6));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.C6));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.E6));
        assertThat(notelist.get(5).getMidiNumber(), equalTo(Pitch.G6));

        c.setInversion(4);
        notelist = c.createMIDITrack();
        assertThat(notelist.size(), equalTo(6));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.D6));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.F6));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.C7));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.E6));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.G6));
        assertThat(notelist.get(5).getMidiNumber(), equalTo(Pitch.BF6));

        c.setInversion(5);
        notelist = c.createMIDITrack();
        assertThat(notelist.size(), equalTo(6));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.F6));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.C7));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.E7));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.G6));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.BF6));
        assertThat(notelist.get(5).getMidiNumber(), equalTo(Pitch.D7));
    }

    @Test
    @Ignore
    public void drops() {
        Chord c = null;
        MIDITrack notelist = null;
        String dropVoicingString = null;

        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj9");
        } catch (UnknownChordException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        assertNotNull(c);
        c.setOctave(5);

        dropVoicingString = ChordVoicing.getDropString(c, 2);
        assertThat(dropVoicingString, equalTo("1 3 5 -7 9"));
        logger.debug(dropVoicingString);
        c.setChordVoicing(dropVoicingString);
        notelist = c.createMIDITrack();
        assertThat(notelist.size(), equalTo(5));
        logger.debug("track\n{}", notelist);
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.C5));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.E5));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.G5));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.B4));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.D6));

        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj9");
        } catch (UnknownChordException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        assertNotNull(c);
        c.setOctave(5);
        dropVoicingString = ChordVoicing.getDropString(c, 1);
        logger.debug(dropVoicingString);
        assertThat(dropVoicingString, equalTo("1 3 5 7 -9"));
        c.setChordVoicing(dropVoicingString);
        notelist = c.createMIDITrack();
        logger.debug("track\n{}", notelist);
        assertThat(notelist.size(), equalTo(5));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.C5));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.E5));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.G5));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.B5));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.D5));

        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj9");
        } catch (UnknownChordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNotNull(c);
        c.setOctave(5);
        dropVoicingString = ChordVoicing.getDropString(c, 3);
        logger.debug(dropVoicingString);
        assertThat(dropVoicingString, equalTo("1 3 -5 7 9"));
        c.setChordVoicing(dropVoicingString);
        notelist = c.createMIDITrack();
        logger.debug("track\n{}", notelist);
        assertThat(notelist.size(), equalTo(5));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.C5));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.E5));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.G4));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.B5));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.D6));

        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj9");
        } catch (UnknownChordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNotNull(c);
        c.setOctave(5);
        dropVoicingString = ChordVoicing.getDropString(c, 4);
        logger.debug(dropVoicingString);
        assertThat(dropVoicingString, equalTo("1 -3 5 7 9"));
        c.setChordVoicing(dropVoicingString);
        notelist = c.createMIDITrack();
        logger.debug("track\n{}", notelist);
        assertThat(notelist.size(), equalTo(5));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.C5));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.E4));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.G5));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.B5));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.D6));

    }

    @Test
    public void testGetNoteListChord() {
        Chord c = null;
        MIDITrack notelist = null;
        int octave = 3;
        ChordVoicing cv = null;
        MIDINote mn = null;

        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj9");
        } catch (UnknownChordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNotNull(c);
        // assertThat(notNullValue(c));
        cv = new ChordVoicing(octave, "1 +3 +5 +7 +9");
        notelist = cv.getNoteList(octave, c);
        assertNotNull(notelist);
        assertThat(notelist.size(), equalTo(5));
        assertThat(c.isMajor(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(true));
        assertThat(c.hasNinth(), equalTo(true));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getSymbol(), equalTo("maj9"));
        mn = notelist.get(0);
        assertThat(mn.getMidiNumber(), equalTo(Pitch.C3));
        mn = notelist.get(1);
        assertThat(mn.getMidiNumber(), equalTo(Pitch.E4));
        mn = notelist.get(2);
        assertThat(mn.getMidiNumber(), equalTo(Pitch.G4));
        mn = notelist.get(3);
        assertThat(mn.getMidiNumber(), equalTo(Pitch.B4));
        mn = notelist.get(4);
        assertThat(mn.getMidiNumber(), equalTo(Pitch.D5));

        try {
            c = ChordFactory.getChordByFullSymbol("Dm9");
        } catch (UnknownChordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        notelist = cv.getNoteList(octave, c);
        assertThat(notelist.size(), equalTo(5));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.D3));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.F4));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.A4));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.C5));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.E5));

        try {
            c = ChordFactory.getChordByFullSymbol("Dm13");
        } catch (UnknownChordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cv = new ChordVoicing(octave, "1 +3 5 7 9 11 13");
        notelist = cv.getNoteList(octave, c);

        assertThat(notelist.size(), equalTo(7));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.D3));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.F4));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.A3));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.C4));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.E4));
        assertThat(notelist.get(5).getMidiNumber(), equalTo(Pitch.G4));
        assertThat(notelist.get(6).getMidiNumber(), equalTo(Pitch.B4));

        cv = new ChordVoicing(octave, "1 +3 5 7 13");
        notelist = cv.getTrack(c);

        assertThat(notelist.size(), equalTo(5));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.D3));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.F4));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.A3));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.C4));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.B4));
        //
        // cv = new ChordVoicing(octave, "+3 5 7 13");
        // notelist = cv.getNoteList(c);
        // assertThat(notelist.size(), equalTo(4));
        // assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.F4));
        // assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.A4));
        // assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.C5));
        // assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.B5));
        //
        //
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj");
        } catch (UnknownChordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cv = new ChordVoicing(octave, "1 +3 +5 +1 ++3 ++5");
        notelist = cv.getTrack(c);
        assertThat(notelist.size(), equalTo(6));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.C3));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.E4));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.G4));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.C5));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.E5));
        assertThat(notelist.get(5).getMidiNumber(), equalTo(Pitch.G5));
        //
        cv = new ChordVoicing(octave, "1 3 5 1 +3 +5 +1 ++3 ++5 ++1 +++3 +++5");
        notelist = cv.getTrack(c);
        logger.debug("track \n{}", notelist);
        assertThat(notelist.size(), equalTo(12));
        assertThat(notelist.get(0).getMidiNumber(), equalTo(Pitch.C3));
        assertThat(notelist.get(1).getMidiNumber(), equalTo(Pitch.E3));
        assertThat(notelist.get(2).getMidiNumber(), equalTo(Pitch.G3));
        assertThat(notelist.get(3).getMidiNumber(), equalTo(Pitch.C4));
        assertThat(notelist.get(4).getMidiNumber(), equalTo(Pitch.E4));
        assertThat(notelist.get(5).getMidiNumber(), equalTo(Pitch.G4));
        assertThat(notelist.get(6).getMidiNumber(), equalTo(Pitch.C5));
        assertThat(notelist.get(7).getMidiNumber(), equalTo(Pitch.E5));
        assertThat(notelist.get(8).getMidiNumber(), equalTo(Pitch.G5));

        assertThat(notelist.get(9).getMidiNumber(), equalTo(Pitch.C6));
        assertThat(notelist.get(10).getMidiNumber(), equalTo(Pitch.E6));
        assertThat(notelist.get(11).getMidiNumber(), equalTo(Pitch.G6));

    }

}
