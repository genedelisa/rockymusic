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
import com.rockhoppertech.music.Note;
import com.rockhoppertech.music.Timed;

/**
 * Class <code>StartModifier</code> Modifies the note's start beat by the
 * amount. The series provided will wrap.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 * @see NoteModifier
 * @see AbstractModifier
 *  @see TimedModifier
 */
public class StartBeatModifier extends AbstractModifier implements
        TimedModifier, NoteModifier {


    private static final Logger logger = LoggerFactory
			.getLogger(StartBeatModifier.class);
    
    private Operation operation = Operation.SET;
    private CircularArrayList<Double> values;
    private NoteModifier successor;

    public StartBeatModifier() {
        this.values = new CircularArrayList<Double>();
        this.values.add(1d);
    }

    public StartBeatModifier(final double n) {
        this.values = new CircularArrayList<Double>();
        this.values.add(n);
    }

    public StartBeatModifier(final Operation op, final double n) {
        this.operation = op;
        this.values = new CircularArrayList<Double>();
        this.values.add(n);
    }

    public StartBeatModifier(final Operation op, final double... array) {
        this.operation = op;
        this.values = new CircularArrayList<Double>();
        this.setValues(array);
    }

    public StartBeatModifier(final Operation op, final List<Double> values2) {
        this.operation = op;
        this.values = new CircularArrayList<Double>();
        this.values.addAll(values2);
    }

    private void doit(final Timed timed) {
        double d = 0d;
        final double value = this.values.next();
        switch (this.operation) {
        case ADD:
            d = timed.getStartBeat() + value;
            if (d < 1d) {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("value %f is < 1, setting to 1",
                                               d));
                }
                d = 1d;
            }
            timed.setStartBeat(d);

            break;

        case SUBTRACT:
            d = timed.getStartBeat() - value;
            if (d < 1d) {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("value %f is < 1, setting to 1",
                                               d));
                }
                d = 1d;
            }
            timed.setStartBeat(d);
            break;

        case DIVIDE:
            d = timed.getStartBeat() / value;
            if (d < 1d) {
                logger.debug("Rounding to 1: " + d);
                d = 1d;
            }
            timed.setStartBeat(d);
            break;

        case MULTIPLY:
            d = timed.getStartBeat() * value;
            if (d < 1d) {
                logger.debug("Rounding to 1: " + d);
                d = 1d;
            }
            timed.setStartBeat(d);
            break;

        case SET:
            d = value;
            if (d < 1d) {
                logger.debug("Rounding to 1: " + d);
                d = 1d;
            }
            timed.setStartBeat(d);
            break;

        case MOD:
            d = timed.getStartBeat() % value;
            if (d < 1d) {
                logger.debug("Rounding to 1: " + d);
                d = 1d;
            }
            timed.setStartBeat(d);
            break;

        case QUANTIZE:
            d = AbstractModifier.quantize(timed.getStartBeat(),
                                          value);
            if (d < 1d) {
                logger.debug("Rounding to 1: " + d);
                d = 1d;
            }
            timed.setStartBeat(d);
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
        return this.operation;
    }

    @Override
    public void modify(final Note note) {
        this.doit(note);
        if (this.successor != null) {
            this.successor.modify(note);
        }
    }

    @Override
    public void modify(final Timed timed) {
        this.doit(timed);
    }

    /**
     * @param operation
     *            the operation to set
     */
    public void setOperation(final Operation operation) {
        this.operation = operation;
    }

    public void setSuccessor(final NoteModifier successor) {
        this.successor = successor;
    }

    @Override
    public void setValues(final double[] array) {
        if (this.values == null) {
            this.values = new CircularArrayList<Double>();
        } else {
            this.values.clear();
        }

        for (final double element : array) {
            this.values.add(element);
        }
    }

    public void setValues(final List<Double> values) {
        if (this.values == null) {
            this.values = new CircularArrayList<Double>();
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
