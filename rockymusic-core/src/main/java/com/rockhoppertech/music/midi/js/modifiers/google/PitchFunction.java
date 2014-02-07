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

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
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
public class PitchFunction extends AbstractMIDINoteFunction {
    private static final Logger logger = LoggerFactory
            .getLogger(PitchFunction.class);

    public PitchFunction() {
        super();
    }

    /**
     * Initialize the function with these values.
     * 
     * @param list
     */
    public PitchFunction(List<Number> list) {
        super(list);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param numbers
     */
    public PitchFunction(Number... numbers) {
        super(numbers);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param op
     * @param list
     */
    public PitchFunction(Operation op, List<Number> list) {
        super(op, list);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param operation
     * @param numbers
     */
    public PitchFunction(Operation operation, Number... numbers) {
        super(operation, numbers);
    }

    /*
     * @see com.google.common.base.Function#apply(java.lang.Object)
     */

    @Override
    public MIDINote apply(MIDINote note) {
        logger.debug("before: " + note);
        final double value = values.next().doubleValue();
        Pitch newPitch = null;
        int midiNumber = 0;

        MIDINote returnedNote;
        if (isCreateDuplicate()) {
            returnedNote = note.duplicate();
        } else {
            returnedNote = note;
        }

        switch (operation) {
        case ADD:
            midiNumber = (int) (note.getMidiNumber() + value);
            if ((midiNumber < 0) || (midiNumber > 127)) {
                midiNumber = note.getMidiNumber();
            }
            newPitch = PitchFactory.getPitch(midiNumber);
            returnedNote.setPitch(newPitch);
            break;
        case SUBTRACT:
            midiNumber = (int) (note.getMidiNumber() - value);
            if ((midiNumber < 0) || (midiNumber > 127)) {
                midiNumber = note.getMidiNumber();
            }
            newPitch = PitchFactory.getPitch(midiNumber);
            returnedNote.setPitch(newPitch);
            break;
        case DIVIDE:
            midiNumber = (int) (note.getMidiNumber() / value);
            if ((midiNumber < 0) || (midiNumber > 127)) {
                midiNumber = note.getMidiNumber();
            }
            newPitch = PitchFactory.getPitch(midiNumber);
            returnedNote.setPitch(newPitch);
            break;
        case MULTIPLY:
            midiNumber = (int) (note.getMidiNumber() * value);
            if ((midiNumber < 0) || (midiNumber > 127)) {
                midiNumber = note.getMidiNumber();
            }
            newPitch = PitchFactory.getPitch(midiNumber);
            returnedNote.setPitch(newPitch);
            break;
        case MOD:
            midiNumber = (int) (note.getMidiNumber() % value);
            if ((midiNumber < 0) || (midiNumber > 127)) {
                midiNumber = note.getMidiNumber();
            }
            newPitch = PitchFactory.getPitch(midiNumber);
            returnedNote.setPitch(newPitch);
            break;
        case SET:
            newPitch = PitchFactory.getPitch(value);
            returnedNote.setPitch(newPitch);
            break;
        case QUANTIZE:
            double d = AbstractModifier.quantize(note.getMidiNumber(),
                    value);
            if ((d < 0) || (d > 127)) {
                d = note.getMidiNumber();
            }
            newPitch = PitchFactory.getPitch(d);
            returnedNote.setPitch(newPitch);
        }
        logger.debug("returnedNote: " + returnedNote);
        return returnedNote;
    }
}
