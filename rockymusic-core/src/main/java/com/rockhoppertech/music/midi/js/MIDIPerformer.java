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

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDIPerformer {

	private static final Logger logger = LoggerFactory
			.getLogger(MIDIPerformer.class);

	public static MidiDevice.Info getMidiDeviceInfo(String strDeviceName,
			boolean bForOutput) {

		MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
		for (Info aInfo : aInfos) {
			if (aInfo.getName().equals(strDeviceName)) {
				try {
					MidiDevice device = MidiSystem.getMidiDevice(aInfo);
					boolean bAllowsInput = device.getMaxTransmitters() != 0;
					boolean bAllowsOutput = device.getMaxReceivers() != 0;
					if (bAllowsOutput && bForOutput || bAllowsInput
							&& !bForOutput) {
						return aInfo;
					}
				} catch (MidiUnavailableException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return null;
	}

	// public static void main(String[] args) {
	// MIDINoteList notelist = null;
	//
	// // notelist = MIDINoteListFactory
	// // .createFromIntervals(1, 2, 3);
	// // notelist.sequential();
	// MIDIPerformer perf = new MIDIPerformer();
	//
	// // perf.with(notelist).atTempo(240f).clickTrack(true).play();
	//
	// notelist = MIDINoteListFactory.createFromIntervals(3, 6, 1)
	// .sequential();
	// notelist.setInstrument(MIDIGMPatch.VOICES);
	// System.err.println(notelist);
	// perf.notelist(notelist).clickTrack(true).atTempo(60).play();
	//
	// }

	private boolean continuousLoop = false;
	private boolean exit = false;
	private List<MidiDevice> openedMidiDeviceList;
	private Object playLock = new Object();
	private Receiver receiver;
	private Sequence sequence;
	private Sequencer sequencer;
	private String sequencerName;
	private float tempo = 120f;
	private float tempoFactor = 1f;

	public MIDIPerformer() {
		openedMidiDeviceList = new ArrayList<MidiDevice>();
		// moved this to play()
		// this.setupSequencer();
	}

	public MIDIPerformer atTempo(float t) {
		tempo = t;
		return this;
	}

	void device(String deviceName) {
		MidiDevice.Info info = getMidiDeviceInfo(deviceName, true);
		if (info == null) {
			logger.error("Cannot find device " + deviceName);
		}
		try {
			MidiDevice midiDevice = MidiSystem.getMidiDevice(info);
			midiDevice.open();
			openedMidiDeviceList.add(midiDevice);
			Receiver midiReceiver = midiDevice.getReceiver();
			Transmitter midiTransmitter = sequencer.getTransmitter();
			midiTransmitter.setReceiver(midiReceiver);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public void play(Score score) {
		Sequence s = ScoreFactory.scoreToSequence(score);
		play(s);
	}

	public void play(MIDITrack track) {
		Sequence sequence = MIDITrackFactory.trackToSequence(track, 480);
		this.play(sequence);
	}

	public void play(Sequence sequence) {
		this.sequence = sequence;

		logger.debug("play");
		setupSequencer();
		if (receiver != null) {
			try {
				sequencer.getTransmitter().setReceiver(receiver);
			} catch (MidiUnavailableException me) {
				if (logger.isErrorEnabled()) {
					logger.error(me.getMessage(), me);
				}
				return;
			}
		}

		if (sequencer != null && sequencer.isOpen()) {
			sequencer.stop();
			sequencer.close();
			logger.error("stopped and closed already open sequencer");

		}
		if (sequencer != null) {
			try {
				sequencer.open();
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
				if (logger.isErrorEnabled()) {
					logger.error(e.getMessage(), e);
				}
				return;
			}
		}

		if (continuousLoop) {
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		}

		try {
			logger.debug("setting tempo to " + tempo);

			sequencer.setTempoFactor(1f);
			sequencer.setTempoInBPM(tempo);
			sequencer.setSequence(this.sequence);
			sequencer.start();
		} catch (InvalidMidiDataException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage(), e);
			}
			return;
		}
	}

	public void playlock(Sequence sequence) {
		synchronized (playLock) {
			if (sequencer != null && sequencer.isRunning()) {
				logger.debug("already RUNNING");
				while (true) {
					try {
						playLock.wait();
					} catch (InterruptedException e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				}
			}
			this.play(sequence);
		}
	}

	/**
	 * @see http://java.sun.com/docs/books/tutorial/sound/MIDI-messages.html
	 */
	private void setupSequencer() {
		if (sequencerName != null) {
			MidiDevice.Info seqInfo = getMidiDeviceInfo(sequencerName,
					true);
			if (seqInfo == null) {
				System.exit(1);
			}
			try {
				sequencer = (Sequencer) MidiSystem.getMidiDevice(seqInfo);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		} else {
			try {
				sequencer = MidiSystem.getSequencer();
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		sequencer.setTempoFactor(tempoFactor);
		sequencer.setTempoInBPM(tempo);

		// if (this.sequencer instanceof Synthesizer) {
		// /* Sun implementation; no action required. */
		// } else {
		// try {
		// Synthesizer synth = MidiSystem.getSynthesizer();
		// synth.open();
		// this.openedMidiDeviceList.add(synth);
		// Receiver synthReceiver = synth.getReceiver();
		// Transmitter seqTransmitter = this.sequencer.getTransmitter();
		// seqTransmitter.setReceiver(synthReceiver);
		// } catch (MidiUnavailableException e) {
		// e.printStackTrace();
		// }
		// }

		// int[] anControllers = new int[128];
		// for (int i = 0; i < anControllers.length; i++) {
		// anControllers[i] = i;
		// }
		// this.sequencer.addControllerEventListener(new
		// ControllerEventListener() {
		// public void controlChange(ShortMessage message) {
		// System.out.println("%%% ShortMessage: " +
		// MIDIUtils.printFull(message));
		// System.out.println("%%% ShortMessage controller: "
		// + message.getData1());
		// System.out.println("%%% ShortMessage value: "
		// + message.getData2());
		// }
		// }, anControllers);

		sequencer.addMetaEventListener(new MetaEventListener() {
			@Override
			public void meta(MetaMessage event) {
				logger.debug("MidiUtil meta: "
						+ Integer.toHexString(event.getType()));
				MIDIUtils.print(event);

				if (event.getType() == 0x2F) {
					if (sequencer.isRunning()) {
						sequencer.stop();
					}
					if (sequencer.isOpen()) {
						sequencer.close();
					}
					// MIDIPerformer.this.playLock.notifyAll();

					// for (MidiDevice device :
					// MIDIPerformer.this.openedMidiDeviceList) {
					// device.close();
					// }

					// bug in jdk1.3
					if (exit) {
						System.exit(0);
					}
				}
			}
		});

	}

}
