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

import java.util.ArrayList;
import java.util.List;

import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.modifiers.DurationModifier;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * Decided in this case to not make it a static inner class.
 * 
 * MIDITrack x = new
 * MIDITrackBuilder().name("test").description("thing").build(); or MIDITrack x
 * = new MIDITrack.Builder().name("test").noteString("C D E").build();
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDITrackBuilder {
	private String name;
	private String description;
	private String noteString;
	// private double duration = Duration.Q;
	private String startPitch = "C";
	private String scaleName;
	private List<Double> durationList;
	private List<MIDINote> noteList;
	private MIDIGMPatch instrument;
	private boolean sequential;

	private MIDITrackBuilder() {
	    
	}
	public static MIDITrackBuilder create() {
	    return new MIDITrackBuilder();
	}
	public MIDITrack build() {
		MIDITrack result = null;
		if (noteString != null && scaleName != null) {
			throw new IllegalArgumentException(
					"Make up your mind. string or scale?");
		}
		if (noteString != null) {
			result = new MIDITrack(noteString);
		} else if (noteList != null) {
			result = new MIDITrack(noteList);
		} else if (scaleName != null) {
			Scale scale = ScaleFactory.getScaleByName(scaleName);
			if (startPitch != null) {
				result = ScaleFactory.createMIDITrack(scale,
						startPitch);
			} else {
				result = ScaleFactory.createMIDITrack(scale);
			}

		} else {
			result = new MIDITrack();
		}

		result.setName(name);
		result.setDescription(description);

		if (durationList != null) {
			DurationModifier mod = new DurationModifier(durationList);
			result.map(mod);
			// } else if (this.duration != 0d) {
			// DurationModifier mod = new DurationModifier(this.duration);
			// result.map(mod);
		}

		if (instrument != null) {
			result.useInstrument(instrument);
		}

		if (sequential) {
			result.sequential();
		}

		reset();
		return result;
	}

	private void reset() {
		startPitch = "C";
		// this.duration = Duration.Q;
		noteString = null;
		scaleName = null;
		name = null;
		description = null;
		durationList = null;
		instrument = null;
	}

	public MIDITrackBuilder name(String name) {
		this.name = name;
		return this;
	}

	public MIDITrackBuilder description(String description) {
		this.description = description;
		return this;
	}

	public MIDITrackBuilder noteString(String noteString) {
		this.noteString = noteString;
		return this;
	}

	public MIDITrackBuilder scaleName(String scaleName) {
		this.scaleName = scaleName;
		return this;
	}

	public MIDITrackBuilder startPitch(String startPitch) {
		this.startPitch = startPitch;
		return this;
	}

	public MIDITrackBuilder sequential() {
		sequential = true;
		return this;
	}

	// public MIDITrackBuilder duration(double duration) {
	// this.duration = duration;
	// return this;
	// }

	public MIDITrackBuilder durations(double... durations) {
		durationList = new ArrayList<Double>();
		for (Double d : durations) {
			durationList.add(d);
		}
		return this;
	}

	public MIDITrackBuilder instrument(MIDIGMPatch instrument) {
		this.instrument = instrument;
		return this;
	}

	public MIDITrackBuilder notes(MIDINote... notes) {
		noteList = new ArrayList<>();
		for (MIDINote n : notes) {
			noteList.add(n);
		}
		return this;
	}

}