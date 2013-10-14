package com.rockhoppertech.music;



import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> 
 * A representation of musical frequency (Pitch).
 * <p>
 * <p>
 * Internally represented by a midi number. The frequency is calculated based on
 * the MIDI number.
 * </p>
 * 
 * See http://en.wikipedia.org/wiki/Scientific_pitch_notation
 * 
 * For MusicXML, Common Lisp Music and others the octave is "off" by 1. In MIDI
 * middle c is C5, for them it is C4.
 * 
 * <p>
 * For equal temperament you should use the PitchFactory to obtain instances a
 * la the GoF Flyweight design pattern. (Most likely you will have zillions of
 * instances of Pitch in your program. The Flyweight helps this problem).
 * </p>
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version 1.0 Mon Aug 10, 1998
 * @see com.rockhoppertech.music.PitchFactory
 * @see com.rockhoppertech.music.PitchFormat
 */

public class Pitch implements Serializable, Comparable<Pitch> {
    /**
     * 
     */
    private static final long serialVersionUID = -4983186242989608159L;
    private static final Logger logger = LoggerFactory
			.getLogger(Pitch.class);

    /**
     * The main datum.
     */
    private int midiNumber;

    private String preferredSpelling;

    private short cents;
    /**
     * Equal temperament frequency for the midiNumber
     */
    private double frequency;

    /**
     * 0 if not microtonal
     */
    private short pitchBend;

    /**
     * middle c or 261.6255653005986 hz Used to calculate the frequency.
     */
    public static final double REF_PITCH = 220d * Math.pow(2d,
                                                           (3d / 12d));

    /**
     * <p>
     * Use the PitchFactory to get an instance instead. Use this only in special
     * circumstances
     * </p>
     * <p>
     * Midi number will be 0.
     * </p>
     */
    public Pitch() {
        this(0);
    }

    /**
     * <p>
     * Use the PitchFactory to get an instance instead. Use this only in special
     * circumstances
     * </p>
     * <p>
     * Can use a constant.
     * <p>
     * 
     * <code><pre>
     * new Pitch(Pitch.C5);
     * </pre></code>
     * 
     * @param n
     *            the midi number to use. 60 is middle c.
     */
    public Pitch(int n) {
        if (n < 0 || n > 127) {
            throw new IllegalArgumentException("MIDI number out of range " + n);
        }
        this.midiNumber = n;
        // this.frequency = Pitch.midiFq(this.midiNumber);
        this.frequency = PitchFactory.EQUAL_TEMPERAMENT.get(this.midiNumber);
    }

    /**
     * <p>
     * Use the PitchFactory to get an instance instead. Use this only in special
     * circumstances
     * </p>
     * <p>
     * Parses a string representation of the pitch using PitchFormat. Also sets
     * the frequency.
     * </p>
     * 
     * @link com.rockhoppertech.music.PitchFormat
     * @param s
     *            String the pitch string like c#5
     */

    public Pitch(String s) {
        this.midiNumber = PitchFormat.stringToMidiNumber(s);
        this.frequency = PitchFactory.EQUAL_TEMPERAMENT.get(this.midiNumber);
    }

    /**
     * @return the internal midi number
     */
    public int getMidiNumber() {
        return this.midiNumber;
    }

    /**
     * Sets the midi number and frequency.
     */
    public void setMidiNumber(int n) {
        this.midiNumber = n;
        this.frequency = PitchFactory.EQUAL_TEMPERAMENT.get(this.midiNumber);
    }

    /**
     * Describe <code>getFrequency</code> method here.
     * 
     * @return a <code>double</code> value
     */
    public double getFrequency() {
        return this.frequency;
    }

    /**
     * <p>
     * <code>transpose</code> pitch up or down by the specified number of minor
     * seconds (half steps). Can use constants.
     * </p>
     * <p>
     * This uses the PitchFactory to return the appropriate instance. The object
     * that is the receiver of this message is not affected in any way.
     * </p>
     * <code><pre>
     * p.transpose(Pitch.TRITONE);
     * p.transpose(Pitch.MAJOR_SEVENTH);
     * </pre></code>
     * 
     * @param minorSeconds
     *            The value added with the receiver's midinumber. Can be
     *            negative to transpose down.
     */
    public Pitch transpose(int minorSeconds) {
        int n = this.midiNumber + minorSeconds;
        Pitch p = PitchFactory.getPitch(n);
        logger.debug(String.format("transposed to %d", n));
        return p;
    }

    /*
     * <p>If you want to use a JSpinner list model and you don't want to write a
     * spinner editor, just return the pitch name here.
     * 
     * </p>
     * 
     * @see toProlixString() for a longer version
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return PitchFormat.getInstance().format(this);
    }

    public String toProlixString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pitch[");
        sb.append("midiNumber=").append(this.midiNumber);
        sb.append(" frequency=").append(this.frequency);
        sb.append(" cents=").append(this.cents);
        sb.append(" pitchBend=").append(this.pitchBend);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Check to see if the pitch is diatonic or not.
     * 
     * @param num
     *            The midi number
     * @return whether it is an accidental
     */
    public static boolean isAccidental(int num) {
        boolean r = false;
        switch (num % 12) {
        case 1:
        case 3:
        case 6:
        case 8:
        case 10:
            r = true;
            break;
        }
        return r;
    }
    
