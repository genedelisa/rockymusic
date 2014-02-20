# Rockhopper Music FX

These are just a few examples of using Rockhopper Music with JavaFX. I have a metric ****ton of Swing apps that use Rockhopper Music and have not ported them all to JavaFX yet. In progress.

## Common Music Notation

I'm not interested in building a better Sibelius or Finale - or even Lilypond or MuseScore. But I would like a simple view of the results of some algorithmic generator as common music notation. Nothing fancy. Just a quick viewer. Not Sibelius.

Around 1990, I wrote a few classes on the NeXT that used the Sonata font. So I ported that work to Swing and Java2D in the late 90s. The Sonata font is a bit long in the tooth, and the licensing is not friendly. Since I use Sibelius, I tried using the Opus font. That works, but, once again, licensing.

Enter Steinberg's [Bravura](http://www.smufl.org/fonts/) font designed by Daniel Spreadbury. It uses the 
[Open Font License](http://scripts.sil.org/cms/scripts/page.php?site_id=nrsi&id=ofl). 

Here is a tl;dr.
> Bravura is made available under the SIL Open Font License, which means that the font is free to download, use, embed, redistribute with other software (including commercial software) or to create derivative versions. The only restrictions on its use are that it cannot be sold on its own, any derivative versions cannot use the reserved font name “Bravura”, and any derivative versions must likewise also be licensed under the SIL Open Font License.

I'm not a lawyer, but to me, that says I can use the font for my toys. If I'm wrong, please let me know.

There is a Bravura derivative by [Andrew Moschou](http://www.amoschou.net/) named Taneyev in case you're interested.

### Design

Right now the Staff is a JavaFX Region, and the font glyphs are Text objects. I tried using a Canvas and drawing them using the graphic context. Lacking font metrics, that was awkward. You can ask a Text node for its width. The downside is that the scene graph will have lots of Text nodes. But once again, not Sibelius.

The MIDITrack class doesn't know about JavaFX properties, but it does fire plain old B flat JavaBean properties. It's not a very good fit since the FX adapter classes are awkward. So, the StaffModel creates an ObservableList from the MIDITrack.

The Bravura font provides JSON metadata files. I've had to add the JSON parser as a maven dependency.

JavaFX currently complains about loading custom fonts. You will see this complaint after you exit the program. I just ignore it.
You will see this:

> Assertion failed: (t->numPointers == 0), function tsi_DeleteMemhandler, file src/tsimem.c, line 89.


I'm undecided on whether to keep the Staff as a Region, or to turn it into a Control. The Behavior classes will still be private in JavaFX 8, and there are improvements to the Skin interface. For now, I'm using the Region and will worry about this later.

### Drawing classes

The **Staff** and **GrandStaff** classes are fairly straightforward. Simply create in instance in code of in fxml. Then give it a **MIDITrack**, and tell it to draw. These classes do not pay attention to start beats in the **MIDINotes**. So, if you have a chord, it won't be "stacked". Once again, not Sibelius.

The **MeasureCanvas** (actually a Region) attempts to map start beats to calcuated x positions. The first cut sort of works. Rests don't work yet for example.