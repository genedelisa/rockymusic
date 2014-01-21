/**
 * 
 */
package com.rockhoppertech.music.examples;

/*
 * #%L
 * Rocky Music Examples
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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.midi.js.Instrument;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDINoteBuilder;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.MIDITrackFactory;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class TrackCreation {
	static Logger logger = LoggerFactory.getLogger(TrackCreation.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		create();
		createWithFactory();
		createMicromanaged();

	}

	public static void create() {
		MIDITrack track = new MIDITrack();
		track.setName("Test track");
		logger.debug(track.toString());

		track = new MIDITrack("C D E");
		track.setName("Test track from MIDI String");
		logger.debug(track.toString());

		List<MIDINote> list = new ArrayList<>();
		list.add(new MIDINote(Pitch.C5));
		list.add(new MIDINote(Pitch.D5));
		track = new MIDITrack(list);
		track.setName("Test track from List of MIDINotes");
		logger.debug(track.toString());

	}

	public static void createWithFactory() {
		MIDITrack track = null;
		track = MIDITrackFactory.createFromIntervals(1, 4, 5);
		track.setName("from intervals");
		logger.debug(track.toString());
		track.sequential();
		logger.debug(track.toString());

		track = MIDITrackFactory.createFromIntervals(Pitch.G3, 1, 2);

		track = MIDITrackFactory.createFromIntervals(
				new int[] { 1, 2 },
				Pitch.G5);

		track = MIDITrackFactory.createFromIntervals(
				new int[] { 1, 2 },
				Pitch.C5,
				1,
				true);
		track = MIDITrackFactory.createFromIntervals(
				new int[] { 1, 2 },
				Pitch.C5,
				1,
				true,
				2);

		track =  MIDITrackBuilder.create()
				.name("track from builder")
				.description("thing")
				.noteString("C D E")
				.durations(.5, 1d)
				.instrument(Instrument.HARP)
				.sequential()
				.build();

		track.play();
		logger.debug(track.toString());

	}

	static void createMicromanaged() {

		MIDINote note =  MIDINoteBuilder.create()
				.channel(1)
				.startBeat(1.5)
				.pitch(Pitch.C5)
				.duration(1d)
				.build();
		MIDINote note2 =  MIDINoteBuilder.create()
				.channel(1)
				.startBeat(3)
				.pitch(Pitch.D5)
				.duration(5d)
				.program(MIDIGMPatch.BANJO.getProgram())
				.build();

		MIDITrack track =  MIDITrackBuilder.create()
				.name("track from builder")
				.description("thing")
				.notes(note, note2)
				.sequential()
				.build();

		track.play();
		logger.debug(track.toString());

		track = MIDITrackBuilder.create()
				.name("track from builder with alto sax")
				.description("thing")
				.notes(note, note2)
				.instrument(Instrument.HARP) // overrides the note's inst
				.sequential()
				.build();
		logger.debug(track.toString());		

	}
}
