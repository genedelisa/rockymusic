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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.gm.MIDIGMPatch;

//TODO get all the cruft out of here
// removed the play methods
// removed the controller and patch stuff

/**
 * Class <code>MidiUtils</code> is a collection of ,um, MIDI utilities.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 */
public final class MIDIUtils {
	
	/**
	 * Default division (resolution) for Sequences generated
	 */
	private static int division = 256;

	
	/**
	 * End of track message
	 */
	public static final byte ENDOFTRACK = 0x2F;

	
	/**
	 * General MIDI reset
	 */
	public static final byte gmreset[] = { 0x05, 0x7E, 0x7F, 0x09, 0x01 };

	// Sequencer sequencer = MIDIUtils.getSequencer();
	// try {
	// sequencer.getTransmitter().setReceiver(receiver);
	// } catch (MidiUnavailableException me) {
	// if (logger.isErrorEnabled()) {
	// logger.error(me.getMessage(), me);
	// }
	// }
	// Sequence seq = notelist.play();
	// MIDIUtils.print(seq);
	// Track track = seq.getTracks()[1];
	// trackTableModel.setTrack(track);

	/*
	 * sequencer.setMicrosecondPosition(long microseconds)
	 */

	
	/**
	 * logging
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MIDIUtils.class);

	public static final int META_COPYRIGHT = 2;
	public static final int META_CUE_POINT = 7;
	// FF 08 len text PROGRAM NAME
	public static final int META_DEVICE_NAME = 9;
	// FF 09 len text DEVICE NAME
	public static final int META_INSTRUMENT = 4;
	public static final int META_KEY_SIG = 0x59;
	public static final int META_LYRIC = 5;
	public static final int META_MARKER = 6;
	public static final int META_NAME = 3; // sequence or track
	/**
	 * see http://www.midi.org/about-midi/smf/rp019.shtml
	 */
	public static final int META_PROGRAM_NAME = 8;
	public static final int META_SMPTE_OFFSET = 0x54;
	public static final int META_TEMPO = 0x51;
	public static final int META_TEXT = 1;
	public static final int META_TIME_SIG = 0x58;
	
	private static Sequencer sequencer;


	/**
	 * every track must end with: FF 2F 00
	 * @param track
	 */
	public static void addEndOfTrack(Track track) {
		try {
			MetaMessage message = new MetaMessage();
			// type, data, length, tick
			message.setMessage(ENDOFTRACK, new byte[] { 0 }, 1);
			MidiEvent event = new MidiEvent(message, track.ticks() + 480);
			track.add(event);
		} catch (InvalidMidiDataException e) {
		    logger.error(e.getLocalizedMessage(),e);
		}
	}

