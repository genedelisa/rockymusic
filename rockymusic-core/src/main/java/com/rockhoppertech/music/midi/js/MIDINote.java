/**
 * 
 */
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

import com.rockhoppertech.music.Note;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.midi.parse.MIDIStringParser;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */

public class MIDINote extends Note {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory
            .getLogger(MIDINote.class);

    static final int defaultPitch = Pitch.C5;
    static final double defaultStartbeat = 1d;
    static final double defaultDuration = 1d;
    static final int defaultChannel = 0;
    static final int defaultVelocity = 64;
    static final int defaultProgram = 0;
    static final int defaultBend = 0;
    static final int defaultBank = 0;
    static final int defaultVoice = 0;
    static final int defaultPan = 64; // center

    // MIDI attributes in addition to "regular" Note attributes
    private int channel;
    private int bank;
    private int program;
    private int velocity;
    private int pitchbend;

    /**
     * CC 10 0x0a (coarse) 0 = left 64 = center 127 = right most devices ignore
     * fine. Pan should effect all notes on the channel, including notes that
     * were triggered prior to the pan message being received, and are still
     * sustaining.
     */
    private int pan = defaultPan;
    /**
     * not exactly midi
     */
    private int voice;

    /**
     * All default values.
     */
    public MIDINote() {
        this(defaultPitch, defaultStartbeat, defaultDuration, defaultChannel,
                defaultVelocity, defaultProgram, defaultBend, defaultBank,
                defaultVoice);
    }

    /**
     * Initialize a new MIDINote with the specified pitch MIDI number. It will
     * use the PitchFactory to retrieve a Pitch.
     * <p>
     * Other instance variables are instantiated as follows:
     * <ul>
     * <li>startbeat = 1
     * <li>duration = 1
     * <li>channel = 0
     * <li>bank = 0
     * <li>program = 0
     * <li>velocity = 64
     * <li>pitchbend = 0
     * <li>voice = 0
     * </ul>
     * 
     * <blockquote>
     * 
     * <pre>
     * MIDINote n1 = new MIDINote(Pitch.C5);
     * </pre>
     * 
     * </blockquote>
     * 
     * @param pitch
     *            The pitch midi number where 60 = middle C. (C5 in our
     *            numbering not C4)
     */
    public MIDINote(final int pitch) {
        this(pitch, defaultStartbeat, defaultDuration, defaultChannel,
                defaultVelocity, defaultProgram, defaultBend, defaultBank,
                defaultVoice);
    }

    public MIDINote(final int pitch, final double startbeat,
            final double duration) {
        this(pitch, startbeat, duration, defaultChannel, defaultVelocity,
                defaultProgram, defaultBend, defaultBank, defaultVoice);
    }

    public MIDINote(final int pitch, final double startbeat,
            final double duration, final int channel) {
        this(pitch, startbeat, duration, channel, defaultVelocity,
                defaultProgram, defaultBend, defaultBank, defaultVoice);
    }

    public MIDINote(final int pitch, final double startbeat,
            final double duration, final int channel, final int velocity,
            final int program) {
        this(pitch, startbeat, duration, channel, velocity, program,
                defaultBend, defaultBank, defaultVoice);
    }

    public MIDINote(final int pitch, final double startbeat,
            final double duration, final int channel, final int velocity,
            final int program, final int bend) {
        this(pitch, startbeat, duration, channel, velocity, program, bend,
                defaultBank, defaultVoice);

    }

    /**
     * This is the constructor that is called by the other constructors which
     * are just conveniences. <blockquote>
     * 
     * <pre>
     * MIDINote n1 = new MIDINote(Pitch.C5, 1d, 1d, 1, 64, 1, 0, 0, 0);
     * </pre>
     * 
     * </blockquote>
     * 
     * @param pitch
     * @param startbeat
     * @param duration
     * @param chan
     * @param velocity
     * @param program
     * @param bend
     * @param bank
     * @param voice
     * @see MIDINoteBuilder
     */

    public MIDINote(final int pitch, final double startbeat,
            final double duration, final int channel, final int velocity,
            final int program, final int bend, final int bank, final int voice) {
        super(pitch, startbeat, duration);
        this.channel = channel;
        this.bank = bank;
        this.program = program;
        this.velocity = velocity;
        pitchbend = bend;
        this.voice = voice;
    }

