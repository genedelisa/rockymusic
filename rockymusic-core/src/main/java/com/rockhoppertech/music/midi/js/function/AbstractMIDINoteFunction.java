package com.rockhoppertech.music.midi.js.function;

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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.modifiers.AbstractMIDINoteModifier;
import com.rockhoppertech.music.modifiers.Modifier.Operation;

/**
 * Superclass for all {@code Function}s that operate on {@code MIDINote}s.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * @see com.google.common.base.Function
 */
public abstract class AbstractMIDINoteFunction implements
        Function<MIDINote, MIDINote> {

    private static final Logger logger = LoggerFactory
            .getLogger(AbstractMIDINoteModifier.class);

    protected CircularArrayList<Number> values;
    protected Operation operation = Operation.SET;
    protected boolean createDuplicate = false;

    public AbstractMIDINoteFunction() {
        this.values = new CircularArrayList<Number>();
        this.values.add(1);
    }

    public AbstractMIDINoteFunction(final Operation operation,
            final Number... numbers) {

        this.values = new CircularArrayList<Number>();
        this.operation = operation;
        this.setValues(numbers);
    }

    public AbstractMIDINoteFunction(final Number... numbers) {
        this(Operation.SET, numbers);
    }

    public AbstractMIDINoteFunction(final Operation op,
            final List<Number> list) {
        this.operation = op;
        this.values = new CircularArrayList<Number>();
        this.setValues(list);
    }

    public AbstractMIDINoteFunction(final List<Number> list) {
        this(Operation.SET, list);
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     * @return the operation
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * @return the values
     */
    public List<Number> getValues() {
        return values;
    }

    /**
     * @param values
     *            the values to set
     */
    public void setValues(List<Number> values) {
        logger.debug("adding values {}", values);
        this.values.clear();
        for (Number n : values) {
            logger.debug("adding value {}", n);
            this.values.add(n);
        }
    }

    private void setValues(Number[] numbers) {
        // logger.debug("setting {}", numbers);
        this.values.clear();
        for (Number n : numbers) {
            logger.debug("adding value {}", n);
            this.values.add(n);
        }
    }

    public void setValues(final double[] array) {
        this.values.clear();
        for (final double element : array) {
            values.add(element);
        }
    }

    /**
     * Whether we're creating a view or are duplicating the MIDINote.
     * 
     * @return the createDuplicate
     */
    public boolean isCreateDuplicate() {
        return createDuplicate;
    }

    /**
     * The default is to create a "view" of the collection. This makes it create
     * a duplicate.
     * 
     * @param createDuplicate
     *            the createDuplicate to set
     */
    public void setCreateDuplicate(boolean createDuplicate) {
        this.createDuplicate = createDuplicate;
    }

}
