/**
 * 
 */
package com.rockhoppertech.music.midi.parse;

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

import static com.rockhoppertech.music.Pitch.C5;
import static com.rockhoppertech.music.Pitch.C6;
import static com.rockhoppertech.music.Pitch.D6;
import static com.rockhoppertech.music.Pitch.E4;
import static com.rockhoppertech.music.Pitch.E6;
import static com.rockhoppertech.music.Pitch.F4;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDIStringParserTest {
	private static final Logger logger = LoggerFactory
			.getLogger(MIDIStringParserTest.class);

	private MIDIStringParser parser;

	/*
	 * + The plus sign indicates that there is one or more ? The question mark
	 * indicates there is zero or one * The asterisk indicates there are zero or
	 * more
	 * 
	 * so, any whitespace followed by one or more pc specifiers (C, Ef, F#) then
	 * one or more octave chars (e.g. octave 10 is two chars)
	 */
	static Pattern fullPitchPattern = Pattern.compile("\\s*[A-Ga-gsS#]+[0-9]+");
	static Pattern shortPitchPattern = Pattern.compile("\\s*[A-Ga-gsS#]+\\s*");

	public static void main(String[] args) {
		MIDIStringParser parser = new MIDIStringParser();
		parser.parsePitch("C4");
		
		parser.parsePitch("C#3");

		parser.parsePitch("Cs3");
		parser.parsePitch("Cf3");
		parser.parsePitch("Cb3");
		parser.parsePitch("Cbb3");
		parser.parsePitch("Cx3");
		parser.parsePitch("Crap");

	}

	// this is just to figure out the regexp. moved it to the actual parser.
	public static void oldmain(String[] args) {
		MIDINote n = null;
		Scanner scanner = new Scanner("C4, C, e, f6, Cf3, F#3, J");
		scanner.useDelimiter(",");

		String p = null;
		while (scanner.hasNext()) {
			if (scanner.hasNext(fullPitchPattern)) {
				p = scanner.next();
				logger.debug("full pitch " + p);
			} else if (scanner.hasNext(shortPitchPattern)) {
				p = scanner.next();
				logger.debug("short pitch " + p);
			} else {
				p = scanner.next();
				logger.debug("junk " + p);
			}

			double start = 1d;
			if (scanner.hasNextDouble()) {
				start = scanner.nextDouble();
			}

			double duration = 1d;
			if (scanner.hasNextDouble()) {
				duration = scanner.nextDouble();
			}

			n = new MIDINote(p, start, duration);
			int oct = n.getPitch().getMidiNumber() / 12;
			logger.debug("oct{}",oct);
		}
		scanner.close();

	}

	@Before
	public void setUp() {
		parser = new MIDIStringParser();
	}

	@Test
	public void parseNotesWithRunningOctave() {
		String s;
		MIDITrack list;
		MIDINote n;

		s = "C, C6, D, E, F4, E";
		list = parser.parseString(s);
		assertNotNull(list);
		assertThat("list size is correct",
				list.size(),
				equalTo(6));

		n = list.get(0);
		assertThat("note is not null",
				n,
				notNullValue());
		assertThat("pitch is correct",
				n.getMidiNumber(),
				equalTo(C5));

		n = list.get(1);
		assertThat("note is not null",
				n,
				notNullValue());
		assertEquals(C6,
				n.getMidiNumber());

		n = list.get(2);
		assertThat("note is not null",
				n,
				notNullValue());
		assertEquals(D6,
				n.getMidiNumber());

		n = list.get(3);
		assertThat("note is not null",
				n,
				notNullValue());
		assertEquals(E6,
				n.getMidiNumber());

		n = list.get(4);
		assertThat("note is not null",
				n,
				notNullValue());
		assertEquals(F4,
				n.getMidiNumber());

		n = list.get(5);
		assertThat("note is not null",
				n,
				notNullValue());
		assertEquals(E4,
				n.getMidiNumber());

	}

	@Test
	public void parseNoteWithOctave() {
		String s;
		MIDINote n;

		s = "C10";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(120,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C#10";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(121,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Db10";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(121,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C9";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(108,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C8";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(96,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C7";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(84,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C6";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(72,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C5";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(60,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C4";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(48,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C3";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(36,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C2";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(24,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C1";
		n = parser.parseNote(s);
		assertNotNull(n);
		assertEquals(12,
				n.getMidiNumber());
		checkNoteDefaults(n);
	}

	private void checkNoteDefaults(MIDINote n) {
		assertThat("the note's startbeat is correct", n.getStartBeat(),
				equalTo(1d));
		assertThat("the note's duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the note's end beat is correct", n.getEndBeat(),
				equalTo(2d));
		assertThat("the note's velocity is correct", n.getVelocity(),
				equalTo(64));
		assertThat("the note's bank is correct", n.getBank(),
				equalTo(0));
		assertThat("the note's program is correct", n.getProgram(),
				equalTo(0));
		assertThat("the note's pitch bend is correct", n.getPitchBend(),
				equalTo(0));
		assertThat("the note's voice is correct", n.getVoice(),
				equalTo(0));
		assertThat("the note's is rest is correct", n.isRest(),
				equalTo(false));
	}

	private void checkNoteNonTimeDefaults(MIDINote n) {
		assertEquals(64,
				n.getVelocity());
		assertEquals(0,
				n.getBank());
		assertEquals(0,
				n.getProgram());
		assertEquals(0,
				n.getPitchBend());
		assertEquals(0,
				n.getVoice());
		assertFalse(n.isRest());
	}

	/**
	 * Given just the note name. No octave, no start, dur etc. Test method for
	 * {@link com.rockhoppertech.music.midi.parse.MIDIStringParser#parseNote(java.lang.String)}
	 * .
	 */
	@Test
	public void parseNoteDefault() {
		String s;
		MIDINote n;

		// default oct is 5
		s = "CF";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the note's pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.B4));
		assertThat("the note's pitch is correct", n.getMidiNumber(),
				equalTo(59));
		checkNoteDefaults(n);

		// the previous parse set the running octave to 4
		parser.setRunningOctave(5);
		s = "Cb";
		n = parser.parseNote(s);
		assertThat("the note's pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.B4));
		assertThat("the note's pitch is correct", n.getMidiNumber(),
				equalTo(59));
		checkNoteDefaults(n);

		parser.setRunningOctave(5);
		s = "C";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(60,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C#";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(61,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "DF";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(61,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Df";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(61,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Db";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(61,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "D";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(62,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "D#";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(63,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "EF";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(63,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Ef";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(63,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Eb";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(63,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "E";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(64,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "F";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(65,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "F#";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(66,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "GF";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(66,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Gf";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(66,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Gb";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(66,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "G";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(67,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "G#";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(68,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "AF";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(68,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Af";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(68,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Ab";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(68,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "A";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(69,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "A#";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(70,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "BF";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(70,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Bf";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(70,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "Bb";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(70,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "B";
		n = parser.parseNote(s);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(71,
				n.getMidiNumber());
		checkNoteDefaults(n);
	}

	/**
	 * If you specify a pitch incorrectly, you should get this exception.
	 */
	@Test(expected = MIDIParserException.class)
	public void expectBadNote() {
		String s;
		s = "Z";
		parser.parseNote(s);
	}

	/**
	 * The string starts with S+ which puts it in append mode. There is no
	 * startbeat specification since append simply, well, appends.
	 */
	@Test
	public void shouldParseAppendPitchDur() {
		String s;
		MIDITrack track;
		MIDINote n;

		// S+ puts it in append mode
		// the token is now brief; just the pitch and duration
		s = "S+ C5,.5 D5,1";
		track = parser.parseString(s);

		assertThat("The track is not null.", track, notNullValue());
		assertThat("the track size is correct", track.size(),
				equalTo(2));
		// assertThat(actual, is(equalTo(expected)));

		n = track.get(0);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the note's pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));
		assertThat("the note's start beat is correct", n.getStartBeat(),
				equalTo(1.0));
		assertThat("the note's duration is correct", n.getDuration(),
				equalTo(0.5));

		n = track.get(1);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the note's pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.D5));
		assertThat("the note's start beat is correct", n.getStartBeat(),
				equalTo(1.5));
		assertThat("the note's duration is correct", n.getDuration(),
				equalTo(1.0));

		logger.debug(track.toString());

	}

	@Test
	public void checkMulti() {
		String s;
		String r;

		s = "S+ X10 (C5 D5)";
		r = parser.checkMulti(s);
		logger.debug("returned '{}'", r);
		r = r.replaceAll(" +", " "); // collapse multi spaces to one
		assertThat("The returned string is not null.", r, notNullValue());
		assertThat("the returned string is correct", r.trim(),
				equalTo("S+ C5 D5 C5 D5 C5 D5 C5 D5 C5 D5 C5 D5 C5 D5 C5 D5 C5 D5 C5 D5"));

		//

		s = "S+ X3 (C5 D5) X2 (E G)";
		r = parser.checkMulti(s);
		logger.debug("returned '{}'", r);
		r = r.replaceAll(" +", " "); // collapse multi spaces to one
		assertThat("The returned string is not null.", r, notNullValue());
		assertThat("the returned string is correct", r.trim(),
				equalTo("S+ C5 D5 C5 D5 C5 D5 E G E G"));

		// s = "S+ X3 C5 D5"; // doesn't work
		// r = this.parser.checkMulti(s);
		// logger.debug("returned '{}'", r);
		// assertThat("The returned string is not null.", r, notNullValue());
		// assertThat("the returned string is correct", r.trim(),
		// equalTo("S+ C5 D5"));

		s = "S+ X3 (C5 D5) E F";
		r = parser.checkMulti(s);
		logger.debug("returned '{}'", r);
		r = r.replaceAll(" +", " "); // collapse multi spaces to one
		assertThat("The returned string is not null.", r, notNullValue());
		assertThat("the returned string is correct", r.trim(),
				equalTo("S+ C5 D5 C5 D5 C5 D5 E F"));

		s = "S+ C5 D5 E F";
		r = parser.checkMulti(s);
		logger.debug("returned '{}'", r);
		r = r.replaceAll(" +", " "); // collapse multi spaces to one
		assertThat("The returned string is not null.", r, notNullValue());
		assertThat("the returned string is correct", r.trim(),
				equalTo("S+ C5 D5 E F"));

		s = "S+ X3 (C5) E F";
		r = parser.checkMulti(s);
		logger.debug("returned '{}'", r);
		r = r.replaceAll(" +", " "); // collapse multi spaces to one
		assertThat("The returned string is not null.", r, notNullValue());
		assertThat("the returned string is correct", r.trim(),
				equalTo("S+ C5 C5 C5 E F"));

		s = "X3 (C5) E F";
		r = parser.checkMulti(s);
		logger.debug("returned '{}'", r);
		r = r.replaceAll(" +", " "); // collapse multi spaces to one
		assertThat("The returned string is not null.", r, notNullValue());
		assertThat("the returned string is correct", r.trim(),
				equalTo("C5 C5 C5 E F"));
	}

	/**
	 * S+ append mode. When the token is simply the pitch, the duration is 1.0
	 */
	@Test
	public void shouldParseAppendPitch() {
		String s;
		MIDITrack track;
		MIDINote n;

		// S+ puts it in append mode
		// the token is now brief; just the pitch and nothing else.
		s = "S+ C5 D5";
		track = parser.parseString(s);

		assertThat("The track is not null.", track, notNullValue());
		assertThat("the track size is correct", track.size(),
				equalTo(2));
		// assertThat(actual, is(equalTo(expected)));
		logger.debug(track.toString());

		n = track.get(0);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the note's pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));
		assertThat("the note's start beat is correct", n.getStartBeat(),
				equalTo(1.0));
		assertThat("the note's duration is correct", n.getDuration(),
				equalTo(1.0));

		n = track.get(1);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the note's pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.D5));
		assertThat("the note's start beat is correct", n.getStartBeat(),
				equalTo(2.0));
		assertThat("the note's duration is correct", n.getDuration(),
				equalTo(1.0));

	}

	@Test
	public void parseStringDefaults() {
		String s;
		MIDITrack midiTrack;
		MIDINote n;

		s = "C D";
		midiTrack = parser.parseString(s);

		assertThat("The track is not null.", midiTrack, notNullValue());
		assertThat("the track size is correct", midiTrack.size(),
				equalTo(2));
		// assertThat(actual, is(equalTo(expected)));

		n = midiTrack.get(0);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the note pitch midi number is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));
		checkNoteDefaults(n);

		n = midiTrack.get(1);
		assertNotNull(n);
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		s = "C C# DF Db D D# Ef Eb E F F# Gb Gf G G# Af Ab A A# Bb B";
		midiTrack = parser.parseString(s);
		assertThat("The list is not null.", midiTrack, notNullValue());
		assertThat("the track size is correct", midiTrack.size(),
				equalTo(21));
		// assertThat(actual, is(equalTo(expected)));

		n = midiTrack.get(0);
		assertNotNull(n);
		assertEquals(60,
				n.getMidiNumber());
		assertEquals(Pitch.C5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(1);
		assertNotNull(n);
		assertEquals(61,
				n.getMidiNumber());
		assertEquals(Pitch.CS5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(2);
		assertNotNull(n);
		assertEquals(61,
				n.getMidiNumber());
		assertEquals(Pitch.DF5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(3);
		assertNotNull(n);
		assertEquals(61,
				n.getMidiNumber());
		assertEquals(Pitch.DF5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(4);
		assertNotNull(n);
		assertEquals(62,
				n.getMidiNumber());
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		// D#
		n = midiTrack.get(5);
		assertNotNull(n);
		assertEquals(63,
				n.getMidiNumber());
		assertEquals(Pitch.DS5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(6);
		assertNotNull(n);
		assertEquals(63,
				n.getMidiNumber());
		assertEquals(Pitch.EF5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(7);
		assertNotNull(n);
		assertEquals(63,
				n.getMidiNumber());
		assertEquals(Pitch.EF5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(8);
		assertNotNull(n);
		assertEquals(64,
				n.getMidiNumber());
		assertEquals(Pitch.E5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(9);
		assertNotNull(n);
		assertEquals(65,
				n.getMidiNumber());
		assertEquals(Pitch.F5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(10);
		assertNotNull(n);
		assertEquals(66,
				n.getMidiNumber());
		assertEquals(Pitch.FS5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(11);
		assertNotNull(n);
		assertEquals(66,
				n.getMidiNumber());
		assertEquals(Pitch.GF5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(12);
		assertNotNull(n);
		assertEquals(66,
				n.getMidiNumber());
		assertEquals(Pitch.GF5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(13);
		assertNotNull(n);
		assertEquals(67,
				n.getMidiNumber());
		assertEquals(Pitch.G5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(14);
		assertNotNull(n);
		assertEquals(68,
				n.getMidiNumber());
		assertEquals(Pitch.GS5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(15);
		assertNotNull(n);
		assertEquals(68,
				n.getMidiNumber());
		assertEquals(Pitch.AF5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(16);
		assertNotNull(n);
		assertEquals(68,
				n.getMidiNumber());
		assertEquals(Pitch.AF5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(17);
		assertNotNull(n);
		assertEquals(69,
				n.getMidiNumber());
		assertEquals(Pitch.A5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(18);
		assertNotNull(n);
		assertEquals(70,
				n.getMidiNumber());
		assertEquals(Pitch.AS5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(19);
		assertNotNull(n);
		assertEquals(70,
				n.getMidiNumber());
		assertEquals(Pitch.BF5,
				n.getMidiNumber());
		checkNoteDefaults(n);

		n = midiTrack.get(20);
		assertNotNull(n);
		assertEquals(71,
				n.getMidiNumber());
		assertEquals(Pitch.B5,
				n.getMidiNumber());
		checkNoteDefaults(n);

	}

	@Test
	public void parseStringWithStartBeat() {
		String s;
		MIDITrack list;
		MIDINote n;

		s = "C,1 D,2";
		list = parser.parseString(s);
		assertNotNull(list);
		assertEquals(2,
				list.size());

		n = list.get(0);
		assertNotNull(n);
		assertEquals(Pitch.C5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);
		assertEquals(2d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		n = list.get(1);
		assertNotNull(n);
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(2d,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);
		assertEquals(3d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		s = "C#,1 Df,2";
		list = parser.parseString(s);
		assertNotNull(list);
		assertEquals(2,
				list.size());

		n = list.get(0);
		assertNotNull(n);
		assertEquals(Pitch.CS5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);
		assertEquals(2d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		n = list.get(1);
		assertNotNull(n);
		assertEquals(Pitch.DF5,
				n.getMidiNumber());
		assertEquals(2d,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);
		assertEquals(3d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

	}

	@Test
	public void parseStringWithStartBeatAndDurationAsString() {
		String s;
		MIDITrack list;
		MIDINote n;

		s = "C,1,h D,2,q.";
		list = parser.parseString(s);
		assertNotNull(list);
		assertEquals(2,
				list.size());

		n = list.get(0);
		assertNotNull(n);
		assertEquals(Pitch.C5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(2d,
				n.getDuration(),
				0d);
		assertEquals(3d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		n = list.get(1);
		assertNotNull(n);
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(2d,
				n.getStartBeat(),
				0d);
		assertEquals(1.5d,
				n.getDuration(),
				0d);
		assertEquals(3.5d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);
	}

	@Test
	public void parseStringWithStartBeatAndDuration() {
		String s;
		MIDITrack list;
		MIDINote n;

		s = "C,1,2 D,2,2.5";
		list = parser.parseString(s);

		assertThat("The list is not null.", list, notNullValue());
		assertThat("the track size is correct", list.size(),
				equalTo(2));
		// assertThat(actual, is(equalTo(expected)));

		n = list.get(0);

		assertThat("The note is not null.", n, notNullValue());
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));
		assertThat("the startbeat is correct", n.getStartBeat(),
				equalTo(1d));
		assertThat("the duration is correct", n.getDuration(),
				equalTo(2d));

		assertEquals(Pitch.C5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(2d,
				n.getDuration(),
				0d);
		assertEquals(3d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		n = list.get(1);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(2d,
				n.getStartBeat(),
				0d);
		assertEquals(2.5d,
				n.getDuration(),
				0d);
		assertEquals(4.5d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		// whitespace delimits notes
		// so, D,1, 2 would not be a note! The 2 would be seen as a pitch

		s = "C#,1,3    Df,2.1,2.5";
		list = parser.parseString(s);
		assertNotNull(list);
		assertEquals(2,
				list.size());

		n = list.get(0);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(Pitch.CS5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(3d,
				n.getDuration(),
				0d);
		assertEquals(4d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		n = list.get(1);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(Pitch.DF5,
				n.getMidiNumber());
		assertEquals(2.1d,
				n.getStartBeat(),
				0d);
		assertEquals(2.5d,
				n.getDuration(),
				0d);
		assertEquals(4.6d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

	}

	@Test
	public void parseStringWithStartBeatAndRunningDur() {
		String s;
		MIDITrack list;
		MIDINote n;

		s = "R2.5 C,1 D,2";
		list = parser.parseString(s);
		assertNotNull(list);
		assertEquals(2,
				list.size());

		n = list.get(0);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(Pitch.C5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(2.5d,
				n.getDuration(),
				0d);
		assertEquals(3.5d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		n = list.get(1);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(2d,
				n.getStartBeat(),
				0d);
		assertEquals(2.5d,
				n.getDuration(),
				0d);
		assertEquals(4.5d,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		s = "R.5 C,1 D,2 R1.5 G5 D";
		list = parser.parseString(s);

		assertThat("The list is not null.", list, notNullValue());
		assertThat("the track size is correct", list.size(),
				equalTo(4));
		// assertThat(actual, is(equalTo(expected)));

		n = list.get(0);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(Pitch.C5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(.5,
				n.getDuration(),
				0d);
		assertEquals(1.5,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		n = list.get(1);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(2d,
				n.getStartBeat(),
				0d);
		assertEquals(.5,
				n.getDuration(),
				0d);
		assertEquals(2.5,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		n = list.get(2);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(Pitch.G5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(1.5,
				n.getDuration(),
				0d);
		assertEquals(2.5,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);

		n = list.get(3);
		assertThat("The note is not null.", n, notNullValue());
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(1.5,
				n.getDuration(),
				0d);
		assertEquals(2.5,
				n.getEndBeat(),
				0d);
		checkNoteNonTimeDefaults(n);
	}

	@Test
	public void parseStringWithPSDV() {
		String s;
		MIDITrack list;
		MIDINote n;
		String pitch = "C5";
		double start = 1.5;
		double duration = 3.2;
		int velocity = 99;
		s = String.format("%s,%f,%f,%d",
				pitch,
				start,
				duration,
				velocity);
		logger.debug(s);
		list = parser.parseString(s);

		assertThat("list is not null",
				list,
				notNullValue());
		logger.debug("track \n{}",list);
		n = list.get(0);
		assertThat("pitch",
				n.getPitch(),
				equalTo(PitchFactory.getPitchByName(pitch)));
		assertThat("start",
				n.getStartBeat(),
				equalTo(start));
		assertThat("duration",
				n.getDuration(),
				equalTo(duration));
		assertThat("velocity",
				n.getVelocity(),
				equalTo(velocity));
		// list.play();
	}

	@Test
	public void parseStringWithAllNoteParams() {
		String s;
		MIDITrack list;
		MIDINote n;
		String pitch = "C5";
		double start = 1.5;
		double duration = 3.2;
		int velocity = 99;
		int pan = 0;
		int channel = 0;
		int bank = 0;
		int program = MIDIGMPatch.BANJO.getProgram();
		int pitchBend = 0;
		s = String.format("%s,%f,%f,%d,%d,%d,%d,%d,%d",
				pitch,
				start,
				duration,
				velocity,
				pan,
				channel,
				bank,
				program,
				pitchBend);
		logger.debug(s);
		list = parser.parseString(s);
		assertThat("list is not null",
				list,
				notNullValue());
		logger.debug("track \n{}",list);

		n = list.get(0);
		assertThat("start",
				n.getStartBeat(),
				equalTo(start));
		assertThat("duration",
				n.getDuration(),
				equalTo(duration));
		assertThat("velocity",
				n.getVelocity(),
				equalTo(velocity));
		assertThat("pan",
				n.getPan(),
				equalTo(pan));
		assertThat("channel",
				n.getChannel(),
				equalTo(channel));
		assertThat("bank",
				n.getBank(),
				equalTo(bank));
		assertThat("program",
				n.getProgram(),
				equalTo(program));
		assertThat("pitchBend",
				n.getPitchBend(),
				equalTo(pitchBend));
		// list.play();

	}

	@Test
	public void shouldIgnoreComments() {
		MIDITrack track;

		StringBuilder sb = new StringBuilder();
		sb.append("C5 D").append('\n');
		sb.append("// a b c").append('\n');
		sb.append("E G").append('\n');
		logger.debug("input " + sb.toString());
		track = parser.parseString(sb.toString());
		logger.debug("track {}", track);
		assertThat("The track is not null.", track, notNullValue());
		assertThat("the track size is correct", track.size(),
				equalTo(4));

	}

	@Test
	public void shouldIgnoreSlashSplatComments() {
		MIDITrack track;

		StringBuilder sb = new StringBuilder();
		sb.append("C5 D").append('\n');
		sb.append("/* a b c */").append('\n');
		sb.append("E G").append('\n');
		logger.debug("input " + sb.toString());
		track = parser.parseString(sb.toString());
		logger.debug("track {}",track);
		assertThat("The track is not null.", track, notNullValue());
		assertThat("the track size is correct", track.size(),
				equalTo(4));

		sb = new StringBuilder();
		sb.append("C5 D").append('\n');
		sb.append("/* a b c */ E G").append('\n');
		logger.debug("input " + sb.toString());
		track = parser.parseString(sb.toString());
		logger.debug("track {}", track);
		assertThat("The track is not null.", track, notNullValue());
		assertThat("the track size is correct", track.size(),
				equalTo(4));

	}

	@Test
	public void parseStringWithProgramChange() {
		String s;
		MIDITrack list;
		MIDINote n;

		s = String.format("I\"%s\" R2.5 C,1 I\"%s\" D,2",
				MIDIGMPatch.PIANO.getName(),
				MIDIGMPatch.BRASS.getName());
		logger.debug(s);

		list = parser.parseString(s);

		assertThat("The list is not null.", list, notNullValue());
		assertThat("the track size is correct", list.size(),
				equalTo(2));
		// assertThat(actual, is(equalTo(expected)));

		n = list.get(0);
		assertNotNull(n);
		assertEquals(Pitch.C5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(2.5d,
				n.getDuration(),
				0d);
		assertEquals(3.5d,
				n.getEndBeat(),
				0d);

		assertEquals(MIDIGMPatch.PIANO.getProgram(),
				n.getProgram());

		// assertEquals(MIDIUtils.getPatchNumber(MIDIUtils.PIANO_PATCH), n
		// .getProgram());

		n = list.get(1);
		assertNotNull(n);
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(2d,
				n.getStartBeat(),
				0d);
		assertEquals(2.5d,
				n.getDuration(),
				0d);
		assertEquals(4.5d,
				n.getEndBeat(),
				0d);
		// assertEquals(MIDIUtils.getPatchNumber(MIDIUtils.BRASS_PATCH), n
		// .getProgram());
		assertEquals(MIDIGMPatch.BRASS.getProgram(),
				n.getProgram());

		s = String.format("I%d R2.5 C,1 I%d D,2",
				MIDIGMPatch.PIANO.getProgram(),
				MIDIGMPatch.BRASS.getProgram());

		// s = String.format("I%d R2.5 c,1 I%d d,2", MIDIUtils
		// .getPatchNumber(MIDIUtils.PIANO_PATCH), MIDIUtils
		// .getPatchNumber(MIDIUtils.BRASS_PATCH));

		list = parser.parseString(s);
		assertThat("The list is not null.", list, notNullValue());
		assertThat("the track size is correct", list.size(),
				equalTo(2));
		// assertThat(actual, is(equalTo(expected)));

		n = list.get(0);
		assertNotNull(n);
		assertEquals(Pitch.C5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(2.5d,
				n.getDuration(),
				0d);
		assertEquals(3.5d,
				n.getEndBeat(),
				0d);
		// assertEquals(MIDIUtils.getPatchNumber(MIDIUtils.PIANO_PATCH), n
		// .getProgram());
		assertEquals(MIDIGMPatch.PIANO.getProgram(),
				n.getProgram());
		n = list.get(1);
		assertNotNull(n);
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(2d,
				n.getStartBeat(),
				0d);
		assertEquals(2.5d,
				n.getDuration(),
				0d);
		assertEquals(4.5d,
				n.getEndBeat(),
				0d);
		// assertEquals(MIDIUtils.getPatchNumber(MIDIUtils.BRASS_PATCH), n
		// .getProgram());
		assertEquals(MIDIGMPatch.BRASS.getProgram(),
				n.getProgram());

	}

	@Test
	public void shouldParseRunningDurAsDurString() {
		String s;
		MIDITrack track;
		MIDINote n;

		s = "S+ Re C D E";

		track = parser.parseString(s);
		logger.debug("track \n{}",track);
		assertThat("The list is not null.", track, notNullValue());
		assertThat("the track size is correct", track.size(),
				equalTo(3));

		n = track.get(0);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(.5));
		n = track.get(1);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(.5));
		n = track.get(2);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(.5));
	}

	@Test
	public void shouldParseRepeatGroup() {
		String s;
		MIDITrack track;
		MIDINote n;

		s = "S+ \nX3 (C D E) F";
		// should become C D E C D E C D E F

		track = parser.parseString(s);
		logger.debug("returned track: {}", track);
		assertThat("The list is not null.", track, notNullValue());
		assertThat("the track size is correct", track.size(),
				equalTo(10));

		n = track.get(0);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));
		n = track.get(1);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.D5));

		n = track.get(2);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.E5));

		n = track.get(3);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));
		n = track.get(4);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.D5));

		n = track.get(5);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.E5));

		n = track.get(6);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));
		n = track.get(7);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.D5));

		n = track.get(8);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.E5));

		n = track.get(9);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.F5));
	}

	@Test
	public void shouldParseRepeat() {
		String s;
		MIDITrack track;
		MIDINote n;

		s = "S+ X3 C D E";
		// should become C C C D E

		track = parser.parseString(s);
		logger.debug("track \n{}",track);
		assertThat("The list is not null.", track, notNullValue());
		assertThat("the track size is correct", track.size(),
				equalTo(5));

		n = track.get(0);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));

		n = track.get(1);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));

		n = track.get(2);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));

		n = track.get(3);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.D5));

		n = track.get(4);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the duration is correct", n.getDuration(),
				equalTo(1d));
		assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.E5));

	}

	@Test
	public void parseStringAppendMode() {
		String s;
		MIDITrack list;
		MIDINote n;

		s = "S+ C D E";

		list = parser.parseString(s);
		logger.debug("track \n{}",list);
		assertThat("The list is not null.", list, notNullValue());
		assertThat("the track size is correct", list.size(),
				equalTo(3));
		// assertThat(actual, is(equalTo(expected)));

		n = list.get(0);
		assertNotNull(n);
		assertEquals(Pitch.C5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);

		n = list.get(1);
		assertNotNull(n);
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(2d,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);

		n = list.get(2);
		assertNotNull(n);
		assertEquals(Pitch.E5,
				n.getMidiNumber());
		assertEquals(3d,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);
	}

	@Test
	public void parseStringAddMode() {
		String s;
		MIDITrack list;
		MIDINote n;

		s = "S= C D E";

		list = parser.parseString(s);
		logger.debug("track \n{}",list);
		assertThat("The list is not null.", list, notNullValue());
		assertThat("the track size is correct", list.size(),
				equalTo(3));
		// assertThat(actual, is(equalTo(expected)));

		n = list.get(0);
		assertNotNull(n);
		assertEquals(Pitch.C5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);

		n = list.get(1);
		assertNotNull(n);
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);

		n = list.get(2);
		assertNotNull(n);
		assertEquals(Pitch.E5,
				n.getMidiNumber());
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);

		s = "S=4.5 C D E";
		list = parser.parseString(s);
		logger.debug("track \n{}",list);
		assertThat("The list is not null.", list, notNullValue());
		assertThat("the track size is correct", list.size(),
				equalTo(3));
		// assertThat(actual, is(equalTo(expected)));

		n = list.get(0);
		assertNotNull(n);
		assertEquals(Pitch.C5,
				n.getMidiNumber());
		assertEquals(4.5,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);

		n = list.get(1);
		assertNotNull(n);
		assertEquals(Pitch.D5,
				n.getMidiNumber());
		assertEquals(4.5,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);

		n = list.get(2);
		assertNotNull(n);
		assertEquals(Pitch.E5,
				n.getMidiNumber());
		assertEquals(4.5,
				n.getStartBeat(),
				0d);
		assertEquals(1d,
				n.getDuration(),
				0d);
	}
}
