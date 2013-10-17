# Rocky (Rockhopper) Music


A set of classes for producing music with JavaSound.

This project is explained in my [Blog post](http://rockhoppertech.com/blog/rockymusic).

## History


I started writing music code way back in the early 1980s on a PDP-11/34 running Music-11 under Unix.
I moved to C++ and Objective-C for my dissertation, since I had a NeXT to play my noises. I moved many of these 
classes to Java in early 1996, and when JavaSound was released, I added many classes to deal with it. The main problem
with the JavaSound classes was that they were (intentionally) low level. 
Musicians don't think note on then note off; they think 
middle c at beat 3 played softly. 
So I needed a way to turn the MIDI stream of events into Notes and back again to a MIDI stream to 
save to a midifile or play in real time.

I'm in the process of extracting "core" clases from my library that anyone can use. The problem is, since my classes were written for my use in whatever piece I was cranking out, I mixed in "personal" classes that would probably be of no use to anyone else. There was also a naming problem for some classes. My original classes were for CSound, a NED Synclavier, or simply MIDI. It's safe to assume that the Synclavier code is of limited use at this point. So, I'm in the process of refactoring.

## Another music library for Java?

Why would I write yet another library for music in Java? There are several others out there.

Yeah, I know; the nerve of those guys! Seriously though, they didn't exist when this started. And even now after looking at them, I don't really like the assumptions they make. Maybe they don't like the assumptions I make. No problem.

## Main classes

The Score class contains **MIDITrack**s. **MIDITrack**s can contain either **MIDINote**s or **MIDIEvent**s. One of the attributes of the **MIDINote** class is a **Pitch** object. There are constants defined for each MIDI pitch number. 

> `I chose to use C5 as middle C - even though other libraries such as Common Music use C4 to represent middle C. If I did that, then the first octaves would have negative numbers. Ugly.`

The **MIDITrack**'s **add** method is overloaded. You can simply pass in a **Pitch** constant and it will create a MIDINote with default attributes. There is a builder for MIDINote if you want to control the universe.


## Simple Example

Here is a Score with a track that contains just middle C:

	Score score = new Score();
	MIDITrack track = new MIDITrack();
	score.add(track);
	track.add(Pitch.C5);
	// then use it
	ScoreFactory.writeToMIDIFile("test.mid"", score);
	// or
	score.play();
	// or even
	track.play();


## Text based note specification

You can also use a text based format for creating a **MIDITrack**. There is a constructor that uses a **MIDIStringParser** to initialize the track.

	MIDITrack track = new MIDITrack("C5 D E F G A B C6");

This creates a track with the specified pitches, but all of the start times are at beat 1. In some cases this is what you want. You can modify the track by calling

	track.sequential();

or you can specify sequential start times in the string by prepending "S+" to the string.

	MIDITrack track = new MIDITrack("S+ C5 D E F G A B C6");

You can specify a start beat with S=N, where N is the beat. This will be a "running"	start time which will apply to all pitches until the next S.

	S+ C D E S=5 E S=8 G
	
No spaces around the =!

### Comments

Multi line comments are not supported yet. But you can
use single line comments using either // or /* */ on a singe line

	C D E // a couple notes
	C /* blah blah */ D E
	

### Durations

You can specify durations as a real number of beats

	C,1 D,1.5 E,2.0

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

	X3 (C)
	yields
	C C C
	
	X3 (C D) E F
	yields
	C D C D C D E F
	
	X2 (C D) E F X2 (G A)
	yields
	C D C D E F G A G A

## Where to go now

Read this: TODO insert something to read :)


## Resources

- [Blog post for this project](http://rockhoppertech.com/blog/rockymusic)



