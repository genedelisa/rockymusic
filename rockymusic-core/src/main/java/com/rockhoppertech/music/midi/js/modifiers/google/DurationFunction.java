package com.rockhoppertech.music.midi.js.modifiers.google;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.modifiers.AbstractModifier;
import com.rockhoppertech.music.modifiers.Modifier.Operation;

/**
 * A Guava Function to change {@code MIDINot}e's duration. Uses a
 * CircularList of values.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @see Circularlist
 */
public class DurationFunction extends AbstractMIDINoteFunction {
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
    public MIDINote apply(MIDINote note) {
        logger.debug("before: " + note);
        final double value = values.next().doubleValue();
        double d = 0d;

        MIDINote returnedNote;
        if (isCreateDuplicate()) {
            returnedNote = note.duplicate();
        } else {
            returnedNote = note;
        }

        switch (operation) {
        case ADD:
            d = returnedNote.getDuration() + value;
            d = roundToOne(d);
            returnedNote.setDuration(d);

            break;
        case SUBTRACT:
            d = returnedNote.getDuration() - value;
            d = roundToOne(d);
            returnedNote.setDuration(d);

            break;
        case DIVIDE:
            d = returnedNote.getDuration() / value;
            d = roundToOne(d);
            returnedNote.setDuration(d);
            break;
        case MULTIPLY:
            d = returnedNote.getDuration() * value;
            d = roundToOne(d);
            returnedNote.setDuration(d);
            break;
        case MOD:
            d = returnedNote.getDuration() % value;
            d = roundToOne(d);
            returnedNote.setDuration(d);
            break;
        case SET:
            returnedNote.setDuration(value);
            break;
        case QUANTIZE:
            d = AbstractModifier.quantize(returnedNote.getDuration(),
                    value);
            d = roundToOne(d);
            returnedNote.setDuration(d);
            break;
        }
        logger.debug("returnedNote: " + returnedNote);
        return returnedNote;
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