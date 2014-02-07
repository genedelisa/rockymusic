package com.rockhoppertech.music.midi.js.modifiers.google;

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

import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.music.modifiers.AbstractMIDINoteModifier;

/**
 * Superclass for all Functions that operate on MIDINotes.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public abstract class AbstractMusicFunction {

    private static final Logger logger = LoggerFactory
            .getLogger(AbstractMIDINoteModifier.class);

    public enum Operation {
        SET,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        MOD,
        QUANTIZE
    }

    protected CircularArrayList<Number> values;
    protected Operation operation = Operation.SET;
    protected boolean createDuplicate = false;

    public AbstractMusicFunction() {
        this.values = new CircularArrayList<Number>();
        this.values.add(1);
    }

    public AbstractMusicFunction(final Operation operation,
            final Number... numbers) {

        this.values = new CircularArrayList<Number>();
        this.operation = operation;
        this.setValues(numbers);
    }

    public AbstractMusicFunction(final Number... numbers) {
        this(Operation.SET, numbers);
    }

    public AbstractMusicFunction(final Operation op,
            final List<Number> list) {
        this.operation = op;
        this.values = new CircularArrayList<Number>();
        this.setValues(list);
    }

    public AbstractMusicFunction(final List<Number> list) {
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
     * @return the createDuplicate
     */
    public boolean isCreateDuplicate() {
        return createDuplicate;
    }

    /**
     * @param createDuplicate
     *            the createDuplicate to set
     */
    public void setCreateDuplicate(boolean createDuplicate) {
        this.createDuplicate = createDuplicate;
    }

}
