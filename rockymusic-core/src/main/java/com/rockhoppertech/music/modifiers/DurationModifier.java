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

/**
 * Class <code>DurationModifier</code> Modifies the note's duration by the
 * amount. The series provided will wrap.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 * @see NoteModifier
 */
public class DurationModifier extends AbstractModifier implements
		TimedModifier, NoteModifier {

	private static final Logger logger = LoggerFactory
			.getLogger(DurationModifier.class);

	private Operation operation = Operation.SET;
	private CircularArrayList<Double> values;
	private NoteModifier successor;

	public DurationModifier() {
		values = new CircularArrayList<Double>();
		values.add(1d);
	}

	public DurationModifier(final double n) {
		values = new CircularArrayList<Double>();
		values.add(n);
	}

	public DurationModifier(final double... array) {
		values = new CircularArrayList<Double>();
		this.setValues(array);
	}

	public DurationModifier(final List<Double> values2) {
		values = new CircularArrayList<Double>();
		values.addAll(values2);
	}

	public DurationModifier(final Operation op, final double n) {
		operation = op;
		values = new CircularArrayList<Double>();
		values.add(n);
	}

	public DurationModifier(final Operation op, final double... array) {
		operation = op;
		values = new CircularArrayList<Double>();
		this.setValues(array);
	}

	public DurationModifier(final Operation op, final List<Double> values2) {
		operation = op;
		values = new CircularArrayList<Double>();
		values.addAll(values2);
	}

	private void doit(final Timed timed) {
		double d = 0d;
		final double value = values.next();
		switch (operation) {
		case ADD:
			d = timed.getDuration() + value;
			if (d <= 0) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("value %f is <=0, setting to 1",
							d));
				}
				d = 1d;
			}
			timed.setDuration(d);

			break;

		case SUBTRACT:
			d = timed.getDuration() - value;
			if (d <= 0) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("value %f is <=0, setting to 1",
							d));
				}
				d = 1d;
			}
			timed.setDuration(d);
			break;

		case DIVIDE:
			d = timed.getDuration() / value;
			if (d <= 0) {
				logger.debug("Rounding to 1: " + d);
				d = 1d;
			}
			timed.setDuration(d);
			break;

		case MULTIPLY:
			d = timed.getDuration() * value;
			if (d <= 0) {
				logger.debug("Rounding to 1: " + d);
				d = 1d;
			}
			timed.setDuration(d);
			break;

		case SET:
			timed.setDuration(value);
			break;

		case MOD:
			timed.setDuration(timed.getDuration() % value);
			break;

		case QUANTIZE:
			timed.setDuration(AbstractModifier.quantize(timed.getDuration(),
					value));
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
		return "Duration modifier";
	}

	/**
	 * <code>getName</code>
	 * 
	 * @return a <code>String</code> value
	 */
	@Override
	public String getName() {
		return "Duration";
	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	@Override
	public void modify(final Note note) {
		doit(note);
		if (successor != null) {
			successor.modify(note);
		}
	}

	@Override
	public void modify(final Timed timed) {
		doit(timed);
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
			values = new CircularArrayList<Double>();
		} else {
			values.clear();
		}

		for (final double element : array) {
			values.add(element);
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
 * This version: $Revision$ Last modified: $Date$ Last modified by: $Author$
 */
