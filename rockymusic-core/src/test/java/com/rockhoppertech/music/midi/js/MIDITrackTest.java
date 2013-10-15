/**
 * 
 */
package com.rockhoppertech.music.midi.js;

import static com.rockhoppertech.music.Pitch.C5;
import static com.rockhoppertech.music.Pitch.D5;
import static com.rockhoppertech.music.Pitch.E5;
import static com.rockhoppertech.music.Pitch.F5;
import static com.rockhoppertech.music.Pitch.G3;
import static com.rockhoppertech.music.Pitch.G5;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.parse.MIDIStringParser;
import com.rockhoppertech.music.modifiers.Modifier.Operation;
import com.rockhoppertech.music.modifiers.NoteModifier;
import com.rockhoppertech.music.modifiers.PitchModifier;

// removed Scale references

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDITrackTest {

	MIDITrack testTrack;

	@Before
	public void cmajor() {
		MIDIStringParser parser = new MIDIStringParser();
		this.testTrack = parser.parseString("C D E F G A B");
		assertNotNull(testTrack);
		assertThat("The test track is not null.", testTrack, notNullValue());

		MIDINote n = testTrack.get(0);
		assertThat("The note is not null.", n, notNullValue());
		assertThat("the note's pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#MIDITrack()}.
	 */
	@Test
	public void createMIDITrack() {
		MIDITrack track = new MIDITrack();
		assertThat("The track is not null.", track, notNullValue());
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#MIDITrack(com.rockhoppertech.music.midi.js.MIDITrack)}
	 * .
	 */
	@Test
	public void createMIDITrackFromMIDITrack() {
		MIDITrack track = new MIDITrack();
		track.add(new MIDINote());
		assertThat("The track is not null.", track, notNullValue());
		assertThat("the note's size is correct", track.size(),
				equalTo(1));

		MIDITrack track2 = new MIDITrack(track);
		assertThat("The track is not null.", track2, notNullValue());
		assertThat("the note's size is correct", track2.size(),
				equalTo(1));
	}

	/**

     */
	// @Test
	// public void createClickTrack() {
	// MIDITrack nl = new MIDITrack("c d e f c d e f");
	// nl.sequential();
	// MIDITrack ct = nl.createClickTrack();
	// System.err.println(nl);
	// System.err.println(ct);
	// assertNotNull(ct);
	// assertThat("track is non null",
	// ct,
	// notNullValue());
	// assertThat("size is correct",
	// ct.size(),
	// equalTo((int)nl.getEndBeat()-1));
	// }

	@Test
	public void fromString() {
		MIDITrack nl = new MIDITrack("c d e f c4 d e f");
		System.err.println(nl);
		assertThat("nl is non null",
				nl,
				notNullValue());
		assertThat("size is correct",
				nl.size(),
				equalTo(8));
		MIDINote note = nl.get(0);
		assertThat("note is non null",
				note,
				notNullValue());
		assertThat("note pitch correct",
				note.getPitch().getMidiNumber(),
				equalTo(C5));
		assertThat("note start correct",
				note.getStartBeat(),
				equalTo(1d));
		assertThat("note duration correct",
				note.getDuration(),
				equalTo(1d));

		nl = new MIDITrack("c,2,.5 d,3,1.5 e,4 f,5.5 c4,6 d,7 e,8 f,9");
		System.err.println(nl);
		assertThat("nl is non null",
				nl,
				notNullValue());
		assertThat("size is correct",
				nl.size(),
				equalTo(8));

		note = nl.get(0);
		assertThat("note is non null",
				note,
				notNullValue());
		assertThat("note pitch correct",
				note.getPitch().getMidiNumber(),
				equalTo(C5));
		assertThat("note start correct",
				note.getStartBeat(),
				equalTo(2d));
		assertThat("note duration correct",
				note.getDuration(),
				equalTo(.5));

		note = nl.get(1);
		assertThat("note is non null",
				note,
				notNullValue());
		assertThat("note pitch correct",
				note.getPitch().getMidiNumber(),
				equalTo(D5));
		assertThat("note start correct",
				note.getStartBeat(),
				equalTo(3d));
		assertThat("note duration correct",
				note.getDuration(),
				equalTo(1.5));

		note = nl.get(2);
		assertThat("note is non null",
				note,
				notNullValue());
		assertThat("note pitch correct",
				note.getPitch().getMidiNumber(),
				equalTo(E5));
		assertThat("note start correct",
				note.getStartBeat(),
				equalTo(4d));
		assertThat("note duration correct",
				note.getDuration(),
				equalTo(1d));

		note = nl.get(3);
		assertThat("note is non null",
				note,
				notNullValue());
		assertThat("note pitch correct",
				note.getPitch().getMidiNumber(),
				equalTo(F5));
		assertThat("note start correct",
				note.getStartBeat(),
				equalTo(5.5));
		assertThat("note duration correct",
				note.getDuration(),
				equalTo(1d));

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#MIDITrack(int[], java.lang.String)}
	 * .
	 */
	// @Test
	// public void createMIDITrackIntArrayString() {
	// MIDITrack list = new MIDITrack(new int[] { Pitch.C5, Pitch.EF5 },
	// MIDIUtils.PIANO_PATCH);
	// assertNotNull(list);
	// assertEquals(list.size(),
	// 2);
	// }

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#MIDITrack(int[], int[])}
	 * .
	 */
	// @Test
	// public void createMIDITrackIntArrayIntArray() {
	// MIDITrack list = new MIDITrack(new int[] { 60, 61 },
	// new double[] { 4d, 4d });
	// assertNotNull(list);
	// assertEquals(list.size(),
	// 2);
	// MIDINote note = list.get(0);
	// assertNotNull(note);
	// assertEquals(note.getMidiNumber(),
	// 60);
	// double delta = 0d;
	// assertEquals(4d,
	// note.getDuration(),
	// delta);
	//
	// }

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#MIDITrack(java.util.Collection)}
	 * .
	 */
	@Test
	public void createMIDITrackCollectionOfMIDINote() {
		Collection<MIDINote> collection = new ArrayList<MIDINote>();
		MIDITrack track = new MIDITrack(collection);

		assertThat("The track is not null.", track, notNullValue());
		assertThat("the track's size is correct", track.size(),
				equalTo(collection.size()));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#createFromTrack(double, javax.sound.midi.Track)}
	 * .
	 */
	// @Test
	// public void createFromTrack() {
	// MIDITrack list = new MIDITrack();
	// list.add(new MIDINote(Pitch.C5));
	// Sequence seq = list.toSequence(480);
	// Track track = seq.getTracks()[1];
	// assertNotNull(track);
	//
	// // track = list.tot
	// System.err.printf("Track size is %d\n",
	// track.size());
	// for (int i = 0; i < track.size(); i++) {
	// MidiEvent me = track.get(i);
	// System.err.println(MIDIUtils.toString(me));
	// }
	//
	// MIDITrack list2 = MIDITrack.createFromTrack(480,
	// track);
	// assertNotNull(list2);
	// assertEquals(list,
	// list2);
	// }

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrackFactory#createFromIntervals(int[], int)}
	 * .
	 */
	@Test
	public void createFromIntervalsIntArrayInt() {
		MIDITrack list = MIDITrackFactory.createFromIntervals(new int[] { 4 },
				Pitch.C5);
		assertNotNull(list);
		assertEquals(2,
				list.size());
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrackFactory#createFromIntervals(int[], int, int, boolean, int)}
	 * .
	 */
	@Test
	public void createFromIntervalsIntArrayIntIntBooleanInt() {
		boolean absolute = true;
		int unit = 1;
		int nOctaves = 1;

		MIDITrack list = MIDITrackFactory.createFromIntervals(new int[] { 4 },
				Pitch.C5,
				unit,
				absolute,
				nOctaves);
		assertNotNull(list);
		assertEquals(2,
				list.size());
		System.err.println(list);

		MIDINote c5 = new MIDINote(Pitch.C5);
		MIDINote e5 = new MIDINote(Pitch.E5);
		MIDINote n = list.get(0);
		assertNotNull(n);
		// System.err.println(n);
		assertEquals(c5.getMidiNumber(),
				n.getMidiNumber());
		assertEquals(c5,
				n);

		n = list.get(1);
		// System.err.println(n);
		assertNotNull(n);
		assertEquals(e5.getMidiNumber(),
				n.getMidiNumber());
		assertEquals(e5,
				n);

		absolute = false;
		list = MIDITrackFactory.createFromIntervals(new int[] { 4 },
				Pitch.C5,
				unit,
				absolute,
				nOctaves);
		assertNotNull(list);
		assertEquals(2,
				list.size());
		n = list.get(0);
		assertNotNull(n);
		assertEquals(new MIDINote(Pitch.C5),
				n);

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrackFactory#createFromIntervals(int[], int, int, boolean)}
	 * .
	 */
	@Test
	public void createFromIntervalsIntArrayIntIntBoolean() {
		boolean absolute = true;
		int unit = 1;
		MIDITrack list = MIDITrackFactory.createFromIntervals(new int[] { 4 },
				Pitch.C5,
				unit,
				absolute);
		assertNotNull(list);
		assertEquals(2,
				list.size());
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrackFactory#createFromPitches(int[])}
	 * .
	 */
	@Test
	public void createFromPitchesIntArray() {
		MIDITrack notelist = MIDITrackFactory.createFromPitches(new int[] { 60,
				72 });
		assertNotNull(notelist);
		assertEquals(2,
				notelist.size());
	}

	/**

     */
	@Test
	public void createFromPitchesIntegers() {
		MIDITrack notelist = MIDITrackFactory.createFromPitches(
				60, 72);
		assertNotNull(notelist);
		assertEquals(2,
				notelist.size());
		MIDINote n = new MIDINote(60);
		MIDINote n2 = notelist.get(0);
		System.out.println(n);
		System.out.println(n2);
		assertEquals(n,
				n2);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getInversion()}.
	 */
	@Test
	public void getInversion() {
		MIDITrack list = new MIDITrack();
		list.add(new MIDINote(Pitch.C5));
		list.add(new MIDINote(Pitch.E5));
		list.add(new MIDINote(Pitch.G5));
		MIDITrack inv = list.getInversion();
		assertNotNull(inv);
		assertEquals(3,
				inv.size());
		MIDINote n = inv.get(0);
		assertEquals(new MIDINote(Pitch.C5),
				n);
		n = inv.get(1);
		assertEquals(new MIDINote(Pitch.AF4),
				n);

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getPitchIntervals()}.
	 */
	@Test
	public void getPitchIntervals() {
		MIDITrack list = new MIDITrack();
		list.add(new MIDINote(Pitch.C5));
		list.add(new MIDINote(Pitch.E5));
		list.add(new MIDINote(Pitch.G5));
		int[] intervals = list.getPitchIntervals();
		assertNotNull(intervals);
		assertEquals(2,
				intervals.length);
		assertEquals(4,
				intervals[0]);
		assertEquals(3,
				intervals[1]);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getNoteDurations()}.
	 */
	@Test
	public void getNoteDurations() {
		MIDITrack list = new MIDITrack();
		list.add(new MIDINote(Pitch.C5));
		list.add(new MIDINote(Pitch.E5));
		list.add(new MIDINote(Pitch.G5));
		double[] durations = list.getNoteDurations();
		assertNotNull(durations);
		assertEquals(3,
				durations.length);
		assertEquals(1d,
				durations[0],
				0d);
		assertEquals(1d,
				durations[1],
				0d);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#retrograde()}.
	 */
	@Test
	public void retrograde() {
		MIDITrack list = new MIDITrack();
		MIDINote n1 = new MIDINote(Pitch.C5);
		MIDINote n2 = new MIDINote(Pitch.E5);
		MIDINote n3 = new MIDINote(Pitch.G5);
		list.add(n1);
		list.add(n2);
		list.add(n3);
		MIDITrack retro = list.retrograde();
		assertNotNull(retro);
		assertEquals(3,
				retro.size());
		assertEquals(n3,
				retro.get(0));
		assertEquals(n2,
				retro.get(1));
		assertEquals(n1,
				retro.get(2));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#sequential()}.
	 */
	@Test
	public void sequential() {
		MIDITrack list = new MIDITrack();
		list.add(new MIDINote(Pitch.C5));
		list.add(new MIDINote(Pitch.E5));
		list.add(new MIDINote(Pitch.G5));
		list.sequential();
		assertTrue(list.get(0).getStartBeat() < list.get(1).getStartBeat());
		assertTrue(list.get(1).getStartBeat() < list.get(2).getStartBeat());
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#sequential(int)}.
	 */
	@Test
	public void sequentialInt() {
		MIDITrack list = new MIDITrack();
		list.add(new MIDINote(Pitch.C5));
		list.add(new MIDINote(Pitch.E5));
		list.add(new MIDINote(Pitch.G5));
		int gap = 1;
		list.sequential(gap);
		assertTrue(list.get(0).getStartBeat() < list.get(1).getStartBeat());
		assertTrue(list.get(1).getStartBeat() < list.get(2).getStartBeat());

		MIDINote n1 = list.get(0);
		MIDINote n2 = list.get(1);
		double s2 = n1.getStartBeat() + n1.getDuration() + gap;
		assertEquals(s2,
				n2.getStartBeat(),
				0d);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#map(com.rockhoppertech.music.modifiers.NoteModifier)}
	 * .
	 */
	@Test
	public void mapNoteModifier() {
		MIDITrack list = new MIDITrack();
		list.add(C5,
				E5,
				G5);
		// transpose up an octave
		NoteModifier mod = new PitchModifier(Operation.ADD, 12);
		list.map(mod);
		assertEquals(PitchFactory.getPitch(Pitch.C6),
				list.get(0).getPitch());
		assertEquals(PitchFactory.getPitch(Pitch.E6),
				list.get(1).getPitch());
		assertEquals(PitchFactory.getPitch(Pitch.G6),
				list.get(2).getPitch());
	}

	/**
	 * these should be in modifier tests
	 */
	// @Test
	public void testMapNoteModifierDouble() {
		fail("Not yet implemented");
	}

	/**
     */
	// @Test
	public void testMapNoteModifierDoubleDouble() {
		fail("Not yet implemented");
	}

	/**
     */
	// @Test
	public void testMapNoteModifierModifierCriteria() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#sublist(double, double, boolean)}
	 * .
	 */
	@Test
	public void sublistDoubleDoubleBoolean() {
		boolean clone = false;
		boolean endInclusive = false;
		testTrack.sequential();
		MIDITrack nl = testTrack.sublist(2d,
				3d,
				clone,
				endInclusive);
		assertNotNull(nl);
		assertEquals(1,
				nl.size());
		MIDINote n = nl.get(0);
		assertNotNull(n);
		assertEquals(2d,
				n.getStartBeat(),
				0d);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#sublist(int)}.
	 */
	@Test
	public void sublistInt() {
		// test track has 7 notes
		testTrack.sequential();
		MIDITrack nl = testTrack.sublist(2);
		assertNotNull(nl);
		assertEquals(5,
				nl.size());
		// 7 - 2 = 5

		MIDINote n = nl.get(0);
		assertNotNull(n);
		assertEquals(3d,
				n.getStartBeat(),
				0d);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#sublist(com.rockhoppertech.music.Pitch, com.rockhoppertech.music.Pitch)}
	 * .
	 */
	@Test
	public void sublistPitchPitch() {
		testTrack.sequential();
		Pitch p1 = PitchFactory.getPitch(Pitch.C5);
		Pitch p2 = PitchFactory.getPitch(Pitch.G5);

		MIDITrack nl = testTrack.sublist(p1,
				p2);
		assertNotNull(nl);
		assertEquals(5,
				nl.size());
		MIDINote n = nl.get(0);
		assertNotNull(n);
		assertEquals(p1,
				n.getPitch());

		assertTrue(testTrack.contains(PitchFactory.getPitch(Pitch.A5)));
		assertFalse(nl.contains(PitchFactory.getPitch(Pitch.A5)));

		System.err.println(nl);
	}

	@Test
	public void contains() {
		assertTrue(testTrack.contains(PitchFactory.getPitch(Pitch.C5)));
		assertTrue(testTrack.contains(PitchFactory.getPitch(Pitch.D5)));
		assertTrue(testTrack.contains(PitchFactory.getPitch(Pitch.E5)));
		assertTrue(testTrack.contains(PitchFactory.getPitch(Pitch.F5)));
		assertTrue(testTrack.contains(PitchFactory.getPitch(Pitch.G5)));
		assertTrue(testTrack.contains(PitchFactory.getPitch(Pitch.A5)));
		assertTrue(testTrack.contains(PitchFactory.getPitch(Pitch.B5)));
		// assertTrue(testTrack.contains(PitchFactory.getPitch(Pitch.C6)));

		assertFalse(testTrack.contains(PitchFactory.getPitch(Pitch.DF5)));
		assertFalse(testTrack.contains(PitchFactory.getPitch(Pitch.EF5)));
		assertFalse(testTrack.contains(PitchFactory.getPitch(Pitch.FS5)));
		assertFalse(testTrack.contains(PitchFactory.getPitch(Pitch.GF5)));
		assertFalse(testTrack.contains(PitchFactory.getPitch(Pitch.AF5)));
		assertFalse(testTrack.contains(PitchFactory.getPitch(Pitch.BF5)));
		assertFalse(testTrack.contains(PitchFactory.getPitch(Pitch.D6)));

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#set(int, com.rockhoppertech.music.midi.js.MIDINote)}
	 * .
	 */
	// @Test
	// public void set() {
	// MIDINote n1 = new MIDINote(Pitch.EF0);
	// testTrack.set(0,
	// n1);
	// MIDINote n = testTrack.get(0);
	// assertEquals(n1,
	// n);
	// }

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#add(com.rockhoppertech.music.midi.js.MIDINote)}
	 * .
	 */
	@Test
	public void addMIDINote() {

		MIDINote n1 = new MIDINote(Pitch.C5);
		testTrack.add(n1);
		MIDINote n = testTrack.get(testTrack.size() - 1);
		assertEquals(n1,
				n);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#add(java.lang.String)}.
	 */
	@Test
	public void addString() {

		testTrack.add("C5");
		MIDINote n = testTrack.get(testTrack.size() - 1);
		MIDINote n1 = new MIDINote(Pitch.C5);
		assertEquals(n1,
				n);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#add(java.lang.String, double)}
	 * .
	 */
	@Test
	public void addStringDouble() {

		testTrack.add("C5",
				1d);
		MIDINote n = testTrack.get(testTrack.size() - 1);
		MIDINote n1 = new MIDINote(Pitch.C5);
		assertEquals(n1,
				n);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#add(java.lang.String, double, double)}
	 * .
	 */
	@Test
	public void addStringDoubleDouble() {

		testTrack.add("C5",
				2d,
				3d);
		MIDINote n = testTrack.get(testTrack.size() - 1);
		MIDINote n1 = new MIDINote(Pitch.C5, 2d, 3d);
		assertEquals(n1,
				n);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#remove(com.rockhoppertech.music.midi.js.MIDINote)}
	 * .
	 */
	@Test
	public void removeMIDINote() {
		MIDINote n1 = new MIDINote(Pitch.C5);
		testTrack.add(n1);
		int size = testTrack.size();

		testTrack.remove(n1);
		assertNotSame(testTrack.size(),
				size);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#remove(int)}.
	 */
	@Test
	public void removeInt() {
		MIDINote n1 = new MIDINote(Pitch.C5);
		testTrack.add(n1);
		int size = testTrack.size();

		testTrack.remove(0);
		assertNotSame(testTrack.size(),
				size);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#add(com.rockhoppertech.music.midi.js.MIDIEvent)}
	 * .
	 */
	// @Test
	public void testAddMIDIEvent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#remove(com.rockhoppertech.music.midi.js.MIDIEvent)}
	 * .
	 */
	// @Test
	public void testRemoveMIDIEvent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#append(com.rockhoppertech.music.midi.js.MIDINote)}
	 * .
	 */
	@Test
	public void append() {
		MIDITrack list = new MIDITrack();
		list.append(new MIDINote(Pitch.D3));
		list.append(new MIDINote(Pitch.G3));
		list.append(new MIDINote(Pitch.G3));
		assertEquals(3,
				list.size());
		MIDINote n = list.get(0);
		assertEquals(1d,
				n.getStartBeat(),
				0d);
		n = list.get(1);
		assertEquals(2d,
				n.getStartBeat(),
				0d);
		n = list.get(2);
		assertEquals(3d,
				n.getStartBeat(),
				0d);

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#merge(com.rockhoppertech.music.midi.js.MIDITrack)}
	 * .
	 */
	@Test
	public void mergeMIDITrack() {
		MIDITrack list = new MIDITrack();
		list.append(new MIDINote(Pitch.D3));
		list.append(new MIDINote(Pitch.G3));
		list.append(new MIDINote(Pitch.G3));
		assertEquals(7,
				testTrack.size());
		testTrack.merge(list);
		assertEquals(10,
				testTrack.size());

		MIDINote n = list.get(0);
		assertEquals(1d,
				n.getStartBeat(),
				0d);

		n = testTrack.getLastNote();
		assertEquals(n.getPitch(),
				PitchFactory.getPitch(Pitch.G3));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#sort(java.util.Comparator)}
	 * .
	 */
	@Test
	public void sort() {
		Comparator<MIDINote> comp = new Comparator<MIDINote>() {
			public int compare(MIDINote o1, MIDINote o2) {
				if (o1.getMidiNumber() > o2.getMidiNumber()) {
					return 1;
				} else if (o1.getMidiNumber() < o2.getMidiNumber()) {
					return -1;
				}
				return 0;
			}
		};
		testTrack.sort(comp);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#sortByAscendingPitches()}
	 * .
	 */
	@Test
	public void sortByAscendingPitches() {
		MIDITrack retro = testTrack.retrograde();
		for (int i = 0; i < retro.size() - 1; i++) {
			MIDINote n1 = retro.get(i);
			MIDINote n2 = retro.get(i + 1);
			assertFalse(n1.getMidiNumber() < n2.getMidiNumber());
		}
		testTrack.sortByAscendingPitches();
		for (int i = 0; i < testTrack.size() - 1; i++) {
			MIDINote n1 = testTrack.get(i);
			MIDINote n2 = testTrack.get(i + 1);
			assertTrue(n1.getMidiNumber() < n2.getMidiNumber());
		}

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#insertListAtIndex(com.rockhoppertech.music.midi.js.MIDITrack, int)}
	 * .
	 */
	@Test
	public void insertListAtIndex() {
		MIDITrack notelist = new MIDITrack();
		MIDINote n = new MIDINote(60);
		MIDINote n2 = new MIDINote(61);
		notelist.add(n);
		notelist.add(n2);
		notelist.sequential();
		assertEquals(2,
				notelist.size());
		System.out.println(notelist);

		MIDITrack notelist2 = new MIDITrack();
		MIDINote nn = new MIDINote(72);
		notelist2.add(nn);
		assertEquals(1,
				notelist2.size());
		System.out.println(notelist2);

		MIDITrack merged = notelist.insertListAtIndex(notelist2,
				1);
		assertNotNull(merged);
		assertEquals(3,
				merged.size());
		assertEquals(nn,
				merged.get(1));
		System.out.println(merged);

		merged = notelist.insertListAtIndex(notelist2,
				0);
		assertNotNull(merged);
		assertEquals(3,
				merged.size());
		assertEquals(nn,
				merged.get(0));
		System.out.println(merged);

		merged = notelist.insertListAtIndex(notelist2,
				2);
		assertNotNull(merged);
		assertEquals(3,
				merged.size());
		assertEquals(nn,
				merged.get(2));
		System.out.println(merged);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#merge(com.rockhoppertech.music.midi.js.MIDITrack, int, boolean)}
	 * .
	 */
	// @Test
	public void testMergeMIDITrackIntBoolean() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#append(com.rockhoppertech.music.midi.js.MIDITrack)}
	 * .
	 */
	// @Test
	public void testAppendMIDITrack() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#append(com.rockhoppertech.music.midi.js.MIDITrack, double)}
	 * .
	 */
	// @Test
	public void testAppendMIDITrackDouble() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#append(com.rockhoppertech.music.midi.js.MIDITrack, double, int, int)}
	 * .
	 */
	// @Test
	public void testAppendMIDITrackDoubleIntInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#remove(com.rockhoppertech.music.midi.js.MIDITrack)}
	 * .
	 */
	// @Test
	public void testRemoveMIDITrack() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#retain(com.rockhoppertech.music.midi.js.MIDITrack)}
	 * .
	 */
	@Test
	public void retain() {
		MIDITrack list = new MIDITrack();
		list.append(testTrack.get(0));
		list.append(testTrack.getLastNote());
		testTrack.retain(list);
		assertEquals(2,
				testTrack.size());

		System.err.println(testTrack);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#clear()}.
	 */
	@Test
	public void clear() {

		assertThat("the track size is correct", testTrack.size(),
				greaterThan(0));

		testTrack.clear();

		assertThat("the track size is correct", testTrack.size(),
				equalTo(0));

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getStartBeat()}.
	 */
	// @Test
	public void testGetStartBeat() {
		//double sb = testTrack.getStartBeat();

		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getEndBeat()}.
	 */
	// @Test
	public void testGetEndBeat() {
		//double sb = testTrack.getEndBeat();
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getEndBeatOfNote(com.rockhoppertech.music.midi.js.MIDINote)}
	 * .
	 */
	// @Test
	public void testGetEndBeatOfNote() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getLowestPitchedNote()}
	 * .
	 */
	@Test
	public void getLowestPitchedNote() {
		MIDINote lowest = testTrack.getLowestPitchedNote();
		assertEquals(new MIDINote(Pitch.C5),
				lowest);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getHighestPitchedNote()}
	 * .
	 */
	@Test
	public void getHighestPitchedNote() {
		MIDINote n = testTrack.getHighestPitchedNote();
		assertEquals(new MIDINote(Pitch.B5),
				n);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getDuration()}.
	 */
	// @Test
	public void testGetDuration() {
		double d = testTrack.getDuration();
		System.err.println("duration is " + d);
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#channelize()}.
	 */
	@Test
	public void channelize() {
		MIDITrack list = new MIDITrack();

		MIDINote n = new MIDINote(Pitch.C5);
		n.setChannel(1);
		list.add(n);

		MIDINote n1 = new MIDINote(Pitch.D5);
		n1.setChannel(2);
		list.add(n1);

		List<List<MIDINote>> channelList = list.channelize();
		assertEquals(2,
				channelList.size());

		for (List<MIDINote> cl : channelList) {
			assertEquals(1,
					cl.size());
		}

		List<MIDINote> cl = channelList.get(0);
		MIDINote mn = cl.get(0);
		assertSame(n,
				mn);

		cl = channelList.get(1);
		mn = cl.get(0);
		assertSame(n1,
				mn);
	}

	@Test
	public void shoudCreateBriefMIDIString() {
		String s = testTrack.toBriefMIDIString();
		assertThat("The string is not null.", s, notNullValue());
		assertThat(
				"the string's contents are correct",
				s.trim(),
				equalToIgnoringCase("S+ C5 ,1.0 D5 ,1.0 E5 ,1.0 F5 ,1.0 G5 ,1.0 A5 ,1.0 B5 ,1.0"));
	}

	@Test
	public void shoudCreateMIDIString() {
		String s = testTrack.toMIDIString();
		StringBuilder sb = new StringBuilder();
		sb.append("C5 ,1.000000,1.000000,64,64,0,0,0,0").append('\n');
		sb.append("D5 ,1.000000,1.000000,64,64,0,0,0,0").append('\n');
		sb.append("E5 ,1.000000,1.000000,64,64,0,0,0,0").append('\n');
		sb.append("F5 ,1.000000,1.000000,64,64,0,0,0,0").append('\n');
		sb.append("G5 ,1.000000,1.000000,64,64,0,0,0,0").append('\n');
		sb.append("A5 ,1.000000,1.000000,64,64,0,0,0,0").append('\n');
		sb.append("B5 ,1.000000,1.000000,64,64,0,0,0,0");
		
		// logger.debug("MIDI String: {}", s);
		System.out.println(s);
		
		assertThat("The string is not null.", s, notNullValue());
		assertThat(
				"the string's contents are correct",
				s.trim(),
				equalTo(sb.toString()));
		
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#setStartBeat(double)}.
	 */
	@Test
	public void setStartBeat() {
		testTrack.setStartBeat(10d);
		MIDINote n = testTrack.get(0);

		assertThat(
				"the returned track start beat is correct",
				testTrack.getStartBeat(),
				equalTo(10d));

		assertThat("the first note's start beat is correct", n.getStartBeat(),
				equalTo(10d));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getPitchesAsString(com.rockhoppertech.music.midi.js.MIDITrack)}
	 * .
	 */
	@Test
	public void getPitchesAsString() {
		String s = MIDITrack.getPitchesAsString(testTrack);
		System.err.println(s);
		assertThat("The string is not null.", s, notNullValue());
		assertThat("the string's contents are correct", s.trim(),
				equalToIgnoringCase("C5  D5  E5  F5  G5  A5  B5"));

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#getDurationsAsString(com.rockhoppertech.music.midi.js.MIDITrack)}
	 * .
	 */
	@Test
	public void getDurationsAsString() {
		String s = MIDITrack.getDurationsAsString(testTrack);
		System.err.println(s);

		assertThat("The string is not null.", s, notNullValue());
		assertThat("the string's contents are correct", s.trim(),
				equalTo("1.0 1.0 1.0 1.0 1.0 1.0 1.0"));

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#indexOfNote(com.rockhoppertech.music.midi.js.MIDINote)}
	 * .
	 */
	@Test
	public void indexOfNote() {
		MIDINote n = testTrack.getLastNote();
		int index = testTrack.indexOfNote(n);
		assertEquals(testTrack.size() - 1,
				index);
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#changeStartBeatOfNoteAtIndex(int, double)}
	 * .
	 */
	@Test
	public void shouldChangeStartBeatOfNoteAtIndex() {

		testTrack.changeStartBeatOfNoteAtIndex(0,
				5d);
		MIDINote n = testTrack.get(0);
		assertThat("the note's start beat is correct", n.getStartBeat(),
				equalTo(5d));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#changeDurationOfNoteAtIndex(int, double)}
	 * .
	 */
	@Test
	public void shouldChangeDurationOfNoteAtIndex() {

		testTrack.changeDurationOfNoteAtIndex(0,
				5d);
		MIDINote n = testTrack.get(0);

		assertThat("the note's duration is correct", n.getDuration(),
				equalTo(5d));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#changeEndBeatOfNoteAtIndex(int, double)}
	 * .
	 */
	@Test
	public void shouldChangeEndBeatOfNoteAtIndex() {

		testTrack.changeEndBeatOfNoteAtIndex(0,
				5d);
		MIDINote n = testTrack.get(0);

		assertThat("the note's end beat is correct", n.getEndBeat(),
				equalTo(5d));

	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#changePitchOfNoteAtIndex(int, int)}
	 * .
	 */
	@Test
	public void shouldChangePitchOfNoteAtIndex() {

		testTrack.changePitchOfNoteAtIndex(0,
				Pitch.G3);
		MIDINote n = testTrack.get(0);

		assertThat("the note's pitch is correct", n.getMidiNumber(),
				equalTo(G3));
	}

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#setDuration(double)}.
	 */
	// @Test
	// public void testSetDuration() {
	// testTrack.sequential();
	// System.err.println(testTrack);
	// testTrack.setDuration(16);
	// System.err.println(testTrack);
	// // TODO boy does this need work
	// fail("Not yet implemented");
	// }

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#asList()}.
	 */
	// @Test
	// public void asList() {
	// List<MIDINote> list = testTrack.asList();
	// assertNotNull(list);
	// assertEquals(list.size(),
	// testTrack.size());
	// }

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#asList(boolean)}.
	 */
	// @Test
	// public void asListBoolean() {
	// List<MIDINote> list = testTrack.asList(true);
	// assertNotNull(list);
	// assertEquals(list.size(),
	// testTrack.size());
	// }

	/**
	 * Test method for
	 * {@link com.rockhoppertech.music.midi.js.MIDITrack#toArray()}.
	 */
	// @Test
	// public void toArray() {
	// MIDINote[] a = testTrack.toArray();
	// assertNotNull(a);
	// assertEquals(a.length,
	// testTrack.size());
	// }
}
