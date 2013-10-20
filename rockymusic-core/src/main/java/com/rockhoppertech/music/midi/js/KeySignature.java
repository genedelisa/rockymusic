/**
 * 
 */
package com.rockhoppertech.music.midi.js;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Track;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;

/**
 * Data follows MIDI file format for key signatures.
 * 
 * <pre>
 *   FF 59 02 sf mi
 * </pre>
 * 
 * sf = -7 for 7 flats, -1 for 1 flat, etc, 0 for key of c, 1 for 1 sharp, etc.
 * mi = 0 for major, 1 for minor
 * 
 * It's serializable in order to drag and drop notelists.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
@SuppressWarnings("serial")
public class KeySignature implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -7040093210129130798L;

    private static final Logger logger = LoggerFactory
            .getLogger(KeySignature.class);

    static final Map<String, KeySignature> names;
    static final Map<Integer, KeySignature> major;
    static final Map<Integer, KeySignature> minor;

    static final Map<Integer, KeySignature> majorSharp;
    static final Map<Integer, KeySignature> minorSharp;
    static final Map<Integer, KeySignature> majorFlat;
    static final Map<Integer, KeySignature> minorFlat;

    // cs and df major are different ks so the mapkey cannot be a pc
    static final Map<String, KeySignature> majorRoots;
    static final Map<String, KeySignature> minorRoots;

    static {
        names = new HashMap<String, KeySignature>();
        major = new TreeMap<Integer, KeySignature>();
        minor = new TreeMap<Integer, KeySignature>();

        majorSharp = new TreeMap<Integer, KeySignature>();
        minorSharp = new TreeMap<Integer, KeySignature>();

        majorFlat = new TreeMap<Integer, KeySignature>();
        minorFlat = new TreeMap<Integer, KeySignature>();

        majorRoots = new TreeMap<String, KeySignature>();
        minorRoots = new TreeMap<String, KeySignature>();
    }

    public static final KeySignature CMAJOR = new KeySignature(0, 0);
    public static final KeySignature CMINOR = new KeySignature(1, -3);
    public static final KeySignature CSMAJOR = new KeySignature(0, 7);
    public static final KeySignature CSMINOR = new KeySignature(1, 4);
    public static final KeySignature DFMAJOR = new KeySignature(0, -5);
    public static final KeySignature DMAJOR = new KeySignature(0, 2);
    public static final KeySignature DMINOR = new KeySignature(1, -1);
    public static final KeySignature EFMAJOR = new KeySignature(0, -3);
    public static final KeySignature EFMINOR = new KeySignature(1, -6);
    public static final KeySignature EMAJOR = new KeySignature(0, 4);
    public static final KeySignature EMINOR = new KeySignature(1, 1);
    public static final KeySignature FMAJOR = new KeySignature(0, -1);
    public static final KeySignature FMINOR = new KeySignature(1, -4);
    public static final KeySignature FSMAJOR = new KeySignature(0, 6);
    public static final KeySignature FSMINOR = new KeySignature(1, 3);
    public static final KeySignature GFMAJOR = new KeySignature(0, -6);
    public static final KeySignature GMAJOR = new KeySignature(0, 1);
    public static final KeySignature GMINOR = new KeySignature(1, -2);
    public static final KeySignature GSMINOR = new KeySignature(1, 5);
    public static final KeySignature AFMAJOR = new KeySignature(0, -4);
    public static final KeySignature AFMINOR = new KeySignature(1, -7);
    public static final KeySignature AMAJOR = new KeySignature(0, 3);
    public static final KeySignature AMINOR = new KeySignature(1, 0);
    public static final KeySignature ASMINOR = new KeySignature(1, 7);
    public static final KeySignature BFMAJOR = new KeySignature(0, -2);
    public static final KeySignature BFMINOR = new KeySignature(1, -5);
    public static final KeySignature BMAJOR = new KeySignature(0, 5);
    public static final KeySignature BMINOR = new KeySignature(1, 2);
    public static final int MAJOR = 0;
    public static final int MINOR = 1;
    // private Scale scale;
    private String root;

    // mm = 0 for major, 1 for minor
    private byte mm;
    // number of sharps or flats. flats < 0, Sharps > 0
    private byte sf;
    private String displayName;

    /**
     * @param majorMinor 0 for major, 1 for minor
     * @param sf number of sharps or flats. flats < 0, Sharps > 0
     */
    private KeySignature(int majorMinor, int sf) {
        mm = (byte) majorMinor;
        this.sf = (byte) sf;
        displayName = getKeySignatureName(mm, this.sf);
        KeySignature.names.put(displayName, this);
        // TODO this needs to be the name as a key, i.e. cs and df
        root = getKeySignatureRootName(mm, this.sf);
        root = root.trim();

        // String s = String.format("root '%s'for %d %d", this.root,
        // this.mm, this.sf);
        // logger.debug(s);

        if (MAJOR == mm) {
            majorRoots.put(root, this);
            KeySignature.major.put(sf, this);
            if (sf > 0) {
                KeySignature.majorSharp.put(sf, this);
            } else {
                KeySignature.majorFlat.put(sf, this);
            }
        } else {
            minorRoots.put(root, this);
            KeySignature.minor.put(sf, this);
            if (sf > 0) {
                KeySignature.minorSharp.put(sf, this);
            } else {
                KeySignature.minorFlat.put(sf, this);
            }
        }
    }

    /**
     * get(Pitch.C0, KeySignature.MAJOR)
     * 
     * @param r
     * @param mm
     * @return
     */
    public static KeySignature get(String root, int mm) {

        // Pitch p = PitchFactory.getPitchByName(root);
        // int pc = p.getMidiNumber() % 12;
        // if (logger.isDebugEnabled()) {
        // logger.debug("root pc is " + pc);
        // logger.debug("root pitch is " + p);
        // }
        KeySignature ks = null;
        if (mm == MAJOR) {
            ks = majorRoots.get(root);
            logger.debug("ks {}", ks);
        } else {
            ks = minorRoots.get(root);
        }
        return ks;
    }

    public boolean isFlatKey() {
        boolean isFlat = false;
        if (mm == MAJOR) {
            isFlat = majorFlat.containsValue(this);
        } else {
            isFlat = minorFlat.containsValue(this);
        }
        return isFlat;
    }

    public boolean isSharpKey() {
        boolean isSharp = false;
        if (mm == MAJOR) {
            isSharp = majorSharp.containsValue(this);
        } else {
            isSharp = minorSharp.containsValue(this);
        }
        return isSharp;
    }

    public static Map<Integer, KeySignature> getMajorKeys() {
        return Collections.unmodifiableMap(major);
    }

    public static Map<Integer, KeySignature> getMinorKeys() {
        return Collections.unmodifiableMap(minor);
    }

    public static Map<Integer, KeySignature> getMajorFlatKeys() {
        return Collections.unmodifiableMap(majorFlat);
    }

    public static Map<Integer, KeySignature> getMinorFlatKeys() {
        return Collections.unmodifiableMap(minorFlat);
    }

    public static Map<Integer, KeySignature> getMajorSharpKeys() {
        return Collections.unmodifiableMap(majorSharp);
    }

    public static Map<Integer, KeySignature> getMinorSharpKeys() {
        return Collections.unmodifiableMap(minorSharp);
    }

    public static void dumpNames() {
        for (String s : names.keySet()) {
            System.out.println(s);
        }
    }

    public static Set<String> getNames() {
        Set<String> n = new TreeSet<String>();
        for (String s : names.keySet()) {
            n.add(s);
        }
        return n;
    }

    public static Set<String> getMajorNames() {
        Set<String> n = new TreeSet<String>();
        for (Integer key : major.keySet()) {
            n.add(minor.get(key).getDisplayName());
        }
        return n;
    }

    public static Set<String> getMinorNames() {
        Set<String> n = new TreeSet<String>();
        for (Integer key : minor.keySet()) {
            n.add(minor.get(key).getDisplayName());
        }
        return n;
    }

    public boolean isMajor() {
        return mm == 0;
    }

    public boolean isMinor() {
        return mm == 1;
    }

    public static KeySignature getByName(String name) {
        return KeySignature.names.get(name);
    }

    public static KeySignature getMajorByNFlats(int nFlats) {
        nFlats *= -1;
        KeySignature ks = null;
        for (Integer n : major.keySet()) {
            String s = String.format("testing %d with %d", n, nFlats);
            System.out.println(s);
            if (n.equals(nFlats)) {
                ks = major.get(n);
                break;
            }
        }
        return ks;
    }

    public static KeySignature getMajorByNSharps(int nSharps) {
        KeySignature ks = null;
        for (Integer n : majorSharp.keySet()) {
            String s = String.format("testing %d with %d", n, nSharps);
            System.out.println(s);
            if (n.equals(nSharps)) {
                ks = majorSharp.get(n);
                break;
            }
        }
        return ks;
    }

    public static KeySignature getMinorByNSharps(int nSharps) {
        KeySignature ks = null;
        for (Integer n : minorSharp.keySet()) {
            String s = String.format("testing %d with %d", n, nSharps);
            System.out.println(s);
            if (n.equals(nSharps)) {
                ks = minorSharp.get(n);
                break;
            }
        }
        return ks;
    }

    public static KeySignature getMinorByNFlats(int nFlats) {
        nFlats *= -1;
        KeySignature ks = null;
        for (Integer n : minorFlat.keySet()) {
            String s = String.format("testing %d with %d", n, nFlats);
            System.out.println(s);
            if (n.equals(nFlats)) {
                ks = minorFlat.get(n);
                break;
            }
        }
        return ks;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append(" displayName=").append(displayName);
        sb.append(" mm=").append(mm);
        sb.append(" sf=").append(sf);
        return sb.toString();
    }

    public void addToTrack(Track track, long tick) {
        MetaMessage mm = getMIDIKeySignature();
        MidiEvent event = new MidiEvent(mm, tick);
        track.add(event);
    }

    public MetaMessage getMIDIKeySignature() {
        MetaMessage message = null;
        try {
            message = new MetaMessage();
            byte[] a = new byte[2];
            a[0] = sf;
            a[1] = mm;
            message.setMessage(0x59, a, a.length);
        } catch (InvalidMidiDataException e) {
            System.err.println(e);
        }
        return message;
    }

    public static KeySignature getKeySignatureFromMIDI(MetaMessage event) {
        byte[] message = event.getData();
        KeySignature ks = new KeySignature(message[1], message[0]);
        return ks;
    }

    public static String getKeySignatureName(MetaMessage event) {
        byte[] message = event.getData();
        return getKeySignatureName(message[1], message[0]);
    }

    public static String getKeySignatureName(int mm, int sf) {
        boolean sharps = false;
        boolean major = false;

        if (sf < 0) {
            sharps = false;
        } else if (sf > 0) {
            sharps = true;
        }

        if (1 == mm) {
            major = false;
        } else {
            major = true;
        }
        String[] sharpKeys = { "C", "G", "D", "A", "E", "B", "F#", "C#" };
        String[] flatKeys = { "C", "F", "Bb", "Eb", "Ab", "Db", "Gb", "Cb" };
        String[] sharpMinorKeys = { "A", "E", "B", "F#", "C#", "G#", "D#", "A#" };
        String[] flatMinorKeys = { "A", "D", "G", "C", "F", "Bb", "Eb", "Ab" };
        int num = Math.abs(sf);
        String str = "";

        if (major) {
            if (sharps) {
                str += sharpKeys[num];
            } else {
                str += flatKeys[num];
            }
            str += " major";

        } else {

            if (sharps) {
                str += sharpMinorKeys[num];
            } else {
                str += flatMinorKeys[num];
            }
            str += " minor";
        }
        return str;
    }

    public static Pitch getKeySignatureRoot(int mm, int sf) {
        boolean sharps = false;
        boolean major = false;

        if (sf < 0) {
            sharps = false;
        } else if (sf > 0) {
            sharps = true;
        }

        if (1 == mm) {
            major = false;
        } else {
            major = true;
        }
        String[] sharpKeys = { "C", "G", "D", "A", "E", "B", "F#", "C#" };
        String[] flatKeys = { "C", "F", "Bb", "Eb", "Ab", "Db", "Gb", "Cb" };
        String[] sharpMinorKeys = { "A", "E", "B", "F#", "C#", "G#", "D#", "A#" };
        String[] flatMinorKeys = { "A", "D", "G", "C", "F", "Bb", "Eb", "Ab" };

        Pitch p = null;
        int num = Math.abs(sf);
        String str = null;
        if (major) {
            if (sharps) {
                // p = PitchFactory.getPitch(sharpKeys[num] + 0);
                p = PitchFactory.getPitchByName(sharpKeys[num]);
            } else {
                str = flatKeys[num] + 0;
                System.out.println(str);
                p = PitchFactory.getPitchByName(flatKeys[num]);
            }

        } else {
            if (sharps) {
                p = PitchFactory.getPitchByName(sharpMinorKeys[num]);
            } else {
                p = PitchFactory.getPitchByName(flatMinorKeys[num]);
            }
        }
        return p;
    }

    public static String getKeySignatureRootName(int mm, int sf) {
        boolean sharps = false;
        boolean major = false;

        if (sf < 0) {
            sharps = false;
        } else if (sf > 0) {
            sharps = true;
        }

        if (1 == mm) {
            major = false;
        } else {
            major = true;
        }
        String[] sharpKeys = { "C", "G", "D", "A", "E", "B", "F#", "C#" };
        String[] flatKeys = { "C", "F", "Bb", "Eb", "Ab", "Db", "Gb", "Cb" };
        String[] sharpMinorKeys = { "A", "E", "B", "F#", "C#", "G#", "D#", "A#" };
        String[] flatMinorKeys = { "A", "D", "G", "C", "F", "Bb", "Eb", "Ab" };

        // Pitch p = null;
        int num = Math.abs(sf);
        String str = null;
        if (major) {
            if (sharps) {
                str = sharpKeys[num];
            } else {
                str = flatKeys[num];
            }
        } else {
            if (sharps) {
                str = sharpMinorKeys[num];
            } else {
                str = flatMinorKeys[num];
            }
        }
        return str;
    }

    public static String getKeySignatureDetailedName(int mm, int sf) {
        boolean sharps = false;
        boolean major = false;

        if (sf < 0) {
            sharps = false;
        } else if (sf > 0) {
            sharps = true;
        }

        if (1 == mm) {
            major = false;
        } else {
            major = true;
        }
        String[] sharpKeys = { "C", "G", "D", "A", "E", "B", "F#", "C#" };
        String[] flatKeys = { "C", "F", "Bb", "Eb", "Ab", "Db", "Gb", "Cb" };
        String[] sharpMinorKeys = { "A", "E", "B", "F#", "C#", "G#", "D#", "A#" };
        String[] flatMinorKeys = { "A", "D", "G", "C", "F", "Bb", "Eb", "Ab" };
        int num = Math.abs(sf);
        String str = " " + num;
        if (sharps) {
            str += " sharps ";
        } else {
            str += " flats ";
        }

        if (major) {
            str += " major ";
            if (sharps) {
                str += sharpKeys[num];
            } else {
                str += flatKeys[num];
            }

        } else {
            str += " minor ";
            if (sharps) {
                str += sharpMinorKeys[num];
            } else {
                str += flatMinorKeys[num];
            }
        }
        return str;
    }

    /**
     * mm = 0 for major, 1 for minor
     * 
     * @return the mm
     */
    public byte getMm() {
        return mm;
    }

    /**
     * number of sharps or flats. flats < 0, Sharps > 0
     * 
     * @return the sf
     */
    public byte getSf() {
        return sf;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    // private boolean needSharps(int noteNum) {
    // int pc = noteNum % 12;
    // switch (this.sf) {
    // case -7:
    // break;
    // // f
    // case -1:
    // break;
    // }
    //
    // return true;
    // }

    public Pitch getRoot() {
        return KeySignature.getKeySignatureRoot(mm, sf);
    }

    // TODO implement
    public KeySignature getRelativeMinor() {
        KeySignature ks = null;
        if (mm == MINOR) {
            throw new WTFException(
                    "What is the relative minor of a minor key skeezix?");
        }
        // Pitch root = this.getRoot();
        // Pitch rm = root.transpose(-3);

        if (sf < 0) {

        }

        return ks;
    }

    // TODO implement
    public KeySignature getRelativeMajor() {
        KeySignature ks = null;
        if (mm == MAJOR) {
            throw new WTFException(
                    "What is the relative major of a major key, skeezix?");
        }
        return ks;
    }

    // public Scale getScale() {
    // if (this.scale == null) {
    // // Pitch p = KeySignature.getKeySignatureRoot(this.mm, this.sf);
    // if (this.mm == 0) {
    // scale = ScaleFactory.getScaleByName("Major");
    // } else {
    // scale = ScaleFactory.getScaleByName("Harmonic Minor");
    // }
    // }
    // scale.setKey(getKeySignatureRootName(this.mm, this.sf));
    // return this.scale;
    // }
    //
    // public boolean isDiatonic(int p) {
    // Scale s = this.getScale();
    // return s.isDiatonic(p);
    // }
}
