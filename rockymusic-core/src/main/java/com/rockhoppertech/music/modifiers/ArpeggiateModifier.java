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

package com.rockhoppertech.music.modifiers;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * Class <code>ArpeggiateModifier</code> Modifies the note's start beat by the
 * amount and operation.
 * <p>
 * Beats are 1 based as defined in MIDINoteList toTrack(sequence, division) If
 * the pattern size is smaller than the notelist size, it will wrap.
 * 
 * 
 * {@code
 * MIDINoteList notelist = ...
 * double startBeat = 1d;
 * double duration = 4d;
 * double gap = 4.3;
 * notelist.map(new ArpeggiateModifier(startBeat, duration, gap));
 * // or
 * List<Double> series = new ArrayList<Double>();
 * series.add(.25d);
 * series.add(.5d);
 * series.add(1d);
 * notelist.map(new ArpeggiateModifier(startBeat, duration, series, NoteModifier.Operation.SET));
 * }
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 * @see com.rockhoppertech.music.modifiers.NoteModifier
 */
public class ArpeggiateModifier implements MIDINoteModifier {

    private static final Logger logger = LoggerFactory
            .getLogger(ArpeggiateModifier.class);

    private Operation operation = Operation.ADD;
    private List<Double> values;
    private int index;

    /** gap distance between attacks */
    // private double gap;
    /** total duration of the chord */
    private double duration;
    private double startBeat;
    private MIDINoteModifier successor;

    /**
     * 
     * @param startBeat
     * 
     * @param duration
     *            total duration of the chord
     * 
     * @param gap
     *            gap distance between attacks
     */
    public ArpeggiateModifier(double startBeat, double duration, double gap) {
        if (gap > duration) {
            throw new IllegalArgumentException("gap cannot be > duration");
        }
        this.startBeat = startBeat;
        this.duration = duration;
        values = new ArrayList<Double>();
        values.add(gap);
    }

    public ArpeggiateModifier(double startBeat, double duration, double gap,
            Operation op) {
        this.startBeat = startBeat;
        this.duration = duration;
        operation = op;
        values = new ArrayList<Double>();
        values.add(gap);
    }

    public ArpeggiateModifier(double startBeat, double duration,
            List<Double> gapValues, Operation op) {
        this.startBeat = startBeat;
        this.duration = duration;
        operation = op;
        values = gapValues;
    }

    double nextValue() {
        logger.debug("next value index: " + index);
        logger.debug("size: " + values.size());

        if (index + 1 > values.size()) {
            logger.debug("setting index to 0");
            index = 0;
        }
        double v = values.get(index++);
        logger.debug("returning " + v);
        return v;
    }

    /**
     * <code>modify</code> Each note starts after the gap. They all end at the
     * same time.
     * 
     * @param note
     *            a <code>Note</code> value
     */
    @Override
    public void modify(MIDINote note) {
        note.setStartBeat(startBeat);
        double d = duration - startBeat + 1d;
        if (d <= 0d) {
            throw new IllegalArgumentException("gaps cannot be > duration");
        }
        note.setDuration(d); // beats are 1 based and not zero based

        if (operation == Operation.ADD) {
            startBeat += nextValue();
        } else if (operation == Operation.SUBTRACT) {
            // does this make sense?
            startBeat -= nextValue();
        } else if (operation == Operation.DIVIDE) {
            startBeat /= nextValue();
        } else if (operation == Operation.MULTIPLY) {
            startBeat *= nextValue();
        } else if (operation == Operation.MOD) {
            startBeat %= nextValue();
        } else if (operation == Operation.SET) {
            startBeat = nextValue();
        }

    }

    // public void arpeggiate(double gap, double duration) {
    // double totalDuration = this.notelist.getDuration();
    // // double delay = totalDuration / (this.notelist.size() / 1);
    // double delay = this.notelist.size() / duration;
    // double sb = notelist.getStartBeat();
    // logger.debug("delay " + delay);
    // double dur = duration / notelist.size();
    //
    // for (MIDINote note : this.notelist) {
    // note.setStartBeat(sb);
    // note.setDuration(duration - sb);
    // sb += gap;
    // }
    // }

    /**
     * <code>getName</code>
     * 
     * @return a <code>String</code> value
     */
    @Override
    public String getName() {
        return "Arpeggiate";
    }

    /**
     * <code>getDescription</code>
     * 
     * @return a <code>String</code> value
     */
    @Override
    public String getDescription() {
        return "Arpeggiate";
    }

    /**
     * @return the operation
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * @param operation
     *            the operation to set
     */
    @Override
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void setValues(double[] array) {
        values = new ArrayList<Double>();
        for (double element : array) {
            values.add(element);
        }
    }

    @Override
    public void setSuccessor(MIDINoteModifier successor) {
        this.successor = successor;

    }

}
