Rocky (Rockhopper) Music
================================

A set of classes for producing music with JavaSound.

This project is explained in my [blog post](http://rockhoppertech.com/blog/rockymusic).

History
------

I started writing music code way back in the early 1980s on a PDP-11/34 running Music-11 under Unix.
I moved to C++ and Objective-C for my dissertation, since I had a NeXT to play my noises. I moved many of these 
classes to Java in early 1996, and when JavaSound was released, I added many classes to deal with it. The main problem
with the JavaSound classes was that they were (intentionally) low level. 
Musicians don't think note on then note off; they think 
middle c at beat 3 played softly. 
So I needed a way to turn the MIDI stream of events into Notes and back again to a MIDI stream to 
save to a midifile or play in real time.

Maven
------
The pom.xml requires Maven 3.

The site plugin with several reporting plugins is also configured. Take a look at src/site.

>   mvn site


Resources
----------
- [blog post for this project](http://rockhoppertech.com/blog/rockymusic)
- [Maven](http://maven.apache.org)