    public MIDINote(final MIDINote n) {
        super(n.getPitch(), n.getStartBeat(), n.getDuration());
        channel = n.getChannel();
        bank = n.getBank();
        program = n.getProgram();
        velocity = n.getVelocity();
        pitchbend = n.getPitchBend();
        voice = n.getVoice();
    }

    /**
     * Initialize a new MIDINote with the specified pitch.
     * <p>
     * Other instance variables are instantiated as follows:
     * <ul>
     * <li>startbeat = 1
     * <li>duration = 1
     * <li>channel = 1
     * <li>bank = 0
     * <li>program = 1
     * <li>velocity = 64
     * <li>pitchbend = 0
     * <li>voice = 1
     * </ul>
     * 
     * <blockquote>
     * 
     * <pre>
     * MIDINote n1 = new MIDINote(PitchFactory.getPitch(Pitch.C5));
     * </pre>
     * 
     * </blockquote>
     * 
     * @param pitch
     *            The pitch instance. Use a PitchFactory to retrieve one.
     */
    public MIDINote(final Pitch pitch) {
        this(pitch.getMidiNumber(), defaultStartbeat, defaultDuration,
                defaultChannel, defaultVelocity, defaultProgram, defaultBend,
                defaultBank, defaultVoice);
    }

    public MIDINote(final Pitch pitch, final double startbeat,
            final double duration) {
        this(pitch.getMidiNumber(), startbeat, duration, defaultChannel,
                defaultVelocity, defaultProgram, defaultBend, defaultBank,
                defaultVoice);
    }

    public MIDINote(final Pitch pitch, final double startbeat,
            final double duration, final int chan, final int velocity,
            final int program, final int bend) {
        this(pitch.getMidiNumber(), startbeat, duration);
    }

    public MIDINote(final Pitch pitch, final double startbeat,
            final double duration, final int chan, final int velocity,
            final int program, final int bend, final int voice) {
        this(pitch.getMidiNumber());
    }

    public MIDINote(final String pitch, final double startbeat,
            final double duration) {
        this(PitchFactory.getPitchByName(pitch).getMidiNumber(), startbeat,
                duration, defaultChannel, defaultVelocity, defaultProgram,
                defaultBend, defaultBank, defaultVoice);
    }

    public MIDINote(final String pitch, final double startbeat,
            final double duration, final int chan, final int velocity,
            final int program, final int bend) {
        this(PitchFactory.getPitchByName(pitch).getMidiNumber(), startbeat,
                duration, chan, velocity, program, bend, defaultBank,
                defaultVoice);
    }

    /**
     * useful for percussion tracks
     * 
     * new MIDINote(HI_WOOD_BLOCK_PERC_PATCH, 1d, 1d, 9);
     * 
     * @param patch
     * @param startBeat
     * @param duration
     * @param channel
     */
    public MIDINote(MIDIGMPatch patch, double startBeat, double duration,
            int channel) {
        this(patch.getProgram(), startBeat, duration, channel);
    }

