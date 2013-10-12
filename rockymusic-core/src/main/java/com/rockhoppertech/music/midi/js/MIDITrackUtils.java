package com.rockhoppertech.music.midi.js;

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
