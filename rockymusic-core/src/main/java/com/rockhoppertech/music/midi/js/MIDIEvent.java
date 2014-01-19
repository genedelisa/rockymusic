/*
 * $Id$
 *
 * Copyright 1998,1999,2000,2001 by Rockhopper Technologies, Inc.,
 * 75 Trueman Ave., Haddonfield, New Jersey, 08033-2529, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Rockhopper Technologies, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with RTI.
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

import java.io.Serializable;
import java.util.Arrays;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.gm.MIDIGMPatch;

/**
 * Class <code>MIDIEvent</code> wraps and adds to Java Sound's MidiEvent class.
 * 
 * Sun's MidiEvent class is not Serializable. It is also useful to schedule
 * events at beats instead of ticks.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 * @see Serializable
 */

public class MIDIEvent implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -7468463852929825843L;

	private static final Logger logger = LoggerFactory
			.getLogger(MIDIEvent.class);

	private long division = 256;

	private double beat;
	/**
	 * variable <code>tick</code>
	 */
	private long tick;
	/**
	 * variable <code>bytes</code>
	 */
	private byte[] bytes;
	/**
	 * variable <code>status</code>
	 */
	private int status;
	/**
	 * variable <code>type</code> is for meta events
	 */
	private int metaMessageType;

	/**
	 * Creates a new <code>MIDIEvent</code> instance from JavaSound's MidiEvent.
	 * 
	 * @param e
	 *            a <code>MidiEvent</code> object.
	 */
	public MIDIEvent(MidiEvent e) {
		tick = e.getTick();
		beat = (double) (tick) / (double) (division);
		MidiMessage mm = e.getMessage();
		byte[] ba = mm.getMessage();
		logger.debug("Original message: " + ba.length);
		for (byte element : ba) {
			logger.debug(Integer.toHexString(element & 0xf0) + " ");
		}

		if (mm instanceof ShortMessage) {
			logger.debug("Is short message");
			ShortMessage se = (ShortMessage) mm;
			bytes = new byte[2];
			bytes[0] = (byte) se.getData1();
			bytes[1] = (byte) se.getData2();
		} else if (mm instanceof SysexMessage) {
			logger.debug("is sysex message");
			SysexMessage sex = (SysexMessage) mm;
			bytes = sex.getData();
		} else if (mm instanceof MetaMessage) {
			logger.debug("is meta message");
			MetaMessage meta = (MetaMessage) mm;
			bytes = meta.getData();
		}

		status = mm.getStatus();
		logger.debug("Status: " + Integer.toHexString(status));
		if (mm instanceof MetaMessage) {
			metaMessageType = ((MetaMessage) mm).getType();
		}

		logger.debug("Constructed: " + toString());
	}

	/*
	 * private void copy(int i, int j, int k) throws InvalidMidiDataException {
	 * int len = 2; try { ByteArrayOutputStream baos = new
	 * ByteArrayOutputStream(); DataOutputStream dos = new
	 * DataOutputStream(baos); dos.writeByte(i); if(len >= 1) dos.writeByte(j);
	 * if(len >= 2) dos.writeByte(k); this.bytes = baos.toByteArray(); }
	 * catch(IOException ioexception) { } }
	 */

	/**
	 * Create a MIDIEvent from a JavaSound MidiMessage instance.
	 * 
	 * @param mm
	 * @param tick
	 */
	public MIDIEvent(MidiMessage mm, long tick) {
		this.tick = tick;
		beat = this.tick * division;

		if (mm instanceof ShortMessage) {
			// ShortMessage se = (ShortMessage) mm;
			bytes = new byte[2];
			bytes[0] = bytes[0];
			bytes[1] = bytes[1];
		} else if (mm instanceof SysexMessage) {
			SysexMessage sex = (SysexMessage) mm;
			bytes = sex.getData();
		} else if (mm instanceof MetaMessage) {
			MetaMessage meta = (MetaMessage) mm;
			bytes = meta.getData();
		}

		status = mm.getStatus();
		if (mm instanceof MetaMessage) {
			metaMessageType = ((MetaMessage) mm).getType();
			bytes = ((MetaMessage) mm).getData();
			// or getMessage which has the status. nah.
		}
		if (mm instanceof SysexMessage) {
			bytes = ((SysexMessage) mm).getData();
		}
	}

	/**
	 * <code>isNote</code> checks the status byte to see if this event is a Note
	 * event.
	 * 
	 * @return a <code>boolean</code> value
	 */
	public boolean isNote() {
		logger.debug("isnote " + Integer.toHexString(status));
		if ((status & 0xF0) == 0x90 || (status & 0xF0) == 0x80) {
			return true;
		}
		return false;
	}

	/**
	 * <code>getTick</code> returns the time scheduled for this event.
	 * 
	 * @return a <code>long</code> value
	 */
	public long getTick() {
		return tick;
	}

	/**
	 * <code>toMidiEvent</code> turns this well written class into JavaSound's
	 * MidiEvent.
	 * 
	 * @return a <code>MidiEvent</code> value
	 */
	public MidiEvent toMidiEvent() {
		MidiMessage mm = createMidiMessage();
		logger.debug("creating event at tick " + tick);
		return new MidiEvent(mm, tick);
	}

	/**
	 * <code>toMidiEvent</code> turns this well written :) class into
	 * JavaSound's MidiEvent.
	 * 
	 * @param division
	 * @return a <code>MidiEvent</code> value
	 */
	public MidiEvent toMidiEvent(int division) {
		MidiMessage mm = createMidiMessage();
		setDivision(division);
		logger.debug("creating event at tick " + tick);
		return new MidiEvent(mm, tick);
	}

	/**
	 * Create a JavaSound MidiMessage from this instance.
	 * 
	 * @param mm
	 * @return
	 */
	private MidiMessage createMidiMessage() {
		MidiMessage mm = null;
		if (MIDIUtils.isChannelMessage(status)) {
			logger.debug("isChannelMessage for "
					+ Integer.toHexString(status));
			mm = MIDIUtils.createShortMessage(status, bytes);

		} else if (MIDIUtils.isMetaMessage(status)) {
			logger.debug("MetaMessage: " + Integer.toHexString(status));
			MetaMessage meta = new MetaMessage();
			try {
				meta.setMessage(metaMessageType, bytes, bytes.length);
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
			mm = meta;

		} else if (MIDIUtils.isSysexMessage(status)) {
			logger.debug("Sysex message: " + Integer.toHexString(status));
			SysexMessage sex = new SysexMessage();
			try {
				sex.setMessage(bytes, bytes.length);
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
			mm = sex;
		} else {
			logger.debug("Unknown status " + Integer.toHexString(status));
		}
		return mm;
	}

	// private int readUnsigned() {
	// int i = -1;
	// try {
	// ByteArrayInputStream bais = new ByteArrayInputStream(this.bytes, 0,
	// this.bytes.length);
	// DataInputStream dis = new DataInputStream(bais);
	// i = dis.readUnsignedByte();
	// } catch (IOException ioexception) {
	// }
	// return i;
	// }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MIDIEvent [");
		sb.append(" tick=").append(tick);
		sb.append(" beat=").append(beat);
		sb.append(" division=").append(division);
		sb.append(" status=").append(Integer.toHexString(status)).append(
				' ');
		sb.append(MIDIUtils.getCommandName(status));
		StringBuilder data = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			data.append("0x").append(
					Integer.toHexString(bytes[i]).toUpperCase());
			if (i != bytes.length - 1) {
				data.append(',');
			}
		}
		sb.append("data=\"").append(data.toString()).append("\" ");
		sb.append(" metatype=").append(metaMessageType);
		sb.append("]");

		return sb.toString();
	}

	public String toReadableString() {
		StringBuilder sb = new StringBuilder();

		// switch(se.getType()) {
		// case MidiEvent.CHANNEL_VOICE_MESSAGE:

		switch (status & 0xF0) {
		case ShortMessage.NOTE_OFF:
			sb.append("Note Off Key=").append(bytes[0])
					.append(" Velocity=").append(bytes[1]);
			break;

		case ShortMessage.NOTE_ON:
			sb.append("Note On Key=").append(bytes[0])
					.append(" Velocity=").append(bytes[1]);
			break;

		case ShortMessage.POLY_PRESSURE:
			sb.append("Polyphonic key pressure key=").append(bytes[0])
					.append(" pressure=").append(bytes[1]);
			break;

		case ShortMessage.CONTROL_CHANGE:
			sb.append("Control Change controller=").append(bytes[0])
					.append(" value=").append(bytes[1]);
			sb.append(" ").append(
					MIDIControllers.getControllerName((int) bytes[0]));

			break;

		case ShortMessage.PROGRAM_CHANGE:
			sb.append("Program Change program=").append(bytes[0])
					.append(" name=").append(MIDIGMPatch.getName(bytes[0]));
			break;

		case ShortMessage.CHANNEL_PRESSURE:
			sb.append("Channel Pressure pressure=").append(bytes[0]);
			break;

		case ShortMessage.PITCH_BEND:
			// int val = (this.bytes[0] & 0x7f) | ((this.bytes[1] & 0x7f) << 7);
			// short centered = 0x2000;
			short s14bit;
			s14bit = bytes[1];
			s14bit <<= 7;
			s14bit |= bytes[0];

			sb.append("Pitch Bend one=").append(bytes[0]).append(" two=")
					.append(bytes[1]);
			sb.append(" val=").append(s14bit);
			break;
		}
        sb.append(" Channel=").append(status & 0x0F);
        sb.append(" status=").append(Integer.toHexString(status));        

		/*
		 * case MidiEvent.CHANNEL_MODE_MESSAGE: printChannelModeMessage(se);
		 * break;
		 * 
		 * case MidiEvent.SYSTEM_COMMON_MESSAGE : System.out.print(" system
		 * common message "); break;
		 * 
		 * case MidiEvent.SYSTEM_REALTIME_MESSAGE : System.out.print(" system
		 * realtime message "); break;
		 * 
		 * default: strMessage = "unknown event: status = " + se.getStatus() +
		 * ", byte1 = " + this.bytes[0] + ", byte2 = " + this.bytes[1];
		 */
		// System.out.print(sw.toString());
		return sb.toString();

	}

	public String toXMLString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<MIDIEvent ");
		sb.append("beat=\"").append(beat).append("\" ");
		sb.append("type=\"").append(MIDIUtils.getCommandName(status))
				.append("\" ");

		StringBuilder data = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			data.append("0x").append(
					Integer.toHexString(bytes[i]).toUpperCase());
			if (i != bytes.length - 1) {
				data.append(',');
			}
		}
		sb.append("data=\"").append(data.toString()).append("\" ");

		/*
		 * sb.append("data=\""); if (this.bytes.length > 0)
		 * sb.append("0x").append(Integer.toHexString(this.bytes[0])); if
		 * (this.bytes.length > 1)
		 * sb.append(", 0x").append(Integer.toHexString(this.bytes[1]));
		 * 
		 * sb.append("\" ");
		 */
		sb.append("metatype=\"").append(metaMessageType).append("\"");
		sb.append("/>");
		return sb.toString();
	}

	/**
	 * @return the beat
	 */
	public double getBeat() {
		return beat;
	}

	/**
	 * @param beat
	 *            the beat to set
	 */
	public void setBeat(double beat) {
		this.beat = beat;
	}

	/**
	 * @return the division
	 */
	public long getDivision() {
		return division;
	}

	/**
	 * @param division
	 *            the division to set
	 */
	public void setDivision(long division) {
		this.division = division;
		tick = (long) (beat * division);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTick(long tick) {
		this.tick = tick;
	}

	public int getMetaMessageType() {
		return metaMessageType;
	}

	public void setMetaMessageType(int metaMessageType) {
		this.metaMessageType = metaMessageType;
	}

	/*
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bytes);
		result = prime * result + (int) (division ^ (division >>> 32));
		result = prime * result + status;
		result = prime * result + (int) (tick ^ (tick >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MIDIEvent other = (MIDIEvent) obj;
		if (!Arrays.equals(bytes, other.bytes)) {
			return false;
		}
		if (division != other.division) {
			return false;
		}
		if (status != other.status) {
			return false;
		}
		if (tick != other.tick) {
			return false;
		}
		return true;
	}

}