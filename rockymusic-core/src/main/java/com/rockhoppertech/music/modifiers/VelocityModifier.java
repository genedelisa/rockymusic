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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.music.Note;
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * Class <code>VelocityModifier</code> Modifies the note's velocity by the
 * amount. The series provided will wrap.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 * @see NoteModifier
 * @see AbstractModifier
 * @see TimedModifier
 */
public class VelocityModifier extends AbstractModifier implements
        MIDINoteModifier {

    private static final Logger logger = LoggerFactory
            .getLogger(VelocityModifier.class);

    private Operation operation = Operation.SET;
    private CircularArrayList<Integer> values;
    private MIDINoteModifier successor;

    public VelocityModifier() {
        values = new CircularArrayList<Integer>();
        values.add(1);
    }

    public VelocityModifier(final int n) {
        values = new CircularArrayList<Integer>();
        values.add(n);
    }

    public VelocityModifier(final Operation op, final int n) {
        operation = op;
        values = new CircularArrayList<Integer>();
        values.add(n);
    }

    public VelocityModifier(final Operation op, final double... array) {
        operation = op;
        values = new CircularArrayList<Integer>();
        this.setValues(array);
    }

    public VelocityModifier(final double... array) {
        this(Operation.SET, array);
    }

    public VelocityModifier(final Operation op, final List<Integer> velocityList) {
        operation = op;
        values = new CircularArrayList<Integer>();
        values.addAll(velocityList);
    }

    public VelocityModifier(List<Integer> velocityList) {
        this(Operation.SET, velocityList);
    }

    private void doit(final MIDINote midiNote) {
        int d = 0;
        final int value = values.next();
        switch (operation) {
        case ADD:
            d = midiNote.getVelocity() + value;
            if (d < 1d) {
                logger.debug("value {} is < 1, setting to 1",
                        d);
                d = 1;
            }
            midiNote.setVelocity(d);

            break;

        case SUBTRACT:
            d = midiNote.getVelocity() - value;
            if (d < 1d) {
                logger.debug("value {} is < 1, setting to 1",
                        d);
                d = 1;
            }
            midiNote.setVelocity(d);
            break;

        case DIVIDE:
            d = midiNote.getVelocity() / value;
            if (d < 1d) {
                logger.debug("Rounding to 1: ", d);
                d = 1;
            }
            midiNote.setVelocity(d);
            break;

        case MULTIPLY:
            d = midiNote.getVelocity() * value;
            if (d < 1d) {
                logger.debug("Rounding to 1: ", d);
                d = 1;
            }
            midiNote.setVelocity(d);
            break;

        case SET:
            d = value;
            if (d < 1d) {
                logger.debug("Rounding to 1: ", d);
                d = 1;
            }
            midiNote.setVelocity(d);
            break;

        case MOD:
            d = midiNote.getVelocity() % value;
            if (d < 1d) {
                logger.debug("Rounding to 1: ", d);
                d = 1;
            }
            midiNote.setVelocity(d);
            break;

        case QUANTIZE:
            d = (int) AbstractModifier.quantize(midiNote.getVelocity(),
                    value);
            if (d < 1) {
                logger.debug("Rounding to 1: ", d);
                d = 1;
            }
            midiNote.setVelocity(d);
            break;
        }

    }

    /**
     * <code>getDescription</code>
     * 
     * @return a <code>String</code> value
     */
    @Override
    public String getDescription() {
        return "Start Beat modifier";
    }

    /**
     * <code>getName</code>
     * 
     * @return a <code>String</code> value
     */
    @Override
    public String getName() {
        return "Start Beat";
    }

    /**
     * @return the operation
     */
    public Operation getOperation() {
        return operation;
    }

    @Override
    public void modify(final MIDINote note) {
        doit(note);
        if (successor != null) {
            successor.modify(note);
        }
    }

    /**
     * @param operation
     *            the operation to set
     */
    @Override
    public void setOperation(final Operation operation) {
        this.operation = operation;
    }

    @Override
    public void setSuccessor(final MIDINoteModifier successor) {
        this.successor = successor;
    }

    @Override
    public void setValues(final double[] array) {
        if (values == null) {
            values = new CircularArrayList<Integer>();
        } else {
            values.clear();
        }

        for (final double element : array) {
            values.add((int) element);
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
