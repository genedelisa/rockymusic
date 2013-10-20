package com.rockhoppertech.music.midi.js;

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
