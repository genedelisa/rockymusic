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
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;


//import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
//import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

//import static org.junit.matchers.JUnitMatchers.*;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class RomanChordParserTest {
    private static Logger logger = LoggerFactory
            .getLogger(RomanChordParserTest.class);

    @Test
    public void isRoman() {
        assertThat(RomanChordParser.isRoman("I"), equalTo(true));
        assertThat(RomanChordParser.isRoman("II"), equalTo(true));
        assertThat(RomanChordParser.isRoman("III"), equalTo(true));
        assertThat(RomanChordParser.isRoman("IV"), equalTo(true));
        assertThat(RomanChordParser.isRoman("V"), equalTo(true));
        assertThat(RomanChordParser.isRoman("VI"), equalTo(true));
        assertThat(RomanChordParser.isRoman("VII"), equalTo(true));
        assertThat(RomanChordParser.isRoman("VIII"), equalTo(true));
        assertThat(RomanChordParser.isRoman("i"), equalTo(true));
        assertThat(RomanChordParser.isRoman("ii"), equalTo(true));
        assertThat(RomanChordParser.isRoman("iii"), equalTo(true));
        assertThat(RomanChordParser.isRoman("iv"), equalTo(true));
        assertThat(RomanChordParser.isRoman("v"), equalTo(true));
        assertThat(RomanChordParser.isRoman("vi"), equalTo(true));
        assertThat(RomanChordParser.isRoman("vii"), equalTo(true));
        assertThat(RomanChordParser.isRoman("viii"), equalTo(true));
        assertThat(RomanChordParser.isRoman("#I"), equalTo(true));
        assertThat(RomanChordParser.isRoman("#II"), equalTo(true));
        assertThat(RomanChordParser.isRoman("#III"), equalTo(true));
        assertThat(RomanChordParser.isRoman("#IV"), equalTo(true));
        assertThat(RomanChordParser.isRoman("#V"), equalTo(true));
        assertThat(RomanChordParser.isRoman("#VI"), equalTo(true));
        assertThat(RomanChordParser.isRoman("#VII"), equalTo(true));
        assertThat(RomanChordParser.isRoman("#VIII"), equalTo(true));
        assertThat(RomanChordParser.isRoman("bI"), equalTo(true));
        assertThat(RomanChordParser.isRoman("bII"), equalTo(true));
        assertThat(RomanChordParser.isRoman("bIII"), equalTo(true));
        assertThat(RomanChordParser.isRoman("bIV"), equalTo(true));
        assertThat(RomanChordParser.isRoman("bV"), equalTo(true));
        assertThat(RomanChordParser.isRoman("bVI"), equalTo(true));
        assertThat(RomanChordParser.isRoman("bVII"), equalTo(true));
        assertThat(RomanChordParser.isRoman("bVIII"), equalTo(true));
        assertThat(RomanChordParser.isRoman("I7"), equalTo(true));
        assertThat(RomanChordParser.isRoman("IImaj7"), equalTo(true));

        assertThat(RomanChordParser.isRoman("c"), equalTo(false));
        assertThat(RomanChordParser.isRoman("d"), equalTo(false));
        assertThat(RomanChordParser.isRoman("e"), equalTo(false));
        assertThat(RomanChordParser.isRoman("f"), equalTo(false));
        assertThat(RomanChordParser.isRoman("g"), equalTo(false));
        assertThat(RomanChordParser.isRoman("a"), equalTo(false));
        assertThat(RomanChordParser.isRoman("C"), equalTo(false));
        assertThat(RomanChordParser.isRoman("D"), equalTo(false));
        assertThat(RomanChordParser.isRoman("E"), equalTo(false));
        assertThat(RomanChordParser.isRoman("F"), equalTo(false));
        assertThat(RomanChordParser.isRoman("G"), equalTo(false));
        assertThat(RomanChordParser.isRoman("A"), equalTo(false));

        assertThat(RomanChordParser.isRoman("cs"), equalTo(false));
        assertThat(RomanChordParser.isRoman("ds"), equalTo(false));
        assertThat(RomanChordParser.isRoman("fs"), equalTo(false));
        assertThat(RomanChordParser.isRoman("gs"), equalTo(false));
        assertThat(RomanChordParser.isRoman("as"), equalTo(false));

        assertThat(RomanChordParser.isRoman("cf"), equalTo(false));
        assertThat(RomanChordParser.isRoman("df"), equalTo(false));
        assertThat(RomanChordParser.isRoman("ef"), equalTo(false));
        assertThat(RomanChordParser.isRoman("gf"), equalTo(false));
        assertThat(RomanChordParser.isRoman("af"), equalTo(false));

        assertThat(RomanChordParser.isRoman("c#"), equalTo(false));
        assertThat(RomanChordParser.isRoman("d#"), equalTo(false));
        assertThat(RomanChordParser.isRoman("e#"), equalTo(false));
        assertThat(RomanChordParser.isRoman("f#"), equalTo(false));
        assertThat(RomanChordParser.isRoman("g#"), equalTo(false));
        assertThat(RomanChordParser.isRoman("a#"), equalTo(false));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.RomanChordParser#getChord(java.lang.String)}
     * .
     */
    @Test
    public void getChordString() {
        // this method assumes C Major
        // you can change it with this:
        // RomanChordParser.setDefaultScaleAndKey(ScaleFactory.getScaleByName("Major"),
        // "C");

        Chord c = RomanChordParser.getChord("i");
        assertThat("The i chord is not null.", c, notNullValue());
        assertThat("the i chord is major", c.isMajor(), equalTo(true));

        c = RomanChordParser.getChord("ii");
        assertThat("The ii chord is not null.", c, notNullValue());
        assertThat("the ii chord is minor", c.isMinor(), equalTo(true));

        c = RomanChordParser.getChord("iii");
        assertThat("The iii chord is not null.", c, notNullValue());
        assertThat("the iii chord is minor", c.isMinor(), equalTo(true));

        c = RomanChordParser.getChord("iv");
        assertThat("The iv chord is not null.", c, notNullValue());
        assertThat("the iv chord is major", c.isMajor(), equalTo(true));

        c = RomanChordParser.getChord("v");
        assertThat("The v chord is not null.", c, notNullValue());
        assertThat("the v chord is major", c.isMajor(), equalTo(true));

        c = RomanChordParser.getChord("vi");
        assertThat("The vi chord is not null.", c, notNullValue());
        assertThat("the vi chord is minor", c.isMinor(), equalTo(true));

        c = RomanChordParser.getChord("vii");
        assertThat("The vii chord is not null.", c, notNullValue());
        assertThat(
                "the vii chord is diminished",
                c.isDiminished(),
                equalTo(true));
    }

    @Test @Ignore
    public void shouldGetImaj7() {

        // Imaj7 #Idim7 | IIm7 #IIdim7 | IIIm7 VI7
        String roman = "Imaj7";
        Chord c = RomanChordParser.getChord(roman);
        assertThat("The Imaj7 chord is not null.", c, notNullValue());

        String s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);

        assertThat(c.isMajor(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(true));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.C0));
        assertThat(c.getThird(), equalTo(Pitch.E0));
        assertThat(c.getFifth(), equalTo(Pitch.G0));
        assertThat(c.getSeventh(), equalTo(Pitch.B0));
        assertThat(c.getSymbol(), equalTo("maj7"));
    }
    @Test @Ignore
    public void shouldGetIdim7() {
        String roman = "#Idim7";
        Chord c = RomanChordParser.getChord(roman);
        assertNotNull(c);
        String s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);
        assertNotNull(c);
        assertThat(c.isDiminished(), equalTo(true));
        assertThat(c.isMajor(), equalTo(false));
        assertThat(c.hasSeventh(), equalTo(true));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.DF0));
        assertThat(c.getThird(), equalTo(4));
        assertThat(c.getFifth(), equalTo(7));
        assertThat(c.getSeventh(), equalTo(10));
        assertThat(c.getSymbol(), equalTo("dim7"));
    }
    @Test
    public void shouldGetIIm7() {
        String roman = "IIm7";
        Chord c = RomanChordParser.getChord(roman);
        assertNotNull(c);
        String s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);

        roman = "#IIdim7";
        c = RomanChordParser.getChord(roman);
        assertNotNull(c);
        s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);

        roman = "IIIm7";
        c = RomanChordParser.getChord(roman);
        assertNotNull(c);
        s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);

        roman = "VI7";
        c = RomanChordParser.getChord(roman);
        assertNotNull(c);
        s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);

        // assertEquals(7, intervals[1]);
        // assertThat(new int[] { 4, 7 }, equalTo(intervals));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.chord.RomanChordParser#getChord(java.lang.String,java.lang.String)}
     * .
     */
    @Test
    public void getChordStringString() {
        // C Major
        String roman = "i";
        Chord c = RomanChordParser.getChord("C", roman);
        assertNotNull(c);
        assertThat(c.isMajor(), equalTo(true));
        String s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);

        roman = "ii";
        c = RomanChordParser.getChord("C", roman);
        s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);

        roman = "i";
        c = RomanChordParser.getChord("D", roman);
        assertThat(c.getRoot(), equalTo(Pitch.D0));
        assertThat(c.isMajor(), equalTo(true));
        s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);

        roman = "V";
        c = RomanChordParser.getChord("D", roman);
        assertThat(c.getRoot(), equalTo(Pitch.A0));
        assertThat(c.isMajor(), equalTo(true));
        s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);

        roman = "i";
        c = RomanChordParser.getChord("Bb", roman);
        assertThat(c.getRoot(), equalTo(Pitch.BF0));
        assertThat(c.isMajor(), equalTo(true));

        s = String.format("Roman='%s' dn='%s' verbose='%s'", roman, c
                .getDisplayName(), c);
        System.out.println(s);

    }

    /**
     * This method is called by the others. Concentrate tests here.
     * 
     * Test method for
     * {@link com.rockhoppertech.music.chord.RomanChordParser#getChord(java.lang.String,com.rockhoppertech.music.Scale,java.lang.String)}
     * .
     */
    @Test
    public void getChordStringScaleString() {
        Scale scale = ScaleFactory.getScaleByName("Major");
        String roman = "i";
        Chord c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMinor(), equalTo(false));
        assertThat(c.hasSeventh(), equalTo(false));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.C0));
        assertThat(c.getSymbol(), equalTo("maj"));

        roman = "imaj7";
        c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMajor(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(true));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.C0));
        assertThat(c.getThird(), equalTo(Pitch.E0));
        assertThat(c.getFifth(), equalTo(Pitch.G0));
        assertThat(c.getSeventh(), equalTo(Pitch.B0));
        assertThat(c.getSymbol(), equalTo("maj7"));

        roman = "ii";
        c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMinor(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(false));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.D0));
        assertThat(c.getSymbol(), equalTo("m"));

        // plain ol dii7 yields a dominant 7. i.e. it ignores the case of the
        // rn.
        roman = "iim7";
        c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMajor(), equalTo(false));
        assertThat(c.hasSeventh(), equalTo(true));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.D0));
        assertThat(c.getThird(), equalTo(Pitch.F0));
        assertThat(c.getFifth(), equalTo(Pitch.A0));
        assertThat(c.getSeventh(), equalTo(Pitch.C1));
        assertThat(c.getSymbol(), equalTo("m7"));

        roman = "iii";
        c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMinor(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(false));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.E0));
        assertThat(c.getSymbol(), equalTo("m"));

        roman = "IV";
        c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMinor(), equalTo(false));
        assertThat(c.hasSeventh(), equalTo(false));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.F0));
        assertThat(c.getSymbol(), equalTo("maj"));

        roman = "V";
        c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMinor(), equalTo(false));
        assertThat(c.hasSeventh(), equalTo(false));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.G0));
        assertThat(c.getSymbol(), equalTo("maj"));

        roman = "V7";
        c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMinor(), equalTo(false));
        assertThat(c.isDominant(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(true));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.G0));
        assertThat(c.getThird(), equalTo(Pitch.B0));
        assertThat(c.getFifth(), equalTo(Pitch.D1));
        assertThat(c.getSeventh(), equalTo(Pitch.F1));
        assertThat(c.getSymbol(), equalTo("7"));

        roman = "V7b5";
        c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMinor(), equalTo(false));
        assertThat(c.isMajor(), equalTo(true));
        assertThat(c.isDominant(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(true));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.G0));
        assertThat(c.getThird(), equalTo(Pitch.B0));
        assertThat(c.getFifth(), equalTo(Pitch.DF1));
        assertThat(c.getSeventh(), equalTo(Pitch.F1));
        assertThat(c.getSymbol(), equalTo("7b5"));

        roman = "vi";
        c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMinor(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(false));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.A0));
        assertThat(c.getSymbol(), equalTo("m"));

        roman = "vii";
        c = RomanChordParser.getChord("C", scale, roman);
        assertNotNull(c);
        assertThat(c.isMinor(), equalTo(true));
        assertThat(c.isDiminished(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(false));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.B0));
        assertThat(c.getSymbol(), equalTo("dim"));
    }

    /**
     * 
     * Test method for
     * {@link com.rockhoppertech.music.chord.RomanChordParser#getChord(int,java.lang.String,java.lang.String)}
     * .
     */
    @Test
    public void getChordIntStringString() {
        // C minor
        String roman = "i";
        // the octave is ignored
        Chord c = RomanChordParser.getChord(Pitch.C5, "Harmonic Minor", roman);
        assertNotNull(c);
        assertThat(c.isMinor(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(false));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(Pitch.C0));
        assertThat(c.getSymbol(), equalTo("m"));

        roman = "imaj7";
        c = RomanChordParser.getChord(Pitch.C0, "Major", roman);
        assertNotNull(c);
        assertThat(c.isMajor(), equalTo(true));
        assertThat(c.hasSeventh(), equalTo(true));
        assertThat(c.hasNinth(), equalTo(false));
        assertThat(c.hasEleventh(), equalTo(false));
        assertThat(c.hasThirteenth(), equalTo(false));
        assertThat(c.getRoot(), equalTo(0));
        assertThat(c.getThird(), equalTo(4));
        assertThat(c.getFifth(), equalTo(7));
        assertThat(c.getSeventh(), equalTo(11));
        assertThat(c.getSymbol(), equalTo("maj7"));
    }

    @Test
    public void romanToPitch() {
        Pitch result = null;
        String key = "C";
        Scale scale = ScaleFactory.createFromName("Major");
        String romanString = "#I";
        result = RomanChordParser.romanToPitch(scale, key, romanString);
        assertThat(result.getSynonymsForPitchClass(), hasItem("Db"));
    }

    @Test
    public void romanToPitchName() {
        String result = null;
        String key = "C";
        Scale scale = ScaleFactory.createFromName("Major");
        String romanString = "I";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("C"));

        romanString = "#I";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("Db"));

        romanString = "II";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("D"));

        romanString = "#II";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("Eb"));

        romanString = "III";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("E"));

        romanString = "IV";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("F"));

        romanString = "#IV";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("Gb"));

        romanString = "V";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("G"));

        romanString = "#V";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("Ab"));

        romanString = "VI";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("A"));

        romanString = "#VI";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("Bb"));

        romanString = "VII";
        result = RomanChordParser.romanToPitchName(scale, key, romanString);
        assertThat(result, equalTo("B"));
    }

    @Test
    public void pitchNameToRoman() {
        String result = null;
        Scale scale = ScaleFactory.createFromName("Major");

        String key = "C";
        result = RomanChordParser.pitchNameToRoman(scale, "C", key);
        assertThat(result, equalTo("I"));
        result = RomanChordParser.pitchNameToRoman(scale, "C#", key);
        assertThat(result, equalTo("#I"));
        result = RomanChordParser.pitchNameToRoman(scale, "Db", key);
        assertThat(result, equalTo("#I"));
        result = RomanChordParser.pitchNameToRoman(scale, "D", key);
        assertThat(result, equalTo("II"));
        result = RomanChordParser.pitchNameToRoman(scale, "D#", key);
        assertThat(result, equalTo("#II"));
        result = RomanChordParser.pitchNameToRoman(scale, "Eb", key);
        assertThat(result, equalTo("#II"));
        result = RomanChordParser.pitchNameToRoman(scale, "E", key);
        assertThat(result, equalTo("III"));
        result = RomanChordParser.pitchNameToRoman(scale, "F", key);
        assertThat(result, equalTo("IV"));
        result = RomanChordParser.pitchNameToRoman(scale, "F#", key);
        assertThat(result, equalTo("#IV"));
        result = RomanChordParser.pitchNameToRoman(scale, "Gb", key);
        assertThat(result, equalTo("#IV"));
        result = RomanChordParser.pitchNameToRoman(scale, "G", key);
        assertThat(result, equalTo("V"));
        result = RomanChordParser.pitchNameToRoman(scale, "G#", key);
        assertThat(result, equalTo("#V"));
        result = RomanChordParser.pitchNameToRoman(scale, "Ab", key);
        assertThat(result, equalTo("#V"));
        result = RomanChordParser.pitchNameToRoman(scale, "A", key);
        assertThat(result, equalTo("VI"));
        result = RomanChordParser.pitchNameToRoman(scale, "A#", key);
        assertThat(result, equalTo("#VI"));
        result = RomanChordParser.pitchNameToRoman(scale, "Bb", key);
        assertThat(result, equalTo("#VI"));
        result = RomanChordParser.pitchNameToRoman(scale, "B", key);
        assertThat(result, equalTo("VII"));

        key = "Eb";
        result = RomanChordParser.pitchNameToRoman(scale, "C", key);
        assertThat(result, equalTo("VI"));
        result = RomanChordParser.pitchNameToRoman(scale, "C#", key);
        assertThat(result, equalTo("#VI"));
        result = RomanChordParser.pitchNameToRoman(scale, "D", key);
        assertThat(result, equalTo("VII"));
        result = RomanChordParser.pitchNameToRoman(scale, "D#", key);
        assertThat(result, equalTo("I"));
        result = RomanChordParser.pitchNameToRoman(scale, "E", key);
        assertThat(result, equalTo("#I"));
        result = RomanChordParser.pitchNameToRoman(scale, "F", key);
        assertThat(result, equalTo("II"));
        result = RomanChordParser.pitchNameToRoman(scale, "F#", key);
        assertThat(result, equalTo("#II"));
        result = RomanChordParser.pitchNameToRoman(scale, "G", key);
        assertThat(result, equalTo("III"));
        result = RomanChordParser.pitchNameToRoman(scale, "G#", key);
        assertThat(result, equalTo("IV"));
        result = RomanChordParser.pitchNameToRoman(scale, "A", key);
        assertThat(result, equalTo("#IV"));
        result = RomanChordParser.pitchNameToRoman(scale, "A#", key);
        assertThat(result, equalTo("V"));
        result = RomanChordParser.pitchNameToRoman(scale, "B", key);
        assertThat(result, equalTo("#V"));

        scale = ScaleFactory.createFromName("Harmonic Minor");
        key = "C";
        result = RomanChordParser.pitchNameToRoman(scale, "C", key);
        assertThat(result, equalTo("I"));
        result = RomanChordParser.pitchNameToRoman(scale, "C#", key);
        assertThat(result, equalTo("#I"));
        result = RomanChordParser.pitchNameToRoman(scale, "Db", key);
        assertThat(result, equalTo("#I"));
        result = RomanChordParser.pitchNameToRoman(scale, "D", key);
        assertThat(result, equalTo("II"));
        result = RomanChordParser.pitchNameToRoman(scale, "D#", key);
        assertThat(result, equalTo("III"));
        result = RomanChordParser.pitchNameToRoman(scale, "Eb", key);
        assertThat(result, equalTo("III"));
        result = RomanChordParser.pitchNameToRoman(scale, "E", key);
        assertThat(result, equalTo("#III"));
        result = RomanChordParser.pitchNameToRoman(scale, "F", key);
        assertThat(result, equalTo("IV"));
        result = RomanChordParser.pitchNameToRoman(scale, "F#", key);
        assertThat(result, equalTo("#IV"));
        result = RomanChordParser.pitchNameToRoman(scale, "Gb", key);
        assertThat(result, equalTo("#IV"));
        result = RomanChordParser.pitchNameToRoman(scale, "G", key);
        assertThat(result, equalTo("V"));
        result = RomanChordParser.pitchNameToRoman(scale, "G#", key);
        assertThat(result, equalTo("VI"));
        result = RomanChordParser.pitchNameToRoman(scale, "Ab", key);
        assertThat(result, equalTo("VI"));
        result = RomanChordParser.pitchNameToRoman(scale, "A", key);
        assertThat(result, equalTo("#VI"));
        result = RomanChordParser.pitchNameToRoman(scale, "A#", key);
        // well, what else would you call it?
        assertThat(result, equalTo("##VI"));
        result = RomanChordParser.pitchNameToRoman(scale, "Bb", key);
        assertThat(result, equalTo("##VI"));
        result = RomanChordParser.pitchNameToRoman(scale, "B", key);
        assertThat(result, equalTo("VII"));
    }

    @Test
    public void romanToArabic() {
        assertThat(RomanChordParser.romanToArabic("I"), equalTo(1));
        assertThat(RomanChordParser.romanToArabic("II"), equalTo(2));
        assertThat(RomanChordParser.romanToArabic("III"), equalTo(3));
        assertThat(RomanChordParser.romanToArabic("IV"), equalTo(4));
        assertThat(RomanChordParser.romanToArabic("V"), equalTo(5));
        assertThat(RomanChordParser.romanToArabic("VI"), equalTo(6));
        assertThat(RomanChordParser.romanToArabic("VII"), equalTo(7));
        assertThat(RomanChordParser.romanToArabic("VIII"), equalTo(8));
        assertThat(RomanChordParser.romanToArabic("IX"), equalTo(9));
        assertThat(RomanChordParser.romanToArabic("X"), equalTo(10));
        assertThat(RomanChordParser.romanToArabic("XI"), equalTo(11));
        assertThat(RomanChordParser.romanToArabic("XL"), equalTo(40));
        assertThat(RomanChordParser.romanToArabic("C"), equalTo(100));
        assertThat(RomanChordParser.romanToArabic("MDCCLXXVI"), equalTo(1776));
    }

    @Test
    public void arabicToRoman() {
        assertThat(RomanChordParser.arabicToRoman(1), equalTo("I"));
        assertThat(RomanChordParser.arabicToRoman(2), equalTo("II"));
        assertThat(RomanChordParser.arabicToRoman(3), equalTo("III"));
        assertThat(RomanChordParser.arabicToRoman(4), equalTo("IV"));
        assertThat(RomanChordParser.arabicToRoman(5), equalTo("V"));
        assertThat(RomanChordParser.arabicToRoman(6), equalTo("VI"));
        assertThat(RomanChordParser.arabicToRoman(7), equalTo("VII"));
        assertThat(RomanChordParser.arabicToRoman(8), equalTo("VIII"));
        assertThat(RomanChordParser.arabicToRoman(9), equalTo("IX"));
        assertThat(RomanChordParser.arabicToRoman(10), equalTo("X"));
        assertThat(RomanChordParser.arabicToRoman(11), equalTo("XI"));

    }
}
