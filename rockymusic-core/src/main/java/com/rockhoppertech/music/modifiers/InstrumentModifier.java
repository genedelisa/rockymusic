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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * Class <code>InstrumentModifier</code>
 *
 *
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 * @see NoteModifier
 */
public class InstrumentModifier implements MIDINoteModifier {

    private static final Logger logger = LoggerFactory
			.getLogger(InstrumentModifier.class);
    
    private List<Integer> programs;
    private List<Integer> banks;
    private Operation operation = Operation.SET;
    private int index;
    private int bindex;

    public InstrumentModifier() {
        this.banks = new ArrayList<Integer>();
        this.banks.add(0);
        this.programs = new ArrayList<Integer>();
        this.programs.add(0);
    }
    public InstrumentModifier(List<Integer> programs) {
        this(programs, Operation.SET);
    }

    public InstrumentModifier(List<Integer> programs, Operation op) {
        this.programs = programs;
        this.operation = op;
    }

    public InstrumentModifier(int program) {
        this(program, Operation.SET);
    }

    public InstrumentModifier(int program, Operation op) {
        this.programs = new ArrayList<Integer>();
        this.banks = new ArrayList<Integer>();
        this.banks.add(0);
        this.programs.add(program);
        this.operation = op;
    }

    public InstrumentModifier(int bank, int program) {
        this.banks = new ArrayList<Integer>();
        this.banks.add(bank);
        this.programs = new ArrayList<Integer>();
        this.programs.add(program);
    }
    public String getName() {
        return "Instrument";
    }

    public String getDescription() {
        return "Instrument modifier. Only the SET operation makes sense";
    }

    @Override
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void setValues(double[] progs) {
        this.programs = new ArrayList<Integer>();
        for (int i = 0; i < progs.length; i++) {
            this.programs.add((int) progs[i]);
        }
    }

    public void setBanks(double[] banks) {
        this.banks = new ArrayList<Integer>();
        for (int i = 0; i < banks.length; i++) {
            this.banks.add((int) banks[i]);
        }
    }

    private int nextValue() {
        logger.debug("next value index: " + this.index);
        logger.debug("size: " + this.programs.size());

        if (this.index + 1 > this.programs.size()) {
            logger.debug("setting index to 0");
            this.index = 0;
        }
        int v = this.programs.get(index++);
        logger.debug("returning " + v);
        return v;
    }

    private int nextBank() {
        if (this.bindex + 1 > this.banks.size()) {
            this.bindex = 0;
        }
        int v = this.banks.get(bindex++);
        return v;
    }

    /**
     * 
     * @param note
     *            a <code>MIDINote</code> value
     */
    @Override
    public void modify(MIDINote note) {
        logger.debug("before: " + note);
        int program = this.nextValue();
        int bank = this.nextBank();

        switch (this.operation) {
        case ADD:
        case SUBTRACT:
        case DIVIDE:
        case MULTIPLY:
        case MOD:
        case SET:
            note.setBank(bank);
            note.setProgram(program);
            break;
		case QUANTIZE:
			break;
		default:
			break;
        }
        logger.debug("after: " + note);
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
