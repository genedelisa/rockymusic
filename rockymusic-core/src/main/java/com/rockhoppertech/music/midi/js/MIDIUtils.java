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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
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

/**
 * Class <code>MidiUtils</code> is a collection of ,um, MIDI utilities.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 */
public class MIDIUtils {
	public static final String ALL_CONTROLLERS_OFF_CONTROLLER = "All Controllers Off";
	public static final String ALL_NOTES_OFF_CONTROLLER = "All Notes Off";
	public static final String ALL_SOUND_OFF_CONTROLLER = "All Sound Off";
	public static final String BALANCE_COARSE_CONTROLLER = "Balance (coarse)";
	public static final String BALANCE_FINE_CONTROLLER = "Balance (fine)";
	public static final String BANK_SELECT_COARSE_CONTROLLER = "Bank Select (coarse)";
	public static final String BANK_SELECT_FINE_CONTROLLER = "Bank Select (fine)";
	public static final String BREATH_CONTROLLER_COARSE_CONTROLLER = "Breath controller (coarse)";
	public static final String BREATH_CONTROLLER_FINE_CONTROLLER = "Breath controller (fine)";

	// look at CONTROLLER_NAMES
	public static final int CC_BANK_SELECT_COARSE = 0;

	public static final int CC_BANK_SELECT_FINE = 32;

	public static final int CC_MODWHEEL_COARSE = 1;

	public static final int CC_MODWHEEL_FINE = 33;

	public static final int CC_PAN_COARSE = 10;

	public static final int CC_PAN_FINE = 42;

	/**
	 * 0 (to 63) is off. 127 (to 64) is on.
	 */
	public static final int CC_PORTAMENTO_ON_OFF = 65;

	public static final int CC_PORTAMENTO_TIME_COARSE = 5;

	public static final int CC_PORTAMENTO_TIME_FINE = 37;

	/**
	 * sometimes called the hold pedal. 0 (to 63) is off. 127 (to 64) is on.
	 */
	public static final int CC_SUSTAIN = 64;

	public static final int CC_VOLUME_COARSE = 7;

	public static final int CC_VOLUME_FINE = 39;

	public static final String CELESTE_LEVEL_CONTROLLER = "Celeste Level";

	public static final String CHORUS_LEVEL_CONTROLLER = "Chorus Level";

	// defined controllers
	public static final Map<String, Integer> CONTROLLER_NAMES = new HashMap<String, Integer>();

	public static final String DATA_BUTTON_DECREMENT_CONTROLLER = "Data Button decrement";

	public static final String DATA_BUTTON_INCREMENT_CONTROLLER = "Data Button increment";

	public static final String DATA_ENTRY_COARSE_CONTROLLER = "Data Entry (coarse)";

	public static final String DATA_ENTRY_FINE_CONTROLLER = "Data Entry (fine)";

	public static boolean debug = false;

	private static int division = 256;

	public static final String EFFECT_CONTROL_1_COARSE_CONTROLLER = "Effect Control 1 (coarse)";
	public static final String EFFECT_CONTROL_1_FINE_CONTROLLER = "Effect Control 1 (fine)";
	public static final String EFFECT_CONTROL_2_COARSE_CONTROLLER = "Effect Control 2 (coarse)";
	public static final String EFFECT_CONTROL_2_FINE_CONTROLLER = "Effect Control 2 (fine)";
	public static final String EFFECTS_LEVEL_CONTROLLER = "Effects Level";

	public static final byte ENDOFTRACK = 0x2F;

	public static final String EXPRESSION_COARSE_CONTROLLER = "Expression (coarse)";
	public static final String EXPRESSION_FINE_CONTROLLER = "Expression (fine)";

	// FF 03 len text

	public static final String FOOT_PEDAL_COARSE_CONTROLLER = "Foot Pedal (coarse)";

	public static final String FOOT_PEDAL_FINE_CONTROLLER = "Foot Pedal (fine)";

