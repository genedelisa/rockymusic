# Rockhopper (rocky) Music


A set of classes for producing music with JavaSound.

The maven generated site for this project is on github pages.
[http://genedelisa.github.io/rockymusic/](http://genedelisa.github.io/rockymusic/)

Besides the usual developer informtion, there are a few pages on how to use the main classes at that site. More is coming.

## History


I started writing music code way back in the early 1980s on a PDP-11/34 running Music-11 under Unix.

In 1989 I wrote  a book entited [Computer Music in C](http://www.amazon.com/gp/product/1574411160/ref=as_li_ss_tl?ie=UTF8&camp=1789&creative=390957&creativeASIN=1574411160&linkCode=as2&tag=httpwwwrockhc-20) with Phil Winsor that dealt with algorithmic composition. It was published by McGraw Hill's Windcrest label, then reprinted by the University of North Texas Press. Not surprisingly, it was written in C. I gave a talk on algorithmic composition a the 1986 International Computer Music Conference too. I've been at this for a while.

I moved to C++ and Objective-C for my dissertation, since I had a NeXT to play my noises. I moved many of these 
classes to Java in early 1996, and when JavaSound was released, I added many classes to deal with it. The main problem
with the JavaSound classes was that they were (intentionally) low level. 
Musicians don't think note on then note off; they think 
middle c at beat 3 played softly. 
So I needed a way to turn the MIDI stream of events into Notes and back again to a MIDI stream to 
save to a midifile or play in real time.

I'm in the process of extracting "core" clases from my library that anyone can use. The problem is, since my classes were written for my use in whatever piece I was cranking out, I mixed in "personal" classes that would probably be of no use to anyone else. There was also a naming problem for some classes. My original classes were for CSound, a NED Synclavier, or simply MIDI. It's safe to assume that the Synclavier code is of limited use at this point - but you will still see variable names like "notelist" which I'll update real soon now. So, I'm still in the process of refactoring. It's an on-going project of sifting through older classes and adapting them. If you look at the code, you will see chunks that are commented out. I'm adding the missing parts in stages.

## Another music library for Java?

Why would I write yet another library for music in Java? There are several others out there.

Yeah, I know; the nerve of those guys! 

Seriously though, they didn't exist when this started. And even now after looking at them, I don't really like the assumptions they make. Maybe they don't like the assumptions I make. No problem. There's room for everyone.


## Where to go now

Read the Quick Start at the project site. [http://genedelisa.github.io/rockymusic/quickstart.html](http://genedelisa.github.io/rockymusic/quickstart.html/) 

I'm also I'm creating an AsciiDoc book right now. I'll post that soon.

