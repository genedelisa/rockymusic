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

import com.rockhoppertech.collections.CircularList;
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.modifiers.AbstractModifier;

/**
 * A Guava Function to change a {@code MIDINot}e's start time. Uses a
 * CircularList of values.
 * 
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * @see com.google.common.base.Function
 * @see Timed
 * @see CircularList
 * @see AbstractMusicFunction
 */

public class StartTimeFunction extends AbstractMusicFunction implements
        TimedFunction {
    private static final Logger logger = LoggerFactory
            .getLogger(StartTimeFunction.class);

    public StartTimeFunction() {
        super();
    }

    /**
     * Initialize the function with these values.
     * 
     * @param list
     */
    public StartTimeFunction(List<Number> list) {
        super(list);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param numbers
     */
    public StartTimeFunction(Number... numbers) {
        super(numbers);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param op
     * @param list
     */
    public StartTimeFunction(Operation op, List<Number> list) {
        super(op, list);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param operation
     * @param numbers
     */
    public StartTimeFunction(Operation operation, Number... numbers) {
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
            d = returnedTimed.getStartBeat() + value;
            d = roundToOne(d);
            returnedTimed.setStartBeat(d);

            break;
        case SUBTRACT:
            d = returnedTimed.getStartBeat() - value;
            d = roundToOne(d);
            returnedTimed.setStartBeat(d);

            break;
        case DIVIDE:
            d = returnedTimed.getStartBeat() / value;
            d = roundToOne(d);
            returnedTimed.setStartBeat(d);
            break;
        case MULTIPLY:
            d = returnedTimed.getStartBeat() * value;
            d = roundToOne(d);
            returnedTimed.setStartBeat(d);
            break;
        case MOD:
            d = returnedTimed.getStartBeat() % value;
            d = roundToOne(d);
            returnedTimed.setStartBeat(d);
            break;
        case SET:
            returnedTimed.setStartBeat(value);
            break;
        case QUANTIZE:
            d = AbstractModifier.quantize(returnedTimed.getStartBeat(),
                    value);
            d = roundToOne(d);
            returnedTimed.setStartBeat(d);
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
