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
import javax.sound.midi.MidiMessage;
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
 * You can play a single Sequence,Track, or Score with play. Or, you can add
 * them, then call start to play them in order.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDIPerformer implements Runnable {

    // TODO is was 3 different classes. Clean it up.

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

    private boolean continuousLoop = false;
    //private boolean playing;
    private boolean midiEnd;
    private int loopCount = 1;
    private List<MidiDevice> openedMidiDeviceList;
    //private Object playLock = new Object();
    private Receiver receiver;
    private Sequence sequence;
    private Sequencer sequencer;
    private String sequencerName;
    private float tempo = 120f;
    private float tempoFactor = 1f;
    private int resolution = 480;    
    private List<Sequence> sequences;
    private ListIterator<Sequence> listIterator;    
    private long silenceBetween = 0;
    private Thread thread;
    private int maxIterations = 2;

    public MIDIPerformer() {
        openedMidiDeviceList = new ArrayList<MidiDevice>();
        sequences = new ArrayList<Sequence>();
        // moved this to play()
        // this.setupSequencer();
    }

    public MIDIPerformer atTempo(float t) {
        this.tempo = t;
        return this;
    }

    public MIDIPerformer atTempoFactor(float t) {
        this.tempoFactor = t;
        return this;
    }

    public MIDIPerformer receiver(Receiver receiver) {
        this.receiver = receiver;
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
        this.sequence = ScoreFactory.scoreToSequence(score);
        this.resolution = score.getResolution();
        play();
    }

    public MIDIPerformer score(Score score) {
        this.sequence = ScoreFactory.scoreToSequence(score);
        this.resolution = score.getResolution();
        return this;
    }
    
    public MIDIPerformer sequence(Sequence s) {
        this.sequence = s;
        this.resolution = s.getResolution();
        return this;
    }

    public void play(MIDITrack track) {
        this.sequence = MIDITrackFactory
                .trackToSequence(track, this.resolution);
        this.play();
    }

    public MIDIPerformer track(MIDITrack track) {
        this.sequence = MIDITrackFactory
                .trackToSequence(track, this.resolution);
        return this;
    }

    /**
     * Play the current Sequence. Does nothing if you haven't set the Sequence.
     */
    public void play() {
        if (this.sequence != null) {
            this.playSequence();
        }
    }

    /**
     * To play multiple Sequences in order. Add them, then start the Performer.
     * 
     * @param sequence the JavaSound Sequence
     * @return this to cascade calls
     */
    public MIDIPerformer add(Sequence sequence) {
        this.sequences.add(sequence);
        this.maxIterations = this.sequences.size();
        this.resolution = sequence.getResolution();
        return this;
    }

    public MIDIPerformer add(MIDITrack track) {
        this.add(MIDITrackFactory.trackToSequence(track, resolution));
        return this;
    }

    // public void play(Sequence... sequences) {
    // this.sequences.clear();
    // for (Sequence s : sequences) {
    // this.sequences.add(s);
    // }
    // }

    /**
     * Plays this.sequence.
     */
    private void playSequence() {
        this.midiEnd = false;

        if (this.sequencer == null) {
            setupSequencer();
        }
        openSequencer();

        try {
            logger.debug("setting tempo to {} BPM", this.tempo);
            sequencer.setTempoFactor(this.tempoFactor);
            sequencer.setTempoInBPM(this.tempo);
            sequencer.setSequence(this.sequence);
        } catch (InvalidMidiDataException e) {
            logger.error(e.getMessage(), e);
            return;
        }
        // do this from a GUI where you can send a stop message
        if (continuousLoop) {
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        }

        logger.debug("Starting sequencer");
        this.sequencer.start();
        while (!this.midiEnd && this.thread !=
                null) {
            // logger.debug("Haven't reached end yet");
            try {
                Thread.sleep(this.silenceBetween);
            } catch (Exception e) {
                break;
            }
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

    // public void playlock(Sequence sequence) {
    // synchronized (playLock) {
    // if (sequencer != null && sequencer.isRunning()) {
    // logger.debug("already RUNNING");
    // while (true) {
    // try {
    // playLock.wait();
    // } catch (InterruptedException e) {
    // logger.error(e.getLocalizedMessage(), e);
    // }
    // }
    // }
    // this.play();
    // }
    // }

    /**
     * Sets this.sequencer. If sequencerName is set, it will try that first.
     * Otherwise it returns the system sequencer. And end of track listener is
     * set. If this.receiver is not null, it is set.
     * 
     * @see http://java.sun.com/docs/books/tutorial/sound/MIDI-messages.html
     */
    private void setupSequencer() {
        if (this.sequencerName != null) {
            MidiDevice.Info seqInfo = getMidiDeviceInfo(this.sequencerName,
                    true);
            if (seqInfo == null) {
                logger.debug("device Info is null for {}", this.sequencerName);
                return;
            }
            try {
                sequencer = (Sequencer) MidiSystem.getMidiDevice(seqInfo);
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        } else {
            try {
                logger.debug("Getting system sequencer");
                this.sequencer = MidiSystem.getSequencer();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
        sequencer.setTempoFactor(tempoFactor);
        sequencer.setTempoInBPM(tempo);

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
                   // playing = false;
                    midiEnd = true;
                }
            }
        });

        this.setReceiver(this.receiver);
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
        this.play();
    }

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
                logger.debug("iteration {}", i);
                this.sequence = this.next();
                this.playSequence();
            }
        }
    }

    private Sequence next() {
        logger.debug("calling next");
        if (this.listIterator.hasNext() == false) {
            logger.debug(
                    "new interator. list has {} sequences",
                    this.sequences.size());
            this.listIterator = this.sequences.listIterator(0);
        }
        Sequence sequence = this.listIterator.next();
        return sequence;
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

    /**
     * @return the maxIterations
     */
    public int getMaxIterations() {
        return maxIterations;
    }

    /**
     * @param maxIterations
     *            the maxIterations to set
     */
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void clear() {
        this.sequences.clear();
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
        if (this.receiver != null && this.sequencer != null) {
            try {
                sequencer.getTransmitter().setReceiver(this.receiver);
            } catch (MidiUnavailableException me) {
                logger.error(me.getMessage(), me);
            }
        }
    }

    /**
     * @return the tempo
     */
    public float getTempo() {
        return tempo;
    }

    /**
     * @param bpm
     *            the tempo to set in beats per minute
     */
    public void setTempo(float bpm) {
        this.tempo = bpm;
        if (this.sequencer != null) {
            logger.debug("setting tempo  to {} BPM", this.tempo);
            sequencer.setTempoInBPM(bpm);
        } else {
            logger.debug("cannot set tempo on null sequencer");
        }
    }

    /**
     * @return the tempoFactor
     */
    public float getTempoFactor() {
        return tempoFactor;
    }

    /**
     * @param tempoFactor
     *            the tempoFactor to set
     */
    public void setTempoFactor(float tempoFactor) {
        this.tempoFactor = tempoFactor;
        if (this.sequencer != null) {
            logger.debug("setting tempo factor to {}", this.tempoFactor);
            sequencer.setTempoFactor(tempoFactor);
        }
    }

    public static void main(String[] args) {
        Scale scale = ScaleFactory.getScaleByName("Major");
        MIDITrack track = ScaleFactory.createMIDITrack(scale,
                C5);
        MIDIPerformer mp = new MIDIPerformer();

        // you can add a Sequence
        Sequence sequence = MIDITrackFactory.trackToSequence(track, 480);
        mp.add(sequence);

        // or a track
        scale = ScaleFactory.getScaleByName("Pelog");
        track = ScaleFactory.createMIDITrack(scale,
                C5);
        mp.add(track);

        mp.setTempoFactor(3f);

        // loop the first sequence
        mp.setContinuousLoop(true);

        // play both, one after the other if continuous is false
        mp.start();

        // since we set loop continuously
        try {
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        mp.stop();

    }

}
