/**
 * 
 */

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


import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ChordFactoryTest {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordFactoryTest.class);
	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#degreesToIntervals(java.lang.String)}
	 * .
	 */
	@Test
	public void degreesToIntervals() {
		int[] intervals = ChordFactory.degreesToIntervals("1 3 5");
		assertNotNull(intervals);
		assertEquals(2, intervals.length);
		assertEquals(4, intervals[0]);
		assertEquals(7, intervals[1]);
		assertThat(new int[] { 4, 7 }, equalTo(intervals));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#add(com.rockhoppertech.music.chord.Chord)}
	 * .
	 */
	@Ignore
	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#addChord(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Ignore
	@Test
	public void testAddChord() {
		fail("Not yet implemented");
	}

	/**
	 * Maybe make this a parameterized test.
	 * 
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#createFromSymbol(java.lang.String)}
	 * .
	 */
	@Test
	public void createFromSymbol() {
		Chord chord = null;
		try {
			chord = ChordFactory.getChordByFullSymbol("C#maj7+11");
		} catch (UnknownChordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(chord);
		assertEquals(Pitch.CS0, chord.getRoot());
		assertEquals(0, chord.getInversion());
		assertEquals("maj7+11", chord.getSymbol());
		assertEquals(5, chord.getSize());
		assertEquals(5, chord.getNumberOfInversions());
		assertThat(new int[] { 4, 7, 11, 18 }, equalTo(chord.getIntervals()));

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#createFromSpelling(java.lang.String)}
	 * .
	 */
	@Test
	public void createFromSpelling() {
		Chord chord = ChordFactory.createFromSpelling("1 3 5");
		assertNotNull(chord);
		assertEquals(Pitch.C5, chord.getRoot());
		assertThat(new int[] { 4, 7 }, equalTo(chord.getIntervals()));
		assertEquals("maj", chord.getSymbol());
		assertEquals("major", chord.getDescription());

		chord = ChordFactory.createFromSpelling("1 3 5 b7 b9 11");
		assertNotNull(chord);
		chord = ChordFactory.createFromSpelling("1 b3 5 bb7 b7 11");
		assertNotNull(chord);
		chord = ChordFactory.createFromSpelling("1 3 b5 b7 #9 #11 13");
		assertNotNull(chord);
		chord = ChordFactory.createFromSpelling("1 3 5 b7 #9 #11 13");
		assertNotNull(chord);
		chord = ChordFactory.createFromSpelling("1 3 5 b7 9 11 13");
		assertNotNull(chord);
		chord = ChordFactory.createFromSpelling("1 3 b6 b7 #9");
		assertNotNull(chord);
		chord = ChordFactory.createFromSpelling("1 3 b6 b7 9");
		assertNotNull(chord);
		chord = ChordFactory.createFromSpelling("1 b3 5 b8 9 11");
		assertNotNull(chord);

		// ChordFactory.displayAll();

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#createFromDescription(java.lang.String)}
	 * .
	 */
	@Test
	public void createFromDescription() {
		Chord chord = ChordFactory
				.createFromDescription(ChordFactory.MAJOR_SEVENTH);
		assertNotNull(chord);

		chord = ChordFactory.createFromDescription(ChordFactory.AUGMENTED);
		assertNotNull(chord);

		chord = ChordFactory
				.createFromDescription(ChordFactory.ADDED_AUGMENTED_ELEVENTH);
		assertNotNull(chord);

		chord = ChordFactory.createFromDescription(ChordFactory.DIMINISHED);
		assertNotNull(chord);

		chord = ChordFactory
				.createFromDescription(ChordFactory.DIMINISHED_SEVENTH);
		assertNotNull(chord);

		chord = ChordFactory.createFromDescription(ChordFactory.ELEVENTH);
		assertNotNull(chord);

		chord = ChordFactory
				.createFromDescription(ChordFactory.ELEVENTH_AUGMENTED_NINTH);
		assertNotNull(chord);

		chord = ChordFactory
				.createFromDescription(ChordFactory.HALF_DIMINISHED);
		assertNotNull(chord);

		chord = ChordFactory.createFromDescription(ChordFactory.MAJOR);
		assertNotNull(chord);

		chord = ChordFactory.createFromDescription(ChordFactory.MINOR);
		assertNotNull(chord);

	}

	/**
	 * This method only works with registered intervals.
	 * 
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#createFromIntervals(int[])}
	 * .
	 */
	@Test
	public void testCreateFromIntervals() {
		Chord chord = null;

		// this is a major chord which is registered
		try {
			chord = ChordFactory.createFromIntervals(new int[] { 4, 7 });
		} catch (UnknownChordException e) {
			e.printStackTrace();
		}
		assertNotNull(chord);
		assertThat(new int[] { 4, 7 }, equalTo(chord.getIntervals()));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#createMIDITrack(int, java.lang.String, double, double)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackIntStringDoubleDouble() {
		double startBeat = 1d;
		double duration = .5;
		MIDITrack chord = ChordFactory.createMIDITrack(Pitch.C5, "maj7",
				startBeat, duration);
		assertNotNull(chord);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#createMIDITrack(int, java.lang.String, double, double, int, int)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackIntStringDoubleDoubleIntInt() {
		MIDITrack chord = ChordFactory.createMIDITrack(Pitch.C5, "maj7",
				1d, .5, 0, 0);
		assertNotNull(chord);

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#createMIDITrack(com.rockhoppertech.music.chord.Chord)}
	 * .
	 */
	@Test
	public void createMIDITrackMIDIChord() {
		Chord chord = ChordFactory.createFromDescription(ChordFactory.MAJOR);
		assertNotNull(chord);
		assertThat(new int[] { 4, 7 }, equalTo(chord.getIntervals()));
		assertThat(Pitch.C5, equalTo(chord.getRoot()));

		MIDITrack list = ChordFactory.createMIDITrack(chord);
		assertNotNull(list);
		assertThat(1d, equalTo(list.get(0).getStartBeat()));
		assertThat(1d, equalTo(list.get(1).getStartBeat()));
		assertThat(1d, equalTo(list.get(2).getStartBeat()));
		assertThat(Pitch.C5, equalTo(list.get(0).getMidiNumber()));
		assertThat(Pitch.E5, equalTo(list.get(1).getMidiNumber()));
		assertThat(Pitch.G5, equalTo(list.get(2).getMidiNumber()));

		System.out.println(chord);
		System.out.println(list);

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#createMIDITrack(int, com.rockhoppertech.music.chord.Chord, double, double, int, int)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackIntMIDIChordDoubleDoubleIntInt() {
		MIDITrack midinotelist;
		int drop = 0;
		int inversion = 0;
		Chord chord = ChordFactory
				.createFromDescription(ChordFactory.MAJOR_SEVENTH);
		assertNotNull(chord);

		midinotelist = ChordFactory.createMIDITrack(Pitch.C5, chord, 1d, 4d,
				inversion, drop);
		assertNotNull(midinotelist);

		drop = 1;
		midinotelist = ChordFactory.createMIDITrack(Pitch.C5, chord, 1d, 4d,
				inversion, drop);
		assertNotNull(midinotelist);

		drop = 2;
		midinotelist = ChordFactory.createMIDITrack(Pitch.C5, chord, 1d, 4d,
				inversion, drop);
		assertNotNull(midinotelist);

		drop = 3;
		midinotelist = ChordFactory.createMIDITrack(Pitch.C5, chord, 1d, 4d,
				inversion, drop);
		assertNotNull(midinotelist);

		drop = 4;
		midinotelist = ChordFactory.createMIDITrack(Pitch.C5, chord, 1d, 4d,
				inversion, drop);
		assertNotNull(midinotelist);

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#getRootFromName(java.lang.String)}
	 * .
	 */
	@Test
	public void getRootFromName() {
		assertEquals(Pitch.C0, ChordFactory.getRootFromName("c7"));
		assertEquals(Pitch.CS0, ChordFactory.getRootFromName("cs7"));
		assertEquals(Pitch.CS0, ChordFactory.getRootFromName("c#7"));
		// assertEquals(Pitch.C0, ChordFactory.getRootFromName("c+7"));
		assertEquals(Pitch.D0, ChordFactory.getRootFromName("d7"));
		assertEquals(Pitch.DF0, ChordFactory.getRootFromName("df7"));
		assertEquals(Pitch.DS0, ChordFactory.getRootFromName("ds7"));
		assertEquals(Pitch.EF0, ChordFactory.getRootFromName("eb7"));
		assertEquals(Pitch.E0, ChordFactory.getRootFromName("e7"));
		assertEquals(Pitch.F0, ChordFactory.getRootFromName("f7"));
		assertEquals(Pitch.FS0, ChordFactory.getRootFromName("f#7"));
		assertEquals(Pitch.G0, ChordFactory.getRootFromName("g7"));
		assertEquals(Pitch.AF0, ChordFactory.getRootFromName("af7"));
		assertEquals(Pitch.GS0, ChordFactory.getRootFromName("g#7"));
		assertEquals(Pitch.AF0, ChordFactory.getRootFromName("ab7"));
		assertEquals(Pitch.A0, ChordFactory.getRootFromName("a7"));
		assertEquals(Pitch.BF0, ChordFactory.getRootFromName("bb7"));
		assertEquals(Pitch.BF0, ChordFactory.getRootFromName("bf7"));
		assertEquals(Pitch.C1, ChordFactory.getRootFromName("bs7"));
		try {
			ChordFactory.getRootFromName("7");
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			logger.error(e.getLocalizedMessage(),e);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void badRootFromName() {
		ChordFactory.getRootFromName("7");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#isAccidental(java.lang.String)}
	 * .
	 */
	@Ignore
	@Test
	public void testIsAccidental() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#getSymbolNames()}.
	 */
	@Test
	public void testGetSymbolNames() {
		Set<String> symbols = ChordFactory.getSymbolNames();
		assertNotNull(symbols);
	}

	/**
	 * The string contains a root pitch.
	 * 
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#getChordByFullSymbol(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetChordByFullSymbol() {
		String s = "Cmaj7+11";
		Chord chord = null;
		try {
			chord = ChordFactory.getChordByFullSymbol(s);
		} catch (UnknownChordException e) {
			e.printStackTrace();
		}
		assertNotNull(chord);

	}

	/**
	 * The string does not contain a root pitch.
	 * 
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#getChordBySymbol(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetChordBySymbol() {
		String s = "maj7+11";
		Chord chord = null;
		try {
			chord = ChordFactory.getChordBySymbol(s);
		} catch (UnknownChordException e) {
			e.printStackTrace();
		}
		assertNotNull(chord);

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#registerChord(com.rockhoppertech.music.chord.Chord)}
	 * .
	 */
	@Ignore
	@Test
	public void testRegisterChord() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#getAll()}.
	 */
	@Test
	public void testGetAll() {
		List<Chord> list = ChordFactory.getAll();
		assertNotNull(list);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#findByNumberOfIntervals(int)}
	 * .
	 */
	@Test
	public void testFindBySize() {
		// 3 is the number of intervals, so the "size" of the chord is 1 more
		// than that.
		List<Chord> list = ChordFactory.findByNumberOfIntervals(3);
		assertNotNull(list);
		for (Chord chord : list) {
			assertThat(4, equalTo(chord.getSize()));
		}
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.chord.ChordFactory#getChordSymbols(Scale scale)}
	 * .
	 */
	@Test
	public void getChordSymbols() {
		String[] symbols = null;
		Scale scale = null;

		logger.debug("Major");
		scale = ScaleFactory.createFromName("Major");
		assertNotNull(scale);
		logger.debug("scale \n{}",scale);
		symbols = ChordFactory.getChordSymbols(scale);
		assertNotNull(symbols);
		for (String s : symbols) {
			logger.debug(s);
		}
		assertThat(symbols.length, equalTo(scale.getIntervals().length));
		assertThat(symbols[0], equalTo("maj"));
		assertThat(symbols[1], equalTo("m"));
		assertThat(symbols[2], equalTo("m"));
		assertThat(symbols[3], equalTo("maj"));
		assertThat(symbols[4], equalTo("maj"));
		assertThat(symbols[5], equalTo("m"));
		assertThat(symbols[6], equalTo("dim"));

		logger.debug("Harmonic Minor");
		scale = ScaleFactory.createFromName("Harmonic Minor");
		assertNotNull(scale);
		logger.debug("scale \n{}",scale);
		symbols = ChordFactory.getChordSymbols(scale);
		assertNotNull(symbols);
		for (String s : symbols) {
			logger.debug(s);
		}
		assertThat(symbols.length, equalTo(7));
		assertThat(symbols[0], equalTo("m"));
		assertThat(symbols[1], equalTo("dim"));
		assertThat(symbols[2], equalTo("aug"));
		assertThat(symbols[3], equalTo("m"));
		assertThat(symbols[4], equalTo("maj"));
		assertThat(symbols[5], equalTo("maj"));
		assertThat(symbols[6], equalTo("dim"));

		logger.debug("Melodic Minor");
		scale = ScaleFactory.createFromName("Melodic Minor");
		assertNotNull(scale);
		logger.debug("scale \n{}",scale);
		symbols = ChordFactory.getChordSymbols(scale);
		assertNotNull(symbols);
		for (String s : symbols) {
			logger.debug(s);
		}
		assertThat(symbols.length, equalTo(7));
		assertThat(symbols[0], equalTo("m"));
		assertThat(symbols[1], equalTo("m"));
		assertThat(symbols[2], equalTo("aug"));
		assertThat(symbols[3], equalTo("maj"));
		assertThat(symbols[4], equalTo("maj"));
		assertThat(symbols[5], equalTo("dim"));
		assertThat(symbols[6], equalTo("dim"));

		scale = ScaleFactory.createFromName("Algerian");
		assertNotNull(scale);
		logger.debug("scale \n{}",scale);
		symbols = ChordFactory.getChordSymbols(scale);
		assertNotNull(symbols);
		for (String s : symbols) {
			logger.debug(s);
		}
		assertThat(symbols.length, equalTo(scale.getIntervals().length));
		assertThat(symbols[0], equalTo("m"));
		assertThat(symbols[1], equalTo("b5"));
		assertThat(symbols[2], equalTo("aug"));
		assertThat(symbols[3], equalTo("wtf-{2,6}"));// invented chord
		assertThat(symbols[4], equalTo("maj"));
		assertThat(symbols[5], equalTo("maj"));
		assertThat(symbols[6], equalTo("m"));

	}

	/**
	 * this is a seventh chord for each degree of the scale
	 */
	@Test
	public void getSeventhChords() {
		Scale scale = null;
		Chord[] chords = null;
		scale = ScaleFactory.createFromName("Major");
		assertNotNull(scale);
		System.out.println(scale);
		chords = ChordFactory.getSeventhChords(scale);
		assertNotNull(chords);
		for (Chord c : chords) {
			logger.debug(c.getDisplayName());
		}
		assertThat(chords[0].getDisplayName(), equalTo("Cmaj7"));
		assertThat(chords[1].getDisplayName(), equalTo("Dm7"));
		assertThat(chords[2].getDisplayName(), equalTo("Em7"));
		assertThat(chords[3].getDisplayName(), equalTo("Fmaj7"));
		assertThat(chords[4].getDisplayName(), equalTo("G7"));
		assertThat(chords[5].getDisplayName(), equalTo("Am7"));
		assertThat(chords[6].getDisplayName(), equalTo("Bm7-5"));

		scale = ScaleFactory.createFromName("Harmonic Minor");
		assertNotNull(scale);
		System.out.println(scale);
		chords = ChordFactory.getSeventhChords(scale);
		assertNotNull(chords);
		for (Chord c : chords) {
			logger.debug(c.getDisplayName());
		}
		assertThat(chords[0].getDisplayName(), equalTo("Cmin/maj7"));
		assertThat(chords[1].getDisplayName(), equalTo("Dm7-5"));
		assertThat(chords[2].getDisplayName(), equalTo("Ebmaj7+5"));
		assertThat(chords[3].getDisplayName(), equalTo("Fm7"));
		assertThat(chords[4].getDisplayName(), equalTo("G7"));
		assertThat(chords[5].getDisplayName(), equalTo("Abmaj7"));
		assertThat(chords[6].getDisplayName(), equalTo("Bdim7"));

	}

	/**
	 * this is a triad for each degree of the scale
	 */
	@Test
	public void getChords() {
		Scale scale = null;
		Chord[] chords = null;
		scale = ScaleFactory.createFromName("Major");
		assertNotNull(scale);
		chords = ChordFactory.getChords(scale);
		assertNotNull(chords);
		assertThat(chords[0].getDisplayName(), equalTo("C"));
		assertThat(chords[1].getDisplayName(), equalTo("Dm"));
		assertThat(chords[2].getDisplayName(), equalTo("Em"));
		assertThat(chords[3].getDisplayName(), equalTo("F"));
		assertThat(chords[4].getDisplayName(), equalTo("G"));
		assertThat(chords[5].getDisplayName(), equalTo("Am"));
		assertThat(chords[6].getDisplayName(), equalTo("Bdim"));
	}

	@Test
	public void getAllDominants() throws UnknownChordException {
		Set<Chord> list = ChordFactory.getDominants();
		assertNotNull(list);
		print(list);
		assertThat(list, hasItem(ChordFactory.getChordBySymbol("7-5")));
		assertThat(list, not(hasItem(ChordFactory.getChordBySymbol("maj7"))));
	}

	@Test
	public void getAllSevenths() throws UnknownChordException {
		Set<Chord> list = ChordFactory.getSevenths();
		assertNotNull(list);
		print(list);
		assertThat(list, hasItem(ChordFactory.getChordBySymbol("7-5")));
		// assertThat(list,
		// not(hasItem(ChordFactory.getChordBySymbol("maj9"))));
	}

	@Test
	public void getAllNinths() throws UnknownChordException {
		Set<Chord> list = ChordFactory.getNinths();
		assertNotNull(list);
		print(list);
		assertThat(list, hasItem(ChordFactory.getChordBySymbol("9")));
		assertThat(list, not(hasItem(ChordFactory.getChordBySymbol("maj7"))));
	}

	@Test
	public void getAllElevenths() throws UnknownChordException {
		Set<Chord> list = ChordFactory.getElevenths();
		assertNotNull(list);
		print(list);
		assertThat(list, hasItem(ChordFactory.getChordBySymbol("11")));
		assertThat(list, not(hasItem(ChordFactory.getChordBySymbol("maj7"))));
	}

	@Test
	public void getAllThirteenths() throws UnknownChordException {
		Set<Chord> list = ChordFactory.getThirteenths();
		assertNotNull(list);
		print(list);
		assertThat(list, hasItem(ChordFactory.getChordBySymbol("13")));
		assertThat(list, not(hasItem(ChordFactory.getChordBySymbol("maj7"))));
	}

	@Test
	public void getAllSet() {
		Set<Chord> list = ChordFactory.getAllSet();
		assertNotNull(list);
		print(list);
		try {
			assertThat(list, hasItem(ChordFactory.getChordBySymbol("13")));
		} catch (UnknownChordException e) {
			e.printStackTrace();
		}
	}

	private void print(Set<Chord> list) {
		for (Chord chord : list) {
			System.out.println(chord.getDisplayNameWithAliases());
		}
	}

	@Test
	public void biabTypes() {
		for (String symbol : ChordFactory.biabTypeNames) {
			logger.debug("checking symbol {}",symbol);
			if (symbol.equals("?") == false && symbol.equals("") == false) {
				Chord chord = null;
				try {
					chord = ChordFactory.getChordBySymbol(symbol);
				} catch (UnknownChordException e) {
					logger.error(e.getLocalizedMessage(),e);
				}
				assertNotNull(chord);
			}
		}
	}
	@Test
	public void getChordsAllTo13() {
		Scale scale = ScaleFactory.createFromName("Major");
		assertThat("Scale is not null", scale, notNullValue());
		Chord[] chords = ChordFactory.getChords(scale, true, true, true, true);
		assertThat("Chords are not null", chords, notNullValue());
		for (Chord c : chords) {
			logger.debug(c.getDisplayName());
		}
		assertThat(chords[0].getDisplayName(), equalTo("Cmaj13"));
		assertThat(chords[1].getDisplayName(), equalTo("Dm13"));
		assertThat(chords[2].getDisplayName(), equalTo("Em7-91113"));
		assertThat(chords[3].getDisplayName(), equalTo("Fmaj13+11"));
		assertThat(chords[4].getDisplayName(), equalTo("G13"));
		assertThat(chords[5].getDisplayName(), equalTo("Am791113"));
		assertThat(chords[6].getDisplayName(), equalTo("Bdim791113"));
		Chord chord = null;
		int root = 0;
		int third = 0;
		int fifth = 0;
		int seventh = 0;
		int ninth = 0;
		int eleventh = 0;
		int thirteenth = 0;

		chord = chords[0];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		thirteenth = chord.getThirteenth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		
		assertThat("the root is correct", root, equalTo(0));
		assertThat("the third is correct", third, equalTo(4));
		assertThat("the fifth is correct", fifth, equalTo(7));
		assertThat("the seventh is correct", seventh, equalTo(11));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("C").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("E").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		assertThat("the thirteenth is correct", thirteenth, equalTo(PitchFactory
				.getPitchByName("A1").getMidiNumber()));
		
		chord = chords[1];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		thirteenth = chord.getThirteenth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(2));
		assertThat("the third is correct", third, equalTo(5));
		assertThat("the fifth is correct", fifth, equalTo(9));
		assertThat("the seventh is correct", seventh, equalTo(12));
		assertThat("the ninth is correct", ninth, equalTo(16));
		assertThat("the eleventh is correct", eleventh, equalTo(19));
		assertThat("the thirteenth is correct", thirteenth, equalTo(23));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("D").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("F").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("G1").getMidiNumber()));
		assertThat("the thirteenth is correct", thirteenth, equalTo(PitchFactory
				.getPitchByName("B1").getMidiNumber()));
		
		chord = chords[2];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		thirteenth = chord.getThirteenth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(4));
		assertThat("the third is correct", third, equalTo(7));
		assertThat("the fifth is correct", fifth, equalTo(11));
		assertThat("the seventh is correct", seventh, equalTo(14));
		assertThat("the ninth is correct", ninth, equalTo(17));
		assertThat("the eleventh is correct", eleventh, equalTo(21));
		assertThat("the thirteenth is correct", thirteenth, equalTo(24));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("E").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("A1").getMidiNumber()));
		assertThat("the thirteenth is correct", thirteenth, equalTo(PitchFactory
				.getPitchByName("C2").getMidiNumber()));
		
		chord = chords[3];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		thirteenth = chord.getThirteenth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(5));
		assertThat("the third is correct", third, equalTo(9));
		assertThat("the fifth is correct", fifth, equalTo(12));
		assertThat("the seventh is correct", seventh, equalTo(16));
		assertThat("the ninth is correct", ninth, equalTo(19));
		assertThat("the eleventh is correct", eleventh, equalTo(23));
		assertThat("the thirteenth is correct", thirteenth, equalTo(26));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("F").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("G1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("B1").getMidiNumber()));
		assertThat("the thirteenth is correct", thirteenth, equalTo(PitchFactory
				.getPitchByName("D2").getMidiNumber()));
		
		chord = chords[4];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		thirteenth = chord.getThirteenth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(7));
		assertThat("the third is correct", third, equalTo(11));
		assertThat("the fifth is correct", fifth, equalTo(14));
		assertThat("the seventh is correct", seventh, equalTo(17));
		assertThat("the ninth is correct", ninth, equalTo(21));
		assertThat("the eleventh is correct", eleventh, equalTo(24));
		assertThat("the thirteenth is correct", thirteenth, equalTo(28));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("A1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("C2").getMidiNumber()));
		assertThat("the thirteenth is correct", thirteenth, equalTo(PitchFactory
				.getPitchByName("E2").getMidiNumber()));
		
		chord = chords[5];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		thirteenth = chord.getThirteenth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(9));
		assertThat("the third is correct", third, equalTo(12));
		assertThat("the fifth is correct", fifth, equalTo(16));
		assertThat("the seventh is correct", seventh, equalTo(19));
		assertThat("the ninth is correct", ninth, equalTo(23));
		assertThat("the eleventh is correct", eleventh, equalTo(26));
		assertThat("the thirteenth is correct", thirteenth, equalTo(29));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("G1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("B1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("D2").getMidiNumber()));
		assertThat("the thirteenth is correct", thirteenth, equalTo(PitchFactory
				.getPitchByName("F2").getMidiNumber()));
		
		chord = chords[6];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		thirteenth = chord.getThirteenth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(11));
		assertThat("the third is correct", third, equalTo(14));
		assertThat("the fifth is correct", fifth, equalTo(17));
		assertThat("the seventh is correct", seventh, equalTo(21));
		assertThat("the ninth is correct", ninth, equalTo(24));
		assertThat("the eleventh is correct", eleventh, equalTo(28));
		assertThat("the thirteenth is correct", thirteenth, equalTo(31));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("A1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("C2").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("E2").getMidiNumber()));
		assertThat("the thirteenth is correct", thirteenth, equalTo(PitchFactory
				.getPitchByName("G2").getMidiNumber()));
	}
	@Test
	public void getChordsAllTo11() {
		Scale scale = ScaleFactory.createFromName("Major");
		assertThat("Scale is not null", scale, notNullValue());
		Chord[] chords = ChordFactory.getChords(scale, true, true, true, false);
		assertThat("Chords are not null", chords, notNullValue());
		for (Chord c : chords) {
			logger.debug(c.getDisplayName());
		}
		assertThat(chords[0].getDisplayName(), equalTo("Cmaj11"));
		assertThat(chords[1].getDisplayName(), equalTo("Dm11"));
		assertThat(chords[2].getDisplayName(), equalTo("Em11-9"));
		assertThat(chords[3].getDisplayName(), equalTo("Fmaj9+11"));
		assertThat(chords[4].getDisplayName(), equalTo("G11"));
		assertThat(chords[5].getDisplayName(), equalTo("Am11"));
		assertThat(chords[6].getDisplayName(), equalTo("Bm11-9-5"));
		Chord chord = null;
		int root = 0;
		int third = 0;
		int fifth = 0;
		int seventh = 0;
		int ninth = 0;
		int eleventh = 0;
		int thirteenth = 0;
	
		chord = chords[0];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		
		assertThat("the root is correct", root, equalTo(0));
		assertThat("the third is correct", third, equalTo(4));
		assertThat("the fifth is correct", fifth, equalTo(7));
		assertThat("the seventh is correct", seventh, equalTo(11));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("C").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("E").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		
		chord = chords[1];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(2));
		assertThat("the third is correct", third, equalTo(5));
		assertThat("the fifth is correct", fifth, equalTo(9));
		assertThat("the seventh is correct", seventh, equalTo(12));
		assertThat("the ninth is correct", ninth, equalTo(16));
		assertThat("the eleventh is correct", eleventh, equalTo(19));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("D").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("F").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("G1").getMidiNumber()));
		
		chord = chords[2];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(4));
		assertThat("the third is correct", third, equalTo(7));
		assertThat("the fifth is correct", fifth, equalTo(11));
		assertThat("the seventh is correct", seventh, equalTo(14));
		assertThat("the ninth is correct", ninth, equalTo(17));
		assertThat("the eleventh is correct", eleventh, equalTo(21));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("E").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("A1").getMidiNumber()));
		
		chord = chords[3];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(5));
		assertThat("the third is correct", third, equalTo(9));
		assertThat("the fifth is correct", fifth, equalTo(12));
		assertThat("the seventh is correct", seventh, equalTo(16));
		assertThat("the ninth is correct", ninth, equalTo(19));
		assertThat("the eleventh is correct", eleventh, equalTo(23));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("F").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("G1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("B1").getMidiNumber()));
		
		chord = chords[4];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(7));
		assertThat("the third is correct", third, equalTo(11));
		assertThat("the fifth is correct", fifth, equalTo(14));
		assertThat("the seventh is correct", seventh, equalTo(17));
		assertThat("the ninth is correct", ninth, equalTo(21));
		assertThat("the eleventh is correct", eleventh, equalTo(24));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("A1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("C2").getMidiNumber()));
		
		chord = chords[5];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(9));
		assertThat("the third is correct", third, equalTo(12));
		assertThat("the fifth is correct", fifth, equalTo(16));
		assertThat("the seventh is correct", seventh, equalTo(19));
		assertThat("the ninth is correct", ninth, equalTo(23));
		assertThat("the eleventh is correct", eleventh, equalTo(26));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("G1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("B1").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("D2").getMidiNumber()));
		
		chord = chords[6];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		eleventh = chord.getEleventh();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(11));
		assertThat("the third is correct", third, equalTo(14));
		assertThat("the fifth is correct", fifth, equalTo(17));
		assertThat("the seventh is correct", seventh, equalTo(21));
		assertThat("the ninth is correct", ninth, equalTo(24));
		assertThat("the eleventh is correct", eleventh, equalTo(28));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("A1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("C2").getMidiNumber()));
		assertThat("the eleventh is correct", eleventh, equalTo(PitchFactory
				.getPitchByName("E2").getMidiNumber()));
	}

	@Test
	public void getChordsAllTo9() {
		Scale scale = ScaleFactory.createFromName("Major");
		assertThat("Scale is not null", scale, notNullValue());
		Chord[] chords = ChordFactory.getChords(scale, true, true, false, false);
		assertThat("Chords are not null", chords, notNullValue());
		for (Chord c : chords) {
			logger.debug(c.getDisplayName());
		}
		assertThat(chords[0].getDisplayName(), equalTo("Cmaj9"));
		assertThat(chords[1].getDisplayName(), equalTo("Dm9"));
		assertThat(chords[2].getDisplayName(), equalTo("Em7-9"));
		assertThat(chords[3].getDisplayName(), equalTo("Fmaj9"));
		assertThat(chords[4].getDisplayName(), equalTo("G9"));
		assertThat(chords[5].getDisplayName(), equalTo("Am9"));
		assertThat(chords[6].getDisplayName(), equalTo("Bm7-9-5"));
		Chord chord = null;
		int root = 0;
		int third = 0;
		int fifth = 0;
		int seventh = 0;
		int ninth = 0;
		int eleventh = 0;
		int thirteenth = 0;
	
		chord = chords[0];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		
		assertThat("the root is correct", root, equalTo(0));
		assertThat("the third is correct", third, equalTo(4));
		assertThat("the fifth is correct", fifth, equalTo(7));
		assertThat("the seventh is correct", seventh, equalTo(11));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("C").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("E").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		
		chord = chords[1];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(2));
		assertThat("the third is correct", third, equalTo(5));
		assertThat("the fifth is correct", fifth, equalTo(9));
		assertThat("the seventh is correct", seventh, equalTo(12));
		assertThat("the ninth is correct", ninth, equalTo(16));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("D").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("F").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		
		chord = chords[2];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(4));
		assertThat("the third is correct", third, equalTo(7));
		assertThat("the fifth is correct", fifth, equalTo(11));
		assertThat("the seventh is correct", seventh, equalTo(14));
		assertThat("the ninth is correct", ninth, equalTo(17));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("E").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		
		chord = chords[3];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(5));
		assertThat("the third is correct", third, equalTo(9));
		assertThat("the fifth is correct", fifth, equalTo(12));
		assertThat("the seventh is correct", seventh, equalTo(16));
		assertThat("the ninth is correct", ninth, equalTo(19));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("F").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("G1").getMidiNumber()));
		
		chord = chords[4];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(7));
		assertThat("the third is correct", third, equalTo(11));
		assertThat("the fifth is correct", fifth, equalTo(14));
		assertThat("the seventh is correct", seventh, equalTo(17));
		assertThat("the ninth is correct", ninth, equalTo(21));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("A1").getMidiNumber()));
		
		chord = chords[5];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(9));
		assertThat("the third is correct", third, equalTo(12));
		assertThat("the fifth is correct", fifth, equalTo(16));
		assertThat("the seventh is correct", seventh, equalTo(19));
		assertThat("the ninth is correct", ninth, equalTo(23));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("G1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("B1").getMidiNumber()));
		
		chord = chords[6];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		ninth = chord.getNinth();
		logger.debug(String.format("%d %d %d %d %d %d %d", root, third,
				fifth, seventh, ninth, eleventh, thirteenth));
		assertThat("the root is correct", root, equalTo(11));
		assertThat("the third is correct", third, equalTo(14));
		assertThat("the fifth is correct", fifth, equalTo(17));
		assertThat("the seventh is correct", seventh, equalTo(21));
		assertThat("the ninth is correct", ninth, equalTo(24));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("A1").getMidiNumber()));
		assertThat("the ninth is correct", ninth, equalTo(PitchFactory
				.getPitchByName("C2").getMidiNumber()));
	}

	@Test
	public void getChordsAllTo7() {
		Scale scale = ScaleFactory.createFromName("Major");
		assertThat("Scale is not null", scale, notNullValue());
		Chord[] chords = ChordFactory.getChords(scale, true, false, false, false);
		assertThat("Chords are not null", chords, notNullValue());
		for (Chord c : chords) {
			logger.debug(c.getDisplayName());
		}
		assertThat(chords[0].getDisplayName(), equalTo("Cmaj7"));
		assertThat(chords[1].getDisplayName(), equalTo("Dm7"));
		assertThat(chords[2].getDisplayName(), equalTo("Em7"));
		assertThat(chords[3].getDisplayName(), equalTo("Fmaj7"));
		assertThat(chords[4].getDisplayName(), equalTo("G7"));
		assertThat(chords[5].getDisplayName(), equalTo("Am7"));
		assertThat(chords[6].getDisplayName(), equalTo("Bm7-5"));
		Chord chord = null;
		int root = 0;
		int third = 0;
		int fifth = 0;
		int seventh = 0;
	
		chord = chords[0];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		
		assertThat("the root is correct", root, equalTo(0));
		assertThat("the third is correct", third, equalTo(4));
		assertThat("the fifth is correct", fifth, equalTo(7));
		assertThat("the seventh is correct", seventh, equalTo(11));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("C").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("E").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		
		chord = chords[1];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		assertThat("the root is correct", root, equalTo(2));
		assertThat("the third is correct", third, equalTo(5));
		assertThat("the fifth is correct", fifth, equalTo(9));
		assertThat("the seventh is correct", seventh, equalTo(12));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("D").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("F").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		
		chord = chords[2];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		assertThat("the root is correct", root, equalTo(4));
		assertThat("the third is correct", third, equalTo(7));
		assertThat("the fifth is correct", fifth, equalTo(11));
		assertThat("the seventh is correct", seventh, equalTo(14));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("E").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		
		chord = chords[3];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		assertThat("the root is correct", root, equalTo(5));
		assertThat("the third is correct", third, equalTo(9));
		assertThat("the fifth is correct", fifth, equalTo(12));
		assertThat("the seventh is correct", seventh, equalTo(16));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("F").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		
		chord = chords[4];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		assertThat("the root is correct", root, equalTo(7));
		assertThat("the third is correct", third, equalTo(11));
		assertThat("the fifth is correct", fifth, equalTo(14));
		assertThat("the seventh is correct", seventh, equalTo(17));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("G").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		
		chord = chords[5];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		assertThat("the root is correct", root, equalTo(9));
		assertThat("the third is correct", third, equalTo(12));
		assertThat("the fifth is correct", fifth, equalTo(16));
		assertThat("the seventh is correct", seventh, equalTo(19));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("A").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("C1").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("E1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("G1").getMidiNumber()));
		
		chord = chords[6];
		root = chord.getRoot();
		third = chord.getThird();
		fifth = chord.getFifth();
		seventh = chord.getSeventh();
		assertThat("the root is correct", root, equalTo(11));
		assertThat("the third is correct", third, equalTo(14));
		assertThat("the fifth is correct", fifth, equalTo(17));
		assertThat("the seventh is correct", seventh, equalTo(21));
		
		assertThat("the root is correct", root, equalTo(PitchFactory
				.getPitchByName("B").getMidiNumber()));
		assertThat("the third is correct", third, equalTo(PitchFactory
				.getPitchByName("D1").getMidiNumber()));
		assertThat("the fifth is correct", fifth, equalTo(PitchFactory
				.getPitchByName("F1").getMidiNumber()));
		assertThat("the seventh is correct", seventh, equalTo(PitchFactory
				.getPitchByName("A1").getMidiNumber()));
	}
}
