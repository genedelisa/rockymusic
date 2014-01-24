/**
 * 
 */
package com.rockhoppertech.music.scale;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * @author edelisa
 * 
 */
public class ScaleFactoryTest {
	static Logger logger = LoggerFactory.getLogger(ScaleFactoryTest.class);

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createFromSpelling(java.lang.String)}
	 * .
	 */
	@Test
	@Ignore
	public void testCreateFromSpelling() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createFromName(java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateFromName() {
		Scale scale = null;
		scale = ScaleFactory.createFromName("Major");
		assertThat("scale is not null", scale, notNullValue());
		assertThat("the length is correct", scale.getLength(), equalTo(8));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createFromIntervals(int[])}
	 * .
	 */
	@Test
	public void testCreateFromIntervals() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#findBySize(int)}.
	 */
	@Test
	public void shouldFindBySize() {
		List<Scale> scales = null;
		scales = ScaleFactory.findBySize(7);

		assertThat("scales of size 7 are not null", scales, notNullValue());
		logger.debug("scales {}", scales);

		Scale scale = null;
		scale = ScaleFactory.createFromName("Major");
		assertThat("major scale is not null", scale, notNullValue());
		assertThat(
				"major scale is in the group of size 7",
				scales,
				hasItem(scale));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#create(int, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreate() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#getPitches(java.lang.String, int)}
	 * .
	 */
	@Test
	public void testGetPitches() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#getNoteListFromScale(int[], int, int, double)}
	 * .
	 */
	@Test
	public void testGetNoteListFromScale() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createMIDITrack(java.lang.String, int)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackIntString() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createMIDITrack(java.lang.String, int, double, double)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackIntStringDoubleDouble() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createMIDITrack(com.rockhoppertech.music.Scale, double, double)}
	 * .
	 */
	@Test
	public void shouldCreateMIDITrackFromScaleStartBeatAndDuration() {
		MIDITrack track = null;
		Scale scale = null;

		scale = ScaleFactory.createFromName("Major");
		assertThat("scale is not null", scale, notNullValue());
		int rootMidiNum = PitchFactory.getPitch(
				scale.getKey() + scale.getOctave()).getMidiNumber();
		assertThat("scale root is 0", rootMidiNum, equalTo(0));
		assertThat("scale root is C0", rootMidiNum, equalTo(Pitch.C0));
		logger.debug("root is {}", rootMidiNum);

		// now test that bad boy
		track = ScaleFactory.createMIDITrack(scale, 1d, 4d);

		assertThat("track is not null", track, notNullValue());
		assertThat(
				"track is same size as the scale",
				track.size(),
				equalTo(scale.getLength()));
		MIDINote note = track.get(0);
		assertThat("note is not null", note, notNullValue());
		int noteMIDINumber = note.getPitch().getMidiNumber();
		assertThat("pitch is correct", noteMIDINumber, equalTo(rootMidiNum));
		assertThat(
				"note's midi number is C0",
				note.getMidiNumber(),
				equalTo(Pitch.C0));
		assertThat("start beat is 1", note.getStartBeat(), equalTo(1d));
		assertThat("duration is 4", note.getDuration(), equalTo(4d));

		note = track.get(1);
		assertThat("note is not null", note, notNullValue());
		assertThat(
				"note's midi number is D0",
				note.getMidiNumber(),
				equalTo(Pitch.D0));
		assertThat("note duration is 4", note.getDuration(), equalTo(4d));

		note = track.get(2);
		assertThat("note is not null", note, notNullValue());
		assertThat(
				"note's midi number is E0",
				note.getMidiNumber(),
				equalTo(Pitch.E0));
		assertThat("note duration is 4", note.getDuration(), equalTo(4d));

		note = track.get(3);
		assertThat("note is not null", note, notNullValue());
		assertThat(
				"note's midi number is F0",
				note.getMidiNumber(),
				equalTo(Pitch.F0));
		assertThat("note duration is 4", note.getDuration(), equalTo(4d));

		note = track.get(4);
		assertThat("note is not null", note, notNullValue());
		assertThat(
				"note's midi number is G0",
				note.getMidiNumber(),
				equalTo(Pitch.G0));
		assertThat("note duration is 4", note.getDuration(), equalTo(4d));

		note = track.get(5);
		assertThat("note is not null", note, notNullValue());
		assertThat(
				"note's midi number is A0",
				note.getMidiNumber(),
				equalTo(Pitch.A0));
		assertThat("note duration is 4", note.getDuration(), equalTo(4d));

		note = track.get(6);
		assertThat("note is not null", note, notNullValue());
		assertThat(
				"note's midi number is B0",
				note.getMidiNumber(),
				equalTo(Pitch.B0));
		assertThat("note duration is 4", note.getDuration(), equalTo(4d));

		// assertThat(scales, hasItem(scale));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createMIDITrack(com.rockhoppertech.music.Scale, int)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackIntScale() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createMIDITrack(com.rockhoppertech.music.Scale, int, double, double)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackIntScaleDoubleDouble() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createMIDITrack(com.rockhoppertech.music.Scale, int, double, double, boolean, int)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackIntScaleDoubleDoubleBooleanInt() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createMIDITrackOverEntireRange(com.rockhoppertech.music.Scale)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackScale() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createMIDITrack(com.rockhoppertech.music.Scale, int, double, double, int)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackIntScaleDoubleDoubleInt() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#createMIDITrack(int[], int, double, double, int, boolean)}
	 * .
	 */
	@Test
	public void testCreateMIDITrackIntIntArrayDoubleDoubleIntBoolean() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#getTrackPattern(com.rockhoppertech.music.Scale, int[], int, int, int, double, boolean, double, boolean)}
	 * .
	 */
	@Test
	public void testGetNoteListPatternScaleIntArrayIntIntIntDoubleBooleanDoubleBoolean() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#getNoteListPattern(int[], int[], int, int, int, double, boolean, double, boolean)}
	 * .
	 */
	@Test
	public void testGetNoteListPatternIntArrayIntArrayIntIntIntDoubleBooleanDoubleBoolean() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#getScaleByKeyAndName(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetScaleByKeyAndName() {

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.scale.ScaleFactory#getScaleByName(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetScaleByName() {

	}

}
