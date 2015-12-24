# Rockhopper Music Quick start

## Main classes

The **Score** class contains **MIDITrack**s. **MIDITrack**s can contain either **MIDINote**s or **MIDIEvent**s. One of the attributes of the **MIDINote** class is a **Pitch** object. There are constants defined for each MIDI pitch number. 

> `I chose to use C5 as middle C - even though other libraries such as Common Music use C4 to represent middle C. If I did that, then the first octaves would have negative numbers. Ugly.`

The **MIDITrack**'s **add** method is overloaded. You can simply pass in a **Pitch** constant and it will create a **MIDINote** with default attributes. There is a builder for **MIDINote** if you want to control the universe.


## Simple Example

Here is a **Score** with a track that contains just middle C:

	Score score = new Score("name of the score");
	// c.f. MIDITrackBuilder
	MIDITrack track = new MIDITrack();
	score.add(track);
	track.add(Pitch.C5);
	// then use it
	ScoreFactory.writeToMIDIFile("test.mid"", score);
	// or
	score.play();
	// or even
	track.play();

This will create a MIDI file (format 1) with a metamessage track (tempo, key signatures etc.) and the specified track[s].

I've created Builders for many of the classes. You'd probably be better off using the **MIDITrackBuilder** class to create **MIDITrack**s.

## Pitches

My original classes needed to be used in CSound (octave.pc or hz), Synclavier, or MIDI. When you look at the **Pitch** class, that is why those methods are in there. I may whittle them down.

I chose C5 to be middle C simply because I didn't want negative octave numbers. MusicXML, Common Lisp Music, and others are "off by 1". Take a look at [Scientific pitch notation](http://en.wikipedia.org/wiki/Scientific_pitch_notation) if you are interested.

There is a *PitchFactory* class. As you may guess, it's the GoF factory design pattern - and for *Pitch* instances it uses the Flyweight design pattern. For that reason, you are not going to write

    new Pitch(60);
    
There are constants defined in **Pitch**.

	public static final int C5 = 60;
	public static final int MIDDLE_C = 60;
	etc.
Those are simply MIDI numbers. To get a Pitch instance, you'd use the factory - which is a Singleton.

    Pitch myPitch = PitchFactory.instance().getPitch(Pitch.C5)
    
If you call this method twice, you will get the same instance. That's how the Flyweight works. What varies is the way the Pitch is uses, which is in a **Note** object, or its subclass **MIDINote**.

In many places in the API, you simply need the MIDI pitch number, so those constants will be enough. 

There is an easier way to use the Pitch class. Don't use it at all. Let the API classes use it. You can simply use English note names. I suppose that I could use other note names as **Lilypond** does, but I haven't needed it.

## Text based note specification

You can also use a text based format for creating a **MIDITrack**. There is a constructor that uses a **MIDIStringParser** to initialize the track. Later, we'll see the builder class uses this parser also.

	MIDITrack track = new MIDITrack("C5 D E F G A B C6");

This creates a track with the specified pitches. The octave is a "running" value. All of the start times are at beat 1. In some cases this is what you want. You can modify the track by calling

	track.sequential();

or you can specify sequential start times in the string by prepending "S+" to the string.

	MIDITrack track = new MIDITrack("S+ C5 D E F G A B C6");

You can specify a start beat with S=N, where N is the beat. This will be a "running"	start time which will apply to all pitches until the next S.

	S+ C D E S=5 E S=8 G
	
No spaces around the = sign!

These examples are on a single line, but the parser will grok newlines. This is handy if you want to save the strings to a file and read them back in. The newlines help with readability.

### Caveat

This text format is simply a way to create tracks quickly. It is not intended to be a full language or even a DSL.

### Comments

Multi line comments are not supported yet. But you can
use single line comments using either // or /* */ on a single line

	C D E // a couple notes
	C /* blah blah */ D E
	

### Durations

You can specify durations as a real number in beats

	C,1 D,1.5 E,2.0

Note that you need to use a comma between the pitch and the duration.

Or you can use these strings:

<table>
    <tr>
        <td>d</td>
        <td>double whole note</td>
    </tr>
     <tr>
        <td>w</td>
        <td>whole note</td>
    </tr>
    <tr>
        <td>h</td>
        <td>half note</td>
    </tr>

     <tr>
        <td>q</td>
        <td>quarter note</td>
    </tr>

     <tr>
        <td>e</td>
        <td>eighth note</td>
    </tr>

     <tr>
        <td>s</td>
        <td>sixteenth note</td>
    </tr>

     <tr>
        <td>t</td>
        <td>thirtysecond note</td>
    </tr>

     <tr>
        <td>x</td>
        <td>sixtyfourth note</td>
    </tr>

     <tr>
        <td>o</td>
        <td>one twenty eighth note</td>
    </tr>

</table>

Each can be modified by appending a t to indicate a triplet.

	C,qt D,qt E,qt
	
You can use dots too.

	C,q. D,e
	
### Repeating

Xn (element) can specify a number of repeated elements

	X3 C
	yields
	C C C
	
Put multiple elements to be repeated inside parentheses.

	
	X3 (C D) E F
	yields
	C D C D C D E F
	
	X2 (C D) E F X2 (G A)
	yields
	C D C D E F G A G A
	
### Instruments

You can specify an instrument with **In** where n=program number or **I"name"**

	I"Piano"
	I25
	
Not sure that this is very useful. I usually just set the instrument on the **MIDITrack**.	

## Builders

I've created a few Builders (ask the Google about the builder design pattern). To create a **MIDITrack**, you can use the **MIDITrackBuilder**. Here is an example that builds a track with the meta text name set, the patch is set to a General MIDI patch, pitches from a note string, and durations set to 4. The start beat is 4.5, and the succeeding notes start after the previous note ends due to the call to **sequential**. Without this, you'd create a chord with the notes all having the same start beat.

	 Score score = new Score("Chord");
     MIDITrack violin = MIDITrackBuilder.create()
                .name("Violin I")
                .instrument(Instrument.VIOLIN)
                .noteString("C6 E G")
                .startBeats(4.5)
                .durations(4d)
                .velocities(32, 64)
                .sequential()
                .build();
     score.add(violin);

The durations can be a comma delimited list that will be cycled. There are also constants for common durations. (A static import can make these easier to use).
	
	...as before
	.durations(Duration.SIXTEENTH_NOTE, Duration.EIGHTH_NOTE)
	...
The startBeats, durations, and velocities methods accept a collection of values. If the number of these values do not match the number of specified pitches, they will wrap - e.g. for 12 pitches and 2 durations, the durations will cycle through the list.
	
### Scales

There are also Scale classes. One easy way to use them is through the **MIDITrackBuilder**. Here is a 3 octave D Major scale. You don't need to call **.sequential** since that is assumed.

	 MIDITrack track = MIDITrackBuilder.create()
                .scaleName("Major")
                .startPitch("D3")
                .nScaleOctaves(3)
                .build();
              
 You can see the predefined Scales like this:
 
 	for (String scalename : ScaleFactory.getScaleNames()) {
            logger.debug(scalename);
        }
        
Or just take a peek at src/main/resources/scaledefs.xml. You can add more **Scale**s here. Be nice and send me any Scales I've missed.

### Chords

I have Chord classes also in my legacy library. I'll refactor them RSN.
		
### GUI

There ia JavaFX 2 project that will let you play around with the note text format. I have a metric ****ton of Swing that uses these classes. I'm porting them to JavaFX now.
	
### There is a lot more to document

There's more to the text format. And there is a metric ****load of documentation to write about all the other classes.
Be patient; I'm getting there.

In the meantime, look at the unit tests for examples, and check out the Factory classes for hints on how to use the classes.


## Resources

- [Blog post for this project](http://rockhoppertech.com/blog/rockymusic)



