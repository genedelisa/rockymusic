package com.rockhoppertech.music.fx.musicfont.model;

/*
 * #%L
 * Rocky Music FX
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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 *
 */
public class BassNote {
    final static Logger logger = LoggerFactory.getLogger(BassNote.class);

    public static void main(String[] args) {
        for (int i = 0; i < 128; i++)
            System.out.println("note " + i + " ledgers "
                    + BassNote.bassLedgersFlat[i]);
    }

    private static int pat[] = { 3, 4, 3, 4, 3 }; // sharps
    private static int pat2[] = { 4, 3, 3, 4, 3 }; // flats

//    private static int pat3[] = { 3, // c b bb
//            4, // a af g gf
//            3, // f e ef
//            3, 4 };
    public static final int[] bassLedgersFlat = new int[128];
    public static final int[] bassLedgersSharp = new int[128];
    static {
        for (int i = 0; i < 128; i++)
            bassLedgersFlat[i] = bassledgers(i, false);
        for (int i = 0; i < 128; i++)
            bassLedgersSharp[i] = bassledgers(i, true);
    }

    static int bassledgers(int num, boolean sharps) {
        int ledgers;

        if (num >= Pitch.B4)
            ledgers = aboveBassLedgers(num, sharps);
        else if (num <= Pitch.E3)
            ledgers = belowBassLedgers(num, sharps);
        else
            return (0);
        return (ledgers);
    }

    static int aboveBassLedgers(int num, boolean sharps) {
        int j, k, n;
        // transition notes where a new line is added
        int[] trans = new int[70];
        int ledgers = 0;
        int gs = 80; // g sharp
        int g = 79; // g natural

        if (sharps) {
            trans[0] = gs;
            for (k = 1, j = gs; j < 127; j++, k++) {
                trans[k] = trans[k - 1] + pat[(k - 1) % 5];
            }
            for (j = gs; j < num; j++) {
                for (n = 0; n < k; n++)
                    if (j == trans[n]) {
                        ledgers++;
                    }
            }
        } else {
            trans[0] = g;
            for (k = 1, j = g; j < 127; j++, k++) {
                trans[k] = trans[k - 1] + pat2[(k - 1) % 5];
            }
            for (j = g; j < num; j++) {
                for (n = 0; n < k; n++)
                    if (j == trans[n]) {
                        ledgers++;
                    }
            }
        }
        return (ledgers);
    }

    static int belowBassLedgers(int num, boolean sharps) {
        int j, k, n;
        int[] trans = new int[70];
        int ledgers = 0;
        /*
         * int d = 62; int df = 61;
         */

        // int d = 41; // f
        // int df = 40; //e
        int d = Pitch.F3; // f
       // int df = Pitch.E3; // e

        if (sharps) {
            trans[0] = d;
            for (k = 1, j = d; j > num; j--, k++) {
                trans[k] = trans[k - 1] - pat[(k - 1) % 5];
            }

            for (j = d; j > num; j--) {
                for (n = 0; n < k; n++)
                    if (j == trans[n]) {
                        ledgers++;
                    }
            }
        } else {
            // the pattern is the number of ledgers in a row
            // e.g. e ef d df all have 1 ledger
            int[] pattern = { 4, // e ef d df
                    3, // c b bb
                    4, // a af g gf
                    3, // f
                    3 }; // flats
            trans[0] = Pitch.F3;

            // for(k=1, j=df; j> num; j--,k++) {
            // for (k = 1, j = Pitch.F3; j > num; j--, k++) {
            // trans[k] = trans[k - 1] - pattern[(k - 1) % 5];
            // }
            //
            // // 41=f
            // for (j = 41; j > num; j--) {
            // for (n = 0; n < k; n++)
            // if (j == trans[n]) {
            // ledgers++;
            // }
            // }

            // FIXME DF still gets 2 instead of 1.

            trans[0] = Pitch.F3;
            for (k = 1, j = Pitch.F3; j > num; j--, k++) {
                trans[k] = trans[k - 1] - pattern[(k - 1) % 5];
            }

            for (j = Pitch.F3; j > num; j--) {
                for (n = 0; n < k; n++)
                    if (j == trans[n]) {
                        ledgers++;
                    }
            }
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("ledgers = %d", ledgers));
            }

        }
        return (ledgers);
    }
}
