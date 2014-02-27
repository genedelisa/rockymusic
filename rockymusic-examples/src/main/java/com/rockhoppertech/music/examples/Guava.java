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

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.function.AbstractMusicFunction.Operation;
import com.rockhoppertech.music.midi.js.function.DurationFunction;
import com.rockhoppertech.music.midi.js.function.IntToMIDINote;
import com.rockhoppertech.music.midi.js.function.PitchFunction;
import com.rockhoppertech.music.midi.js.function.StartTimeFunction;
import com.rockhoppertech.music.midi.js.predicate.PitchGreaterThanPredicate;
import com.rockhoppertech.music.midi.js.predicate.PitchLessThanPredicate;

/**
 * Guava playground.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class Guava {
    static Logger logger = LoggerFactory.getLogger(Guava.class);

    public static void main(String[] args) {
        // composed();
        // permute();
        //toNotes();
        ranges();
    }

    public static void ranges() {
        RangeSet<Double> set = TreeRangeSet.create();
//        for (double i = 1d; i < 4d; i++) {
//            set.add(Range.closed(i, i + 1d));
//        }
        
        set.add(Range.closed(1d, 4d));
        logger.debug("the set {}", set);
        
        set.remove(Range.closed(2.5, 3d));
        logger.debug("after remove: set {}", set);
        RangeSet<Double> comp = set.complement();
        logger.debug("after remove: set comp {}", comp);

        Range<Double> first = Iterables.getFirst(set.asRanges(), null);
        logger.debug("first {}", first);
        
        //Iterables.
        
        for(Range<Double> r : set.asRanges()) {
            logger.debug("iterated range {}", r);
        }


//lowerEndpoint();
        
                set.clear();
        set.add(Range.open(1d, 4d));
        comp = set.complement();
        logger.debug("open comp {}", comp);
        

    }

    public static void composed() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C D E F G")
                .sequential()
                .build();

        AddFunction function = new AddFunction(1);
        Predicate<MIDINote> p = Predicates.compose(
                Predicates.alwaysTrue(), function
                );
        ImmutableList<MIDINote> notes = FluentIterable
                .from(track)
                .filter(p)
                .toList();
        for (MIDINote note : notes) {
            System.err.println(note);
        }

        track = MIDITrackBuilder.create()
                .noteString("C D E F G")
                .sequential()
                .build();
        p = new PitchGreaterThanPredicate(Pitch.E5);
        notes = FluentIterable
                .from(track)
                .filter(p)
                .toList();
        System.err.println("gt");
        for (MIDINote note : notes) {
            System.err.println(note);
        }

        track = MIDITrackBuilder.create()
                .noteString("C D E F G")
                .sequential()
                .build();
        PitchFunction pf = new PitchFunction(Operation.ADD, 1);
        p = Predicates.compose(
                new PitchGreaterThanPredicate(Pitch.E5),
                pf);
        notes = FluentIterable
                .from(track)
                .filter(p)
                .toList();
        System.err.println("gt add");
        for (MIDINote note : notes) {
            System.err.println(note);
        }

        Function<Timed, Timed> startAndDur = Functions.compose(
                new DurationFunction(Operation.SET, Duration.S),
                new StartTimeFunction(Operation.ADD, 1d));
        track = MIDITrackBuilder.create()
                .noteString("C D E F G")
                .sequential()
                .build();
        // they are actually MIDINotes
        ImmutableList<Timed> times = FluentIterable
                .from(track)
                .transform(startAndDur)
                .toList();
        System.err.println("start and dur");
        for (Timed note : times) {
            System.err.println(note);
        }

    }

    public static void permute() {
        List<Integer> numz = Lists.newArrayList(1, 2, 3);
        Collection<List<Integer>> perms = Collections2.permutations(numz);
        for (List<Integer> p : perms) {
            System.out.println(p);
        }

        // Collections2.orderedPermutations(elements, comparator)
    }

    static void and() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C D E G F4")
                .sequential()
                .build();

        Predicate<MIDINote> and = Predicates.and(new PitchGreaterThanPredicate(
                Pitch.C5), new PitchLessThanPredicate(Pitch.F5));

        ImmutableList<MIDINote> bandpass = ImmutableList.copyOf(Iterables
                .filter(track, and));
        for (MIDINote in : bandpass) {
            System.out.println(in);
        }
    }

    public static void goofaround() {
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

    static void toNotes() {
        final Function<Integer, MIDINote> intToNote = new Function<Integer, MIDINote>() {
            public MIDINote apply(Integer n) {
                Preconditions.checkNotNull(n, "cannot be null.");
                Preconditions.checkArgument(
                        n < 128,
                        "arg was %s. MIDI pitch must be 0 - 127", n);
                Preconditions
                        .checkArgument(
                                n >= 0,
                                "arg was %s. MIDI pitch must be 0 - 127",
                                n);
                return new MIDINote(n);
            }
        };

        List<Integer> pits = Lists.newArrayList(Pitch.C5, Pitch.EF5);
        ImmutableList<MIDINote> notes = FluentIterable
                .from(pits)
                .transform(intToNote)
                .toList();
        for (MIDINote note : notes) {
            System.out.println(note);
        }

        // in library
        IntToMIDINote iToMN = new IntToMIDINote();

        System.out.println("another");
        ImmutableList<MIDINote> noteList = ImmutableList.copyOf(
                Iterables.transform(pits, iToMN));
        for (MIDINote note : noteList) {
            System.out.println(note);
        }

        Iterable<MIDINote> noteIterable =
                Iterables.transform(pits, intToNote);
        for (MIDINote note : noteIterable) {
            System.out.println(note);
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

        Predicate<MIDINote> compose = Predicates.compose(
                new PitchGreaterThanPredicate(
                        Pitch.C5),
                transposeFunction);
    }

}
