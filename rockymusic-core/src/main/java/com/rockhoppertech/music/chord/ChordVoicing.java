package com.rockhoppertech.music.chord;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * 
 * @author Gene De Lisa
 * 
 */
public class ChordVoicing {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordVoicing.class);
    private int octave = 0;
    private String voicing;

    public ChordVoicing() {
        this.voicing = "1 3 5";
    }

    public ChordVoicing(int octave, String s) {
        this.octave = octave;
        this.voicing = s;
    }

    public ChordVoicing(String s) {
        this.voicing = s;
    }

    public MIDITrack getNoteList(int octave, Chord c) {
        Chord cl = (Chord) c.clone();
        int root = c.getRoot();
        int pc = root % 12;
        int n = octave * 12 + pc;
        cl.setRoot(n);
        return parse(this.voicing, cl);
    }

    public MIDITrack getTrack(Chord c) {
        Chord cl = (Chord) c.clone();
        int root = c.getRoot();
        int pc = root % 12;
        int n = this.octave * 12 + pc;
        cl.setRoot(n);
        logger.debug("new root for chord is {} ", n);
        MIDITrack nl = ChordVoicing.parse(this.voicing, cl);
        logger.debug("track chord is {}", nl);
        return nl;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }

    public String getVoicing() {
        return voicing;
    }

    public void setVoicing(String voicing) {
        this.voicing = voicing;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("octave =").append(this.octave);
        sb.append(" voicing =").append(this.voicing);
        return sb.toString();
    }

    /**
     * The input voicing is something like 1 3 5 7 9 11 13. You can add a series
     * of + or - characters to jump octaves.
     * 
     * <pre>
     * MIDITrack nl = ChordVoicing.parse(&quot;1 5 ++3&quot;, c);
     * </pre>
     * 
     * If you give an invalid voicing it just complains. e.g. "1 3 5 7" for a
     * triad. The seventh will be ignored.
     * 
     * 
     * @param s
     *            a Voicing String
     * @param c
     *            a Chord instance
     * @return a MIDITrack
     * @see MIDITrack
     */
    private static MIDITrack parse(String s, Chord c) {
        int octaveOffset = 0;
        boolean repeatedRoot = false;
        boolean repeatedThird = false;
        boolean repeatedFifth = false;
        boolean repeatedSeventh = false;
        boolean repeatedNinth = false;
        boolean repeatedEleventh = false;
        boolean repeatedThirteenth = false;

        MIDITrack track = new MIDITrack();
        Scanner scanner = new Scanner(s.trim());
        while (scanner.hasNext()) {
            octaveOffset = 0;
            String d = scanner.next().trim();
            while (d.indexOf('+') != -1) {
                octaveOffset++;
                d = d.substring(1);
            }
            while (d.indexOf('-') != -1) {
                octaveOffset--;
                d = d.substring(1);
            }

            int i = Integer.parseInt(d);
            int n = 0;
            switch (i) {
            case 1:
                n = c.getRoot();
                // if the voicing is something like: 1 3 5 1 3 5 the root is
                // repeated and the oct
                // needs to be bumped up.
                if (repeatedRoot) {
                    octaveOffset++;
                    repeatedRoot = false;
                }
                addMIDINote(octaveOffset, track, n, c);
                repeatedRoot = true;
                break;
            case 3:
                n = c.getThird();
                // what about 1 3 3 5?
                // nah, would be best to make the exception for 1
                // and make the others specify the jump.
                // so, 1 3 + 3 5

                // if (repeatedThird) {
                // octaveOffset++;
                // }
                addMIDINote(octaveOffset, track, n, c);
                repeatedThird = true;
                break;
            case 5:
                n = c.getFifth();
                addMIDINote(octaveOffset, track, n, c);
                repeatedFifth = true;
                break;
            case 7:
                if (c.hasSeventh()) {
                    n = c.getSeventh();
                    addMIDINote(octaveOffset, track, n, c);
                    repeatedSeventh = true;
                } else {
                    System.err.printf("%s does not have a %d%n", c
                            .getDisplayName(), i);
                    logger.error(String.format("%s does not have a %d%n", c
                            .getDisplayName(), i));
                }
                break;
            case 9:
                if (c.hasNinth()) {
                    n = c.getNinth();
                    addMIDINote(octaveOffset, track, n, c);
                    repeatedNinth = true;
                } else {
                    System.err.printf("%s does not have a %d%n", c
                            .getDisplayName(), i);
                    logger.error(String.format("%s does not have a %d%n", c
                            .getDisplayName(), i));
                }
                break;
            case 11:
                if (c.hasEleventh()) {
                    n = c.getEleventh();
                    addMIDINote(octaveOffset, track, n, c);
                    repeatedEleventh = true;
                } else {
                    System.err.printf("%s does not have a %d%n", c
                            .getDisplayName(), i);
                    logger.error(String.format("%s does not have a %d%n", c
                            .getDisplayName(), i));
                }
                break;
            case 13:
                if (c.hasThirteenth()) {
                    n = c.getThirteenth();
                    addMIDINote(octaveOffset, track, n, c);
                    repeatedThirteenth = true;
                } else {
                    System.err.printf("%s does not have a %d%n", c
                            .getDisplayName(), i);
                    logger.error(String.format("%s does not have a %d%n", c
                            .getDisplayName(), i));
                }
                break;
                default :
                    logger.debug("Don't know {}", i);
                    break;
            }
        }
        scanner.close();

        return track;
    }

    private static void addMIDINote(int octaveOffset, MIDITrack notelist,
            int n, Chord chord) {
        logger.debug(String.format("adding note %d with offset %d%n", n,
                octaveOffset));
        if (octaveOffset > 0) {
            while (octaveOffset-- > 0) {
                n += 12;
            }
        } else if (octaveOffset < 0) {
            while (octaveOffset++ < 0) {
                n -= 12;
            }
        }
        MIDINote mn = new MIDINote(n, chord.getStartBeat(), chord.getDuration());
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "pitch %d created and added midi note %s", n, mn));
        }
        notelist.add(mn);
    }

    public int getNumberOfInversions(Chord chord) {
        String[] sa = chord.getDefaultVoicingString().split("\\s");
        return sa.length;
    }

    // drop 2 is drop the second highest pitch an octave
    public static String getDropString(Chord chord, int drop) {
        StringBuilder sb = new StringBuilder();
        MIDITrack notelist = chord.createMIDITrack();
        notelist.sortByAscendingPitches();
        int size = notelist.size();
        int index = 0;
        if (drop > 0)
            index = size - drop;
        else
            index = 0;
        MIDINote n = notelist.get(index);
        logger.debug("Dropping " + n);
        if (n.getMidiNumber() >= 12) {
            n.setPitch(n.getPitch().transpose(-12));
        } else {
            logger.error("Too low to drop " + n);
        }

        for (MIDINote note : notelist) {
            if (note == n && index != 0) {
                sb.append("-");
            }
            int mn = note.getMidiNumber();
            int degree = chord.pitchToChordDegree(mn);
            logger.debug(String.format("chord %s mn %d degree %d", chord,
                    mn, degree));
            sb.append(degree).append(' ');
        }

        return sb.toString().trim();
    }

    /**
     * pretty much closed position.
     * 
     * @param chord the chord
     * @param inversion the inversion
     * @return a String with the inversion
     */
    public String getInversion(Chord chord, int inversion) {
        StringBuilder sb = new StringBuilder();
        String[] sa = chord.getDefaultVoicingString().split("\\s");
        // String[] sa = this.voicing.split("\\s");
        String[] dest = new String[sa.length];

       // int croot = chord.getRoot();
        // int coct = croot / 12;
        // logger.debug(String.format("croot %d coct is %d", croot, coct));

        // rotate
        System.arraycopy(sa, inversion, dest, 0, sa.length - inversion);
        System.arraycopy(sa, 0, dest, sa.length - inversion, inversion);

        String firstPitchString = dest[0];
        if (firstPitchString.indexOf('+') != -1) {
            firstPitchString = firstPitchString.substring(firstPitchString
                    .lastIndexOf('+') + 1);
        }
        if (firstPitchString.indexOf('-') != -1) {
            firstPitchString = firstPitchString.substring(firstPitchString
                    .lastIndexOf('-') + 1);
        }
        int firstPitch = chord.getChordDegree(Integer
                .parseInt(firstPitchString));
        logger.debug(String.format("first pitch is %d", firstPitch));
        // firstPitch = croot;
        logger.debug(String.format("first pitch is %d", firstPitch));
        // int firstPitchOct = firstPitch / 12;
        logger.debug(String.format("%d is the first pitch", firstPitch));
        for (String s : dest) {
            if (s.indexOf('+') != -1) {
                s = s.substring(s.lastIndexOf('+') + 1);
            }
            if (s.indexOf('-') != -1) {
                s = s.substring(s.lastIndexOf('-') + 1);
            }
            int i = Integer.parseInt(s);
            int p = chord.getChordDegree(i);
            while (p < firstPitch && p < Pitch.MAX) {
                logger.debug(String.format("%d bumped up an octave to %d, %d",
                        p, p + 12, firstPitch));
                p += 12;
                sb.append('+');

            }
            logger.debug(String.format("%d is the pitch degree for %s", p, s));
            //
            // int nPlus = (p /12) - coct;
            // logger.debug(String.format("root oct %d nplus is %d", (p /12),
            // nPlus));

            sb.append(s).append(' ');
        }

        if (logger.isDebugEnabled()) {
            String s = String.format("inversion %d is this voicing %s",
                    inversion, sb.toString());
            logger.debug(s);
        }
        return sb.toString();
    }

    public void setInversion(Chord chord, int inversion) {
        String inv = this.getInversion(chord, inversion);
        this.octave = chord.getRoot() / 12;
        this.voicing = inv;
    }

    public static void main(String[] args) {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj9");
        } catch (UnknownChordException e) {
            e.printStackTrace();
            return;
        }
        c.setRoot(Pitch.C3);
        String cv = c.getChordVoicing();
        System.out.println(c);
        System.out.println(cv);
    }

    static void foo() {
        Chord c = null;
        try {
            c = ChordFactory.getChordByFullSymbol("Dm9");
        } catch (UnknownChordException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(c.getIntervalstoString());
        // so you get 3 7. relative to the root.
        MIDITrack notelist = null;
        int octave = 3;
        ChordVoicing cv = null;

        cv = new ChordVoicing(octave, "1 +3 5 7 9");
        notelist = cv.getNoteList(octave, c);
        System.out.println(notelist);
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj9");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        cv = new ChordVoicing(octave, "1 +3 5 7 9");
        notelist = cv.getNoteList(octave, c);
        System.out.println(notelist);

        try {
            c = ChordFactory.getChordByFullSymbol("C");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        c.setChordVoicing("1 +3 5");
        notelist = c.createMIDITrack();
        System.out.println(notelist);
        /*
         * or this turnaround C4 Bb4 E5 G5 A5 C6 A3 G4 Cs5 F5 Bb5 D4 A4 C5 E5 A5
         * G3 F4 B4 Eb5 Af5 C4 B4 D5 E5 G5
         */

        MIDITrack prog = new MIDITrack();
        try {
            c = ChordFactory.getChordByFullSymbol("C13");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        c.setDuration(2);
        c.setChordVoicing("1 7 +3 5 1");
        prog.append(c.createMIDITrack());
        System.out.println(c.createMIDITrack());
        try {
            c = ChordFactory.getChordByFullSymbol("A7-9+5");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        c.setDuration(2);
        c.setChordVoicing("1 7 +3 5 9");
        System.out.println(c.createMIDITrack());
        prog.append(c.createMIDITrack());
        try {
            c = ChordFactory.getChordByFullSymbol("D9");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        c.setDuration(2);
        c.setChordVoicing("1 5 7 9 +5"); // the + 5 since
        // the 5 and the
        // 9 are in same
        // oct
        System.out.println(c.createMIDITrack());
        prog.append(c.createMIDITrack());
        try {
            c = ChordFactory.getChordByFullSymbol("g7-9+5");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        c.setDuration(2);
        c.setChordVoicing("1 +7 3 5 9");
        System.out.println(c.createMIDITrack());
        prog.append(c.createMIDITrack());
        try {
            c = ChordFactory.getChordByFullSymbol("Cmaj9");
        } catch (UnknownChordException e) {
            e.printStackTrace();
        }
        c.setDuration(4);
        c.setChordVoicing("1 7 9 3 5");
        System.out.println(c.createMIDITrack());
        prog.append(c.createMIDITrack());

        prog.play();

        // c.setRoot(Pitch.D3);
        // notelist = parse("1 +3 5 7 9", c);
        //
        // c.setRoot(Pitch.D3);
        // notelist = parse("1 3 5 7 9", c);
        //
        // c.setRoot(Pitch.D3);
        // notelist = parse("1 3 ++5 +7 9", c);

        // ChordVoicing cv = new ChordVoicing(Pitch.C3, new int[] { 1 });

        /*
         * capture these: D3 F4 A4 C5 E5 G3 F4 A4 B4 E5 C3 E4 G4 B4 E5
         * 
         * so that's root, 3 5 7 9 root, 3 up an octave, 5 7 9 "R +3 5 7 9"
         * where + means skip an octave then the rest in order
         * 
         * 
         * 
         * 
         * for cmaj9 C3 B4 D5 E5 G5 dm9 D3 C5 E5 F5 A5 g13 G5 B4 E5 F5 A5
         * 
         * 
         * Key ii (tritone) V (tritone) I VI (tritone) C Dm7 (Abm7) G7 (Db7) C
         * A7alt (Eb7alt )
         */
    }

    public String getDisplayName() {
        return this.voicing.trim();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + octave;
        result = prime * result + ((voicing == null) ? 0 : voicing.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChordVoicing other = (ChordVoicing) obj;
        if (octave != other.octave) {
            return false;
        }
        if (voicing == null) {
            if (other.voicing != null)
                return false;
        } else if (!voicing.equals(other.voicing)) {
            return false;
        }
        return true;
    }

}
