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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.collections.CircularArrayList;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public abstract class AbstractModifier implements Modifier {
    private static final Logger logger = LoggerFactory
            .getLogger(AbstractModifier.class);

    protected CircularArrayList<Number> values;
    protected Operation operation = Operation.SET;
    protected NoteModifier successor;

    public AbstractModifier() {
        this.values = new CircularArrayList<Number>();
        this.values.add(1);
    }

    public AbstractModifier(final Operation operation,
            final Number... numbers) {
        logger.debug("setting values to {}", Arrays.toString(numbers));
        this.values = new CircularArrayList<Number>();
        this.operation = operation;
        this.setValues(numbers);
    }

    public AbstractModifier(final Number... numbers) {
        this(Operation.SET, numbers);
    }

    public AbstractModifier(final Operation op,
            final List<Number> list) {
        this.operation = op;
        this.values = new CircularArrayList<Number>();
        this.setValues(list);
    }

    public AbstractModifier(final List<Number> list) {
        this(Operation.SET, list);
    }

    @Override
    public String getDescription() {
        return "None";
    }

    @Override
    public String getName() {
        return "Untitled";
    }

    // @Override
    public void setSuccessor(NoteModifier successor) {
        this.successor = successor;
    }

    @Override
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public static double quantize(final double value, final double q) {
        double result = 0d;
        int decimalPlaces = 3;
        BigDecimal ratio = null;

        if (value == q) {
            // System.err.printf("value %f == %f no change\n",
            // value,
            // q);
            return value;
        }

        if (value < q) {
            double v = value;
            // make it q at a minimum
            while (v < q) {
                v += q;
            }
            ratio = new BigDecimal(v);
            ratio = ratio.setScale(decimalPlaces, BigDecimal.ROUND_HALF_DOWN);
            ratio = ratio.divide(new BigDecimal(q));
            // System.err.println(ratio.doubleValue());
            result = Math.floor(ratio.doubleValue()) * q;

            // can't have a duration of 0 for example
            if (result == 0d) {
                result = value < q ? value : q;
            }
            // System.err.println("<");
            return result;
        }

        ratio = new BigDecimal(value);
        ratio = ratio.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        ratio = ratio.divide(new BigDecimal(q));

        // System.err.println(ratio.doubleValue());
        // System.err.println(">");
        result = Math.floor(ratio.doubleValue()) * q;
        // System.err.printf("value is %f quant %f is %f\n",
        // value,
        // q,
        // result);
        return result;
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

    @Override
    public void setValues(final double[] array) {
        this.values.clear();
        for (final double element : array) {
            values.add(element);
        }
    }

    /**
     * @return the operation
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * @return the successor
     */
    public NoteModifier getSuccessor() {
        return successor;
    }
}
