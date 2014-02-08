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

import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.modifiers.AbstractModifier;

/**
 * A Guava Function to change {@code MIDINot}e's MIDI channel. Uses a
 * CircularList of values.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * @see com.rockhoppertech.collections.CircularArrayList
 * @see AbstractMusicFunction
 * @see MIDINoteFunction
 * @see com.google.common.base.Function
 */

public class ChannelFunction extends AbstractMusicFunction implements
        MIDINoteFunction {
    private static final Logger logger = LoggerFactory
            .getLogger(ChannelFunction.class);

    public ChannelFunction() {
        super();
    }

    /**
     * Initialize the function with these values.
     * 
     * @param list
     */
    public ChannelFunction(List<Number> list) {
        super(list);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param numbers
     */
    public ChannelFunction(Number... numbers) {
        super(numbers);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param op
     * @param list
     */
    public ChannelFunction(Operation op, List<Number> list) {
        super(op, list);
    }

    /**
     * Initialize the function with these values.
     * 
     * @param operation
     * @param numbers
     */
    public ChannelFunction(Operation operation, Number... numbers) {
        super(operation, numbers);
    }

    /*
     * @see com.google.common.base.Function#apply(java.lang.Object)
     */

    @Override
    public MIDINote apply(MIDINote note) {
        logger.debug("before: " + note);
        final int value = values.next().intValue();
        int midiNumber = 0;

        MIDINote returnedNote;
        if (isCreateDuplicate()) {
            returnedNote = note.duplicate();
        } else {
            returnedNote = note;
        }

        switch (operation) {
        case ADD:
            midiNumber = note.getChannel() + value;
            midiNumber = validateValue(note, midiNumber);
            returnedNote.setChannel(midiNumber);
            break;
        case SUBTRACT:
            midiNumber = note.getChannel() - value;
            midiNumber = validateValue(note, midiNumber);
            returnedNote.setChannel(midiNumber);
            break;
        case DIVIDE:
            midiNumber = note.getChannel() / value;
            midiNumber = validateValue(note, midiNumber);
            returnedNote.setChannel(midiNumber);
            break;
        case MULTIPLY:
            midiNumber = note.getChannel() * value;
            midiNumber = validateValue(note, midiNumber);
            returnedNote.setChannel(midiNumber);
            break;
        case MOD:
            midiNumber = note.getChannel() % value;
            midiNumber = validateValue(note, midiNumber);
            returnedNote.setChannel(midiNumber);
            break;
        case SET:
            returnedNote.setChannel(midiNumber);
            break;
        case QUANTIZE:
            double d = AbstractModifier.quantize(note.getChannel(),
                    value);
            if ((d < 0) || (d > 127)) {
                d = note.getChannel();
            }
            returnedNote.setChannel(midiNumber);
        }
        logger.debug("returnedNote: " + returnedNote);
        return returnedNote;
    }

    private int validateValue(MIDINote note, int midiNumber) {
        if ((midiNumber < 0) || (midiNumber > 127)) {
            midiNumber = note.getChannel();
        }
        return midiNumber;
    }
}
