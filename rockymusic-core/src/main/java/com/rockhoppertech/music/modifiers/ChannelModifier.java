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
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * Class <code>ChannelModifier</code>
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 * @see MIDINoteModifier
 */
public class ChannelModifier extends AbstractMIDINoteModifier {

    private static final Logger logger = LoggerFactory
            .getLogger(ChannelModifier.class);

    public ChannelModifier() {

    }

    public ChannelModifier(List<Number> list) {
        super(list);
    }

    public ChannelModifier(Number... numbers) {
        super(numbers);
    }

    public ChannelModifier(Operation op, List<Number> list) {
        super(op, list);
    }

    public ChannelModifier(Operation operation, Number... numbers) {
        super(operation, numbers);
    }

    /**
     * <code>getDescription</code>
     * 
     * @return a <code>String</code> value
     */
    @Override
    public String getDescription() {
        return "Channel modifier changes the MIDI channel";
    }

    /**
     * <code>getName</code>
     * 
     * @return a <code>String</code> value
     */
    @Override
    public String getName() {
        return "Channel Modifier";
    }

   

    @Override
    public void modify(final MIDINote note) {
        logger.debug("before: " + note);
        final double value = values.next().doubleValue();
        int midiChannel = 0;
        int max = 16;

        switch (operation) {
        case ADD:
            midiChannel = (int) (note.getChannel() + value);
            if (midiChannel < 0 || midiChannel > max) {
                midiChannel = note.getChannel();
            }
            note.setChannel(midiChannel);
            break;
        case SUBTRACT:
            midiChannel = (int) (note.getChannel() - value);
            if (midiChannel < 0 || midiChannel > max) {
                midiChannel = note.getChannel();
            }
            note.setChannel(midiChannel);
            break;
        case DIVIDE:
            midiChannel = (int) (note.getChannel() / value);
            if (midiChannel < 0 || midiChannel > max) {
                midiChannel = note.getChannel();
            }
            note.setChannel(midiChannel);
            break;
        case MULTIPLY:
            midiChannel = (int) (note.getChannel() * value);
            if (midiChannel < 0 || midiChannel > max) {
                midiChannel = note.getChannel();
            }
            note.setChannel(midiChannel);
            break;
        case MOD:
            midiChannel = (int) (note.getChannel() % value);
            if (midiChannel < 0 || midiChannel > max) {
                midiChannel = note.getChannel();
            }
            note.setChannel(midiChannel);
            break;
        case SET:
            midiChannel = (int) value;
            note.setChannel(midiChannel);
            break;
        case QUANTIZE:
            double d = AbstractModifier.quantize(note.getChannel(),
                    value);
            if (d < 0 || d > max) {
                d = note.getChannel();
            }
            note.setChannel((int) d);
        }
        logger.debug("after: " + note);
    }

  
}
