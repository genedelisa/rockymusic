package com.rockhoppertech.music.fx.cmn.model;

/*
 * #%L
 * Rocky Music FX
 * %%
 * Copyright (C) 1991 - 2014 Rockhopper Technologies
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


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFactory;

import static com.rockhoppertech.music.Pitch.*;

/**
 * A note drawn in a staff using the alto clef.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 *
 */
public class AltoNote {
    final static Logger logger = LoggerFactory.getLogger(AltoNote.class);

    static  Map<Integer, Integer> flatLedgersMap = new HashMap<Integer, Integer>();
    static  Map<Integer, Integer> sharpLedgersMap = new HashMap<Integer, Integer>();
    
    public static void main(String[] args) {
        AltoNote n;
        for (int i = 0; i < 121; i++)
            System.out.println("note " + PitchFactory.getPitch(i) + " ledgers "
                    + AltoNote.altoLedgersFlat[i]);
    }

    private static int pat[] = { 3, 4, 3, 4, 3 }; // sharps
//    private static int pat2[] = { 4, 3, 3, 4, 3 }; // flats
    private static int pat2[] = {2, 1, 4, 1, 2, 3};
//    private static int pat3[] = { 3, // c b bb
//            4, // a af g gf
//            3, // f e ef
//            3, 4 };
    public static final int[] altoLedgersFlat = new int[121];
    public static final int[] altoLedgersSharp = new int[121];
    static {
        initMaps();
        
        for (int i = 0; i < 121; i++)
            altoLedgersFlat[i] = altoledgers(i, false);
        for (int i = 0; i < 121; i++)
            altoLedgersSharp[i] = altoledgers(i, true);
        
        
    }

    static int altoledgers(int num, boolean sharps) {
        int ledgers = 0;

        if(flatLedgersMap == null) {
            System.err.println("map is null");
        }
        if(sharps == false) {
            ledgers = flatLedgersMap.get(num);
            return ledgers;
        } else {
            ledgers = sharpLedgersMap.get(num);
            return ledgers;
        }
        
//        if (num >= Pitch.A5)
//            ledgers = aboveAltoLedgers(num, sharps);
//        else if (num <= Pitch.E4)
//            ledgers = belowAltoLedgers(num, sharps);
//        else
//            return (0);
//        return (ledgers);
    }