	public static final String GENERAL_PURPOSE_BUTTON_1_ON_OFF_CONTROLLER = "General Purpose Button 1 (on/off)";

	public static final String GENERAL_PURPOSE_BUTTON_2_ON_OFF_CONTROLLER = "General Purpose Button 2 (on/off)";

	public static final String GENERAL_PURPOSE_BUTTON_3_ON_OFF_CONTROLLER = "General Purpose Button 3 (on/off)";

	public static final String GENERAL_PURPOSE_BUTTON_4_ON_OFF_CONTROLLER = "General Purpose Button 4 (on/off)";

	public static final String GENERAL_PURPOSE_SLIDER_1_CONTROLLER = "General Purpose Slider 1";

	public static final String GENERAL_PURPOSE_SLIDER_2_CONTROLLER = "General Purpose Slider 2";

	public static final String GENERAL_PURPOSE_SLIDER_3_CONTROLLER = "General Purpose Slider 3";

	public static final String GENERAL_PURPOSE_SLIDER_4_CONTROLLER = "General Purpose Slider 4";

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

	public static final String HOLD_2_PEDAL_ON_OFF_CONTROLLER = "Hold 2 Pedal (on/off)";
	public static final String HOLD_PEDAL_ON_OFF_CONTROLLER = "Hold Pedal (on/off)";

	public static final String LEGATO_PEDAL_ON_OFF_CONTROLLER = "Legato Pedal (on/off)";

	public static final String LOCAL_KEYBOARD_ON_OFF_CONTROLLER = "Local Keyboard (on/off)";

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
	public static final String MODULATION_WHEEL_COARSE_CONTROLLER = "Modulation Wheel (coarse)";
	public static final String MODULATION_WHEEL_FINE_CONTROLLER = "Modulation Wheel (fine)";
	public static final String MONO_OPERATION_CONTROLLER = "Mono Operation";
	public static final String NON_REGISTERED_PARAMETER_COARSE_CONTROLLER = "Non-registered Parameter (coarse)";
	public static final String NON_REGISTERED_PARAMETER_FINE_CONTROLLER = "Non-registered Parameter (fine)";

	public static final String PAN_POSITION_COARSE_CONTROLLER = "Pan position (coarse)";
	public static final String PAN_POSITION_FINE_CONTROLLER = "Pan position (fine)";

	public static final String PHASER_LEVEL_CONTROLLER = "Phaser Level";

	public static final String POLY_OPERATION_CONTROLLER = "Poly Operation";
	public static final String PORTAMENTO_ON_OFF_CONTROLLER = "Portamento (on/off)";
	public static final String PORTAMENTO_TIME_COARSE_CONTROLLER = "Portamento Time (coarse)";
	public static final String PORTAMENTO_TIME_FINE_CONTROLLER = "Portamento Time (fine)";
	public static final String REGISTERED_PARAMETER_COARSE_CONTROLLER = "Registered Parameter (coarse)";
	public static final String REGISTERED_PARAMETER_FINE_CONTROLLER = "Registered Parameter (fine)";

	private static Sequencer sequencer;

	public static final String SOFT_PEDAL_ON_OFF_CONTROLLER = "Soft Pedal (on/off)";
	public static final String SOUND_ATTACK_TIME_CONTROLLER = "Sound Attack Time";
	public static final String SOUND_BRIGHTNESS_CONTROLLER = "Sound Brightness";
	public static final String SOUND_CONTROL_10_CONTROLLER = "Sound Control 10";
	public static final String SOUND_CONTROL_6_CONTROLLER = "Sound Control 6";
	public static final String SOUND_CONTROL_7_CONTROLLER = "Sound Control 7";
	public static final String SOUND_CONTROL_8_CONTROLLER = "Sound Control 8";
	public static final String SOUND_CONTROL_9_CONTROLLER = "Sound Control 9";
	public static final String SOUND_RELEASE_TIME_CONTROLLER = "Sound Release Time";
	public static final String SOUND_TIMBRE_CONTROLLER = "Sound Timbre";
	public static final String SOUND_VARIATION_CONTROLLER = "Sound Variation";

