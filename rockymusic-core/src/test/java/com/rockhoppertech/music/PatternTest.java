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

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.chord.Chord;
import com.rockhoppertech.music.chord.ChordFactory;
import com.rockhoppertech.music.chord.UnknownChordException;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

import static com.rockhoppertech.music.Duration.*;

import static com.rockhoppertech.music.Pitch.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class PatternTest {
    private static final Logger logger = LoggerFactory
            .getLogger(PatternTest.class);

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Pattern#Pattern(int[], int[])}.
     */
    @Test
    public void patternIntArrayIntArray() {
        int[] degrees = new int[] { 0, 2, 4, 5, 7, 9, 11 };
        int[] pattern = new int[] { 0, 2 };
        Pattern nlPattern = new Pattern(degrees, pattern);
        nlPattern.setUpAndDown(true);
        MIDITrack nl = nlPattern.createTrack(1d, true);
        assertThat("notelist is not null",
                nl,
                notNullValue());

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Pattern#Pattern(int[], int[], int, int, double, boolean, double)}
     * .
     */
    @Test
    public void patternIntArrayIntArrayIntIntDoubleBooleanDouble() {
        int[] degrees = new int[] { 0, 2, 4, 5, 7, 9, 11 };
        int[] pattern = new int[] { 0, 2, 4 };
        int numOctaves = 1;
        double duration = Q;
        double restBetweenPatterns = Q;
        boolean reverse = false;
        Pattern nlPattern = new Pattern(degrees, pattern, C5, numOctaves,
                duration, reverse, restBetweenPatterns);
        nlPattern.setUpAndDown(false);
        MIDITrack nl = nlPattern.createTrack(1d, true);
        assertThat("notelist is not null",
                nl,
                notNullValue());
        logger.debug("pattern {}", nlPattern);
        logger.debug("track \n{}", nl);

        // assertThat("the length is correct", nl.get, equalTo(8));

    }

    /*
     * 
     * MIDINote[startBeat: 001.00 pitch: C5 dur: 001.00 endBeat: 002.00 midinum:
     * 60 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano)
     * pitchbend: 0 voice: 0 ] MIDINote[startBeat: 002.00 pitch: E5 dur: 001.00
     * endBeat: 003.00 midinum: 64 rest: false] chan: 0 velocity: 64 bank: 0
     * program: 0 (Piano) pitchbend: 0 voice: 0 ] MIDINote[startBeat: 004.00
     * pitch: E5 dur: 001.00 endBeat: 005.00 midinum: 64 rest: false] chan: 0
     * velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 ]
     * MIDINote[startBeat: 005.00 pitch: C5 dur: 001.00 endBeat: 006.00 midinum:
     * 60 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano)
     * pitchbend: 0 voice: 0 ] MIDINote[startBeat: 007.00 pitch: D5 dur: 001.00
     * endBeat: 008.00 midinum: 62 rest: false] chan: 0 velocity: 64 bank: 0
     * program: 0 (Piano) pitchbend: 0 voice: 0 ] MIDINote[startBeat: 008.00
     * pitch: F5 dur: 001.00 endBeat: 009.00 midinum: 65 rest: false] chan: 0
     * velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 ]
     * MIDINote[startBeat: 010.00 pitch: F5 dur: 001.00 endBeat: 011.00 midinum:
     * 65 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano)
     * pitchbend: 0 voice: 0 ] MIDINote[startBeat: 011.00 pitch: D5 dur: 001.00
     * endBeat: 012.00 midinum: 62 rest: false] chan: 0 velocity: 64 bank: 0
     * program: 0 (Piano) pitchbend: 0 voice: 0 ] MIDINote[startBeat: 013.00
     * pitch: E5 dur: 001.00 endBeat: 014.00 midinum: 64 rest: false] chan: 0
     * velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 ]
     * MIDINote[startBeat: 014.00 pitch: G5 dur: 001.00 endBeat: 015.00 midinum:
     * 67 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano)
     * pitchbend: 0 voice: 0 ] MIDINote[startBeat: 016.00 pitch: G5 dur: 001.00
     * endBeat: 017.00 midinum: 67 rest: false] chan: 0 velocity: 64 bank: 0
     * program: 0 (Piano) pitchbend: 0 voice: 0 ] MIDINote[startBeat: 017.00
     * pitch: E5 dur: 001.00 endBeat: 018.00 midinum: 64 rest: false] chan: 0
     * velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 ]
     * MIDINote[startBeat: 019.00 pitch: F5 dur: 001.00 endBeat: 020.00 midinum:
     * 65 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano)
     * pitchbend: 0 voice: 0 ] MIDINote[startBeat: 020.00 pitch: A5 dur: 001.00
     * endBeat: 021.00 midinum: 69 rest: false] chan: 0 velocity: 64 bank: 0
     * program: 0 (Piano) pitchbend: 0 voice: 0 ] MIDINote[startBeat: 022.00
     * pitch: A5 dur: 001.00 endBeat: 023.00 midinum: 69 rest: false] chan: 0
     * velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 ]
     * MIDINote[startBeat: 023.00 pitch: F5 dur: 001.00 endBeat: 024.00 midinum:
     * 65 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano)
     * pitchbend: 0 voice: 0 ] MIDINote[startBeat: 025.00 pitch: G5 dur: 001.00
     * endBeat: 026.00 midinum: 67 rest: false] chan: 0 velocity: 64 bank: 0
     * program: 0 (Piano) pitchbend: 0 voice: 0 ] MIDINote[startBeat: 026.00
     * pitch: B5 dur: 001.00 endBeat: 027.00 midinum: 71 rest: false] chan: 0
     * velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 ]
     * MIDINote[startBeat: 028.00 pitch: B5 dur: 001.00 endBeat: 029.00 midinum:
     * 71 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano)
     * pitchbend: 0 voice: 0 ] MIDINote[startBeat: 029.00 pitch: G5 dur: 001.00
     * endBeat: 030.00 midinum: 67 rest: false] chan: 0 velocity: 64 bank: 0
     * program: 0 (Piano) pitchbend: 0 voice: 0 ]
     */

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Pattern#Pattern(com.rockhoppertech.music.Scale, int[])}
     * .
     */
    @Test
    public void patternScaleIntArray() {
        Scale scale = ScaleFactory.getScaleByName("Major");
        int[] pattern = new int[] { 0, 2 };
        Pattern scalePattern = new Pattern(scale, pattern);
        scalePattern.setUpAndDown(true);
        MIDITrack nl = scalePattern.createTrack(1d, true);
        assertThat("notelist is not null",
                nl,
                notNullValue());
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Pattern#Pattern(com.rockhoppertech.music.Scale, int[], int)}
     * .
     */
    @Test
    public void patternScaleIntArrayInt() {
        Scale scale = ScaleFactory.getScaleByName("Major");
        int[] pattern = new int[] { 0, 2 };
        Pattern scalePattern = new Pattern(scale, pattern, C5);
        scalePattern.setUpAndDown(true);
        MIDITrack nl = scalePattern.createTrack(1d, true);
        assertThat("notelist is not null",
                nl,
                notNullValue());
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Pattern#Pattern(com.rockhoppertech.music.Scale, int[], int, int, double, boolean, double)}
     * .
     */
    @Test
    public void patternScaleIntArrayIntIntDoubleBooleanDouble() {
        Scale scale = ScaleFactory.getScaleByName("Major");
        logger.debug("scale \n{}", scale);
        int[] pattern = new int[] { 0, 1, 2 };
        int numOctaves = 1;
        double duration = Duration.Q;
        double restBetweenPatterns = Duration.Q;
        boolean reverse = false;
        Pattern scalePattern = new Pattern(scale, pattern, C5, numOctaves,
                duration, reverse, restBetweenPatterns);
        scalePattern.setUpAndDown(false);
        MIDITrack nl = scalePattern.createTrack(1d, true);
        assertThat("notelist is not null",
                nl,
                notNullValue());

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.Pattern#Pattern(com.rockhoppertech.music.Chord, int[], int, int, double, boolean, double)}
     * .
     * 
     * @throws UnknownChordException
     */
    @Test
    public void patternChordIntArrayIntIntDoubleBooleanDouble()
            throws UnknownChordException {
        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7+11");
        logger.debug(ArrayUtils.toString(chord.getPitchClasses()));
        int[] someInts = new int[] { 0, 1, 2 };
        int numOctaves = 3;
        double duration = Duration.Q;
        double restBetweenPatterns = 0;
        boolean reverse = false;
        Pattern pattern = new Pattern(chord, someInts, C5, numOctaves,
                duration, reverse, restBetweenPatterns);
        pattern.setUpAndDown(false);
        MIDITrack nl = pattern.createTrack(1d, true);
        assertThat("notelist is not null",
                nl,
                notNullValue());

    }

    @Test
    public void builderPatternChordIntArrayIntIntDoubleBooleanDouble()
            throws UnknownChordException {

        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7+11");
        int[] someInts = new int[] { 0, 1, 2 };

        Pattern pattern = new PatternBuilder(chord.getPitchClasses(), someInts)
                .startPitch(C5).numOctaves(3).duration(Q).reverse(false)
                .restBetweenPatterns(0).upAndDown(false).build();

        MIDITrack nl = pattern.createTrack(1d, true);
        assertThat("track is not null",
                nl,
                is(notNullValue()));

        // or even shorter using defaults
        pattern = new PatternBuilder(chord.getPitchClasses(), someInts)
                .numOctaves(3).build();

        nl = pattern.createTrack(1d, true);
        assertThat("track is not null",
                nl,
                is(notNullValue()));
        // Staffer.showTrebleStaff(nl,
        // "Pattern");
    }
}
