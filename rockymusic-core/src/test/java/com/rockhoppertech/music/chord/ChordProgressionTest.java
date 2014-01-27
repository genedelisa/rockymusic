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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ChordProgressionTest {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordProgressionTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#createFromNames(java.lang.String)}
     * .
     */
    @Test
    public void testCreateFromNames() {
        // two beats each in m 1
        String input = "C G7 | Dm G7 | C";
        Chord chord = null;
        ChordProgression chordProgression = ChordProgression
                .createFromNames(input);
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat(
                "progression length is correct",
                chordProgression.getLength(),
                equalTo(5));

        chord = chordProgression.get(0);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(2d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(1d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("maj"));

        chord = chordProgression.get(1);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.G5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(2d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(3d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("7"));

        chord = chordProgression.get(2);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.D5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(2d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(5d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("m"));

        chord = chordProgression.get(3);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.G5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(2d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(7d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("7"));

        chord = chordProgression.get(4);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(4d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(9d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("maj"));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.ChordProgression#createFromRoman(java.lang.String)}
     * .
     */
    @Test
    public void testCreateFromRoman() {
        String input = "I V7 | II V7 | I";
        ChordProgression chordProgression = null;
        Chord chord = null;
        // C Major is the default
        chordProgression = ChordProgression.createFromRoman(input);
        assertThat("chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat("chordProgression length is correct",
                chordProgression.getLength(),
                equalTo(5));

        chord = chordProgression.get(0);
        assertThat("chord is not null", chord, notNullValue());
        assertThat("duration is correct", chord.getDuration(), equalTo(2d));
        assertThat("root is correct", chord.getRoot(), equalTo(Pitch.C5));
        assertThat("symbol is correct", chord.getSymbol(), equalTo("maj"));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(1d));

        chord = chordProgression.get(1);
        assertThat("chord is not null", chord, notNullValue());
        assertThat("duration is correct", chord.getDuration(), equalTo(2d));
        assertThat("root is correct", chord.getRoot(), equalTo(Pitch.G5));
        assertThat("symbol is correct", chord.getSymbol(), equalTo("7"));
        assertThat("chord start beat is correct",
                chord.getStartBeat(),
                equalTo(3d));

        chord = chordProgression.get(2);
        assertThat("chord is not null", chord, notNullValue());
        assertThat(chord.getDuration(), equalTo(2d));
        assertThat("root is correct", chord.getRoot(), equalTo(Pitch.D5));
        assertThat("symbol is correct", chord.getSymbol(), equalTo("m"));
        assertThat("chord start beat is correct",
                chord.getStartBeat(),
                equalTo(5d));

        chordProgression = ChordProgression
                .createFromRoman(input, "Major", "D");
        assertThat("chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat("chordProgression length correct",
                chordProgression.getLength(),
                equalTo(5));

        chord = chordProgression.get(0);
        assertThat("chord is not null", chord, notNullValue());
        assertThat("duration is correct", chord.getDuration(), equalTo(2d));
        assertThat("root is correct", chord.getRoot(), equalTo(Pitch.D5));
        assertThat("symbol is correct", chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertThat("chord is not null",
                chord,
                notNullValue());
        assertThat("duration is correct", chord.getDuration(), equalTo(2d));
        assertThat("root is correct", chord.getRoot(), equalTo(Pitch.A5));
        assertThat("symbol is correct", chord.getSymbol(), equalTo("7"));

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
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat(chordProgression.getLength(), equalTo(5));
        chord = chordProgression.get(0);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        boolean ok = chordProgression.remove(chord);
        assertThat(ok, equalTo(true));
        assertThat(chordProgression.getLength(), equalTo(4));

        chord = chordProgression.get(1);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
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
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat(chordProgression.getLength(), equalTo(5));
        chord = chordProgression.get(0);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        boolean ok = chordProgression.remove(chord);
        assertThat(ok, equalTo(true));
        assertThat(chordProgression.getLength(), equalTo(4));

        chord = chordProgression.get(1);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        System.out.println(chord);
        assertThat(chord.getRoot(), equalTo(Pitch.D5));
        assertThat(chord.getSymbol(), equalTo("m"));
        // assertThat(chord.getStartBeat(), equalTo(5d));
        assertThat(chord.getDuration(), equalTo(2d));

        double total = chordProgression.closeTimeline();
        System.err.println(total);
        chord = chordProgression.get(1);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
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
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        String progString = chordProgression.toHTML();

        assertThat(
                "progString is not null",
                progString,
                notNullValue());
        logger.debug(progString);

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
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        String progString = chordProgression.toStdRows(4);
        assertThat(
                "progString is not null",
                progString,
                notNullValue());
        logger.debug(progString);

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
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        String progString = chordProgression.toStd(false);
        assertThat(
                "progString is not null",
                progString,
                notNullValue());
        logger.debug(progString);
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
        assertThat(
                "progString is not null",
                progString,
                notNullValue());
        logger.debug(progString);
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
        ChordProgression chordProgression = ChordProgression
                .createFromNames(input);
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        String progString = ChordProgression.asString(chordProgression, 4);
        assertThat(
                "progString is not null",
                progString,
                notNullValue());

        assertThat("as string is equal", progString,
                equalTo("key:C\nbeatsPerMeasure:4\nC / G7 / |C / / /"));

        input = "C / / F | Dm G7 / / |C";
        chordProgression = ChordProgression.createFromNames(input);
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());

        progString = ChordProgression.asString(chordProgression, 4);
        assertThat(
                "progString is not null",
                progString,
                notNullValue());
        assertThat(
                "as string is equal",
                progString,
                equalTo("key:C\nbeatsPerMeasure:4\nC / / F |Dm G7 / / |C / / /"));
    }

    @Test
    public void create() {
        String input = null;

        input = "C G7 | C";
        ChordProgression chordProgression = ChordProgression.create(input);
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());

        input = "I V7 | I";
        chordProgression = ChordProgression.create(input);
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
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
        chordProgression = ChordProgression
                .createFromNames(input);
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat(
                "progression length is correct",
                chordProgression.getLength(),
                equalTo(3));

        String roman = ChordProgression
                .toRoman(chordProgression, "C", scale, 4);
        assertThat(
                "roman is not null",
                roman,
                notNullValue());
        logger.debug(roman);
        assertThat(roman, equalTo("Imaj / V7 / |Imaj / / /"));

        input = "C F | Dm G7 |C";
        chordProgression = ChordProgression.createFromNames(input);

        roman = ChordProgression.toRoman(chordProgression, "C", scale, 4);
        assertThat(
                "roman is not null",
                roman,
                notNullValue());
        logger.debug(roman);
        assertThat(roman, equalTo("Imaj / IVmaj / |IIm / V7 / |Imaj / / /"));

        roman = ChordProgression.toRoman(chordProgression, "D", scale, 4);
        assertThat(
                "roman is not null",
                roman,
                notNullValue());
        logger.debug(roman);
        assertThat(
                "roman contents are correct",
                roman,
                equalTo("VImaj / #IImaj / |Im / IV7 / |VImaj / / /"));

        // input = "C G7 | Dm G7 | C C# | d ef| e f | f# g";

        input = "C / / F | Dm G7 / / |C";
        chordProgression = ChordProgression.createFromNames(input);
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat(
                "progression length is correct",
                chordProgression.getLength(),
                equalTo(5));

        roman = ChordProgression.toRoman(chordProgression, "D", scale, 4);
        assertThat(
                "roman is not null",
                roman,
                notNullValue());
        logger.debug(roman);
        assertThat("the chords string is correct", roman,
                equalTo("VImaj / / #IImaj |Im IV7 / / |VImaj / / /"));
    }

    @Test
    public void shouldGetChordAtBeat() {
        String input = "I V7";
        ChordProgression chordProgression = ChordProgression
                .createFromRoman(input);
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());

        chordProgression.append(chordProgression);

        assertThat(
                "chordProgression length is correct",
                chordProgression.getLength(),
                equalTo(4));

        double expectedStart = 1d;
        Chord chord = chordProgression.getChordAtBeat(expectedStart);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(expectedStart));

        expectedStart++;
        chord = chordProgression.getChordAtBeat(expectedStart);
        assertThat(
                "chord at beat 2 is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root at beat 2 is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));

        expectedStart++;
        chord = chordProgression.getChordAtBeat(expectedStart);
        assertThat(
                "chord at beat 3 is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root at beat 3 is correct",
                chord.getRoot(),
                equalTo(Pitch.G5));

        expectedStart++;
        chord = chordProgression.getChordAtBeat(expectedStart);
        assertThat(
                "chord at beat 4 is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root at beat 4 is correct",
                chord.getRoot(),
                equalTo(Pitch.G5));
    }

    @Test
    public void append() {

        // RomanChordParser.setDefaultScaleAndKey(
        // ScaleFactory.getScaleByName("Major"),
        // "C");
        String input = "I V7";
        ChordProgression chordProgression = ChordProgression
                .createFromRoman(input, "Major", "C");
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());

        chordProgression.append(chordProgression);

        assertThat(
                "chordProgression length is correct",
                chordProgression.getLength(),
                equalTo(4));

        String progString = chordProgression.toStd(false);
        assertThat(
                "std string is not null",
                progString,
                notNullValue());
        assertThat(
                "the std string is correct",
                progString,
                equalTo("C / G7 / | C / G7 / |"));

        logger.debug(chordProgression.toChordStrings());

        Chord chord = chordProgression.getChordAtBeat(1d);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));

        chord = chordProgression.getChordAtBeat(2d);
        assertThat(
                "chord at beat 2 is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root at beat 2 is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));

        chord = chordProgression.getChordAtBeat(3d);
        assertThat(
                "chord at beat 3 is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root at beat 3 is correct",
                chord.getRoot(),
                equalTo(Pitch.G5));
        assertThat(
                "chord symbol at beat 3 is correct",
                chord.getSymbol(),
                equalTo("7"));

        chord = chordProgression.getChordAtBeat(5d);
        assertThat(
                "chord at beat 5 is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root at beat 5 is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));

        chord = chordProgression.getChordAtBeat(6d);
        assertThat(
                "chord at beat 6 is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root at beat 6 is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));

        chord = chordProgression.getChordAtBeat(7d);
        assertThat(
                "chord at beat 7 is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root at beat 7 is correct",
                chord.getRoot(),
                equalTo(Pitch.G5));

        // chordProgression.append(chordProgression).append(chordProgression);

    }

    @Test
    public void slashes() {
        String input = null;
        Chord chord = null;
        ChordProgression chordProgression = null;

        input = "C / / G7 | C";
        chordProgression = ChordProgression.createFromNames(input);
        logger.debug("chord progression\n{}", chordProgression.toChordStrings());
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat(
                "progression length is correct",
                chordProgression.getLength(),
                equalTo(3));

        chord = chordProgression.get(0);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(3d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(1d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("maj"));

        chord = chordProgression.get(1);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.G5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(1d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(4d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("7"));

        chord = chordProgression.get(2);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(4d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(5d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("maj"));

        input = "C / G7 / | C";
        chordProgression = ChordProgression.createFromNames(input);
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat(
                "progression length is correct",
                chordProgression.getLength(),
                equalTo(3));

        chord = chordProgression.get(0);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(2d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(1d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("maj"));

        chord = chordProgression.get(1);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(chord.getDuration(), equalTo(2d));
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        chord = chordProgression.get(2);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(chord.getDuration(), equalTo(4d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        input = "C G7 / / | C";
        try {
            chordProgression = ChordProgression.createFromNames(input);
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat(
                "progression length is correct",
                chordProgression.getLength(),
                equalTo(3));

        chord = chordProgression.get(0);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(chord.getDuration(), equalTo(1d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        chord = chordProgression.get(1);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(chord.getDuration(), equalTo(3d));
        assertThat(chord.getRoot(), equalTo(Pitch.G5));
        assertThat(chord.getSymbol(), equalTo("7"));

        chord = chordProgression.get(2);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(chord.getDuration(), equalTo(4d));
        assertThat(chord.getRoot(), equalTo(Pitch.C5));
        assertThat(chord.getSymbol(), equalTo("maj"));

        input = "I / / V7 | I";
        chordProgression = ChordProgression.createFromRoman(input);
        logger.debug("chord progression\n{}", chordProgression.toChordStrings());
        assertThat(
                "chordProgression is not null",
                chordProgression,
                notNullValue());
        assertThat(
                "progression length is correct",
                chordProgression.getLength(),
                equalTo(3));

        chord = chordProgression.get(0);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(3d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(1d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("maj"));

        chord = chordProgression.get(1);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.G5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(1d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(4d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("7"));

        chord = chordProgression.get(2);
        assertThat(
                "chord is not null",
                chord,
                notNullValue());
        assertThat(
                "chord root is correct",
                chord.getRoot(),
                equalTo(Pitch.C5));
        assertThat(
                "chord duration is correct",
                chord.getDuration(),
                equalTo(4d));
        assertThat(
                "chord start beat is correct",
                chord.getStartBeat(),
                equalTo(5d));
        assertThat(
                "chord symbol is correct",
                chord.getSymbol(),
                equalTo("maj"));

    }

}
