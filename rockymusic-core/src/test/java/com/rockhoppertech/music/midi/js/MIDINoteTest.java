package com.rockhoppertech.music.midi.js;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
//import static org.junit.Assert.*;

import org.junit.Test;

import com.rockhoppertech.music.Pitch;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDINoteTest {

	@Test
	public void shouldHaveDefaultPitch() {
		MIDINote note = new MIDINote.Builder().build();
		assertThat("The note is not null.", note, notNullValue());
		assertThat("the default pitch is C5", note.getPitch().getMidiNumber(),
				equalTo(Pitch.C5));
	}

	@Test
	public void shouldSetPitch() {
		MIDINote note = new MIDINote.Builder().pitch(Pitch.C4).build();
		assertThat("The note is not null.", note, notNullValue());
		assertThat("the default pitch is C4", note.getPitch().getMidiNumber(),
				equalTo(Pitch.C4));
	}
	
	@Test
	public void shouldSetPitchStartAndDuration() {
		MIDINote note = new MIDINote.Builder()
		.pitch(Pitch.C4)
		.duration(3d)
		.startBeat(1d)
		.build();
		assertThat("The note is not null.", note, notNullValue());
		assertThat("the default pitch is C4", note.getPitch().getMidiNumber(),
				equalTo(Pitch.C4));
		assertThat("the duration is 3", note.getDuration(),
				equalTo(3d));
		assertThat("the start beat is 1", note.getStartBeat(),
				equalTo(1d));
	}

}
