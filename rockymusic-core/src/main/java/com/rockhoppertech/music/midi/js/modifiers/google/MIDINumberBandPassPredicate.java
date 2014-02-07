/**
 * 
 */
package com.rockhoppertech.music.midi.js.modifiers.google;

import com.google.common.collect.Range;
import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDINumberBandPassPredicate
        extends AbstractMIDINotePredicate
{

    private Range<Integer> range;
    private int low;
    private int high;

    /**
     * True only if MIDI pitch number is in this range.
     * 
     * @param low
     *            lower limit
     * @param high
     *            upper limit
     */
    public MIDINumberBandPassPredicate(int low, int high) {
        this.low = low;
        this.high = high;
        this.useClosedRange();
    }

    public void useOpenRange() {
        range = Range.open(low, high);
    }

    public void useClosedRange() {
        range = Range.closed(low, high);
    }

    /*
     * 
     * @see com.google.common.base.Predicate#apply(java.lang.Object)
     */
    @Override
    public boolean apply(MIDINote n) {
        boolean accept = false;
        int d = n.getMidiNumber();

        if (range.contains(d)) {
            accept = true;
        }

        // if (d >= low && d <= high) {
        // accept = true;
        // } else {
        // accept = false;
        // }
        return accept;
    }
}