    public final static int REGEX_FLAGS = Pattern.CASE_INSENSITIVE
    | Pattern.UNICODE_CASE | Pattern.CANON_EQ;
    
    public static boolean isSharp(String pitchName) {
        String sharpNoteNameRegexp = "([A-G])(#|s|x)";
        Pattern pattern = Pattern.compile(sharpNoteNameRegexp,
                                          REGEX_FLAGS);
        Matcher match = pattern.matcher(pitchName);
        if (match.matches()) {
            System.err.println(pitchName + " matches");
            return true;
        }
       return false;
    }
    public static boolean isFlat(String pitchName) {
        String flatNoteNameRegexp = "([A-G])(-|b|f)";
        Pattern pattern = Pattern.compile(flatNoteNameRegexp,
                                          REGEX_FLAGS);
        Matcher match = pattern.matcher(pitchName);
        if (match.matches()) {
            System.err.println(pitchName + " matches");
            return true;
        }
       return false;
    }

    /**
     * <p>
     * Calculate frequency of a pitch class/octave pair.
     * </p>
     * 
     * @param pitch
     *            the pitch class
     * @param oct
     *            the octave. 6.0 is middle C's octave
     * @return the frequency
     * @exception none
     */
    public static double midiFq(double pitch, double oct) {
        return (REF_PITCH * Math.pow(2d,
                                     (oct - 5d)) * Math.pow(2d,
                                                            (pitch / 12d)));
    }

    /**
     * Calculate equal tempered frequency of a midi note. The PitchFactory
     * contains a cached table of Equal Temp. pitches. so use that instead of
     * this method.
     * 
     * <pre>
     * &lt;code&gt;frequency = PitchFactory.EQUAL_TEMPERAMENT.get(this.midiNumber);&lt;/code&gt;
     * </pre>
     * 
     * @param midiNoteNumber
     *            an <code>int</code> value
     * @return the frequency
     * @exception should
     *                throw out of range
     */
    public static double midiFq(int midiNoteNumber) {
        return (REF_PITCH * Math.pow(2d,
                                     ((midiNoteNumber / 12) - 5d)) * Math
                .pow(2d,
                     ((midiNoteNumber % 12) / 12d)));
    }

