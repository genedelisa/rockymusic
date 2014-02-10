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

import com.google.common.base.Function;
import com.rockhoppertech.music.midi.js.MIDINote;

import static com.google.common.base.Preconditions.*;

/**
 * Guava Function to turn an integer to a {@code MIDINote}.
 * 
 * <pre>
 * {@code
 *  List<Integer> pits = Lists.newArrayList(Pitch.C5, Pitch.EF5);
 *  IntToMIDINote intToNote = new IntToMIDINote();
 *  Iterable<MIDINote> noteIterable =
                Iterables.transform(pits, intToNote);
 * }
 * </pre>
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * @see Function
 * @see MIDINote
 */
public class IntToMIDINote implements Function<Integer, MIDINote> {
    @Override
    public MIDINote apply(Integer n) {
        checkNotNull(n, "Pitch number cannot be null");
        checkArgument(n < 128,
                "arg was %s. MIDI pitch must be 0 - 127", n);

        checkArgument(n >= 0,
                "arg was %s. MIDI pitch must be 0 - 127",
                n);
        return new MIDINote(n);
    }
}
