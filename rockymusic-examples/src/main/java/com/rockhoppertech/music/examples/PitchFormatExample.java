package com.rockhoppertech.music.examples;

/*
 * #%L
 * Rocky Music Examples
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