    /*
     * <p>Uses the MIDI number for natural ordering. </p>
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Pitch other) {

        int val = 0;
        if (this.getMidiNumber() == other.getMidiNumber()) {
            val = 0;
        } else if (this.getMidiNumber() < other.getMidiNumber()) {
            val = -1;
        } else {
            val = 1;
        }
        return val;
    }
    
    public enum P {
         C0 (0),
         CS0 (1),
         DF0 (1),
         D0 (2),
         DS0 (3),
         EF0 (3),
         E0 (4),
         F0 (5),
         FS0 (6),
         GF0 (6),
         G0 (7),
         GS0 (8),
         AF0 (8),
         A0 (9),
         AS0 (10),
         BF0 (10),
         B0 (11),

         C1 (12),
         CS1 (13),
         DF1 (13),
         D1 (14),
         DS1 (15),
         EF1 (15),
         E1 (16),
         F1 (17),
         FS1 (18),
         GF1 (18),
         G1 (19),
         GS1 (20),
         AF1 (20),
         A1 (21),
         AS1 (22),
         BF1 (22),
         B1 (23),

         C2 (24),
         CS2 (25),
         DF2 (25),
         D2 (26),
         DS2 (27),
         EF2 (27),
         E2 (28),
         F2 (29),
         FS2 (30),
         GF2 (30),
         G2 (31),
         GS2 (32),
         AF2 (32),
         A2 (33),
         AS2 (34),
         BF2 (34),
         B2 (35),

         C3 (36),
         CS3 (37),
         DF3 (37),
         D3 (38),
         DS3 (39),
         EF3 (39),
         E3 (40),
         F3 (41),
         FS3 (42),
         GF3 (42),
         G3 (43),
         GS3 (44),
         AF3 (44),
         A3 (45),
         AS3 (46),
         BF3 (46),
         B3 (47),

         C4 (48),
         CS4 (49),
         DF4 (49),
         D4 (50),
         DS4 (51),
         EF4 (51),
         E4 (52),
         F4 (53),
         FS4 (54),
         GF4 (54),
         G4 (55),
         GS4 (56),
         AF4 (56),
         A4 (57),
         AS4 (58),
         BF4 (58),
         B4 (59),

         C5 (60),
         MIDDLE_C (60),
         CS5 (61),
         DF5 (61),
         D5 (62),
         DS5 (63),
         EF5 (63),
         E5 (64),
         F5 (65),
         FS5 (66),
         GF5 (66),
         G5 (67),
         GS5 (68),
         AF5 (68),
         A5 (69),
         AS5 (70),
         BF5 (70),
         B5 (71),

         C6 (72),
         CS6 (73),
         DF6 (73),
         D6 (74),
         DS6 (75),
         EF6 (75),
         E6 (76),
         F6 (77),
         FS6 (78),
         GF6 (78),
         G6 (79),
         GS6 (80),
         AF6 (80),
         A6 (81),
         AS6 (82),
         BF6 (82),
         B6 (83),

         C7 (84),
         CS7 (85),
         DF7 (85),
         D7 (86),
         DS7 (87),
         EF7 (87),
         E7 (88),
         F7 (89),
         FS7 (90),
         GF7 (90),
         G7 (91),
         GS7 (92),
         AF7 (92),
         A7 (93),
         AS7 (94),
         BF7 (94),
         B7 (95),

         C8 (96),
         CS8 (97),
         DF8 (97),
         D8 (98),
         DS8 (99),
         EF8 (99),
         E8 (100),
         F8 (101),
         FS8 (102),
         GF8 (102),
         G8 (103),
         GS8 (104),
         AF8 (104),
         A8 (105),
         AS8 (106),
         BF8 (106),
         B8 (107),

         C9 (108),
         CS9 (109),
         DF9 (109),
         D9 (110),
         DS9 (111),
         EF9 (111),
         E9 (112),
         F9 (113),
         FS9 (114),
         GF9 (114),
         G9 (115),
         GS9 (116),
         AF9 (116),
         A9 (117),
         AS9 (118),
         BF9 (118),
         B9 (119),

         C10 (120),
         CS10 (121),
         DF10 (121),
         D10 (122),
         DS10 (123),
         EF10 (123),
         E10 (124),
         F10 (125),
         FS10 (126),
         GF10 (126),
         G10 (127);
        
        private int value;
        private P(int value) {
        	this.value = value;
        }
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		// all this nonsense just to change middle C from C5 to something else like C4 
		// TODO would be better if the Pitch class itself were an enum instad of class?
		private void plus(int amt) {
			this.value += amt;
		}
		
		public void add12() {
			 P[] a = Pitch.P.class.getEnumConstants();
			 for(P p: a) {
				 p.plus(12);
			 }
		}
		private void minus(int amt) {
			this.value -= amt;
		}
		
		public void sub12() {
			 P[] a = Pitch.P.class.getEnumConstants();
			 for(P p: a) {
				 p.minus(12);
			 }
		}
    }

    public static final int C0 = 0;
    public static final int CS0 = 1;
    public static final int DF0 = 1;
    public static final int D0 = 2;
    public static final int DS0 = 3;
    public static final int EF0 = 3;
    public static final int E0 = 4;
    public static final int F0 = 5;
    public static final int FS0 = 6;
    public static final int GF0 = 6;
    public static final int G0 = 7;
    public static final int GS0 = 8;
    public static final int AF0 = 8;
    public static final int A0 = 9;
    public static final int AS0 = 10;
    public static final int BF0 = 10;
    public static final int B0 = 11;

    public static final int C1 = 12;
    public static final int CS1 = 13;
    public static final int DF1 = 13;
    public static final int D1 = 14;
    public static final int DS1 = 15;
    public static final int EF1 = 15;
    public static final int E1 = 16;
    public static final int F1 = 17;
    public static final int FS1 = 18;
    public static final int GF1 = 18;
    public static final int G1 = 19;
    public static final int GS1 = 20;
    public static final int AF1 = 20;
    public static final int A1 = 21;
    public static final int AS1 = 22;
    public static final int BF1 = 22;
    public static final int B1 = 23;

    public static final int C2 = 24;
    public static final int CS2 = 25;
    public static final int DF2 = 25;
    public static final int D2 = 26;
    public static final int DS2 = 27;
    public static final int EF2 = 27;
    public static final int E2 = 28;
    public static final int F2 = 29;
    public static final int FS2 = 30;
    public static final int GF2 = 30;
    public static final int G2 = 31;
    public static final int GS2 = 32;
    public static final int AF2 = 32;
    public static final int A2 = 33;
    public static final int AS2 = 34;
    public static final int BF2 = 34;
    public static final int B2 = 35;

    public static final int C3 = 36;
    public static final int CS3 = 37;
    public static final int DF3 = 37;
    public static final int D3 = 38;
    public static final int DS3 = 39;
    public static final int EF3 = 39;
    public static final int E3 = 40;
    public static final int F3 = 41;
    public static final int FS3 = 42;
    public static final int GF3 = 42;
    public static final int G3 = 43;
    public static final int GS3 = 44;
    public static final int AF3 = 44;
    public static final int A3 = 45;
    public static final int AS3 = 46;
    public static final int BF3 = 46;
    public static final int B3 = 47;

    public static final int C4 = 48;
    public static final int CS4 = 49;
    public static final int DF4 = 49;
    public static final int D4 = 50;
    public static final int DS4 = 51;
    public static final int EF4 = 51;
    public static final int E4 = 52;
    public static final int F4 = 53;
    public static final int FS4 = 54;
    public static final int GF4 = 54;
    public static final int G4 = 55;
    public static final int GS4 = 56;
    public static final int AF4 = 56;
    public static final int A4 = 57;
    public static final int AS4 = 58;
    public static final int BF4 = 58;
    public static final int B4 = 59;

    public static final int C5 = 60;
    public static final int MIDDLE_C = 60;
    public static final int CS5 = 61;
    public static final int DF5 = 61;
    public static final int D5 = 62;
    public static final int DS5 = 63;
    public static final int EF5 = 63;
    public static final int E5 = 64;
    public static final int F5 = 65;
    public static final int FS5 = 66;
    public static final int GF5 = 66;
    public static final int G5 = 67;
    public static final int GS5 = 68;
    public static final int AF5 = 68;
    public static final int A5 = 69;
    public static final int AS5 = 70;
    public static final int BF5 = 70;
    public static final int B5 = 71;

    public static final int C6 = 72;
    public static final int CS6 = 73;
    public static final int DF6 = 73;
    public static final int D6 = 74;
    public static final int DS6 = 75;
    public static final int EF6 = 75;
    public static final int E6 = 76;
    public static final int F6 = 77;
    public static final int FS6 = 78;
    public static final int GF6 = 78;
    public static final int G6 = 79;
    public static final int GS6 = 80;
    public static final int AF6 = 80;
    public static final int A6 = 81;
    public static final int AS6 = 82;
    public static final int BF6 = 82;
    public static final int B6 = 83;

    public static final int C7 = 84;
    public static final int CS7 = 85;
    public static final int DF7 = 85;
    public static final int D7 = 86;
    public static final int DS7 = 87;
    public static final int EF7 = 87;
    public static final int E7 = 88;
    public static final int F7 = 89;
    public static final int FS7 = 90;
    public static final int GF7 = 90;
    public static final int G7 = 91;
    public static final int GS7 = 92;
    public static final int AF7 = 92;
    public static final int A7 = 93;
    public static final int AS7 = 94;
    public static final int BF7 = 94;
    public static final int B7 = 95;

    public static final int C8 = 96;
    public static final int CS8 = 97;
    public static final int DF8 = 97;
    public static final int D8 = 98;
    public static final int DS8 = 99;
    public static final int EF8 = 99;
    public static final int E8 = 100;
    public static final int F8 = 101;
    public static final int FS8 = 102;
    public static final int GF8 = 102;
    public static final int G8 = 103;
    public static final int GS8 = 104;
    public static final int AF8 = 104;
    public static final int A8 = 105;
    public static final int AS8 = 106;
    public static final int BF8 = 106;
    public static final int B8 = 107;

    public static final int C9 = 108;
    public static final int CS9 = 109;
    public static final int DF9 = 109;
    public static final int D9 = 110;
    public static final int DS9 = 111;
    public static final int EF9 = 111;
    public static final int E9 = 112;
    public static final int F9 = 113;
    public static final int FS9 = 114;
    public static final int GF9 = 114;
    public static final int G9 = 115;
    public static final int GS9 = 116;
    public static final int AF9 = 116;
    public static final int A9 = 117;
    public static final int AS9 = 118;
    public static final int BF9 = 118;
    public static final int B9 = 119;

    public static final int C10 = 120;
    public static final int CS10 = 121;
    public static final int DF10 = 121;
    public static final int D10 = 122;
    public static final int DS10 = 123;
    public static final int EF10 = 123;
    public static final int E10 = 124;
    public static final int F10 = 125;
    public static final int FS10 = 126;
    public static final int GF10 = 126;
    public static final int G10 = 127;

    public static final int MIN = C0;
    public static final int MAX = G10;

    public static final int MINOR_SECOND = 1;
    public static final int MAJOR_SECOND = 2;
    public static final int MINOR_THIRD = 3;
    public static final int MAJOR_THIRD = 4;
    public static final int PERFECT_FOURTH = 5;
    public static final int TRITONE = 6;
    public static final int PERFECT_FIFTH = 7;
    public static final int MINOR_SIXTH = 8;
    public static final int MAJOR_SIXTH = 9;
    public static final int MINOR_SEVENTH = 10;
    public static final int MAJOR_SEVENTH = 11;
    public static final int OCTAVE = 12;

    /*
     * <p>See Effective Java. </p>
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + this.midiNumber;
        return result;
    }

    /*
     * <p>Compares just MIDI number. </p>
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Pitch other = (Pitch) obj;
        if (this.midiNumber != other.midiNumber)
            return false;
        return true;
    }

    /**
     * @return the cents
     */
    public short getCents() {
        return this.cents;
    }

