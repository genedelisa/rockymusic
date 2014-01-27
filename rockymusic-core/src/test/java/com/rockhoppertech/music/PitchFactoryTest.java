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


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.*;

/**
 *
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class PitchFactoryTest {

    /**
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.rockhoppertech.music.PitchFactory#getPitch(int)}.
     */
    @Test
    public void testGetPitchInt() {
        for (int p = Pitch.MIN; p < Pitch.MAX; p++) {
            Pitch pitch = PitchFactory.getPitch(p);
            assertNotNull(pitch);
            assertEquals(p,
                         pitch.getMidiNumber());
        }
    }

    /**
     * Test method for {@link com.rockhoppertech.music.PitchFactory#getPitch(java.lang.String)}.
     */
    @Test
    public void testGetPitchString() {
        for (int p = Pitch.MIN; p < Pitch.MAX; p++) {
            Pitch pitch = PitchFactory.getPitch(PitchFormat
                    .midiNumberToString(p));
            assertNotNull(pitch);
            assertEquals(p,
                         pitch.getMidiNumber());
        }
    }

    /**
     * the names are in Pitch nameMap
     */
    @Test
    public void getPitchByName() {
        Pitch pitch = PitchFactory.getPitchByName("CS");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(1));

        int oct = 0;
        pitch = PitchFactory.getPitchByName("C");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 0));

        pitch = PitchFactory.getPitchByName("CS");
        Pitch reference = pitch;
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 1));

        pitch = PitchFactory.getPitchByName("Cs");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 1));
        assertThat("the pitch is the same instance",
                   pitch,
                   is(reference));

        pitch = PitchFactory.getPitchByName("C#");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 1));
        assertThat("the pitch is the same instance",
                   pitch,
                   is(reference));

        pitch = PitchFactory.getPitchByName("DF");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 1));
        assertThat("the pitch is the same instance",
                   pitch,
                   is(reference));

        pitch = PitchFactory.getPitchByName("Df");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 1));
        assertThat("the pitch is the same instance",
                   pitch,
                   is(reference));

        pitch = PitchFactory.getPitchByName("Db");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 1));
        assertThat("the pitch is the same instance",
                   pitch,
                   is(reference));

        pitch = PitchFactory.getPitchByName("DB");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 1));
        assertThat("the pitch is the same instance",
                   pitch,
                   is(reference));

        // ///
        pitch = PitchFactory.getPitchByName("D");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 2));

        pitch = PitchFactory.getPitchByName("Ds");
        reference = pitch;
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 3));

        pitch = PitchFactory.getPitchByName("Ef");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 3));
        assertThat("the pitch is the same instance",
                   pitch,
                   is(reference));

        //
        pitch = PitchFactory.getPitchByName("E");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 4));

        pitch = PitchFactory.getPitchByName("F");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 5));

        pitch = PitchFactory.getPitchByName("F#");
        reference = pitch;
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 6));

        pitch = PitchFactory.getPitchByName("Gf");
        reference = pitch;
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 6));
        assertThat("the pitch is the same instance",
                   pitch,
                   is(reference));

        pitch = PitchFactory.getPitchByName("G");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 7));

        pitch = PitchFactory.getPitchByName("Gs");
        reference = pitch;
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 8));

        pitch = PitchFactory.getPitchByName("Af");
        reference = pitch;
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 8));
        assertThat("the pitch is the same instance",
                   pitch,
                   is(reference));

        pitch = PitchFactory.getPitchByName("A");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 9));

        pitch = PitchFactory.getPitchByName("Bf");
        reference = pitch;
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 10));

        pitch = PitchFactory.getPitchByName("As");
        System.err.println(pitch);
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 10));
        assertThat("the pitch is the same instance",
                   pitch,
                   is(reference));

        pitch = PitchFactory.getPitchByName("B");
        assertThat("pitch is not null",
                   pitch,
                   notNullValue());
        assertThat("the MIDI number is correct",
                   pitch.getMidiNumber(),
                   equalTo(oct + 11));
    }

    @Test
    public void getAllPitchByName() {
        Pitch pitch = null;

        for (int oct = 0; oct < 10; oct++) {
            pitch = PitchFactory.getPitchByName("C" + oct);
            System.err.println(pitch);
            int expectedPitch = (oct * 12) + 0;
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            expectedPitch = (oct * 12) + 1;
            pitch = PitchFactory.getPitchByName("CS" + oct);
            Pitch reference = pitch;
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            pitch = PitchFactory.getPitchByName("Cs" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
            assertThat("the pitch is the same instance",
                       pitch,
                       is(reference));

            pitch = PitchFactory.getPitchByName("C#" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
            assertThat("the pitch is the same instance",
                       pitch,
                       is(reference));

            pitch = PitchFactory.getPitchByName("DF" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
            assertThat("the pitch is the same instance",
                       pitch,
                       is(reference));

            pitch = PitchFactory.getPitchByName("Df" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
            assertThat("the pitch is the same instance",
                       pitch,
                       is(reference));

            pitch = PitchFactory.getPitchByName("Db" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
            assertThat("the pitch is the same instance",
                       pitch,
                       is(reference));

            pitch = PitchFactory.getPitchByName("DB" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
            assertThat("the pitch is the same instance",
                       pitch,
                       is(reference));

            // ///
            expectedPitch = (oct * 12) + 2;
            pitch = PitchFactory.getPitchByName("D" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            expectedPitch = (oct * 12) + 3;
            pitch = PitchFactory.getPitchByName("Ds" + oct);
            reference = pitch;
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            pitch = PitchFactory.getPitchByName("Ef" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
            assertThat("the pitch is the same instance",
                       pitch,
                       is(reference));

            //
            expectedPitch = (oct * 12) + 4;
            pitch = PitchFactory.getPitchByName("E" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            expectedPitch = (oct * 12) + 5;
            pitch = PitchFactory.getPitchByName("F" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            expectedPitch = (oct * 12) + 6;
            pitch = PitchFactory.getPitchByName("F#" + oct);
            reference = pitch;
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            pitch = PitchFactory.getPitchByName("Gf" + oct);
            reference = pitch;
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
            assertThat("the pitch is the same instance",
                       pitch,
                       is(reference));

            expectedPitch = (oct * 12) + 7;
            pitch = PitchFactory.getPitchByName("G" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            expectedPitch = (oct * 12) + 8;
            pitch = PitchFactory.getPitchByName("Gs" + oct);
            reference = pitch;
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            pitch = PitchFactory.getPitchByName("Af" + oct);
            reference = pitch;
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
            assertThat("the pitch is the same instance",
                       pitch,
                       is(reference));

            expectedPitch = (oct * 12) + 9;
            pitch = PitchFactory.getPitchByName("A" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            expectedPitch = (oct * 12) + 10;
            pitch = PitchFactory.getPitchByName("Bf" + oct);
            reference = pitch;
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));

            pitch = PitchFactory.getPitchByName("As" + oct);
            System.err.println(pitch);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
            assertThat("the pitch is the same instance",
                       pitch,
                       is(reference));

            expectedPitch = (oct * 12) + 11;
            pitch = PitchFactory.getPitchByName("B" + oct);
            assertThat("pitch is not null",
                       pitch,
                       notNullValue());
            assertThat(String.format("the MIDI number is correct %d",
                                     expectedPitch),
                       expectedPitch,
                       equalTo(pitch.getMidiNumber()));
        }
    }

    @Test
    public void createFromFrequency() {
        for (double fq : PitchFactory.EQUAL_TEMPERAMENT.values()) {
            System.err.println(fq);
        }

        double freq = 261.6255653005986;
        Pitch p = PitchFactory.createFromFrequency(freq);
        Pitch p2 = PitchFactory.createFromFrequency(freq);
        System.err.println(p.toProlixString());
        assertThat("The pitches are the same",
                   p,
                   sameInstance(p2));
        assertThat("The pitch is not null",
                   p,
                   notNullValue());
        assertThat("The fq is correct",
                   p.getFrequency(),
                   equalTo(freq));
        assertThat("The midi number is correct",
                   p.getMidiNumber(),
                   equalTo(60));
        assertThat("The pitch is middle c",
                   p,
                   equalTo(new Pitch(Pitch.C5)));
        assertThat("The cents is correct",
                   p.getCents(),
                   equalTo((short) 0));
        assertThat("The pitchbend is correct",
                   p.getPitchBend(),
                   equalTo((short) 0));

        freq = 261;
        p = PitchFactory.createFromFrequency(freq);
        p2 = PitchFactory.createFromFrequency(freq);
        assertThat("The pitches are the same",
                   p,
                   sameInstance(p2));
        System.err.println(p.toProlixString());
        assertThat("The pitch is not null",
                   p,
                   notNullValue());
        assertThat("The fq is correct",
                   p.getFrequency(),
                   equalTo(freq));
        assertThat("The midi number is correct",
                   p.getMidiNumber(),
                   equalTo(60));
        assertThat("The cents is correct",
                   p.getCents(),
                   equalTo((short) -4));
        assertThat("The pitchbend is correct",
                   p.getPitchBend(),
                   equalTo((short) 8188));

    }

}