/**
 * 
 */
package com.rockhoppertech.music.midi.js;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Note;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */

public class MIDINote extends Note implements Cloneable {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(MIDINote.class);

	private static final int defaultPitch = Pitch.C5;
	private static final double defaultStartbeat = 1d;
	private static final double defaultDuration = 1d;
	private static final int defaultChannel = 0;
	private static final int defaultVelocity = 64;
	private static final int defaultProgram = 0;
	private static final int defaultBend = 0;
	private static final int defaultBank = 0;
	private static final int defaultVoice = 0;
	private static final int defaultPan = 64; // center

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

	public static class Builder {
		private int pitch = defaultPitch;
		private double startBeat = defaultStartbeat;
		private double duration = defaultDuration; // in beats
		private boolean rest = false;
		private int pan = defaultPan;
		private int bank = defaultBank;
		private int channel = defaultChannel;
		private int pitchBend = defaultBend;
		private int program = defaultProgram;
		private int velocity = defaultVelocity;
		private int voice = defaultVoice;

//		public Builder(int pitch, double startBeat) {
//			this.pitch = pitch;
//			this.startBeat = startBeat;
//		}

		public MIDINote build() {
			MIDINote result = new MIDINote(this.pitch);
			result.setStartBeat(this.startBeat);
			result.setDuration(this.duration);
			result.setRest(this.rest);
			result.setPan(this.pan);
			result.setBank(this.bank);
			result.setChannel(this.channel);
			result.setPitchBend(this.pitchBend);
			result.setProgram(this.program);
			result.setVelocity(this.velocity);
			result.setVoice(this.voice);
			return result;
		}

		public Builder pitch(int pitchNumber) {
			this.pitch = pitchNumber;
			return this;
		}

		public Builder startBeat(double value) {
			this.startBeat = value;
			return this;
		}

		public Builder duration(double beats) {
			this.duration = beats;
			return this;
		}

		public Builder rest(boolean value) {
			this.rest = value;
			return this;
		}

		public Builder pan(int value) {
			this.pan = value;
			return this;
		}

		public Builder bank(int value) {
			this.bank = value;
			return this;
		}

		public Builder channel(int value) {
			this.channel = value;
			return this;
		}

		public Builder pitchBend(int value) {
			this.pitchBend = value;
			return this;
		}

		public Builder program(int value) {
			this.program = value;
			return this;
		}

		public Builder velocity(int value) {
			this.velocity = value;
			return this;
		}

		public Builder voice(int value) {
			this.voice = value;
			return this;
		}
	}

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
		// this(pitch, 1d, 1d, 0, 64, 0, 0, 0, 1);

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
	 */

	public MIDINote(final int pitch, final double startbeat,
			final double duration, final int channel, final int velocity,
			final int program, final int bend, final int bank, final int voice) {
		super(pitch, startbeat, duration);
		this.channel = channel;
		this.bank = bank;
		this.program = program;
		this.velocity = velocity;
		this.pitchbend = bend;
		this.voice = voice;
	}

	public MIDINote(final MIDINote n) {
		super(n.getPitch(), n.getStartBeat(), n.getDuration());
		this.channel = n.getChannel();
		this.bank = n.getBank();
		this.program = n.getProgram();
		this.velocity = n.getVelocity();
		this.pitchbend = n.getPitchBend();
		this.voice = n.getVoice();
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
	 * <code>clone</code> simply calls the copy constructor. Consider using the
	 * copy constructor directly instead. MIDINote is not Cloneable[sic].
	 * 
	 * @return a <code>MIDINote</code> value
	 */
	@Override
	public Object clone() {
		final MIDINote clone = new MIDINote(this.getPitch().getMidiNumber(),
				this.getStartBeat(), this.getDuration(), this.channel,
				this.velocity, this.program, this.pitchbend, this.bank,
				this.voice);
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

		if (n.channel != this.channel) {
			return false;
		}
		if (n.bank != this.bank) {
			return false;
		}
		if (n.program != this.program) {
			return false;
		}
		if (n.velocity != this.velocity) {
			return false;
		}
		if (n.pitchbend != this.pitchbend) {
			return false;
		}
		if (n.voice != this.voice) {
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
		return this.bank;
	}

	/**
	 * <code>getChannel</code>
	 * 
	 * @return an <code>int</code> value
	 */
	public int getChannel() {
		return this.channel;
	}

	/**
	 * @return the pan
	 */
	public int getPan() {
		return this.pan;
	}

	/**
	 * <code>getPitchBend</code>
	 * 
	 * @return an <code>int</code> value
	 */
	public int getPitchBend() {
		return this.pitchbend;
	}

	/**
	 * <code>getProgram</code>
	 * 
	 * @return an <code>int</code> value
	 */
	public int getProgram() {
		return this.program;
	}

	/**
	 * <code>getVelocity</code>
	 * 
	 * @return an <code>int</code> value
	 */
	public int getVelocity() {
		return this.velocity;
	}

	/**
	 * 
	 * @return
	 */
	public int getVoice() {
		return this.voice;
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
				+ (this.getPitch().getMidiNumber() + this.channel + this.program);
		return result;
	}

	/**
	 * <code>setBank</code>
	 * 
	 * @param p
	 *            an <code>int</code> value
	 */
	public void setBank(final int p) {
		this.bank = p;
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
		this.channel = c;
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
		this.pitchbend = b;
	}

	/**
	 * <code>setProgram</code>
	 * 
	 * @param p
	 *            an <code>int</code> value
	 */
	public void setProgram(final int p) {
		this.program = p;
	}

	/**
	 * <code>setVelocity</code>
	 * 
	 * @param v
	 *            an <code>int</code> value
	 */
	public void setVelocity(final int v) {
		if (MIDINote.logger.isDebugEnabled()) {
			MIDINote.logger.debug(String.format("new velocity %d", v));
		}
		if ((v < 0) || (v > 127)) {
			if (MIDINote.logger.isDebugEnabled()) {
				MIDINote.logger.debug(String.format("setVelocity bad value %d",
						v));
			}
			throw new IllegalArgumentException(
					"value must be between 0 and 127");
		}
		this.velocity = v;
		if (MIDINote.logger.isDebugEnabled()) {
			MIDINote.logger.debug(String.format(
					"firing velocity new %d", this.velocity));
		}
	}

	/**
	 * @param voice
	 *            the voice to set
	 */
	public void setVoice(final int voice) {
		this.voice = voice;
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
		sb.append(" chan: ").append(this.channel).append(' ');
		sb.append("velocity: ").append(this.velocity).append(' ');
		sb.append("bank: ").append(this.bank).append(' ');
		sb.append("program: ").append(this.program).append(' ');
		sb.append("(").append(MIDIGMPatch.getName(this.program)).append(") ");
		sb.append("pitchbend: ").append(this.pitchbend).append(' ');
		sb.append("voice: ").append(this.voice).append(' ');
		sb.append(']');
		return sb.toString();
	}

	/*
	 * MIDIStringParser compatible
	 * 
	 * Pitch, Start beat, Duration, Velocity, Pan, Channel, Bank, Program,
	 * Pitchbend
	 * 
	 * @see com.rockhoppertech.music.Note#getString()
	 */
	@Override
	public String getString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getString()).append(',');
		sb.append(this.velocity).append(',');
		sb.append(this.pan).append(',');
		sb.append(this.channel).append(','); 
		sb.append(this.bank).append(',');
		sb.append(this.program).append(' ');

		return sb.toString();
	}
}
