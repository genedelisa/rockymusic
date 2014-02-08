# Rockhopper Music Tracks

MIDITracks contain MIDINotes and MIDIEvents. You can create a MIDITrack using
its constructors, the MIDITrackFactory (or other factories such as the
ScaleFactory), or the MIDITrackBuilder.

In this chapter, I will start with the low level way to create tracks and notes.
As we go along you will see easier ways to accomplish the same thing.

## Low level creation

Here is the long way to do it. Create a track and a note. The MIDINote
constructor is overloaded for most MIDI parameters. Here I'm just setting the
pitch using a constant for middle C from the Pitch class. There is a constructor
to set the start and duration along with the pitch.

After the track has a note, we can hear it by having a MIDIPerformer play it.

### Create a MIDITrack with a MIDINote

    MIDITrack track = new MIDITrack();
    MIDINote note = new MIDINote(Pitch.C5);
    note.setStartBeat(1d);
    note.setDuration(1d);
    track.add(note);
    MIDIPerformer perf = new MIDIPerformer();
    perf.play(track);

If you print the track to stdout or a logger, you will see this:

	Track Name:null
	Instrument:Instrument [patch=PIANO, name=Piano, minPitch=21, maxPitch=96]
	MIDINote[startBeat: 001.00 pitch: C5  dur: 001.00 : q endBeat: 002.00 midinum: 60 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 pan: 64 MIDITrack: unnamed track ]
	no events

The default instrument is the General MIDI Piano patch. The MIDITrack class has a setter to use a different patch via the Instrument class.

### The add method creating a MIDINote

You can have the track instantiate the MIDINote by passing a pitch number into
the track's add method. The track uses its own MIDIPerformer to play the track's
contents. If you want to set various playback parameters such as tempo, create a
performer instance and use that to play the track.

### Create a MIDITrack with a note via the add method

	MIDITrack track = new MIDITrack();
	track.add(Pitch.C5);
	track.play();

The add method is part of the track's minimal "fluent interface". Ask the Google
about that. You can "cascade" calls to add. The start beats are all the same, so
this creates a "chord".

### Create a chord

	import static com.rockhoppertech.music.Pitch.*;
	...
	MIDITrack track = new MIDITrack();
	track.add(Pitch.C5).add(E5).add(G5); // 1
	track.play();

1 The static import allow you to specify simply E5, G5.

If you want the notes to be sequential, i.e. their start beats are after the
previous note's start + duration, call the sequential method.

### Create a sequence of notes

	MIDITrack track = new MIDITrack();
	track.add(Pitch.C5).add(D5).add(E5).add(F5).add(G5); // 1
	track.sequential(); // 2
	track.play();

1 the track's add method allows as many calls as you like.
2 make the start beats sequential.

## Builder

I find it easier to create MIDITracks using a Builder that uses a fluent
interface. Here is an empty MIDITrack created via a builder.

###Builder

I use a modifed Builder pattern tht Josh Bloch described. JavaFX 2 used this
style of builder too.

	MIDITrack track = MIDITrackBuilder.create()
          .build();


Ok, not especially useful. How about some notes as a sequence?

###Builder with Note string


	MIDITrack track = MIDITrackBuilder.create()
          .noteString("C5 D E F G")
          .sequential()
          .build();
	track.play();

There are methods to set many track attributes. Here we set the duration of each note to 4 and the instrument used when the track is played to the GM Violin patch.

	 MIDITrack track = MIDITrackBuilder.create()
                .noteString("C E G")
                .durations(4d)
                .sequential()
                .instrument(Instrument.VIOLIN)
                .build();

###Circular collections
The builder uses a CircularList for several track attributes: start beat, duration, and velocity. Here is an example for duration. Notice that there are fewer durations than pitches. The CircularList will wrap.

	  MIDITrack track = MIDITrackBuilder.create()
		 .noteString("C E G")
		 .durations(Duration.E, Duration.Q)
		 .sequential()
		 .instrument(Instrument.VIOLIN)
		 .build();


## Track operations

### Append

You can build longer tracks by appending one to another. The append method makes
a deep copy of the MIDINotes and MIDIEvents.

####Appending one track to another


	MIDITrack track = MIDITrackBuilder.create()
                .noteString("C5 D E F G")
                .sequential()
                .build();
	MIDITrack track2 = MIDITrackBuilder.create()
                .noteString("F5 e d c") // 1
                .sequential()
                .build();

	logger.debug("here is the track\n{}", track);
	track.append(track2); // <2>
	logger.debug("here is the second track\n{}", track2);
	logger.debug("here is the track after the append\n{}", track);

