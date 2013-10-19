package com.rockhoppertech.music.midi.js;

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

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
//import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ScoreTest {
	private static final Logger logger = LoggerFactory
			.getLogger(ScoreTest.class);

	@Test
	public void shouldHaveDefaultPitch() {

		Score score = new Score();
		MIDITrack track = new MIDITrack();
		score.add(track);
		MIDINote note = new MIDINote.Builder().build();
		track.add(note);
		note = new MIDINote.Builder().pitch(Pitch.E5).build();
		track.append(note);
		logger.debug("score with E5 \n{}", score);

		assertThat("The score is not null.", score, notNullValue());
		assertThat("the pitch is E5", note.getPitch().getMidiNumber(),
				equalTo(Pitch.E5));
		// Score automatically creates a metaevent track
		assertThat("there are 2 tracks", score.getTracks().size(), equalTo(2));
		assertThat(
				"there are 2 notes on the track",
				track.getNotes().size(),
				equalTo(2));
	}

}
