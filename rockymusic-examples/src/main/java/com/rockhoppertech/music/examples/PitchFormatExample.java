package com.rockhoppertech.music.examples;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.PitchFormat;

public class PitchFormatExample {
    /**
     * just a test.
     * 
     * @param args
     *            command line arguments ignored.
     */
    public static void main(String[] args) {
        Pitch[] a = new Pitch[12];
        PitchFormat pf = PitchFormat.getInstance();
        int num = 60;

        for (int i = Pitch.C0; i < Pitch.C1; i += Pitch.MINOR_SECOND) {
            a[i] = PitchFactory.getPitch(num++);
        }

        pf.setJustification(PitchFormat.CENTER_JUSTIFY);
        pf.setWidth(4);

        System.out.println("center justified width = 4");
        for (int i = Pitch.C0; i < Pitch.C1; i += Pitch.MINOR_SECOND) {
            System.out.print(pf.format(a[i]) + "|");
        }
        System.out.println();

        pf.setWidth(8);
        System.out.println("center justified width = 8");
        for (int i = Pitch.C0; i < Pitch.C1; i += Pitch.MINOR_SECOND) {
            System.out.print(pf.format(a[i]) + "|");
        }
        System.out.println();

        pf.setWidth(4);
        System.out.println("left justified");
        pf.setJustification(PitchFormat.LEFT_JUSTIFY);
        for (int i = 0; i < 12; i++) {
            System.out.print(pf.format(a[i]) + "|");
        }
        System.out.println();

        System.out.println("right justified");
        pf.setJustification(PitchFormat.RIGHT_JUSTIFY);
        for (int i = 0; i < 12; i++) {
            System.out.print(pf.format(a[i]) + "|");
        }
        System.out.println();

        System.out.println("right justified as sharps");
        pf.setJustification(PitchFormat.RIGHT_JUSTIFY);
        pf.setDisplaySharps();
        for (int i = 0; i < 12; i++) {
            System.out.print(pf.format(a[i]) + "|");
        }
        System.out.println();

        /*
         * for (int i = 0; i < 12; i++) { System.out.print(
         * PitchFormat.stringToMidiNumber(a[i].toString()) + " "); }
         * System.out.println();
         */

        System.out.println("midi numz 60 to 108");
        for (int i = 60; i < 108; i++) {
            /*
             * System.out.print( Pitch.midiNumberToString(i,false,
             * Pitch.CENTER_JUSTIFY, 3) + "|");
             */
            System.out.print(PitchFormat.midiNumberToString(i) + "|");
        }
        System.out.println();
    }
}
