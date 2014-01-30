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


import java.util.List;
import java.util.Set;


import org.junit.Test;

//import static org.hamcrest.CoreMatchers.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.*;
//import static org.junit.matchers.JUnitMatchers.hasItem;

import static com.rockhoppertech.music.Pitch.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class PitchTest {
    private static final Logger logger = LoggerFactory
            .getLogger(PitchTest.class);
    // private Logger logger = LoggerFactory.getLogger(PitchTest.class);

    /**
     * Test method for {@link com.rockhoppertech.music.Pitch#Pitch()}.
     */
    @Test
    public void testPitch() {
        Pitch p = new Pitch();
        assertNotNull(p);
        assertEquals(p.getMidiNumber(),
                     0);
    }
    
    @Test
    public void isSharp() {
        boolean b = Pitch.isSharp("Cs");
        assertThat("should be sharp",
                   b,
                   equalTo(true));
        b = Pitch.isSharp("C#");
        assertThat("should be sharp",
                   b,
                   equalTo(true));
        b = Pitch.isSharp("Cx");
        assertThat("should be sharp",
                   b,
                   equalTo(true));
        
        b = Pitch.isSharp("C");
        assertThat("should not be sharp",
                   b,
                   equalTo(false));
        b = Pitch.isSharp("C1");
        assertThat("should not be sharp",
                   b,
                   equalTo(false));
        b = Pitch.isSharp("Cf");
        assertThat("should not be sharp",
                   b,
                   equalTo(false));
        b = Pitch.isSharp("Cb");
        assertThat("should not be sharp",
                   b,
                   equalTo(false));
    }
    
    @Test
    public void isFlat() {
        boolean b = Pitch.isFlat("C-");
        assertThat("should be flat",
                   b,
                   equalTo(true));
        
        b = Pitch.isFlat("Cb");
        assertThat("should be flat",
                   b,
                   equalTo(true));
        b = Pitch.isFlat("Cf");
        assertThat("should be flat",
                   b,
                   equalTo(true));
        
        b = Pitch.isFlat("C");
        assertThat("should not be flat",
                   b,
                   equalTo(false));
        b = Pitch.isFlat("C1");
        assertThat("should not be flat",
                   b,
                   equalTo(false));
        b = Pitch.isFlat("Cs");
        assertThat("should not be flat",
                   b,
                   equalTo(false));
        b = Pitch.isFlat("C#");
        assertThat("should not be flat",
                   b,
                   equalTo(false));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Pitch#Pitch(int)}.
     */
    @Test
    public void testPitchInt() {
        Pitch p = new Pitch(60);
        assertNotNull(p);
        assertEquals(p.getMidiNumber(),
                     60);
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Pitch#Pitch(java.lang.String)}.
     */
    @Test
    public void testPitchString() {
        Pitch p = new Pitch("C5");
        assertNotNull(p);
        assertEquals(p.getMidiNumber(),
                     60);
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Pitch#getFrequency()}.
     */
    @Test
    public void testGetFrequency() {
        Pitch p = new Pitch(69);

        // logger.debug("fq is {}",p.getFrequency());
        assertNotNull(p);
        assertEquals(p.getFrequency(),
                     440d,
                     0d);
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Pitch#transpose(int)}.
     */
    @Test
    public void testTranspose() {
        Pitch p = new Pitch(60);
        assertNotNull(p);
        assertEquals(60,
                     p.getMidiNumber());
        p = p.transpose(1);
        assertEquals(61,
                     p.getMidiNumber());
        p = p.transpose(Pitch.MAJOR_SEVENTH);
        assertEquals(72,
                     p.getMidiNumber());

    }

    /**
     * Test method for {@link com.rockhoppertech.music.Pitch#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        Pitch p = new Pitch(60);
        assertNotNull(p);
        Pitch p2 = new Pitch(60);
        assertNotNull(p2);
        assertEquals(p,
                     p2);
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Pitch#isAccidental(int)}.
     */
    @Test
    public void testIsAccidental() {
        assertFalse(Pitch.isAccidental(60));
        assertFalse(Pitch.isAccidental(62));
        assertFalse(Pitch.isAccidental(64));
        assertFalse(Pitch.isAccidental(65));
        assertFalse(Pitch.isAccidental(67));
        assertFalse(Pitch.isAccidental(69));
        assertFalse(Pitch.isAccidental(71));
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Pitch#midiFq(double, double)}.
     */
    @Test
    public void testMidiFqDoubleDouble() {
        double fq = Pitch.midiFq(9d,
                                 5);
        // logger.debug("fq is {}",fq);
        assertEquals(fq,
                     440d,
                     0d);
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Pitch#midiFq(int)}.
     */
    @Test
    public void testMidiFqInt() {
        int midiNumber = 60;
        double fq = Pitch.midiFq(midiNumber);
        // logger.debug("fq is {}",fq);
        assertEquals(fq,
                     261.625,
                     .01);
    }

    /**
     * Test method for {@link com.rockhoppertech.music.Pitch#compareTo(java.lang.Object)}.
     */
    @Test
    public void testCompareTo() {
        Pitch p = new Pitch(60);
        Pitch p2 = new Pitch(61);
        assertTrue(p.compareTo(p2) < 0);
        assertTrue(p2.compareTo(p) > 0);
        p2 = new Pitch(60);
        assertTrue(p2.compareTo(p) == 0);
    }

    @Test
    public void getSynonymsForPitchClass() {
        Pitch p = PitchFactory.getPitch("Db");
        assertNotNull(p);
        for (String s : p.getSynonymsForPitchClass()) {
            logger.debug(s);
        }
        Set<String> syns = p.getSynonymsForPitchClass();

        // assertThat(syns, anyOf(hasItem("color"), hasItem("colour")));

        assertThat(syns,
                   hasItem("Db"));
        assertThat(syns,
                   hasItem("Df"));
        assertThat(syns,
                   hasItem("Cs"));
        assertThat(syns,
                   hasItem("C#"));
    }

    @Test
    public void getSynonyms() {
        Pitch p = PitchFactory.getPitch("Db");
        assertThat("pitch is not null",
                   p,
                   notNullValue());
        for (String s : p.getSynonyms()) {
            logger.debug(s);
        }
        Set<String> syns = p.getSynonyms();
        int expectedSize = 11;
        assertThat("syns are not null",
                   syns,
                   notNullValue());
        assertThat("syns are the right size",
                   syns.size(),
                   equalTo(expectedSize));
        assertThat("C#5 is a possible name",
                   syns,
                   hasItem("C#5"));
        assertThat("CS5 is a possible name",
                   syns,
                   hasItem("CS5"));
        assertThat("CS5 is a possible name",
                   syns,
                   hasItem("Cs5"));
        assertThat("DB5 is a possible name",
                   syns,
                   hasItem("DB5"));
        assertThat("DF5 is a possible name",
                   syns,
                   hasItem("DF5"));
        assertThat("Db5 is a possible name",
                   syns,
                   hasItem("Db5"));
        assertThat("Df5 is a possible name",
                   syns,
                   hasItem("Df5"));
        assertThat("c#5 is a possible name",
                   syns,
                   hasItem("c#5"));
        assertThat("cs5 is a possible name",
                   syns,
                   hasItem("cs5"));
        assertThat("db5 is a possible name",
                   syns,
                   hasItem("db5"));
        assertThat("df5 is a possible name",
                   syns,
                   hasItem("df5"));
    }

    @Test
    public void staticGetSynonyms() {
        Set<String> syns = Pitch.getSynonyms(CS5);
        int expectedSize = 11;
        assertThat("syns are not null",
                   syns,
                   notNullValue());
        assertThat("syns are the right size",
                   syns.size(),
                   equalTo(expectedSize));
        assertThat("C#5 is a possible name",
                   syns,
                   hasItem("C#5"));
        assertThat("CS5 is a possible name",
                   syns,
                   hasItem("CS5"));
        assertThat("CS5 is a possible name",
                   syns,
                   hasItem("Cs5"));
        assertThat("DB5 is a possible name",
                   syns,
                   hasItem("DB5"));
        assertThat("DF5 is a possible name",
                   syns,
                   hasItem("DF5"));
        assertThat("Db5 is a possible name",
                   syns,
                   hasItem("Db5"));
        assertThat("Df5 is a possible name",
                   syns,
                   hasItem("Df5"));
        assertThat("c#5 is a possible name",
                   syns,
                   hasItem("c#5"));
        assertThat("cs5 is a possible name",
                   syns,
                   hasItem("cs5"));
        assertThat("db5 is a possible name",
                   syns,
                   hasItem("db5"));
        assertThat("df5 is a possible name",
                   syns,
                   hasItem("df5"));

        for (String name : syns) {
            logger.debug(name);
        }

        expectedSize = 2;
        syns = Pitch.getSynonyms(C5);
        assertThat("syns are not null",
                   syns,
                   notNullValue());
        assertThat("syns are the right size",
                   syns.size(),
                   equalTo(expectedSize));
        assertThat("C5 is a possible name",
                   syns,
                   hasItem("C5"));
        assertThat("C5 is a possible name",
                   syns,
                   hasItem("c5"));
        for (String name : syns) {
            logger.debug(name);
        }

    }

    @Test
    public void foo() {

        for (int i = Pitch.C0; i < Pitch.C1; i += Interval.MINOR_SECOND) {
            Pitch p = PitchFactory.getPitch(i);
            logger.debug("Pitch {}",p);
        }
    }
}
