package com.rockhoppertech.music.midi.js;

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

	public MIDITrack build() {
		MIDITrack result = null;
		if (this.noteString != null && this.scaleName != null) {
			throw new IllegalArgumentException(
					"Make up your mind. string or scale?");
		}
		if (this.noteString != null) {
			result = new MIDITrack(this.noteString);
		} else if (this.noteList != null) {
			result = new MIDITrack(noteList);
		} else if (this.scaleName != null) {			
			Scale scale = ScaleFactory.getScaleByName(scaleName);
			if (this.startPitch != null) {
				result = ScaleFactory.createMIDITrack(scale,
						startPitch);
			} else {
				result = ScaleFactory.createMIDITrack(scale);
			}

		} else {
			result = new MIDITrack();
		}

		result.setName(this.name);
		result.setDescription(this.description);

		if (this.durationList != null) {
			DurationModifier mod = new DurationModifier(this.durationList);
			result.map(mod);
			// } else if (this.duration != 0d) {
			// DurationModifier mod = new DurationModifier(this.duration);
			// result.map(mod);
		}

		if (this.instrument != null) {
			result.useInstrument(this.instrument);
		}
		
		if (this.sequential) {
			result.sequential();
		}

		this.reset();
		return result;
	}

	private void reset() {
		this.startPitch = "C";
		// this.duration = Duration.Q;
		this.noteString = null;
		this.scaleName = null;
		this.name = null;
		this.description = null;
		this.durationList = null;
		this.instrument = null;
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
		this.sequential = true;
		return this;
	}

	// public MIDITrackBuilder duration(double duration) {
	// this.duration = duration;
	// return this;
	// }

	public MIDITrackBuilder durations(double... durations) {
		this.durationList = new ArrayList<Double>();
		for (Double d : durations) {
			this.durationList.add(d);
		}
		return this;
	}

	public MIDITrackBuilder instrument(MIDIGMPatch instrument) {
		this.instrument = instrument;
		return this;
	}

	

	public MIDITrackBuilder notes(MIDINote... notes) {
		this.noteList = new ArrayList<>();
		for (MIDINote n : notes) {
			this.noteList.add(n);
		}
		return this;
	}

}