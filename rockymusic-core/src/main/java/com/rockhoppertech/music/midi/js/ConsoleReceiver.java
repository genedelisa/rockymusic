package com.rockhoppertech.music.midi.js;

/*
 * #%L
 * Rocky Music Core
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


import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 * A JavaSound receiver that prints to stdout.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ConsoleReceiver implements Receiver {
    private boolean open = false;
    // private int mode = OMNI_ON_POLY;
    private int mode = 1;
    private MidiDevice.Info info;

    double tempo = 120d;
    double division = 480d;
    double factor = (60000000d / tempo) / division;

    /**
     * <p>
     * We receive a message from the transmitter here. That's receive and not
     * send. What a confusing name!!!!!! The timeStamp is in ms not ticks. Go
     * figure. A -1 value means send immediately.
     * </p>
     * 
     * @see javax.sound.midi.Receiver#send(javax.sound.midi.MidiMessage, long)
     */
    @Override
    public void send(MidiMessage mm, long timeStamp) {

        System.out.println("ConsoleReceiver status 0x"
                + Integer.toHexString(mm.getStatus())
                + " timeStamp " + timeStamp);

        byte[] ba = mm.getMessage();
        // skip active sensing
        if (ba[0] == ShortMessage.ACTIVE_SENSING)
            return;
        StringBuilder sb = new StringBuilder();
        sb.append("\ttimestamp: ");
        sb.append(timeStamp);
        if (timeStamp == -1) {
            sb.append(" (Send immediately) ");
        }
        // print out the tick
        long tick = (long) (timeStamp / factor);
        sb.append(" tick:").append((timeStamp == -1 ? 0 : tick));
        sb.append(" msg data:");
        for (int i = 0; i < mm.getLength(); i++)
            sb.append(" 0x").append(Integer.toHexString(ba[i] & 0xFF))
                    .append(' ');

        System.out.println(sb.toString());

        if (mm instanceof ShortMessage) {
            System.out.println("\t" + MIDIUtils.printFull((ShortMessage) mm));
        }

        System.out.println();
    }

    @Override
    public void close() {
        System.out.println("ConsoleReceiver closed");
        this.open = false;
    }

    public MidiDevice.Info getDeviceInfo() {
        if (this.info == null) {
            this.info = new Info("ConsoleReceiver", "Rockhopper Technologies",
                    "Displays messages to stdout", "1.0");
        }
        return this.info;
    }

    public int getMode() {
        return this.mode;
    }

    /*
     * public int[] getModes() { return new int[]{OMNI_ON_POLY, OMNI_ON_MONO}; }
     */

    public boolean isOpen() {
        return this.open;
    }

    public void open() {
        System.out.println("ConsoleReceiver opened");
        this.open = true;
    }

    public void setMode(int mode) {
        System.out.println("ConsoleReceiver setmode " + mode);
        this.mode = mode;
    }

    /**
     * The constructor in MidiDevice.Info is protected.
     * 
     * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
     * 
     */
    class Info extends MidiDevice.Info {
        protected Info(String name, String vendor, String description,
                String version) {
            super(name, vendor, description, version);
        }
    }

}
