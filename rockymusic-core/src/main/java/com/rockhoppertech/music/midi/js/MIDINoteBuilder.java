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

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDINoteBuilder {
    private int pitch = MIDINote.defaultPitch;
    private double startBeat = MIDINote.defaultStartbeat;
    private double duration = MIDINote.defaultDuration; // in beats
    private boolean rest = false;
    private int pan = MIDINote.defaultPan;
    private int bank = MIDINote.defaultBank;
    private int channel = MIDINote.defaultChannel;
    private int pitchBend = MIDINote.defaultBend;
    private int program = MIDINote.defaultProgram;
    private int velocity = MIDINote.defaultVelocity;
    private int voice = MIDINote.defaultVoice;

    // public Builder(int pitch, double startBeat) {
    // this.pitch = pitch;
    // this.startBeat = startBeat;
    // }
    public static final MIDINoteBuilder create() {
        return new MIDINoteBuilder();
    }

    private MIDINoteBuilder() {

    }

    public MIDINote build() {
        MIDINote result = new MIDINote(pitch);
        result.setStartBeat(startBeat);
        result.setDuration(duration);
        result.setRest(rest);
        result.setPan(pan);
        result.setBank(bank);
        result.setChannel(channel);
        result.setPitchBend(pitchBend);
        result.setProgram(program);
        result.setVelocity(velocity);
        result.setVoice(voice);
        return result;
    }

    public MIDINoteBuilder pitch(int pitchNumber) {
        pitch = pitchNumber;
        return this;
    }

    public MIDINoteBuilder startBeat(double value) {
        startBeat = value;
        return this;
    }

    public MIDINoteBuilder duration(double beats) {
        duration = beats;
        return this;
    }

    public MIDINoteBuilder rest(boolean value) {
        rest = value;
        return this;
    }

    public MIDINoteBuilder pan(int value) {
        pan = value;
        return this;
    }

    public MIDINoteBuilder bank(int value) {
        bank = value;
        return this;
    }

    public MIDINoteBuilder channel(int value) {
        channel = value;
        return this;
    }

    public MIDINoteBuilder pitchBend(int value) {
        pitchBend = value;
        return this;
    }

    public MIDINoteBuilder program(int value) {
        program = value;
        return this;
    }

    public MIDINoteBuilder velocity(int value) {
        velocity = value;
        return this;
    }

    public MIDINoteBuilder voice(int value) {
        voice = value;
        return this;
    }
}
