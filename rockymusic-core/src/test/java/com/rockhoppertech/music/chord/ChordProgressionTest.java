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


/**
 * 
 */

import org.junit.Ignore;
import org.junit.Test;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
//import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ChordProgressionTest {

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#createFromNames(java.lang.String)}
     * .
     */
    @Test
    public void testCreateFromNames() {
        String input = "C G7 | Dm G7 | C";
        Chord chord = null;
        ChordProgression chordProgression = null;
        try {
            chordProgression = ChordProgression
                    .createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        assertThat(chordProgression.getLength(), equalTo(5));
        chord = chordProgression.get(0);
        assertNotNull(chord);
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#createFromRoman(java.lang.String)}
     * .
     */
    @Test
    public void testCreateFromRoman() {
        String input = "I V7 | ii V7 | I";
        ChordProgression chordProgression = null;
        Chord chord = null;
        chordProgression = ChordProgression.createFromRoman(input);
        assertNotNull(chordProgression);
        assertThat(chordProgression.getLength(), equalTo(5));

        chord = chordProgression.get(0);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(2d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(2d));
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        chord = chordProgression.get(2);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(2d));
        assertThat(chord.getRoot(), equalTo(Pitch.D5));
        assertThat(chord.getSymbol(), equalTo("m"));

        Scale scale = ScaleFactory.getScaleByName("Major");
        String key = "D";
        RomanChordParser.setDefaultScaleAndKey(scale, key);
        chordProgression = ChordProgression.createFromRoman(input);
        assertNotNull(chordProgression);
        assertThat(chordProgression.getLength(), equalTo(5));

        chord = chordProgression.get(0);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(2d));
        assertThat(chord.getRoot(), equalTo(Pitch.D5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(2d));
        assertThat(chord.getRoot(), equalTo(Pitch.A5));
        assertThat(chord.getSymbol(), equalTo("7"));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#add(com.rockhoppertech.music.chord.Chord)}
     * .
     */
    @Test
    @Ignore
    public void testAdd() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#getLength()}.
     */
    @Test
    @Ignore
    public void testGetLength() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#get(int)}.
     */
    @Test
    @Ignore
    public void testGet() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#set(int, com.rockhoppertech.music.chord.Chord)}
     * .
     */
    @Test
    @Ignore
    public void testSet() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#remove(int)}.
     */
    @Test
    @Ignore
    public void testRemoveInt() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#remove(com.rockhoppertech.music.chord.Chord)}
     * .
     */
    @Test
    public void removeChord() {
        String input = "C G7 | Dm G7 | C";
        Chord chord = null;
        ChordProgression chordProgression = null;
        try {
            chordProgression = ChordProgression
                    .createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        assertThat(chordProgression.getLength(), equalTo(5));
        chord = chordProgression.get(0);
        assertNotNull(chord);
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        boolean ok = chordProgression.remove(chord);
        assertThat(ok, equalTo(true));
        assertThat(chordProgression.getLength(), equalTo(4));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getRoot(), equalTo(Pitch.D5));
        assertThat(chord.getSymbol(), equalTo("m"));

        // System.err.println(chordProgression.getMIDINoteList());

    }

    @Test
    public void closeTimeLine() {
        String input = "C G7 | Dm G7 | C";
        Chord chord = null;
        ChordProgression chordProgression = null;
        try {
            chordProgression = ChordProgression
                    .createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        assertThat(chordProgression.getLength(), equalTo(5));
        chord = chordProgression.get(0);
        assertNotNull(chord);
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        boolean ok = chordProgression.remove(chord);
        assertThat(ok, equalTo(true));
        assertThat(chordProgression.getLength(), equalTo(4));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        System.out.println(chord);
        assertThat(chord.getRoot(), equalTo(Pitch.D5));
        assertThat(chord.getSymbol(), equalTo("m"));
        //assertThat(chord.getStartBeat(), equalTo(5d));
        assertThat(chord.getDuration(), equalTo(2d));

        double total = chordProgression.closeTimeline();
        System.err.println(total);
        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getRoot(), equalTo(Pitch.D5));
        assertThat(chord.getSymbol(), equalTo("m"));
        assertThat(chord.getStartBeat(), equalTo(3d));
        assertThat(chord.getDuration(), equalTo(2d));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#iterator()}.
     */
    @Test
    @Ignore
    public void testIterator() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#sortByStartBeat()}
     * .
     */
    @Test
    @Ignore
    public void testSortByStartBeat() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#getMIDINoteList()}
     * .
     */
    @Test
    public void shouldGetMIDITrack() {

        String input = "C G7 | Dm G7 | C";
        Chord chord = null;
        ChordProgression chordProgression = null;
        try {
            chordProgression = ChordProgression
                    .createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }

        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        MIDITrack track = chordProgression.createMIDITrack();
        // chordProgression.sortByStartBeat();

        assertThat("track is not null", track, notNullValue());

        // assertThat("pitch is correct", note.getMidiNumber(),
        // equalTo(expectedPitch));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#getChordAtBeat(double)}
     * .
     */
    @Test
    @Ignore
    public void testGetChordAtBeat() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#insertAtIndex(int, com.rockhoppertech.music.chord.ChordProgression)}
     * .
     */
    @Test
    @Ignore
    public void testInsertAtIndex() {
        fail("Not yet implemented");
    }

    @Test
    public void toHTML() {
        String input = null;

        input = "C / / / | C / / / | G7 / / / | C / / /|dm7 g7 | em7 am7 | D7 g7b5 | Cmaj7";
        ChordProgression chordProgression = null;
        try {
            chordProgression = ChordProgression
                    .createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        String progString = chordProgression.toHTML();
        assertNotNull(progString);
        System.out.println(progString);

    }

    @Test
    public void toStdRows() {
        String input = null;

        input = "C / / / | C / / / | G7 / / / | C / / /|dm7 g7 | em7 am7 | D7 g7b5 | Cmaj7| G7 / / / | C / / /|dm7 g7 | em7 am7 | D7 g7b5 | Cmaj7";
        ChordProgression chordProgression = null;
        try {
            chordProgression = ChordProgression
                    .createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        String progString = chordProgression.toStdRows(4);
        assertNotNull(progString);
        System.out.println(progString);

    }

    @Test
    public void toStdBoolean() {
        String input = null;

        input = "C / / / | C / / / | G7 / / / | C / / /";
        ChordProgression chordProgression = null;
        try {
            chordProgression = ChordProgression
                    .createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        String progString = chordProgression.toStd(false);
        assertNotNull(progString);
        System.out.println(progString);
        // assertThat(progString,
        // equalTo("C / / / | C / / / | G7 / / / | C / / /"));

        System.out.println(chordProgression.getLength());

        chordProgression = new ChordProgression();
        chordProgression.setBeatsPerMeasure(4);
        try {
            Chord chord = ChordFactory.getChordByFullSymbol("C");
            chord.setDuration(8d);
            chordProgression.add(chord);
            chord = ChordFactory.getChordByFullSymbol("G7");
            chord.setDuration(4d);
            chordProgression.add(chord);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        progString = chordProgression.toStd(false);
        assertNotNull(progString);
        System.out.println(progString);
        assertThat(progString, equalTo("C / / / | C / / / | G7 / / / |"));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#asString(com.rockhoppertech.music.chord.ChordProgression, int)}
     * .
     */
    @Test
    public void asString() {
        String input = null;

        input = "C G7 | C";
        ChordProgression chordProgression = null;
        try {
            chordProgression = ChordProgression
                    .createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        String progString = ChordProgression.asString(chordProgression, 4);
        assertNotNull(progString);

        assertThat("as string is equal", progString,
                equalTo("key:C\nbeatsPerMeasure:4\nC / G7 / |C / / /"));

        input = "C / / F | Dm G7 / / |C";
        try {
            chordProgression = ChordProgression.createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        progString = ChordProgression.asString(chordProgression, 4);
        assertNotNull(progString);
        assertThat(
                "as string is equal",
                progString,
                equalTo("key:C\nbeatsPerMeasure:4\nC / / F |Dm G7 / / |C / / /"));
    }

    @Test
    public void create() {
        String input = null;

        input = "C G7 | C";
        ChordProgression chordProgression = null;
        try {
            chordProgression = ChordProgression.create(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);

        input = "I V7 | I";
        try {
            chordProgression = ChordProgression.create(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#toRoman(com.rockhoppertech.music.chord.ChordProgression, java.lang.String, com.rockhoppertech.music.Scale)}
     * .
     */
    @Test
    public void testToRoman() {
        Scale scale = ScaleFactory.getScaleByName("Major");
        String input = null;

        input = "C G7 | C";
        ChordProgression chordProgression = null;
        try {
            chordProgression = ChordProgression
                    .createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        String roman = ChordProgression
                .toRoman(chordProgression, "C", scale, 4);
        assertNotNull(roman);
        System.out.println(roman);
        assertThat(roman, equalTo("Imaj / V7 / |Imaj / / /"));

        input = "C F | Dm G7 |C";
        try {
            chordProgression = ChordProgression.createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        roman = ChordProgression.toRoman(chordProgression, "C", scale, 4);
        assertNotNull(roman);
        System.out.println(roman);
        assertThat(roman, equalTo("Imaj / IVmaj / |IIm / V7 / |Imaj / / /"));

        roman = ChordProgression.toRoman(chordProgression, "D", scale, 4);
        assertNotNull(roman);
        System.out.println(roman);
        assertThat(roman, equalTo("VImaj / #IImaj / |Im / IV7 / |VImaj / / /"));

        input = "C G7 | Dm G7 | C C# | d ef| e f | f# g";

        input = "C / / F | Dm G7 / / |C";
        try {
            chordProgression = ChordProgression.createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        roman = ChordProgression.toRoman(chordProgression, "C", scale, 4);
        assertNotNull(roman);
        System.out.println(roman);
        assertThat(roman, equalTo("Imaj / / IVmaj |IIm V7 / / |Imaj / / /"));
    }

    @Test
    public void slashes() {
        String input = null;
        Chord chord = null;
        ChordProgression chordProgression = null;

        input = "C / / G7 | C";
        try {
            chordProgression = ChordProgression.createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        assertThat(chordProgression.getLength(), equalTo(3));
        chord = chordProgression.get(0);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(3d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(1d));
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        chord = chordProgression.get(2);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(4d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        input = "C / G7 / | C";
        try {
            chordProgression = ChordProgression.createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        assertThat(chordProgression.getLength(), equalTo(3));
        chord = chordProgression.get(0);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(2d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(2d));
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        chord = chordProgression.get(2);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(4d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        input = "C G7 / / | C";
        try {
            chordProgression = ChordProgression.createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertNotNull(chordProgression);
        assertThat(chordProgression.getLength(), equalTo(3));
        chord = chordProgression.get(0);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(1d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(3d));
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        chord = chordProgression.get(2);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(4d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        input = "I / / V7 | I";
        chordProgression = ChordProgression.createFromRoman(input);
        assertNotNull(chordProgression);
        assertThat(chordProgression.getLength(), equalTo(3));
        chord = chordProgression.get(0);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(3d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(1d));
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        chord = chordProgression.get(2);
        assertNotNull(chord);
        assertThat(chord.getDuration(), equalTo(4d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));
    }

}