    static int aboveAltoLedgers(int num, boolean sharps) {
        int j, k, n;
        // transition notes where a new line is added
        int[] trans = new int[70];
        int ledgers = 0;
        int gs = 10; // g sharp
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
         * int d = 12; int df = 11;
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
    
   

    static void initMaps () {
        for(int i = 0; i < 127 ; i++) {
            flatLedgersMap.put(i, 0);
            sharpLedgersMap.put(i, 0);
        }
        flatLedgersMap.put(BF5, 1);
        flatLedgersMap.put(B5, 1);
        
        flatLedgersMap.put(C6, 1);
        flatLedgersMap.put(DF6, 2);
        flatLedgersMap.put(D6, 2);
        flatLedgersMap.put(EF6, 2);
        flatLedgersMap.put(E6, 2);
        
        flatLedgersMap.put(F6, 3);
        flatLedgersMap.put(GF6, 3);
        flatLedgersMap.put(G6, 3);
        flatLedgersMap.put(AF6, 4);
        flatLedgersMap.put(A6, 4);
        flatLedgersMap.put(BF6, 4);
        flatLedgersMap.put(B6, 4);
        
        // not filled in for oct 7 and 8
        flatLedgersMap.put(C7, 1);
        flatLedgersMap.put(DF7, 1);
        flatLedgersMap.put(D7, 2);
        flatLedgersMap.put(EF7, 2);
        flatLedgersMap.put(E7, 2);
        flatLedgersMap.put(F7, 3);
        flatLedgersMap.put(GF7, 3);
        flatLedgersMap.put(G7, 3);
        flatLedgersMap.put(AF7, 4);
        flatLedgersMap.put(A7, 4);
        flatLedgersMap.put(BF7, 4);
        flatLedgersMap.put(B7, 4);
        
        flatLedgersMap.put(C8, 8);
        flatLedgersMap.put(DF8, 8);
        flatLedgersMap.put(D8, 2);
        flatLedgersMap.put(EF8, 2);
        flatLedgersMap.put(E8, 2);
        flatLedgersMap.put(F8, 3);
        flatLedgersMap.put(GF8, 3);
        flatLedgersMap.put(G8, 3);
        flatLedgersMap.put(AF8, 4);
        flatLedgersMap.put(A8, 4);
        flatLedgersMap.put(BF8, 4);
        flatLedgersMap.put(B8, 4);
        
        
        flatLedgersMap.put(D4, 1);
        flatLedgersMap.put(DF4, 1);
        
        flatLedgersMap.put(C4, 1);
        flatLedgersMap.put(DF4, 1);
        flatLedgersMap.put(D4, 1);
        flatLedgersMap.put(EF4, 0);

        // these are all 0
//        flatLedgersMap.put(E4, 2);
//        flatLedgersMap.put(F4, 4);
//        flatLedgersMap.put(GF4, 4);
//        flatLedgersMap.put(G4, 4);
//        flatLedgersMap.put(AF4, 4);
//        flatLedgersMap.put(A4, 4);
//        flatLedgersMap.put(BF4, 4);
//        flatLedgersMap.put(B4, 4);
        
        
        flatLedgersMap.put(C3, 5);
        flatLedgersMap.put(DF3, 4);
        flatLedgersMap.put(D3, 4);
        flatLedgersMap.put(EF3, 4);
        flatLedgersMap.put(E3, 4);
        flatLedgersMap.put(F3, 3);
        flatLedgersMap.put(GF3, 3);
        flatLedgersMap.put(G3, 3);
        flatLedgersMap.put(AF3, 2);
        flatLedgersMap.put(A3, 2);
        flatLedgersMap.put(BF3, 2);
        flatLedgersMap.put(B3, 2);
        
        // wrong after here
        
        flatLedgersMap.put(C2, 1);
        flatLedgersMap.put(DF2, 1);
        flatLedgersMap.put(D2, 2);
        flatLedgersMap.put(EF2, 2);
        flatLedgersMap.put(E2, 2);
        flatLedgersMap.put(F2, 3);
        flatLedgersMap.put(GF2, 3);
        flatLedgersMap.put(G2, 3);
        flatLedgersMap.put(AF2, 4);
        flatLedgersMap.put(A2, 4);
        flatLedgersMap.put(BF2, 4);
        flatLedgersMap.put(B2, 4);
        
        flatLedgersMap.put(C1, 1);
        flatLedgersMap.put(DF1, 1);
        flatLedgersMap.put(D1, 2);
        flatLedgersMap.put(EF1, 2);
        flatLedgersMap.put(E1, 2);
        flatLedgersMap.put(F1, 3);
        flatLedgersMap.put(GF1, 3);
        flatLedgersMap.put(G1, 3);
        flatLedgersMap.put(AF1, 4);
        flatLedgersMap.put(A1, 4);
        flatLedgersMap.put(BF1, 4);
        flatLedgersMap.put(B1, 4);
        
        flatLedgersMap.put(C0, 0);
        flatLedgersMap.put(DF0, 0);
        flatLedgersMap.put(D0, 2);
        flatLedgersMap.put(EF0, 2);
        flatLedgersMap.put(E0, 2);
        flatLedgersMap.put(F0, 3);
        flatLedgersMap.put(GF0, 3);
        flatLedgersMap.put(G0, 3);
        flatLedgersMap.put(AF0, 4);
        flatLedgersMap.put(A0, 4);
        flatLedgersMap.put(BF0, 4);
        flatLedgersMap.put(B0, 4);
        
        //
        sharpLedgersMap.put(C0, 0);
        sharpLedgersMap.put(CS0, 0);
        sharpLedgersMap.put(D0, 2);
        sharpLedgersMap.put(DS0, 2);
        sharpLedgersMap.put(E0, 2);
        sharpLedgersMap.put(F0, 3);
        sharpLedgersMap.put(FS0, 3);
        sharpLedgersMap.put(G0, 3);
        sharpLedgersMap.put(GS0, 4);
        sharpLedgersMap.put(A0, 4);
        sharpLedgersMap.put(AS0, 4);
        sharpLedgersMap.put(B0, 4);
        
        sharpLedgersMap.put(C1, 1);
        sharpLedgersMap.put(CS1, 1);
        sharpLedgersMap.put(D1, 2);
        sharpLedgersMap.put(DS1, 2);
        sharpLedgersMap.put(E1, 2);
        sharpLedgersMap.put(F1, 3);
        sharpLedgersMap.put(FS1, 3);
        sharpLedgersMap.put(G1, 3);
        sharpLedgersMap.put(GS1, 4);
        sharpLedgersMap.put(A1, 4);
        sharpLedgersMap.put(AS1, 4);
        sharpLedgersMap.put(B1, 4);
        
        sharpLedgersMap.put(C2, 2);
        sharpLedgersMap.put(CS2, 2);
        sharpLedgersMap.put(D2, 2);
        sharpLedgersMap.put(DS2, 2);
        sharpLedgersMap.put(E2, 2);
        sharpLedgersMap.put(F2, 3);
        sharpLedgersMap.put(FS2, 3);
        sharpLedgersMap.put(G2, 3);
        sharpLedgersMap.put(GS2, 4);
        sharpLedgersMap.put(A2, 4);
        sharpLedgersMap.put(AS2, 4);
        sharpLedgersMap.put(B2, 6);
        // above here wrong, fix oct 2,1, and 0
        
        sharpLedgersMap.put(C3, 5);
        sharpLedgersMap.put(CS3, 5);
        sharpLedgersMap.put(D3, 4);
        sharpLedgersMap.put(DS3, 4);
        sharpLedgersMap.put(E3, 4);
        sharpLedgersMap.put(F3, 3);
        sharpLedgersMap.put(FS3, 3);
        sharpLedgersMap.put(G3, 3);
        sharpLedgersMap.put(GS3, 3);
        sharpLedgersMap.put(A3, 2);
        sharpLedgersMap.put(AS3, 2);
        sharpLedgersMap.put(B3, 2);
        
        sharpLedgersMap.put(C4, 1);
        sharpLedgersMap.put(CS4, 1);
        sharpLedgersMap.put(D4, 1);
        sharpLedgersMap.put(DS4, 1);
        sharpLedgersMap.put(E4, 0);
        sharpLedgersMap.put(F4, 0);
        sharpLedgersMap.put(FS4, 0);
        sharpLedgersMap.put(G4, 0);
        sharpLedgersMap.put(GS4, 0);
        sharpLedgersMap.put(A4, 0);
        sharpLedgersMap.put(AS4, 0);
        sharpLedgersMap.put(B4, 0);
        
        sharpLedgersMap.put(C5, 0);
        sharpLedgersMap.put(CS5, 0);
        sharpLedgersMap.put(D5, 0);
        sharpLedgersMap.put(DS5, 0);
        sharpLedgersMap.put(E5, 0);
        sharpLedgersMap.put(F5, 0);
        sharpLedgersMap.put(FS5, 0);
        sharpLedgersMap.put(G5, 0);
        sharpLedgersMap.put(GS5, 0);
        sharpLedgersMap.put(A5, 0);
        sharpLedgersMap.put(AS5, 0);
        sharpLedgersMap.put(B5, 1);
        
        sharpLedgersMap.put(C6, 1);
        sharpLedgersMap.put(CS6, 1);
        sharpLedgersMap.put(D6, 2);
        sharpLedgersMap.put(DS6, 2);
        sharpLedgersMap.put(E6, 3);
        sharpLedgersMap.put(F6, 4);
        sharpLedgersMap.put(FS6, 3);
        sharpLedgersMap.put(G6, 3);
        sharpLedgersMap.put(GS6, 3);
        sharpLedgersMap.put(A6, 4);
        sharpLedgersMap.put(AS6, 4);
        sharpLedgersMap.put(B6, 4);
        
        sharpLedgersMap.put(C7, 5);
        sharpLedgersMap.put(CS7, 5); // fix after here
        sharpLedgersMap.put(D7, 2);
        sharpLedgersMap.put(DS7, 2);
        sharpLedgersMap.put(E7, 2);
        sharpLedgersMap.put(F7, 3);
        sharpLedgersMap.put(FS7, 3);
        sharpLedgersMap.put(G7, 3);
        sharpLedgersMap.put(GS7, 4);
        sharpLedgersMap.put(A7, 4);
        sharpLedgersMap.put(AS7, 4);
        sharpLedgersMap.put(B7, 4);
        
        sharpLedgersMap.put(C8, 8);
        sharpLedgersMap.put(CS8, 8);
        sharpLedgersMap.put(D8, 2);
        sharpLedgersMap.put(DS8, 2);
        sharpLedgersMap.put(E8, 2);
        sharpLedgersMap.put(F8, 3);
        sharpLedgersMap.put(FS8, 3);
        sharpLedgersMap.put(G8, 3);
        sharpLedgersMap.put(GS8, 4);
        sharpLedgersMap.put(A8, 4);
        sharpLedgersMap.put(AS8, 4);
        sharpLedgersMap.put(B8, 4);
        
    }
    
}
