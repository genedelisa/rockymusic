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
public class ScoreTest {

	@Test
	public void shouldHaveDefaultPitch() {

		Score score = new Score();
		MIDITrack track = new MIDITrack();
		score.add(track);
		MIDINote note = new MIDINote.Builder().build();
		track.add(note);
		note = new MIDINote.Builder().pitch(Pitch.E5).build();
		track.append(note);
		System.out.println(score);

		assertThat("The score is not null.", score, notNullValue());
		assertThat("the pitch is E5", note.getPitch().getMidiNumber(),
				equalTo(Pitch.E5));
		assertThat("there is 1 track", score.getTracks().size(), equalTo(1));
		assertThat("there are 2 notes on the track", track.getNotes().size(), equalTo(2));		
	}

}
