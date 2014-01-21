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

import static com.rockhoppertech.music.Pitch.C5;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * You can play a single Sequence,Track, or Score with play.
 * Or, you can add them, then call start to play them in order.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDIPerformer implements Runnable {

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
    private boolean playing;
    private boolean midiEnd;
    private int loopCount = 1;
    private List<MidiDevice> openedMidiDeviceList;
    private Object playLock = new Object();
    private Receiver receiver;
    private Sequence sequence;
    private Sequencer sequencer;
    private String sequencerName;
    private float tempo = 120f;
    private float tempoFactor = 1f;
    private List<Sequence> sequences;
    long silenceBetween = 0;

    public MIDIPerformer() {
        openedMidiDeviceList = new ArrayList<MidiDevice>();
        sequences = new ArrayList<Sequence>();
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

    /**
     * To play multiple Sequences in order.
     * Add them, then start the Performer.
     * 
     * @param sequence
     */
    public void add(Sequence sequence) {
        this.sequences.add(sequence);
        this.maxIterations = this.sequences.size();
    }
    
    public void add(MIDITrack track) {
        this.add(MIDITrackFactory.trackToSequence(track, 480));
    }

//    public void play(Sequence... sequences) {
//        this.sequences.clear();
//        for (Sequence s : sequences) {
//            this.sequences.add(s);
//        }
//    }

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
        openSequencer();

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

    private void openSequencer() {
        if (sequencer != null) {
            try {
                sequencer.open();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
                return;
            }
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
                logger.debug("MIDIPerformer meta: "
                        + Integer.toHexString(event.getType()));
                MIDIUtils.print(event);

                if (event.getType() == 0x2F
                        && loopCount != Sequencer.LOOP_CONTINUOUSLY) {
                    logger.debug("end of MIDI. stopping sequencer");
                    stopPlaying();
                    playing = false;
                    midiEnd = true;
                }
            }
        });

    }

    public void stopPlaying() {
        if (sequencer == null) {
            return;
        }
        if (sequencer.isRunning()) {
            sequencer.stop();
        }
        /*
         * List<Receiver> receivers = this.sequencer.getReceivers();
         * for(Receiver r : receivers) { System.out.println(r); r.close(); }
         */
        if (sequencer.isOpen()) {
            sequencer.close();
        }
        if (sequencer.isRecording()) {
            sequencer.stopRecording();
        }

    }

    /**
     * @return the continuousLoop
     */
    public boolean isContinuousLoop() {
        return continuousLoop;
    }

    /**
     * @param continuousLoop
     *            the continuousLoop to set
     */
    public void setContinuousLoop(boolean continuousLoop) {
        this.continuousLoop = continuousLoop;
    }

    public void playFirstInList() {
        this.sequence = this.sequences.get(0);
        this.play(this.sequence);
    }

    private Thread thread;
    private ListIterator<Sequence> listIterator;
    private int maxIterations = 2;

    public void start() {
        this.listIterator = this.sequences.listIterator(0);
        this.thread = new Thread(this);
        this.thread.setName("Juke");
        this.thread.start();
    }

    /**
     * <p>
     * Stops the jukebox.
     * </p>
     * 
     */
    public void stop() {
        if (this.thread != null) {
            this.thread.interrupt();
        }
        this.thread = null;
    }

    /*
     * <p>The Juke box run method. </p>
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        if (this.maxIterations == Sequencer.LOOP_CONTINUOUSLY) {
            while (true) {
                this.next();
                this.playSequence();
            }
        } else {
            for (int i = 0; i < this.maxIterations; i++) {
                this.sequence = this.next();
                this.openSequencer();
                this.playSequence();
            }
        }
    }

    private Sequence next() {
        if (this.listIterator.hasNext() == false) {
            this.listIterator = this.sequences.listIterator(0);
        }
        Sequence sequence = (Sequence) this.listIterator.next();
        return sequence;
    }

    private void playSequence() {
        this.midiEnd = false;
        if (this.thread != null) {
            if (this.sequencer == null) {
                setupSequencer();
                openSequencer();
            }
            try {
                this.sequencer.setSequence(sequence);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
            this.sequencer.start();
            while (!this.midiEnd && this.thread !=
                    null) {
                try {
                    
                    Thread.sleep(silenceBetween);
                } catch (Exception e) {
                    break;
                }
            }
            if (this.sequencer.isOpen()) {
                this.sequencer.stop();
                this.sequencer.close();
            }
        }
    }

    public static void main(String[] args) {
        Scale scale = ScaleFactory.getScaleByName("Major");
        MIDITrack track = ScaleFactory.createMIDITrack(scale,
                C5);
        MIDIPerformer mp = new MIDIPerformer();
        Sequence sequence = MIDITrackFactory.trackToSequence(track, 480);
        mp.add(sequence);

        scale = ScaleFactory.getScaleByName("Pelog");
        track = ScaleFactory.createMIDITrack(scale,
                C5);
        mp.add(track);

        mp.start();
    }

    // get this to work?
    void exec() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            Runnable worker = new MIDIPerformer();
            executor.execute(worker);
        }
        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();

        // Wait until all threads are finished
        try {
            executor.awaitTermination(0, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
