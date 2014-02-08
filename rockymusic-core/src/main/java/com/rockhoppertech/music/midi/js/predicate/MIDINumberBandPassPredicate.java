/**
 * 
 */
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
import com.google.common.collect.Range;
import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * A "bandpass" filter for {@code MIDINote}s.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * @see com.google.common.base.Predicate
 * @see Range
 */
public class MIDINumberBandPassPredicate extends AbstractMIDINotePredicate {

    private Range<Integer> range;
    private int low;
    private int high;

    /**
     * True only if MIDI pitch number is in this range. The range is closed by
     * default.
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

    /**
     * Use an open {@code Range}.
     */
    public void useOpenRange() {
        range = Range.open(low, high);
    }

    /**
     * Use a closed {@code Range}. This is the default.
     */
    public void useClosedRange() {
        range = Range.closed(low, high);
    }

    /*
     * 
     * @see com.google.common.base.Predicate#apply(java.lang.Object)
     */
    @Override
    public boolean apply(MIDINote n) {
        Preconditions.checkNotNull(n);
        boolean accept = false;
        int d = n.getMidiNumber();
        if (range.contains(d)) {
            accept = true;
        }
        return accept;
    }
}
