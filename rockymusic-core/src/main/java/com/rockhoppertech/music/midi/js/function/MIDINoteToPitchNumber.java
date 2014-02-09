package com.rockhoppertech.music.midi.js.function;

import com.google.common.base.Function;
import static com.google.common.base.Preconditions.*;
import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * Guava Function to turn a {@code MIDINote} into a MIDI pitch number.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * @see Function
 * @see MIDINote
 */
public class MIDINoteToPitchNumber implements Function<MIDINote, Integer> {
    @Override
    public Integer apply(MIDINote n) {
        checkNotNull(n, "MIDINote cannot be null");
        return n.getMidiNumber();
    }
}
