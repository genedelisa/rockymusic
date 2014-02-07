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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.modifiers.google.PitchGreaterThanPredicate;
import com.rockhoppertech.music.midi.js.modifiers.google.PitchLessThanPredicate;

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
    
    public static void funpred() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C5 CS F FS E F4")
                .build();

        final Predicate<MIDINote> trebleNote = new Predicate<MIDINote>() {
            public boolean apply(MIDINote n) {
                return n.getMidiNumber() > Pitch.C5;
            }
        };
        List<MIDINote> highNotes = Lists.newArrayList(Iterables.filter(track,
                trebleNote));
        // for (MIDINote note : highNotes) {
        // System.out.println(note);
        // }

        Predicate<MIDINote> high = new PitchGreaterThanPredicate(Pitch.C5);
        highNotes = Lists.newArrayList(Iterables.filter(track, high));
        for (MIDINote note : highNotes) {
            System.out.println(note);
        }

        // final List<MIDINote> highNotes =
        // Lists.immutableList(Iterables.filter(list, trebleNote));
        // ImmutableList<MIDINote> imlist =
        // ImmutableList.copyOf(Iterables.filter(list, high));
        //
        //
        final Function<MIDINote, MIDINote> transposeFunction = new Function<MIDINote, MIDINote>() {
            public MIDINote apply(MIDINote n) {
                n.setMidiNumber(n.getMidiNumber() + 1);
                return n;
            }
        };
        // Iterable<MIDINote> mni = Iterables.transform(Iterables.filter(list,
        // high), fun);

        final Function<MIDINote, Integer> tonums = new Function<MIDINote, Integer>() {
            @Override
            public Integer apply(MIDINote n) {
                return n.getMidiNumber();
            }
        };

        ImmutableList<Integer> imInts = ImmutableList.copyOf(Iterables
                .transform(Iterables.filter(track, high), tonums));
        for (Integer in : imInts) {
            System.out.println(in);
        }

        // Iterable i = Iterables.transform(Iterables.filter(Iterables.filter(
        // list, MIDINote.class), high), nameForNumber);

        Predicate<MIDINote> and = Predicates.and(new PitchGreaterThanPredicate(
                Pitch.C5), new PitchLessThanPredicate(Pitch.F5));
        ImmutableList<MIDINote> bandpass = ImmutableList.copyOf(Iterables
                .filter(track, and));
        for (MIDINote in : bandpass) {
            System.out.println(in);
        }

        
        Predicate<MIDINote> compose = Predicates.compose(
                new PitchGreaterThanPredicate(
                        Pitch.C5),
                transposeFunction);
    }

}
