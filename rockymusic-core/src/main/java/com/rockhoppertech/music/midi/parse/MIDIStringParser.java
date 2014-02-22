/**
 * 
 */
package com.rockhoppertech.music.midi.parse;

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

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.DurationParser;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;

/*
 * Running "status"
 * R1.5 means duration = 1.5 beats
 * I"patch name" means use this general midi patch
 * I25 use patch number 25
 * 
 * C means C5,1d,1d
 * Pitch,start,dur
 * 
 */
/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDIStringParser {
    private static final Logger logger = LoggerFactory
            .getLogger(MIDIStringParser.class);

    public MIDITrack parseChord(String s) {
        MIDITrack list = new MIDITrack();
        Scanner sc = new Scanner(s);
        sc.useDelimiter("+");
        while (sc.hasNext()) {
            String p = sc.next();
            MIDINote n = parseNote(p);
            list.add(n);
        }
        sc.close();
        return list;
    }

    public static String createString(MIDITrack notelist) {
        // Pitch, Start beat, Duration, Velocity, Pan, Channel, Bank, Pitchbend
        StringBuilder sb = new StringBuilder();
        for (MIDINote n : notelist) {
            sb.append(n.getPitch().toString().trim()).append(',');
            sb.append(n.getStartBeat()).append(',');
            sb.append(n.getDuration()).append(',');
            sb.append(n.getVelocity()).append(',');
            sb.append(n.getPan()).append(',');
            sb.append(n.getChannel()).append(',');
            sb.append(n.getBank()).append(',');
            sb.append(n.getProgram()).append(',');
            sb.append(n.getPitchBend()).append(' ');
        }
        return sb.toString();
    }

    public static String createStringBrief(MIDITrack notelist) {
        // Pitch, Start beat, Duration
        StringBuilder sb = new StringBuilder();
        for (MIDINote n : notelist) {
            sb.append(n.getPitch().toString().trim()).append(',');
            sb.append(n.getStartBeat()).append(',');
            sb.append(n.getDuration()).append(' ');
        }
        return sb.toString();
    }

    /*
     * + The plus sign indicates that there is one or more ? The question mark
     * indicates there is zero or one * The asterisk indicates there are zero or
     * more
     */

    public final static int REGEX_FLAGS = Pattern.CASE_INSENSITIVE
            | Pattern.UNICODE_CASE | Pattern.CANON_EQ;
    // any whitespace followed by one or more pc specifiers (C, Ef, F#) then one
    // or more octave chars
    // (e.g. octave 10 is two chars)
    static Pattern fullPitchPattern = Pattern.compile("\\s*[A-Ga-gsS#]+[0-9]+",
            REGEX_FLAGS);
    static Pattern shortPitchPattern = Pattern.compile("\\s*[A-Ga-gsS#]+\\s*");

    public void setRunningOctave(int runningOctave) {
        this.runningOctave = runningOctave;
    }

    public Pitch parsePitch(String s) {
        // TODO need this? not used yet
        // ?: Non-capturing parentheses
        // String noteNameWithOctave1 = "([A-G](?:bb|b||f||#|s|x))(\\d)";
        String noteNameWithOctave = "(([A-G])(bb|b||f||#|s|x)(\\d))";
        Pattern pattern = Pattern.compile(noteNameWithOctave,
                REGEX_FLAGS);
        Matcher match = pattern.matcher(s);
        if (match.matches()) {
            // the entire thing
            System.err.println(match.group(0));
            // the letter number
            System.err.println(match.group(1));
            // the octave
            System.err.println(match.group(2));
            System.err.println(match.group(3));

            String letter = match.group(1);
            String oct = match.group(2);
            System.err.println("letter " + letter);
            System.err.println("oct " + oct);

        }

        return null;
    }

    /**
     * Format:
     * <p>
     * 
     * <pre>
	 * Pitch 
	 * Pitch, Start beat 
	 * Pitch, Start beat, Duration 
	 * Pitch, Start beat, Duration, Velocity 
	 * Pitch, Start beat, Duration, Velocity, Pan 
	 * Pitch, Start beat, Duration, Velocity, Pan, Channel 
	 * Pitch, Start beat, Duration, Velocity, Pan, Channel, Bank, Program 
	 * Pitch, Start beat, Duration, Velocity, Pan, Channel, Bank, Program, Pitchbend
	 * </pre>
     * 
     * @param s
     * @return
     */
    public MIDINote parseNote(String s) {
        MIDINote n = null;
        Scanner sc = new Scanner(s);
        sc.useDelimiter(",");

        String p = null;

        if (sc.hasNext(fullPitchPattern)) {
            p = sc.next();
        } else if (sc.hasNext(shortPitchPattern)) {
            p = sc.next();
            p += runningOctave;
            logger.debug("short " + p);
        } else {
            String tok = sc.next();
            sc.close();
            throw new MIDIParserException(String
                    .format("Bad pitch token '%s'",
                            tok));
        }

        double start = 1d;
        double duration = 1d;

        if (sc.hasNextDouble()) {
            start = sc.nextDouble();
        }
        
//        boolean sequential = false;
//        if(sc.hasNext("\\+")) {
//            sequential = true;
//            logger.debug("sequential");
//        }

        // couldBeDurationString(tok);

        if (sc.hasNext(DurationParser.allPattern)) {
            String tok = sc.next(DurationParser.allPattern);
            logger.debug("token {}", tok);
            duration = DurationParser.getDuration(tok);
        } else if (sc.hasNextDouble()) {
            duration = sc.nextDouble();
        }

        n = new MIDINote(p, start, duration);
        runningOctave = n.getPitch().getMidiNumber() / 12;
        logger.debug(
                String.format("p %s note %s ro %d",
                        p,
                        n,
                        runningOctave));

        logger.debug("pitch {} note {} running octave {}",
                p,
                n,
                runningOctave);

        if (sc.hasNextInt()) {
            int velocity = sc.nextInt();
            n.setVelocity(velocity);
        }

        if (sc.hasNextInt()) {
            int pan = sc.nextInt();
            n.setPan(pan);
        }

        if (sc.hasNextInt()) {
            int channel = sc.nextInt();
            n.setChannel(channel);
        }

        if (sc.hasNextInt()) {
            int bank = sc.nextInt();
            n.setBank(bank);
        }
        if (sc.hasNextInt()) {
            int program = sc.nextInt();
            n.setProgram(program);
        }

        if (sc.hasNextInt()) {
            int pb = sc.nextInt();
            n.setPitchBend(pb);
        }
        sc.close();
        return n;
    }

    /**
     * Just the pitch and duration. The start beatw will be 1. Tell the track to
     * be sequential if desired: track.sequential()
     * 
     * Format:
     * <p>
     * Pitch, Duration
     * 
     * @param s
     * @return
     */
    public MIDINote parseBriefNote(String s) {
        MIDINote n = null;
        Scanner sc = new Scanner(s);
        sc.useDelimiter(",");

        String p = null;

        if (sc.hasNext(fullPitchPattern)) {
            p = sc.next();
        } else if (sc.hasNext(shortPitchPattern)) {
            p = sc.next();
            p += runningOctave;
            logger.debug("short " + p);
        } else {
            String se = String
                    .format("Bad pitch token '%s'",
                            sc.next());
            sc.close();
            throw new MIDIParserException(se);
        }

        double duration = 1d;
        if (sc.hasNext(DurationParser.allPattern)) {
            String tok = sc.next(DurationParser.allPattern);
            logger.debug("token " + tok);
            duration = DurationParser.getDuration(tok);
        } else if (sc.hasNextDouble()) {
            duration = sc.nextDouble();
        }

        n = new MIDINote(p, 1d, duration);
        runningOctave = n.getPitch().getMidiNumber() / 12;
        logger.debug(
                String.format("p %s note %s ro %d",
                        p,
                        n,
                        runningOctave));
        sc.close();
        return n;
    }

    double runningDuration = 1d;
    double runningStart = 1d;
    int runningVelocity = 64;
    int runningBank = 0;
    int runningProgram = 1;
    int runningBend = 0;
    int runningVoice = 1;
    int runningOctave = 5;

    public enum StartMode {
        ADD,
        APPEND
    }

    // by default all startbeats are the same
    StartMode startOp = StartMode.ADD;

    public MIDITrack parseString(String s) {
        MIDITrack list = new MIDITrack();
        return parseString(list, s);
    }

    public String checkMulti(String s) {
        Scanner sc = new Scanner(s);
        String patternString = " *X(\\d)\\s+\\((.+?)\\)+?";
        Pattern multiPattern = Pattern
                .compile(patternString);
        int numRepeats = 0;
        StringBuilder origsb = new StringBuilder(s);

        logger.debug("checking for multi '{}'", s);
        sc.useDelimiter("\\Z");
        // logger.debug("multi next '{}'", sc.next());
        String multi = sc.findInLine(multiPattern);
        logger.debug("multi '{}'", multi);
        if (multi != null) {
            numRepeats = Integer.parseInt(sc.match().group(1));
            int start = sc.match().start();
            int end = sc.match().end();

            logger.debug("multi group 1 '{}'", sc.match().group(1));
            logger.debug("multi group 2 '{}'", sc.match().group(2));
            logger.debug("multi group count '{}'", sc.match()
                    .groupCount());
            logger.debug("multi start {} end {}", start, end);

            StringBuilder sb2 = new StringBuilder();
            sb2.append(' ');
            for (int i = 0; i < numRepeats; i++) {
                sb2.append(sc.match().group(2)).append(' ');
            }

            logger.debug("origsb before '{}'", origsb);
            origsb.replace(start, end, sb2.toString());
            logger.debug("origsb after '{}'", origsb);

            Matcher matcher = multiPattern.matcher(origsb);
            boolean recurse = matcher.find();
            logger.debug("recurse {}", recurse);
            if (recurse) {
                origsb = new StringBuilder(checkMulti(origsb.toString()));
            }
        } else {
            logger.debug("no multi in '{}'", s);
        }
        sc.close();
        return origsb.toString().trim();
    }

    /**
     * Pretty much the main logic of the parser.
     * 
     * @param track
     * @param s
     * @return
     */
    public MIDITrack parseString(MIDITrack track, String s) {
        s = s.trim();
        logger.debug("string '{}'", s);

        // get rid of comments
        String removeSlashSplat = s.replaceAll("/\\*.*\\*/", "");
        s = removeSlashSplat.replaceAll("//.*(?=\\n)", "");
        // s = s.replaceAll("//.*?\n","\n");
        logger.debug("string minus comments '{}'", s);
        s = s.replaceAll("\n", " ");
        s = s.replaceAll(" +", " "); // collapse multi spaces to one
        logger.debug("string minus newlines '{}'", s);
        boolean isRunningDurationSet = false;
        boolean isRunningProgramSet = false;
        boolean isRunningVelocitySet = false;
        boolean isRepeatSet = false;
        double startBeat = -1d; // an invalid value

        logger.debug("scanning '{}'", s);
        s = checkMulti(s);

        if (!s.equals("")) {
            Scanner sc = new Scanner(s);
            // sc.useDelimiter("\\W");

            while (sc.hasNext()) {
                String tok = sc.next();

                logger.debug("token '{}'", tok);

                // if (sc.hasNext(pat1)) {
                // tok = sc.next(pat1);
                // logger.debug("pat1 found paren token {}", tok);
                // } else {
                // logger.debug("pat1 no parens");
                // }

                // System.out.println(sc.findInLine("\\w+"));

                // if (tok.contains("+")) {
                // MIDINoteList chord = parseChord(tok);
                // list.append(chord);
                // }

                // Pattern multiPattern = Pattern
                // .compile(" +X(\\d)\\s+\\((.+?)\\)");
                String multi = null;

                if (tok.startsWith("R") || tok.startsWith("r")) {
                    String ds = tok.substring(1);
                    logger.debug("running dur string: {}", ds);
                    double d = 1d;

                    // is it a duration string e.g. q or just a float?
                    Matcher matcher = DurationParser.allPattern.matcher(ds);
                    if (matcher.find()) {
                        d = DurationParser.getDuration(matcher.group(0));
                        logger.debug("found dur string {}", d);
                    } else {
                        d = Double.parseDouble(ds);
                    }

                    logger.debug("running dur candidate {}", d);

                    isRunningDurationSet = true;
                    // if (d != runningDuration) {
                    if (Math.abs(d - runningDuration) > .00001) {
                        runningDuration = d;
                    }
                    continue;
                }

                if (tok.startsWith("V") || tok.startsWith("v")) {
                    runningVelocity = Integer.parseInt(tok.substring(1));
                    isRunningVelocitySet = true;
                    continue;
                }

                // S+ C5,.5 or just pitch and duration
                // S= C5, 1.0, .5 is the default anyway pitch, start, dur
                if (tok.startsWith("S") || tok.startsWith("s")) {
                    String op = tok.substring(1);
                    op = op.trim();
                    if (op.startsWith("+")) {
                        startOp = StartMode.APPEND;
                        logger.debug("S+ Append mode");
                    }
                    if (op.startsWith("=")) {
                        logger.debug("S= Add mode");
                        startOp = StartMode.ADD;
                        op = op.substring(1);
                        if ( !op.equals("")) {
                            startBeat = Double.parseDouble(op);
                            logger.debug("Start op arg '{}'={}", op, startBeat);
                        }
                    }
                    continue;
                    // sc.useDelimiter("\\Z");
                    // tok = sc.next();
                    // remove leading space
                    // tok = tok.replaceAll("^ +", "");
                }
                logger.debug("token after s '{}'", tok);

                // repeat next note this many times
                int numRepeats = 0;

                /*
                 * sc.useDelimiter("\\Z"); //logger.debug("multi next '{}'",
                 * sc.next()); String multi = sc.findInLine(multiPattern);
                 * logger.debug("multi '{}'", multi); if (multi != null) {
                 * logger.debug("multi group 1 '{}'", sc.match().group(1));
                 * logger.debug("multi group 2 '{}'", sc.match().group(2));
                 * numRepeats = Integer.parseInt(sc.match().group(1)); tok =
                 * sc.match().group(2); logger.debug("multi group count '{}'",
                 * sc.match() .groupCount());
                 * 
                 * } else { sc.reset(); if (tok.startsWith("X") ||
                 * tok.startsWith("x")) { tok = sc.next();
                 * logger.debug("nexttok x '{}'", tok); } // tok = sc.next(); //
                 * logger.debug("nexttok '{}'", tok);
                 * 
                 * //logger.debug("nexttok '{}'", sc.next());
                 * 
                 * }
                 */

                // for X3 C D E to get C C C D E
                if (tok.startsWith("X") || tok.startsWith("x")) {
                    // skip over X
                    String op = tok.substring(1);
                    // get the value
                    op = op.trim();
                    numRepeats = Integer.parseInt(op);
                    logger.debug("num repeats {}", numRepeats);

                    // not greedy
                    // Pattern pat = Pattern
                    // .compile("\\((.+?)\\)");

                    // Pattern square = Pattern.compile("\\[([^\\]]+)]");

                    // square = Pattern.compile("\\[([A-Za-z0-9.]+)\\]",
                    // Pattern.DOTALL);

                    // Scanner sc2 = new Scanner(s);
                    // sc2.useDelimiter("^.*$");
                    // sc.useDelimiter("\\[.*?\\]|w+");

                    // if (sc.hasNext(pat)) {
                    // tok = sc.next(pat);
                    // logger.debug("sc2 found paren token {}", tok);
                    // } else {
                    // logger.debug("sc2 no parens");
                    // }

                    // String next = sc.next();
                    // logger.debug("next {}", next);
                    // Matcher match = square.matcher(next);
                    // if (match.matches()) {
                    // logger.debug("mather {}", match.group(0));
                    // }

                    // Pattern pat = Pattern
                    // .compile(Pattern.quote("((.*?))"),Pattern.CASE_INSENSITIVE);
                    // .compile(Pattern.quote("((.*?))"), Pattern.DOTALL);
                    // logger.debug("pattern {}", pat);
                    // logger.debug("findinline {}", sc.findWithinHorizon(pat,
                    // 90));
                    // logger.debug("findinline {}", sc.findInLine(pat));
                    // logger.debug("findinline match {}", sc.match().group(0));
                    // logger.debug("findinline {}", sc.findInLine("C D E"));

                    // sc.useDelimiter(pat);
                    // while (sc.hasNext()) {
                    // logger.debug("dump {}", sc.next());
                    // }
                    //
                    // if (sc.hasNext(pat)) {
                    // tok = sc.next(pat);
                    // logger.debug("found paren token {}", tok);
                    // } else {
                    // logger.debug("no parens");
                    // tok = sc.next();
                    // }

                    // now get the note
                    tok = sc.next();
                    // sc.useDelimiter("\\W");
                }

                // will create a ShortMessage.PROGRAM_CHANGE
                if (tok.startsWith("I") || tok.startsWith("i")) {
                    // try {
                    // skip over I
                    tok = tok.substring(1);
                    if (tok.startsWith("\"")) {
                        int end = tok.lastIndexOf("\"");
                        tok = tok.substring(1,
                                end);
                        runningProgram = MIDIGMPatch.getPatch(tok)
                                .getProgram();
                    } else if (tok.startsWith("'")) {
                        int end = tok.lastIndexOf("'");
                        tok = tok.substring(1,
                                end);
                        runningProgram = MIDIGMPatch.getPatch(tok)
                                .getProgram();
                    } else {
                        int num = Integer.parseInt(tok);
                        runningProgram = num;
                    }

                    isRunningProgramSet = true;
                    // } catch (MIDIParserException e) {
                    // logger.error(e.getLocalizedMessage(), e);
                    // }
                    continue;
                }

                MIDINote note = null;
                do {
                    if (startOp == StartMode.ADD) {
                        // start beat is honored with add.
                        // the start beat is chenged with append

                        // if (multi != null) {
                        //
                        // Scanner ms = new Scanner(sc.match().group(2));
                        // while (ms.hasNext()) {
                        // String mt = ms.next();
                        // if (mt.startsWith("X")) {
                        // continue;
                        // }
                        // note = parseNote(mt);
                        // track.add(note);
                        // logger.debug("added {}", note);
                        // if (startBeat != -1d) {
                        // note.setStartBeat(startBeat);
                        // logger.debug("Set start beat to {} for {}",
                        // startBeat,
                        // note);
                        //
                        // }
                        // }
                        // ms.close();
                        //
                        // } else {
                        note = parseNote(tok);
                        track.add(note);
                        logger.debug("added {}", note);
                        if (startBeat != -1d) {
                            note.setStartBeat(startBeat);
                            logger.debug("Set start beat to {} for {}",
                                    startBeat,
                                    note);

                        }

                    } else if (startOp == StartMode.APPEND) {
                        // since append ignores startbeat, there's no point in
                        // specifying it!
                        // parseBrief is just pitch and duration.
                        // logger.debug("multi group count '{}'",sc.match().groupCount()
                        // );
                        logger.debug("appending {}", tok);
                        if (tok.startsWith("X") || tok.startsWith("x")) {
                            logger.debug("skipping note creation for {}", tok);
                            continue;
                        }

                        // if (multi != null) {
                        // logger.debug("multi append '{}'", multi);
                        // Scanner ms = new Scanner(sc.match().group(2));
                        // while (ms.hasNext()) {
                        // String mt = ms.next();
                        // note = parseBriefNote(mt);
                        // track.append(note);
                        // logger.debug("appended {}", note);
                        // }
                        // ms.close();
                        // } else {
                        logger.debug("multi  null append tok '{}'", tok);
                        note = parseBriefNote(tok);
                        track.append(note);
                        logger.debug("appended {}", note);
                        // }

                        logger.debug("APPEND, parsing brief. note {} ", note);

                    }
                } while (--numRepeats > 0);

                if (isRunningDurationSet) {
                    note.setDuration(runningDuration);
                }
                if (isRunningProgramSet) {
                    note.setProgram(runningProgram);
                }
                if (isRunningVelocitySet) {
                    note.setVelocity(runningVelocity);
                }

                if (isRepeatSet) {
                    logger.debug("repeat is set");
                    isRepeatSet = false;
                }
            }
            sc.close();
        }

        return track;
    }
}
