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
		banks = new ArrayList<Integer>();
		banks.add(0);
		programs = new ArrayList<Integer>();
		programs.add(0);
	}

	public InstrumentModifier(List<Integer> programs) {
		this(programs, Operation.SET);
	}

	public InstrumentModifier(List<Integer> programs, Operation op) {
		this.programs = programs;
		operation = op;
	}

	public InstrumentModifier(int program) {
		this(program, Operation.SET);
	}

	public InstrumentModifier(int program, Operation op) {
		programs = new ArrayList<Integer>();
		banks = new ArrayList<Integer>();
		banks.add(0);
		programs.add(program);
		operation = op;
	}

	public InstrumentModifier(int bank, int program) {
		banks = new ArrayList<Integer>();
		banks.add(bank);
		programs = new ArrayList<Integer>();
		programs.add(program);
	}

	@Override
	public String getName() {
		return "Instrument";
	}

	@Override
	public String getDescription() {
		return "Instrument modifier. Only the SET operation makes sense";
	}

	@Override
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	@Override
	public void setValues(double[] progs) {
		programs = new ArrayList<Integer>();
		for (double prog : progs) {
			programs.add((int) prog);
		}
	}

	public void setBanks(double[] banks) {
		this.banks = new ArrayList<Integer>();
		for (double bank : banks) {
			this.banks.add((int) bank);
		}
	}

	private int nextValue() {
		logger.debug("next value index: " + index);
		logger.debug("size: " + programs.size());

		if (index + 1 > programs.size()) {
			logger.debug("setting index to 0");
			index = 0;
		}
		int v = programs.get(index++);
		logger.debug("returning " + v);
		return v;
	}

	private int nextBank() {
		if (bindex + 1 > banks.size()) {
			bindex = 0;
		}
		int v = banks.get(bindex++);
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
		int program = nextValue();
		int bank = nextBank();

		switch (operation) {
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
 * This version: $Revision$ Last modified: $Date$ Last modified by: $Author$
 */
