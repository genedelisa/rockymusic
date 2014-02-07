/**
 * 
 */
package com.rockhoppertech.music.midi.js.modifiers.google;

import com.rockhoppertech.music.midi.js.MIDINote;

/**
 *
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */

public class PitchGreaterThanPredicate extends AbstractMIDINotePredicate {

    private int testPitch;

    public PitchGreaterThanPredicate(int testPitch) {
        this.testPitch = testPitch;
    }

    public int getTestPitch() {
        return this.testPitch;
    }

    public void setTestPitch(int testPitch) {
        this.testPitch = testPitch;
    }

    @Override
    public boolean apply(MIDINote n) {
        return n.getMidiNumber() > this.testPitch;
    }
}
