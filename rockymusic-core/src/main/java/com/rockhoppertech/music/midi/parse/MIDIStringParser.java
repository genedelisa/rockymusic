/**
 * 
 */
package com.rockhoppertech.music.midi.parse;

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
     *  +    The plus sign indicates that there is one or more
     *  ?    The question mark indicates there is zero or one 
     *  *   The asterisk indicates there are zero or more 
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
        //TODO need this? not used yet
        // ?:  Non-capturing parentheses
      //  String noteNameWithOctave1 = "([A-G](?:bb|b||f||#|s|x))(\\d)";
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
     *  Pitch
     *  Pitch, Start beat
     *  Pitch, Start beat, Duration
     *  Pitch, Start beat, Duration, Velocity
     *  Pitch, Start beat, Duration, Velocity, Pan
     *  Pitch, Start beat, Duration, Velocity, Pan, Channel
     *  Pitch, Start beat, Duration, Velocity, Pan, Channel, Bank, Program
     *  Pitch, Start beat, Duration, Velocity, Pan, Channel, Bank, Program, Pitchbend  
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
            p += this.runningOctave;
            logger.debug("short " + p);
        } else {
        	String tok = sc.next();
        	sc.close();
            throw new IllegalArgumentException(String
                    .format("Bad pitch token '%s'",
                            tok));
        }

        double start = 1d;
        double duration = 1d;

        if (sc.hasNextDouble()) {
            start = sc.nextDouble();
        }

        // couldBeDurationString(tok);

        if (sc.hasNext(DurationParser.allPattern)) {
            String tok = sc.next(DurationParser.allPattern);
            logger.debug("token " + tok);
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
     * Just the pitch and duration. The start beatw will be 1.
     * Tell the track to be sequential if desired: track.sequential()
     * 
     * Format:
     * <p>
     *  Pitch
     *  Pitch, Duration  
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
            p += this.runningOctave;
            logger.debug("short " + p);
        } else {
        	sc.close();
            throw new IllegalArgumentException(String
                    .format("Bad pitch token '%s'",
                            sc.next()));
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
        return parseString(list,
                           s);
    }

    public MIDITrack parseString(MIDITrack list, String s) {
        s = s.trim();
        boolean isRunningDurationSet = false;
        boolean isRunningProgramSet = false;
        boolean isRunningVelocitySet = false;
        double startBeat = -1d; // an invalid value

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("scanning '%s'",
                                       s));
        }
        if (s != null && !s.equals("")) {
            Scanner sc = new Scanner(s);
            // sc.useDelimiter("\\W");

            while (sc.hasNext()) {
                String tok = sc.next();
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("token '%s'",
                                               tok));
                }

                // if (tok.contains("+")) {
                // MIDINoteList chord = parseChord(tok);
                // list.append(chord);
                // }

                if (tok.startsWith("R") || tok.startsWith("r")) {
                    double d = Double.parseDouble(tok.substring(1));
                    isRunningDurationSet = true;
                    if (d != runningDuration) {
                        runningDuration = d;
                    }
                    continue;
                }

                if (tok.startsWith("V") || tok.startsWith("v")) {
                    runningVelocity = Integer.parseInt(tok.substring(1));
                    isRunningVelocitySet = true;
                    continue;
                }

                // S+ C5,.5
                // S= C5, 1.0, .5
                if (tok.startsWith("S") || tok.startsWith("s")) {
                    String op = tok.substring(1);
                    op = op.trim();
                    if (op.startsWith("+")) {
                        startOp = StartMode.APPEND;
                    }
                    if (op.startsWith("=")) {
                        startOp = StartMode.ADD;
                        op = op.substring(1);
                        if (op != null && !op.equals("")) {
                            startBeat = Double.parseDouble(op);
                            if (logger.isDebugEnabled()) {
                                String st = String
                                        .format("Start op arg '%s'=%f%n",
                                                op,
                                                startBeat);
                                logger.debug(st);
                            }
                        }
                    }
                    continue;
                }

                // will create a ShortMessage.PROGRAM_CHANGE
                if (tok.startsWith("I") || tok.startsWith("i")) {
                    try {
                        // skip over I
                        tok = tok.substring(1);
                        if (tok.startsWith("\"")) {
                            int end = tok.lastIndexOf("\"");
                            tok = tok.substring(1,
                                                end);
                            // this.runningProgram =
                            // MIDIUtils.getPatchNumber(tok);
                            this.runningProgram = MIDIGMPatch.getPatch(tok)
                                    .getProgram();
                        } else if (tok.startsWith("'")) {
                            int end = tok.lastIndexOf("'");
                            tok = tok.substring(1,
                                                end);
                            // this.runningProgram =
                            // MIDIUtils.getPatchNumber(tok);
                            this.runningProgram = MIDIGMPatch.getPatch(tok)
                                    .getProgram();
                        } else {
                            int num = Integer.parseInt(tok);
                            this.runningProgram = num;
                        }

                        isRunningProgramSet = true;
                    } catch (IllegalArgumentException e) {
                    	 logger.error(e.getLocalizedMessage(), e);
                    }
                    continue;
                }

                MIDINote note = null;

                if (startOp == StartMode.ADD) {
                	//start beat is honored with add.
                	// the start beat is chenged with append
                	
                	note = parseNote(tok);

                	 if (startBeat != -1d) {
                         note.setStartBeat(startBeat);
                         if (logger.isDebugEnabled()) {
                             String st = String
                                     .format("Set start beat to %f for %s%n",
                                             startBeat,
                                             note);
                             logger.debug(st);
                         }
                     }
                    list.add(note);
                    if (logger.isDebugEnabled()) {
                        String st = String.format("added %s",
                                                  note);
                        logger.debug(st);
                    }
                } else if (startOp == StartMode.APPEND) {
                	// since append ignores startbeat, there's no point in specifying it!
                	// parseBrief is just pitch and duration.
                	note = parseBriefNote(tok);
                	
                    list.append(note);
                    if (logger.isDebugEnabled()) {
                        String st = String.format("appended %s",
                                                  note);
                        logger.debug(st);
                    }
                }
                
                if (isRunningDurationSet) {
                    note.setDuration(runningDuration);
                }
                if (isRunningProgramSet) {
                    note.setProgram(runningProgram);
                }
                if (isRunningVelocitySet) {
                    note.setVelocity(runningVelocity);
                }
            }
            sc.close();
        }
       
        return list;
    }
}
