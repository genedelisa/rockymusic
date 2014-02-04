# Chords


There is an incipient chord capability in the library.

Well known chords are defined in chorddefs.xml which is read by the ChordFactory class. You can add your own chords here.


## Creating a chord

You can instantiate a Chord instance, or you can have the ChordFactory create one for you. The factory is the easy way to do it.


	Chord chord = ChordFactory.getChordByFullSymbol("C");
	
The Chord class has methods to set the inversion and voicing.

	chord.setInversion(1);
	
### Creating a MIDITrack from a Chord
You can use the cunningly named method createMIDITrack to, well, create a MIDITrack from a Chord.

	MIDITrack track = chord.createMIDITrack();
	// the pitches are in octave 0. transpose it to middle c
	PitchModifier mod = new PitchModifier(Modifier.Operation.ADD, 		Pitch.C5);
	track.map(mod);



## Create a Chord Progression

	String input = "C G7 | Dm G7 | C";
	ChordProgression chordProgression = ChordProgression
                .createFromNames(input);



## Create a Chord Progression using Roman Numerals


	String input = "I V7 | II V7 | I";
	ChordProgression chordProgression = 	
		ChordProgression.createFromRoman(input);


Yields this:

	C / G7 / | Dm / G7 / | C / / / |


You can also specify the scale and key.

## Create a Chord Progression using Roman Numerals, specifying the key/scale


	chordProgression = ChordProgression.createFromRoman(
		input, "Major", "D");

To get:

	D / A7 / | Em / A7 / | D / / / |
	
As you see, printing a chord progreesion yields an output similar to what you see the fake books like The Real Book. Only with better handwriting. 

## Chord progression from string

You can create a progression using fake book notation.

	String input = "C G7 | Dm G7 | C";
	ChordProgression chordProgression = ChordProgression
		.createFromNames(input);

You can read from a text file that contains chord changes.		

	ChordProgression chordProgression = ChordProgression
		.create(new File("src/test/resources/giantSteps.changes"));
		

## Next Steps

Take a look at the JUnit classes and the examples subproject for more.

