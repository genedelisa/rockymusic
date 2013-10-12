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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * Class <code>ChannelModifier</code>
 *
 *
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 * @see NoteModifier
 */
public class ChannelModifier implements MIDINoteModifier {

    private static final Logger logger = LoggerFactory
			.getLogger(ChannelModifier.class);


    private Operation operation = Operation.SET;
    private CircularArrayList<Integer> values;

    public ChannelModifier() {
        this.values = new CircularArrayList<Integer>();
        this.values.add(Pitch.C5);
    }

    public ChannelModifier(final int n) {
        this.values = new CircularArrayList<Integer>();
        this.values.add(n);
    }

    public ChannelModifier(final Operation op, final int n) {
        this.operation = op;
        this.values = new CircularArrayList<Integer>();
        this.values.add(n);
    }

    public ChannelModifier(final Operation op, final double... array) {
        this.operation = op;
        this.values = new CircularArrayList<Integer>();
        this.setValues(array);
    }

    public ChannelModifier(final Operation op, final List<Integer> values2) {
        this.operation = op;
        this.values = new CircularArrayList<Integer>();
        this.values.addAll(values2);
    }

    /**
     * <code>getDescription</code>
     * 
     * @return a <code>String</code> value
     */
    @Override
    public String getDescription() {
        return "Channel modifier changes the MIDI channel";
    }

    /**
     * <code>getName</code>
     * 
     * @return a <code>String</code> value
     */
    @Override
    public String getName() {
        return "Channel Modifier";
    }

    /**
     * @return the operation
     */
    public Operation getOperation() {
        return this.operation;
    }

    @Override
    public void modify(final MIDINote note) {
        logger.debug("before: " + note);
        final double value = this.values.next();
        int midiChannel = 0;
        int max = 16;

        switch (this.operation) {
        case ADD:
            midiChannel = (int) (note.getChannel() + value);
            if (midiChannel < 0 || midiChannel > max) {
                midiChannel = note.getChannel();
            }
            note.setChannel(midiChannel);
            break;
        case SUBTRACT:
            midiChannel = (int) (note.getChannel() - value);
            if (midiChannel < 0 || midiChannel > max) {
                midiChannel = note.getChannel();
            }
            note.setChannel(midiChannel);
            break;
        case DIVIDE:
            midiChannel = (int) (note.getChannel() / value);
            if (midiChannel < 0 || midiChannel > max) {
                midiChannel = note.getChannel();
            }
            note.setChannel(midiChannel);
            break;
        case MULTIPLY:
            midiChannel = (int) (note.getChannel() * value);
            if (midiChannel < 0 || midiChannel > max) {
                midiChannel = note.getChannel();
            }
            note.setChannel(midiChannel);
            break;
        case MOD:
            midiChannel = (int) (note.getChannel() % value);
            if (midiChannel < 0 || midiChannel > max) {
                midiChannel = note.getChannel();
            }
            note.setChannel(midiChannel);
            break;
        case SET:
            midiChannel = (int) value;
            note.setChannel(midiChannel);
            break;
        case QUANTIZE:
            double d = AbstractModifier.quantize(note.getChannel(),
                                                 value);
            if (d < 0 || d > max) {
                d = note.getChannel();
            }
            note.setChannel(midiChannel);
        }
        logger.debug("after: " + note);
    }

    /**
     * @param operation
     *            the operation to set
     */
    public void setOperation(final Operation operation) {
        this.operation = operation;
    }

    @Override
    public void setValues(final double[] array) {
        if (this.values == null) {
            this.values = new CircularArrayList<Integer>();
        } else {
            this.values.clear();
        }

        for (final Double element : array) {
            this.values.add(element.intValue());
        }
    }

    public void setValues(final List<Integer> values) {
        if (this.values == null) {
            this.values = new CircularArrayList<Integer>();
        } else {
            this.values.clear();
        }
        this.values.addAll(values);
    }
}
/*
 * History:
 *
 * $Log$
 *
 * This version: $Revision$
 * Last modified: $Date$
 * Last modified by: $Author$
 */
