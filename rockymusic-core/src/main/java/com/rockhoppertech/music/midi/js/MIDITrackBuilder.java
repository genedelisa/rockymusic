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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.modifiers.DurationModifier;
import com.rockhoppertech.music.modifiers.Modifier;
import com.rockhoppertech.music.modifiers.StartBeatModifier;
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
    private static final Logger logger = LoggerFactory
            .getLogger(MIDITrackBuilder.class);
    private String name;
    private String description;
    private String noteString;
    // private double duration = Duration.Q;
    private String startPitch = "C";
    private String scaleName;
    private List<Double> durationList;
    private List<MIDINote> noteList;
   // private MIDIGMPatch instrument;
    private boolean sequential;
    private double startBeat = -1d;
    private int nScaleOctaves = 1;
    boolean chord = false;
    private int repeats = 1;
    private Instrument instrument;

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
                // result = ScaleFactory.createMIDITrack(scale,
                // startPitch);
                result = ScaleFactory.createMIDITrack(
                        scale,
                        PitchFactory.getPitch(
                                startPitch).getMidiNumber(),
                        startBeat == -1 ? 1d : startBeat,
                        1d,
                        nScaleOctaves);
            } else {
                // result = ScaleFactory.createMIDITrack(scale);
                result = ScaleFactory.createMIDITrack(
                        scale,
                        Pitch.C5,
                        1d,
                        1d,
                        nScaleOctaves);
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

        logger.debug("startBeat is {}", startBeat);
        if (startBeat != -1d) {
            Modifier.Operation op;
            // if(chord) {
            // op = Modifier.Operation.SET;
            // logger.debug("op is set, startBeat is {}", startBeat);
            // } else {
            // op = Modifier.Operation.ADD;
            // logger.debug("op is add, startBeat is {}", startBeat);
            // }

            op = Modifier.Operation.SET;
            logger.debug("op is set, startBeat is {}", startBeat);
            StartBeatModifier mod = new StartBeatModifier(op, startBeat);
            result.map(mod);
        }

        // this needs to be after setting the start beat
        if (sequential) {
            result.sequential();
        }

        if (repeats > 1) {
            result = repeat(result, repeats);
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
        startBeat = -1d;
        chord = false;
        nScaleOctaves = 1;
        repeats = 1;
    }

    private static MIDITrack repeat(final MIDITrack track,
            final int numberOfRepeats) {
        MIDITrack repeated = new MIDITrack(track);

        for (int i = 0; i < numberOfRepeats; i++) {
            repeated.append(track);
        }
        return repeated.sequential();
    }

    public MIDITrackBuilder nScaleOctaves(int nScaleOctaves) {
        this.nScaleOctaves = nScaleOctaves;
        return this;
    }

    public MIDITrackBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    public MIDITrackBuilder repeats(int repeats) {
        this.repeats = repeats;
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

    public MIDITrackBuilder chord() {
        chord = true;
        startBeat = 1d;
        return this;
    }

    public MIDITrackBuilder chord(double startBeat) {
        this.startBeat = startBeat;
        return this;
    }

    public MIDITrackBuilder startBeat(double startBeat) {
        chord = false;
        this.startBeat = startBeat;
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

    public MIDITrackBuilder instrument(Instrument instrument) {
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
