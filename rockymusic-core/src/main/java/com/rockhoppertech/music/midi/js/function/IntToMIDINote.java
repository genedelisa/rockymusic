package com.rockhoppertech.music.midi.js.function;

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
