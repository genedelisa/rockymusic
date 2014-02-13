package com.rockhoppertech.music.fx.cmn.model;

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
import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.PitchFormat;

/**
 * A note drawn in a staff using the alto clef.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 *
 */
public class AltoNote {
    final static Logger logger = LoggerFactory.getLogger(AltoNote.class);

    public static void main(String[] args) {
        for (int i = 0; i < 128; i++)
            System.out.println("note " + PitchFactory.getPitch(i) + " ledgers "
                    + AltoNote.altoLedgersFlat[i]);
    }

    private static int pat[] = { 3, 4, 3, 4, 3 }; // sharps
    private static int pat2[] = { 4, 3, 3, 4, 3 }; // flats

//    private static int pat3[] = { 3, // c b bb
//            4, // a af g gf
//            3, // f e ef
//            3, 4 };
    public static final int[] altoLedgersFlat = new int[128];
    public static final int[] altoLedgersSharp = new int[128];
    static {
        for (int i = 0; i < 128; i++)
            altoLedgersFlat[i] = altoledgers(i, false);
        for (int i = 0; i < 128; i++)
            altoLedgersSharp[i] = altoledgers(i, true);
    }

    static int altoledgers(int num, boolean sharps) {
        int ledgers;

        if (num >= Pitch.A5)
            ledgers = aboveAltoLedgers(num, sharps);
        else if (num <= Pitch.E4)
            ledgers = belowAltoLedgers(num, sharps);
        else
            return (0);
        return (ledgers);
    }

    static int aboveAltoLedgers(int num, boolean sharps) {
        int j, k, n;
        // transition notes where a new line is added
        int[] trans = new int[70];
        int ledgers = 0;
        int gs = 80; // g sharp
        int g = 79; // g natural
        
        gs = Pitch.A5; // actually a5
        g = Pitch.AF5; // actually 

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

    static int belowAltoLedgers(int num, boolean sharps) {
        int j, k, n;
        int[] trans = new int[70];
        int ledgers = 0;
        /*
         * int d = 62; int df = 61;
         */

        // int d = 41; // f
        // int df = 40; //e
        int d = Pitch.E4; // lowest without a ledger
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
            // e.g. e ef d df all have 1 ledger in the bass clef
//            int[] pattern = { 4, // e ef d df
//                    3, // c b bb
//                    4, // a af g gf
//                    3, // f
//                    3 }; // flats
            
            int[] pattern = { 4, // e ef d df
                    3, // c b bb
                    4, // a af g gf
                    3, // f
                    3 }; // flats
            trans[0] = Pitch.EF4;

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


            trans[0] = Pitch.EF4;
            for (k = 1, j = Pitch.EF4; j > num; j--, k++) {
                trans[k] = trans[k - 1] - pattern[(k - 1) % 5];
            }

            for (j = Pitch.EF4; j > num; j--) {
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
