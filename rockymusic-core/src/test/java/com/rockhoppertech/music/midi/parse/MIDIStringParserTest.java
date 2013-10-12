/**
 * 
 */
package com.rockhoppertech.music.midi.parse;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static com.rockhoppertech.music.Pitch.*;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDIStringParserTest {
    private MIDIStringParser parser;

    /*
     *  +    The plus sign indicates that there is one or more
     *  ?    The question mark indicates there is zero or one 
     *  *   The asterisk indicates there are zero or more 
     *  
     *  so, any whitespace followed by one or more pc specifiers (C, Ef, F#) then one or more octave chars
     *  (e.g. octave 10 is two chars)
     */
    static Pattern fullPitchPattern = Pattern.compile("\\s*[A-Ga-gsS#]+[0-9]+");
    static Pattern shortPitchPattern = Pattern.compile("\\s*[A-Ga-gsS#]+\\s*");

    
    public static void main(String[] args) {
        MIDIStringParser parser = new MIDIStringParser();
        parser.parsePitch("C4");
        System.err.println();
        parser.parsePitch("C#3");
        
        parser.parsePitch("Cs3");
        parser.parsePitch("Cf3");
        parser.parsePitch("Cb3");
        parser.parsePitch("Cbb3");
        parser.parsePitch("Cx3");
        parser.parsePitch("Crap");
        
        
    }
    // tis is just to figure out the regexp. moved it to the actual parser.
    public static void oldmain(String[] args) {
        MIDINote n = null;
        Scanner scanner = new Scanner("C4, C, e, f6, Cf3, F#3, J");
        scanner.useDelimiter(",");

        String p = null;
        while (scanner.hasNext()) {
            if (scanner.hasNext(fullPitchPattern)) {
                p = scanner.next();
                System.err.println("full pitch " + p);
            } else if (scanner.hasNext(shortPitchPattern)) {
                p = scanner.next();
                System.err.println("short pitch " + p);
            } else {
                p = scanner.next();
                System.err.println("junk " + p);
            }

            double start = 1d;
            if (scanner.hasNextDouble()) {
                start = scanner.nextDouble();
            }

            double duration = 1d;
            if (scanner.hasNextDouble()) {
                duration = scanner.nextDouble();
            }

            n = new MIDINote(p, start, duration);
            int oct = n.getPitch().getMidiNumber() / 12;
            System.err.println(oct);
        }

    }

    @Before
    public void setUp() {
        this.parser = new MIDIStringParser();
    }

    @Test
    public void parseNotesWithRunningOctave() {
        String s;
        MIDITrack list;
        MIDINote n;

        s = "C, C6, D, E, F4, E";
        list = this.parser.parseString(s);
        assertNotNull(list);
        assertThat("list size is correct",
                   list.size(),
                   equalTo(6));

        n = list.get(0);
        assertThat("note is not null",
                   n,
                   notNullValue());
        assertThat("pitch is correct",
                   n.getMidiNumber(),
                   equalTo(C5));

        n = list.get(1);
        assertThat("note is not null",
                   n,
                   notNullValue());
        assertEquals(C6,
                     n.getMidiNumber());

        n = list.get(2);
        assertThat("note is not null",
                   n,
                   notNullValue());
        assertEquals(D6,
                     n.getMidiNumber());

        n = list.get(3);
        assertThat("note is not null",
                   n,
                   notNullValue());
        assertEquals(E6,
                     n.getMidiNumber());

        n = list.get(4);
        assertThat("note is not null",
                   n,
                   notNullValue());
        assertEquals(F4,
                     n.getMidiNumber());

        n = list.get(5);
        assertThat("note is not null",
                   n,
                   notNullValue());
        assertEquals(E4,
                     n.getMidiNumber());

    }

    @Test
    public void parseNoteWithOctave() {
        String s;
        MIDINote n;

        s = "C10";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(120,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C#10";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(121,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Db10";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(121,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C9";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(108,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C8";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(96,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C7";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(84,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C6";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(72,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C5";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(60,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C4";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(48,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C3";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(36,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C2";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(24,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C1";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(12,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);
    }

    private void checkNoteDefaults(MIDINote n) {
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);
        assertEquals(2d,
                     n.getEndBeat(),
                     0d);
        assertEquals(64,
                     n.getVelocity());
        assertEquals(0,
                     n.getBank());
        assertEquals(0,
                     n.getProgram());
        assertEquals(0,
                     n.getPitchBend());
        assertEquals(0,
                     n.getVoice());
        assertFalse(n.isRest());
    }

    private void checkNoteNonTimeDefaults(MIDINote n) {
        assertEquals(64,
                     n.getVelocity());
        assertEquals(0,
                     n.getBank());
        assertEquals(0,
                     n.getProgram());
        assertEquals(0,
                     n.getPitchBend());
        assertEquals(0,
                     n.getVoice());
        assertFalse(n.isRest());
    }

    /**
     * Given just the note name. No octave, no start, dur etc. Test method for
     * {@link com.rockhoppertech.music.midi.parse.MIDIStringParser#parseNote(java.lang.String)}
     * .
     */
    @Test
    public void parseNoteDefault() {
        String s;
        MIDINote n;

        // default oct is 5
        s = "CF";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(59,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        // the previous parse set the running octave to 4
        this.parser.setRunningOctave(5);
        s = "Cb";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(59,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        this.parser.setRunningOctave(5);
        s = "C";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(60,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C#";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(61,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "DF";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(61,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Df";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(61,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Db";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(61,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "D";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(62,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "D#";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(63,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "EF";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(63,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Ef";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(63,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Eb";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(63,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "E";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(64,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "F";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(65,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "F#";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(66,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "GF";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(66,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Gf";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(66,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Gb";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(66,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "G";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(67,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "G#";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(68,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "AF";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(68,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Af";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(68,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Ab";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(68,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "A";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(69,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "A#";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(70,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "BF";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(70,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Bf";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(70,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "Bb";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(70,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "B";
        n = this.parser.parseNote(s);
        assertNotNull(n);
        assertEquals(71,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void expectBadNote() {
        String s;
        MIDINote n;
        s = "Z";
        n = this.parser.parseNote(s);
    }

    @Test
    public void parseStringDefaults() {
        String s;
        MIDITrack list;
        MIDINote n;

        s = "C D";
        list = this.parser.parseString(s);

        assertThat("The list is not null.", list, notNullValue());
        assertThat("the track size is correct", list.size().intValue(),
				equalTo(2));
        //assertThat(actual, is(equalTo(expected)));

        n = list.get(0);
        assertNotNull(n);
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(1);
        assertNotNull(n);
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        s = "C C# DF Db D D# Ef Eb E F F# Gb Gf G G# Af Ab A A# Bb B";
        list = this.parser.parseString(s);
        assertThat("The list is not null.", list, notNullValue());
        assertThat("the track size is correct", list.size().intValue(),
				equalTo(21));
        //assertThat(actual, is(equalTo(expected)));

        n = list.get(0);
        assertNotNull(n);
        assertEquals(60,
                     n.getMidiNumber());
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(1);
        assertNotNull(n);
        assertEquals(61,
                     n.getMidiNumber());
        assertEquals(Pitch.CS5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(2);
        assertNotNull(n);
        assertEquals(61,
                     n.getMidiNumber());
        assertEquals(Pitch.DF5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(3);
        assertNotNull(n);
        assertEquals(61,
                     n.getMidiNumber());
        assertEquals(Pitch.DF5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(4);
        assertNotNull(n);
        assertEquals(62,
                     n.getMidiNumber());
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        // D#
        n = list.get(5);
        assertNotNull(n);
        assertEquals(63,
                     n.getMidiNumber());
        assertEquals(Pitch.DS5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(6);
        assertNotNull(n);
        assertEquals(63,
                     n.getMidiNumber());
        assertEquals(Pitch.EF5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(7);
        assertNotNull(n);
        assertEquals(63,
                     n.getMidiNumber());
        assertEquals(Pitch.EF5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(8);
        assertNotNull(n);
        assertEquals(64,
                     n.getMidiNumber());
        assertEquals(Pitch.E5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(9);
        assertNotNull(n);
        assertEquals(65,
                     n.getMidiNumber());
        assertEquals(Pitch.F5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(10);
        assertNotNull(n);
        assertEquals(66,
                     n.getMidiNumber());
        assertEquals(Pitch.FS5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(11);
        assertNotNull(n);
        assertEquals(66,
                     n.getMidiNumber());
        assertEquals(Pitch.GF5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(12);
        assertNotNull(n);
        assertEquals(66,
                     n.getMidiNumber());
        assertEquals(Pitch.GF5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(13);
        assertNotNull(n);
        assertEquals(67,
                     n.getMidiNumber());
        assertEquals(Pitch.G5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(14);
        assertNotNull(n);
        assertEquals(68,
                     n.getMidiNumber());
        assertEquals(Pitch.GS5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(15);
        assertNotNull(n);
        assertEquals(68,
                     n.getMidiNumber());
        assertEquals(Pitch.AF5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(16);
        assertNotNull(n);
        assertEquals(68,
                     n.getMidiNumber());
        assertEquals(Pitch.AF5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(17);
        assertNotNull(n);
        assertEquals(69,
                     n.getMidiNumber());
        assertEquals(Pitch.A5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(18);
        assertNotNull(n);
        assertEquals(70,
                     n.getMidiNumber());
        assertEquals(Pitch.AS5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(19);
        assertNotNull(n);
        assertEquals(70,
                     n.getMidiNumber());
        assertEquals(Pitch.BF5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

        n = list.get(20);
        assertNotNull(n);
        assertEquals(71,
                     n.getMidiNumber());
        assertEquals(Pitch.B5,
                     n.getMidiNumber());
        this.checkNoteDefaults(n);

    }

    @Test
    public void parseStringWithStartBeat() {
        String s;
        MIDITrack list;
        MIDINote n;

        s = "C,1 D,2";
        list = this.parser.parseString(s);
        assertNotNull(list);
        assertEquals(2,
                     list.size().intValue());

        n = list.get(0);
        assertNotNull(n);
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);
        assertEquals(2d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        n = list.get(1);
        assertNotNull(n);
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(2d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);
        assertEquals(3d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        s = "C#,1 Df,2";
        list = this.parser.parseString(s);
        assertNotNull(list);
        assertEquals(2,
                     list.size().intValue());

        n = list.get(0);
        assertNotNull(n);
        assertEquals(Pitch.CS5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);
        assertEquals(2d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        n = list.get(1);
        assertNotNull(n);
        assertEquals(Pitch.DF5,
                     n.getMidiNumber());
        assertEquals(2d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);
        assertEquals(3d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

    }

    @Test
    public void parseStringWithStartBeatAndDurationAsString() {
        String s;
        MIDITrack list;
        MIDINote n;

        s = "C,1,h D,2,q.";
        list = this.parser.parseString(s);
        assertNotNull(list);
        assertEquals(2,
                     list.size().intValue());

        n = list.get(0);
        assertNotNull(n);
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(2d,
                     n.getDuration(),
                     0d);
        assertEquals(3d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        n = list.get(1);
        assertNotNull(n);
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(2d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1.5d,
                     n.getDuration(),
                     0d);
        assertEquals(3.5d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);
    }

    @Test
    public void parseStringWithStartBeatAndDuration() {
        String s;
        MIDITrack list;
        MIDINote n;

        s = "C,1,2 D,2,2.5";
        list = this.parser.parseString(s);

        assertThat("The list is not null.", list, notNullValue());
        assertThat("the track size is correct", list.size().intValue(),
				equalTo(2));
        //assertThat(actual, is(equalTo(expected)));

        n = list.get(0);
        
        assertThat("The note is not null.", n, notNullValue());
        assertThat("the pitch is correct", n.getMidiNumber(),
				equalTo(Pitch.C5));
        assertThat("the startbeat is correct", n.getStartBeat(),
				equalTo(1d));
        assertThat("the duration is correct", n.getDuration(),
				equalTo(2d));
        
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(2d,
                     n.getDuration(),
                     0d);
        assertEquals(3d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        n = list.get(1);
        assertThat("The note is not null.", n, notNullValue());
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(2d,
                     n.getStartBeat(),
                     0d);
        assertEquals(2.5d,
                     n.getDuration(),
                     0d);
        assertEquals(4.5d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        // whitespace delimits notes
        // so, D,1, 2 would not be a note! The 2 would be seen as a pitch

        s = "C#,1,3    Df,2.1,2.5";
        list = this.parser.parseString(s);
        assertNotNull(list);
        assertEquals(2,
                     list.size().intValue());

        n = list.get(0);
        assertThat("The note is not null.", n, notNullValue());
        assertEquals(Pitch.CS5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(3d,
                     n.getDuration(),
                     0d);
        assertEquals(4d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        n = list.get(1);
        assertThat("The note is not null.", n, notNullValue());
        assertEquals(Pitch.DF5,
                     n.getMidiNumber());
        assertEquals(2.1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(2.5d,
                     n.getDuration(),
                     0d);
        assertEquals(4.6d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

    }

    @Test
    public void parseStringWithStartBeatAndRunningDur() {
        String s;
        MIDITrack list;
        MIDINote n;

        s = "R2.5 C,1 D,2";
        list = this.parser.parseString(s);
        assertNotNull(list);
        assertEquals(2,
                     list.size().intValue());

        n = list.get(0);
        assertThat("The note is not null.", n, notNullValue());
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(2.5d,
                     n.getDuration(),
                     0d);
        assertEquals(3.5d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        n = list.get(1);
        assertThat("The note is not null.", n, notNullValue());
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(2d,
                     n.getStartBeat(),
                     0d);
        assertEquals(2.5d,
                     n.getDuration(),
                     0d);
        assertEquals(4.5d,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        s = "R.5 C,1 D,2 R1.5 G5 D";
        list = this.parser.parseString(s);
        
    	assertThat("The list is not null.", list, notNullValue());
        assertThat("the track size is correct", list.size().intValue(),
				equalTo(4));
        //assertThat(actual, is(equalTo(expected)));

        
        n = list.get(0);
        assertThat("The note is not null.", n, notNullValue());
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(.5,
                     n.getDuration(),
                     0d);
        assertEquals(1.5,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        n = list.get(1);
        assertThat("The note is not null.", n, notNullValue());
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(2d,
                     n.getStartBeat(),
                     0d);
        assertEquals(.5,
                     n.getDuration(),
                     0d);
        assertEquals(2.5,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        n = list.get(2);
        assertThat("The note is not null.", n, notNullValue());
        assertEquals(Pitch.G5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1.5,
                     n.getDuration(),
                     0d);
        assertEquals(2.5,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);

        n = list.get(3);
        assertThat("The note is not null.", n, notNullValue());
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1.5,
                     n.getDuration(),
                     0d);
        assertEquals(2.5,
                     n.getEndBeat(),
                     0d);
        this.checkNoteNonTimeDefaults(n);
    }

    @Test
    public void parseStringWithPSDV() {
        String s;
        MIDITrack list;
        MIDINote n;
        String pitch = "C5";
        double start = 1.5;
        double duration = 3.2;
        int velocity = 99;
        s = String.format("%s,%f,%f,%d",
                          pitch,
                          start,
                          duration,
                          velocity);
        System.err.println(s);
        list = this.parser.parseString(s);
        
        assertThat("list is not null",
                   list,
                   notNullValue());
        System.err.println(list);
        n = list.get(0);
        assertThat("pitch",
                   n.getPitch(),
                   equalTo(PitchFactory.getPitchByName(pitch)));
        assertThat("start",
                   n.getStartBeat(),
                   equalTo(start));
        assertThat("duration",
                   n.getDuration(),
                   equalTo(duration));
        assertThat("velocity",
                   n.getVelocity(),
                   equalTo(velocity));
       // list.play();
    }

    @Test
    public void parseStringWithAllNoteParams() {
        String s;
        MIDITrack list;
        MIDINote n;
        String pitch = "C5";
        double start = 1.5;
        double duration = 3.2;
        int velocity = 99;
        int pan = 0;
        int channel = 0;
        int bank = 0;
        int program = MIDIGMPatch.BANJO.getProgram();
        int pitchBend = 0;
        s = String.format("%s,%f,%f,%d,%d,%d,%d,%d,%d",
                          pitch,
                          start,
                          duration,
                          velocity,
                          pan,
                          channel,
                          bank,
                          program,
                          pitchBend);
        System.err.println(s);
        list = this.parser.parseString(s);
        assertThat("list is not null",
                   list,
                   notNullValue());
        System.err.println(list);

        n = list.get(0);
        assertThat("start",
                   n.getStartBeat(),
                   equalTo(start));
        assertThat("duration",
                   n.getDuration(),
                   equalTo(duration));
        assertThat("velocity",
                   n.getVelocity(),
                   equalTo(velocity));
        assertThat("pan",
                   n.getPan(),
                   equalTo(pan));
        assertThat("channel",
                   n.getChannel(),
                   equalTo(channel));
        assertThat("bank",
                   n.getBank(),
                   equalTo(bank));
        assertThat("program",
                   n.getProgram(),
                   equalTo(program));
        assertThat("pitchBend",
                   n.getPitchBend(),
                   equalTo(pitchBend));
        //list.play();

    }

    @Test
    public void parseStringWithProgramChange() {
        String s;
        MIDITrack list;
        MIDINote n;

        s = String.format("I\"%s\" R2.5 C,1 I\"%s\" D,2",
                          MIDIGMPatch.PIANO.getName(),
                          MIDIGMPatch.BRASS.getName());
        System.err.println(s);

        list = this.parser.parseString(s);

        assertThat("The list is not null.", list, notNullValue());
        assertThat("the track size is correct", list.size().intValue(),
				equalTo(2));
        //assertThat(actual, is(equalTo(expected)));

        n = list.get(0);
        assertNotNull(n);
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(2.5d,
                     n.getDuration(),
                     0d);
        assertEquals(3.5d,
                     n.getEndBeat(),
                     0d);

        assertEquals(MIDIGMPatch.PIANO.getProgram(),
                     n.getProgram());

        // assertEquals(MIDIUtils.getPatchNumber(MIDIUtils.PIANO_PATCH), n
        // .getProgram());

        n = list.get(1);
        assertNotNull(n);
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(2d,
                     n.getStartBeat(),
                     0d);
        assertEquals(2.5d,
                     n.getDuration(),
                     0d);
        assertEquals(4.5d,
                     n.getEndBeat(),
                     0d);
        // assertEquals(MIDIUtils.getPatchNumber(MIDIUtils.BRASS_PATCH), n
        // .getProgram());
        assertEquals(MIDIGMPatch.BRASS.getProgram(),
                     n.getProgram());

        s = String.format("I%d R2.5 C,1 I%d D,2",
                          MIDIGMPatch.PIANO.getProgram(),
                          MIDIGMPatch.BRASS.getProgram());

        // s = String.format("I%d R2.5 c,1 I%d d,2", MIDIUtils
        // .getPatchNumber(MIDIUtils.PIANO_PATCH), MIDIUtils
        // .getPatchNumber(MIDIUtils.BRASS_PATCH));

        list = this.parser.parseString(s);
        assertThat("The list is not null.", list, notNullValue());
        assertThat("the track size is correct", list.size().intValue(),
				equalTo(2));
        //assertThat(actual, is(equalTo(expected)));

        n = list.get(0);
        assertNotNull(n);
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(2.5d,
                     n.getDuration(),
                     0d);
        assertEquals(3.5d,
                     n.getEndBeat(),
                     0d);
        // assertEquals(MIDIUtils.getPatchNumber(MIDIUtils.PIANO_PATCH), n
        // .getProgram());
        assertEquals(MIDIGMPatch.PIANO.getProgram(),
                     n.getProgram());
        n = list.get(1);
        assertNotNull(n);
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(2d,
                     n.getStartBeat(),
                     0d);
        assertEquals(2.5d,
                     n.getDuration(),
                     0d);
        assertEquals(4.5d,
                     n.getEndBeat(),
                     0d);
        // assertEquals(MIDIUtils.getPatchNumber(MIDIUtils.BRASS_PATCH), n
        // .getProgram());
        assertEquals(MIDIGMPatch.BRASS.getProgram(),
                     n.getProgram());

    }

    @Test
    public void parseStringAppendMode() {
        String s;
        MIDITrack list;
        MIDINote n;

        s = "S+ C D E";

        list = this.parser.parseString(s);
        System.err.println(list);
        assertThat("The list is not null.", list, notNullValue());
        assertThat("the track size is correct", list.size().intValue(),
				equalTo(3));
        //assertThat(actual, is(equalTo(expected)));

        n = list.get(0);
        assertNotNull(n);
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);

        n = list.get(1);
        assertNotNull(n);
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(2d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);

        n = list.get(2);
        assertNotNull(n);
        assertEquals(Pitch.E5,
                     n.getMidiNumber());
        assertEquals(3d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);
    }

    @Test
    public void parseStringAddMode() {
        String s;
        MIDITrack list;
        MIDINote n;

        s = "S= C D E";

        list = this.parser.parseString(s);
        System.err.println(list);
        assertThat("The list is not null.", list, notNullValue());
        assertThat("the track size is correct", list.size().intValue(),
				equalTo(3));
        //assertThat(actual, is(equalTo(expected)));

        n = list.get(0);
        assertNotNull(n);
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);

        n = list.get(1);
        assertNotNull(n);
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);

        n = list.get(2);
        assertNotNull(n);
        assertEquals(Pitch.E5,
                     n.getMidiNumber());
        assertEquals(1d,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);

        s = "S=4.5 C D E";
        list = this.parser.parseString(s);
        System.err.println(list);
        assertThat("The list is not null.", list, notNullValue());
        assertThat("the track size is correct", list.size().intValue(),
				equalTo(3));
        //assertThat(actual, is(equalTo(expected)));

        n = list.get(0);
        assertNotNull(n);
        assertEquals(Pitch.C5,
                     n.getMidiNumber());
        assertEquals(4.5,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);

        n = list.get(1);
        assertNotNull(n);
        assertEquals(Pitch.D5,
                     n.getMidiNumber());
        assertEquals(4.5,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);

        n = list.get(2);
        assertNotNull(n);
        assertEquals(Pitch.E5,
                     n.getMidiNumber());
        assertEquals(4.5,
                     n.getStartBeat(),
                     0d);
        assertEquals(1d,
                     n.getDuration(),
                     0d);
    }
}
