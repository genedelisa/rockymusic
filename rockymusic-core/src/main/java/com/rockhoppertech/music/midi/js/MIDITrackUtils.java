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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.PitchFormat;

/**
 * @author <a href="http://rockhoppertech.com/">Gene De Lisa</a>
 * 
 */
public class MIDITrackUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(MIDITrackUtils.class);

	/**
	 * Display Notes to the logger
	 * 
	 * @param n
	 */
	public static void printPitches(MIDITrack n) {
		logger.debug("Pitches");
		logger.debug(getPitchesAsString(n));
	}

	public static String getPitchesAsString(MIDITrack n) {
		StringBuilder sb = new StringBuilder();
		for (MIDINote note : n) {
			sb.append(PitchFormat.getInstance().format(note.getPitch()))
					.append(' ');
		}
		return sb.toString();
	}

	public static String getPitchesMIDINumbersAsString(MIDITrack n) {
		StringBuilder sb = new StringBuilder();
		for (MIDINote note : n) {
			sb.append(note.getPitch().getMidiNumber()).append(' ');
		}
		return sb.toString();
	}

	/**
	 * Display Notes to the logger
	 * 
	 * @param n
	 */
	public static void printDurations(MIDITrack n) {
		logger.debug("Durations");

		logger.debug(getDurationsAsString(n));
	}

	public static String getDurationsAsString(MIDITrack n) {
		StringBuilder sb = new StringBuilder();
		for (MIDINote note : n) {
			sb.append(note.getDuration()).append(' ');

		}
		return sb.toString();
	}

	public static String getStartBeatsAsString(MIDITrack n) {
		StringBuilder sb = new StringBuilder();
		for (MIDINote note : n) {
			sb.append(note.getStartBeat()).append(' ');
		}
		return sb.toString();
	}

}
