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
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;

/**
 * Class <code>PitchModifier</code> Modifies the note's pitch MIDI number by the
 * amount. The series provided will wrap.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 * @see NoteModifier
 * @see AbstractModifier
 */
public class PitchModifier extends AbstractModifier implements NoteModifier {

	private static final Logger logger = LoggerFactory
			.getLogger(PitchModifier.class);

	private Operation operation = Operation.SET;
	private CircularArrayList<Number> values;
	private NoteModifier successor;

	public PitchModifier() {
		values = new CircularArrayList<Number>();
		values.add(Pitch.C5);
	}

	public PitchModifier(final int n) {
		values = new CircularArrayList<Number>();
		values.add(n);
	}

	public PitchModifier(final List<Double> list) {
		if (values == null) {
			values = new CircularArrayList<Number>();
		} else {
			values.clear();
		}
		values.addAll(values);
	}

	public PitchModifier(final Operation op, final double... array) {
		operation = op;
		values = new CircularArrayList<Number>();
		this.setValues(array);
	}

	public PitchModifier(final Operation op, final int n) {
		operation = op;
		values = new CircularArrayList<Number>();
		values.add(n);
	}

	public PitchModifier(final Operation op, final List<Integer> values2) {
		operation = op;
		values = new CircularArrayList<Number>();
		values.addAll(values2);
	}

	/**
	 * <code>getDescription</code>
	 * 
	 * @return a <code>String</code> value
	 */
	@Override
	public String getDescription() {
		return "Pitch modifier changes the MIDI number";
	}

	/**
	 * <code>getName</code>
	 * 
	 * @return a <code>String</code> value
	 */
	@Override
	public String getName() {
		return "Pitch Modifier";
	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	@Override
	public void modify(final Note note) {
		logger.debug("before: " + note);
		final double value = values.next().doubleValue();
		Pitch newPitch = null;
		int midiNumber = 0;

		switch (operation) {
		case ADD:
			midiNumber = (int) (note.getMidiNumber() + value);
			if ((midiNumber < 0) || (midiNumber > 127)) {
				midiNumber = note.getMidiNumber();
			}
			newPitch = PitchFactory.getPitch(midiNumber);
			note.setPitch(newPitch);
			break;
		case SUBTRACT:
			midiNumber = (int) (note.getMidiNumber() - value);
			if ((midiNumber < 0) || (midiNumber > 127)) {
				midiNumber = note.getMidiNumber();
			}
			newPitch = PitchFactory.getPitch(midiNumber);
			note.setPitch(newPitch);
			break;
		case DIVIDE:
			midiNumber = (int) (note.getMidiNumber() / value);
			if ((midiNumber < 0) || (midiNumber > 127)) {
				midiNumber = note.getMidiNumber();
			}
			newPitch = PitchFactory.getPitch(midiNumber);
			note.setPitch(newPitch);
			break;
		case MULTIPLY:
			midiNumber = (int) (note.getMidiNumber() * value);
			if ((midiNumber < 0) || (midiNumber > 127)) {
				midiNumber = note.getMidiNumber();
			}
			newPitch = PitchFactory.getPitch(midiNumber);
			note.setPitch(newPitch);
			break;
		case MOD:
			midiNumber = (int) (note.getMidiNumber() % value);
			if ((midiNumber < 0) || (midiNumber > 127)) {
				midiNumber = note.getMidiNumber();
			}
			newPitch = PitchFactory.getPitch(midiNumber);
			note.setPitch(newPitch);
			break;
		case SET:
			newPitch = PitchFactory.getPitch(value);
			note.setPitch(newPitch);
			break;
		case QUANTIZE:
			double d = AbstractModifier.quantize(note.getMidiNumber(),
					value);
			if ((d < 0) || (d > 127)) {
				d = note.getMidiNumber();
			}
			newPitch = PitchFactory.getPitch(d);
			note.setPitch(newPitch);
		}
		logger.debug("after: " + note);

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
	public void setSuccessor(final NoteModifier successor) {
		this.successor = successor;
	}

	@Override
	public void setValues(final double[] array) {
		if (values == null) {
			values = new CircularArrayList<Number>();
		} else {
			values.clear();
		}

		for (final Double element : array) {
			values.add(element.intValue());
		}
	}

	public void setValues(final List<Number> values) {
		if (this.values == null) {
			this.values = new CircularArrayList<Number>();
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
 * This version: $Revision$ Last modified: $Date$ Last modified by: $Author$
 */
