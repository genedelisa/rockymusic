package com.rockhoppertech.music.examples;

/*
 * #%L
 * Rocky Music Examples
 * %%
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
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

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDINoteBuilder;
import com.rockhoppertech.music.midi.js.MIDISender;

public class Send {

    public static void main(String[] args) {
        MIDISender sender = new MIDISender();

        MIDINote note = MIDINoteBuilder.create()
                .channel(0)
                .startBeat(1d)
                .pitch(Pitch.D5)
                .duration(5d)
                .program(MIDIGMPatch.BANJO.getProgram())
                .build();
        sender.play(note);
        sender.closeDevice();

        // Sequencer sequencer = MIDIUtils.getSequencer();
        // try {
        // sequencer.open();
        // sequencer.getReceiver().send(message, 0L);
        // // sequencer.getReceiver().send(offmessage, 1000);
        // sequencer.close();
        // } catch (MidiUnavailableException e) {
        // e.printStackTrace();
        // }
        // try {
        // Synthesizer synthesizer = MidiSystem.getSynthesizer();
        // synthesizer.open();
        // MidiChannel[] channels = synthesizer.getChannels();
        // channels[0].noteOn(note.getMidiNumber(), note.getVelocity());
        // } catch (MidiUnavailableException e) {
        // e.printStackTrace();
        // }

      //  play(note);

        // Sequence sequence = noteToSequence(note);
        // if (sequence != null) {
        // try {
        // play(sequence);
        // } catch (MidiUnavailableException e) {
        // e.printStackTrace();
        // }
        // }

    }

    static Sequence noteToSequence(MIDINote note) {
        ShortMessage on = new ShortMessage();
        ShortMessage off = new ShortMessage();
        try {
            on.setMessage(
                    ShortMessage.NOTE_ON,
                    note.getChannel(),
                    note.getMidiNumber(),
                    note.getVelocity());
            off.setMessage(
                    ShortMessage.NOTE_ON,
                    note.getChannel(),
                    note.getMidiNumber(),
                    0);
        } catch (InvalidMidiDataException e1) {
            e1.printStackTrace();
        }
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Sequence sequence = new Sequence(Sequence.PPQ, 256);
            Track track = sequence.createTrack();
            long tick = 0;
            track.add(new MidiEvent(on, tick));
            tick += (note.getDuration() * sequence.getResolution());
            track.add(new MidiEvent(off, tick));
            sequencer.close();
            return sequence;
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void play(MIDINote note) {
        Synthesizer synthesizer;
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            return;
        }

        Instrument[] instrument = synthesizer.getDefaultSoundbank()
                .getInstruments();
        synthesizer.loadInstrument(instrument[note.getProgram()]);
        MidiChannel[] channels = synthesizer.getChannels();
        MidiChannel channel = channels[note.getChannel()];
        channel.programChange(note.getProgram());
        channel.noteOn(note.getMidiNumber(), note.getVelocity());
        System.out.println("note on");
        // wait(note.getDuration());
        // channel.noteOff(note.getMidiNumber());
        synthesizer.close();
    }

    static void play(Sequence sequence) throws MidiUnavailableException {
        Sequencer sequencer = MidiSystem.getSequencer(false);
        Receiver receiver = MidiSystem.getReceiver();
        sequencer.open();
        sequencer.getTransmitter().setReceiver(receiver);
        try {
            sequencer.setSequence(sequence);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        sequencer.start();

    }

    public static void wait(double duration) {
        long now = System.currentTimeMillis();
        long end = (long) (now + duration);
        while (now < end) {
            try {
                Thread.sleep((int) (end - now));
            } catch (InterruptedException e) {
            }
            now = System.currentTimeMillis();
        }
    }

}