    /**
     * @param cents
     *            the cents to set
     */
    public void setCents(short cents) {
        this.cents = cents;
    }

    /**
     * @param frequency
     *            the frequency to set
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public short getPitchBend() {
        return this.pitchBend;
    }

    public void setPitchBend(short pitchBend) {
        this.pitchBend = pitchBend;
    }

    public int getPitchClass() {
        return this.midiNumber % 12;
    }

    public Set<String> getSynonyms() {
        return Pitch.getSynonyms(this.midiNumber);
    }

    /**
     * 
     * Here is a JUnit example. <code>
     * assertThat(somePitch.getSynonymsForPitchClass(), hasItem("Db"));
     * </code>
     * 
     * @return
     */
    public Set<String> getSynonymsForPitchClass() {
        return Pitch.getSynonyms(this.midiNumber % 12);
    }

    public static Set<String> getSynonyms(int midiNumber) {
        Set<String> syn = new TreeSet<String>();
        for (Map.Entry<String, Integer> entry : nameMap.entrySet()) {
            if (entry.getValue() == midiNumber) {
                // don't really feel like writing custom JUnit matchers
                String key = entry.getKey();
                syn.add(key);
                syn.add(key.toLowerCase());
                syn.add(key.toUpperCase());
                syn.add(StringUtils.capitalize(key.toLowerCase()));
            }
        }
        return syn;
    }

