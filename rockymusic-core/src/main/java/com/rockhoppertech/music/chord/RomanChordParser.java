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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * Parse chord strings specified by Weber's roman numeral conventions.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class RomanChordParser {
    private static final Logger logger = LoggerFactory
            .getLogger(RomanChordParser.class);
    static Map<String, Integer> romanSymbolMap = new LinkedHashMap<String, Integer>();
    // don't really need all of these for this application but here they are.
    static {
        romanSymbolMap.put("M", 1000);
        romanSymbolMap.put("CM", 900);
        romanSymbolMap.put("D", 500);
        romanSymbolMap.put("CD", 400);
        romanSymbolMap.put("C", 100);
        romanSymbolMap.put("XC", 90);
        romanSymbolMap.put("L", 50);
        romanSymbolMap.put("XL", 40);
        romanSymbolMap.put("X", 10);
        romanSymbolMap.put("IX", 9);
        romanSymbolMap.put("V", 5);
        romanSymbolMap.put("IV", 4);
        romanSymbolMap.put("I", 1);
        romanSymbolMap.put("N", 0); // really. they had zero. ignore the
        // propaganda.
        romanSymbolMap.put("-", 0);
    }
    // syntactic sugar for the methods. You won't have to continually
    // specify the key and scale.
    static Scale defaultScale = ScaleFactory.getScaleByName("Major");
    static String defaultKey = "C";

    public static void setDefaultScaleAndKey(Scale scale, String key) {
        defaultScale = scale;
        defaultKey = key;
    }

    public static void setDefaultKey(String key) {
        defaultKey = key;
    }

    public static void setDefaultScale(Scale scale) {
        defaultScale = scale;
    }

    // this will match all roman numerals. Music doesn't need all of them.
    // private String romanRegexp =
    // "^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";
    // private static String musicRomanRegexp = "(#|b|B)?(IV|V?I{0,3})";
    private static String musicRomanRegexp = "(#|b)?(IV|V?I{0,3})";
    private static Pattern romanPattern = Pattern.compile(musicRomanRegexp);

    public static Chord getChord(String key, String romanString) {
        return getChord(key, defaultScale, romanString);
    }

    /**
     * Uses a major scale based on the specified root.
     * 
     * @param scaleRoot
     *            the root
     * @param romanString
     *            a string in Roman numerals
     * @return a Chord
     */
    public static Chord getChord(Pitch scaleRoot, String romanString) {
        return getChord(scaleRoot.getMidiNumber(), "Major", romanString);
    }

    public static String romanToPitchName(Scale scale, String key,
            String romanString) {
        Pitch p = romanToPitch(scale, key, romanString);
        return PitchFormat.getPitchString(p);
    }

    public static boolean isRoman(String input) {
        // input = input.toUpperCase();
        Matcher matcher = romanPattern.matcher(input);
        logger.debug("checking {}", input);
        matcher.reset();
        boolean result = false;
        if (matcher.find()) {
            int length = matcher.end() - matcher.start();
            if (length > 0)
                result = true;
            String rs = input.substring(matcher.start(), matcher.end());
            String val = String.format("start %d len %d: roman %s",
                    matcher.start(), length, rs
                    );
            logger.debug("matched roman value: {}", val);

        } else {
            logger.debug("no match for {}", input);
        }
        return result;
    }

    public static Pitch romanToPitch(Scale scale, String key, String romanString) {
        romanString = romanString.trim();
        romanString = romanString.toUpperCase(Locale.ENGLISH);
        Matcher matcher = romanPattern.matcher(romanString);
        int degree = 0;
        int accidental = 0;
        switch (romanString.charAt(0)) {
        case '#':
            accidental = 1;
            break;
        case 'b':
            accidental = -1;
            break;
        case 'B':
            accidental = -1;
            break;
        default:
            accidental = 0;
            break;
        }
        if (matcher.find()) {
            // the rn without any chord symbol
            String rs = romanString.substring(matcher.start(), matcher.end());
            degree = romanToArabic(rs);
            String val = String.format(
                    "start %d len %d: roman '%s' = degree %d",
                    matcher.start(),
                    matcher.end() - matcher.start(),
                    rs,
                    degree);
            if (logger.isDebugEnabled()) {
                String s = String.format("%s", val);
                logger.debug(s);
            }
        } else {
            String s = String.format("Ain't no Roman numeral: '%s'",
                    romanString);
            throw new IllegalArgumentException(s);
        }
        List<Pitch> pits = scale.getDegreesAsPitches(key);
        // account for zero based indexing
        int idx = degree - 1;
        if (idx < 0 || idx > pits.size()) {
            throw new IllegalArgumentException(String.format(
                    "I don't grok this: '%s'",
                    romanString));
        }
        Pitch p = pits.get(idx);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("got pitch %s for degree %d, acc=%d", p,
                    degree, accidental));
        }
        if (accidental != 0) {
            p = p.transpose(accidental);
        }
        return p;
    }

    public static Chord getChord(String romanString) {
        return getChord(defaultKey, defaultScale, romanString);
    }

    /**
     * 
     * <code>
     *  Scale scale = ScaleFactory.getScaleByName("Major");
     *  Chord foo = RomanParser.getChord("C", scale, "V7b9");
     *  </code>
     * 
     * @param key
     *            a Pitch name
     * @param scale
     *            a scale
     * @param romanString
     *            The roman numeral along with the music stuff
     * @return a Chord
     */
    public static Chord getChord(String key, Scale scale, String romanString) {
        String origRoman = romanString;
        // romanString = romanString.toUpperCase().trim();
        Pitch p = romanToPitch(scale, key, romanString);
        String chordSymbol = null;
        Matcher matcher = romanPattern.matcher(romanString);
        if (matcher.find()) {
            chordSymbol = origRoman.substring(matcher.end()).trim();
            logger.debug(String.format("chordsymbol is '%s'", chordSymbol));
        }
        String name = PitchFormat.getPitchString(p);
        if (chordSymbol != null && chordSymbol.equals("") == false) {
            name += chordSymbol;
        } else {
            String[] symbols = ChordFactory.getChordSymbols(scale);
            List<Pitch> pits = scale.getDegreesAsPitches(key);
            int degree = pits.indexOf(p);
            logger.debug(String.format("degree %d for %s", degree, p));
            if (degree != -1 && symbols[degree] != null) {
                name += symbols[degree];
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("no symbol for degree %d",
                            degree));
                }
            }
        }
        logger.debug(String.format("created name: %s", name));
        Chord chord = null;
        try {
            chord = ChordFactory.getChordByFullSymbol(name);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                String s = String.format("bad '%s'", name);
                logger.error(s, e);
            }
        }
        if (logger.isDebugEnabled()) {
            String s = String.format("name '%s' returning chord: %s", name,
                    chord.getDisplayName());
            logger.debug(s);
        }
        return chord;
    }

    /**
     * 
     * <code>
     *  Chord foo = RomanParser.getChord(Pitch.C5, "Major", "V7b9");
     *  </code>
     * 
     * @param scaleRoot
     *            a MIDI number. The octave will be ignored.
     * @param scaleName
     *            a scale name registered with the ScaleFactory
     * @param romanString
     *            The roman numeral along with the music stuff
     * @return a Chord
     */
    public static Chord getChord(int scaleRoot, String scaleName,
            String romanString) {
        Chord chord = null;
        romanString = romanString.trim();
        String origString = romanString;
        romanString = romanString.toUpperCase(Locale.ENGLISH);
        Matcher matcher = romanPattern.matcher(romanString);
        int degree = 0;
        String chordSymbol = null;
        int accidental = 0;
        switch (romanString.charAt(0)) {
        case '#':
            accidental = 1;
            break;
        case 'b':
            accidental = -1;
            break;
        case 'B':
            accidental = -1;
            break;
        default:
            accidental = 0;
            break;
        }
        if (matcher.find()) {
            // the rn without any chord symbol
            String rs = romanString.substring(matcher.start(), matcher.end());
            logger.debug(String.format("roman numeral = '%s' is from '%s'", rs,
                    romanString));
            chordSymbol = origString.substring(matcher.end()).trim();
            degree = romanToArabic(rs);
            String val = String.format("start %d len %d: %s = %d", matcher
                    .start(), matcher.end() - matcher.start(), rs, degree);
            if (logger.isDebugEnabled()) {
                String s = String.format("%s", val);
                logger.debug(s);
                s = String.format("chord symbol: '%s'", chordSymbol);
                logger.debug(s);
            }
        } else {
            String s = String.format("Ain't no Roman numeral: '%s'",
                    romanString);
            throw new IllegalArgumentException(s);
        }

        List<Pitch> pits = ScaleFactory.getPitches(scaleName, scaleRoot);
        // getDegreesAsPitches

        // account for zero based indexing
        Pitch p = pits.get(degree - 1);
        if (accidental != 0) {
            p = p.transpose(accidental);
        }
        String name = PitchFormat.getPitchString(p);
        if (chordSymbol.equals("") == false) {
            name += chordSymbol;
        } else {
            Scale scale = ScaleFactory.createFromName(scaleName);
            String[] symbols = ChordFactory.getChordSymbols(scale);
            name += symbols[degree - 1];

            // if (scaleName.equalsIgnoreCase("Major")) {
            // name += majorChords[degree - 1];
            // } else {
            // name += minorChords[degree - 1];
            // }
        }
        try {
            chord = ChordFactory.getChordByFullSymbol(name);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                String s = String.format("bad '%s'", name);
                logger.error(s, e);
            }
        }
        if (logger.isDebugEnabled()) {
            String s = String.format("name '%s' returning chord: %s", name,
                    chord.getDisplayName());
            logger.debug(s);
        }
        return chord;
    }

    // static String[] majorChords = { "maj", "m", "m", "maj", "maj", "m", "dim"
    // };
    public static String pitchNameToRoman(Scale scale, String ps) {
        return pitchNameToRoman(scale, ps, "C");

        // Pitch c = PitchFactory.getPitch(ps);
        // int pc = c.getMidiNumber() % 12;
        // logger.debug(ps);
        // logger.debug("pc " + pc);
        // int[] d = scale.getDegrees();
        // String ss = null;
        // for (int i = 0; i < d.length; i++) {
        // if (pc == d[i]) {
        // logger.debug("match at Index " + i);
        // ss = String.format("%s", RomanChordParser.arabicToRoman(i + 1));
        // break;
        // }
        // if (pc + 1 == d[i]) {
        // logger.debug("match at raised Index " + i);
        // ss = String.format("#%s", RomanChordParser.arabicToRoman(i));
        // break;
        // }
        // }
        // logger.debug(ss);
        // return ss;
    }

    static boolean containsPitchClass(List<Pitch> list, int pc) {
        boolean contains = false;
        for (Pitch p : list) {
            if (p.getPitchClass() == pc) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    static int indexOfPitchClass(List<Pitch> list, int pc) {
        int index = 0;
        for (Pitch p : list) {
            if (p.getPitchClass() == pc) {
                break;
            }
            index++;
        }
        return index;
    }

    public static String pitchNameToRoman(Scale scale, String ps, String key) {
        Pitch c = PitchFactory.getPitch(ps);
        int pc = c.getPitchClass();
        List<Pitch> pits = scale.getDegreesAsPitches(key);
        String msg = null;
        String ss = null;
        int testpc = pc + 1;
        while (testpc >= 12)
            testpc -= 12;

        msg = String.format("ps=%s pc %d mn %d for %s", ps, pc, c
                .getMidiNumber(), c);
        logger.debug(msg);
        logger.debug("pitches {}", pits);

        if (containsPitchClass(pits, pc)) {
            int index = indexOfPitchClass(pits, pc);
            logger.debug(String.format("contains at index=%d", index));
            ss = String.format("%s", RomanChordParser.arabicToRoman(index + 1));
        } else {
            logger.debug(String.format("Does not contain %s", c));

            // Yikes!
            // and I mean Yikes.

            // for (int i = 0; i < pits.size(); i++) {
            // Pitch cp = pits.get(i);
            // logger.debug(String.format("is current %d == %d", cp
            // .getPitchClass(), testpc));
            // if (cp.getPitchClass() == testpc) {
            // ss = String
            // .format("#%s", RomanChordParser.arabicToRoman(i));
            // }
            // }
            // if (ss == null) {
            logger.debug(String.format("testpc %d Not found", testpc));
            if (c.getMidiNumber() <= pits.get(0).getMidiNumber()) {
                c = c.transpose(12);
                testpc = c.getPitchClass();
                logger.debug(String.format("transposed testpc %d for %s",
                        testpc, c));
            }
            Pitch[] array = new Pitch[pits.size()];
            array = pits.toArray(array);
            // int ins = Arrays.binarySearch(array, c,
            // Pitch.pitchClassComparator);
            int ins = Arrays.binarySearch(array, c);
            ins *= -1;
            ins -= 1;
            logger.debug(String.format("binary insertion point %d", ins));
            logger.debug(String.format("pitch at ins is %s", pits.get(ins)));
            Pitch beforeIns = pits.get(ins - 1);

            String accidental = "";
            testpc = pc;
            while (testpc >= 12)
                testpc -= 12;
            int b = beforeIns.getPitchClass();
            while (b++ < testpc) {
                accidental += "#";
                logger.debug(String.format("testpc %d acc %s b %d", testpc,
                        accidental, b));
            }

            // while (containsPitchClass(pits, testpc++) == false) {
            // accidental += "#";
            // logger.debug(String.format("testpc %d acc %s", testpc,
            // accidental));
            // }
            // int index = indexOfPitchClass(pits, testpc);
            // logger.debug(String.format("testpc %s is at index=%d",
            // testpc,
            // index));
            ss = String.format("%s%s", accidental, RomanChordParser
                    .arabicToRoman(ins));
            // .arabicToRoman((ins * -1) - 1));

            // testpc++;
            // for (int i = 0; i < pits.size(); i++) {
            // Pitch cp = pits.get(i);
            // logger.debug(String.format("is current %d == %d", cp
            // .getPitchClass(), testpc));
            // if (cp.getPitchClass() == testpc) {
            // ss = String
            // .format("##%s", RomanChordParser.arabicToRoman(i));
            // }
            // }
            // }
        }
        logger.debug(String.format("final roman = '%s'", ss));
        return ss;
    }

    public static int romanToArabic(String roman) {
        int arabic = 0;
        int max = 0;
        char[] charArray = roman.toUpperCase(Locale.ENGLISH).toCharArray();
        // loop from the end
        for (int index = charArray.length - 1; index >= 0; index--) {
            for (String c : romanSymbolMap.keySet()) {
                int value = romanSymbolMap.get(c).intValue();
                if (c.length() == 1 && c.charAt(0) == charArray[index]) {
                    if (value >= max) {
                        max = value;
                        arabic += max;
                    } else {
                        arabic -= value;
                    }
                }
            }
        }
        return arabic;
    }

    public static String arabicToRoman(int arabic) {
        if (logger.isDebugEnabled()) {
            String s = String.format("converting %d", arabic);
            logger.debug(s);
        }
        StringBuffer result = new StringBuffer(10);
        for (String c : romanSymbolMap.keySet()) {
            int value = romanSymbolMap.get(c).intValue();
            // System.out.println("value: " + value);
            // System.out.println("c " + c);
            while (arabic >= value && arabic > 0) {
                // System.out.println("loop " + arabic);
                arabic -= value;
                result.append(c);
            }
        }
        return result.toString();
    }

    public static void main(String... args) {
        for (int i = 1; i <= 20; i++) {
            String roman = arabicToRoman(i);
            System.out.println(roman);
        }
        System.out.println(romanToArabic("I"));
        System.out.println(romanToArabic("II"));
        System.out.println(romanToArabic("III"));
        System.out.println(romanToArabic("IV"));
        System.out.println(romanToArabic("V"));
        System.out.println(romanToArabic("VI"));
        System.out.println(romanToArabic("VII"));
        System.out.println(romanToArabic("VIII"));
        System.out.println(romanToArabic("IX"));
        System.out.println(romanToArabic("X"));
        System.out.println(romanToArabic("XI"));
        System.out.println(romanToArabic("XL"));
        System.out.println(romanToArabic("C"));
        System.out.println(romanToArabic("MDCCLXXVI"));

        // Chord c = getChord("C", "iim7");
        // System.out.println(c);
        // c = getChord("C", "Vmaj9b5");
        // System.out.println(c);
        // c = getChord("C", "Imaj7");
        // System.out.println(c);
        // c = getChord("C", "I");
        // System.out.println(c);
        // c = getChord(Pitch.C5, "Harmonic Minor", "I");
        // System.out.println(c);
        // c = getChord(Pitch.C5, "Major", "#Idim7");
        // System.out.println(c);
        // c = getChord(Pitch.C5, "Major", "i0");
        // System.out.println(c);
        // c = getChord(Pitch.C5, "Major", "i07");
        // System.out.println(c);
        // c = getChord(Pitch.C5, "Major", "vii");
        // System.out.println(c);

        // c = getChord("C", "IV");
        // c = getChord("C", "V7");
        // c = getChord("C", "vim7");
        // c = getChord("C", "viidim7");
        // c = getChord("C", "iim7b5");
        // c = getChord("C", "v7b9");

        // http://www.jazzguitar.be/jazz_chord_progressions.html
        // IIm7 V7 | Imaj7
        // Imaj7 VIm7 | IIm7 V7 | IIIm7 VI7 | IIm7 V7
        // Imaj7 | | (IIm7 | V7 ) | Imaj7 | | (IIm7 | V7 )
        // Imaj7 #Idim7 | IIm7 #IIdim7 | IIIm7 VI7
        // Imaj7 | | II7 | | IIm7 | V7 | Imaj7 | |
        // Imaj7 | (IIm7 V7) | IVmaj7
        // Imaj7 | I7 | IVmaj7| IVm7| IIIm7 VI7| IIm7 V7|Imaj7| |

        // C07 = Eb07 = Gb07 = A07
    }
}
/*
 * Tritone Substitution blues F7 | Bb7 | F7 | Cm7 F7 | Bb7 | Bm7 E7 | F7 E7 |
 * Eb7 D7 | Gm7 | C7 Bb7 | Am7 D7 | Gm7 C7
 */
