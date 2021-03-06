package com.rockhoppertech.music.midi.js.predicate;

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

import com.google.common.base.Preconditions;
import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * Predicate that checks the {@code MIDINote}'s MIDI number to determine if it
 * is greater than the test number.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * @see com.google.common.base.Predicate
 * @see AbstractMIDINotePredicate
 */
public class PitchGreaterThanPredicate extends AbstractMIDINotePredicate {

    private int midiNumber;

    public PitchGreaterThanPredicate(int testPitch) {
        this.midiNumber = testPitch;
    }

    @Override
    public boolean apply(MIDINote n) {
        Preconditions.checkNotNull(n);
        return n.getMidiNumber() > this.midiNumber;
    }
}