    /**
     * Matches either exact midi number or pitchclass.
     * 
     * @param midiNumber
     * @param pitchClass
     * @return
     */
    // public static List<String> getSynonyms(int midiNumber, int pitchClass) {
    // List<String> syn = new ArrayList<String>();
    // for (Map.Entry<String, Integer> entry : nameMap.entrySet()) {
    // if (entry.getValue() == midiNumber
    // || entry.getValue() == pitchClass) {
    // syn.add(entry.getKey());
    // syn.add(entry.getKey().toLowerCase());
    // syn.add(entry.getKey().toUpperCase());
    // }
    // }
    // return syn;
    // }



    // this is the fastest and easiest. a pain to set up but now its set up.
    public static final Map<String, Integer> nameMap = new TreeMap<String, Integer>();
    static {
        nameMap.put("C",
                    0);
        nameMap.put("CS",
                    1);
        nameMap.put("Cs",
                    1);
        nameMap.put("C#",
                    1);
        nameMap.put("DF",
                    1);
        nameMap.put("Df",
                    1);
        nameMap.put("DB",
                    1);
        nameMap.put("D",
                    2);
        nameMap.put("DS",
                    3);
        nameMap.put("Ds",
                    3);
        nameMap.put("D#",
                    3);
        nameMap.put("EF",
                    3);
        nameMap.put("Ef",
                    3);
        nameMap.put("EB",
                    3);
        nameMap.put("E",
                    4);
        nameMap.put("F",
                    5);
        nameMap.put("FS",
                    6);
        nameMap.put("Fs",
                    6);
        nameMap.put("F#",
                    6);
        nameMap.put("GF",
                    6);
        nameMap.put("Gf",
                    6);
        nameMap.put("GB",
                    6);
        nameMap.put("G",
                    7);
        nameMap.put("GS",
                    8);
        nameMap.put("Gs",
                    8);
        nameMap.put("G#",
                    8);
        nameMap.put("AF",
                    8);
        nameMap.put("Af",
                    8);
        nameMap.put("AB",
                    8);
        nameMap.put("A",
                    9);
        nameMap.put("AS",
                    10);
        nameMap.put("As",
                    10);
        nameMap.put("A#",
                    10);
        nameMap.put("BF",
                    10);
        nameMap.put("Bf",
                    10);
        nameMap.put("BB",
                    10);
        nameMap.put("B",
                    11);
        nameMap.put("CB",
                    11);
        nameMap.put("CF",
                    11);
        nameMap.put("Cf",
                    11);

        nameMap.put("C0",
                    0);
        nameMap.put("CS0",
                    1);
        nameMap.put("C#0",
                    1);
        nameMap.put("DF0",
                    1);
        nameMap.put("Df0",
                    1);
        nameMap.put("DB0",
                    1);
        nameMap.put("D0",
                    2);
        nameMap.put("DS0",
                    3);
        nameMap.put("D#0",
                    3);
        nameMap.put("EF0",
                    3);
        nameMap.put("Ef0",
                    3);
        nameMap.put("EB0",
                    3);
        nameMap.put("E0",
                    4);
        nameMap.put("F0",
                    5);
        nameMap.put("FS0",
                    6);
        nameMap.put("F#0",
                    6);
        nameMap.put("GF0",
                    6);
        nameMap.put("Gf0",
                    6);
        nameMap.put("GB0",
                    6);
        nameMap.put("G0",
                    7);
        nameMap.put("GS0",
                    8);
        nameMap.put("G#0",
                    8);
        nameMap.put("AF0",
                    8);
        nameMap.put("Af0",
                    8);
        nameMap.put("AB0",
                    8);
        nameMap.put("A0",
                    9);
        nameMap.put("AS0",
                    10);
        nameMap.put("A#0",
                    10);
        nameMap.put("BF0",
                    10);
        nameMap.put("Bf0",
                    10);
        nameMap.put("BB0",
                    10);
        nameMap.put("B0",
                    11);

        nameMap.put("CF1",
                    11);
        nameMap.put("Cf1",
                    11);
        nameMap.put("CB1",
                    11);
        nameMap.put("C1",
                    12);
        nameMap.put("CS1",
                    13);
        nameMap.put("C#1",
                    13);
        nameMap.put("DF1",
                    13);
        nameMap.put("DB1",
                    13);
        nameMap.put("D1",
                    14);
        nameMap.put("DS1",
                    15);
        nameMap.put("D#1",
                    15);
        nameMap.put("EF1",
                    15);
        nameMap.put("EB1",
                    15);
        nameMap.put("E1",
                    16);
        nameMap.put("F1",
                    17);
        nameMap.put("FS1",
                    18);
        nameMap.put("F#1",
                    18);
        nameMap.put("GF1",
                    18);
        nameMap.put("GB1",
                    18);
        nameMap.put("G1",
                    19);
        nameMap.put("GS1",
                    20);
        nameMap.put("G#1",
                    20);
        nameMap.put("AF1",
                    20);
        nameMap.put("AB1",
                    20);
        nameMap.put("A1",
                    21);
        nameMap.put("AS1",
                    22);
        nameMap.put("A#1",
                    22);
        nameMap.put("BF1",
                    22);
        nameMap.put("BB1",
                    22);
        nameMap.put("B1",
                    23);

        nameMap.put("CF2",
                    23);
        nameMap.put("Cf2",
                    23);
        nameMap.put("CB2",
                    23);
        nameMap.put("C2",
                    24);
        nameMap.put("CS2",
                    25);
        nameMap.put("C#2",
                    25);
        nameMap.put("DF2",
                    25);
        nameMap.put("DB2",
                    25);
        nameMap.put("D2",
                    26);
        nameMap.put("DS2",
                    27);
        nameMap.put("D#2",
                    27);
        nameMap.put("EF2",
                    27);
        nameMap.put("EB2",
                    27);
        nameMap.put("E2",
                    28);
        nameMap.put("F2",
                    29);
        nameMap.put("FS2",
                    30);
        nameMap.put("F#2",
                    30);
        nameMap.put("GF2",
                    30);
        nameMap.put("GB2",
                    30);
        nameMap.put("G2",
                    31);
        nameMap.put("GS2",
                    32);
        nameMap.put("G#2",
                    32);
        nameMap.put("AF2",
                    32);
        nameMap.put("AB2",
                    32);
        nameMap.put("A2",
                    33);
        nameMap.put("AS2",
                    34);
        nameMap.put("A#2",
                    34);
        nameMap.put("BF2",
                    34);
        nameMap.put("BB2",
                    34);
        nameMap.put("B2",
                    35);

        nameMap.put("CF3",
                    35);
        nameMap.put("Cf3",
                    35);
        nameMap.put("CB3",
                    35);
        nameMap.put("C3",
                    36);
        nameMap.put("CS3",
                    37);
        nameMap.put("C#3",
                    37);
        nameMap.put("DF3",
                    37);
        nameMap.put("DB3",
                    37);
        nameMap.put("D3",
                    38);
        nameMap.put("DS3",
                    39);
        nameMap.put("D#3",
                    39);
        nameMap.put("EF3",
                    39);
        nameMap.put("EB3",
                    39);
        nameMap.put("E3",
                    40);
        nameMap.put("F3",
                    41);
        nameMap.put("FS3",
                    42);
        nameMap.put("F#3",
                    42);
        nameMap.put("GF3",
                    42);
        nameMap.put("GB3",
                    42);
        nameMap.put("G3",
                    43);
        nameMap.put("GS3",
                    44);
        nameMap.put("G#3",
                    44);
        nameMap.put("AF3",
                    44);
        nameMap.put("AB3",
                    44);
        nameMap.put("A3",
                    45);
        nameMap.put("AS3",
                    46);
        nameMap.put("A#3",
                    46);
        nameMap.put("BF3",
                    46);
        nameMap.put("BB3",
                    46);
        nameMap.put("B3",
                    47);

        nameMap.put("CF4",
                    47);
        nameMap.put("Cf4",
                    47);
        nameMap.put("CB4",
                    47);
        nameMap.put("C4",
                    48);
        nameMap.put("CS4",
                    49);
        nameMap.put("C#4",
                    49);
        nameMap.put("DF4",
                    49);
        nameMap.put("Df4",
                    49);
        nameMap.put("DB4",
                    49);
        nameMap.put("D4",
                    50);
        nameMap.put("DS4",
                    51);
        nameMap.put("D#4",
                    51);
        nameMap.put("EF4",
                    51);
        nameMap.put("Ef4",
                    51);
        nameMap.put("EB4",
                    51);
        nameMap.put("E4",
                    52);
        nameMap.put("F4",
                    53);
        nameMap.put("FS4",
                    54);
        nameMap.put("F#4",
                    54);
        nameMap.put("GF4",
                    54);
        nameMap.put("Gf4",
                    54);
        nameMap.put("GB4",
                    54);
        nameMap.put("G4",
                    55);
        nameMap.put("GS4",
                    56);
        nameMap.put("G#4",
                    56);
        nameMap.put("AF4",
                    56);
        nameMap.put("Af4",
                    56);
        nameMap.put("AB4",
                    56);
        nameMap.put("A4",
                    57);
        nameMap.put("AS4",
                    58);
        nameMap.put("A#4",
                    58);
        nameMap.put("BF4",
                    58);
        nameMap.put("Bf4",
                    58);
        nameMap.put("BB4",
                    58);
        nameMap.put("B4",
                    59);

        // changed PitchFactory.getPitchByName to lookup in uppercase
        // so the lc versions are not needed
        nameMap.put("CF5",
                    B4);
        nameMap.put("Cf5",
                    B4);
        nameMap.put("CB5",
                    B4);
        nameMap.put("C5",
                    C5);
        nameMap.put("C#5",
                    CS5);
        nameMap.put("CS5",
                    CS5);
        nameMap.put("DF5",
                    CS5);
        nameMap.put("Df5",
                    CS5);
        nameMap.put("DB5",
                    CS5);
        nameMap.put("D5",
                    D5);
        nameMap.put("DS5",
                    63);
        nameMap.put("D#5",
                    63);
        nameMap.put("EF5",
                    63);
        nameMap.put("Ef5",
                    63);
        nameMap.put("EB5",
                    63);
        nameMap.put("E5",
                    64);
        nameMap.put("F5",
                    65);
        nameMap.put("FS5",
                    66);
        nameMap.put("F#5",
                    66);
        nameMap.put("GF5",
                    66);
        nameMap.put("Gf5",
                    66);
        nameMap.put("GB5",
                    66);
        nameMap.put("G5",
                    67);
        nameMap.put("GS5",
                    68);
        nameMap.put("G#5",
                    68);
        nameMap.put("AF5",
                    68);
        nameMap.put("Af5",
                    68);
        nameMap.put("AB5",
                    68);
        nameMap.put("A5",
                    69);
        nameMap.put("AS5",
                    70);
        nameMap.put("A#5",
                    70);
        nameMap.put("BF5",
                    70);
        nameMap.put("Bf5",
                    70);
        nameMap.put("BB5",
                    70);
        nameMap.put("B5",
                    71);

        nameMap.put("CF6",
                    71);
        nameMap.put("Cf6",
                    71);
        nameMap.put("CB6",
                    71);
        nameMap.put("C6",
                    72);
        nameMap.put("CS6",
                    73);
        nameMap.put("C#6",
                    73);
        nameMap.put("DF6",
                    73);
        nameMap.put("Df6",
                    73);
        nameMap.put("DB6",
                    73);
        nameMap.put("D6",
                    74);
        nameMap.put("DS6",
                    75);
        nameMap.put("D#6",
                    75);
        nameMap.put("EF6",
                    75);
        nameMap.put("Ef6",
                    75);
        nameMap.put("EB6",
                    75);
        nameMap.put("E6",
                    76);
        nameMap.put("F6",
                    77);
        nameMap.put("FS6",
                    78);
        nameMap.put("F#6",
                    78);
        nameMap.put("GF6",
                    78);
        nameMap.put("Gf6",
                    78);
        nameMap.put("GB6",
                    78);
        nameMap.put("G6",
                    79);
        nameMap.put("GS6",
                    80);
        nameMap.put("G#6",
                    80);
        nameMap.put("AF6",
                    80);
        nameMap.put("Af6",
                    80);
        nameMap.put("AB6",
                    80);
        nameMap.put("A6",
                    81);
        nameMap.put("AS6",
                    82);
        nameMap.put("A#6",
                    82);
        nameMap.put("BF6",
                    82);
        nameMap.put("Bf6",
                    82);
        nameMap.put("BB6",
                    82);
        nameMap.put("B6",
                    83);

        nameMap.put("CF7",
                    83);
        nameMap.put("Cf7",
                    83);
        nameMap.put("CB7",
                    83);
        nameMap.put("C7",
                    84);
        nameMap.put("CS7",
                    85);
        nameMap.put("C#7",
                    85);
        nameMap.put("DF7",
                    85);
        nameMap.put("Df7",
                    85);
        nameMap.put("DB7",
                    85);
        nameMap.put("D7",
                    86);
        nameMap.put("DS7",
                    87);
        nameMap.put("D#7",
                    87);
        nameMap.put("EF7",
                    87);
        nameMap.put("Ef7",
                    87);
        nameMap.put("EB7",
                    87);
        nameMap.put("E7",
                    88);
        nameMap.put("F7",
                    89);
        nameMap.put("FS7",
                    90);
        nameMap.put("F#7",
                    90);
        nameMap.put("GF7",
                    90);
        nameMap.put("Gf7",
                    90);
        nameMap.put("GB7",
                    90);
        nameMap.put("G7",
                    91);
        nameMap.put("GS7",
                    92);
        nameMap.put("G#7",
                    92);
        nameMap.put("AF7",
                    92);
        nameMap.put("Af7",
                    92);
        nameMap.put("AB7",
                    92);
        nameMap.put("A7",
                    93);
        nameMap.put("AS7",
                    94);
        nameMap.put("A#7",
                    94);
        nameMap.put("BF7",
                    94);
        nameMap.put("Bf7",
                    94);
        nameMap.put("BB7",
                    94);
        nameMap.put("B7",
                    95);

        nameMap.put("CF8",
                    95);
        nameMap.put("Cf8",
                    95);
        nameMap.put("CB8",
                    95);
        nameMap.put("C8",
                    96);
        nameMap.put("CS8",
                    97);
        nameMap.put("C#8",
                    97);
        nameMap.put("DF8",
                    97);
        nameMap.put("Df8",
                    97);
        nameMap.put("DB8",
                    97);
        nameMap.put("D8",
                    98);
        nameMap.put("DS8",
                    99);
        nameMap.put("D#8",
                    99);
        nameMap.put("EF8",
                    99);
        nameMap.put("Ef8",
                    99);
        nameMap.put("EB8",
                    99);
        nameMap.put("E8",
                    100);
        nameMap.put("F8",
                    101);
        nameMap.put("FS8",
                    102);
        nameMap.put("F#8",
                    102);
        nameMap.put("GF8",
                    102);
        nameMap.put("Gf8",
                    102);
        nameMap.put("GB8",
                    102);
        nameMap.put("G8",
                    103);
        nameMap.put("GS8",
                    104);
        nameMap.put("G#8",
                    104);
        nameMap.put("AF8",
                    104);
        nameMap.put("Af8",
                    104);
        nameMap.put("AB8",
                    104);
        nameMap.put("A8",
                    105);
        nameMap.put("AS8",
                    106);
        nameMap.put("A#8",
                    106);
        nameMap.put("BF8",
                    106);
        nameMap.put("Bf8",
                    106);
        nameMap.put("BB8",
                    106);
        nameMap.put("B8",
                    107);

        nameMap.put("CF9",
                    107);
        nameMap.put("Cf9",
                    107);
        nameMap.put("CB9",
                    107);
        nameMap.put("C9",
                    108);
        nameMap.put("CS9",
                    109);
        nameMap.put("C#9",
                    109);
        nameMap.put("DF9",
                    109);
        nameMap.put("Df9",
                    109);
        nameMap.put("DB9",
                    109);
        nameMap.put("D9",
                    110);
        nameMap.put("DS9",
                    111);
        nameMap.put("D#9",
                    111);
        nameMap.put("EF9",
                    111);
        nameMap.put("Ef9",
                    111);
        nameMap.put("EB9",
                    111);
        nameMap.put("E9",
                    112);
        nameMap.put("F9",
                    113);
        nameMap.put("FS9",
                    114);
        nameMap.put("F#9",
                    114);
        nameMap.put("GF9",
                    114);
        nameMap.put("GB9",
                    114);
        nameMap.put("G9",
                    115);
        nameMap.put("GS9",
                    116);
        nameMap.put("G#9",
                    116);
        nameMap.put("AF9",
                    116);
        nameMap.put("Af9",
                    116);
        nameMap.put("AB9",
                    116);
        nameMap.put("A9",
                    117);
        nameMap.put("AS9",
                    118);
        nameMap.put("A#9",
                    118);
        nameMap.put("BF9",
                    118);
        nameMap.put("Bf9",
                    118);
        nameMap.put("BB9",
                    118);
        nameMap.put("B9",
                    119);

        nameMap.put("CF10",
                    119);
        nameMap.put("Cf10",
                    119);
        nameMap.put("CB10",
                    119);
        nameMap.put("C10",
                    120);
        nameMap.put("CS10",
                    121);
        nameMap.put("C#10",
                    121);
        nameMap.put("DF10",
                    121);
        nameMap.put("Df10",
                    121);
        nameMap.put("DB10",
                    121);
        nameMap.put("D10",
                    122);
        nameMap.put("DS10",
                    123);
        nameMap.put("D#10",
                    123);
        nameMap.put("EF10",
                    123);
        nameMap.put("Ef10",
                    123);
        nameMap.put("EB10",
                    123);
        nameMap.put("E10",
                    124);
        nameMap.put("F10",
                    125);
        nameMap.put("FS10",
                    126);
        nameMap.put("F#10",
                    126);
        nameMap.put("GF10",
                    126);
        nameMap.put("Gf10",
                    126);
        nameMap.put("GB10",
                    126);
        nameMap.put("G10",
                    127);
    }

    public static final PitchClassComparator pitchClassComparator = new PitchClassComparator();

    public static final class PitchClassComparator implements Comparator<Pitch>, Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 4929792298044867022L;

		@Override
        public int compare(Pitch o1, Pitch o2) {
            int val = 0;
            if (o1.getPitchClass() == o2.getPitchClass()) {
                val = 0;
            } else if (o1.getPitchClass() < o2.getPitchClass()) {
                val = -1;
            } else {
                val = 1;
            }
            return val;
        }
    }

    /**
     * @return the preferredSpelling
     */
    public String getPreferredSpelling() {
        return this.preferredSpelling;
    }

    /**
     * @param preferredSpelling the preferredSpelling to set
     */
    public void setPreferredSpelling(String preferredSpelling) {
        this.preferredSpelling = preferredSpelling;
    };

}

/*
 * History:
 * 
 * $Log$
 * 
 * This version: $Revision$ Last modified: $Date$ Last modified by: $Author$
 */
