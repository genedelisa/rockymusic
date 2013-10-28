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

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.VoiceStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shoves data directly to a channel on a device. No real timing. Useful for GUI
 * things like keyboards or sliders.
 * <pre>
MIDINote note = MIDINoteBuilder.create()
                .channel(0)
                .startBeat(1d)
                .pitch(Pitch.D5)
                .duration(5d)
                .program(MIDIGMPatch.BANJO.getProgram())
                .build();

MIDISender sender = new MIDISender();
sender.play(note);
sender.closeDevice();
 </pre>                                         
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDISender {
    /**
     * 
     */
    private final static Logger logger = LoggerFactory
            .getLogger(MIDISender.class);
    private int num;
    private Synthesizer synth;
    private int sendChannel = 0;
    private MidiChannel channel;
    private int sendVelocity = 100;
    private MidiDevice midiDevice;
    // private Receiver receiver;
    List<Receiver> receivers = new ArrayList<Receiver>();
    private long sleepValue = 1000;
    private int octave = 5;

    /**
     * Uses the JavaSound Synthesizer.
     */
    public MIDISender() {
        try {
            this.midiDevice = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }
    
    /**
     * Plays on the specified MIDI device.
     * @param midiDevice the device to play on.
     */
    public MIDISender(MidiDevice midiDevice) {
        this.midiDevice = midiDevice;
    }

   

    public void play(MIDINote note) {
        this.openDevice();
        this.sleepValue = (long) (note.getDuration() * 1000L);
        this.sendMessage(note.getMidiNumber(), note.getVelocity(), note.getProgram());
        // this.closeDevice();
    }

    public void sendMessage(final int num) {
        this.sendMessage(num, 0, sendVelocity);
    }

    public void sendMessage(final int midiPitchNumber, final int velocity, final int program) {
        // Timer timer = new Timer();
        // timer.schedule(new TimerTask() {
        // @Override
        // public void run() {
        // channel.noteOn(num, 0);
        // }}, sleepValue);

        if (!midiDevice.isOpen()) {
            this.openDevice();
        }
        setProgram(program);

        logger.debug("sending midiPitchNumber {}", midiPitchNumber);

        // this.channel.programChange(program);
        if (this.channel != null) {
            logger.debug("using channel");
            this.channel.noteOn(midiPitchNumber, velocity);
            try {
                Thread.sleep(this.sleepValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.channel.noteOn(midiPitchNumber, 0);
        } else {
            logger.debug("not using channel");
        }

        if (this.receivers != null && this.receivers.isEmpty() == false) {
            logger.debug("using port");
            ShortMessage sm = new ShortMessage();
            // try {
            // sm.setMessage(ShortMessage.PROGRAM_CHANGE, sendChannel, 0,
            // program);
            // } catch (InvalidMidiDataException e1) {
            // e1.printStackTrace();
            // }
            // for (Receiver r : receivers) {
            // logger.debug("sending to receiver " + sm);
            // r.send(sm, this.midiDevice.getMicrosecondPosition());
            // }

            try {
                sm.setMessage(ShortMessage.NOTE_ON, sendChannel, midiPitchNumber,
                        sendVelocity);
            } catch (InvalidMidiDataException e1) {
                e1.printStackTrace();
            }
            for (Receiver r : receivers) {
                logger.debug("sending to receiver " + sm);
                r.send(sm, this.midiDevice.getMicrosecondPosition());
            }
            // this.receiver.send(sm, this.midiDevice.getMicrosecondPosition());
            logger.debug(
                    "ms posiiton {}",
                    this.midiDevice.getMicrosecondPosition());

            try {
                Thread.sleep(this.sleepValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // turn it off
            try {
                sm.setMessage(ShortMessage.NOTE_ON, sendChannel, midiPitchNumber, 0);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
            for (Receiver r : receivers) {
                r.send(sm, this.synth.getMicrosecondPosition());
            }
            // this.receiver.send(sm, this.synth.getMicrosecondPosition());
        } else {

            logger.debug("Not using port");

        }
    }

    public Instrument[] openDevice() {
        Instrument[] insts = null;
        if (this.midiDevice == null) {
            logger.debug("midi device is null! Using default synthesizer.");
            try {
                this.midiDevice = MidiSystem.getSynthesizer();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
        if (midiDevice.isOpen()) {
            if (this.midiDevice instanceof Synthesizer) {
                this.synth = (Synthesizer) this.midiDevice;
                insts = synth.getAvailableInstruments();
                return insts;
            }
            return null;
        }

        try {
            this.midiDevice.open();

            if (this.midiDevice instanceof Synthesizer) {
                this.synth = (Synthesizer) this.midiDevice;
                channel = this.synth.getChannels()[this.sendChannel];

                insts = synth.getAvailableInstruments();
            } else {
                receivers.add(this.midiDevice.getReceiver());
                // this.receiver = this.midiDevice.getReceiver();
            }

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);

        }
        return insts;
    }

    public void closeDevice() {
        logger.debug("Closing device {}",
                this.midiDevice.getDeviceInfo().getName());
        if (this.midiDevice.isOpen() == false) {
            return;
        }
        if (this.midiDevice != null)
            this.midiDevice.close();
        if (this.receivers != null) {
            // this.receiver.close();
        }
        this.midiDevice = null;
        // this.receiver = null;
        this.channel = null;
    }

    /**
     * @param inst
     */
    public Instrument setInstrument(Instrument inst) {
        if (inst == null) {
            logger.debug("inst is null");
            return null;
        }
        if (this.synth == null)
            this.openDevice();

        this.synth.loadInstrument(inst);
        channel = synth.getChannels()[sendChannel];
        channel.programChange(inst.getPatch().getBank(), inst.getPatch()
                .getProgram());

        int bank = inst.getPatch().getBank();
        logger.debug("bank: " + bank);
        int setbank = (channel.getController(0) * 128)
                + channel.getController(32);
        logger.debug("setbank: " + setbank);

        VoiceStatus[] vs = this.synth.getVoiceStatus();
        for (VoiceStatus v : vs) {
            logger.debug("vs bank" + v.bank);
            logger.debug("vs channel" + v.channel);
            logger.debug("vs program" + v.program);
        }
        return inst;

    }

    /**
     * @param num
     */
    public void setProgram(int num) {
        logger.debug("sending pchange {}", num);
        /*
         * if(this.midiDevice == null) this.openDevice(); if(this.receiver ==
         * null) this.openDevice();
         */

        if (this.channel != null) {
            this.channel.programChange(num);
        }
        if (this.receivers != null) {
            ShortMessage sm = new ShortMessage();
            try {
                sm.setMessage(ShortMessage.PROGRAM_CHANGE, sendChannel, num, 0);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
            // this.receiver.send(sm, this.midiDevice.getMicrosecondPosition());
            for (Receiver r : receivers) {
                r.send(sm, this.midiDevice.getMicrosecondPosition());
            }

        }

    }

    /**
     * @param midiDevice
     *            the midiDevice to set
     */
    public void setMidiDevice(MidiDevice midiDevice) {
        this.midiDevice = midiDevice;
    }

    /**
     * @return the num
     */
    public int getNum() {
        return this.num;
    }

    /**
     * @param num
     *            the num to set
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * @return the octave
     */
    public int getOctave() {
        return this.octave;
    }

    /**
     * @param octave
     *            the octave to set
     */
    public void setOctave(int octave) {
        this.octave = octave;
    }

    /**
     * @return the sendChannel
     */
    public int getSendChannel() {
        return this.sendChannel;
    }

    /**
     * @param sendChannel
     *            the sendChannel to set
     */
    public void setSendChannel(int sendChannel) {
        this.sendChannel = sendChannel;
    }

    /**
     * @return the sendVelocity
     */
    public int getSendVelocity() {
        return this.sendVelocity;
    }

    /**
     * @param sendVelocity
     *            the sendVelocity to set
     */
    public void setSendVelocity(int sendVelocity) {
        this.sendVelocity = sendVelocity;
    }

    /**
     * @return the sleepValue
     */
    public long getSleepValue() {
        return this.sleepValue;
    }

    /**
     * @param sleepValue
     *            the sleepValue to set
     */
    public void setSleepValue(long sleepValue) {
        this.sleepValue = sleepValue;
    }

    /**
     * doesn't work
     * 
     * @param myReceiver
     */
    // TODO fix this
    public void addReceiver(Receiver myReceiver) {
        receivers.add(myReceiver);
        // try {
        // midiDevice.getTransmitter().setReceiver(myReceiver);
        // } catch (MidiUnavailableException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // midiDevice.getReceivers().add(myReceiver);

    }

}

/*
 * Controller 0 - Bank Select MSB is used in conjunction often with the Bank
 * Select LSB to select the bank for the next program change message. This means
 * the patch does not change until the program change is received (bank messages
 * alone are not enough). The combination of Bank MSB and LSB messages gives a
 * possible 16384 banks, each of 128 different sounds, giving over 2 million
 * possible sounds! In practice, most manufacturers only use a few different
 * banks, and you can usually ignore the LSB message.
 */
/*
 * channel = synth.getChannels()[sendChannel];
 * channel.programChange(inst.getPatch().getBank(), inst
 * .getPatch().getProgram());
 * 
 * int bank = inst.getPatch().getBank(); logger.debug("bank: " + bank); int
 * setbank = (channel.getController(0) * 128) + channel.getController(32);
 * logger.debug("setbank: " + setbank);
 */

/*
 * ShortMessage sm = new ShortMessage(); try {
 * 
 * 
 * //the bank number to switch to (0 to 16383) int bank =
 * inst.getPatch().getBank(); logger.debug("bank: " + bank); int msb = bank &
 * 0xF0; int lsb = bank & 0x0F;
 * 
 * msb = bank >>> 8; lsb = bank & 0xFF;
 * 
 * msb = ((int) bank & 0x000000FF) / 128; lsb = ((int) bank & 0x000000FF) % 128;
 * logger.debug("msb: " + msb); logger.debug("lsb: " + lsb);
 * 
 * // msb sm.setMessage(ShortMessage.CONTROL_CHANGE, sendChannel, 0, msb);
 * receiver.send(sm, synth.getMicrosecondPosition());
 * 
 * // lsb sm.setMessage(ShortMessage.CONTROL_CHANGE, sendChannel, 0x20, lsb);
 * receiver.send(sm, synth.getMicrosecondPosition());
 * 
 * sm.setMessage(ShortMessage.PROGRAM_CHANGE, sendChannel,
 * inst.getPatch().getProgram(), 0); receiver.send(sm,
 * synth.getMicrosecondPosition());
 * 
 * } catch (InvalidMidiDataException ex) { ex.printStackTrace(); }
 */