	public static final String SUSTENUTO_PEDAL_ON_OFF_CONTROLLER = "Sustenuto Pedal (on/off)";

	// public static final List SYNTH_LIST;

	// private static float tempo = 120f;
	//
	// static {
	// SYNTH_LIST = new LinkedList();
	// }
	static {

		// public static final String BANK_SELECT_CONTROLLER = "Bank Select
		// (coarse)";
		CONTROLLER_NAMES.put(HOLD_PEDAL_ON_OFF_CONTROLLER, new Integer(64));
		CONTROLLER_NAMES.put(ALL_NOTES_OFF_CONTROLLER, new Integer(123));
		CONTROLLER_NAMES.put(SOUND_TIMBRE_CONTROLLER, new Integer(71));
		CONTROLLER_NAMES.put(
				GENERAL_PURPOSE_BUTTON_2_ON_OFF_CONTROLLER,
				new Integer(81));
		CONTROLLER_NAMES.put(FOOT_PEDAL_FINE_CONTROLLER, new Integer(36));
		CONTROLLER_NAMES
				.put(BREATH_CONTROLLER_FINE_CONTROLLER, new Integer(34));
		CONTROLLER_NAMES.put(BALANCE_FINE_CONTROLLER, new Integer(40));
		CONTROLLER_NAMES.put(FOOT_PEDAL_COARSE_CONTROLLER, new Integer(4));
		CONTROLLER_NAMES.put(SOUND_CONTROL_7_CONTROLLER, new Integer(76));
		CONTROLLER_NAMES.put(
				GENERAL_PURPOSE_BUTTON_1_ON_OFF_CONTROLLER,
				new Integer(80));
		CONTROLLER_NAMES.put(PHASER_LEVEL_CONTROLLER, new Integer(95));
		CONTROLLER_NAMES.put(MONO_OPERATION_CONTROLLER, new Integer(126));
		CONTROLLER_NAMES.put(
				EFFECT_CONTROL_2_COARSE_CONTROLLER,
				new Integer(13));
		CONTROLLER_NAMES.put(DATA_ENTRY_FINE_CONTROLLER, new Integer(38));
		CONTROLLER_NAMES.put(SOUND_CONTROL_9_CONTROLLER, new Integer(78));
		CONTROLLER_NAMES.put(BANK_SELECT_FINE_CONTROLLER, new Integer(32));
		CONTROLLER_NAMES.put(EFFECT_CONTROL_2_FINE_CONTROLLER, new Integer(45));
		CONTROLLER_NAMES.put(BALANCE_COARSE_CONTROLLER, new Integer(8));
		CONTROLLER_NAMES.put(EXPRESSION_COARSE_CONTROLLER, new Integer(11));
		CONTROLLER_NAMES
				.put(LOCAL_KEYBOARD_ON_OFF_CONTROLLER, new Integer(122));
		CONTROLLER_NAMES.put(SOUND_ATTACK_TIME_CONTROLLER, new Integer(73));
		CONTROLLER_NAMES
				.put(SUSTENUTO_PEDAL_ON_OFF_CONTROLLER, new Integer(66));
		CONTROLLER_NAMES.put(
				REGISTERED_PARAMETER_COARSE_CONTROLLER,
				new Integer(101));
		CONTROLLER_NAMES.put(DATA_BUTTON_DECREMENT_CONTROLLER, new Integer(97));
		CONTROLLER_NAMES.put(SOUND_CONTROL_8_CONTROLLER, new Integer(77));
		CONTROLLER_NAMES.put(LEGATO_PEDAL_ON_OFF_CONTROLLER, new Integer(68));
		CONTROLLER_NAMES.put(CHORUS_LEVEL_CONTROLLER, new Integer(93));
		CONTROLLER_NAMES.put(PAN_POSITION_COARSE_CONTROLLER, new Integer(10));
		CONTROLLER_NAMES.put(ALL_CONTROLLERS_OFF_CONTROLLER, new Integer(121));
		CONTROLLER_NAMES.put(SOUND_CONTROL_6_CONTROLLER, new Integer(75));
		// CONTROLLER_NAMES.put(VOLUME_FINE_CONTROLLER,
		// new Integer(39));
		// CONTROLLER_NAMES.put(OMNI_MODE_ON_CONTROLLER,
		// new Integer(125));
		CONTROLLER_NAMES.put(PORTAMENTO_ON_OFF_CONTROLLER, new Integer(65));
		CONTROLLER_NAMES.put(SOUND_RELEASE_TIME_CONTROLLER, new Integer(72));
		CONTROLLER_NAMES.put(GENERAL_PURPOSE_SLIDER_3_CONTROLLER, new Integer(
				18));
		CONTROLLER_NAMES.put(
				GENERAL_PURPOSE_BUTTON_3_ON_OFF_CONTROLLER,
				new Integer(82));
		CONTROLLER_NAMES.put(PORTAMENTO_TIME_FINE_CONTROLLER, new Integer(37));
		// CONTROLLER_NAMES.put(VOLUME_COARSE_CONTROLLER,
		// new Integer(7));
		CONTROLLER_NAMES.put(GENERAL_PURPOSE_SLIDER_4_CONTROLLER, new Integer(
				19));
		CONTROLLER_NAMES.put(DATA_BUTTON_INCREMENT_CONTROLLER, new Integer(96));
		CONTROLLER_NAMES.put(REGISTERED_PARAMETER_FINE_CONTROLLER, new Integer(
				100));
		CONTROLLER_NAMES.put(PAN_POSITION_FINE_CONTROLLER, new Integer(42));
		CONTROLLER_NAMES.put(EFFECTS_LEVEL_CONTROLLER, new Integer(91));
		CONTROLLER_NAMES.put(
				NON_REGISTERED_PARAMETER_COARSE_CONTROLLER,
				new Integer(99));
		// CONTROLLER_NAMES.put(TREMULO_LEVEL_CONTROLLER,
		// new Integer(92));
		CONTROLLER_NAMES.put(GENERAL_PURPOSE_SLIDER_2_CONTROLLER, new Integer(
				17));
		CONTROLLER_NAMES.put(CELESTE_LEVEL_CONTROLLER, new Integer(94));
		CONTROLLER_NAMES.put(EFFECT_CONTROL_1_FINE_CONTROLLER, new Integer(44));
		CONTROLLER_NAMES.put(EXPRESSION_FINE_CONTROLLER, new Integer(43));
		CONTROLLER_NAMES.put(SOUND_BRIGHTNESS_CONTROLLER, new Integer(74));
		CONTROLLER_NAMES.put(
				NON_REGISTERED_PARAMETER_FINE_CONTROLLER,
				new Integer(98));
		CONTROLLER_NAMES.put(SOUND_CONTROL_10_CONTROLLER, new Integer(79));
		CONTROLLER_NAMES.put(
				BREATH_CONTROLLER_COARSE_CONTROLLER,
				new Integer(2));
		CONTROLLER_NAMES.put(PORTAMENTO_TIME_COARSE_CONTROLLER, new Integer(5));
		CONTROLLER_NAMES
				.put(MODULATION_WHEEL_COARSE_CONTROLLER, new Integer(1));
		CONTROLLER_NAMES.put(HOLD_2_PEDAL_ON_OFF_CONTROLLER, new Integer(69));
		CONTROLLER_NAMES.put(
				EFFECT_CONTROL_1_COARSE_CONTROLLER,
				new Integer(12));
		CONTROLLER_NAMES.put(BANK_SELECT_COARSE_CONTROLLER, new Integer(0));
		CONTROLLER_NAMES.put(POLY_OPERATION_CONTROLLER, new Integer(127));
		CONTROLLER_NAMES.put(GENERAL_PURPOSE_SLIDER_1_CONTROLLER, new Integer(
				16));
		CONTROLLER_NAMES.put(ALL_SOUND_OFF_CONTROLLER, new Integer(120));
		CONTROLLER_NAMES.put(
				GENERAL_PURPOSE_BUTTON_4_ON_OFF_CONTROLLER,
				new Integer(83));
		CONTROLLER_NAMES.put(MODULATION_WHEEL_FINE_CONTROLLER, new Integer(33));
		// CONTROLLER_NAMES.put(OMNI_MODE_OFF_CONTROLLER,
		// new Integer(124));
		CONTROLLER_NAMES.put(DATA_ENTRY_COARSE_CONTROLLER, new Integer(6));
		CONTROLLER_NAMES.put(SOFT_PEDAL_ON_OFF_CONTROLLER, new Integer(67));
		CONTROLLER_NAMES.put(SOUND_VARIATION_CONTROLLER, new Integer(70));
		/*
		 * CONTROLLER_NAMES.put("Bank Select (coarse)", new Integer(0));
		 * CONTROLLER_NAMES.put("Modulation Wheel (coarse)", new Integer(1));
		 * CONTROLLER_NAMES.put("Breath controller (coarse)", new Integer(2));
		 * CONTROLLER_NAMES.put("Foot Pedal (coarse)", new Integer(4));
		 * CONTROLLER_NAMES.put("Portamento Time (coarse)", new Integer(5));
		 * CONTROLLER_NAMES.put("Data Entry (coarse)", new Integer(6));
		 * CONTROLLER_NAMES.put("Volume (coarse)", new Integer(7));
		 * CONTROLLER_NAMES.put("Balance (coarse)", new Integer(8));
		 * CONTROLLER_NAMES.put("Pan position (coarse)", new Integer(10));
		 * CONTROLLER_NAMES.put("Expression (coarse)", new Integer(11));
		 * CONTROLLER_NAMES.put("Effect Control 1 (coarse)", new Integer(12));
		 * CONTROLLER_NAMES.put("Effect Control 2 (coarse)", new Integer(13));
		 * CONTROLLER_NAMES.put("General Purpose Slider 1", new Integer(16));
		 * CONTROLLER_NAMES.put("General Purpose Slider 2", new Integer(17));
		 * CONTROLLER_NAMES.put("General Purpose Slider 3", new Integer(18));
		 * CONTROLLER_NAMES.put("General Purpose Slider 4", new Integer(19));
		 * CONTROLLER_NAMES.put("Bank Select (fine)", new Integer(32));
		 * CONTROLLER_NAMES.put("Modulation Wheel (fine)", new Integer(33));
		 * CONTROLLER_NAMES.put("Breath controller (fine)", new Integer(34));
		 * CONTROLLER_NAMES.put("Foot Pedal (fine)", new Integer(36));
		 * CONTROLLER_NAMES.put("Portamento Time (fine)", new Integer(37));
		 * CONTROLLER_NAMES.put("Data Entry (fine)", new Integer(38));
		 * CONTROLLER_NAMES.put("Volume (fine)", new Integer(39));
		 * CONTROLLER_NAMES.put("Balance (fine)", new Integer(40));
		 * CONTROLLER_NAMES.put("Pan position (fine)", new Integer(42));
		 * CONTROLLER_NAMES.put("Expression (fine)", new Integer(43));
		 * CONTROLLER_NAMES.put("Effect Control 1 (fine)", new Integer(44));
		 * CONTROLLER_NAMES.put("Effect Control 2 (fine)", new Integer(45));
		 * CONTROLLER_NAMES.put("Hold Pedal (on/off)", new Integer(64));
		 * CONTROLLER_NAMES.put("Portamento (on/off)", new Integer(65));
		 * CONTROLLER_NAMES.put("Sustenuto Pedal (on/off)", new Integer(66));
		 * CONTROLLER_NAMES.put("Soft Pedal (on/off)", new Integer(67));
		 * CONTROLLER_NAMES.put("Legato Pedal (on/off)", new Integer(68));
		 * CONTROLLER_NAMES.put("Hold 2 Pedal (on/off)", new Integer(69));
		 * CONTROLLER_NAMES.put("Sound Variation", new Integer(70));
		 * CONTROLLER_NAMES.put("Sound Timbre", new Integer(71));
		 * CONTROLLER_NAMES.put("Sound Release Time", new Integer(72));
		 * CONTROLLER_NAMES.put("Sound Attack Time", new Integer(73));
		 * CONTROLLER_NAMES.put("Sound Brightness", new Integer(74));
		 * CONTROLLER_NAMES.put("Sound Control 6", new Integer(75));
		 * CONTROLLER_NAMES.put("Sound Control 7", new Integer(76));
		 * CONTROLLER_NAMES.put("Sound Control 8", new Integer(77));
		 * CONTROLLER_NAMES.put("Sound Control 9", new Integer(78));
		 * CONTROLLER_NAMES.put("Sound Control 10", new Integer(79));
		 * CONTROLLER_NAMES.put("General Purpose Button 1 (on/off)", new
		 * Integer( 80)); CONTROLLER_NAMES.put("General Purpose Button 2
		 * (on/off)", new Integer( 81)); CONTROLLER_NAMES.put("General Purpose
		 * Button 3 (on/off)", new Integer( 82)); CONTROLLER_NAMES.put("General
		 * Purpose Button 4 (on/off)", new Integer( 83));
		 * CONTROLLER_NAMES.put("Effects Level", new Integer(91));
		 * CONTROLLER_NAMES.put("Tremulo Level", new Integer(92));
		 * CONTROLLER_NAMES.put("Chorus Level", new Integer(93));
		 * CONTROLLER_NAMES.put("Celeste Level", new Integer(94));
		 * CONTROLLER_NAMES.put("Phaser Level", new Integer(95));
		 * CONTROLLER_NAMES.put("Data Button increment", new Integer(96));
		 * CONTROLLER_NAMES.put("Data Button decrement", new Integer(97));
		 * CONTROLLER_NAMES .put("Non-registered Parameter (fine)", new
		 * Integer(98)); CONTROLLER_NAMES.put("Non-registered Parameter
		 * (coarse)", new Integer( 99)); CONTROLLER_NAMES.put("Registered
		 * Parameter (fine)", new Integer(100));
		 * CONTROLLER_NAMES.put("Registered Parameter (coarse)", new
		 * Integer(101)); CONTROLLER_NAMES.put("All Sound Off", new
		 * Integer(120)); CONTROLLER_NAMES.put("All Controllers Off", new
		 * Integer(121)); CONTROLLER_NAMES.put("Local Keyboard (on/off)", new
		 * Integer(122)); CONTROLLER_NAMES.put("All Notes Off", new
		 * Integer(123)); CONTROLLER_NAMES.put("Omni Mode Off", new
		 * Integer(124)); CONTROLLER_NAMES.put("Omni Mode On", new
		 * Integer(125)); CONTROLLER_NAMES.put("Mono Operation", new
		 * Integer(126)); CONTROLLER_NAMES.put("Poly Operation", new
		 * Integer(127));
		 */
	}

