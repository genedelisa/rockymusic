package com.rockhoppertech.music.examples.track;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

import static com.rockhoppertech.music.Pitch.*;

/**
 * Examples of using MIDITrack.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class TrackExamples {
    private static final Logger logger = LoggerFactory
            .getLogger(TrackExamples.class);

    static void ex01() {
        MIDITrack track = new MIDITrack();
        track.add(Pitch.C5);
        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void ex02() {
        MIDITrack track = new MIDITrack();
        track.add(Pitch.C5).add(D5).add(E5).add(F5).add(G5);
        track.sequential();
        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void ex03() {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C5 D E F G")
                .sequential()
                .build();
        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void ex04() {
        // just an empty track
        MIDITrack track = MIDITrackBuilder.create()
                .build();
        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void ex05() {
        MIDITrack track = new MIDITrack();
        MIDITrack c = MIDITrackBuilder.create()
                .noteString("C4 C5 E G")
                .build();
        MIDITrack f = MIDITrackBuilder.create()
                .noteString("F4 C5 F A")
                .build();
        MIDITrack g7 = MIDITrackBuilder.create()
                .noteString("G4 B D5 F G")
                .build();

        track.append(c).append(f).append(g7).append(c);
        track.play();
        logger.debug("here is the track\b{}", track);
    }

    static void ex06() {
        MIDITrack track = new MIDITrack();
        MIDITrack c = MIDITrackBuilder.create()
                .noteString("C4 C5 E G")
                .build();

        MIDITrack g7 = MIDITrackBuilder.create()
                .noteString("G4 B D5 F G")
                .build();

        track.append(c);
        g7.sequential();
        track.append(g7);
        g7.chordify();
        track.append(g7);
        track.append(c);
        
        g7.chordify(12d);
        // ignores g7's start beat!
        //track.append(g7);   
        track.merge(g7);

        track.play();
        logger.debug("here is the track\b{}", track);
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {

        final Integer[] choices = new Integer[] { 1, 2, 3, 4, 5, 6 };
        final Integer choice = (Integer) JOptionPane
                .showInputDialog(null,
                        "Which Example?",
                        "Example chooser",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        choices,
                        choices[0]);

        switch (choice) {
        case 1:
            ex01();
            break;
        case 2:
            ex02();
            break;
        case 3:
            ex03();
            break;
        case 4:
            ex04();
            break;
        case 5:
            ex05();
            break;
        case 6:
            ex06();
            break;
        default:
            ex01();
            break;
        }
    }

}
