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

import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.modifiers.AbstractModifier;

/**
 * A Guava Function to change {@code MIDINot}e's duration. Uses a CircularList
 * of values.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * @see com.rockhoppertech.collections.CircularArrayList
 * @see AbstractMusicFunction
 * @see TimedFunction
 * @see com.google.common.base.Function
 */
public class DurationFunction extends AbstractMusicFunction implements
        TimedFunction {
    private static final Logger logger = LoggerFactory
            .getLogger(DurationFunction.class);

    public DurationFunction() {
        super();
    }

    /**
     * Initialize the function with these values.
     * 
     * @param list
     */
    public DurationFunction(List<Number> list) {
        super(list);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param numbers
     */
    public DurationFunction(Number... numbers) {
        super(numbers);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param op
     * @param list
     */
    public DurationFunction(Operation op, List<Number> list) {
        super(op, list);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param operation
     * @param numbers
     */
    public DurationFunction(Operation operation, Number... numbers) {
        super(operation, numbers);
    }

    /*
     * @see com.google.common.base.Function#apply(java.lang.Object)
     */

    @Override
    public Timed apply(Timed timed) {
        logger.debug("before: " + timed);
        final double value = values.next().doubleValue();
        double d = 0d;

        Timed returnedTimed;
        if (isCreateDuplicate()) {
            returnedTimed = timed.duplicate();
        } else {
            returnedTimed = timed;
        }

        switch (operation) {
        case ADD:
            d = returnedTimed.getDuration() + value;
            d = roundToOne(d);
            returnedTimed.setDuration(d);

            break;
        case SUBTRACT:
            d = returnedTimed.getDuration() - value;
            d = roundToOne(d);
            returnedTimed.setDuration(d);

            break;
        case DIVIDE:
            d = returnedTimed.getDuration() / value;
            d = roundToOne(d);
            returnedTimed.setDuration(d);
            break;
        case MULTIPLY:
            d = returnedTimed.getDuration() * value;
            d = roundToOne(d);
            returnedTimed.setDuration(d);
            break;
        case MOD:
            d = returnedTimed.getDuration() % value;
            d = roundToOne(d);
            returnedTimed.setDuration(d);
            break;
        case SET:
            returnedTimed.setDuration(value);
            break;
        case QUANTIZE:
            d = AbstractModifier.quantize(returnedTimed.getDuration(),
                    value);
            d = roundToOne(d);
            returnedTimed.setDuration(d);
            break;
        }
        logger.debug("returnedNote: " + returnedTimed);
        return returnedTimed;
    }

    private double roundToOne(double d) {
        if (d < 1d) {
            logger.debug("value {} is < 1, setting to 1",
                    d);
            d = 1d;
        }
        return d;
    }

}
