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

import org.junit.Ignore;
import org.junit.Test;

import com.rockhoppertech.music.chord.Chord;
import com.rockhoppertech.music.chord.ChordFactory;
import com.rockhoppertech.music.chord.UnknownChordException;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author edelisa
 * 
 */
public class ScaleTest {

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#Scale()}.
	 */
	@Test
	@Ignore
	public void testScale() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#Scale(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	@Ignore
	public void testScaleStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#Scale(java.lang.String, int[])}.
	 */
	@Test
	@Ignore
	public void testScaleStringIntArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#Scale(java.lang.String, java.lang.Integer[], java.lang.String)}
	 * .
	 */
	@Test
	@Ignore
	public void testScaleStringIntegerArrayString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#isDiatonic(int)}.
	 */
	@Test
	@Ignore
	public void testIsDiatonic() {
		fail("Not yet implemented");
		Scale scale = ScaleFactory.createFromName("Major");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#getIntervalsAsString()}.
	 */
	@Test
	@Ignore
	public void testGetIntervalsAsString() {
		fail("Not yet implemented");
		Scale scale = ScaleFactory.createFromName("Major");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#getDescendingIntervalsAsString()}.
	 */
	@Test
	@Ignore
	public void testGetDescendingIntervalsAsString() {
		fail("Not yet implemented");
		Scale scale = ScaleFactory.createFromName("Major");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#getDegreesAsString()}.
	 */
	@Test
	@Ignore
	public void testGetDegreesAsString() {
		fail("Not yet implemented");
		Scale scale = ScaleFactory.createFromName("Major");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#getDegreesAsPitchString()}.
	 */
	@Test
	@Ignore
	public void testGetDegreesAsPitches() {
		fail("Not yet implemented");
		Scale scale = ScaleFactory.createFromName("Major");
		String pits = scale.getDegreesAsPitchString();
		assertNotNull(pits);
		assertThat(pits, not(equalTo(null)));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#getDegreesAsPitches(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDegreesAsPitchesString() {
		Scale scale = ScaleFactory.createFromName("Major");
		List<Pitch> pits = scale.getDegreesAsPitches("C");
		assertNotNull(pits);
		assertThat(pits.get(0).getPitchClass(), equalTo(PitchFactory.getPitch(
				"C").getPitchClass()));
		assertThat(pits.get(1).getPitchClass(), equalTo(PitchFactory.getPitch(
				"D").getPitchClass()));
		assertThat(pits.get(2).getPitchClass(), equalTo(PitchFactory.getPitch(
				"E").getPitchClass()));
		assertThat(pits.get(3).getPitchClass(), equalTo(PitchFactory.getPitch(
				"F").getPitchClass()));
		assertThat(pits.get(4).getPitchClass(), equalTo(PitchFactory.getPitch(
				"G").getPitchClass()));
		assertThat(pits.get(5).getPitchClass(), equalTo(PitchFactory.getPitch(
				"A").getPitchClass()));
		assertThat(pits.get(6).getPitchClass(), equalTo(PitchFactory.getPitch(
				"B").getPitchClass()));
		for (Pitch p : pits) {
			System.err.printf("%s ", p);
		}
		System.err.println();

		pits = scale.getDegreesAsPitches("Eb");
		assertNotNull(pits);
		assertThat(pits.get(0).getPitchClass(), equalTo(PitchFactory.getPitch(
				"Eb").getPitchClass()));
		assertThat(pits.get(1).getPitchClass(), equalTo(PitchFactory.getPitch(
				"F").getPitchClass()));
		assertThat(pits.get(2).getPitchClass(), equalTo(PitchFactory.getPitch(
				"G").getPitchClass()));
		assertThat(pits.get(3).getPitchClass(), equalTo(PitchFactory.getPitch(
				"Ab").getPitchClass()));
		assertThat(pits.get(4).getPitchClass(), equalTo(PitchFactory.getPitch(
				"Bb").getPitchClass()));
		assertThat(pits.get(5).getPitchClass(), equalTo(PitchFactory.getPitch(
				"C").getPitchClass()));
		assertThat(pits.get(6).getPitchClass(), equalTo(PitchFactory.getPitch(
				"D").getPitchClass()));
		for (Pitch p : pits) {
			System.err.printf("%s ", p);
		}
		System.err.println();

		scale = ScaleFactory.createFromName("Harmonic Minor");
		pits = scale.getDegreesAsPitches("C");
		assertNotNull(pits);
		assertThat(pits.get(0).getPitchClass(), equalTo(PitchFactory.getPitch(
				"C").getPitchClass()));
		assertThat(pits.get(1).getPitchClass(), equalTo(PitchFactory.getPitch(
				"D").getPitchClass()));
		assertThat(pits.get(2).getPitchClass(), equalTo(PitchFactory.getPitch(
				"Eb").getPitchClass()));
		assertThat(pits.get(3).getPitchClass(), equalTo(PitchFactory.getPitch(
				"F").getPitchClass()));
		assertThat(pits.get(4).getPitchClass(), equalTo(PitchFactory.getPitch(
				"G").getPitchClass()));
		assertThat(pits.get(5).getPitchClass(), equalTo(PitchFactory.getPitch(
				"Ab").getPitchClass()));
		assertThat(pits.get(6).getPitchClass(), equalTo(PitchFactory.getPitch(
				"B").getPitchClass()));
		for (Pitch p : pits) {
			System.err.printf("%s ", p);
		}
		System.err.println();

	}

	@Test
	public void pitchToDegree() {
		Scale scale = ScaleFactory.createFromName("Major");
		String[] pcs = { "A", "B", "C", "D", "E", "F", "G" };
		for (String p : pcs) {
			int d = scale.pitchToDegree("C", p);
			System.err.printf("d=%d for %s\n", d, p);
		}
		pcs = new String[] { "A", "B", "C", "D", "E", "F#", "G" };
		for (String p : pcs) {
			int d = scale.pitchToDegree("C", p);
			System.err.printf("d=%d for %s\n", d, p);
		}

	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#getDegree(int)}.
	 */
	@Test
	public void testGetDegree() {
		Scale scale = ScaleFactory.createFromName("Major");
		int degree = scale.getDegree(0);
		assertThat(degree, equalTo(Pitch.C0));
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#getIntervals()}.
	 */
	@Test
	public void testGetIntervals() {
		Scale scale = ScaleFactory.createFromName("Major");
		int[] i = scale.getIntervals();
		assertNotNull(i);
		assertThat(i, notNullValue());
		assertThat(i.length > 0, is(true));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#setIntervals(int[])}.
	 */
	@Test
	@Ignore
	public void testSetIntervals() {
		Scale scale = ScaleFactory.createFromName("Major");
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#getName()}.
	 */
	@Test
	public void testGetName() {
		Scale scale = ScaleFactory.createFromName("Major");
		String name = scale.getName();
		assertNotNull(name);
		assertThat(name, equalTo("Major"));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#setName(java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testSetName() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#getSpelling()}.
	 */
	@Test
	public void testGetSpelling() {
		Scale scale = ScaleFactory.createFromName("Major");
		String sp = scale.getSpelling();
		assertNotNull(sp);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#setSpelling(java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testSetSpelling() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#getDescription()}.
	 */
	@Test
	@Ignore
	public void testGetDescription() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#setDescription(java.lang.String)}.
	 */
	@Test
	@Ignore
	public void testSetDescription() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#getDegrees()}.
	 */
	@Test
	@Ignore
	public void testGetDegrees() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#setDegrees(int[])}.
	 */
	@Test
	@Ignore
	public void testSetDegrees() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#getLength()}.
	 */
	@Test
	@Ignore
	public void testGetLength() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#setLength(int)}.
	 */
	@Test
	@Ignore
	public void testSetLength() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#setOctave(int)}.
	 */
	@Test
	@Ignore
	public void testSetOctave() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.rockhoppertech.music.Scale#getOctave()}.
	 */
	@Test
	@Ignore
	public void testGetOctave() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#getDescendingIntervals()}.
	 */
	@Test
	@Ignore
	public void testGetDescendingIntervals() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#setDescendingIntervals(int[])}.
	 */
	@Test
	@Ignore
	public void testSetDescendingIntervalsIntArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#setDescendingIntervals(java.lang.Integer[])}
	 * .
	 */
	@Test
	@Ignore
	public void testSetDescendingIntervalsIntegerArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#isDescendingDifferent()}.
	 */
	@Test
	@Ignore
	public void testIsDescendingDifferent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.Scale#setDescendingDifferent(boolean)}.
	 */
	@Test
	@Ignore
	public void testSetDescendingDifferent() {
		fail("Not yet implemented");
	}

	@Test
	public void contains() {
		Scale scale = ScaleFactory.getScaleByKeyAndName("C", "Major");
		String name = scale.getName();
		assertNotNull(name);
		assertThat(name, equalTo("Major"));
		Chord chord = null;
		try {
			chord = ChordFactory.getChordBySymbol("maj");
		} catch (UnknownChordException e) {
			e.printStackTrace();
		}
		assertNotNull(chord);
		
		assertThat(scale.contains("c", chord), equalTo(true));
		assertThat(scale.contains("c#", chord), equalTo(false));
		assertThat(scale.contains("d", chord), equalTo(false));
		assertThat(scale.contains("ef", chord), equalTo(false));
		assertThat(scale.contains("e", chord), equalTo(false));
		assertThat(scale.contains("f", chord), equalTo(true));
		assertThat(scale.contains("f#", chord), equalTo(false));
		assertThat(scale.contains("g", chord), equalTo(true));
		assertThat(scale.contains("af", chord), equalTo(false));
		assertThat(scale.contains("a", chord), equalTo(false));
		assertThat(scale.contains("bf", chord), equalTo(false));
		assertThat(scale.contains("b", chord), equalTo(false));
		
		
	}
}
