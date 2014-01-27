/**
 * 
 */
package com.rockhoppertech.music;

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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.chord.ChordProgressionTest;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class PitchFormatTest {
    private static final Logger logger = LoggerFactory
            .getLogger(PitchFormatTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.PitchFormat#midiNumberToString(int)}.
     */
    @Test
    public void testMidiNumberToStringInt() {
        for (int p = Pitch.MIN; p < Pitch.MAX; p++) {
            Pitch pitch = PitchFactory.getPitch(p);
            assertNotNull(pitch);
            assertEquals(p, pitch.getMidiNumber());
            String ps = PitchFormat.midiNumberToString(p);
            assertNotNull(p);
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.PitchFormat#midiNumberToString(int, boolean, int, int)}
     * .
     */
    @Test
    @Ignore
    public void testMidiNumberToStringIntBooleanIntInt() {
        fail("Not yet implemented"); // TODO

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.PitchFormat#stringToMidiNumber(java.lang.String)}
     * .
     */
    @Test
    public void testStringToMidiNumber() {
        int mn = PitchFormat.stringToMidiNumber("C0");


        for (int p = Pitch.MIN; p < Pitch.MAX; p++) {
            logger.debug("testing {}", p);
            Pitch pitch = PitchFactory.getPitch(p);
            String ps = PitchFormat.getInstance().format(pitch);
            logger.debug("pitchstring '{}' for p {}", ps, p);
            assertThat(
                    "ps is not null",
                    ps,
                    notNullValue());
            mn = PitchFormat.stringToMidiNumber(ps);
            logger.debug("stringToMidiNumber result {}", mn);
            assertThat(
                    "p = pitch midi number",
                    p,
                    equalTo(pitch.getMidiNumber()));
            assertThat("p = nm", p, equalTo(mn));

        }
        // int i = PitchFormat.stringToMidiNumber("Cb0");

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.PitchFormat#stringToPC(java.lang.String)}
     * .
     */
    @Test
    public void testStringToPC() {

        int pc = 0;

        pc = PitchFormat.stringToPC("C");
        assertThat("pc is correct", pc, equalTo(0));

        pc = PitchFormat.stringToPC("C#");
        assertThat("pc is correct", pc, equalTo(1));

        pc = PitchFormat.stringToPC("Db");
        assertThat("pc is correct", pc, equalTo(1));

        pc = PitchFormat.stringToPC("D");
        assertThat("pc is correct", pc, equalTo(2));

        pc = PitchFormat.stringToPC("D#");
        assertThat("pc is correct", pc, equalTo(3));

        pc = PitchFormat.stringToPC("Eb");
        assertThat("pc is correct", pc, equalTo(3));

        pc = PitchFormat.stringToPC("E");
        assertThat("pc is correct", pc, equalTo(4));

        pc = PitchFormat.stringToPC("F");
        assertThat("pc is correct", pc, equalTo(5));

        pc = PitchFormat.stringToPC("F#");
        assertThat("pc is correct", pc, equalTo(6));

        pc = PitchFormat.stringToPC("Gb");
        assertThat("pc is correct", pc, equalTo(6));

        pc = PitchFormat.stringToPC("G");
        assertThat("pc is correct", pc, equalTo(7));

        pc = PitchFormat.stringToPC("G#");
        assertThat("pc is correct", pc, equalTo(8));

        pc = PitchFormat.stringToPC("Ab");
        assertThat("pc is correct", pc, equalTo(8));

        pc = PitchFormat.stringToPC("A");
        assertThat("pc is correct", pc, equalTo(9));

        pc = PitchFormat.stringToPC("A#");
        assertThat("pc is correct", pc, equalTo(10));

        pc = PitchFormat.stringToPC("Bb");
        assertThat("pc is correct", pc, equalTo(10));

        pc = PitchFormat.stringToPC("B");
        assertThat("pc is correct", pc, equalTo(11));

        // pc = PitchFormat.stringToPC("Cb");
        // assertThat("pc is correct", pc, equalTo(11));

        pc = PitchFormat.stringToPC("C");
        assertThat("pc is correct", pc, equalTo(0));

        for (int octave = 1; octave < 9; octave++) {
            pc = PitchFormat.stringToPC("C" + octave);
            assertThat("pc is correct", pc, equalTo(0));

            pc = PitchFormat.stringToPC("C#" + octave);
            assertThat("pc is correct", pc, equalTo(1));

            pc = PitchFormat.stringToPC("Db" + octave);
            assertThat("pc is correct", pc, equalTo(1));

            pc = PitchFormat.stringToPC("D" + octave);
            assertThat("pc is correct", pc, equalTo(2));

            pc = PitchFormat.stringToPC("D#" + octave);
            assertThat("pc is correct", pc, equalTo(3));

            pc = PitchFormat.stringToPC("Eb" + octave);
            assertThat("pc is correct", pc, equalTo(3));

            pc = PitchFormat.stringToPC("E" + octave);
            assertThat("pc is correct", pc, equalTo(4));

            pc = PitchFormat.stringToPC("F" + octave);
            assertThat("pc is correct", pc, equalTo(5));

            pc = PitchFormat.stringToPC("F#" + octave);
            assertThat("pc is correct", pc, equalTo(6));

            pc = PitchFormat.stringToPC("Gb" + octave);
            assertThat("pc is correct", pc, equalTo(6));

            pc = PitchFormat.stringToPC("G" + octave);
            assertThat("pc is correct", pc, equalTo(7));

            pc = PitchFormat.stringToPC("G#" + octave);
            assertThat("pc is correct", pc, equalTo(8));

            pc = PitchFormat.stringToPC("Ab" + octave);
            assertThat("pc is correct", pc, equalTo(8));

            pc = PitchFormat.stringToPC("A" + octave);
            assertThat("pc is correct", pc, equalTo(9));

            pc = PitchFormat.stringToPC("A#" + octave);
            assertThat("pc is correct", pc, equalTo(10));

            pc = PitchFormat.stringToPC("Bb" + octave);
            assertThat("pc is correct", pc, equalTo(10));

            pc = PitchFormat.stringToPC("B" + octave);
            assertThat("pc is correct", pc, equalTo(11));
        }

        // pc = PitchFormat.stringToPC("Cb" + octave);
        // assertThat("pc is correct", pc, equalTo(11));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.PitchFormat#isAccidental(int)}.
     */
    @Test
    @Ignore
    public void testIsAccidental() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.PitchFormat#format(com.rockhoppertech.music.Pitch)}
     * .
     */
    @Test
    @Ignore
    public void testFormat() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.PitchFormat#getPitchString(com.rockhoppertech.music.Pitch)}
     * .
     */
    @Test
    @Ignore
    public void testGetPitchStringPitch() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.PitchFormat#getPitchString(int)}.
     */
    @Test
    @Ignore
    public void testGetPitchStringInt() {
        fail("Not yet implemented"); // TODO
    }

}
