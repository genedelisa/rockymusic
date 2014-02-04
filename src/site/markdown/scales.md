#Scales

documentation coming soon.

## ScaleFactory
The preferred way to get a Scale instance is to use the factory.
The ScaleFactory class reads scaledefs.xml, which defines many different scales. You can add your own scales to the XML file.

The first example shows you how to get a Scale instance from the factory by its name. (The names, not surprisingly, are defined in the XML file).
### Getting a known scale by name
	Scale scale = ScaleFactory.createFromName("Major");
	
	
The ScaleFactory has methods to create a MIDITrack from a Scale instance. Here we pass in a scale and the "key", or pitch om which to start the scale. The track is already sequential.
###Scale to MIDITrack

	MIDITrack track = ScaleFactory.createMIDITrack(scale, Pitch.C5);

Here we pass in a scale, a start beat, and a duration for each note in the scale.
 	
	MIDItrack track = ScaleFactory.createMIDITrack(scale, 1d, Duration.Q);
	

# Defined Scales
Here are the currently defined scales in sccaledefs.xml. You pass any of these names to the factory to create a Scale instance.

	Harmonic Minor
	Pelog2
	Augmented
	Algerian
	Gypsy
	Melodic Minor
	Arabian
	Ethiopian
	Enigmatic
	Harmonic Major
	Diminished dominant
	Balinese
	Byzantine
	Diminished minor
	Hawaiian
	Oriental
	Pelog
	Octatonic
	Mohammedan
	Hungarian Gypsy
	Egyptian
	Kumoi
	Whole-Tone
	Major
	Overtone
	Leading Whole Tone
	Hungarian minor
	Neapolitan minor
	Chinese
	Whole-Tone b5
	Hirajoshi
	Iwato
	Mongolian
	Bebop half-diminished
	Hindu
	Double Harmonic
	Japanese
	Spanish
	Jewish
	Neapolitan Major
	Persian
	Lydian minor
	Eight-tone Spanish
	Bebop minor
	Bebop Dominant
	Major Phrygian
	Javanese
	Chromatic
	Hungarian Major
	Major Locrian
	
