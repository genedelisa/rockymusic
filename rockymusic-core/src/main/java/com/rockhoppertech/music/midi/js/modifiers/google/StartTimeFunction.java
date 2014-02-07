package com.rockhoppertech.music.midi.js.modifiers.google;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.modifiers.AbstractModifier;
import com.rockhoppertech.music.modifiers.Modifier.Operation;

/**
 * A Guava Function to change {@code MIDINot}e's MIDI pitch number. Uses a
 * CircularList of values.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @see Circularlist
 */
public class StartTimeFunction extends AbstractMIDINoteFunction {
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
            d = returnedNote.getStartBeat() + value;
            d = roundToOne(d);
            returnedNote.setStartBeat(d);

            break;
        case SUBTRACT:
            d = returnedNote.getStartBeat() - value;
            d = roundToOne(d);
            returnedNote.setStartBeat(d);

            break;
        case DIVIDE:
            d = returnedNote.getStartBeat() / value;
            d = roundToOne(d);
            returnedNote.setStartBeat(d);
            break;
        case MULTIPLY:
            d = returnedNote.getStartBeat() * value;
            d = roundToOne(d);
            returnedNote.setStartBeat(d);
            break;
        case MOD:
            d = returnedNote.getStartBeat() % value;
            d = roundToOne(d);
            returnedNote.setStartBeat(d);
            break;
        case SET:
            returnedNote.setStartBeat(value);
            break;
        case QUANTIZE:
            d = AbstractModifier.quantize(returnedNote.getStartBeat(),
                    value);
            d = roundToOne(d);
            returnedNote.setStartBeat(d);
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
