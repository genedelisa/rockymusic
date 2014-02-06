package com.rockhoppertech.music.examples;

/*
 * #%L
 * Rocky Music Examples
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

/**
 * Guava playground.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class Guava {
    static Logger logger = LoggerFactory.getLogger(Guava.class);

    public static void main(String[] args) {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C D E")
                .sequential()
                .build();

        // http://docs.guava-libraries.googlecode.com/git-history/release12/javadoc/com/google/common/collect/FluentIterable.html#filter(com.google.common.base.Predicate)

        ImmutableList<MIDINote> notes = FluentIterable
                .from(track)
                .transform(new AddFunction(1))
                .limit(10)
                .toList();

        for (MIDINote note : notes) {
            logger.debug("{}", note);
        }

        RangeSet<Double> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1d, 10d));
        rangeSet.add(Range.closed(11d, 20d));        
        for (Range<Double> r : rangeSet.asRanges()) {
            logger.debug("{}", r);
        }
        logger.debug("span {}", rangeSet.span());
        logger.debug("contains {}", rangeSet.contains(20d));        
        logger.debug("contains {}", rangeSet.contains(20.1));        
        

    }

    /**
     * Adds the value to each MIDINote's pitch.
     *
     */
    static class AddFunction implements Function<MIDINote, MIDINote> {
        private int value = 1;

        public AddFunction(int v) {
            this.value = v;
        }

        @Override
        public MIDINote apply(MIDINote note) {
            note.setMidiNumber(note.getMidiNumber() + this.value);
            return note;
        }
    }

}