    /**
     * cloning sucks. Just say no.
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

   

    /**
     * Cloning sucks. http://www.artima.com/intv/bloch13.html
     * 
     * @return a duplicate of this instance
     */
    public MIDINote duplicate() {
        final MIDINote clone = new MIDINote(getPitch().getMidiNumber(),
                getStartBeat(), getDuration(), channel,
                velocity, program, pitchbend, bank,
                voice);
        return clone;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o instanceof MIDINote) == false) {
            return false;
        }

        final MIDINote n = (MIDINote) o;

        if (n.channel != channel) {
            return false;
        }
        if (n.bank != bank) {
            return false;
        }
        if (n.program != program) {
            return false;
        }
        if (n.velocity != velocity) {
            return false;
        }
        if (n.pitchbend != pitchbend) {
            return false;
        }
        if (n.voice != voice) {
            return false;
        }

        return super.equals(o);
    }

    /**
     * <code>getBank</code>
     * 
     * @return an <code>int</code> value
     */
    public int getBank() {
        return bank;
    }

    /**
     * <code>getChannel</code>
     * 
     * @return an <code>int</code> value
     */
    public int getChannel() {
        return channel;
    }

    /**
     * @return the pan
     */
    public int getPan() {
        return pan;
    }

    /**
     * <code>getPitchBend</code>
     * 
     * @return an <code>int</code> value
     */
    public int getPitchBend() {
        return pitchbend;
    }

    /**
     * <code>getProgram</code>
     * 
     * @return an <code>int</code> value
     */
    public int getProgram() {
        return program;
    }

    /**
     * <code>getVelocity</code>
     * 
     * @return an <code>int</code> value
     */
    public int getVelocity() {
        return velocity;
    }

    /**
     * 
     * @return
     */
    public int getVoice() {
        return voice;
    }

    /*
     * <p>See Effective Java. </p>
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME
                * result
                + (getPitch().getMidiNumber() + channel + program);
        return result;
    }

    /**
     * <code>setBank</code>
     * 
     * @param p
     *            an <code>int</code> value
     */
    public void setBank(final int p) {
        bank = p;
    }

    /**
     * <code>setChannel</code>
     * 
     * @param c
     *            an <code>int</code> value
     */
    public void setChannel(final int c) {
        if ((c < 0) || (c > 127)) {
            throw new IllegalArgumentException(
                    "value must be between 0 and 127");
        }
        channel = c;
    }

    /**
     * @param pan
     *            the pan to set
     */
    public void setPan(final int pan) {
        if ((pan < 0) || (pan > 127)) {
            throw new IllegalArgumentException(
                    "value must be between 0 and 127");
        }
        this.pan = pan;
    }

    /**
     * <code>setPitchBend</code>
     * 
     * @param b
     *            an <code>int</code> value
     */
    public void setPitchBend(final int b) {
        pitchbend = b;
    }

    /**
     * <code>setProgram</code>
     * 
     * @param p
     *            an <code>int</code> value
     */
    public void setProgram(final int p) {
        program = p;
    }

    /**
     * <code>setVelocity</code> sets the MIDI velocity (volume).
     * 
     * @param v
     *            an <code>int</code> value between 0 and 127
     */
    public void setVelocity(final int v) {

        logger.debug("new velocity {}", v);

        if ((v < 0) || (v > 127)) {
            MIDINote.logger.debug("setVelocity bad value {}",
                    v);
            throw new IllegalArgumentException(
                    "value must be between 0 and 127");
        }
        velocity = v;
    }

    /**
     * @param voice
     *            the voice to set
     */
    public void setVoice(final int voice) {
        this.voice = voice;
    }

    /**
     * @return the string representation of the program (instrument)
     */
    public String getProgramName() {
        return MIDIGMPatch.getName(this.program);
    }

    /**
     * @param name
     *            the MIDI GM Patch name
     */
    public void setProgramName(final String name) {
        this.program = MIDIGMPatch.getPatch(name).getProgram();
    }

    /**
     * @param pitchString
     */
    public void setPitchString(String pitchString) {
        logger.debug("new pitch string {}", pitchString);
        logger.debug("value {}"
                + PitchFactory.getPitch(pitchString).getMidiNumber());
        setMidiNumber(PitchFactory.getPitch(pitchString).getMidiNumber());
    }

    /**
     * @return
     */
    public String getPitchString() {
        logger.debug("getting pitch string");
        return PitchFormat
                .midiNumberToString(this.getMidiNumber());
        // return PitchFormat.getPitchString(this.getMidiNumber());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        // NumberFormat nf = NumberFormat.getInstance();
        // nf.setMaximumFractionDigits(2);
        // nf.setMinimumFractionDigits(2);
        // nf.setMaximumIntegerDigits(3);
        // nf.setMinimumIntegerDigits(3);
        sb.append(super.toString());
        sb.append(" chan: ").append(channel).append(' ');
        sb.append("velocity: ").append(velocity).append(' ');
        sb.append("bank: ").append(bank).append(' ');
        sb.append("program: ").append(program).append(' ');
        sb.append("(").append(MIDIGMPatch.getName(program)).append(") ");
        sb.append("pitchbend: ").append(pitchbend).append(' ');
        sb.append("voice: ").append(voice).append(' ');
        sb.append(']');
        return sb.toString();
    }

    /**
     * MIDIStringParser compatible
     * 
     * Pitch, Start beat, Duration, Velocity, Pan, Channel, Bank, Program,
     * Pitchbend
     * 
     * @see com.rockhoppertech.music.Note#getString()
     * @see MIDIStringParser
     */
    @Override
    public String getString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.getString()).append(',');
        sb.append(velocity).append(',');
        sb.append(pan).append(',');
        sb.append(channel).append(',');
        sb.append(bank).append(',');
        sb.append(program).append(' ');

        return sb.toString();
    }
}