	// every track must end with: FF 2F 00
	public static void addEndOfTrack(Track track) {
		try {
			MetaMessage message = new MetaMessage();
			// type, data, length, tick
			message.setMessage(ENDOFTRACK, new byte[] { 0 }, 1);
			MidiEvent event = new MidiEvent(message, track.ticks() + 480);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			System.err.println(e);
		}
	}

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
	 * adds the note to the end of the track.
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
				System.err.println("MidiUtils.CreateShortMess Bad status "
						+ (status));
			}
		} catch (InvalidMidiDataException e) {
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

	public static String getControllerName(Integer value) {
		String r = "Unknown controller: " + value;
		for (Iterator<Entry<String, Integer>> i = CONTROLLER_NAMES.entrySet()
				.iterator(); i.hasNext();) {
			Entry<String, Integer> e = i.next();
			if (e.getValue().equals(value)) {
				r = e.getKey();
				break;
			}
		}
		return r;
	}

	public static int getControllerNumber(String name) {
		Integer i = (Integer) CONTROLLER_NAMES.get(name);
		if (i == null) {
			System.err.println("name is null");
			throw new IllegalArgumentException(name);
		}
		return i.intValue();
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

	static Map<String, MidiDevice.Info> getSynthList() {
		MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
		Map<String, MidiDevice.Info> map = new HashMap<String, MidiDevice.Info>();
		try {
			for (int i = 0; i < info.length; i++) {
				MidiDevice md = MidiSystem.getMidiDevice(info[i]);
				if (md instanceof Synthesizer) {
					map.put(info[i].getName(), info[i]);
					logger.debug("name " + info[i].getName());
					logger.debug(" Is a synthesizer ");
				}
			}
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		return map;
	}

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
			System.err.println(e);
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

	public static void insertPan(Track track, long tick, int chan, int value) {
		if (value < 0 || value > 127) {
			throw new IllegalArgumentException(
					"value must be between 0 and 127");
		}
		insertControlChange(track, tick, chan, CC_PAN_COARSE, value);
	}

	public static void insertPortamentoOff(Track track, long tick, int chan) {
		insertControlChange(track, tick, chan, CC_PORTAMENTO_ON_OFF, 0);
	}

	public static void insertPortamentoOn(Track track, long tick, int chan) {
		insertControlChange(track, tick, chan, CC_PORTAMENTO_ON_OFF, 127);
	}

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

	public static void insertSustainOff(Track track, long tick, int chan) {
		insertControlChange(track, tick, chan, CC_SUSTAIN, 0);
	}

	public static void insertSustainOn(Track track, long tick, int chan) {
		insertControlChange(track, tick, chan, CC_SUSTAIN, 127);
	}

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
			System.err.println(e);
		}
	}

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
			System.err.println(e);
		}
	}

	public static void insertVolumeChange(Track track, long tick, int chan,
			int value) {
		insertControlChange(track, tick, chan, CC_VOLUME_COARSE, value);
	}

	public static boolean isChannelMessage(int b) {
		if ((b < 0xF0) && (b >= 0x80)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isCopyright(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_COPYRIGHT) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isCuePoint(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_CUE_POINT) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isInstrument(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_INSTRUMENT) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isLyric(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_LYRIC) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMarker(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_MARKER) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMetaMessage(int aByte) {
		if (0xFF == aByte) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isName(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_NAME) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSysexMessage(int aByte) {
		if (0xF0 == aByte) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isText(MetaMessage mm) {
		int type = mm.getType();
		if (type == META_TEXT) {
			return true;
		} else {
			return false;
		}
	}

	public static void log(ShortMessage se) {
		logger.debug(toString(se));
	}

	public static void logDebug(ShortMessage se) {
		logger.debug(toString(se));
	}

	public static void logError(ShortMessage se) {
		logger.error(toString(se));
	}

	public static void logInfo(ShortMessage se) {
		logger.info(toString(se));
	}

	public static void main(String[] args) {
		MIDIUtils.systemInfo();
	}

	public static void print(MetaMessage event) {
		//PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));

		int type = event.getType();
		logger.debug("printMetaMessage " + type);
		switch (type) {
		case META_TEXT:
			logger.debug("Text");
			logger.debug(new String(event.getData()));
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
			logger.debug("value " + value + " BPM");
			logger.debug("tempo " + tempo + " microseconds per quarter note");
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

		}

		byte[] data = event.getData();
		logger.debug("data: ");
		for (int i = 0; i < data.length; i++) {
			System.out.print(" " + Integer.toHexString(data[i]));
		}
	}

	public static void print(MidiFileFormat form) {
		logger.debug("form: " + form);
		logger.debug("form division: " + form.getDivisionType());
		// logger.debug("form len: " + form.getLength());
		// logger.debug("form dur: " + form.getDuration());
		logger.debug("form res: " + form.getResolution());
		logger.debug("form type: " + form.getType());
	}

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
		for (int i = 0; i < patches.length; i++) {
			logger.debug("bank: " + patches[i].getBank() + " p: "
					+ patches[i].getProgram());
		}

		Track[] st = sequence.getTracks();
		logger.debug("" + st.length + " Tracks ");
		for (int i = 0; i < st.length; i++) {
			logger.debug("Track " + i
					+ " ======================================");
			print(st[i]);
		}

		Patch[] p = sequence.getPatchList();
		for (int i = 0; i < p.length; i++) {
			logger.debug("patch bank: " + p[i].getBank());
			logger.debug("patch program: " + p[i].getProgram());
		}
	}

	public static void print(Sequence sequence, MIDIEventFilter filter) {
		Track[] st = sequence.getTracks();
		for (int i = 0; i < st.length; i++) {
			print(st[i], filter);
		}
	}

	public static void print(ShortMessage se) {
		logger.debug(toString(se));
	}

	public static void print(SysexMessage event) {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
		writer.println("Sysex event");
		byte[] data = event.getData();
		writer.println("data: ");
		for (int i = 0; i < data.length; i++) {
			writer.print(" " + Integer.toHexString(data[i]));
		}
		writer.println();
	}

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
			sb.append(" ").append(getControllerName(se.getData1()));

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

	static void printKeySignature(MetaMessage event) {
		String str = getKeySignature(event);
		logger.debug(str);
	}

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

	static void printTimeSignature(MetaMessage event) {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
		printTimeSignature(writer, event);
	}

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

	public static Sequence read(File f) {
		return read(f.getAbsolutePath());
	}

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

	static void systemInfo() {
		MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
		logger.debug("system info " + info.length);

		try {
			for (int i = 0; i < info.length; i++) {
				logger.debug("name " + info[i].getName());
				logger.debug("vendor " + info[i].getVendor());
				logger.debug("version " + info[i].getVersion());
				logger.debug("description " + info[i].getDescription());
				MidiDevice md = MidiSystem.getMidiDevice(info[i]);
				logger.debug("device class " + md.getClass().getName());

				if (md instanceof Synthesizer) {
					// SYNTH_LIST.add(info[i]);

					logger.debug("Is a synthesizer ");
					Synthesizer synth = (Synthesizer) md;
					Instrument[] insts = synth.getAvailableInstruments();
					for (int j = 0; j < insts.length; j++) {
						logger.debug("inst: " + insts[j].getName());
					}

				} else if (md instanceof Sequencer) {
					logger.debug("Is a sequencer ");
				}
			}
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

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
		for (int i = 0; i < data.length; i++) {
			sb.append(" ").append(Integer.toHexString(data[i]));
		}
		return sb.toString();
	}

	public static String toString(MidiEvent me) {
		StringBuffer sb = new StringBuffer();
		MidiMessage mess = me.getMessage();
		long tick = me.getTick();
		sb.append("tick=" + tick); // no append for long!
		sb.append(" message=").append(toString(mess));
		return sb.toString();
	}

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

	public static String toStringFull(MidiEvent me) {
		StringBuffer sb = new StringBuffer();
		MidiMessage mess = me.getMessage();
		long tick = me.getTick();
		sb.append("tick=" + tick); // no append for long!
		sb.append(" ").append(toStringFull(mess));
		return sb.toString();
	}

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

		} catch (java.io.IOException ie) {
			System.err.println(ie);
		}
	}

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

		} catch (java.io.IOException ie) {
			System.err.println(ie);
		}
	}

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

		} catch (java.io.IOException ie) {
			System.err.println(ie);
		}
	}

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

/*
 * History:
 * 
 * $Log$
 * 
 * This version: $Revision$ Last modified: $Date$ Last modified by: $Author$
 */
