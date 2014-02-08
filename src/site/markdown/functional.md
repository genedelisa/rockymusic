# Rockhopper Music

Rockhopper Music uses Google's [Guava](https://code.google.com/p/guava-libraries/) classes. Guava provides many useful classes, including a taste of [Funcitonal Programming](http://en.wikipedia.org/wiki/Functional_programming).

## What?

Here is an example of using a **Predicate**.

	MIDITrack track = MIDITrackBuilder.create()
                .noteString("C5 CS F FS E F4")
                .build();

        final Predicate<MIDINote> trebleNote = 
        	new Predicate<MIDINote>() {
            public boolean apply(MIDINote n) {
                return n.getMidiNumber() > Pitch.C5;
            }
        };
        List<MIDINote> highNotes = 
        	Lists.newArrayList(Iterables.filter(track,
                trebleNote));

## Predicates

Package **com.rockhoppertech.music.midi.js.predicate** contains implementations of Guava's **Predicate**.

Here is a predicate that checks each **MIDINote's** pitch number to see if it's greater than E5.

	MIDITrack track = MIDITrackBuilder.create()
                .noteString("C D E F G")
                .sequential()
                .build();
    Predicate<MIDINote> p = 
    	new PitchGreaterThanPredicate(Pitch.E5);
    ImmutableList<MIDINote> notes = 
    	FluentIterable
                .from(track)
                .filter(p)
                .toList();
                
Predicates can be connected by **Predicates.and** or **Predicates.or**.

	MIDITrack track = MIDITrackBuilder.create()
                .noteString("C D E G F4")
                .sequential()
                .build();

        Predicate<MIDINote> and = Predicates.and(
			new PitchGreaterThanPredicate(Pitch.C5), 
			new PitchLessThanPredicate(Pitch.F5));

        ImmutableList<MIDINote> bandpass = 
        	ImmutableList.copyOf(Iterables
                .filter(track, and));
                                
You can compose a predicate with a function.

	PitchFunction pf = new PitchFunction(Operation.ADD, 1);
	Predicate<MIDINote> p = 
         Predicates.compose(
                new PitchGreaterThanPredicate(Pitch.E5),
                pf);
    ImmutableList<MIDINote> notes = 
          FluentIterable
                .from(track)
                .filter(p)
                .toList();
                
                
Creating a **MIDITrack** by filtering a track with a predicate.

	MIDITrack track = ...
	MIDINumberBandPassPredicate p = 
		new MIDINumberBandPassPredicate(
                Pitch.C5, Pitch.E5);
	MIDITrack newTrack = MIDITrackFactory
                .createTrackFromPredicate(track, p);                

## Functions

Package **com.rockhoppertech.music.midi.js.function** contains implementations of Guava's **Function**.

Functions can be composed.

	Function<Timed, Timed> startAndDur = Functions.compose(
                new DurationFunction(Operation.SET, Duration.S),
                new StartTimeFunction(Operation.ADD, 1d));
    MIDITrack track = ...
    ImmutableList<Timed> times = FluentIterable
                .from(track)
                .transform(startAndDur)
                .toList();
                
                
Function to change the duration of each **MIDINote**.

	MIDITrack track = ...	
	DurationFunction function = new DurationFunction();
    function.setOperation(Operation.ADD);

     MIDITrack newTrack = MIDITrackFactory
                .createFromTimed(track, function);
	//or
	 List<Timed> newnotes = 
	 	Lists.transform(track.getNotes(), function);                
                                
# More docs to come               