1 Case does not matter.
2 Here is where the aciton is going down.


Results of an append

	here is the track
	Track Name:null
	Instrument:Instrument [patch=PIANO, name=Piano, minPitch=21, maxPitch=96]
	MIDINote[startBeat: 001.00 pitch: c5  dur: 001.00 : q endBeat: 002.00 midinum: 60 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 002.00 pitch: d5  dur: 001.00 : q endBeat: 003.00 midinum: 62 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 003.00 pitch: e5  dur: 001.00 : q endBeat: 004.00 midinum: 64 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 004.00 pitch: F5  dur: 001.00 : q endBeat: 005.00 midinum: 65 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 005.00 pitch: G5  dur: 001.00 : q endBeat: 006.00 midinum: 67 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	events

	here is the second track
	Track Name:null
	Instrument:Instrument [patch=PIANO, name=Piano, minPitch=21, maxPitch=96]
	MIDINote[startBeat: 001.00 pitch: F5  dur: 001.00 : q endBeat: 002.00 midinum: 65 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 002.00 pitch: e5  dur: 001.00 : q endBeat: 003.00 midinum: 64 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 003.00 pitch: d5  dur: 001.00 : q endBeat: 004.00 midinum: 62 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 004.00 pitch: c5  dur: 001.00 : q endBeat: 005.00 midinum: 60 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	events

	here is the track after the append
	Track Name:null
	Instrument:Instrument [patch=PIANO, name=Piano, minPitch=21, maxPitch=96]
	MIDINote[startBeat: 001.00 pitch: c5  dur: 001.00 : q endBeat: 002.00 midinum: 60 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 002.00 pitch: d5  dur: 001.00 : q endBeat: 003.00 midinum: 62 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 003.00 pitch: e5  dur: 001.00 : q endBeat: 004.00 midinum: 64 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 004.00 pitch: F5  dur: 001.00 : q endBeat: 005.00 midinum: 65 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 005.00 pitch: G5  dur: 001.00 : q endBeat: 006.00 midinum: 67 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 006.00 pitch: F5  dur: 001.00 : q endBeat: 007.00 midinum: 65 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 007.00 pitch: e5  dur: 001.00 : q endBeat: 008.00 midinum: 64 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 008.00 pitch: d5  dur: 001.00 : q endBeat: 009.00 midinum: 62 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 009.00 pitch: c5  dur: 001.00 : q endBeat: 010.00 midinum: 60 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	events


It's a bit clunky, but you can build a chord progression by using append. BTW., there is an actual ChordProgression class in the chord pacakge.

####Chord progression


	MIDITrack track = new MIDITrack();
	MIDITrack c = MIDITrackBuilder.create()
                .noteString("C4 C5 E G")
                .build();
	MIDITrack f = MIDITrackBuilder.create()
                .noteString("F4 C5 F A")
                .build();
	MIDITrack g7 = MIDITrackBuilder.create()
                .noteString("G4 B D5 F G")
                .build();

	track.append(c).append(f).append(g7).append(c);


### Merge

There is also a merge method. This also makes a deep copy, but does not alter
the start beats of the merged track.

#### Results of a merge

	here is the track after the merge
	Track Name:null
	Instrument:Instrument [patch=PIANO, name=Piano, minPitch=21, maxPitch=96]
	MIDINote[startBeat: 001.00 pitch: c5  dur: 001.00 : q endBeat: 002.00 midinum: 60 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 001.00 pitch: F5  dur: 001.00 : q endBeat: 002.00 midinum: 65 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 002.00 pitch: d5  dur: 001.00 : q endBeat: 003.00 midinum: 62 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 002.00 pitch: e5  dur: 001.00 : q endBeat: 003.00 midinum: 64 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 003.00 pitch: e5  dur: 001.00 : q endBeat: 004.00 midinum: 64 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 003.00 pitch: d5  dur: 001.00 : q endBeat: 004.00 midinum: 62 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 004.00 pitch: F5  dur: 001.00 : q endBeat: 005.00 midinum: 65 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 004.00 pitch: c5  dur: 001.00 : q endBeat: 005.00 midinum: 60 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	MIDINote[startBeat: 005.00 pitch: G5  dur: 001.00 : q endBeat: 006.00 midinum: 67 rest: false] chan: 0 velocity: 64 bank: 0 program: 0 (Piano) pitchbend: 0 voice: 0 MIDITrack: unnamed track ]
	events


If you don't like the results, you can unmerge

####Unmerged track


	track.unmerge(track2);


### Textual display of the Track

The usual toString method is provided. You can also print a MIDI String that the
MIDIStringParser class can parse. There are two versions, brief, and not so
brief.
The Not So Brief versions lists the parameters in this order:

pitch, start beat, duration, velocity, pan, channel, bank, program, pitchbend

The brief version is simply pitch, start beat, and duration.

Printing the track:


	// same code as the append example, then
	logger.debug("here is the track after the append\n{}", track); // <1>
	logger.debug("here is the track after the append\n{}",track.toBriefMIDIString());
	logger.debug("here is the track after the append\n{}", track.toMIDIString());

1 toString() we've already seen

Output

	// toBriefMIDIString
	// pitch, start beat, duration
	S+ c5,1.0,1.0 d5,2.0,1.0 e5,3.0,1.0 F5,4.0,1.0 G5,5.0,1.0 F5,6.0,1.0 e5,7.0,1.0 d5,8.0,1.0 c5,9.0,1.0

	// toMIDIString
	// pitch, start beat, duration, velocity, pan, channel, bank, program, pitchbend
	c5,1.000,1.000,64,64,0,0,0,0
	d5,2.000,1.000,64,64,0,0,0,0
	e5,3.000,1.000,64,64,0,0,0,0
	F5,4.000,1.000,64,64,0,0,0,0
	G5,5.000,1.000,64,64,0,0,0,0
	F5,6.000,1.000,64,64,0,0,0,0
	e5,7.000,1.000,64,64,0,0,0,0
	d5,8.000,1.000,64,64,0,0,0,0
	c5,9.000,1.000,64,64,0,0,0,0


### Chordify

The opposite of sequential() is chordify(). There is a version that takes a
start beat.

#### Chordify example


	MIDITrack track = new MIDITrack();
	MIDITrack c = MIDITrackBuilder.create()
	                .noteString("C4 C5 E G")
	                .build();

	MIDITrack g7 = MIDITrackBuilder.create()
	                .noteString("G4 B D5 F G")
	                .build();

	track.append(c);
	g7.sequential();
	track.append(g7);
	g7.chordify();
	track.append(g7);
	track.append(c);

	g7.chordify(12d);
	// ignores g7's start beat!
	// track.append(g7);
	track.merge(g7);


## Modifiers

You can modify a MIDITrack with a Modifier. There are several implementations
for most note parameters. You specify an operation and a value
or list of values. Internally the modifiers use a CircularList.

Here is an example which uses the PitchModifier. Not suprisingly, it modifies
the pitch of each note in the track when you call the MIDITrack's map() method.

####Transpose example


	MIDITrack track = MIDITrackBuilder
	                .create()
	                .name("transpose")
	                .noteString("C6 E G")
	                .build();
	PitchModifier mod = new PitchModifier(Modifier.Operation.ADD, 12);
	track.map(mod);


There are several operations that are defined in an enum in the Modifier interface.

#### Operations in the Modifier interface


	public enum Operation {
			SET,
			ADD,
			SUBTRACT,
			MULTIPLY,
			DIVIDE,
			MOD,
			QUANTIZE


There are modifiers for channel, start beat, duration, velocity, and instrument.
Each one uses a CircularList.

Writing your own is trivial. There are abstract classes that you should
subclass. See AbstractModifier and AbstractMIDINoteModifier.

###Anonymous modifier example


	MIDITrack track = MIDITrackBuilder.create()
	                    .name("pan modifier")
	                    .noteString("C6 E G")
	                    .build();

	MIDINoteModifier mod = new AbstractMIDINoteModifier(32) {
	    @Override
	     public void modify(MIDINote n) {
	          int pan = values.next().intValue();
	          logger.debug("pan value is {}", pan);
	          n.setPan(pan);
	      }
	};
	track.map(mod);



## Writing a MIDITrack to a file

Since JavaSound Track objects belong to a Sequence object, you will need to
create a Score, add the MIDITrack to the Score, then write to a file.

###Writing a Track to a MIDI file

	String filename = "src/test/resources/midifiles/sdv.mid"; // <1>
	Score score = new Score("SDV");
	score.add(track);
	ScoreFactory.writeToMIDIFile(filename, score); // <2>

1 Using Maven's directory structure here.
2 The score factory does the actual writing.



### Under the hood
MIDITrackFactory will turn a MIDITrack into a JavaSound Track.

####MIDITrack to Track

	public static Track trackFromMIDITrack(MIDITrack mt, Sequence sequence)

## What's next?

Take a look at the JUnit tests. There are many examples of usage. Also, there is
an "examples" subproject.