	/**
	 * @param track
	 * @param chan
	 * @param d1
	 * @param d2
	 */
	public static void appendControlChange(Track track, int chan, int d1, int d2) {
		try {
			ShortMessage message = new ShortMessage();
			long tick = track.ticks();
			// message.setMessage(0xB0 + chan, d1, d2);
			message.setMessage(ShortMessage.CONTROL_CHANGE, chan, d1, d2);
			MidiEvent event = new MidiEvent(message, tick);
			track.add(event);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *  adds the note to the end of the track.
	 * @param track
	 * @param chan
	 * @param num
	 * @param vel
	 * @param duration
	 */
	public static void appendNote(Track track, int chan, int num, int vel,
			long duration) {

		try {
			long tick = track.ticks();
			ShortMessage message = new ShortMessage();
			// message.setMessage(0x90+chan, num, vel);
			message.setMessage(ShortMessage.NOTE_ON, chan, num, vel);
			MidiEvent event = new MidiEvent(message, tick);
			track.add(event);
			print(message);

			message = new ShortMessage();
			// message.setMessage(0x90 + chan, num, 0);
			message.setMessage(ShortMessage.NOTE_ON, chan, num, 0);
			event = new MidiEvent(message, tick + (division * duration));
			track.add(event);
			print(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param track
	 * @param chan
	 * @param num
	 */
	public static void appendProgramChange(Track track, int chan, int num) {
		try {
			ShortMessage message = new ShortMessage();
			long tick = track.ticks();
			// message.setMessage(0xC0 + chan, num, 0);
			message.setMessage(ShortMessage.PROGRAM_CHANGE, chan, num, 0);
			MidiEvent event = new MidiEvent(message, tick);
			track.add(event);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param tick
	 * @param metaTextType
	 * @param text
	 * @return
	 * @throws InvalidMidiDataException
	 */
	public static MidiEvent createMetaTextMessage(long tick, int metaTextType,
			String text) {
		MetaMessage message = new MetaMessage();
		byte[] b = text.getBytes();
		try {
			message.setMessage(metaTextType, b, b.length);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		MidiEvent event = new MidiEvent(message, tick);
		return event;
	}

	/**
	 * @param origstatus
	 * @param bytes
	 * @return
	 */
	public static ShortMessage createShortMessage(int origstatus, byte[] bytes) {
		int status = origstatus & 0xF0;
		// int origstatus = bytes[0];
		ShortMessage msg = new ShortMessage();
		try {
			switch (status) {
			case 0x80:
				msg.setMessage(origstatus, bytes[0], bytes[1]);
				// msg.setMessage(origstatus, bytes[1], bytes[2]);
				break;
			case 0x90:
				// msg.setMessage(origstatus, bytes[1], bytes[2]);
				msg.setMessage(origstatus, bytes[0], bytes[1]);
				break;
			case 0xA0: // Aftertouch, note, pressure
				msg.setMessage(origstatus, bytes[0], bytes[1]);
				break;
			case 0xB0: // Controller, cont, value
				msg.setMessage(origstatus, bytes[0], bytes[1]);
				break;
			case 0xC0: // ProgramChange number 1 datum
				msg.setMessage(origstatus, bytes[0], 0);
				break;
			case 0xD0: // ChannelPressure pressure 1 datum
				msg.setMessage(origstatus, bytes[0], 0);

				break;
			case 0xE0: // PitchWheel
				msg.setMessage(origstatus, bytes[0], bytes[1]);
				break;
			default:
			    logger.error(
				"MidiUtils.CreateShortMess Bad status {}",
						status);
			}
		} catch (InvalidMidiDataException e) {
		    logger.error(e.getLocalizedMessage(),e);
			e.printStackTrace();
		}
		return msg;
	}

	// perhaps filter the whole sequence. js .99 really made this awkward
	public static Track filter(Sequence sequence, Track t,
			MIDIEventFilter filter) {
		Track track = sequence.createTrack();
		// Track track = new Track();
		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			if (filter.accept(me) == false) {
				continue;
			} else {
				track.add(me);
			}
		}
		return track;
	}

	/**
	 * @param command
	 * @return
	 */
	public static String getCommandName(int command) {
		StringBuilder sb = new StringBuilder();
		switch (command) {
		case ShortMessage.NOTE_OFF:
			sb.append("Note off");
			break;

		case ShortMessage.NOTE_ON:
			sb.append("Note on");
			break;

		case ShortMessage.POLY_PRESSURE:
			sb.append("Polyphonic key pressure");
			break;

		case ShortMessage.CONTROL_CHANGE:
			sb.append("Control Change ");
			// sb.append(getControllerName(se.getData1()));
			break;

		case ShortMessage.PROGRAM_CHANGE:
			sb.append("Program Change ");
			// sb.append(getPatchName(se.getData1()));
			break;

		case ShortMessage.CHANNEL_PRESSURE:
			sb.append("Key pressure");
			break;

		case ShortMessage.PITCH_BEND:
			sb.append("Pitch Wheel");
			break;
		}
		// sb.append("Channel " + se.getChannel());

		return sb.toString();

	}

	

	/**
	 * @param event
	 * @return
	 */
	private static String getKeySignature(MetaMessage event) {
		byte[] message = event.getData();
		boolean sharps = false;
		boolean major = false;
		if (message[0] < 0) {
			sharps = false;
		} else if (message[0] > 0) {
			sharps = true;
		}
		if (1 == message[1]) {
			major = false;
		} else {
			major = true;
		}
		String[] sharpKeys = { "C", "G", "D", "A", "E", "B", "F#", "C#" };
		String[] flatKeys = { "C", "F", "Bb", "Eb", "Ab", "Db", "Gb", "Cb" };
		String[] sharpMinorKeys = { "A", "E", "B", "F#", "C#", "G#", "D#", "A#" };
		String[] flatMinorKeys = { "A", "D", "G", "C", "F", "Bb", "Eb", "Ab" };
		int num = Math.abs(message[0]);
		String str = " " + num;
		if (sharps) {
			str += " sharps ";
		} else {
			str += " flats ";
		}

		if (major) {
			str += " major ";
			if (sharps) {
				str += sharpKeys[num];
			} else {
				str += flatKeys[num];
			}

		} else {
			str += " minor ";
			if (sharps) {
				str += sharpMinorKeys[num];
			} else {
				str += flatMinorKeys[num];
			}
		}
		return str;
	}

	/**
	 * @param t
	 * @return
	 */
	public static List<MetaMessage> getMetaTextEvents(Track t) {
		List<MetaMessage> list = new ArrayList<>();
		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			MidiMessage mm = me.getMessage();
			if (mm instanceof MetaMessage) {
				MetaMessage meta = (MetaMessage) mm;
				if (isText(meta)) {
					list.add(meta);
				}
			}
		}
		return list;
	}

	/**
	 * @param t
	 * @return
	 */
	public static List<MidiEvent> getMetaMessages(Track t) {
		List<MidiEvent> list = new ArrayList<>();
		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			MidiMessage mm = me.getMessage();
			if (mm instanceof MetaMessage) {
				list.add(me);
			}
		}
		return list;
	}

	/**
	 * @param t
	 * @return
	 */
	public static List<MidiEvent> getKeySignatures(Track t) {
		List<MidiEvent> list = new ArrayList<>();
		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			MidiMessage mm = me.getMessage();
			if (mm instanceof MetaMessage) {
				MetaMessage meta = (MetaMessage) mm;
				logger.debug("checking mm for keysig {}", toString(mm));
				if (meta.getType() == META_KEY_SIG) {
					logger.debug("adding ks");
					list.add(me);
				}
			}
		}
		return list;
	}

	/**
	 * @param t
	 * @return
	 */
	public static List<MidiEvent> getTimeSignatures(Track t) {
		List<MidiEvent> list = new ArrayList<>();
		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			MidiMessage mm = me.getMessage();
			if (mm instanceof MetaMessage) {
				MetaMessage meta = (MetaMessage) mm;
				if (meta.getType() == META_TIME_SIG) {
					list.add(me);
				}
			}
		}
		return list;
	}

	/**
	 * @param t
	 * @return
	 */
	public static List<MidiEvent> getTempi(Track t) {
		List<MidiEvent> list = new ArrayList<>();
		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			MidiMessage mm = me.getMessage();
			if (mm instanceof MetaMessage) {
				MetaMessage meta = (MetaMessage) mm;
				if (meta.getType() == META_TEMPO) {
					list.add(me);
				}
			}
		}
		return list;
	}

	/**
	 * @return
	 */
	public static Sequencer getSequencer() {
		if (MIDIUtils.sequencer == null) {
			try {
				MIDIUtils.sequencer = MidiSystem.getSequencer();
			} catch (javax.sound.midi.MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		return MIDIUtils.sequencer;
	}

	/**
	 * @param info
	 * @return
	 */
	public static Sequencer getSequencer(MidiDevice.Info info) {
		try {
			MidiDevice md = MidiSystem.getMidiDevice(info);
			if (md instanceof Synthesizer == false) {
				throw new IllegalArgumentException("must be a synth");
			}
			return getSequencer((Synthesizer) md);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param synth
	 * @return
	 */
	public static Sequencer getSequencer(Synthesizer synth) {
		Sequencer seq = null;
		try {
			seq = MidiSystem.getSequencer();
			seq.getTransmitter().setReceiver(synth.getReceiver());
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		return seq;
	}

	/**
	 * @param mm
	 * @return
	 */
	public static String getSequenceTrackName(MetaMessage mm) {
		if (isName(mm)) {
			return new String(mm.getData());
		} else {
			return null;
		}
	}

	/**
	 * should be only one. this gets the first one.
	 * 
	 * @param t
	 * @return
	 */
	public static String getSequenceTrackName(Track t) {
		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			MidiMessage mm = me.getMessage();
			if (mm instanceof MetaMessage) {
				MetaMessage meta = (MetaMessage) mm;
				if (isName(meta)) {
					return getSequenceTrackName(meta);
				}
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	static Map<String, MidiDevice.Info> getSynthList() {
		MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
		Map<String, MidiDevice.Info> map = new HashMap<String, MidiDevice.Info>();
		try {
			for (Info element : info) {
				MidiDevice md = MidiSystem.getMidiDevice(element);
				if (md instanceof Synthesizer) {
					map.put(element.getName(), element);
					logger.debug("name " + element.getName());
					logger.debug(" Is a synthesizer ");
				}
			}
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @param event
	 * @return
	 */
	static String getTimeSignature(MetaMessage event) {
		StringBuilder sb = new StringBuilder();
		byte[] message = event.getData();
		byte numerator = message[0];
		byte power = message[1];
		sb.append(numerator);
		sb.append('/');
		sb.append((int) Math.pow(2, power));
		return sb.toString();
	}

	/**
	 * @param track
	 * @param tick
	 * @param chan
	 * @param d1
	 * @param d2
	 */
	public static void insertControlChange(Track track, long tick, int chan,
			int d1, int d2) {
		try {
			ShortMessage message = new ShortMessage();
			// message.setMessage(0xB0 + chan, d1, d2);
			message.setMessage(ShortMessage.CONTROL_CHANGE, chan, d1, d2);
			MidiEvent event = new MidiEvent(message, tick);
			track.add(event);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * must be first event on first track
	 * 
	 * @param track
	 * @param tick
	 * @param copy
	 */
	public static void insertCopyright(Track track, long tick, String copy) {
		insertMetaText(track, tick, META_COPYRIGHT, copy);
	}

	/**
	 * @param track
	 * @param tick
	 * @param name
	 */
	public static void insertDeviceName(Track track, long tick, String name) {
		insertMetaText(track, tick, META_DEVICE_NAME, name);
	}

	/**
	 * sf = -7: 7 flats sf = -1: 1 flat sf = 0: key of C sf = 1: 1 sharp sf = 7:
	 * 7 sharps
	 * 
	 * mi = 0: major key mi = 1: minor key
	 * 
	 * @param track
	 * @param sf
	 *            number of sharps or flats -7 to +7
	 * @param mi
	 *            major or minor 0 = major 1 = minor
	 * @param tick
	 */
	public static void insertKeySignature(Track track, long tick, int sf, int mi) {
		try {
			MetaMessage message = new MetaMessage();
			byte[] a = new byte[2];
			a[0] = (byte) sf;
			a[1] = (byte) mi;
			message.setMessage(0x59, a, a.length);
			MidiEvent event = new MidiEvent(message, tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
		    logger.error(e.getLocalizedMessage(),e);
		}
	}

	/**
	 * Inserts a lyric at the given tick. each syllable should be a separate
	 * lyric event which begins at the event's time.
	 * 
	 * @param track
	 * @param tick
	 * @param lyric
	 */
	public static void insertLyric(Track track, long tick, String lyric) {
		insertMetaText(track, tick, META_LYRIC, lyric);
	}

	/**
	 * Normally in a format 0 track, or the first track in a format 1 file. The
	 * name of that point in the sequence, such as a rehearsal letter or section
	 * name
	 * 
	 * @param track
	 * @param tick
	 * @param marker
	 */
	public static void insertMarker(Track track, long tick, String marker) {
		insertMetaText(track, tick, META_MARKER, marker);
	}

	/**
	 * this event exists merely to provide the user with visual feedback of the
	 * instrumentation for a track.
	 * 
	 * @param track
	 * @param tick
	 * @param instrument
	 */
	public static void insertMetaInstrument(Track track, long tick,
			String instrument) {
		insertMetaText(track, tick, META_INSTRUMENT, instrument);
	}

	/**
	 * inserts the text at the absolute tick.
	 * 
	 * @param track
	 * @param tick
	 * @param text
	 */
	public static void insertMetaText(Track track, long tick, int metaTextType,
			String text) {
		try {
			MidiEvent event = createMetaTextMessage(tick, metaTextType, text);
			track.add(event);
			// print(event);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * inserts the note at the absolute tick.
	 */
	public static void insertNote(Track track, long tick, int chan, int num,
			int vel, double duration) {
		try {
			ShortMessage message = new ShortMessage();
			// message.setMessage(0x90 + chan, num, vel);
			message.setMessage(ShortMessage.NOTE_ON, chan, num, vel);
			MidiEvent event = new MidiEvent(message, tick);
			track.add(event);
			// print(event);

			message = new ShortMessage();
			// message.setMessage(0x90 + chan, num, 0);
			message.setMessage(ShortMessage.NOTE_ON, chan, num, 0);
			event = new MidiEvent(message, (int) (tick + (division * duration)));
			track.add(event);
			// print(event);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	

	/**
	 * @param track
	 * @param tick
	 * @param chan
	 * @param num
	 */
	public static void insertProgramChange(Track track, long tick, int chan,
			int num) {
		try {
			ShortMessage message = new ShortMessage();
			// message.setMessage(0xC0 + chan, num, 0);
			message.setMessage(ShortMessage.PROGRAM_CHANGE, chan, num, 0);
			MidiEvent event = new MidiEvent(message, tick);
			track.add(event);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param track
	 * @param tick
	 * @param name
	 */
	public static void insertProgramName(Track track, long tick, String name) {
		insertMetaText(track, tick, META_PROGRAM_NAME, name);
	}

	/**
	 * This is either the sequence or track name in the specification. When read
	 * into Sibelius 3 the track 0 name is both the name of the track and title
	 * of page. (also from the score info menu button). If in a format 0 track,
	 * or the first track in a format 1 file, the name of the sequence.
	 * Otherwise, the name of the track.
	 * 
	 * @param track
	 * @param tick
	 * @param name
	 */
	public static void insertSequenceName(Track track, long tick, String name) {
		insertMetaText(track, tick, META_NAME, name);
	}


	/**
	 * @param track
	 * @param tick
	 * @param tempo
	 */
	public static void insertTempo(Track track, long tick, int tempo) {
		int temp = 60000000 / tempo;
		tempo = temp;
		byte[] b = new byte[3];
		int tmp = tempo >> 16;
		b[0] = (byte) tmp;
		tmp = tempo >> 8;
		b[1] = (byte) tmp;
		b[2] = (byte) tempo;

		try {
			MetaMessage message = new MetaMessage();
			// type, data, length, tick
			message.setMessage(0x51, b, b.length);
			MidiEvent event = new MidiEvent(message, tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @param track
	 * @param tick
	 * @param text
	 */
	public static void insertText(Track track, long tick, String text) {
		insertMetaText(track, tick, META_TEXT, text);
	}

	/**
	 * @param denominator
	 *            needs to be a power that 2 is raised to
	 */
	public static void insertTimeSignature(Track track, long tick,
			int numerator, int denominator) {

		/*
		 * int power=0; int n=denominator; while( (n/=2) > 0) { power++; }
		 */

		// Math.log is ln or base e. change of base to base 2
		double pow = Math.log(denominator) / Math.log(2d);
		byte b = new Double(pow).byteValue();
		// the power that 2 is raised to

		try {
			MetaMessage message = new MetaMessage();
			// type, data, length, tick
			byte[] a = new byte[4];
			a[0] = (byte) numerator;
			a[1] = b; // power of 2
			a[2] = 24; // 24 MIDI Clocks per quarter note
			a[3] = 8; // Number of 1/32 notes per 24 MIDI clocks
			message.setMessage(0x58, a, a.length);
			MidiEvent event = new MidiEvent(message, tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			logger.error(e.getLocalizedMessage(),e);
		}
	}

	

	/**
	 * @param b
	 * @return
	 */
	public static boolean isChannelMessage(int b) {
		if ((b < 0xF0) && (b >= 0x80)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param mm
	 * @return
	 */
	public static boolean isCopyright(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_COPYRIGHT) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param mm
	 * @return
	 */
	public static boolean isCuePoint(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_CUE_POINT) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param mm
	 * @return
	 */
	public static boolean isInstrument(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_INSTRUMENT) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param mm
	 * @return
	 */
	public static boolean isLyric(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_LYRIC) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param mm
	 * @return
	 */
	public static boolean isMarker(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_MARKER) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param aByte
	 * @return
	 */
	public static boolean isMetaMessage(int aByte) {
		if (0xFF == aByte) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param mm
	 * @return
	 */
	public static boolean isName(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_NAME) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param aByte
	 * @return
	 */
	public static boolean isSysexMessage(int aByte) {
		if (0xF0 == aByte) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param mm
	 * @return
	 */
	public static boolean isText(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_TEXT) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param se
	 */
	public static void log(ShortMessage se) {
		logger.debug(toString(se));
	}

	/**
	 * @param se
	 */
	public static void logDebug(ShortMessage se) {
		logger.debug(toString(se));
	}

	/**
	 * @param se
	 */
	public static void logError(ShortMessage se) {
		logger.error(toString(se));
	}

	/**
	 * @param se
	 */
	public static void logInfo(ShortMessage se) {
		logger.info(toString(se));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MIDIUtils.systemInfo();
	}

	/**
	 * Write the event to the logger at debug level.
	 * @param event The event to display
	 */
	public static void print(MetaMessage event) {
		// PrintWriter writer = new PrintWriter(new
		// OutputStreamWriter(System.out));

		int type = event.getType();
		logger.debug("printMetaMessage " + type);
		switch (type) {
		case META_TEXT:
			logger.debug("Text");
			logger.debug(new String(event.getData(), StandardCharsets.UTF_8));
			//TODO set the encoding on the rest
			return;
		case META_COPYRIGHT:
			logger.debug("Copyright");
			logger.debug(new String(event.getData()));
			return;

		case META_NAME:
			logger.debug("Sequence name");
			logger.debug(new String(event.getData()));
			return;
		case META_INSTRUMENT:
			logger.debug("Instrument");
			logger.debug(new String(event.getData()));
			return;
		case META_LYRIC:
			logger.debug("Lyric");
			logger.debug(new String(event.getData()));
			return;
		case META_MARKER:
			logger.debug("Marker");
			logger.debug(new String(event.getData()));
			return;
		case META_CUE_POINT:
			logger.debug("Cue point");
			logger.debug(new String(event.getData()));
			return;
		case META_TEMPO:
			logger.debug("Tempo");
			byte[] message = event.getData();
			int value = (message[0] & 0xff);
			value = (value << 8) + (message[1] & 0xff);
			value = (value << 8) + (message[2] & 0xff);
			int tempo = 60000000 / value;
			logger.debug("value {} BPM", value);
			logger.debug("tempo {} microseconds per quarter note", tempo);
			break;
		case META_SMPTE_OFFSET:
			logger.debug("SMPTE Offset");
			break;
		case META_TIME_SIG:
			logger.debug("Time Signature");
			printTimeSignature(event);
			break;
		case META_KEY_SIG:
			logger.debug("Key Signature");
			printKeySignature(event);
			break;
        default:
            logger.debug("Unknown type: {}", type);
            break;

		}

		byte[] data = event.getData();
		logger.debug("data: ");
		for (byte element : data) {
			System.out.print(" " + Integer.toHexString(element));
		}
	}

	/**
	 * @param form
	 */
	public static void print(MidiFileFormat form) {
		logger.debug("form: " + form);
		logger.debug("form division: " + form.getDivisionType());
		// logger.debug("form len: " + form.getLength());
		// logger.debug("form dur: " + form.getDuration());
		logger.debug("form res: " + form.getResolution());
		logger.debug("form type: " + form.getType());
	}

	/**
	 * @param sequence
	 */
	public static void print(Sequence sequence) {
		logger.debug("Sequence length: " + sequence.getTickLength() + " ticks");
		logger.debug("Sequence duration: " + sequence.getMicrosecondLength()
				+ " ms");

		float fDivisionType = sequence.getDivisionType();
		String str = null;
		if (fDivisionType == Sequence.PPQ) {
			str = "PPQ";
		} else if (fDivisionType == Sequence.SMPTE_24) {
			str = "SMPTE, 24 frames per second";
		} else if (fDivisionType == Sequence.SMPTE_25) {
			str = "SMPTE, 25 frames per second";
		} else if (fDivisionType == Sequence.SMPTE_30DROP) {
			str = "SMPTE, 29.97 frames per second";
		} else if (fDivisionType == Sequence.SMPTE_30) {
			str = "SMPTE, 30 frames per second";
		}
		logger.debug("Division Type: " + str);
		if (sequence.getDivisionType() == Sequence.PPQ) {
			str = " ticks per beat";
		} else {
			str = " ticks per frame";
		}
		logger.debug("Resolution: " + sequence.getResolution() + str);
		logger.debug("patches");
		Patch[] patches = sequence.getPatchList();
		for (Patch patche : patches) {
			logger.debug("bank: " + patche.getBank() + " p: "
					+ patche.getProgram());
		}

		Track[] st = sequence.getTracks();
		logger.debug("" + st.length + " Tracks ");
		for (int i = 0; i < st.length; i++) {
			logger.debug("Track " + i
					+ " ======================================");
			print(st[i]);
		}

		Patch[] p = sequence.getPatchList();
		for (Patch element : p) {
			logger.debug("patch bank: " + element.getBank());
			logger.debug("patch program: " + element.getProgram());
		}
	}

	/**
	 * @param sequence
	 * @param filter
	 */
	public static void print(Sequence sequence, MIDIEventFilter filter) {
		Track[] st = sequence.getTracks();
		for (Track element : st) {
			print(element, filter);
		}
	}

	/**
	 * @param se
	 */
	public static void print(ShortMessage se) {
		logger.debug(toString(se));
	}

	/**
	 * @param event
	 */
	public static void print(SysexMessage event) {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
		writer.println("Sysex event");
		byte[] data = event.getData();
		writer.println("data: ");
		for (byte element : data) {
			writer.print(" " + Integer.toHexString(element));
		}
		writer.println();
	}

	/**
	 * @param t
	 */
	public static void print(Track t) {
		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			MidiMessage mm = me.getMessage();
			long tick = me.getTick();

			System.out.print("tick " + me.getTick());
			System.out.print(" status " + Integer.toHexString(mm.getStatus()));
			// System.out.print(" type " + me.getType() +
			// " 0x" + Integer.toHexString(me.getType()));

			if (mm instanceof ShortMessage) {
				ShortMessage se = (ShortMessage) mm;
				logger.debug(printFull(se));

			} else if (mm instanceof SysexMessage) {
				SysexMessage sex = (SysexMessage) mm;
				print(sex);

			} else if (mm instanceof MetaMessage) {
				MetaMessage meta = (MetaMessage) mm;
				print(meta);

			} else {
				logger.debug("Unknown MIDI event " + me);
			}
		}
		long tt = t.ticks();
		logger.debug("track ticks: " + tt);
	}

	/**
	 * @param t
	 * @param filter
	 */
	public static void print(Track t, MIDIEventFilter filter) {
		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			MidiMessage mm = me.getMessage();
			long tick = me.getTick();

			if (filter.accept(me) == false) {
				continue;
			}

			if (mm instanceof ShortMessage) {
				ShortMessage se = (ShortMessage) mm;
				printFull(se);
			} else if (mm instanceof SysexMessage) {
				SysexMessage sex = (SysexMessage) mm;
				print(sex);
			} else if (mm instanceof MetaMessage) {
				MetaMessage meta = (MetaMessage) mm;
				print(meta);

			} else {
				logger.debug("Unknown MIDI event " + me);
			}
		}
	}

	/**
	 * @param se
	 */
	public static void printChannelModeMessage(ShortMessage se) {
		switch (se.getData1()) {
		case 0x78:
			logger.debug("All sound off");
			break;
		case 0x79:
			logger.debug("Reset All Controllers");
			break;
		case 0x7A:
			logger.debug("Local Control");
			int d2 = se.getData2();
			if (d2 == 0) {
				logger.debug("disconnect local from sound generator");
			} else if (d2 == 0x7F) {
				logger.debug("resconnect");
			}
			break;
		case 0x7B:
			logger.debug("All notes off");
			break;
		case 0x7C:
			logger.debug("Omni mode off");
			break;
		case 0x7D:
			logger.debug("Omni mode on");
			break;
		case 0x7E:
			logger.debug("Mono mode on");
			break;
		case 0x7F:
			logger.debug("Poly mode on");
			break;
		}
	}

	/**
	 * <p>
	 * Returns a formatted string representation of the message.
	 * </p>
	 * 
	 * @param se
	 *            A JavaSound ShortMessage
	 * @return The formatted String
	 */
	public static String printFull(ShortMessage se) {
		StringBuilder sb = new StringBuilder();

		// switch(se.getType()) {
		// case MidiEvent.CHANNEL_VOICE_MESSAGE:

		switch (se.getCommand()) {
		case ShortMessage.NOTE_OFF:
			sb.append("Note Off Key=").append(se.getData1())
					.append(" Velocity=").append(se.getData2());
			break;

		case ShortMessage.NOTE_ON:
			sb.append("Note On Key=").append(se.getData1())
					.append(" Velocity=").append(se.getData2());
			break;

		case ShortMessage.POLY_PRESSURE:
			sb.append("Polyphonic key pressure key=").append(se.getData1())
					.append(" pressure=").append(se.getData2());
			break;

		case ShortMessage.CONTROL_CHANGE:
			sb.append("Control Change controller=").append(se.getData1())
					.append(" value=").append(se.getData2());
			sb.append(" ").append(MIDIControllers.getControllerName(se.getData1()));

			break;

		case ShortMessage.PROGRAM_CHANGE:
			sb.append("Program Change program=").append(se.getData1())
					.append(" name=")
					.append(MIDIGMPatch.getName(se.getData1()));
			break;

		case ShortMessage.CHANNEL_PRESSURE:
			sb.append("Channel Pressure pressure=").append(se.getData1());
			break;

		case ShortMessage.PITCH_BEND:
			int val = (se.getData1() & 0x7f) | ((se.getData2() & 0x7f) << 7);
			short centered = 0x2000;
			short s14bit;
			s14bit = (short) se.getData2();
			s14bit <<= 7;
			s14bit |= (short) se.getData1();

			sb.append("Pitch Bend one=").append(se.getData1()).append(" two=")
					.append(se.getData2());
			sb.append(" val=").append(s14bit);
			break;
		}
		sb.append(" Channel=").append(se.getChannel());

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
		 * ", byte1 = " + se.getData1() + ", byte2 = " + se.getData2();
		 */
		// System.out.print(sw.toString());
		return sb.toString();

	}

	/**
	 * @param event
	 */
	static void printKeySignature(MetaMessage event) {
		String str = getKeySignature(event);
		logger.debug(str);
	}

	/**
	 * @param se
	 */
	public static void printSystemCommonMessage(ShortMessage se) {
		switch (se.getData1()) {
		case 0xF1:
			logger.debug("MTC quarter frame");
			break;
		case 0xF2:
			logger.debug("Song position pointer");
			break;
		case 0xF3:
			logger.debug("Song select");
			break;
		case 0xF4:
			logger.debug("undefined");
			break;
		case 0xF5:
			logger.debug("undefined");
			break;
		case 0xF6:
			logger.debug("Tune request");
			break;
		case 0xF7:
			logger.debug("End of Sysex");
			break;
		}
	}

	/**
	 * @param event
	 */
	static void printTimeSignature(MetaMessage event) {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
		printTimeSignature(writer, event);
	}

	/**
	 * @param writer
	 * @param event
	 */
	static void printTimeSignature(PrintWriter writer, MetaMessage event) {
		byte[] message = event.getData();
		byte numerator = message[0];
		byte power = message[1];
		byte clocks = message[2];
		byte thirtyseconds = message[3];
		writer.println(getTimeSignature(event));
		writer.println("clocks " + clocks);
		writer.println("1/32 per clock " + thirtyseconds);
	}

	/**
	 * @param f
	 * @return
	 */
	public static Sequence read(File f) {
		return read(f.getAbsolutePath());
	}

	/**
	 * @param filename
	 * @return
	 */
	public static Sequence read(String filename) {
		Sequence sequence = null;
		try {
			sequence = MidiSystem.getSequence(new File(filename));

			// sequence = MidiSystem.getSequence( new BufferedInputStream(
			// new FileInputStream(new File(filename))), 1024);

		} catch (InvalidMidiDataException e) {
			logger.error(e.getLocalizedMessage(), e);
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return sequence;
	}

	/**
	 * 
	 */
	static void systemInfo() {
		MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
		logger.debug("system info " + info.length);

		try {
			for (Info element : info) {
				logger.debug("name " + element.getName());
				logger.debug("vendor " + element.getVendor());
				logger.debug("version " + element.getVersion());
				logger.debug("description " + element.getDescription());
				MidiDevice md = MidiSystem.getMidiDevice(element);
				logger.debug("device class " + md.getClass().getName());

				if (md instanceof Synthesizer) {
					// SYNTH_LIST.add(info[i]);

					logger.debug("Is a synthesizer ");
					Synthesizer synth = (Synthesizer) md;
					Instrument[] insts = synth.getAvailableInstruments();
					for (Instrument inst : insts) {
						logger.debug("inst: " + inst.getName());
					}

				} else if (md instanceof Sequencer) {
					logger.debug("Is a sequencer ");
				}
			}
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param event
	 * @return
	 */
	public static String toString(MetaMessage event) {
		StringBuilder sb = new StringBuilder();

		int type = event.getType();
		sb.append(" metamess type= ").append(type).append(' ');
		switch (type) {
		case META_TEXT:
			sb.append("Text ").append(new String(event.getData()));
			return sb.toString();
		case META_COPYRIGHT:
			sb.append("Copyright ").append(new String(event.getData()));
			return sb.toString();
		case META_NAME:
			sb.append("Sequence name ").append(new String(event.getData()));
			return sb.toString();
		case META_INSTRUMENT:
			sb.append("Instrument ").append(new String(event.getData()));
			return sb.toString();
		case META_LYRIC:
			sb.append("Lyric ").append(new String(event.getData()));
			return sb.toString();
		case META_MARKER:
			sb.append("Marker ").append(new String(event.getData()));
			return sb.toString();
		case META_CUE_POINT:
			sb.append("Cue point ").append(new String(event.getData()));
			return sb.toString();
		case META_TEMPO:
			byte[] message = event.getData();
			int value = (message[0] & 0xff);
			value = (value << 8) + (message[1] & 0xff);
			value = (value << 8) + (message[2] & 0xff);
			int tempo = 60000000 / value;
			sb
					.append("Tempo ")
					.append("value " + value + " BPM")
					.append("tempo " + tempo + " microseconds per quarter note");
			break;
		case META_SMPTE_OFFSET:
			sb.append("SMPTE Offset");
			break;
		case META_TIME_SIG:
			sb.append("Time Signature");
			sb.append("TS ").append(getTimeSignature(event));
			break;
		case META_KEY_SIG:
			sb.append("Key Signature");
			sb.append("KS ").append(getKeySignature(event));
			break;
		case ENDOFTRACK:
			sb.append("End of track ");
			break;
		}

		byte[] data = event.getData();
		for (byte element : data) {
			sb.append(" ").append(Integer.toHexString(element));
		}
		return sb.toString();
	}

	/**
	 * @param me
	 * @return
	 */
	public static String toString(MidiEvent me) {
		StringBuilder sb = new StringBuilder();
		MidiMessage mess = me.getMessage();
		long tick = me.getTick();
		sb.append("tick=" + tick); // no append for long!
		sb.append(" message=").append(toString(mess));
		return sb.toString();
	}

	/**
	 * @param mm
	 * @return
	 */
	public static String toString(MidiMessage mm) {
		String s = null;
		if (mm instanceof ShortMessage) {
			ShortMessage se = (ShortMessage) mm;
			s = toString(se);
		} else if (mm instanceof SysexMessage) {
			SysexMessage sex = (SysexMessage) mm;
			s = toString(sex);
		} else if (mm instanceof MetaMessage) {
			MetaMessage meta = (MetaMessage) mm;
			s = toString(meta);
		} else {
			s = "WTF?";
		}
		return s;
	}

	public static String toString(ShortMessage se) {
		StringBuilder sb = new StringBuilder();
		// sb.append(" tick " + se.getTick());
		sb.append("cmd 0x").append(Integer.toHexString(se.getCommand()));
		sb.append(" chan ").append(se.getChannel());
		sb.append(" d1 0x").append(Integer.toHexString(se.getData1()));
		sb.append(" d2 0x").append(Integer.toHexString(se.getData2()));
		sb.append(" len ").append(se.getLength());
		sb.append(" status 0x").append(Integer.toHexString(se.getStatus()));
		return sb.toString();
	}

	public static String toString(SysexMessage sex) {
		StringBuilder sb = new StringBuilder();
		// TODO do I really want to do this?
		return sb.toString();
	}

	/**
	 * @param me
	 * @return
	 */
	public static String toStringFull(MidiEvent me) {
		StringBuilder sb = new StringBuilder();
		MidiMessage mess = me.getMessage();
		long tick = me.getTick();
		sb.append("tick=" + tick); // no append for long!
		sb.append(" ").append(toStringFull(mess));
		return sb.toString();
	}

	/**
	 * @param mm
	 * @return
	 */
	public static String toStringFull(MidiMessage mm) {
		String s = null;
		if (mm instanceof ShortMessage) {
			ShortMessage se = (ShortMessage) mm;
			s = printFull(se);
		} else if (mm instanceof SysexMessage) {
			SysexMessage sex = (SysexMessage) mm;
			s = toString(sex);
		} else if (mm instanceof MetaMessage) {
			MetaMessage meta = (MetaMessage) mm;
			s = toString(meta);
		} else {
			s = "WTF?";
		}
		return s;
	}

	/**
	 * @param sequence
	 * @param os
	 */
	public static void write(Sequence sequence, OutputStream os) {
		try {
			int[] types = MidiSystem.getMidiFileTypes(sequence);

			if (MidiSystem.isFileTypeSupported(1, sequence)) {
				if (MidiSystem.write(sequence, 1, os) == -1) {
					throw new IOException("Problems writing to stream");
				}
			} else {
				if (MidiSystem.write(sequence, types[0], os) == -1) {
					throw new IOException("Problems writing to stream");
				}
			}

		} catch (IOException ie) {
			logger.error(ie.getLocalizedMessage(), ie);
		}
	}

	/**
	 * @param sequence
	 * @param filename
	 */
	public static void write(Sequence sequence, String filename) {
		/*
		 * this seems to be only used to read.
		 * 
		 * MidiFileFormat mff = new MidiFileFormat( 1, // type == one or more
		 * simultaneous tracks Sequence.PPQ, sequence.getResolution(),
		 * MidiFileFormat.UNKNOWN_LENGTH, sequence.getMicrosecondLength()); //
		 * microsecs // s dur in microseconds // mff dur in nanoseconds
		 */
		try {
			/*
			 * int[] types = MidiSystem.getMidiFileTypes(sequence); for(int i=0;
			 * i<types.length; i++) logger.debug("types: " + types[i]);
			 * MidiFileFormat[] forms = MidiSystem.getMidiFileFormats(types[0],
			 * sequence); for(int i=0; i<forms.length; i++) { print(forms[i]); }
			 */
			int[] types = MidiSystem.getMidiFileTypes(sequence);

			if (MidiSystem.isFileTypeSupported(1, sequence)) {
				logger.debug("Writing MIDI type 1 file {}", filename);
				if (MidiSystem.write(sequence, 1, new File(filename)) == -1) {
					throw new IOException("Problems writing to file");
				}
			} else {
				if (MidiSystem.write(sequence, types[0], new File(filename)) == -1) {
					throw new IOException("Problems writing to file");
				}
			}

		} catch (IOException e) {
		    logger.error(e.getLocalizedMessage(),e);
		}
	}

	/**
	 * @param sequence
	 * @param filename
	 * @param type
	 */
	public static void write(Sequence sequence, String filename, int type) {
		try {
			int[] types = MidiSystem.getMidiFileTypes(sequence);
			// check type and types

			if (MidiSystem.write(sequence, type, new File(filename)) == -1) {
				throw new IOException("Problems writing to file");
			}

			/*
			 * MidiFileFormat[] forms = MidiSystem.getMidiFileFormats(type,
			 * sequence);
			 * 
			 * 
			 * logger.debug("This number of formats possible " + forms.length);
			 * if (forms.length == 0) { logger.debug("Can't save sequence as
			 * type"); } else { if(debug) print(forms[0]); if
			 * (MidiSystem.write(sequence, forms[0], new File(filename)) == -1)
			 * { throw new IOException("Problems writing to file"); } }
			 */

		} catch (IOException ie) {
			logger.error(ie.getLocalizedMessage(),ie);
		}
	}

	/**
	 * No instantiation
	 */
	private MIDIUtils() {
	}

	void junk() {
		Sequencer seq;
		Transmitter seqTrans;
		Synthesizer synth;
		Receiver synthRcvr;
		try {
			seq = MidiSystem.getSequencer();
			seqTrans = seq.getTransmitter();

			// or plug in your own synth
			synth = MidiSystem.getSynthesizer();
			synthRcvr = synth.getReceiver();
			seqTrans.setReceiver(synthRcvr);
		} catch (MidiUnavailableException e) {
			// handle or throw exception
		}
	}

	/**
	 * Stops the currently playing sequencer and closes it.
	 */
	public void stopPlaying() {
		if (sequencer == null) {
			return;
		}
		if (sequencer.isRunning()) {
			sequencer.stop();
		}
		if (sequencer.isOpen()) {
			sequencer.close();
		}
	}

}

/*
 * ShortMessage.NOTE_OFF 8 ShortMessage.NOTE_ON 9 ShortMessage.POLY_PRESSURE a
 * ShortMessage.CONTROL_CHANGE b ShortMessage.PROGRAM_CHANGE c
 * ShortMessage.CHANNEL_PRESSURE d ShortMessage.PITCH_BEND e
 */

/*
 * info is like this:
 * 
 * system info 6 name Java Sound Synthesizer vendor Sun Microsystems version
 * Version 1.0 description Software wavetable synthesizer and receiver Is a
 * synthesizer
 * 
 * name Java Sound Sequencer vendor Sun Microsystems version Version 1.0
 * description Software sequencer / synthesizer module Is a synthesizer
 * 
 * name MIDI Mapper vendor Unknown Vendor version Unknown Version description
 * Windows MIDI_MAPPER driver
 * 
 * name Out- USB MidiSport 1x1 vendor Unknown Vendor version Unknown Version
 * description External MIDI Port
 * 
 * name Maetsro MPU-401 vendor Unknown Vendor version Unknown Version
 * description External MIDI Port
 * 
 * name Maestro WaveSynth vendor Unknown Vendor version Unknown Version
 * description Internal synthesizer (generic)
 */
